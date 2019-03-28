/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.app.Activity;
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
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
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

    /**
     * 起動モード初期値.
     */
    private int mStartMode = 0;
    /**
     * 起動モードキー名.
     */
    public static final String FROM_WHERE = "FROM_WHERE";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_resetting_main_layout);
        Intent intent = getIntent();
        if (intent != null) {
           mStartMode = intent.getIntExtra(StbSelectActivity.FROM_WHERE, -1);
        }
        //Headerの設定
        setTitleText(getString(R.string.str_stb_paring_setting_title));
        enableHeaderBackIcon(false);
        enableGlobalMenuIcon(false);

        setContents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_daccount_resetting),
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(DaccountResettingActivity.this) : null);
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
        if (v.getId() == R.id.d_account_registration_help) {
            // ヘルプ画面に遷移
            onDAccountRegButton();
        } else if (v.getId() == R.id.d_account_input_activation_code) {
            // アカウントアプリを起動する
            onDAccountRegton();
        } else if (v.getId() == R.id.d_account_again_pairing) {
            // 再度ペアリング
            onReturnButton();
        } else if (v.getId() == R.id.d_account_login_other_account) {
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
        intent.putExtra(FROM_WHERE, mStartMode);
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

    @Override
    protected void restartMessageDialog(final String... message) {
        //呼び出し用のアクティビティの退避
        final Activity activity = this;

        //出力メッセージのデフォルトはdアカウント用
        String printMessage = getString(R.string.d_account_change_message);

        //引数がある場合はその先頭を使用する
        if (message != null && message.length > 0) {
            printMessage = message[0];
        }

        //初期フローの場合はダイアログ出さない
        if (mStartMode == StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            return;
        }
        //ダイアログを、OKボタンのコールバックありに設定する
        CustomDialog restartDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        //枠外を押した時の操作を無視するように設定する
        restartDialog.setOnTouchOutside(false);
        restartDialog.setContent(printMessage);
        //startAppDialog.setTitle(getString(R.string.dTV_content_service_start_dialog));
        restartDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                //OKが押されたので、ホーム画面の表示
                reStartApplication();
            }
        });
        restartDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
            @Override
            public void allDismissCallback() {
                //NOP
            }

            @Override
            public void otherDismissCallback() {
                //OKが押されたのと同じ、ホーム画面の表示
                reStartApplication();
            }
        });
        restartDialog.showDialog();
    }
}