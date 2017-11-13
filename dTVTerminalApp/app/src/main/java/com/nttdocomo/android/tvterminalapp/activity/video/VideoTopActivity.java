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
import java.util.Map;

/**
 * ビデオ＞ジャンル/サブジャンル一覧 Activityクラス
 */
public class VideoTopActivity extends BaseActivity implements View.OnClickListener,
        VideoGenreProvider.apiContentCountDataProviderCallback,
        VideoGenreProvider.apiGenreListDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private ImageView mMenuImageView;
    private List mContentsList;
    private ListView mListView;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoGenreProvider mVideoGenreProvider;

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

        mVideoGenreProvider = new VideoGenreProvider(this);

        String genreId = getIntent().getStringExtra(VIDEO_CONTENTS_BUNDLE_KEY);

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

        // TODO 後で消す
        test();

        initView();
    }

    // TODO 後で消す
    public void test() {
        List<VideoGenreList> contentsDataList = new ArrayList<>();
        VideoGenreList videoGenreList;

        videoGenreList = new VideoGenreList();
        videoGenreList.setTitle("あにめ");
        videoGenreList.setContentCount("146");
        contentsDataList.add(videoGenreList);

        videoGenreList = new VideoGenreList();
        videoGenreList.setTitle("どらま");
        videoGenreList.setContentCount("21421");
        contentsDataList.add(videoGenreList);

        videoGenreList = new VideoGenreList();
        videoGenreList.setTitle("洋画");
        videoGenreList.setContentCount("243");
        contentsDataList.add(videoGenreList);

        for (int i = 0; i < contentsDataList.size(); ++i) {
            mContentsList.add(contentsDataList.get(i));
        }

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
    private void setVideoGenre(List<Map<String, String>> videoGenreList) {
        if (null == videoGenreList || 0 == videoGenreList.size()) {
            return;
        }
        List<VideoGenreList> videoGenreListData = setVideoGenreList(videoGenreList);

        for (int i = 0; i < videoGenreListData.size(); ++i) {
            if (null != mContentsList) {
                mContentsList.add(videoGenreListData.get(i));
            }
        }
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる
     *
     * @param contentList コンテンツリストデータ
     * @return dataList 読み込み表示フラグ
     */
    private List<VideoGenreList> setVideoGenreList(
            List<Map<String, String>> contentList) {
        List<VideoGenreList> contentsDataList = new ArrayList<>();

        VideoGenreList videoGenreList;

        for (int i = 0; i < contentList.size(); i++) {
            videoGenreList = new VideoGenreList();
            // TODO ひとまず仮でKEYをセット
            videoGenreList.setTitle(contentList.get(i)
                    .get("title"));
            videoGenreList.setContentCount(contentList.get(i)
                    .get("count"));
            videoGenreList.setGenreId(contentList.get(i)
                    .get("genre_id"));

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
        startActivity(VideoTopActivity.class, bundle);
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
    public void genreListCallback(List<Map<String, String>> genreHashMap) {

    }

    @Override
    public void contentCountCallback(List<Map<String, String>> countHashMap) {

    }
}