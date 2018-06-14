/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsInfo;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.relayclient.RelayServiceResponseMessage;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherApi;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherUtil;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * ペアリング、再ペアリングするためのクラス.
 */
public class StbSelectActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        DlnaManager.DlnaManagerListener {

    // region variable
    /**
     * DMSリスト.
     */
    private List<DlnaDmsItem> mDlnaDmsItemList = new ArrayList<>();
    private DlnaDmsInfo mDlnaDmsInfo = new DlnaDmsInfo();
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
    private int mSelectDevice;
    /**
     * 次回表示しないフラグ.
     */
    private boolean mIsNextTimeHide = false;
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
     * チェックボックス.
     */
    private CheckBox mCheckBox = null;
    /**
     * ペアリングしないでアプリを利用するTextView.
     */
    private TextView mParingTextView = null;
    /**
     * ペアリングされたデバイス名を表示するためのTextView.
     */
    private TextView mParingDevice;
    /**
     * 次回表示しないTextView.
     */
    private TextView mCheckboxText;
    /**
     * 次回表示しないTextView表示フラグ.
     */
    private boolean mIsShowCheckboxText = false;
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
     * ステータス表示の高さの初期値.
     */
    private int mFirstStatusHeight = 0;

    /**
     * ステータス表示の高さの補正値.
     */
    private final double ANDROID_4_4_FIX_SIZE = 1.2;
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

