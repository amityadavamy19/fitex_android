package com.ssg.tracker.model;

import java.util.ArrayList;

public class SectionModel {
    private String sectionLabel;
    private ArrayList<BazarInfoModel> itemArrayList;

    public SectionModel(String sectionLabel, ArrayList<BazarInfoModel> itemArrayList) {
        this.sectionLabel = sectionLabel;
        this.itemArrayList = itemArrayList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<BazarInfoModel> getItemArrayList() {
        return itemArrayList;
    }
}
