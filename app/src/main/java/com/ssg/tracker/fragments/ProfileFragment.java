package com.ssg.tracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.DashBoardView;
import com.ssg.tracker.controller.LoginController;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.model.ProfileModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    TextView PfofileBold, leavel_txt, total_coins, age_txt, weight_txt, height_txt, cal_txt, step_txt, km_txt, username_text, useremai_text;
    SSGInfo ssgInterface;
    ProfileModel profileObject;
    SpinKitView loadinview;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);
        getVisisbleViews(view);
        return view;
    }

    private void getVisisbleViews(View view) {
        username_text = view.findViewById(R.id.username_text);
        useremai_text = view.findViewById(R.id.useremai_text);
        PfofileBold = view.findViewById(R.id.PfofileBold);
        leavel_txt = view.findViewById(R.id.leavel_txt);
        total_coins = view.findViewById(R.id.total_coins);
        age_txt = view.findViewById(R.id.age_txt);
        weight_txt = view.findViewById(R.id.weight_txt);
        height_txt = view.findViewById(R.id.height_txt);
        cal_txt = view.findViewById(R.id.cal_txt);
        step_txt = view.findViewById(R.id.step_txt);
        km_txt = view.findViewById(R.id.km_txt);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // setValueUser();
        getCoinHistory();
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
            if(model.getUser_level()!=null){
                leavel_txt.setText(model.getUser_level());
            }
            if(model.getTotal_coins()!=null){
                total_coins.setText(model.getTotal_coins());
            }
        } else {
            System.out.println("login response ins null");
        }

    }

    private void getCoinHistory() {

        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getProfile(Config.API_KEY, Config.APP_ID, "70");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
