/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_item_main_view);
        mFirstPairingHelpWebView = findViewById(R.id.setting_menu_main_webview);
        mFirstPairingHelpWebView.setWebViewClient(new WebViewClient());
        mFirstPairingHelpWebView.setBackgroundColor(Color.TRANSPARENT);
        mFirstPairingHelpWebView.loadUrl(SETTING_MENU_LICENSE_URL);
        mFirstPairingHelpWebView.setWebViewClient(new WebViewClient());

        //Headerの設定
        setTitleText(getString(R.string.str_app_title));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(false);
        enableGlobalMenuIcon(false);
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