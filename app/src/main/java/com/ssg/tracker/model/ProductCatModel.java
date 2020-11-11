package com.ssg.tracker.model;

import java.util.ArrayList;

public class ProductCatModel {
    private String powerby_img;

    private String powerby_link;

    private ArrayList<String> pro_image;

    private String description;

    private String web_link;

    private String prod_name;

    private String terms;

    private String instruction;

    private String price;

    private String cat_id;

    private String id;

    public ProductCatModel(String powerby_img, String powerby_link, ArrayList<String> pro_image, String description, String web_link, String prod_name, String terms, String instruction, String price, String cat_id, String id) {
        this.powerby_img = powerby_img;
        this.powerby_link = powerby_link;
        this.pro_image = pro_image;
        this.description = description;
        this.web_link = web_link;
        this.prod_name = prod_name;
        this.terms = terms;
        this.instruction = instruction;
        this.price = price;
        this.cat_id = cat_id;
        this.id = id;
    }

    public String getPowerby_img() {
        return powerby_img;
    }

    public void setPowerby_img(String powerby_img) {
        this.powerby_img = powerby_img;
    }

    public String getPowerby_link() {
        return powerby_link;
    }

    public void setPowerby_link(String powerby_link) {
        this.powerby_link = powerby_link;
    }

    public ArrayList<String> getPro_image() {
        return pro_image;
    }

    public void setPro_image(ArrayList<String> pro_image) {
        this.pro_image = pro_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
