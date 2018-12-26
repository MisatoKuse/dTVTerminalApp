/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity;
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

import static com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity.PAGE_NO_OF_SERVICE_CLEAR;
import static com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity.PAGE_NO_OF_SERVICE_DTV_CHANNEL;
import static com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity.PAGE_NO_OF_SERVICE_TELEVISION;

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
    private List<ContentsData> mData;
    /**
     * 検索結果件数.
     */
    private TextView mCountText = null;
    /**
     * 検索結果線.
     */
    private TextView mLineText = null;
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
    private ContentsAdapter mContentsAdapter = null;
    /**
     * 検索結果のリスト部分のView全体.
     */
    private View mTvFragmentView;
    /**
     * 検索結果のリストView.
     */
    private ListView mTvListView = null;
    /**
     * 検索中ProgressDialog.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

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
        DTVTLogger.start();
        if (null == mTvFragmentView) {
            mTvFragmentView = View.inflate(getActivity(), R.layout.fragment_televi_content, null);
            mTvListView = mTvFragmentView.findViewById(R.id.lv_searched_result);
            mRelativeLayout = mTvFragmentView.findViewById(R.id.lv_searched_progress);

            mTvListView.setOnScrollListener(this);
            mTvListView.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mContext).inflate(R.layout.search_load_more, mTvListView, false);
        }
        if (getContext() != null) {
            mContentsAdapter = new ContentsAdapter(getContext(), mData, ContentsAdapter.ActivityTypeItem.TYPE_SEARCH_LIST);
        }
        mTvListView.setAdapter(mContentsAdapter);

        if (null == mCountText) {
            mCountText = mTvFragmentView.findViewById(R.id.tv_searched_result);
            mLineText = mTvFragmentView.findViewById(R.id.fragment_search_result_line);
        }
        mNoDataMessage = mTvFragmentView.findViewById(R.id.search_list_no_items);

        DTVTLogger.end();
        return mTvFragmentView;
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    public void showProgressBar(final boolean showProgressBar) {
        //Activityから直接呼び出されるためここでNullチェック
        if (mTvFragmentView == null) {
            return;
        }
        mTvListView = mTvFragmentView.findViewById(R.id.lv_searched_result);
        mRelativeLayout = mTvFragmentView.findViewById(R.id.lv_searched_progress);
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(getActivity())) {
                return;
            }
            mTvListView.setVisibility(View.GONE);
            mTvListView.smoothScrollToPosition(0);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mTvListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * プロセスバー表示判定.
     *
     * @return  true:表示中 false:非表示
     */
    public boolean isProgressBarShowing() {
        if (mRelativeLayout == null) {
            return false;
        }
        return mRelativeLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 画面の更新.
     *
     * @param count 検索結果件数
     * @param tabPosition タブポジション
     */
    public void notifyDataSetChanged(final String count, final int tabPosition) {
        DTVTLogger.debug("count:" + count + ",tabPosition:" + tabPosition);
        if (null != mContentsAdapter) {
            switch (tabPosition) {
                case PAGE_NO_OF_SERVICE_TELEVISION:
                    mContentsAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_TV);
                    break;
                case PAGE_NO_OF_SERVICE_DTV_CHANNEL:
                    mContentsAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_D_CHANNEL);
                    break;
                default:
                    mContentsAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_DEFAULT);
                    break;
            }
            mContentsAdapter.resetMaxItemCount();
            mContentsAdapter.notifyDataSetChanged();
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
                mLineText.setVisibility(View.VISIBLE);
            } else {
                mCountText.setVisibility(View.INVISIBLE);
                mLineText.setVisibility(View.INVISIBLE);
                setNoDataMessageVisibility(false);
            }
        }
    }

    /**
     * 検索結果0件の表示/非表示.
     *
     * @param visibility true:表示 false:非表示
     */
    public void setNoDataMessageVisibility(final Boolean visibility) {
        if (null != mNoDataMessage) {
            if (visibility) {
                mNoDataMessage.setVisibility(View.VISIBLE);
            } else {
                mNoDataMessage.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * リストのデータをクリアする.
     * @param context コンテキスト
     */
    public void clear(final Context context) {
        if (null != mData) {
            mData.clear();
        }
        notifyDataSetChanged(context.getResources().getString(R.string.keyword_search_default_count),
                PAGE_NO_OF_SERVICE_CLEAR);
        showProgressBar(false);
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        if (null != mSearchBaseFragmentScrollListener) {
            mSearchBaseFragmentScrollListener.onUserVisibleHint(isVisibleToUser, this);
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        if (null != mSearchBaseFragmentScrollListener) {
            mSearchBaseFragmentScrollListener.onScrollChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        if (mLoadMoreView.equals(view)) {
            return;
        }

        ContentsData info =  mData.get(i);
        SearchTopActivity searchTopActivity = (SearchTopActivity) mContext;
        if (ContentUtils.isChildContentList(info)) {
            searchTopActivity.startChildContentListActivity(info);
        } else {
            Intent intent = new Intent(mContext, ContentDetailActivity.class);
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
            OtherContentsDetailData detailData = DataConverter.getContentDataToContentsDetail(info, ContentUtils.SEARCH_INFO_BUNDLE_KEY);
            intent.putExtra(ContentUtils.RECOMMEND_INFO_BUNDLE_KEY, detailData);
            searchTopActivity.startActivity(intent);
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
     * コンテンツ情報追加.
     *
     * @param content コンテンツ
     */
    public void addContentData(final ContentsData content) {
        if (mData != null) {
            mData.add(content);
        }
    }

    /**
     * コンテンツ数取得.
     */
    public int getContentDataSize() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }
}
