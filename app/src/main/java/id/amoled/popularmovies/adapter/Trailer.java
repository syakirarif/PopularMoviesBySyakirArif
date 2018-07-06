package id.amoled.popularmovies.adapter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Wim on 5/29/17.
 */

public class Trailer extends BaseModel<TrailerData> {

    @SerializedName("id")
    private int id;

    public Trailer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
