/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.util.List;

/**
 * STB接続済みActivity.
 */
public class StbConnectActivity extends BaseActivity implements UserInfoDataProvider.UserDataProviderCallback,
        DlnaManager.LocalRegisterListener {

    /** 遅延時間.*/
    private static final int DELAYED_TIME = 3000;
    /**ハンドラー.*/
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_connect_main_layout);
        //SharedPreferenceにSTB接続完了をセット
        SharedPreferencesUtils.setSharedPreferencesStbConnect(this, true);
        setContents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DlnaManager.shared().StartDmp();
    }
    @Override
    protected void onStop() {
        super.onStop();
        DlnaManager.shared().StopDmp();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return false;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 画面上の表示をセットする.
     */
    private void setContents() {
        DTVTLogger.start();
        setTitleText(getString(R.string.str_app_title));
        enableHeaderBackIcon(false);
        setStatusBarColor(true);
        TextView connectResult = findViewById(R.id.connect_result_text);
        connectResult.setVisibility(View.VISIBLE);
        connectResult.setText(R.string.str_stb_connect_success_text);
        //契約情報確認、契約情報再取得要
        checkContractInfo(true);
        DTVTLogger.end();
    }
    /**
     * STB接続できたら、ホーム画面に自動遷移する.
     */
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                    StbConnectActivity.this, true);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    @Override
    public void userInfoListCallback(final boolean isDataChange, final List<UserInfoList> userList) {
        //契約情報確認、契約情報再取得不要
        checkContractInfo(false);
    }

    /**
     * ユーザ状態判定.
     *
     * @param doGetUserInfo 契約情報再取得要否
     */
    private void checkContractInfo(final boolean doGetUserInfo) {
        String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(this));
        DTVTLogger.debug("contractInfo: " + contractInfo);
        if ((contractInfo == null || contractInfo.isEmpty() || UserInfoUtils.CONTRACT_INFO_NONE.equals(contractInfo))
                && doGetUserInfo) {
            new UserInfoDataProvider(this, this).getUserInfo();
        } else {
            if (UserInfoUtils.CONTRACT_INFO_H4D.equals(contractInfo)) {
                //H4d契約済の場合ローカルレジストレーションを促すダイアログを表示
                showRemoteConfirmDialog();
            } else {
                //ホーム画面に遷移
                mHandler.postDelayed(runnable, DELAYED_TIME);
            }
        }
    }

    /**
     * ローカルレジストレーションを促すダイアログを表示.
     */
    private void showRemoteConfirmDialog() {
        //　アプリが無ければインストール画面に誘導
        CustomDialog remoteConfirmDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        remoteConfirmDialog.setContent(getResources().getString(R.string.main_setting_remote_confirm_message));
        remoteConfirmDialog.setConfirmText(R.string.positive_response);
        remoteConfirmDialog.setCancelText(R.string.negative_response);
        remoteConfirmDialog.setOnTouchOutside(false);
        remoteConfirmDialog.setCancelable(false);
        remoteConfirmDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                if (isOK) {
                    setRemoteProgressVisible(View.VISIBLE);
                    boolean result = DlnaUtils.getActivationState(StbConnectActivity.this);
                    if (result) {
                        DlnaManager.shared().mLocalRegisterListener = StbConnectActivity.this;
                        DlnaManager.shared().StartDtcp();
                        DlnaManager.shared().RestartDirag();
                        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(StbConnectActivity.this);
                        DlnaManager.shared().RequestLocalRegistration(dlnaDmsItem.mUdn);
                    } else {
                        setRemoteProgressVisible(View.GONE);
                        showActivationErrorDialog();
                    }
                }
            }
        });
        remoteConfirmDialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                //ホーム画面に遷移
                mHandler.post(runnable);
            }
        });
        remoteConfirmDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
            @Override
            public void allDismissCallback() {
                //NOP
            }
            @Override
            public void otherDismissCallback() {
                //ホーム画面に遷移
                mHandler.post(runnable);
            }
        });
        remoteConfirmDialog.showDialog();
    }

    @Override
    public void onRegisterCallBack(final boolean result, final DlnaManager.LocalRegistrationErrorType errorType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setRemoteProgressVisible(View.GONE);
                showRegistResultDialog(result, errorType);
            }
        });
    }

    /**
     * ローカルレジストレーションの処理結果.
     *
     * @param isSuccess true 成功 false 失敗
     * @param errorType エラータイプ
     */
    private void showRegistResultDialog(final boolean isSuccess, final DlnaManager.LocalRegistrationErrorType errorType) {
        CustomDialog resultDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        resultDialog.setOnTouchOutside(false);
        resultDialog.setCancelable(false);
        if (isSuccess) {
            resultDialog.setContent(getString(R.string.common_text_regist_progress_done));
        } else {
            switch (errorType) {
                case OVER:
                    resultDialog.setContent(getString(R.string.common_text_regist_over_error));
                    break;
                default:
                    resultDialog.setContent(getString(R.string.common_text_regist_other_error));
                    break;
            }
            resultDialog.setConfirmText(R.string.common_text_close);
        }
        resultDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                mHandler.post(runnable);
            }
        });
        resultDialog.showDialog();
    }

    /**
     * アクティベーションのエラー表示.
     */
    private void showActivationErrorDialog() {
        CustomDialog resultDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        resultDialog.setOnTouchOutside(false);
        resultDialog.setCancelable(false);
        resultDialog.setContent(getString(R.string.activation_failed_msg));
        resultDialog.setConfirmText(R.string.common_text_close);
        resultDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                mHandler.post(runnable);
            }
        });
        resultDialog.showDialog();
    }
}