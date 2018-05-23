/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoContentProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopVideoContentConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.VideoGenreListDataInfo;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * ビデオコンテンツ一覧画面.
 */
public class VideoContentListActivity extends BaseActivity implements View.OnClickListener,
        VideoContentProvider.ApiVideoContentDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        AbsListView.OnTouchListener {

    /**
     * メニュー.
     */
    private ImageView mMenuImageView;
    /**
     * ビデオコンテンツ一覧用ビデオコンテンツプロバイダー.
     */
    private VideoContentProvider mVideoContentProvider;
    /**
     * ビデオコンテンツ一覧用アダプター.
     */
    private ContentsAdapter mContentsAdapter;
    /**
     * 検索プログレスバー.
     */
    private View mLoadMoreView;
    /**
     * ビデオコンテンツリストを表示するリスト.
     */
    private ListView mListView = null;
    /**
     * ビデオコンテンツリスト用.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * コンテンツデータ一覧のリスト.
     */
    private List<ContentsData> mContentsList;
    /**
     * データの追加読み込み状態の識別フラグ.
     */
    private boolean mIsCommunicating = false;
    /**
     * ジャンルID.
     */
    private String mGenreId;

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
     * ビデオ一覧（コンテンツツリー）からののIntent KEY.
     */
    private static final String VIDEO_CONTENTS_BUNDLE_KEY = "videoContentKey";
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList<>();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        setStatusBarColor(true);

        // コンテンツツリー画面からのデータ受け取り
        VideoGenreListDataInfo info = getIntent().getParcelableExtra(VIDEO_CONTENTS_BUNDLE_KEY);
        mGenreId = info.getGenreId();
        if (info.getVideoGenreListShowData() == null) {
            setTitleText(getString(R.string.video_list_genre_all_contents));
        } else {
            setTitleText(info.getVideoGenreListShowData().getTitle());
        }
        resetPaging();

        initView();
        showProgressBar(true);
    }

    /**
     * ListViewの表示.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        if (mContentsList == null) {
            mContentsList = new ArrayList<>();
        }
        mListView = findViewById(R.id.tv_rank_list);
        mRelativeLayout = findViewById(R.id.tv_rank_progress);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        //スクロールの上下方向検知用のリスナーを設定
        mListView.setOnTouchListener(this);
        mContentsAdapter = new ContentsAdapter(
                this,
                mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_CONTENT_LIST
        );
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = View.inflate(this, R.layout.search_load_more, null);
        mNoDataMessage  = findViewById(R.id.tv_rank_list_no_items);
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        mListView = findViewById(R.id.tv_rank_list);
        mRelativeLayout = findViewById(R.id.tv_rank_progress);
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            mListView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 再読み込み時の処理.
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 読み込み表示を行う.
     *
     * @param bool 読み込み表示フラグ
     */
    private void displayMoreData(final boolean bool) {
        if (null != mListView) {
            if (bool) {
                mListView.addFooterView(mLoadMoreView);
                //スクロール位置を最下段にすることで、追加した更新フッターを画面内に入れる
                mListView.setSelection(mListView.getMaxScrollAmount());
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * ページングリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (0 != getCurrentNumber(mContentsList) && null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    /**
     * 再読み込み実施フラグ設定.
     *
     * @param bool 読み込み表示フラグ
     */
    private void setCommunicatingStatus(final boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }
    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }

            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;

            if (firstVisibleItem + visibleItemCount == totalItemCount && 0 != totalItemCount) {
                DTVTLogger.debug(
                        "Activity::onScroll, paging, firstVisibleItem=" + firstVisibleItem
                                + ", totalItemCount=" + totalItemCount
                                + ", visibleItemCount=" + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == mContentsAdapter.getCount() - 1) {
                if (mIsCommunicating) {
                    return;
                }

                //スクロール位置がリストの先頭で上スクロールだった場合は、更新をせずに帰る
                if (mFirstVisibleItem == 0 && mLastScrollUp) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");
                displayMoreData(true);
                setCommunicatingStatus(true);
                mNoDataMessage.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int offset = 0;
                        if (null != mContentsList) {
                            offset = mContentsList.size() + 1;
                        }
                        mVideoContentProvider.getVideoContentData(mGenreId, offset);
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        if (mLoadMoreView == view) {
            return;
        }
        ContentsData contentsData = mContentsList.get(position);
        if (ContentUtils.isChildContentList(contentsData)) {
            startChildContentListActivity(contentsData);
        } else {
            Intent intent = new Intent(this, ContentDetailActivity.class);
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, getComponentName().getClassName());
            OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(contentsData, ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
            intent.putExtra(detailData.getRecommendFlg(), detailData);
            startActivity(intent);
        }
    }

    /**
     * 取得結果の設定・表示.
     * @param videoContentInfo  ビデオコンテンツ情報
     */
    private void setShowVideoContent(final List<ContentsData> videoContentInfo) {
        if (null == videoContentInfo) {
            displayMoreData(false);

            //エラーメッセージを取得する
            ErrorState errorState = mVideoContentProvider.getError();
            if (errorState != null) {
                String message = errorState.getApiErrorMessage(getApplicationContext());
                //有無で処理を分ける
                if (!TextUtils.isEmpty(message)) {
                    showDialogToClose(this, message);
                    return;
                }
            }
            showDialogToClose(this);
            return;
        }
        //既にデータが取得された場合表示しない
        if (0 == videoContentInfo.size() && (null == mContentsList || mContentsList.size() == 0)) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            displayMoreData(false);
            return;
        }

        for (int i = 0; i < videoContentInfo.size(); ++i) {
            DTVTLogger.debug("i = " + i);
            if (null != mContentsList) {
                mContentsList.add(videoContentInfo.get(i));
            }
        }
        resetCommunication();
        mContentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void videoContentCallback(final List<ContentsData> videoHashMap) {
        showProgressBar(false);
        setShowVideoContent(videoHashMap);
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
        }
        return false;
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mVideoContentProvider != null) {
            mVideoContentProvider.enableConnect();
        }
        if (mContentsAdapter != null) {
            mContentsAdapter.enableConnect();
        }
        if (mListView != null) {
            mListView.invalidateViews();
        }
        if (mContentsList == null || mContentsList.size() == 0) {
            //コンテンツ情報が無ければ取得を行う
            mVideoContentProvider = new VideoContentProvider(this);
            mVideoContentProvider.getVideoContentData(mGenreId, 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        showProgressBar(false);
        StopVideoContentConnect stopConnect = new StopVideoContentConnect();
        stopConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mVideoContentProvider);
        StopContentsAdapterConnect stopAdapterConnect = new StopContentsAdapterConnect();
        stopAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                 mContentsAdapter);
    }
}
