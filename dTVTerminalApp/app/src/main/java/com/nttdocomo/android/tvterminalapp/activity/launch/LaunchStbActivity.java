/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

/**
 * STB初期設定扉.
 */
public class LaunchStbActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_stb_main_layout);
        setTitleText(getString(R.string.str_stb_paring_setting_title));
        Intent intent = getIntent();
        boolean isShowBackKey = intent.getBooleanExtra(ContentUtils.LAUNCH_STB_BACK_KEY, false);
        if (!isShowBackKey) {
            enableHeaderBackIcon(false);
        }
        initView();
        if (!SharedPreferencesUtils.getSharedPreferencesStbLauchFirst(LaunchStbActivity.this)) {
            SharedPreferencesUtils.setSharedPreferencesStbLauchFirst(LaunchStbActivity.this, true);
        }
    }

    /**
     * ビュー初期化.
     */
    private void initView() {
        TextView connectBtn = findViewById(R.id.launch_stb_main_layout_tv_btn);
        TextView linkBtn = findViewById(R.id.launch_stb_main_layout_tv_link);
        linkBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        connectBtn.setOnClickListener(this);
        linkBtn.setOnClickListener(this);
    }

    /**
     * 選択ダイアログ表示.
     */
    private void showSelectDialog() {
        CustomDialog selectDialog = new CustomDialog(LaunchStbActivity.this, CustomDialog.DialogType.SELECT);
        selectDialog.setOnTouchOutside(false);
        selectDialog.setCancelable(false);
        selectDialog.setOnTouchBackkey(false);
        selectDialog.setContent(getString(R.string.launch_stb_select_dialog_title));
        selectDialog.setConfirmText(R.string.launch_stb_select_dialog_txt1);
        selectDialog.setCancelText(R.string.launch_stb_select_dialog_txt2);
        selectDialog.setLeftText(R.string.launch_stb_select_dialog_txt3);
        selectDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                startToHome();
            }
        });
        selectDialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                SharedPreferencesUtils.setSharedPreferencesShowLauchStbStatus(LaunchStbActivity.this, true);
                startToHome();
            }
        });
        selectDialog.showDialog();
    }
    /**
     * ホーム画面へ遷移.
     */
    private void startToHome() {
        Intent intent = new Intent(LaunchStbActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.launch_stb_main_layout_tv_btn:
                startActivity(new Intent(this, StbWifiSetActivity.class));
                break;
            case R.id.launch_stb_main_layout_tv_link:
                showSelectDialog();
                break;
            case R.id.header_layout_back:
                super.onClick(view);
                break;
            default:
                break;
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
