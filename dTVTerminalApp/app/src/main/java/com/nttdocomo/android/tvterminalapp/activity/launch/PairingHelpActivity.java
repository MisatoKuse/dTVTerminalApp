/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
        WebView firstPairingHelpWebView = findViewById(R.id.setting_menu_main_webview);
        firstPairingHelpWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = firstPairingHelpWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setTextZoom(100);
        if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Launch.ordinal()) {
            firstPairingHelpWebView.loadUrl(UrlConstants.WebUrl.SETTING_HELP_PAIRING_URL);
        } else if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Setting.ordinal()) {
            webSettings.setAllowUniversalAccessFromFileURLs(false);
            webSettings.setAllowFileAccessFromFileURLs(false);
            firstPairingHelpWebView.loadUrl(UrlConstants.WebUrl.SETTING_SUPPORT_PAIRING_URL);
        }

        //Headerの設定
        setTitleText(getString(R.string.str_app_title));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(false);
        setStatusBarColor(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Launch.ordinal()) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_paring_help_first_time));
        } else if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Setting.ordinal()) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_paring_help));
        }
    }

    @Override
    protected void contentsDetailBackKey(final View view) {
       super.finish();
        overridePendingTransition(R.anim.in_righttoleft, R.anim.out_lefttoright);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            contentsDetailBackKey(null);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}