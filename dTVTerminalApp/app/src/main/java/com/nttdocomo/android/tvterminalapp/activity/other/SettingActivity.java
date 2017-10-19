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

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_toSettingDetail;
    private Button bt_back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main_layout);
        tv_toSettingDetail = (TextView) findViewById(R.id.tv_setting);
        bt_back1 = (Button) findViewById(R.id.Back1);
        tv_toSettingDetail.setClickable(true);
        bt_back1.setOnClickListener(this);
        tv_toSettingDetail.setOnClickListener(this);
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
            default:
                break;
        }
    }
}
