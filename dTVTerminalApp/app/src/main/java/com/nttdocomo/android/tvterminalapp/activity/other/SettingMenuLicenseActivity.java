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
     * グローバルメニューから起動しているかどうか.
     */
    private Boolean mIsMenuLaunch = false;

    /**
     * TODO 仮のHTMLファイル
     */
    private final static String SETTING_MENU_LICENSE_URL = "file:///android_asset/osslicense.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_item_main_view);
        WebView licenseWebView = (WebView) findViewById(R.id.setting_menu_main_webview);
        licenseWebView.setWebViewClient(new WebViewClient());
        licenseWebView.setBackgroundColor(Color.TRANSPARENT);
        licenseWebView.loadUrl(SETTING_MENU_LICENSE_URL);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_menu_title_license));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合はアプリ終了ダイアログを表示
                    showTips();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}