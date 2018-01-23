/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

public class SettingMenuLicenseActivity extends BaseActivity {

    /**
     * WebView.
     */
    WebView mLicenseWebView = null;

    /**
     * TODO 仮のHTMLファイル
     */
    private final static String SETTING_MENU_LICENSE_URL = "file:///android_asset/osslicense.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_item_main_view);

        mLicenseWebView = findViewById(R.id.setting_menu_main_webview);
        mLicenseWebView.setWebViewClient(new WebViewClient());
        mLicenseWebView.setBackgroundColor(Color.TRANSPARENT);
        mLicenseWebView.loadUrl(SETTING_MENU_LICENSE_URL);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_menu_title_license));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mLicenseWebView.canGoBack() ) {
                mLicenseWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}