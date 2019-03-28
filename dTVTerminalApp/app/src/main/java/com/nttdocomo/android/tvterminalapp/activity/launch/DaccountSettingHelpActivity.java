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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserAgentUtils;

/**
 * ドコモテレビターミナルにdアカウントを登録するには 画面.
 */
public class DaccountSettingHelpActivity extends BaseActivity {

    /**
     * WebView.
     */
    private WebView dAccountHelpPageWebView = null;
    /**
     * webViewの読み込み進行度.
     */
    private int mProgress = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_help_main_layout);

        dAccountHelpPageWebView = findViewById(R.id.stb_d_account_regist_help_webview);
        dAccountHelpPageWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = dAccountHelpPageWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setTextZoom(100);
        webSettings.setUserAgentString(UserAgentUtils.getCustomUserAgent());
        dAccountHelpPageWebView.loadUrl(UrlConstants.WebUrl.STB_REGIST_D_ACCOUNT_URL);
        setTitleText(getString(R.string.str_stb_paring_setting_title));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_daccount_login_help),
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(DaccountSettingHelpActivity.this) : null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DTVTLogger.start();
        if (mProgress != PROGRESS_FINISH) {
            dAccountHelpPageWebView.reload();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DTVTLogger.start();
        mProgress = dAccountHelpPageWebView.getProgress();
        if (mProgress != PROGRESS_FINISH) {
            dAccountHelpPageWebView.stopLoading();
        }
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