package uk.dan_gilbert.paytouch;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Created by dangilbert on 26/12/14.
 */
public class PayTouchApp extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        buildObjectGraphAndInject();
    }

    public void inject(Object o) { objectGraph.inject(o); }

    public static PayTouchApp get(Context context){
        return (PayTouchApp) context.getApplicationContext();
    };

    private void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);
    }

}
