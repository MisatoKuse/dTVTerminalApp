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

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RentalDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRentalDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.List;

/**
 * レンタル一覧を表示.
 */
public class PremiumVideoActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener, RentalDataProvider.ApiDataProviderCallback, AbsListView.OnTouchListener {

    /**
     * レンタル一覧を取得するデータプロパイダ.
     */
    private RentalDataProvider mRentalDataProvider;
    /**
     * レンタル一覧を表示するリスト.
     */
    private ListView mListView;
    /**
     * データの追加読み込み時のダイアログ.
     */
    private View mLoadMoreView;
    /**
     * コンテンツ一覧.
     */
    private List<ContentsData> mContentsList;
    /**
     * データの追加読み込み中判定フラグ.
     */
    private boolean mIsCommunicating = false;
    /**
     * グローバルメニューからの起動かどうか.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * コンテンツを表示するリストのアダプタ.
     */
    private ContentsAdapter mContentsAdapter;
    /**
     * ページ当たりの取得件数.
     */
    private static final int NUM_PER_PAGE = 20;
    /**
     * データの追加読み込みディレイ時間.
     */
    private static final int LOAD_PAGE_TIME_DELAY = 1000;
    /**
     * スクロール位置の記録.
     */
    private int mFirstVisibleItem = 0;
    /**
     * 最後のスクロール方向が上ならばtrue.
     */
    private boolean mLastScrollUp = false;
    /**
     * 指を置いたY座標.
     */
    private float mStartY = 0;
    /**
     * プログレスダイアログ.
     */
    private RelativeLayout mRelativeLayout = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium_video_main_layout);
        mContentsList = new ArrayList<>();

        //Headerの設定
        setTitleText(getString(R.string.nav_menu_item_premium_video));
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
    }

    /**
     * アダプタを設定.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mListView = findViewById(R.id.premium_video_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mRelativeLayout = findViewById(R.id.premium_video_progress);
        mRelativeLayout.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        //スクロールの上下方向検知用のリスナーを設定
        mListView.setOnTouchListener(this);
        if (mContentsList == null) {
            mContentsList = new ArrayList<>();
        }
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_PREMIUM_VIDEO_LIST);
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    /**
     * 再読み込み時のダイアログ表示処理.
     *
     * @param bool true:ダイアログ表示 false:非表示
     */
    private void displayMoreData(final boolean bool) {
        if (null != mListView && null != mLoadMoreView) {
            if (bool) {
                mListView.addFooterView(mLoadMoreView);
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * 再読み込み時の処理.
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 再読み込み実施フラグ設定.
     *
     * @param b フラグ
     */
    private void setCommunicatingStatus(final boolean b) {
        synchronized (this) {
            mIsCommunicating = b;
        }
    }

    @Override
    public void rentalListCallback(final List<ContentsData> dataList) {
        mRelativeLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        if (null == dataList) {
            resetPaging();
            resetCommunication();
            RentalDataProvider dataProvider = new RentalDataProvider(this, RentalDataProvider.RentalType.PREMIUM_VIDEO);
            dataProvider.getDbRentalList();
            return;
        }

        if (0 == dataList.size()) {
            resetCommunication();
            return;
        }

        int pageNumber = getCurrentNumber();
        //現在表示しているコンテンツ数よりもデータ取得件数が上回っている時のみ更新する
        if (mContentsList.size() < dataList.size()) {
            for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE
                    && i < dataList.size(); i++) { //mPageNumber
                mContentsList.add(dataList.get(i));
            }
        }

        DTVTLogger.debug("rentalListCallback, mData.size==" + mContentsList.size());

        resetCommunication();
        mContentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void rentalListNgCallback() {
        DTVTLogger.start();
        mRelativeLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        //データ取得失敗時
        resetCommunication();
    }

    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * ページング数取得.
     *
     * @return ページング数
     */
    private int getCurrentNumber() {
        if (null == mContentsList) {
            return 0;
        }
        return mContentsList.size() / NUM_PER_PAGE;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        if (mLoadMoreView.equals(view)) {
            return;
        }
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(mContentsList.get(i), ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        startActivity(intent);
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

                //再読み込み処理
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRentalDataProvider.getRentalData(false);
                    }
                }, LOAD_PAGE_TIME_DELAY);
            }
        }
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }

            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;

            if (firstVisibleItem + visibleItemCount == totalItemCount && 0 != totalItemCount) {
                DTVTLogger.debug("onScroll, paging, firstVisibleItem=" + firstVisibleItem
                        + ", totalItemCount=" + totalItemCount + ", visibleItemCount="
                        + visibleItemCount);
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
        if (mRentalDataProvider != null) {
            mRentalDataProvider.enableConnect();
        }
        if (mContentsAdapter != null) {
            mContentsAdapter.enableConnect();
        }
        if (mListView != null) {
            mListView.invalidateViews();
        }

        if (mContentsList == null || mContentsList.size() == 0) {
            //コンテンツ情報が無ければ取得を行う
            mRentalDataProvider = new RentalDataProvider(this, RentalDataProvider.RentalType.PREMIUM_VIDEO);
            mRentalDataProvider.getRentalData(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopRentalDataConnect stopConnect = new StopRentalDataConnect();
        stopConnect.execute(mRentalDataProvider);
        StopContentsAdapterConnect stopAdapterConnect = new StopContentsAdapterConnect();
        stopAdapterConnect.execute(mContentsAdapter);
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