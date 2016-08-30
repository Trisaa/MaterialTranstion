package com.trisaa.android;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lebron on 16-8-24.
 */
public class RevealActivity extends BaseActivity {
    private RelativeLayout relativeLayout;
    private TextView textView, textview2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTransition();
        setContentView(R.layout.activity_reveal);
        relativeLayout = (RelativeLayout) findViewById(R.id.ll_container);
        textView = (TextView) findViewById(R.id.tv_reveal);
        textview2 = (TextView) findViewById(R.id.tv_reveal2);
        findViewById(R.id.img_green).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    float finalRadius = (float) Math.hypot(relativeLayout.getWidth(), relativeLayout.getHeight());
                    Animator anim = ViewAnimationUtils.createCircularReveal(textview2, (int) motionEvent.getRawX(), (int) motionEvent.getRawY(), 0, finalRadius);
                    anim.setDuration(1000L);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            textview2.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            //textView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    anim.start();
                }
                return false;
            }
        });
    }

    private void initTransition() {
        Explode explode = new Explode();
        explode.setDuration(1000L);
        getWindow().setEnterTransition(explode);
        explode.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                Animator animation = ViewAnimationUtils.createCircularReveal(textView, textView.getWidth() / 2, textView.getHeight() / 2, 0, textView.getWidth() / 2);
                animation.setDuration(1000L);
                textView.setVisibility(View.VISIBLE);
                animation.start();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }
}
