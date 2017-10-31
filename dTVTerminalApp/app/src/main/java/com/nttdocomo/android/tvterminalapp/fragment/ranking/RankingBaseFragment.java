package com.nttdocomo.android.tvterminalapp.fragment.ranking;

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
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayList;
import java.util.List;


public class RankingBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public Context mActivity;
    public List mData = null;

    private View mRankingFragmentView = null;
    private ListView mRankingListView = null;

    private ContentsAdapter mContentsAdapter;
    private RankingFragmentScrollListener mRankingBaseFragmentScrollListener = null;

    private View mLoadMoreView;
    private int mRankingMode = 0;


    public RankingBaseFragment() {
        if (mData == null) {
            mData = new ArrayList();
        }
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initData();//一時使うデータ
        DTVTLogger.debug("onCreateView");

        return initView();
    }

    /**
     * 各タブ画面は別々に実現して表示されること
     */
    private View initView() {
        if (mData == null) {
            mData = new ArrayList();
        }
        if (null == mRankingFragmentView) {
            mRankingFragmentView = View.inflate(getActivity()
                    , R.layout.fragment_ranking_content, null);
            mRankingListView = mRankingFragmentView.findViewById(R.id.lv_ranking_list);

            mRankingListView.setOnScrollListener(this);
            mRankingListView.setOnItemClickListener(this);

        }
        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.search_load_more, null);
        if (mContentsAdapter == null) {
            initRankingView();
        }
        return mRankingFragmentView;
    }

    /**
     * リスナーの設定
     *
     * @param lis
     */
    public void setRankingBaseFragmentScrollListener(RankingFragmentScrollListener lis) {
        mRankingBaseFragmentScrollListener = lis;
    }

    /**
     * 各ランキングページを判定
     */
    public void initRankingView() {
        switch (mRankingMode) {
            case RankingConstants.RANKING_MODE_NO_OF_WEEKLY: // 週間
                initWeeklyContentListView();
                break;
            case RankingConstants.RANKING_MODE_NO_OF_VIDEO: // ビデオ
                initVideoContentListView();
                break;
            default:
                break;
        }
    }

    /**
     * 各ランキングページを切り替え
     */
    public void switchRankingMode(int rankingMode) {
        mRankingMode = rankingMode;
        mContentsAdapter = null;
        if (mRankingListView != null) {
            initRankingView();
        }
    }

    /**
     * Adapterを取得
     *
     * @return
     */
    public ContentsAdapter getRankingAdapter() {
        return mContentsAdapter;
    }

    /**
     * 週間ランキングコンテンツ初期化
     */
    private void initWeeklyContentListView() {

        mContentsAdapter
                = new ContentsAdapter(getContext(),
                mData, ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK);
        mRankingListView.setAdapter(mContentsAdapter);
    }

    /**
     * ビデオランキングコンテンツ初期化
     */
    private void initVideoContentListView() {
        mContentsAdapter = new ContentsAdapter(getContext(),
                mData, ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
        mRankingListView.setAdapter(mContentsAdapter);
    }

    /**
     * Adapterをリフレッシュする
     */
    public void noticeRefresh() {
        if (null != mContentsAdapter) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 読み込み表示を行う
     *
     * @param b
     */
    public void displayMoreData(boolean b) {
        if (null != mRankingListView) {
            if (b) {
                mRankingListView.addFooterView(mLoadMoreView);
            } else {
                mRankingListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (null != mRankingBaseFragmentScrollListener) {
            mRankingBaseFragmentScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mRankingBaseFragmentScrollListener) {
            mRankingBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mLoadMoreView == view) {
            return;
        }
        ((BaseActivity) mActivity).startActivity(TvPlayerActivity.class, null);
    }
}
