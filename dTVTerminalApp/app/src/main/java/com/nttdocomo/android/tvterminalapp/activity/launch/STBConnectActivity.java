/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * STB接続Activity.
 */
public class STBConnectActivity extends BaseActivity {

    /**STB接続フラグ.*/
    private boolean isStbConnected = false;
    /** 遅延時間.*/
    private static final int DELAYED_TIME = 3000;
    /** コンテキスト.*/
    private Context mContext = null;
    /**ハンドラー.*/
    private Handler handler = new Handler();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_connect_main_layout);
        mContext = this;
        //TODO SharedPreferenceにSTB接続完了をセット
        SharedPreferencesUtils.setSharedPreferencesStbConnect(this, true);
        setContents();
    }

    /**
     * 画面上の表示をセットする.
     */
    private void setContents() {
        DTVTLogger.start();
        setTitleText(getString(R.string.str_app_title));
        enableHeaderBackIcon(false);
        setStatusBarColor(true);
        TextView connectResult = findViewById(R.id.connect_result_text);
        connectResult.setVisibility(View.VISIBLE);
        connectResult.setText(R.string.str_stb_connect_success_text);
        handler.postDelayed(runnable, DELAYED_TIME);
        DTVTLogger.end();
    }
    /**
     * STB接続できたら、ホーム画面に自動遷移する.
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            if (!isStbConnected) {
                SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                        mContext, true);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            DTVTLogger.end();
        }
    };
}