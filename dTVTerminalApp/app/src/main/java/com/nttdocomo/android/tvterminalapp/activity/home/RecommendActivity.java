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
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRecommendDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendFragmentFactory;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * おすすめ番組・ビデオ.
 */
public class RecommendActivity extends BaseActivity implements
        RecommendDataProvider.RecommendApiDataProviderCallback,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
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
     * ビューページャ.
     */
    private ViewPager mRecommendViewPager = null;
    /**
     * プロバイダー.
     */
    private RecommendDataProvider mRecommendDataProvider = null;
    /**
     * タブポジション(テレビ).
     */
    private int mSearchLastItem = 0;
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
     * タブポジション(dTVチャンネル).
     */
    private static final int RECOMMEND_LIST_PAGE_NO_OF_DTV_CHANNEL = 3;
    /**
     * 表示開始タブ指定キー.
     */
    public static final String RECOMMEND_LIST_START_PAGE = "recommendListStartPage";
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;
    /**
     * フラグメント作成クラス.
     */
    private RecommendFragmentFactory mRecommendFragmentFactory = null;

    /**
     * チャンネル情報控え.
     */
    private ArrayList<ChannelInfo> mChannels;
    /**
     * チャンネル情報取得プロバイダー.
     */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    /**
     * チャンネル情報取得用ハンドラー.
     */
    final private Handler mHandle = new Handler();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.recommend_list_title));
        Intent intent = getIntent();
        int startPageNo = intent.getIntExtra(RECOMMEND_LIST_START_PAGE, RECOMMEND_LIST_PAGE_NO_OF_TV);
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            startPageNo = RECOMMEND_LIST_PAGE_NO_OF_TV;
        }
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initData();
        initRecommendListView();
        setSearchStart(false);

        //初回起動フラグをONにする
        mIsFirst = true;
        //初回表示のみ前画面からのタブ指定を反映する
        mRecommendViewPager.setCurrentItem(startPageNo);
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
        mRecommendFragmentFactory = new RecommendFragmentFactory();

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
        if (null == mRecommendViewPager) {
            return;
        }

        int requestService = mRecommendViewPager.getCurrentItem();

        //戻り値を使用せず、データは必ずコールバック経由なので、falseを指定する
        mRecommendDataProvider.startGetRecommendData(requestService);
    }

    /**
     * レコメンドのリストを初期化.
     */
    private void initRecommendListView() {

        mNoDataMessage = findViewById(R.id.recommend_list_no_items);
        mTabLayout = initTabData(mTabLayout, mTabNames);
        mRecommendViewPager = findViewById(R.id.vp_recommend_list_items);

        mRecommendViewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        // フリックによるtab切り替え
        mRecommendViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                clearAllFragment();
                setPagingStatus(false);
                mSearchLastItem = 0;
                //ここでフラグをクリアしないと、以後の更新が行われなくなる場合がある
                setSearchStart(false);
                requestRecommendData();
            }
        });
    }
    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != mRecommendViewPager) {
            DTVTLogger.debug("viewpager not null");
            mRecommendViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * フラグメントの取得.
     *
     * @return 現在のフラグメント
     */
    private RecommendBaseFragment getCurrentRecommendBaseFragment() {
        if (mRecommendViewPager != null) {
            int currentPageNo = mRecommendViewPager.getCurrentItem();
            return mRecommendFragmentFactory.createFragment(currentPageNo);
        }
        return null;
    }

    /**
     * レコメンド取得完了時の表示処理.
     *
     * @param resultInfoList レコメンド情報
     */
    private void recommendDataProviderSuccess(final List<ContentsData> resultInfoList) {
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
            if (!showErrorMessage(mRecommendViewPager.getCurrentItem())) {
                mNoDataMessage.setVisibility(View.VISIBLE);
            }
        }

        if (0 < resultInfoList.size()) {
            for (ContentsData info : resultInfoList) {
                //チャンネル名を付加
                info.setChannelName(searchChannelName(info.getChannelId()));

                baseFragment.mData.add(info);
            }

            DTVTLogger.debug("baseFragment.mData.mSize = " + baseFragment.mData.size());

            // フラグメントの更新
            baseFragment.notifyDataSetChanged(mRecommendViewPager.getCurrentItem());
            //ゼロ以下ならばゼロにする
            if (mSearchLastItem < 0) {
                DTVTLogger.debug("mSearchLastItem = " + mSearchLastItem);
                mSearchLastItem = 0;
            }

            baseFragment.setSelection(mSearchLastItem);
            baseFragment.displayLoadMore(false);
            setSearchStart(false);
        }
    }

    /**
     * 指定されたIDを持つチャンネル名を見つける.
     *
     * @param channelId チャンネルID
     * @return 見つかったチャンネル名
     */
    private String searchChannelName(final String channelId) {
        //チャンネルデータの取得がまだの場合や、チャンネル名を使うのはテレビタブだけなので、それ以外のタブなら帰る
        if (mChannels == null
                || (mRecommendViewPager.getCurrentItem() != RECOMMEND_LIST_PAGE_NO_OF_TV
                && mRecommendViewPager.getCurrentItem() != RECOMMEND_LIST_PAGE_NO_OF_DTV_CHANNEL)) {
            return "";
        }

        //チャンネル名検索
        for (int ct = 0; ct <= mChannels.size(); ct++) {
            if (!TextUtils.isEmpty(channelId)
                    && mChannels.get(ct).getServiceId().equals(channelId)) {
                //チャンネルIDが見つかった
                return mChannels.get(ct).getTitle();
            }
        }

        //見つからなければ空文字
        return "";
    }

    /**
     * フラグメントクリア.
     */
    private void clearAllFragment() {

        if (null != mRecommendViewPager) {
            int sum = mRecommendFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                RecommendBaseFragment baseFragment = mRecommendFragmentFactory.createFragment(i);
                baseFragment.clear();
            }
        }
    }

    /**
     * データ取得失敗時の処理.
     */
    private void recommendDataProviderFinishNg() {
        //エラーメッセージを表示する
        showErrorMessage(mRecommendViewPager.getCurrentItem());
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
                return mRecommendFragmentFactory.createFragment(position);
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
     * @return errorMessage
     */
    private boolean showErrorMessage(final int tabFlg) {
        boolean isError = false;
        ErrorState errorState = mRecommendDataProvider.getError(tabFlg);
        if (errorState != null && errorState.getErrorType() != DtvtConstants.ErrorType.SUCCESS) {
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
                if (mRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("Chan Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
                    if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV) {
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
                if (mRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("vid Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
                    if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO) {
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
                if (mRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("dtv Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
                    if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV) {
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
                if (mRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("ani Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
                    if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME) {
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
                if (mRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("dCH Callback DataSize:"
                            + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
                    if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL) {
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
                    //メニューから起動の場合ホーム画面に戻る
                    contentsDetailBackKey(null);
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

        //この組み合わせは、Androidのソースでも行われている正当な方法です。
        System.gc();
        System.runFinalization();
        System.gc();
    }


    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();

        //チャンネル情報の取得を依頼する
        getChannel();

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
        stopRecommendDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mRecommendDataProvider);

        //チャンネル情報の通信を止める
        if (mScaledDownProgramListDataProvider != null) {
            StopScaledProListDataConnect stopScaledProListDataConnect = new StopScaledProListDataConnect();
            stopScaledProListDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mScaledDownProgramListDataProvider);
        }

        //FragmentにContentsAdapterの通信を止めるように通知する
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null) {
            baseFragment.stopContentsAdapterCommunication();
        }
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        //こちらは使用しない
    }

    /**
     * チャンネル情報の取得をデータプロバイダーに依頼する.
     */
    private void getChannel() {
        DTVTLogger.start();
        synchronized (mProgramList) {
            super.onStartCommunication();

            if (null == mScaledDownProgramListDataProvider) {
                mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            } else {
                mScaledDownProgramListDataProvider.enableConnect();
            }
            mHandle.postDelayed(mProgramList, 0);
        }
        DTVTLogger.end();
    }


    /**
     * 番組表データスレッド.
     */
    private final Runnable mProgramList = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            //全番組表の取り込み
            mScaledDownProgramListDataProvider.getChannelList(0, 0, "", 0);
            DTVTLogger.end();
        }
    };

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        //チャンネル情報を受け取る

        DTVTLogger.start();

        //取得できたかどうかの判断
        if (null == channels) {
            //エラーメッセージを取得する
            String message = mScaledDownProgramListDataProvider.
                    getChannelError().getErrorMessage();

            //有無で処理を分ける
            if (TextUtils.isEmpty(message)) {
                showGetDataFailedToast();
            } else {
                showGetDataFailedToast(message);
            }

            DTVTLogger.end();
            return;
        }

        //後で使用する為に控えておく
        mChannels = channels;

        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null && baseFragment.mData != null) {
            //おすすめ情報にはチャンネル名が無いので、取得したチャンネル名をIDで検索して設定を行う
            for (int count = 0; count < baseFragment.mData.size(); count++) {
                baseFragment.mData.get(count).setChannelName(
                        searchChannelName(baseFragment.mData.get(count).getChannelId()));
            }

            if (baseFragment.mData.size() > 0) {
                //処理を行ったデータが存在するならば再描画
                baseFragment.notifyDataSetChanged(mRecommendViewPager.getCurrentItem());
            }
        }

        DTVTLogger.end();
    }
}