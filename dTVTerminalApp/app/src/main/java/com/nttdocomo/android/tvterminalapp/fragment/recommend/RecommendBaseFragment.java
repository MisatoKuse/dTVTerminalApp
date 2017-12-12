/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

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
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.RecommendListBaseAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.model.recommend.RecommendContentInfo;

import java.util.ArrayList;
import java.util.List;


public class RecommendBaseFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener {

    public Context mActivity;
    public List mData;
    private RecommendBaseFragmentScrollListener mRecommendBaseFragmentScrollListener = null;
    private View mLoadMoreView;
    private View mLoadCompleteView;
    private RecommendListBaseAdapter mRecommendListBaseAdapter = null;

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

    private View mRecommendFragmentView;
    private ListView mRecommendListview;

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
     * @param b
     */
    public void displayLoadMore(boolean b) {
        if (null != mRecommendListview && null != mLoadMoreView) {
            if (b) {
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
        RecommendContentInfo info = (RecommendContentInfo) mData.get(i);
        Bundle args = new Bundle();
        args.putParcelable(DtvContentsDetailActivity.RECOMMEND_INFO_BUNDLE_KEY,
                getOtherContentsDetailData(info));
        ((BaseActivity) mActivity).startActivity(DtvContentsDetailActivity.class, args);
    }

    /**
     * コンテンツ詳細に必要なデータを返す
     *
     * @param info レコメンド情報
     * @return コンテンツ情報
     */
    public OtherContentsDetailData getOtherContentsDetailData(RecommendContentInfo info) {
        OtherContentsDetailData detailData = new OtherContentsDetailData();
        detailData.setTitle(info.title);
        detailData.setThumb(info.contentPictureUrl);
        detailData.setDetail(info.synop);
        detailData.setComment(info.comment);
        detailData.setHighlight(info.highlight);
        detailData.setServiceId(info.serviceId);

        //コンテンツIDの受け渡しを追加
        detailData.setContentId(info.contentId);

        return detailData;
    }
}
