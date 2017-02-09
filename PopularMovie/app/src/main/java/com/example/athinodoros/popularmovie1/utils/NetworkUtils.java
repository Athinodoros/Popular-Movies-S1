
package com.example.athinodoros.popularmovie1.utils;

import android.net.Uri;

import com.example.athinodoros.popularmovie1.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Athinodoros on 2/8/2017.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    private static final String BASE_URL =
            "http://api.themoviedb.org/3/movie/";

    public static int page=0;

    public static URL buildUrl(String sort) {

        page++;
        Uri builtUri = Uri.parse(BASE_URL+sort)
                .buildUpon().appendQueryParameter(MainActivity.API,MainActivity.API_KEY)
                .appendQueryParameter(MainActivity.PAGE,String.valueOf(page))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }
}