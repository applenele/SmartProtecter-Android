package com.newren.smartprotecter.model;

/**
 * Created by 乐 on 2015/8/30.
 */
public class Reply {
    private Integer id;

    private Integer userId;

    private  Integer accidentId;

    private String userName;

    private String time;

    private  String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(Integer accidentId) {
        this.accidentId = accidentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Reply() {
    }

    public Reply(Integer id, Integer userId, Integer accidentId, String userName, String time, String description) {
        this.id = id;
        this.userId = userId;
        this.accidentId = accidentId;
        this.userName = userName;
        this.time = time;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", userId=" + userId +
                ", accidentId=" + accidentId +
                ", userName='" + userName + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
