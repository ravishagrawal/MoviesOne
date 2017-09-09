package com.example.android.moviesone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(moviePosterOnClickListener);

        if (savedInstanceState == null) {
            getMoviesFromTMDb(getSortMethod());
        } else {
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.parcel_movie_structure));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                MovieStructure[] movies = new MovieStructure[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (MovieStructure) parcelable[i];
                }

                mGridView.setAdapter(new ImageAdapter(this, movies));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mail, mMenu);

        mMenu = menu;

        mMenu.add(Menu.NONE, R.string.pref_sort_pop_desc, Menu.NONE, null)
                .setVisible(false)
                .setIcon(R.drawable.ic_import_export_white_18dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        mMenu.add(Menu.NONE, R.string.pref_sort_ratings_desc, Menu.NONE, null)
                .setVisible(false)
                .setIcon(R.drawable.ic_star_rate_white_18dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        updateMenu();

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int numMovieObjects = mGridView.getCount();
        if (numMovieObjects > 0) {
            MovieStructure[] movies = new MovieStructure[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (MovieStructure) mGridView.getItemAtPosition(i);
            }

            outState.putParcelableArray(getString(R.string.parcel_movie_structure), movies);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.pref_sort_pop_desc:
                updateSharedPrefs(getString(R.string.tmdb_sort_pop_desc));
                updateMenu();
                getMoviesFromTMDb(getSortMethod());
                return true;
            case R.string.pref_sort_ratings_desc:
                updateSharedPrefs(getString(R.string.tmdb_sort_ratings_desc));
                updateMenu();
                getMoviesFromTMDb(getSortMethod());
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    private final GridView.OnItemClickListener moviePosterOnClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MovieStructure movie = (MovieStructure) parent.getItemAtPosition(position);

            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra(getResources().getString(R.string.parcel_movie_structure), movie);

            startActivity(intent);
        }
    };



    private void getMoviesFromTMDb(String sortMethod) {
        if (isNetworkAvailable()) {
            String apiKey = getString(R.string.secret_key);


            OnLoadComplete taskCompleted = new OnLoadComplete() {
                @Override
                public void onLoadMoviesCompleted(MovieStructure[] movies) {
                    mGridView.setAdapter(new ImageAdapter(getApplicationContext(), movies));
                }
            };

           MovieAsyncTask movieTask = new MovieAsyncTask(taskCompleted, apiKey);
            movieTask.execute(sortMethod);
        } else {
            Toast.makeText(this, getString(R.string.error_internet), Toast.LENGTH_LONG).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void updateMenu() {
        String sortMethod = getSortMethod();

        if (sortMethod.equals(getString(R.string.tmdb_sort_pop_desc))) {
            mMenu.findItem(R.string.pref_sort_pop_desc).setVisible(false);
            mMenu.findItem(R.string.pref_sort_ratings_desc).setVisible(true);
        } else {
            mMenu.findItem(R.string.pref_sort_ratings_desc).setVisible(false);
            mMenu.findItem(R.string.pref_sort_pop_desc).setVisible(true);
        }

    }


    private String getSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        return prefs.getString(getString(R.string.sort_method),
                getString(R.string.tmdb_sort_pop_desc));
    }

    private void updateSharedPrefs(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sort_method), sortMethod);
        editor.apply();
    }
}