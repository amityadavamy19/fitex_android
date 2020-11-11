package com.ssg.tracker.controller;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.fragments.WorkOutFragment;
import com.ssg.tracker.model.DetailsWorkModel;
import com.ssg.tracker.model.WorkoutModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOutDetailsView extends AppCompatActivity {
    ArrayList<DetailsWorkModel> work_model;
    ImageView close_layout;
    RecyclerView recycler_view;
    int totalvale;
    SSGInfo ssgInterface;
    String cat_id;
    String cat_name;
    SpinKitView loadinview;
    int countervalue =1;
    TextView tv_textmsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        loadinview = findViewById(R.id.loadinview);
        tv_textmsg = findViewById(R.id.tv_textmsg);
        tv_textmsg.setVisibility(View.GONE);
        loadinview.setVisibility(View.VISIBLE);
        loadViewObjects();
        loadWorkOut();
    }

    private void loadViewObjects() {
        close_layout = findViewById(R.id.close_layout);
        recycler_view = findViewById(R.id.recycler_view);
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadWorkOut() {
        cat_id = getIntent().getStringExtra("gif_id");
        cat_name = getIntent().getStringExtra("gif_name");
//        work_model = new ArrayList<DetailsWorkModel>();
//        for (int i = 0; i < 7; i++) {
//            work_model.add(new DetailsWorkModel("BackPress", "15", "https://i.pinimg.com/originals/1b/18/65/1b1865514735f5b576466d26b25441e3.gif"));
//
//        }
        getworkout();

    }

    private class WorkOutAdapter extends RecyclerView.Adapter<WorkOutAdapter.MyViewHolder> {


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView work_name, work_count;
            ImageView work_image;
            RelativeLayout containor_viw;

            public MyViewHolder(View view) {
                super(view);
                work_name = view.findViewById(R.id.work_name);
                work_count = view.findViewById(R.id.work_count);
                work_image = view.findViewById(R.id.work_image);
                containor_viw = view.findViewById(R.id.containor_viw);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.workout_details_info, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.work_name.setText(work_model.get(position).getTitle());
            holder.work_count.setText(work_model.get(position).getWork_count());
            Glide.with(WorkOutDetailsView.this)
                    .asBitmap()
                    .load(Config.imagebase_url + work_model.get(position).getWork_image())
                    .into(holder.work_image);
            holder.containor_viw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogView(Config.imagebase_url + work_model.get(position).getWork_image(), Integer.parseInt(work_model.get(position).getWork_count()));
                }
            });


        }

        @Override
        public int getItemCount() {
            return work_model.size();
        }
    }
    RelativeLayout button_negative,button_positive;
    AppCompatButton play_gif;
    TextView total_count;
    ImageView cancle_img;
    private void showDialogView(String image, final int count) {
        totalvale = count;
        countervalue = 1;
//        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Dialog dialog = new Dialog(WorkOutDetailsView.this,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.play_gif_view);
         play_gif = dialog.findViewById(R.id.play_gif);
        ImageView gifView = dialog.findViewById(R.id.animation_view);
        cancle_img=dialog.findViewById(R.id.cancle_img);
        TextView textView_title = dialog.findViewById(R.id.title_txt);
        TextView textView_message = dialog.findViewById(R.id.message_txt);
         total_count = dialog.findViewById(R.id.total_count);
        button_negative = dialog.findViewById(R.id.button_negative);
        button_positive = dialog.findViewById(R.id.button_positive);
        textView_title.setText("");
        textView_message.setText("");
        total_count.setText(String.valueOf(totalvale));
        button_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalvale = totalvale + 1;
                total_count.setText(String.valueOf(totalvale));
            }
        });
        button_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalvale == 0) {
                } else {
                    totalvale = totalvale - 1;
                }
                total_count.setText(String.valueOf(totalvale));

            }
        });
        showGif(gifView, image, totalvale);

        play_gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_gif.setVisibility(View.INVISIBLE);
                button_positive.setVisibility(View.INVISIBLE);
                button_negative.setVisibility(View.INVISIBLE);

                countervalue = 0;
                //timetoplay =0;
                showGif(gifView, image, totalvale);
            }
        });
        cancle_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDestroyed()&&this!=null){
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    private void showGif(ImageView gifView, String image, final int count) {
        System.out.println("caleed gif count : "+count);

        Glide.with(this).asGif().load(image).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable gifDrawable, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                gifDrawable.setLoopCount(1);
                // Note that you'd need to unregister this in onLoadCleared to avoid a potential memory leak.
                gifDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        System.out.println("count : value : "+countervalue + " count : "+totalvale);
                        if(countervalue == totalvale){
                            play_gif.setVisibility(View.VISIBLE);
                            button_positive.setVisibility(View.VISIBLE);
                            button_negative.setVisibility(View.VISIBLE);
                        }else{
                            countervalue++;
                            total_count.setText(String.valueOf(countervalue));
                            if(countervalue == totalvale){
                                play_gif.setVisibility(View.VISIBLE);
                                button_positive.setVisibility(View.VISIBLE);
                                button_negative.setVisibility(View.VISIBLE);
                            }else{
                                showGif(gifView, image, totalvale);
                            }

                        }
                    }
                });
                return false;
            }
        }).into(gifView);


    }

    private void getworkout() {
        work_model = new ArrayList<DetailsWorkModel>();
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getworkoutDetails(Config.API_KEY, Config.APP_ID, cat_id);
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
//                title=
                work_model.add(new DetailsWorkModel(cat_name, mainobjec.getString("cat_id"), mainobjec.getString("gif_name"), mainobjec.getString("title")));


            }
            WorkOutAdapter adapter = new WorkOutAdapter();
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(WorkOutDetailsView.this);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(adapter);
            loadinview.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
