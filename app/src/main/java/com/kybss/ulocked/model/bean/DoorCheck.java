package com.kybss.ulocked.model.bean;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class DoorCheck {
    private int id;
    private int user_id;
    private long check_time;
    private int rand_num;
    private int door_num;
    private int status;

    public long getCheck_time() {
        return check_time;
    }

    public void setCheck_time(long check_time) {
        this.check_time = check_time;
    }

    public int getDoor_num() {
        return door_num;
    }

    public void setDoor_num(int door_num) {
        this.door_num = door_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRand_num() {
        return rand_num;
    }

    public void setRand_num(int rand_num) {
        this.rand_num = rand_num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
