/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video_temp;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import com.nttdocomo.android.tvterminalapp.activity.player_temp.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class VideoContentListActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_content_list_main_layout);
        initView();
    }

    private void initView(){
        mRelativeLayout = findViewById(R.id.video_content_main_layout_ll1);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TvPlayerActivity.class,null);
            }
        });
    }
}
