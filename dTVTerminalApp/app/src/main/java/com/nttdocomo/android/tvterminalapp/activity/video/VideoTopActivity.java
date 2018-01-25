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

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.VideoGenreAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.struct.VideoGenreListDataInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ビデオ＞ジャンル/サブジャンル一覧 Activityクラス
 */
public class VideoTopActivity extends BaseActivity implements VideoGenreProvider.apiGenreListDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, VideoGenreProvider.GenreListMapCallback {

    private ImageView mMenuImageView;
    private List mContentsList;
    private Boolean mIsMenuLaunch = false;

    private VideoGenreAdapter mVideoGenreAdapter;
    private VideoGenreListDataInfo mVideoGenreListDataInfo = null;
    private List<VideoGenreList> mShowContentsList = null;
    private VideoGenreProvider mVideoGenreProvider = null;

    // ジャンルIDのIntent KEY
    private static final String VIDEO_CONTENTS_BUNDLE_KEY = "videoContentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_genre_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        mVideoGenreProvider = new VideoGenreProvider(this);

        Intent intent = getIntent();
        if (intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false)) {
            mVideoGenreListDataInfo = null;
        } else {
            mVideoGenreListDataInfo = intent.getParcelableExtra(VIDEO_CONTENTS_BUNDLE_KEY);
        }
        VideoGenreList showData = null;
        // 初回 + VideoTopActivityが消えていない状態で遷移した際に初期画面を表示する
        if (mVideoGenreListDataInfo == null
                || mVideoGenreListDataInfo.getVideoGenreListShowData() == null
                || mVideoGenreListDataInfo.getVideoGenreListShowData().getSubGenre() == null) {
            mVideoGenreListDataInfo = new VideoGenreListDataInfo();
            mShowContentsList = new ArrayList<VideoGenreList>();
            DTVTLogger.debug("mVideoGenreListData is Null");
            // ジャンル画面のタイトル
            setTitleText(getString(R.string.video_content_top_title));
            //ジャンル一覧表示データ取得要求開始
            mVideoGenreProvider.getGenreListDataRequest();
        } else {
            DTVTLogger.debug("Show SubGenreList");
            showData = mVideoGenreListDataInfo.getVideoGenreListShowData();
            // サブジャンル画面のタイトル
            setTitleText(showData.getTitle());
            mShowContentsList = new ArrayList<VideoGenreList>();
            if (!GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(mVideoGenreListDataInfo.getGenreId())) {
                // "すべて" を一番上に表示する
                VideoGenreList firstList = mVideoGenreListDataInfo.getVideoGenreListData(GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS);
                firstList.setGenreId(mVideoGenreListDataInfo.getGenreId());
                mShowContentsList.add(firstList);
            }
            List<String> genreIdList = new ArrayList<String>();
            // リスト表示データ取得
            for (int i = 0; i < showData.getSubGenre().size(); i++) {
                mShowContentsList.add(mVideoGenreListDataInfo.getVideoGenreListData(showData.getSubGenre().get(i)));
                genreIdList.add(showData.getSubGenre().get(i));
            }

            // 表示データのコンテンツ数を取得
            mVideoGenreProvider.getContentCountListData(genreIdList);
        }
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

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
    private void noticeRefresh(List<VideoGenreList> list) {
        DTVTLogger.start("list size : " + list.size());
        if (null != mVideoGenreAdapter) {
            DTVTLogger.debug("Notify Data Set Change");
            mContentsList = list;
            mVideoGenreAdapter.mData = mContentsList;
            mVideoGenreAdapter.notifyDataSetChanged();
        }
        DTVTLogger.end();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        VideoGenreList videoGenreList = (VideoGenreList) mContentsList.get(position);

        // 次画面遷移用データを設定
        VideoGenreListDataInfo info = new VideoGenreListDataInfo();
        // "すべて"をタップ
        if (videoGenreList.getGenreId().equals(GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS)) {
            DTVTLogger.debug("Select Genre ALL Contents");
            info.setGenreId(null);
        } else {
            info.setGenreId(videoGenreList.getGenreId());
        }
        info.setSubGenre(mVideoGenreListDataInfo.getSubGenre());

        bundle.putParcelable(VIDEO_CONTENTS_BUNDLE_KEY, info);

        // サブジャンル最終階層の場合は、コンテンツ一覧画面に遷移
        if (videoGenreList.getSubGenre() == null ||
                videoGenreList.getSubGenre().size() == 0) {
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
    public void genreListCallback(List<GenreCountGetMetaData> genreList) {
        DTVTLogger.start();
        if (genreList != null) {
            DTVTLogger.debug("Contents Count request is Success");
            int allContentsCnt = 0;
            List<Integer> noContentsList = new ArrayList<Integer>();
            for (int i = 0; i < mShowContentsList.size(); i++) {
                for (GenreCountGetMetaData cntData : genreList) {
                    // 表示中の項目とジャンルIDが一致するものにコンテンツ数を設定する
                    if (mShowContentsList.get(i).getGenreId().equals(cntData.getGenreId())) {
                        if (cntData.getCount() != 0) {
                            mShowContentsList.get(i).setContentCount(String.valueOf(cntData.getCount()));
                            allContentsCnt += cntData.getCount();
                        } else {
                            noContentsList.add(i);
                        }
                    }
                }
            }
            // 初回表示以外の場合
            if (mVideoGenreListDataInfo.getGenreId() != null) {
                if (allContentsCnt == 0) {
                    // 表示項目のすべてが0件だった場合
                    mShowContentsList = new ArrayList<VideoGenreList>();
                } else {
                    // 0件のコンテンツをリストから削除
                    int position;
                    for (int j = 0; j < noContentsList.size(); j++) {
                        position = noContentsList.get(j) - j;
                        mShowContentsList.remove(position);
                    }
                }
            } else {
                if (allContentsCnt == 0) {
                    // 表示項目のすべてが0件だった場合
                    // 0件のコンテンツをリストから削除
                    int position;
                    for (int j = 0; j < noContentsList.size(); j++) {
                        position = noContentsList.get(j) - j;
                        mShowContentsList.remove(position);
                    }
                    // "すべて" を削除
                    if (!GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(mVideoGenreListDataInfo.getGenreId())) {
                        mShowContentsList.remove(0);
                    }
                } else {
                    // 0件のコンテンツをリストから削除
                    int position;
                    for (int j = 0; j < noContentsList.size(); j++) {
                        position = noContentsList.get(j) - j;
                        mShowContentsList.remove(position);
                    }
                }
            }
        } else {
            DTVTLogger.debug("Contents Count request is faild");
        }
        // ジャンル情報取得後はリストを更新
        noticeRefresh(mShowContentsList);
        DTVTLogger.end();
    }

    @Override
    public void genreListMapCallback(Map<String, VideoGenreList> listMap, List<String> firstGenreIdList) {
        if (listMap != null && firstGenreIdList != null) {
            DTVTLogger.start("ListMap.size() :" + listMap.size());
            List<String> requestGenreIdList = new ArrayList<String>();
            // 次画面へ送信するデータを格納
            mVideoGenreListDataInfo.setSubGenre(listMap);
            for (String genreId : firstGenreIdList) {
                // ビデオ一覧画面の初回画面を表示
                DTVTLogger.debug("Add GenreId:" + genreId);
                mShowContentsList.add(listMap.get(genreId));
                // "すべて"、"NOD"を除いたジャンルIDリストを生成
                if (!GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(genreId)
                        && !GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS.equals(genreId)) {
                    requestGenreIdList.add(genreId);
                }
            }
            // 現在表示中の項目のコンテンツ数の取得要求を行う
            mVideoGenreProvider.getContentCountListData(requestGenreIdList);
            DTVTLogger.end();
        } else {
            DTVTLogger.debug("genreListMapCallback is Null");
        }
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