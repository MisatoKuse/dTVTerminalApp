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

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.view.RecommendListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendBaseFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener {

    private Context mActivity = null;
    public List<ContentsData> mData = null;
    private View mLoadMoreView = null;
    private View mRecommendFragmentView = null;
    private RecommendListView mRecommendListview = null;
    private ContentsAdapter mRecommendListBaseAdapter = null;
    private RecommendBaseFragmentScrollListener mRecommendBaseFragmentScrollListener = null;
    //チャンネルデータ
    private List<Map<String, String>> mChannelMap;
    private static final int POSITION_TV = 0;
    private static final int POSITION_VIDEO = POSITION_TV + 1;
    private static final int POSITION_D_TV = POSITION_TV + 2;
    private static final int POSITION_D_CHANNEL = POSITION_TV + 3;
    private static final int POSITION_D_ANIMATION = POSITION_TV + 4;


    public void setRecommendBaseFragmentScrollListener(final RecommendBaseFragmentScrollListener lis) {
        mRecommendBaseFragmentScrollListener = lis;
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        initData();
        return initView(container);
    }

    //モックデータ
    private void initData() {
        mData = new ArrayList();
    }

    /**
     * Viewの初期設定.
     *
     * @return この行のビュー
     */
    private View initView(final ViewGroup container) {
        if (null == mRecommendFragmentView) {
            mRecommendFragmentView = View.inflate(getActivity(),
                    R.layout.fragment_recommend_content, null);
            mRecommendListview = mRecommendFragmentView.findViewById(R.id.lv_recommend_list);

            mRecommendListview.setOnScrollListener(this);
            mRecommendListview.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mActivity).inflate(
                    R.layout.search_load_more, container, false);
        }

        if (getContext() != null) {
            mRecommendListBaseAdapter = new ContentsAdapter(getContext(), mData,
                    ContentsAdapter.ActivityTypeItem.TYPE_RECOMMEND_LIST);
        }
        mRecommendListview.setAdapter(mRecommendListBaseAdapter);

        return mRecommendFragmentView;
    }

    /**
     * データの更新.
     */
    public void notifyDataSetChanged(final int tabPosition) {
        if (null != mRecommendListBaseAdapter) {
            if (tabPosition == POSITION_TV) {
                mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_TV);
            } else if (tabPosition == POSITION_D_TV) {
                mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_D_TV);
            } else if (tabPosition == POSITION_D_ANIMATION) {
                mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_D_ANIMATE);
            } else if (tabPosition == POSITION_D_CHANNEL) {
                mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_D_CHANNEL);
            } else if (tabPosition == POSITION_VIDEO) {
                mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_VIDEO);
            }
            mRecommendListBaseAdapter.notifyDataSetChanged();
//            mRecommendListBaseAdapter.setSelectedTab(tabPosition);
        }
    }

    /**
     * リストのカーソルを移動.
     *
     * @param itemNo アイテム番号
     */
    public void setSelection(final int itemNo) {
        if (null != mRecommendListview) {
            mRecommendListview.setSelection(itemNo);
        }
    }

    /**
     * チャンネルリストを受け取り、アダプターに渡す.
     *
     * @param channelData チャンネル情報
     */
    public void setChannelData(final List<Map<String, String>> channelData) {
        if (channelData == null || channelData.size() <= 0) {
            //データが無いならば即座に帰る
            return;
        }

        //チャンネル情報の蓄積
        mChannelMap = channelData;

        //さらにアダプターに横流しする
//        mRecommendListBaseAdapter.setChannel(mChannelMap);

        //リストにチャンネル情報を反映させる為に再描画する
        if (mRecommendListBaseAdapter != null) {
            mRecommendListBaseAdapter.notifyDataSetChanged();
        }
    }

    /**
     * リストの最後に更新中の行を追加または追加した行を削除する.
     *
     * @param loadFlag ロード中フラグ
     */
    public void displayLoadMore(final boolean loadFlag) {
        if (null != mRecommendListview && null != mLoadMoreView) {
            if (loadFlag) {
                mRecommendListview.addFooterView(mLoadMoreView);
            } else {
                mRecommendListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void clear() {
        //データがヌルなら初期化する
        if (mData == null) {
            initData();
        }

        mData.clear();
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
    }

    @Override
    public void onScroll(final AbsListView absListView,
                         final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        if (null != mRecommendBaseFragmentScrollListener) {
            mRecommendBaseFragmentScrollListener.onScroll(this, absListView,
                    firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        ContentsData info = mData.get(i);
        Intent intent = new Intent(mActivity, ContentDetailActivity.class);
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
        detailData.setServiceId(Integer.parseInt(info.getServiceId()));
        detailData.setMobileViewingFlg(info.getMobileViewingFlg());
        detailData.setReserved4(info.getReserved4());
        detailData.setReserved1(info.getReserved1());
        detailData.setReserved2(info.getReserved2());

        //コンテンツIDの受け渡しを追加
        detailData.setContentId(info.getContentsId());
        detailData.setChannelId(info.getChannelId());
        detailData.setRecommendOrder(info.getRecommendOrder());
        detailData.setPageId(info.getPageId());
        detailData.setGroupId(info.getGroupId());
        detailData.setRecommendMethodId(info.getRecommendMethodId());
        detailData.setCategoryId(info.getCategoryId());
        detailData.setRecommendFlg(ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY);

        detailData.setAvailStartDate(info.getStartViewing());
        detailData.setAvailEndDate(info.getEndViewing());
        return detailData;
    }

    /**
     * ContentsAdapterの通信を止める.
     */
    public void stopContentsAdapterCommunication() {
        DTVTLogger.start();
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        if (mRecommendListBaseAdapter != null) {
            stopContentsAdapterConnect.execute(mRecommendListBaseAdapter);
        }
    }

    /**
     * ContentsAdapterで止めた通信を再度可能な状態にする.
     */
    public void enableContentsAdapterCommunication() {
        DTVTLogger.start();
        if (mRecommendListBaseAdapter != null) {
            mRecommendListBaseAdapter.enableConnect();
        }
    }
}