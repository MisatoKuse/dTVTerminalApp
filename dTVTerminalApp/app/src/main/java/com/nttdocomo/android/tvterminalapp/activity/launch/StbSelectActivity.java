/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsInfo;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.relayclient.RelayServiceResponseMessage;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountControl;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountReceiver;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.IDimDefines;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.OttGetAuthSwitch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ペアリング、再ペアリングするためのクラス.
 */
public class StbSelectActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        DlnaManager.DlnaManagerListener,
        CustomDialog.DismissCallback,
        DaccountReceiver.DaccountChangedCallBackForStb {

    // region variable
    /**
     * DMSリスト.
     */
    private final List<DlnaDmsItem> mDlnaDmsItemList = new ArrayList<>();
    private final DlnaDmsInfo mDlnaDmsInfo = new DlnaDmsInfo();
    /**
     * プログレスバー.
     */
    private ProgressBar mLoadMoreView = null;

    /**
     * タイムアウト設定クラス.
     */
    private StbInfoCallBackTimer mCallbackTimer = null;
    /**
     * DMS一覧リスト.
     */
    private final List<HashMap<String, String>> mContentsList = new ArrayList<>();
    /**
     * 起動モード初期値.
     */
    private int mStartMode = 0;
    /**
     * 選択されたSTBデバイス番号.
     */
    private int mSelectDevice = SELECT_DEVICE_ITEM_DEFAULT;
    /**
     * dアカウントアプリインストールフラグ.
     */
    private boolean mIsAppDL = false;
    /**
     * デバイスアダプター.
     */
    private SimpleAdapter mDeviceAdapter;
    /**
     * チェックマーク.
     */
    private ImageView mCheckMark;
    /**
     * ペアリング中Image.
     */
    private ImageView mPairingImage;
    /**
     * ペアリングしないでアプリを利用するTextView.
     */
    private TextView mParingTextView = null;
    /**
     * ペアリングされたデバイス名を表示するためのTextView.
     */
    private TextView mParingDevice;
    /**
     * DMSリストビュー.
     */
    private ListView mDeviceListView;
    /**
     * ペアリング設定画面Divider.
     */
    private TextView mTextDivider1;
    /**
     * ペアリング設定画面Divider.
     */
    private TextView mTextDivider2;
    /**
     * TextView.
     */
    private TextView mDeviceSelectText;
    /**
     * 新dアカウント設定フラグ.
     */
    private boolean mNewDaccountGet = false;
    /**
     * 起動モードキー名.
     */
    public static final String FROM_WHERE = "FROM_WHERE";
    /**
     * Dアカウントアプリ Package名.
     */
    private static final String D_ACCOUNT_APP_PACKAGE_NAME = "com.nttdocomo.android.idmanager";
    /**
     * Dアカウントアプリ Activity名.
     */
    private static final String D_ACCOUNT_APP_ACTIVITY_NAME = ".activity.DocomoIdTopActivity";
    /**
     * マージンZero.
     */
    private static final int MARGIN0 = 0;
    /**
     * プログレスバーマージンTop値.
     */
    private static final int MARGIN84 = 84;
    /**
     * プログレスバーサイズ.
     */
    private static final int PROGRESSSIZE = 16;
    /**
     * ペアリング設定画面プログレスバーマージンTop値.
     */
    private static final int MARGIN23 = 23;
    /**
     * ペアリング設定画面プログレスバーマージンRight値.
     */
    private static final int MARGIN9 = 9;
    /**
     * デバイスFriendNameキー名.
     */
    private static final String DEVICE_NAME_KEY = "DEVICE_NAME_KEY";
    /**
     * デバイスを選択してDアカウントを登録フラグ.
     */
    private boolean mDaccountFlag = false;
    /**
     * OTT処理完了フラグ.
     */
    private boolean mOttGetComplete = false;
    /**
     * デバイス選択デフォルト値.
     */
    private static final int SELECT_DEVICE_ITEM_DEFAULT = -1;
    /**
     * アプリケーションバージョンチェックダイアログ表示中フラグ.
     */
    private boolean mIsApplicationVersionCheckIncompatibleDialogFlag = false;
    /**
     * dアカウントエラーダイアログ表示中フラグ.
     */
    private boolean mIsDAccountErrorDialogShowing = false;
    /**
     * dアカウント単純起動中フラグ.
     */
    private boolean mIsDAccountAppStarting = false;
    /**
     * 最大30sほどかかります.
     */
    private TextView mIsSearchingTextView;
    /**
     * STB検出ステータス.
     */
    private  TextView mStatusTextView;
    /**
     * STB検出エラータイプ.
     */
    public static final  String ERROR_TYPE = "ERROR_TYPE ";

    /**
     * タイマーステータス.
     */
    private enum TimerStatus {
        /**
         * 初期状態.
         */
        TIMER_STATUS_DEFAULT,
        /**
         * 起動中.
         */
        TIMER_STATUS_DURING_STARTUP,
        /**
         * タイマー処理実行済み.
         */
        TIMER_STATUS_EXECUTION,
        /**
         * キャンセル.
         */
        TIMER_STATUS_CANCEL,
    }

    /**
     * 起動モード.
     */
    public enum StbSelectFromMode {
        /**
         * ランチャーから起動.
         */
        StbSelectFromMode_Launch,
        /**
         * 設定から起動.
         */
        StbSelectFromMode_Setting,
    }
    // endregion variable

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setTitleText(getString(R.string.str_stb_paring_setting_title));
        Intent intent = getIntent();
        if (intent != null) {
            mStartMode = intent.getIntExtra(FROM_WHERE, -1);
        }
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            setContentView(R.layout.stb_select_main_layout);
            initLaunchView();
        } else if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            if (savedInstanceState != null) {
                savedInstanceState.clear();
            }
            setContentView(R.layout.stb_select_device_list_setting);
            enableHeaderBackIcon(true);
            initSettingView();
        }
        // dアカウントチェック処理は後ほど自力で呼び出しているため不要を宣言
        setUnnecessaryDaccountRegistService();
        DTVTLogger.end();
    }

    /**
     * 設定から起動時のビューの初期化.
     */
    private void initSettingView() {
        mDeviceListView = findViewById(R.id.stb_device_name_setting_list);
        mDeviceAdapter = new SimpleAdapter(this, mContentsList, R.layout.stb_device_name_setting_layout,
                new String[]{DEVICE_NAME_KEY}, new int[]{R.id.stb_search_device_name_setting});
        mParingDevice = findViewById(R.id.paring_device_select_setting_text);
        mCheckMark = findViewById(R.id.stb_device_check_setting_mark);
        mDeviceListView.setAdapter(mDeviceAdapter);
        mDeviceListView.setOnItemClickListener(this);
        mLoadMoreView = findViewById(R.id.stb_device_setting_progressbar);
        mParingTextView = findViewById(R.id.use_without_paring_setting);
        mPairingImage = findViewById(R.id.paring_search_image_setting);
        mParingTextView.setVisibility(View.VISIBLE);
        mParingTextView.setOnClickListener(this);

        mTextDivider1 = findViewById(R.id.paring_device_select_setting_divider1);
        mTextDivider2 = findViewById(R.id.paring_device_select_setting_divider2);
        mDeviceSelectText = findViewById(R.id.paring_device_select_text);
    }

    /**
     * ランチャーから起動時のビューの初期化.
     */
    private void initLaunchView() {
        DTVTLogger.start();
        mDeviceListView = findViewById(R.id.stb_device_name_list);
        mPairingImage = findViewById(R.id.paring_search_image);
        mDeviceAdapter = new SimpleAdapter(this, mContentsList, R.layout.device_name_layout,
                new String[]{DEVICE_NAME_KEY}, new int[]{R.id.item_search_result_device_name});
        mDeviceListView.setAdapter(mDeviceAdapter);
        mDeviceListView.setOnItemClickListener(this);
        mDeviceListView.setVisibility(View.VISIBLE);
        mLoadMoreView = findViewById(R.id.stb_device_progress);
        mParingTextView = findViewById(R.id.useWithoutPairingButton);
        mParingTextView.setPaintFlags(mParingTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mParingTextView.setOnClickListener(this);
        mParingTextView.setVisibility(View.VISIBLE);
        mStatusTextView = findViewById(R.id.stb_select_status_text);
        mIsSearchingTextView = findViewById(R.id.stb_select_latency_text);
    }

    @Override
    protected void checkDAccountOnRestart() {
        DTVTLogger.start();
    }

    /**
     * 画面上に表示するコンテンツの初期化を行う.
     */
    private void initView() {
        DTVTLogger.start();
        if (mStartMode == (StbSelectFromMode.StbSelectFromMode_Launch.ordinal())) {
            //初回起動時
            enableHeaderBackIcon(false);
            enableGlobalMenuIcon(false);
            mPairingImage.setImageResource(R.mipmap.startup_icon_01);
            return;
        } else if (mStartMode == (StbSelectFromMode.StbSelectFromMode_Setting.ordinal())) {
            ImageView pairingImageView = findViewById(R.id.paring_search_image_setting);
            TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);
            if (pairingImageView.getVisibility() == View.VISIBLE && statusSetting.getVisibility() == View.VISIBLE) {
                pairingImageView.setVisibility(View.GONE);
                statusSetting.setVisibility(View.GONE);
            }
            mLoadMoreView = findViewById(R.id.stb_device_setting_progressbar);
            //設定画面からの遷移
            enableHeaderBackIcon(true);
            enableStbStatusIcon(true);
            enableGlobalMenuIcon(true);

            TextView stbSearchFailed = findViewById(R.id.stb_device_search_result);
            TextView stbSearchHelp = findViewById(R.id.stb_paring_failed_red_help);

            if (stbSearchFailed.getVisibility() == View.VISIBLE && stbSearchHelp.getVisibility() == View.VISIBLE) {
                stbSearchFailed.setVisibility(View.GONE);
                stbSearchHelp.setVisibility(View.GONE);
            }
            if (mDeviceSelectText.getVisibility() == View.GONE && mTextDivider1.getVisibility() == View.GONE && mTextDivider2.getVisibility() == View.GONE) {
                mDeviceSelectText.setVisibility(View.VISIBLE);
                mTextDivider1.setVisibility(View.VISIBLE);
                mTextDivider2.setVisibility(View.VISIBLE);
            }
            mDeviceListView.setVisibility(View.VISIBLE);
            //sharedPreferencesからSTB情報を取得する
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
            //リモコンUIを出す
            findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
            ImageView mMenuImageView = findViewById(R.id.header_layout_menu);
            mMenuImageView.setVisibility(View.VISIBLE);
            mMenuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (isFastClick()) {
                        displayGlobalMenu();
                    }
                }
            });

            if (TextUtils.isEmpty(dlnaDmsItem.mUdn)) {
                //未ペアリング
                return;
            } else {
                //ペアリング済み
                mTextDivider1.setVisibility(View.VISIBLE);
                mParingDevice.setText(dlnaDmsItem.mFriendlyName);
                if (!mParingDevice.getText().toString().isEmpty()) {
                    mParingDevice.setVisibility(View.VISIBLE);
                    mCheckMark.setVisibility(View.VISIBLE);
                    mParingDevice.setTextColor(Color.WHITE);
                    mParingTextView.setText(R.string.str_stb_paring_cancel_text);
                }
            }
        } else {
            DTVTLogger.debug("StbSelectFromMode :StartMode Error ");
        }
        DTVTLogger.end();
    }

    @Override
    public void onResume() {
        super.onResume();
        DTVTLogger.start();
        //新しいdアカウントが送られていた場合は、STB選択画面の再起動を行う
        if (mNewDaccountGet) {
            reStartApplication();
            return;
        }
        mIsDAccountAppStarting = false;
        if (mIsFromBgFlg) {
            String screenName;
            if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                screenName = getString(R.string.google_analytics_screen_name_stb_select);
            } else {
                screenName = getString(R.string.google_analytics_screen_name_setting_paring);
            }
            super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(StbSelectActivity.this));
        }
        initResumeView();
        startDlnaSearch();

        //dアカウント変更コールバックの設定
        DaccountReceiver.setDaccountChangedCallBackForStb(this);

        DTVTLogger.end();
    }

    /**
     * 初期状態に戻る.
     */
    private void initResumeView() {
        DTVTLogger.start();
        mSelectDevice = SELECT_DEVICE_ITEM_DEFAULT;
        mParingTextView.setVisibility(View.VISIBLE);
        initView();
        if (mIsAppDL) {
            //dアカウントアプリDLからの戻り時、各種Viewを初期状態に戻す
            mIsAppDL = false;
            if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                mPairingImage.setImageResource(R.mipmap.startup_icon_01);
                mDeviceListView.setVisibility(View.VISIBLE);
                super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select), null);
            } else {
                mPairingImage.setVisibility(View.GONE);
                TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);
                statusSetting.setVisibility(View.GONE);
                mDeviceSelectText.setVisibility(View.VISIBLE);
                mTextDivider2.setVisibility(View.VISIBLE);
                mDeviceListView.setVisibility(View.VISIBLE);
                super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_paring), null);
            }
        }
        DTVTLogger.end();
    }

    /**
     * DMP検索スタート.
     */
    private void startDlnaSearch() {
        DlnaManager.shared().StopDmp();
        DlnaManager.shared().mDlnaManagerListener = null;
        //STB検索開始
        DlnaManager.shared().mDlnaManagerListener = this;
        if (DlnaManager.shared().getContext() == null) {
            DlnaManager.shared().setContext(getApplicationContext());
        }
        DlnaManager.shared().StartDmp();
        //デバイスリストの更新
        updateDeviceList(null, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        DTVTLogger.start();
        leaveActivity();
        displayMoreData(false);

        if (isFinishing()) {
            //dアカウント受け取りコールバックの解除
            DaccountReceiver.setDaccountChangedCallBackForStb(null);
        }
        DTVTLogger.end();
    }

    /**
     * 画面から離れる場合の処理.
     */
    private void leaveActivity() {
        DTVTLogger.start();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallbackTimer != null
                    && mCallbackTimer.getTimerStatus() != TimerStatus.TIMER_STATUS_CANCEL) {
                    //タイマー停止処理・飛び先でcancel等を行っているので、ここにcancel()等の処理は無用
                    stopCallbackTimer();
                }
            }
        });
        DlnaManager.shared().mDlnaManagerListener = null;
        DlnaManager.shared().StopDmp();
        mDlnaDmsInfo.clear();
    }

    /**
     * STB検索中の画面表示を設定.
     */
    private void showSearchingView() {
        DTVTLogger.start();
        // STB検索中文言表示
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            mStatusTextView.setText(getString(R.string.str_stb_select_result_text_search));
            mIsSearchingTextView.setVisibility(View.VISIBLE);
            super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select_loading), null);
        } else if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            //プログレスビューを初期状態に戻る
            RelativeLayout parentLayout = findViewById(R.id.paring_select_text_relative_layout);
            if (parentLayout.getChildCount() == 1) {
                RelativeLayout relativeLayout = findViewById(R.id.stb_device_list_relative_layout);
                relativeLayout.removeView(mLoadMoreView);
                parentLayout.addView(mLoadMoreView);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        dip2px(PROGRESSSIZE), dip2px(PROGRESSSIZE));
                layoutParams.setMargins(dip2px(MARGIN9), dip2px(MARGIN23), MARGIN0, MARGIN0);
                layoutParams.addRule(RelativeLayout.END_OF, R.id.paring_device_select_text);
                mLoadMoreView.setLayoutParams(layoutParams);
            }
            super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_paring), null);
        }
        DTVTLogger.end();
    }

    /**
     * STBが見つかった際の画面表示を設定.
     */
    private void showResultCompleteView() {
        DTVTLogger.start();
        // STB検索中文言表示
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            mStatusTextView.setText(getString(R.string.str_stb_select_result_text));
            mIsSearchingTextView.setVisibility(View.GONE);
            mDeviceListView.setVisibility(View.VISIBLE);
        } else {
            mPairingImage.setVisibility(View.GONE);
            TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);
            statusSetting.setVisibility(View.GONE);
            mDeviceSelectText.setVisibility(View.VISIBLE);
            mTextDivider2.setVisibility(View.VISIBLE);
        }

        DTVTLogger.end();
    }

    /**
     * ペアリング中画面表示を設定.
     */
    private void showParingView() {
        DTVTLogger.start();
        //ペアリングしてるので、タイマーを解除する.
        stopCallbackTimer();
        mPairingImage.setImageResource(R.mipmap.startup_icon_03);
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            mIsSearchingTextView.setVisibility(View.GONE);
            mStatusTextView.setText(getString(R.string.str_stb_pairing));
            super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_connect), null);
        } else if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            if (mParingDevice.getVisibility() == View.VISIBLE && mCheckMark.getVisibility() == View.VISIBLE) {
                mCheckMark.setVisibility(View.GONE);
                mParingDevice.setVisibility(View.GONE);
            }
            enableHeaderBackIcon(false);
            enableStbStatusIcon(false);
            enableGlobalMenuIcon(false);
            TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);

            statusSetting.setVisibility(View.VISIBLE);
            mDeviceSelectText.setVisibility(View.GONE);
            mTextDivider1.setVisibility(View.GONE);
            mTextDivider2.setVisibility(View.GONE);
            mPairingImage.setVisibility(View.VISIBLE);
            //プログレスビューを中央にする
            RelativeLayout parentLayout = findViewById(R.id.paring_select_text_relative_layout);
            if (parentLayout.getChildCount() == 2) {
                parentLayout.removeView(mLoadMoreView);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.stb_select_status_text_setting);
                layoutParams.setMargins(dip2px(MARGIN0), dip2px(MARGIN84), MARGIN0, MARGIN0);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mLoadMoreView.setLayoutParams(layoutParams);
                RelativeLayout newLayout = findViewById(R.id.stb_device_list_relative_layout);
                newLayout.addView(mLoadMoreView);
            }
        }
        mParingTextView.setVisibility(View.GONE);
        mDeviceListView.setVisibility(View.GONE);
        mLoadMoreView.setVisibility(View.VISIBLE);
        DTVTLogger.end();
    }

    /**
     * ボタン押されたときの動作.
     *
     * @param v view
     */
    @Override
    public void onClick(final View v) {
        DTVTLogger.start();
        if (v.equals(mParingTextView)) {
            if (mParingTextView.getText().equals(
                    getString(R.string.str_stb_to_set_later_text))
                    || mParingTextView.getText().equals(
                    getString(R.string.str_stb_paring_cancel_text))) {
                onUseWithoutPairingButton();
            }
        } else if (v.getId() == R.id.stb_paring_failed_red_help) {
            // ペアリングヘルプ画面へ遷移
            Intent intent = new Intent(getApplicationContext(), PairingHelpActivity.class);
            intent.putExtra(PairingHelpActivity.START_WHERE, PairingHelpActivity.ParingHelpFromMode.
                    ParingHelpFromMode_Setting.ordinal());
            startActivity(intent);
        } else if (v.getId() == R.id.header_layout_back) {
            finish();
        }
        DTVTLogger.end();
    }

    /**
     * ペアリング状態を解除し、キャシューデータを削除する.
     */
    private void releaseParingStation() {
        DTVTLogger.start();
        SharedPreferencesUtils.resetSharedPreferencesStbInfo(this);
        StbConnectionManager.shared().setConnectionStatus(StbConnectionManager.ConnectionStatus.NONE_PAIRING);
        DlnaManager.shared().setStop();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        DTVTLogger.end();
    }
    /**
     * あとで設定するボタンタップ.
     */
    private void onUseWithoutPairingButton() {
        DTVTLogger.start();
        DlnaManager.shared().mDlnaManagerListener = null;
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            //あとで設定する
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            //ペアリング解除する場合、すべてのSTBキャッシュデータを削除して、ホーム画面に遷移する
            if (!isFinishing()) {
                createParingReleaseDialog();
            }
        }
        DTVTLogger.end();
    }

    /**
     * ペアリング解除警告ダイアログを表示する.
     */
    private void createParingReleaseDialog() {
        DTVTLogger.start();
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        customDialog.setOnTouchOutside(false);
        customDialog.setConfirmText(R.string.main_setting_paring_release_text);
        customDialog.setContent(getString(R.string.main_setting_paring_release_dialog_confirm_text));
        customDialog.setCancelable(false);
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                releaseParingStation();
            }
        });
        customDialog.showDialog();
        DTVTLogger.end();
    }

    /**
     * STB選択画面でデバイス名Itemがタップされた時に画面遷移する.
     */
    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        DTVTLogger.start();

        //STB選択画面起動時にdアカウント認証画面を表示しないために、ここでdアカウントの処理を開始する
        //一度再認証オプション付けたらそれ以上付けない制御があるが、この場合必ず認証画面を出す.
        OttGetAuthSwitch.INSTANCE.setNowAuth(true);
        //選択されたSTB番号を保持
        mSelectDevice = i;
        setDaccountControl();
        mOttGetComplete = false;
        if (mDlnaDmsItemList != null) {
            //IPアドレスを設定する
            mRemoteControlRelayClient.setRemoteIp(mDlnaDmsItemList.get(i).mIPAddress);
        }
        //ペアリング中画面を出す
        showParingView();
   }

    /**
     * dアカウントのOTT取得完了.
     * @param result 正常終了ならばtrue
     */
    @Override
    protected void onDaccountOttGetComplete(final boolean result) {
        DTVTLogger.start();

        if (result) {
            //OTT取得終わったのでtrueにする
            mOttGetComplete = true;
            boolean isDAccountFlag = checkDAccountLogin();

            if (isDAccountFlag) {
                setRelayClientHandler();
                RemoteControlRelayClient.getInstance().isUserAccountExistRequest(this);
                storeSTBData();
            } else {
                //万が一ID保存されていなかったケースはエラーダイアログ出して戻る.
                CustomDialog errorDialog = new CustomDialog(
                        this, CustomDialog.DialogType.ERROR);
                errorDialog.setContent(getString(R.string.d_account_regist_error));
                errorDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        //ダイアログを閉じたらSTB選択画面に戻る.
                        resetDmpForResume();
                        initResumeView();
                        startDlnaSearch();
                    }
                });
                errorDialog.setCancelable(false);
                errorDialog.setOnTouchBackkey(false);

                //次のダイアログを呼ぶ為の処理
                errorDialog.setDialogDismissCallback(this);
                errorDialog.setOnTouchOutside(false);
                errorDialog.showDialog();

                //ダイアログ表示のタイミングでタイマ停止
                mIsDAccountErrorDialogShowing = true;
                stopCallbackTimer();
            }
        } else {
            //失敗原因コードを取得
            DaccountControl daccountControl = getDAccountControl();
            int errorCode = 0;
            //初期値を格納している
            DaccountControl.CheckLastClassEnum checkLastClassEnum =
                    DaccountControl.CheckLastClassEnum.CHECK_SERVICE;
            if (daccountControl != null && mSelectDevice != SELECT_DEVICE_ITEM_DEFAULT) {
                errorCode = daccountControl.getResult();
                checkLastClassEnum =  daccountControl.getmResultClass();
                if (DaccountControl.CheckLastClassEnum.REGIST_SERVICE.equals(checkLastClassEnum)
                    || DaccountControl.CheckLastClassEnum.CHECK_SERVICE.equals(checkLastClassEnum)
                    || DaccountControl.CheckLastClassEnum.ONE_TIME_PASS_WORD.equals(checkLastClassEnum)
                    || DaccountControl.CheckLastClassEnum.CONTROL.equals(checkLastClassEnum)) {
                    if (errorCode == DaccountUtils.D_ACCOUNT_APP_NOT_FOUND_ERROR_CODE) {
                        // dアカアプリ未インストールの場合はdアカアプリ起動(関数内でチェックしてストア誘導する画面に飛ぶ).
                        DTVTLogger.debug("dAccountApp is not installed.");
                        checkDAccountApp();
                    } else if (errorCode == IDimDefines.RESULT_NO_AVAILABLE_ID || errorCode == IDimDefines.RESULT_INVALID_ID ) {
                        //有効ID無しなら同じくdアカアプリ起動.
                        DTVTLogger.debug("dAccountID is not Registered.");
                        // dアカアプリを単純起動してID登録せずに戻るとなぜかOTTの応答コールバックが呼ばれる
                        // 延々と呼び続ける事になるので、ガードする.
                        // dアカアプリ起動～ドコテレ復帰の間は再起動しない.
                        // また多くの場合はコールバックが復帰後に来るため、ペアリング中の状態以外では起動しない
                        // （Resume時には必ずSTB選択の状態に戻している事を利用）
                        if (!mIsDAccountAppStarting && mLoadMoreView.getVisibility() == View.VISIBLE) {
                            checkDAccountApp();
                        }
                    }
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * 選択されたSTBを保存して画面遷移を行う.
     */
    private void storeSTBData() {
        DTVTLogger.start();
        do {
            if (mCallbackTimer == null) {
                break;
            }
            if (mCallbackTimer.getTimerStatus() == TimerStatus.TIMER_STATUS_DURING_STARTUP) {
                break;
            }
            if (mStartMode != StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
                break;
            }
            if (mParingDevice.getVisibility() != View.VISIBLE) {
                break;
            }
            DTVTLogger.debug("mCheckMark.setVisibility(View.GONE)");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCheckMark.setVisibility(View.GONE);
                }
            });
        } while (false);
        DTVTLogger.end();
    }
    @Override
    public void joinDms(final String name, final  String host, final  String udn, final String controlUrl, final String eventSubscriptionUrl) {
        DlnaDmsItem item = new DlnaDmsItem();
        item.mFriendlyName = name;
        item.mIPAddress = host;
        item.mUdn = udn;
        item.mControlUrl = controlUrl;
        item.mHttp = host;
        if (mSelectDevice == SELECT_DEVICE_ITEM_DEFAULT) {
            updateDeviceList(item, true);
        }
    }

    @Override
    public void leaveDms(final String udn) {
        DlnaDmsItem item = new DlnaDmsItem();
        item.mUdn = udn;
        updateDeviceList(item, false);
    }

    /**
     * デバイスリスト情報を更新する.
     * @param deviceItem DMS
     * @param isAdd joinまたはleave
     */
    private void updateDeviceList(final DlnaDmsItem deviceItem, final boolean isAdd) {
        DTVTLogger.start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAdd) {
                    mDlnaDmsInfo.add(deviceItem);
                } else {
                    if (deviceItem != null) {
                        mDlnaDmsInfo.remove(deviceItem.mUdn);
                    }
                }
                DTVTLogger.debug("runOnUiThread start");
                DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(StbSelectActivity.this);

                mContentsList.clear();
                mDlnaDmsItemList.clear();

                for (int i = 0; i < mDlnaDmsInfo.size(); i++) {
                    DlnaDmsItem item = mDlnaDmsInfo.get(i);
                    DTVTLogger.debug("item.mFriendlyName = " + item.mFriendlyName);
                    DTVTLogger.debug("item.mIPAddress = " + item.mIPAddress);
                    DTVTLogger.debug("item.mUdn = " + item.mUdn);
                    boolean flag = false;
                    if (dlnaDmsItem != null && !TextUtils.isEmpty(dlnaDmsItem.mUdn)) {
                        if (dlnaDmsItem.mUdn.equals(item.mUdn)) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(DEVICE_NAME_KEY, item.mFriendlyName);
                        hashMap.put(item.mUdn, item.mUdn);
                        if (mContentsList.size() > 0) {
                            //0以上の場合重複チェック
                            for (HashMap<String, String> oldHashMap : mContentsList) {
                                if (!oldHashMap.containsKey(item.mUdn)) {
                                    mContentsList.add(hashMap);
                                    mDlnaDmsItemList.add(item);
                                    //デバイス追加されたらbreak
                                    break;
                                }
                            }
                        } else {
                            //0件の場合そのままリストに追加する
                            mContentsList.add(hashMap);
                            mDlnaDmsItemList.add(item);
                        }
                    }
                }
                DTVTLogger.debug("ContentsList.size = " + mContentsList.size());
                DTVTLogger.debug("mDlnaDmsItemList.size = " + mDlnaDmsItemList.size());
                if (mCallbackTimer == null
                        || mCallbackTimer.getTimerStatus() == TimerStatus.TIMER_STATUS_CANCEL) {
                    if (mCallbackTimer != null) {
                        DTVTLogger.debug("timer canceled remake");
                    }
                    //タイマーが存在しないか既にキャンセル済みだった場合は、新たに作成する
                    mCallbackTimer = new StbInfoCallBackTimer(new Handler());
                }
                // 0件の場合タイムアウトを設定する
                if (mContentsList.size() <= 0) {
                    mDeviceAdapter.notifyDataSetChanged();
                    displayMoreData(true);
                    showSearchingView();
                    startCallbackTimer();
                    DTVTLogger.debug("ContentsList.size <= 0 ");
                } else if (mCallbackTimer.getTimerStatus() != TimerStatus.TIMER_STATUS_EXECUTION) { // 30秒以内にSTBの通知あり
                    displayMoreData(false);
                    stopCallbackTimer();
                    showResultCompleteView();
                    if (null != mDeviceAdapter) {
                        mDeviceAdapter.notifyDataSetChanged();
                    }
                    DTVTLogger.debug("TimerTaskNotExecuted");
                } else { // 既にタイムアウトとなっていた場合
                    // nop.
                    DTVTLogger.debug("TimerTaskExecuted");
                }
                DTVTLogger.debug("runOnUiThread end");
            }
        });
        DTVTLogger.end();
    }

    /**
     * 再読み込み時のダイアログ表示処理.
     *
     * @param b 再読み込みフラグ.
     */
    private void displayMoreData(final boolean b) {
        DTVTLogger.start("displayMoreData:" + b);
        if (b) {
            mLoadMoreView.setVisibility(View.VISIBLE);
        } else {
            mLoadMoreView.setVisibility(View.GONE);
        }
        DTVTLogger.end();
    }

    /**
     * タイムアウト時間を設定.
     */
    private void startCallbackTimer() {
        DTVTLogger.start();

        if (mCallbackTimer == null
                || mCallbackTimer.getTimerStatus() == TimerStatus.TIMER_STATUS_CANCEL) {
            //タイマーが存在しないか既にキャンセル済みだった場合は、新たに作成する
            mCallbackTimer = new StbInfoCallBackTimer(new Handler());
        }
        mCallbackTimer.executeTimerTask();
        DTVTLogger.end();
    }

    /**
     * タイムアウト時間設定を解除.
     */
    private void stopCallbackTimer() {
        DTVTLogger.start();
        if (mCallbackTimer != null
              && mCallbackTimer.getTimerStatus() == TimerStatus.TIMER_STATUS_DURING_STARTUP) {
            mCallbackTimer.cancel();
            mCallbackTimer = null;
        }
        DTVTLogger.end();
    }

    /**
     * エラータイプ切り分け.
     */
   public enum ErrorType {
        /**検出タイムアウトエラー.*/
        TIME_OUT_ERROR,
        /**WiFi未接続エラー.*/
        WIFI_NO_CONNECT_ERROR
    }

    /**
     * タイムアウト時の画面表示.
     */
    private void showTimeoutView() {
        DTVTLogger.start();
        displayMoreData((false));
        DlnaManager.shared().mDlnaManagerListener = null;
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            if (isWifiOn()) {
                //Wifi接続の場合、検出エラー画面を表示する
                Intent stbSelectErrorIntent = new Intent(this, StbSelectErrorActivity.class);
                stbSelectErrorIntent.putExtra(StbSelectActivity.ERROR_TYPE, ErrorType.TIME_OUT_ERROR.ordinal());
                startActivity(stbSelectErrorIntent);
            } else {
                //Wifi未接続に場合、ネットワークエラー画面表示する
                Intent stbSelectErrorIntent = new Intent(this, StbSelectErrorActivity.class);
                stbSelectErrorIntent.putExtra(StbSelectActivity.ERROR_TYPE, ErrorType.WIFI_NO_CONNECT_ERROR.ordinal());
                startActivity(stbSelectErrorIntent);
            }
        } else {
            // リストを非表示
            mDeviceListView.setVisibility(View.GONE);
            if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()
                    && !mParingDevice.getText().toString().isEmpty()) {
                mParingTextView.setText(R.string.str_stb_paring_cancel_text);
            }
            // STB検索タイムアウト文言表示
            TextView stbSearchFailed = findViewById(R.id.stb_device_search_result);
            TextView stbSearchHelp = findViewById(R.id.stb_paring_failed_red_help);
            stbSearchFailed.setVisibility(View.VISIBLE);
            stbSearchHelp.setVisibility(View.VISIBLE);
            stbSearchHelp.setOnClickListener(this);
        }
        DTVTLogger.end();

    }

    /**
     * タイムアウト設定クラス.
     */
    private class StbInfoCallBackTimer extends Timer {
        /**
         * STB検出タイムアウト時間.
         */
        private static final long STB_SEARCH_TIMEOUT = 30000;
        /**
         * タイマータスク.
         */
        private TimerTask mTimerTask = null;
        /**
         * ハンドラー.
         */
        private Handler mHandler = null;
        /**
         * タイマーの状態.
         */
        private TimerStatus mTimerStatus = TimerStatus.TIMER_STATUS_DEFAULT;

        /**
         * STB情報取得のタイムアウト時間コールバック.
         *
         * @param handler ハンドラ
         */
        private StbInfoCallBackTimer(final Handler handler) {
            mHandler = handler;
        }

        /**
         * TimerTask実行予約処理.
         */
        private void executeTimerTask() {
            DTVTLogger.start();
            setTimerTask();
            mTimerStatus = TimerStatus.TIMER_STATUS_DURING_STARTUP;
            schedule(mTimerTask, STB_SEARCH_TIMEOUT);
            DTVTLogger.end();
        }

        /**
         * TimerTask処理の設定.
         */
        private void setTimerTask() {
            DTVTLogger.start();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    // タイムアウト処理
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showTimeoutView();
                            mTimerStatus = TimerStatus.TIMER_STATUS_EXECUTION;
                        }
                    });
                }
            };
            DTVTLogger.end();
        }

        /**
         * TimerTaskキャンセル処理.
         */
        private void timerTaskCancel() {
            DTVTLogger.start();
            mTimerStatus = TimerStatus.TIMER_STATUS_CANCEL;
            mTimerTask.cancel();
            DTVTLogger.end();
        }

        @Override
        public void cancel() {
            DTVTLogger.start();
            if (mTimerStatus == TimerStatus.TIMER_STATUS_DURING_STARTUP) {
                timerTaskCancel();
            }
            super.cancel();
            DTVTLogger.end();
        }

        /**
         * TimerTask実行状態取得.
         *
         * @return mTimerStatus:タイムアウト処理実行状態
         */
        private TimerStatus getTimerStatus() {
            return mTimerStatus;
        }
    }

    /**
     * dアカウントの登録状態を確認する.
     *
     * @return flag
     */
    private boolean checkDAccountLogin() {
        DTVTLogger.start();
        String userId = SharedPreferencesUtils.getSharedPreferencesDaccountId(this);
        if (userId != null && !userId.equals("")) {
            DTVTLogger.debug("checkDAccountLogin() true");
            return true;
        } else {
            DTVTLogger.debug("checkDAccountLogin() false");
            return false;
        }
    }

    /**
     * dアカウントアプリが端末にインストールされているか確認を行い、インストールされている場合は.
     * dアカウントアプリを起動する.
     */
    private void checkDAccountApp() {
        DTVTLogger.start();
        Intent intent = new Intent();
        intent.setClassName(D_ACCOUNT_APP_PACKAGE_NAME,
                D_ACCOUNT_APP_PACKAGE_NAME + D_ACCOUNT_APP_ACTIVITY_NAME);
        try {
            mIsDAccountAppStarting = true;
            startActivity(intent);
            mSelectDevice = SELECT_DEVICE_ITEM_DEFAULT;
            mDaccountFlag = true;
            //ログイン後にユーザ操作でこの画面に戻ってきた際には再度STB選択を行わせる
        } catch (ActivityNotFoundException e) {
            revertSelectStbState();
        }
        DTVTLogger.end();
    }

    /**
     * dアカウントアプリインストール誘導画面に遷移.
     */
    private void revertSelectStbState() {
        DTVTLogger.debug("not daccount app");
        //端末内にdアカウントアプリがない場合はdアカウントアプリDL誘導を行う
        mIsAppDL = true;
        Intent intent = new Intent(this, DaccountInductionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void onStbClientResponse(final Message msg) {
        RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES requestCommand
                = ((RelayServiceResponseMessage) msg.obj).getRequestCommandTypes();
        DTVTLogger.debug(String.format("msg.what:%s requestCommand:%s", msg.what, requestCommand));
        switch (msg.what) {
            case RelayServiceResponseMessage.RELAY_RESULT_OK:
                switch (requestCommand) {
                    case START_APPLICATION:
                    case TITLE_DETAIL:
                        // STB_REQUEST_COMMAND_TYPES misses case 抑制.
                        // ※RELAY_RESULT_OK 応答時は requestCommand に START_APPLICATION/TITLE_DETAIL は設定されない
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                        //ナスネとペアリングしたいときは以下をコメントアウト　SharedPreferencesにSTBデータを保存
                        if (mSelectDevice != SELECT_DEVICE_ITEM_DEFAULT) {
                            SharedPreferencesUtils.setSharedPreferencesStbInfo(this, mDlnaDmsItemList.get(mSelectDevice));
                            Intent intent = new Intent(this, StbConnectActivity.class);
                            intent.putExtra(FROM_WHERE, mStartMode);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        break;
                    case SET_DEFAULT_USER_ACCOUNT:
                    case CHECK_APPLICATION_VERSION_COMPATIBILITY:
                    case CHECK_APPLICATION_REQUEST_PROCESSING:
                    case KEYEVENT_KEYCODE_POWER:
                    case COMMAND_UNKNOWN:
                        // STB_REQUEST_COMMAND_TYPES misses case 抑制.
                        // ※RELAY_RESULT_OK 応答時は requestCommand に SET_DEFAULT_USER_ACCOUNT/CHECK_APPLICATION_VERSION_COMPATIBILITY
                        // /CHECK_APPLICATION_REQUEST_PROCESSING/KEYEVENT_KEYCODE_POWER/COMMAND_UNKNOWNは設定されない
                    default:
                        break;
                }
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_ERROR:
                int resultCode = ((RelayServiceResponseMessage) msg.obj).getResultCode();
                mSelectDevice = SELECT_DEVICE_ITEM_DEFAULT;
                DTVTLogger.debug("resultCode: " + resultCode);
                switch (requestCommand) {
                    case KEYEVENT_KEYCODE_POWER:
                        // STB_REQUEST_COMMAND_TYPES misses case 抑制.
                        // ※RELAY_RESULT_ERROR 応答時は requestCommand に KEYEVENT_KEYCODE_POWER は設定されない
                    case START_APPLICATION:
                    case TITLE_DETAIL:
                        // STB_REQUEST_COMMAND_TYPES misses case 抑制.
                        // ※RELAY_RESULT_ERROR 応答時は requestCommand に START_APPLICATION/TITLE_DETAIL は設定されない
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                        switch (resultCode) {
                            case RelayServiceResponseMessage.RELAY_RESULT_INTERNAL_ERROR://サーバエラー
                            case RelayServiceResponseMessage.RELAY_RESULT_SET_DEFAULT_USER_ACCOUNT_REJECTED: // 要求受付失敗
                            case RelayServiceResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE: // サービス未登録、又は、署名による呼び出し元不正
                            case RelayServiceResponseMessage.RELAY_RESULT_USER_ID_CHANGED: // 要求時とは別docomoIDで指定ユーザに切り替え成功 ※本来は正常終了だが異常終了とする
                            case RelayServiceResponseMessage.RELAY_RESULT_USER_TIMEOUT: // ユーザタイムアウト
                            case RelayServiceResponseMessage.RELAY_RESULT_USER_CANCEL: // ユーザ中断
                            case RelayServiceResponseMessage.RELAY_RESULT_USER_INVALID_STATE: // ユーザ状態異常
                            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_SERVER_ERROR: // ユーザアカウント切り替えのサーバエラー
                            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_NETWORK_ERROR: // ユーザアカウント切り替えのネットワークエラー
                            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_INTERNAL_ERROR: // ユーザアカウント切り替えの内部エラー
                            case RelayServiceResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY: // 中継アプリからの応答待ち中に新しい要求を行った場合／中継アプリの処理中の場合
                                createErrorDialog(getResources().getString(R.string.str_launch_stb_communication_failed_error));
                                break;
                            case RelayServiceResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID://指定ユーザIDなし
                                // チェック処理の状態で処理を分岐する
                                SharedPreferencesUtils.resetSharedPreferencesStbInfo(getApplicationContext());
                                Intent dAccountResetIntent = new Intent(this, DaccountResettingActivity.class);
                                dAccountResetIntent.putExtra(FROM_WHERE, mStartMode);
                                startActivity(dAccountResetIntent);
                                break;
                            case RelayServiceResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                                Intent homeIntent = new Intent(this, HomeActivity.class);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(homeIntent);
                                break;
                            default:
                                break;
                        }
                        break;
                    case SET_DEFAULT_USER_ACCOUNT:
                        // ※RELAY_RESULT_ERROR 応答時は requestCommand に SET_DEFAULT_USER_ACCOUNT は設定されない
                        break;
                    case CHECK_APPLICATION_VERSION_COMPATIBILITY:
                        // アプリケーションバージョンチェックのエラーダイアログを表示する
                        mIsApplicationVersionCheckIncompatibleDialogFlag = true;
                        switchApplicationVersionCode(resultCode);
                        break;
                    case CHECK_APPLICATION_REQUEST_PROCESSING:
                        //中継アプリからの応答待ち中に新しい要求を行った場合
                    case COMMAND_UNKNOWN:
                        switch (resultCode) {
                            case RelayServiceResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                                createErrorDialog(getResources().getString(R.string.str_launch_stb_communication_failed_error));
                                break;
                            case RelayServiceResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY: // 他の端末の要求処理中
                                createErrorDialog(getResources().getString(R.string.main_setting_stb_busy_error_message));
                                break;
                            default:
                                break;
                        }
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * アプリケーションバージョンチェックのエラーダイアログのクローズ.
     */
    @Override
    protected void startNextProcess() {
        if (mIsApplicationVersionCheckIncompatibleDialogFlag) {
            DTVTLogger.debug("Application version check incompatible dialog closed");
            //初期状態に戻る
            resetDmpForResume();
            initResumeView();
            startDlnaSearch();
            if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                initLaunchView();
            }
        }
        mIsApplicationVersionCheckIncompatibleDialogFlag = false;
    }

    /**
     * エラーダイアログ表示.
     * @param errorMessage エラーメッセージ.
     */
    private void createErrorDialog(final String errorMessage) {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(StbSelectActivity.this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(errorMessage);
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        failedRecordingReservationDialog.setOnTouchOutside(false);
        failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                //初期状態に戻る
                resetDmpForResume();
                initResumeView();
                startDlnaSearch();
                if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                    initLaunchView();
                }
            }
        });
        failedRecordingReservationDialog.showDialog();
    }
    /**
     * STBとのペアリングエラー時からのペアリング選択画面の再起動処理.
     */
    private void resetDmpForResume() {
        DlnaManager.shared().mDlnaManagerListener = null;
        mContentsList.clear();
        mDlnaDmsItemList.clear();
        mDlnaDmsInfo.clear();
        //タイムアウト用のタイマーは停止させる
        stopCallbackTimer();
    }

    /**
     * 再起動時のダイアログ・引数無しに対応するため、可変長引数とする.
     *
     * @param message 省略した場合はdアカウント用メッセージを表示。指定した場合は、常に先頭文字列のみ使用される
     */
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

        //ペアリング画面の時はダイアログ出さない
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            return;
        }
        //ペアリング設定画面かつユーザーがデバイスを選択してDアカウントを登録の場合はダイアログ出さない
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal() && mDaccountFlag) {
            mDaccountFlag = false;
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

    @Override
    protected void showDAccountErrorDialog() {
        //専用処理がある為オーバライド.
        //初期フローのペアリングの場合は、再認証キャンセル時はログアウトでなくエラーダイアログの表示を行う
        //ログアウトダイアログは設定＞ペアリングの際のみ必要だが別メソッドで表示
        //エラーダイアログを閉じたらSTB選択画面へ戻る
        CustomDialog errorDialog = new CustomDialog(
                this, CustomDialog.DialogType.ERROR);
        int errorCode = 0;
        //初期値を格納している
        DaccountControl.CheckLastClassEnum checkLastClassEnum =
                DaccountControl.CheckLastClassEnum.CHECK_SERVICE;
        //失敗原因コードを取得
        DaccountControl daccountControl = getDAccountControl();
        if (daccountControl != null) {
            errorCode = daccountControl.getResult();
            checkLastClassEnum =  daccountControl.getmResultClass();
        }

        boolean isNeedDialog = true;
        if (DaccountControl.CheckLastClassEnum.REGIST_SERVICE.equals(checkLastClassEnum)
            || DaccountControl.CheckLastClassEnum.CHECK_SERVICE.equals(checkLastClassEnum)
            || DaccountControl.CheckLastClassEnum.ONE_TIME_PASS_WORD.equals(checkLastClassEnum)
            || DaccountControl.CheckLastClassEnum.CONTROL.equals(checkLastClassEnum)) {
            DTVTLogger.debug("showDAccountErrorDialog errCode:" + errorCode);
            switch (errorCode) {
                case IDimDefines.RESULT_USER_INVALID_STATE:
                    errorDialog.setContent(getString(R.string.d_account_user_abnormality_error));
                    break;
                case IDimDefines.RESULT_NETWORK_ERROR:
                    errorDialog.setContent(getString(R.string.d_account_network_error));
                    break;
                case IDimDefines.RESULT_USER_TIMEOUT:
                    errorDialog.setContent(getString(R.string.d_account_user_timeout_error));
                    break;
                case IDimDefines.RESULT_USER_CANCEL:
                    if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                        errorDialog.setContent(getString(R.string.d_account_user_interruption_error));
                    } else {
                        //設定>ペアリングの場合は、別途ログアウトダイアログを表示するのでエラーダイアログは表示しない
                        isNeedDialog = false;
                    }
                    break;
                case IDimDefines.RESULT_SERVER_ERROR:
                    errorDialog.setContent(getString(R.string.d_account_server_error));
                    break;
                case IDimDefines.RESULT_INVALID_ID:
                    // OTT取得を再認証オプションで呼び出しているため基本的にID無効でエラーダイアログは出さない.
                    isNeedDialog = false;
//                    errorDialog.setContent(getString(R.string.d_account_authentication_invalid_error));
                    break;
                case IDimDefines.RESULT_INTERNAL_ERROR:
                    errorDialog.setContent(getString(R.string.d_account_internal_error));
                    break;
                case IDimDefines.RESULT_NOT_REGISTERED_SERVICE:
                    errorDialog.setContent(getString(R.string.d_account_service_unregistered_error));
                    break;
                case IDimDefines.RESULT_REMOTE_EXCEPTION:
                    errorDialog.setContent(getString((R.string.d_account_remote_exception_error)));
                    break;
                case IDimDefines.RESULT_NO_AVAILABLE_ID:
                    //有効IDなし.
                    //STB選択は別途専用画面に誘導するのでダイアログは出さない.
                    isNeedDialog = false;
                    break;
                case IDimDefines.RESULT_COMPLETE:
                    //正常終了
                    isNeedDialog = false;
                    break;
                case DaccountUtils.D_ACCOUNT_APP_NOT_FOUND_ERROR_CODE:
                    //事前チェックでdアカウント設定アプリが未インストールである事が分かった場合のエラー
                    //STB選択は別途専用画面に誘導するのでダイアログは出さない.
                    isNeedDialog = false;
//                    errorDialog.setContent(getString(R.string.d_account_deleted_message));
                    break;
                default:
                    errorDialog.setContent(getString(R.string.d_account_regist_error));
                    break;
            }
        }
        if (isNeedDialog) {
            errorDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    //ダイアログを閉じたらSTB選択画面に戻る.
                    resetDmpForResume();
                    initResumeView();
                    startDlnaSearch();
                }
            });
            errorDialog.setCancelable(false);
            errorDialog.setOnTouchBackkey(false);

            //次のダイアログを呼ぶ為の処理
            errorDialog.setDialogDismissCallback(this);
            errorDialog.setOnTouchOutside(false);
            errorDialog.showDialog();

            //ダイアログ表示のタイミングでタイマ停止
            mIsDAccountErrorDialogShowing = true;
            stopCallbackTimer();
        }
        DTVTLogger.end();
    }

    @Override
    public void showLogoutDialog() {
        //初期ペアリングではログアウトダイアログは出さないのでオーバライドする
        //ただし設定＞ペアリングの場合はログアウトダイアログを表示するため親クラス処理を利用する
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            super.showLogoutDialog();
        }
    }

    @Override
    public void allDismissCallback() {
        //ダイアログを閉じたタイミングでSTB未検出ならタイマ開始
        if (mDlnaDmsInfo.size() <= 0 && mIsDAccountErrorDialogShowing) {
            mIsDAccountErrorDialogShowing = false;
            startCallbackTimer();
            showSearchingView();
        } else {
            //dアカウントエラーダイアログ以外の場合はそのまま閉じる
            super.allDismissCallback();
        }
    }

    /**
     * 起動モードのゲッター.
     * @return 起動モードの値
     */
    public int getmStartMode() {
        return mStartMode;
    }

    /**
     * 新dアカウントの受信.
     *
     * @param dAccount 送られてきたdアカウント
     */
    @Override
    public void onChangeAndSendDaccount(final String dAccount) {
        //dアカウントが変更された際に送信されてくる
        DTVTLogger.start("new dacount =" + dAccount);
        //新しいdアカウントが来たことを知らせ、onResumeでSTB選択画面の再起動をさせる
        //(送られてきたdアカウントは直接使用せず、既存のdアカウント取得処理に委ねる)
        mNewDaccountGet = true;
        DTVTLogger.end();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
                return !closeDrawerMenu() && super.onKeyDown(keyCode, event);
            }
        }
        return false;
    }
}
