package com.ssg.tracker.model;

import com.google.gson.annotations.SerializedName;

public class FbLoginBody {
    @SerializedName("api_id")
    String api_id;
    @SerializedName("api_key")
    String api_key;
    @SerializedName("id")
    String id;
    @SerializedName("s_email")
    String s_email;
    @SerializedName("s_name")
    String s_name;

    public FbLoginBody(String api_id, String api_key, String userid, String email, String password) {
        this.api_id = api_id;
        this.api_key = api_key;
        this.id = userid;
        this.s_email = email;
        this.s_name = password;
    }
}
