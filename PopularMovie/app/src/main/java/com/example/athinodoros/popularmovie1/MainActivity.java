package com.example.athinodoros.popularmovie1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.athinodoros.popularmovie1.model.FullResponseObject;
import com.example.athinodoros.popularmovie1.model.MovieItem;
import com.example.athinodoros.popularmovie1.utils.AsyncListener;
import com.example.athinodoros.popularmovie1.utils.GetData;
import com.example.athinodoros.popularmovie1.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.movieClickListener, Spinner.OnItemSelectedListener {

    @BindView(R.id.error_text_holder) TextView errorHolder;
    @BindView(R.id.movie_recycle) RecyclerView movieRecyclerView;
    @BindView(R.id.spinner_and_title) LinearLayout mSpinnerHolder;
    @BindView(R.id.sorting_spinner) Spinner mSpinner;

    public static final String BY_POPULAR = "popular";
    public static final String BY_RATING = "top_rated";
    public static final String URL_BASE = "http://api.themoviedb.org/3/movie/";
    public static final String API = "api_key";
    public static final String API_KEY = "---------API KEY HERE----------"; //TODO: A API-key is needed here
    public static final String PAGE = "page";

    private ArrayList<String> spinnerOptions = new ArrayList<>();
    public static final String POSTER_PATH = "POSTER_PATH";
    public static final String ADULT = "ADULT";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String ORIGINAL_TITLE = "ORIGINAL_TITLE";
    public static final String TITLE = "TITLE";
    public static final String R_DATE = "R_DATE";
    public static final String LANGUAGE_O = "LANGUAGE_O";
    public static final String POPULARITY = "POPULARITY";
    public static final String VOTE_COUNT = "VOTE_COUNT";
    public static final String VOTE_AVRG = "VOTE_AVRG";

    AsyncListener asyncListener = new AsyncListener() {
        @Override
        public void onTaskComplete(Collection<MovieItem> movieItems) {
            if (movieItems != null) {
                showError();
                movieAdapter.setMovieData((ArrayList<MovieItem>) movieItems);
                movieAdapter.notifyDataSetChanged();
            } else {
                showResults();
            }
        }
    };

    MovieAdapter movieAdapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        spinnerOptions.add(BY_POPULAR);
        spinnerOptions.add(BY_RATING);
        movieAdapter = new MovieAdapter(this, this);
        final LinearLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this), GridLayoutManager.VERTICAL, false);
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setAdapter(movieAdapter);
        movieRecyclerView.setHasFixedSize(true);
        ArrayList<MovieItem> demoData = new ArrayList<>();
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerOptions);
        mSpinner.setAdapter(stringArrayAdapter);
        movieAdapter.setMovieData(demoData);
        movieRecyclerView.setAdapter(movieAdapter);
        errorHolder.setVisibility(View.INVISIBLE);
        errorHolder.setText(com.example.athinodoros.popularmovie1.R.string.values_not_loaded);
        mSpinner.setOnItemSelectedListener(this);

        movieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem > movieAdapter.getItemCount() - 5) {
                    new GetData(asyncListener, getApplicationContext()).execute(getSharedPreferences("BY", MODE_PRIVATE).getString("BY", BY_POPULAR));
                }
            }
        });
        if (isOnline())
            new GetData(asyncListener, getApplicationContext()).execute(getSharedPreferences("BY", MODE_PRIVATE).getString("BY", BY_POPULAR));
        else {
            showError();
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        NetworkUtils.page = 0;
        SharedPreferences sharedPreferences = getSharedPreferences("BY", MODE_PRIVATE);
        if (i == 0)
            sharedPreferences.edit().putString("BY", BY_POPULAR).commit();
        if (i == 1)
            sharedPreferences.edit().putString("BY", BY_RATING).commit();
        movieAdapter.emptyList();
        new GetData(asyncListener, getApplicationContext()).execute(sharedPreferences.getString("BY", BY_POPULAR));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


//    class GetData extends AsyncTask<String, Void, Collection<MovieItem>> {
//
//        @Override
//        protected Collection<MovieItem> doInBackground(String... strings) {
//            ArrayList<MovieItem> movieItems = (ArrayList<MovieItem>) loadMovieData();
//            return movieItems;
//
//        }
//
//        @Override
//        protected void onPostExecute(Collection<MovieItem> movieItems) {
////        super.onPostExecute(movieItems);
//            if (movieItems != null) {
//                showError();
//                movieAdapter.setMovieData((ArrayList<MovieItem>) movieItems);
//                movieAdapter.notifyDataSetChanged();
//            } else {
//                showResults();
//            }
//        }
//    }

    private void showResults() {
        mSpinnerHolder.setVisibility(View.GONE);
        errorHolder.setVisibility(View.VISIBLE);
    }

    private void showError() {
        errorHolder.setVisibility(View.GONE);
        mSpinnerHolder.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(MovieItem movie) {
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra(POSTER_PATH, movie.getPoster_path());
        intent.putExtra(ADULT, movie.isAdult());
        intent.putExtra(OVERVIEW, movie.getOverview());
        intent.putExtra(ORIGINAL_TITLE, movie.getOriginal_title());
        intent.putExtra(TITLE, movie.getTitle());
        intent.putExtra(R_DATE, movie.getRelease_date());
        intent.putExtra(LANGUAGE_O, movie.getOriginal_language());
        intent.putExtra(POPULARITY, movie.getPopularity());
        intent.putExtra(VOTE_COUNT, movie.getVote_count());
        intent.putExtra(VOTE_AVRG, movie.getVote_average());

        startActivity(intent);
//        Toast.makeText(this, movieID, Toast.LENGTH_SHORT).show();
    }


}
