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
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
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
     * タブインデックス　テレビ.
     */
    private static final int TAB_INDEX_TV = 0;
    /**
     * タブインデックス　ビデオ.
     */
    private static final int TAB_INDEX_VIDEO = 1;
    /**
     * タブインデックス　dTV.
     */
    private static final int TAB_INDEX_DTV = 2;
    /**
     * タブインデックス　dTVチャンネル.
     */
    private static final int TAB_INDEX_DTV_CHANNEL = 3;
    /**
     * タブインデックス　dアニメストア.
     */
    private static final int TAB_INDEX_DANIME = 4;
    /**
     * タブインデックス　DAZN.
     */
    private static final int TAB_INDEX_DAZN = 5;
    /**
     * 表示中タブのインデックス.
     */
    private int mSelectedTabIndex = 0;
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
    /**
     * FG→BGに遷移する前のポジション.
     */
    private static final String RECOMMEND_VIEWPAGER_POSITION = "recommend_viewpager_position";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        DTVTLogger.start();
        Intent intent = getIntent();
        int startPageNo = intent.getIntExtra(RECOMMEND_LIST_START_PAGE, RECOMMEND_LIST_PAGE_NO_OF_TV);
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            startPageNo = RECOMMEND_LIST_PAGE_NO_OF_TV;
        }
        if (savedInstanceState != null) {
            startPageNo = savedInstanceState
                    .getInt(RECOMMEND_VIEWPAGER_POSITION);
            savedInstanceState.clear();
        }
        //call super.onCreate() after savedInstanceState.clear() to work around the bug caused by dashO.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.recommend_list_title));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);

        initData();
        initRecommendListView();

        //初回起動フラグをONにする
        mIsFirst = true;
        //初回表示のみ前画面からのタブ指定を反映する
        mRecommendViewPager.setCurrentItem(startPageNo);
        mTabLayout.setTab(startPageNo);
        DTVTLogger.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        sendScreenViewForPosition(mSelectedTabIndex, false);
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
        mRecommendFragmentFactory = new RecommendFragmentFactory();
    }

    /**
     * データプロバイダへデータ取得要求.
     */
    private void requestRecommendData() {

        if (mRecommendDataProvider != null) {
            mRecommendDataProvider.stopConnect();
        }
        mRecommendDataProvider = new RecommendDataProvider(this);

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
                mSelectedTabIndex = position;
                sendScreenViewForPosition(position, true);
                clearAllFragment();
                showProgressBar(true);
                mSearchLastItem = 0;
                requestRecommendData();
            }
        });
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     * @param position タブポジション
     * @param isFromTab true:タブ切り替え false:onResume
     */
    private void sendScreenViewForPosition(final int position, final boolean isFromTab) {
        String screenName = null;
        String serviceName = null;
        switch (position) {
            case TAB_INDEX_TV:
                screenName = getString(R.string.google_analytics_screen_name_recommend_tv);
                serviceName = getString(R.string.google_analytics_custom_dimension_service_h4d);
                break;
            case TAB_INDEX_VIDEO:
                screenName = getString(R.string.google_analytics_screen_name_recommend_video);
                serviceName = getString(R.string.google_analytics_custom_dimension_service_h4d);
                break;
            case TAB_INDEX_DTV:
                screenName = getString(R.string.google_analytics_screen_name_recommend_dtv);
                serviceName = getString(R.string.google_analytics_custom_dimension_service_dtv);
                break;
            case TAB_INDEX_DTV_CHANNEL:
                screenName = getString(R.string.google_analytics_screen_name_recommend_dtv_channel);
                serviceName = getString(R.string.google_analytics_custom_dimension_service_dtv_ch);
                break;
            case TAB_INDEX_DAZN:
                screenName = getString(R.string.google_analytics_screen_name_recommend_dazn);
                serviceName = getString(R.string.google_analytics_custom_dimension_service_dazn);
                break;
            case TAB_INDEX_DANIME:
                screenName = getString(R.string.google_analytics_screen_name_recommend_danime);
                serviceName = getString(R.string.google_analytics_custom_dimension_service_danime);
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(screenName)) {
            if (mIsFromBgFlg && !isFromTab) {
                super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(RecommendActivity.this));
            } else {
                SparseArray<String> customDimensions = new SparseArray<>();
                customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, serviceName);
                super.sendScreenView(screenName, customDimensions);
            }
        }
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != mRecommendViewPager) {
            DTVTLogger.debug("viewpager not null");

            //現在選択されているタブと違うタブが押された場合に処理を行う
            if (mRecommendViewPager.getCurrentItem() != position) {
                mRecommendViewPager.setCurrentItem(position);
                RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
                if (fragment != null) {
                    fragment.showNoDataMessage(false, null);
                }
            } else {
                DTVTLogger.debug("viewpager same tab");
            }
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

        baseFragment.clear();
        baseFragment.setProgressBarFlag(false);
        if (resultInfoList == null) {
            baseFragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
        } else if (resultInfoList.isEmpty() || resultInfoList.size() <= 0) {
            baseFragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
        }
        if (resultInfoList != null && 0 < resultInfoList.size()) {
            for (ContentsData info : resultInfoList) {
                //チャンネル名を付加
                info.setChannelName(searchChannelName(info.getChannelId(), info.getServiceId(), info.getCategoryId()));

                baseFragment.addData(info);
            }

            DTVTLogger.debug("baseFragment.mData.mSize = " + baseFragment.getDataSize());

            // フラグメントの更新
            baseFragment.notifyDataSetChanged(mRecommendViewPager.getCurrentItem());
            //ゼロ以下ならばゼロにする
            if (mSearchLastItem < 0) {
                DTVTLogger.debug("mSearchLastItem = " + mSearchLastItem);
                mSearchLastItem = 0;
            }

            baseFragment.setSelection(mSearchLastItem);
        }
    }

    /**
     * 指定されたIDを持つチャンネル名を見つける.
     *
     * @param channelId チャンネルID
     * @param serviceId サービスID
     * @param categoryId カテゴリID
     * @return 見つかったチャンネル名
     */
    private String searchChannelName(final String channelId, final String serviceId, final String categoryId) {
        //チャンネルデータの取得がまだの場合や、チャンネル名を使うのはテレビタブだけなので、それ以外のタブなら帰る
        if (mChannels == null
                || (mRecommendViewPager.getCurrentItem() != RECOMMEND_LIST_PAGE_NO_OF_TV
                && mRecommendViewPager.getCurrentItem() != RECOMMEND_LIST_PAGE_NO_OF_DTV_CHANNEL)) {
            return "";
        }
        return ContentUtils.getChannelName(channelId, serviceId, categoryId, mChannels);
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
        if (mRecommendViewPager != null) {
            RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
            if (fragment != null) {
                fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                //プログレスは消す
                fragment.showProgressBar(false);
                fragment.setProgressBarFlag(false);
            }
        }
        showErrorMessage(mRecommendViewPager.getCurrentItem());
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment == null) {
            return;
        }

        DTVTLogger.debug("onSearchDataProviderFinishNg");
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

    @Override
    public void recommendHomeChannelCallback(final List<ContentsData> recommendContentInfoList) {
        //こちらは使用しない
    }

    @Override
    public void recommendHomeVideoCallback(final List<ContentsData> recommendContentInfoList) {
        //こちらは使用しない
    }

    /**
     * Fragmentの取得.
     * @param viewPager viewPager
     * @param recommendFragmentFactory RecommendFragmentFactory
     * @return Fragment
     */
    protected RecommendBaseFragment getCurrentFragment(final ViewPager viewPager, final RecommendFragmentFactory recommendFragmentFactory) {
        int i = viewPager.getCurrentItem();
        if (recommendFragmentFactory != null) {
            return recommendFragmentFactory.createFragment(i);
        }
        return null;
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
                    if (mRecommendViewPager != null) {
                        RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
                        if (fragment != null) {
                            fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                        }
                    }
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
                    if (mRecommendViewPager != null) {
                        RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
                        if (fragment != null) {
                            fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                        }
                    }
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
                    if (mRecommendViewPager != null) {
                        RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
                        if (fragment != null) {
                            fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                        }
                    }
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
                    if (mRecommendViewPager != null) {
                        RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
                        if (fragment != null) {
                            fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                        }
                    }
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
                    if (mRecommendViewPager != null) {
                        RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
                        if (fragment != null) {
                            fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                        }
                    }
                    showErrorMessage(RecommendDataProvider.API_INDEX_OTHER);
                }
            }
        });
    }

    @Override
    public void recommendDAZNCallback(final List<ContentsData> recommendContentInfoList) {
        //DbThreadからのコールバックの場合UIスレッド扱いにならないときがある
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRecommendViewPager != null && recommendContentInfoList != null) {
                    DTVTLogger.debug("ani Callback DataSize:" + recommendContentInfoList.size()
                            + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
                    if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DAZN) {
                        recommendDataProviderSuccess(recommendContentInfoList);
                    }
                } else {
                    if (mRecommendViewPager != null) {
                        RecommendBaseFragment fragment = getCurrentFragment(mRecommendViewPager, mRecommendFragmentFactory);
                        if (fragment != null) {
                            fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                        }
                    }
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
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTabLayout != null) {
            if (mSelectedTabIndex < 0) {
                mSelectedTabIndex = 0;
            }
            outState.putInt(RECOMMEND_VIEWPAGER_POSITION, mSelectedTabIndex);
        }

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
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final String[] serviceIdUniq) {
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
            mScaledDownProgramListDataProvider.setAreaCode(UserInfoUtils.getAreaCode(RecommendActivity.this));
            mScaledDownProgramListDataProvider.getChannelList(0, 0, "", 0);
            DTVTLogger.end();
        }
    };

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        //チャンネル情報を受け取る
        DTVTLogger.start();

        if (null == channels) {
            //チャンネルリストが取得できなければ表示上不都合（チャンネル名が一切表示できない）なのでダイアログ表示で戻る
            //エラーメッセージを取得する
            String message = mScaledDownProgramListDataProvider.
                    getChannelError().getErrorMessage();
            //有無で処理を分ける
            showGetDataFailedToast(message);
            DTVTLogger.end();
            return;
        }

        //後で使用する為に控えておく
        mChannels = channels;

        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null && baseFragment.getData() != null) {
            //おすすめ情報にはチャンネル名が無いので、取得したチャンネル名をIDで検索して設定を行う
            for (int count = 0; count < baseFragment.getDataSize(); count++) {
                if (baseFragment.getData() != null) {
                    ContentsData contentsData = baseFragment.getData().get(count);
                    baseFragment.getData().get(count).setChannelName(
                            searchChannelName(contentsData.getChannelId(), contentsData.getServiceId(), contentsData.getCategoryId()));
                }
            }

            if (baseFragment.getDataSize() > 0) {
                //処理を行ったデータが存在するならば再描画
                baseFragment.notifyDataSetChanged(mRecommendViewPager.getCurrentItem());
            }
        }

        DTVTLogger.end();
    }

    @Override
    public void clipKeyResult() {
        //Nop 仕様により実装のみ
    }

    /**
     * 読み込みアイコンの表示切替.
     *
     * @param showProgressBar ウェイト表示を行うならばtrue
     */
    private void showProgressBar(final boolean showProgressBar) {
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null) {
            baseFragment.showProgressBar(showProgressBar);

        }
    }
}