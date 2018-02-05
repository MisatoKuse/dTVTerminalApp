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
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;

import java.util.ArrayList;
import java.util.List;

/**
 * 検索結果画面を表示する.
 */
public class SearchBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    /**
     * コンテキスト.
     */
    private Context mContext;
    /**
     * 表示するコンテンツデータリスト.
     */
    public List<ContentsData> mData;
    /**
     * 検索結果件数.
     */
    private TextView mCountText = null;
    /**
     * スクロールリスナー.
     */
    private SearchBaseFragmentScrollListener mSearchBaseFragmentScrollListener = null;
    /**
     * 追加読み込み時のプログレスダイアログ表示用View.
     */
    private View mLoadMoreView;
    /**
     * 検索結果リスト用アダプタ.
     */
    private ContentsAdapter mSearchResultBaseAdapter = null;
    /**
     * 検索結果のリスト部分のView全体.
     */
    private View mTvFragmentView;
    /**
     * 検索結果のリストView.
     */
    private ListView mTvListView;
    /**
     * 検索結果数文字列の初期値.
     */
    public final static String SearchCountDefault = "検索結果:0件";

    /**
     * スクロールリスナーをセットする.
     *
     * @param listener リスナー
     */
    public void setSearchBaseFragmentScrollListener(final SearchBaseFragmentScrollListener listener) {
        mSearchBaseFragmentScrollListener = listener;
    }

    @Override
    public Context getContext() {
        this.mContext = getActivity();
        return mContext;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        initData();
        return initView();
    }

    /**
     * データの初期化(モックデータ).
     */
    private void initData() {
        mData = new ArrayList<>();
    }

    /**
     * Viewの初期化.
     *
     * @return View
     */
    private View initView() {
        if (null == mTvFragmentView) {
            mTvFragmentView = View.inflate(getActivity(), R.layout.fragment_televi_content, null);
            mTvListView = mTvFragmentView.findViewById(R.id.lv_searched_result);

            mTvListView.setOnScrollListener(this);
            mTvListView.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mContext).inflate(R.layout.search_load_more, null);
        }

        if (getContext() != null) {
            mSearchResultBaseAdapter = new ContentsAdapter(getContext(), mData, ContentsAdapter.ActivityTypeItem.TYPE_SEARCH_LIST);
        }
        mTvListView.setAdapter(mSearchResultBaseAdapter);

        if (null == mCountText) {
            mCountText = mTvFragmentView.findViewById(R.id.tv_searched_result);
        }

        return mTvFragmentView;
    }

    /**
     * 画面の更新.
     *
     * @param count 検索結果件数
     */
    public void notifyDataSetChanged(final String count) {
        if (null != mSearchResultBaseAdapter) {
            mSearchResultBaseAdapter.notifyDataSetChanged();
        }
        if (null != mCountText) {
            mCountText.setText(count);
        }
    }

    /**
     * リストの表示を更新する.
     */
    public void invalidateViews() {
        if (null != mTvListView) {
            mTvListView.invalidateViews();
        }
    }

    /**
     * プログレスダイアログの表示/非表示.
     *
     * @param visibility true:表示 false:非表示
     */
    public void displayLoadMore(final boolean visibility) {
        if (null != mTvListView && null != mLoadMoreView) {
            if (visibility) {
                mTvListView.addFooterView(mLoadMoreView);
            } else {
                mTvListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * 検索結果件数の表示/非表示.
     *
     * @param visibility true:表示 false:非表示
     */
    public void setResultTextVisibility(final Boolean visibility) {
        if (null != mCountText) {
            if (visibility) {
                mCountText.setVisibility(View.VISIBLE);
            } else {
                mCountText.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * リストのデータをクリアする.
     */
    public void clear() {
        if (null != mData) {
            mData.clear();
        }
        notifyDataSetChanged(SearchCountDefault);
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        if (null != mSearchBaseFragmentScrollListener) {
            mSearchBaseFragmentScrollListener.onScroll(
                    this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        ContentsData info =  mData.get(i);
        if (mLoadMoreView.equals(view)) {
            return;
        }
        Intent intent = new Intent(mContext, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
        intent.putExtra(ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY,
                getOtherContentsDetailData(info));
        startActivity(intent);
    }

    /**
     * コンテンツ詳細に必要なデータを返す.
     *
     * @param info レコメンド情報
     * @return コンテンツ情報
     */
    private OtherContentsDetailData getOtherContentsDetailData(final ContentsData info) {
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
