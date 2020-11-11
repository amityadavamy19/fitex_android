package com.ssg.tracker;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.facebook.ParseFacebookUtils;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class AppApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if desired
                .clientKey(getString(R.string.back4app_client_key))
                .server("https://parseapi.back4app.com/")
                .build()
        );

        ParseFacebookUtils.initialize(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
