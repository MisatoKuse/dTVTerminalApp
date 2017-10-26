/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingConstants;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentScrollListener;

import java.util.List;
import java.util.Map;

public class WeeklyTvRankingActivity extends BaseActivity implements View.OnClickListener, RankingTopDataProvider.WeeklyRankingApiDataProviderCallback, RankingFragmentScrollListener {

    private ImageView mMenuImageView;
    private boolean mIsCommunicating = false;
    private int NUM_PER_PAGE = 20;
    private String[] mTabNames;
    private RankingTopDataProvider mRankingDataProvider;
    private RankingFragmentFactory mRankingFragmentFactory=null;
    private HorizontalScrollView mTabScrollView;
    private LinearLayout mLinearLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_list_main);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.str_clip_activity_title));
        initData();

        initView();
        resetPaging();
    }

    private void resetPaging() {
        synchronized (this){
            setCommunicatingStatus(false);
            RankingBaseFragment b=getCurrentFragment();
            if(null!=b && null != b.mData){
                b.mData.clear();
                b.noticeRefresh();
            }
        }
    }

    private void setCommunicatingStatus(boolean b){
        synchronized (this){
            mIsCommunicating = b;
        }
    }

    /**
     * 表示中のページ数を取得
     * @return
     */
    private int getCurrentNumber(){
        RankingBaseFragment b = getCurrentFragment();
        if(null==b || null==b.mData || 0==b.mData.size()){
            return 0;
        }
        return b.mData.size()/NUM_PER_PAGE;
    }

    private void initData() {
        mTabNames = getResources().getStringArray(R.array.ranking_tab_names);
        mRankingDataProvider = new RankingTopDataProvider(this);
        mRankingFragmentFactory = new RankingFragmentFactory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(int i=0;i<4; ++i){ // タブの数だけ処理を行う
            RankingBaseFragment b =mRankingFragmentFactory.createFragment
                    (RankingConstants.RankingModeNo.RANKING_MODE_NO_OF_WEEKLY,i, this);
            if(null!=b){
                b.mData.clear();
            }
        }
    }

    /**
     *  tabのレイアウトを設定
     */
    private void initView() {
        mTabScrollView.removeAllViews();
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setBackgroundColor(Color.BLACK);
        mLinearLayout.setGravity(Gravity.CENTER);
        mTabScrollView.addView(mLinearLayout);

        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins(30, 0, 0, 0);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(15);
            tabTextView.setBackgroundColor(Color.BLACK);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.indicating);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    mViewPager.setCurrentItem(position);
                    setTab(position);
                }
            });
            mLinearLayout.addView(tabTextView);
        }
    }
    /**
     * コンテンツ詳細への遷移
     *
     * @param view
     */
    public void contentsDetailButton(View view) {
        startActivity(TvPlayerActivity.class, null);
    }

    /**
     * クリップボタン
     *
     * @param view
     */
    public void clipButton(View view) {
        Toast.makeText(this, "クリップしました", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onScroll(RankingBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    @Override
    public void onScrollStateChanged(RankingBaseFragment fragment, AbsListView absListView, int scrollState) {

    }

    /*インジケーター設置*/
    public void setTab(int position) {
        //mCurrentPageNum = position;
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.indicating);
                } else {
                    mTextView.setBackgroundResource(R.drawable.indicating_no);
                }
            }
        }
    }



    private RankingBaseFragment getCurrentFragment(){

        int i= mViewPager.getCurrentItem();
        return mRankingFragmentFactory.createFragment(RankingConstants.RankingModeNo.RANKING_MODE_NO_OF_WEEKLY,i, this);
    }


    /**
     * 取得条件"総合"用コールバック
     *
     * @param weeklyHashMap
     */
    public void weeklyRankSynthesisCallback(List<Map<String, String>> weeklyHashMap) {

    }

    /**
     * 取得条件"海外映画"用コールバック
     *
     * @param weeklyHashMap
     */
    public void weeklyRankOverseasMovieCallback(List<Map<String, String>> weeklyHashMap) {

    }

    /**
     * 取得条件"国内映画"用コールバック
     *
     * @param weeklyHashMap
     */
    public void weeklyRankDomesticMovieCallback(List<Map<String, String>> weeklyHashMap) {

    }

    /**
     * 取得条件"海外TV番組・ドラマ"用コールバック
     *
     * @param weeklyHashMap
     */
    public void weeklyRankOverseasChannelCallback(List<Map<String, String>> weeklyHashMap) {

    }


}
