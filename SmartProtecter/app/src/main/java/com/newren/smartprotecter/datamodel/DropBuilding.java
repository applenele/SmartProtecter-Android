package com.newren.smartprotecter.datamodel;

/**
 * Created by 乐 on 2015/8/24.
 */
public class DropBuilding {
    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DropBuilding(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public DropBuilding() {
    }

    public String toString() {
        return value;
    }
}
