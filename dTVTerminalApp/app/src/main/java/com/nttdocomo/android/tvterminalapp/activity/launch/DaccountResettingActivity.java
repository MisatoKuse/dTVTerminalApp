/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

/**
 * 端末に登録されているdアカウントとSTBに登録されているdアカウントが一致しなかった場合に表示される.
 */
public class DaccountResettingActivity extends BaseActivity implements View.OnClickListener {

    /**
     * STBにdアカウントを登録するには.
     */
    private TextView mDAccountRegistrationhelp = null;
    /**
     * アクティベーションコードを入力する.
     */
    private TextView mDAccountInputActivationCode = null;
    /**
     * 再度ペアリングを実行する.
     */
    private TextView mDAccountAgainPairing = null;
    /**
     * 別のdアカウントを実行する.
     */
    private TextView mDAccountLoginOtherAccount = null;

    /**
     * Dアカウントアプリ Package名.
     */
    private static final String D_ACCOUNT_APP_PACKAGE_NAME = "com.nttdocomo.android.idmanager";
    /**
     * Dアカウントアプリ Activity名.
     */
    private static final String D_ACCOUNT_APP_ACTIVITY_NAME = ".activity.DocomoIdTopActivity";
    /**
     * DアカウントアプリURI.
     */
    private static final String D_ACCOUNT_APP_URI = "https://play.google.com/store/apps/details?id=com.nttdocomo.android.idmanager";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_resetting_main_layout);

        //Headerの設定
        setTitleText("");
        enableHeaderBackIcon(false);
        enableStbStatusIcon(false);
        enableGlobalMenuIcon(false);
        setStatusBarColor(true);

        setContents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_daccount_resetting));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * 各クリックリスナーを登録する.
     */
    private void setContents() {

        mDAccountRegistrationhelp = findViewById(R.id.d_account_registration_help);
        mDAccountRegistrationhelp.setOnClickListener(this);

        mDAccountInputActivationCode = findViewById(R.id.d_account_input_activation_code);
        mDAccountInputActivationCode.setOnClickListener(this);

        mDAccountAgainPairing = findViewById(R.id.d_account_again_pairing);
        mDAccountAgainPairing.setOnClickListener(this);

        mDAccountLoginOtherAccount = findViewById(R.id.d_account_login_other_account);
        mDAccountLoginOtherAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        if (v.equals(mDAccountRegistrationhelp)) {
            // ヘルプ画面に遷移
            onDAccountRegButton();
        } else if (v.equals(mDAccountInputActivationCode)) {
            // アカウントアプリを起動する
            onDAccountRegton();
        } else if (v.equals(mDAccountAgainPairing)) {
            // 再度ペアリング
            onReturnButton();
        } else if (v.equals(mDAccountLoginOtherAccount)) {
            // アカウントアプリを起動する
            onDAccountRegton();
        }
    }

    /**
     * ペアリングの再実行を行う.
     */
    private void onReturnButton() {
       setFinishActivity();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setFinishActivity();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * アニメーションを付加してアクティビティを終了する.
     */
    private void setFinishActivity() {
        super.finish();
        //アニメーションを付加する
        overridePendingTransition(R.anim.in_righttoleft, R.anim.out_lefttoright);
    }

    /**
     * dアカウント設定ヘルプ.
     */
    private void onDAccountRegButton() {
        Intent intent = new Intent(getApplicationContext(), DaccountSettingHelpActivity.class);
        startActivity(intent);
    }

    /**
     * dアカウントアプリを起動する.
     */
    private void onDAccountRegton() {
        Intent intent = new Intent();
        intent.setClassName(D_ACCOUNT_APP_PACKAGE_NAME,
                D_ACCOUNT_APP_PACKAGE_NAME + D_ACCOUNT_APP_ACTIVITY_NAME);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //　アプリが無ければインストール画面に誘導
            CustomDialog dAccountUninstallDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
            Resources resources = getResources();
            dAccountUninstallDialog.setContent(resources.getString(R.string.main_setting_d_account_message));
            dAccountUninstallDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    Uri uri = Uri.parse(D_ACCOUNT_APP_URI);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            dAccountUninstallDialog.showDialog();
        }
    }
}