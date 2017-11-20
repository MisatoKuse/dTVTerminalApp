/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;


public class STBConnectActivity extends BaseActivity {
    private boolean isStbConnected = false;
    private static final int DELAYED_TIME = 3000;
    private TextView mConnectResult;
    private ImageView mParingImageView;
    private TextView mBackIcon;
    private final static String STATUS = "status";
    private final static String DTVT = "dTVTerminal";
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_connect_main_layout);
        mContext = this;
        //TODO SharedPreferenceにSTB接続完了をセット
        SharedPreferencesUtils.setSharedPreferencesStbConnect(this, true);
        setContents();
    }

    /**
     * 画面上の表示をセットする
     */
    private void setContents() {
        mBackIcon = findViewById(R.id.header_layout_back);
        mBackIcon.setVisibility(View.GONE);
        mParingImageView = findViewById(R.id.header_layout_menu);
        mParingImageView.setImageResource(R.drawable.tvicon);
        mParingImageView.setVisibility(View.VISIBLE);
        setTitleText(getString(R.string.str_app_title));
        mConnectResult = findViewById(R.id.connect_result_text);
        mConnectResult.setVisibility(View.VISIBLE);
        mConnectResult.setText(R.string.str_stb_connect_success_text);
        handler.postDelayed(runnable, DELAYED_TIME);
    }

    /**
     * STB接続できたら、ホーム画面に自動遷移する
     */
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            try {
                if (!isStbConnected) {
                    SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                            mContext, SharedPreferencesUtils.STATE_TO_HOME_PAIRING_OK);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                DTVTLogger.error(DTVT);
            }
            DTVTLogger.end();
        }
    };
}
