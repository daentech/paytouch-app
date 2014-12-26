package uk.dan_gilbert.paytouch.data.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dangilbert on 26/12/14.
 */
public class Actor {

    public String name;
    public boolean adult;
    public int identifier;
    public Film[] knownFor;
    public float popularity;
    public String profilePath;
    public String location;
    public String description;
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
}
