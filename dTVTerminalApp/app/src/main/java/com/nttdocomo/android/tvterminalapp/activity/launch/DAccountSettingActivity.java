/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.temp.GoogleStoreActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;


public class DAccountSettingActivity extends BaseActivity implements View.OnClickListener {

    private Button mDAccountDownloadDAccountSettingActivity=null;
    private Button mDLoginNoUseDAccountSettingActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_setting_main_layout);

        setContents();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void setContents() {
        TextView title=(TextView)findViewById(R.id.titleDAccountSettingActivity);
        title.setText(getScreenTitle());

        mDAccountDownloadDAccountSettingActivity=(Button)findViewById(R.id.dAccountDownloadDAccountSettingActivity);
        mDAccountDownloadDAccountSettingActivity.setOnClickListener(this);

        mDLoginNoUseDAccountSettingActivity=(Button)findViewById(R.id.dLoginNoUseDAccountSettingActivity);
        mDLoginNoUseDAccountSettingActivity.setOnClickListener(this);
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_d_account_setting_title);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mDLoginNoUseDAccountSettingActivity)) {
            mDLoginNoUseButton();
        } else if(v.equals(mDAccountDownloadDAccountSettingActivity)) {
            onDAccountDownloadButton();
        }

    }

    private void onDAccountDownloadButton() {
        startActivity(GoogleStoreActivity.class, null);
    }

    private void mDLoginNoUseButton() {
        SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                this, SharedPreferencesUtils.STATE_TO_HOME_PAIRING_NG);
        startActivity(HomeActivity.class, null);
    }
}