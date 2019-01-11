/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.setting;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.MyChannelEditActivity;
import com.nttdocomo.android.tvterminalapp.adapter.MainSettingListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.MainSettingUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 設定画面.
 */
public class SettingActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        DlnaManager.LocalRegisterListener {

    /**
     * 項目名の配列を保持.
     */
    private String[] mItemName = null;
    /**
     * Resourcesを保持.
     */
    private Resources mResources = null;
    /**
     * 表示するリストのアダプタ.
     */
    private MainSettingListAdapter mAdapter = null;
    /**
     * 表示するリスト.
     */
    private final List<MainSettingUtils> mSettingList = new ArrayList<>();

    /**
     * 空白文字.
     */
    private static final String BLANK = "";

    /**
     * メニュー項目index（Dアカウント設定）.
     */
    private static final int SETTING_MENU_INDEX_D_ACCOUNT = 0;

    /**
     * メニュー項目index（ペアリング設定）.
     */
    private static final int SETTING_MENU_INDEX_PAIRING = 1;

    /**
     * メニュー項目index（マイ番組表設定）.
     */
    private static final int SETTING_MENU_INDEX_MY_PROGRAM = 2;

    /**
     * メニュー項目index（リモート視聴設定）.
     */
    private static final int SETTING_MENU_INDEX_REMOTE = 3;

    /**
     * メニュー項目index（外出先視聴時の画質設定）.
     */
    private static final int SETTING_MENU_INDEX_QUALITY = 4;

    /**
     * メニュー項目index（FAQ）.
     */
    private static final int SETTING_MENU_INDEX_FAQ = 5;

    /**
     * メニュー項目index（アプリケーション情報）.
     */
    private static final int SETTING_MENU_INDEX_APP_INFO = 6;

    /**
     * メニュー項目index（ライセンス）.
     */
    private static final int SETTING_MENU_INDEX_LICENCE = 7;

    /**
     * メニュー項目index（プライバシーポリシー）.
     */
    private static final int SETTING_MENU_INDEX_PRIVACY_POLICY = 8;

    /**
     * メニュー項目index（APP）.
     */
    private static final int SETTING_MENU_INDEX_APP_PRIVACY_POLICY = 9;

