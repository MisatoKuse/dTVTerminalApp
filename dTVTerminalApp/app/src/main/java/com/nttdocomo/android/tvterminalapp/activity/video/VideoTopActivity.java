/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.VideoGenreAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreListData;

import java.util.ArrayList;
import java.util.List;

/**
 * ビデオ＞ジャンル/サブジャンル一覧 Activityクラス
 */
public class VideoTopActivity extends BaseActivity implements AbsListView.OnScrollListener,
        VideoGenreProvider.apiGenreListDataProviderCallback,
        AdapterView.OnItemClickListener, VideoGenreProvider.GenreListMapCallback {

    private ImageView mMenuImageView;
    private List mContentsList;
    private Boolean mIsMenuLaunch = false;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoGenreListData mVideoGenreListData;

    // ジャンルIDのIntent KEY
    private static final String VIDEO_CONTENTS_BUNDLE_KEY = "videoContentKey";
    private static final String GET_GENRE_MAP_BUNDLE_KEY = "getGenreMapBundleKey";

    // サブジャンル有無判定用
    private static final int SUB_GENRE_NOTHING_BORDER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_genre_main_layout);
        mContentsList = new ArrayList();

        VideoGenreProvider videoGenreProvider = new VideoGenreProvider(this);

        Intent intent = getIntent();
        String genreId = intent.getStringExtra(VIDEO_CONTENTS_BUNDLE_KEY);
        mVideoGenreListData = intent.getParcelableExtra(GET_GENRE_MAP_BUNDLE_KEY);

        //Headerの設定
        if (null != genreId) {
            // サブジャンル画面のタイトル
            setTitleText(mVideoGenreListData.getTitleMap().get(genreId));
        } else {
            // ジャンル画面のタイトル
            setTitleText(getString(R.string.video_content_top_title));
        }
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        //ジャンル一覧表示データ取得要求開始
        videoGenreProvider.getGenreListDataRequest();
        initView();
    }

    /**
     * レイアウト初期化
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }
        ListView listView = findViewById(R.id.genre_list);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        mVideoGenreAdapter = new VideoGenreAdapter(
                this,
                mContentsList
        );
        listView.setAdapter(mVideoGenreAdapter);
    }

    /**
     * List更新処理
     */
    private void noticeRefresh() {
        if (null != mVideoGenreAdapter) {
            mVideoGenreAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        VideoGenreList videoGenreList = (VideoGenreList) mContentsList.get(position);

        //"すべて"は画面遷移なし
        if(videoGenreList.getTitle().equals(getString(R.string.video_content_all_title))){
            return;
        }

        // サブジャンル有無フラグ設定
        boolean endGenreFlag = false;
        ArrayList<GenreListMetaData.SubContent> subGenre = videoGenreList.getSubGenre();
        if(subGenre == null || SUB_GENRE_NOTHING_BORDER > subGenre.size()){
            endGenreFlag = true;
        }

        mVideoGenreListData.setSubGenre(videoGenreList.getSubGenre());

        bundle.putParcelable(GET_GENRE_MAP_BUNDLE_KEY, mVideoGenreListData);
        bundle.putString(VIDEO_CONTENTS_BUNDLE_KEY, videoGenreList.getGenreId());

        // サブジャンル最終階層の場合は、コンテンツ一覧画面に遷移
        if (endGenreFlag) {
            // サブジャンルがなければコンテンツ一覧を表示する
            startActivity(VideoContentListActivity.class, bundle);
        } else {
            // サブジャンルがあればサブジャンル一覧を表示する
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
        // ジャンル情報取得後はリストを更新
        mContentsList = genreList;
        mVideoGenreAdapter.mData = genreList;
        noticeRefresh();
    }

    @Override
    public void genreListMapCallback(VideoGenreListData listData) {
        mVideoGenreListData = listData;
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
        if (checkRemoteControllerView()) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}