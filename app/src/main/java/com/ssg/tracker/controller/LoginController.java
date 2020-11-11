package com.ssg.tracker.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;
import com.ssg.tracker.R;
import com.ssg.tracker.model.LoginBody;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController extends AppCompatActivity implements View.OnClickListener {
    FancyButton login_button, fb_btn, google_btn;
    AppCompatTextView signup_click,forget_password;
    int REQUEST_CODE_INTRO = 1;
    SSGInfo ssgInterface;
    TextInputEditText input_name, input_password;
    SpinKitView loadinview;
    public static final String TAG = "permmition";
    private static final int OUR_REQUEST_CODE = 49404;
    private GoogleApiClient mGoogleApiClient;
    String email_social, name_social, user_fb_id;
    private static final  int PERMISSION_REQUEST_ACTIVITY_RECOGNITION=1002;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_view);
        //falseLogin();
        askForActivityPermission();

    }
    public void askForActivityPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d(TAG, "askForActivityPermission: q or greater");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                getVisibleViews();
                LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(LoginController.this, "loginResponse");
                if (loginResponse != null) {
                    int age = Integer.parseInt(loginResponse.getAge());
                    int weight = Integer.parseInt(loginResponse.getAge());
                    String gender = loginResponse.getGender();
                    boolean isfarward = false;
                    if (gender != null) {
                        if (!gender.isEmpty()) {
                            isfarward = true;
                        } else {
                            isfarward = false;
                        }
                    } else {
                        isfarward = false;
                    }

                    System.out.println("Json vale : " + age + " : " + weight + " : " + isfarward);
                    if (loginResponse.getUserId() != null && !loginResponse.getEmail().isEmpty() && age != 0 && weight != 0 && isfarward == true) {

                        PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", loginResponse.getUserId()).apply();

                        fireDashBoard();
                    } else {
                        // show registration
                        if (loginResponse.getUserId() != null) {
                            email_social = loginResponse.getEmail();
                            name_social = loginResponse.getName();
                            if (age == 0 || weight == 0 || isfarward) {
                                setPreferenceValue(LoginController.this);
                                setPreferenceObjectRegistration(LoginController.this, loginResponse, "response");
                            }
                        }

                    }
                } else {
                    System.out.println("getting no data json null");
                }
                getVisibleViews();
                createObjectsView();
                loadKeyHas();
            }

        } else {
            getVisibleViews();
            LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(LoginController.this, "loginResponse");
            if (loginResponse != null) {
                int age = Integer.parseInt(loginResponse.getAge());
                int weight = Integer.parseInt(loginResponse.getAge());
                String gender = loginResponse.getGender();
                boolean isfarward = false;
                if (gender != null) {
                    if (!gender.isEmpty()) {
                        isfarward = true;
                    } else {
                        isfarward = false;
                    }
                } else {
                    isfarward = false;
                }

                System.out.println("Json vale : " + age + " : " + weight + " : " + isfarward);
                if (loginResponse.getUserId() != null && !loginResponse.getEmail().isEmpty() && age != 0 && weight != 0 && isfarward == true) {

                    PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", loginResponse.getUserId()).apply();

                    fireDashBoard();
                } else {
                    // show registration
                    if (loginResponse.getUserId() != null) {
                        email_social = loginResponse.getEmail();
                        name_social = loginResponse.getName();
                        if (age == 0 || weight == 0 || isfarward) {
                            setPreferenceValue(LoginController.this);
                            setPreferenceObjectRegistration(LoginController.this, loginResponse, "response");
                        }
                    }

                }
            } else {
                System.out.println("getting no data json null");
            }
            getVisibleViews();
            createObjectsView();
            loadKeyHas();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String permissionResult = "Request code: " + requestCode + ", Permissions: " +
                Arrays.toString(permissions) + ", Results: " + Arrays.toString(grantResults);

        Log.d(TAG, "onRequestPermissionsResult(): " + permissionResult);

        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            Log.d(TAG, "onRequestPermissionsResult: permission granted?");

            getVisibleViews();
            LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(LoginController.this, "loginResponse");
            if (loginResponse != null) {
                int age = Integer.parseInt(loginResponse.getAge());
                int weight = Integer.parseInt(loginResponse.getAge());
                String gender = loginResponse.getGender();
                boolean isfarward = false;
                if (gender != null) {
                    if (!gender.isEmpty()) {
                        isfarward = true;
                    } else {
                        isfarward = false;
                    }
                } else {
                    isfarward = false;
                }

                System.out.println("Json vale : " + age + " : " + weight + " : " + isfarward);
                if (loginResponse.getUserId() != null && !loginResponse.getEmail().isEmpty() && age != 0 && weight != 0 && isfarward == true) {

                    PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", loginResponse.getUserId()).apply();

                    fireDashBoard();
                } else {
                    // show registration
                    if (loginResponse.getUserId() != null) {
                        email_social = loginResponse.getEmail();
                        name_social = loginResponse.getName();
                        if (age == 0 || weight == 0 || isfarward) {
                            setPreferenceValue(LoginController.this);
                            setPreferenceObjectRegistration(LoginController.this, loginResponse, "response");
                        }
                    }

                }
            } else {
                System.out.println("getting no data json null");
            }
            getVisibleViews();
            createObjectsView();
            loadKeyHas();
        }
    }
    private void falseLogin() {
        Intent intent = new Intent(LoginController.this, DashBoardView.class);
        startActivity(intent);
        finish();
        return;
    }

    private void loadKeyHas() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ssg.tracker",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash facebook:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void getVisibleViews() {
        login_button = findViewById(R.id.login_button);
        fb_btn = findViewById(R.id.fb_btn);
        google_btn = findViewById(R.id.google_btn);
        signup_click = findViewById(R.id.signup_click);
        forget_password=findViewById(R.id.forget_password);
        login_button.setCustomTextFont("app_textfont.ttf");
        fb_btn.setCustomTextFont("app_textfont.ttf");
        google_btn.setCustomTextFont("app_textfont.ttf");
        setMultiColorText();
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        loadinview = findViewById(R.id.loadinview);
        forget_password.setOnClickListener(this);

    }

    private void setMultiColorText() {
        Spannable word = new SpannableString("Don't have an account?");
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#E1E7EF")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signup_click.setText(word);
        Spannable wordTwo = new SpannableString(" Signup Now!");
        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#157CD1")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signup_click.append(wordTwo);
    }

    private void createObjectsView() {

        login_button.setOnClickListener(this);
        fb_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);
        signup_click.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_button:
                if( isNetworkConnected()){
                    setclearPrefrence(LoginController.this, "response");
                    hideSoftKeyboard(LoginController.this);
                    loadinview.setVisibility(View.VISIBLE);
                    String username = input_name.getText().toString();
                    String password = input_password.getText().toString();
                    if (username == null || username.isEmpty()) {
                        showToastMessage("Please enter username or email");
                        Animation shake = AnimationUtils.loadAnimation(LoginController.this, R.anim.shake);
                        input_name.startAnimation(shake);
                        loadinview.setVisibility(View.GONE);
                        return;
                    } else if (password == null || password.isEmpty()) {
                        showToastMessage("Please enter password");
                        Animation shake = AnimationUtils.loadAnimation(LoginController.this, R.anim.shake);
                        input_password.startAnimation(shake);
                        loadinview.setVisibility(View.GONE);
                        return;
                    } else {
                        doLogin(username, password);
                    }
                }else{
                    showToastMessage("Please check your internet connection");
                }

                break;


            case R.id.fb_btn:
                if( isNetworkConnected()){
                    name_social = "";
                    email_social = "";
                    setPreferenceValueBlank(LoginController.this);
                    loadinview.setVisibility(View.VISIBLE);
                    setclearPrefrence(LoginController.this, "response");
                    doFacebookLogin();
                }else{
                    showToastMessage("Please check your internet connection");
                }

                break;
            case R.id.google_btn:
                if(isNetworkConnected()){
                    name_social = "";
                    email_social = "";
                    setPreferenceValueBlank(LoginController.this);
                    loadinview.setVisibility(View.VISIBLE);
                    setclearPrefrence(LoginController.this, "response");
                    doGoogleLogin();
                }else{
                    showToastMessage("Please check your internet connection");
                }

                break;
            case R.id.signup_click:
                if( isNetworkConnected()){
                    setclearPrefrence(LoginController.this, "response");
                    loadRegistration();
                }else{
                    showToastMessage("Please check your internet connection");
                }


                break;
            case R.id.forget_password:
                if( isNetworkConnected()){
                    fireForgotPassword();
                }else{
                    showToastMessage("Please check your internet connection");
                }


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == RESULT_OK) {
                fireDashBoard();
            } else {

                finish();
            }
        } else if (requestCode == OUR_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else {
            ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void fireDashBoard() {
        Intent intent = new Intent(LoginController.this, DashBoardView.class);
        startActivity(intent);
        finish();
    }
    private void fireForgotPassword() {
        Intent intent = new Intent(LoginController.this, ForgetPasswordController.class);
        startActivity(intent);
        finish();
    }
    private void doLogin(String username, String password) {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        LoginBody body = new LoginBody(Config.APP_ID, Config.API_KEY, username, password);
        Call<JsonObject> calllogin = ssgInterface.doLoginMultipart(geresposneBody(Config.APP_ID), geresposneBody(Config.API_KEY), geresposneBody(username), geresposneBody(password));
        calllogin.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("Response : " + new Gson().toJson(response));
                String object = new Gson().toJson(response.body());
                System.out.println("final response : " + object);
                try {
                    JSONObject mainObjec = new JSONObject(object);
                    int status_code = mainObjec.getInt("response_code");
                    if (status_code == 500) {
                        showToastMessage("Username or password mismatch");
                        loadinview.setVisibility(View.GONE);
                        Animation shake = AnimationUtils.loadAnimation(LoginController.this, R.anim.shake);
                        input_password.startAnimation(shake);
                        input_name.startAnimation(shake);
                        return;
                    } else if (status_code == 200) {
                        JSONArray dataarray = mainObjec.getJSONArray("data");
                        for (int i = 0; i < dataarray.length(); i++) {

                            JSONObject mainobjec = dataarray.getJSONObject(i);
                            System.out.println("mainobjec : " + mainobjec.toString());
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(mainobjec.toString(), LoginResponse.class);
                            int age = Integer.parseInt(loginResponse.getAge());
                            int weight = Integer.parseInt(loginResponse.getAge());
                            String gender = loginResponse.getGender();
                            boolean isfarward = false;
                            if (gender != null) {
                                if (!gender.isEmpty()) {
                                    isfarward = true;
                                } else {
                                    isfarward = false;
                                }
                            } else {
                                isfarward = false;
                            }
                            if (loginResponse.getUserId() != null && !loginResponse.getEmail().isEmpty() && age != 0 && weight != 0 && isfarward == true) {
                                PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", loginResponse.getUserId()).apply();

                                setPreferenceObject(LoginController.this, loginResponse, "response");
                            } else {
                                setPreferenceValue(LoginController.this);
                                PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", loginResponse.getUserId()).apply();

                                setPreferenceObjectRegistration(LoginController.this, loginResponse, "response");
                            }



                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("object : object : " + object);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(LoginController.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private RequestBody geresposneBody(String bodypart) {
        return RequestBody.create(MediaType.parse("text/plain"), bodypart);
    }


    public void setPreferenceObject(Context c, Object modal, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        prefsEditor.putString(key, jsonObject);
        prefsEditor.commit();
        loadinview.setVisibility(View.GONE);
        fireDashBoard();

    }

    public void setPreferenceObjectRegistration(Context c, Object modal, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        prefsEditor.putString("response", jsonObject);
        prefsEditor.commit();
        loadinview.setVisibility(View.GONE);
        loadRegistration();
    }
    public void setPreferenceObjectDashboardFacebook(Context c, Object modal, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        prefsEditor.putString("response", jsonObject);
        prefsEditor.commit();
        loadinview.setVisibility(View.GONE);
        fireDashBoard();

    }
    public void setPreferenceObjectRegistrationGoogle(Context c, Object modal, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        prefsEditor.putString("response", jsonObject);
        prefsEditor.commit();
        loadinview.setVisibility(View.GONE);
        fireDashBoard();

    }
    private void setNullValue()  {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("response", "");
        prefsEditor.commit();
    }

    public void setclearPrefrence(Context c, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("response", null);
        prefsEditor.commit();
        PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", "").apply();

    }

    public static void hideSoftKeyboard(Activity mActivity) {
        try {
            View view = mActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doFacebookLogin() {
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginController.this, permissions, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException err) {
                if (err != null) {
                    loadinview.setVisibility(View.GONE);
                    System.out.println("ssgApp error facebook login : " + err.toString());

                }
                if (user == null) {
                    loadinview.setVisibility(View.GONE);
                    System.out.println("ssgApp Uh oh. The user cancelled the Facebook login.");
                    ParseUser.logOut();
                    //Uh oh. The user cancelled the Facebook login.

                } else if (user.isNew()) {
                    // User signed up and logged in through Facebook
                    System.out.println("ssgApp User signed up and logged in through Facebook");
                    getUserDetailFromFB();
                } else {
                    //"Oh, you!","Welcome back!"
                    System.out.println("ssgApp Oh, you!" + "Welcome back!");
                    getUserDetailFromFB();
                }
            }
        });
    }

    private void getUserDetailFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                System.out.println("ssgApp object : " + object);
                //ParseUser user = ParseUser.getCurrentUser();
                try {
                    if (object.has("name")) {
                        name_social = object.getString("name");
                        // user.setUsername(object.getString("name"));
                    } else {
                        name_social = "";
                    }
                    if (object.has("email")) {
                        email_social = object.getString("email");
                        //user.setEmail(object.getString("email"));
                    } else {
                        email_social = "";
                    }
                    if (object.has("id")) {
                        user_fb_id = object.getString("id");
                        //user.setObjectId(object.getString("id"));
                    } else {
                        user_fb_id = "";
                    }
                    checkFbUser();
                    //setPreferenceValue(LoginController.this);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                user.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        //"First Time Login!", "Welcome!"
//                        System.out.println("First Time Login!" + "Welcome!");
//
//                    }
//                });
            }

        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void doGoogleLogin() {
        String serverClientId = getString(R.string.google_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this /* Context */)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        System.out.println("onConnectionFailed : " + connectionResult.getErrorMessage().toString());
                        loadinview.setVisibility(View.GONE);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, OUR_REQUEST_CODE);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            email_social = account.getEmail();
            name_social = account.getDisplayName();
            System.out.println("santi called login socical : ");
            loginGoogledata();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            System.out.println("signInResult:failed code = " + e.getStatusCode());

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(LoginController.this);
            mGoogleApiClient.disconnect();
        }

    }

    private void loadRegistration() {
        loadinview.setVisibility(View.GONE);
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                LoginController.this);
        String json = appSharedPrefs.getString("name_social1", "nodata");
        System.out.println("json :daata ; " + json + " : " + name_social);
        Intent intent = new Intent(LoginController.this, UserInfoDetailController.class);
        startActivityForResult(intent, REQUEST_CODE_INTRO);
    }

    public void setPreferenceValue(Context c) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("name_social1", name_social);
        prefsEditor.putString("email_social1", email_social);
        prefsEditor.commit();
        // loadRegistration();

    }

    public void setPreferenceValueBlank(Context c) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("name_social1", name_social);
        prefsEditor.putString("email_social1", email_social);
        prefsEditor.commit();

    }

    public Object getPreferenceObjectJson(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        /**** get user data    *****/
        String json = appSharedPrefs.getString("response", "nodata");
        System.out.println("santi login controller data json" + json);
        if (json.contains("nodata")) {
            return null;
        }
        Gson gson = new Gson();
        LoginResponse selectedUser = gson.fromJson(json, LoginResponse.class);
        return selectedUser;
    }

    private void checkFbUser() {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonObject> calllogin = ssgInterface.dofbLofin(geresposneBody(Config.APP_ID), geresposneBody(Config.API_KEY), geresposneBody(user_fb_id), geresposneBody(email_social), geresposneBody(name_social));
        calllogin.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String object = new Gson().toJson(response.body());
                System.out.println("final response fb user: " + object);
                try {
                    JSONObject mainObjec = new JSONObject(object);
                    int status_code = mainObjec.getInt("response_code");
                    if (status_code == 500) {
                        showToastMessage("Username or password mismatch");
                        loadinview.setVisibility(View.GONE);
                        Animation shake = AnimationUtils.loadAnimation(LoginController.this, R.anim.shake);
                        input_password.startAnimation(shake);
                        input_name.startAnimation(shake);
                        return;
                    } else if (status_code == 200) {
                        JSONArray dataarray = mainObjec.getJSONArray("data");
                        for (int i = 0; i < dataarray.length(); i++) {

                            JSONObject mainobjec = dataarray.getJSONObject(i);
                            System.out.println("mainobjec loop fb: " + mainobjec.toString());
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(mainobjec.toString(), LoginResponse.class);
                            int age = Integer.parseInt(loginResponse.getAge());
                            int weight = Integer.parseInt(loginResponse.getWeight());
                            String gender = loginResponse.getGender();
                            boolean isfarward = false;
                            if (gender != null) {
                                if (!gender.isEmpty()) {
                                    isfarward = true;
                                } else {
                                    isfarward = false;
                                }
                            } else {
                                isfarward = true;
                            }
                            if (age == 0 || weight == 0 || isfarward==false) {
                                setPreferenceValue(LoginController.this);
                                setPreferenceObjectRegistration(LoginController.this, loginResponse, "response");
                            } else {
                                PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", loginResponse.getUserId()).apply();
                                setPreferenceValue(LoginController.this);
                                setPreferenceObjectDashboardFacebook(LoginController.this, loginResponse, "response");
                            }


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("object : object : " + object);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void loginGoogledata() {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonObject> calllogin = ssgInterface.dogoogleLofin(geresposneBody(Config.APP_ID), geresposneBody(Config.API_KEY), geresposneBody(email_social), geresposneBody(name_social));
        calllogin.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("Response google : " + new Gson().toJson(response));
                String object = new Gson().toJson(response.body());
                System.out.println("santi goole response : " + object);
                try {
                    JSONObject mainObjec = new JSONObject(object);
                    int status_code = mainObjec.getInt("response_code");
                    if (status_code == 500) {
                        showToastMessage("Username or password mismatch");
                        loadinview.setVisibility(View.GONE);
                        Animation shake = AnimationUtils.loadAnimation(LoginController.this, R.anim.shake);
                        input_password.startAnimation(shake);
                        input_name.startAnimation(shake);
                        return;
                    } else if (status_code == 200) {
                        JSONArray dataarray = mainObjec.getJSONArray("data");
                        for (int i = 0; i < dataarray.length(); i++) {

                            JSONObject mainobjec = dataarray.getJSONObject(i);
                            System.out.println("mainobjec : " + mainobjec.toString());
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(mainobjec.toString(), LoginResponse.class);
                            int age = Integer.parseInt(loginResponse.getAge());
                            int weight = Integer.parseInt(loginResponse.getAge());
                            String gender = loginResponse.getGender();
                            boolean isfarward = false;
                            if (gender != null) {
                                if (!gender.isEmpty()) {
                                    isfarward = true;
                                } else {
                                    isfarward = false;
                                }
                            } else {
                                isfarward = true;
                            }
                            if (age == 0 || weight == 0 || isfarward == false) {
                                setPreferenceValue(LoginController.this);
                                setPreferenceObjectRegistration(LoginController.this, loginResponse, "response");
                            } else {
                                PreferenceManager.getDefaultSharedPreferences(LoginController.this).edit().putString("userId", loginResponse.getUserId()).apply();

                                setPreferenceValue(LoginController.this);

                                setPreferenceObjectRegistrationGoogle(LoginController.this, loginResponse, "response");
                            }


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("object : object : " + object);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("onFailure : " + t.getMessage());
            }
        });

    }
//    check Intetnet connection
private boolean isNetworkConnected() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
}
}
