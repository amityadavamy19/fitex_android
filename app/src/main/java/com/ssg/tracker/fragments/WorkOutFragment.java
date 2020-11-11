package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.DashBoardView;
import com.ssg.tracker.controller.FriendsController;
import com.ssg.tracker.controller.UserController;
import com.ssg.tracker.controller.WorkOutDetailsView;
import com.ssg.tracker.controller.WorkoutPlay;
import com.ssg.tracker.model.WorkoutModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOutFragment extends Fragment {
    RecyclerView workout_list;
    ArrayList<WorkoutModel> wkList;
    RelativeLayout friends_click, user_click;
    SSGInfo ssgInterface;

    public WorkOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bazar_list, container, false);
        getVisisbleViews(view);
        return view;
    }

    private void getVisisbleViews(View view) {
        workout_list = view.findViewById(R.id.bazar_new_list);
        friends_click = view.findViewById(R.id.friends_click);
        user_click = view.findViewById(R.id.user_click);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadWorkOut();
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
    }

    private void loadWorkOut() {
        getworkout();


    }

    public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ItemViewHolder> {

        class ItemViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView title_gif;

            public ItemViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.work_out);
                title_gif = itemView.findViewById(R.id.title_gif);
            }
        }


        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_items, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {

            String imageUrl = wkList.get(position).getGifView();
            holder.title_gif.setText(wkList.get(position).getGifName());
            Glide.with(getActivity())
                    .asBitmap()
                    .load(Config.imagebase_url+imageUrl)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkConnected()){
                        String imageUrl = wkList.get(position).getGifView();
                        //Intent intent = new Intent(getActivity(), WorkoutPlay.class);
                        Intent intent = new Intent(getActivity(), WorkOutDetailsView.class);
                        intent.putExtra("gif_url", imageUrl);
                        intent.putExtra("gif_name", wkList.get(position).getGifName());
                        intent.putExtra("gif_id", wkList.get(position).getId());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), DashBoardView.class);
                        startActivity(intent);
                        getActivity().finish();
//                    showToastMessage("Please check your internet connection");
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return wkList.size();
        }


    }

    private void getworkout() {
        wkList = new ArrayList<>();
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getworkout(Config.API_KEY, Config.APP_ID);
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                parseworout(jsonObject);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }
    private void parseworout(String object) {
        try {
            JSONObject mainObjectdata = new JSONObject(object);
            JSONArray data = mainObjectdata.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject mainobjec = data.getJSONObject(i);

                wkList.add(new WorkoutModel(mainobjec.getString("id"), mainobjec.getString("cat_image"), mainobjec.getString("cat_name")));


            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
            workout_list.setLayoutManager(gridLayoutManager);
            WorkoutAdapter adapter = new WorkoutAdapter();
            workout_list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible&&getActivity()!=null) {
            //Do your stuff here
            if(isNetworkConnected()){
//                userId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("userId", "novalue");
//                getProfile();
//                getDashBoard();
            }else{
                Intent intent = new Intent(getActivity(), DashBoardView.class);
                startActivity(intent);
                getActivity().finish();
//                showToastMessage("Please check your internet connection");
            }

        }

        super.setMenuVisibility(visible);
    }
    //    check Intetnet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
