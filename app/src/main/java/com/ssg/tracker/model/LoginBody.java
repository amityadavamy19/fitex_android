package com.ssg.tracker.model;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("api_id")
    String api_id;
    @SerializedName("api_key")
    String api_key;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;

    public LoginBody(String api_id, String api_key, String email, String password) {
        this.api_id = api_id;
        this.api_key = api_key;
        this.email = email;
        this.password = password;
    }
}
