package com.example.android.moviesone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 03-09-2017.
 */

public class MovieStructure implements Parcelable {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private String mTitle;
    private String mPosterLink;
    private String mSynopsis;
    private Double mRatings;
    private String mReleaseDate;


    public MovieStructure() {
    }


    public void setTitle(String title) {
        mTitle = title;
    }


    public void setPosterLink(String PosterLink) {
        mPosterLink = PosterLink;
    }


    public void setSynopsis(String synopsis) {
        if (!synopsis.equals("null")) {
            mSynopsis = synopsis;
        }
    }


    public void setRatings(Double ratings) {
        mRatings = ratings;
    }


    public void setReleaseDate(String releaseDate) {
        if (!releaseDate.equals("null")) {
            mReleaseDate = releaseDate;
        }
    }

    public String getTitle() {
        return mTitle;
    }


    public String getPosterLink() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + mPosterLink;
    }


    public String getSynopsis() {
        return mSynopsis;
    }


    private Double getRatings() {
        return mRatings;
    }


    public String getReleaseDate() {
        return mReleaseDate;
    }


    public String getAverageRatings() {
        return String.valueOf(getRatings()) + "/10";
    }


    public String getDateFormat() {
        return DATE_FORMAT;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterLink);
        dest.writeString(mSynopsis);
        dest.writeValue(mRatings);
        dest.writeString(mReleaseDate);
    }

    private MovieStructure(Parcel in) {
        mTitle = in.readString();
        mPosterLink = in.readString();
        mSynopsis = in.readString();
        mRatings = (Double) in.readValue(Double.class.getClassLoader());
        mReleaseDate = in.readString();
    }

    public static final Parcelable.Creator<MovieStructure> CREATOR = new Parcelable.Creator<MovieStructure>() {
        public MovieStructure createFromParcel(Parcel source) {
            return new MovieStructure(source);
        }

        public MovieStructure[] newArray(int size) {
            return new MovieStructure[size];
        }
    };
}
