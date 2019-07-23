package com.kybss.ulocked.model.bean;

public class ResponseError_other {
    private int code;
    private String message;
    public ResponseError_other(int status, String message) {
        this.code = status;
        this.message = message;
    }
    public int getStatus() {
        return code;
    }

    public void setStatus(int status) {
        this.code = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "ResponseError{" +
                "status=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
