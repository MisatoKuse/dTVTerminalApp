/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRecordingReservationListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 録画予約一覧画面.
 */
public class RecordReservationListActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener,
        RecordingReservationListDataProvider.ApiDataProviderCallback {

    /**
     * 録画予約データ取得用データプロパイダ.
     */
    private RecordingReservationListDataProvider mProvider = null;
    /**
     * 録画予約リスト表示用アダプタ.
     */
    private ContentsAdapter mContentsAdapter = null;
    /**
     * 録画予約表示用リスト.
     */
    private ListView mListView = null;
    /**
     * 録画予約詳細データリスト.
     */
    private List<ContentsData> mContentsList = null;
    /**
     * 先頭の区切り線.
     */
    private View mHeaderDivider = null;
    /**
     * 通信中判定フラグ.
     */
    private boolean mIsCommunicating = false;
    /**
     * グローバルメニューからの起動かどうか.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setContentView(R.layout.record_reservation_list_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.recording_reservation_list_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(true);
        }
        enableGlobalMenuIcon(true);

        mContentsList = new ArrayList<>();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        if (mIsFromBgFlg) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_recording_reservation),
                    ContentUtils.getParingAndLoginCustomDimensions(RecordReservationListActivity.this));
        } else {
            SparseArray<String> customDimensions  = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
            super.sendScreenView(getString(R.string.google_analytics_screen_name_recording_reservation), customDimensions);
        }
    }

    /**
     * Viewの初期化.
     */
    private void initView() {
        DTVTLogger.start();
        // ContentsListAdapter設定
        mListView = findViewById(R.id.record_reservation_list_view);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        if (mContentsList == null) {
            mContentsList = new ArrayList<>();
        }
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_RECORDING_RESERVATION_LIST);
        mListView.setAdapter(mContentsAdapter);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(
                mRemoteControllerOnClickListener);

        //先頭の区切り線を取得
        mHeaderDivider = findViewById(R.id.header_divider);
        mNoDataMessage = findViewById(R.id.record_reservation_list_no_items);

        DTVTLogger.end();
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        mListView = findViewById(R.id.record_reservation_list_view);
        RelativeLayout relativeLayout = findViewById(R.id.record_reservation_progress);
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            mListView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * リストの更新時間を取得.
     */
    private void setUpdateTime() {
        String reservationTime = mProvider.getReservationTime();
        if (reservationTime != null) {
            if (!TextUtils.isEmpty(reservationTime)) {
                TextView textView = findViewById(R.id.reservation_update_time);
                String strBuilder = StringUtils.getConnectStrings(reservationTime,
                        getString(R.string.recording_reservation_list_update_time));
                textView.setText(strBuilder);
            }
        }
    }

    // スクロール処理(ページング)
    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        //先頭の区切り線の表示の有無の切り替え
        if (mHeaderDivider != null && absListView != null
                && absListView.getChildAt(0) != null) {
            //スクロール位置の判定
            if (absListView.getChildAt(0).getTop() == 0) {
                //先頭なので、線を表示する
                mHeaderDivider.setVisibility(View.VISIBLE);
            } else {
                //先頭以外なので、線は消す
                mHeaderDivider.setVisibility(View.INVISIBLE);
            }
        }

        synchronized (this) {
            if (firstVisibleItem + visibleItemCount == totalItemCount && 0 != totalItemCount) {
                DTVTLogger.debug("onScroll, paging, firstVisibleItem=" + firstVisibleItem
                        + ", totalItemCount=" + totalItemCount + ", visibleItemCount="
                        + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
       //何も処理しない
    }
    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            if (null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        // 録画予約一覧画面ではItemのタップで画面遷移しない
    }

    @Override
    public void recordingReservationListCallback(final List<ContentsData> dataList) {
        DTVTLogger.start();
        setUpdateTime();
        showProgressBar(false);
        if (null == dataList) {
            //通信とJSON Parseに関してのerror処理
            DTVTLogger.debug("RecordingReservationListActivity::RecordingReservationListCallback, 録画予約一覧取得失敗");
            resetPaging();
            //エラーメッセージを取得する
            String message = mProvider.getError().getApiErrorMessage(getApplicationContext());

            //有無で処理を分ける
            if (TextUtils.isEmpty(message)) {
                showDialogToClose(this);
            } else {
                showDialogToClose(this, message);
            }
            return;
        }

        if (0 == dataList.size()) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }
        for (int i = 0; i < dataList.size(); i++) {
                mContentsList.add(dataList.get(i));
        }
        DTVTLogger.debug("Callback, mData.size==" + mContentsList.size());
        mContentsAdapter.notifyDataSetChanged();
        DTVTLogger.end();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (closeDrawerMenu()) {
                    return false;
                }
                if (mIsMenuLaunch) {
                    //メニューから起動の場合ホーム画面に戻る
                    contentsDetailBackKey(null);
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();

        showProgressBar(true);
        //プロパイダが無ければ作成し、あれば通信を許可する
        if (mProvider != null) {
            mProvider.enableConnect();
        }
        if (mContentsAdapter != null) {
            mContentsAdapter.enableConnect();
            mContentsAdapter.notifyDataSetChanged();
        }
        if (mListView != null) {
            mListView.invalidateViews();
        }

        if (mContentsList == null || mContentsList.size() == 0) {
            //コンテンツ情報が無ければ取得を行う
            mProvider = new RecordingReservationListDataProvider(this);
            mProvider.requestRecordingReservationListData();
        } else if (mContentsList != null && mContentsList.size() > 0) {
            showProgressBar(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        showProgressBar(false);
        //通信を止める
        StopRecordingReservationListDataConnect stopRecordingReservationListDataConnect =
                new StopRecordingReservationListDataConnect();
        stopRecordingReservationListDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mProvider);
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        stopContentsAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mContentsAdapter);
    }
}
