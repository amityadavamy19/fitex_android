package com.ssg.tracker.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BazarInfoModel implements Parcelable {
    String id;
    String uName;
    String uImage;
    String uUrl;
    String uDesc;

    public BazarInfoModel(String id, String uName, String uImage, String uUrl, String uDesc) {
        this.id = id;
        this.uName = uName;
        this.uImage = uImage;
        this.uUrl = uUrl;
        this.uDesc = uDesc;
    }

    protected BazarInfoModel(Parcel in) {
        id = in.readString();
        uName = in.readString();
        uImage = in.readString();
        uUrl = in.readString();
        uDesc = in.readString();
    }

    public static final Creator<BazarInfoModel> CREATOR = new Creator<BazarInfoModel>() {
        @Override
        public BazarInfoModel createFromParcel(Parcel in) {
            return new BazarInfoModel(in);
        }

        @Override
        public BazarInfoModel[] newArray(int size) {
            return new BazarInfoModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuImage() {
        return uImage;
    }

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }

    public String getuUrl() {
        return uUrl;
    }

    public void setuUrl(String uUrl) {
        this.uUrl = uUrl;
    }

    public String getuDesc() {
        return uDesc;
    }

    public void setuDesc(String uDesc) {
        this.uDesc = uDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uName);
        dest.writeString(uImage);
        dest.writeString(uUrl);
        dest.writeString(uDesc);
    }
}
