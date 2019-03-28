/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

/**
 * Dアカウント誘導Activity.
 */
public class DaccountInductionActivity extends BaseActivity {
    /**
     * Dアカウントアプリパッケージ名.
     */
    private static final   String D_ACCOUNT_PACKAGE_NAME = "com.nttdocomo.android.idmanager";
    /**
     * Dアカウントアプリ Activity名.
     */
    private static final String D_ACCOUNT_APP_ACTIVITY_NAME = ".activity.DocomoIdTopActivity";
    /**
     *DアカウントアプリURI.
     */
    private static final String D_ACCOUNT_APP_URI = "https://play.google.com/store/apps/details?id=com.nttdocomo.android.idmanager";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_d_account_induction_layout);
        setContents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_daccount_invitation),
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(DaccountInductionActivity.this) : null);
    }

    /**
     *画面っ上に表示するコンテンツをセット.
     */
    private void setContents() {
        setTitleText(getString(R.string.str_stb_paring_setting_title));
        findViewById(R.id.header_layout_back).setVisibility(View.INVISIBLE);
        TextView downLoadTextView = findViewById(R.id.d_account_application_download);
        TextView useWithoutLogIn = findViewById(R.id.use_without_login_induction);
        useWithoutLogIn.setPaintFlags(useWithoutLogIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        downLoadTextView.setOnClickListener(this);
        useWithoutLogIn.setOnClickListener(this);

    }

    /**
     * ボタンタップされた時.
     * @param view ボタンのビュー
     */
    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.d_account_application_download) {
            Intent intent = new Intent();
            intent.setClassName(D_ACCOUNT_PACKAGE_NAME,
                    D_ACCOUNT_PACKAGE_NAME + D_ACCOUNT_APP_ACTIVITY_NAME);
            try {
                //ダウンロードされていれば起動する
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                //ダウンロードされてないので、Playストアへ誘導
                CustomDialog dAccountInstallDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                dAccountInstallDialog.setContent(getResources().getString(R.string.main_setting_d_account_message));
                dAccountInstallDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        Uri uri = Uri.parse(D_ACCOUNT_APP_URI);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                //ダイアログの枠外の操作の無効化
                dAccountInstallDialog.setOnTouchOutside(false);
                dAccountInstallDialog.showDialog();
            }
        } else if (view.getId() == R.id.use_without_login_induction) {
            //dアカウント未登録時の"ログインしないで使用する"ボタン
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final  KeyEvent event) {
        finish();
        //アニメーションを付加する
        overridePendingTransition(R.anim.in_righttoleft, R.anim.out_lefttoright);
        return super.onKeyDown(keyCode, event);
    }
}
