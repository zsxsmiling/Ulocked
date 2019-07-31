package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    long id;
    @SerializedName("rank")
    int rank;

    @SerializedName("stuid")
    String usernumber;



    @SerializedName("dormInfo")
    String userdorm;

    @SerializedName("username")
    String username;


    @SerializedName("nickname")
    String nickname;

    @SerializedName("telephone")
    String mobile;

    @SerializedName("email")
    String email;

/*    @SerializedName("access_token")
    String token;*/

    @SerializedName("avatar_url")
    String avatarUrl;

    @SerializedName("last_address_id")
    long lastAddressId;

    @SerializedName("token")
    String token;
    /**
     * code : 200
     * message : 操作成功
     * data : {"tokenHead":"123123","token":"123131"}
     */

    private int code;
    private String message;
    private DataBean data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

/*    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }*/

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getLastAddressId() {
        return lastAddressId;
    }

    public void setLastAddressId(long lastAddressId) {
        this.lastAddressId = lastAddressId;
    }


    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getUserdorm() {
        return userdorm;
    }

    public void setUserdorm(String userdorm) {
        this.userdorm = userdorm;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
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

    public static class DataBean {
        /**
         * tokenHead : 123123
         * token : 123131
         */

        private String tokenHead;
        private String token;

        public String getTokenHead() {
            return tokenHead;
        }

        public void setTokenHead(String tokenHead) {
            this.tokenHead = tokenHead;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
