package com.ssg.tracker.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ssg.tracker.R;
import com.ssg.tracker.fragments.HWAfragment;
import com.ssg.tracker.fragments.OtpVerificationfragment;
import com.ssg.tracker.fragments.Registrationfragment;
import com.ssg.tracker.fragments.SexSelectionfragment;
import com.ssg.tracker.library.FragmentSlide;
import com.ssg.tracker.library.IntroActivity;
import com.ssg.tracker.library.OnNavigationBlockedListener;
import com.ssg.tracker.library.Slide;

public class UserInfoDetailController extends IntroActivity {

    boolean fullscreen = true;
    boolean scrollable = true;
    boolean customFragments = true;
    boolean permissions = true;
    boolean showBack = true;
    boolean showNext = true;
    boolean skipEnabled = false;
    boolean finishEnabled = true;
    boolean getStartedEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String name_social = intent.getStringExtra("name_social");
        String email_social = intent.getStringExtra("email_social");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setFullscreen(fullscreen);
        setButtonBackFunction(skipEnabled ? BUTTON_BACK_FUNCTION_SKIP : BUTTON_BACK_FUNCTION_BACK);
        setButtonNextFunction(
                finishEnabled ? BUTTON_NEXT_FUNCTION_NEXT_FINISH : BUTTON_NEXT_FUNCTION_NEXT);
        setButtonBackVisible(showBack);
        setButtonNextVisible(showNext);
        setButtonCtaVisible(getStartedEnabled);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);
        //registration
        Slide loginSlide = new FragmentSlide.Builder()
                .background(R.color.transparent)

                .backgroundDark(R.color.transparent)
                .fragment(Registrationfragment.newInstance())
                .build();
        addSlide(loginSlide);
        // otp selction
        Slide otpSlide = new FragmentSlide.Builder()
                .background(R.color.transparent)
                .backgroundDark(R.color.transparent)
                .fragment(OtpVerificationfragment.newInstance())
                .build();
        addSlide(otpSlide);
        // sex selction
        Slide usernameSlide = new FragmentSlide.Builder()
                .background(R.color.transparent)
                .backgroundDark(R.color.transparent)
                .fragment(SexSelectionfragment.newInstance())
                .build();
        addSlide(usernameSlide);

        // HWA selction
        Slide hwaslide = new FragmentSlide.Builder()
                .background(R.color.transparent)
                .backgroundDark(R.color.transparent)
                .fragment(HWAfragment.newInstance())
                .build();
        addSlide(hwaslide);
        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);
                    System.out.println("position : " + position);


                }
            }
        });

    }


}
