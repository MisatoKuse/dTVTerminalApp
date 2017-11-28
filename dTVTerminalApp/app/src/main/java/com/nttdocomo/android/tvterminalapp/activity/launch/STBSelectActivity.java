/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.temp.DAccountAppliActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDevList;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class STBSelectActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, DlnaDevListListener {
    public static final String StateModeRepair = "Repair";
    private boolean mIsNextTimeHide = false;
    CheckBox mCheckBoxSTBSelectActivity = null;
    TextView useWithoutPairingSTBParingInvitationTextView = null;
    Button mDAccountLoginYesSTBSelectActivity = null;
    Button mDAccountLoginNoSTBSelectActivity = null;
    Button mDAccountAppliYesSTBSelectActivity = null;
    Button mDAccountAppliNoSTBSelectActivity = null;
    Button mDAccountSameYesSTBSelectActivity = null;
    Button mDAccountSameNoSTBSelectActivity = null;
    private TextView mBackIcon;
    private ImageView mParingImageView;
    private ListView mDeviceListView;
    List<ContentsData> mContentsList;
    private ContentsAdapter mContentsAdapter;
    private View mLoadMoreView = null;
    private DlnaProvDevList mDlnaProvDevList = null;
    private StbInfoCallBackTimer mCallbackTimer = null;
    private DlnaDMSInfo mDlnaDMSInfo = null;

    private enum TimerStatus {
        TIMER_STATUS_DEFAULT,// 初期状態
        TIMER_STATUS_DURING_STARTUP, // 起動中
        TIMER_STATUS_EXECUTION, // タイマー処理実行済み
        TIMER_STATUS_CANCEL, // キャンセル
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.stb_select_main_layout);
        mContentsList = new ArrayList();
        mBackIcon = findViewById(R.id.header_layout_back);
        mBackIcon.setVisibility(View.GONE);
        mParingImageView = findViewById(R.id.header_layout_menu);
        mParingImageView.setImageResource(R.mipmap.ic_personal_video_white_24dp);
        mParingImageView.setVisibility(View.INVISIBLE);
        setTitleText(getString(R.string.str_app_title));

        DTVTLogger.end();
    }
    //TODO 削除する予定
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    /**
     * 画面上に表示するコンテンツの初期化を行う
     */
    private void initView() {
        DTVTLogger.start();
        mDeviceListView = findViewById(R.id.stb_device_name_list);
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_STB_SELECT_LIST);
        mDeviceListView.setAdapter(mContentsAdapter);
        mDeviceListView.setOnItemClickListener(this);
        mDeviceListView.setVisibility(View.VISIBLE);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
        DTVTLogger.end();
    }

    /**
     * デバイスListenerを設定する
     */
    private void setDevListener() {
        DTVTLogger.start();
        if(null==mDlnaProvDevList){
            mDlnaProvDevList = new DlnaProvDevList();

        }
        mDlnaProvDevList.start(this);
        updateDeviceList(mDlnaProvDevList.getDlnaDMSInfo());
        DTVTLogger.end();
    }

    /**
     * 画面上に表示するコンテンツを設定する
     */
    private void setContents() {
        DTVTLogger.start();
        useWithoutPairingSTBParingInvitationTextView = (TextView) findViewById(
                R.id.useWithoutPairingSTBParingInvitation);
        useWithoutPairingSTBParingInvitationTextView.setOnClickListener(this);

        mCheckBoxSTBSelectActivity = (CheckBox) findViewById(R.id.checkBoxSTBSelectActivity);
        mCheckBoxSTBSelectActivity.setOnClickListener(this);
        mCheckBoxSTBSelectActivity.setChecked(false);

        // TODO dアカウント取得画面実装時に削除
        mDAccountLoginYesSTBSelectActivity = (Button) findViewById(R.id.dAccountLoginYesSTBSelectActivity);
        mDAccountLoginYesSTBSelectActivity.setOnClickListener(this);

        mDAccountLoginNoSTBSelectActivity = (Button) findViewById(R.id.dAccountLoginNoSTBSelectActivity);
        mDAccountLoginNoSTBSelectActivity.setOnClickListener(this);

        mDAccountAppliYesSTBSelectActivity = (Button) findViewById(R.id.dAccountAppliYesSTBSelectActivity);
        mDAccountAppliYesSTBSelectActivity.setOnClickListener(this);

        mDAccountAppliNoSTBSelectActivity = (Button) findViewById(R.id.dAccountAppliNoSTBSelectActivity);
        mDAccountAppliNoSTBSelectActivity.setOnClickListener(this);

        mDAccountSameYesSTBSelectActivity = (Button) findViewById(R.id.dAccountSameYesSTBSelectActivity);
        mDAccountSameYesSTBSelectActivity.setOnClickListener(this);

        mDAccountSameNoSTBSelectActivity = (Button) findViewById(R.id.dAccountSameNoSTBSelectActivity);
        mDAccountSameNoSTBSelectActivity.setOnClickListener(this);

        setDAccountButtonVisibility(View.GONE);

        DTVTLogger.end();
    }

    @Override
    public void onResume() {
        super.onResume();
        DTVTLogger.start();
        // この画面に来た時点でSharedPreferencesのSTB情報をリセットする
        SharedPreferencesUtils.resetSharedPreferencesStbInfo(this);
        setContents();
        initView();
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
    public void leaveActivity() {
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
        TextView statusTextView = (TextView) findViewById(R.id.stb_select_status_text);
        statusTextView.setText(R.string.str_stb_select_result_text_search);

        // STBが見つかるまで非表示
        mCheckBoxSTBSelectActivity.setVisibility(View.INVISIBLE);
        mParingImageView.setVisibility(View.INVISIBLE);
        DTVTLogger.end();
    }

    /**
     * STBが見つかった際の画面表示を設定
     */
    private void showResultCompleteView() {
        DTVTLogger.start();
        // STB検索中文言表示
        TextView statusTextView = (TextView) findViewById(R.id.stb_select_status_text);
        statusTextView.setText(R.string.str_stb_select_result_text);

        // STBが見つかったため表示する
        mParingImageView.setVisibility(View.VISIBLE);
        mCheckBoxSTBSelectActivity.setVisibility(View.VISIBLE);
        TextView checkBoxText = (TextView) findViewById(R.id.useWithoutPairingSTBParingInvitation);
        checkBoxText.setVisibility(View.VISIBLE);
        DTVTLogger.end();
    }

    /**
     * ボタン押されたときの動作
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO dアカウント取得画面実装時に一部を除いて削除
        DTVTLogger.start();
        if (v.equals(useWithoutPairingSTBParingInvitationTextView)) {
            onUseWithoutPairingButton();
        } else if (v.equals(mDAccountLoginYesSTBSelectActivity)) {
            onDAccountLoginYesButton();
        } else if (v.equals(mDAccountLoginNoSTBSelectActivity)) {
            onDAccountLoginNoButton();
        } else if (v.equals(mDAccountAppliYesSTBSelectActivity)) {
            onDAccountAppliYesButton();
        } else if (v.equals(mDAccountAppliNoSTBSelectActivity)) {
            onDAccountAppliNoButton();
        } else if (v.equals(mDAccountSameYesSTBSelectActivity)) {
            onDAccountSameYesButton();
        } else if (v.equals(mDAccountSameNoSTBSelectActivity)) {
            onDAccountSameNoButton();
        } else if (v.equals(mCheckBoxSTBSelectActivity)) {
            mIsNextTimeHide = mCheckBoxSTBSelectActivity.isChecked();
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

    // TODO dアカウント取得画面実装時に削除

    /**
     * STBに同じdアカウントが登録されている
     */
    private void onDAccountSameYesButton() {
        startActivity(STBConnectActivity.class, null);
    }

    // TODO dアカウント取得画面実装時に削除

    /**
     * 端末内にdアカウントアプリがあるか --> ない
     * dアカウントアプリ誘導画面へ
     */
    private void onDAccountAppliNoButton() {
        startActivity(DAccountSettingActivity.class, null);
    }

    // TODO dアカウント取得画面実装時に削除

    /**
     * 端末内にdアカウントアプリがあるか --> ある
     */
    private void onDAccountAppliYesButton() {

        startActivity(DAccountAppliActivity.class, null);
    }

    // TODO dアカウント取得画面実装時に削除

    /**
     * dアカウント登録状態チェック --> 未ログイン
     */
    private void onDAccountLoginNoButton() {
        setDAccountAppliButtonsVisibility(View.VISIBLE);
        setDAccountLoginButtonsVisibility(View.GONE);
    }

    // TODO dアカウント取得画面実装時に削除

    /**
     * dアカウント登録状態チェック --> ログイン済
     */
    private void onDAccountLoginYesButton() {
        setDAccountLoginButtonsVisibility(View.GONE);
        setDAccountSameButtonsVisibility(View.VISIBLE);
    }

    private void onUseWithoutPairingButton() {
        DTVTLogger.start();
        mDlnaProvDevList.stopListen();
        //STB選択画面"次回以降表示しない" 状態をSharedPreferenceに保存
        SharedPreferencesUtils.setSharedPreferencesStbSelect(this, mIsNextTimeHide);
        SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                this, false);
        if (SharedPreferencesUtils.getSharedPreferencesIsDisplayedParingInvitation(this)) {
            startActivity(HomeActivity.class, null);
        } else {
            startActivity(STBParingInvitationActivity.class, null);
        }
        DTVTLogger.end();
    }

    //TODO　dアカウントクラス実装次第削除する予定
    private void setDAccountButtonVisibility(int visibility) {
        mDAccountLoginYesSTBSelectActivity.setVisibility(visibility);
        mDAccountLoginNoSTBSelectActivity.setVisibility(visibility);

        mDAccountAppliYesSTBSelectActivity.setVisibility(visibility);
        mDAccountAppliNoSTBSelectActivity.setVisibility(visibility);

        mDAccountSameYesSTBSelectActivity.setVisibility(visibility);
        mDAccountSameNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountLoginButtonsVisibility(int visibility) {
        mDAccountLoginYesSTBSelectActivity.setVisibility(visibility);
        mDAccountLoginNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountAppliButtonsVisibility(int visibility) {
        mDAccountAppliYesSTBSelectActivity.setVisibility(visibility);
        mDAccountAppliNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountSameButtonsVisibility(int visibility) {
        mDAccountSameYesSTBSelectActivity.setVisibility(visibility);
        mDAccountSameNoSTBSelectActivity.setVisibility(visibility);
    }

    /**
     * STB選択画面でデバイス名Itemがタップされた時に画面遷移する
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DTVTLogger.start();
        if (mCallbackTimer.getTimerStatus() != TimerStatus.TIMER_STATUS_DURING_STARTUP) {
            // SharedPreferencesにSTBデータを保存
            if (mDlnaDMSInfo != null) {
                SharedPreferencesUtils.setSharedPreferencesStbInfo(this, mDlnaDMSInfo.get(i));
            }
            // TODO dアカウントクラス実装時に遷移先を修正
            startActivity(STBConnectActivity.class, null);
        }
        DTVTLogger.end();
    }

    /**
     * 新しいデバイスが見つかった時にリストに追加する
     *
     * @param curInfo カレントDlnaDMSInfo
     * @param newItem 新しいDms情報
     */
    @Override
    public void onDeviceJoin(DlnaDMSInfo curInfo, DlnaDmsItem newItem) {
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
    public void onDeviceLeave(DlnaDMSInfo curInfo, String leaveDmsUdn) {
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
     */
    private void updateDeviceList(DlnaDMSInfo info) {
        DTVTLogger.start();
        mContentsList.clear();
        for (int i = 0; i < info.size(); i++) {
            ContentsData data = new ContentsData();
            data.setDeviceName(info.get(i).mFriendlyName);
            DTVTLogger.debug("ContentsList.size = " + info.get(i).mFriendlyName);
            DTVTLogger.debug("DlnaDMSInfo.mIPAddress = " + info.get(i).mIPAddress);
            mContentsList.add(data);
        }
        mDlnaDMSInfo = info;
        DTVTLogger.debug("ContentsList.size = " + mContentsList.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 0件の場合タイムアウトを設定する
                if (mContentsList.size() <= 0) {
                    mContentsAdapter.notifyDataSetChanged();
                    displayMoreData(true);
                    startCallbackTimer();
                    DTVTLogger.debug("ContentsList.size <= 0 ");
                } else if (mCallbackTimer.getTimerStatus() != TimerStatus.TIMER_STATUS_EXECUTION) { // 30秒以内にSTBの通知あり
                    displayMoreData(false);
                    stopCallbackTimer();
                    showResultCompleteView();
                    mContentsAdapter.notifyDataSetChanged();
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
     *
     * @param b
     */
    private void displayMoreData(boolean b) {
        DTVTLogger.start("displayMoreData:" + b);
        if (null != mDeviceListView && null != mLoadMoreView) {
            if (b) {
                mDeviceListView.addFooterView(mLoadMoreView);
            } else {
                mDeviceListView.removeFooterView(mLoadMoreView);
            }
        } else if (null == mDeviceListView) {
            DTVTLogger.debug("mDeviceListView is Null");

        } else {
            DTVTLogger.debug("mLoadMoreView is Null");
        }
        DTVTLogger.end();
    }

    /**
     * STB情報取得のタイムアウト時間を設定
     */
    private void startCallbackTimer() {
        DTVTLogger.start();
        showSearchingView();
        displayMoreData(true);
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
        mCallbackTimer.timerTaskCancel();
        DTVTLogger.end();
    }

    /**
     * タイムアウト時の画面表示
     */
    private void showTimeoutView() {
        DTVTLogger.start();
        displayMoreData((false));
        // STB検索タイムアウト文言表示
        TextView statusTextView = (TextView) findViewById(R.id.stb_select_status_text);
        statusTextView.setText(R.string.str_stb_select_result_text_failed);

        // STB未検出のため非表示
        mCheckBoxSTBSelectActivity.setVisibility(View.INVISIBLE);

        // リストを非表示
        mDeviceListView.setVisibility(View.GONE);
        mDlnaProvDevList.stopListen();
        DTVTLogger.end();
    }

    // タイムアウト設定クラス
    private class StbInfoCallBackTimer extends Timer {
        // STB検出タイムアウト時間
        private final long STB_SEARCH_TIMEOUT = 30000;
        private TimerTask mTimerTask = null;
        private Handler mHandler = null;
        // タイマーの状態
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
}
