package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.CoinHistory;
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
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinFragment extends Fragment {

    RelativeLayout friends_click, user_click;
    RelativeLayout transaction, spencoin;
    String userId;
    SSGInfo ssgInterface;

    ProfileModel profileObject;
    SpinKitView loadinview;

    TextView total_steps;

    public CoinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coinview, container, false);
        getVisisbleViews(view);
        return view;
    }

    private void getVisisbleViews(View view) {
        friends_click = view.findViewById(R.id.friends_click);
        user_click = view.findViewById(R.id.user_click);
        transaction = view.findViewById(R.id.transaction);
        spencoin = view.findViewById(R.id.spencoin);
        loadinview = view.findViewById(R.id.loadinview);
        total_steps = view.findViewById(R.id.total_steps);
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
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    Intent intent = new Intent(getActivity(), CoinHistory.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
//                    showToastMessage("Please check your internet connection");
                }

            }
        });
        spencoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardView.viewPager.setCurrentItem(2);
            }
        });
    }
//    for total values of steps coin cal
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
                    setValueUser(total_coins);
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
//            setValueUser(profileObject);
            loadinview.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setValueUser(String total_coins) {
//        String totalcoin=model.getTotal_coins();
        double dtotalcoin=Double.parseDouble(total_coins);
        String twodecimaltotalcoin=String.format("%.2f", dtotalcoin);
        total_steps.setText(twodecimaltotalcoin);
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
//                finish();
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