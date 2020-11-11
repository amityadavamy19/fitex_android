package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.LoginController;
import com.ssg.tracker.library.SlideFragment;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SexSelectionfragment extends SlideFragment implements View.OnClickListener {
    private boolean nextViewenable = false;
    FancyButton username_button;
    AppCompatImageView female_id, male_id;
    TextInputEditText user_name_field;
    LoginResponse loginResponse;
    int melefemale = 0;
    SpinKitView loadinview;
    String userId;
    SSGInfo ssgInterface;

    public SexSelectionfragment() {
        // Required empty public constructor
    }

    public static SexSelectionfragment newInstance() {
        return new SexSelectionfragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sex_selection_view, container, false);
        getVisibleViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
        if (loginResponse != null) {
            user_name_field.setText(loginResponse.getName());
            userId = loginResponse.getUserId();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            System.out.println("Fragment is visble isVisibleToUser ");
            loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
            if (loginResponse != null) {
                user_name_field.setText(loginResponse.getName());
                userId = loginResponse.getUserId();
                System.out.println("Login Response is null ");
            } else {
                System.out.println("Login Response is null ");
            }
        } else {
            System.out.println("Fragment is visble not isVisibleToUser ");

        }

    }

    private void getVisibleViews(View view) {
        username_button = view.findViewById(R.id.username_button);
        username_button.setCustomTextFont("app_textfont.ttf");
        user_name_field = view.findViewById(R.id.user_name_field);
        loadinview = view.findViewById(R.id.loadinview1);
        female_id = view.findViewById(R.id.female_id);
        male_id = view.findViewById(R.id.male_id);
        username_button.setOnClickListener(this);
        female_id.setOnClickListener(this);
        male_id.setOnClickListener(this);
       // user_name_field
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean canGoForward() {
        return nextViewenable;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.username_button:

                String username = user_name_field.getText().toString();
                if (username == null || username.isEmpty()) {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    user_name_field.startAnimation(shake);

                } else if (melefemale == 0) {

                    showToastMessage("Please select Sex");
                } else {
                    String sex;
                    if (melefemale == 1) {
                        sex = "female";
                    } else {
                        sex = "male";
                    }
                    setsexUsername(userId, username, sex);
                }

                break;
            case R.id.female_id:
                female_id.setColorFilter(Color.parseColor("#20D0D3"));
                male_id.setColorFilter(Color.parseColor("#ffffff"));
                melefemale = 1;
                break;
            case R.id.male_id:
                female_id.setColorFilter(Color.parseColor("#ffffff"));
                male_id.setColorFilter(Color.parseColor("#20D0D3"));
                melefemale = 2;
                break;

        }
    }

    private void setsexUsername(String userId, String username, String sex) {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callsexUsername = ssgInterface.insertSexUsername(Config.API_KEY, Config.APP_ID, userId, username, sex, "true");
        callsexUsername.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("sext jsonObject : " + jsonObject);
                parserSexUsername(jsonObject, username, sex);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }

    private void parserSexUsername(String object, String userName, String sex) {
        try {
            JSONObject mainObject = new JSONObject(object);
            if (mainObject.has("response_code")) {
                int registrationcode = mainObject.getInt("response_code");
                if (registrationcode == 500) {
                    if (mainObject.has("message")) {
                        String message = mainObject.getString("message");
                        showToastMessage(message);
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        user_name_field.startAnimation(shake);
                        loadinview.setVisibility(View.GONE);
                    } else {
                        showToastMessage("Api error");
                        loadinview.setVisibility(View.GONE);
                    }

                } else if (registrationcode == 200) {
                    String message = mainObject.getString("message");
                    if (message.contains("Success")) {
                        loginResponse.setName(userName);
                        loginResponse.setGender(sex);
                        setPreferenceObject(getActivity(), loginResponse, "response");
                    }

                } else {

                }

            } else {
                showToastMessage("Api response code null");
                loadinview.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public Object getPreferenceObjectJson(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        String json = appSharedPrefs.getString("response", "nodata");
        if (json.contains("nodata")) {
            return null;
        } else {
            Gson gson = new Gson();
            LoginResponse selectedUser = gson.fromJson(json, LoginResponse.class);
            return selectedUser;
        }
    }

    public void setPreferenceObject(Context c, Object modal, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        prefsEditor.putString("response", jsonObject);
        prefsEditor.commit();
        loadinview.setVisibility(View.GONE);
        nextViewenable = true;
        showToastMessage("Please click next");

    }

}