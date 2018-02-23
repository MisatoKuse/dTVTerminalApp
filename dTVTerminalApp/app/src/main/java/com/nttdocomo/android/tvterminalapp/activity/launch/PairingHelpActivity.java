/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;

/**
 * 初回起動ペアリング(ヘルプページ) 画面.
 */
public class PairingHelpActivity extends BaseActivity {

    /**
     * WebView.
     */
    private WebView mFirstPairingHelpWebView = null;

    /**
     * 起動元判定キー.
     */
    public static final String START_WHERE = "START_WHERE";

    /**
     *ペアリングヘルプ画面起動識別子.
     */
    public enum ParingHelpFromMode {
        /**
         * ランチャーから起動.
         */
        ParingHelpFromMode_Launch,
        /**
         * 設定から起動.
         */
        ParingHelpFromMode_Setting,
    }
    /**
     * 起動元判定モード.
     */
    private int mFromMode = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mFromMode = intent.getIntExtra(START_WHERE, -1);
        }
        setContentView(R.layout.setting_menu_item_main_view);
        mFirstPairingHelpWebView = findViewById(R.id.setting_menu_main_webview);
        mFirstPairingHelpWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mFirstPairingHelpWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mFirstPairingHelpWebView.setBackgroundColor(Color.TRANSPARENT);
        if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Launch.ordinal()) {
            mFirstPairingHelpWebView.loadUrl(UrlConstants.WebUrl.SETTING_HELP_PAIRING_URL);
        } else if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Setting.ordinal()) {
            webSettings.setAllowUniversalAccessFromFileURLs(false);
            webSettings.setAllowFileAccessFromFileURLs(false);
            mFirstPairingHelpWebView.loadUrl(UrlConstants.WebUrl.SETTING_SUPPORT_PAIRING_URL);
        }
        mFirstPairingHelpWebView.setWebViewClient(new WebViewClient());

        //Headerの設定
        setTitleText(getString(R.string.str_app_title));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(false);
        enableGlobalMenuIcon(false);
        setStatusBarColor(true);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFirstPairingHelpWebView.canGoBack()) {
                mFirstPairingHelpWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}