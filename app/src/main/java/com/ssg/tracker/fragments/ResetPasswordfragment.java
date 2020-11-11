package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.LoginController;
import com.ssg.tracker.library.SlideFragment;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordfragment extends SlideFragment implements View.OnClickListener,
        OnOtpCompletionListener {
    private boolean nextVnew = false;
    AppCompatTextView text_info_multi, otp_info;
    String mobile_number = "9898989898";
    LoginResponse loginResponse;
    OtpView otp_view_sms;
    String mobile_otp, ismobileverified;
    String vtext = "Mobile Verification has successfully done!";
    AppCompatTextView verification_done;
    //    add pref for today value
    public static String RESETPASS_VALUES = "RESETPASS_VALUES";
    private SharedPreferences msharedprefResetValues;
    private TextInputEditText input_newpassword,input_newpasswordagain;
     FancyButton reset_button;
    SSGInfo ssgInterface;
    String userId;
    boolean resetsuccessfully=false;
    public ResetPasswordfragment() {
        // Required empty public constructor
    }

    public static ResetPasswordfragment newInstance() {
        return new ResetPasswordfragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_view, container, false);
        verification_done = view.findViewById(R.id.verification_done);
        getVisisbleVIes(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nextVnew = false;
        resetsuccessfully=false;
        setMultiColorText1();
        setMultiColorText();
        otp_view_sms.setOtpCompletionListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            msharedprefResetValues = getActivity().getSharedPreferences(RESETPASS_VALUES, 0);
            int  mobile_otp1= msharedprefResetValues.getInt("OTP", 0);
            mobile_otp=String.valueOf(mobile_otp1);
            String name = msharedprefResetValues.getString("NAME", null);
            String email = msharedprefResetValues.getString("EMAIL", null);
            mobile_number = msharedprefResetValues.getString("MOBILE", null);
            userId = msharedprefResetValues.getString("USERID", null);
            System.out.println("Fragment is visble isVisibleToUser ");
            setMultiColorText1();
        } else {
            System.out.println("Fragment is visble not isVisibleToUser ");

        }

    }

    private void getVisisbleVIes(View view) {
        text_info_multi = view.findViewById(R.id.text_info_multi);
        otp_info = view.findViewById(R.id.otp_info);
        otp_view_sms = view.findViewById(R.id.otp_view_sms);
        input_newpassword=view.findViewById(R.id.input_newpassword);
        input_newpasswordagain=view.findViewById(R.id.input_newpasswordagain);
        reset_button=view.findViewById(R.id.reset_button);
        reset_button.setCustomTextFont("app_textfont.ttf");
        reset_button.setText("Reset Password");

        reset_button.setOnClickListener(this);

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
            case R.id.reset_button:
                if(!resetsuccessfully){
                    if(input_newpassword!=null&&input_newpasswordagain!=null){
                        String newPassword=input_newpassword.getText().toString().trim();
                        String newPasswordagain=input_newpasswordagain.getText().toString().trim();
                        String otp=otp_view_sms.getText().toString().trim();
                        if(!newPassword.isEmpty()&& newPassword.length()>1){
                            if(!newPasswordagain.isEmpty()&& newPasswordagain.length()>1){
                                if(newPassword.equalsIgnoreCase(newPasswordagain)){
                                    if(otp_view_sms!=null){
                                        if(otp.contains(mobile_otp)){
                                            resetPass(userId,newPassword);

                                        }else{
                                            showToastMessage("Otp not valid");
                                        }
                                    }

                                }else{
                                    showToastMessage("Both password Missmatch!");
                                }

                            }else{
                                showToastMessage("Please re-enter new password");
                            }

                        }else{
                            showToastMessage("Please enter new password");
                        }
                    }
                }else{
                    msharedprefResetValues = getActivity().getSharedPreferences(RESETPASS_VALUES, 0);
                    SharedPreferences.Editor todayvalueEditor = msharedprefResetValues.edit();
                    todayvalueEditor.clear();
                    todayvalueEditor.commit();
                    Intent intent=new Intent(getActivity(), LoginController.class);
                    startActivity(intent);
                    getActivity().finish();
                }

                break;

        }
    }

    @Override
    public void onOtpCompleted(String otp) {
        System.out.println("otp :"+otp);
//        if (otp.contains(mobile_otp)) {
//            nextVnew = true;
//            verification_done.setText(vtext);
//            showToastMessage("Otp validation done please click next");

//        } else {
//            showToastMessage("Otp not valid");
//        }
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
//    For reset Password
private void resetPass(String userId,String password) {
    System.out.println("santi called 5 ");
    ssgInterface = APIClient.getClient().create(SSGInfo.class);
    Call<JsonElement> callregistrationmeail;
    callregistrationmeail = ssgInterface.resetPass(Config.API_KEY, Config.APP_ID, userId,password );
    callregistrationmeail.enqueue(new Callback<JsonElement>() {
        @Override
        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            //System.out.println("Response body: " + response.body());
            Gson gson = new Gson();
            String jsonObject = gson.toJson(response.body());
            System.out.println("jsonObject : " + jsonObject);
            parseResetPassword(jsonObject);
        }

        @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {

        }
    });

}
    private void parseResetPassword(String object) {
        try {
            JSONObject mainObject = new JSONObject(object);
            if (mainObject.has("response_code")) {
                int registrationcode = mainObject.getInt("response_code");
                if (registrationcode == 500) {
                    if (mainObject.has("message")) {
                        String datamessage = mainObject.getString("message");
                        showToastMessage(datamessage);
//                        loadinview.setVisibility(View.GONE);
                        return;
                    }
                    else {
                        showToastMessage("Api error");
//                        loadinview.setVisibility(View.GONE);
                        return;
                    }

                } else if (registrationcode == 200) {
                    // sout parse data here for registration
                    JSONObject data = mainObject.getJSONObject("data");
                    verification_done.setText("Reset Password Successfully");
                    showToastMessage("Reset Password Successfully");
                    reset_button.setText("Back To Login");
                    resetsuccessfully=true;
                }
            } else {
                showToastMessage("Api response code null");
//                loadinview.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
