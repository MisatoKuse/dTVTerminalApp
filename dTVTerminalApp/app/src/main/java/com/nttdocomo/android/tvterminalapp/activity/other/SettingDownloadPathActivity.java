/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.RuntimePermissionUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

public class SettingDownloadPathActivity extends BaseActivity implements View.OnClickListener {

    private String mStatus;
    private CheckBox checkBoxDevice;
    private CheckBox checkBoxSDCard;

    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final int REQUEST_CODE = 100;

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
                markDeviceCheckBox(true);
                break;
            case R.id.main_setting_relativelayout_storage_outside:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    //android6.0未満ではRuntimePermissionは存在しない
                    markSDCardCheckBox();
                } else {
                    //Android6.0以上ではRuntimePermissionの確認を行う
                    if (RuntimePermissionUtils.hasSelfPermissions(this,
                            READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)) {
                        markSDCardCheckBox();
                    } else {
                        String[] requestPermissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
                        requestPermissions(requestPermissions, REQUEST_CODE);
                    }
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            // Permissionが許可されなかった場合
            if (!RuntimePermissionUtils.checkGrantResults(grantResults)) {
                //チェックが二重に表示されるのを防ぐために一旦チェックを外す
                checkBoxDevice.setChecked(false);
                //TODO 独自ダイアログの使用。現在はAlertDialogを使用している。
                new AlertDialog.Builder(this)
                        .setMessage(R.string.main_setting_no_permission)
                        .setNeutralButton(R.string.positive_response, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                markDeviceCheckBox(false);
                            }
                        })
                        .setCancelable(false)
                        .show();
            } else {
                markSDCardCheckBox();
            }
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
     * 内部ストレージ側のチェックボックスにチェックをつけてこのActivityを終了させる
     */
    private void markDeviceCheckBox(Boolean isFinish) {
        checkBoxDevice.setChecked(true);
        checkBoxSDCard.setChecked(false);
        storeStatus(true);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     * SDカード側のチェックボックスにチェックをつけてこのActivityを終了させる
     */
    private void markSDCardCheckBox() {
        checkBoxDevice.setChecked(false);
        checkBoxSDCard.setChecked(true);
        storeStatus(false);
        this.finish();
    }

    /**
     * タップされた設定値を保存する
     * @param status 保存先の設定値
     */
    private void storeStatus(Boolean status) {
        SharedPreferencesUtils.setSharedPreferencesStoragePath(this, status);
    }
}
