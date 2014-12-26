package uk.dan_gilbert.paytouch.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import uk.dan_gilbert.paytouch.data.model.Actor;

/**
 * Created by dangilbert on 26/12/14.
 */
public class ActorsResponse {

    @SerializedName("data")
    public List<Actor> actors;

}
