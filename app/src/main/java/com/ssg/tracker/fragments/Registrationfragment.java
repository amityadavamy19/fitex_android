package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.LoginController;
import com.ssg.tracker.controller.UserController;
import com.ssg.tracker.library.IntroActivity;
import com.ssg.tracker.library.SlideFragment;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.model.OtpModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registrationfragment extends SlideFragment implements View.OnClickListener,MyInterface {
    private boolean nextViewenable = false;
    AppCompatTextView text_mutliecolor;
    SpinKitView loadinview;
    RelativeLayout clickotp;
    TextInputEditText input_email, input_name, input_mobile, password_mobile,input_refral;
    SSGInfo ssgInterface;

    String name_social, email_social, isEmailVarified, is_mobileverified, mobilenumber, provider_vale, userId;

    public Registrationfragment() {

    }

    public static Registrationfragment newInstance() {
        return new Registrationfragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_view, container, false);
        getVisisbleVIes(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getIntroActivity().setInstance(this);
        setVlaueFromOld();
    }

    private void getVisisbleVIes(View view) {
        text_mutliecolor = view.findViewById(R.id.text_mutliecolor);
        loadinview = view.findViewById(R.id.loadinview);
        input_name = view.findViewById(R.id.input_name);
        input_mobile = view.findViewById(R.id.input_mobile);
        password_mobile = view.findViewById(R.id.password_mobile);
        input_email = view.findViewById(R.id.input_email);
        input_refral=view.findViewById(R.id.input_refral);
        clickotp = view.findViewById(R.id.clickotp);
        clickotp.setOnClickListener(this);
        setMultiColorText();
    }

    private void setMultiColorText() {
        Spannable word = new SpannableString("Get verification code on your");
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#0FD5D0")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        word.setSpan(new RelativeSizeSpan(1f), 0, word.length(), 0);
        text_mutliecolor.setText(word);
        Spannable wordTwo = new SpannableString(" Mobile Number");
        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#7B87A5")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_mutliecolor.append(wordTwo);
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
            case R.id.clickotp:
                String useremail = input_email.getText().toString();
                String username = input_name.getText().toString();
                String mobile = input_mobile.getText().toString();
                String password = password_mobile.getText().toString();
                String referalCode=input_refral.getText().toString().trim();
                loadinview.setVisibility(View.VISIBLE);
                Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                if (useremail == null || useremail.isEmpty()) {
                    showToastMessage("Please enter email");
                    loadinview.setVisibility(View.GONE);
                    input_email.startAnimation(shake);
                    hideKeyBoard();
                    return;

                } else if (username == null || username.isEmpty()) {
                    showToastMessage("Please enter mobile name");
                    loadinview.setVisibility(View.GONE);
                    input_name.startAnimation(shake);
                    hideKeyBoard();
                    return;
                } else if (mobile == null || mobile.isEmpty()) {
                    showToastMessage("Please enter mobile number");
                    loadinview.setVisibility(View.GONE);
                    input_mobile.startAnimation(shake);
                    hideKeyBoard();
                    return;
                } else if (password == null || password.isEmpty()) {
                    showToastMessage("Please enter password");
                    loadinview.setVisibility(View.GONE);
                    password_mobile.startAnimation(shake);
                    hideKeyBoard();
                    return;
                } else {
                    hideKeyBoard();
                    if (isEmailVarified == null) {
                        System.out.println("santi called 4");
                        registeredEmailPassword(useremail, password, username, mobile,referalCode);

                    } else {
                        if (isEmailVarified.contains("false")) {
                            System.out.println("santi called 3");
                            registeredEmailPassword(useremail, password, username, mobile,referalCode);
                        } else {
                            if (isEmailVarified != null && is_mobileverified.contains("false")) {
                                // load otp view
                                System.out.println("santi called 2");
                                registeredEmailPassword(useremail, password, username, mobile,referalCode);

                            } else if (is_mobileverified != null && is_mobileverified.contains("true")) {
                                mobilenumber = input_mobile.getText().toString();
                                loaOtpResponse(mobilenumber);
                            } else {
                                nextViewenable = true;
                            }
                        }
                    }

                }


                break;

        }
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void registeredEmailPassword(String userEmail, String password, String username, String mobile,String referalCode) {
        System.out.println("santi called 5 ");
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callregistrationmeail;
        System.out.println("provider : " + provider_vale);
        if (provider_vale != null && !provider_vale.equals("null") && provider_vale.contains("Facebook") || provider_vale.contains("Google")) {
            System.out.println("santi called dorRgisterSocialMobile");
            callregistrationmeail = ssgInterface.dorRgisterSocialMobile(Config.API_KEY, Config.APP_ID, userEmail, password, username, mobile, userId,referalCode);
        }
        else {
            System.out.println("santi called dorRgisterNameMobile");
            callregistrationmeail = ssgInterface.dorRgisterNameMobile(Config.API_KEY, Config.APP_ID, userEmail, password, username, mobile,referalCode);
        }

        callregistrationmeail.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                parseregistrationView(jsonObject);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void parseregistrationView(String object) {
        try {
            JSONObject mainObject = new JSONObject(object);
            if (mainObject.has("response_code")) {
                int registrationcode = mainObject.getInt("response_code");
                if (registrationcode == 500) {
                    if (mainObject.has("message")) {

                        String datamessage = mainObject.getString("message");
                        showToastMessage(datamessage);
                        loadinview.setVisibility(View.GONE);
                        return;
                    } else {
                        showToastMessage("Api error");
                        loadinview.setVisibility(View.GONE);
                        return;
                    }

                } else if (registrationcode == 200) {
                    // sout parse data here for registration
                    System.out.println("parse data here");
                    loadinview.setVisibility(View.GONE);
                    JSONArray dataarray = mainObject.getJSONArray("data");
                    for (int i = 0; i < dataarray.length(); i++) {

                        JSONObject mainobjec = dataarray.getJSONObject(i);
                        System.out.println("mainobjec : " + mainobjec.toString());
                        Gson gson = new Gson();
                        LoginResponse loginResponse = gson.fromJson(mainobjec.toString(), LoginResponse.class);
                        setPreferenceObject(getActivity(), loginResponse, "response");

                    }
                }
            } else {
                showToastMessage("Api response code null");
                loadinview.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

    public String getPreferenceValue(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        String json = appSharedPrefs.getString(key, "nodata");
        return json;
    }

    private void setVlaueFromOld() {
        LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
        if (loginResponse != null) {
            System.out.println("Login Response is null ");
            name_social = loginResponse.getName();
            email_social = loginResponse.getEmail();
            mobilenumber = loginResponse.getMobile();
            userId = loginResponse.getUserId();
            System.out.println("santi registration userid : " + userId);
            String emailv = loginResponse.getEmail_verified();
            String mobilev = loginResponse.getMobile_verified();
            provider_vale = loginResponse.getProvider();
            if (name_social != null && !name_social.isEmpty()) {
                input_name.setText(name_social);
            }
            if (email_social != null && !email_social.isEmpty()) {
                input_email.setText(email_social);
            }
            if (mobilenumber != null && !mobilenumber.isEmpty()) {
                input_mobile.setText(mobilenumber);
            }
            if (emailv != null && !emailv.isEmpty()) {
                isEmailVarified = emailv;
            } else {
                isEmailVarified = null;
            }
            if (mobilev != null && !mobilev.isEmpty()) {
                is_mobileverified = mobilev;
            } else {
                is_mobileverified = null;
            }
        } else {
            provider_vale = "null";
            System.out.println("Login Response is null ");
        }
    }

    public Object getPreferenceObjectJson(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        String json = appSharedPrefs.getString("response", "nodata");
        System.out.println("santi registrantion josn : " + json);
        if (json.contains("nodata")) {
            return null;
        } else {
            Gson gson = new Gson();
            LoginResponse selectedUser = gson.fromJson(json, LoginResponse.class);
            return selectedUser;
        }

    }

    private void loaOtpResponse(String mobileNumber) {
        System.out.println("santi called 1 :" + mobileNumber);
        loadinview.setVisibility(View.VISIBLE);
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callsexUsername = ssgInterface.getOtpAgain(Config.API_KEY, Config.APP_ID, mobileNumber);
        callsexUsername.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                OtpModel selectedUser = gson.fromJson(jsonObject, OtpModel.class);
                if (selectedUser.getResponse_code().contains("200")) {
                    LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
                    loginResponse.setOtp(selectedUser.getOtp());
                    String mobile = input_mobile.getText().toString();
                    String password = password_mobile.getText().toString();
                    System.out.println("Mobile number : " + mobileNumber);
                    loginResponse.setMobile(mobile);
                    loginResponse.setPassword(password);
                    setPreferenceObject(getActivity(), loginResponse, "response");
                } else {
                    loadinview.setVisibility(View.GONE);
                    showToastMessage("Error getting Otp");
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println("onFailure : "+t.getMessage());
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(getActivity()!=null){
                getIntroActivity().setInstance(Registrationfragment.this);
            }
        }else{

            nextViewenable = false;
        }
    }

    @Override
    public void prevClick() {
        System.out.println("Click prevClick: ");
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
//               getIntroActivity());
//        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        prefsEditor.putString("response", null);
//        prefsEditor.commit();
//        PreferenceManager.getDefaultSharedPreferences(getIntroActivity()).edit().putString("userId", "").apply();
//        if(getActivity()!=null){
//            getIntroActivity().setInstance(null);
//        }
    }

    @Override
    public void nextClick() {
        System.out.println("Click nextClick: ");
     if(nextViewenable){
         is_mobileverified="false";
         if(getActivity()!=null){
             getIntroActivity().setInstance(null);
         }
     }else{
         showToastMessage("Please click(Get verification code on your Mobile)");
     }
    }

}
