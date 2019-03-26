/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Paint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;

/**
 * ペアリング設定（wifi接続状態）.
 */
public class StbWifiSetActivity extends BaseActivity implements View.OnClickListener {

    /** タイトル.*/
    private TextView mWifiTitleTxt = null;
    /** 接続状態.*/
    private TextView mWifiStateTxt = null;
    /** ヘルプリンク.*/
    private TextView mLinkHelpBtn;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_wifi_set_main_layout);
        setTitleText(getString(R.string.stb_wifi_set_header));
        enableHeaderBackIcon(false);
        initView();
    }

    /**
     * ビュー初期化.
     */
    private void initView() {
        mWifiTitleTxt = findViewById(R.id.stb_wifi_set_main_layout_tv_title);
        mWifiStateTxt = findViewById(R.id.stb_wifi_set_main_layout_tv_wifi_state_txt);
        TextView mNextBtn = findViewById(R.id.stb_wifi_set_main_layout_set_next_btn);
        TextView linkNextBtn = findViewById(R.id.stb_wifi_set_main_layout_tv_link);
        mLinkHelpBtn = findViewById(R.id.stb_wifi_set_main_layout_help_link);
        linkNextBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mLinkHelpBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkNextBtn.setOnClickListener(this);
        mLinkHelpBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
    }

    /**
     * wifi状態変更ジのビュー描画.
     */
    private void showWifiStateChangeView() {
        if (isWifiOn()) {
            mWifiTitleTxt.setText(getString(R.string.stb_wifi_set_title_wifi_on));
            mWifiStateTxt.setText(getString(R.string.stb_wifi_set_wifi_on_txt));
            String wifiStateTxt = getString(R.string.stb_wifi_set_wifi_on_txt);
            StringBuilder sb = new StringBuilder();
            sb.append(wifiStateTxt);
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wm.getConnectionInfo();
            String ssId = wifiInfo.getSSID();
            if (!TextUtils.isEmpty(ssId)) {
                sb.append(getString(R.string.stb_wifi_set_wifi_state_hyphen_txt));
                ssId = ssId.replace("\"", "");
                sb.append(ssId);
            }
            SpannableString spannableString = new SpannableString(sb.toString());
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(StbWifiSetActivity.this, R.color.stb_wifi_set_white_color)),
                    wifiStateTxt.length(), sb.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mWifiStateTxt.setText(spannableString);
            mLinkHelpBtn.setVisibility(View.GONE);
        } else {
            mLinkHelpBtn.setVisibility(View.VISIBLE);
            mWifiTitleTxt.setText(getString(R.string.stb_wifi_set_title_wifi_off));
            mWifiStateTxt.setText(getString(R.string.stb_wifi_set_wifi_off_txt));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showWifiStateChangeView();
    }

    @Override
    public void onClick(final View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.stb_wifi_set_main_layout_tv_link:
                intent = new Intent(getApplicationContext(), HomeActivity.class);
                break;
            case R.id.stb_wifi_set_main_layout_help_link:
                intent = new Intent(getApplicationContext(), PairingHelpActivity.class);
                intent.putExtra(PairingHelpActivity.START_WHERE, PairingHelpActivity.ParingHelpFromMode.
                        ParingHelpFromMode_Launch.ordinal());
                startActivity(intent);
                return;
            case R.id.stb_wifi_set_main_layout_set_next_btn:
                intent = new Intent(getApplicationContext(), StbSelectActivity.class);
                intent.putExtra(StbSelectActivity.FROM_WHERE, StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Launch.ordinal());
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
