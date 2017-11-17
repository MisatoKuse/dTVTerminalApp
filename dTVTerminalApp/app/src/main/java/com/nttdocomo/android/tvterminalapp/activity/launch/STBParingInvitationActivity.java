/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;


public class STBParingInvitationActivity extends BaseActivity implements View.OnClickListener {

    private static boolean mIsFirstDisplay = true;
    private Button mUseWithoutPairingSTBParingInvitationActivity = null;
    private ImageView mParingImageView;
    private TextView mBackIcon;
    private final static String STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_paring_main_layout);
        setTitleText(getString(R.string.str_app_title));
        mBackIcon = findViewById(R.id.header_layout_back);
        mBackIcon.setVisibility(View.GONE);
        mParingImageView = findViewById(R.id.header_layout_menu);
        mParingImageView.setImageResource(R.drawable.tvicon);
        mParingImageView.setVisibility(View.VISIBLE);
        setContents();
    }
    private void setContents() {
        DTVTLogger.start();
        //TODO SharedPreferenceから初回表示判定を取得する
        /**
         *  ペアリング勧誘
         *  一度表示されたら以降表示されない
         */
        if (!mIsFirstDisplay) {
            Bundle b = new Bundle();
            b.putString(STATUS, LaunchActivity.mStateToHomePairingNg);
            startActivity(HomeActivity.class, b);
            return;
        }
        mUseWithoutPairingSTBParingInvitationActivity =
                findViewById(R.id.useWithoutPairingSTBParingInvitationActivity);
        mUseWithoutPairingSTBParingInvitationActivity.setOnClickListener(this);
        mIsFirstDisplay = false;
        DTVTLogger.end();
    }

    @Override
    public void onClick(View v) {
        Bundle b = new Bundle();
        b.putString(STATUS, LaunchActivity.mStateToHomePairingNg);
        startActivity(HomeActivity.class, b);
    }
}
