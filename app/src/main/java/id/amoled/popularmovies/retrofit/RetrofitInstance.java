package id.amoled.popularmovies.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * </> with <3 by SyakirArif.
 */

public class RetrofitInstance {

    static final String URL = "http://api.themoviedb.org/3/";

    public static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
