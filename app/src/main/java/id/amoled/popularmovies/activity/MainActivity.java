package id.amoled.popularmovies.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.amoled.popularmovies.model.MovieResponse;
import id.amoled.popularmovies.retrofit.Constant;
import id.amoled.popularmovies.retrofit.RetrofitEndpoint;
import id.amoled.popularmovies.retrofit.RetrofitInstance;
import id.amoled.popularmovies.adapter.MovieAdapter;
import id.amoled.popularmovies.R;
import id.amoled.popularmovies.model.MovieResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * </> with <3 by SyakirArif.
 */

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<MovieResult> movieList;
    ProgressDialog pd;
    //public static final String LOG_TAG = MovieAdapter.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }

    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        checkSortOrder();

    }

    private void loadJSONPopular() {

        try {
            if (Constant.API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            RetrofitEndpoint retrofitEndpoint =
                    RetrofitInstance.getRetrofitInstance().create(RetrofitEndpoint.class);
            Call<MovieResponse> call = retrofitEndpoint.getPopularMovies(Constant.API_KEY);
            call.enqueue(new Callback<MovieResponse>() {
                ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);

                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    dialog.dismiss();
                    List<MovieResult> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    dialog.dismiss();
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching data. Check your internet connection!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJSONTopRated() {

        try {
            if (Constant.API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            RetrofitEndpoint retrofitEndpoint =
                    RetrofitInstance.getRetrofitInstance().create(RetrofitEndpoint.class);
            Call<MovieResponse> call = retrofitEndpoint.getTopRatedMovies(Constant.API_KEY);
            call.enqueue(new Callback<MovieResponse>() {

                ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);

                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    dialog.dismiss();
                    List<MovieResult> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    dialog.dismiss();
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                initViews();
                return true;
            case R.id.action_setting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_about:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("About");
                builder.setMessage("</> with <3 by Muhammad Syakir Arif \n\nPowered by Udacity");
                builder.setCancelable(true);
                builder.setNeutralButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to exit?");
        builder.setCancelable(true);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Toast.makeText(this, "Preferences updated", Toast.LENGTH_SHORT)
                .show();
        checkSortOrder();
    }

    private void checkSortOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            //Log.d(LOG_TAG, "Sorting by most popular");
            loadJSONPopular();
            //change action bar title
            getSupportActionBar().setTitle("Most Popular Movies");
        } else {
            //Log.d(LOG_TAG, "Sorting by Top Rated");
            loadJSONTopRated();
            //change action bar title
            getSupportActionBar().setTitle("Top Rated Movies");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (movieList.isEmpty()) {
            checkSortOrder();
            checkSortOrder();
        }
    }
}

