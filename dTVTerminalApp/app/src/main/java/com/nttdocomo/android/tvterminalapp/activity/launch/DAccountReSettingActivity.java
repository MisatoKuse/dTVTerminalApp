/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.temp.DAccountAppliActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

public class DAccountReSettingActivity extends BaseActivity implements View.OnClickListener {

    private Button mOtherDAccountDAccounReSettingActivity=null;
    private Button mDAccountRegDAccountReSettingActivity=null;
    private Button mReturnDAccountReSettingActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_resetting_main_layout);

        setContents();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_d_account_resetting_title);
    }

    private void setContents() {
        TextView title= (TextView)findViewById(R.id.titleDAccountReSettingActivity);
        title.setText(getScreenTitle());

        mOtherDAccountDAccounReSettingActivity=(Button)findViewById(R.id.otherDAccountDAccounReSettingActivity);
        mOtherDAccountDAccounReSettingActivity.setOnClickListener(this);

        mDAccountRegDAccountReSettingActivity=(Button)findViewById(R.id.dAccountRegDAccountReSettingActivity);
        mDAccountRegDAccountReSettingActivity.setOnClickListener(this);

        mReturnDAccountReSettingActivity=(Button)findViewById(R.id.returnDAccountReSettingActivity);
        mReturnDAccountReSettingActivity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.equals(mOtherDAccountDAccounReSettingActivity)){
            onOtherDAccountButton();
        } else if(v.equals(mDAccountRegDAccountReSettingActivity)){
            onDAccountRegButton();
        } else if(v.equals(mReturnDAccountReSettingActivity)){
            onReturnButton();
        }
    }

    private void onReturnButton() {
        startActivity(STBSelectActivity.class, null);
    }

    /**
     * 別のdアカウントでログイン
     */
    private void onOtherDAccountButton() {
        startActivity(DAccountAppliActivity.class, null);
    }

    /**
     * dアカウント設定ヘルプ
     */
    private void onDAccountRegButton() {
        startActivity(DAccountSettingHelpActivity.class, null);
    }

}