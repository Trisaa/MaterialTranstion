package com.trisaa.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by lebron on 16-8-24.
 */
public class ShareElementsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_elements);
        Fragment sharedElementFragment1 = new ShareElementsFragment1();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_container, sharedElementFragment1)
                .commit();
    }
}
