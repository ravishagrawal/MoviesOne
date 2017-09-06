package com.example.android.moviesone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by hp on 03-09-2017.
 */

public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final MovieStructure[] mMoviesStructure;


    public ImageAdapter(Context context, MovieStructure[] MoviesStructure) {
        mContext = context;
        mMoviesStructure = MoviesStructure;
    }

    @Override
    public int getCount() {
        if (mMoviesStructure == null || mMoviesStructure.length == 0) {
            return -1;
        }

        return mMoviesStructure.length;
    }

    @Override
    public MovieStructure getItem(int position) {
        if (mMoviesStructure == null || mMoviesStructure.length == 0) {
            return null;
        }

        return mMoviesStructure[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mMoviesStructure[position].getPosterLink())
                .resize(mContext.getResources().getInteger(R.integer.poster_width),
                        mContext.getResources().getInteger(R.integer.poster_length))
                .into(imageView);

        return imageView;
    }
}
