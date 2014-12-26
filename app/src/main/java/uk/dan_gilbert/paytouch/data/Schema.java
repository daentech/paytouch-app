package uk.dan_gilbert.paytouch.data;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.ArrayList;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;
import timber.log.Timber;
import uk.dan_gilbert.paytouch.data.migration.Initialise;
import uk.dan_gilbert.paytouch.data.model.Actor;
import uk.dan_gilbert.paytouch.data.model.Film;

/**
 * Created by dangilbert on 26/12/14.
 */
public class Schema {

    private static ArrayList<Class<? extends Migration>> migrations = new ArrayList<Class<? extends Migration>>(){{
        add(Initialise.class);
    }};

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void addMigrations(Application app, Sprinkles sprinkles) {
        for (Class<? extends Migration> migration : migrations) {
            try {
                sprinkles.addMigration(migration.newInstance());
            } catch (InstantiationException e) {
                // This error shouldn't happen before KitKat?
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
                Timber.e("Failed to convert migrations to objects: " + e.getMessage());
            } catch (IllegalAccessException e) {
                Timber.e("Failed to convert migrations to objects: " + e.getMessage());
            }
        }

        app.getSharedPreferences(DB_PREFS, Context.MODE_PRIVATE).edit().putInt(DB_VERSION, migrations.size());
    }

    private static final String DB_PREFS = "db_prefs";
    private static final String DB_VERSION = "db_version";

    public static boolean isOutOfDate(Application app) {
        SharedPreferences sp = app.getSharedPreferences(DB_PREFS, Context.MODE_PRIVATE);
        int dbVersion = sp.getInt(DB_VERSION, 0);
        return dbVersion < migrations.size();
    }

    public static void registerTypes(Sprinkles sprinkles) {
        sprinkles.registerType(Film[].class, new Actor.FilmArraySerializer());
    }

}
