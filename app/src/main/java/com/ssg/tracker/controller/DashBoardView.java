package com.ssg.tracker.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.parse.facebook.ParseFacebookUtils;
import com.ssg.tracker.R;
import com.ssg.tracker.fragments.BazarFragment;
import com.ssg.tracker.fragments.CoinFragment;
import com.ssg.tracker.fragments.DasBoardfragment;
import com.ssg.tracker.fragments.FrinedsBazarFragment;
import com.ssg.tracker.fragments.InsentiveFregment;
import com.ssg.tracker.fragments.ProfileFragment;
import com.ssg.tracker.fragments.WorkOutFragment;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import mehdi.sakout.fancybuttons.FancyButton;

public class DashBoardView extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    RelativeLayout adView_ads, rl_nointernet;
    FancyButton retry_btn;
    SpinKitView loadinview;
    TextView tv_no_internet;
    private int[] tabIcons = {
            R.drawable.ic_dashboard,
            R.drawable.ic_gym,
            R.drawable.ic_bazzar, R.drawable.ic_doller, R.drawable.ic_incentives
    };
    Typeface fontface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        setContentView(R.layout.dashboard_containor);
        getAllView();
    }

    private void getAllView() {
        adView_ads = findViewById(R.id.adView_ads);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getResources().getString(R.string.ad_id_banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView_ads.addView(adView);
        toolbar = findViewById(R.id.toolbar_new);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        retry_btn = findViewById(R.id.retry_btn);
        rl_nointernet = findViewById(R.id.rl_nointernet);
        loadinview= findViewById(R.id.loadinview);
        tv_no_internet= findViewById(R.id.tv_no_internet);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        if (isNetworkConnected()) {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            rl_nointernet.setVisibility(View.GONE);
        } else {
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            rl_nointernet.setVisibility(View.VISIBLE);
        }
        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    tv_no_internet.setVisibility(View.GONE);
                    retry_btn.setVisibility(View.GONE);
                    loadinview.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            Intent intent = new Intent(DashBoardView.this, DashBoardView.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);

                }else{
                    tv_no_internet.setVisibility(View.GONE);
                    retry_btn.setVisibility(View.GONE);
                    loadinview.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            if(isNetworkConnected()){
                                Intent intent = new Intent(DashBoardView.this, DashBoardView.class);
                                startActivity(intent);
                                finish();
                            }else{
                                tv_no_internet.setVisibility(View.VISIBLE);
                                retry_btn.setVisibility(View.VISIBLE);
                                loadinview.setVisibility(View.GONE);
                            }

                        }
                    }, 2000);
                }

            }
        });
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(DashBoardView.this, R.color.registration_image_color);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(DashBoardView.this, R.color.white);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
        fontface = Typeface.createFromAsset(getAssets(),
                "fonts/app_textfont.ttf");
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(fontface, Typeface.NORMAL);
                    ((TextView) tabViewChild).setTextSize(8);
                }
            }
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapternew adapter = new ViewPagerAdapternew(getSupportFragmentManager());
        adapter.addFrag(new DasBoardfragment(), "Home");
        adapter.addFrag(new WorkOutFragment(), "Workout video");
        adapter.addFrag(new BazarFragment(), "Store");
        adapter.addFrag(new CoinFragment(), "Coin");
        adapter.addFrag(new InsentiveFregment(), "Milestones");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapternew extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapternew(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static void setPagerValue() {

    }

    //    check Intetnet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
