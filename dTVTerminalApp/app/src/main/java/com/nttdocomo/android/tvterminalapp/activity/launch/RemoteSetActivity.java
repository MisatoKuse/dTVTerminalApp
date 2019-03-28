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
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

/**
 * リモート視聴確認.
 */
public class RemoteSetActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_set_main_layout);
        setTitleText(getString(R.string.remote_introduce_header));
        enableHeaderBackIcon(false);
        initView();
    }

    /**
     * ビュー初期化.
     */
    private void initView() {
        TextView setBtn = findViewById(R.id.remote_set_main_layout_set_btn);
        TextView linkBtn = findViewById(R.id.remote_set_main_layout_tv_link);
        linkBtn.setPaintFlags(linkBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setBtn.setOnClickListener(this);
        linkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.remote_set_main_layout_set_btn:
                Intent remoteSetIntent = new Intent(getApplicationContext(), RemoteSetIntroduceActivity.class);
                startIntentActivity(remoteSetIntent);
                break;
            case R.id.remote_set_main_layout_tv_link:
                CustomDialog resultDialog = new CustomDialog(RemoteSetActivity.this, CustomDialog.DialogType.CONFIRM);
                resultDialog.setOnTouchOutside(false);
                resultDialog.setCancelable(false);
                resultDialog.setOnTouchBackkey(false);
                resultDialog.setContent(getString(R.string.remote_introduce_dialog_txt));
                resultDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                    Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startIntentActivity(homeIntent);
                    }
                });
                resultDialog.showDialog();
                break;
            default:
                break;
        }
    }
    /**
     * Activity起動.
     * @param intent intent
     */
    private void startIntentActivity(final Intent intent) {
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
