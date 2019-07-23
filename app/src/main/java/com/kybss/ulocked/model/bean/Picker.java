package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\4\24 0024.
 */

public class Picker implements Serializable {
    @SerializedName("is_date")
    boolean is_date;

    @SerializedName("date")
    String date;

    @SerializedName("time")
    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean is_date() {
        return is_date;
    }

    public void setIs_date(boolean is_date) {
        this.is_date = is_date;
    }
}