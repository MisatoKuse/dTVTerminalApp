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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;

/**
 * 設定画面 プライバシーポリシー.
 */
public class SettingMenuPrivacyPolicyActivity extends BaseActivity {

    /**
     * WebView.
     */
    private WebView mPrivacyPolicyWebView = null;
    /**
     * webViewの読み込み進行度.
     */
    private int mProgress = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_item_main_view);

        mPrivacyPolicyWebView = findViewById(R.id.setting_menu_main_webview);
        mPrivacyPolicyWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mPrivacyPolicyWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setTextZoom(100);
        mPrivacyPolicyWebView.loadUrl(UrlConstants.WebUrl.SETTING_MENU_PRIVACY_POLICY_URL);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_menu_title_privacy_policy));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_privacy_policy));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DTVTLogger.start();
        if (mProgress != PROGRESS_FINISH) {
            mPrivacyPolicyWebView.reload();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DTVTLogger.start();
        mProgress = mPrivacyPolicyWebView.getProgress();
        if (mProgress != PROGRESS_FINISH) {
            mPrivacyPolicyWebView.stopLoading();
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPrivacyPolicyWebView.canGoBack()) {
                mPrivacyPolicyWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}