/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        RankingTopDataProvider.ApiDataProviderCallback, AdapterView.OnItemClickListener {

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
     * ProgressBar.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * コンテンツ詳細表示フラグ.
     */
    private boolean mContentsDetailDisplay = false;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

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
        setStatusBarColor(true);
        resetPaging();

        initView();
        mListView.setVisibility(View.GONE);
        mRelativeLayout.setVisibility(View.VISIBLE);
        mRankingTopDataProvider = new RankingTopDataProvider(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        if (mContentsDetailDisplay) {
            mContentsDetailDisplay = false;
            if (null != mContentsAdapter) {
                List<ContentsData> list = mRankingTopDataProvider.checkClipStatus(mContentsList);
                mContentsAdapter.setListData(list);
                mContentsAdapter.notifyDataSetChanged();
                DTVTLogger.debug("DailyTvRankingActivity::Clip Status Update");
            }
        }
        DTVTLogger.end();
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

        mRelativeLayout = findViewById(R.id.tv_rank_progress);
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_DAILY_RANK);
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = View.inflate(this, R.layout.search_load_more, null);
        mNoDataMessage  = findViewById(R.id.tv_rank_list_no_items);
    }

    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            if (0 != getCurrentNumber() && null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
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
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        if (mLoadMoreView.equals(view)) {
            return;
        }
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(mContentsList.get(position), ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        //コンテンツ詳細表示フラグを有効にする
        mContentsDetailDisplay = true;
        startActivity(intent);
    }

    @Override
    public void dailyRankListCallback(final List<ContentsData> contentsDataList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListView.setVisibility(View.VISIBLE);
                mRelativeLayout.setVisibility(View.GONE);
                setShowDailyRanking(contentsDataList);
            }
        });
    }

    /**
     * 取得結果の設定・表示.
     *
     * @param contentsDataList 取得したコンテンツデータリスト
     */
    private void setShowDailyRanking(final List<ContentsData> contentsDataList) {
        if (null == contentsDataList) {
            showDialogToClose();
            return;
        }
        if (0 == contentsDataList.size()) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (null != mContentsList && mContentsList.size() >= contentsDataList.size()) {
            return;
        }

        for (ContentsData info : contentsDataList) {
            if (null != mContentsList) {
                mContentsList.add(info);
            }
            mContentsAdapter.notifyDataSetChanged();
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
        return !checkRemoteControllerView() && super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
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
