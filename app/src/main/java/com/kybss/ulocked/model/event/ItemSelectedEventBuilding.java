package com.kybss.ulocked.model.event;

public class ItemSelectedEventBuilding {

    private String building;
    private String floor;


    public ItemSelectedEventBuilding(String building,String floor){
        this.building = building;
        this.floor = floor;
    }
    public String getBuilding(){
        return this.building;
    }
    public String getFloor(){
        return this.floor;
    }
}

