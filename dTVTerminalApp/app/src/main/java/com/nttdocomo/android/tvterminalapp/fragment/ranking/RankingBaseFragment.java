/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayList;
import java.util.List;


public class RankingBaseFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnTouchListener {

    public Context mActivity;
    public List mData = null;

    private View mRankingFragmentView = null;
    private ListView mRankingListView = null;

    private ContentsAdapter mContentsAdapter;
    private RankingFragmentScrollListener mRankingBaseFragmentScrollListener = null;

    private View mLoadMoreView;
    private int mRankingMode = 0;

    //スクロール位置の記録
    private int mFirstVisibleItem = 0;

    //最後のスクロール方向が上ならばtrue
    private boolean mLastScrollUp = false;

    //指を置いたY座標
    private float mStartY = 0;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

            //スクロールの上下方向検知用のリスナーを設定
            mRankingListView.setOnTouchListener(this);

        }
        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.search_load_more,
                null);
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
        if (mData != null) {
            mContentsAdapter
                    = new ContentsAdapter(getContext(),
                    mData, ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK);
            mRankingListView.setAdapter(mContentsAdapter);
        }
    }

    /**
     * ビデオランキングコンテンツ初期化
     */
    private void initVideoContentListView() {
        if (mData != null) {
            mContentsAdapter = new ContentsAdapter(getContext(),
                    mData, ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
            mRankingListView.setAdapter(mContentsAdapter);
        }
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
     * @param bool
     */
    public void displayMoreData(boolean bool) {
        if (null != mRankingListView) {
            if (bool) {
                mRankingListView.addFooterView(mLoadMoreView);

                //スクロール位置を最下段にすることで、追加した更新フッターを画面内に入れる
                mRankingListView.setSelection(mRankingListView.getMaxScrollAmount());

            } else {
                mRankingListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //スクロール位置がリストの先頭で上スクロールだった場合は、更新をせずに帰る
        if (mFirstVisibleItem == 0 && mLastScrollUp) {
            return;
        }

        if (null != mRankingBaseFragmentScrollListener) {
            mRankingBaseFragmentScrollListener.onScrollStateChanged(this, absListView,
                    scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        if (null != mRankingBaseFragmentScrollListener) {
            mRankingBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }

        //現在のスクロール位置の記録
        mFirstVisibleItem = firstVisibleItem;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(mLoadMoreView.equals(view) || mActivity == null) {
            return;
        }
        if (view == getActivity().findViewById(R.id.item_common_result_clip_tv)) {
            //TODO:クリップ処理実装
            Toast.makeText(getActivity(), "クリップしました", Toast.LENGTH_SHORT).show();
        } else {
            ((BaseActivity) mActivity).startActivity(DtvContentsDetailActivity.class, null);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!(view instanceof ListView)) {
            //今回はリストビューの事しか考えないので、他のビューならば帰る
            return false;
        }

        //指を動かした方向を検知する
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //指を降ろしたので、位置を記録
                mStartY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                //指を離したので、位置を記録
                float mEndY = motionEvent.getY();

                mLastScrollUp = false;

                //スクロール方向の判定
                if (mStartY < mEndY) {
                    //終了時のY座標の方が大きいので、上スクロール
                    mLastScrollUp = true;
                }

                break;
            default:
                //現状処理は無い・警告対応
        }

        return false;
    }
}