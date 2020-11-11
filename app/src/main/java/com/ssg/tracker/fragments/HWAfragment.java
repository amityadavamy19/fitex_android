package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.shawnlin.numberpicker.NumberPicker;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HWAfragment extends SlideFragment implements View.OnClickListener {
    private boolean nextViewenable = false;
    RelativeLayout ft_view, inch_view, age_view, weight_view;
    AppCompatTextView height_one_txt, height_two_txt, age_txt, weight_txt;
    SSGInfo ssgInterface;
    String userId;
    LoginResponse loginResponse;
    SpinKitView loadinview;
    FancyButton submit_button;

    public HWAfragment() {
        // Required empty public constructor
    }

    public static HWAfragment newInstance() {
        return new HWAfragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.body_info_view, container, false);
        getVisisVleViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createObject();
    }

    private void createObject() {
        ft_view.setOnClickListener(this);
        inch_view.setOnClickListener(this);
        age_view.setOnClickListener(this);
        weight_view.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
        if (loginResponse != null) {
            userId = loginResponse.getUserId();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("userId", loginResponse.getUserId()).apply();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            System.out.println("Fragment is visble isVisibleToUser ");
            loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
            if (loginResponse != null) {
                userId = loginResponse.getUserId();
                System.out.println("Login Response is null ");
            } else {
                System.out.println("Login Response is null ");
            }
        } else {
            System.out.println("Fragment is visble not isVisibleToUser ");

        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void getVisisVleViews(View view) {
        submit_button = view.findViewById(R.id.submit_button);
        loadinview = view.findViewById(R.id.loadinview1);
        ft_view = view.findViewById(R.id.ft_view);
        inch_view = view.findViewById(R.id.inch_view);
        age_view = view.findViewById(R.id.age_view);
        weight_view = view.findViewById(R.id.weight_view);
        height_one_txt = view.findViewById(R.id.height_one_txt);
        height_two_txt = view.findViewById(R.id.height_two_txt);
        age_txt = view.findViewById(R.id.age_txt);
        weight_txt = view.findViewById(R.id.weight_txt);
        height_one_txt.setText("5");
        height_two_txt.setText("5");
        age_txt.setText("15");
        weight_txt.setText("20");
    }

    @Override
    public boolean canGoForward() {
        return nextViewenable;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ft_view:
                String ft = height_one_txt.getText().toString();
                showCustomDaialog("Ft", height_one_txt, 12, 0, Integer.parseInt(ft));
                break;
            case R.id.inch_view:
                String inch = height_two_txt.getText().toString();
                showCustomDaialog("Inch", height_two_txt, 11, 0, Integer.parseInt(inch));

                break;
            case R.id.age_view:
                String age = age_txt.getText().toString();
                showCustomDaialog("Age", age_txt, 110, 0, Integer.parseInt(age));
                break;
            case R.id.weight_view:
                String weight = weight_txt.getText().toString();
                showCustomDaialog("Weight", weight_txt, 200, 0, Integer.parseInt(weight));
                break;
            case R.id.submit_button:
                setWAH();
                break;
        }

    }

    private void showCustomDaialog(String title, AppCompatTextView settext, int maxValue, int minValue, int setvale) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog custmdialo = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.fancygifdialog, null);
        NumberPicker number_picker = dialogLayout.findViewById(R.id.number_picker);
        Button negativeBtn = dialogLayout.findViewById(R.id.negativeBtn);
        negativeBtn.setText("Cancel");
        Button positiveBtn = dialogLayout.findViewById(R.id.positiveBtn);
        positiveBtn.setText("Set");
        number_picker.setMaxValue(maxValue);
        number_picker.setMinValue(minValue);
        number_picker.setTypeface("app_textfont.ttf");
        number_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println("oldvalue : " + oldVal + "  : new value : " + newVal);
                settext.setText(String.valueOf(newVal));
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (custmdialo != null && custmdialo.isShowing()) {
                    custmdialo.dismiss();
                }

            }
        });
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (custmdialo != null && custmdialo.isShowing()) {
                    custmdialo.dismiss();

                }

            }
        });

        builder.setTitle(title);
        // builder.setView(dialogLayout);

        builder.setCancelable(true);
        custmdialo.setView(dialogLayout);
        custmdialo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custmdialo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        custmdialo.show();
    }

    private void setWAH() {
        loadinview.setVisibility(View.VISIBLE);
        String ft = height_one_txt.getText().toString();
        String inche = height_two_txt.getText().toString();
        String weight = weight_txt.getText().toString();
        String age = age_txt.getText().toString();
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callsexUsername = ssgInterface.insertBodyInfo(Config.API_KEY, Config.APP_ID, userId, ft, inche, age, weight);
        callsexUsername.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                parserHWA(jsonObject, ft, inche, weight, age);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void parserHWA(String object, String ft, String inch, String weight, String age) {
        try {
            JSONObject mainObject = new JSONObject(object);
            if (mainObject.has("response_code")) {
                int registrationcode = mainObject.getInt("response_code");
                if (registrationcode == 500) {
                    if (mainObject.has("message")) {
                        String message = mainObject.getString("message");
                        showToastMessage(message);

                        loadinview.setVisibility(View.GONE);
                    } else {
                        showToastMessage("Api error");
                        loadinview.setVisibility(View.GONE);
                    }

                } else if (registrationcode == 200) {
                    String message = mainObject.getString("message");
                    if (message.contains("Success")) {
                        loginResponse.setAge(age);
                        loginResponse.setWeight(weight);
                        loginResponse.setHeight(ft + "-" + inch);
                        setPreferenceObject(getActivity(), loginResponse, "loginResponse");
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

    public Object getPreferenceObjectJson(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        String json = appSharedPrefs.getString("response", "nodata");
        System.out.println("login response called : " + json);
        Gson gson = new Gson();
        LoginResponse selectedUser = gson.fromJson(json, LoginResponse.class);
        return selectedUser;
    }

    public void setPreferenceObject(Context c, Object modal, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        System.out.println("json response final page :" + jsonObject);
        prefsEditor.putString("response", jsonObject);
        prefsEditor.commit();
        loadinview.setVisibility(View.GONE);
        nextViewenable = true;
        showToastMessage("Please click next");
        calledLogginResponse();

    }

    private void calledLogginResponse() {
        LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "loginResponse");
        if (loginResponse != null) {
            System.out.println("not null value");
        } else {
            System.out.println("value is null");
        }
    }
}
