/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.other.RemoteControlActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.fragment.player.TvPlayerBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.TvPlayerFragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.player.TvPlayerFragmentScrollListener;

public class TvPlayerActivity extends BaseActivity implements TvPlayerFragmentScrollListener {

    private HorizontalScrollView mScrollView;
    private ViewPager mViewPager;
    private TvPlayerFragmentFactory mFragmentFactory;
    private LinearLayout mLinearLayout;
    private String[] mTabNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_main_layout);
        setStatusBarColor(R.color.contents_header_background);
        setNoTitle();
        initData();
        initView();
    }

    /**
     * ViewPagerの設定
     */
    private void initView() {
        mScrollView = findViewById(R.id.contents_detail_tab_strip_scroll);
        mViewPager = findViewById(R.id.dtv_contents_detail_tab);
        TvPlayerPagerAdapter tvPlayerPagerAdapter =
                new TvPlayerPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tvPlayerPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
                //TODO:タブ切替ごとのリクエストがある場合はここに実装する
            }
        });
    }

    /**
     * インジケータ設置
     *
     * @param position
     */
    public void setTab(int position) {
        if(mLinearLayout != null){
            for(int i=0;i<mTabNames.length;i++){
                TextView mTextView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.indicating);
                }else{
                    mTextView.setBackgroundResource(R.drawable.indicating_no);
                }
            }
        }
    }

    /**
     * リモコン画面への遷移
     *
     * @param view
     */
    public void remoteControlButton(View view) {
        startActivity(RemoteControlActivity.class, null);
    }

    /**
     * おすすめへの遷移
     *
     * @param view
     */
    public void recommendButton(View view) {
        startActivity(RecommendPlayerActivity.class, null);
    }

    public void channelButton(View view) {
        startActivity(ChannelDetailPlayerActivity.class, null);
    }

    /**
     * 録画予約ボタン
     *
     * @param view
     */
    public void scheduleRecButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view1 = View.inflate(this, R.layout.schedule_rec_dialog_layout, null);
        dialog.setView(view1, 0, 0, 0, 0);
        dialog.show();
        view1.findViewById(R.id.video_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TvPlayerActivity.this);
                final AlertDialog dialog = builder.create();
                View view1 = View.inflate(TvPlayerActivity.this, R.layout.schedule_rec_dialog_layout2, null);
                dialog.setView(view1, 0, 0, 0, 0);
                dialog.show();
            }
        });
        view1.findViewById(R.id.video_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    /**
     * データ初期化
     */
    private void initData() {
        mTabNames = getResources().getStringArray(R.array.other_contents_detail_tabs);
        //TODO:データプロバイダの初期化処理はここ
        mFragmentFactory = new TvPlayerFragmentFactory();
    }

    private void setTitle() {
        //TODO:コンテンツ詳細用タイトル設定
    }

    private TvPlayerBaseFragment getCurrentFragment() {
        int i = mViewPager.getCurrentItem();
        return mFragmentFactory.createFragment(i, this);
    }

    @Override
    public void onScroll(TvPlayerBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(TvPlayerBaseFragment fragment, AbsListView absListView, int scrollState) {

    }

    /**
     * Fragmentページャアダプター
     */
    class TvPlayerPagerAdapter extends FragmentStatePagerAdapter {

        public TvPlayerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
