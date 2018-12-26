/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * コンテンツ詳細画面表示用Fragment(チャンネル).
 */
public class DtvContentsChannelFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    /** チャンネルリスト親ビュー.*/
    private View mView = null;
    /** チャンネルリストビュー.*/
    private ListView mChannelListView = null;
    /** チャンネルリストアダプター.*/
    private ContentsAdapter mContentsAdapter = null;
    /** チャンネルリストデータ.*/
    private List<ContentsData> mContentsData;
    /** Activity.*/
    private Activity mActivity;
    /** チャンネルアイコン.*/
    private ImageView mChannelImg;
    /** チャンネル名.*/
    private TextView mChannelTxt;
    /** 通信フラグ.*/
    private boolean mIsLoading;
    /** コールバックリスナー.*/
    private ChangedScrollLoadListener mChangedScrollLoadListener;
    /** ヘッダービュー.*/
    private View mHeaderView;
    /** フッタービュー.*/
    private View mFootView;
    /** 番組データがない場合.*/
    private TextView mNoMessageTxt;


    /**
     * コールバックリスナー.
     */
    public interface ChangedScrollLoadListener {

        /**
         * スクロール時のコールバック.
         */
        void onChannelLoadMore();

        /**
         * Fragment見えるのコールバック.
         * @param isVisibleToUser isVisibleToUser
         * @param fragment fragment
         */
        void onUserVisibleHint(boolean isVisibleToUser, DtvContentsChannelFragment fragment);
    }

    /**
     * コールバックリスナー設定.
     *
     * @param mChangedScrollLoadListener コンテナ
     */
    public void setScrollCallBack(final ChangedScrollLoadListener mChangedScrollLoadListener) {
        this.mChangedScrollLoadListener = mChangedScrollLoadListener;
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return initView(container);
    }

    /**
     * コンテンツ詳細画面チャンネルタブ表示されること.
     *
     * @param container コンテナ
     * @return view ビュー
     */
    private View initView(final ViewGroup container) {
        DTVTLogger.start();
        if (null == mView) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dtv_contents_channel_fragment, container, false);
        }
        if (mContentsData == null) {
            mContentsData = new ArrayList<>();
        }
        mChannelListView = mView.findViewById(R.id.dtv_contents_channel_list);
        mNoMessageTxt = mView.findViewById(R.id.dtv_contents_channel_list_no_data);
        mChannelListView.setOnScrollListener(this);
        mChannelListView.setOnItemClickListener(this);
        if (null == mFootView) {
            mFootView = View.inflate(getContext(), R.layout.search_load_more, null);
        }
        if (null == mHeaderView) {
            mHeaderView = View.inflate(getContext(), R.layout.dtv_contents_channel_fragment_header, null);
            mHeaderView.setOnClickListener(null);
            mHeaderView.setVisibility(View.GONE);
        }
        mContentsAdapter = new ContentsAdapter(getContext(), mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST);
        mChannelListView.setAdapter(mContentsAdapter);
        mChannelImg = mHeaderView.findViewById(R.id.dtv_contents_channel_header_img);
        mChannelTxt = mHeaderView.findViewById(R.id.dtv_contents_channel_header_name);
        mChannelListView.addHeaderView(mHeaderView);
        mChannelListView.setVisibility(View.INVISIBLE);
        DTVTLogger.end();
        return mView;
    }

    /**
     * チャンネルアイコン取得.
     *
     * @param info チャンネル情報
     */
    public void setChannelDataChanged(final ChannelInfo info) {
        if (!TextUtils.isEmpty(info.getTitle())) {
            mHeaderView.setVisibility(View.VISIBLE);
            mChannelTxt.setText(info.getTitle());
        }
        if (!TextUtils.isEmpty(info.getThumbnail())) {
            ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(getContext(), ThumbnailDownloadTask.ImageSizeType.CHANNEL);
            mChannelImg.setTag(info.getThumbnail());
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mChannelImg, info.getThumbnail());
            if (bitmap != null) {
                mChannelImg.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * データ更新.
     */
    public void setNotifyDataChanged() {
        mContentsAdapter.notifyDataSetChanged();
        // ChannelProgressBar をここで非表示にする(非表示漏れ対策)
        showProgress(false);
        loadComplete();
    }

    /**
     * 一覧更新して、フッタービューの非表示.
     */
    public void loadComplete() {
        if (mContentsData != null && mContentsData.size() > 0) {
            mChannelListView.setVisibility(View.VISIBLE);
            mNoMessageTxt.setVisibility(View.GONE);
        } else {
            mNoMessageTxt.setVisibility(View.VISIBLE);
            mNoMessageTxt.setText(mActivity.getResources().getString(R.string.common_empty_data_message));
        }
        mChannelListView.removeFooterView(mFootView);
        mIsLoading = false;
    }

    /**
     * ロード開始前の状態を設定.
     */
    public void setLoadInit() {
        mChannelListView.removeFooterView(mFootView);
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
        mChannelListView.setVisibility(View.GONE);
        mChannelListView.setSelection(0);
    }

    /**
     * ローディング開始.
     */
    private void loadStart() {
        mChannelListView.addFooterView(mFootView);
        mChannelListView.setSelection(mChannelListView.getMaxScrollAmount());
        mFootView.setVisibility(View.VISIBLE);
        mIsLoading = true;
        if (mChangedScrollLoadListener != null) {
            mChangedScrollLoadListener.onChannelLoadMore();
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
        if (!mIsLoading && scrollState == SCROLL_STATE_IDLE && absListView.getLastVisiblePosition()
                == mChannelListView.getAdapter().getCount() - 1) {
            loadStart();
        }
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        //ヘッダービューを除く
        int position = i - 1;
        ContentsData contentsData = mContentsData.get(position);
        ContentDetailActivity contentDetailActivity = (ContentDetailActivity) mActivity;
        if (ContentUtils.isChildContentList(contentsData)) {
            contentDetailActivity.startChildContentListActivity(contentsData);
        } else {
            Intent intent = new Intent();
            intent.setClass(mActivity, ContentDetailActivity.class);
            intent.putExtra(ContentUtils.PLALA_INFO_BUNDLE_KEY, contentsData.getContentsId());
            contentDetailActivity.startActivity(intent);
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
     * クリップステータスチェック.
     */
    public void checkChannelClipStatus() {
        ContentsDetailDataProvider provider = new ContentsDetailDataProvider(getContext());
        mContentsData = provider.checkClipStatus(mContentsData);
        DTVTLogger.debug("DtvContentsChannelFragment::Clip Status Update");
    }

    /**
     * コンテンツデータクリア.
     */
    public void clearContentsData() {
        if (mContentsData != null) {
            mContentsData.clear();
        }
    }

    /**
     * コンテンツデータ追加.
     * @param content コンテンツ情報
     */
    public void addContentsData(final ContentsData content) {
        if (mContentsData != null) {
            mContentsData.add(content);
        }
    }

    /**
     * チャンネルリストデータ取得.
     *
     * @return チャンネルリストデータ
     */
    public List<ContentsData> getContentsData() {
        return mContentsData;
    }

    /**
     * チャンネルリストデータ設定.
     *
     * @param contentsDataList チャンネルリストデータ
     */
    public void setContentsData(final List<ContentsData> contentsDataList) {
        this.mContentsData = contentsDataList;
    }

    /**
     * チャンネルデータ読み込み中アイコンの表示切替.
     *
     * @param showProgress 表示するならばtrue
     */
    public void showProgress(final boolean showProgress) {
        //ヌルチェックの追加
        if (mView == null) {
            return;
        }
        View progressView = mView.findViewById(R.id.channel_progress_rl);
        if (progressView == null) {
            return;
        }

        if (showProgress) {
            // 既にコンテンツデータがある場合は表示しない
            if (mContentsData != null && mContentsData.size() > 0) {
                return;
            }
            progressView.setVisibility(View.VISIBLE);
        } else {
            progressView.setVisibility(View.GONE);
        }
    }
}