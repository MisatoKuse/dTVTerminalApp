/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * お知らせ画面.
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * WebView.
     */
    private WebView mNewsWebView = null;
    /**
     * webViewの読み込み進行度.
     */
    private int mProgress = 0;
    /**
     * webViewの読み込み完了値.
     */
    private final static int PROGRESS_FINISH = 100;
    /**
     * 表示するWebPageのURL.
     * TODO 仮のHTMLファイル
     */
    private final static String NEWS_URL = "https://www.nttdocomo.co.jp/";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main_layout);

        mNewsWebView = findViewById(R.id.news_main_webview);
        mNewsWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mNewsWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        mNewsWebView.loadUrl(NEWS_URL);

        //Headerの設定
        setTitleText(getString(R.string.information_title));
        Intent intent = getIntent();
        enableHeaderBackIcon(false);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DTVTLogger.start();
        if (mProgress != PROGRESS_FINISH) {
            mNewsWebView.reload();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DTVTLogger.start();
        mProgress = mNewsWebView.getProgress();
        if (mProgress != PROGRESS_FINISH) {
            mNewsWebView.stopLoading();
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mNewsWebView.canGoBack()) {
                    mNewsWebView.goBack();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}


