package uk.dan_gilbert.paytouch.data.model;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by dangilbert on 26/12/14.
 */
public class Film implements Comparable<Film> {
    public boolean adult;
    public String backdropPath;
    public long id;
    public String originalTitle;
    public Date releaseDate;
    public String posterPath;
    public float popularity;
    public String title;
    public boolean video;
    public float voteAverage;
    public long voteCount;
    public String mediaType;

    @Override
    public int compareTo(@NonNull Film another) {
        return Float.valueOf(another.voteAverage).compareTo(this.voteAverage);
    }
}
