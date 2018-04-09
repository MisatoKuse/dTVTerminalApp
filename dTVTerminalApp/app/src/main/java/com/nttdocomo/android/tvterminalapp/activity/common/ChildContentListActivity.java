/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.ChildContentDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 子コンテンツ表示専用アクティビティ
 */
public class ChildContentListActivity extends BaseActivity implements
        ChildContentDataProvider.DataCallback, AdapterView.OnItemClickListener {

    // region variable
    /**
     * リスト表示用アダプタ.
     */
    private ContentsAdapter mContentsAdapter;
    /**
     * ランキングリストを表示するリスト.
     */
    private ListView mListView;
    /**
     * コンテンツデータ一覧のリスト.
     */
    private List<ContentsData> mContentsList;
    /**
     * ProgressBar.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * コンテンツ詳細表示フラグ.
     */
    private boolean mContentsDetailDisplay = false;

    // view
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;


    // data
    private ChildContentDataProvider mChildContentDataProvider;

    public static final String INTENT_KEY_CRID = "crid",
            INTENT_KEY_TITLE = "title",
            INTENT_KEY_DISP_TYPE = "dispType";


    private String mCrid;
    private String mTitle;
    private String mDispType;
    private int mOffset = 1;
    // endregion variable

    // region Activity LifeCycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_content_main_layout);
        mListView = (ListView)findViewById(R.id.child_content_list);

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
                DTVTLogger.debug("Clip Status Update");
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
        mChildContentDataProvider.getChildContentList(mCrid, mOffset, mDispType);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        ContentsData contentsData = mContentsList.get(position);
        if (isChildContentList(contentsData)) {
            startChildContentListActivity(contentsData);
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
                //エラー情報の存在を見る
                // TODO: 後で実装する
/*
                ErrorState errorState = mChildContentDataProvider.getDailyRankWebApiErrorState();
                if(errorState != null) {
                    //エラー情報が存在すれば、DBにデータが無く、通信も失敗しているので、エラーメッセージを出して帰る
                    String message = errorState.getApiErrorMessage(
                            getApplication().getApplicationContext());

                    //メッセージの有無を確認
                    if(TextUtils.isEmpty(message)) {
                        //メッセージが無いので、デフォルトメッセージで表示
                        showDialogToClose(context);
                    } else {
                        //メッセージがあるので表示
                        showDialogToClose(context, message);
                    }
                    return;
                }
*/
                setShowChildContent(contentsDataList);
            }
        });
    }

    /**
     * 取得結果の設定・表示.
     *
     * @param contentsDataList 取得したコンテンツデータリスト
     */
    private void setShowChildContent(final List<ContentsData> contentsDataList) {
        if (null == contentsDataList) {
            showDialogToClose(this);
            return;
        }
        if (0 == contentsDataList.size()) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (null != mContentsList && mContentsList.size() >= contentsDataList.size()) {
            return;
        }

        for (ContentsData info : contentsDataList) {
            if (null != mContentsList) {
                mContentsList.add(info);
            }
            mContentsAdapter.notifyDataSetChanged();
        }
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
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_DAILY_RANK);
        mListView.setAdapter(mContentsAdapter);
        mNoDataMessage  = findViewById(R.id.child_content_list_no_items);
    }

    /**
     * 多階層コンテンツであるか判定する.
     * @param contentsData
     * @return
     */
    private boolean isChildContentList(final ContentsData contentsData) {
        if (null != contentsData) {
            return contentsData.hasChildContentList();
        }
        return false;
    }

    /**
     * ウイザード（多階層コンテンツ）画面を表示する.
     */
    private void startChildContentListActivity(final ContentsData contentsData) {
        Intent intent = new Intent(this, ChildContentListActivity.class);
        intent.putExtra(ChildContentListActivity.INTENT_KEY_CRID, contentsData.getCrid());
        intent.putExtra(ChildContentListActivity.INTENT_KEY_TITLE, contentsData.getTitle());
        intent.putExtra(ChildContentListActivity.INTENT_KEY_DISP_TYPE, contentsData.getDispType());
        startActivity(intent);
    }


}
