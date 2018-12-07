/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.ArrayList;

/**
 * チャンネルリスト.
 */
public class ChannelListActivity extends BaseActivity implements
        View.OnClickListener,
        ChannelListFragment.ChannelListFragmentListener,
        ChannelListFragment.OnClickChannelItemListener,
        TabItemLayout.OnClickTabTextListener,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        HikariTvChannelDataProvider.ContentsDataCallback {

    // region declaration
    /** チャンネルリストのタイプ. */
    public enum ChannelListDataType {
        /** タイプ:ひかりTV. */
        CH_LIST_DATA_TYPE_HIKARI,
        /** タイプ:地上波. */
        CH_LIST_DATA_TYPE_TDB,
        /** タイプ:BS. */
        CH_LIST_DATA_TYPE_BS,
        /** タイプ:dTVチャンネル. */
        CH_LIST_DATA_TYPE_DCH
    }
    // endregion declaration

    // region variable
    /** AreaCodeが取得できなかった時. */
    private static final ChannelListDataType[] CHANNEL_LIST_TAB_AREA_CODE_NOT_EXIST_LIST =
            {
                    ChannelListDataType.CH_LIST_DATA_TYPE_HIKARI,
                    ChannelListDataType.CH_LIST_DATA_TYPE_BS,
                    ChannelListDataType.CH_LIST_DATA_TYPE_DCH
            };
    /** AreaCodeが取得できた時.*/
    private static final ChannelListDataType[] CHANNEL_LIST_AREA_CODE_EXIST_LIST =
            {
                    ChannelListDataType.CH_LIST_DATA_TYPE_HIKARI,
                    ChannelListDataType.CH_LIST_DATA_TYPE_TDB,
                    ChannelListDataType.CH_LIST_DATA_TYPE_BS,
                    ChannelListDataType.CH_LIST_DATA_TYPE_DCH
            };

    /** タブインデックス　ひかりTV for docomo.*/
    private static final int TAB_INDEX_HIKARI = 0;
    /** タブインデックス　地上波.*/
    private static final int TAB_INDEX_TER = 1;
    /** タブインデックス　BS.*/
    private static final int TAB_INDEX_BS = 2;
    /** タブインデックス　dTVチャンネル.*/
    private static final int TAB_INDEX_DTV = 3;

    /** ビューページャ. */
    private ViewPager mViewPager = null;
    /** 共通タブ. */
    private TabItemLayout mTabLayout = null;
    /** チャンネルリストフラグメントファクトリー. */
    private ChannelListFragmentFactory mFactory = null;
    /** リスト0件メッセージ. */
    private TextView mNoDataMessage;

    /** hikariTVチャンネルプロバイダー. */
    private HikariTvChannelDataProvider mHikariTvChannelDataProvider = null;

    /** ハンドラー(DataProvider). */
    private final Handler mDataProviderHandler = new Handler();

    /**ひかりTVチャンネル一覧.*/
    private ArrayList<ChannelInfo> mHikariTvChannelList;
    /**地デジチャンネル一覧.*/
    private ArrayList<ChannelInfo> mTTbChannelList;
    /**BSチャンネル一覧.*/
    private ArrayList<ChannelInfo> mBsChannelList;
    /**dTvチャンネル一覧.*/
    private ArrayList<ChannelInfo> mdTvChannelList;

    /** タブリスト. */
    private String[] mTabNames = null;
    /** 現在タイプ. */
    private ChannelListDataType mCurrentType = ChannelListDataType.CH_LIST_DATA_TYPE_HIKARI;
    /** メニュー表示フラグ.*/
    private Boolean mIsMenuLaunch = false;
    /** 前回のタブ位置.*/
    private static final int DEFAULT_TAB_INDEX = -1;
    /** 前回のタブ位置.*/
    private int mTabIndex = DEFAULT_TAB_INDEX;
    /** 前回のタブ位置.*/
    private static final String TAB_INDEX_BEFORE = "tabIndex";
    // endregion variable

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.channel_list_main_layout);
        if (savedInstanceState != null) {
            mTabIndex = savedInstanceState.getInt(TAB_INDEX_BEFORE);
            savedInstanceState.clear();
        }
        setTitleText(getString(R.string.channel_list_activity_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);

        initView();
        initData();
        DTVTLogger.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        sendScreenViewForPosition(mViewPager.getCurrentItem(), false);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_INDEX_BEFORE, mViewPager.getCurrentItem());
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        if (null == mHikariTvChannelDataProvider) {
            mHikariTvChannelDataProvider = new HikariTvChannelDataProvider(this, this);
        }
        mHikariTvChannelDataProvider.enableConnect();
    }

    /**
     * CHリストデータ取得.
     */
    private void getChListData() {
        mNoDataMessage.setVisibility(View.GONE);
        switch (mCurrentType) {
            case CH_LIST_DATA_TYPE_HIKARI:
                mHikariTvChannelDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_HIKARI);
                break;
            case CH_LIST_DATA_TYPE_TDB:
                mHikariTvChannelDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_TTB);
                break;
            case CH_LIST_DATA_TYPE_BS:
                mHikariTvChannelDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_BS);
                break;
            case CH_LIST_DATA_TYPE_DCH:
                mHikariTvChannelDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_DCH);
                break;
            default:
                break;
        }

    }

    /**
     * onPause処理.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (null != mHikariTvChannelDataProvider) {
            mHikariTvChannelDataProvider.stopConnect();
        }
    }

    /**
     * データ初期化.
     */
    private void initData() {
        mFactory = new ChannelListFragmentFactory();
        if (null == mHikariTvChannelDataProvider) {
            mHikariTvChannelDataProvider = new HikariTvChannelDataProvider(this, this);
        }
    }

    /**
     * view初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mNoDataMessage = findViewById(R.id.channel_list_no_items);
        initChannelListTab();
        ChannelListPagerAdapter adp = new ChannelListPagerAdapter(getSupportFragmentManager());
        final ChannelListFragment.ChannelListFragmentListener lis = this;
        mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        mViewPager.setAdapter(adp);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(final int position) {
                        super.onPageSelected(position);
                        mTabLayout.setTab(position);
                        sendScreenViewForPosition(position, true);
                    }
                });
    }

    /**
     * tab関連Viewの初期化.
     */
    private void initChannelListTab() {
        Resources res = getResources();
        String areaCode = UserInfoUtils.getAreaCode(this);
        if (areaCode != null && !areaCode.isEmpty()) {
            mTabNames = res.getStringArray(R.array.channel_list_tab_names);
        } else {
            mTabNames = res.getStringArray(R.array.channel_list_tab_names_no_paring);
        }

        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.SEARCH_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_channel_list_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
    }

    /**
     * 送信containerIdと表示中のタブ一致のチェック.
     * @param isSuccessMessage true　0件：false 取得失敗
     */
    private void showListMessage(final boolean isSuccessMessage) {
        mNoDataMessage.setVisibility(View.VISIBLE);
        if (isSuccessMessage) {
            mNoDataMessage.setText(getString(R.string.common_empty_data_message));
        } else {
            mNoDataMessage.setText(getString(R.string.common_get_data_failed_message));
        }
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.debug("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     * @param position ポジション
     * @param isFromTab true:タブ切り替え
     */
    private void sendScreenViewForPosition(final int position, final boolean isFromTab) {
        String screenName = null;
        switch (position) {
            case TAB_INDEX_HIKARI:
                screenName = getString(R.string.google_analytics_screen_name_channel_list_h4d);
                break;
            case TAB_INDEX_TER:
                if (mTabNames.length == CHANNEL_LIST_TAB_AREA_CODE_NOT_EXIST_LIST.length) {
                    screenName = getString(R.string.google_analytics_screen_name_channel_list_bs);
                } else {
                    screenName = getString(R.string.google_analytics_screen_name_channel_list_ter);
                }
                break;
            case TAB_INDEX_BS:
                if (mTabNames.length == CHANNEL_LIST_TAB_AREA_CODE_NOT_EXIST_LIST.length) {
                    screenName = getString(R.string.google_analytics_screen_name_channel_list_dtv);
                } else {
                    screenName = getString(R.string.google_analytics_screen_name_channel_list_bs);
                }

                break;
            case TAB_INDEX_DTV:
                screenName = getString(R.string.google_analytics_screen_name_channel_list_dtv);
                break;
            default:
                break;
        }
        super.sendScreenView(screenName,
                (mIsFromBgFlg && !isFromTab) ? ContentUtils.getParingAndLoginCustomDimensions(ChannelListActivity.this) : null);
    }

    /**
     * Activityを取得.
     *
     * @return this this
     */
    private ChannelListActivity getActivity() {
        return this;
    }

    @Override
    public void onClickChannelItem(final int pos, final ChannelListDataType type,
                                   final ChannelListFragment fragment) {
        switch (type) {
            case CH_LIST_DATA_TYPE_BS:
                getNowOnAirProgramForItemClick(mBsChannelList, pos);
                break;
            case CH_LIST_DATA_TYPE_TDB:
                getNowOnAirProgramForItemClick(mTTbChannelList, pos);
                break;
            case CH_LIST_DATA_TYPE_HIKARI:
                getNowOnAirProgramForItemClick(mHikariTvChannelList, pos);
                break;
            case CH_LIST_DATA_TYPE_DCH:
                getNowOnAirProgramForItemClick(mdTvChannelList, pos);
                break;
        }
    }

    private void getNowOnAirProgramForItemClick(final ArrayList<ChannelInfo> channelList, final int pos) {
        if (channelList.size() < pos) {
            DTVTLogger.error("pos = " + pos + " is invalid ChannelList.size() = " + channelList.size());
            return;
        }

        if (NetWorkUtils.isOnline(ChannelListActivity.this)) {
            ChannelInfo channelInfo = channelList.get(pos);
            mHikariTvChannelDataProvider.getNowOnAirProgram(channelInfo.getServiceIdUniq());
        } else {
            showErrorDialog(getString(R.string.network_nw_error_message_dialog));
        }
    }

    @Override
    public void onContentDataGet(final ContentsData data) {
        if (data == null) {
            //Now On Airコンテンツがない場合はダイアログ表示
            showErrorDialog(getString(R.string.contents_detail_now_on_air_contents_nothing));
            return;
        }
        Intent intent = new Intent(this, ContentDetailActivity.class);
        ComponentName componentName = this.getComponentName();
        intent.putExtra(DtvtConstants.SOURCE_SCREEN, componentName.getClassName());
        OtherContentsDetailData detailData = DataConverter.getOtherContentsDetailData(data, ContentUtils.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser, final ChannelListFragment fragment) {
        DTVTLogger.start();
        mNoDataMessage.setVisibility(View.GONE);
        if (!isVisibleToUser || fragment.getDataCount() > 0) {
            fragment.showProgressBar(false);
            DTVTLogger.end();
            return;
        }

        fragment.showProgressBar(true);
        ChannelListDataType type = fragment.getChListDataType();
        setCurrentType(type);
        getChListData();
        DTVTLogger.end();
    }

    /**
     * データタイプ取得.
     *
     * @param viewPagerIndex viewPagerIndex
     * @return ChannelListDataType
     */
    private ChannelListDataType getTypeFromViewPagerIndex(final int viewPagerIndex) {
        DTVTLogger.start();
        ChannelListDataType ret;
        if (mTabNames.length == CHANNEL_LIST_AREA_CODE_EXIST_LIST.length) {
            ret = CHANNEL_LIST_AREA_CODE_EXIST_LIST[viewPagerIndex];
        } else {
            ret = CHANNEL_LIST_TAB_AREA_CODE_NOT_EXIST_LIST[viewPagerIndex];
        }
        DTVTLogger.end();
        return ret;
    }

    /**
     * チャンネルリストアダプター.
     */
    private class ChannelListPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * コンストラクタ.
         *
         * @param fm fm
         */
        ChannelListPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            if (null == mFactory) {
                return null;
            }
            return mFactory.createFragment(position, getActivity(), getTypeFromViewPagerIndex(position), getActivity());
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

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final String[] serviceIdUniqs) {
        DTVTLogger.error(" >>>");
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        if (mTabIndex >= 0) {
            mViewPager.setCurrentItem(mTabIndex);
            mTabIndex = DEFAULT_TAB_INDEX;
        }
        int pos = mViewPager.getCurrentItem();
        ChannelListDataType chType = getTypeFromViewPagerIndex(pos);
        ChannelListFragment fragment = mFactory.createFragment(pos, this, chType, this);
        fragment.showProgressBar(false);

        if (null == channels) {
            if (NetWorkUtils.isOnline(this)) {
                showListMessage(true);
            } else {
                showListMessage(false);
            }
            //エラーメッセージを取得する
            String message = mHikariTvChannelDataProvider.getChannelError().getErrorMessage();

            //有無で処理を分ける
            if (TextUtils.isEmpty(message)) {
                showGetDataFailedToast();
            } else {
                showGetDataFailedToast(message);
            }

            DTVTLogger.end();
            return;
        }
        int size = channels.size();

        if (0 == size) {
            if (NetWorkUtils.isOnline(this)) {
                showListMessage(true);
            } else {
                showListMessage(false);
                showGetDataFailedToast(getString(R.string.network_nw_error_message_dialog));
            }
            return;
        }
        fragment = mFactory.createFragment(pos, this, chType, this);
        switch (chType) {
            case CH_LIST_DATA_TYPE_HIKARI:
                mHikariTvChannelList = channels;
                break;
            case CH_LIST_DATA_TYPE_DCH:
                mdTvChannelList = channels;
                break;
            case CH_LIST_DATA_TYPE_TDB:
                mTTbChannelList = channels;
                break;
            case CH_LIST_DATA_TYPE_BS:
                mBsChannelList = channels;
                break;
            default:
                fragment = null;
                break;
        }
        if (null == fragment) {
            return;
        }
        updateUi(fragment, channels);
        DTVTLogger.end();
    }

    /**
     * フラグメントのUI更新.
     * @param fragment フラグメント
     * @param list     リスト
     */
    private void updateUi(final ChannelListFragment fragment, final ArrayList list) {
        DTVTLogger.start();
        mDataProviderHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null == fragment || null == list) {
                    return;
                }

                for (Object item : list) {
                    fragment.addData(item);
                }
                noticeRefresh(fragment);
                fragment.showProgressBar(false);
            }
        });

        DTVTLogger.end();
    }

    /**
     * 更新を知らせ.
     *
     * @param fragment fragment
     */
    private void noticeRefresh(final ChannelListFragment fragment) {
        if (null == fragment) {
            return;
        }
        mDataProviderHandler.post(new Runnable() {
            @Override
            public void run() {
                fragment.noticeRefresh();
            }
        });
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

    /**
     * type値設定.
     *
     * @param type type
     */
    private synchronized void setCurrentType(final ChannelListDataType type) {
        mCurrentType = type;
    }

    @Override
    public void clipKeyResult() {
        //Nop 仕様により実装のみ
    }
}