    /**
     * メニュー項目index（利用規約）.
     */
    private static final int SETTING_MENU_INDEX_AGREEMENT = 10;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        DTVTLogger.start();
        setContentView(R.layout.setting_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.nav_menu_item_setting));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        setHeaderColor(true);

        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        //設定画面に表示するデータを設定する
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        ListView mListView = findViewById(R.id.main_setting_list);
        mAdapter = new MainSettingListAdapter(this, R.layout.setting_main_list_layout, mSettingList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        checkIsPairing();
        checkImageQuality();
        if (mIsFromBgFlg) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_setting),
                    ContentUtils.getParingAndLoginCustomDimensions(SettingActivity.this));
        } else {
            UserState userState = UserInfoUtils.getUserState(SettingActivity.this);
            String loginStatus;
            if (UserState.LOGIN_NG.equals(userState)) {
                loginStatus = getString(R.string.google_analytics_custom_dimension_login_ng);
            } else {
                loginStatus = getString(R.string.google_analytics_custom_dimension_login_ok);
            }
            SparseArray<String> customDimensions = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_LOGIN, loginStatus);
            super.sendScreenView(getString(R.string.google_analytics_screen_name_setting), customDimensions);
        }
    }

    @SuppressWarnings("OverlyComplexMethod")
    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        String tappedItemName = mSettingList.get(i).getText();

        if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_D_ACCOUNT])) {
            //Dアカウント設定
            DaccountUtils.startDAccountApplication(SettingActivity.this);
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_PAIRING])) {
            if (isSettingPossible(false, tappedItemName)) {
                //ペアリング設定
                Intent intent = new Intent(getApplicationContext(), StbSelectActivity.class);
                intent.putExtra(StbSelectActivity.FROM_WHERE, StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Setting.ordinal());
                startActivity(intent);
            }
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_MY_PROGRAM])) {
            if (isSettingPossible(true, tappedItemName)) {
                //マイ番組表設定
                startActivity(MyChannelEditActivity.class, null);
            }
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_REMOTE])) {
            if (isSettingPossible(true, tappedItemName)) {
                //ローカルレジストレーションを促すダイアログを表示
                showRemoteConfirmDialog();
            }
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_QUALITY])) {
            if (isSettingPossible(true, tappedItemName)) {
                //外出先視聴時の画質設定画面への遷移
                Intent intent = new Intent(this, SettingImageQualityActivity.class);
                intent.putExtra(getString(R.string.main_setting_quality_status), mSettingList.get(i).getStateText());
                startActivity(intent);
            }
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_FAQ])) {
            //FAQ
            startActivity(SettingMenuFaqActivity.class, null);
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_APP_INFO])) {
            //アプリケーション情報への遷移
            startActivity(ApplicationInfoActivity.class, null);
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_LICENCE])) {
            //ライセンス
            startActivity(SettingMenuLicenseActivity.class, null);
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_PRIVACY_POLICY])) {
            //プライバシーポリシー
            startBrowser(UrlConstants.WebUrl.SETTING_MENU_PRIVACY_POLICY_URL);
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_APP_PRIVACY_POLICY])) {
            //APP
            startActivity(SettingMenuAppActivity.class, null);
        } else if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_AGREEMENT])) {
            //利用規約
            startActivity(SettingMenuTermsOfServiceActivity.class, null);
        }
    }

    /**
     * 設定画面に表示する情報をリストに入れる.
     */
    private void initData() {
        mResources = getResources();
        mItemName = mResources.getStringArray(R.array.main_setting_items);

        // ペアリング状態の確認
        String isParing = mResources.getString(R.string.main_setting_pairing);
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (dlnaDmsItem.mControlUrl.isEmpty()) {
            // 未ペアリング時
            isParing = mResources.getString(R.string.main_setting_not_paring);
        }

        //画質設定の設定値を確認
        String imageQuality = SharedPreferencesUtils.getSharedPreferencesImageQuality(this);
        if (imageQuality.isEmpty()) {
            //値が保存されていない場合は初期値を設定
            imageQuality = mResources.getString(R.string.main_setting_image_quality_high);
            /* test code begin */
            SharedPreferencesUtils.setSharedPreferencesImageQuality(this, imageQuality);
            /* test code end */
        }

        String dAccountId = SharedPreferencesUtils.getSharedPreferencesDaccountId(this);

        //項目名、設定値、>の画像、カテゴリ情報
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_D_ACCOUNT], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_PAIRING], isParing));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_MY_PROGRAM], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_REMOTE], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_QUALITY], imageQuality));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_FAQ], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_APP_INFO], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_LICENCE], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_PRIVACY_POLICY], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_APP_PRIVACY_POLICY], BLANK));
        mSettingList.add(new MainSettingUtils(mItemName[SETTING_MENU_INDEX_AGREEMENT], BLANK));
    }

    /**
     * ユーザ状態判定.
     *
     * @param isDisplay 表示フラグ
     * @param tappedItemName tappedItemName
     * @return 設定操作可否
     */
    private boolean isSettingPossible(final boolean isDisplay, final String tappedItemName) {
        UserState userState = UserInfoUtils.getUserState(this);
        if (userState.equals(UserState.LOGIN_NG)) {
            //未ログインならダイアログ表示
            settingErrorDialog(R.string.main_setting_logon_request_error_message);
            return false;
        } else if (userState.equals(UserState.LOGIN_OK_CONTRACT_NG)) {
            if (isDisplay) {
                //未契約ならダイアログ表示
                settingErrorDialog(R.string.main_setting_h4d_not_signed_message);
                return false;
            }
        }
        if (tappedItemName.equals(mItemName[SETTING_MENU_INDEX_REMOTE])) {
            StbConnectionManager.ConnectionStatus connectionStatus = StbConnectionManager.shared().getConnectionStatus();
            switch (connectionStatus) {
                case NONE_PAIRING: //未ペアリングならダイアログ表示
                    settingErrorDialog(R.string.main_setting_remote_paring_request);
                    return false;
                case HOME_IN:
                    break;
                case HOME_OUT:
                case NONE_LOCAL_REGISTRATION:
                case HOME_OUT_CONNECT:
                    //宅外ならダイアログ表示
                    settingErrorDialog(R.string.main_setting_stb_not_connected_message);
                    return false;
            }
        }
        return true;
    }

    /**
     * 未ログイン時のダイアログ表示.
     * @param resId エラーメッセージ
     */
    private void settingErrorDialog(final int resId) {
        //　アプリが無ければインストール画面に誘導
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        customDialog.setContent(getResources().getString(resId));
        customDialog.showDialog();
    }

    /**
     * 再ペアリング画面からの復帰時に値を確認し、変更されていた場合は更新を行う.
     */
    private void checkIsPairing() {
        String isParing = mResources.getString(R.string.main_setting_pairing);
        if (!SharedPreferencesUtils.getSharedPreferencesDecisionParingSettled(this)) {
            // 未ペアリング時
            isParing = mResources.getString(R.string.main_setting_not_paring);
        }
        for (int i = 0; i < mSettingList.size(); i++) {
            if (mSettingList.get(i).getText().equals(mItemName[SETTING_MENU_INDEX_PAIRING])) {
                mSettingList.set(i, new MainSettingUtils(mItemName[SETTING_MENU_INDEX_PAIRING], isParing));
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 外出先視聴時の画質設定からの復帰時に値を確認し、変更されていた場合変更を行う.
     */
    private void checkImageQuality() {
        String imageQuality = SharedPreferencesUtils.getSharedPreferencesImageQuality(this);
        for (int i = 0; i < mSettingList.size(); i++) {
            if (mSettingList.get(i).getText().equals(mItemName[SETTING_MENU_INDEX_QUALITY])) {
                mSettingList.set(i, new MainSettingUtils(mItemName[SETTING_MENU_INDEX_QUALITY], imageQuality));
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (closeDrawerMenu()) {
                    return false;
                }
                contentsDetailBackKey(null);
                return false;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ローカルレジストレーションを促すダイアログを表示.
     */
    private void showRemoteConfirmDialog() {
        CustomDialog remoteConfirmDialog = DlnaUtils.getRemoteConfirmDialog(SettingActivity.this);
        remoteConfirmDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                if (isOK) {
                    setRemoteProgressVisible(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DlnaUtils.ExcuteLocalRegistrationErrorType errorType =
                                    DlnaUtils.excuteLocalRegistration(getApplicationContext(), SettingActivity.this);
                            boolean result = DlnaUtils.ExcuteLocalRegistrationErrorType.NONE.equals(errorType);
                            if (!result) {
                                showRegistResultDialog(false, errorType);
                            }
                        }
                    }).start();
                }
            }
        });
        remoteConfirmDialog.showDialog();
    }

    @Override
    public void onRegisterCallBack(final boolean result, final DlnaUtils.ExcuteLocalRegistrationErrorType errorType) {
        if (result) {
            //ローカルレジストレーションが成功したので日時を蓄積する
            SharedPreferencesUtils.setRegistTime(getApplicationContext());
        }
        showRegistResultDialog(result, errorType);
    }

    /**
     * ローカルレジストレーションの処理結果.
     * @param errorType errorType
     * @param isSuccess true 成功 false 失敗
     */
    private void showRegistResultDialog(final boolean isSuccess, final DlnaUtils.ExcuteLocalRegistrationErrorType errorType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog resultDialog = DlnaUtils.getRegistResultDialog(SettingActivity.this, isSuccess, errorType);
                setRemoteProgressVisible(View.GONE);
                resultDialog.showDialog();
            }
        });
    }

}