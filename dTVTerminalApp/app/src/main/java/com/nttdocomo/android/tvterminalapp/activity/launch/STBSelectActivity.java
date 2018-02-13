/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
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
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDevList;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class STBSelectActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, DlnaDevListListener {

    private List<DlnaDmsItem> mDlnaDmsItemList;
    private ProgressBar mLoadMoreView = null;
    private DlnaProvDevList mDlnaProvDevList = null;
    private StbInfoCallBackTimer mCallbackTimer = null;
    private List<HashMap<String, String>> mContentsList = new ArrayList<>();
    private int mStartMode = 0;
    private int mSelectDevice;
    private boolean mIsNextTimeHide = false;
    private boolean mIsAppDL = false;
    private SimpleAdapter mDeviceAdapter;
    private ImageView mCheckMark;
    private ImageView mPairingImage;
    private CheckBox mCheckBoxSTBSelectActivity = null;
    private TextView mUseWithoutPairingSTBParingInvitationTextView = null;
    private TextView mParingDevice;
    private TextView mCheckboxText;
    private ListView mDeviceListView;
    private TextView mTextDivider1;
    private TextView mTextDivider2;
    private TextView mDeviceSelectText;

    public static final String StateModeRepair = "Repair";
    public static final String FROM_WHERE = "FROM_WHERE";
    /**
     * Dアカウントアプリ Package名
     */
    private static final String D_ACCOUNT_APP_PACKAGE_NAME = "com.nttdocomo.android.idmanager";
    /**
     * Dアカウントアプリ Activity名
     */
    private static final String D_ACCOUNT_APP_ACTIVITY_NAME=".activity.DocomoIdTopActivity";
    private static final int MARGIN0 = 0;
    private static final int MARGIN84 = 84;
    private static final int PROGRESSSIZE = 16;
    private static final int MARGIN23 = 23;
    private static final int MARGIN9 = 9;
    private static final String DEVICE_NAME_KEY = "DEVICE_NAME_KEY";

    private enum TimerStatus {
        /**
         * 初期状態
         */
        TIMER_STATUS_DEFAULT,
        /**
         * 起動中
         */
        TIMER_STATUS_DURING_STARTUP,
        /**
         * タイマー処理実行済み
         */
        TIMER_STATUS_EXECUTION,
        /**
         * キャンセル
         */
        TIMER_STATUS_CANCEL,
    }

    public enum STBSelectFromMode {
        /**
         * ランチャーから起動
         */
        STBSelectFromMode_Launch,
        /**
         * 設定から起動
         */
        STBSelectFromMode_Setting,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        Intent intent = getIntent();
        if (intent != null) {
            mStartMode = intent.getIntExtra(FROM_WHERE, -1);
        }
        //
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            setContentView(R.layout.stb_select_main_layout);
            initLaunchView();
        } else if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()) {
            setContentView(R.layout.stb_select_device_list_setting);
            initSettingView();
        }
        mDlnaDmsItemList = new ArrayList<>();
        DTVTLogger.end();
    }

    /**
     * 設定から起動時のビューの初期化
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
        mUseWithoutPairingSTBParingInvitationTextView = findViewById(R.id.use_without_paring_setting);
        mPairingImage = findViewById(R.id.paring_search_image_setting);
        mUseWithoutPairingSTBParingInvitationTextView.setVisibility(View.VISIBLE);
        mUseWithoutPairingSTBParingInvitationTextView.setOnClickListener(this);

        mTextDivider1 = findViewById(R.id.paring_device_select_setting_divider1);
        mTextDivider2 = findViewById(R.id.paring_device_select_setting_divider2);
        mDeviceSelectText = findViewById(R.id.paring_device_select_text);
    }

    /**
     * ランチャーから起動時のビューの初期化
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
        mCheckBoxSTBSelectActivity = findViewById(R.id.checkBoxSTBSelectActivity);
        mCheckboxText = findViewById(R.id.launch_select_check_box_text);
        mCheckBoxSTBSelectActivity.setVisibility(View.VISIBLE);
        mCheckboxText.setVisibility(View.VISIBLE);
        mCheckBoxSTBSelectActivity.setOnClickListener(this);
        mCheckboxText.setOnClickListener(this);
        mCheckBoxSTBSelectActivity.setChecked(false);
        mCheckBoxSTBSelectActivity.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                        if (mCheckBoxSTBSelectActivity.isChecked()) {
                            mCheckBoxSTBSelectActivity.setBackgroundResource(R.drawable.
                                    ic_check_box_outline_blank_white_24dp);
                        } else {
                            mCheckBoxSTBSelectActivity.setBackgroundResource(R.drawable.
                                    ic_check_box_white_24dp);
                        }
                    }
                });
        mUseWithoutPairingSTBParingInvitationTextView = findViewById(
                R.id.useWithoutPairingSTBParingInvitation);
        mUseWithoutPairingSTBParingInvitationTextView.setOnClickListener(this);
    }

    @Override
    protected void checkDAccountOnRestart() {
        DTVTLogger.start();
    }

    /**
     * 画面上に表示するコンテンツの初期化を行う
     */
    private void initView() {
        DTVTLogger.start();
        if (mStartMode == (STBSelectFromMode.STBSelectFromMode_Launch.ordinal())) {
            //初回起動時
            enableHeaderBackIcon(false);
            setTitleText(getString(R.string.str_app_title));
            setStbStatusIconVisibility(false);
            enableGlobalMenuIcon(false);
            return;
        } else if (mStartMode == (STBSelectFromMode.STBSelectFromMode_Setting.ordinal())) {
            mLoadMoreView = findViewById(R.id.stb_device_setting_progressbar);
//            //設定画面からの遷移
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
            boolean status = true;
            //sharedPreferencesからSTB情報を取得する
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
            if ((null != dlnaDmsItem && null != dlnaDmsItem.mUdn && 0 == dlnaDmsItem.mUdn.length())) {
                //未ペアリング
                status = false;
            }
            setStbStatus(status);

//            mUseWithoutPairingSTBParingInvitationTextView.setVisibility(View.GONE);
            ImageView mMenuImageView = findViewById(R.id.header_layout_menu);
            mMenuImageView.setVisibility(View.VISIBLE);
            mMenuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFastClick()) {
                        onSampleGlobalMenuButton_PairLoginOk();
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
                    mUseWithoutPairingSTBParingInvitationTextView.setVisibility(View.VISIBLE);
                    mUseWithoutPairingSTBParingInvitationTextView.setText(R.string.str_stb_paring_cancel_text);
                }
            }
        } else {
            DTVTLogger.debug("STBSelectFromMode :StartMode Error ");
        }
        DTVTLogger.end();
    }

    /**
     * デバイスListenerを設定する
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
        initView();
//        setContents();
        if (mIsAppDL) {
            //dアカウントアプリDLからの戻り時、各種Viewを初期状態に戻す
            mIsAppDL = false;
            if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {

                TextView statusTextView = findViewById(R.id.stb_select_status_text);
                mPairingImage.setImageResource(R.mipmap.startup_icon_01);
                statusTextView.setText(R.string.str_stb_select_result_text_search);
                mCheckBoxSTBSelectActivity.setVisibility(View.VISIBLE);
                mCheckboxText.setVisibility(View.VISIBLE);
                mDeviceListView.setVisibility(View.VISIBLE);
                mUseWithoutPairingSTBParingInvitationTextView.setText(R.string.str_stb_no_pair_use_text);
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
     * 画面から離れる場合の処理
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
     * STB検索中の画面表示を設定
     */
    private void showSearchingView() {
        DTVTLogger.start();
        // STB検索中文言表示
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);
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
     * STBが見つかった際の画面表示を設定
     */
    private void showResultCompleteView() {
        DTVTLogger.start();
        // STB検索中文言表示
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);
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
     * ペアリング中画面表示を設定
     */
    private void showPairingeView() {
        DTVTLogger.start();
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            TextView statusTextView = findViewById(R.id.stb_select_status_text);
            statusTextView.setText(R.string.str_stb_pairing);

            mCheckBoxSTBSelectActivity.setVisibility(View.GONE);
            mCheckboxText.setVisibility(View.GONE);
        } else if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()) {

            if (mParingDevice.getVisibility() == View.VISIBLE && mCheckMark.getVisibility() == View.VISIBLE) {
                mCheckMark.setVisibility(View.GONE);
                mParingDevice.setVisibility(View.GONE);
            }
            TextView statusSetting = findViewById(R.id.stb_select_status_text_setting);
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
                RelativeLayout newLayout = (RelativeLayout) findViewById(R.id.stb_device_list_relative_layout);
                newLayout.addView(mLoadMoreView);
            }
        }
        mUseWithoutPairingSTBParingInvitationTextView.setVisibility(View.INVISIBLE);
        mDeviceListView.setVisibility(View.GONE);
        mLoadMoreView.setVisibility(View.VISIBLE);
        DTVTLogger.end();
    }

    /**
     * ボタン押されたときの動作
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        DTVTLogger.start();
        if (v.equals(mUseWithoutPairingSTBParingInvitationTextView)) {
            if (mUseWithoutPairingSTBParingInvitationTextView.getText().equals(
                    getString(R.string.str_stb_no_pair_use_text))
                    || mUseWithoutPairingSTBParingInvitationTextView.getText().equals
                    (getString(R.string.str_stb_paring_cancel_text))) {
                onUseWithoutPairingButton();
            }
        } else if (v.equals(mCheckBoxSTBSelectActivity)) {
            mIsNextTimeHide = mCheckBoxSTBSelectActivity.isChecked();
        } else if (v.getId() == R.id.launch_select_check_box_text) {
            if (!mCheckBoxSTBSelectActivity.isChecked()) {
                mCheckBoxSTBSelectActivity.setChecked(true);
            } else {
                if (mCheckBoxSTBSelectActivity.isChecked()) {
                    mCheckBoxSTBSelectActivity.setChecked(false);
                }
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

    // TODO dアカウント取得画面実装時に削除

    /**
     * STBに同じdアカウントが登録されていない
     */
    private void onDAccountSameNoButton() {
        startActivity(DAccountReSettingActivity.class, null);
    }

    private void onUseWithoutPairingButton() {
        DTVTLogger.start();
        mDlnaProvDevList.stopListen();
        //STB選択画面"次回以降表示しない" 状態をSharedPreferenceに保存
        if (mStartMode == STBSelectFromMode.STBSelectFromMode_Launch.ordinal()) {
            SharedPreferencesUtils.setSharedPreferencesStbSelect(this, mIsNextTimeHide);
            startActivity(HomeActivity.class, null);
        } else if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()) {
            if (mParingDevice.getVisibility() == View.VISIBLE) {
                //ペアリング解除する場合、すべてのSTBキャッシュデータを削除して、ホーム画面に遷移する
                mDlnaProvDevList.dmsRemove();
                SharedPreferencesUtils.resetSharedPreferencesStbInfo(this);
                startActivity(HomeActivity.class, null);
            } else {
                //ペアリングししないで利用する場合、設定画面に戻る
                finish();
            }
        }
        DTVTLogger.end();
    }

    /**
     * STB選択画面でデバイス名Itemがタップされた時に画面遷移する
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DTVTLogger.start();

        showPairingeView();

        //選択されたSTB番号を保持
        mSelectDevice = i;
        checkDAccountApp();
//        //dカウント登録状態チェック
//        checkDAccountLogin();
    }

    /**
     * 選択されたSTBを保存して画面遷移を行う
     * @param selectDevice 選択されたSTB
     */
    private void storeSTBData( int selectDevice) {
        DTVTLogger.start();
        if (mCallbackTimer.getTimerStatus() != TimerStatus.TIMER_STATUS_DURING_STARTUP) {
            // SharedPreferencesにSTBデータを保存
            //SharedPreferencesUtils.setSharedPreferencesStbInfo(this, mDlnaDmsItemList.get(selectDevice));
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
        //dアカチェックテスト終わったら、コメントアウト
//        startActivity(STBConnectActivity.class, null);
        DTVTLogger.end();
    }

    /**
     * 新しいデバイスが見つかった時にリストに追加する
     *
     * @param curInfo カレントDlnaDMSInfo
     * @param newItem 新しいDms情報
     */
    @Override
    public void onDeviceJoin(final DlnaDMSInfo curInfo, final DlnaDmsItem newItem) {
        DTVTLogger.start();
        updateDeviceList(curInfo);
        DTVTLogger.end();
    }

    /**
     * デバイスが消える時リストから削除する
     *
     * @param curInfo     　カレントDlnaDMSInfo
     * @param leaveDmsUdn 　消えるDmsのudn名
     */
    @Override
    public void onDeviceLeave(final DlnaDMSInfo curInfo, final String leaveDmsUdn) {
        DTVTLogger.start();
        updateDeviceList(curInfo);
        DTVTLogger.end();
    }

    /**
     * エラー発生時のログ出力
     *
     * @param msg エラー情報
     */
    @Override
    public void onError(String msg) {
        DTVTLogger.error("DevListListener error msg" + msg);
    }

    /**
     * デバイスリスト情報を更新する
     * @param info
     */
    private void updateDeviceList(final DlnaDMSInfo info) {
        DTVTLogger.start();
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);

        mContentsList.clear();
        mDlnaDmsItemList.clear();
        for (int i = 0; i < info.size(); i++) {

            DTVTLogger.debug("ContentsList.size = " + info.get(i).mFriendlyName);
            DTVTLogger.debug("DlnaDMSInfo.mIPAddress = " + info.get(i).mIPAddress);
            boolean flag = false;
            if (dlnaDmsItem != null && !TextUtils.isEmpty(dlnaDmsItem.mUdn)) {
                if (dlnaDmsItem.mUdn.equals(info.get(i).mUdn)) {
                    flag = true;
                }
            }
            if (!flag) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(DEVICE_NAME_KEY, info.get(i).mFriendlyName);
                hashMap.put(info.get(i).mUdn, info.get(i).mUdn);
                if (mContentsList.size() > 0) {
                    //0以上の場合重複チェック
                    for (HashMap<String, String> oldHashMap : mContentsList) {
                        if (!oldHashMap.containsKey(info.get(i).mUdn)) {
                            mContentsList.add(hashMap);
                            mDlnaDmsItemList.add(info.get(i));
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
     * 再読み込み時のダイアログ表示処理
     * @param b
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
     * STB情報取得のタイムアウト時間を設定
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
     * タイムアウト時間設定を解除
     */
    private void stopCallbackTimer() {
        DTVTLogger.start();
        if (mCallbackTimer.getTimerStatus() == TimerStatus.TIMER_STATUS_DURING_STARTUP) {
            mCallbackTimer.timerTaskCancel();
        }
        DTVTLogger.end();
    }

    /**
     * タイムアウト時の画面表示
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
//            TextView statusTextView = findViewById(R.id.stb_select_status_text);
            if (mStartMode == STBSelectFromMode.STBSelectFromMode_Setting.ordinal()
                    && !mParingDevice.getText().toString().isEmpty()) {
                mUseWithoutPairingSTBParingInvitationTextView.setText(R.string.str_stb_paring_cancel_text);
            }
            // STB検索タイムアウト文言表示
//            statusTextView.setText(R.string.str_stb_select_result_text_failed);
            TextView stbSearchFailed = findViewById(R.id.stb_device_search_result);
            TextView stbSearchHelp = findViewById(R.id.stb_paring_failed_red_help);
            stbSearchFailed.setVisibility(View.VISIBLE);
            stbSearchHelp.setVisibility(View.VISIBLE);
            stbSearchHelp.setOnClickListener(this);
        }
        DTVTLogger.end();
    }

    // タイムアウト設定クラス
    private class StbInfoCallBackTimer extends Timer {
        /**
         * STB検出タイムアウト時間
         */
        private static final long STB_SEARCH_TIMEOUT = 30000;
        private TimerTask mTimerTask = null;
        private Handler mHandler = null;
        /**
         * タイマーの状態
         */
        private TimerStatus mTimerStatus = TimerStatus.TIMER_STATUS_DEFAULT;

        private StbInfoCallBackTimer(Handler handler) {
            mHandler = handler;
        }

        /**
         * TimerTask実行予約処理
         */
        private void executeTimerTask() {
            DTVTLogger.start();
            setTimerTask();
            mTimerStatus = TimerStatus.TIMER_STATUS_DURING_STARTUP;
            schedule(mTimerTask, STB_SEARCH_TIMEOUT);
            DTVTLogger.end();
        }

        /**
         * TimerTask処理の設定
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
         * TimerTaskキャンセル処理
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
         * TimerTask実行状態取得
         *
         * @return mTimerStatus:タイムアウト処理実行状態
         */
        private TimerStatus getTimerStatus() {
            return mTimerStatus;
        }
    }

    /**
     * dアカウントの登録状態を確認する
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
     * dアカウントアプリが端末にインストールされているか確認を行い、インストールされている場合は
     * dアカウントアプリを起動する
     */
    private void checkDAccountApp() {
        DTVTLogger.start();
        Intent intent = new Intent();
        intent.setClassName(D_ACCOUNT_APP_PACKAGE_NAME,
                D_ACCOUNT_APP_PACKAGE_NAME + D_ACCOUNT_APP_ACTIVITY_NAME);
        try {
            //端末内にdアカウントアプリがある場合はアプリ起動
//            startActivity(intent);

            //dカウント登録状態チェック
            boolean isDAccountFlag = checkDAccountLogin();

            if (isDAccountFlag) {
                setRelayClientHandler();
                RemoteControlRelayClient.getInstance().isUserAccountExistRequest(this);

                storeSTBData(mSelectDevice);
            } else {
                startActivity(intent);
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

    @Override
    protected void onStbClientResponse(final Message msg){
        RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES requestCommand
                = ((RemoteControlRelayClient.ResponseMessage) msg.obj).getRequestCommandTypes();
        DTVTLogger.debug("msg.what: " + msg.what + "requestCommand: " + requestCommand);
        DTVTLogger.debug(String.format("requestCommand:%s", requestCommand));
        switch (msg.what) {
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_OK:
                switch (requestCommand) {
                    case START_APPLICATION:
                    case TITLE_DETAIL:
                        menuRemoteController();
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                        //TODO 皆さんに影響内容ないようにコメントアウト
                        SharedPreferencesUtils.setSharedPreferencesStbInfo(this, mDlnaDmsItemList.get(mSelectDevice));
                        startActivity(STBConnectActivity.class, null);
                        Intent intent = new Intent(this, STBConnectActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case COMMAND_UNKNOWN:
                    default:
                        break;
                }
                break;
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_ERROR:
                int resultcode = ((RemoteControlRelayClient.ResponseMessage) msg.obj).getResultCode();
                DTVTLogger.debug("resultcode: " + resultcode);
                switch (requestCommand) {
                    case START_APPLICATION:
                    case TITLE_DETAIL:
                        RemoteControlRelayClient.STB_APPLICATION_TYPES appId
                                = ((RemoteControlRelayClient.ResponseMessage) msg.obj).getApplicationTypes();
                        startApplicationErrorHandler(resultcode, appId);
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                        switch (resultcode) {
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_INTERNAL_ERROR://サーバエラー
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE://ユーザアカウントチェックサービス未登録
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY:// //中継アプリからの応答待ち中に新しい要求を行った場合
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_CONNECTION_TIMEOUT: //STBの中継アプリ~応答が無かった場合(要求はできたのでSTBとの通信はOK)
                                createErrorDialog();
                                break;
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID://指定ユーザIDなし
                                // チェック処理の状態で処理を分岐する
                                SharedPreferencesUtils.resetSharedPreferencesStbInfo(getApplicationContext());
                                Intent intent = new Intent(this, DAccountReSettingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                                // TODO STBと接続しないとHOMEにいけない為、本体側のSTB機能が搭載されるまでは一旦ホームに遷移させておく.
                                Intent homeintent = new Intent(this, HomeActivity.class);
                                homeintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeintent);

                                break;
                            default:
                                break;
                        }
                        break;
                    case COMMAND_UNKNOWN:
                        switch (resultcode) {
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                                // TODO STBと接続しないとHOMEにいけない為、本体側のSTB機能が搭載されるまでは一旦ホームに遷移させておく.
                                Intent homestartintent = new Intent(this, HomeActivity.class);
                                homestartintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homestartintent);
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
    private CustomDialog createErrorDialog() {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.str_stb_stb_error));
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                revertSelectStbState();
            }
        });
        return failedRecordingReservationDialog;
    }
}
