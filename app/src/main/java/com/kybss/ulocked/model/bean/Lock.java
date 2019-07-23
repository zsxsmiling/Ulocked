package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\4\28 0028.
 */

public class Lock implements Serializable {


    @SerializedName("pin")
    String pin;

    @SerializedName("doorId")
    String door_id;

    @SerializedName("admin")
    boolean admin;

    @SerializedName("mac")
    String mac;
    @SerializedName("currentPwd")
    String currentPwd;
    @SerializedName("nextPwd")
    String nextPwd;

    public String getCurrentPwd() {
        return currentPwd;
    }

    public void setCurrentPwd(String currentPwd) {
        this.currentPwd = currentPwd;
    }

    public String getNextPwd() {
        return nextPwd;
    }

    public void setNextPwd(String nextPwd) {
        this.nextPwd = nextPwd;
    }

    /**
     * code : 200
     * message : 操作成功
     * data : {"currentPwd":"100000","nextPwd":"424917"}
     */
    @SerializedName("code")
    int code;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    DataBean data;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getDoor_id() {
        return door_id;
    }

    public void setDoor_id(String door_id) {
        this.door_id = door_id;
    }





    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * currentPwd : 100000
         * nextPwd : 424917
         */
        @SerializedName("currentPwd")
        String currentPwd;
        @SerializedName("nextPwd")
        String nextPwd;

        public String getCurrentPwd() {
            return currentPwd;
        }

        public void setCurrentPwd(String currentPwd) {
            this.currentPwd = currentPwd;
        }

        public String getNextPwd() {
            return nextPwd;
        }

        public void setNextPwd(String nextPwd) {
            this.nextPwd = nextPwd;
        }
    }
}