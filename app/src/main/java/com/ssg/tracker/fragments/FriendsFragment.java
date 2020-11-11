package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.LoginController;
import com.ssg.tracker.model.FriendsModel;
import com.ssg.tracker.model.ProfileModel;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {
    RecyclerView recycler_view;
    ArrayList<FriendsModel> friendslist;
    SSGInfo ssgInterface;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_list, container, false);
        getVisisbleViews(view);
        return view;
    }

    private void getVisisbleViews(View view) {
        recycler_view = view.findViewById(R.id.recycler_view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      //  addFriends();
        getFriends();
    }


//    private void addFriends() {
//        friendslist = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            int random = new Random().nextInt(99) + 20;
//            int random2 = new Random().nextInt(1000) + 20;
//            friendslist.add(new FriendsModel(randomName(), String.valueOf(1), String.valueOf(1), String.valueOf(random), String.valueOf(random2)));
//
//        }
//        System.out.println("friends list size : " + friendslist.size());
//        FriendsAdapter mAdapter = new FriendsAdapter();
//
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recycler_view.setLayoutManager(mLayoutManager);
//        recycler_view.setItemAnimator(new DefaultItemAnimator());
//        recycler_view.setAdapter(mAdapter);
//
//    }

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
            BaseRatingBar ratinfview;

            public MyViewHolder(View view) {
                super(view);
                progressView = view.findViewById(R.id.progressView);
                usernae_friends = view.findViewById(R.id.usernae_friends);
                coin_text = view.findViewById(R.id.coin_text);
                steps_txt = view.findViewById(R.id.steps_txt);
                ratinfview = view.findViewById(R.id.ratinfview);
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
            holder.ratinfview.setRating(2f);

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
        callworkoutelement = ssgInterface.getFriends(Config.API_KEY, Config.APP_ID, "70");
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
            JSONArray data = mainObjectdata.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject mainobjec = data.getJSONObject(i);

                friendslist.add(new FriendsModel(mainobjec.getString("name"), mainobjec.getString("level"), mainobjec.getString("level"), mainobjec.getString("total_coins"), mainobjec.getString("total_steps")));

            }
            FriendsAdapter mAdapter = new FriendsAdapter();

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
