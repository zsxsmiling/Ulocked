package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\5\23 0023.
 */

public class Help implements Serializable {
    @SerializedName("result")
    String result;

    @SerializedName("code")
    String code;
}
