/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecommendActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.view.RecommendListView;

import java.util.ArrayList;
import java.util.List;

/**
 * おすすめ番組・ビデオ（タブフラグメントクラス）.
 */
public class RecommendBaseFragment extends Fragment implements AdapterView.OnItemClickListener {

    /**
     * コンテスト.
     */
    private Context mActivity = null;
    /**
     * リストデータ.
     */
    private List<ContentsData> mData = null;
    /**
     * フラグメントビュー.
     */
    private View mRecommendFragmentView = null;
    /**
     * リストビュー.
     */
    private RecommendListView mRecommendListView = null;
    /**
     * ProgressBar.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage = null;
    /**
     * プログレスバーの再表示フラグ.
     */
    private boolean mIsShowProgressBar = true;
    /**
     * アダプター.
     */
    private ContentsAdapter mRecommendListBaseAdapter = null;
    /**
     * テレビ（タブインデックス）.
     */
    private static final int POSITION_TV = 0;
    /**
     * ビデオ（タブインデックス）.
     */
    private static final int POSITION_VIDEO = POSITION_TV + 1;
    /**
     * dTV（タブインデックス）.
     */
    private static final int POSITION_D_TV = POSITION_TV + 2;
    /**
     * dTVチャンネル（タブインデックス）.
     */
    private static final int POSITION_D_CHANNEL = POSITION_TV + 3;
    /**
     * DAZN（タブインデックス）.
     */
    private static final int POSITION_DAZN = POSITION_TV + 4;
    /**
     * dアニメ（タブインデックス）.
     */
    private static final int POSITION_D_ANIMATION = POSITION_TV + 5;

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        initData();
        return initView();
    }

    /**
     * リストを初期化.
     */
    private void initData() {
        mData = new ArrayList<>();
    }

    /**
     * Viewの初期設定.
     * @return この行のビュー
     */
    private View initView() {
        if (null == mRecommendFragmentView) {
            mRecommendFragmentView = View.inflate(getActivity(),
                    R.layout.fragment_recommend_content, null);
            mRecommendListView = mRecommendFragmentView.findViewById(R.id.lv_recommend_list);
            mRelativeLayout = mRecommendFragmentView.findViewById(R.id.lv_recommend_progress);

            mRecommendListView.setOnItemClickListener(this);

            getContext();
        }
        showProgressBar(true);
        if (getContext() != null) {
            mRecommendListBaseAdapter = new ContentsAdapter(getContext(), mData,
                    ContentsAdapter.ActivityTypeItem.TYPE_RECOMMEND_LIST);
        }
        mRecommendListView.setAdapter(mRecommendListBaseAdapter);

        return mRecommendFragmentView;
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    public void showProgressBar(final boolean showProgressBar) {
        if (mRecommendFragmentView == null) {
            return;
        }
        mRecommendListView = mRecommendFragmentView.findViewById(R.id.lv_recommend_list);
        mRelativeLayout = mRecommendFragmentView.findViewById(R.id.lv_recommend_progress);
        mNoDataMessage = mRecommendFragmentView.findViewById(R.id.recommend_no_items);
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(getActivity())) {
                return;
            }
            mRecommendListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
            showNoDataMessage(false, null);
        } else {
            mRecommendListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * リスト0件表示.
     *
     * @param showNoDataMessage プロセスバーを表示するかどうか
     * @param message 0件表示の文言
     */
    public void showNoDataMessage(final boolean showNoDataMessage, final String message) {
        DTVTLogger.start();
        if (mNoDataMessage == null) {
            return;
        }
        if (showNoDataMessage) {
            if (!TextUtils.isEmpty(message)) {
                mNoDataMessage.setText(message);
            }
            mNoDataMessage.setVisibility(View.VISIBLE);
        } else {
            mNoDataMessage.setVisibility(View.GONE);
        }
        DTVTLogger.end();
    }

    /**
     * データの更新.
     *
     * @param tabPosition タブインデックス
     */
    public void notifyDataSetChanged(final int tabPosition) {
        if (null != mRecommendListBaseAdapter) {
            switch (tabPosition) {
                case POSITION_TV:
                    mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_TV);
                    break;
                case POSITION_D_TV:
                    mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_D_TV);
                    break;
                case POSITION_D_ANIMATION:
                    mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_D_ANIMATE);
                    break;
                case POSITION_D_CHANNEL:
                    mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_D_CHANNEL);
                    break;
                case POSITION_VIDEO:
                    mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_VIDEO);
                    break;
                case POSITION_DAZN:
                    mRecommendListBaseAdapter.setTabTypeItem(ContentsAdapter.TabTypeItem.TAB_DAZN);
                    break;
                default:
                    break;
            }
            mRecommendListBaseAdapter.resetMaxItemCount();
            mRecommendListBaseAdapter.notifyDataSetChanged();
            showProgressBar(false);
        }
    }

    /**
     * リストのカーソルを移動.
     *
     * @param itemNo アイテム番号
     */
    public void setSelection(final int itemNo) {
        if (null != mRecommendListView) {
            mRecommendListView.setSelection(itemNo);
        }
    }

    /**
     * リストの表示を更新する.
     */
    public void invalidateViews() {
        if (null != mRecommendListView) {
            mRecommendListView.invalidateViews();
        }
    }

    /**
     * リストのクリアを行う.
     */
    public void clear() {
        //データがヌルなら初期化する
        if (mData == null) {
            initData();
        }

        mData.clear();
        if (mRecommendListBaseAdapter != null) {
            mRecommendListBaseAdapter.resetMaxItemCount();
            mRecommendListBaseAdapter.notifyDataSetChanged();
            showProgressBar(false);
        }
    }

    /**
     * プログレスバーの再表示フラグセット.
     * @param isShowProgressBar
     */
    public void setProgressBarFlag(final boolean isShowProgressBar) {
        // すでにコールバックされた場合はBG→FGでプログレスバーを表示しない
        // BG→FG後に再取得処理はないため、BG→FG後は再表示しない
        mIsShowProgressBar = isShowProgressBar;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        ContentsData info = mData.get(i);
        RecommendActivity recommendActivity = (RecommendActivity) mActivity;
        if (ContentUtils.isChildContentList(info)) {
            recommendActivity.startChildContentListActivity(info);
        } else {
            Intent intent = new Intent(mActivity, ContentDetailActivity.class);
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
            intent.putExtra(ContentUtils.RECOMMEND_INFO_BUNDLE_KEY, DataConverter.getContentDataToContentsDetail(info, ContentUtils.RECOMMEND_INFO_BUNDLE_KEY));
            recommendActivity.startActivity(intent);
        }
    }

    /**
     * ContentsAdapterの通信を止める.
     */
    public void stopContentsAdapterCommunication() {
        DTVTLogger.start();
        showProgressBar(false);
        StopContentsAdapterConnect stopContentsAdapterConnect = new StopContentsAdapterConnect();
        if (mRecommendListBaseAdapter != null) {
            stopContentsAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mRecommendListBaseAdapter);
        }
    }

    /**
     * ContentsAdapterで止めた通信を再度可能な状態にする.
     */
    public void enableContentsAdapterCommunication() {
        DTVTLogger.start();
        // 情報取得のコールバックがなかった場合は、一律プログレスバーを表示
        if (mIsShowProgressBar) {
            if (mData == null || mData.size() == 0) {
                showProgressBar(true);
            }
        }
        if (mRecommendListBaseAdapter != null) {
            mRecommendListBaseAdapter.enableConnect();
        }
    }

    /**
     * コンテンツデータ取得.
     *
     * @return コンテンツリスト
     */
    public List<ContentsData> getData() {
        return mData;
    }

    /**
     * コンテンツ数取得.
     *
     * @return コンテンツ数
     */
    public int getDataSize() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    /**
     * コンテンツデータ追加.
     *
     * @param content コンテンツデータ
     */
    public void addData(final ContentsData content) {
        if (mData != null) {
            mData.add(content);
        }
    }

}