/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.setting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;

/**
 * お知らせ画面.
 */
public class NoticeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * WebView.
     */
    private WebView mNoticeWebView = null;
    /**
     * webViewの読み込み進行度.
     */
    private int mProgress = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setContentView(R.layout.notice_main_layout);

        mNoticeWebView = findViewById(R.id.notice_main_webview);
        mNoticeWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mNoticeWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setTextZoom(100);
        mNoticeWebView.loadUrl(UrlConstants.WebUrl.NOTICE_URL);

        //Headerの設定
        setTitleText(getString(R.string.information_title));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        super.sendScreenView(getString(R.string.google_analytics_screen_name_news));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DTVTLogger.start();
        if (mProgress != PROGRESS_FINISH) {
            mNoticeWebView.reload();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DTVTLogger.start();
        mProgress = mNoticeWebView.getProgress();
        if (mProgress != PROGRESS_FINISH) {
            mNoticeWebView.stopLoading();
        }
    }

    @Override
    protected void contentsDetailBackKey(final View view) {
        webViewGoBackEvent(mNoticeWebView);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (closeDrawerMenu()) {
                return false;
            }
            if (mNoticeWebView.canGoBack()) {
                mNoticeWebView.goBack();
                return false;
            } else {
                //ホーム画面に戻る
                startHomeActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}


