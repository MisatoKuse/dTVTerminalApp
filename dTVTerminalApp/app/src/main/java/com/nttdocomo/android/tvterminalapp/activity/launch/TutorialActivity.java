/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

/**
 * Created by ryuhan on 2017/09/25.
 */

public class TutorialActivity extends BaseActivity implements View.OnClickListener {

    private Button mSkipOrFinishTutorialAcivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_main_layout);

        setContents();
    }

    private void setContents() {

        TextView title= (TextView)findViewById(R.id.titleTutorialActivity);
        title.setText(getScreenTitle());

        mSkipOrFinishTutorialAcivity=(Button)findViewById(R.id.skipOrFinishTutorialAcivity);
        mSkipOrFinishTutorialAcivity.setOnClickListener(this);
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_tutorial_title);
    }

    @Override
    public String getScreenID() {
        return "";
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mSkipOrFinishTutorialAcivity)){
            onSkipOrFinish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void onSkipOrFinish() {
        Bundle b=new Bundle();
        b.putString("state", LaunchActivity.mStateFromTutorialActivity);
        startActivity(LaunchActivity.class, b);

        LaunchActivity.setNotFirstRun();
        finish();
    }
}
