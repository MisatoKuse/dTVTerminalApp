/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home_temp;


import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class WatchingVideoListActivity extends BaseActivity {

//    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_video_list_main_layout);
    }

//    private void initView(){
//        mLinearLayout = findViewById(R.id.watch_video_list_main_layout_ll1);
//        mLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(TvPlayerActivity.class,null);
//            }
//        });
//    }
}