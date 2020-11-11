package com.ssg.tracker.library.parallax;

import android.os.Bundle;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import android.view.View;


import com.ssg.tracker.library.SlideFragment;
import com.ssg.tracker.library.parallax.util.ParallaxUtil;

import java.util.ArrayList;
import java.util.List;


public class ParallaxSlideFragment extends SlideFragment implements Parallaxable {

    private final List<Parallaxable> parallaxableChildren = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        parallaxableChildren.addAll(ParallaxUtil.findParallaxableChildren(view));
    }


    @Override
    public void setOffset(@FloatRange(from = -1.0, to = 1.0) float offset) {
        ParallaxUtil.setOffsetToParallaxableList(parallaxableChildren, offset);
    }
}
