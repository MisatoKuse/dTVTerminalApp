/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.setting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

/**
 * APP画面.
 */
public class SettingMenuAppActivity extends BaseActivity {

    /**
     * WebView.
     */
    WebView mAppWebView = null;

    /**
     * TODO 仮のURL.
     */
    private final static String SETTING_MENU_APP_URL = "https://www.nttdocomo.co.jp/";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_item_main_view);

        mAppWebView = findViewById(R.id.setting_menu_main_webview);
        mAppWebView.setWebViewClient(new WebViewClient());
        mAppWebView.getSettings().setJavaScriptEnabled(true);
        mAppWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mAppWebView.loadUrl(SETTING_MENU_APP_URL);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_menu_title_app));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mAppWebView.canGoBack() ) {
                mAppWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}