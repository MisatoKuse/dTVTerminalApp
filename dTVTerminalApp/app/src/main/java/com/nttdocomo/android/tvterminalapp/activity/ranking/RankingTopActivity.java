/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;

import java.util.List;

/**
 * ランキングトップ画面.
 */
public class RankingTopActivity extends BaseActivity implements RankingTopDataProvider.ApiDataProviderCallback {

    /**
     * ランキングトップ画面の主View.
     */
    private LinearLayout mLinearLayout;
    /**
     * グローバルメニューからの起動かを判定するフラグ.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * コンテンツ一覧数.
     */
    private final static int CONTENT_LIST_COUNT = 3;
    /**
     * UIの上下表示順(今日のテレビランキング).
     */
    private final static int TODAY_SORT = 0;
    /**
     * UIの上下表示順(週刊のテレビランキング).
     */
    private final static int WEEK_SORT = 1;
    /**
     * UIの上下表示順(ビデオランキング).
     */
    private final static int VIDEO_SORT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_top_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.nav_menu_item_ranking));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        //ビューの初期化処理
        initView();
        RankingTopDataProvider rankingTopDataProvider = new RankingTopDataProvider(this);
        rankingTopDataProvider.getRankingTopData();
    }

    /**
     * 機能.
     * ビューの初期化処理
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mLinearLayout = findViewById(R.id.ranking_top_main_layout_linearLayout);
        //各ランキングリストのUIをあらかじめ用意する
        for (int i = 0; i < CONTENT_LIST_COUNT; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.home_main_layout_item, null, false);
            view.setTag(i);
            view.setVisibility(View.GONE);
            mLinearLayout.addView(view);
        }
    }

    /**
     * 機能.
     * コンテンツ一覧ビューを設定
     */
    private void setRecyclerView(List<ContentsData> contentsDataList, final int tag) {
        String typeContentName = getContentTypeName(tag);
        View view = mLinearLayout.getChildAt(tag);
        view.setVisibility(View.VISIBLE);
        TextView typeTextView = view.findViewById(R.id.home_main_item_type_tx);
        TextView countTextView = view.findViewById(R.id.home_main_item_type_tx_count);
        //各一覧を遷移すること
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTo(tag);
            }
        });
        RecyclerView mRecyclerView = view.findViewById(R.id.home_main_item_recyclerview);
        //コンテンツタイプを設定
        typeTextView.setText(typeContentName);
        countTextView.setText(String.valueOf(contentsDataList.size()));
        //リサイクルビューデータ設定
        setRecyclerViewData(mRecyclerView, contentsDataList, tag);
    }

    /**
     * 機能
     * コンテンツ一覧データを設定
     */
    private void setRecyclerViewData(RecyclerView mRecyclerView, List<ContentsData> contentsDataList, final int index) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        HomeRecyclerViewAdapter mHorizontalViewAdapter = new HomeRecyclerViewAdapter(this, contentsDataList);
        mRecyclerView.setAdapter(mHorizontalViewAdapter);
        View footer = LayoutInflater.from(this).inflate(R.layout.home_main_layout_recyclerview_footer, mRecyclerView, false);
        TextView mTextView = footer.findViewById(R.id.home_main_layout_recyclerview_footer);
        mTextView.setText(getString(R.string.contents_more));
        //もっと見るの遷移先を設定
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTo(index);
            }
        });
        //リサイクルビューデータの設定
        mHorizontalViewAdapter.setFooterView(footer);
    }

    /**
     * 機能.
     * 遷移先を設定
     *
     * @param index リスト番号
     */
    private void startTo(int index) {
        switch (index) {
            case 0:
                //今日のテレビランキングへ遷移
                startActivity(DailyTvRankingActivity.class, null);
                break;
            case 1:
                //週刊テレビランキングへ遷移
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
     * @param tag リスト番号
     */
    private String getContentTypeName(int tag) {
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

    @Override
    public void dailyRankListCallback(List<ContentsData> contentsDataList) {
        if (contentsDataList != null && contentsDataList.size() > 0) {
            setRecyclerView(contentsDataList, TODAY_SORT);
        }
    }

    @Override
    public void weeklyRankCallback(List<ContentsData> contentsDataList) {
        if (contentsDataList != null && contentsDataList.size() > 0) {
            setRecyclerView(contentsDataList, WEEK_SORT);
        }
    }

    @Override
    public void videoRankCallback(List<ContentsData> contentsDataList) {
        if (contentsDataList != null && contentsDataList.size() > 0) {
            setRecyclerView(contentsDataList, VIDEO_SORT);
        }
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
