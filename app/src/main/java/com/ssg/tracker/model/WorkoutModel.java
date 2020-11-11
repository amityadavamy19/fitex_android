package com.ssg.tracker.model;

public class WorkoutModel {
    String id;
    String gifView;
    String gifName;

    public WorkoutModel(String id, String gifView, String gifName) {

        this.id = id;
        this.gifView = gifView;
        this.gifName = gifName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGifView() {
        return gifView;
    }

    public void setGifView(String gifView) {
        this.gifView = gifView;
    }

    public String getGifName() {
        return gifName;
    }

    public void setGifName(String gifName) {
        this.gifName = gifName;
    }
}
