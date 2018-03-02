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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.WatchListenVideoListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopWatchListenVideoListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * 視聴中ビデオ一覧.
 */
public class WatchingVideoListActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,
        WatchListenVideoListDataProvider.WatchListenVideoListProviderCallback,
        AbsListView.OnScrollListener, View.OnTouchListener {

    /**
     * 接続状態フラグ.
     */
    private boolean mIsCommunicating = false;
    /**
     * メニュー開始フラグ.
     */
    private Boolean mIsMenuLaunch = false;

    /**
     * スクロール位置の記録.
     */
    private int mFirstVisibleItem = 0;

    /**
     * 最後のスクロール方向が上ならばtrue.
     */
    private boolean mLastScrollUp = false;
    /**
     * コンテンツ詳細表示フラグ.
     */
    private boolean mContentsDetailDisplay = false;

    /**
     * ContentsDataList.
     */
    private List<ContentsData> mWatchingVideoListData = new ArrayList<>();

    /**
     * 指を置いたY座標.
     */
    private float mStartY = 0;

    /**
     * 指を置いたX座標.
     */
    private float mStartX = 0;

    /**
     * loading表示.
     */
    private View mLoadMoreView = null;
    /**
     * ListView.
     */
    private ListView mListView = null;
    /**
     * RelativeLayout.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * ContentsAdapter.
     */
    private ContentsAdapter mWatchListenVideoBaseAdapter = null;
    /**
     * DataProvider.
     */
    private WatchListenVideoListDataProvider mWatchListenVideoListDataProvider = null;

    /**
     * 横スクロール判定用倍率.
     */
    private static final float RANGE_MAGNIFICATION = 3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        setStatusBarColor(true);

        resetPaging();

        initView();
        mListView.setVisibility(View.GONE);
        mRelativeLayout.setVisibility(View.VISIBLE);

        //スクロールの上下方向検知用のリスナーを設定
        mListView.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        if (mContentsDetailDisplay) {
            mContentsDetailDisplay = false;
            if (null != mWatchListenVideoBaseAdapter) {
                List<ContentsData> list = mWatchListenVideoListDataProvider.checkClipStatus(mWatchingVideoListData);
                mWatchListenVideoBaseAdapter.setListData(list);
                mWatchListenVideoBaseAdapter.notifyDataSetChanged();
                DTVTLogger.debug("PremiumVideoActivity::Clip Status Update");
            }
        }
        DTVTLogger.end();
    }

    /**
     * View初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mRelativeLayout = findViewById(R.id.video_watching_progress);
        mListView = findViewById(R.id.video_watching_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        mWatchListenVideoBaseAdapter
                = new ContentsAdapter(this, mWatchingVideoListData, ContentsAdapter.ActivityTypeItem.TYPE_WATCHING_VIDEO_LIST);
        mListView.setAdapter(mWatchListenVideoBaseAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
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

    /**
     * 接続状態設定.
     *
     * @param b 接続状態
     */
    private void setCommunicatingStatus(final boolean b) {
        synchronized (this) {
            mIsCommunicating = b;
        }
    }

    /**
     * ページング番号返却.
     *
     * @return ページング番号
     */
    private int getCurrentNumber() {
        if (null == mWatchingVideoListData) {
            return 0;
        }
        return mWatchingVideoListData.size() / NUM_PER_PAGE;
    }

    @Override
    public void watchListenVideoListCallback(final List<ContentsData> watchListenVideoContentInfo) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListView.setVisibility(View.VISIBLE);
                mRelativeLayout.setVisibility(View.GONE);
                if (null == watchListenVideoContentInfo) {
                    //通信とJSON Parseに関してerror処理
                    DTVTLogger.debug("ClipListActivity::VodClipListCallback, get data failed.");
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
                for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE
                        && i < watchListenVideoContentInfo.size(); ++i) { //mPageNumber
                    mWatchingVideoListData.add(watchListenVideoContentInfo.get(i));
                }

                //アナライザーの指摘によるヌルチェック
                if (mWatchingVideoListData != null) {
                    DTVTLogger.debug("WatchListenVideoCallback, mWatchingVideoListData.size==" + mWatchingVideoListData.size());
                }

                resetCommunication();
                mWatchListenVideoBaseAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 接続状態解除.
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(
                mWatchingVideoListData.get(i), ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        mContentsDetailDisplay = true;
        startActivity(intent);
    }

    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (null != mWatchingVideoListData) {
                mWatchingVideoListData.clear();
                if (null != mWatchListenVideoBaseAdapter) {
                    mWatchListenVideoBaseAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * スクロール時の追加表示.
     *
     * @param b 更新フラグ
     */
    private void displayMoreData(final boolean b) {
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
    public void onScroll(final AbsListView absListView, final int firstVisibleItem, final int visibleItemCount,
                         final int totalItemCount) {
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
                DTVTLogger.debug("onScroll, paging, firstVisibleItem=" + firstVisibleItem + ","
                        + " totalItemCount=" + totalItemCount + ", visibleItemCount=" + visibleItemCount);
                //setSetPagingStatus(true);
            }
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1) {

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
                        if (null != mWatchingVideoListData) {
                            offset = mWatchingVideoListData.size() + 1;
                        }
                        mWatchListenVideoListDataProvider.getWatchListenVideoData(offset);

                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
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

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mWatchListenVideoListDataProvider != null) {
            mWatchListenVideoListDataProvider.enableConnect();
        }
        if (mWatchListenVideoBaseAdapter != null) {
            mWatchListenVideoBaseAdapter.enableConnect();
        }
        if (mListView != null) {
            mListView.invalidateViews();
        }
        if (mWatchingVideoListData == null || mWatchingVideoListData.size() == 0) {
            mWatchListenVideoListDataProvider = new WatchListenVideoListDataProvider(this);
            mWatchListenVideoListDataProvider.getWatchListenVideoData(WatchListenVideoListDataProvider.DEFAULT_PAGE_OFFSET);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopWatchListenVideoListDataConnect stopConnect = new StopWatchListenVideoListDataConnect();
        stopConnect.execute(mWatchListenVideoListDataProvider);
        StopContentsAdapterConnect stopAdapterConnect = new StopContentsAdapterConnect();
        stopAdapterConnect.execute(mWatchListenVideoBaseAdapter);
    }
}
