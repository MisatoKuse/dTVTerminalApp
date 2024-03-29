/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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
import com.nttdocomo.android.tvterminalapp.dataprovider.RentalDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentsAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRentalDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * レンタル一覧を表示.
 */
public class PremiumVideoActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,

        RentalDataProvider.ApiDataProviderCallback {
    // region variable
    /** レンタル一覧を表示するリスト. */
    private ListView mListView;
    /** プログレスダイアログ. */
    private RelativeLayout mRelativeLayout = null;
    /** リスト0件メッセージ. */
    private TextView mNoDataMessage;

    /** コンテンツを表示するリストのアダプタ. */
    private ContentsAdapter mContentsAdapter;
    /** レンタル一覧を取得するデータプロパイダ. */
    private RentalDataProvider mRentalDataProvider;
    /** コンテンツ一覧. */
    private List<ContentsData> mContentsList;

    /** コンテンツ詳細表示フラグ. */
    private boolean mContentsDetailDisplay = false;
    /** グローバルメニューからの起動かどうか. */
    private Boolean mIsMenuLaunch = false;
    // endregion variable

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setContentView(R.layout.premium_video_main_layout);
        mContentsList = new ArrayList<>();

        //Headerの設定
        setTitleText(getString(R.string.nav_menu_item_premium_video));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        DTVTLogger.debug("mContentsDetailDisplay = " + mContentsDetailDisplay);
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        if (mContentsDetailDisplay) {
            mContentsDetailDisplay = false;
            List<ContentsData> list = mRentalDataProvider.checkClipStatus(mContentsList);
            mContentsAdapter.setListData(list);
            mContentsAdapter.notifyDataSetChanged();
            DTVTLogger.debug("PremiumVideoActivity::Clip Status Update");
        }
        if (mIsFromBgFlg) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_premium_video),
                    ContentUtils.getParingAndLoginCustomDimensions(PremiumVideoActivity.this));
        } else {
            SparseArray<String> customDimensions  = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
            super.sendScreenView(getString(R.string.google_analytics_screen_name_premium_video), customDimensions);
        }
    }

    /**
     * アダプタを設定.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mListView = findViewById(R.id.premium_video_list);
        mListView.setOnItemClickListener(this);
        mRelativeLayout = findViewById(R.id.premium_video_progress);
        showProgressBar(true);
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_PREMIUM_VIDEO_LIST);
        mListView.setAdapter(mContentsAdapter);
        mNoDataMessage = findViewById(R.id.premium_video_list_no_items);
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        mListView = findViewById(R.id.premium_video_list);
        mRelativeLayout = findViewById(R.id.premium_video_progress);
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

    @Override
    public void rentalListCallback(final List<ContentsData> dataList) {
        final RentalDataProvider dataProvider = new RentalDataProvider(this, RentalDataProvider.RentalType.PREMIUM_VIDEO);
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                if (null == dataList) {
                    dataProvider.getDbRentalList();
                    return;
                }

                mContentsList.clear();
                mContentsList.addAll(dataList);
                mContentsAdapter.notifyDataSetChanged();
                mNoDataMessage.setVisibility(mContentsList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void rentalListNgCallback() {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRelativeLayout.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                showProgressBar(false);
                mNoDataMessage.setVisibility(View.VISIBLE);
                mNoDataMessage.setText(getString(R.string.common_get_data_failed_message));
                if (!NetWorkUtils.isOnline(PremiumVideoActivity.this)) {
                    showDialogToClose(PremiumVideoActivity.this, getResources().getString(R.string.network_nw_error_message_dialog));
                } else {
                    ErrorState errorState = mRentalDataProvider.getError();
                    if (errorState != null) {
                        String message = errorState.getApiErrorMessage(getApplicationContext());
                        if (!TextUtils.isEmpty(message)) {
                            showDialogToClose(PremiumVideoActivity.this, message);
                            return;
                        }
                    }
                    showDialogToClose(PremiumVideoActivity.this);
                }
            }
        });
    }

    @Override
    protected void showDialogToClose(final Context context, final String message) {
        CustomDialog closeDialog = new CustomDialog(context, CustomDialog.DialogType.ERROR);
        closeDialog.setContent(message);
        closeDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                contentsDetailBackKey(null);
            }
        });
        //戻るボタン等でダイアログが閉じられた時もOKと同じ挙動
        closeDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
            @Override
            public void allDismissCallback() {
                //NOP
            }

            @Override
            public void otherDismissCallback() {
                contentsDetailBackKey(null);
            }
        });
        closeDialog.setCancelable(false);
        offerDialog(closeDialog);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        mContentsDetailDisplay = true;

        ContentsData contentsData = mContentsList.get(i);
        if (ContentUtils.isChildContentList(contentsData)) {
            startChildContentListActivity(contentsData);
        } else {
            Intent intent = new Intent(this, ContentDetailActivity.class);
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, getComponentName().getClassName());
            intent.putExtra(ContentUtils.PLALA_INFO_BUNDLE_KEY, contentsData.getContentsId());
            startActivity(intent);
        }

    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (closeDrawerMenu()) {
                    return false;
                }
                if (mIsMenuLaunch) {
                    //メニューから起動の場合ホーム画面に戻る
                    startHomeActivity();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void contentsDetailBackKey(final View view) {
        if (mIsMenuLaunch) {
            //メニューから起動の場合ホーム画面に戻る
            startHomeActivity();
        } else {
            //ランチャーから起動の場合
            finish();
        }
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mRentalDataProvider != null) {
            mRentalDataProvider.enableConnect();
        }
        if (mContentsAdapter != null) {
            mContentsAdapter.enableConnect();
        }
        if (mListView != null) {
            mListView.invalidateViews();
        }

        if (mContentsList.isEmpty()) {
            //コンテンツ情報が無ければ取得を行う
            mRentalDataProvider = new RentalDataProvider(this, RentalDataProvider.RentalType.PREMIUM_VIDEO);
            mRentalDataProvider.getRentalData(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopRentalDataConnect stopConnect = new StopRentalDataConnect();
        stopConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mRentalDataProvider);
        StopContentsAdapterConnect stopAdapterConnect = new StopContentsAdapterConnect();
        stopAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mContentsAdapter);
    }

}