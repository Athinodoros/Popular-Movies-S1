package com.example.athinodoros.popularmovie1.model;

import java.util.ArrayList;

/**
 * Created by Athinodoros on 2/8/2017.
 */
public class FullResponseObject {
    private int page;
    ArrayList<MovieItem> results;
    String total_results;
    String total_pages;

    public FullResponseObject(int page) {
        this.page = page;
    }

    public FullResponseObject(int page, ArrayList<MovieItem> movies, String total_results, String total_pages) {
        this.page = page;
        this.results = movies;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MovieItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieItem> results) {
        this.results = results;
    }

    public String getTotal_results() {
        return total_results;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public String toString() {
        return "FullResponseObject{" +
                "page=" + page +
                ", results=" + results.size() +
                ", total_results='" + total_results + '\'' +
                ", total_pages='" + total_pages + '\'' +
                '}';
    }
}
