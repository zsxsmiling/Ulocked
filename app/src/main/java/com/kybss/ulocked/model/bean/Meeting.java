package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class Meeting implements Serializable {

     @SerializedName("doors")
    public List<Door> results;

    public List<Door> getResults() {
        return results;
    }

    public void setResults(List<Door> results) {
        this.results = results;
    }

    @SerializedName("id")
    int id;

    @SerializedName("roomId")
    String r_id;

    @SerializedName("theme")

    String theme;

    @SerializedName("auditorName")
    String auditor;

    @SerializedName("startTime")
    String start_time;

    @SerializedName("endTime")
    String end_time;

    @SerializedName("username")
    String user_id;

    @SerializedName("updatedAt")
    String updated_at;

    @SerializedName("status")
    String status;



    @SerializedName("createdAt")
    String created_at;

    @SerializedName("contain")
    String contain;

    @SerializedName("description")
    String description;

    @SerializedName("owner")
    long owner;

    @SerializedName("tag")
    String tag;

    @SerializedName("building")
    String building;

    @SerializedName("floor")
    int floor;

    @SerializedName("room")
    String room;

    @SerializedName("orderId")
    int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

}
