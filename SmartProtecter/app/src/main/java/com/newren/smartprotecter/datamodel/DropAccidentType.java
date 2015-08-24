package com.newren.smartprotecter.datamodel;

/**
 * Created by 乐 on 2015/8/24.
 */
public class DropAccidentType {
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

    public DropAccidentType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public DropAccidentType() {
    }

    public String toString() {
        return value;
    }
}
