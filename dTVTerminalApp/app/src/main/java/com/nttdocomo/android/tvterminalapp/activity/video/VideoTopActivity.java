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
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoContentProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoTopActivity extends BaseActivity implements View.OnClickListener,
        VideoContentProvider.videoGerneApiDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    // メニューアイコン
    private ImageView mMenuImageView;
    private List mContentsList;
    private ListView mListView;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoContentProvider mVideoContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.video_content_top_title));
        setShowVidoeGenreList();
        initView();
        mVideoContentProvider = new VideoContentProvider(this);
//        mVideoContentProvider.getVideoData();
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
        // TODO 仮 サブジャンル一覧画面へ遷移
        startActivity(VideoSubGenreActivity.class, null);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // NOP
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // NOP
    }

    /**
     * 取得結果の設定・表示
     */
    private void setShowVidoeGenreList() {
        List<Map<String, String>> testListData = new ArrayList<>();
        // TODO テストデータ 本来コールバックで返ってきた値を使う
        if (null == testListData || 0 == testListData.size()) {
            return;
        }

        List<VideoGenreList> genreList = new ArrayList<>();

        VideoGenreList videoGenreList;

        for (int i = 0; i < testListData.size(); i++) {
            videoGenreList = new VideoGenreList();
            videoGenreList.setTitle(testListData.get(i).get("title"));
            videoGenreList.setGenreId(testListData.get(i).get("id"));

            genreList.add(videoGenreList);
        }

        for (int i = 0; i < genreList.size(); i++) {
            if (null != mContentsList) {
                mContentsList.add(testListData.get(i));
            }
        }
    }

    @Override
    public void videoGerneApiDataProviderCallback(List<Map<String, String>> genreList) {
//        setShowVidoeGenreList(videoContentList);
    }
}
