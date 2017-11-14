/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.VideoGenreAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreListData;

import java.util.ArrayList;
import java.util.List;

/**
 * ビデオ＞ジャンル/サブジャンル一覧 Activityクラス
 */
public class VideoTopActivity extends BaseActivity implements View.OnClickListener,
        VideoGenreProvider.apiGenreListDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, VideoGenreProvider.GenreListMapCallback {

    private ImageView mMenuImageView;
    private List mContentsList;
    private ListView mListView;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoGenreProvider mVideoGenreProvider;
    private VideoGenreListData mVideoGenreListData;

    // 最終階層サブジャンルフラグ
    private boolean endGenreFlag = false;

    // ジャンルIDのIntent KEY
    public static final String VIDEO_CONTENTS_BUNDLE_KEY = "videoContentKey";
    public static final String GET_GENRE_MAP_BUNDLE_KEY = "getGenreMapBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_genre_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        mVideoGenreProvider = new VideoGenreProvider(this);

        String genreId = getIntent().getStringExtra(VIDEO_CONTENTS_BUNDLE_KEY);
        mVideoGenreListData = getIntent().getParcelableExtra(GET_GENRE_MAP_BUNDLE_KEY);

        if (null != genreId) {
            // サブジャンル画面のタイトル
            setTitleText(mVideoGenreListData.getTitleMap().get(genreId));
            mVideoGenreProvider.setVideoGenreListData(mVideoGenreListData);
            mVideoGenreProvider.getContentCountDataRequest(genreId);
        } else {
            // ジャンル画面のタイトル
            setTitleText(getString(R.string.video_content_top_title));
            // ジャンル一覧取得のリクエスト
            mVideoGenreProvider.getGenreListDataRequest();
        }
        initView();
    }

    private void initView() {
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }
        mListView = findViewById(R.id.genre_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mVideoGenreAdapter = new VideoGenreAdapter(
                this,
                mContentsList
        );
        mListView.setAdapter(mVideoGenreAdapter);
    }

    /**
     * 取得結果の設定・表示
     */
    private void setVideoGenre(List<VideoGenreList> listData) {
        for (int i = 0; i < listData.size(); ++i) {
            if (null != mContentsList) {
                mContentsList.add(listData.get(i));
            }
        }
    }

    public void noticeRefresh() {
        if (null != mVideoGenreAdapter) {
            mVideoGenreAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        VideoGenreList videoGenreList = (VideoGenreList) mContentsList.get(position);

        if (null == videoGenreList.getSubGenre()) {
            endGenreFlag = true;
        } else {
            endGenreFlag = false;
        }

        mVideoGenreListData.setSubGenre(videoGenreList.getSubGenre());

        bundle.putParcelable(GET_GENRE_MAP_BUNDLE_KEY, mVideoGenreListData);
        bundle.putString(VIDEO_CONTENTS_BUNDLE_KEY, videoGenreList.getGenreId());

        // サブジャンル最終階層の場合は、コンテンツ一覧画面に遷移
        if (endGenreFlag) {
            startActivity(VideoContentListActivity.class, bundle);
        } else {
            startActivity(VideoTopActivity.class, bundle);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // NOP
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // NOP
    }

    @Override
    public void genreListCallback(List<VideoGenreList> genreList) {
        mContentsList = genreList;
        mVideoGenreAdapter.mData = genreList;
        noticeRefresh();
    }

    @Override
    public void genreListMapCallback(VideoGenreListData listData) {
        mVideoGenreListData = listData;
    }
}