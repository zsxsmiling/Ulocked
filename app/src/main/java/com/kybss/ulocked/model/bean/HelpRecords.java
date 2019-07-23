package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\5\24 0024.
 */

public class HelpRecords implements Serializable {
    @SerializedName("id")
    String id;

    @SerializedName("helper")
    String helper;

    @SerializedName("reason")

    String reason;

    @SerializedName("contact")
    String contact;

    @SerializedName("created_at")
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContact() {

        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
