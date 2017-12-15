/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

public class SettingDownloadPathActivity extends BaseActivity implements View.OnClickListener {

    private String mStatus;
    private CheckBox checkBoxDevice;
    private CheckBox checkBoxSDCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.setting_download_storage_change);

        //header部分の設定
        setTitleText(getString(R.string.main_setting_storage_setting_header));
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        menuImageView.setOnClickListener(this);

        //画質設定の設定値を取得
        mStatus = getIntent().getStringExtra(getString(R.string.main_setting_storage_status));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RelativeLayout relativeLayoutDevice = findViewById(R.id.main_setting_relativelayout_storage_device);
        RelativeLayout relativeLayoutOutside = findViewById(R.id.main_setting_relativelayout_storage_outside);
        checkBoxDevice = findViewById(R.id.main_setting_download_checkbox_device);
        checkBoxSDCard = findViewById(R.id.main_setting_download_checkbox_outside);

        relativeLayoutDevice.setOnClickListener(this);
        relativeLayoutOutside.setOnClickListener(this);

        initCheckBox();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.main_setting_relativelayout_storage_device:
                checkBoxDevice.setChecked(true);
                checkBoxSDCard.setChecked(false);
                storeStatus(true);
                this.finish();
                break;
            case R.id.main_setting_relativelayout_storage_outside:
                //TODO RuntimePermissionの確認を行う
                checkBoxDevice.setChecked(false);
                checkBoxSDCard.setChecked(true);
                storeStatus(false);
                this.finish();
                break;
            case R.id.header_layout_menu:
                //ダブルクリックを防ぐ
                if (isFastClick()) {
                    onSampleGlobalMenuButton_PairLoginOk();
                }
                break;
            default:
                break;
        }
    }

    /**
     * CheckBoxの初期状態を設定
     */
    private void initCheckBox() {
        if (mStatus.equals(getString(R.string.main_setting_device_storage))) {
            checkBoxDevice.setChecked(true);
        } else if (mStatus.equals(getString(R.string.main_setting_outside_storage))) {
            checkBoxSDCard.setChecked(true);
        }
    }

    /**
     * タップされた設定値を保存する
     * @param status 画質の設定値
     */
    private void storeStatus(Boolean status) {
        SharedPreferencesUtils.setSharedPreferencesStoragePath(this, status);
    }
}
