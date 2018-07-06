package id.amoled.popularmovies.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.util.List;

import id.amoled.popularmovies.R;
import id.amoled.popularmovies.adapter.Trailer;
import id.amoled.popularmovies.adapter.TrailerData;
import id.amoled.popularmovies.model.MovieResult;
import id.amoled.popularmovies.retrofit.ApiService;
import id.amoled.popularmovies.util.ConnectionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * </> with <3 by SyakirArif.
 */

public class DetailsActivity extends AppCompatActivity {
    TextView nameOfMovie, plotSynopsis, userRating, releaseDate, durationTime, genreMovie;
    ImageView imageView;
    LinearLayout viewTrailer, viewReview;
    ProgressBar pgReviews, pgTrailers;
    ApiService apiService;
    MovieResult movieResult;

    private static final String TAG = "DetailsActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //change action bar title
        getSupportActionBar().setTitle("Details");

        //Initializing fields

        imageView = (ImageView) findViewById(R.id.img_poster);
        nameOfMovie = (TextView) findViewById(R.id.title);
        plotSynopsis = (TextView) findViewById(R.id.plotsynopsis);
        userRating = (TextView) findViewById(R.id.userrating);
        releaseDate = (TextView) findViewById(R.id.releasedate);
        durationTime = (TextView) findViewById(R.id.durationTime);
        genreMovie = (TextView) findViewById(R.id.genreMovie);

        viewReview = (LinearLayout) findViewById(R.id.view_reviews);
        viewTrailer = (LinearLayout) findViewById(R.id.view_trailers);

        pgReviews = (ProgressBar) findViewById(R.id.pg_review);
        pgTrailers = (ProgressBar) findViewById(R.id.pg_trailer);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("original_title")) {

            //get all needed extras intent
            String thumbnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String synopsis = getIntent().getExtras().getString("overview");
            String rating = getIntent().getExtras().getString("vote_average");
            String dateOfRelease = getIntent().getExtras().getString("release_date");
            Integer id = getIntent().getExtras().getInt("id");
            String idnya = Integer.toString(id);
//            Log.d(TAG, idnya);

            //setting data to appropriate views
            Glide.with(this)
                    .load(thumbnail)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
            loadTrailer(id);

        } else {

            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();

        }

    }

    private void loadTrailer(int id) {
        pgTrailers.setVisibility(View.VISIBLE);

        apiService.getTrailers(id, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Trailer trailer = (Trailer) response.body();

                if (trailer != null) {
                    showTrailers(trailer.getResults());
                } else {
                    Toast.makeText(getApplicationContext(), "No Data!", Toast.LENGTH_LONG).show();
                }

                pgTrailers.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), "Request Timeout. Please try again!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                }

                pgTrailers.setVisibility(View.GONE);
            }
        });
    }


    private void showTrailers(List<TrailerData> trailerDatas) {
        viewTrailer.removeAllViews();

        for (int i = 0; i < trailerDatas.size(); i++) {

            final TrailerData trailerData = trailerDatas.get(i);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_trailer, viewTrailer, false);

            ImageView trailerThumb = (ImageView) view.findViewById(R.id.trailer_thumb);
            TextView trailerName = (TextView) view.findViewById(R.id.trailer_name);

            if (trailerData.getSite().equalsIgnoreCase("youtube")) {
                Picasso.with(getApplicationContext())
                        .load("http://img.youtube.com/vi/" + trailerData.getKey() + "/default.jpg")
                        .into(trailerThumb);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(trailerData.getKey());
                }
            });


            trailerName.setText(trailerData.getName());
            viewTrailer.addView(view);
        }
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
