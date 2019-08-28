/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.cliplist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
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
    private List<ContentsData> mClipListData;

    /** フッター追加用View. */
    private View mLoadMoreView = null;
    /** Fragmentレイアウト. */
    private View mTvFragmentView = null;
    /** FragmentListView. */
    private ListView mTvListView = null;
    /** FragmentProgressDialog. */
    private RelativeLayout mRelativeLayout = null;

    /** コンテンツリストアダプター. */
    private ContentsAdapter mClipMainAdapter = null;

    /** スクロール位置の記録. */
    private int mFirstVisibleItem = 0;
    /** 指を置いたY座標. */
    private float mStartY = 0;
    /** 最後のスクロール方向が上ならばtrue. */
    private boolean mLastScrollUp = false;
    /**コンテンツ詳細表示フラグ. */
    private boolean mContentsDetailDisplay = false;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

    /** コンストラクタ. */
    public ClipListBaseFragment() {
        mClipListData = new ArrayList<>();
    }

    @Override
    public Context getContext() {
        this.mContext = getActivity();
        return mContext;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        //initData();//一時使うデータ
        return initView();
    }

    /**
     * 各タブ画面は別々に実現して表示されること.
     * @return タブ画面
     */
    private View initView() {
        if (mClipListData == null) {
            mClipListData = new ArrayList<>();
        }
        mTvFragmentView = View.inflate(getActivity(), R.layout.fragment_clip_list_item_content, null);

        mLoadMoreView = View.inflate(getActivity(), R.layout.search_load_more, null);

        initContentListView();
        return mTvFragmentView;
    }

    /**
     * テレビタブコンテンツ初期化.
     */
    private void initContentListView() {
        DTVTLogger.start();
        mTvListView = mTvFragmentView
                .findViewById(R.id.clip_list_lv_searched_result);
        mRelativeLayout = mTvFragmentView.findViewById(R.id.clip_list_progress);
        mNoDataMessage = mTvFragmentView.findViewById(R.id.clip_list_no_items);

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
        showProgressBar(true);
        mClipMainAdapter = new ContentsAdapter(getContext(), mClipListData, type);
        mTvListView.setAdapter(mClipMainAdapter);
        DTVTLogger.end();
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
     * コンテンツ詳細表示フラグ設定.
     *
     * @param contentsDetailDisplay コンテンツ詳細表示フラグ
     */
    public void setContentsDetailDisplay(final boolean contentsDetailDisplay) {
        mContentsDetailDisplay = contentsDetailDisplay;
    }

    /**
     * コンテンツ詳細表示フラグ取得.
     *
     * @return コンテンツ詳細表示フラグ
     */
    public boolean getContentsDetailDisplay() {
        return mContentsDetailDisplay;
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
     * コンテンツリストデータ取得.
     * @return コンテンツリストデータ
     */
    public List<ContentsData> getClipListData() {
        return mClipListData;
    }

    /**
     * コンテンツリストデータサイズ取得.
     * @return コンテンツリストデータサイズ
     */
    public int getClipListDataSize() {
        if (mClipListData != null) {
            return mClipListData.size();
        }
        return 0;
    }

    /**
     * コンテンツリストデータ追加.
     * @param content コンテンツリストデータ
     */
    public void addClipListData(final ContentsData content) {
        if (mClipListData != null) {
            mClipListData.add(content);
        }
    }

    /**
     * コンテンツリストデータクリア.
     */
    public void clearClipListData() {
        if (mClipListData != null) {
            mClipListData.clear();
        }
    }

    /**
     * リスト0件メッセージを表示する.
     * @param showNoDataMessage メッセージを表示するかどうか
     * @param message メッセージに表示する文言
     */
    public void showNoDataMessage(final boolean showNoDataMessage, final String message) {
        DTVTLogger.start();
        //Viewが生成にActivityから直接呼ばれたとき用
        if (mTvFragmentView == null) {
            return;
        }

        if (showNoDataMessage) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(message)) {
                // 取得に失敗した場合に指定される
                mNoDataMessage.setText(message);
            }
        } else {
            mNoDataMessage.setVisibility(View.GONE);
        }
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
            mClipMainAdapter.resetMaxItemCount();
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
            if (!contentsData.isAfterLimitContents()) {
                ClipListActivity clipListActivity = (ClipListActivity) mContext;
                if (ContentUtils.isChildContentList(contentsData)) {
                    clipListActivity.startChildContentListActivity(contentsData);
                } else {
                    mContentsDetailDisplay = true;
                    Intent intent = new Intent(mContext, ContentDetailActivity.class);
                    intent.putExtra(DtvtConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
                    intent.putExtra(ContentUtils.PLALA_INFO_BUNDLE_KEY, contentsData.getContentsId());
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
    }
}