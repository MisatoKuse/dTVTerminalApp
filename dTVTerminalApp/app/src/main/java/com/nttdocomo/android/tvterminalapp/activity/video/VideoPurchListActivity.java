/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;

public class VideoPurchListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_purch_list_main_layout);
        initView();
    }

    private void initView() {
        RelativeLayout relativeLayout = null;
        relativeLayout = findViewById(R.id.purch_video_list_main_layout_ll1);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DtvContentsDetailActivity.class, null);
            }
        });
    }
}