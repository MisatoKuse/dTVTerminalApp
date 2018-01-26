/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
     * 進むアイコン.
     */
    private ImageView mNextImageView = null;
    /**
     * 戻るアイコン.
     */
    private ImageView mBackImageView = null;
    /**
     * リロードアイコン.
     */
    private ImageView mReloadImageView = null;

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

        mFirstPairingHelpWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {
                // webView機能メニューアイコン状態の更新
                setWebPageFunctionMenu();
            }
        });
        RelativeLayout relativeLayout = findViewById(R.id.webview_function_menu);
        relativeLayout.setVisibility(View.VISIBLE);

        mBackImageView = findViewById(R.id.function_menu_back);
        mBackImageView.setOnClickListener(this);
        mNextImageView = findViewById(R.id.function_menu_next);
        mNextImageView.setOnClickListener(this);
        mReloadImageView = findViewById(R.id.function_menu_reload);
        mReloadImageView.setOnClickListener(this);

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

    /**
     * webView 機能メニューアイコンの状態を更新する.
     */
    private void setWebPageFunctionMenu() {
        if (mFirstPairingHelpWebView.canGoBack()) {
            mBackImageView.setImageResource(R.drawable.icon_webview_function_menu_back_active);
        } else {
            mBackImageView.setImageResource(R.drawable.icon_webview_function_menu_back_inactive);
        }
        if (mFirstPairingHelpWebView.canGoForward()) {
            mNextImageView.setImageResource(R.drawable.icon_webview_function_menu_next_active);
        } else {
            mNextImageView.setImageResource(R.drawable.icon_webview_function_menu_next_inactive);
        }
    }

    @Override
    public void onClick(final View v) {
        if (v == mBackImageView) {
            mFirstPairingHelpWebView.goBack();
        } else if (v == mNextImageView) {
            mFirstPairingHelpWebView.goForward();
        } else if (v == mReloadImageView) {
            mFirstPairingHelpWebView.reload();
        }
    }
}