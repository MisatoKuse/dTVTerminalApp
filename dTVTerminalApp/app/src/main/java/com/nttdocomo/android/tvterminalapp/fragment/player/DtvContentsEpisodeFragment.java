/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.List;

/**
 * コンテンツ詳細画面表示用Fragment(エピソード).
 */
public class DtvContentsEpisodeFragment extends Fragment implements AbsListView.OnScrollListener, ContentsAdapter.EpisodeItemClickCallback {

    /** エピソードリスト親ビュー.*/
    private View mView;
    /** エピソードリストビュー.*/
    private ListView mEpisodeListView;
    /** エピソードリストアダプター.*/
    private ContentsAdapter mContentsAdapter;
    /** エピソードリストデータ.*/
    private List<ContentsData> mContentsData;
    /** Activity.*/
    private Activity mActivity;
    /** 通信フラグ.*/
    private boolean mIsLoading;
    /** コールバックリスナー.*/
    private ChangedScrollLoadListener mChangedScrollLoadListener;
    /** フッタービュー.*/
    private View mFootView;
    /** データがない場合.*/
    private TextView mNoMessageTxt;
    /** 1ページング最大値.*/
    private static final int PAGE_MAX_COUNT = 50;
    /** 最後ページであるかどうか.*/
    private boolean mIsEndPage = false;
    /**
     * コールバックリスナー.
     */
    public interface ChangedScrollLoadListener {

        /**
         * スクロール時のコールバック.
         * @param position ポジション
         */
        void onEpisodeLoadMore(int position);

        /**
         * アイテムタップコールバック.
         * @param contentsData contentsData
         * @param isThumbnailTap true:サムネイル false:全文を読む
         */
        void onItemClickCallback(ContentsData contentsData, boolean isThumbnailTap);

        /**
         * Fragment初期化されるコールバック.
         * @param isVisibleToUser isVisibleToUser
         * @param fragment fragment
         */
        void onUserVisibleHint(boolean isVisibleToUser, DtvContentsEpisodeFragment fragment);
    }

    /**
     * コールバックリスナー設定.
     * @param changedScrollLoadListener リスナーを設定
     */
    public void setScrollCallBack(final ChangedScrollLoadListener changedScrollLoadListener) {
        this.mChangedScrollLoadListener = changedScrollLoadListener;
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(@Nullable final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return initView(container);
    }

    /**
     * コンテンツ詳細画面エピソードタブ表示されること.
     * @param container コンテナ
     * @return view ビュー
     */
    private View initView(final ViewGroup container) {
        DTVTLogger.start();
        if (null == mView) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dtv_contents_episode_fragment, container, false);
        }
        if (mContentsData == null) {
            mContentsData = new ArrayList<>();
        }
        mEpisodeListView = mView.findViewById(R.id.dtv_contents_episode_list);
        mNoMessageTxt = mView.findViewById(R.id.dtv_contents_episode_list_no_data);
        mEpisodeListView.setOnScrollListener(this);
        if (null == mFootView) {
            mFootView = View.inflate(getContext(), R.layout.search_load_more, null);
        }
        mContentsAdapter = new ContentsAdapter(getContext(), mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_CONTENT_DETAIL_EPISODE_LIST);
        mContentsAdapter.setEpisodeThumbnailClickCallback(this);
        mEpisodeListView.setAdapter(mContentsAdapter);
        mEpisodeListView.setVisibility(View.INVISIBLE);
        DTVTLogger.end();
        return mView;
    }

    /**
     * データ更新.
     */
    public void setNotifyDataChanged() {
        mContentsAdapter.notifyDataSetChanged();
        showProgress(false);
        loadComplete();
    }

    /**
     * 一覧更新して、フッタービューの非表示.
     */
    public void loadComplete() {
        if (mContentsData != null && mContentsData.size() > 0) {
            mEpisodeListView.setVisibility(View.VISIBLE);
            mNoMessageTxt.setVisibility(View.GONE);
        } else {
            mNoMessageTxt.setVisibility(View.VISIBLE);
            mNoMessageTxt.setText(mActivity.getResources().getString(R.string.common_empty_data_message));
        }
        mEpisodeListView.removeFooterView(mFootView);
        mIsLoading = false;
    }

    /**
     * 取得失敗の場合.
     */
    public void loadFailed() {
        if (mContentsData != null && mContentsData.size() == 0) {
            mNoMessageTxt.setVisibility(View.VISIBLE);
            mNoMessageTxt.setText(mActivity.getResources().getString(R.string.common_get_data_failed_message));
        }
    }

    /**
     * ビュー初期化.
     */
    public void initLoad() {
        mEpisodeListView.setVisibility(View.GONE);
        mEpisodeListView.setSelection(0);
    }

    /**
     * ローディング開始.
     */
    private void pagingLoadStart() {
        mEpisodeListView.addFooterView(mFootView);
        mEpisodeListView.setSelection(mEpisodeListView.getMaxScrollAmount());
        mFootView.setVisibility(View.VISIBLE);
        mIsLoading = true;
        if (mChangedScrollLoadListener != null) {
            mChangedScrollLoadListener.onEpisodeLoadMore(getContentsData().size() + 1);
        }
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        DTVTLogger.start();
        if (mChangedScrollLoadListener != null) {
            mChangedScrollLoadListener.onUserVisibleHint(isVisibleToUser, this);
        }
        DTVTLogger.end();
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        if (!mIsEndPage && !mIsLoading && scrollState == SCROLL_STATE_IDLE && absListView.getLastVisiblePosition()
                == mEpisodeListView.getAdapter().getCount() - 1) {
            pagingLoadStart();
        }
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
    }

    @Override
    public void onThumbnailClick(final ContentsData contentsData) {
        mChangedScrollLoadListener.onItemClickCallback(contentsData, true);
    }

    @Override
    public void onMoreBtnClick(final ContentsData contentsData) {
        mChangedScrollLoadListener.onItemClickCallback(contentsData, false);
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
     * コンテンツデータ追加.
     * @param content コンテンツ情報
     */
    public void addContentsData(final List<ContentsData> content) {
        if (mContentsData != null) {
            if (content.size() < PAGE_MAX_COUNT) {
                mIsEndPage = true;
            }
            mContentsData.addAll(content);
        }
    }

    /**
     * エピソードリストデータ取得.
     * @return エピソードリストデータ
     */
    public List<ContentsData> getContentsData() {
        return mContentsData;
    }

    /**
     * エピソードリストデータ設定.
     * @param contentsDataList エピソードリストデータ
     */
    public void setContentsData(final List<ContentsData> contentsDataList) {
        this.mContentsData = contentsDataList;
    }

    /**
     * エピソードデータ読み込み中アイコンの表示切替.
     * @param showProgress 表示するならばtrue
     */
    public void showProgress(final boolean showProgress) {
        //ヌルチェックの追加
        if (mView == null) {
            return;
        }
        View progressView = mView.findViewById(R.id.episode_progress_rl);
        if (progressView == null) {
            return;
        }
        if (showProgress) {
            // 既にコンテンツデータがある場合は表示しない
            if (mContentsData != null && mContentsData.size() > 0) {
                return;
            }
            progressView.setVisibility(View.VISIBLE);
            if (mNoMessageTxt != null) {
                mNoMessageTxt.setVisibility(View.GONE);
            }
        } else {
            progressView.setVisibility(View.GONE);
        }
    }
}