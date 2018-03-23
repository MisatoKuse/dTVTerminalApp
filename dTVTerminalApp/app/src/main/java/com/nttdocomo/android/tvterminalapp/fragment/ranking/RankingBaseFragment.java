/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

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
    public List<ContentsData> mData = null;
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
     * リスト表示用アダプタ.
     */
    public ContentsAdapter mContentsAdapter;
    /**
     * ランキング種別.
     */
    private ContentsAdapter.ActivityTypeItem mRankingMode;
    /**
     * コンテンツ詳細表示フラグ.
     */
    public boolean mContentsDetailDisplay = false;

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

        return initView(container);
    }

    /**
     * 各タブ画面は別々に実現して表示されること.
     *
     * @return 各タブ画面
     */
    private View initView(final ViewGroup container) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (null == mRankingFragmentView) {
            mRankingFragmentView = View.inflate(getActivity(),
                    R.layout.fragment_ranking_content, null);
            mRankingListView = mRankingFragmentView.findViewById(R.id.lv_ranking_list);
            mRelativeLayout = mRankingFragmentView.findViewById(R.id.lv_ranking_progress);

            mRankingListView.setOnItemClickListener(this);
        }
        if (mRankingListView != null && mRelativeLayout != null) {
            showProgressBar(true);
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
    private void showProgressBar(final boolean showProgressBar) {
        mRankingListView = mRankingFragmentView.findViewById(R.id.lv_ranking_list);
        mRelativeLayout = mRankingFragmentView.findViewById(R.id.lv_ranking_progress);
        if (showProgressBar) {
            mRankingListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mRankingListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 各ランキングページを判定.
     */
    private void initRankingView() {
        if (ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK.equals(mRankingMode)) { // 週間
            initWeeklyContentListView();
        } else if (ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK.equals(mRankingMode)) { // ビデオ
            initVideoContentListView();
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
            showProgressBar(false);
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view,
                            final int position, final long l) {
        Intent intent = new Intent(mContext, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
        OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(mData.get(position), ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        mContentsDetailDisplay = true;
        startActivity(intent);
    }

    /**
     * ContentsAdapterの通信を止める.
     */
    public void stopContentsAdapterCommunication() {
        DTVTLogger.start();
        showProgressBar(false);
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
            showProgressBar(true);
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
}