package com.trisaa.android;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lebron on 16-8-24.
 */
public class TransitionActivity extends BaseActivity {
    private String transition;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_second);
        transition = getIntent().getStringExtra("transition");
        initAnimation();
    }

    private void initAnimation() {
        imageView = (ImageView) findViewById(R.id.img_transition);
        textView = (TextView) findViewById(R.id.tv_transition_type);
        textView.setText(transition);
        switch (transition) {
            case "explode":
                imageView.setBackgroundResource(R.drawable.circle_red);
                Explode explode = new Explode();
                explode.setDuration(1000L);
                getWindow().setEnterTransition(explode);
                break;
            case "slide":
                imageView.setBackgroundResource(R.drawable.circle_purple);
                Slide slide = new Slide(Gravity.BOTTOM);
                slide.setDuration(1000L);
                getWindow().setEnterTransition(slide);
                break;
            case "fade":
                imageView.setBackgroundResource(R.drawable.circle_blue);
                Fade fade = new Fade();
                fade.setDuration(1000L);
                getWindow().setEnterTransition(fade);
                break;
        }
    }
}
