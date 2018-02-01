/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

/**
 * ドコモテレビターミナルにdアカウントを登録するには 画面.
 */
public class DAccountSettingHelpActivity extends BaseActivity {

    /**
     * WebView.
     */
    private WebView dAccountHelpPageWebView = null;
    /**
     * 表示するWebPageのURL.
     * TODO 仮のURL
     */
    private final static String STB_REGIST_D_ACCOUNT_URL = "https://www.nttdocomo.co.jp/";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_help_main_layout);

        dAccountHelpPageWebView = findViewById(R.id.stb_d_account_regist_help_webview);
        dAccountHelpPageWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = dAccountHelpPageWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        dAccountHelpPageWebView.loadUrl(STB_REGIST_D_ACCOUNT_URL);

        //Headerの設定
        //TODO ヘッダーのタイトル名変更対応(BaseActivity側で変更予定のため仮)
        setTitleText("ドコテレ画像");
        enableHeaderBackIcon(true);
        enableStbStatusIcon(false);
        enableGlobalMenuIcon(false);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dAccountHelpPageWebView.canGoBack()) {
                dAccountHelpPageWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}