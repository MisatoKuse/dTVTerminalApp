/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.setting;

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
 * 利用規約.
 */
public class SettingMenuTermsOfServiceActivity extends BaseActivity {

    /**
     * WebView.
     */
    private WebView mTermsOfServiceWebView = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_item_main_view);

        mTermsOfServiceWebView = findViewById(R.id.setting_menu_main_webview);
        mTermsOfServiceWebView.setWebViewClient(new WebViewClient());
        mTermsOfServiceWebView.setBackgroundColor(Color.TRANSPARENT);
        WebSettings webSettings = mTermsOfServiceWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mTermsOfServiceWebView.loadUrl(UrlConstants.WebUrl.SETTING_MENU_TERMS_OF_SERVICE_HTML);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_menu_title_terms_of_service));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mTermsOfServiceWebView.canGoBack()) {
                mTermsOfServiceWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}