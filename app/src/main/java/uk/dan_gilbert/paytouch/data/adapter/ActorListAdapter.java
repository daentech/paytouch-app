package uk.dan_gilbert.paytouch.data.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.dan_gilbert.paytouch.R;
import uk.dan_gilbert.paytouch.data.model.Actor;
import uk.dan_gilbert.paytouch.ui.transformation.CircleTransform;

/**
 * Created by dangilbert on 26/12/14.
 */
public class ActorListAdapter extends BaseAdapter {

    private Context ctx;
    private LinkedHashMap<Integer, Actor> actors = new LinkedHashMap<>();

    public ActorListAdapter(Context context, LinkedHashMap<Integer, Actor> actors) {
        super();
        if (actors != null) {
            this.actors = actors;
        }
        this.ctx = context;
    }

    @Override
    public int getCount() {
        return actors.size();
    }

    @Override
    public Actor getItem(int position) {
        return (Actor) actors.values().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return ((Actor)actors.values().toArray()[position]).identifier;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.actor_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindView(getItem(position), position);

        return convertView;
    }

    public void setActors(LinkedHashMap<Integer, Actor> actors) {
        this.actors = actors;
    }

    static class ViewHolder {

        Resources res;
        Context ctx;

        private Drawable lightPin, darkPin;
        private int pinWidth;

        @InjectView(R.id.item_background) View background;
        @InjectView(R.id.actor_name) TextView actorName;
        @InjectView(R.id.actor_location) TextView actorLocation;
        @InjectView(R.id.actor_description) TextView actorDescription;
        @InjectView(R.id.actor_profile_image) ImageView actorProfileImage;
        @InjectView(R.id.actor_popularity) TextView actorPopularity;


        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
            res = v.getResources();
            ctx = v.getContext();

            pinWidth = res.getDimensionPixelSize(R.dimen.pin_width);
        }

        public void bindView(Actor actor, int position) {

            switch (position % 2) {
                case 0:
                    background.setBackgroundResource(R.drawable.dark_rounded_background);
                    actorName.setTextColor(res.getColor(R.color.dark_item_text_color));
                    actorLocation.setTextColor(res.getColor(R.color.dark_item_text_color));
                    actorDescription.setTextColor(res.getColor(R.color.dark_item_text_color));

                    if (lightPin == null) {
                        lightPin = res.getDrawable(R.drawable.pin_white);
                        lightPin.setBounds(0, 0, pinWidth, pinWidth);
                    }

                    actorLocation.setCompoundDrawables(lightPin, null, null, null);
                    break;
                case 1:
                    background.setBackgroundResource(R.drawable.light_rounded_background);
                    actorName.setTextColor(res.getColor(R.color.light_item_text_color));
                    actorLocation.setTextColor(res.getColor(R.color.light_item_text_color));
                    actorDescription.setTextColor(res.getColor(R.color.light_item_text_color));

                    if (darkPin == null) {
                        darkPin = res.getDrawable(R.drawable.pin_black);
                        darkPin.setBounds(0, 0, pinWidth, pinWidth);
                    }

                    actorLocation.setCompoundDrawables(darkPin, null, null, null);
                    break;
            }





            actorName.setText(Html.fromHtml(actor.displayName()));
            actorLocation.setText(actor.location);
            actorDescription.setText(actor.description);
            actorPopularity.setText(String.format(Locale.getDefault(), "%.2f", actor.popularity));

            Picasso.with(ctx).load(actor.profilePath)
                    .placeholder(R.drawable.profile_placeholder)
                    .transform(new CircleTransform())
                    .into(actorProfileImage);
        }
    }
}
