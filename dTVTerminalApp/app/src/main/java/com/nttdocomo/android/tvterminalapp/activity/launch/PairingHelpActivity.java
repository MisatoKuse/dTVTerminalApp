/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

/**
 * 初回起動ペアリング(ヘルプページ) 画面.
 */
public class PairingHelpActivity extends BaseActivity {

    /**
     * WebView.
     */
    private WebView mFirstPairingHelpWebView = null;

    /**
     * TODO 仮のHTMLファイル.
     */
    private final static String SETTING_MENU_LICENSE_URL = "file:///android_asset/first_pairing_help.html";
    /**
     *ペアリングヘルプHTTP URL
     */
    private final static String SETTING_HTTP_LICENSE_URL = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/support/pairing.html";

    public static final String START_WHERE = "START_WHERE";
    /**
     *ペアリングヘルプ画面起動識別子
     */
    public enum ParingHelpFromMode {
        /**
         * ランチャーから起動
         */
        ParingHelpFromMode_Launch,
        /**
         * 設定から起動
         */
        ParingHelpFromMode_Setting,
    }
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
        mFirstPairingHelpWebView.setBackgroundColor(Color.TRANSPARENT);
        if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Launch.ordinal()) {
            mFirstPairingHelpWebView.loadUrl(SETTING_MENU_LICENSE_URL);
        } else if (mFromMode == ParingHelpFromMode.ParingHelpFromMode_Setting.ordinal()) {
            mFirstPairingHelpWebView.loadUrl(SETTING_HTTP_LICENSE_URL);
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