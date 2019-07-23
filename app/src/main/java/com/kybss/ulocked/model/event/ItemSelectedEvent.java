package com.kybss.ulocked.model.event;

public class ItemSelectedEvent {
    private String mac;
    private String d_id;

    public ItemSelectedEvent(String mac,String d_id){
        this.mac = mac;
        this.d_id = d_id;
    }
    public String getMac(){
        return this.mac;
    }
    public String getD_id(){
        return this.d_id;
    }

}
