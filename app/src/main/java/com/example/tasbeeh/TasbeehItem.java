package com.example.tasbeeh;
public class TasbeehItem {
    private String name;
    private int count;
    private long timestamp;
    private boolean isUpdateMode; // Add this field
    public TasbeehItem(String name, int count, long timestamp) {
        this.name = name;
        this.count = count;
        this.timestamp = timestamp;
        this.isUpdateMode = false;

    }


    public Boolean getUpdateMode() {
        return isUpdateMode;
    }

    public void setUpdateMode(Boolean updateMode) {
        isUpdateMode = updateMode;
    }
    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
