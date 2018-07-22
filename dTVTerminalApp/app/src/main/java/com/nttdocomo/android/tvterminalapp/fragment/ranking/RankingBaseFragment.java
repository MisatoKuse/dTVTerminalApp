/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * タブありランキング画面の各タブ画面を表示するためのフラグメント.
 */
public class RankingBaseFragment extends Fragment implements AdapterView.OnItemClickListener {
    /**
     * コンテキスト.
     */
    private Context mContext;
    /**
     * コンテンツ詳細データのリスト.
     */
    private List<ContentsData> mData = null;
    /**
     * 各タブのView.
     */
    private View mRankingFragmentView = null;
    /**
     * 各タブのリストView.
     */
    private ListView mRankingListView = null;
    /**
     * 各タブのProgressBar.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage = null;

    /**
     * リスト表示用アダプタ.
     */
    private ContentsAdapter mContentsAdapter;
    /**
     * ランキング種別.
     */
    private ContentsAdapter.ActivityTypeItem mRankingMode = null;

    /**
     * コンテンツ詳細表示フラグ.
     */
    private boolean mContentsDetailDisplay = false;

    /**
     * コンストラクタ.
     */
    public RankingBaseFragment() {
        mData = new ArrayList<>();
    }

    @Override
    public Context getContext() {
        this.mContext = getActivity();
        return mContext;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        //initData();//一時使うデータ
        DTVTLogger.debug("onCreateView");

        return initView();
    }

    /**
     * 各タブ画面は別々に実現して表示されること.
     *
     * @return 各タブ画面
     */
    private View initView() {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (null == mRankingFragmentView) {
            mRankingFragmentView = View.inflate(getActivity(),
                    R.layout.fragment_ranking_content, null);
            mRankingListView = mRankingFragmentView.findViewById(R.id.lv_ranking_list);
            mRelativeLayout = mRankingFragmentView.findViewById(R.id.lv_ranking_progress);
            mNoDataMessage = mRankingFragmentView.findViewById(R.id.ranking_no_items);

            mRankingListView.setOnItemClickListener(this);
        }
        if (mContentsAdapter == null) {
            initRankingView();
        }
        return mRankingFragmentView;
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    public void showProgressBar(final boolean showProgressBar) {
        DTVTLogger.start();
        //Viewが生成にActivityから直接呼ばれたとき用
        if (mRankingFragmentView == null) {
            return;
        }
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(getActivity())) {
                return;
            }
            mRankingListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
            showNoDataMessage(false, null);
        } else {
            mRankingListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
        DTVTLogger.end();
    }

    /**
     * リスト0件表示.
     *
     * @param showNoDataMessage プロセスバーを表示するかどうか
     * @param message 0件表示の文言
     */
    public void showNoDataMessage(final boolean showNoDataMessage, final String message) {
        DTVTLogger.start();
        if (mNoDataMessage == null) {
            return;
        }
        if (showNoDataMessage) {
            if (!TextUtils.isEmpty(message)) {
                mNoDataMessage.setText(message);
            }
            mNoDataMessage.setVisibility(View.VISIBLE);
        } else {
            mNoDataMessage.setVisibility(View.GONE);
        }
        DTVTLogger.end();
    }

    /**
     * 各ランキングページを判定.
     */
    private void initRankingView() {
        if (mRankingMode != null) {
            if (mRankingMode == ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK) {
                initWeeklyContentListView();
            } else if (mRankingMode == ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK) {
                initVideoContentListView();
            }
        }
    }

    /**
     * 各ランキングページを切り替え.
     *
     * @param rankingMode ランキング種別
     */
    public void switchRankingMode(final ContentsAdapter.ActivityTypeItem rankingMode) {
        mRankingMode = rankingMode;
        mContentsAdapter = null;
        if (mRankingListView != null) {
            initRankingView();
        }
    }

    /**
     * Adapterを取得.
     *
     * @return Adapter
     */
    public ContentsAdapter getRankingAdapter() {
        return mContentsAdapter;
    }

    /**
     * 週間ランキングコンテンツ初期化.
     */
    private void initWeeklyContentListView() {
        if (mData != null) {
            mContentsAdapter
                    = new ContentsAdapter(getContext(),
                    mData, ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK);
            mRankingListView.setAdapter(mContentsAdapter);
        }
    }

    /**
     * ビデオランキングコンテンツ初期化.
     */
    private void initVideoContentListView() {
        if (mData != null) {
            mContentsAdapter = new ContentsAdapter(getContext(),
                    mData, ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
            mRankingListView.setAdapter(mContentsAdapter);
        }
    }

    /**
     * Adapterをリフレッシュする.
     */
    public void noticeRefresh() {
        if (null != mContentsAdapter) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view,
                            final int position, final long l) {
        mContentsDetailDisplay = true;

        ContentsData contentsData = mData.get(position);
        BaseActivity baseActivity = (BaseActivity) mContext;
        if (ContentUtils.isChildContentList(contentsData)) {
            baseActivity.startChildContentListActivity(contentsData);
        } else {
            Intent intent = new Intent(mContext, ContentDetailActivity.class);
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
            OtherContentsDetailData detailData = DataConverter.getOtherContentsDetailData(contentsData, ContentUtils.PLALA_INFO_BUNDLE_KEY);
            intent.putExtra(detailData.getRecommendFlg(), detailData);
            baseActivity.startActivity(intent);
        }
    }

    /**
     * ContentsAdapterの通信を止める.
     */
    public void stopContentsAdapterCommunication() {
        DTVTLogger.start();
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        if (mContentsAdapter != null) {
            stopContentsAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mContentsAdapter);
        }
        showProgressBar(false);
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

    /**
     * Fragment経由でContentsAdapterを更新する.
     *
     * @param contentsDataList コンテンツリスト
     */
    public void updateContentsList(final List<ContentsData> contentsDataList) {
        mContentsAdapter.setListData(contentsDataList);
        noticeRefresh();
    }

    /**
     * コンテンツデータ取得.
     *
     * @return コンテンツリスト
     */
    public List<ContentsData> getData() {
        return mData;
    }

    /**
     * コンテンツ数取得.
     *
     * @return コンテンツ数
     */
    public int getDataSize() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    /**
     * コンテンツデータ追加.
     *
     * @param content コンテンツデータ
     */
    public void addData(final ContentsData content) {
        if (mData != null) {
            mData.add(content);
        }
    }

    /**
     * コンテンツデータクリア.
     */
    public void clearData() {
        if (mData != null) {
            mData.clear();
        }
    }

    /**
     * コンテンツ詳細表示フラグ取得.
     *
     * @return コンテンツ詳細表示フラグ
     */
    public boolean isContentsDetailDisplay() {
        return mContentsDetailDisplay;
    }

    /**
     * コンテンツ詳細表示フラグ設定.
     *
     * @param contentsDetailDisplay コンテンツ詳細表示フラグ
     */
    public void setContentsDetailDisplay(final boolean contentsDetailDisplay) {
        this.mContentsDetailDisplay = contentsDetailDisplay;
    }

    /**
     * リスト表示用アダプタ取得.
     *
     * @return リスト表示用アダプタ
     */
    public ContentsAdapter getContentsAdapter() {
        return mContentsAdapter;
    }
}