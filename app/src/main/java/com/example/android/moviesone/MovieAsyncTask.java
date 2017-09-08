package com.example.android.moviesone;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hp on 03-09-2017.
 */

public class MovieAsyncTask extends AsyncTask<String, Void, MovieStructure[]> {

    private final String LOG_TAG = MovieAsyncTask.class.getSimpleName();
    private final String mApiKey;
    private final OnLoadComplete mListener;


    public MovieAsyncTask(OnLoadComplete listener, String apiKey) {
        super();

        mListener = listener;
        mApiKey = apiKey;
    }

    @Override
    protected MovieStructure[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        String moviesJsonStr = null;

        try {
            URL url = getApiUrl(params);


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            moviesJsonStr = builder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private MovieStructure[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        final String TAG_RESULTS = "results";
        final String TAG_TITLE = "original_title";
        final String TAG_POSTER_LINK = "poster_path";
        final String TAG_SYNOPSIS = "overview";
        final String TAG_RATINGS = "vote_average";
        final String TAG_RELEASE_DATE = "release_date";


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);


        MovieStructure[] movies = new MovieStructure[resultsArray.length()];


        for (int i = 0; i < resultsArray.length(); i++) {

            movies[i] = new MovieStructure();


            JSONObject movieInfo = resultsArray.getJSONObject(i);


            movies[i].setTitle(movieInfo.getString(TAG_TITLE));
            movies[i].setPosterLink(movieInfo.getString(TAG_POSTER_LINK));
            movies[i].setSynopsis(movieInfo.getString(TAG_SYNOPSIS));
            movies[i].setRatings(movieInfo.getDouble(TAG_RATINGS));
            movies[i].setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
        }

        return movies;
    }


    private URL getApiUrl(String[] parameters) throws MalformedURLException {
        final String TMDB_BASE_URL_1 = "https://api.themoviedb.org/3/movie/top_rated?";
        final String TMDB_BASE_URL_2 = "https://api.themoviedb.org/3/movie/popular?";
        final String SORT_BY_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";

        if (SORT_BY_PARAM.equals(parameters[0])) {

            Uri builtUri = Uri.parse(TMDB_BASE_URL_2).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, mApiKey)
                    .build();
            return new URL(builtUri.toString());

        }else{
            Uri builtUri = Uri.parse(TMDB_BASE_URL_1).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, mApiKey)
                    .build();
            return new URL(builtUri.toString());
        }

    }

    @Override
    protected void onPostExecute(MovieStructure[] movies) {
        super.onPostExecute(movies);

        mListener.onLoadMoviesCompleted(movies);
    }
}
