package com.nttdocomo.android.tvterminalapp.activity.Home;

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

import java.util.List;

import com.nttdocomo.android.tvterminalapp.activity.Home.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.activity.Ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.Ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.TvProgram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.beans.HomeBean;
import com.nttdocomo.android.tvterminalapp.beans.HomeBeanContent;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.HomeDataProvider;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, HomeDataProvider.ApiDataProviderCallback {

    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    //外部ブラウザー遷移先
    private final static String PR_URL = "https://www.hikaritv.net/video";

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
                                return;
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
    }

    /**
     * 機能
     * コンテンツ一覧ビューを設定
     */
    private void setRecyclerView(HomeBean mHomeBean) {
        String typeContentName = mHomeBean.getContentTypeName();
        String resultCount = mHomeBean.getContentCount();
        final int index = mHomeBean.getContentType();
        View view = LayoutInflater.from(this).inflate(R.layout.home_main_layout_item, null, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.home_main_item_type_rl);
        int height = getHeightDensity();
        LinearLayout.LayoutParams relIp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height / 15);
        relativeLayout.setLayoutParams(relIp);
        TextView typeTextView = view.findViewById(R.id.home_main_item_type_tx);
        TextView countTextView = view.findViewById(R.id.home_main_item_type_tx_count);
        //各一覧を遷移すること
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTo(index);
            }
        });
        mRecyclerView = view.findViewById(R.id.home_main_item_recyclerview);
        //コンテンツタイプを設定（NOW ON AIR）
        typeTextView.setText(typeContentName);
        //コンテンツカウントを設定（20）
        countTextView.setText(resultCount);
        //リサイクルビューをスクロールビューに追加する
        mLinearLayout.addView(view);
        //リサイクルビューデータ設定
        setRecyclerViewData(mRecyclerView, mHomeBean.getContentList(), index);
    }

    /**
     * 機能
     * コンテンツ一覧データを設定
     */
    private void setRecyclerViewData(RecyclerView mRecyclerView, List<HomeBeanContent> mList, final int index) {
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
            case 0:
                //チャンネルリスト一覧へ遷移
                startActivity(ChannelListActivity.class, null);
                break;
            case 1:
            case 2:
                //おすすめへ遷移
                startActivity(RecommendActivity.class, null);
                break;
            case 3:
                //今日のテレビランキングへ遷移
                startActivity(DailyTvRankingActivity.class, null);
                break;
            case 4:
                //ビデオランキングへ遷移
                startActivity(VideoRankingActivity.class, null);
                break;
            case 5:
                //クリップ一覧へ遷移
                startActivity(ClipListActivity.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void ChannelListCallback(HomeBean homeBean) {
        if (homeBean != null && homeBean.getContentList() != null
                && homeBean.getContentList().size() > 0) {
            setRecyclerView(homeBean);
        }
    }

    @Override
    public void DailyRankListCallback(HomeBean homeBean) {
        if (homeBean != null && homeBean.getContentList() != null
                && homeBean.getContentList().size() > 0) {
            setRecyclerView(homeBean);
        }
    }

    @Override
    public void TvScheduleCallback(HomeBean homeBean) {
        if (homeBean != null && homeBean.getContentList() != null
                && homeBean.getContentList().size() > 0) {
            setRecyclerView(homeBean);
        }
    }

    @Override
    public void UserInfoCallback(HomeBean homeBean) {
    }

    @Override
    public void VodClipListCallback(HomeBean homeBean) {
        if (homeBean != null && homeBean.getContentList() != null
                && homeBean.getContentList().size() > 0) {
            setRecyclerView(homeBean);
        }
    }

    @Override
    public void WeeklyRankCallback(HomeBean homeBean) {
        if (homeBean != null && homeBean.getContentList() != null
                && homeBean.getContentList().size() > 0) {
            setRecyclerView(homeBean);
        }
    }

    @Override
    public void RecommendChannelCallback(HomeBean homeBean) {
        if (homeBean != null && homeBean.getContentList() != null
                && homeBean.getContentList().size() > 0) {
            setRecyclerView(homeBean);
        }
    }

    @Override
    public void RecommemdVideoCallback(HomeBean homeBean) {
        if (homeBean != null && homeBean.getContentList() != null
                && homeBean.getContentList().size() > 0) {
            setRecyclerView(homeBean);
        }
    }
}
