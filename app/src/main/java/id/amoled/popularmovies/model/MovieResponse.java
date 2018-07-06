
package id.amoled.popularmovies.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * </> with <3 by SyakirArif.
 */

public class MovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MovieResult> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieResult> getResults() {
        return results;
    }

    public List<MovieResult> getMovies() {
        return results;
    }

    public void setResults(List<MovieResult> results) {
        this.results = results;
    }

    public void setMovies(List<MovieResult> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
