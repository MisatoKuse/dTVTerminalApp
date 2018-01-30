/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.callback.VideoRankingApiDataProviderCallback;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.model.TabItemLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoRankingActivity extends BaseActivity implements VideoRankingApiDataProviderCallback, RankingFragmentScrollListener,
        VideoGenreProvider.RankGenreListCallback, TabItemLayout.OnClickTabTextListener {
    private boolean mIsCommunicating = false;
    private final int NUM_PER_PAGE = 2;
    private String[] mTabNames;
    private RankingTopDataProvider mRankingDataProvider;
    private RankingFragmentFactory mRankingFragmentFactory = null;
    private TabItemLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<GenreListMetaData> genreMetaDataList;
    private ProgressBar progressBar;

    //標準タブ数
    private static final int DEFAULT_TAB_MAX = 4;

    //設定するマージンのピクセル数
    private static final int LEFT_MARGIN = 30;
    //設定する文字サイズ
    private static final int TEXT_SIZE = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_ranking_main_layout);
        DTVTLogger.start();

        //Headerの設定
        setTitleText(getString(R.string.video_ranking_title));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        initView();
        getGenreTabData();
        resetPaging();
    }

    private void getGenreTabData(){
        new VideoGenreProvider(this, this, ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK).getGenreListDataRequest();
    }

    // ページのリセット
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null != baseFragment && null != baseFragment.mData) {
                baseFragment.mData.clear();
                baseFragment.noticeRefresh();
            }
        }
    }

    /**
     * mIsCommunicationを変更.
     *
     * @param bool
     */
    private void setCommunicatingStatus(boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    /**
     * ページングを行った回数を取得.
     *
     * @return
     */
    private int getCurrentNumber() {
        RankingBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment || null == baseFragment.mData || 0 == baseFragment.mData.size()) {
            return 0;
        } else if (baseFragment.mData.size() < NUM_PER_PAGE) {
            return 1;
        }
        return baseFragment.mData.size() / NUM_PER_PAGE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //タブ数の取得
        int tabCount;
        if (mTabNames != null) {
            //通常はタブ名の数を取得して対処
            tabCount = mTabNames.length;
        } else {
            //取得できなかった場合はデフォルト数で指定
            tabCount = DEFAULT_TAB_MAX;
        }

        for (int i = 0; i < tabCount; ++i) { // タブの数だけ処理を行う
            RankingBaseFragment baseFragment = mRankingFragmentFactory.createFragment
                    (ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK, i, this);
            if (null != baseFragment) {
                baseFragment.mData.clear();
            }
        }
    }

    /**
     * Viewの初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        progressBar = findViewById(R.id.video_ranking_progress);
        mViewPager = findViewById(R.id.vp_video_ranking_result);
    }

    private void initTab(){
        if(mRankingFragmentFactory == null){
            mRankingFragmentFactory = new RankingFragmentFactory();
        }
        RankingPagerAdapter rankingPagerAdapter = new RankingPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(rankingPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                setTab(position);
                getGenreData();
            }
        });
        initTabData();
        getGenreData();
    }

    /**
     * タブ毎にリクエストを行う.
     */
    private void getGenreData() {
        if(mRankingDataProvider == null){
            mRankingDataProvider =
                    new RankingTopDataProvider(this, ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
        }
        if(genreMetaDataList != null && genreMetaDataList.size() > 0){
            mRankingDataProvider.getVideoRankingData(genreMetaDataList.get(mViewPager.getCurrentItem()).getId());
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * tabのレイアウトを設定.
     */
    private void initTabData() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.VIDEO_RANKING_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_video_ranking_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(int position) {
        DTVTLogger.start("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * 取得結果の設定・表示.
     */
    private void setShowVideoRanking(List<ContentsData> videoRankMapList) {
        if (null == videoRankMapList || 0 == videoRankMapList.size()) {
            //通信とJSON Parseに関してerror処理
            //TODO: エラー表示は検討の必要あり
            Toast.makeText(this, "ランキングデータ取得失敗", Toast.LENGTH_SHORT).show();
            return;
        }


        RankingBaseFragment fragment = mRankingFragmentFactory.createFragment(
                ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK,
                mViewPager.getCurrentItem(), this);

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (null != fragment.mData && fragment.mData.size() >= videoRankMapList.size()) {
            fragment.displayMoreData(false);
            return;
        }

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE
                && i < videoRankMapList.size(); ++i) {
            DTVTLogger.debug("i = " + i);
            if (null != fragment.mData) {
                fragment.mData.add(videoRankMapList.get(i));
            }
        }
        if (fragment.mData != null) {
            DTVTLogger.debug("Fragment.mData.size :" + String.valueOf(fragment.mData.size()));
        }
        resetCommunication();
        fragment.noticeRefresh();

    }


    /**
     * 読み込み中表示を非表示に変更.
     */
    private void resetCommunication() {
        RankingBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment) {
            return;
        }
        baseFragment.displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     */
    private List<ContentsData> setVideoRankingContentData(
            List<Map<String, String>> videoRankMapList) {
        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData rankingContentInfo;

        for (int i = 0; i < videoRankMapList.size(); i++) {
            rankingContentInfo = new ContentsData();
            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setThumURL(videoRankMapList.get(i)
                    .get(JsonConstants.META_RESPONSE_THUMB_448));
            rankingContentInfo.setTitle(videoRankMapList.get(i)
                    .get(JsonConstants.META_RESPONSE_TITLE));
            rankingContentInfo.setRatStar(videoRankMapList.get(i)
                    .get(JsonConstants.META_RESPONSE_RATING));

            rankingContentsDataList.add(rankingContentInfo);
            DTVTLogger.info("RankingContentInfo " + rankingContentInfo.getRank());
        }

        return rankingContentsDataList;
    }

    /**
     * コンテンツ詳細への遷移.
     *
     * @param view
     */
    public void contentsDetailButton(View view) {
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        startActivity(intent);
    }

    /**
     * クリップボタン.
     *
     * @param view
     */
    public void clipButton(View view) {
        Toast.makeText(this, "クリップしました", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScroll(RankingBaseFragment fragment, AbsListView absListView,
                         int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        synchronized (this) {
            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getRankingAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("Activity::onScroll, paging, firstVisibleItem="
                        + firstVisibleItem + ", totalItemCount=" + totalItemCount
                        + ", visibleItemCount=" + visibleItemCount);

            }
        }
    }

    @Override
    public void onScrollStateChanged(RankingBaseFragment fragment, AbsListView absListView,
                                     int scrollState) {
        synchronized (this) {

            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getRankingAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == fragment.getRankingAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");

                fragment.displayMoreData(true);
                setCommunicatingStatus(true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getGenreData();
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }

    }

    /**
     * インジケーター設置.
     */
    private void setTab(int position) {
        mTabLayout.setTab(position);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Fragmentの取得.
     *
     * @return
     */
    private RankingBaseFragment getCurrentFragment() {

        int i = mViewPager.getCurrentItem();
        if(mRankingFragmentFactory != null){
            return mRankingFragmentFactory.createFragment(ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK, i, this);
        }
        return null;
    }

    private VideoRankingActivity getVideoRankingActivity() {
        return this;
    }


    /**
     * 取得条件"総合"用コールバック.
     * TODO:正規のジャンルで動的に処理するようにしないといけない
     *
     * @param videoRankList
     */
    @Override
    public void onVideoRankListCallback(List<ContentsData> videoRankList) {
        progressBar.setVisibility(View.GONE);
        DTVTLogger.start("ResponseDataSize :" + videoRankList.size());
        setShowVideoRanking(videoRankList);
        DTVTLogger.end();
    }

    @Override
    public void onRankGenreListCallback(ArrayList<GenreListMetaData> genreMetaDataList) {
        if(genreMetaDataList != null){
            this.genreMetaDataList = genreMetaDataList;
            int totalSize = genreMetaDataList.size();
            mTabNames = new String[totalSize];
            for(int i=0; i < totalSize; i++){
                mTabNames[i] = genreMetaDataList.get(i).getTitle();
            }
            initTab();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoRankingActivity.this,"ジャンルデータ取得失敗しました", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 検索結果タブ専用アダプター.
     */
    private class RankingPagerAdapter extends FragmentStatePagerAdapter {
        private RankingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mRankingFragmentFactory.createFragment(
                    ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK,
                    position, getVideoRankingActivity());
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DTVTLogger.start();
        if (checkRemoteControllerView()) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
