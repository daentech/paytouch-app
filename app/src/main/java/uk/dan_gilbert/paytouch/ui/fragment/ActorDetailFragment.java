package uk.dan_gilbert.paytouch.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import uk.dan_gilbert.paytouch.PayTouchApp;
import uk.dan_gilbert.paytouch.R;
import uk.dan_gilbert.paytouch.data.ActorController;
import uk.dan_gilbert.paytouch.data.adapter.FilmAdapter;
import uk.dan_gilbert.paytouch.data.model.Actor;
import uk.dan_gilbert.paytouch.ui.TypographyFacade;
import uk.dan_gilbert.paytouch.ui.transformation.CircleTransform;

/**
 * A fragment representing a single Actor detail screen.
 * This fragment is either contained in a {@link uk.dan_gilbert.paytouch.ui.activity.ActorListActivity}
 * in two-pane mode (on tablets) or a {@link uk.dan_gilbert.paytouch.ui.activity.ActorDetailActivity}
 * on handsets.
 */
public class ActorDetailFragment extends ListFragment {

    @Inject ActorController actorController;

    @InjectView(android.R.id.list) ListView listView;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actor_detail, container, false);

        ButterKnife.inject(this, rootView);

        // Setup the header view
        listView.addHeaderView(initHeaderView(inflater));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        setListAdapter(new FilmAdapter(actor.knownFor));
    }

    private View initHeaderView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.actor_detail_header, listView, false);

        HeaderViewHolder holder = new HeaderViewHolder(view);
        view.setTag(holder);

        holder.bindView(actor);

        return view;
    }

    static class HeaderViewHolder {

        Context ctx;

        @InjectView(R.id.actor_name)
        TextView actorName;
        @InjectView(R.id.actor_location) TextView actorLocation;
        @InjectView(R.id.actor_description) TextView actorDescription;
        @InjectView(R.id.actor_profile_image)
        ImageView actorProfileImage;


        @InjectViews( { R.id.actor_name, R.id.actor_location, R.id.actor_description, R.id.filmography_title } ) List<TextView> textViewList;

        public HeaderViewHolder(View v) {
            ButterKnife.inject(this, v);
            ctx = v.getContext();
            ButterKnife.apply(textViewList, TypographyFacade.typographyAction);
        }

        public void bindView(Actor actor) {
            actorName.setText(Html.fromHtml(actor.displayName()));
            actorLocation.setText(actor.location);
            actorDescription.setText(actor.description);

            Picasso.with(ctx).load(actor.profilePath)
                    .placeholder(R.drawable.profile_placeholder)
                    .transform(new CircleTransform(ctx))
                    .into(actorProfileImage);
        }
    }
}
