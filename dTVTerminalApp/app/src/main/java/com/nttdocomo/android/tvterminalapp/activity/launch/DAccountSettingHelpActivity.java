/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;


public class DAccountSettingHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_help_main_layout);
        WebView dAccountHelpPageWebView = (WebView)findViewById(R.id.d_account_help_webView);
        dAccountHelpPageWebView.setWebViewClient(new WebViewClient());
        //TODO ヘルプページ webview URL指定
        dAccountHelpPageWebView.loadUrl("https://www.nttdocomo.co.jp/");
    }
}