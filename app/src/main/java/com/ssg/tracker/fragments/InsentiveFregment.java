package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.DashBoardView;
import com.ssg.tracker.controller.FriendsController;
import com.ssg.tracker.controller.UserController;
import com.ssg.tracker.model.ProfileModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsentiveFregment extends Fragment {

    RelativeLayout friends_click, user_click, stepinfo, coininfo, love_app;
    String userId;
    SSGInfo ssgInterface;

    ProfileModel profileObject;
    SpinKitView loadinview;

    TextView PfofileBold, username_text, useremai_text, leavel_txt, total_coins, stepvaluetext;
     TextView tv_textcount;
    public InsentiveFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insentive_view, container, false);
        getVisisbleViews(view);
        return view;
    }

    private void getVisisbleViews(View view) {
        love_app = view.findViewById(R.id.love_app);
        loadinview = view.findViewById(R.id.loadinview);
        friends_click = view.findViewById(R.id.friends_click);
        user_click = view.findViewById(R.id.user_click);
        coininfo = view.findViewById(R.id.coininfo);
        tv_textcount = view.findViewById(R.id.tv_textcount);
        stepinfo = view.findViewById(R.id.stepinfo);
        PfofileBold = view.findViewById(R.id.PfofileBold);
        username_text = view.findViewById(R.id.username_text);
        useremai_text = view.findViewById(R.id.useremai_text);
        leavel_txt = view.findViewById(R.id.leavel_txt);
        total_coins = view.findViewById(R.id.total_coins);
        stepvaluetext = view.findViewById(R.id.stepvaluetext);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("userId", "novalue");
        getProfile();
        getDashBoard();
        user_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    Intent intent = new Intent(getActivity(), UserController.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
//                    showToastMessage("Please check your internet connection");
                }

            }
        });
        friends_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    Intent intent = new Intent(getActivity(), FriendsController.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
//                    showToastMessage("Please check your internet connection");
                }
            }
        });
        coininfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    shoeLevelInfo();
                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });
        stepinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    showStpInfo();
                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });
        love_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    if(getActivity()!=null){
                        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        getCoinBonus();
                    }

                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });
    }
    private void getCoinBonus() {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callsexUsername = ssgInterface.bonusCoin(Config.API_KEY, Config.APP_ID, userId);
        callsexUsername.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());

                try {
                    JSONObject   mainObject = new JSONObject(jsonObject);
                    if (mainObject.has("response_code")) {
                        int registrationcode = mainObject.getInt("response_code");
                        if (registrationcode == 500) {
                            if (mainObject.has("data")) {
                                String datamessage = mainObject.getString("data");
                                showToastMessage(datamessage);
                                loadinview.setVisibility(View.GONE);
                                return;
                            }
                        }else{
//                            responce ok
                        }
                    }else{
                            showToastMessage("Api response code null");
                            loadinview.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("jsonObject dashboard Steps : " + jsonObject);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
    private void showStpInfo() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View dialogView = inflater.inflate(R.layout.step_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
//        builder.setPositiveButton(
//                "Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.login_back);
//        Button buttonbackground = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        if(buttonbackground!=null){
//            buttonbackground.setBackgroundColor(Color.WHITE);
//        }

        RelativeLayout close_view = dialogView.findViewById(R.id.close_view);
        close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void shoeLevelInfo() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.coin_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
//
//        builder.setPositiveButton(
//                "Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.login_back);
        RelativeLayout close_view = dialogView.findViewById(R.id.close_view);
        close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();

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
                    String ref_coin= data.getString("ref_coin");
                    tv_textcount.setText("Coins through referral "+ref_coin);
                    System.out.println("total Data : " + total_steps+"::"+total_calorie+"::"+total_coins);
                    float distance = (float) (Integer.parseInt(total_steps) * 78) / (float) 100000;
                    String totalKM = String.format("%.2f", distance);
                    gettotalsteptsvalue(total_steps);
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

    private void setValueUser(ProfileModel model) {
        String totalcoin=model.getTotal_coins();
        double dtotalcoin=Double.parseDouble(totalcoin);
        String twodecimaltotalcoin=String.format("%.2f", dtotalcoin);
        total_coins.setText(twodecimaltotalcoin);
        leavel_txt.setText(model.getUser_level());
        username_text.setText(model.getName());
        useremai_text.setText(model.getEmail());
        char first = model.getName().charAt(0);
        PfofileBold.setText(String.valueOf(first));

    }

    private void gettotalsteptsvalue(String total_steps) {
//        String steps = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("steps", "novalue");
//        if (steps.equalsIgnoreCase("novalue")) {
        double dtotal_steps=Double.parseDouble(total_steps.trim());
        if (dtotal_steps<=0.0) {
            if (stepvaluetext != null) {
                String value = "Total step taken " + "0";
                stepvaluetext.setText(value);
            }

        } else {
            if (stepvaluetext != null) {
                String value2 = "Total step taken " + total_steps;
                stepvaluetext.setText(value2);
            }

        }
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible&&getActivity()!=null) {
            //Do your stuff here
            if(isNetworkConnected()){
                userId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("userId", "novalue");
                getProfile();
                getDashBoard();
            }else{
                Intent intent = new Intent(getActivity(), DashBoardView.class);
                startActivity(intent);
                getActivity().finish();
//                showToastMessage("Please check your internet connection");
            }

        }

        super.setMenuVisibility(visible);
    }
    //    check Intetnet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
