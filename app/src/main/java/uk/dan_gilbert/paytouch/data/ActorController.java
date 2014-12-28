package uk.dan_gilbert.paytouch.data;

import java.util.ArrayList;
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

    public Observable<LinkedHashMap<Integer, Actor>> getActorsWithFilter(Filter filter) {
        return Observable.create(subscriber -> {
            String query = "SELECT * FROM actors WHERE name LIKE ? AND location LIKE ? AND popularity > ? AND popularity < ?";

            ArrayList<String> args = new ArrayList<>();

            args.add('%'+filter.name+'%');
            args.add('%'+filter.location+'%');
            args.add(String.valueOf(filter.ratingLow));
            args.add(String.valueOf(filter.ratingHigh));

            if (filter.isTop != Filter.IS_TOP.ALL) {
                query = query + "  AND top = ?";
                args.add(String.valueOf(filter.isTop.ordinal()));
            }

            if (filter.orderBy == Filter.ORDERBY.NAME) {
                query = query + " ORDER BY name ASC";
            } else if (filter.orderBy == Filter.ORDERBY.POPULARITY) {
                query = query + " ORDER BY popularity DESC";
            }

            List<Actor> actorList = Query.many(Actor.class, query, args.toArray(new String[args.size()]))
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
