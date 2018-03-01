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

/**
 * チャンネルリスト用フラグメント.
 */
public class ChannelListFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    /**
     * コールバックリスナー.
     */
    public interface ChannelListFragmentListener {
        /**
         * スクロール時のコールバック.
         *
         * @param fragment         fragment
         * @param absListView      absListView
         * @param firstVisibleItem firstVisibleItem
         * @param visibleItemCount visibleItemCount
         * @param totalItemCount   totalItemCount
         */
        void onScroll(ChannelListFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);

        /**
         * スクロール状態が変化した時のコールバック.
         *
         * @param fragment    fragment
         * @param absListView absListView
         * @param scrollState スクロール状態
         */
        void onScrollStateChanged(ChannelListFragment fragment, AbsListView absListView, int scrollState);

        /**
         * 表示状態/非表示状態の変化時のコールバック.
         *
         * @param isVisibleToUser true:表示 false:非表示
         * @param fragment        fragment
         */
        void setUserVisibleHint(boolean isVisibleToUser, ChannelListFragment fragment);
    }

    /**
     * コンテキスト.
     */
    private Context mContext;
    /**
     * 表示するデータ.
     */
    private List mData = null;
    /**
     * 表示するListView自体.
     */
    private ListView mListview;
    /**
     * データ読み込み中のView.
     */
    private View mLoadMoreView;
    /**
     * Listにセットするアダプタ.
     */
    private ChannelListAdapter mChannelListAdapter;
    /**
     * リストスクロールのコールバックリスナー.
     */
    private ChannelListFragmentListener mScrollListener;
    /**
     * データタイプ.
     */
    private ChannelListAdapter.ChListDataType mChListDataType;

    /**
     * コンストラクタ.
     */
    public ChannelListFragment() {
        mData = new ArrayList();
    }

    /**
     * データタイプを設定する.
     *
     * @param type データタイプ
     */
    public void setChListDataType(final ChannelListAdapter.ChListDataType type) {
        mChListDataType = type;
    }

    /**
     * データタイプを取得する.
     *
     * @return データタイプ
     */
    public ChannelListAdapter.ChListDataType getChListDataType() {
        return mChListDataType;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View mRootView;
        if (mData == null) {
            mData = new ArrayList();
        }
        mRootView = View.inflate(getActivity(), R.layout.channel_list_content, null);

        mLoadMoreView = View.inflate(getActivity(), R.layout.search_load_more, null);

        initContentListView(mRootView);
        return mRootView;
    }

    @Override
    public void onAttach(final Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mContext = null;
        super.onDetach();
    }

    /**
     * スクロールに対するリスナーを設定する.
     *
     * @param lis リスナー
     */
    public void setScrollListener(final ChannelListFragmentListener lis) {
        mScrollListener = lis;
    }

    /**
     * アダプタを取得する.
     *
     * @return アダプタ
     */
    public ChannelListAdapter getAdapter() {
        return mChannelListAdapter;
    }

    /**
     * Viewの初期化処理.
     * @param mRootView parentView
     */
    private void initContentListView(final View mRootView) {
        mListview = mRootView.findViewById(R.id.channel_list_content_body_lv);

        mListview.setOnScrollListener(this);
        mListview.setOnItemClickListener(this);

        mChannelListAdapter = new ChannelListAdapter(getContext(), mData, R.layout.channel_list_item);
        mListview.setAdapter(mChannelListAdapter);
    }

    /**
     * リストの更新を促す.
     */
    public void noticeRefresh() {
        if (null != mChannelListAdapter) {
            mChannelListAdapter.setChListDataType(mChListDataType);
            mChannelListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 追加読み込みViewの表示/非表示を行う.
     *
     * @param b true:表示する false:非表示にする
     */
    public void displayMoreData(final boolean b) {
        if (null != mListview) {
            if (b) {
                mListview.addFooterView(mLoadMoreView);
            } else {
                mListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * 表示するデータを設定する.
     *
     * @param item 表示するデータ
     */
    public void addData(final Object item) {
        if (mData == null) {
            mData = new ArrayList();
        }
        mData.add(item);
    }

    /**
     * 表示するデータを消去する.
     */
    public void clearDatas() {
        if (null != mData) {
            mData.clear();
        }
    }

    /**
     * 表示しているコンテンツ数を返却する.
     *
     * @return コンテンツ数
     */
    public int getDataCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    /**
     * 表示されているデータに、引数のデータが含まれているかどうか.
     *
     * @param item 表示されているか確認を行うデータ
     * @return true:含まれている false:含まれていない
     */
    public boolean hasData(final Object item) {
        if (null == item || null == mData || 0 == mData.size()) {
            return false;
        }
        boolean ret = false;
        if (mChListDataType != null) {
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                    if (item instanceof DlnaBsChListItem) {
                        DlnaBsChListItem bs2 = (DlnaBsChListItem) item;
                        for (Object obj : mData) {
                            if (obj instanceof DlnaBsChListItem) {
                                DlnaBsChListItem bs1 = (DlnaBsChListItem) obj;
                                ret = bs1.equalTo(bs2);
                                if (ret) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_TER:
                    if (item instanceof DlnaTerChListItem) {
                        DlnaTerChListItem ter2 = (DlnaTerChListItem) item;
                        for (Object obj : mData) {
                            if (obj instanceof DlnaTerChListItem) {
                                DlnaTerChListItem ter1 = (DlnaTerChListItem) obj;
                                ret = ter1.equalTo(ter2);
                                if (ret) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_HIKARI:
                case CH_LIST_DATA_TYPE_DTV:
                    if (item instanceof ChannelInfo) {
                        ChannelInfo ch2 = (ChannelInfo) item;
                        for (Object obj : mData) {
                            if (obj instanceof ChannelInfo) {
                                ChannelInfo ch1 = (ChannelInfo) obj;
                                ret = ch1.equalTo(ch2);
                                if (ret) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_INVALID:
                    return true;    //データを追加しない
            }
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
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        if (null == mData || i < 0) {
            return;
        }
        if (mLoadMoreView == view) {
            return;
        }
        if (mChListDataType != null) {
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_HIKARI:
                case CH_LIST_DATA_TYPE_DTV:
                    return;
                case CH_LIST_DATA_TYPE_BS:
                case CH_LIST_DATA_TYPE_TER:
                case CH_LIST_DATA_TYPE_INVALID:
                    break;
            }
        }
        RecordedContentsDetailData datas = getParcleData(i);
        if (null == datas) {
            return;
        }
        if (null != mContext) {
            Intent intent = new Intent(mContext, ContentDetailActivity.class);
            intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
            intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, datas);
            startActivity(intent);
        }
    }

    /**
     * タップされたコンテンツから、コンテンツ詳細画面に渡すデータを取得する.
     *
     * @param i タップされたリスト番号
     * @return 整形されたデータ
     */
    private RecordedContentsDetailData getParcleData(final int i) {
        RecordedContentsDetailData ret = new RecordedContentsDetailData();
        if (mChListDataType != null) {
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
        }
        return ret;
    }
}
