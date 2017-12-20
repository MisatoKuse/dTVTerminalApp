/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.launch.STBSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.MyChannelEditActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_toSettingDetail;
    private Button bt_back1;
    private Button mBtnToStbSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main_layout);
        tv_toSettingDetail = (TextView) findViewById(R.id.tv_setting);
        bt_back1 = (Button) findViewById(R.id.Back1);
        tv_toSettingDetail.setClickable(true);
        bt_back1.setOnClickListener(this);
        tv_toSettingDetail.setOnClickListener(this);
        mBtnToStbSelect=findViewById(R.id.setting_activity_btn_to_stb_select);
        mBtnToStbSelect.setOnClickListener(this);
        findViewById(R.id.setting_activity_btn_to_my_program_list_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Back1:
                finish();
                break;
            case R.id.tv_setting:
                startActivity(SettingDetailActivity.class,null);
                break;
            case R.id.setting_activity_btn_to_stb_select:
                startActivity(STBSelectActivity.class, null);
                break;
            case R.id.setting_activity_btn_to_my_program_list_setting:
                startActivity(MyChannelEditActivity.class, null);
                break;
            default:
                break;
        }
    }
}
