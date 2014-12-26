package uk.dan_gilbert.paytouch.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.dan_gilbert.paytouch.PayTouchApp;
import uk.dan_gilbert.paytouch.R;
import uk.dan_gilbert.paytouch.data.ActorController;
import uk.dan_gilbert.paytouch.data.adapter.ActorListAdapter;
import uk.dan_gilbert.paytouch.data.model.Actor;

/**
 * A list fragment representing a list of Actors. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link uk.dan_gilbert.paytouch.ui.fragment.ActorDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ActorListFragment extends ListFragment {

    @Inject
    ActorController actorController;

    @InjectView(android.R.id.list) ListView listView;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(int id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActorListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PayTouchApp.get(getActivity()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actor_list, container, false);

        ButterKnife.inject(this, v);
        listView.setOnScrollListener(new EndlessScrollListener());

        listView.addFooterView(inflater.inflate(R.layout.loading_view, container, false));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadActors(1);

    }

    public void loadActorsOrderedByName() {

        ActorListAdapter adapter = (ActorListAdapter) getListAdapter();

        adapter.clear();

        actorController.getActorsSortedByName()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(actorResultAction);
    }

    private Action1<LinkedHashMap<Integer, Actor>> actorResultAction = (Action1<LinkedHashMap<Integer, Actor>>) actors -> {
        ActorListAdapter adapter = (ActorListAdapter) getListAdapter();
        if (getListAdapter() == null) {
            adapter = new ActorListAdapter(getActivity(), null);
            setListAdapter(adapter);
        }

        adapter.setActors(actors);
        adapter.notifyDataSetChanged();
    };


    public void loadActorsOrderedByPopularity() {

        ActorListAdapter adapter = (ActorListAdapter) getListAdapter();
        adapter.clear();

        actorController.getActorsSortedByPopularity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(actorResultAction);
    }

    private void loadActors(int pageNumber) {

        actorController.getActors(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(actorResultAction,
                throwable -> {
                    if (throwable instanceof RetrofitError) {
                        RetrofitError err = (RetrofitError)throwable;
                        String message = null;

                        if (err.getKind() == RetrofitError.Kind.NETWORK) {
                            message = "An error occurred communicating with the server. Please check your network connection and try again";
                        } else {

                            switch (err.getResponse().getStatus()) {
                                case 400:
                                    // Invalid request. page out of range?
                                    message = "Invalid page number provided";
                                    break;
                                case 401:
                                    // Unauthorised
                                    message = "The request was unauthorised";
                                    break;
                                case 403:
                                    // Forbidden
                                    message = "You are not allowed to access that resource";
                                    break;
                                case 404:
                                    // Not found
                                    message = "Page not found";
                                    break;
                                default:
                                    message = err.getMessage();
                                    break;
                            }
                        }

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Error")
                                .setMessage(message)
                                .setNegativeButton("OK", null)
                                .create()
                                .show();

                        Timber.d(err.getMessage());

                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Error")
                                .setMessage(throwable.getLocalizedMessage())
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }
                });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        Timber.d("Opening item: " + id);
        mCallbacks.onItemSelected((int)id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 5;
        private int currentPage = 1;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                Timber.d("Loading next page");
                loadActors(currentPage);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
}
