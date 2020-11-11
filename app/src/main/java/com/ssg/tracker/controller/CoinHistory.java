package com.ssg.tracker.controller;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.model.CoinInfoObject;
import com.ssg.tracker.model.DetailsWorkModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinHistory extends AppCompatActivity {
    ImageView close_layout;
    RecyclerView recycler_view;
    SpinKitView loadinview;
    SSGInfo ssgInterface;
    ArrayList<CoinInfoObject> coinModel;
    String userId;
    TextView nofrineds,tv_textmsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        userId = PreferenceManager.getDefaultSharedPreferences(CoinHistory.this).getString("userId", "novalue");
        nofrineds = findViewById(R.id.nofrineds);
        loadinview = findViewById(R.id.loadinview);
        loadinview.setVisibility(View.VISIBLE);
        tv_textmsg=findViewById(R.id.tv_textmsg);
        tv_textmsg.setText("Maximum steps countable per day is 10000");
        recycler_view = findViewById(R.id.recycler_view);
        close_layout = findViewById(R.id.close_layout);
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getCoinHistory();
    }

    private void getCoinHistory() {
        coinModel = new ArrayList<CoinInfoObject>();
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getCoinHistory(Config.API_KEY, Config.APP_ID, userId);
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
                    coinModel.add(new CoinInfoObject(mainobjec.getString("date"), mainobjec.getString("u_id"), mainobjec.getString("coins"), mainobjec.getString("id"), mainobjec.getString("remarks"), mainobjec.getString("status")));
                }

                CoinAdapter adapter = new CoinAdapter();
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CoinHistory.this);
                recycler_view.setLayoutManager(mLayoutManager);
                recycler_view.setItemAnimator(new DefaultItemAnimator());
                recycler_view.setAdapter(adapter);
                loadinview.setVisibility(View.GONE);
            } else {
                JSONObject data = mainObjectdata.getJSONObject("data");

                nofrineds.setText(data.getString("message"));
                nofrineds.setVisibility(View.VISIBLE);
                loadinview.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.MyViewHolder> {


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView reviewtxt, datetxt, totalcoin_;
            ImageView coin_logo;
            RelativeLayout containor_viw;

            public MyViewHolder(View view) {
                super(view);
                reviewtxt = view.findViewById(R.id.reviewtxt);
                datetxt = view.findViewById(R.id.datetxt);
                totalcoin_ = view.findViewById(R.id.totalcoin_);
                coin_logo = view.findViewById(R.id.coin_logo);
                containor_viw = view.findViewById(R.id.containor_viw);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.coin_info_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.reviewtxt.setText(coinModel.get(position).getRemarks());
            holder.datetxt.setText(coinModel.get(position).getDate());
            holder.totalcoin_.setText(coinModel.get(position).getCoins());


        }

        @Override
        public int getItemCount() {
            return coinModel.size();
        }
    }

}
