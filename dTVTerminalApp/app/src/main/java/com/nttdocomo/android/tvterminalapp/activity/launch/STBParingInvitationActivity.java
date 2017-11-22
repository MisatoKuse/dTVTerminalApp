/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;


public class STBParingInvitationActivity extends BaseActivity implements View.OnClickListener {

    private TextView mUseWithoutPairingSTBParingInvitationActivity = null;
    private ImageView mParingImageView;
    private TextView mBackIcon;
    private final static String STATUS = "status";
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_paring_main_layout);

        //STBペアリング画像表示の高さを定義する
        mRelativeLayout = findViewById(R.id.stb_icon_relative_layout);
        float mHight = getHeightDensity();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (mHight / 5));
        mRelativeLayout.setLayoutParams(params);
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
        mParingImageView = findViewById(R.id.header_layout_menu);
        mParingImageView.setImageResource(R.drawable.tvicon);
        mParingImageView.setVisibility(View.VISIBLE);
        //TODO SharedPreferenceから初回表示判定を取得する
        //ペアリング勧誘
        //一度表示されたら以降表示されない
        if (SharedPreferencesUtils.getSharedPreferencesParingInvitationIsDisplayed(this)) {
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                    this, SharedPreferencesUtils.STATE_TO_HOME_PAIRING_NG);
            startActivity(HomeActivity.class, null);
            return;
        }
        mUseWithoutPairingSTBParingInvitationActivity =
                findViewById(R.id.useWithoutPairingSTBParingInvitationActivity);
        mUseWithoutPairingSTBParingInvitationActivity.setOnClickListener(this);
        SharedPreferencesUtils.setSharedPreferencesParingInvitationIsDisplayed(this, true);
        DTVTLogger.end();
    }

    /**
     * ペアリングしない状態でホームActivityを起動する
     *
     * @param v ビュー
     */
    @Override
    public void onClick(View v) {
        DTVTLogger.start();
        SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                this, SharedPreferencesUtils.STATE_TO_HOME_PAIRING_NG);
        startActivity(HomeActivity.class, null);
        DTVTLogger.end();
    }
}
