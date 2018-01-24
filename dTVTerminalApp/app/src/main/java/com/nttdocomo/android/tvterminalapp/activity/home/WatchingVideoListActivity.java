/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.WatchListenVideoBaseAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.WatchListenVideoListDataProvider;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class WatchingVideoListActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,
        WatchListenVideoListDataProvider.WatchListenVideoListProviderCallback,
        AbsListView.OnScrollListener, View.OnTouchListener {

    private boolean mIsCommunicating = false;
    private Boolean mIsMenuLaunch = false;


    //スクロール位置の記録
    private int mFirstVisibleItem = 0;

    //最後のスクロール方向が上ならばtrue
    private boolean mLastScrollUp = false;

    private List mData = new ArrayList<>();

    //指を置いたY座標
    private float mStartY = 0;

    //指を置いたX座標
    private float mStartX = 0;

    private View mLoadMoreView = null;
    private ListView mListView = null;

    private WatchListenVideoBaseAdapter mWatchListenVideoBaseAdapter = null;
    //private boolean mPagingStatus = false;
    private WatchListenVideoListDataProvider mWatchListenVideoListDataProvider = null;

    //横スクロール判定用倍率
    private static final float RANGE_MAGNIFICATION = 3;

    private final int NUM_PER_PAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watching_video_list_main_layout);

        setTitleText(getString(R.string.str_watching_video_activity_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        resetPaging();

        initView();
        mWatchListenVideoListDataProvider = new WatchListenVideoListDataProvider(this);
        mWatchListenVideoListDataProvider.getWatchListenVideoData(1);

        //スクロールの上下方向検知用のリスナーを設定
        mListView.setOnTouchListener(this);
    }

    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mListView = findViewById(R.id.video_watching_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        mWatchListenVideoBaseAdapter
                = new WatchListenVideoBaseAdapter(this, mData, R.layout.item_watching_video);
        mListView.setAdapter(mWatchListenVideoBaseAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
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
                mStartX = motionEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                //指を離したので、位置を記録
                float endY = motionEvent.getY();
                float endX = motionEvent.getX();

                //縦横の移動距離を求める
                float xRange = abs(endX - mStartX);
                float yRange = abs(endY - mStartY);

                if (xRange > yRange * RANGE_MAGNIFICATION) {
                    //横方向の動きが甚だしく大きい場合、横方向のスクロールとみなす。
                    //今回は縦スクロールのみなので、イベントを浪費して、操作を無視する
                    return true;
                }

                mLastScrollUp = false;

                //スクロール方向の判定
                if (mStartY < endY) {
                    //終了時のY座標の方が大きいので、上スクロール
                    mLastScrollUp = true;
                }
                break;
            default:
                //現状処理は無い・警告対応
        }
        return false;
    }

    private void setCommunicatingStatus(boolean b) {
        synchronized (this) {
            mIsCommunicating = b;
        }
    }

    private int getCurrentNumber() {
        if (null == mData) {
            return 0;
        }
        return mData.size() / NUM_PER_PAGE;
    }

    @Override
    public void watchListenVideoListCallback(List<ContentsData> watchListenVideoContentInfo) {
        if (null == watchListenVideoContentInfo) {
            //通信とJSON Parseに関してerror処理
            DTVTLogger.debug("ClipListActivity::VodClipListCallback, get data failed.");
            // TODO:エラーメッセージ表示はリスト画面上に表示する
            Toast.makeText(this, "クリップデータ取得失敗", Toast.LENGTH_SHORT).show();
            resetPaging();
            resetCommunication();
            return;
        }

        if (0 == watchListenVideoContentInfo.size()) {
            //doing
            resetCommunication();
            return;
        }

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE &&
                i < watchListenVideoContentInfo.size(); ++i) { //mPageNumber
            mData.add(watchListenVideoContentInfo.get(i));
        }

        //アナライザーの指摘によるヌルチェック
        if (mData != null) {
            DTVTLogger.debug("WatchListenVideoCallback, mData.size==" + mData.size());
        }

        resetCommunication();
        mWatchListenVideoBaseAdapter.notifyDataSetChanged();
    }

    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        startActivity(intent);
    }

    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (null != mData) {
                mData.clear();
                if (null != mWatchListenVideoBaseAdapter) {
                    mWatchListenVideoBaseAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void displayMoreData(boolean b) {
        if (null != mListView && null != mLoadMoreView) {
            if (b) {
                mListView.addFooterView(mLoadMoreView);

                //スクロール位置を最下段にすることで、追加した更新フッターを画面内に入れる
                mListView.setSelection(mListView.getMaxScrollAmount());
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        //int page = getCurrentNumber();
        synchronized (this) {
            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;

            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                //&& 0 != firstVisibleItem
                //&& page <= mMaxPage
                //&& !mPagingStatus
                    ) {
                DTVTLogger.debug("onScroll, paging, firstVisibleItem=" + firstVisibleItem + "," +
                        " totalItemCount=" + totalItemCount +
                        ", visibleItemCount=" + visibleItemCount);
                //setSetPagingStatus(true);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        synchronized (this) {
            //if (mPagingStatus && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //if (absListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1
            //        && scrollState == SCROLL_STATE_IDLE && !mIsLoadingOrComplete && !mIsAllVisible) {
            if (/*mPagingStatus && */scrollState ==
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() ==
                            mListView.getAdapter().getCount() - 1) {
                /*
                if(mIsFirstScroll){
                    setFirstScroll(false);
                    return;
                }
                */

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

                        int offset = 0;
                        if (null != mData) {
                            offset = mData.size();
                        }
                        mWatchListenVideoListDataProvider.getWatchListenVideoData(offset);

                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
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
        return super.onKeyDown(keyCode, event);
    }
}