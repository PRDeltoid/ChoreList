package com.taylor.chorelist;

public class ChoreItem {
    private int interval,
                index;
    private String name;

    public ChoreItem(String name, int interval, int index) {
        this.name = name;
        this.interval = interval;
        this.index = index;
    }

    public ChoreItem() {
        this.interval = 0;
        this.name = "";
        this.index = -1;
    }

    public void set_interval(int interval) {
        this.interval = interval;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public void set_index(int index) {
        this.index = index;
    }

    public int get_interval() {
        return this.interval;
    }

    public String get_name() {
       return this.name;
    }

    public int get_index() {
        return this.index;
    }
}
