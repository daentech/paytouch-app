package uk.dan_gilbert.paytouch.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import timber.log.Timber;
import uk.dan_gilbert.paytouch.PayTouchApp;
import uk.dan_gilbert.paytouch.R;
import uk.dan_gilbert.paytouch.data.Filter;
import uk.dan_gilbert.paytouch.ui.TypographyFacade;
import uk.dan_gilbert.paytouch.ui.fragment.ActorDetailFragment;
import uk.dan_gilbert.paytouch.ui.fragment.ActorListFragment;


/**
 * An activity representing a list of Actors. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link uk.dan_gilbert.paytouch.ui.activity.ActorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link uk.dan_gilbert.paytouch.ui.fragment.ActorListFragment} and the item details
 * (if present) is a {@link uk.dan_gilbert.paytouch.ui.fragment.ActorDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link uk.dan_gilbert.paytouch.ui.fragment.ActorListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ActorListActivity extends ActionBarActivity
        implements ActorListFragment.Callbacks {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.button_sort) ImageButton sortButton;
    @InjectView(R.id.button_filter) ImageButton filterButton;
    @InjectView(R.id.order_by_container) View orderByContainer;
    @InjectView(R.id.order_by_name) Button orderByNameButton;
    @InjectView(R.id.order_by_popularity) Button orderByPopularityButton;

    @InjectView(R.id.drawer_shade) View drawerShade;
    @InjectView(R.id.drawer_container) View drawerContainer;

    @InjectView(R.id.popularity_range_bar) RangeBar rangeBar;

    @InjectView(R.id.rangeBarLow) TextView rangeBarLow;
    @InjectView(R.id.rangeBarHigh) TextView rangeBarHigh;

    @InjectView(R.id.name_search) EditText nameSearch;
    @InjectView(R.id.location_search_spinner) Spinner locationSearchSpinner;
    @InjectView(R.id.radiogroup_is_top) RadioGroup radioGroup;

    @InjectView(R.id.search_title) TextView searchTitle;

    @InjectViews( {
            R.id.order_by_name,
            R.id.order_by_popularity,
            R.id.search_title,
            R.id.search_button,
            R.id.name_search,
            R.id.name_title,
            R.id.location_title,
            R.id.is_top_title,
            R.id.radio_yes_button,
            R.id.radio_no_button,
            R.id.radio_all_button,
            R.id.popularity_title,
            R.id.rangeBarHigh,
            R.id.rangeBarLow,
    } )List<TextView> textViewList;

    @Inject Filter filter;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_list);

        PayTouchApp.get(this).inject(this);

        ButterKnife.inject(this);
        ButterKnife.apply(textViewList, TypographyFacade.typographyAction);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        orderByNameButton.setText(Html.fromHtml((String) orderByNameButton.getText()));
        orderByPopularityButton.setText(Html.fromHtml((String) orderByPopularityButton.getText()));

        rangeBar.setOnRangeBarChangeListener((rangeBar1, i, i2) -> {
            rangeBarLow.setText(String.valueOf(i));
            rangeBarHigh.setText(String.valueOf(i2));
        });

        searchTitle.requestFocus();

        setupLocationSearchSpinner();


        if (findViewById(R.id.actor_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ActorListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.actor_list))
                    .setActivateOnItemClick(true);
        }
    }

    private void setupLocationSearchSpinner() {
        String[] spinnerArray = new String[] {
                "select location", "Barcelona", "London", "Paris", "New York", "Dallas", "Dublin", "Helsinki", "Berlin", "San Francisco", "Sydney", "Vancouver", "Rome", "Chicago", "Boston", "Los Angeles"
        };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.location_spinner_item, android.R.id.text1, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSearchSpinner.setAdapter(spinnerArrayAdapter);
    }

    /**
     * Callback method from {@link ActorListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ActorDetailFragment.ARG_ITEM_ID, id);
            ActorDetailFragment fragment = new ActorDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.actor_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ActorDetailActivity.class);
            detailIntent.putExtra(ActorDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @OnClick(R.id.button_sort)
    public void sortButtonPressed(ImageButton button) {
        Timber.d("Sort button pressed");
        button.setSelected(!button.isSelected());
        orderByContainer.setVisibility(button.isSelected() ? View.VISIBLE : View.GONE);
        int right = button.getRight();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) orderByContainer.getLayoutParams();
        lp.setMargins(right - (int)(184 * getResources().getDisplayMetrics().density), 0, 0, 0);
        orderByContainer.setLayoutParams(lp);
    }

    @OnClick(R.id.order_by_name)
    public void orderByNamePressed() {
        ActorListFragment fragment = (ActorListFragment) getSupportFragmentManager().findFragmentById(R.id.actor_list);

        sortButton.setSelected(false);
        orderByContainer.setVisibility(View.GONE);

        filter.orderBy = Filter.ORDERBY.NAME;

        fragment.loadActorsByFilter();
    }

    @OnClick(R.id.order_by_popularity)
    public void orderByPopularityPressed() {
        ActorListFragment fragment = (ActorListFragment) getSupportFragmentManager().findFragmentById(R.id.actor_list);

        sortButton.setSelected(false);
        orderByContainer.setVisibility(View.GONE);

        filter.orderBy = Filter.ORDERBY.POPULARITY;

        fragment.loadActorsByFilter();
    }

    @OnClick(R.id.button_filter)
    public void filterButtonPressed(ImageButton button) {
        sortButton.setSelected(false);
        orderByContainer.setVisibility(View.GONE);

        toggleFilterDrawer();
    }

    private void setFilterDetails() {
        nameSearch.setText(filter.name);

        if (filter.location == "") {
            locationSearchSpinner.setSelection(0);
        } else {
            for (int i = 1; i < locationSearchSpinner.getAdapter().getCount(); i++) {
                if (filter.location.equals(locationSearchSpinner.getItemAtPosition(i))) {
                    locationSearchSpinner.setSelection(i);
                    return;
                }
            }
        }

        int radioId = R.id.radio_no_button;
        switch (filter.isTop) {
            case NO:
                radioId = R.id.radio_no_button;
                break;
            case YES:
                radioId = R.id.radio_yes_button;
                break;
            case ALL:
                radioId = R.id.radio_all_button;
                break;
        }
        radioGroup.check(radioId);
        rangeBar.setThumbIndices(filter.ratingLow, filter.ratingHigh);
    }

    @OnClick(R.id.search_button)
    public void searchButtonPressed() {
        filter.name = nameSearch.getText().toString();
        filter.location = locationSearchSpinner.getSelectedItemPosition() == 0 ? "" : (String) locationSearchSpinner.getSelectedItem();

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_no_button:
                filter.isTop = Filter.IS_TOP.NO;
                break;
            case R.id.radio_yes_button:
                filter.isTop = Filter.IS_TOP.YES;
                break;
            case R.id.radio_all_button:
                filter.isTop = Filter.IS_TOP.ALL;
                break;
        }

        filter.ratingLow = rangeBar.getLeftIndex();
        filter.ratingHigh = rangeBar.getRightIndex();

        toggleFilterDrawer();

        ActorListFragment fragment = (ActorListFragment) getSupportFragmentManager().findFragmentById(R.id.actor_list);

        fragment.loadActorsByFilter();
    }

    @OnClick(R.id.reset_button)
    public void resetButtonPressed() {
        filter.clearFilter();
        setFilterDetails();
        toggleFilterDrawer();

        ActorListFragment fragment = (ActorListFragment) getSupportFragmentManager().findFragmentById(R.id.actor_list);

        fragment.loadActors(1);
    }

    @OnClick(R.id.close_button)
    public void closeButtonPressed(ImageButton button) {
        toggleFilterDrawer();
    }

    ValueAnimator drawerAnimator;

    public void toggleFilterDrawer() {

        if (drawerAnimator != null) {
            drawerAnimator.cancel();
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) drawerContainer.getLayoutParams();


        if (drawerShade.getVisibility() == View.VISIBLE) {
            drawerAnimator = ValueAnimator.ofFloat(drawerShade.getAlpha(), 0.0f);

            int targetRightMargin = -drawerContainer.getWidth();
            drawerAnimator.addUpdateListener(animation -> {
                drawerShade.setAlpha((Float) animation.getAnimatedValue());
                lp.rightMargin = (int)(-(1.0f - (float)animation.getAnimatedValue()) * lp.width);
                drawerContainer.setLayoutParams(lp);
            });
        } else {
            setFilterDetails();
            drawerShade.setAlpha(0);
            drawerShade.setVisibility(View.VISIBLE);
            int rightMargin = lp.rightMargin;
            drawerAnimator = ValueAnimator.ofFloat(0, 1.0f);
            drawerAnimator.addUpdateListener(animation -> {
                drawerShade.setAlpha((Float) animation.getAnimatedValue());
                lp.rightMargin = (int)(rightMargin * (1 - (float)animation.getAnimatedValue()));
                drawerContainer.setLayoutParams(lp);
            });
        }

        drawerAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                drawerAnimator = null;
                if (drawerShade.getAlpha() == 0) {
                    drawerShade.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                drawerAnimator = null;
            }
        });

        drawerAnimator.start();
    }
}
