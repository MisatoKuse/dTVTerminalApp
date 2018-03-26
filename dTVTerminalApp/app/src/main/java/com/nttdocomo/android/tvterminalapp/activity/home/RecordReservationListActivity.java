/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRecordingReservationListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
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
     * 追加読み込みビュー.
     */
    private View mLoadMoreView = null;
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
        setContentView(R.layout.record_reservation_list_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.recording_reservation_list_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        mContentsList = new ArrayList<>();
        initView();
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
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(
                mRemoteControllerOnClickListener);

        //先頭の区切り線を取得
        mHeaderDivider = findViewById(R.id.header_divider);
        mNoDataMessage = findViewById(R.id.record_reservation_list_no_items);

        DTVTLogger.end();
    }

    /**
     * リストの更新時間を取得.
     */
    private void setUpdateTime() {
        if (!TextUtils.isEmpty(mProvider.mReservationTime)) {
            TextView textView = findViewById(R.id.reservation_update_time);
            String strBuilder = StringUtils.getConnectStrings(mProvider.mReservationTime,
                    getString(R.string.recording_reservation_list_update_time));
            textView.setText(strBuilder);
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
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");
                mNoDataMessage.setVisibility(View.GONE);
                displayMoreData(true);
                setCommunicatingStatus(true);

                //再読み込み処理
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProvider.requestRecordingReservationListData();
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    /**
     * 再読み込み時のダイアログ表示処理.
     *
     * @param bool フッター付加フラグ
     */
    private void displayMoreData(final boolean bool) {
        if (null != mListView && null != mLoadMoreView) {
            if (bool) {
                mListView.addFooterView(mLoadMoreView);
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * 再読み込み時の処理.
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 再読み込み実施フラグ設定.
     *
     * @param bool 再読み込み実施フラグ
     */
    private void setCommunicatingStatus(final boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * ページング数取得.
     *
     * @return ページング数
     */
    private int getCurrentNumber() {
        if (null == mContentsList) {
            return 0;
        }
        return mContentsList.size() / NUM_PER_PAGE;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        // 録画予約一覧画面ではItemのタップで画面遷移しない
    }

    @Override
    public void recordingReservationListCallback(final List<ContentsData> dataList) {
        DTVTLogger.start();
        setUpdateTime();
        if (null == dataList) {
            //通信とJSON Parseに関してのerror処理
            DTVTLogger.debug("RecordingReservationListActivity::RecordingReservationListCallback, 録画予約一覧取得失敗");
            resetPaging();
            resetCommunication();
            //エラーメッセージを取得する
            String message = mProvider.getError().getApiErrorMessage(getApplicationContext());

            //有無で処理を分ける
            if (TextUtils.isEmpty(message)) {
                showDialogToClose();
            } else {
                showDialogToClose(message);
            }
            return;
        }

        if (0 == dataList.size()) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            resetCommunication();
            return;
        }

        int pageNumber = getCurrentNumber();
        // 表示中コンテンツ数よりもデータ取得件数が多い場合のみ更新する
        if (mContentsList.size() < dataList.size()) {
            for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE
                    && i < dataList.size(); i++) { //mPageNumber
                mContentsList.add(dataList.get(i));
            }
        }
        DTVTLogger.debug("Callback, mData.size==" + mContentsList.size());
        resetCommunication();
        mContentsAdapter.notifyDataSetChanged();
        DTVTLogger.end();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合はアプリ終了ダイアログを表示
                    showTips();
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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopRecordingReservationListDataConnect stopRecordingReservationListDataConnect =
                new StopRecordingReservationListDataConnect();
        stopRecordingReservationListDataConnect.execute(mProvider);
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        stopContentsAdapterConnect.execute(mContentsAdapter);
    }
}