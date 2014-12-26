package uk.dan_gilbert.paytouch.api;

import retrofit.http.GET;
import retrofit.http.Query;
import uk.dan_gilbert.paytouch.api.response.ActorsResponse;

/**
 * Created by dangilbert on 26/12/14.
 */
public interface ActorService {

    @GET("/rest/actors")
    public ActorsResponse getActors(@Query("page") int page);

}
