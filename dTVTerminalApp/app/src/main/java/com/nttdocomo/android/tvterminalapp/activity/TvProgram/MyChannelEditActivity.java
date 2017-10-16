/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.TvProgram;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class MyChannelEditActivity extends BaseActivity implements View.OnClickListener {

    private Button btnBack,btnAdd;
    private TextView channel1,channel2,channel3,channel4,channel5,channel6,backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_channel_edit_main_layout);
        initView();
    }

    private void initView() {
        btnBack = (Button)findViewById(R.id.btn_back);
        btnAdd = (Button)findViewById(R.id.btn_add);
        channel1= (TextView)findViewById(R.id.channel1);
        channel2= (TextView)findViewById(R.id.channel2);
        channel3= (TextView)findViewById(R.id.channel3);
        channel4= (TextView)findViewById(R.id.channel4);
        channel5= (TextView)findViewById(R.id.channel5);
        channel6= (TextView)findViewById(R.id.channel6);
        backIcon = (TextView)findViewById(R.id.back_key);
        backIcon.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        channel1.setOnClickListener(this);
        channel2.setOnClickListener(this);
        channel3.setOnClickListener(this);
        channel4.setOnClickListener(this);
        channel5.setOnClickListener(this);
        channel6.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
            case R.id.back_key:
                finish();
                break;
            case R.id.btn_add:
            case R.id.channel1:
            case R.id.channel2:
            case R.id.channel3:
            case R.id.channel4:
            case R.id.channel5:
            case R.id.channel6:
            case R.id.add_over:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder.create();
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                View view1 = View.inflate(this, R.layout.channel_add_dialog_layout, null);
                dialog.setView(view1,0,0,0,0);
                dialog.show();
                view1.findViewById(R.id.add_over).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                break;
        }
    }
}
