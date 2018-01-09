/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

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
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.RecommendListBaseAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;

import java.util.ArrayList;
import java.util.List;

public class RecommendBaseFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener {

    public Context mActivity = null;
    public List<ContentsData> mData = null;
    private View mLoadMoreView = null;
    private View mRecommendFragmentView = null;
    private ListView mRecommendListview = null;
    private RecommendListBaseAdapter mRecommendListBaseAdapter = null;
    private RecommendBaseFragmentScrollListener mRecommendBaseFragmentScrollListener = null;

    public void setRecommendBaseFragmentScrollListener(RecommendBaseFragmentScrollListener lis) {
        mRecommendBaseFragmentScrollListener = lis;
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        initData();
        return initView();
    }

    //モックデータ
    private void initData() {
        mData = new ArrayList();
    }

    /**
     * Viewの初期設定
     *
     * @return
     */
    public View initView() {
        if (null == mRecommendFragmentView) {
            mRecommendFragmentView = View.inflate(getActivity()
                    , R.layout.fragment_recommend_content, null);
            mRecommendListview = mRecommendFragmentView.findViewById(R.id.lv_recommend_list);

            mRecommendListview.setOnScrollListener(this);
            mRecommendListview.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mActivity).inflate(R.layout.search_load_more, null);
        }

        //SearchResultBaseAdapter searchResultBaseAdapter
        mRecommendListBaseAdapter =
                new RecommendListBaseAdapter(getContext(), mData, R.layout.item_recommend_list);
        mRecommendListview.setAdapter(mRecommendListBaseAdapter);

        return mRecommendFragmentView;
    }

    /**
     * データの更新
     */
    public void notifyDataSetChanged() {
        if (null != mRecommendListBaseAdapter) {
            mRecommendListBaseAdapter.notifyDataSetChanged();
        }
    }

    /**
     * リストのカーソルを移動
     *
     * @param itemNo
     */
    public void setSelection(int itemNo) {
        if (null != mRecommendListview) {
            mRecommendListview.setSelection(itemNo);
        }
    }

    /**
     * リストの最後に更新中の行を追加または追加した行を削除する
     *
     * @param loadFlag
     */
    public void displayLoadMore(boolean loadFlag) {
        if (null != mRecommendListview && null != mLoadMoreView) {
            if (loadFlag) {
                mRecommendListview.addFooterView(mLoadMoreView);
            } else {
                mRecommendListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void clear() {
        mData.clear();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView,
                         int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mRecommendBaseFragmentScrollListener) {
            mRecommendBaseFragmentScrollListener.onScroll(this, absListView,
                    firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ContentsData info = mData.get(i);
        Intent intent = new Intent(mActivity, DtvContentsDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
        intent.putExtra(DtvContentsDetailActivity.RECOMMEND_INFO_BUNDLE_KEY,
                getOtherContentsDetailData(info));
        startActivity(intent);
    }

    /**
     * コンテンツ詳細に必要なデータを返す
     *
     * @param info レコメンド情報
     * @return コンテンツ情報
     */
    public OtherContentsDetailData getOtherContentsDetailData(ContentsData info) {
        OtherContentsDetailData detailData = new OtherContentsDetailData();
        detailData.setTitle(info.getTitle());
        detailData.setThumb(info.getThumURL());
        detailData.setDetail(info.getSynop());
        detailData.setComment(info.getComment());
        detailData.setHighlight(info.getHighlight());
        detailData.setServiceId(Integer.parseInt(info.getServiceId()));
        detailData.setReserved4(info.getReserved());

        //コンテンツIDの受け渡しを追加
        detailData.setContentId(info.getContentsId());
        detailData.setChannelId(info.getChannelId());
        detailData.setRecommendOrder(info.getRecommendOrder());
        detailData.setPageId(info.getPageId());
        detailData.setGroupId(info.getGroupId());
        detailData.setRecommendMethodId(info.getRecommendMethodId());
        detailData.setCategoryId(info.getCategoryId());

        return detailData;
    }
}