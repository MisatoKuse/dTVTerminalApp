/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

/**
 * Dアカウント誘導Activity.
 */
public class DaccountInductionActivity extends BaseActivity {
    /**
     *Dアカウントダウンロードボタン.
     */
    private TextView mDownLoadTextView;
    /**
     *ログインしないで利用するボタン.
     */
    private TextView mUseWithoutLogIn;
    /**
     *DアカウントアプリURI.
     */
    private static final String D_ACCOUNT_APP_URI = "market://details?id=com.nttdocomo.android.idmanager";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_d_account_induction_layout);
        setContents();
    }

    /**
     *画面っ上に表示するコンテンツをセット.
     */
    private void setContents() {
        setTitleText(getString(R.string.str_app_title));
        setStatusBarColor(true);
        findViewById(R.id.header_layout_back).setVisibility(View.INVISIBLE);
        mDownLoadTextView = findViewById(R.id.d_account_application_download);
        mUseWithoutLogIn = findViewById(R.id.use_without_login_induction);

        mDownLoadTextView.setOnClickListener(this);
        mUseWithoutLogIn.setOnClickListener(this);

    }

    /**
     * ボタンタップされた時.
     * @param view ボタンのビュー
     */
    @Override
    public void onClick(final View view) {
        if (view.equals(mDownLoadTextView)) {
            //Playストアへ誘導
            CustomDialog dAccountInstallDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
            dAccountInstallDialog.setContent(getResources().getString(R.string.main_setting_d_account_message));
            dAccountInstallDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    Uri uri = Uri.parse(D_ACCOUNT_APP_URI);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    finish();
                }
            });
            dAccountInstallDialog.showDialog();
        } else if (view.equals(mUseWithoutLogIn)) {
            //dアカウント未登録時の"ログインしないで使用する"ボタン
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
