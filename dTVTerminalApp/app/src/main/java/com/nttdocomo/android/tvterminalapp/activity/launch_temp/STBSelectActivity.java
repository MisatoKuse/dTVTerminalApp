/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch_temp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home_temp.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.temp_temp.DAccountAppliActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

/**
 * Created by ryuhan on 2017/09/22.
 */

public class STBSelectActivity extends BaseActivity implements View.OnClickListener {

    public static final String StateModeRepair="Repair";

    private static boolean mIsNextTimeHide=false;

    CheckBox mCheckBoxSTBSelectActivity=null;
    Button mUseWithoutPairingSTBParingInvitationActivity=null;
    Button mButton1STBSelectActivity=null;
    Button mButton2STBSelectActivity=null;
    Button mButton3STBSelectActivity=null;
    Button mDAccountLoginYesSTBSelectActivity=null;
    Button mDAccountLoginNoSTBSelectActivity=null;
    Button mDAccountAppliYesSTBSelectActivity=null;
    Button mDAccountAppliNoSTBSelectActivity=null;
    Button mDAccountSameYesSTBSelectActivity=null;
    Button mDAccountSameNoSTBSelectActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_select_main_layout);

        setContents();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void setContents() {

        TextView title= (TextView)findViewById(R.id.titleStbSelectActivity);
        title.setText(getScreenTitle());

        mUseWithoutPairingSTBParingInvitationActivity=(Button)findViewById(R.id.useWithoutPairingSTBParingInvitationActivity);
        mUseWithoutPairingSTBParingInvitationActivity.setOnClickListener(this);

        mCheckBoxSTBSelectActivity=(CheckBox)findViewById(R.id.checkBoxSTBSelectActivity);
        mCheckBoxSTBSelectActivity.setOnClickListener(this);

        mButton1STBSelectActivity=(Button)findViewById(R.id.button1STBSelectActivity);
        mButton1STBSelectActivity.setOnClickListener(this);

        mButton2STBSelectActivity=(Button)findViewById(R.id.button2STBSelectActivity);
        mButton2STBSelectActivity.setOnClickListener(this);

        mButton3STBSelectActivity=(Button)findViewById(R.id.button3STBSelectActivity);
        mButton3STBSelectActivity.setOnClickListener(this);

        mDAccountLoginYesSTBSelectActivity=(Button)findViewById(R.id.dAccountLoginYesSTBSelectActivity);
        mDAccountLoginYesSTBSelectActivity.setOnClickListener(this);

        mDAccountLoginNoSTBSelectActivity=(Button)findViewById(R.id.dAccountLoginNoSTBSelectActivity);
        mDAccountLoginNoSTBSelectActivity.setOnClickListener(this);

        mDAccountAppliYesSTBSelectActivity=(Button)findViewById(R.id.dAccountAppliYesSTBSelectActivity);
        mDAccountAppliYesSTBSelectActivity.setOnClickListener(this);

        mDAccountAppliNoSTBSelectActivity=(Button)findViewById(R.id.dAccountAppliNoSTBSelectActivity);
        mDAccountAppliNoSTBSelectActivity.setOnClickListener(this);

        mDAccountSameYesSTBSelectActivity=(Button)findViewById(R.id.dAccountSameYesSTBSelectActivity);
        mDAccountSameYesSTBSelectActivity.setOnClickListener(this);

        mDAccountSameNoSTBSelectActivity=(Button)findViewById(R.id.dAccountSameNoSTBSelectActivity);
        mDAccountSameNoSTBSelectActivity.setOnClickListener(this);

        setDAccountButtonVisibility(View.GONE);

        repair();
    }

    private void repair() {
        Bundle b= getIntent().getExtras();
        String state="";
        try {
            state = b.getString("state");
        } catch (Exception e) {

        }

        if(state.equals(StateModeRepair)){
            onStbSelected();
        }

    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_stb_select_title);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mUseWithoutPairingSTBParingInvitationActivity)){
            onUseWithoutPairingButton();
        } else if(v.equals(mCheckBoxSTBSelectActivity)) {
            mIsNextTimeHide=mCheckBoxSTBSelectActivity.isChecked();
        } else if(v.equals(mButton1STBSelectActivity) || v.equals(mButton2STBSelectActivity) || v.equals(mButton3STBSelectActivity) ){
            onStbSelected();
        } else if(v.equals(mDAccountLoginYesSTBSelectActivity) ) {
            onDAccountLoginYesButton();
        } else if(v.equals(mDAccountLoginNoSTBSelectActivity) ) {
            onDAccountLoginNoButton();
        } else if(v.equals(mDAccountAppliYesSTBSelectActivity) ) {
            onDAccountAppliYesButton();
        } else if(v.equals(mDAccountAppliNoSTBSelectActivity) ) {
            onDAccountAppliNoButton();
        } else if(v.equals(mDAccountSameYesSTBSelectActivity) ) {
            onDAccountSameYesButton();
        } else if(v.equals(mDAccountSameNoSTBSelectActivity) ) {
            onDAccountSameNoButton();
        }
    }

    /**
     * STBに同じdアカウントが登録されていない
     */
    private void onDAccountSameNoButton() {
        startActivity(DAccountReSettingActivity.class, null);
    }


    /**
     * STBに同じdアカウントが登録されている
     */
    private void onDAccountSameYesButton() {
        startActivity(STBConnectActivity.class, null);
    }

    /**
     * 端末内にdアカウントアプリがあるか --> ない
     * dアカウントアプリ誘導画面へ
     */
    private void onDAccountAppliNoButton() {
        startActivity(DAccountSettingActivity.class, null);
    }

    /**
     * 端末内にdアカウントアプリがあるか --> ある
     */
    private void onDAccountAppliYesButton() {

        startActivity(DAccountAppliActivity.class, null);
    }

    /**
     * dアカウント登録状態チェック --> 未ログイン
     */
    private void onDAccountLoginNoButton() {
        setDAccountAppliButtonsVisibility(View.VISIBLE);
        setDAccountLoginButtonsVisibility(View.GONE);
    }

    /**
     * dアカウント登録状態チェック --> ログイン済
     */
    private void onDAccountLoginYesButton() {
        setDAccountLoginButtonsVisibility(View.GONE);
        setDAccountSameButtonsVisibility(View.VISIBLE);
    }


    private void onStbSelected() {
        //dAccountState();
        //setDAccountLoginButtonsVisibility(View.VISIBLE);
        startActivity(DAccountRegConfirmationActivity.class, null);
    }

    private void onUseWithoutPairingButton() {
        Bundle b=new Bundle();
        b.putString("state", LaunchActivity.mStateToHomePairingNg);
        if(mIsNextTimeHide){
            startActivity(HomeActivity.class, b);
        } else {
            startActivity(STBParingInvitationActivity.class, b);
        }

    }

    private void dAccountState(){
        mUseWithoutPairingSTBParingInvitationActivity.setVisibility(View.GONE);
        mCheckBoxSTBSelectActivity.setVisibility(View.GONE);
        mButton1STBSelectActivity.setVisibility(View.GONE);
        mButton2STBSelectActivity.setVisibility(View.GONE);
        mButton3STBSelectActivity.setVisibility(View.GONE);
    }

    private void setDAccountButtonVisibility(int visibility){
        mDAccountLoginYesSTBSelectActivity.setVisibility(visibility);
        mDAccountLoginNoSTBSelectActivity.setVisibility(visibility);

        mDAccountAppliYesSTBSelectActivity.setVisibility(visibility);
        mDAccountAppliNoSTBSelectActivity.setVisibility(visibility);

        mDAccountSameYesSTBSelectActivity.setVisibility(visibility);
        mDAccountSameNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountLoginButtonsVisibility(int visibility){
        mDAccountLoginYesSTBSelectActivity.setVisibility(visibility);
        mDAccountLoginNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountAppliButtonsVisibility(int visibility){
        mDAccountAppliYesSTBSelectActivity.setVisibility(visibility);
        mDAccountAppliNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountSameButtonsVisibility(int visibility){
        mDAccountSameYesSTBSelectActivity.setVisibility(visibility);
        mDAccountSameNoSTBSelectActivity.setVisibility(visibility);
    }
}