/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.cliplist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
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

/**
 * クリップリスト用Fragment.
 */
public class ClipListBaseFragment extends Fragment
        implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnTouchListener {

    /** コンテキストファイル. */
    private Context mContext = null;
    /** スクロールリスナー. */
    private ClipListBaseFragmentScrollListener mClipListBaseFragmentScrollListener = null;

    /** コンテンツリストデータ. */
    public List<ContentsData> mClipListData = new ArrayList<>();

    /** フッター追加用View. */
    private View mLoadMoreView = null;
    /** Fragmentレイアウト. */
    private View mTvFragmentView = null;
    /** FragmentListView. */
    private ListView mTvListView = null;
    /** FragmentProgressDialog. */
    private RelativeLayout mRelativeLayout = null;

    /** コンテンツリストアダプター. */
    public ContentsAdapter mClipMainAdapter = null;

    /** スクロール位置の記録. */
    private int mFirstVisibleItem = 0;
    /** 指を置いたY座標. */
    private float mStartY = 0;
    /** 最後のスクロール方向が上ならばtrue. */
    private boolean mLastScrollUp = false;
    /**コンテンツ詳細表示フラグ. */
    public boolean mContentsDetailDisplay = false;

    /** コンストラクタ. */
    public ClipListBaseFragment() {
        mClipListData = new ArrayList();
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
        return initView(container);
    }

    /**
     * スクロールリスナ.
     *
     * @param lis リスナ
     */
    public void setClipListBaseFragmentScrollListener(final ClipListBaseFragmentScrollListener lis) {
        mClipListBaseFragmentScrollListener = lis;
    }

    /**
     * 各タブ画面は別々に実現して表示されること.
     * @param container container
     * @return タブ画面
     */
    private View initView(final ViewGroup container) {
        if (mClipListData == null) {
            mClipListData = new ArrayList();
        }
        mTvFragmentView = View.inflate(getActivity(), R.layout.fragment_clip_list_item_content, null);

        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.search_load_more, container, false);

        initContentListView();
        showProgressBar(true);
        return mTvFragmentView;
    }

    /**
     * アダプタ取得.
     *
     * @return ContentsAdapter
     */
    public ContentsAdapter getClipMainAdapter() {
        return mClipMainAdapter;
    }

    /**
     * テレビタブコンテンツ初期化.
     */
    private void initContentListView() {
        DTVTLogger.start();
        mTvListView = mTvFragmentView
                .findViewById(R.id.clip_list_lv_searched_result);
        mRelativeLayout = mTvFragmentView.findViewById(R.id.clip_list_progress);

        mTvListView.setOnScrollListener(this);
        mTvListView.setOnItemClickListener(this);

        //スクロールの上下方向検知用のリスナーを設定
        mTvListView.setOnTouchListener(this);
        int position = getArguments().getInt(ClipListFragmentFactory.POSITION);
        ContentsAdapter.ActivityTypeItem type;
        if (position == 0) {
            type = ContentsAdapter.ActivityTypeItem.TYPE_CLIP_LIST_MODE_TV;
        } else {
            type = ContentsAdapter.ActivityTypeItem.TYPE_CLIP_LIST_MODE_VIDEO;
        }
        mClipMainAdapter = new ContentsAdapter(getContext(), mClipListData, type);
        mTvListView.setAdapter(mClipMainAdapter);
        DTVTLogger.end();
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    public void showProgressBar(final boolean showProgressBar) {
        DTVTLogger.start();
        //Viewが生成にActivityから直接呼ばれたとき用
        if (mTvFragmentView == null) {
            return;
        }
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(getActivity())) {
                return;
            }
            mTvListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mTvListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
        DTVTLogger.end();
    }

    /**
     * リスト更新.
     */
    public void noticeRefresh() {
        if (null != mClipMainAdapter) {
            mClipMainAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ページング.
     *
     * @param loadFlag ページングフラグ
     */
    public void displayMoreData(final boolean loadFlag) {
        DTVTLogger.start();
        if (null != mTvListView) {
            if (loadFlag) {
                mTvListView.addFooterView(mLoadMoreView);

                //スクロール位置を最下段にすることで、追加した更新フッターを画面内に入れる
                mTvListView.setSelection(mTvListView.getMaxScrollAmount());
            } else {
                mTvListView.removeFooterView(mLoadMoreView);
            }
        }
        DTVTLogger.end();
    }

    /**
     * タブ種別設定.
     *
     * @param mode タブ種別
     */
    public void setMode(final ContentsAdapter.ActivityTypeItem mode) {
        if (null != mClipMainAdapter) {
            mClipMainAdapter.setMode(mode);
            mClipListData.clear();
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        DTVTLogger.start();
        if (null != mClipListBaseFragmentScrollListener) {
            //スクロール位置がリストの先頭で上スクロールだった場合は、更新をせずに帰る
            if (mFirstVisibleItem == 0 && mLastScrollUp) {
                return;
            }

            mClipListBaseFragmentScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
        DTVTLogger.end();
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        DTVTLogger.start();
        if (null != mClipListBaseFragmentScrollListener) {
            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;

            mClipListBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
        DTVTLogger.end();
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
                mLastScrollUp = false;
                //スクロール方向の判定
                if (mStartY < mEndY) {
                    //終了時のY座標の方が大きいので、上スクロール
                    mLastScrollUp = true;
                }
                break;
            default:
                //現状処理は無い・警告対応
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        if (mLoadMoreView.equals(view)) {
            return;
        }

        if (mContext != null) {
            ContentsData contentsData = mClipListData.get(i);
            if (!contentsData.isIsAfterLimitContents()) {
                ClipListActivity clipListActivity = (ClipListActivity) mContext;
                if (ContentUtils.isChildContentList(contentsData)) {
                    clipListActivity.startChildContentListActivity(contentsData);
                } else {
                    mContentsDetailDisplay = true;
                    Intent intent = new Intent(mContext, ContentDetailActivity.class);
                    intent.putExtra(DtvtConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
                    OtherContentsDetailData detailData = DataConverter.getOtherContentsDetailData(contentsData, ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
                    intent.putExtra(detailData.getRecommendFlg(), detailData);
                    clipListActivity.startActivity(intent);
                }
            }
        }
    }

    /**
     * 通信を再開する.
     */
    public void enableContentsAdapterConnect() {
        if (mClipMainAdapter != null) {
            mClipMainAdapter.enableConnect();
        }
        if (mTvListView != null) {
            mTvListView.invalidateViews();
        }
    }

    /**
     * 通信を停止する.
     */
    public void stopContentsAdapterConnect() {
        StopContentsAdapterConnect stopAdapterConnect = new StopContentsAdapterConnect();
        stopAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mClipMainAdapter);
    }

    /**
     * Fragment経由でContentsAdapterを更新する.
     *
     * @param contentsDataList コンテンツリスト
     */
    public void updateContentsList(final List<ContentsData> contentsDataList) {
        mClipMainAdapter.setListData(contentsDataList);
        noticeRefresh();
        showProgressBar(false);
    }
}