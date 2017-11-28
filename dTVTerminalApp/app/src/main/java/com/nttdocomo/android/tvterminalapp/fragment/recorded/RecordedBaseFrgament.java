/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

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
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.model.recommend.RecommendContentInfo;
import com.nttdocomo.android.tvterminalapp.model.search.SearchContentInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClassNameUtils;

import java.util.ArrayList;
import java.util.List;

public class RecordedBaseFrgament extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public Context mActivity;
    public List<ContentsData> mContentsData;
    private View mLoadMoreView;
    private ContentsAdapter mContentsAdapter = null;

    @Override
    public Context getContext() {
        DTVTLogger.start();
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        DTVTLogger.start();
        initData();
        return initView();
    }

    //モックデータ
    private void initData() {
        DTVTLogger.start();
        mContentsData = new ArrayList();
    }

    private View mRecordedFragmentView;
    private ListView mRecordedListview;

    public View initView() {
        DTVTLogger.start();
        if (null == mRecordedFragmentView) {
            mRecordedFragmentView = View.inflate(getActivity()
                    , R.layout.record_contents_list_layout, null);
            mRecordedListview = mRecordedFragmentView.findViewById(R.id.recorded_contents_result);

            mRecordedListview.setOnScrollListener(this);
            mRecordedListview.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mActivity).inflate(R.layout.search_load_more, null);
        }

        mContentsAdapter = new ContentsAdapter(getContext(),
                mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_RECORDED_LIST);
        mRecordedListview.setAdapter(mContentsAdapter);

        return mRecordedFragmentView;
    }

    public void notifyDataSetChanged() {
        DTVTLogger.start();
        if (null != mContentsAdapter) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    public void setSelection(int itemNo) {
        DTVTLogger.start();
        if (null != mRecordedListview) {
            mRecordedListview.setSelection(itemNo);
        }
    }

    public void displayLoadMore(boolean b) {
        DTVTLogger.start();
        if (null != mRecordedListview && null != mLoadMoreView) {
            if (b) {
                mRecordedListview.addFooterView(mLoadMoreView);
            } else {
                mRecordedListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void clear() {
        DTVTLogger.start();
        mContentsData.clear();
    }

    public List<ContentsData> getContentsData() {
        DTVTLogger.start();
        return mContentsData;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DlnaRecVideoItem videoItem = new DlnaRecVideoItem();
        Bundle args = new Bundle();
        args.putParcelable(RecordedListActivity.RECORD_LIST__KEY,
                getRecordedContentsDetailData(videoItem));
        if (null != mActivity) {
            ((BaseActivity) mActivity).startActivity(TvPlayerActivity.class, args);
        }
    }

    /**
     * コンテンツ詳細に必要なデータを返す
     *
     * @param videoItem レコメンド情報
     * @return
     */
    public RecordedContentsDetailData getRecordedContentsDetailData(DlnaRecVideoItem videoItem) {
        RecordedContentsDetailData detailData = new RecordedContentsDetailData();
        detailData.setBitrate(videoItem.mBitrate);
        detailData.setDuration(videoItem.mDuration);
        detailData.setResolution(videoItem.mResolution);
        detailData.setSize(videoItem.mSize);
        detailData.setThumbnail(videoItem.mThumbnail);
        return detailData;
    }
}