/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ClipList;

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
import com.nttdocomo.android.tvterminalapp.adapter.ClipMainAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipContentInfo;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipContentInfo;

import java.util.ArrayList;
import java.util.List;


public class ClipListBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public Context mActivity;
    public List mData;

    private View mTeleviFragmentView;
    private ListView mTeveviListview;

    private ClipMainAdapter mClipMainAdapter;

    public ClipListBaseFragment(){
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

    private ClipListBaseFragmentScrollListener mClipListBaseFragmentScrollListener=null;

    public void setClipListBaseFragmentScrollListener(ClipListBaseFragmentScrollListener lis){
        mClipListBaseFragmentScrollListener=lis;
    }

    /**
     * 各タブ画面は別々に実現して表示されること
     */
    private View initView(){
        if(mData == null){
            mData = new ArrayList();
        }
        mTeleviFragmentView = View.inflate(getActivity()
                , R.layout.fragment_clip_list_item_content, null);

        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.search_load_more, null);

        /*
        if (mData.size() != 0) {
            initContentListView();
        } else {
            initLoadingContentView();
        }
        */
        initContentListView();
        return mTeleviFragmentView;
    }

    public ClipMainAdapter getClipMainAdapter(){
        return mClipMainAdapter;
    }

    private View initLoadingContentView() {
        View defaultView = View.inflate(getActivity(), R.layout.clip_list_default_loading_view, null);
        return defaultView;
    }

    /*テレビタブコンテンツ初期化*/
    private void initContentListView() {
        mTeveviListview = mTeleviFragmentView
                .findViewById(R.id.clip_list_lv_searched_result);

        mTeveviListview.setOnScrollListener(this);
        mTeveviListview.setOnItemClickListener(this);

        ThumbnailProvider thumbnailProvider = new ThumbnailProvider(getActivity());
        mClipMainAdapter
                = new ClipMainAdapter(getContext()
                ,mData, R.layout.item_clip_list_dvideo,thumbnailProvider);
        mTeveviListview.setAdapter(mClipMainAdapter);
    }

    public void noticeRefresh(){
        if(null!=mClipMainAdapter){
            mClipMainAdapter.notifyDataSetChanged();
        }
    }

    private View mLoadMoreView;

    public void displayMoreData(boolean b) {
        if(null!=mTeveviListview && null!=mTeveviListview){
            if(b){
                mTeveviListview.addFooterView(mLoadMoreView);
            } else {
                mTeveviListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void setMode(ClipMainAdapter.Mode mode){
        if(null!=mClipMainAdapter){
            mClipMainAdapter.setMode(mode);
            mData.clear();
            /*
            ClipMainAdapter.Mode old= mClipMainAdapter.getMode();
            if(old!=mode){
                mData.clear();
            }
            */
        }
    }

    /*
    public void clearAllDatas(){
        if(null!=mData){
            mData.clear();
        }
    }
    */

    public void setDataVod(VodClipContentInfo.VodClipContentInfoItem clipContentInfoItem) {
        if(mData == null){
            mData = new ArrayList();
        }
        mData.add(clipContentInfoItem);
    }

    public void setDataTv(TvClipContentInfo.TvClipContentInfoItem clipContentInfoItem) {
        if(mData == null){
            mData = new ArrayList();
        }
        mData.add(clipContentInfoItem);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if(null!=mClipListBaseFragmentScrollListener){
            mClipListBaseFragmentScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(null!=mClipListBaseFragmentScrollListener){
            mClipListBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
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
