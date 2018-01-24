/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.search;

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
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.adapter.SearchResultBaseAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;

import java.util.ArrayList;
import java.util.List;


public class SearchBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public Context mActivity;
    public List<ContentsData> mData;
    private TextView mCountText = null;
    private SearchBaseFragmentScrollListener mSearchBaseFragmentScrollListener = null;
    private View mLoadMoreView;
    private SearchResultBaseAdapter mSearchResultBaseAdapter = null;

    public void setSearchBaseFragmentScrollListener(SearchBaseFragmentScrollListener lis) {
        mSearchBaseFragmentScrollListener = lis;
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

    private View mTeleviFragmentView;
    private ListView mTeveviListview;

    public View initView() {
        if (null == mTeleviFragmentView) {
            mTeleviFragmentView = View.inflate(getActivity()
                    , R.layout.fragment_televi_content, null);
            mTeveviListview = mTeleviFragmentView.findViewById(R.id.lv_searched_result);

            mTeveviListview.setOnScrollListener(this);
            mTeveviListview.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mActivity).inflate(R.layout.search_load_more, null);
        }

        //SearchResultBaseAdapter searchResultBaseAdapter
        mSearchResultBaseAdapter = new SearchResultBaseAdapter(getContext(), mData, R.layout.item_search_result_televi);
        mTeveviListview.setAdapter(mSearchResultBaseAdapter);

        if (null == mCountText) {
            mCountText = mTeleviFragmentView.findViewById(R.id.tv_searched_result);
        }

        return mTeleviFragmentView;
    }

    public void notifyDataSetChanged(String count) {
        if (null != mSearchResultBaseAdapter) {
            initView();
            mSearchResultBaseAdapter.notifyDataSetChanged();
        }
        if (null != mCountText) {
            mCountText.setText(count);
        }
    }

    public void setSelection(int itemNo) {
        if (null != mTeveviListview) {
            mTeveviListview.setSelection(itemNo);
        }
    }

    public void displayLoadMore(boolean b) {
        if (null != mTeveviListview && null != mLoadMoreView) {
            if (b) {
                mTeveviListview.addFooterView(mLoadMoreView);
            } else {
                mTeveviListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void setResultTextVisibility(Boolean visibility) {
        if (null != mCountText) {
            if (visibility) {
                mCountText.setVisibility(View.VISIBLE);
            } else {
                mCountText.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void clear() {
        if (null != mData) {
            mData.clear();
        }
        notifyDataSetChanged(SearchTopActivity.sSearchCountDefault);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mSearchBaseFragmentScrollListener) {
            mSearchBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ContentsData info =  mData.get(i);
        if (mLoadMoreView.equals(view)) {
            return;
        }
        Intent intent = new Intent(mActivity, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
        intent.putExtra(ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY,
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

        //コンテンツIDの受け渡しを追加
        detailData.setContentId(info.getContentsId());
        detailData.setRecommendOrder(info.getRecommendOrder());

        return detailData;
    }
}
