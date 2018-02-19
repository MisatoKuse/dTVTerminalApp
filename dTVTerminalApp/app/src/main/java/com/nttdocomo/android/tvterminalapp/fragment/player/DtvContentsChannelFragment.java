/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.List;

public class DtvContentsChannelFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private View mView = null;
    private ListView mChannelListView = null;
    private ContentsAdapter mContentsAdapter = null;
    public List<ContentsData> mContentsData;
    private Activity mActivity;
    private ImageView mChannelImg;
    private TextView mChannelTxt;
    private boolean mIsLoading;
    private ChangedScrollLoadListener mChangedScrollLoadListener;
    private View mHeaderView;
    private View mFootView;


    /**
     * コールバックリスナー.
     */
    public interface ChangedScrollLoadListener {

        /**
         * スクロール時のコールバック.
         */
        void onChannelLoadMore();

        /**
         * Fragment見えるのコールバック.
         */
        void onUserVisibleHint();
    }

    public void setScrollCallBack(ChangedScrollLoadListener mChangedScrollLoadListener){
        this.mChangedScrollLoadListener = mChangedScrollLoadListener;
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(container);
    }

    /**
     * コンテンツ詳細画面チャンネルタブ表示されること.
     *
     * @param container コンテナ
     * @return view ビュー
     */
    private View initView(final ViewGroup container) {
        DTVTLogger.start();
        if (null == mView) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dtv_contents_channel_fragment, container, false);
        }
        if(mContentsData == null){
            mContentsData = new ArrayList<>();
        }
        mChannelListView = mView.findViewById(R.id.dtv_contents_channel_list);
        mChannelListView.setOnScrollListener(this);
        mChannelListView.setOnItemClickListener(this);
        if (null == mFootView) {
            mFootView = View.inflate(getContext(), R.layout.search_load_more, null);
        }
        if(null == mHeaderView){
            mHeaderView = View.inflate(getContext(), R.layout.dtv_contents_channel_fragment_header, null);
            mHeaderView.setOnClickListener(null);
        }
        mContentsAdapter = new ContentsAdapter(getContext(), mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST);
        mChannelListView.setAdapter(mContentsAdapter);
        mChannelImg = mHeaderView.findViewById(R.id.dtv_contents_channel_header_img);
        mChannelTxt = mHeaderView.findViewById(R.id.dtv_contents_channel_header_name);
        mChannelListView.addHeaderView(mHeaderView);
        DTVTLogger.end();
        return mView;
    }

    public void setChannelDataChanged(ChannelInfo info){
        if(!TextUtils.isEmpty(info.getTitle())){
            mChannelTxt.setText(info.getTitle());
        }
        if (!TextUtils.isEmpty(info.getThumbnail())) {
            ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(getContext());
            mChannelImg.setTag(info.getThumbnail());
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mChannelImg, info.getThumbnail());
            if (bitmap != null) {
                mChannelImg.setImageBitmap(bitmap);
            }
        }
    }

    public void setNotifyDataChanged(){
        mContentsAdapter.notifyDataSetChanged();
        loadComplete();
    }

    public void loadComplete() {
        mChannelListView.setVisibility(View.VISIBLE);
        mChannelListView.removeFooterView(mFootView);
        mIsLoading = false;
    }

    public void initLoad() {
        mChannelListView.setVisibility(View.GONE);
        mChannelListView.setSelection(0);
    }

    private void loadStart() {
        mChannelListView.addFooterView(mFootView);
        mChannelListView.setSelection(mChannelListView.getMaxScrollAmount());
        mFootView.setVisibility(View.VISIBLE);
        mIsLoading = true;
        if(mChangedScrollLoadListener != null){
            mChangedScrollLoadListener.onChannelLoadMore();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        DTVTLogger.start();
        if(!isVisibleToUser){
            mChangedScrollLoadListener.onUserVisibleHint();
        }
        DTVTLogger.end();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (!mIsLoading && scrollState == SCROLL_STATE_IDLE && absListView.getLastVisiblePosition() ==
                mChannelListView.getAdapter().getCount() - 1) {
            loadStart();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.setClass(mActivity, ContentDetailActivity.class);
        startActivity(intent);
    }

    /**
     * ContentsAdapterの通信を止める.
     */
    public void stopContentsAdapterCommunication() {
        DTVTLogger.start();
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        if (mContentsAdapter != null) {
            stopContentsAdapterConnect.execute(mContentsAdapter);
        }
    }

    /**
     * ContentsAdapterで止めた通信を再度可能な状態にする.
     */
    public void enableContentsAdapterCommunication() {
        DTVTLogger.start();
        if (mContentsAdapter != null) {
            mContentsAdapter.enableConnect();
        }
    }
}