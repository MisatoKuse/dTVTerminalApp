/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

public class ApplicationInfoActivity extends BaseActivity {

    /**
     * 画質の設定値.
     */
    private ImageView mAppIcon;
    private TextView mAppName;
    private TextView mAppVersion;
    private String mPackageName;
    private PackageManager mPackageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DTVTLogger.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_info_main_layout);
        //Headerの設定
        setTitleText(getString(R.string.main_setting_application_info_header));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        initView();
        settingView();
    }

    /**
     * ビューに値に設置する
     */
    private void settingView() {
        mPackageName = getPackageName();
        mPackageManager = getPackageManager();
        //アプリケーションアイコン設置
        Drawable appIcon = getAppIcon();
        if (appIcon != null) {
            mAppIcon.setImageDrawable(appIcon);
        }
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
     * コンポネント初期化
     */
    private void initView() {
        mAppIcon = findViewById(R.id.application_info_main_layout_icon_content);
        mAppName = findViewById(R.id.application_info_main_layout_name_value);
        mAppVersion = findViewById(R.id.application_info_main_layout_version_value);
    }

    /**
     * アプリケーションアイコンを取得する
     *
     * @return Drawable
     */
    public Drawable getAppIcon() {
        try {
            ApplicationInfo info = mPackageManager.getApplicationInfo(mPackageName, 0);
            return info.loadIcon(mPackageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * アプリケーション名を取得する
     *
     * @return String
     */
    public String getAppName() {
        try {
            ApplicationInfo info = mPackageManager.getApplicationInfo(mPackageName, 0);
            return info.loadLabel(mPackageManager).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * アプリケーションバージョンを取得する
     *
     * @return String
     */
    public String getAppVersion() {
        try {
            PackageInfo info = mPackageManager.getPackageInfo(mPackageName, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}