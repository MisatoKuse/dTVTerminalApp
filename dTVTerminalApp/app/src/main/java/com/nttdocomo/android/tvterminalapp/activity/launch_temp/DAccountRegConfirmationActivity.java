/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch_temp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.temp_temp.DAccountAppliActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

public class DAccountRegConfirmationActivity extends BaseActivity implements View.OnClickListener {

    private static boolean mHasDAccountAppli=false;
    private static boolean mIsDAccountLoginOk=false;

    Button mDAccountLoginYesDAccountRegConfirmationActivity=null;
    Button mDAccountLoginNoDAccountRegConfirmationActivity=null;
    Button mDAccountAppliYesDAccountRegConfirmationActivity=null;
    Button mDAccountAppliNoDAccountRegConfirmationActivity=null;
    Button mDAccountSameYesDAccountRegConfirmationActivity=null;
    Button mDAccountSameNoDAccountRegConfirmationActivity=null;
    Button mReturnDAccountRegConfirmationActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daccount_reg_confirmation);

        setContents();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void setContents() {

        TextView title= (TextView)findViewById(R.id.titleDAccountRegConfirmationActivity);
        title.setText(getScreenTitle());

        mDAccountLoginYesDAccountRegConfirmationActivity=(Button)findViewById(R.id.dAccountLoginYesDAccountRegConfirmationActivity);
        mDAccountLoginYesDAccountRegConfirmationActivity.setOnClickListener(this);

        mDAccountLoginNoDAccountRegConfirmationActivity=(Button)findViewById(R.id.dAccountLoginNoDAccountRegConfirmationActivity);
        mDAccountLoginNoDAccountRegConfirmationActivity.setOnClickListener(this);

        mDAccountAppliYesDAccountRegConfirmationActivity=(Button)findViewById(R.id.dAccountAppliYesDAccountRegConfirmationActivity);
        mDAccountAppliYesDAccountRegConfirmationActivity.setOnClickListener(this);

        mDAccountAppliNoDAccountRegConfirmationActivity=(Button)findViewById(R.id.dAccountAppliNoDAccountRegConfirmationActivity);
        mDAccountAppliNoDAccountRegConfirmationActivity.setOnClickListener(this);

        mDAccountSameYesDAccountRegConfirmationActivity=(Button)findViewById(R.id.dAccountSameYesDAccountRegConfirmationActivity);
        mDAccountSameYesDAccountRegConfirmationActivity.setOnClickListener(this);

        mDAccountSameNoDAccountRegConfirmationActivity=(Button)findViewById(R.id.dAccountSameNoDAccountRegConfirmationActivity);
        mDAccountSameNoDAccountRegConfirmationActivity.setOnClickListener(this);

        mReturnDAccountRegConfirmationActivity=(Button)findViewById(R.id.returnDAccountRegConfirmationActivity);
        mReturnDAccountRegConfirmationActivity.setOnClickListener(this);


        setDAccountButtonVisibility(View.GONE);

        if(mIsDAccountLoginOk){
            setDAccountLoginButtonsVisibility(View.GONE);
            onDAccountLoginYesButton();
        }
    }


    @Override
    public String getScreenTitle() {
        return getString(R.string.str_d_account_reg_confirmation_title);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mDAccountLoginYesDAccountRegConfirmationActivity) ) {
            onDAccountLoginYesButton();
        } else if(v.equals(mDAccountLoginNoDAccountRegConfirmationActivity) ) {
            onDAccountLoginNoButton();
        } else if(v.equals(mDAccountAppliYesDAccountRegConfirmationActivity) ) {
            onDAccountAppliYesButton();
        } else if(v.equals(mDAccountAppliNoDAccountRegConfirmationActivity) ) {
            onDAccountAppliNoButton();
        } else if(v.equals(mDAccountSameYesDAccountRegConfirmationActivity) ) {
            onDAccountSameYesButton();
        } else if(v.equals(mDAccountSameNoDAccountRegConfirmationActivity) ) {
            onDAccountSameNoButton();
        } else if(v.equals(mReturnDAccountRegConfirmationActivity)){
            startActivity(STBSelectActivity.class, null);
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

//    private void onUseWithoutPairingButton() {
//        Bundle b=new Bundle();
//        b.putString("state", LaunchActivity.mStateToHomePairingNg);
//        startActivity(STBParingInvitationActivity.class, b);
//    }

    static public void installDAccountAppli(){
        mHasDAccountAppli=true;
    }

    static public void dAccountLogin(){
        mIsDAccountLoginOk=true;
    }

    private void setDAccountButtonVisibility(int visibility){
        //mDAccountLoginYesDAccountRegConfirmationActivity.setVisibility(visibility);
        //mDAccountLoginNoDAccountRegConfirmationActivity.setVisibility(visibility);

        mDAccountAppliYesDAccountRegConfirmationActivity.setVisibility(visibility);
        mDAccountAppliNoDAccountRegConfirmationActivity.setVisibility(visibility);

        mDAccountSameYesDAccountRegConfirmationActivity.setVisibility(visibility);
        mDAccountSameNoDAccountRegConfirmationActivity.setVisibility(visibility);
    }

    private void setDAccountLoginButtonsVisibility(int visibility){
        mDAccountLoginYesDAccountRegConfirmationActivity.setVisibility(visibility);
        mDAccountLoginNoDAccountRegConfirmationActivity.setVisibility(visibility);
    }

    private void setDAccountAppliButtonsVisibility(int visibility){
        if(mHasDAccountAppli){
            mDAccountAppliNoDAccountRegConfirmationActivity.setVisibility(View.GONE);
        } else {
            mDAccountAppliNoDAccountRegConfirmationActivity.setVisibility(visibility);
        }
        mDAccountAppliYesDAccountRegConfirmationActivity.setVisibility(visibility);
    }

    private void setDAccountSameButtonsVisibility(int visibility){
        mDAccountSameYesDAccountRegConfirmationActivity.setVisibility(visibility);
        mDAccountSameNoDAccountRegConfirmationActivity.setVisibility(visibility);
    }
}