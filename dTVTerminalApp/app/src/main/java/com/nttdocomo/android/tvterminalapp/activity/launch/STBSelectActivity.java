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

import com.digion.dixim.android.activation.helper.ActivationHelper;
import com.digion.dixim.android.secureplayer.MediaPlayerDefinitions;
import com.digion.dixim.android.secureplayer.SecuredMediaPlayerController;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDevList;
import com.nttdocomo.android.tvterminalapp.jni.remote.DlnaInterfaceRI;
import com.nttdocomo.android.tvterminalapp.jni.remote.DlnaRemoteRet;
import com.nttdocomo.android.tvterminalapp.relayclient.RelayServiceResponseMessage;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.relayclient.StbConnectRelayClient;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherApi;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ペアリング、再ペアリングするためのクラス.
 */
public class STBSelectActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, DlnaDevListListener {

    // region variable
    /**
     * DMSリスト.
     */
    private List<DlnaDmsItem> mDlnaDmsItemList;
    /**
     * プログレスバー.
     */
    private ProgressBar mLoadMoreView = null;
    /**
     * DMSデバイス一覧を提供するクラス.
     */
    private DlnaProvDevList mDlnaProvDevList = null;
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
    /** アクティベーション.*/
    private ActivationHelper mActivationHelper;
    /** アクティベーションパス.*/
    private String mDeviceKey;
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
    public enum STBSelectFromMode {
        /**
         * ランチャーから起動.
         */
        STBSelectFromMode_Launch,
        /**
         * 設定から起動.
         */
        STBSelectFromMode_Setting,
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
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            setContentView(R.layout.stb_select_main_layout);
            initLaunchView();
        } else if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()) {
            setContentView(R.layout.stb_select_device_list_setting);
            initSettingView();
        }
        mDlnaDmsItemList = new ArrayList<>();
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
        if (mStartMode == (STBSelectFromMode.STBSelectFromMode_Launch.ordinal())) {
            //初回起動時
            enableHeaderBackIcon(false);
            setTitleText(getString(R.string.str_app_title));
            setStbStatusIconVisibility(false);
            enableGlobalMenuIcon(false);
            return;
        } else if (mStartMode == (STBSelectFromMode.STBSelectFromMode_Setting.ordinal())) {
            mParingTextView.setVisibility(View.VISIBLE);
            mLoadMoreView = findViewById(R.id.stb_device_setting_progressbar);
            //設定画面からの遷移
            enableHeaderBackIcon(true);
            setTitleText(getString(R.string.str_stb_paring_setting_title));
            setStbStatusIconVisibility(true);
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

            if (null == dlnaDmsItem) {
                //未ペアリング
                return;
            } else {
                //ペアリング済み
                DlnaDMSInfo info = new DlnaDMSInfo();
                info.add(dlnaDmsItem);
                updateDeviceList(info);
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
            DTVTLogger.debug("STBSelectFromMode :StartMode Error ");
        }
        DTVTLogger.end();
    }

    /**
     * デバイスListenerを設定する.
     */
    private void setDevListener() {
        DTVTLogger.start();
        if (null == mDlnaProvDevList) {
            mDlnaProvDevList = new DlnaProvDevList();
        }
        mDlnaProvDevList.start(this);
        updateDeviceList(mDlnaProvDevList.getDlnaDMSInfo());
        DTVTLogger.end();
    }

    @Override
    public void onResume() {
        super.onResume();
        DTVTLogger.start();
        //dアカウント情報取得
        setDaccountControl();
        initView();
        if (mIsAppDL) {
            //dアカウントアプリDLからの戻り時、各種Viewを初期状態に戻す
            mIsAppDL = false;
            if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {

                TextView statusTextView = findViewById(R.id.stb_select_status_text);

                //Android5.0未満の表示問題対策
                fixStatusTextView(statusTextView);

                mPairingImage.setImageResource(R.mipmap.startup_icon_01);
                statusTextView.setText(R.string.str_stb_select_result_text_search);
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckboxText.setVisibility(View.VISIBLE);
                mDeviceListView.setVisibility(View.VISIBLE);
                mParingTextView.setText(R.string.str_stb_no_pair_use_text);
            } else {
                RelativeLayout relativeLayout = findViewById(R.id.stb_icon_relative_layout_setting);
                relativeLayout.setVisibility(View.GONE);
                mPairingImage.setVisibility(View.GONE);
                TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);
                statusSetting.setVisibility(View.GONE);
                mDeviceSelectText.setVisibility(View.VISIBLE);
                mTextDivider2.setVisibility(View.VISIBLE);
                mDeviceListView.setVisibility(View.VISIBLE);
            }
        }

        setDevListener();
        DTVTLogger.end();
    }

    @Override
    public void onPause() {
        DTVTLogger.start();
        leaveActivity();
        displayMoreData(false);
        DTVTLogger.end();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        DTVTLogger.start();
        leaveActivity();
        displayMoreData(false);
        DTVTLogger.end();
        super.onDestroy();
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
                mDlnaProvDevList.stopListen();
            }
        });
        DTVTLogger.end();
    }

    /**
     * STB検索中の画面表示を設定.
     */
    private void showSearchingView() {
        DTVTLogger.start();
        // STB検索中文言表示
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);

            //Android5.0未満の表示問題対策
            fixStatusTextView(statusTextView);

            statusTextView.setText(R.string.str_stb_select_result_text_search);

            // STBが見つかるまで非表示
            TextView checkBoxText = findViewById(R.id.useWithoutPairingSTBParingInvitation);
            checkBoxText.setVisibility(View.VISIBLE);
        } else if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()) {
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
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
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
    private void showPairingeView() {
        DTVTLogger.start();
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);
            statusTextView.setText(R.string.str_stb_pairing);

            mCheckBox.setVisibility(View.GONE);
            mCheckboxText.setVisibility(View.GONE);
        } else if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()) {

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
        }
        DTVTLogger.end();
    }

    /**
     * ペアリングしないでアプリを利用するボタンタップ.
     */
    private void onUseWithoutPairingButton() {
        DTVTLogger.start();
        mDlnaProvDevList.stopListen();
        //STB選択画面"次回以降表示しない" 状態をSharedPreferenceに保存
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            SharedPreferencesUtils.setSharedPreferencesStbSelect(this, mIsNextTimeHide);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()) {
            if (mParingDevice.getVisibility() == View.VISIBLE) {
                //ペアリング解除する場合、すべてのSTBキャッシュデータを削除して、ホーム画面に遷移する
                mDlnaProvDevList.dmsRemove();
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
        //IPアドレスを設定する
        mRemoteControlRelayClient.setRemoteIp(mDlnaDmsItemList.get(i).mIPAddress);
        //ペアリング中画面を出す
        showPairingeView();
        //鍵交換
        exchangeKey();
    }

    private void exchangeKey() {
        CipherApi cipherApi = new CipherApi(new CipherApi.CipherApiCallback() {
            @Override
            public void apiCallback(boolean result, String data) {
                if (result) {
                    checkDAccountApp();
                } else {
                    // TODO: fail popup
                }
            }
        });
        cipherApi.requestSendPublicKey();
    }

    /**
     * 選択されたSTBを保存して画面遷移を行う.
     *
     * @param selectDevice 選択されたSTB
     */
    private void storeSTBData(final int selectDevice) {
        DTVTLogger.start();
        if (mCallbackTimer.getTimerStatus() != TimerStatus.TIMER_STATUS_DURING_STARTUP) {
            if (mDlnaDmsItemList != null) {
                if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()
                        && mParingDevice.getVisibility() == View.VISIBLE) {
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCheckMark.setVisibility(View.GONE);
                        }
                    });

                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * 新しいデバイスが見つかった時にリストに追加する.
     *
     * @param curInfo カレントDlnaDMSInfo.
     * @param newItem 新しいDms情報
     */
    @Override
    public void onDeviceJoin(final DlnaDMSInfo curInfo, final DlnaDmsItem newItem) {
        super.onDeviceJoin(curInfo, newItem);
        DTVTLogger.start();
        updateDeviceList(curInfo);
        DTVTLogger.end();
    }

    /**
     * デバイスが消える時リストから削除する.
     *
     * @param curInfo     　カレントDlnaDMSInfo
     * @param leaveDmsUdn 　消えるDmsのudn名
     */
    @Override
    public void onDeviceLeave(final DlnaDMSInfo curInfo, final String leaveDmsUdn) {
        super.onDeviceLeave(curInfo, leaveDmsUdn);
        DTVTLogger.start();
        updateDeviceList(curInfo);
        DTVTLogger.end();
    }

    /**
     * エラー発生時のログ出力.
     *
     * @param msg エラー情報
     */
    @Override
    public void onError(final String msg) {
        DTVTLogger.error("DevListListener error msg" + msg);
    }

    /**
     * デバイスリスト情報を更新する.
     *
     * @param info デバイスリスト情報
     */
    private void updateDeviceList(final DlnaDMSInfo info) {
        DTVTLogger.start();
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);

        mContentsList.clear();
        mDlnaDmsItemList.clear();
        for (int i = 0; i < info.size(); i++) {

            DTVTLogger.debug("ContentsList.size = " + info.get(i).mFriendlyName);
            DTVTLogger.debug("DlnaDMSInfo.mIPAddress = " + info.get(i).mIPAddress);
            DTVTLogger.debug("DlnaDMSInfo.mUdn = " + info.get(i).mUdn);
            boolean flag = false;
            if (dlnaDmsItem != null && !TextUtils.isEmpty(dlnaDmsItem.mUdn)) {
                if (dlnaDmsItem.mUdn.equals(info.get(i).mUdn)) {
                    flag = true;
                }
            }
            if (!flag) {
                HashMap<String, String> hashMap = new HashMap<>();
                // Todo: 試験のために以下のように一時的に変更する。試験後はコメントアウトした内容に戻すこと
                hashMap.put(DEVICE_NAME_KEY, StringUtils.getConnectString(new String[]{info.get(i).mFriendlyName, "_", info.get(i).mIPAddress}));
//                hashMap.put(DEVICE_NAME_KEY, info.get(i).mFriendlyName);
                hashMap.put(info.get(i).mUdn, info.get(i).mUdn);
                if (mContentsList.size() > 0) {
                    //0以上の場合重複チェック
                    for (HashMap<String, String> oldHashMap : mContentsList) {
                        if (!oldHashMap.containsKey(info.get(i).mUdn)) {
                            mContentsList.add(hashMap);
                            mDlnaDmsItemList.add(info.get(i));
                            //デバイス追加されたらbreak
                            break;
                        }
                    }
                } else {
                    //0件の場合そのままリストに追加する
                    mContentsList.add(hashMap);
                    mDlnaDmsItemList.add(info.get(i));
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
        } else {
            mLoadMoreView.setVisibility(View.GONE);
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
        mDlnaProvDevList.stopListen();
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            startActivity(STBSelectErrorActivity.class, null);
        } else {
            // リストを非表示
            mDeviceListView.setVisibility(View.GONE);
            if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()
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

                storeSTBData(mSelectDevice);
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
        startActivity(DAccountInductionActivity.class, null);
    }

    /**
     * ローカルレジストレーション.
     */
    private void localRegist() {
        //アクティーベション
        if (isActivited()) {
            //ローカルレジストレーション
            excuteLocalRegist();
        }
    }

    /**
     * ローカルレジストレーションを実行.
     */
    private void excuteLocalRegist() {
        DlnaInterfaceRI mDlnaInterfaceRI = new DlnaInterfaceRI();
        //start
        DlnaRemoteRet r = mDlnaInterfaceRI.startDlnaRI(this);
        //rigist
        mDlnaInterfaceRI.regist();
        //stop
        mDlnaInterfaceRI.stop();
    }

    /**
     * アクティベーションThread.
     */
    private class ActivationThread extends Thread {

        @Override
        public void run() {
            if (STBSelectActivity.this.mActivationHelper != null) {
                int result = STBSelectActivity.this.mActivationHelper.activation(STBSelectActivity.this.mDeviceKey);
                if (result == ActivationHelper.ACTC_OK) {
                    excuteLocalRegist();
                } else {
                    DTVTLogger.debug(getString(R.string.activation_failed_error));
                }
            }
        }
    }

    /**
     * 機能：プレバイトデータフォルダを戻す.
     *
     * @return プレバイトデータフォルダ
     */
    private String getPrivateDataHome() {
        return EnvironmentUtil.getPrivateDataHome(this, EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
    }

    /**
     * DTCP-IPアクティーベションを行う.
     * @return isActivited:true or false
     */
    private boolean isActivited() {
        DTVTLogger.start();
        boolean isActivited = false;
        String path = getPrivateDataHome();
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                DTVTLogger.debug("activited file path create failed");
            }
        }
        SecuredMediaPlayerController mPlayerController = new SecuredMediaPlayerController(this, true, true, true);
        int ret = mPlayerController.dtcpInit(path);
        if (ret != MediaPlayerDefinitions.SP_SUCCESS) {
            activate();
        } else {
            isActivited = true;
        }
        return isActivited;
    }

    /**
     * アクティベーションダイアログ表示.
     */
    private void activate() {
        DTVTLogger.start();
        mDeviceKey = getPrivateDataHome();
        mActivationHelper = new ActivationHelper(this, mDeviceKey);
        new ActivationThread().start();
        DTVTLogger.end();
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
                        //TODO ローカルレジストレーション処理を一応追加して、また修正が必要があります。
                        //localRegist();
                        //TODO ナスネとペアリングしたいときは以下をコメントアウト　SharedPreferencesにSTBデータを保存
                        SharedPreferencesUtils.setSharedPreferencesStbInfo(this, mDlnaDmsItemList.get(mSelectDevice));
                        Intent intent = new Intent(this, STBConnectActivity.class);
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
                            case RelayServiceResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE://ユーザアカウントチェックサービス未登録
                            case RelayServiceResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY:// //中継アプリからの応答待ち中に新しい要求を行った場合
                            case RelayServiceResponseMessage.RELAY_RESULT_CONNECTION_TIMEOUT: //STBの中継アプリ~応答が無かった場合(要求はできたのでSTBとの通信はOK)
                                createErrorDialog();
                                break;
                            case RelayServiceResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID://指定ユーザIDなし
                                // チェック処理の状態で処理を分岐する
                                SharedPreferencesUtils.resetSharedPreferencesStbInfo(getApplicationContext());
                                startActivity(DAccountReSettingActivity.class, null);
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
        CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.main_setting_connect_error_message));
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                //初期状態に戻る
                onResume();
                if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
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
        CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.str_stb_stb_error));
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                revertSelectStbState();
            }
        });
        failedRecordingReservationDialog.showDialog();
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
        String printMessage = getString(R.string.d_account_chamge_message);

        //引数がある場合はその先頭を使用する
        if (message != null && message.length > 0) {
            printMessage = message[0];
        }

        //ペアリング画面の時はダイアログ出さない
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            return;
        }
        //ペアリング設定画面かつユーザーがデバイスを選択してDアカウントを登録の場合はダイアログ出さない
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal() && mDaccountFlag) {
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
