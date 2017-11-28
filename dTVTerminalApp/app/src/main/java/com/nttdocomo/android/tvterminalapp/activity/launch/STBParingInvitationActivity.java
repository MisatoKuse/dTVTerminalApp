/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;


public class STBParingInvitationActivity extends BaseActivity implements View.OnClickListener {

    private TextView mUseWithoutPairingSTBParingInvitationActivity = null;
    private TextView mBackIcon;
    private final static String STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_paring_main_layout);
        setContents();
    }

    /**
     * 画面上の表示をセットする
     */
    private void setContents() {
        DTVTLogger.start();
        setTitleText(getString(R.string.str_app_title));
        mBackIcon = findViewById(R.id.header_layout_back);
        mBackIcon.setVisibility(View.GONE);
        //TODO SharedPreferenceから初回表示判定を取得する
        //ペアリング勧誘
        //一度表示されたら以降表示されない
        if (SharedPreferencesUtils.getSharedPreferencesIsDisplayedParingInvitation(this)) {
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                    this, false);
            startActivity(HomeActivity.class, null);
            return;
        }
        mUseWithoutPairingSTBParingInvitationActivity =
                findViewById(R.id.useWithoutPairingSTBParingInvitationActivity);
        mUseWithoutPairingSTBParingInvitationActivity.setOnClickListener(this);
        SharedPreferencesUtils.setSharedPreferencesIsDisplayedParingInvitation(this, true);
        DTVTLogger.end();
    }

    /**
     * ペアリングしない状態でホームActivityを起動する
     *
     * @param v ビュー
     */
    @Override
    public void onClick(View v) {
        SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                this, false);
        startActivity(HomeActivity.class, null);
    }
}
