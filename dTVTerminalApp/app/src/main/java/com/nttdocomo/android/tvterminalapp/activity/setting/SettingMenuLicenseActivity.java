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
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;

/**
 * ライセンス.
 */
public class SettingMenuLicenseActivity extends BaseActivity {

    /**
     * WebView.
     */
    private WebView mLicenseWebView = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setContentView(R.layout.setting_menu_item_main_view);

        mLicenseWebView = findViewById(R.id.setting_menu_main_webview);
        mLicenseWebView.setWebViewClient(new WebViewClient());
        mLicenseWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mLicenseWebView.loadUrl(UrlConstants.WebUrl.SETTING_MENU_LICENSE_URL);
        WebSettings webSettings = mLicenseWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setTextZoom(100);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_menu_title_license));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_license),
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(SettingMenuLicenseActivity.this) : null);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (closeDrawerMenu()) {
                return false;
            }
            if (mLicenseWebView.canGoBack()) {
                mLicenseWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}