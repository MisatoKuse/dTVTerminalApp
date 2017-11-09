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
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ビデオ＞ジャンル/サブジャンル一覧画面 Activityクラス
 */
public class VideoTopActivity extends BaseActivity implements View.OnClickListener,
        VideoGenreProvider.apiVideoGenreDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    // メニューアイコン
    private ImageView mMenuImageView;
    private List mContentsList;
    private ListView mListView;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoGenreProvider mVideoGenreProvider;

    // TODO 仮　ジャンルID
    public static final String GENRE_ID_INTENT_KEY = "genreId";
    // TODO 仮　タイトル
    public static final String TITLE_KEY = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        String genreId = getIntent().getStringExtra(GENRE_ID_INTENT_KEY);
        // TODO intentでidがなかったらジャンル一覧、あればサブジャンル一覧を表示
        if (null != genreId) {
            // TODO 選択したサブジャンルの名前を取得してここにいれる
//             setTitleText(getString(map.get(GENRE_ID_INTENT_KEY)));

            // TODO コンテンツ数だけ取得
            mVideoGenreProvider = new VideoGenreProvider(this);
//            mVideoGenreProvider.getVideoGenreData(getString(map.get(GENRE_ID_INTENT_KEY)));
        } else {
            // ジャンル一覧のタイトルを表示
            setTitleText(getString(R.string.video_content_top_title));

            // TODO コンテンツ数とジャンル一覧を取得
            mVideoGenreProvider = new VideoGenreProvider(this);
//            mVideoGenreProvider.getVideoGenreData(getString(map.get(GENRE_ID_INTENT_KEY)));
        }
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
        // TODO　タップされたIdを得てIntentでIdを送る
        // TODO　サブジャンルが存在しない場合は、コンテンツ一覧画面に遷移する


        mListView = (ListView) parent;
        // クリックされたアイテムを取得します
        String item = (String) mListView.getItemAtPosition(position);

        // TODO
        String genreId = "仮";
        Intent intent = new Intent(this, VideoTopActivity.class);
        intent.putExtra(GENRE_ID_INTENT_KEY, genreId);

        // TODO 仮 コンテンツ一覧画面へ遷移
        startActivity(VideoContentListActivity.class, null);
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

    /**
     * 取得したリストマップをVideoGenreListクラスへ入れる
     *
     * @param dailyRankMapList コンテンツリストデータ
     * @return dataList 読み込み表示フラグ
     */
    private List<VideoGenreList> setVideoGenreListData(
            List<Map<String, String>> dailyRankMapList) {
        // TODO ListViewにIDを仕込む


        List<VideoGenreList> genreList = new ArrayList<>();
//        VideoGenreList videoGenreList;
//        for (int i = 0; i < dailyRankMapList.size(); i++) {
//            videoGenreList = new VideoGenreList();
//            // TODO 仮のキーを代入
//            videoGenreList.setGenreId(dailyRankMapList.get(i)
//                    .get(VideoRankJsonParser.VIDEORANK_LIST_TITLE_ID));
//            videoGenreList.setTitle(dailyRankMapList.get(i)
//                    .get(VideoRankJsonParser.VIDEORANK_LIST_TITLE));
//
//            genreList.add(videoGenreList);
//        }

        return genreList;
    }

    @Override
    public void videoGenreCallback(List<Map<String, String>> genreList) {
        setGenreIdList(genreList);
    }

    @Override
    public void videoContentCountCallback(List<Map<String, String>> countMap) {
    }
}