    /** 暗号化処理の鍵交換を同期処理で実行する. */
    private CountDownLatch mLatch = null;
    /** 暗号化処理の鍵交換の同期カウンター. */
    private static int LATCH_COUNT_MAX = 1;

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
        Intent intent = getIntent();
        if (intent != null) {
            mStartMode = intent.getIntExtra(FROM_WHERE, -1);
        }
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            setContentView(R.layout.stb_select_main_layout);
            initLaunchView();
        } else if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            setContentView(R.layout.stb_select_device_list_setting);
            enableHeaderBackIcon(true);
            initSettingView();
        }
        // dアカウントチェック処理はonResumeで自身で呼び出しているため不要
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
        mCheckBox = findViewById(R.id.checkBoxSTBSelectActivity);
        mCheckboxText = findViewById(R.id.launch_select_check_box_text);
        mCheckBox.setVisibility(View.VISIBLE);
        mCheckboxText.setVisibility(View.VISIBLE);
        mCheckBox.setOnClickListener(this);
        mCheckboxText.setOnClickListener(this);
        mCheckBox.setChecked(false);
        mCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                        mIsNextTimeHide = isChecked;
                        if (isChecked) {
                            mCheckBox.setBackgroundResource(R.drawable.
                                    ic_check_box_white_24dp);
                        } else {
                            mCheckBox.setBackgroundResource(R.drawable.
                                    ic_check_box_outline_blank_white_24dp);
                        }
                    }
                });
        mParingTextView = findViewById(
                R.id.useWithoutPairingSTBParingInvitation);
        mParingTextView.setOnClickListener(this);
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
        setStatusBarColor(true);
        if (mStartMode == (StbSelectFromMode.StbSelectFromMode_Launch.ordinal())) {
            //初回起動時
            enableHeaderBackIcon(false);
            setTitleText(getString(R.string.str_app_title));
            enableStbStatusIcon(false);
            enableGlobalMenuIcon(false);
            return;
        } else if (mStartMode == (StbSelectFromMode.StbSelectFromMode_Setting.ordinal())) {
            mParingTextView.setVisibility(View.VISIBLE);
            mLoadMoreView = findViewById(R.id.stb_device_setting_progressbar);
            //設定画面からの遷移
            enableHeaderBackIcon(true);
            setTitleText(getString(R.string.str_stb_paring_setting_title));
            enableStbStatusIcon(true);
            enableGlobalMenuIcon(true);

            TextView stbSearchFailed = findViewById(R.id.stb_device_search_result);
            TextView stbSearchHelp = findViewById(R.id.stb_paring_failed_red_help);

            if (stbSearchFailed.getVisibility() == View.VISIBLE && stbSearchHelp.getVisibility() == View.VISIBLE) {
                stbSearchFailed.setVisibility(View.GONE);
                stbSearchHelp.setVisibility(View.GONE);
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
                updateDeviceList();
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

        //別途BaseActivityの物は禁止してあるので、こちらで呼び出す
        setDaccountControl();

        initView();
        TextView statusTextView = findViewById(R.id.stb_select_status_text);

        if (mIsAppDL) {
            //dアカウントアプリDLからの戻り時、各種Viewを初期状態に戻す
            mIsAppDL = false;
            if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {

                //Android5.0未満の表示問題対策
                fixStatusTextView(statusTextView);

                mPairingImage.setImageResource(R.mipmap.startup_icon_01);
                statusTextView.setText(R.string.str_stb_select_result_text_search);
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckboxText.setVisibility(View.VISIBLE);
                mDeviceListView.setVisibility(View.VISIBLE);
                mParingTextView.setText(R.string.str_stb_no_pair_use_text);
                super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select));
            } else {
                RelativeLayout relativeLayout = findViewById(R.id.stb_icon_relative_layout_setting);
                relativeLayout.setVisibility(View.GONE);
                mPairingImage.setVisibility(View.GONE);
                TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);
                statusSetting.setVisibility(View.GONE);
                mDeviceSelectText.setVisibility(View.VISIBLE);
                mTextDivider2.setVisibility(View.VISIBLE);
                mDeviceListView.setVisibility(View.VISIBLE);
                super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_paring));
            }
        } else {
            if (mLoadMoreView != null && mLoadMoreView.getVisibility() == View.VISIBLE) {
                super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select_loading));
            } else {
                if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                    super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select));
                } else {
                    super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_paring));
                }
            }
        }

        if (mIsShowCheckboxText) {
            //次回表示しないTextView再表示する
            mIsShowCheckboxText = false;
            mCheckBox.setVisibility(View.VISIBLE);
            mCheckboxText.setVisibility(View.VISIBLE);
        }

        DlnaManager.shared().mDlnaManagerListener = this;
        DlnaManager.shared().StartDmp();
        DTVTLogger.end();
    }

    @Override
    public void onPause() {
        super.onPause();
        DTVTLogger.start();
        leaveActivity();
        displayMoreData(false);
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
                if (mCallbackTimer != null) {
                    stopCallbackTimer();
                    mCallbackTimer.cancel();
                    mCallbackTimer = null;
                }
            }
        });
        DlnaManager.shared().mDlnaManagerListener = null;
        mDlnaDmsInfo.clear();
        DlnaManager.shared().StopDmp();
    }

    /**
     * STB検索中の画面表示を設定.
     */
    private void showSearchingView() {
        DTVTLogger.start();
        // STB検索中文言表示
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);

            //Android5.0未満の表示問題対策
            fixStatusTextView(statusTextView);

            statusTextView.setText(R.string.str_stb_select_result_text_search);

            // STBが見つかるまで非表示
            TextView checkBoxText = findViewById(R.id.useWithoutPairingSTBParingInvitation);
            checkBoxText.setVisibility(View.VISIBLE);
            super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select_loading));
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
            DTVTLogger.end();
        }
    }

    /**
     * STBが見つかった際の画面表示を設定.
     */
    private void showResultCompleteView() {
        DTVTLogger.start();
        // STB検索中文言表示
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);

            //Android5.0未満の表示問題対策
            fixStatusTextView(statusTextView);

            statusTextView.setText(R.string.str_stb_select_result_text);

            // STBが見つかったため表示する
            TextView checkBoxText = findViewById(R.id.useWithoutPairingSTBParingInvitation);
            checkBoxText.setVisibility(View.VISIBLE);
            mDeviceListView.setVisibility(View.VISIBLE);
        } else {
            // STBが見つかったため表示する
            RelativeLayout relativeLayout = findViewById(R.id.stb_icon_relative_layout_setting);
            relativeLayout.setVisibility(View.GONE);
            mPairingImage.setVisibility(View.GONE);
            TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);
            statusSetting.setVisibility(View.GONE);
            mDeviceSelectText.setVisibility(View.VISIBLE);
            mTextDivider2.setVisibility(View.VISIBLE);
        }

        DTVTLogger.end();
    }

    /**
     * Android5.0未満で発生する文字表示欠けを予防する.
     *
     * @param statusTextView 文字の欠けるテキストビュー
     */
    private void fixStatusTextView(final TextView statusTextView) {
        //ヌルかAndroid5.0以上ならば帰る
        if (statusTextView == null || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        //現在の縦サイズを取得する
        ViewGroup.LayoutParams layoutParams = statusTextView.getLayoutParams();

        //既に補正済みならば帰る
        if (mFirstStatusHeight != 0 && mFirstStatusHeight != layoutParams.height) {
            return;
        }

        //Android5.0未満の場合、行間を1.5倍にする設定の悪影響で一部文字表示が欠けるので、
        //防止用に現在の縦サイズを取得して、1.2倍に拡大する
        mFirstStatusHeight = layoutParams.height;
        layoutParams.height = (int) (mFirstStatusHeight * ANDROID_4_4_FIX_SIZE);
        statusTextView.setLayoutParams(layoutParams);
        statusTextView.requestLayout();
    }

    /**
     * ペアリング中画面表示を設定.
     */
    private void showParingView() {
        DTVTLogger.start();
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);
            statusTextView.setText(R.string.str_stb_pairing);
            super.sendScreenView(getString(R.string.str_stb_pairing));

            mCheckBox.setVisibility(View.GONE);
            mCheckboxText.setVisibility(View.GONE);
            //次回表示しないTextView表示フラグをTRUEにする
            mIsShowCheckboxText = true;
        } else if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {

            if (mParingDevice.getVisibility() == View.VISIBLE && mCheckMark.getVisibility() == View.VISIBLE) {
                mCheckMark.setVisibility(View.GONE);
                mParingDevice.setVisibility(View.GONE);
            }
            TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);

            //Android5.0未満の表示問題対策
            fixStatusTextView(statusSetting);

            statusSetting.setVisibility(View.VISIBLE);
            RelativeLayout relativeLayout = findViewById(R.id.stb_icon_relative_layout_setting);
            relativeLayout.setVisibility(View.VISIBLE);
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
        mParingTextView.setVisibility(View.INVISIBLE);
        mDeviceListView.setVisibility(View.GONE);
        mLoadMoreView.setVisibility(View.VISIBLE);
        super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select_loading));
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
                    getString(R.string.str_stb_no_pair_use_text))
                    || mParingTextView.getText().equals(
                    getString(R.string.str_stb_paring_cancel_text))) {
                onUseWithoutPairingButton();
            }
        } else if (v.getId() == R.id.launch_select_check_box_text) {
            if (!mCheckBox.isChecked()) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
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
     * ペアリングしないでアプリを利用するボタンタップ.
     */
    private void onUseWithoutPairingButton() {
        DTVTLogger.start();
        DlnaManager.shared().mDlnaManagerListener = null;
        //STB選択画面"次回以降表示しない" 状態をSharedPreferenceに保存
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            SharedPreferencesUtils.setSharedPreferencesStbSelect(this, mIsNextTimeHide);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (mStartMode == StbSelectFromMode.StbSelectFromMode_Setting.ordinal()) {
            if (mParingDevice.getVisibility() == View.VISIBLE) {
                //ペアリング解除する場合、すべてのSTBキャッシュデータを削除して、ホーム画面に遷移する
                DlnaManager.shared().StopDmp();
                SharedPreferencesUtils.resetSharedPreferencesStbInfo(this);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                //ペアリングししないで利用する場合、設定画面に戻る
                finish();
            }
        }
        DTVTLogger.end();
    }

    /**
     * STB選択画面でデバイス名Itemがタップされた時に画面遷移する.
     */
    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        DTVTLogger.start();
        //選択されたSTB番号を保持
        mSelectDevice = i;
        if (mDlnaDmsItemList != null) {
            //IPアドレスを設定する
            mRemoteControlRelayClient.setRemoteIp(mDlnaDmsItemList.get(i).mIPAddress);
        }
        //ペアリング中画面を出す
        showParingView();
        //鍵交換
        exchangeKey();
    }

    /**
     * 鍵交換処理を行い画面遷移する.
     */
    private void exchangeKey() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                syncRequestPublicKey();
                if (CipherUtil.hasShareKey()) { // 鍵交換に成功
                    checkDAccountApp();
                } else {
                    createErrorDialog();
                }
            }
        });
    }

    /**
     * 鍵交換処理を同期処理で実行する.
     */
    private void syncRequestPublicKey() {
        CipherApi api = new CipherApi(new CipherApi.CipherApiCallback() {
            @Override
            public void apiCallback(final boolean result, final String data) {
                // 鍵交換処理同期ラッチカウンターを解除する
                mLatch.countDown();
            }
        });
        DTVTLogger.debug("sending public key");
        api.requestSendPublicKey();
        // 鍵交換処理が終わるまでキーコード送信を待機をさせる.
        mLatch = new CountDownLatch(LATCH_COUNT_MAX);
        try {
            DTVTLogger.debug("sync to completion of public key transmission");
            mLatch.await();
            DTVTLogger.debug("completion of public key transmission");
        } catch (InterruptedException e) {
            DTVTLogger.debug(e);
            return;
        }
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
    public void joinDms(String name, String host, String udn, String controlUrl, String eventSubscriptionUrl) {
        DlnaDmsItem item = new DlnaDmsItem();
        item.mFriendlyName = name;
        item.mIPAddress = host;
        item.mUdn = udn;
        item.mControlUrl = controlUrl;
        item.mHttp = host;
        mDlnaDmsInfo.add(item);
        updateDeviceList();
    }

    @Override
    public void leaveDms(String udn) {
        mDlnaDmsInfo.remove(udn);
        updateDeviceList();
    }

    /**
     * デバイスリスト情報を更新する.
     */
    private void updateDeviceList() {
        DTVTLogger.start();
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);

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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mCallbackTimer == null) {
                    mCallbackTimer = new StbInfoCallBackTimer(new Handler());
                }
                // 0件の場合タイムアウトを設定する
                if (mContentsList.size() <= 0) {
                    mDeviceAdapter.notifyDataSetChanged();
                    displayMoreData(true);
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
            super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select_loading));
        } else {
            mLoadMoreView.setVisibility(View.GONE);
            if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                super.sendScreenView(getString(R.string.google_analytics_screen_name_stb_select));
            } else {
                super.sendScreenView(getString(R.string.google_analytics_screen_name_setting_paring));
            }
        }
        DTVTLogger.end();
    }

    /**
     * STB情報取得のタイムアウト時間を設定.
     */
    private void startCallbackTimer() {
        DTVTLogger.start();
        showSearchingView();
        if (mCallbackTimer == null) {
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
        if (mCallbackTimer.getTimerStatus() == TimerStatus.TIMER_STATUS_DURING_STARTUP) {
            mCallbackTimer.cancel();
        }
        DTVTLogger.end();
    }

    /**
     * タイムアウト時の画面表示.
     */
    private void showTimeoutView() {
        DTVTLogger.start();
        displayMoreData((false));
        DlnaManager.shared().mDlnaManagerListener = null;
        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
            startActivity(StbSelectErrorActivity.class, null);
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

            //dカウント登録状態チェック
            boolean isDAccountFlag = checkDAccountLogin();

            if (isDAccountFlag) {
                setRelayClientHandler();
                RemoteControlRelayClient.getInstance().isUserAccountExistRequest(this);
                storeSTBData();
            } else {
                //端末内にdアカウントアプリがある場合はアプリ起動
                startActivity(intent);
                mDaccountFlag = true;
            }
            //ログイン後にユーザ操作でこの画面に戻ってきた際には再度STB選択を行わせる
        } catch (ActivityNotFoundException e) {
            revertSelectStbState();
        }
        DTVTLogger.end();
    }

    /**
     * 再度STB選択状態へ戻す.
     */
    private void revertSelectStbState() {
        DTVTLogger.debug("not daccount app");
        //端末内にdアカウントアプリがない場合はdアカウントアプリDL誘導を行う
        mIsAppDL = true;
        startActivity(DaccountInductionActivity.class, null);
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
                        //TODO ナスネとペアリングしたいときは以下をコメントアウト　SharedPreferencesにSTBデータを保存
                        SharedPreferencesUtils.setSharedPreferencesStbInfo(this, mDlnaDmsItemList.get(mSelectDevice));
                        Intent intent = new Intent(this, StbConnectActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
                                createErrorDialog();
                                break;
                            case RelayServiceResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID://指定ユーザIDなし
                                // チェック処理の状態で処理を分岐する
                                SharedPreferencesUtils.resetSharedPreferencesStbInfo(getApplicationContext());
                                startActivity(DaccountResettingActivity.class, null);
                                mParingTextView.setText(R.string.str_stb_no_pair_use_text);
                                break;
                            case RelayServiceResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                                // TODO STBと接続しないとHOMEにいけない為、本体側のSTB機能が搭載されるまでは一旦ホームに遷移させておく.
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
                        switch (resultCode) {
                            case RelayServiceResponseMessage.RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE:
                                CustomDialog dTVTUpDateDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                                dTVTUpDateDialog.setContent(getResources().getString(R.string.d_tv_terminal_application_version_update_dialog));
                                dTVTUpDateDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                                    @Override
                                    public void onOKCallback(final boolean isOK) {
                                        toGooglePlay(DTVTERMINAL_GOOGLEPLAY_DOWNLOAD_URL);
                                    }
                                });
                                dTVTUpDateDialog.showDialog();
                                break;
                            case RelayServiceResponseMessage.RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE:
                                showErrorDialog(getResources().getString(R.string.stb_application_version_update));
                                break;
                            default:
                                break;
                        }
                        break;
                    case CHECK_APPLICATION_REQUEST_PROCESSING:
                        //中継アプリからの応答待ち中に新しい要求を行った場合
                    case COMMAND_UNKNOWN:
                        switch (resultCode) {
                            case RelayServiceResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                            case RelayServiceResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY: // 他の端末の要求処理中
                                // TODO STBと接続しないとHOMEにいけない為、本体側のSTB機能が搭載されるまでは一旦ホームに遷移させておく.
                                createUnKnownDialog();
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
     * エラーダイアログ表示.
     */
    private void createUnKnownDialog() {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(StbSelectActivity.this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.main_setting_connect_error_message));
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                //初期状態に戻る
                resetDmpForResume();
                onResume();
                if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                    initLaunchView();
                }
            }
        });
        failedRecordingReservationDialog.showDialog();
    }

    /**
     * エラーダイアログ表示.
     */
    private void createErrorDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog failedRecordingReservationDialog = new CustomDialog(StbSelectActivity.this, CustomDialog.DialogType.ERROR);
                failedRecordingReservationDialog.setContent(getResources().getString(R.string.str_launch_stb_communication_failed_error));
                failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
                // Cancelable
                failedRecordingReservationDialog.setCancelable(false);
                failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        resetDmpForResume();
                        onResume();
                        if (mStartMode == StbSelectFromMode.StbSelectFromMode_Launch.ordinal()) {
                            initLaunchView();
                        }
                    }
                });
                failedRecordingReservationDialog.showDialog();
            }
        });
    }

    /**
     * STBとのペアリングエラー時からのペアリング選択画面の再起動処理.
     */
    private void resetDmpForResume() {
        DlnaManager.shared().mDlnaManagerListener = null;
        mContentsList.clear();
        mDlnaDmsItemList.clear();
        mDlnaDmsInfo.clear();
        DlnaManager.shared().StopDmp();
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
        restartDialog.setContent(printMessage);
        //startAppDialog.setTitle(getString(R.string.dTV_content_service_start_dialog));
        restartDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                //OKが押されたので、ホーム画面の表示
                reStartApplication();
            }
        });
        restartDialog.showDialog();
    }
}
