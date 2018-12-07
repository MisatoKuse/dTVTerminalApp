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
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * チャンネルリスト用フラグメント.
 */
public class ChannelListFragment extends Fragment implements AdapterView.OnItemClickListener {

    /**
     * コールバックリスナー.
     */
    public interface ChannelListFragmentListener {

        /**
         * 表示状態/非表示状態の変化時のコールバック.
         *
         * @param isVisibleToUser true:表示 false:非表示
         * @param fragment        fragment
         */
        void setUserVisibleHint(boolean isVisibleToUser, ChannelListFragment fragment);
    }

    /**
     * callback.
     */
    public interface OnClickChannelItemListener {
        /**
         * チャンネルがタップされた場合のコールバック.
         *
         * @param pos タップされたポジション.
         * @param type チャンネルリストのタイプ
         * @param fragment タップされた時のフラグメント
         */
        void onClickChannelItem(int pos, ChannelListActivity.ChannelListDataType type,
                                ChannelListFragment fragment);
    }
    /**コンテキスト.*/
    private Context mContext;
    /** 表示するデータ.*/
    private List mData = null;
    /** 親ビュー.*/
    private View mRootView = null;
    /** 表示するListView自体.*/
    private ListView mListView;
    /** 表示するProgressDialog.*/
    private RelativeLayout mRelativeLayout;
    /** Listにセットするアダプタ.*/
    private ChannelListAdapter mChannelListAdapter;
    /** リストスクロールのコールバックリスナー.*/
    private ChannelListFragmentListener mScrollListener;
    /** データタイプ.*/
    private ChannelListActivity.ChannelListDataType mChannelListDataType;
    /** リスナー.*/
    private OnClickChannelItemListener mOnClickChannelItemListener;
    /** フッタービュー.*/
    private View mFootView;
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
    public void setChListDataType(final ChannelListActivity.ChannelListDataType type) {
        mChannelListDataType = type;
    }

    /**
     * リスナー設定.
     * @param listener listener
     */
    public void setClickItemListener(final OnClickChannelItemListener listener) {
        mOnClickChannelItemListener = listener;
    }

    /**
     * データタイプを取得する.
     *
     * @return データタイプ
     */
    public ChannelListActivity.ChannelListDataType getChListDataType() {
        return mChannelListDataType;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (mData == null) {
            mData = new ArrayList();
        }
        mRootView = View.inflate(getActivity(), R.layout.channel_list_content, null);
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
     * @param rootView parentView
     */
    private void initContentListView(final View rootView) {
        mListView = rootView.findViewById(R.id.channel_list_content_body_lv);
        mRelativeLayout = rootView.findViewById(R.id.channel_list_progress);
        if (null == mFootView) {
            mFootView = View.inflate(getContext(), R.layout.search_load_more, null);
        }
        if (getDataCount() > 0) {
            showProgressBar(false);
        } else {
            showProgressBar(true);
        }

        mListView.setOnItemClickListener(this);

        mChannelListAdapter = new ChannelListAdapter(getContext(), mData);
        mListView.setAdapter(mChannelListAdapter);
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    public void showProgressBar(final boolean showProgressBar) {
        DTVTLogger.start("showProgressBar = " + showProgressBar);
        //Activityから直接呼び出されるためNullチェック
        if (mRootView == null) {
            return;
        }

        DTVTLogger.debug("mRootView = " + mRootView);
        mListView = mRootView.findViewById(R.id.channel_list_content_body_lv);
        mRelativeLayout = mRootView.findViewById(R.id.channel_list_progress);
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(getActivity())) {
                return;
            }
            mListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }

        DTVTLogger.end();
    }

    /**
     * リストの更新を促す.
     */
    public void noticeRefresh() {
        if (null != mChannelListAdapter) {
            mChannelListAdapter.setChannelListDataType(mChannelListDataType);
            mChannelListAdapter.resetMaxItemCount();
            mChannelListAdapter.notifyDataSetChanged();
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

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        if (null != mScrollListener) {
            mScrollListener.setUserVisibleHint(isVisibleToUser, this);
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i,
                            final long l) {
        if (mOnClickChannelItemListener != null) {
            //フラグメントも伝えるように変更
            mOnClickChannelItemListener.onClickChannelItem(i, mChannelListDataType, this);
        }
    }

}
