/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

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
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChannelListFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public interface ChannelListFragmentListener {
        public void onScroll(ChannelListFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
        public void onScrollStateChanged(ChannelListFragment fragment, AbsListView absListView, int scrollState);
        public void setUserVisibleHint(boolean isVisibleToUser, ChannelListFragment fragment);
    }

    private Context mActivity;
    private List mData;
    private View mRootView;
    private ListView mListview;
    private ChannelListAdapter mChannelListAdapter;
    private ChannelListFragmentListener mScrollListener;
    private ChannelListAdapter.ChListDataType mChListDataType;

    public ChannelListFragment(){
        if(mData == null){
            mData = new ArrayList();
        }
    }

    public void setChListDataType(ChannelListAdapter.ChListDataType type){
        mChListDataType= type;
    }

    public ChannelListAdapter.ChListDataType getChListDataType(){
        return mChListDataType;
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mData == null){
            mData = new ArrayList();
        }
        mRootView = View.inflate(getActivity(), R.layout.channel_list_content, null);

        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.search_load_more, null);

        initContentListView();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        mActivity=context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mActivity=null;
        super.onDetach();
    }

    public void setScrollListener(ChannelListFragmentListener lis){
        mScrollListener =lis;
    }

    public ChannelListAdapter getAdapter(){
        return mChannelListAdapter;
    }

    private void initContentListView() {
        mListview = mRootView.findViewById(R.id.channel_list_content_body_lv);

        mListview.setOnScrollListener(this);
        mListview.setOnItemClickListener(this);

        mChannelListAdapter = new ChannelListAdapter(getContext(), mData, R.layout.channel_list_item);
        mListview.setAdapter(mChannelListAdapter);
    }

    public void noticeRefresh(){
        if(null!=mChannelListAdapter){
            mChannelListAdapter.setChListDataType(mChListDataType);
            mChannelListAdapter.notifyDataSetChanged();
        }
    }

    private View mLoadMoreView;

    public void displayMoreData(boolean b) {
        if(null!= mListview){
            if(b){
                mListview.addFooterView(mLoadMoreView);
            } else {
                mListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void addData(Object item) {
        if(mData == null){
            mData = new ArrayList();
        }
        mData.add(item);
    }

    public void clearDatas(){
        if(null!=mData){
            mData.clear();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(null!= mScrollListener){
            mScrollListener.setUserVisibleHint(isVisibleToUser, this);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if(null!= mScrollListener){
            mScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(null!= mScrollListener){
            mScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
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
