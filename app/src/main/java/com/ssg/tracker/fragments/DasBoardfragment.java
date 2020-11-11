package com.ssg.tracker.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.DashBoardView;
import com.ssg.tracker.controller.FriendsController;
import com.ssg.tracker.controller.LoginController;
import com.ssg.tracker.controller.UserController;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.model.ProfileModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DasBoardfragment extends Fragment implements RewardedVideoAdListener {
    TextView kmtext, kaltext, mintext, total_steps, todaydate, today_coins;
    String kmstring = "0.0";
    String kcalString = "0.0";
    String minString = "300";
    CircularProgressBar step_progress;
    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;

    SSGInfo ssgInterface;
    String userId, totlaStepstoday;
    SpinKitView loadinview;
    RelativeLayout friends_click, user_click, level_lay, click_refrel;
    private RewardedVideoAd mRewardedVideoAd;
    ImageView clickto_refreer;
    ProfileModel profileObject;
    //    add pref for today value
    public static String TODYAYS_VALUES = "TODYAYS_VALUES";
    private SharedPreferences msharedprefToyayvalue;
    boolean isStepfetchec
            ,isCalfetched;
    TextView couter;
    private static final  int PERMISSION_REQUEST_ACTIVITY_RECOGNITION=1002;
    View view;
    public DasBoardfragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.dashboard, container, false);
        getVisisbleViews(view);
        return view;
    }

    public void askForActivityPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d(TAG, "askForActivityPermission: q or greater");
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                System.out.println("PERMISSION_GRANTED : ");
            }
            else  if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACTIVITY_RECOGNITION)) {
                // Permission is not granted
                requestPermissions(
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
            }

            else  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                System.out.println("PERMISSION_GRANTED : not");
            }

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("onRequestPermissionsResult : "+requestCode);
        String permissionResult = "Request code: " + requestCode + ", Permissions: " +
                Arrays.toString(permissions) + ", Results: " + Arrays.toString(grantResults);

        Log.d(TAG, "onRequestPermissionsResult(): " + permissionResult);
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(getActivity(), DashBoardView.class);
                startActivity(intent);
                getActivity().finish();
                System.out.println("onRequestPermissionsResult : finish"+permissionResult);
            }else{

            }
            System.out.println("onRequestPermissionsResult : "+permissionResult);

        }else{
            System.out.println("onRequestPermissionsResult : else "+permissionResult);
        }
    }
    private void getVisisbleViews(View view) {
        clickto_refreer = view.findViewById(R.id.clickto_refreer);
        click_refrel = view.findViewById(R.id.click_refrel);
        today_coins = view.findViewById(R.id.today_coins);
        loadinview = view.findViewById(R.id.loadinview);
        kmtext = view.findViewById(R.id.kmtext);
        kaltext = view.findViewById(R.id.kaltext);
        mintext = view.findViewById(R.id.mintext);
        step_progress = view.findViewById(R.id.step_progress);
        total_steps = view.findViewById(R.id.total_steps);
        todaydate = view.findViewById(R.id.todaydate);
        friends_click = view.findViewById(R.id.friends_click);
        user_click = view.findViewById(R.id.user_click);
        level_lay = view.findViewById(R.id.level_lay);
        couter=view.findViewById(R.id.couter);
    }

    private void setTwoColorText() {
        Spannable kmword = new SpannableString(kmstring);
        kmword.setSpan(new ForegroundColorSpan(Color.parseColor("#20D0D3")), 0, kmword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kmtext.setText(kmword);
        Spannable kmword2 = new SpannableString(" Km");
        kmword2.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, kmword2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kmtext.append(kmword2);

        Spannable kcalword = new SpannableString(kcalString);
        kcalword.setSpan(new ForegroundColorSpan(Color.parseColor("#20D0D3")), 0, kcalword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kaltext.setText(kcalword);
        Spannable kcalword2 = new SpannableString(" Kcal");
        kcalword2.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, kcalword2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kaltext.append(kcalword2);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MobileAds.initialize(getActivity(), (getResources().getString(R.string.app_id)));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        String useridvalue = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("userId", "novalue");
        System.out.println("santi user id : " + useridvalue);
        createReward();
        setTwoColorText();
        setProgressValue();
        LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(getActivity(), "response");
        if (loginResponse != null) {
            userId = loginResponse.getUserId();
            System.out.println("dashboard user id : " + userId);
            if(isNetworkConnected()){
                getProfile();
                getDashBoard();
            } else{
                showToastMessage("Please check your internet connection");
            }

        }

        loadinview.setVisibility(View.VISIBLE);
        loadStepPermission();
        getCurrentdat();
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
        level_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewardVideo();
            }
        });

        click_refrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    invite(userId);
                }else{
                    showToastMessage("Please check your internet connection");
                }

