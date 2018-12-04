/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
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
    private final Handler mHandler = new Handler();
    /**起動モード.*/
    private int mStartMode = 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_connect_main_layout);
        Intent intent = getIntent();
        if (intent != null) {
            mStartMode = intent.getIntExtra(StbSelectActivity.FROM_WHERE, -1);
        }
        //SharedPreferenceにSTB接続完了をセット
        SharedPreferencesUtils.setSharedPreferencesStbConnect(this, true);
        StbConnectionManager.shared().setConnectionStatus(StbConnectionManager.ConnectionStatus.HOME_IN);
        setContents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DlnaManager.shared().StopDmp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DlnaManager.shared().StartDmp();
        if (mIsFromBgFlg) {
            String screenName;
            if (mStartMode == StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                screenName = getString(R.string.google_analytics_screen_name_paring_completed);
            } else {
                screenName = getString(R.string.google_analytics_screen_name_setting_paring_completed);
            }
            super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(StbConnectActivity.this));
        } else {
            SparseArray<String> customDimensions = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_PAIRING, getString(R.string.google_analytics_custom_dimension_pairing_ok));
            if (mStartMode == StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                super.sendScreenView(getString(R.string.google_analytics_screen_name_paring_completed), customDimensions);
            } else {
                super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_paring_completed), customDimensions);
            }
        }
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
        if (mStartMode == StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            setTitleText(getString(R.string.str_app_title));
        } else if (mStartMode == StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            setTitleText(getString(R.string.str_stb_paring_setting_title));
        }
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
    public void userInfoListCallback(final boolean isDataChange,
             final List<UserInfoList> userList, final boolean isUserContract) {
        startTvProgramIntentService();
        //契約情報確認、契約情報再取得不要
        checkContractInfo(false);
        String contractType = ContentUtils.getContractType(StbConnectActivity.this);
        if (!TextUtils.isEmpty(contractType)) {
            SparseArray<String> customDimensions = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTRACT, contractType);
            sendEvent(getString(R.string.google_analytics_category_service_name_contract),
                    getString(R.string.google_analytics_category_action_remote_contract_get_success),
                    null, customDimensions);
        }
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
        remoteConfirmDialog.setContent(getResources().getString(R.string.main_setting_remote_confirm_message_first_start));
        remoteConfirmDialog.setConfirmText(R.string.custom_dialog_ok);
        remoteConfirmDialog.setCancelText(R.string.custom_dialog_cancel);
        remoteConfirmDialog.setOnTouchOutside(false);
        remoteConfirmDialog.setCancelable(false);
        remoteConfirmDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                if (isOK) {
                    setRemoteProgressVisible(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result = DlnaUtils.getActivationState(StbConnectActivity.this);
                            DTVTLogger.warning("result = " + result);
                            if (result) {
                                DlnaManager.shared().mLocalRegisterListener = StbConnectActivity.this;
                                DlnaManager.shared().StartDtcp();
                                //初回使用からRestartで良いとの事なので、StartDiragへの変更は無用
                                DlnaManager.shared().RestartDirag();
                                DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(StbConnectActivity.this);
                                DlnaManager.shared().RequestLocalRegistration(dlnaDmsItem.mUdn, getApplicationContext());
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setRemoteProgressVisible(View.GONE);
                                        showActivationErrorDialog();
                                    }
                                });
                            }
                        }
                    }).start();
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
        if (result) {
            //ローカルレジストレーションが成功したので日時を蓄積する
            SharedPreferencesUtils.setRegistTime(getApplicationContext());
        }

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
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(StbConnectActivity.this);
            String expireDate = DlnaManager.shared().GetRemoteDeviceExpireDate(dlnaDmsItem.mUdn);
            SharedPreferencesUtils.setRemoteDeviceExpireDate(StbConnectActivity.this, expireDate);
        } else {
            switch (errorType) {
                case OVER:
                    resultDialog.setContent(getString(R.string.common_text_regist_over_error_setting));
                    break;
                case NONE:
                case UNKNOWN:
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
        resultDialog.setContent(getString(R.string.activation_failed_error_local_registration));
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
