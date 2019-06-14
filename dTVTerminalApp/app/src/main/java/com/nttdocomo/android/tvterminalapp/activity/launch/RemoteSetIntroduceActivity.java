/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.util.List;

/**
 * リモート視聴設定.
 */
public class RemoteSetIntroduceActivity extends BaseActivity implements View.OnClickListener, DlnaManager.LocalRegisterListener,
        UserInfoDataProvider.UserDataProviderCallback {

    /** 画像の素材.*/
    private final int[] mImagResource = {R.mipmap.remote_setting_01, R.mipmap.remote_setting_02, R.mipmap.remote_setting_03};
    /** レイアウトのid.*/
    private final int[] mImagId = {R.id.remote_introduce_main_layout_img1, R.id.remote_introduce_main_layout_img2, R.id.remote_introduce_main_layout_img3};
    /** タブレット幅（スクリーンの67％）.*/
    private static final int IMAGE_WIDTH_PERCENT_100 = 100;
    /** タブレット幅（スクリーンの67％）.*/
    private static final int IMAGE_WIDTH_PERCENT_67 = 67;
    /** launchからの遷移.*/
    private boolean mIsFromLaunch = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mIsFromLaunch = intent.getBooleanExtra(ContentUtils.LAUNCH_REMOTE_SETTING, true);
        setContentView(R.layout.remote_introduce_main_layout);
        setTitleText(getString(R.string.remote_introduce_header));
        enableHeaderBackIcon(false);
        initView();
    }

    /**
     * ビュー初期化.
     */
    private void initView() {
        LinearLayout.LayoutParams layoutParams;
        TextView setBtn = findViewById(R.id.remote_introduce_main_layout_set_btn);
        TextView linkBtn = findViewById(R.id.remote_introduce_main_layout_tv_link);
        linkBtn.setPaintFlags(linkBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setBtn.setOnClickListener(this);
        linkBtn.setOnClickListener(this);
        for (int i = 0; i < mImagResource.length; i++) {
            Bitmap srcBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), mImagResource[i]);
            float margin = getResources().getDimension(R.dimen.stb_launch_padding_start_end);
            float maxWidth;
            if (ContentUtils.isTablet(getApplicationContext())) {
                maxWidth = getWidthDensity() / IMAGE_WIDTH_PERCENT_100 * IMAGE_WIDTH_PERCENT_67;
            } else {
                maxWidth = getWidthDensity() - margin * 2;
            }
            float ratio =  maxWidth / srcBitmap.getWidth();
            if (ratio > 0) {
                Matrix matrix = new Matrix();
                matrix.postScale(ratio, ratio);
                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
            }
            ImageView imageView = findViewById(mImagId[i]);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, srcBitmap.getHeight());
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(srcBitmap);
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
                executeLocalRegistration();
            } else {
                //h4d未契約として扱い（未契約である旨のエラーを表示すること）
                showRegistrationResultDialog(false, DlnaUtils.ExecuteLocalRegistrationErrorType.NO_H4D_CONTRACT, "");
            }
        }
    }

    /**
     * ローカルレジストレーション実施.
     */
    private void executeLocalRegistration() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DlnaUtils.ExecuteLocalRegistrationErrorType errorType =
                        DlnaUtils.executeLocalRegistration(getApplicationContext(), RemoteSetIntroduceActivity.this);
                if (DlnaUtils.ExecuteLocalRegistrationErrorType.NONE != errorType) {
                    showRegistrationResultDialog(false, errorType, DlnaManager.shared().getLocalRegistrationErrorCode());
                }
            }
        }).start();
    }

    /**
     * ローカルレジストレーションの処理結果.
     *
     * @param isSuccess true 成功 false 失敗
     * @param errorType エラータイプ
     * @param errorCode エラーコード
     */
    private void showRegistrationResultDialog(final boolean isSuccess, final DlnaUtils.ExecuteLocalRegistrationErrorType errorType, final String errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final CustomDialog resultDialog = DlnaUtils.getRegistResultDialog(RemoteSetIntroduceActivity.this, isSuccess, errorType, errorCode);
                resultDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        if (isSuccess) {
                            startTransition();
                        } else {
                            resultDialog.dismissDialog();
                        }
                        DlnaManager.shared().clearErrorCode();
                    }
                });
                setRemoteProgressVisible(View.GONE);
                sendRemoteEvent(isSuccess, errorType);
                resultDialog.showDialog();
            }
        });
    }

    /**
     * リモート視聴設定イベント送信.
     * @param isSuccess isSuccess
     * @param errorType errorType
     */
    private void sendRemoteEvent(final boolean isSuccess, final DlnaUtils.ExecuteLocalRegistrationErrorType errorType) {
        if (isSuccess) {
            sendEvent(getString(R.string.google_analytics_category_service_name_remote_viewing_settings),
                    getString(R.string.google_analytics_category_action_remote_success),
                    null, null);
        } else {
            sendEventTrackingByErrorType(errorType);
        }
    }

    /**
     * イベント送信.
     * @param errorType エラータイプ
     */
    private void sendEventTrackingByErrorType(final DlnaUtils.ExecuteLocalRegistrationErrorType errorType) {
        switch (errorType) {
            case ACTIVATION:
                sendEvent(getString(R.string.google_analytics_category_service_name_remote_viewing_settings),
                        getString(R.string.google_analytics_category_action_action_remote_activation_error),
                        null, null);
                break;
            case DEVICE_OVER:
                sendEvent(getString(R.string.google_analytics_category_service_name_remote_viewing_settings),
                        getString(R.string.google_analytics_category_action_remote_device_over_error),
                        null, null);
                break;
            case NO_H4D_CONTRACT:
                sendEvent(getString(R.string.google_analytics_category_service_name_remote_viewing_settings),
                        getString(R.string.google_analytics_category_action_remote_no_h4d_contract_error),
                        null, null);
                break;
            case START_DTCP:
            case START_DIRAG:
            case OTHER:
            case NONE:
            default:
                sendEvent(getString(R.string.google_analytics_category_service_name_remote_viewing_settings),
                        getString(R.string.google_analytics_category_action_remote_other_error),
                        null, null);
                break;
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.remote_introduce_main_layout_set_btn:
                setRemoteProgressVisible(View.VISIBLE);
                checkContractInfo(true);
                break;
            case R.id.remote_introduce_main_layout_tv_link:
                CustomDialog resultDialog = new CustomDialog(RemoteSetIntroduceActivity.this, CustomDialog.DialogType.ERROR);
                resultDialog.setOnTouchOutside(false);
                resultDialog.setCancelable(false);
                resultDialog.setOnTouchBackkey(false);
                resultDialog.setContent(getString(R.string.remote_introduce_dialog_txt));
                resultDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        startTransition();
                    }
                });
                resultDialog.showDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 遷移もとによって画面遷移を行う.
     */
    private void  startTransition() {
        if (mIsFromLaunch) {
            //Launchからの場合ホーム画面に遷移する
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            //設定画面からの場合、前画面に戻る
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DlnaManager.shared().StartDmp();
        if (mIsFromBgFlg) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_remote_set),
                    ContentUtils.getParingAndLoginCustomDimensions(RemoteSetIntroduceActivity.this));
        } else {
            SparseArray<String> customDimensions = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_REMOTE, ContentUtils.getRemoteSettingStatus(RemoteSetIntroduceActivity.this));
            super.sendScreenView(getString(R.string.google_analytics_screen_name_remote_set), customDimensions);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DlnaManager.shared().StopDmp();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mIsFromLaunch) {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRegisterCallBack(final boolean result, final DlnaUtils.ExecuteLocalRegistrationErrorType errorType, final String errorCode) {
        if (result) {
            //ローカルレジストレーションが成功したので日時を蓄積する
            SharedPreferencesUtils.setRegistTime(getApplicationContext());
        }
        showRegistrationResultDialog(result, errorType, errorCode);
    }

    @Override
    public void userInfoListCallback(final boolean isDataChange,
                                     final List<UserInfoList> userList, final boolean isUserContract) {
        startTvProgramIntentService();
        //契約情報確認、契約情報再取得不要
        checkContractInfo(false);
        String contractType = ContentUtils.getContractType(RemoteSetIntroduceActivity.this);
        if (!TextUtils.isEmpty(contractType)) {
            SparseArray<String> customDimensions = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTRACT, contractType);
            sendEvent(getString(R.string.google_analytics_category_service_name_contract),
                    getString(R.string.google_analytics_category_action_remote_contract_get_success),
                    null, customDimensions);
        }
    }
}
