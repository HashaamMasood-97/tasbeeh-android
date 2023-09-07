package com.example.tasbeeh;
public class TasbeehItem {
    private String name;
    private int count;
    private long timestamp;

    public TasbeehItem(String name, int count, long timestamp) {
        this.name = name;
        this.count = count;
        this.timestamp = timestamp;
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
}
