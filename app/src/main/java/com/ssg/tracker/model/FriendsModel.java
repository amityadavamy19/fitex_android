package com.ssg.tracker.model;

public class FriendsModel {
    String name;
    String id;
    String level;
    String totalcoins;
    String steps;

    public FriendsModel(String name, String id, String level, String totalcoins, String steps) {
        this.name = name;
        this.id = id;
        this.level = level;
        this.totalcoins = totalcoins;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTotalcoins() {
        return totalcoins;
    }

    public void setTotalcoins(String totalcoins) {
        this.totalcoins = totalcoins;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
