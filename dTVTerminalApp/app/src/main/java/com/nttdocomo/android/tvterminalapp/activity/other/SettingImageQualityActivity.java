/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

public class SettingImageQualityActivity extends BaseActivity implements View.OnClickListener {

    private String mStatus;
    private CheckBox checkBoxHigh;
    private CheckBox checkBoxMiddle;
    private CheckBox checkBoxLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.setting_quality_change);

        //Headerの設定
        setTitleText(getString(R.string.main_setting_quality_setting_header));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //画質設定の設定値を取得
        mStatus = getIntent().getStringExtra(getString(R.string.main_setting_quality_status));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RelativeLayout relativeLayoutHigh = findViewById(R.id.main_setting_relativelayout_quality_high);
        RelativeLayout relativeLayoutMiddle = findViewById(R.id.main_setting_relativelayout_quality_middle);
        RelativeLayout relativeLayoutLow = findViewById(R.id.main_setting_relativelayout_quality_low);
        checkBoxHigh = findViewById(R.id.main_setting_quality_checkbox_high);
        checkBoxMiddle = findViewById(R.id.main_setting_quality_checkbox_middle);
        checkBoxLow = findViewById(R.id.main_setting_quality_checkbox_low);

        relativeLayoutHigh.setOnClickListener(this);
        relativeLayoutMiddle.setOnClickListener(this);
        relativeLayoutLow.setOnClickListener(this);

        initCheckBox();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.main_setting_relativelayout_quality_high:
                checkBoxHigh.setChecked(true);
                checkBoxMiddle.setChecked(false);
                checkBoxLow.setChecked(false);
                storeStatus(getString(R.string.main_setting_image_quality_high));
                this.finish();
                break;
            case R.id.main_setting_relativelayout_quality_middle:
                checkBoxHigh.setChecked(false);
                checkBoxMiddle.setChecked(true);
                checkBoxLow.setChecked(false);
                storeStatus(getString(R.string.main_setting_image_quality_middle));
                this.finish();
                break;
            case R.id.main_setting_relativelayout_quality_low:
                checkBoxHigh.setChecked(false);
                checkBoxMiddle.setChecked(false);
                checkBoxLow.setChecked(true);
                storeStatus(getString(R.string.main_setting_image_quality_low));
                this.finish();
                break;
            default:
                super.onClick(view);
        }
    }

    /**
     * CheckBoxの初期状態を設定
     */
    private void initCheckBox() {
        if (mStatus.equals(getString(R.string.main_setting_image_quality_high))) {
            checkBoxHigh.setChecked(true);
        } else if (mStatus.equals(getString(R.string.main_setting_image_quality_middle))) {
            checkBoxMiddle.setChecked(true);
        } else if (mStatus.equals(getString(R.string.main_setting_image_quality_low))) {
            checkBoxLow.setChecked(true);
        }
    }

    /**
     * タップされた設定値を保存する
     * @param status 画質の設定値
     */
    private void storeStatus(String status) {
        SharedPreferencesUtils.setSharedPreferencesImageQuality(this, status);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DTVTLogger.start();
        if (checkRemoteControllerView()) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}