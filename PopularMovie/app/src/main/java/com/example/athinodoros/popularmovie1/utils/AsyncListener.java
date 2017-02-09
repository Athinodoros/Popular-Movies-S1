package com.example.athinodoros.popularmovie1.utils;

import com.example.athinodoros.popularmovie1.MainActivity;
import com.example.athinodoros.popularmovie1.model.MovieItem;

import java.util.Collection;
import java.util.List;

/**
 * Created by Athinodoros on 2/9/2017.
 */

public abstract class AsyncListener  {
    public abstract void onTaskComplete(Collection<MovieItem> movieItems);

}
