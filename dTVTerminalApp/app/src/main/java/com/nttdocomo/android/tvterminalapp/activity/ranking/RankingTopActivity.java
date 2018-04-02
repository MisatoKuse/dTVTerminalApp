/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopHomeRecyclerViewAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRankingTopDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.List;

/**
 * ランキングトップ画面.
 */
public class RankingTopActivity extends BaseActivity
        implements RankingTopDataProvider.ApiDataProviderCallback {

    /**
     * ランキングトップ画面の主View.
     */
    private LinearLayout mLinearLayout;
    /**
     * ランキングトップ画面のProgressDialog.
     */
    private RelativeLayout mRelativeLayout;
    /**
     * ランキングトップ画面のデータ取得用データプロパイダ.
     */
    private RankingTopDataProvider mRankingTopDataProvider;
    /**
     * 今日のテレビランキングのリスト表示用アダプタ.
     */
    private HomeRecyclerViewAdapter mHorizontalViewAdapterToday = null;
    /**
     * 週間のテレビランキングのリスト表示用アダプタ.
     */
    private HomeRecyclerViewAdapter mHorizontalViewAdapterWeekly = null;
    /**
     * ビデオランキングのリスト表示用アダプタ.
     */
    private HomeRecyclerViewAdapter mHorizontalViewAdapterVod = null;
    /**
     * グローバルメニューからの起動かを判定するフラグ.
     */
    private Boolean mIsMenuLaunch = false;

    /**
     * 今日の番組ランキングの表示処理を行ったフラグ.
     */
    private boolean mIsDailyExeced = false;
    /**
     * 週間番組ランキングの表示処理を行ったフラグ.
     */
    private boolean mIsWeeklyExeced = false;
    /**
     * ビデオランキングの表示処理を行ったフラグ.
     */
    private boolean mIsVideoExeced = false;

    /**
     * 今日の番組ランキングのエラー情報.
     */
    private ErrorState mDailyErrorState = null;
    /**
     * 週間番組ランキングのエラー情報.
     */
    private ErrorState mWeeklyErrorState = null;
    /**
     * ビデオランキングのエラー情報.
     */
    private ErrorState mVideoErrorState = null;

    /**
     * コンテンツ一覧数.
     */
    private final static int CONTENT_LIST_COUNT = 3;
    /**
     * UIの上下表示順(今日のテレビランキング).
     */
    private final static int TODAY_SORT = 0;
    /**
     * UIの上下表示順(週間のテレビランキング).
     */
    private final static int WEEK_SORT = 1;
    /**
     * UIの上下表示順(ビデオランキング).
     */
    private final static int VIDEO_SORT = 2;
    /**
     * アダプタ内でのリスト識別用定数.
     */
    private final static int RANKING_CONTENTS_DISTINCTION_ADAPTER = 20;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_top_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.nav_menu_item_ranking));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(true);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        //ビューの初期化処理
        initView();

        //各ランキングの実行済みフラグとエラーステータスをクリアする
        mIsDailyExeced = false;
        mIsWeeklyExeced = false;
        mIsVideoExeced = false;
        mDailyErrorState = null;
        mWeeklyErrorState = null;
        mVideoErrorState = null;
    }

    /**
     * 機能.
     * ビューの初期化処理
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mLinearLayout = findViewById(R.id.ranking_top_main_layout_linearLayout);
        mRelativeLayout = findViewById(R.id.ranking_top_layout_progress_bar_Layout);
        //各ランキングリストのUIをあらかじめ用意する
        for (int i = 0; i < CONTENT_LIST_COUNT; i++) {
            View view = View.inflate(this, R.layout.home_main_layout_item, null);
            view.setTag(i);
            view.setVisibility(View.GONE);
            mLinearLayout.addView(view);
        }
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        enableGlobalMenuIcon(!showProgressBar);
        mLinearLayout = findViewById(R.id.ranking_top_main_layout_linearLayout);
        mRelativeLayout = findViewById(R.id.ranking_top_layout_progress_bar_Layout);
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            mLinearLayout.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 機能.
     * コンテンツ一覧ビューを設定
     *
     * @param contentsDataList コンテンツ一覧
     * @param tag              ランキング種別
     */
    private void setRecyclerView(final List<ContentsData> contentsDataList, final int tag) {
        String typeContentName = getContentTypeName(tag);
        View view = mLinearLayout.getChildAt(tag);
        view.setVisibility(View.VISIBLE);
        TextView typeTextView = view.findViewById(R.id.home_main_item_type_tx);
        ImageView rightArrowImageView = view.findViewById(R.id.home_main_item_right_arrow);
        //各一覧を遷移すること

        rightArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startTo(tag);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.home_main_item_recyclerview);
        //コンテンツタイプを設定
        typeTextView.setText(typeContentName);
        //リサイクルビューデータ設定
        setRecyclerViewData(recyclerView, contentsDataList, tag);
    }

    /**
     * 機能.
     * コンテンツ一覧データを設定
     *
     * @param recyclerView     リサイクルビュー
     * @param contentsDataList コンテンツ一覧
     * @param index            ランキング種別
     */
    private void setRecyclerViewData(final RecyclerView recyclerView, final List<ContentsData> contentsDataList, final int index) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        HomeRecyclerViewAdapter horizontalViewAdapter = new HomeRecyclerViewAdapter(this, contentsDataList, index + RANKING_CONTENTS_DISTINCTION_ADAPTER);
        recyclerView.setAdapter(horizontalViewAdapter);
        View footer = LayoutInflater.from(this).inflate(R.layout.home_main_layout_recyclerview_footer, recyclerView, false);
        RelativeLayout rankingMore = footer.findViewById(R.id.home_main_layout_recyclerview_footer);
        //もっと見るの遷移先を設定
        rankingMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startTo(index);
            }
        });
        //リサイクルビューデータの設定
        horizontalViewAdapter.setFooterView(footer);
        //各アダプタを保存
        if (index == TODAY_SORT) {
            mHorizontalViewAdapterToday = horizontalViewAdapter;
        } else if (index == WEEK_SORT) {
            mHorizontalViewAdapterWeekly = horizontalViewAdapter;
        } else if (index == VIDEO_SORT) {
            mHorizontalViewAdapterVod = horizontalViewAdapter;
        }
    }

    /**
     * 機能.
     * 遷移先を設定
     *
     * @param index リスト番号
     */
    private void startTo(final int index) {
        switch (index) {
            case 0:
                //今日のテレビランキングへ遷移
                startActivity(DailyTvRankingActivity.class, null);
                break;
            case 1:
                //週間テレビランキングへ遷移
                startActivity(WeeklyTvRankingActivity.class, null);
                break;
            case 2:
                //ビデオランキングへ遷移
                startActivity(VideoRankingActivity.class, null);
                break;
            default:
                break;
        }
    }

    /**
     * 機能.
     * コンテンツ一覧タイトル取得
     *
     * @param tag 機能.
     * @return コンテンツ一覧タイトル
     */
    private String getContentTypeName(final int tag) {
        String typeName = "";
        switch (tag) {
            case 0:
                typeName = getResources().getString(R.string.daily_tv_ranking_title);
                break;
            case 1:
                typeName = getResources().getString(R.string.weekly_tv_ranking_title);
                break;
            case 2:
                typeName = getResources().getString(R.string.video_ranking_title);
                break;
            default:
                break;
        }
        return typeName;
    }

    /**
     * エラーメッセージ制御処理.
     */
    private void controlErrorMessage() {
        //各ランキングの実行状況を取得
        if (!mIsDailyExeced || !mIsWeeklyExeced || !mIsVideoExeced) {
            //成功失敗を問わず、どれかのランキングが未実行の場合は帰る
            return;
        }

        //ランキング全部の実行が終わったので、エラーを取得する
        if (mDailyErrorState != null
                && mWeeklyErrorState != null
                && mVideoErrorState != null) {
            //全部ランキングのエラーステータスがあるならば、データは一つも取れていない。
            //代表して今日の番組のエラーを表示する。
            //TODO: 各画面が積み重なって表示するようになった際はダイアログ表示にするので、getErrorMessageをgetApiErrorMessageに変更する
            //String message = mDailyErrorState.getApiErrorMessage(getApplicationContext());
            String message = mDailyErrorState.getErrorMessage();

            //TODO: 各画面が積み重なって表示するようになった際は、showGetDataFailedToastをshowDialogToCloseに変更すると、前の画面に戻るようになる。
            if (TextUtils.isEmpty(message)) {
                //showDialogToClose();
                showGetDataFailedToast();
            } else {
                //showDialogToClose(message);
                showGetDataFailedToast(message);
            }
            return;
        }

        //個々のランキングのメッセージをトーストで表示する
        if (mDailyErrorState != null) {
            showGetDataFailedToast(mDailyErrorState.getErrorMessage());
        }
        if (mWeeklyErrorState != null) {
            showGetDataFailedToast(mWeeklyErrorState.getErrorMessage());
        }
        if (mVideoErrorState != null) {
            showGetDataFailedToast(mVideoErrorState.getErrorMessage());
        }
    }

    @Override
    public void dailyRankListCallback(final List<ContentsData> contentsDataList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //今日の番組ランキングの実行済みフラグを立てる
                mIsDailyExeced = true;

                showProgressBar(false);
                if (contentsDataList != null && contentsDataList.size() > 0) {
                    setRecyclerView(contentsDataList, TODAY_SORT);
                } else {
                    //データが来ていないので、今日の番組ランキングのエラー情報を取得する
                    mDailyErrorState = mRankingTopDataProvider.getDailyRankWebApiErrorState();
                }
                //エラー表示制御処理に飛ぶ
                controlErrorMessage();
            }
        });
    }

    @Override
    public void weeklyRankCallback(final List<ContentsData> contentsDataList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //週間番組ランキングの実行済みフラグを立てる
                mIsWeeklyExeced = true;

                showProgressBar(false);
                if (contentsDataList != null && contentsDataList.size() > 0) {
                    setRecyclerView(contentsDataList, WEEK_SORT);
                } else {
                    //データが来ていないので、週間番組ランキングのエラー情報を取得する
                    mWeeklyErrorState =
                            mRankingTopDataProvider.getWeeklyRankWebApiErrorState();
                }
                //エラー表示制御処理に飛ぶ
                controlErrorMessage();
            }
        });
    }

    @Override
    public void videoRankCallback(final List<ContentsData> contentsDataList) {
        //DBスレッドからのコールバックではUIスレッド扱いされないことがある
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //ビデオランキングの実行済みフラグを立てる
                mIsVideoExeced = true;

                showProgressBar(false);
                if (contentsDataList != null && contentsDataList.size() > 0) {
                    setRecyclerView(contentsDataList, VIDEO_SORT);
                } else {
                    //データが来ていないので、ビデオランキングのエラー情報を取得する
                    mVideoErrorState =
                            mRankingTopDataProvider.getContentsListPerGenreWebApiErrorState();
                }
                //エラー表示制御処理に飛ぶ
                controlErrorMessage();
            }
        });
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
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
        boolean getData = false;

        //データプロパイダがあれば通信を許可し、無ければ作成する.
        if (mRankingTopDataProvider != null) {
            mRankingTopDataProvider.enableConnect();
        } else {
            mRankingTopDataProvider = new RankingTopDataProvider(this);
        }

        //各ランキングのコンテンツ情報が無ければデータの取得フラグを立てる
        if (mHorizontalViewAdapterToday != null) {
            mHorizontalViewAdapterToday.enableConnect();
            if (mHorizontalViewAdapterToday.getItemCount() == 0) {
                getData = true;
            } else {
                mHorizontalViewAdapterToday.notifyDataSetChanged();
            }
        }
        if (mHorizontalViewAdapterWeekly != null) {
            mHorizontalViewAdapterWeekly.enableConnect();
            if (mHorizontalViewAdapterWeekly.getItemCount() == 0) {
                getData = true;
            } else {
                mHorizontalViewAdapterWeekly.notifyDataSetChanged();
            }
        }
        if (mHorizontalViewAdapterVod != null) {
            mHorizontalViewAdapterVod.enableConnect();
            if (mHorizontalViewAdapterVod.getItemCount() == 0) {
                getData = true;
            } else {
                mHorizontalViewAdapterVod.notifyDataSetChanged();
            }
        }

        //作成されていないアダプタがあればデータの取得フラグを立てる
        if (mHorizontalViewAdapterToday == null || mHorizontalViewAdapterWeekly == null
                || mHorizontalViewAdapterVod == null) {
            getData = true;
        }

        //足りないデータがあるのでデータの取得を行う
        if (getData) {
            showProgressBar(true);
            mRankingTopDataProvider.getRankingTopData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        showProgressBar(false);
        StopRankingTopDataConnect stopRankingTopDataConnect = new StopRankingTopDataConnect();
        stopRankingTopDataConnect.execute(mRankingTopDataProvider);
        StopHomeRecyclerViewAdapterConnect stopHomeRecyclerViewAdapterConnect = new StopHomeRecyclerViewAdapterConnect();
        stopHomeRecyclerViewAdapterConnect.execute(mHorizontalViewAdapterToday,
                mHorizontalViewAdapterWeekly, mHorizontalViewAdapterVod);
    }
}