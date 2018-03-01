/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

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
public class RankingBaseFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnTouchListener {
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
     * リスト表示用アダプタ.
     */
    private ContentsAdapter mContentsAdapter;
    /**
     * リストのスクロール状態をコールバックするリスナー.
     */
    private RankingFragmentScrollListener mRankingBaseFragmentScrollListener = null;
    /**
     * ランキング種別.
     */
    private ContentsAdapter.ActivityTypeItem mRankingMode;
    /**
     * スクロール位置の記録.
     */
    private int mFirstVisibleItem = 0;
    /**
     * 最後のスクロール方向が上ならばtrue.
     */
    private boolean mLastScrollUp = false;
    /**
     * 指を置いたY座標.
     */
    private float mStartY = 0;

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

            mRankingListView.setOnScrollListener(this);
            mRankingListView.setOnItemClickListener(this);

            //スクロールの上下方向検知用のリスナーを設定
            mRankingListView.setOnTouchListener(this);
        }
        if (mContentsAdapter == null) {
            initRankingView();
        }
        return mRankingFragmentView;
    }

    /**
     * リスナーの設定.
     *
     * @param listener リスナー
     */
    public void setRankingBaseFragmentScrollListener(final RankingFragmentScrollListener listener) {
        mRankingBaseFragmentScrollListener = listener;
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
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
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
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        if (null != mRankingBaseFragmentScrollListener) {
            mRankingBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }

        //現在のスクロール位置の記録
        mFirstVisibleItem = firstVisibleItem;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view,
                            final int position, final long l) {
        Intent intent = new Intent(mContext, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
        OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(mData.get(position), ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
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

                changeLastScrollUp(false);

                //スクロール方向の判定
                if (mStartY < mEndY) {
                    //終了時のY座標の方が大きいので、上スクロール
                    changeLastScrollUp(true);
                }

                break;
            default:
                //現状処理は無い・警告対応
                break;
        }

        return false;
    }

    /**
     * mLastScrollUpを設定する.
     *
     * @param bool 追加データ読み込みを許可するかどうか
     */
    public void changeLastScrollUp(final Boolean bool) {
        mLastScrollUp = bool;
    }

    /**
     * ContentsAdapterの通信を止める.
     */
    public void stopContentsAdapterCommunication() {
        DTVTLogger.start();
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
            mContentsAdapter.enableConnect();
        }
    }
}