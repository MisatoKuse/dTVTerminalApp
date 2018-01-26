/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListItem;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

public class ChannelListFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public interface ChannelListFragmentListener {
        void onScroll(ChannelListFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);

        void onScrollStateChanged(ChannelListFragment fragment, AbsListView absListView, int scrollState);

        void setUserVisibleHint(boolean isVisibleToUser, ChannelListFragment fragment);
    }

    private Context mActivity;
    private List mData = null;
    private View mRootView;
    private ListView mListview;
    private ChannelListAdapter mChannelListAdapter;
    private ChannelListFragmentListener mScrollListener;
    private ChannelListAdapter.ChListDataType mChListDataType;

    public ChannelListFragment() {
        mData = new ArrayList();
    }

    public void setChListDataType(final ChannelListAdapter.ChListDataType type) {
        mChListDataType = type;
    }

    public ChannelListAdapter.ChListDataType getChListDataType() {
        return mChListDataType;
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (mData == null) {
            mData = new ArrayList();
        }
        mRootView = View.inflate(getActivity(), R.layout.channel_list_content, null);

        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.search_load_more, null);

        initContentListView();
        return mRootView;
    }

    @Override
    public void onAttach(final Context context) {
        mActivity = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public void setScrollListener(final ChannelListFragmentListener lis) {
        mScrollListener = lis;
    }

    public ChannelListAdapter getAdapter() {
        return mChannelListAdapter;
    }

    private void initContentListView() {
        mListview = mRootView.findViewById(R.id.channel_list_content_body_lv);

        mListview.setOnScrollListener(this);
        mListview.setOnItemClickListener(this);

        mChannelListAdapter = new ChannelListAdapter(getContext(), mData, R.layout.channel_list_item);
        mListview.setAdapter(mChannelListAdapter);
    }

    public void noticeRefresh() {
        if (null != mChannelListAdapter) {
            mChannelListAdapter.setChListDataType(mChListDataType);
            mChannelListAdapter.notifyDataSetChanged();
        }
    }

    private View mLoadMoreView;

    public void displayMoreData(final boolean b) {
        if (null != mListview) {
            if (b) {
                mListview.addFooterView(mLoadMoreView);
            } else {
                mListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void addData(final Object item) {
        if (mData == null) {
            mData = new ArrayList();
        }
        mData.add(item);
    }

    public void clearDatas() {
        if (null != mData) {
            mData.clear();
        }
    }

    public int getDataCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public boolean hasData(final Object item) {
        if (null == item || null == mData || 0 == mData.size()) {
            return false;
        }
        boolean ret = false;
        switch (mChListDataType) {
            case CH_LIST_DATA_TYPE_BS:
                DlnaBsChListItem bs2 = (DlnaBsChListItem) item;
                for (Object obj : mData) {
                    DlnaBsChListItem bs1 = (DlnaBsChListItem) obj;
                    ret = bs1.equalTo(bs2);
                    if (ret) {
                        break;
                    }
                }
                break;
            case CH_LIST_DATA_TYPE_TER:
                DlnaTerChListItem ter2 = (DlnaTerChListItem) item;
                for (Object obj : mData) {
                    DlnaTerChListItem ter1 = (DlnaTerChListItem) obj;
                    ret = ter1.equalTo(ter2);
                    if (ret) {
                        break;
                    }
                }
                break;
            case CH_LIST_DATA_TYPE_HIKARI:
            case CH_LIST_DATA_TYPE_DTV:
                ChannelInfo ch2 = (ChannelInfo) item;
                for (Object obj : mData) {
                    ChannelInfo ch1 = (ChannelInfo) obj;
                    ret = ch1.equalTo(ch2);
                    if (ret) {
                        break;
                    }
                }
                break;
            case CH_LIST_DATA_TYPE_INVALID:
                return true;    //データを追加しない
        }
        return ret;
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        if (null != mScrollListener) {
            mScrollListener.setUserVisibleHint(isVisibleToUser, this);
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, View view, final int i, final long l) {
        if (null == mData || i < 0) {
            return;
        }
        if (mLoadMoreView == view) {
            return;
        }
        switch (mChListDataType) {
            case CH_LIST_DATA_TYPE_HIKARI:
            case CH_LIST_DATA_TYPE_DTV:
                return;
            case CH_LIST_DATA_TYPE_BS:
            case CH_LIST_DATA_TYPE_TER:
            case CH_LIST_DATA_TYPE_INVALID:
                break;
        }
        RecordedContentsDetailData datas = getParcleData(i);
        if (null == datas) {
            return;
        }
        if (null != mActivity) {
            Intent intent = new Intent(mActivity, ContentDetailActivity.class);
            intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
            intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, datas);
            startActivity(intent);
        }
    }

    private RecordedContentsDetailData getParcleData(final int i) {
        RecordedContentsDetailData ret = new RecordedContentsDetailData();
        switch (mChListDataType) {
            case CH_LIST_DATA_TYPE_HIKARI:
            case CH_LIST_DATA_TYPE_DTV:
                return null;
            case CH_LIST_DATA_TYPE_BS:
                DlnaBsChListItem bsI = (DlnaBsChListItem) mData.get(i);
                ret.setUpnpIcon(null);
                ret.setSize(bsI.mSize);
                ret.setResUrl(bsI.mResUrl);
                ret.setResolution(bsI.mResolution);
                ret.setBitrate(bsI.mBitrate);
                ret.setDuration(bsI.mDuration);
                ret.setTitle(bsI.mTitle);
                ret.setDetailParamFromWhere(RecordedContentsDetailData.DetailParamFromWhere.DetailParamFromWhere_ChList_TabBs);
                ret.setVideoType(bsI.mVideoType);
                break;
            case CH_LIST_DATA_TYPE_TER:
                DlnaTerChListItem bsT = (DlnaTerChListItem) mData.get(i);
                ret.setUpnpIcon(null);
                ret.setSize(bsT.mSize);
                ret.setResUrl(bsT.mResUrl);
                ret.setResolution(bsT.mResolution);
                ret.setBitrate(bsT.mBitrate);
                ret.setDuration(bsT.mDuration);
                ret.setTitle(bsT.mTitle);
                ret.setDetailParamFromWhere(RecordedContentsDetailData.DetailParamFromWhere.DetailParamFromWhere_ChList_TabTer);
                ret.setVideoType(bsT.mVideoType);
                break;
            case CH_LIST_DATA_TYPE_INVALID:
                return null;
        }
        return ret;
    }


}
