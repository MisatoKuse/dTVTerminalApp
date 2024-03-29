/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRankingTopDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

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
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList<>();

        //Headerの設定
        setTitleText(getString(R.string.daily_tv_ranking_title));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        resetPaging();

        initView();
        showProgressBar(true);
        mRankingTopDataProvider = new RankingTopDataProvider(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        enableStbStatusIcon(true);
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
        if (mIsFromBgFlg) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_daily_ranking),
                    ContentUtils.getParingAndLoginCustomDimensions(DailyTvRankingActivity.this));
        } else {
            SparseArray<String> customDimensions = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
            super.sendScreenView(getString(R.string.google_analytics_screen_name_daily_ranking), customDimensions);
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
        mNoDataMessage  = findViewById(R.id.tv_rank_list_no_items);
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        mListView = findViewById(R.id.tv_rank_list);
        mRelativeLayout = findViewById(R.id.tv_rank_progress);
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            mListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            if (0 != getCurrentNumber(mContentsList) && null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }

    }
    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        //コンテンツ詳細表示フラグを有効にする
        mContentsDetailDisplay = true;

        ContentsData contentsData = mContentsList.get(position);
        if (ContentUtils.isChildContentList(contentsData)) {
            startChildContentListActivity(contentsData);
        } else {
            Intent intent = new Intent(this, ContentDetailActivity.class);
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, getComponentName().getClassName());
            intent.putExtra(ContentUtils.PLALA_INFO_BUNDLE_KEY, contentsData.getContentsId());
            startActivity(intent);
        }
    }

    @Override
    public void dailyRankListCallback(final List<ContentsData> contentsDataList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        final Context context = this;
        // クリップ状態の判定Status変更済みコンテンツデータリスト
        final List<ContentsData> clipStatusContentsDataList = mRankingTopDataProvider.checkClipStatus(contentsDataList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                //エラー情報の存在を見る
                ErrorState errorState = mRankingTopDataProvider.getDailyRankWebApiErrorState();
                if (errorState != null) {
                    //エラー情報が存在すれば、DBにデータが無く、通信も失敗しているので、エラーメッセージを出して帰る
                    String message = errorState.getApiErrorMessage(
                            getApplication().getApplicationContext());
                    mNoDataMessage.setVisibility(View.VISIBLE);
                    mNoDataMessage.setText(getString(R.string.common_get_data_failed_message));

                    //メッセージの有無を確認
                    if (TextUtils.isEmpty(message)) {
                        //メッセージが無いので、デフォルトメッセージで表示
                        showDialogToClose(context);
                    } else {
                        //メッセージがあるので表示
                        showDialogToClose(context, message);
                    }
                    return;
                }

                setShowDailyRanking(clipStatusContentsDataList);
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
            showDialogToClose(this);
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
    public void onClipRegistResult() {
        DTVTLogger.start();
        //コンテンツリストに登録ステータスを反映する.
        setContentsListClipStatus(mContentsList);
        super.onClipRegistResult();
        DTVTLogger.end();
    }

    @Override
    public void onClipDeleteResult() {
        DTVTLogger.start();
        //コンテンツリストに削除ステータスを反映する.
        setContentsListClipStatus(mContentsList);
        super.onClipDeleteResult();
        DTVTLogger.end();
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (closeDrawerMenu()) {
                return false;
            }
        }
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
        stopRankingTopDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mRankingTopDataProvider);
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        stopContentsAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mContentsAdapter);
    }
}
