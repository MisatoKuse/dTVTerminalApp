/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.adapter.RankingAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipContentInfo;

import java.util.ArrayList;
import java.util.List;


public class RankingBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public Context mActivity;
    public List mData;

    private View mRankingFragmentView;
    private ListView mRankingListView;

    private RankingAdapter mRankingAdapter;
    private RankingFragmentScrollListener mRankingBaseFragmentScrollListener=null;

    private View mLoadMoreView;


    public RankingBaseFragment() {
        if(mData == null){
            mData = new ArrayList();
        }
    }
    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initData();//一時使うデータ
        return initView();
    }

    /**
     * 各タブ画面は別々に実現して表示されること
     */
    private View initView(){
        if(mData == null){
            mData = new ArrayList();
        }
        if (null == mRankingFragmentView) {
            mRankingFragmentView = View.inflate(getActivity()
                    , R.layout.fragment_ranking_content, null);
            mRankingListView = mRankingFragmentView.findViewById(R.id.lv_ranking_list);

            mRankingListView.setOnScrollListener(this);
            mRankingListView.setOnItemClickListener(this);

        }
        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.search_load_more, null);

        return mRankingFragmentView;
    }



    public void setClipListBaseFragmentScrollListener(RankingFragmentScrollListener lis){
        mRankingBaseFragmentScrollListener = lis;
    }

    /**
     * 各ランキングページを判定
     */
    public View initRankingView(int rankingMode){
        switch (rankingMode) {
            case RankingConstants.RankingModeNo.RANKING_MODE_NO_OF_WEEKLY : // 週間
                initWeeklyContentListView();
                break;
            case RankingConstants.RankingModeNo.RANKING_MODE_NO_OF_VIDEO : // ビデオ
                break;
            default:
                break;
        }
        return mRankingFragmentView;
    }

    public RankingAdapter getClipMainAdapter(){
        return mRankingAdapter;
    }

    private View initLoadingContentView() {
        View defaultView = View.inflate(getActivity(), R.layout.clip_list_default_loading_view, null);
        return defaultView;
    }



    /**
     *  週間ランキングコンテンツ初期化
     */
    private void initWeeklyContentListView() {

        ThumbnailProvider thumbnailProvider = new ThumbnailProvider(getActivity());
        mRankingAdapter
                = new RankingAdapter(getContext()
                ,mData, R.layout.item_ranking_list,thumbnailProvider);
        mRankingListView.setAdapter(mRankingAdapter);
    }

    public void noticeRefresh(){
        if(null!= mRankingAdapter){
            mRankingAdapter.notifyDataSetChanged();
        }
    }

    public void displayMoreData(boolean b) {
        if(null!= mRankingListView){
            if(b){
                mRankingListView.addFooterView(mLoadMoreView);
            } else {
                mRankingListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void setMode(int mode){
        if(null!= mRankingAdapter){
            mRankingAdapter.setMode(mode);
            mData.clear();
        }
    }


    public void setDataWeekly(VodClipContentInfo.VodClipContentInfoItem clipContentInfoItem) {
        if(mData == null){
            mData = new ArrayList();
        }
        mData.add(clipContentInfoItem);
    }

    public void setDataVd() {
        // TODO ビデオランキングの値を入れる
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if(null!=mRankingBaseFragmentScrollListener){
            mRankingBaseFragmentScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(null!=mRankingBaseFragmentScrollListener){
            mRankingBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(mLoadMoreView == view){
            return;
        }
        ((BaseActivity)mActivity).startActivity(TvPlayerActivity.class, null);
    }
}
