package com.example.athinodoros.popularmovie1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.athinodoros.popularmovie1.model.FullResponseObject;
import com.example.athinodoros.popularmovie1.model.MovieItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static android.content.Context.MODE_PRIVATE;
import static com.example.athinodoros.popularmovie1.MainActivity.BY_POPULAR;

/**
 * Created by Athinodoros on 2/9/2017.
 */

public class GetData extends AsyncTask<String, Void, Collection<MovieItem>> {
    AsyncListener asyncListener;
    Context context;

    public GetData(AsyncListener listener,Context context) {
        this.asyncListener = listener;
        this.context = context;
    }

    @Override
    protected Collection<MovieItem> doInBackground(String... strings) {
        ArrayList<MovieItem> movieItems = (ArrayList<MovieItem>) loadMovieData();
        return movieItems;

    }

    @Override
    protected void onPostExecute(Collection<MovieItem> movieItems) {
//        super.onPostExecute(movieItems);
        asyncListener.onTaskComplete(movieItems);
    }

    private Collection<MovieItem> loadMovieData() {
        URL fullURL = null;
        String response = null;
        SharedPreferences byPref = context.getSharedPreferences("BY", MODE_PRIVATE);
        if (!byPref.contains("BY")) {
            byPref.edit().putString("BY", BY_POPULAR).commit();
        }
        fullURL = NetworkUtils.buildUrl(byPref.getString("BY", null));
        try {
            response = NetworkUtils.getResponseFromHttpUrl(fullURL);
            Gson gson = new Gson();
            FullResponseObject fullResponseObject = gson.fromJson(response, FullResponseObject.class);
            return fullResponseObject.getResults();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}