package com.ssg.tracker.controller;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ssg.tracker.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

public class WorkoutPlay extends AppCompatActivity {
    ImageView gifImageView;
    ImageView close_view;
    String gifUrl;
    int loopcount = 3;
    TextView loopcountxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_play);
        gifImageView = findViewById(R.id.gifImageView);
        close_view = findViewById(R.id.close_view);
        loopcountxt = findViewById(R.id.loopcountxt);
        gifUrl = getIntent().getStringExtra("gif_url");
        Glide.with(WorkoutPlay.this)
                .load(gifUrl)
                .into(new DrawableImageViewTarget(gifImageView) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (resource instanceof GifDrawable) {
                            ((GifDrawable) resource).setLoopCount(loopcount);
                        }
                        super.onResourceReady(resource, transition);
                    }
                });
        close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        settextLoop();
    }

    private void settextLoop() {

        Spannable kmword = new SpannableString(String.valueOf("Loop count "));
        kmword.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, kmword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        loopcountxt.setText(kmword);
        Spannable kmword2 = new SpannableString(String.valueOf(loopcount));
        kmword2.setSpan(new ForegroundColorSpan(Color.parseColor("#20D0D3")), 0, kmword2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        loopcountxt.append(kmword2);

    }
}
