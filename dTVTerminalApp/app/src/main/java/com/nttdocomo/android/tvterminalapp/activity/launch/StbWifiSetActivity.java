/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
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
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;

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
    /** UNKNOWN_SSID.*/
    private static final String UNKNOWN_SSID = "<unknown ssid>";
    /** 遷移元.*/
    private int mLaunchStbFrom = -1;
    /** フィルター.*/
    private IntentFilter mFilter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_wifi_set_main_layout);
        Intent intent = getIntent();
        mLaunchStbFrom = intent.getIntExtra(ContentUtils.LAUNCH_STB_FROM, -1);
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
        linkNextBtn.setPaintFlags(linkNextBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mLinkHelpBtn.setPaintFlags(mLinkHelpBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
            String ssId = getWiFiSSID();
            if (!TextUtils.isEmpty(ssId)) {
                sb.append(getString(R.string.stb_wifi_set_wifi_state_hyphen_txt));
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

    /**
     * SSID取得.
     * @return SSID
     */
    private String getWiFiSSID() {
        String ssId = "";
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                ssId = networkInfo.getExtraInfo();
                if (ssId != null) {
                    return ssId.replace("\"", "");
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return ssId;
                }
            }
            WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo info = mWifiManager.getConnectionInfo();
            ssId = info.getSSID();
            if (ssId != null) {
                ssId = ssId.replace("\"", "");
            }
        }
        if (UNKNOWN_SSID.equals(ssId)) {
            ssId = "";
        }
        return ssId;
    }

    /**
     * SSID取得.
     */
    private void registerBroadcast() {
        if (mFilter == null) {
            mFilter = new IntentFilter();
            mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(mWifiBroadcastReceiver, mFilter);
        }
    }

    /**
     * Wifi監視レシーバー.
     */
    private BroadcastReceiver mWifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    if (state == NetworkInfo.State.CONNECTED) {
                        showWifiStateChangeView();
                    } else if (state == NetworkInfo.State.DISCONNECTED) {
                        showWifiStateChangeView();
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiBroadcastReceiver);
        mFilter = null;
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLaunchStbFrom == -1) {
                return false;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
