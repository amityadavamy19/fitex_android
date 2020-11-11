package com.ssg.tracker.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.shawnlin.numberpicker.NumberPicker;
import com.ssg.tracker.R;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.model.ProfileModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController extends AppCompatActivity {
    ImageView close_layout;
    TextView PfofileBold, leavel_txt, total_coins, age_txt, weight_txt, height_txt, cal_txt, step_txt, km_txt, username_text, useremai_text;
    SSGInfo ssgInterface;
    ProfileModel profileObject;
    SpinKitView loadinview;
    RelativeLayout age_click, weight_click, heigt_click;
    Integer valurfor = 0;
    String userId;
    String inch1 = "0";
    RelativeLayout logout_click;
   TextView query_email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        userId = PreferenceManager.getDefaultSharedPreferences(UserController.this).getString("userId", "novalue");

        getVisisbleViews();
        loadinview.setVisibility(View.VISIBLE);
        getProfile();
        getDashBoard();
    }

    private void getVisisbleViews() {
        logout_click = findViewById(R.id.logout_click);

        loadinview = findViewById(R.id.loadinview);
        close_layout = findViewById(R.id.close_layout);
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        username_text = findViewById(R.id.username_text);
        useremai_text = findViewById(R.id.useremai_text);
        PfofileBold = findViewById(R.id.PfofileBold);
        leavel_txt = findViewById(R.id.leavel_txt);
        total_coins = findViewById(R.id.total_coins);
        age_txt = findViewById(R.id.age_txt);
        weight_txt = findViewById(R.id.weight_txt);
        height_txt = findViewById(R.id.height_txt);
        cal_txt = findViewById(R.id.cal_txt);
        step_txt = findViewById(R.id.step_txt);
        km_txt = findViewById(R.id.km_txt);
        age_click = findViewById(R.id.age_click);
        weight_click = findViewById(R.id.weight_click);
        heigt_click = findViewById(R.id.heigt_click);
        query_email=findViewById(R.id.query_email);

        query_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"support@fitex.co.in"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
        age_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDaialog("Age", 100, 10, Integer.parseInt(profileObject.getAge()));

            }
        });
        weight_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDaialog("Weight", 500, 30, Integer.parseInt(profileObject.getWeight()));
            }
        });
        heigt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heightvalue = profileObject.getHeight();
                String[] separated = heightvalue.split("-");
                String feet = separated[0];
                String inch = separated[1];
                showCustomDaialog("Ft", 12, 0, Integer.parseInt(feet));
            }
        });
        logout_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                prefsEditor.putString("response", null);
                prefsEditor.commit();
                PreferenceManager.getDefaultSharedPreferences(UserController.this).edit().putString("userId", "").apply();
                Intent intent = new Intent(UserController.this, LoginController.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
//        gettotalsteptsvalue();
//        settextKM();
    }


    public Object getPreferenceObjectJson(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        /**** get user data    *****/
        String json = appSharedPrefs.getString(key, "nodata");
        System.out.println("getting no data json" + json);
        if (json.contains("nodata")) {
            return null;
        }
        Gson gson = new Gson();
        LoginResponse selectedUser = gson.fromJson(json, LoginResponse.class);
        return selectedUser;
    }

    private void setValueUser(ProfileModel model) {
        //LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "loginResponse");
        if (model != null) {
            if (model.getAge() != null) {
                age_txt.setText(model.getAge());
            }
            if (model.getHeight() != null) {
                height_txt.setText(model.getHeight());

            }
            if (model.getWeight() != null) {
                weight_txt.setText(model.getWeight());

            }
            if (model.getName() != null) {
                username_text.setText(model.getName());
                char first = model.getName().charAt(0);
                PfofileBold.setText(String.valueOf(first));
            }
            if (model.getEmail() != null) {
                useremai_text.setText(model.getEmail());
            }
            if (model.getUser_level() != null) {
                leavel_txt.setText(model.getUser_level());
            }
            if (model.getTotal_coins() != null) {
//                for two decimal place
                String totalcoin=model.getTotal_coins();
                double dtotalcoin=Double.parseDouble(totalcoin);
                String twodecimaltotalcoin=String.format("%.2f", dtotalcoin);
                total_coins.setText(twodecimaltotalcoin);

            }
        } else {
            System.out.println("login response ins null");
        }

    }
    private void getDashBoard() {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getDashboard(Config.API_KEY, Config.APP_ID, userId);
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                try {
                    Gson gson = new Gson();
                    String jsonObject = gson.toJson(response.body());
                    JSONObject mainObjectdata = null;
                    mainObjectdata = new JSONObject(jsonObject);
                    JSONObject data = mainObjectdata.getJSONObject("data");
                    String total_steps= data.getString("total_steps");
                    String total_calorie= data.getString("total_calorie");
                    String total_coins= data.getString("total_coins");
                    System.out.println("total Data : " + total_steps+"::"+total_calorie+"::"+total_coins);
                    float distance = (float) (Integer.parseInt(total_steps) * 78) / (float) 100000;
                    String totalKM = String.format("%.2f", distance);
                    gettotalsteptsvalue(total_steps);
                    settextCal(total_calorie);
                    settextKM(totalKM);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                parseCoinObject(jsonObject);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }
    private void getProfile() {

        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getProfile(Config.API_KEY, Config.APP_ID, userId);
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                parseCoinObject(jsonObject);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }

    private void parseCoinObject(String object) {
        try {
            JSONObject mainObjectdata = new JSONObject(object);
            JSONObject data = mainObjectdata.getJSONObject("data");
            Gson gson = new Gson();

            profileObject = gson.fromJson(data.toString(), ProfileModel.class);
            setValueUser(profileObject);
            loadinview.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void updateProfile(String userIdold, String feet, String inch, String weight, String age) {
        loadinview.setVisibility(View.VISIBLE);
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.updateProfile(Config.API_KEY, Config.APP_ID, userId, feet, inch, weight, age);
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                updateVIew(feet, inch, weight, age, jsonObject);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println("santi called error now ");
            }
        });


    }

    private void updateVIew(String feet, String inch, String weight, String age, String Response) {
        loadinview.setVisibility(View.GONE);

        weight_txt.setText(weight);
        age_txt.setText(age);
        height_txt.setText(feet + "-" + inch);

        profileObject.setWeight(weight);
        profileObject.setAge(age);
        profileObject.setHeight(feet + "-" + inch);


    }

    private void showCustomDaialog(String title, int maxValue, int minValue, int setvalue) {
        System.out.println("santi value of weight : " + setvalue);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserController.this);
        AlertDialog custmdialo = builder.create();
        builder.setTitle(title);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.fancygifdialog, null);
        NumberPicker number_picker = dialogLayout.findViewById(R.id.number_picker);
        Button negativeBtn = dialogLayout.findViewById(R.id.negativeBtn);
        negativeBtn.setText("Cancel");
        Button positiveBtn = dialogLayout.findViewById(R.id.positiveBtn);
        positiveBtn.setText("Set");
        number_picker.setMaxValue(maxValue);
        number_picker.setMinValue(minValue);
        number_picker.setValue(setvalue);
        number_picker.setTypeface("app_textfont.ttf");
        number_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println("oldvalue : " + oldVal + "  : new value : " + newVal);
                valurfor = newVal;

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
                    setvaluefor(title, String.valueOf(valurfor));

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

    private void setvaluefor(String title, String text) {
        System.out.println("vale for title ");
        switch (title) {
            case "Ft":
                inch1 = "";
                String heightvalue = profileObject.getHeight();
                String[] separated = heightvalue.split("-");
                String feetinch1 = separated[0];
                inch1 = text;
                //updateProfile("", text, inch, profileObject.getWeight(), profileObject.getAge());
                showCustomDaialog("Inch", 11, 0, Integer.parseInt(feetinch1));
                break;
            case "Inch":
//                String heightvalue1 = profileObject.getHeight();
//                String[] separated1 = heightvalue1.split("-");
//                String feet1 = separated1[0];
//                String inch1 = separated1[1];
                updateProfile("", inch1, text, profileObject.getWeight(), profileObject.getAge());
                break;
            case "Weight":
                String heightvalue2 = profileObject.getHeight();
                String[] separated2 = heightvalue2.split("-");
                String feet2 = separated2[0];
                String inch2 = separated2[1];
                updateProfile("", feet2, inch2, text, profileObject.getAge());
                break;
            case "Age":

                String heightvalue3 = profileObject.getHeight();
                String[] separated3 = heightvalue3.split("-");
                String feet3 = separated3[0];
                String inch3 = separated3[1];
                updateProfile("", feet3, inch3, profileObject.getWeight(), text);

                break;
        }
    }

