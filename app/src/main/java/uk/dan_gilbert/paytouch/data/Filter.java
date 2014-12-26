package uk.dan_gilbert.paytouch.data;

/**
 * Created by dangilbert on 26/12/14.
 */
public class Filter {

    public String name;
    public String location;
    public boolean isTop;
    public int ratingLow;
    public int ratingHigh;

    public Filter() {
        clearFilter();
    }

    public void clearFilter() {
        name = "";
        location = "";
        isTop = false;
        ratingLow = 0;
        ratingHigh = 5;
    }

}
