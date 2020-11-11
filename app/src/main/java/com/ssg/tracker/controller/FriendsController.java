package com.ssg.tracker.controller;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.skydoves.progressview.ProgressView;
import com.ssg.tracker.R;
import com.ssg.tracker.fragments.FriendsFragment;
import com.ssg.tracker.model.FriendsModel;
import com.ssg.tracker.ratingbar.BaseRatingBar;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsController extends AppCompatActivity {
    RecyclerView recycler_view;
    ArrayList<FriendsModel> friendslist;
    ImageView close_layout;
    SSGInfo ssgInterface;
    SpinKitView loadinview;
    String userId;
    TextView nofrineds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        userId = PreferenceManager.getDefaultSharedPreferences(FriendsController.this).getString("userId", "novalue");
        getVisisbleViews();
        loadinview.setVisibility(View.VISIBLE);
        getFriends();
    }

    private void getVisisbleViews() {
        nofrineds = findViewById(R.id.nofrineds);
        loadinview = findViewById(R.id.loadinview);
        recycler_view = findViewById(R.id.recycler_view);
        close_layout = findViewById(R.id.close_layout);
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addFriends() {
        friendslist = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int random = new Random().nextInt(99) + 20;
            int random2 = new Random().nextInt(1000) + 20;
            friendslist.add(new FriendsModel(randomName(), String.valueOf(1), String.valueOf(1), String.valueOf(random), String.valueOf(random2)));

        }
        System.out.println("friends list size : " + friendslist.size());
        FriendsAdapter mAdapter = new FriendsAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FriendsController.this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);

    }

    public String randomName() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(8);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {


        public class MyViewHolder extends RecyclerView.ViewHolder {
            ProgressView progressView;
            TextView usernae_friends, coin_text, steps_txt;
            // BaseRatingBar ratinfview;

            public MyViewHolder(View view) {
                super(view);
                progressView = view.findViewById(R.id.progressView);
                usernae_friends = view.findViewById(R.id.usernae_friends);
                coin_text = view.findViewById(R.id.coin_text);
                steps_txt = view.findViewById(R.id.steps_txt);
                //ratinfview = view.findViewById(R.id.ratinfview);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friends_info, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.coin_text.setText(friendslist.get(position).getTotalcoins());
            holder.steps_txt.setText(friendslist.get(position).getSteps());
            holder.progressView.setMax(5f);
            float f1 = Float.parseFloat(friendslist.get(position).getLevel());
            holder.progressView.setProgress(f1);
            holder.progressView.setLabelText("Steps " + friendslist.get(position).getLevel());
            // holder.ratinfview.setRating(2f);
            holder.usernae_friends.setText(friendslist.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return friendslist.size();
        }
    }

    private void getFriends() {
        friendslist = new ArrayList<>();

        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getFriends(Config.API_KEY, Config.APP_ID, userId);
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
            int registrationcode = mainObjectdata.getInt("response_code");
            if (registrationcode == 200) {
                JSONArray data = mainObjectdata.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject mainobjec = data.getJSONObject(i);

                    friendslist.add(new FriendsModel(mainobjec.getString("name"), mainobjec.getString("level"), mainobjec.getString("level"), mainobjec.getString("total_coins"), mainobjec.getString("total_steps")));

                }
                FriendsAdapter mAdapter = new FriendsAdapter();

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FriendsController.this);
                recycler_view.setLayoutManager(mLayoutManager);
                recycler_view.setItemAnimator(new DefaultItemAnimator());
                recycler_view.setAdapter(mAdapter);
                loadinview.setVisibility(View.GONE);
            } else {
                loadinview.setVisibility(View.GONE);
                // Toast.makeText(FriendsController.this, "Error getting friends ", Toast.LENGTH_SHORT).show();
                JSONObject data = mainObjectdata.getJSONObject("data");

                nofrineds.setText(data.getString("message"));
                nofrineds.setVisibility(View.VISIBLE);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
