/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.TvClipDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VodClipDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopTvClipDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopVodClipDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.cliplist.ClipListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.cliplist.ClipListBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.cliplist.ClipListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.List;

/**
 * クリップ画面.
 */
public class ClipListActivity extends BaseActivity implements
        VodClipDataProvider.ApiDataProviderCallback,
        TvClipDataProvider.TvClipDataProviderCallback,
        ClipListBaseFragmentScrollListener,
        TabItemLayout.OnClickTabTextListener {

    /**
     * タブ名.
     */
    private String[] mTabNames = null;
    /**
     * 接続中フラグ.
     */
    private boolean mIsCommunicating = false;
    /**
     * タブアイテムレイアウト.
     */
    private TabItemLayout mTabLayout = null;
    /**
     * ViewPager.
     */
    private ViewPager mViewPager = null;
    /**
     * メニュー表示フラグ.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * 開始ページ.
     */
    private int mStartPageNo = 0;

    /**
     * VodClipデータプロバイダ.
     */
    private VodClipDataProvider mVodClipDataProvider = null;
    /**
     * TvClipデータプロバイダ.
     */
    private TvClipDataProvider mTvClipDataProvider = null;
    /**
     * FragmentFactory.
     */
    private ClipListFragmentFactory mClipListFragmentFactory = null;

    /**
     * タブポジション(Tv).
     */
    private static final int CLIP_LIST_PAGE_NO_OF_TV = 0;
    /**
     * タブポジション(ビデオ).
     */
    public static final int CLIP_LIST_PAGE_NO_OF_VOD = 1;
    /**
     * 表示開始タブ指定キー.
     */
    public static final String CLIP_LIST_START_PAGE = "clipListStartPage";
    /**
     *  前回のポジション.
     */
    private static final String TAB_INDEX = "tabIndex";

    /** ロード終了. */
    private boolean mIsEndPageVOD = false;
    /** ロード終了. */
    private boolean mIsEndPageTV = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.clip_list_main);
        //Headerの設定
        setTitleText(getString(R.string.str_clip_activity_title));
        Intent intent = getIntent();
        mStartPageNo = intent.getIntExtra(CLIP_LIST_START_PAGE, CLIP_LIST_PAGE_NO_OF_TV);
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            mStartPageNo = CLIP_LIST_PAGE_NO_OF_TV;
        }
        if (savedInstanceState != null) {
            mStartPageNo = savedInstanceState.getInt(TAB_INDEX);
            savedInstanceState.clear();
        }
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initData();
        initView();
        resetPaging();
        //初回表示のみ前画面からのタブ指定を反映する
        mViewPager.setCurrentItem(mStartPageNo);
        mTabLayout.setTab(mStartPageNo);
        //開始位置がTVタブの時は onPageSelected() が効かないため、ここでデータ取得する
        if (mStartPageNo == CLIP_LIST_PAGE_NO_OF_TV) {
            setTv();
        }
        DTVTLogger.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        enableStbStatusIcon(true);
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        ClipListBaseFragment baseFragment = getCurrentFragment();
        if (baseFragment != null && baseFragment.getContentsDetailDisplay()) {
            baseFragment.setContentsDetailDisplay(false);
            if (null != baseFragment.getClipMainAdapter()) {
                List<ContentsData> list;
                list = mTvClipDataProvider.checkClipStatus(baseFragment.getClipListData());
                baseFragment.updateContentsList(list);
                DTVTLogger.debug("ClipListActivity::Clip Status Update");
            }
        }
        sendScreenViewForPosition(getCurrentPosition(), false);
        DTVTLogger.end();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_INDEX, mStartPageNo);
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     * @param position ポジション
     * @param isFromTab true:タブ切り替え false:onResume
     */
    private void sendScreenViewForPosition(final int position, final boolean isFromTab) {
        String screenName;
        if (position == CLIP_LIST_PAGE_NO_OF_TV) {
            screenName = getString(R.string.google_analytics_screen_name_clip_tv);
        } else {
            screenName = getString(R.string.google_analytics_screen_name_clip_video);
        }
        if (!isFromTab && mIsFromBgFlg) {
            super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(ClipListActivity.this));
        } else {
            SparseArray<String> customDimensions  = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
            super.sendScreenView(screenName, customDimensions);
        }
    }

    /**
     * リセットページ開始.
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null != baseFragment) {
                baseFragment.noticeRefresh();
                baseFragment.showProgressBar(true);
            }
        }
    }

    /**
     * 接続中フラグ設定.
     *
     * @param bool 接続中フラグ
     */
    private void setCommunicatingStatus(final boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    /**
     * ページング単位値取得.
     *
     * @return ページング単位
     */
    private int getCurrentNumber() {
        ClipListBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment) {
            return 0;
        }
        return baseFragment.getClipListDataSize() / NUM_PER_PAGE;
    }

    /**
     * 取得結果が更新前と同じなら更新しない.
     *
     * @param tvClipContentInfo 取得コンテンツデータ
     * @return コンテンツデータ更新フラグ
     */
    private boolean isSkipTv(final List<ContentsData> tvClipContentInfo) {
        ClipListBaseFragment baseFragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        if (null == baseFragment || 0 == baseFragment.getClipListDataSize() || null == baseFragment.getClipListData()) {
            return false;
        }

        if (null == tvClipContentInfo || 0 == tvClipContentInfo.size()) {
            return true;
        }

        ContentsData item1 = baseFragment.getClipListData().get(baseFragment.getClipListDataSize() - 1);
        ContentsData item2 = tvClipContentInfo.get(tvClipContentInfo.size() - 1);
        return isContentEqual(item1, item2);
    }

    /**
     * 取得結果が更新前と同じなら更新しない.
     *
     * @param vodClipContentInfo 取得コンテンツデータ
     * @return コンテンツデータ更新フラグ
     */
    private boolean isSkipVod(final List<ContentsData> vodClipContentInfo) {
        ClipListBaseFragment baseFragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        if (null == baseFragment || 0 == baseFragment.getClipListDataSize() || null == baseFragment.getClipListData()) {
            return false;
        }

        if (null == vodClipContentInfo || 0 == vodClipContentInfo.size()) {
            return true;
        }

        ContentsData item1 = baseFragment.getClipListData().get(baseFragment.getClipListDataSize() - 1);
        ContentsData item2 = vodClipContentInfo.get(vodClipContentInfo.size() - 1);
        return isContentEqual(item1, item2);
    }

    /**
     * 同じコンテンツデータか判定.
     *
     * @param item1 取得済みデータ
     * @param item2 更新データ
     * @return 判定結果
     */
    private boolean isContentEqual(final ContentsData item1, final ContentsData item2) {
        return !(null == item1 || null == item2)
                && !(null == item1.getThumURL() || null == item2.getThumURL())
                && item1.getThumURL().equals(item2.getThumURL())
                && !(null == item1.getRatStar() || null == item2.getRatStar())
                && item1.getRatStar().equals(item2.getRatStar())
                && !(null == item1.getTitle() || null == item2.getTitle())
                && item1.getTitle().equals(item2.getTitle());
    }

    /**
     * データ初期化.
     */
    private void initData() {
        mTabNames = getResources().getStringArray(R.array.tab_clip_names);
        mVodClipDataProvider = new VodClipDataProvider(this);
        mTvClipDataProvider = new TvClipDataProvider(this);
        mClipListFragmentFactory = new ClipListFragmentFactory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //全フラグメントを削除
        if (null != mViewPager) {
            int sum = mClipListFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                ClipListBaseFragment baseFragment = mClipListFragmentFactory.createFragment(i, this);
                if (null != baseFragment) {
                    baseFragment.clearClipListData();
                }
            }
        }
    }

    @Override
    public void tvClipListCallback(final List<ContentsData> clipContentInfo) {
        DTVTLogger.start();
        if (getCurrentPosition() != CLIP_LIST_PAGE_NO_OF_TV) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(
                        CLIP_LIST_PAGE_NO_OF_TV, getClipListActivity());

                if (fragment != null) {
                    fragment.showProgressBar(false);
                }
                if (null == clipContentInfo) {
                    //通信とJSON Parseに関してerror処理
                    DTVTLogger.debug("ClipListActivity::TvClipListCallback, get data failed.");
                    // ネットワークエラーの取得
                    ErrorState errorState = mTvClipDataProvider.getNetworkError();
                    String message = "";
                    if (errorState != null) {
                        message = errorState.getErrorMessage();
                    }

                    //メッセージの有無で表示方法を分ける
                    if (TextUtils.isEmpty(message) && message.length() <= 0) {
                        showGetDataFailedToast();
                    } else {
                        if (errorState != null && errorState.getErrorType() == DtvtConstants.ErrorType.NETWORK_ERROR) {
                            message = getResources().getString(R.string.network_nw_error_message_dialog);
                        }
                        showDialogToClose(ClipListActivity.this, message);
                    }

                    if (fragment.getClipListDataSize() == 0) {
                        fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                    }

                    return;
                }

                if (clipContentInfo.size() < DtvtConstants.REQUEST_LIMIT_1) {
                    mIsEndPageTV = true;
                }

	            if (0 == clipContentInfo.size()) {
                    //doing
                    if (fragment.getClipListDataSize() == 0) {
                        fragment.showNoDataMessage(true, getString(R.string.str_clip_list_no_content));
                    }
                    resetCommunication();
                    return;
                }

	            if (isSkipTv(clipContentInfo)) {
                    resetCommunication();
                    return;
                }

	            for (int i = 0; i < NUM_PER_PAGE && i < clipContentInfo.size(); i++) {
                    fragment.addClipListData(clipContentInfo.get(i));
                }

	            DTVTLogger.debug("tvClipListCallback");

                resetCommunication();
	            fragment.noticeRefresh();
            }
        });
        DTVTLogger.end();
    }

    /**
     * タブポジションを取得.
     * @return タブポジション
     */
    private int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

    @Override
    public void vodClipListCallback(final List<ContentsData> clipContentInfo) {
        DTVTLogger.start();
        if (getCurrentPosition() != CLIP_LIST_PAGE_NO_OF_VOD) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(
                        CLIP_LIST_PAGE_NO_OF_VOD, getClipListActivity());
                if (fragment != null) {
                    fragment.showProgressBar(false);
                }

                if (null == clipContentInfo) {
                    //通信とJSON Parseに関してerror処理
                    DTVTLogger.debug("ClipListActivity::VodClipListCallback, get data failed");
                    // ネットワークエラーの取得
                    ErrorState errorState = mVodClipDataProvider.getNetworkError();
                    String message = "";
                    if (errorState != null) {
                        message = errorState.getErrorMessage();
                    }

                    //メッセージの有無で表示方法を分ける
                    if (TextUtils.isEmpty(message) && message.length() <= 0) {
                        showGetDataFailedToast();
                    } else {
                        if (errorState != null && errorState.getErrorType() == DtvtConstants.ErrorType.NETWORK_ERROR) {
                            message = getResources().getString(R.string.network_nw_error_message_dialog);
                        }
                        showDialogToClose(ClipListActivity.this, message);
                    }

                    if (fragment.getClipListDataSize() == 0) {
                        fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                    }

                    return;
                }

                if (clipContentInfo.size() < DtvtConstants.REQUEST_LIMIT_1) {
                    mIsEndPageVOD = true;
                }

                if (0 == clipContentInfo.size()) {
                    //doing
                    if (fragment.getClipListDataSize() == 0) {
                        fragment.showNoDataMessage(true, getString(R.string.str_clip_list_no_content));
                    }
                    resetCommunication();
                    return;
                }

                if (isSkipVod(clipContentInfo)) {
                    resetCommunication();
                    return;
                }

                for (int i = 0; i < NUM_PER_PAGE && i < clipContentInfo.size(); i++) {
                    fragment.addClipListData(clipContentInfo.get(i));
                }

                DTVTLogger.debug("vodClipListCallback");

                resetCommunication();
                fragment.noticeRefresh();
            }
        });

        DTVTLogger.end();
    }

    /**
     * 接続中止.
     */
    private void resetCommunication() {
        ClipListBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment) {
            return;
        }
        baseFragment.displayMoreData(false);
        setCommunicatingStatus(false);
    }

    @Override
    public void onClipRegistResult() {
        DTVTLogger.start();
        //コンテンツリストに登録ステータスを反映する.
        setContentsListClipStatus(getContentsList());
        super.onClipRegistResult();
        DTVTLogger.end();
    }

    @Override
    public void onClipDeleteResult() {
        DTVTLogger.start();
        //コンテンツリストに削除ステータスを反映する.
        setContentsListClipStatus(getContentsList());
        super.onClipDeleteResult();
        DTVTLogger.end();
    }

    /**
     * コンテンツリストを返す.
     * @return List<ContentsData>
     */
    private List<ContentsData> getContentsList() {
        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(
                getCurrentPosition(), getClipListActivity());
        return fragment.getClipListData();
    }

    @Override
    public void onScroll(
            final ClipListBaseFragment fragment, final AbsListView absListView,
            final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        synchronized (this) {
            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getClipMainAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("ClipListActivity::onScroll, paging, firstVisibleItem="
                        + firstVisibleItem + ", totalItemCount=" + totalItemCount + ", visibleItemCount=" + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(final ClipListBaseFragment fragment,
                                     final AbsListView absListView, final int scrollState) {
        if ((getCurrentPosition() == CLIP_LIST_PAGE_NO_OF_VOD &&  mIsEndPageVOD)
                || getCurrentPosition() == CLIP_LIST_PAGE_NO_OF_TV && mIsEndPageTV) {
            return;
        }
        synchronized (this) {
            DTVTLogger.start();
            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getClipMainAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == fragment.getClipMainAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");

                fragment.displayMoreData(true);
                setCommunicatingStatus(true);

                //非同期中にタブ移動することがあるため、ここでスクロール開始タブ位置を保持しておく
                final int TAB_POSITION = getCurrentPosition();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        DTVTLogger.debug("onScrollStateChanged, paging thread start");
                        fragment.showNoDataMessage(false, null);
                        switch (TAB_POSITION) {
                            case CLIP_LIST_PAGE_NO_OF_TV:
                                mTvClipDataProvider.getClipData(mTvClipDataProvider.getPagerOffset());
                                break;
                            case CLIP_LIST_PAGE_NO_OF_VOD:
                                mVodClipDataProvider.getClipData(mVodClipDataProvider.getPagerOffset());
                                break;
                            default:
                                break;
                        }
                        DTVTLogger.debug("onScrollStateChanged, paging thread end");
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
        DTVTLogger.end();
    }

    /**
     * Activity取得.
     *
     * @return this
     */
    private ClipListActivity getClipListActivity() {
        return this;
    }

    /**
     * 検索結果タブ専用アダプター.
     */
    private class ClipPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * コンストラクタ.
         *
         * @param fm FragmentManager
         */
        private ClipPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            return mClipListFragmentFactory.createFragment(position, getClipListActivity());
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            return mTabNames[position];
        }
    }

    /**
     * 画面初期設定.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mViewPager = findViewById(R.id.vp_clip_result);
        ClipPagerAdapter clipPagerAdapter
                = new ClipPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(clipPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                DTVTLogger.debug("init view onPageSelected");
                mTabLayout.setTab(position);
                sendScreenViewForPosition(position, true);
                //タブ移動時にページリセット
                resetPaging();

                switch (getCurrentPosition()) {
                    case CLIP_LIST_PAGE_NO_OF_TV:
                        mStartPageNo = CLIP_LIST_PAGE_NO_OF_TV;
                        setTv();
                        break;
                    case CLIP_LIST_PAGE_NO_OF_VOD:
                        mStartPageNo = CLIP_LIST_PAGE_NO_OF_VOD;
                        setVod();
                        break;
                    default:
                        break;
                }
            }
        });
        mTabLayout = initTabData(mTabLayout, mTabNames);
    }

    /**
     * クリップ(ビデオ)画面設定.
     */
    private void setVod() {
        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        fragment.enableContentsAdapterConnect();
        //スワイプ時にページング中のプログレスバーを非表示にする
        resetCommunication();
        fragment.setMode(ContentsAdapter.ActivityTypeItem.TYPE_CLIP_LIST_MODE_VIDEO);
        fragment.showNoDataMessage(false, null);
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            if (fragment.getClipListDataSize() < 1) {
                // ビューは設定されているが、前回取得時に0件だった場合は取得しにいく
                mVodClipDataProvider.getClipData(1);
            } else {
                fragment.showProgressBar(false);
            }
        } else {
            // 遷移後の初回表示時は取得しにいく
            mVodClipDataProvider.getClipData(1);
        }
    }

    /**
     * クリップ(テレビ)画面設定.
     */
    private void setTv() {
        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        fragment.enableContentsAdapterConnect();
        //スワイプ時にページング中のプログレスバーを非表示にする
        resetCommunication();
        fragment.setMode(ContentsAdapter.ActivityTypeItem.TYPE_CLIP_LIST_MODE_TV);
        fragment.showNoDataMessage(false, null);
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            if (fragment.getClipListDataSize() < 1) {
                // ビューは設定されているが、前回取得時に0件だった場合は取得しにいく
                mTvClipDataProvider.getClipData(1);
            } else {
                fragment.showProgressBar(false);
            }
        } else {
            // 遷移後の初回表示時は取得しにいく
            mTvClipDataProvider.getClipData(1);
        }
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * Fragment生成.
     *
     * @return Fragment
     */
    private ClipListBaseFragment getCurrentFragment() {
        return mClipListFragmentFactory.createFragment(getCurrentPosition(), this);
    }

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
                    startHomeActivity();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void contentsDetailBackKey(final View view) {
        if (mIsMenuLaunch) {
            //メニューから起動の場合ホーム画面に戻る
            startHomeActivity();
        } else {
            //ランチャーから起動の場合
            finish();
        }
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mVodClipDataProvider != null) {
            ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
            fragment.enableContentsAdapterConnect();
            mVodClipDataProvider.enableConnect();
        } else {
            //現在表示中のTabのデータ取得を行う
            setVod();
        }
        if (mTvClipDataProvider != null) {
            ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
            fragment.enableContentsAdapterConnect();
            mTvClipDataProvider.enableConnect();
        } else {
            //現在表示中のTabのデータ取得を行う
            setTv();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopVodClipDataConnect stopVodClipDataConnect = new StopVodClipDataConnect();
        stopVodClipDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mVodClipDataProvider);
        StopTvClipDataConnect stopTvClipDataConnect = new StopTvClipDataConnect();
        stopTvClipDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mTvClipDataProvider);
        ClipListBaseFragment fragment = getCurrentFragment();
        if (fragment != null) {
            fragment.stopContentsAdapterConnect();
        }
    }
}
