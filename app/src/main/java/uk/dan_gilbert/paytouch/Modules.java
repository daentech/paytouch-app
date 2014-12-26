package uk.dan_gilbert.paytouch;

import android.app.Application;

/**
 * Created by dangilbert on 26/12/14.
 */
public class Modules {
    public static Object[] list(Application app) {
        return new Object[]{
                new PayTouchModule(app)
        };
    }
}
