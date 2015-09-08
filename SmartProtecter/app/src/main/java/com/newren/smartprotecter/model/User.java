package com.newren.smartprotecter.model;


public class User {

    private Integer id;

    private String number;

    private  String name;

    private String password;

    private int sexAsInt;

    private int roleAsInt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSexAsInt() {
        return sexAsInt;
    }

    public void setSexAsInt(int sexAsInt) {
        this.sexAsInt = sexAsInt;
    }

    public int getRoleAsInt() {
        return roleAsInt;
    }

    public void setRoleAsInt(int roleAsInt) {
        this.roleAsInt = roleAsInt;
    }
}
