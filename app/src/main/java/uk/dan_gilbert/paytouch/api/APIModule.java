package uk.dan_gilbert.paytouch.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import uk.dan_gilbert.paytouch.BuildConfig;

/**
 * Created by dangilbert on 26/12/14.
 */
@Module(
        library = true
)
public class APIModule {

    @Provides
    @Singleton
    RestAdapter providesDefaultRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(BuildConfig.ENDPOINT)
                .setConverter(new GsonConverter(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()))
                .build();
    }

    @Provides
    @Singleton
    ActorService providesActorService(RestAdapter adapter) {
        return adapter.create(ActorService.class);
    }

}
