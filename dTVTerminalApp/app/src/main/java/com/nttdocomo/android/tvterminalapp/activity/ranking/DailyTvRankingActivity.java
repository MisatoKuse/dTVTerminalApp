/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRankingTopDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.List;

/**
 * 今日のテレビランキング一覧表示画面.
 */
public class DailyTvRankingActivity extends BaseActivity implements
        RankingTopDataProvider.ApiDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        AbsListView.OnTouchListener {

    /**
     * 最大表示件数.
     */
    private final static int NUM_PER_PAGE = 20;
    /**
     * タイムアウト時間.
     */
    private final static int LOAD_PAGE_DELAY_TIME = 1000;
    /**
     * ランキングデータ取得用プロパイダ.
     */
    private RankingTopDataProvider mRankingTopDataProvider;
    /**
     * リスト表示用アダプタ.
     */
    private ContentsAdapter mContentsAdapter;
    /**
     * データの追加読み込み時に表示するプログレスダイアログのView.
     */
    private View mLoadMoreView;
    /**
     * ランキングリストを表示するリスト.
     */
    private ListView mListView;
    /**
     * コンテンツデータ一覧のリスト.
     */
    private List<ContentsData> mContentsList;
    /**
     * データの追加読み込み状態の識別フラグ.
     */
    private boolean mIsCommunicating = false;
    /**
     * スクロール位置の記録.
     */
    private int mFirstVisibleItem = 0;
    /**
     * 最後のスクロール方向が上ならばtrue.
     */
    private boolean mLastScrollUp = false;
    /**
     * 指を置いたY座標.
     */
    private float mStartY = 0;
    private RelativeLayout mRelativeLayout = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList<>();

        //Headerの設定
        setTitleText(getString(R.string.daily_tv_ranking_title));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        resetPaging();

        initView();
        mListView.setVisibility(View.GONE);
        mRelativeLayout.setVisibility(View.VISIBLE);
        mRankingTopDataProvider = new RankingTopDataProvider(this);
        mRankingTopDataProvider.getDailyRankList();
    }

    /**
     * ListViewの表示.
     */
    private void initView() {
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        if (mContentsList == null) {
            mContentsList = new ArrayList<>();
        }
        mListView = findViewById(R.id.tv_rank_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        //スクロールの上下方向検知用のリスナーを設定
        mListView.setOnTouchListener(this);

        mRelativeLayout = findViewById(R.id.tv_rank_progress);
        //アナライズの警告対応のsynchronized
        synchronized (this) {
            mContentsAdapter = new ContentsAdapter(this, mContentsList,
                    ContentsAdapter.ActivityTypeItem.TYPE_DAILY_RANK);
            mListView.setAdapter(mContentsAdapter);
        }
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    /**
     * 再読み込み時の処理.
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 読み込み表示を行う.
     *
     * @param bool 読み込み表示フラグ
     */
    private void displayMoreData(final boolean bool) {
        if (null != mListView) {
            if (bool) {
                mListView.addFooterView(mLoadMoreView);

                //スクロール位置を最下段にすることで、追加した更新フッターを画面内に入れる
                mListView.setSelection(mListView.getMaxScrollAmount());

            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (0 != getCurrentNumber() && null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    /**
     * 再読み込み実施フラグ設定.
     *
     * @param bool 読み込み表示フラグ
     */
    private void setCommunicatingStatus(final boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    /**
     * ページングを行った回数を取得.
     *
     * @return ページング回数
     */
    private int getCurrentNumber() {
        if (null == mContentsList || 0 == mContentsList.size()) {
            return 0;
        } else if (mContentsList.size() < NUM_PER_PAGE) {
            return 1;
        }
        return mContentsList.size() / NUM_PER_PAGE;
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (!(view instanceof ListView)) {
            //今回はリストビューの事しか考えないので、他のビューならば帰る
            return false;
        }

        //指を動かした方向を検知する
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //指を降ろしたので、位置を記録
                mStartY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                //指を離したので、位置を記録
                float mEndY = motionEvent.getY();

                mLastScrollUp = false;

                //スクロール方向の判定
                if (mStartY < mEndY) {
                    //終了時のY座標の方が大きいので、上スクロール
                    mLastScrollUp = true;
                }

                break;

            default:
                //現状処理は無い・警告対応
        }

        return false;
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }

            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;

            if (firstVisibleItem + visibleItemCount == totalItemCount && 0 != totalItemCount) {
                DTVTLogger.debug(
                        "Activity::onScroll, paging, firstVisibleItem=" + firstVisibleItem
                                + ", totalItemCount=" + totalItemCount
                                + ", visibleItemCount=" + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == mContentsAdapter.getCount() - 1) {
                if (mIsCommunicating) {
                    return;
                }

                //スクロール位置がリストの先頭で上スクロールだった場合は、更新をせずに帰る
                if (mFirstVisibleItem == 0 && mLastScrollUp) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");
                displayMoreData(true);
                setCommunicatingStatus(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRankingTopDataProvider.getDailyRankList();
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        if (mLoadMoreView.equals(view)) {
            return;
        }
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(mContentsList.get(position), ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        startActivity(intent);
    }

    @Override
    public void dailyRankListCallback(final List<ContentsData> contentsDataList) {
        mListView.setVisibility(View.VISIBLE);
        mRelativeLayout.setVisibility(View.GONE);
        setShowDailyRanking(contentsDataList);
    }

    /**
     * 取得結果の設定・表示.
     *
     * @param contentsDataList 取得したコンテンツデータリスト
     */
    private void setShowDailyRanking(final List<ContentsData> contentsDataList) {
        if (null == contentsDataList || 0 == contentsDataList.size()) {
            resetCommunication();
            return;
        }

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (null != mContentsList && mContentsList.size() >= contentsDataList.size()) {
            displayMoreData(false);
            return;
        }

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1)
                * NUM_PER_PAGE && i < contentsDataList.size(); ++i) {
            DTVTLogger.debug("i = " + i);
            if (null != mContentsList) {
                mContentsList.add(contentsDataList.get(i));
            }
            resetCommunication();
            synchronized (this) {
                mContentsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void weeklyRankCallback(final List<ContentsData> contentsDataList) {
        // NOP
    }

    @Override
    public void videoRankCallback(final List<ContentsData> contentsDataList) {
        // NOP
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        if (checkRemoteControllerView()) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();

        //データプロパイダあれば通信を許可し、無ければ作成
        if (mRankingTopDataProvider != null) {
            mRankingTopDataProvider.enableConnect();
        } else {
            mRankingTopDataProvider = new RankingTopDataProvider(this);
        }

        //アダプタがあれば更新を行い、無ければデータの取得を行う
        if (mContentsAdapter != null) {
            mContentsAdapter.enableConnect();
            if (mContentsAdapter.getCount() == 0) {
                //初回取得中に通信が停止された場合、アダプタは存在するがデータは0件という状態になるため、
                //その場合にはデータの再取得を行う.
                mRankingTopDataProvider.getDailyRankList();
            } else {
                mContentsAdapter.notifyDataSetChanged();
            }
        } else {
            mRankingTopDataProvider.getDailyRankList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopRankingTopDataConnect stopRankingTopDataConnect = new StopRankingTopDataConnect();
        stopRankingTopDataConnect.execute(mRankingTopDataProvider);
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        stopContentsAdapterConnect.execute(mContentsAdapter);
    }
}
