package uk.dan_gilbert.paytouch;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.dan_gilbert.paytouch.api.APIModule;
import uk.dan_gilbert.paytouch.api.ActorService;
import uk.dan_gilbert.paytouch.data.ActorController;
import uk.dan_gilbert.paytouch.data.Filter;
import uk.dan_gilbert.paytouch.ui.fragment.ActorListFragment;

/**
 * Created by dangilbert on 26/12/14.
 */
@Module (
        includes = {
                APIModule.class,
        },
        injects = {
                PayTouchApp.class,
                ActorListFragment.class,
        },
        library = true
)
public class PayTouchModule {

    private Application app;

    public PayTouchModule(Application app) { this.app = app; }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    ActorController providesActorController(ActorService actorService) {
        return new ActorController(actorService);
    }

    @Provides
    @Singleton
    Filter providesFilter() {
        return new Filter();
    }
}
