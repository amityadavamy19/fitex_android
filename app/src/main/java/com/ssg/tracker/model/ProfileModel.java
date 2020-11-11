package com.ssg.tracker.model;

public class ProfileModel {
      private String total_coins;

    private String name;

    private String weight;

    private String user_level;

    private String email;

    private String age;

    private String height;

    public ProfileModel(String total_coins, String name, String weight, String user_level, String email, String age, String height) {
        this.total_coins = total_coins;
        this.name = name;
        this.weight = weight;
        this.user_level = user_level;
        this.email = email;
        this.age = age;
        this.height = height;
    }

    public String getTotal_coins() {
        return total_coins;
    }

    public void setTotal_coins(String total_coins) {
        this.total_coins = total_coins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
