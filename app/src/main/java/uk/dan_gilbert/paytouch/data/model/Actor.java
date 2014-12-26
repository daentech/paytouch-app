package uk.dan_gilbert.paytouch.data.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.typeserializers.SqlType;
import se.emilsjolander.sprinkles.typeserializers.TypeSerializer;

/**
 * Created by dangilbert on 26/12/14.
 */
@Table("actors")
public class Actor extends Model {


    @Key
    @Column("id")
    public int identifier;

    @Column("name")
    public String name;

    @Column("adult")
    public boolean adult;

    @Column("knownFor")
    public Film[] knownFor;

    @Column("popularity")
    public float popularity;

    @Column("profilePath")
    public String profilePath;

    @Column("location")
    public String location;

    @Column("description")
    public String description;

    @Column("top")
    public boolean top;

    private String displayName;

    public String displayName() {

        if (displayName != null) {
            return displayName;
        }

        ArrayList<String> parts = new ArrayList<>(Arrays.asList(name.split(" ")));
        StringBuilder sb = new StringBuilder();

        sb.append("<b>");
        sb.append(parts.remove(0));
        sb.append("</b> ");
        sb.append(TextUtils.join(" ", parts));

        displayName = sb.toString();

        return displayName;
    }

    public static class FilmArraySerializer implements TypeSerializer<Film[]> {

        @Override
        public Film[] unpack(Cursor cursor, String s) {
            return new Gson().fromJson(cursor.getString(cursor.getColumnIndex(s)), Film[].class);
        }

        @Override
        public void pack(Film[] films, ContentValues contentValues, String s) {
            contentValues.put(s, new Gson().toJson(films));
        }

        @Override
        public String toSql(Film[] films) {
            return new Gson().toJson(films);
        }

        @Override
        public SqlType getSqlType() {
            return SqlType.TEXT;
        }
    }
}
