package com.trisaa.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lebron on 16-8-26.
 */
public class ShareElementsFragment1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_elements1, container, false);
        final Fragment sharedElementFragment2 = new ShareElementsFragment2();
        final View sharedView = view.findViewById(R.id.view_top);
        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Slide slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(1000L);
                /*changeBounds - 改变目标视图的布局边界

                changeClipBounds - 裁剪目标视图边界

                changeTransform - 改变目标视图的缩放比例和旋转角度

                changeImageTransform - 改变目标图片的大小和缩放比例*/
                ChangeBounds changeBoundsTransition = new ChangeBounds();
                changeBoundsTransition.setDuration(1000L);

                sharedElementFragment2.setEnterTransition(slideTransition);
                sharedElementFragment2.setAllowEnterTransitionOverlap(false);
                sharedElementFragment2.setAllowReturnTransitionOverlap(false);
                sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);
                getFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_container, sharedElementFragment2)
                        .addToBackStack(null)
                        .addSharedElement(sharedView, "share")
                        .commit();
            }
        });
        return view;
    }
}
