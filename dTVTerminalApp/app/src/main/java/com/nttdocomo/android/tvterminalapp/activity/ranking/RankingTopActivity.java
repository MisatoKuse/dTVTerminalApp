/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.SpaceItemDecoration;
import com.nttdocomo.android.tvterminalapp.activity.home.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;

import java.util.List;
import java.util.Map;

public class RankingTopActivity extends BaseActivity implements View.OnClickListener, RankingTopDataProvider.ApiDataProviderCallback {

    private LinearLayout mLinearLayout;
    //コンテンツ一覧数
    private final static int CONTENT_LIST_COUNT = 3;
    //UIの上下表示順(今日のテレビランキング)
    private final static int TODAY_SORT = 0;
    //UIの上下表示順(週刊のテレビランキング)
    private final static int WEEK_SORT = 1;
    //UIの上下表示順(ビデオランキング)
    private final static int VIDEO_SORT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_top_main_layout);
        setTitleText("ランキング");
        //ビューの初期化処理
        initView();
        RankingTopDataProvider rankingTopDataProvider = new RankingTopDataProvider(this);
        rankingTopDataProvider.getRankingTopData();
    }

    /**
     * 機能
     * ビューの初期化処理
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getStbStatus()) {
                    createRemoteControllerView();
                    remoteControllerView.startRemoteUI();
                }
            }
        });
        mLinearLayout = findViewById(R.id.ranking_top_main_layout_linearLayout);
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        findViewById(R.id.header_layout_back).setVisibility(View.INVISIBLE);
        menuImageView.setOnClickListener(this);
        int height = getHeightDensity();
        //各ランキングリストのUIをあらかじめ用意する
        for (int i = 0; i < CONTENT_LIST_COUNT; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.home_main_layout_item, null, false);
            RelativeLayout relativeLayout = view.findViewById(R.id.home_main_item_type_rl);
            LinearLayout.LayoutParams relIp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    height / 15);
            relativeLayout.setLayoutParams(relIp);
            view.setTag(i);
            view.setVisibility(View.GONE);
            mLinearLayout.addView(view);
        }
    }

    /**
     * 機能
     * コンテンツ一覧ビューを設定
     */
    private void setRecyclerView(List<Map<String, String>> contentsData, final int tag) {
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
        int spacingInPixels = (int) getDensity() * 5;
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //コンテンツタイプを設定
        typeTextView.setText(typeContentName);
        countTextView.setText("＞");
        //リサイクルビューデータ設定
        setRecyclerViewData(mRecyclerView, contentsData, tag);
    }

    /**
     * 機能
     * コンテンツ一覧データを設定
     */
    private void setRecyclerViewData(RecyclerView mRecyclerView, List<Map<String, String>> mList, final int index) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        HomeRecyclerViewAdapter mHorizontalViewAdapter = new HomeRecyclerViewAdapter(this, mList);
        mRecyclerView.setAdapter(mHorizontalViewAdapter);
        View footer = LayoutInflater.from(this).inflate(R.layout.home_main_layout_recyclerview_footer, mRecyclerView, false);
        TextView mTextView = footer.findViewById(R.id.home_main_layout_recyclerview_footer);
        mTextView.setText("more");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_layout_menu:
                //ダブルクリックを防ぐ
                if (isFastClick()) {
                    onSampleGlobalMenuButton_PairLoginOk();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 機能
     * 遷移先を設定
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
     * 機能
     * コンテンツ一覧タイトル取得
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
    public void dailyRankListCallback(List<Map<String, String>> dailyMap) {
        if (dailyMap != null && dailyMap.size() > 0) {
            setRecyclerView(dailyMap, TODAY_SORT);
        }
    }

    @Override
    public void weeklyRankCallback(List<Map<String, String>> weeklyMap) {
        if (weeklyMap != null && weeklyMap.size() > 0) {
            setRecyclerView(weeklyMap, WEEK_SORT);
        }
    }

    @Override
    public void videoRankCallback(List<Map<String, String>> videoMap) {
        if (videoMap != null && videoMap.size() > 0) {
            setRecyclerView(videoMap, VIDEO_SORT);
        }
    }
}
