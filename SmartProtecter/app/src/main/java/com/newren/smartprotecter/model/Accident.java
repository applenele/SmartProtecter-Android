package com.newren.smartprotecter.model;

import java.sql.Date;

/**
 * Created by 乐 on 2015/8/24.
 */
public class Accident  {

    private  Integer id;

    private  String district;

    private String building;

    private String userNmae;

    private String floor;

    private String room;

    private String type;

    private String Description;

    private String time;

    private Integer statuAsInt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUserNmae() {
        return userNmae;
    }

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStatuAsInt() {
        return statuAsInt;
    }

    public void setStatuAsInt(Integer statuAsInt) {
        this.statuAsInt = statuAsInt;
    }
}
