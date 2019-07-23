package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Door implements Serializable {

    @SerializedName("doorId")
    String d_id;

    @SerializedName("bltmac")
    String MAC;

    @SerializedName("description")
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }
}