//    private void gettotalsteptsvalue() {
//        String steps = PreferenceManager.getDefaultSharedPreferences(UserController.this).getString("steps", "novalue");
//        if (steps.equalsIgnoreCase("novalue")) {
//            if (step_txt != null) {
//                String value = "0";
//                step_txt.setText(value);
//            }
//
//        } else {
//            if (step_txt != null) {
//                String value2 = steps;
//                step_txt.setText(value2);
//            }
//
//        }
//    }
    private void gettotalsteptsvalue(String total_steps) {
//        String steps = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("steps", "novalue");
//        if (steps.equalsIgnoreCase("novalue")) {
        double dtotal_steps=Double.parseDouble(total_steps.trim());

        if (dtotal_steps<=0.0) {
            if (step_txt != null) {
                String value ="0";
                step_txt.setText(value);
            }

        } else {
            if (step_txt != null) {

                String value2 = total_steps;
                step_txt.setText(value2);
            }

        }
    }
    private void settextCal(String total_calorie) {
//        String cal = PreferenceManager.getDefaultSharedPreferences(UserController.this).getString("cal", "novalue");
        double dtotal_calorie=Double.parseDouble(total_calorie.trim());
        if (dtotal_calorie<=0.0) {
            if (step_txt != null) {
                cal_txt.setText("0.0");
            }
        } else {

            if (cal_txt != null) {
                String value2 = total_calorie;
                cal_txt.setText(value2);
            }

        }
    }

    private void settextKM(String totalKM) {
//        String totalKM = PreferenceManager.getDefaultSharedPreferences(UserController.this).getString("totalKM", "novalue");
        double dtotal_calorie=Double.parseDouble(totalKM.trim());
        if (dtotal_calorie<=0.0) {
            if (km_txt != null) {
                km_txt.setText("0.0");
            }
        } else {

            if (km_txt != null) {
                String value2 = totalKM.trim();
                km_txt.setText(value2);
            }

        }
    }
}
