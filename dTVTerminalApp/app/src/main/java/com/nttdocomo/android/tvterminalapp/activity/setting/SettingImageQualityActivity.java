/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.setting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * 設定画面の外出先視聴時の画質設定の設定画面.
 */
public class SettingImageQualityActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 画質の設定値.
     */
    private String mStatus;
    /**
     * 最高画質用チェックボックス.
     */
    private CheckBox mCheckBoxHigh;
    /**
     * 高画質用チェックボックス.
     */
    private CheckBox mCheckBoxMiddle;
    /**
     * 標準画質用チェックボックス.
     */
    private CheckBox mCheckBoxLow;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.setting_quality_change);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_quality_setting_header));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //画質設定の設定値を取得
        mStatus = getIntent().getStringExtra(getString(R.string.main_setting_quality_status));
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        RelativeLayout relativeLayoutHigh = findViewById(R.id.main_setting_relativelayout_quality_high);
        RelativeLayout relativeLayoutMiddle = findViewById(R.id.main_setting_relativelayout_quality_middle);
        RelativeLayout relativeLayoutLow = findViewById(R.id.main_setting_relativelayout_quality_low);
        mCheckBoxHigh = findViewById(R.id.main_setting_quality_checkbox_high);
        mCheckBoxMiddle = findViewById(R.id.main_setting_quality_checkbox_middle);
        mCheckBoxLow = findViewById(R.id.main_setting_quality_checkbox_low);

        relativeLayoutHigh.setOnClickListener(this);
        relativeLayoutMiddle.setOnClickListener(this);
        relativeLayoutLow.setOnClickListener(this);

        initCheckBox();
    }

    @Override
    public void onClick(final View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.main_setting_relativelayout_quality_high:
                mCheckBoxHigh.setChecked(true);
                mCheckBoxMiddle.setChecked(false);
                mCheckBoxLow.setChecked(false);
                storeStatus(getString(R.string.main_setting_image_quality_high));
                this.finish();
                break;
            case R.id.main_setting_relativelayout_quality_middle:
                mCheckBoxHigh.setChecked(false);
                mCheckBoxMiddle.setChecked(true);
                mCheckBoxLow.setChecked(false);
                storeStatus(getString(R.string.main_setting_image_quality_middle));
                this.finish();
                break;
            case R.id.main_setting_relativelayout_quality_low:
                mCheckBoxHigh.setChecked(false);
                mCheckBoxMiddle.setChecked(false);
                mCheckBoxLow.setChecked(true);
                storeStatus(getString(R.string.main_setting_image_quality_low));
                this.finish();
                break;
            default:
                super.onClick(view);
        }
    }

    /**
     * CheckBoxの初期状態を設定.
     */
    private void initCheckBox() {
        if (mStatus.equals(getString(R.string.main_setting_image_quality_high))) {
            mCheckBoxHigh.setChecked(true);
        } else if (mStatus.equals(getString(R.string.main_setting_image_quality_middle))) {
            mCheckBoxMiddle.setChecked(true);
        } else if (mStatus.equals(getString(R.string.main_setting_image_quality_low))) {
            mCheckBoxLow.setChecked(true);
        }
    }

    /**
     * タップされた設定値を保存する.
     * @param status 画質の設定値
     */
    private void storeStatus(final String status) {
        SharedPreferencesUtils.setSharedPreferencesImageQuality(this, status);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (closeDrawerMenu()) {
                return false;
            }
        }
        return !checkRemoteControllerView() && super.onKeyDown(keyCode, event);
    }
}