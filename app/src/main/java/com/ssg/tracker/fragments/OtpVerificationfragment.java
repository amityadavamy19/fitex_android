package com.ssg.tracker.fragments;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.LoginController;
import com.ssg.tracker.controller.UserController;
import com.ssg.tracker.library.IntroActivity;
import com.ssg.tracker.library.OnNavigationBlockedListener;
import com.ssg.tracker.library.SlideFragment;
import com.ssg.tracker.model.LoginResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

public class OtpVerificationfragment extends SlideFragment implements View.OnClickListener,
        OnOtpCompletionListener,MyInterface {
    private boolean nextVnew = false;
    AppCompatTextView text_info_multi, otp_info;
    String mobile_number = "9898989898";
    LoginResponse loginResponse;
    OtpView otp_view_sms;
    String mobile_otp, ismobileverified;
    String vtext = "Mobile Verification has successfully done!";
    AppCompatTextView verification_done;

    public OtpVerificationfragment() {
        // Required empty public constructor
    }

    public static OtpVerificationfragment newInstance() {
        return new OtpVerificationfragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_veryfication_view, container, false);
        verification_done = view.findViewById(R.id.verification_done);
        getVisisbleVIes(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        nextVnew = false;
        LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
        if (loginResponse != null) {
            mobile_number = loginResponse.getMobile();
            mobile_otp = loginResponse.getOtp();
            System.out.println("Login Response is null ");
        } else {
            System.out.println("Login Response is null ");
        }

        setMultiColorText1();
        setMultiColorText();
        otp_view_sms.setOtpCompletionListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            otp_view_sms.setText("");
            otp_view_sms.clearComposingText();
            otp_view_sms.clearFocus();
            if(getActivity()!=null){
                getIntroActivity().setInstance(OtpVerificationfragment.this);
            }

            System.out.println("Fragment is visble isVisibleToUser ");
            LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
            if (loginResponse != null) {
                mobile_number = loginResponse.getMobile();
                mobile_otp = loginResponse.getOtp();
                setMultiColorText1();
                ismobileverified = loginResponse.getMobile_verified();
//                if (ismobileverified != null && ismobileverified.contains("true")) {
//////                    nextVnew = true;
////                    showToastMessage("Please click next");
////                }
                System.out.println("Login Response is not null ");
            } else {
                System.out.println("Login Response is null ");
            }
        } else {
//            if(getActivity()!=null){
//                getIntroActivity().setInstance(null);
//            }

            nextVnew=false;
            System.out.println("Fragment is visble not isVisibleToUser ");

        }

    }

    private void getVisisbleVIes(View view) {
        text_info_multi = view.findViewById(R.id.text_info_multi);
        otp_info = view.findViewById(R.id.otp_info);
        otp_view_sms = view.findViewById(R.id.otp_view_sms);


     view.setOnKeyListener(new View.OnKeyListener() {
         @Override
         public boolean onKey(View v, int keyCode, KeyEvent event) {
             System.out.println("onKey ");
             return false;
         }
     });
    }

    private void setMultiColorText1() {
        Spannable word = new SpannableString("We have send the Otp on");
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#D5D4DD")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        otp_info.setText(word);
        Spannable wordTwo = new SpannableString(" " + mobile_number);
        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#31C3CE")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        otp_info.append(wordTwo);

        Spannable wordThree = new SpannableString(" will apply auto to the field");
        wordThree.setSpan(new ForegroundColorSpan(Color.parseColor("#D5D4DD")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        otp_info.append(wordThree);
    }

    private void setMultiColorText() {
        Spannable word = new SpannableString("Enter your");
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#D5D4DD")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_info_multi.setText(word);
        Spannable wordTwo = new SpannableString(" 5 Digit");
        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#31C3CE")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_info_multi.append(wordTwo);
        Spannable wordthree = new SpannableString(" Verification Code");
        wordthree.setSpan(new ForegroundColorSpan(Color.parseColor("#D5D4DD")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_info_multi.append(wordthree);
    }

    @Override
    public void updateNavigation() {
        System.out.println("updateNavigation : ");
        super.updateNavigation();
    }



    @Override
    public void onDestroy() {

        super.onDestroy();
    }



    @Override
    public boolean canGoForward() {
        return nextVnew;
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.otp_view_sms:
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;
        }
    }

    @Override
    public void onOtpCompleted(String otp) {
        if (otp.contains(mobile_otp)) {
            nextVnew = true;
            verification_done.setText(vtext);
            showToastMessage("Otp validation done please click next");

        } else {
            showToastMessage("Otp not valid");
        }
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void prevClick() {
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
//                getIntroActivity());
//        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        prefsEditor.putString("response", null);
//        prefsEditor.commit();
//        PreferenceManager.getDefaultSharedPreferences(getIntroActivity()).edit().putString("userId", "").apply();
        System.out.println("Click prevClickotp :");
    }

    @Override
    public void nextClick() {
          if(nextVnew){

          }else{
              showToastMessage("Please enter otp");
          }
        System.out.println("Click nextClickotp :");
    }



}
