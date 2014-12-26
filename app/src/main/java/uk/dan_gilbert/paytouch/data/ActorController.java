package uk.dan_gilbert.paytouch.data;

import java.util.LinkedHashMap;
import java.util.List;

import rx.Observable;
import se.emilsjolander.sprinkles.Query;
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

        return Query.one(Actor.class, "SELECT * FROM Actors WHERE id = ?", id).get();
    }

    public Observable<LinkedHashMap<Integer, Actor>> getActors(int pageNumber) {
        return Observable.create(subscriber -> {
            try {
                ActorsResponse response = actorService.getActors(pageNumber);;
                for (Actor a : response.actors) {
                    actors.put(a.identifier, a);
                    a.save();
                }
                subscriber.onNext(actors);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

    }

    public Observable<LinkedHashMap<Integer, Actor>> getActorsSortedByName() {
        return Observable.create(subscriber -> {
           List<Actor> actorList = Query.many(Actor.class, "SELECT * FROM actors ORDER BY name ASC")
                   .get().asList();
            LinkedHashMap<Integer, Actor> orderedActors = new LinkedHashMap<>();
            for (Actor a : actorList) {
                orderedActors.put(a.identifier, a);
            }
            subscriber.onNext(orderedActors);
            subscriber.onCompleted();
        });
    }

    public Observable<LinkedHashMap<Integer, Actor>> getActorsSortedByPopularity() {
        return Observable.create(subscriber -> {
            List<Actor> actorList = Query.many(Actor.class, "SELECT * FROM actors ORDER BY popularity DESC")
                    .get().asList();
            LinkedHashMap<Integer, Actor> orderedActors = new LinkedHashMap<>();
            for (Actor a : actorList) {
                orderedActors.put(a.identifier, a);
            }
            subscriber.onNext(orderedActors);
            subscriber.onCompleted();
        });
    }

    public Observable<LinkedHashMap<Integer, Actor>> getActorsWithFilter(Filter filter) {
        return Observable.create(subscriber -> {
            List<Actor> actorList = Query.many(Actor.class, "SELECT * FROM actors WHERE name LIKE ? AND location LIKE ? AND popularity > ? AND popularity < ? AND top = ?", '%'+filter.name+'%', '%'+filter.location+'%', filter.ratingLow, filter.ratingHigh, filter.isTop)
                    .get().asList();
            LinkedHashMap<Integer, Actor> orderedActors = new LinkedHashMap<>();
            for (Actor a : actorList) {
                orderedActors.put(a.identifier, a);
            }
            subscriber.onNext(orderedActors);
            subscriber.onCompleted();
        });
    }
}
