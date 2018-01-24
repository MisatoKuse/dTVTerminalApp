/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.temp.GoogleStoreActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;


public class DAccountSettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mDAccountDownLoad;
    private TextView mDNoLogin;

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
        setTitleText(getString(R.string.str_app_title));

        mDAccountDownLoad=(TextView)findViewById(R.id.d_account_app_download);
        mDAccountDownLoad.setOnClickListener(this);

        mDNoLogin=(TextView)findViewById(R.id.use_without_login_in);
        mDNoLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(mDNoLogin)) {
            mDLoginNoUseButton();
        } else if(v.equals(mDAccountDownLoad)) {
            onDAccountDownloadButton();
        }
    }

    private void onDAccountDownloadButton() {
        startActivity(GoogleStoreActivity.class, null);
    }

    private void mDLoginNoUseButton() {
        SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                this, false);
        startActivity(HomeActivity.class, null);
    }
}