//                String baseUrl = Config.deellink + userId;
//                Intent i = new Intent(android.content.Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(android.content.Intent.EXTRA_TEXT, baseUrl);
//                startActivity(Intent.createChooser(i, ""));
            }
        });
//        clickto_refreer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String baseUrl = Config.deellink + userId;
//                Intent i = new Intent(android.content.Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(android.content.Intent.EXTRA_TEXT, baseUrl);
//                startActivity(Intent.createChooser(i, ""));
//
//            }
//        });

        storedate();
    }
    private void invite(String userId) {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callregistrationmeail;
        callregistrationmeail = ssgInterface.invite(Config.API_KEY, Config.APP_ID, userId);
        callregistrationmeail.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                parsereferalData(jsonObject);
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });

    }
    private void parsereferalData(String object) {
        try {
            JSONObject mainObject = new JSONObject(object);
            if (mainObject.has("response_code")) {
                int registrationcode = mainObject.getInt("response_code");
                if (registrationcode == 500) {
                    if (mainObject.has("data")) {
                        String datamessage = mainObject.getString("data");
                        showToastMessage(datamessage);
                        loadinview.setVisibility(View.GONE);
                        return;
                    }
                    else {
                        showToastMessage("Api error");
                        loadinview.setVisibility(View.GONE);
                        return;
                    }

                } else if (registrationcode == 200) {
                    // sout parse data here for registration
                    System.out.println("parse data here");
                    loadinview.setVisibility(View.GONE);
                    JSONObject jsonObject = mainObject.getJSONObject("data");
                    String msg=jsonObject.getString("msg");
//                    String baseUrl = Config.deellink + userId;
                    Intent i = new Intent(android.content.Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(android.content.Intent.EXTRA_TEXT, msg);
                    startActivity(Intent.createChooser(i, ""));
                }
            }
            else {
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

    private void setStepsToday(String stepsvalue) {
        long timeStamp = System.currentTimeMillis();
        Date dateTaday = getDate(timeStamp);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("steps", stepsvalue).apply();
        msharedprefToyayvalue = getActivity().getSharedPreferences(TODYAYS_VALUES, 0);
        SharedPreferences.Editor todayvalueEditor = msharedprefToyayvalue.edit();
        todayvalueEditor.putInt("STEPS", Integer.parseInt(stepsvalue.trim()));
        todayvalueEditor.putLong("DATE", dateTaday.getTime());
        todayvalueEditor.putString("CALORY", kcalString);
        todayvalueEditor.putString("DISTANCE", kmstring);
        todayvalueEditor.commit();

    }

    private void setProgressValue() {
        step_progress.setStartAngle(0f);
        step_progress.setProgress(0f);
        step_progress.setProgressMax(10000f);
        step_progress.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
        step_progress.setOnProgressChangeListener(progress -> {
            // Do something

            System.out.println("Progress for foot : " + progress);
            return Unit.INSTANCE;
        });
        total_steps.setText("0");
//        setStepsToday("0");

    }

    public Object getPreferenceObjectJson(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        /**** get user data    *****/
        String json = appSharedPrefs.getString("response", "nodata");
        System.out.println("dashboard  called json : " + json);
        if (json.contains("nodata")) {
            return null;
        }
        Gson gson = new Gson();
        LoginResponse selectedUser = gson.fromJson(json, LoginResponse.class);
        return selectedUser;
    }

    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.getRecordingClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Successfully subscribed!");
                                    isStepfetchec=false;
                                    isCalfetched=false;
                                    readData();
                                    getcalories();
                                    getMiles();
//                                    postUserStepsDetails();
                                } else {
                                    System.out.println("There was a problem subscribing."+ task.getException());
                                    Log.w(TAG, "There was a problem subscribing.", task.getException());
                                }
                            }
                        });
    }

    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    private void readData() {
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                Log.i(TAG, "Total steps: " + total);
                                System.out.println("Total steps: " + total);
                                setSteps((int) total);

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                subscribe();
            }
        }
    }

    private void loadStepPermission() {
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getActivity()), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(getActivity()),
                    fitnessOptions);
        } else {
            subscribe();
        }
    }
     int todaysTotalStep;
    private void setSteps(int totlavalue) {
        Double todayCoins = ((double) (totlavalue) * .12) / 120.0;
        String twodecimaltodaycoin = String.format("%.2f", todayCoins);
        if(todayCoins<=10){
            today_coins.setText(twodecimaltodaycoin);
        }
        msharedprefToyayvalue = getActivity().getSharedPreferences(TODYAYS_VALUES, 0);
        long timeStamp = msharedprefToyayvalue.getLong("DATE", 0);
        long todayTimeStamp = System.currentTimeMillis();
        String date1=getDate(todayTimeStamp).toString();
        String date=getDate(timeStamp).toString();
        System.out.println("check date :"+date+":::::::"+date1);
        int totlavalueNew;
        if (date1.equalsIgnoreCase(date) )
        {
            int steps = msharedprefToyayvalue.getInt("STEPS", 0);
            totlavalueNew = totlavalue - steps;
        }
        else {
            totlavalueNew = totlavalue;
        }
        totlaStepstoday = String.valueOf(totlavalueNew);
        System.out.println("totla value : totlavalue" + totlavalue);
        total_steps.setText(String.valueOf(totlavalue));


        if (totlavalue > 0) {
            float z = (float) totlavalue;
            step_progress.setProgress(z);
        } else {
            step_progress.setProgress(0f);
        }
        todaysTotalStep=totlavalue;
//        getKMValueFromSteps(String.valueOf(totlavalue));
        isStepfetchec=true;
        if(isStepfetchec&&isCalfetched){
            setStepsToday(String.valueOf(totlavalue));
            postUserStepsDetails();
        }
    }

    private void getcalories() {
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        cal.setTime(now);
//        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.WEEK_OF_YEAR, -1);
//        long startTime = cal.getTimeInMillis();
//        DataReadRequest readRequest = new DataReadRequest.Builder()
//                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
//                .bucketByActivityType(1, TimeUnit.SECONDS)
//                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                .build();
//        Task<DataReadResponse> response = Fitness.getHistoryClient((getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity())).readData(readRequest);
//        List<DataSet> dataSets = response.getResult().getDataSets();


        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))

                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {


                                float totalCal =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();

                                System.out.println("santi cal Total steps: " + totalCal);
                                //setkal((int) total);
                                getCalText(totalCal);

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }

    private void getMiles() {
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                float total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
                                float distance = total / 1000;
                                double dis = Math.round(distance * 100.0) / 100.0;
                                Log.i(TAG, "Total steps: " + dis);
                                System.out.println("Total setKm: " + dis+":::"+total);
                                // setKm(String.format("%.2f", total));

                                getKMValueFromSteps(dis);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }

    private void setkal(String totalCal) {
        System.out.println("santi total cal " + String.valueOf(totalCal));
        msharedprefToyayvalue = getActivity().getSharedPreferences(TODYAYS_VALUES, 0);
        long timeStamp = msharedprefToyayvalue.getLong("DATE", 0);
        long todayTimeStamp = System.currentTimeMillis();
        String date1=getDate(todayTimeStamp).toString();
        String date=getDate(timeStamp).toString();
        if (date1.equalsIgnoreCase(date)) {
            String strCal=msharedprefToyayvalue.getString("CALORY", null);
            if(strCal!=null){
                Double preCal=Double.parseDouble(strCal);
                Double recentCal=Double.parseDouble(totalCal);
                Double sendCal=recentCal-preCal;
                String totalcal = String.format("%.2f", sendCal);
                kcalString =totalcal;
            }
        } else {
            kcalString =String.valueOf(totalCal);
        }
        Spannable kcalword = new SpannableString(totalCal);
        kcalword.setSpan(new ForegroundColorSpan(Color.parseColor("#20D0D3")), 0, kcalword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kaltext.setText(kcalword);
        Spannable kcalword2 = new SpannableString(" Kcal");
        kcalword2.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, kcalword2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kaltext.append(kcalword2);
        isCalfetched=true;
        if(isCalfetched&&isStepfetchec){
            setStepsToday(String.valueOf(todaysTotalStep));
            postUserStepsDetails();
        }

    }

    private void setKm(String totalkm) {
        kmstring = totalkm;
        System.out.println("santi kmstring : " + kmstring + ": " + totalkm);
        Spannable kmword = new SpannableString(kmstring);
        kmword.setSpan(new ForegroundColorSpan(Color.parseColor("#20D0D3")), 0, kmword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kmtext.setText(kmword);
        Spannable kmword2 = new SpannableString(" Km");
        kmword2.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, kmword2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        kmtext.append(kmword2);
    }

    private void getCurrentdat() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d");
        String formattedDate = df.format(c);
        todaydate.setText(formattedDate);
    }

    private void postUserStepsDetails() {
        loadinview.setVisibility(View.GONE);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callsexUsername = ssgInterface.postSteps(Config.API_KEY, Config.APP_ID, userId, kcalString, ts, totlaStepstoday);
        callsexUsername.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject dashboard Steps : " + jsonObject);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void createReward() {
        mRewardedVideoAd.loadAd((getResources().getString(R.string.ads_id_rewarded)),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
//        shwoMessage("onRewarded! currency: " + reward.getType() + "  amount: " +
//       reward.getAmount());
        if(getActivity()!=null){
            getCoinFree();
        }

    }


    @Override
    public void onRewardedVideoAdLeftApplication() {
//        shwoMessage("onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdClosed() {
//        shwoMessage("onRewardedVideoAdClosed");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
//        shwoMessage("onRewardedVideoAdFailedToLoad");
    }

    @Override
    public void onRewardedVideoAdLoaded() {
//         shwoMessage("onRewardedVideoAdLoaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        shwoMessage("onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {


    }

    @Override
    public void onRewardedVideoCompleted() {

    }
    private void getCoinFree() {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callsexUsername = ssgInterface.freeCoin(Config.API_KEY, Config.APP_ID, userId);
        callsexUsername.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject dashboard Steps : " + jsonObject);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void shwoMessage(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showRewardVideo() {
        long checkOneHour = PreferenceManager.getDefaultSharedPreferences(getActivity()).getLong("checkOneHour", 1);
        long currentTime=System.currentTimeMillis();
        if(checkOneHour<currentTime){
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
                long afteroneHour=currentTime+3600000;
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putLong("checkOneHour", afteroneHour).apply();
            }
            else {
                //shwoMessage("Rewarded Video not available for now");
                shwoMessage("Video not available!");
            }

        }else{
           long difftime= checkOneHour-currentTime;
            String show_time=millisecToTime(difftime);
//            shwoMessage("Please wait for : "+show_time);
            couter.setVisibility(View.VISIBLE);
            new CountDownTimer(difftime, 1000) {

                public void onTick(long difftime) {
                    couter.setText("Please Wait for : "+millisecToTime(difftime));

                }
                public void onFinish() {
//                    couter.setText("done!");
                }
            }.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    couter.setVisibility(View.GONE);
                }
            }, 5000);
        }
