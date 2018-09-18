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
    /** 宅内、宅外区分.*/
    private boolean mIsRemote;

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
        showProgressBar(true);

        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(this);

        mChannelListAdapter = new ChannelListAdapter(getContext(), mData);
        mListView.setAdapter(mChannelListAdapter);
    }

    /**
     * ローディング処理（ページング）.
     */
    public void loadComplete() {
        mListView.removeFooterView(mFootView);
    }

    /**
     * ローディング開始.
     */
    public void loadStart() {
        mListView.addFooterView(mFootView);
        mListView.setSelection(mListView.getMaxScrollAmount());
        mFootView.setVisibility(View.VISIBLE);
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
     * 最新の接続ステータス取得.
     * @return true:宅外　false:宅内
     */
    public boolean getConnectionStatus() {
        return StbConnectionManager.shared().getConnectionStatus() != StbConnectionManager.ConnectionStatus.HOME_IN;
    }

    /**
     * 元の接続ステータス取得.getConnectionStatusとは違い、元々動作していた際のステータスを返却する(変化があるかの確認用).
     * @return true:宅外　false:宅内
     */
    public boolean isRemote() {
        return mIsRemote;
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
        mIsRemote = getConnectionStatus();
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
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i,
                            final long l) {
        if (mOnClickChannelItemListener != null) {
            //フラグメントも伝えるように変更
            mOnClickChannelItemListener.onClickChannelItem(i, mChannelListDataType, this);
        }
        if (null == mData || i < 0) {
            return;
        }

        RecordedContentsDetailData datas = getParcleData(i);
        if (null == datas) {
            return;
        }
        if (null != mContext) {
            Intent intent = new Intent(mContext, ContentDetailActivity.class);
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
            intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, datas);
            ChannelListActivity channelListActivity = (ChannelListActivity) mContext;
            channelListActivity.startActivity(intent);
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
        if (mChannelListDataType != null) {
            switch (mChannelListDataType) {
                case CH_LIST_DATA_TYPE_HIKARI:
                case CH_LIST_DATA_TYPE_DCH:
                    return null;
                case CH_LIST_DATA_TYPE_BS:
                    DlnaObject bsI = (DlnaObject) mData.get(i);
                    ret.setUpnpIcon(null);
                    ret.setSize(bsI.mSize);
                    ret.setResUrl(bsI.mResUrl);
                    ret.setResolution(bsI.mResolution);
                    ret.setBitrate(bsI.mBitrate);
                    ret.setDuration(bsI.mDuration);
                    ret.setTitle(bsI.mChannelName);
                    ret.setDetailParamFromWhere(RecordedContentsDetailData.DetailParamFromWhere.DetailParamFromWhere_ChList_TabBs);
                    ret.setVideoType(bsI.mVideoType);
                    ret.setIsLive(true);
                    ret.setIsRemote(StbConnectionManager.shared().getConnectionStatus() != StbConnectionManager.ConnectionStatus.HOME_IN);
                    break;
                case CH_LIST_DATA_TYPE_TDB:
                    DlnaObject bsT = (DlnaObject) mData.get(i);
                    ret.setUpnpIcon(null);
                    ret.setSize(bsT.mSize);
                    ret.setResUrl(bsT.mResUrl);
                    ret.setResolution(bsT.mResolution);
                    ret.setBitrate(bsT.mBitrate);
                    ret.setDuration(bsT.mDuration);
                    ret.setTitle(bsT.mChannelName);
                    ret.setDetailParamFromWhere(RecordedContentsDetailData.DetailParamFromWhere.DetailParamFromWhere_ChList_TabTer);
                    ret.setVideoType(bsT.mVideoType);
                    ret.setIsLive(true);
                    ret.setIsRemote(StbConnectionManager.shared().getConnectionStatus() != StbConnectionManager.ConnectionStatus.HOME_IN);
                    break;
                default:
                    return null;
            }
        }
        return ret;
    }
}
