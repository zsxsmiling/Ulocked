package com.kybss.ulocked.model.bean;

import java.util.Date;

public class resultpagenew {

    /**
     * createdAt : 2019-01-12 00:00:00
     * id : 1
     * roomId : Q-401
     * description : 前工院401
     * owner : 1
     * tag : lab
     * building : QGY
     * floor : 4
     * room : 401
     */

    private Date createdAt;
    private long id;
    private String roomId;
    private String description;
    private long owner;
    private String tag;
    private String building;
    private String floor;
    private String room;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
