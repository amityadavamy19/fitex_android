package com.ssg.tracker.model;

public class DetailsWorkModel {
    String work_name;
    String work_count;
    String work_image;
    String title;

    public DetailsWorkModel(String work_name, String work_count, String work_image, String title) {
        this.work_name = work_name;
        this.work_count = work_count;
        this.work_image = work_image;
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getWork_count() {
        return work_count;
    }

    public void setWork_count(String work_count) {
        this.work_count = work_count;
    }

    public String getWork_image() {
        return work_image;
    }

    public void setWork_image(String work_image) {
        this.work_image = work_image;
    }
}
