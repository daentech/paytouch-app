package uk.dan_gilbert.paytouch.data;

import java.util.LinkedHashMap;

import rx.Observable;
import uk.dan_gilbert.paytouch.api.ActorService;
import uk.dan_gilbert.paytouch.api.response.ActorsResponse;
import uk.dan_gilbert.paytouch.data.model.Actor;

/**
 * Created by dangilbert on 26/12/14.
 */
public class ActorController {

    private ActorService actorService;

    private LinkedHashMap<Integer, Actor> actors = new LinkedHashMap<>();

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    public Actor getActor(int id) {
        if (actors == null) {
            return null;
        }

        return actors.get(id);
    }

    public Observable<LinkedHashMap<Integer, Actor>> getActors(int pageNumber) {
        // Use the current filters
        return Observable.create(subscriber -> {
            try {
                ActorsResponse response = actorService.getActors(pageNumber);;
                for (Actor a : response.actors) {
                    actors.put(a.identifier, a);
                }
                subscriber.onNext(actors);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

    }
}
