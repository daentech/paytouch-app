package uk.dan_gilbert.paytouch.data.migration;

import android.database.sqlite.SQLiteDatabase;

import se.emilsjolander.sprinkles.Migration;

/**
 * Created by dangilbert on 26/12/14.
 */
public class Initialise extends Migration {
    @Override
    protected void doMigration(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Actors (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name TEXT,"+
                "adult INTEGER,"+
                "popularity REAL,"+
                "top INTEGER,"+
                "profilePath TEXT,"+
                "location TEXT,"+
                "description TEXT,"+
                "knownFor TEXT"+
                ")");
    }
}
