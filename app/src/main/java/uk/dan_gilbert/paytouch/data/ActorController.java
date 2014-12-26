package uk.dan_gilbert.paytouch.data;

import java.util.List;

import rx.Observable;
import uk.dan_gilbert.paytouch.api.ActorService;
import uk.dan_gilbert.paytouch.api.response.ActorsResponse;
import uk.dan_gilbert.paytouch.data.model.Actor;

/**
 * Created by dangilbert on 26/12/14.
 */
public class ActorController {

    private ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    public Actor getActor(long id) {
        return null;
    }

    public Observable<List<Actor>> getActors(int pageNumber) {
        // Use the current filters
        return Observable.create(subscriber -> {
            try {
                ActorsResponse response = actorService.getActors(pageNumber);;
                subscriber.onNext(response.actors);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

    }
}
