/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.ChildContentDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ActivityUtil;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 子コンテンツ表示専用アクティビティ
 */
public class ChildContentListActivity extends BaseActivity implements
        ChildContentDataProvider.DataCallback,
        AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener,
        AbsListView.OnTouchListener
{

    // region variable
    // view
    /**
     * ランキングリストを表示するリスト.
     */
    private ListView mListView;
    /**
     * ProgressBar.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;
    /**
     * 検索プログレスバー.
     */
    private View mLoadMoreView;


    // data
    public static final String INTENT_KEY_CRID = "crid",
            INTENT_KEY_TITLE = "title",
            INTENT_KEY_DISP_TYPE = "dispType";

    private ChildContentDataProvider mChildContentDataProvider;
    /**
     * リスト表示用アダプタ.
     */
    private ContentsAdapter mContentsAdapter;

    /**
     * コンテンツデータ一覧のリスト.
     */
    private List<ContentsData> mContentsList;
    /**
     * コンテンツ詳細表示フラグ.
     */
    private boolean mContentsDetailDisplay = false;
    private String mCrid;
    private String mTitle;
    private String mDispType;

    /**
     * データの追加読み込み状態の識別フラグ.
     */
    private boolean mIsCommunicating = false;
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

    // endregion variable

    // region Activity LifeCycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_content_main_layout);

        Intent intent = getIntent();

        mCrid = intent.getStringExtra(INTENT_KEY_CRID);
        mTitle = intent.getStringExtra(INTENT_KEY_TITLE);
        mDispType = intent.getStringExtra(INTENT_KEY_DISP_TYPE);

        DTVTLogger.debug("mCrid = " + mCrid + ", mTitle = " + mTitle);
        setTitleText(mTitle);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        if (mContentsDetailDisplay) {
            mContentsDetailDisplay = false;
            if (null != mContentsAdapter) {
                List<ContentsData> list = mChildContentDataProvider.checkClipStatus(mContentsList);
                mContentsAdapter.setListData(list);
                mContentsAdapter.notifyDataSetChanged();
            }
        }
        DTVTLogger.end();
    }
    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
    }
    // endregion Activity LifeCycle

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        if (mChildContentDataProvider != null) {
            mChildContentDataProvider.enableConnect();
        }

        //コンテンツ情報が無ければ取得を行う
        mChildContentDataProvider = new ChildContentDataProvider(this);
        mChildContentDataProvider.getChildContentList(mCrid, 1, mDispType);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        ContentsData contentsData = mContentsList.get(position);
        if (ActivityUtil.isChildContentList(contentsData)) {
            ActivityUtil.startChildContentListActivity(this, contentsData);
        } else {
            Intent intent = new Intent(this, ContentDetailActivity.class);
            intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
            OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(contentsData, ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
            intent.putExtra(detailData.getRecommendFlg(), detailData);
            //コンテンツ詳細表示フラグを有効にする
            mContentsDetailDisplay = true;
            startActivity(intent);
        }
    }

    @Override
    public void childContentListCallback(@Nullable final List<ContentsData> contentsDataList) {
        DTVTLogger.warning("list.size() = " + contentsDataList.size());
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                displayChildContentList(contentsDataList);
            }
        });
    }

    /**
     * 取得結果の設定・表示.
     *
     * @param contentsDataList 取得したコンテンツデータリスト
     */
    private void displayChildContentList(final List<ContentsData> contentsDataList) {
        DTVTLogger.warning(">>> contentsDataList.size() = " + contentsDataList.size());
        if (null == contentsDataList) {
            displayMoreData(false);

            //エラーメッセージを取得する
            ErrorState errorState = mChildContentDataProvider.getError();
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

        for (ContentsData item: contentsDataList) {
            mContentsList.add(item);
        }

        resetCommunication();
        mContentsAdapter.notifyDataSetChanged();
        DTVTLogger.end();
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        mListView = findViewById(R.id.child_content_list);
        mRelativeLayout = findViewById(R.id.child_content_progress);
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
     * 画面初期設定.
     */
    private void initView() {
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        if (mContentsList == null) {
            mContentsList = new ArrayList<>();
        }
        mListView = findViewById(R.id.child_content_list);
        mListView.setOnItemClickListener(this);

        mRelativeLayout = findViewById(R.id.child_content_progress);

        mListView.setOnScrollListener(this);

        //スクロールの上下方向検知用のリスナーを設定
        mListView.setOnTouchListener(this);
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_DAILY_RANK);
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = View.inflate(this, R.layout.search_load_more, null);
        mNoDataMessage  = findViewById(R.id.child_content_list_no_items);
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
    public void onScroll(final AbsListView absListView, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }
            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;
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
                DTVTLogger.warning("mIsCommunicating = " + mIsCommunicating);
                if (mIsCommunicating) {
                    return;
                }
                DTVTLogger.warning("mFirstVisibleItem = " + mFirstVisibleItem + ", mLastScrollUp = " + mLastScrollUp);
                //スクロール位置がリストの先頭で上スクロールだった場合は、更新をせずに帰る
                if (mFirstVisibleItem == 0 && mLastScrollUp) {
                    return;
                }

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
                        mChildContentDataProvider.getChildContentList(mCrid, offset, mDispType);
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
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
        DTVTLogger.warning("displayMoreDatadisplayMoreDatadisplayMoreData");
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
                break;
        }
        return false;
    }
}
