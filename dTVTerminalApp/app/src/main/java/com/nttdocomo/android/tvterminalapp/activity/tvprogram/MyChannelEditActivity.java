/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.MyChannelEditAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.ArrayList;

/**
 * マイ番組表設定.
 */
public class MyChannelEditActivity extends BaseActivity implements View.OnClickListener,
        MyChannelEditAdapter.EditMyChannelItemImpl,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        MyChannelDataProvider.ApiDataProviderCallback, AbsListView.OnScrollListener {

    //region variable
    /** マイ番組表数.*/
    private static final int EDIT_CHANNEL_LIST_COUNT = WebApiBasePlala.MY_CHANNEL_MAX_INDEX;
    /** マイ番組表データプロバイダー.*/
    private MyChannelDataProvider mMyChannelDataProvider;
    /** 番組表データプロバイダー. */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    /** My番組表一覧. */
    private ArrayList<MyChannelMetaData> mMyChannelMetaData = null;
    /** コンマ.*/
    private static final String COMMA = ",";
    /** bracket left.*/
    private static final String BRACKET_LEFT = "[";
    /** bracket right.*/
    private static final String BRACKET_RIGHT = "]";
    /** チャンネル情報キー.*/
    private static final String CHANNEL_INFO = "channel_info";
    /** チャンネルサービスID.*/
    private static final String SERVICE_ID = "service_id";
    /** マイ番組表サービスIDキー.*/
    private static final String SERVICE_IDS = "service_ids";
    /** チャンネルタイトル.*/
    private static final String TITLE = "title";
    /** チャンネルアダルトフラグ.*/
    private static final String ADULT = "adult";
    /** チャンネルアダルトフラグ値"1".*/
    private static final String ADULT_VALUE_ONE = "1";
    /** マイ番組表編集リスト.*/
    private ListView mEditListView;
    /** マイ番組表データコレクション.*/
    private ArrayList<MyChannelMetaData> mEditList;
    /** 追加ポジション.*/
    private int mAddPosition = 0;
    /** 削除ポジション.*/
    private int mDeletePosition = 0;
    /** WebAPIのコールバックよりも前に2度目のWebAPIが呼び出されるのを防止する.*/
    boolean getOk = true;
    /** 先頭に表示したアイテムポジション.*/
    private int mScrollPosition = 0;
    /** 先頭に表示したアイテムポジション.*/
    private static final String SCROLL_POSITION = "scrollPosition";
    /** 上端からの距離.*/
    private int mScrollTop = 0;
    /** 上端からの距離.*/
    private static final String SCROLL_TOP = "scrollTop";
    // endregion

    // region Activity LifeCycle
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_channel_edit_main_layout);
        if (savedInstanceState != null) {
            mScrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
            mScrollTop = savedInstanceState.getInt(SCROLL_TOP);
            savedInstanceState.clear();
        }
        setTitleText(getString(R.string.my_channel_list_setting));
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);
        initView();
        getOk = true;
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        super.sendScreenView(getString(R.string.google_analytics_screen_name_channel_edit_menu),
                mIsFromBgFlg ? ContentUtils.getParingAndLoginCustomDimensions(MyChannelEditActivity.this) : null);
    }

    /**
     * チャンネル選択画面からのデータを渡す.
     *
     * @param requestCode リクエストコード
     * @param resultCode  結果コード
     * @param data        却ってきた情報
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case 0:
                DTVTLogger.start();
                Bundle info = data.getBundleExtra(CHANNEL_INFO);
                ChannelInfo channel = new ChannelInfo();
                channel.setServiceId(info.getString(SERVICE_ID));
                channel.setTitle(info.getString(TITLE));
                channel.setAdult(info.getString(ADULT));
                //MYチャンネル登録実行
                executeMyChannelListRegister(mAddPosition, channel);
                DTVTLogger.end();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //マイ番組表設定通信を止める
        StopMyProgramListDataConnect stopMyConnect = new StopMyProgramListDataConnect();
        stopMyConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMyChannelDataProvider);
    }
    // endregion

    /**
     * view 初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        findViewById(R.id.header_layout_menu).setOnClickListener(this);
        mEditListView = findViewById(R.id.my_channel_edit_main_layout_edit_lv);
        mEditListView.setOnScrollListener(this);
    }

    /**
     * データ初期化.
     */
    private void loadData() {
        if (getOk) {
            showProgressView(View.VISIBLE);
            mMyChannelDataProvider = new MyChannelDataProvider(this);
            mMyChannelDataProvider.getMyChannelList(R.layout.my_channel_edit_main_layout);
            getOk = false;
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POSITION, mScrollPosition);
        outState.putInt(SCROLL_TOP, mScrollTop);
    }

    /**
     * サーバからデータ取得.
     *
     * @param myChannelMetaData 画面に渡すチャンネル番組情報
     */
    @Override
    public void onMyChannelListCallback(final ArrayList<MyChannelMetaData> myChannelMetaData) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myChannelMetaData != null) {
                    mMyChannelMetaData = myChannelMetaData;
                    //画面表示用チャンネル名を取得するためにチャンネル一覧を取得
                    mScaledDownProgramListDataProvider =
                            new ScaledDownProgramListDataProvider(MyChannelEditActivity.this);
                    mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
                    //for channelListCallback
                } else {
                    //その他のエラーなので、その他のエラーを表示
                    showProgressView(View.GONE);
                    showErrorDialog();
                }
            }
        });
        DTVTLogger.end();
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        final MyChannelEditActivity me = this;
        runOnUiThread(new Runnable() {
            @SuppressWarnings("OverlyLongMethod")
            @Override
            public void run() {
                //APIの実行が終わったので、再実行を許可
                getOk = true;

                if (channels != null) {
                    mEditList = new ArrayList<>();
                    if (mEditList.size() == 0) {
                        for (int i = 1; i <= EDIT_CHANNEL_LIST_COUNT; i++) {
                            MyChannelMetaData myChannelItemData = null;
                            for (int j = 0; j < mMyChannelMetaData.size(); j++) {
                                MyChannelMetaData myChData = mMyChannelMetaData.get(j);
                                //念のため初期値に blank を設定
                                myChData.setDisplayTitle("");
                                for (int k = 0; k < channels.size(); k++) {
                                    //画面表示用チャンネル名はチャンネル一覧のものを使用する
                                    if (myChData.getServiceId().equals(channels.get(k).getServiceId())) {
                                        myChData.setDisplayTitle(channels.get(k).getTitle());
                                        break;
                                    }
                                }
                                int index = Integer.parseInt(myChData.getIndex());
                                if (i == index) {
                                    myChannelItemData = myChData;
                                    break;
                                }
                            }
                            if (myChannelItemData == null) {
                                myChannelItemData = new MyChannelMetaData();
                                myChannelItemData.setIndex(String.valueOf(i));
                            }
                            mEditList.add(myChannelItemData);
                        }
                    } else {
                        ArrayList<MyChannelMetaData> rmList = new ArrayList<>();
                        for (int i = 0; i < mMyChannelMetaData.size(); i++) {
                            for (int j = 0; j < mEditList.size(); j++) {
                                String myChannelItemServiceId = mMyChannelMetaData.get(i).getServiceId();
                                if (myChannelItemServiceId != null) {
                                    if (myChannelItemServiceId.equals(mEditList.get(j).getServiceId())) {
                                        rmList.add(mMyChannelMetaData.get(i));
                                        break;
                                    }
                                }
                            }
                        }
                        mMyChannelMetaData.removeAll(rmList);
                        mEditList.addAll(mMyChannelMetaData);
                    }
                    MyChannelEditAdapter myEditAdapter = new MyChannelEditAdapter(me, mEditList);
                    mEditListView.setAdapter(myEditAdapter);
                    myEditAdapter.notifyDataSetChanged();
                } else {
                    //その他のエラーなので、その他のエラーを表示
                    showErrorDialog();
                }
            }
        });
        if (mEditListView != null) {
            mEditListView.setSelectionFromTop(mScrollPosition, mScrollTop);
        }
        showProgressView(View.GONE);
        DTVTLogger.end();
    }

    /**
     * 登録結果返し.
     *
     * @param result 登録状態
     */
    @Override
    public void onMyChannelRegisterCallback(final String result) {
        if (JsonConstants.META_RESPONSE_STATUS_OK.equals(result)) {
            //そのまま通信してデータ取得
            loadData();
        } else {
            showProgressView(View.GONE);
            showFailedDialog(getResources().getString(R.string
                    .my_channel_list_setting_failed_dialog_content_register));
        }
    }

    /**
     * 動作失敗ダイアログ.
     *
     * @param content コンテキスト
     */
    private void showFailedDialog(final String content) {
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        customDialog.setContent(content);
        customDialog.showDialog();
    }

    /**
     * 解除結果返し.
     *
     * @param result 解除状態
     */
    @Override
    public void onMyChannelDeleteCallback(final String result) {
        if (JsonConstants.META_RESPONSE_STATUS_OK.equals(result)) {
            //そのまま通信してデータ取得
            loadData();
        } else {
            showProgressView(View.GONE);
            showFailedDialog(getResources().getString(R.string
                    .my_channel_list_setting_failed_dialog_content_unregister));
        }
    }

    /**
     * くるくる処理.
     *
     * @param visible 進捗バー
     */
    private void showProgressView(final int visible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setRemoteProgressVisible(visible);
            }
        });
    }

    /**
     * マイ番組表設定登録.
     *
     * @param position ポジション
     * @param channel チャンネル情報
     */
    private void executeMyChannelListRegister(final int position, final ChannelInfo channel) {
        showProgressView(View.VISIBLE);
        mMyChannelDataProvider = new MyChannelDataProvider(this);
        mMyChannelDataProvider.getMyChannelRegisterStatus(channel.getServiceId(),
                channel.getTitle(), WebApiBasePlala.MY_CHANNEL_R_VALUE_G,
                getAdultType(channel.getAdult()), position + 1);
    }

    /**
     * チャンネルのアダルトタイプ取得.
     *
     * @param adult Channelアダルトフラグ
     * @return チャンネルのアダルトタイプ
     */
    private String getAdultType(final String adult) {
        if (ADULT_VALUE_ONE.equals(adult)) {
            return WebApiBasePlala.MY_CHANNEL_ADULT_TYPE_ADULT;
        }
        return WebApiBasePlala.MY_CHANNEL_ADULT_TYPE_EMPTY;
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (closeDrawerMenu()) {
                return false;
            }
        }
        return !checkRemoteControllerView() && super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.header_layout_menu || view.getId() == R.id.header_layout_back) {
            super.onClick(view);
        }
    }

    /**
     * マイ番組表サービスIDを纏めて取得する.
     *
     * @return  マイ番組表サービスIDリスト
     */
    private String[] getServiceIds() {
        String str;
        StringBuilder buf = new StringBuilder();
        for (MyChannelMetaData myChannelData : mEditList) {
            if (myChannelData.getServiceId() != null) {
                str = StringUtils.getConnectStrings(myChannelData.getServiceId(), COMMA);
                buf.append(str);
            }
        }
        return  buf.toString().split(MyChannelEditAdapter.COMMA);
    }

    /**
     * 解除確認ダイアログ表示.
     *
     * @param bundle 　削除する情報
     */
    private void showDialogToConfirmUnRegistration(final Bundle bundle) {
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        customDialog.setContent(BRACKET_LEFT + mEditList.get(mDeletePosition).getDisplayTitle() + BRACKET_RIGHT
                + getResources().getString(R.string.my_channel_list_setting_dialog_content_unregister));
        customDialog.setConfirmText(R.string.positive_response);
        customDialog.setCancelText(R.string.negative_response);
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                if (isOK && bundle != null) {
                    //解除通信
                    showProgressView(View.VISIBLE);
                    mMyChannelDataProvider.getMyChannelDeleteStatus(bundle.getString(MyChannelEditAdapter.SERVICE_ID_MY_CHANNEL_LIST));
                }
            }
        });
        customDialog.showDialog();
    }

    @Override
    public void onAddChannelItem(final int position) {
        //画面遷移
        if (isFastClick()) {
            mAddPosition = position;
            Intent intent = new Intent(this, SelectChannelActivity.class);
            intent.putExtra(SERVICE_IDS, getServiceIds());
            startActivityForResult(intent, 0);
        }
    }

    /**
     * 解除ボタン.
     *
     * @param bundle 削除する情報
     */
    @Override
    public void onRemoveChannelItem(final Bundle bundle, final int position) {
        mDeletePosition = position;
        showDialogToConfirmUnRegistration(bundle);
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        //マイ番組表通信許可
        if (mMyChannelDataProvider != null) {
            mMyChannelDataProvider.enableConnect();
        }
        if (mEditListView != null) {
            mEditListView.invalidateViews();
        }
        if (mEditList == null || mEditList.size() == 0) {
            //トークンエラー以外ならば再読み込み
            loadData();
        }
    }

    /**
     * エラーダイアログを表示する.
     */
    private void showErrorDialog() {
        ErrorState errorState = mMyChannelDataProvider.getMyChannelListError();
        if (errorState == null || errorState.getErrorType() == DtvtConstants.ErrorType.SUCCESS) {
            return;
        }

        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        customDialog.setContent(errorState.getApiErrorMessage(this));
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                finish();
            }
        });
        customDialog.showDialog();
    }
    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mScrollPosition = mEditListView.getFirstVisiblePosition();
        }
        View v = mEditListView .getChildAt(0);
        mScrollTop = (v == null) ? 0 : v.getTop();
    }

    @Override
    public void onScroll(final AbsListView absListView, final  int i, final int i1, final  int i2) {
    }
}
