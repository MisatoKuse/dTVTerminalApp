/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.HomeDataProvider;

import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity implements View.OnClickListener, HomeDataProvider.ApiDataProviderCallback {

    private LinearLayout mLinearLayout;
    //外部ブラウザー遷移先
    private final static String PR_URL = "https://www.hikaritv.net/video";
    //コンテンツ一覧数
    private final static int CONTENT_LIST_COUNT = 6;
    //ヘッダのmargin
    private final static int CONTENT_LIST_START_INDEX = 2;
    //UIの上下表示順(NOW ON AIR)
    private final static int CHANNEL_SORT = 2;
    //UIの上下表示順(おすすめ番組)
    private final static int REDCH_SORT = 3;
    //UIの上下表示順(おすすめビデオ)
    private final static int REDVD_SORT = 4;
    //UIの上下表示順(今日のテレビランキング)
    private final static int TODAY_SORT = 5;
    //UIの上下表示順(ビデオランキング)
    private final static int VIDEO_SORT = 6;
    //UIの上下表示順(クリップ)
    private final static int CLIP_SORT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_layout);
        setTitleText("ホーム");
        //ビューの初期化処理
        initView();
        HomeDataProvider homeDataProvider = new HomeDataProvider(this);
        homeDataProvider.getHomeData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_main_layout_pr:
            case R.id.home_main_layout_kytv:
                if (isFastClick()) {
                    startBrowser();
                }
                break;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isFastClick()) {
                    showTips();
                }
                break;

            default:
                break;
        }
        return false;
    }

    /**
     * 機能
     * アプリ終了ダイアログ
     */
    private void showTips() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("アプリ終了")
                .setMessage("アプリ終了してよろしいでしょうか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
        alertDialog.show();
        alertDialog.setCancelable(true);
    }

    /**
     * 機能
     * 外部ブラウザーを起動する
     */
    private void startBrowser() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(PR_URL);
        intent.setData(content_url);
        startActivity(intent);
    }

    /**
     * 機能
     * ビューの初期化処理
     */
    private void initView() {
        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        TextView agreementTextView = findViewById(R.id.home_main_layout_kytv);
        LinearLayout agreementRl = findViewById(R.id.home_main_layout_kyrl);
        ImageView prImageView = findViewById(R.id.home_main_layout_pr);
        int height = getHeightDensity();
        //多機種を対応できるよう
        LinearLayout.LayoutParams imgIp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height / 3);
        LinearLayout.LayoutParams textLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height / 10);
        prImageView.setLayoutParams(imgIp);
        agreementRl.setLayoutParams(textLp);
        menuImageView.setOnClickListener(this);
        agreementTextView.setOnClickListener(this);
        prImageView.setOnClickListener(this);
        //各コンテンツのビューを作成する
        for (int i = CONTENT_LIST_START_INDEX; i < CONTENT_LIST_COUNT + CONTENT_LIST_START_INDEX; i++) {
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
    private void setRecyclerView(List<Map<String, String>> contentList, final int tag) {
        String typeContentName = getContentTypeName(tag);
        String resultCount = String.valueOf(contentList.size());
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
        //リサイクルビューの間隔
        int spacingInPixels = (int) getDensity() * 5;
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //コンテンツタイプを設定（NOW ON AIR）
        typeTextView.setText(typeContentName);
        //コンテンツカウントを設定（20）
        countTextView.setText(resultCount);
        //リサイクルビューデータ設定
        setRecyclerViewData(mRecyclerView, contentList, tag);
    }

    /**
     * 機能
     * コンテンツ一覧タイトル取得
     */
    private String getContentTypeName(int tag) {
        String typeName = "";
        switch (tag) {
            case 2:
                typeName = getResources().getString(R.string.home_label_now_on_air);
                break;
            case 3:
                typeName = getResources().getString(R.string.home_label_recommend_program);
                break;
            case 4:
                typeName = getResources().getString(R.string.home_label_recommend_video);
                break;
            case 5:
                typeName = getResources().getString(R.string.daily_tv_ranking_title);
                break;
            case 6:
                typeName = getResources().getString(R.string.video_ranking_title);
                break;
            case 7:
                typeName = getResources().getString(R.string.nav_menu_item_clip);
                break;
            default:
                break;
        }
        return typeName;
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
     * 機能
     * 遷移先を設定
     */
    private void startTo(int index) {
        switch (index) {
            case 2:
                //チャンネルリスト一覧へ遷移
                startActivity(ChannelListActivity.class, null);
                break;
            case 3:
            case 4:
                //おすすめへ遷移
                startActivity(RecommendActivity.class, null);
                break;
            case 5:
                //今日のテレビランキングへ遷移
                startActivity(DailyTvRankingActivity.class, null);
                break;
            case 6:
                //ビデオランキングへ遷移
                startActivity(VideoRankingActivity.class, null);
                break;
            case 7:
                //クリップ一覧へ遷移
                startActivity(ClipListActivity.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void channelListCallback(List<Map<String, String>> channelList) {
        if (channelList != null && channelList.size() > 0) {
            setRecyclerView(channelList, CHANNEL_SORT);
        }
    }

    @Override
    public void dailyRankListCallback(List<Map<String, String>> dailyRankList) {
        if (dailyRankList != null && dailyRankList.size() > 0) {
            setRecyclerView(dailyRankList, TODAY_SORT);
        }
    }

    @Override
    public void vodClipListCallback(List<Map<String, String>> clipList) {
        if (clipList != null && clipList.size() > 0) {
            setRecyclerView(clipList, CLIP_SORT);
        }
    }

    @Override
    public void videoRankCallback(List<Map<String, String>> videoRankList) {
        if (videoRankList != null && videoRankList.size() > 0) {
            setRecyclerView(videoRankList, VIDEO_SORT);
        }
    }

    @Override
    public void recommendChannelCallback(List<Map<String, String>> redChList) {
        if (redChList != null && redChList.size() > 0) {
            setRecyclerView(redChList, REDCH_SORT);
        }
    }

    @Override
    public void recommemdVideoCallback(List<Map<String, String>> redVdList) {
        if (redVdList != null && redVdList.size() > 0) {
            setRecyclerView(redVdList, REDVD_SORT);
        }
    }

    @Override
    public void userInfoCallback(List<Map<String, String>> userList) {

    }
}
