/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram_temp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.nttdocomo.android.tvterminalapp.activity.home_temp.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.player_temp.ChannelDetailPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class ChannelListActivity extends BaseActivity implements View.OnClickListener {
    private Button btnBack,btnChannelInfo;
    private HorizontalScrollView mHorizontalScrollView;
    private List<String> tabNames;
    private LinearLayout mLinearLayout;
    private ListView mListView;
    private TextView channel1,channel2,channel3,channel4,channel5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cannel_list_main_layout);
        initView();
    }

    private void initView() {
        tabNames = new ArrayList<>();
        tabNames.add("ひかりTV");
        tabNames.add("地上波");
        tabNames.add("BS");
        tabNames.add("dチャンネル");
        tabNames.add("test1");
        tabNames.add("test2");

        mHorizontalScrollView = (HorizontalScrollView)findViewById(R.id.mHorizontalScrollView);
        mHorizontalScrollView.removeAllViews();
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        mHorizontalScrollView.addView(mLinearLayout);


        for (int i = 0;i < tabNames.size();i++){
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i != 0){
                params.setMargins(35,0,0,0);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(tabNames.get(i));
            tabTextView.setTextSize(20);
//            tabTextView.setBackgroundColor(Color.BLACK);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);

            mLinearLayout.addView(tabTextView);
        }


        btnBack = (Button)findViewById(R.id.btn_back);
        btnChannelInfo = (Button)findViewById(R.id.btn_channel_info);
        channel1= (TextView)findViewById(R.id.channel1);
        channel2= (TextView)findViewById(R.id.channel2);
        channel3= (TextView)findViewById(R.id.channel3);
        channel4= (TextView)findViewById(R.id.channel4);
        channel5= (TextView)findViewById(R.id.channel5);
        btnBack.setOnClickListener(this);
        btnChannelInfo.setOnClickListener(this);
        channel1.setOnClickListener(this);
        channel2.setOnClickListener(this);
        channel3.setOnClickListener(this);
        channel4.setOnClickListener(this);
        channel5.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                startActivity(HomeActivity.class,null);
                break;
            case R.id.btn_channel_info:
            case R.id.channel1:
            case R.id.channel2:
            case R.id.channel3:
            case R.id.channel4:
            case R.id.channel5:
                startActivity(ChannelDetailPlayerActivity.class,null);
                break;
            default:
                break;

        }
    }
}
