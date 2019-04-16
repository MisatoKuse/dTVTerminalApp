/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;

/**
 * STB選択エラーActivity.
 */
public class StbSelectErrorActivity extends BaseActivity {
    /**エラータイプ.*/
    private int mErrorType = 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_select_error_layout);
        setContents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String screenName;
        if (mErrorType == StbSelectActivity.ErrorType.WIFI_NO_CONNECT_ERROR.ordinal()) {
            screenName = getString(R.string.google_analytics_screen_name_paring_wifi_cut);
        } else {
            screenName = getString(R.string.google_analytics_screen_name_stb_not_detected);
        }
        super.sendScreenView(screenName,
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(StbSelectErrorActivity.this) : null);
    }

    /**
     * 画面上に表示するコンテンツを設定する.
     */
    private void setContents() {
        Intent intent = getIntent();
        setTitleText(getString(R.string.str_stb_paring_setting_title));
        TextView timeOutTextView = findViewById(R.id.stb_select_status_error_text);
        TextView wifiNotTheCommonTextView = findViewById(R.id.launch_stb_paring_power_and_wifi_check_text);
        if (intent != null) {
            mErrorType = intent.getIntExtra(StbSelectActivity.ERROR_TYPE, -1);
        }
        if (mErrorType == StbSelectActivity.ErrorType.WIFI_NO_CONNECT_ERROR.ordinal()) {
            timeOutTextView.setText(getString(R.string.str_stb_select_wifi_not_connect));
            wifiNotTheCommonTextView.setVisibility(View.GONE);
        }
        TextView mParingAgain = findViewById(R.id.stb_search_failed_paring_again);
        TextView netWorkSetHelp = findViewById(R.id.network_set_help_text_view);
        netWorkSetHelp.setPaintFlags(netWorkSetHelp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        findViewById(R.id.header_layout_back).setVisibility(View.GONE);
        TextView withoutParing = findViewById(R.id.use_without_paring);
        withoutParing.setPaintFlags(withoutParing.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        netWorkSetHelp.setOnClickListener(this);
        mParingAgain.setOnClickListener(this);
        withoutParing.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.stb_search_failed_paring_again) {
            finish();
        } else if (v.getId() == R.id.network_set_help_text_view) {
            Intent intent = new Intent(getApplicationContext(), PairingHelpActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.use_without_paring) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}