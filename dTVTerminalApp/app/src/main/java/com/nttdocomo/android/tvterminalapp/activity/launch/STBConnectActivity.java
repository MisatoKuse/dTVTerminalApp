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


public class STBConnectActivity extends BaseActivity {
    private Context mContext;
    private boolean isStbConnected = false;
    private static final int DELAYED_TIME = 1500;
    private TextView mConnectResult;
    private ImageView mParingImageView;
    private TextView mBackIcon;
    private final static String STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_connect_main_layout);
        mBackIcon = findViewById(R.id.header_layout_back);
        mBackIcon.setVisibility(View.GONE);
        mParingImageView = findViewById(R.id.header_layout_menu);
        mParingImageView.setImageResource(R.drawable.tvicon);
        mParingImageView.setVisibility(View.VISIBLE);
        setTitleText(getString(R.string.str_app_title));
        setContents();
    }

    private void setContents() {
        mConnectResult = findViewById(R.id.connect_result_text);
        mConnectResult.setVisibility(View.VISIBLE);
        mConnectResult.setText(R.string.str_stb_connect_success_text);
        handler.postDelayed(runnable, DELAYED_TIME);
        //TODO ユーザー情報を保存する
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
                    isStbConnected = true;
                    handler.postDelayed(this, DELAYED_TIME);
                } else {
                    handler.removeCallbacks(runnable);
                    Bundle b = new Bundle();
                    b.putString(STATUS, LaunchActivity.mStateToHomePairingOk);
                    startActivity(HomeActivity.class, b);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                DTVTLogger.end();
            }
        }
    };
}
