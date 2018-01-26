/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;

import java.util.ArrayList;
import java.util.List;

public class RecordReservationListActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener,
        RecordingReservationListDataProvider.ApiDataProviderCallback {

    private RelativeLayout mRelativeLayout = null;
    private ImageView mMenuImageView = null;
    private RecordingReservationListDataProvider mProvider = null;
    private ContentsAdapter mContentsAdapter = null;
    private ListView mListView = null;
    private List mContentsList = null;
    private View mLoadMoreView = null;

    //先頭の区切り線
    private View mHeaderDivider = null;

    private boolean mIsCommunicating = false;
    private Boolean mIsMenuLaunch = false;
    private final int NUM_PER_PAGE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        mProvider = new RecordingReservationListDataProvider(this);
        mContentsList = new ArrayList();
        initView();
        mProvider.requestRecordingReservationListData();
    }

    private void initView() {
        DTVTLogger.start();
        // ContentsListAdapter設定
        mListView = findViewById(R.id.record_reservation_list_view);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        if (mContentsList == null) {
            mContentsList = new ArrayList();
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

        DTVTLogger.end();
    }

    /**
     * リストの更新時間を取得
     */
    private void setUpdateTime() {
        if (null != mProvider.mReservationTime) {
            TextView textView = findViewById(R.id.reservation_update_time);
            StringBuilder strBuilder = new StringBuilder();
            //「現在」の部分だけ縦書きに仕様変更されたが、javaではなく元の文字列の「現」と「在」の間に改行を入れて対応した。
            strBuilder.append(mProvider.mReservationTime)
                    .append(getString(R.string.recording_reservation_list_update_time));
            textView.setText(strBuilder.toString());
        }
    }

    // スクロール処理(ページング)
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        //先頭の区切り線の表示の有無の切り替え
        if(mHeaderDivider != null && absListView != null &&
                absListView.getChildAt(0) != null) {
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
            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("onScroll, paging, firstVisibleItem=" + firstVisibleItem
                        + ", totalItemCount=" + totalItemCount + ", visibleItemCount="
                        + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");

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
     * 再読み込み時のダイアログ表示処理
     *
     * @param bool フッター付加フラグ
     */
    private void displayMoreData(boolean bool) {
        if (null != mListView && null != mLoadMoreView) {
            if (bool) {
                mListView.addFooterView(mLoadMoreView);
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * 再読み込み時の処理
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 再読み込み実施フラグ設定
     *
     * @param bool 再読み込み実施フラグ
     */
    private void setCommunicatingStatus(boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    /**
     * ページングリセット
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
     * ページング数取得
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // 録画予約一覧画面ではItemのタップで画面遷移しない
    }

    @Override
    public void recordingReservationListCallback(List<ContentsData> dataList) {
        DTVTLogger.start();
        setUpdateTime();
        if (null == dataList) {
            //通信とJSON Parseに関してのerror処理
            // TODO データ取得失敗時の仕様決定後に修正が必要
            DTVTLogger.debug("RecordingReservationListActivity::RecordingReservationListCallback, 録画予約一覧取得失敗");
            Toast.makeText(this, R.string.recording_reservation_list_error_toast, Toast.LENGTH_SHORT).show();
            resetPaging();
            resetCommunication();
            return;
        }

        if (0 == dataList.size()) {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}