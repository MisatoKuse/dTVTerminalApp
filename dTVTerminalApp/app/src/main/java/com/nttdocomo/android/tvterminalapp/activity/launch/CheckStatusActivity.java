/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * アプリ起動時に、OTT取得やアカウントやペアリング状態等の確認や取得を行うActivity
 */
public class CheckStatusActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;
    private Button mFinishGetInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.activity_check_status);

        enableHeaderBackIcon(false);
        setTitleText(getString(R.string.str_get_info_title));
        enableStbStatusIcon(false);
        enableGlobalMenuIcon(false);

        initView();
        initData();
    }

    private void initView() {
        mRelativeLayout = findViewById(R.id.check_status_relative_layout);
        mFinishGetInfoButton = findViewById(R.id.check_status_finish_get_info);
        mFinishGetInfoButton.setOnClickListener(this);
    }
    private void initData() {
        //プログレスダイアログを表示する
        ProgressBar progressBar = new ProgressBar(CheckStatusActivity.this, null, android.R.attr.progressBarStyle);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mRelativeLayout.addView(progressBar, params);
    }

    @Override
    public void onClick(View view) {
        //TODO 現在はボタンクリックにて画面遷移を行うが、将来的にはデータ取得完了時に自動的に遷移する
        if (view == mFinishGetInfoButton) {
            if (SharedPreferencesUtils.getSharedPreferencesStbConnect(this)) {
                // ペアリング済み HOME画面遷移
                SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, true);
                startActivity(HomeActivity.class, null);
                DTVTLogger.debug("ParingOK Start HomeActivity");
            } else if (SharedPreferencesUtils.getSharedPreferencesStbSelect(this)) {
                // 次回から表示しないをチェック済み
                // 未ペアリング HOME画面遷移
                SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, false);
                startActivity(HomeActivity.class, null);
                DTVTLogger.debug("ParingNG Start HomeActivity");
            } else {
                // STB選択画面へ遷移
                Intent intent = new Intent(getApplicationContext(),STBSelectActivity.class);
                intent.putExtra(STBSelectActivity.FROM_WHERE, STBSelectActivity.STBSelectFromMode.STBSelectFromMode_Launch.ordinal());
                startActivity(intent);
                DTVTLogger.debug("Start STBSelectActivity");
            }
        }
    }
}
