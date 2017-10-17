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
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;


public class STBParingInvitationActivity extends BaseActivity implements View.OnClickListener {

    private static boolean mIsFirstDisplay=true;

    private Button mUseWithoutPairingSTBParingInvitationActivity=null;
    private Button mReturnSTBParingInvitationActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_paring_main_layout);

        setContents();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void setContents() {

        /**
         * ペアリング勧誘
         * ※一度表示されたら以降表示されない
         */
        if(!mIsFirstDisplay){
            Bundle b= new Bundle();
            b.putString("state", LaunchActivity.mStateToHomePairingNg);
            startActivity(HomeActivity.class, b);
            return;
        }

        TextView title= (TextView)findViewById(R.id.button0StbParingInvitationActivity);
        title.setText(getScreenTitle());

        mUseWithoutPairingSTBParingInvitationActivity=(Button)findViewById(R.id.useWithoutPairingSTBParingInvitationActivity);
        mUseWithoutPairingSTBParingInvitationActivity.setOnClickListener(this);

        mReturnSTBParingInvitationActivity=(Button)findViewById(R.id.returnSTBParingInvitationActivity);
        mReturnSTBParingInvitationActivity.setOnClickListener(this);

        mIsFirstDisplay=false;
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_stb_pair_invitation_title);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mUseWithoutPairingSTBParingInvitationActivity)) {
            onUseWithoutPairingButton();
        } else if(v.equals(mReturnSTBParingInvitationActivity)){
            onReturnButton();
        }
    }

    private void onReturnButton() {
        startActivity(STBSelectActivity.class, null);
    }

    private void onUseWithoutPairingButton() {
        Bundle b=new Bundle();
        b.putString("state", LaunchActivity.mStateToHomePairingNg);
        startActivity(HomeActivity.class, b);
    }
}
