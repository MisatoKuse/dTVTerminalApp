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
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreListData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ビデオ＞ジャンル/サブジャンル一覧 Activityクラス
 */
public class VideoTopActivity extends BaseActivity implements View.OnClickListener,
        VideoGenreProvider.apiGenreListDataProviderCallback,
        VideoGenreProvider.apiContentCountDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private ImageView mMenuImageView;
    private List mContentsList;
    private ListView mListView;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoGenreProvider mVideoGenreProvider;

    private OtherContentsDetailData mDetailData;
    private VideoGenreListData mVideoGenreListData;

    // 最終階層サブジャンルフラグ
    private boolean endGenreFlag = false;

    // ジャンルIDのIntent KEY
    public static final String VIDEO_CONTENTS_BUNDLE_KEY = "videoContentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_genre_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);

        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        mDetailData = getIntent().getParcelableExtra(VIDEO_CONTENTS_BUNDLE_KEY);

        if (null != mDetailData) {
            // TODO サブジャンル画面のタイトル
//            setTitleText(getString(map.get(GENRE_ID_INTENT_KEY)));
        } else {
            // ジャンル画面のタイトル
            setTitleText(getString(R.string.video_content_top_title));
            // ジャンル一覧のリクエスト
            mVideoGenreProvider.getGenreListDataRequest();
        }

//        getCountData();

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

    private void getCountData(String GenreId){
        // TODO ジャンルIDの取得
        mVideoGenreProvider.getContentCountDataRequest(GenreId);
    }

    /**
     * 取得結果の設定・表示
     */
    private void setVideoGenre(Map<String, String> titleMap, Map<String, String> countMap) {
        List<VideoGenreList> videoGenreListData = setVideoGenreList(titleMap, countMap);
        for (int i = 0; i < videoGenreListData.size(); ++i) {
            if (null != mContentsList) {
                mContentsList.add(videoGenreListData.get(i));
            }
        }
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる
     *
     * @return dataList 読み込み表示フラグ
     */
    private List<VideoGenreList> setVideoGenreList(Map<String, String> titleMap,
                                                   Map<String, String> countMap) {
        List<VideoGenreList> contentsDataList = new ArrayList<>();
        VideoGenreList videoGenreList;

        if ((null == titleMap || 0 == titleMap.size())
                && (null == countMap || 0 == countMap.size())) {
            return null;
        }

        for (int i = 0; i < titleMap.size(); i++) {
            videoGenreList = new VideoGenreList();
            videoGenreList.setTitle(titleMap.get("title"));
            videoGenreList.setGenreId(titleMap.get("genre_id"));
            videoGenreList.setContentCount(countMap.get("count"));
            contentsDataList.add(videoGenreList);
        }

        return contentsDataList;
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
        VideoGenreListData videoGenreListData = (VideoGenreListData) mContentsList.get(position);
        bundle.putParcelable(VIDEO_CONTENTS_BUNDLE_KEY, videoGenreListData);

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
    public void genreListCallback(Map<String, String> titleMap, Map<String, String> subTitleMap) {

    }

    @Override
    public void contentCountCallback(Map<String, String> mapData) {
        Map<String, String> subTitleMap;
//        setVideoGenre(mapData, subTitleMap);
    }
}