//
//        if (isvideoplay()) {
//            if (mRewardedVideoAd.isLoaded()) {
//                mRewardedVideoAd.show();
//            }
//            else {
//                //shwoMessage("Rewarded Video not available for now");
//                shwoMessage("Video not available!");
//            }
//        } else {
//            shwoMessage("Your daily video limit is over!");
//        }

    }
    String millisecToTime(long millisec) {
        long sec = millisec/1000;
        long second = sec % 60;
        long minute = sec / 60;
        if (minute >= 60) {
            long hour = minute / 60;
            minute %= 60;
            return hour + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        }
        return minute + ":" + (second < 10 ? "0" + second : second);
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
                    System.out.println("data : "+data.toString());
                    String total_steps= data.getString("total_steps");
                    String total_calorie= data.getString("total_calorie");
                    String total_coins= data.getString("total_coins");
                    String today_steps= data.getString("total_steps");
                    String today_calorie= data.getString("total_calorie");
                    // for show only two decimal place
                    String totalcoin =total_coins;
                    double dtotalcoin = Double.parseDouble(totalcoin);
                    String twodecimaltotalcoin = String.format("%.2f", dtotalcoin);
                    Spannable minWord = new SpannableString(twodecimaltotalcoin);
                    minWord.setSpan(new ForegroundColorSpan(Color.parseColor("#20D0D3")), 0, minWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mintext.setText(minWord);

                    Spannable minWord2 = new SpannableString(" Coins");
                    minWord2.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, minWord2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mintext.append(minWord2);
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
                System.out.println("getProfile jsonObject : " + jsonObject);
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
            System.out.println("dashbaoed parseCoinObject json data : " + data.toString());
            profileObject = gson.fromJson(data.toString(), ProfileModel.class);
            setValueUser(profileObject);
            loadinview.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setValueUser(ProfileModel model) {
        System.out.println("santi called setValueUser");
//        for show only two decimal place
        String totalcoin = model.getTotal_coins();
        double dtotalcoin = Double.parseDouble(totalcoin);
        String twodecimaltotalcoin = String.format("%.2f", dtotalcoin);
//        today_coins.setText(twodecimaltotalcoin);
        Spannable minWord = new SpannableString(twodecimaltotalcoin);
        minWord.setSpan(new ForegroundColorSpan(Color.parseColor("#20D0D3")), 0, minWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mintext.setText(minWord);

        Spannable minWord2 = new SpannableString(" Coins");
        minWord2.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, minWord2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mintext.append(minWord2);
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private void storedate() {
        String useridvalue = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("cdate", "novalue");
        String currentdate = getCurrentDate();
        System.out.println("same vale data : " + useridvalue + " : " + currentdate);
        if (useridvalue.equalsIgnoreCase(currentdate)) {
            System.out.println("same vale found ");
        } else {
            System.out.println("same value not found");
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("cdate", currentdate).apply();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("cint", 1).apply();

        }
    }

    private boolean isvideoplay() {
        Integer dafaultvale = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("cint", 1);

        if (dafaultvale == 8) {
            return false;
        } else {
            dafaultvale = dafaultvale + 1;
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("cint", dafaultvale).apply();
            return true;
        }
    }

    private void getKMValueFromSteps(double dis) {

//        float distance = (float) (Integer.parseInt(totalSteps) * 78) / (float) 100000;
        String totalKM = String.format("%.2f", dis);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("totalKM", totalKM).apply();

        setKm(totalKM);
    }

    private void getCalText(float totalCal) {

//        float kal = (float) (Integer.parseInt(steps) * 0.063) / (float) 1400;
        String totalcal = String.format("%.0f", totalCal);
        setkal(totalcal);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("cal", totalcal).apply();
    }
//    for two day date

    private Date getDate(long time) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Date date = cal.getTime();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println("date : " + date.toString());
        return cal.getTime();
    }
    //    check Intetnet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible&&getActivity()!=null) {
            //Do your stuff here
            if(isNetworkConnected()){
                askForActivityPermission();
            }else{
                Intent intent = new Intent(getActivity(), DashBoardView.class);
                startActivity(intent);
                getActivity().finish();
//                showToastMessage("Please check your internet connection");
            }

        }

        super.setMenuVisibility(visible);
    }
}