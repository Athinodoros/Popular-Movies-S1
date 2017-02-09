package com.example.athinodoros.popularmovie1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Athinodoros on 2/8/2017.
 */
public class MovieDetails extends AppCompatActivity {

    @BindView(R.id.movie_title_holder) TextView titleHolder;
    @BindView(R.id.movie_image_holder)ImageView imageHolder;
    @BindView(R.id.movie_synopsis_holder)TextView synopsisHolder;
    @BindView(R.id.movie_rating_holder) RatingBar rating;
    @BindView(R.id.rel_date_holder)TextView dateHolder ;
    @BindView(R.id.rating_in_text)TextView ratingTextHolder ;
    private final String base_img_url = "http://image.tmdb.org/t/p/w342/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        String title =  getIntent().getExtras().getString(MainActivity.TITLE);
        String path = getIntent().getExtras().getString(MainActivity.POSTER_PATH);
        String synopsis = getIntent().getExtras().getString(MainActivity.OVERVIEW);
        float ratingIn = getIntent().getExtras().getFloat(MainActivity.VOTE_AVRG);
        String release = getIntent().getExtras().getString(MainActivity.R_DATE);

        titleHolder.setText(title);
        synopsisHolder.setText(synopsis);
        Picasso.with(this).load(base_img_url+path).into(imageHolder);
        rating.setMax(10);
        rating.setRating( new Float(ratingIn).floatValue()/2);//because there are only half the stars
        ratingTextHolder.setText("("+ratingIn+")");
        dateHolder.setText(release);
    }
}
