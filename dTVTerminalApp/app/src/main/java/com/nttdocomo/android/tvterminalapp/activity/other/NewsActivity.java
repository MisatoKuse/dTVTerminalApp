/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

public class NewsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_toNewsDetail1;
    private TextView tv_toNewsDetail2;
    private Button bt_back3;
    private Boolean mIsMenuLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.information_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        tv_toNewsDetail1 = findViewById(R.id.tv_news1);
        tv_toNewsDetail2 = findViewById(R.id.tv_news2);
        bt_back3 = findViewById(R.id.Back3);
        tv_toNewsDetail1.setClickable(true);
        tv_toNewsDetail2.setClickable(true);
        tv_toNewsDetail1.setOnClickListener(this);
        tv_toNewsDetail2.setOnClickListener(this);
        bt_back3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.Back3:
                finish();
                break;
            case R.id.tv_news1:
                case R.id.tv_news2:
                    startActivity(NewsDetailActivity.class, null);
                    break;
            default:
                super.onClick(view);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合はアプリ終了ダイアログを表示
                    showTips();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}


