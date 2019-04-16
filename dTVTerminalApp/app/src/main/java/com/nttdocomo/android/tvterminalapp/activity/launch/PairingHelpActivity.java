/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

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
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserAgentUtils;

/**
 * 初回起動ペアリング(ヘルプページ) 画面.
 */
public class PairingHelpActivity extends BaseActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_item_main_view);
        WebView firstPairingHelpWebView = findViewById(R.id.setting_menu_main_webview);
        firstPairingHelpWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = firstPairingHelpWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setTextZoom(100);
        webSettings.setUserAgentString(UserAgentUtils.getCustomUserAgent());
        setTitleText(getString(R.string.str_stb_paring_setting_title));
        if ((NetWorkUtils.isOnline(this))) {
            webSettings.setAllowUniversalAccessFromFileURLs(false);
            webSettings.setAllowFileAccessFromFileURLs(false);
            firstPairingHelpWebView.loadUrl(UrlConstants.WebUrl.SETTING_SUPPORT_PAIRING_URL);
        } else {
            //ローカルhtmlを表示する.
            firstPairingHelpWebView.loadUrl(UrlConstants.WebUrl.SETTING_HELP_PAIRING_URL);
        }

        //Headerの設定
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_paring_help),
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(PairingHelpActivity.this) : null);
    }

    @Override
    protected void contentsDetailBackKey(final View view) {
       super.finish();
        overridePendingTransition(R.anim.in_righttoleft, R.anim.out_lefttoright);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            contentsDetailBackKey(null);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}