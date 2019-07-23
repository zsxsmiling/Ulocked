package com.kybss.ulocked.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * author：cheikh on 16/5/12 00:11
 * email：wanghonghi@126.com
 */
public class Token {

    @SerializedName("user_id")
    String userId;

    @SerializedName("expires_in")
    int expiresin;

    @SerializedName("scope")
    String scope;

    @SerializedName("refresh_token")
    String refreshToken;
    /**
     * code : 200
     * message : 操作成功
     * data : {"tokenHead":"123123","token":"123131"}
     */

    private int code;
    private String message;
    private DataBean data;




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(int expiresin) {
        this.expiresin = expiresin;
    }



    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
