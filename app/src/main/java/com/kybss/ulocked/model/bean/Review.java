package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class Review implements Serializable {
    @SerializedName("message")
    String result;

    @SerializedName("code")
    int code;

    @SerializedName("data")
    String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
