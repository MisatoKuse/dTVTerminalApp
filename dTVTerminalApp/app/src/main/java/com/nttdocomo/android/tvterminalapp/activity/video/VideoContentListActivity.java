/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoContentProvider;
import com.nttdocomo.android.tvterminalapp.model.videogenrelist.VideoGenreListDataInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * ビデオコンテンツ一覧画面
 */
public class VideoContentListActivity extends BaseActivity implements View.OnClickListener,
        VideoContentProvider.apiVideoContentDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        AbsListView.OnTouchListener {

    // 最大表示件数
    private final static int NUM_PER_PAGE = 10;

    private ImageView mMenuImageView;
    private VideoContentProvider mVideoContentProvider;
    private ContentsAdapter mContentsAdapter;

    private View mLoadMoreView;
    private ListView mListView;
    private List<ContentsData> mContentsList;

    private boolean mIsCommunicating = false;

    String mGenreId;

    //スクロール位置の記録
    private int mFirstVisibleItem = 0;

    //最後のスクロール方向が上ならばtrue
    private boolean mLastScrollUp = false;

    //指を置いたY座標
    private float mStartY = 0;

    // ビデオ一覧（コンテンツツリー）からののIntent KEY
    public static final String VIDEO_CONTENTS_BUNDLE_KEY = "videoContentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.video_content_sub_genre_title));
        enableStbStatusIcon(true);

        // コンテンツツリー画面からのデータ受け取り
        VideoGenreListDataInfo info = getIntent().getParcelableExtra(VIDEO_CONTENTS_BUNDLE_KEY);
        mGenreId = info.getGenreId();

        resetPaging();

        initView();
        mVideoContentProvider = new VideoContentProvider(this);
        mVideoContentProvider.getVideoContentData(mGenreId);
    }

    /**
     * ListViewの表示
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }
        mListView = findViewById(R.id.tv_rank_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        //スクロールの上下方向検知用のリスナーを設定
        mListView.setOnTouchListener(this);

        //アナライズの警告対応のsynchronized
        synchronized (this) {
            mContentsAdapter = new ContentsAdapter(
                    this,
                    mContentsList,
                    ContentsAdapter.ActivityTypeItem.TYPE_DAILY_RANK);
            mListView.setAdapter(mContentsAdapter);
        }

        mContentsAdapter = new ContentsAdapter(
                this,
                mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_CONTENT_LIST
        );
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    /**
     * 再読み込み時の処理
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 読み込み表示を行う
     *
     * @param bool 読み込み表示フラグ
     */
    private void displayMoreData(boolean bool) {
        if (null != mListView) {
            if (bool) {
                mListView.addFooterView(mLoadMoreView);
                //スクロール位置を最下段にすることで、追加した更新フッターを画面内に入れる
                mListView.setSelection(mListView.getMaxScrollAmount());
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * ページングリセット
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (0 != getCurrentNumber() && null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    /**
     * 再読み込み実施フラグ設定
     *
     * @param bool 読み込み表示フラグ
     */
    private void setCommunicatingStatus(boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    /**
     * ページングを行った回数を取得
     *
     * @return ページング回数
     */
    private int getCurrentNumber() {
        if (null == mContentsList || 0 == mContentsList.size()) {
            return 0;
        } else if (mContentsList.size() < NUM_PER_PAGE) {
            return 1;
        }
        return mContentsList.size() / NUM_PER_PAGE;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }

            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;

            if (firstVisibleItem + visibleItemCount == totalItemCount && 0 != totalItemCount) {
                DTVTLogger.debug(
                        "Activity::onScroll, paging, firstVisibleItem=" + firstVisibleItem
                                + ", totalItemCount=" + totalItemCount
                                + ", visibleItemCount=" + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == mContentsAdapter.getCount() - 1) {
                if (mIsCommunicating) {
                    return;
                }

                //スクロール位置がリストの先頭で上スクロールだった場合は、更新をせずに帰る
                if (mFirstVisibleItem == 0 && mLastScrollUp) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");
                displayMoreData(true);
                setCommunicatingStatus(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoContentProvider.getVideoContentData(mGenreId);
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mLoadMoreView == view) {
            return;
        }
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        startActivity(intent);
    }

    /**
     * 取得結果の設定・表示
     */
    private void setShowVideoContent(List<ContentsData> videoContentInfo) {
        if (null == videoContentInfo || 0 == videoContentInfo.size()) {
            return;
        }

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (null != mContentsList && mContentsList.size() >= videoContentInfo.size()) {
            displayMoreData(false);
            return;
        }

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1)
                * NUM_PER_PAGE && i < videoContentInfo.size(); ++i) {
            DTVTLogger.debug("i = " + i);
            if (null != mContentsList) {
                mContentsList.add(videoContentInfo.get(i));
            }
        }
        resetCommunication();
        synchronized (this) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる
     *
     * @param videoContentMapList コンテンツリストデータ
     * @return dataList 読み込み表示フラグ
     */
    private List<ContentsData> setVideoContentData(
            List<Map<String, String>> videoContentMapList) {
        List<ContentsData> videoContentsDataList = new ArrayList<>();

        ContentsData contentsData;

        for (int i = 0; i < videoContentMapList.size(); i++) {
            contentsData = new ContentsData();
            contentsData.setThumURL(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_THUMB_448));
            contentsData.setTitle(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_TITLE));
            contentsData.setRatStar(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_RATING));

            videoContentsDataList.add(contentsData);
        }

        return videoContentsDataList;
    }

    @Override
    public void videoContentCallback(List<ContentsData> videoHashMap) {
        setShowVideoContent(videoHashMap);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!(view instanceof ListView)) {
            //今回はリストビューの事しか考えないので、他のビューならば帰る
            return false;
        }
        //指を動かした方向を検知する
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //指を降ろしたので、位置を記録
                mStartY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                //指を離したので、位置を記録
                float mEndY = motionEvent.getY();
                mLastScrollUp = false;
                //スクロール方向の判定
                if (mStartY < mEndY) {
                    //終了時のY座標の方が大きいので、上スクロール
                    mLastScrollUp = true;
                }
                break;
            default:
                //現状処理は無い・警告対応
        }
        return false;
    }
}
