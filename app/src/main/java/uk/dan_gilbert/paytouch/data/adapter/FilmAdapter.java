package uk.dan_gilbert.paytouch.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import uk.dan_gilbert.paytouch.R;
import uk.dan_gilbert.paytouch.data.model.Film;
import uk.dan_gilbert.paytouch.ui.TypographyFacade;

/**
 * Created by dangilbert on 26/12/14.
 */
public class FilmAdapter extends BaseAdapter {

    private Film[] films;

    public FilmAdapter(Film[] films) {
        this.films = films;
    }

    @Override
    public int getCount() {
        return films == null ? 0 : films.length;
    }

    @Override
    public Film getItem(int position) {
        return films[position];
    }

    @Override
    public long getItemId(int position) {
        return films[position].id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindView(getItem(position));

        return convertView;
    }

    static class ViewHolder {

        private Context ctx;
        static SimpleDateFormat sdf;

        @InjectView(R.id.film_title) TextView title;
        @InjectView(R.id.film_date) TextView date;
        @InjectView(R.id.average_rating) TextView rating;
        @InjectView(R.id.ratings_count) TextView ratingCount;
        @InjectView(R.id.imageview_film_poster) ImageView poster;

        @InjectViews( { R.id.film_title, R.id.film_date, R.id.average_rating, R.id.ratings_count} ) List<TextView> textViewList;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
            this.ctx = v.getContext();
            ButterKnife.apply(textViewList, TypographyFacade.typographyAction);
        }

        public void bindView(Film film) {
            title.setText(film.title);
            date.setText(convertDate(film.releaseDate));
            rating.setText(String.format(Locale.getDefault(), "%.1f", film.voteAverage));
            ratingCount.setText(String.valueOf(film.voteCount + " votes"));

            Picasso.with(ctx)
                    .load(film.posterPath)
                    .into(poster);
        }

        private String convertDate(long dateMillis) {

            if (sdf == null) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            }

            return sdf.format(new Date(dateMillis));
        }

        private String convertDate(Date date) {

            if (sdf == null) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            }

            return sdf.format(date);
        }

    }
}
