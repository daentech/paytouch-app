package uk.dan_gilbert.paytouch.data;

/**
 * Created by dangilbert on 26/12/14.
 */
public class Filter {

    public String name;
    public String location;
    public IS_TOP isTop;
    public int ratingLow;
    public int ratingHigh;
    public ORDERBY orderBy;


    public enum IS_TOP {
        NO,
        YES,
        ALL,
    }
    public enum ORDERBY {
        NAME,
        POPULARITY,
        DEFAULT,
    }

    public Filter() {
        clearFilter();
    }

    public void clearFilter() {
        name = "";
        location = "";
        isTop = IS_TOP.ALL;
        ratingLow = 0;
        ratingHigh = 100;
        orderBy = ORDERBY.DEFAULT;
    }

}
