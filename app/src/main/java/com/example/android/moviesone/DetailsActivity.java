package com.example.android.moviesone;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;


/**
 * Created by hp on 02-09-2017.
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView tvTitle = (TextView) findViewById(R.id.textview_movie_title);
        ImageView ivPoster = (ImageView) findViewById(R.id.imageview_poster);
        TextView tvSynopsis = (TextView) findViewById(R.id.textView_synopsis);
        TextView tvRatings = (TextView) findViewById(R.id.textview_ratings);
        TextView tvReleaseDate = (TextView) findViewById(R.id.textview_release_date);

        Intent intent = getIntent();
        MovieStructure movie = intent.getParcelableExtra(getString(R.string.parcel_movie_structure));

        tvTitle.setText(movie.getTitle());

        Picasso.with(this)
                .load(movie.getPosterLink())
                .resize(getResources().getInteger(R.integer.poster_width),
                        getResources().getInteger(R.integer.poster_length))
                .into(ivPoster);

        String overView = movie.getSynopsis();
        if (overView == null) {
            tvSynopsis.setTypeface(null, Typeface.ITALIC);
            overView = getResources().getString(R.string.no_synopsis_found);
        }
        tvSynopsis.setText(overView);
        tvRatings.setText(movie.getAverageRatings());


        String releaseDate = movie.getReleaseDate();
        if(releaseDate != null) {try {
            releaseDate = DateFormatter.getDate(this,
                    releaseDate, movie.getDateFormat());
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error with parsing movie release date", e);
        }

        } else {
            tvReleaseDate.setTypeface(null, Typeface.ITALIC);
            releaseDate = getResources().getString(R.string.release_date_missing);
        }
        tvReleaseDate.setText(releaseDate);
    }
}

