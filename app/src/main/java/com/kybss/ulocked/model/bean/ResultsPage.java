package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsPage<T> {


    public List<T> results;





    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }


}