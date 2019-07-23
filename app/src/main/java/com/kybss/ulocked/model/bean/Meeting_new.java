package com.kybss.ulocked.model.bean;

import java.util.List;

public class Meeting_new {
    /**
     * id : 13
     * roomId : QF4
     * username : 熊琅钰
     * theme : 开启4楼大门
     * status : 3
     * auditorName : 熊琅钰
     * startTime : 2019-01-12 14:00:00
     * endTime : 2019-02-12 14:00:00
     * createdAt : 2019-05-27 20:33:14
     * updatedAt : 2019-06-24 16:02:42
     * doors : [{"doorId":"QF41","description":"前工院4楼西门","bltmac":"78:04:73:CF:AC:0E"},{"doorId":"QF42","description":"前工院4楼中门","bltmac":"78:04:73:CF:D4:A3"},{"doorId":"QF43","description":"前工院4楼东门","bltmac":"78:04:73:CF:B0:B5"}]
     */

    private long id;
    private String roomId;
    private String username;
    private String theme;
    private int status;
    private String auditorName;
    private String startTime;
    private String endTime;
    private String createdAt;
    private String updatedAt;
    private List<DoorsBean> doors;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<DoorsBean> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorsBean> doors) {
        this.doors = doors;
    }

    public static class DoorsBean {
        /**
         * doorId : QF41
         * description : 前工院4楼西门
         * bltmac : 78:04:73:CF:AC:0E
         */

        private String doorId;
        private String description;
        private String bltmac;

        public String getDoorId() {
            return doorId;
        }

        public void setDoorId(String doorId) {
            this.doorId = doorId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getBltmac() {
            return bltmac;
        }

        public void setBltmac(String bltmac) {
            this.bltmac = bltmac;
        }
    }
}
