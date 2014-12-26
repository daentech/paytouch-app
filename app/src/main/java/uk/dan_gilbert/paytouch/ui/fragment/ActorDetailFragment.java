package uk.dan_gilbert.paytouch.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import uk.dan_gilbert.paytouch.PayTouchApp;
import uk.dan_gilbert.paytouch.R;
import uk.dan_gilbert.paytouch.data.ActorController;
import uk.dan_gilbert.paytouch.data.model.Actor;

/**
 * A fragment representing a single Actor detail screen.
 * This fragment is either contained in a {@link uk.dan_gilbert.paytouch.ui.activity.ActorListActivity}
 * in two-pane mode (on tablets) or a {@link uk.dan_gilbert.paytouch.ui.activity.ActorDetailActivity}
 * on handsets.
 */
public class ActorDetailFragment extends Fragment {

    @Inject ActorController actorController;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Actor actor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActorDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PayTouchApp.get(getActivity()).inject(this);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            actor = actorController.getActor(getArguments().getInt(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actor_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (actor != null) {
            ((TextView) rootView.findViewById(R.id.actor_detail)).setText(actor.name);
        }

        return rootView;
    }
}
