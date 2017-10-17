/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Search;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.nttdocomo.android.tvterminalapp.activity.Player.ChannelDetailPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class SearchTopActivity extends BaseActivity implements View.OnClickListener {
    private FloatingActionButton fab_sort;
    private Button bt_backHome;
    private RelativeLayout mrelativeLayout;
    private RelativeLayout mrelativeLayout1;
    private RelativeLayout mrelativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_top_main_layout);
        bt_backHome = findViewById(R.id.bt_backHome);
        mrelativeLayout = findViewById(R.id.Search_result);
        fab_sort = findViewById(R.id.fab);
        mrelativeLayout1 = findViewById(R.id.Search_result1);
        mrelativeLayout2 = findViewById(R.id.Search_result2);
        bt_backHome.setOnClickListener(this);
        mrelativeLayout.setOnClickListener(this);
        fab_sort.setOnClickListener(this);
        mrelativeLayout1.setOnClickListener(this);
        mrelativeLayout2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Search_result:
                startActivity(ChannelDetailPlayerActivity.class, null);
                break;
            case R.id.Search_result1:
                startActivity(ChannelDetailPlayerActivity.class, null);
                break;
            case R.id.Search_result2:
                startActivity(ChannelDetailPlayerActivity.class, null);
                break;
            case R.id.fab:
                Toast.makeText(this, "人気順で並び替えました", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_backHome:
                finish();
                break;
            default:
                break;
        }
    }
}


