/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;

/**
 * アプリケーション情報.
 */
public class ApplicationInfoActivity extends BaseActivity {

    /**
     * 画質の設定値.
     */
    private ImageView mAppIcon;
    /**
     * アプリネーム.
     */
    private TextView mAppName;
    /**
     * アプリバージョン.
     */
    private TextView mAppVersion;
    /**
     * パケージネーム.
     */
    private String mPackageName;
    /**
     * パケージマネージャー.
     */
    private PackageManager mPackageManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        DTVTLogger.start();
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setContentView(R.layout.application_info_main_layout);
        //Headerの設定
        setTitleText(getString(R.string.main_setting_application_info_header));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        initView();
        settingView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_application_info),
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(ApplicationInfoActivity.this) : null);
    }

    /**
     * ビューに値に設置する.
     */
    private void settingView() {
        mPackageName = getPackageName();
        mPackageManager = getPackageManager();
        //アプリケーションアイコン設置
        mAppIcon.setBackgroundResource(R.mipmap.icd_info_tvterminal_128);
        //アプリケーション名設置
        String appName = getAppName();
        if (!TextUtils.isEmpty(appName)) {
            mAppName.setText(appName);
        }
        //アプリケーションバージョン設置
        String appVersion = getAppVersion();
        if (!TextUtils.isEmpty(appVersion)) {
            mAppVersion.setText(appVersion);
        }
    }

    /**
     * コンポネント初期化.
     */
    private void initView() {
        mAppIcon = findViewById(R.id.application_info_main_layout_icon);
        mAppName = findViewById(R.id.application_info_main_layout_name_value);
        mAppVersion = findViewById(R.id.application_info_main_layout_version_value);
    }

    /**
     * アプリケーション名を取得する.
     *
     * @return String
     */
    private String getAppName() {
        return  getResources().getString(R.string.application_info_application_name_value);
    }

    /**
     * アプリケーションバージョンを取得する.
     *
     * @return String
     */
    private String getAppVersion() {
        try {
            PackageInfo info = mPackageManager.getPackageInfo(mPackageName, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (closeDrawerMenu()) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}