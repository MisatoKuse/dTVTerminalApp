/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRecommendDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendFragmentFactory;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.List;

/**
 * おすすめ番組・ビデオ.
 */
public class RecommendActivity extends BaseActivity implements
        RecommendDataProvider.RecommendApiDataProviderCallback,
        TabItemLayout.OnClickTabTextListener {

    /**
     * タブ名.
     */
    private String[] mTabNames = null;
    /**
     * 通信中フラグ.
     */
    private boolean mIsSearching = false;
    /**
     * 遷移元フラグ.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * タブ用レイアウト（共通）.
     */
    private TabItemLayout mTabLayout = null;
    /**
     * ビューページャ
     * ページャーのクラス(staticにしないと前回の値が維持され、データの更新に失敗する場合がある).
     */
    private static ViewPager sRecommendViewPager = null;
    /**
     * プロバイダー.
     */
    private RecommendDataProvider mRecommendDataProvider = null;
    /**
     * タブポジション(テレビ).
     * 表示中の最後の行を保持(staticにしないと前回の値が維持され、データの更新に失敗する場合がある)
     */
    private static int sSearchLastItem = 0;
    /**
     * ページング判定.
     */
    private boolean mIsPaging = false;
    /**
     * アクティビティ初回起動フラグ.
     */
    private boolean mIsFirst = false;
    /**
     * タブポジション(テレビ).
     */
    private static final int RECOMMEND_LIST_PAGE_NO_OF_TV = 0;
    /**
     * タブポジション(ビデオ).
     */
    public static final int RECOMMEND_LIST_PAGE_NO_OF_VOD = 1;
    /**
     * 表示開始タブ指定キー.
     */
    public static final String RECOMMEND_LIST_START_PAGE = "recommendListStartPage";
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.recommend_list_title));
        Intent intent = getIntent();
        int startPageNo = intent.getIntExtra(RECOMMEND_LIST_START_PAGE, RECOMMEND_LIST_PAGE_NO_OF_TV);
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initData();
        initRecommendListView();
        setSearchStart(false);

        //初回起動フラグをONにする
        mIsFirst = true;
        //初回表示のみ前画面からのタブ指定を反映する
        sRecommendViewPager.setCurrentItem(startPageNo);
        mTabLayout.setTab(startPageNo);
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //フォーカスを得て、初回起動だった場合の判定
        if (hasFocus && mIsFirst) {
            //画面の初期表示処理は、onCreateでは実行が早すぎて画面に表示されないので、こちらに移動
            requestRecommendData();

            //初回起動の処理が終了したので、falseとする
            mIsFirst = false;
        }
    }

    /**
     * データの初期化.
     */
    private void initData() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mTabNames = getResources().getStringArray(R.array.recommend_list_tab_names);
        mRecommendDataProvider = new RecommendDataProvider(this);

    }

    /**
     * 検索中フラグの変更.
     *
     * @param searchingFlag 検索中フラグ
     */
    private void setSearchStart(final boolean searchingFlag) {
        synchronized (this) {
            mIsSearching = searchingFlag;
        }
    }

    /**
     * データプロバイダへデータ取得要求.
     */
    private void requestRecommendData() {
        mNoDataMessage.setVisibility(View.GONE);
        if (null == mRecommendDataProvider) {
            DTVTLogger.debug("RecommendActivity::setRecommendData, mRecommendDataProvider is null");
            return;
        }
        synchronized (this) {
            if (!mIsSearching) {
                setSearchStart(true);
            } else {
                return;
            }
        }
        if (null == sRecommendViewPager) {
            return;
        }

        int requestService = sRecommendViewPager.getCurrentItem();

        //戻り値を使用せず、データは必ずコールバック経由なので、falseを指定する
        mRecommendDataProvider.startGetRecommendData(requestService);
    }

    /**
     * レコメンドのリストを初期化.
     */
    private void initRecommendListView() {

        if (null != sRecommendViewPager) {
            return;
        }
        sRecommendViewPager = findViewById(R.id.vp_recommend_list_items);
        mNoDataMessage = findViewById(R.id.recommend_list_no_items);
        initTabVIew();

        sRecommendViewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        // フリックによるtab切り替え
        sRecommendViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                clearAllFragment();
                setPagingStatus(false);
                sSearchLastItem = 0;
                //ここでフラグをクリアしないと、以後の更新が行われなくなる場合がある
                setSearchStart(false);
                requestRecommendData();
            }
        });
    }

    /**
     * tabの関連Viewを初期化.
     */
    private void initTabVIew() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.RECOMMEND_LIST_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_recommend_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != sRecommendViewPager) {
            DTVTLogger.debug("viewpager not null");
            sRecommendViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * フラグメントの取得.
     *
     * @return 現在のフラグメント
     */
    private RecommendBaseFragment getCurrentRecommendBaseFragment() {
        if (sRecommendViewPager != null) {
            int currentPageNo = sRecommendViewPager.getCurrentItem();
            return RecommendFragmentFactory.createFragment(currentPageNo);
        }
        return null;
    }

    /**
     * レコメンド取得完了時の表示処理.
     *
     * @param resultInfoList レコメンド情報
     */
    public void recommendDataProviderSuccess(final List<ContentsData> resultInfoList) {
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment == null) {
            return;
        }

        synchronized (this) {
            if (mIsPaging) {
                baseFragment.displayLoadMore(false);
                setPagingStatus(false);
            } else {
                baseFragment.clear();
            }
        }

        if (0 == resultInfoList.size()) {
            if (!showErrorMessage(sRecommendViewPager.getCurrentItem())) {
                mNoDataMessage.setVisibility(View.VISIBLE);
            }
        }

        if (0 < resultInfoList.size()) {
            for (ContentsData info : resultInfoList) {
                baseFragment.mData.add(info);
            }

            DTVTLogger.debug("baseFragment.mData.size = " + baseFragment.mData.size());

            // フラグメントの更新
            baseFragment.notifyDataSetChanged(sRecommendViewPager.getCurrentItem());
            //ゼロ以下ならばゼロにする
            if (sSearchLastItem < 0) {
                DTVTLogger.debug("sSearchLastItem = " + sSearchLastItem);
                sSearchLastItem = 0;
            }

            baseFragment.setSelection(sSearchLastItem);
            baseFragment.displayLoadMore(false);
            setSearchStart(false);
        }
    }

    /**
     * フラグメントクリア.
     */
    public void clearAllFragment() {

        if (null != sRecommendViewPager) {
            int sum = RecommendFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                RecommendBaseFragment baseFragment = RecommendFragmentFactory.createFragment(i);
                baseFragment.clear();
            }
        }
    }

    /**
     * データ取得失敗時の処理.
     */
    private void recommendDataProviderFinishNg() {
        showGetDataFailedToast();
        mNoDataMessage.setVisibility(View.VISIBLE);
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment == null) {
            return;
        }
        synchronized (this) {
            // ページング処理判定
            if (mIsPaging) {
                baseFragment.displayLoadMore(false);
                setPagingStatus(false);
            } else {
                baseFragment.clear();
                clearAllFragment();
            }
        }
        setSearchStart(false);
        DTVTLogger.debug("onSearchDataProviderFinishNg");
    }

    /**
     * ページング判定の変更.
     *
     * @param bool ページングの有無
     */
    private void setPagingStatus(final boolean bool) {
        synchronized (this) {
            mIsPaging = bool;
        }
    }

    /**
     * タブ専用アダプター.
     */
    private class TabAdapter extends FragmentStatePagerAdapter {

        /**
         * コンストラクタ.
         *
         * @param fm FragmentManager
         */
        TabAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            synchronized (this) {
                return RecommendFragmentFactory.createFragment(position);
            }
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
     * おすすめテレビ用コールバック.
     *
     * @param tabFlg タブ区別フラグ
     */
    private boolean showErrorMessage(final int tabFlg) {
        boolean isError = false;
        ErrorState errorState = mRecommendDataProvider.getError(tabFlg);
        if (errorState != null && errorState.getErrorType() != DTVTConstants.ERROR_TYPE.SUCCESS) {
            String message = errorState.getErrorMessage();
            if (!TextUtils.isEmpty(message)) {
                isError = true;
                showGetDataFailedToast(message);
            }
        }
        return isError;
    }

    /**
     * おすすめテレビ用コールバック.
     *
     * @param recommendContentInfoList テレビタブ用情報
     */
    @Override
    public void recommendChannelCallback(final List<ContentsData> recommendContentInfoList) {
        //DbThreadからのコールバックの場合UIスレッド扱いにならないときがある
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("Chan Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
                    if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV) {
                        recommendDataProviderSuccess(recommendContentInfoList);
                    }
                } else {
                    showErrorMessage(RecommendDataProvider.API_INDEX_OTHER);
                }
            }
        });
    }

    /**
     * おすすめビデオ用コールバック.
     *
     * @param recommendContentInfoList ビデオタブ用情報
     */
    @Override
    public void recommendVideoCallback(final List<ContentsData> recommendContentInfoList) {
        //DbThreadからのコールバックの場合UIスレッド扱いにならないときがある
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("vid Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
                    if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO) {
                        recommendDataProviderSuccess(recommendContentInfoList);
                    }
                } else {
                    showErrorMessage(RecommendDataProvider.API_INDEX_TV);
                }
            }
        });
    }

    /**
     * おすすめdTV用コールバック.
     *
     * @param recommendContentInfoList dTV用情報
     */
    @Override
    public void recommendDTVCallback(final List<ContentsData> recommendContentInfoList) {
        //DbThreadからのコールバックの場合UIスレッド扱いにならないときがある
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("dtv Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
                    if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV) {
                        recommendDataProviderSuccess(recommendContentInfoList);
                    }
                } else {
                    showErrorMessage(RecommendDataProvider.API_INDEX_OTHER);
                }
            }
        });
    }

    /**
     * おすすめdアニメ用コールバック.
     *
     * @param recommendContentInfoList dアニメ用情報
     */
    @Override
    public void recommendDAnimeCallback(final List<ContentsData> recommendContentInfoList) {
        //DbThreadからのコールバックの場合UIスレッド扱いにならないときがある
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("ani Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
                    if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME) {
                        recommendDataProviderSuccess(recommendContentInfoList);
                    }
                } else {
                    showErrorMessage(RecommendDataProvider.API_INDEX_OTHER);
                }
            }
        });
    }

    /**
     * おすすめdチャンネル用コールバック.
     *
     * @param recommendContentInfoList dTVチャンネル用情報
     */
    @Override
    public void recommendDChannelCallback(final List<ContentsData> recommendContentInfoList) {
        //DbThreadからのコールバックの場合UIスレッド扱いにならないときがある
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("dCH Callback DataSize:"
                            + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
                    if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL) {
                        recommendDataProviderSuccess(recommendContentInfoList);
                    }
                } else {
                    showErrorMessage(RecommendDataProvider.API_INDEX_OTHER);
                }
            }
        });
    }

    /**
     * 0件コールバック.
     */
    @Override
    public void recommendNGCallback() {
        recommendDataProviderFinishNg();
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
    protected void onStop() {
        super.onStop();

        //終了する場合はフラグをクリアする
        if (isFinishing()) {
            mIsFirst = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ヌルを入れて解放されやすくする
        sRecommendViewPager = null;

        //この組み合わせは、Androidのソースでも行われている正当な方法です。
        System.gc();
        System.runFinalization();
        System.gc();
    }


    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mRecommendDataProvider != null) {
            mRecommendDataProvider.enableConnect();
        }
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null) {
            baseFragment.enableContentsAdapterCommunication();
            baseFragment.displayLoadMore(false);
            baseFragment.invalidateViews();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();

        //おすすめ番組・ビデオの通信を止める
        StopRecommendDataConnect stopRecommendDataConnect = new StopRecommendDataConnect();
        stopRecommendDataConnect.execute(mRecommendDataProvider);

        //FragmentにContentsAdapterの通信を止めるように通知する
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null) {
            baseFragment.stopContentsAdapterCommunication();
        }
    }

}