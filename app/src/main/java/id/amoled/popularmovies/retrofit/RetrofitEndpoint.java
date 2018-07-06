package id.amoled.popularmovies.retrofit;

import id.amoled.popularmovies.adapter.Review;
import id.amoled.popularmovies.adapter.Trailer;
import id.amoled.popularmovies.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * </> with <3 by SyakirArif.
 */

public interface RetrofitEndpoint {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET(Constant.MOVIE_PATH + "/{movie_id}/" + Constant.VIDEOS)
    Call<Trailer> trailers(
            @Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET(Constant.MOVIE_PATH + "/{movie_id}/" + Constant.REVIEWS)
    Call<Review> reviews(
            @Path("movie_id") int movieId, @Query("api_key") String apiKey);

}
