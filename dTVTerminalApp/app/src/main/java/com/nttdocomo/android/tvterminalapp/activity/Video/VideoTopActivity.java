/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Video;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class VideoTopActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout mRelativeLayout1;
    private RelativeLayout mRelativeLayout2;
    private RelativeLayout mRelativeLayout3;
    private RelativeLayout mRelativeLayout4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_top_main_layout);
        init();
    }
    private void init(){
        mRelativeLayout1 = findViewById(R.id.video_top_main_layout_rl1);
        mRelativeLayout2 = findViewById(R.id.video_top_main_layout_rl2);
        mRelativeLayout3 = findViewById(R.id.video_top_main_layout_rl3);
        mRelativeLayout4 = findViewById(R.id.video_top_main_layout_rl4);

        mRelativeLayout1.setOnClickListener(this);
        mRelativeLayout2.setOnClickListener(this);
        mRelativeLayout3.setOnClickListener(this);
        mRelativeLayout4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.video_top_main_layout_rl1:
            case R.id.video_top_main_layout_rl2:
            case R.id.video_top_main_layout_rl3:
            case R.id.video_top_main_layout_rl4:
                startActivity(VideoSubGenreActivity.class,null);
                break;
        }
    }
}
