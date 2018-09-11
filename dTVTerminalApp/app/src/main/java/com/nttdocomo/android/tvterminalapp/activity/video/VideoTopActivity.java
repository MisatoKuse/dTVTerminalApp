/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.VideoGenreAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.GenreListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopGenreListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.VideoGenreListDataInfo;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ビデオ＞ジャンル/サブジャンル一覧 Activityクラス.
 */
public class VideoTopActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,

        GenreListDataProvider.ApiDataProviderCallback,
        GenreListDataProvider.GenreListMapCallback {
    // region variable
    // view
    /** ビデオジャンルのListView. **/
    private ListView mListView = null;
    /** ビデオジャンルのProgressDialog. **/
    private RelativeLayout mRelativeLayout = null;
    /** リスト0件メッセージ. */
    private TextView mNoDataMessage;

    // data
    /**ビデオジャンルデータプロバイダ.*/
    private GenreListDataProvider mVideoGenreProvider = null;
    /**ビデオジャンルアダプター.*/
    private VideoGenreAdapter mVideoGenreAdapter;
    /**ビデオジャンル一覧データ情報.*/
    private VideoGenreListDataInfo mVideoGenreListDataInfo = null;
    /**ビデオ＞ジャンル/サブジャンル一覧.*/
    private List<VideoGenreList> mContentsList;
    /**メニューから起動フラグ.*/
    private Boolean mIsMenuLaunch = false;
    /**リスト表示データ.*/
    private List<VideoGenreList> mShowContentsList = null;

    /**ジャンルIDのIntent KEY.*/
    private static final String VIDEO_GENRE_ID_BUNDLE_KEY = "videoContentKey";
    /**ジャンル・トップ・タイトル.*/
    private static final String VIDEO_GENRE_TOP_TITLE = "ビデオジャンル";
    // endregion variable
    // region Activity LifeCycle
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_genre_main_layout);
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        menuImageView.setOnClickListener(this);

        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        String title = mTitleTextView.getText().toString();

        if (title.equals(VIDEO_GENRE_TOP_TITLE)) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_video_top));
        } else {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_video_sub_genre));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        StopGenreListDataConnect stopConnect = new StopGenreListDataConnect();
        stopConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mVideoGenreProvider);
        DTVTLogger.end();
    }
    // endregion Activity LifeCycle

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (closeDrawerMenu()) {
                    return false;
                }
                if (mIsMenuLaunch) {
                    //メニューから起動の場合ホーム画面に戻る
                    contentsDetailBackKey(null);
                    return false;
                }
            default:
                break;
        }
        return !checkRemoteControllerView() && super.onKeyDown(keyCode, event);
    }

    @Override
    protected void contentsDetailBackKey(final View view) {
        if (mIsMenuLaunch) {
            //メニューから起動の場合ホーム画面に戻る
            super.contentsDetailBackKey(null);
        } else {
            finish();
        }
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mVideoGenreProvider != null) {
            mVideoGenreProvider.enableConnect();
        }
        if (mListView != null) {
            mListView.invalidateViews();
        }
        if (mContentsList == null || mContentsList.size() == 0) {
            //コンテンツ情報が無ければ取得を行う
            mVideoGenreProvider = new GenreListDataProvider(this);
            Intent intent = getIntent();
            if (intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false)) {
                mVideoGenreListDataInfo = null;
            } else {
                mVideoGenreListDataInfo = intent.getParcelableExtra(VIDEO_GENRE_ID_BUNDLE_KEY);
            }
            VideoGenreList showData = null;
            // 初回 + VideoTopActivityが消えていない状態で遷移した際に初期画面を表示する
            if (mVideoGenreListDataInfo == null
                    || mVideoGenreListDataInfo.getVideoGenreListShowData() == null
                    || mVideoGenreListDataInfo.getVideoGenreListShowData().getSubGenre() == null) {
                mVideoGenreListDataInfo = new VideoGenreListDataInfo();
                mShowContentsList = new ArrayList<>();
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
                mShowContentsList = new ArrayList<>();
                if (!GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(mVideoGenreListDataInfo.getGenreId())) {
                    // "すべて" を一番上に表示する
                    VideoGenreList firstList = mVideoGenreListDataInfo.getVideoGenreListData(GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS);
                    firstList.setGenreId(mVideoGenreListDataInfo.getGenreId());
                    mShowContentsList.add(firstList);
                }
                List<String> genreIdList = new ArrayList<>();
                // リスト表示データ取得
                for (String subGenre: showData.getSubGenre()) {
                    mShowContentsList.add(mVideoGenreListDataInfo.getVideoGenreListData(subGenre));
                    genreIdList.add(subGenre);
                }
                // 表示データのコンテンツ数を取得
                mVideoGenreProvider.getContentCountListData(genreIdList);
            }
            DTVTLogger.debug("mShowContentsList.size() = " + mShowContentsList.size());

            mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
            if (mIsMenuLaunch) {
                enableHeaderBackIcon(true);
            }
        }
    }

    // region Implement
    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        Bundle bundle = new Bundle();
        VideoGenreList videoGenreList = mContentsList.get(position);

        // 次画面遷移用データを設定
        VideoGenreListDataInfo info = new VideoGenreListDataInfo();
        // "すべて"をタップ
        if (GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS.equals(videoGenreList.getGenreId())) {
            DTVTLogger.debug("Select Genre ALL Contents");
            info.setGenreId(null);
        } else {
            info.setGenreId(videoGenreList.getGenreId());
        }
        //ジャンルのすべてをタップ
        if (getString(R.string.video_list_genre_all).equals(videoGenreList.getTitle())
                && mVideoGenreListDataInfo.getGenreId() != null) {
            info.setSubGenre(null);
            info.setGenreId(videoGenreList.getGenreId());
        } else {
            info.setSubGenre(mVideoGenreListDataInfo.getSubGenre());
        }

        bundle.putParcelable(VIDEO_GENRE_ID_BUNDLE_KEY, info);

        // サブジャンル最終階層の場合は、コンテンツ一覧画面に遷移
        if (videoGenreList.getSubGenre() == null
                || videoGenreList.getSubGenre().size() == 0) {
            // サブジャンルがなければコンテンツ一覧を表示する
            startActivity(VideoContentListActivity.class, bundle);
        } else {
            // サブジャンルがあればサブジャンル一覧を表示する
            startActivity(VideoTopActivity.class, bundle);
        }
    }

    @Override
    public void genreListCallback(@Nullable final List<GenreCountGetMetaData> genreList) {
        showProgressBar(false);

        if (genreList == null) {
            DTVTLogger.debug("Contents Count request is faild");
            showError();
        } else {
            DTVTLogger.start("genreList.size = " + genreList.size());

            final int allContentsCnt = generateShowContentsList(genreList);
            if (mVideoGenreListDataInfo.getGenreId() != null) { // 初回表示以外の場合
                showGenerListView(allContentsCnt);
            } else { // 初回表示の場合
                showFirstGenreListView(allContentsCnt);
            }
        }

        //判断条件にgenreListがヌルの場合を追加・通信エラーでgenreListがヌルになると、「すべて」が2行になってしまうのを避ける為
        if (mVideoGenreListDataInfo != null
                && mVideoGenreListDataInfo.getGenreId() != null
                && !GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(mVideoGenreListDataInfo.getGenreId())
                && !GenreListMetaData.VIDEO_LIST_GENRE_ID_DTV.equals(mVideoGenreListDataInfo.getGenreId())
                && genreList != null) { // ジャンル情報取得後はリストを更新
            VideoGenreList videoGenreList = new VideoGenreList();
            videoGenreList.setTitle(this.getResources().getString(R.string.video_list_genre_all));
            videoGenreList.setContentCount(
                    String.valueOf(mVideoGenreListDataInfo.getVideoGenreListData(mVideoGenreListDataInfo.getGenreId()).getContentCount()));
            videoGenreList.setGenreId(mVideoGenreListDataInfo.getGenreId());
            mShowContentsList.add(0, videoGenreList);
        }

        if (0 == mShowContentsList.size()) { //コンテンツ0件表示
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }

        noticeRefresh(mShowContentsList);
        DTVTLogger.end();
    }

    @Override
    public void genreListMapCallback(@Nullable final Map<String, VideoGenreList> listMap, @Nullable final List<String> firstGenreIdList) {
        if (listMap == null || firstGenreIdList == null) {
            showError();
            DTVTLogger.debug("listMap or firstGenreIdList is Null");

            //エラーなのでプログレスバーを消す
            showProgressBar(false);
            //データ無しを表示
            mNoDataMessage.setVisibility(View.VISIBLE);
            mNoDataMessage.setText(getString(R.string.common_get_data_failed_message));
            return;
        }

        DTVTLogger.start("listMap.size() = " + listMap.size() + ", firstGenreIdList.size() = " + firstGenreIdList.size());
        List<String> requestGenreIdList = new ArrayList<>();
        // 次画面へ送信するデータを格納
        mVideoGenreListDataInfo.setSubGenre(listMap);
        ///
        for (String genreId : firstGenreIdList) {
            // ビデオ一覧画面の初回画面を表示
            DTVTLogger.debug("Add GenreId:" + genreId);
            mShowContentsList.add(listMap.get(genreId));
            // "すべて(VOD)"、"NOD","dTV"を除いたジャンルIDリストを生成
            if (!GenreListMetaData.VIDEO_LIST_GENRE_ID_VOD.equals(genreId)
                    && !GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(genreId)
                    && !GenreListMetaData.VIDEO_LIST_GENRE_ID_DTV.equals(genreId)
                    && !GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS.equals(genreId)) {
                requestGenreIdList.add(genreId);
            }
        } ///
        // 現在表示中の項目のコンテンツ数の取得要求を行う
        mVideoGenreProvider.getContentCountListData(requestGenreIdList);
        DTVTLogger.end();
    }
    // endregion Implement

    // region private mehtod
    /**
     * レイアウト初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        if (mContentsList == null) {
            mContentsList = new ArrayList<>();
        }
        mListView = findViewById(R.id.genre_list);
        mRelativeLayout = findViewById(R.id.genre_progress);
        mNoDataMessage = findViewById(R.id.genre_list_no_items);
        showProgressBar(true);
        mListView.setOnItemClickListener(this);
        mVideoGenreAdapter = new VideoGenreAdapter(this, mContentsList);
        mListView.setAdapter(mVideoGenreAdapter);
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        if (showProgressBar) { //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            mListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * List更新処理.
     * @param list 更新リスト
     */
    private void noticeRefresh(final List<VideoGenreList> list) {
        if (mVideoGenreAdapter == null) {
            return;
        }
        DTVTLogger.start("list size : " + list.size());
        mContentsList = list;
        mVideoGenreAdapter.setData(mContentsList);
        mVideoGenreAdapter.notifyDataSetChanged();

        DTVTLogger.end();
    }

    /**
     * エラーダイアログ表示.
     */
    private void showError() {
        ErrorState errorState = mVideoGenreProvider.getGenreListError();

        //デフォルトメッセージを指定
        String errorMessage = getString(R.string.common_get_data_failed_message);

        if (errorState != null && !TextUtils.isEmpty(errorState.getErrorMessage())) {
            //メッセージが存在するので取得する
            errorMessage = errorState.getApiErrorMessage(getApplicationContext());
        }

        //ジャンルや件数が取得できない場合はエラーダイアログで前画面に戻す.
        //ジャンルのみ取得できていても、コンテンツ数が0ならジャンルは見せないようにする必要あり.
        //それを代用してジャンル自体のR制限はチェックしていない.
        if(mVideoGenreListDataInfo.getGenreId() != null) {
            //初回以外は、OKを押すと前画面遷移するダイアログで表示
            showFinishDialog(errorMessage);
        } else {
            //初回表示は、ホーム遷移ダイアログで表示
            showDialogToClose(this, errorMessage);
        }
    }
    // endregion private mehtod

    /**
     * 表示中のコンテンツ数を設定.
     * @param genreList genreList
     * @return 表示中のコンテンツ数
     */
    private int generateShowContentsList(@NonNull final List<GenreCountGetMetaData> genreList) {
        int retVal = 0;
        for (VideoGenreList videoGenreList: mShowContentsList) {
            for (GenreCountGetMetaData metaData: genreList) {
                if (videoGenreList.getGenreId().equals(metaData.getGenreId())
                        && metaData.getCount() > 0) { // 表示中の項目とジャンルIDが一致するものにコンテンツ数を設定する
                    final int contentCount =  metaData.getCount();
                    videoGenreList.setContentCount(String.valueOf(contentCount));
                    retVal += contentCount;
                }
            }
        }
        DTVTLogger.debug("retVal = " + retVal);
        return retVal;
    }

    /**
     * 親ジャンル表示.
     *
     * @param allContentsCount 全コンテンツ数
     */
    private void showFirstGenreListView(final int allContentsCount) {
        DTVTLogger.debug("allContentsCount = " + allContentsCount);
        Iterator<VideoGenreList> iterator = mShowContentsList.iterator();
        if (allContentsCount == 0) { // 全部0件の場合
            //表示項目のすべてが0件だった場合「すべて(VOD)」「NOD」「dTV」以外はすべて削除する + VOD?
            while (iterator.hasNext()) {
                VideoGenreList list = iterator.next();
                if (GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS.equals(list.getGenreId())
                        || GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(list.getGenreId())
                        || GenreListMetaData.VIDEO_LIST_GENRE_ID_DTV.equals(list.getGenreId())) {
                    continue;
                }
                iterator.remove();
            }
        } else {
            //0件のコンテンツをリストから削除
            while (iterator.hasNext()) {
                VideoGenreList list = iterator.next();
                if (GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS.equals(list.getGenreId())
                        || GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD.equals(list.getGenreId())
                        || GenreListMetaData.VIDEO_LIST_GENRE_ID_DTV.equals(list.getGenreId())) {
                    continue;
                }
                if (list.getContentCount() == null
                        || list.getContentCount().isEmpty()) {
                    DTVTLogger.debug("list.getGenreId()=" + list.getGenreId());
                    iterator.remove();
                }
            }
        }
        DTVTLogger.debug("mShowContentsList.size()=" + mShowContentsList.size());
    }

    /**
     * 親ジャンル以外の表示.
     *
     * @param allContentsCount 全コンテンツ数
     */
    private void showGenerListView(final int allContentsCount) {
        // 0件のコンテンツをリストから削除
        DTVTLogger.debug("allContentsCount = " + allContentsCount);
        Iterator<VideoGenreList> iterator = mShowContentsList.iterator();
        if (allContentsCount == 0) { // "すべて"以外を削除する
            while (iterator.hasNext()) {
                VideoGenreList list = iterator.next();
                if (!GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS.equals(list.getGenreId())) {
                    iterator.remove();
                }
            }
        } else {
            while (iterator.hasNext()) {
                VideoGenreList list = iterator.next();
                if (!GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS.equals(list.getGenreId())) {
                    if (list.getContentCount() == null || list.getContentCount().isEmpty()) {
                        iterator.remove();
                    }
                } else {
                    list.setContentCount(String.valueOf(allContentsCount));
                }
            }
        } // if (allContentsCount == 0)
    }
}
