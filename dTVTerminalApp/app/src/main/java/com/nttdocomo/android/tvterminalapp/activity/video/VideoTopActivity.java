/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.VideoGenreAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ビデオ＞ジャンル/サブジャンル一覧 Activityクラス
 */
public class VideoTopActivity extends BaseActivity implements View.OnClickListener,
        VideoGenreProvider.apiContentCountDataProviderCallback,
        VideoGenreProvider.apiGenreListDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    // メニューアイコン
    private ImageView mMenuImageView;
    private List mContentsList;
    private ListView mListView;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoGenreProvider mVideoGenreProvider;

    // TODO 仮　ジャンルID
    public static final String GENRE_ID_INTENT_KEY = "genreId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        mVideoGenreProvider = new VideoGenreProvider(this);

        String genreId = getIntent().getStringExtra(GENRE_ID_INTENT_KEY);

        if (null != genreId) {
            // サブジャンル画面のタイトル
//            setTitleText(getString(map.get(GENRE_ID_INTENT_KEY)));
        } else {
            // ジャンル画面のタイトル
            setTitleText(getString(R.string.video_content_top_title));
            // ジャンル一覧のリクエスト
            mVideoGenreProvider.getGenreListDataRequest();
        }

        // TODO コンテンツ数のリクエスト
//        mVideoGenreProvider.getContentCountDataRequest();


        initView();
    }

    private void initView() {
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }
        mListView = findViewById(R.id.tv_rank_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mVideoGenreAdapter = new VideoGenreAdapter(
                this,
                mContentsList
        );
        mListView.setAdapter(mVideoGenreAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // タップしたpositionのデータ(genreId)を取得
        ListView list = (ListView) parent;
        String genreId = (String) list.getItemAtPosition(position);
        // TODO　genreIdがとれるかの確認する
        DTVTLogger.debug("VideoTopActivity onItemClick() genreId: " + genreId);

        // IntentでタップしたジャンルのIDを送る
        Intent intent;
        // TODO　サブジャンルが存在しない場合は、コンテンツ一覧画面に遷移する分岐の実装
//        if () {
        // 下階層にまだサブジャンルが存在する場合
        intent = new Intent(this, VideoTopActivity.class);
        intent.putExtra(GENRE_ID_INTENT_KEY, genreId);
//        } else {
        //        // 最下のサブジャンル階層にいる場合
        //        intent = new Intent(this, VideoContentListActivity.class);
        //        intent.putExtra(GENRE_ID_INTENT_KEY, genreId);
//        }
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // NOP
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // NOP
    }

    private void setGenreIdList(List<Map<String, String>> genreList) {
        if (null == genreList || 0 == genreList.size()) {
            return;
        }
        List<VideoGenreList> listData = new ArrayList<>();
        VideoGenreList videoGenreList;
        for (int i = 0; i < genreList.size(); i++) {
//            videoGenreList.setGenreId(genreList.get(i).get(GENRE_ID_INTENT_KEY));
//            listData.add(videoGenreList);
        }
        for (int i = 0; i < genreList.size(); i++) {
            if (null != mContentsList) {
                mContentsList.add(listData.get(i));
            }
        }
    }

    @Override
    public void genreListCallback(List<Map<String, String>> genreHashMap) {

    }

    @Override
    public void contentCountCallback(List<Map<String, String>> countHashMap) {

    }
}