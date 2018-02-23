/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.MyChannelEditAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.ArrayList;

import static com.nttdocomo.android.tvterminalapp.adapter.MyChannelEditAdapter.SERVICE_ID_MY_CHANNEL_LIST;
import static com.nttdocomo.android.tvterminalapp.common.JsonConstants.META_RESPONSE_STATUS_OK;
import static com.nttdocomo.android.tvterminalapp.view.CustomDialog.DialogType.CONFIRM;
import static com.nttdocomo.android.tvterminalapp.view.CustomDialog.DialogType.ERROR;
import static com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala.MY_CHANNEL_MAX_INDEX;

/**
 * マイ番組表設定.
 */
public class MyChannelEditActivity extends BaseActivity implements View.OnClickListener,
        MyChannelEditAdapter.EditMyChannelItemImpl,
        MyChannelDataProvider.ApiDataProviderCallback {

    /**
     * マイ番組表数.
     */
    private static final int EDIT_CHANNEL_LIST_COUNT = MY_CHANNEL_MAX_INDEX;
    /**
     * マイ番組表データプロバイダー.
     */
    private MyChannelDataProvider mMyChannelDataProvider;
    /**
     * コンマ.
     */
    private static final String COMMA = ",";
    /**
     * チャンネル情報キー.
     */
    private static final String CHANNEL_INFO = "channel_info";
    /**
     * 選択中チャンネルポジション.
     */
    private static final String POSITION = "position";
    /**
     * チャンネルサービスID.
     */
    private static final String SERVICE_ID = "service_id";
    /**
     * マイ番組表サービスIDキー.
     */
    private static final String SERVICE_IDS = "service_ids";
    /**
     * チャンネルタイトル.
     */
    private static final String TITLE = "title";
    /**
     * マイ番組表編集リスト.
     */
    private ListView mEditListView;
    /**
     * マイ番組表データコレクション.
     */
    private ArrayList<MyChannelMetaData> mEditList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_channel_edit_main_layout);
        //Headerの設定
        setTitleText(getString(R.string.my_channel_list_setting));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);
        initView();
        loadData();
    }

    /**
     * view 初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        findViewById(R.id.header_layout_menu).setOnClickListener(this);
        mEditListView = findViewById(R.id.my_channel_edit_main_layout_edit_lv);
    }

    /**
     * データ初期化.
     */
    private void loadData() {
        mMyChannelDataProvider = new MyChannelDataProvider(this);
        mMyChannelDataProvider.getMyChannelList(R.layout.my_channel_edit_main_layout);
    }

    /**
     * サーバからデータ取得.
     *
     * @param myChannelMetaData 画面に渡すチャンネル番組情報
     */
    @Override
    public void onMyChannelListCallback(final ArrayList<MyChannelMetaData> myChannelMetaData) {
        DTVTLogger.start();
        if (myChannelMetaData != null) {
            mEditList = new ArrayList<>();
            if (mEditList.size() == 0) {
                for (int i = 1; i <= EDIT_CHANNEL_LIST_COUNT; i++) {
                    MyChannelMetaData myChannelItemData = null;
                    for (int j = 0; j < myChannelMetaData.size(); j++) {
                        MyChannelMetaData myChData = myChannelMetaData.get(j);
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
                for (int i = 0; i < myChannelMetaData.size(); i++) {
                    for (int j = 0; j < mEditList.size(); j++) {
                        String myChannelItemServiceId = myChannelMetaData.get(i).getServiceId();
                        if (myChannelItemServiceId != null) {
                            if (myChannelItemServiceId.equals(mEditList.get(j).getServiceId())) {
                                rmList.add(myChannelMetaData.get(i));
                                break;
                            }
                        }
                    }
                }
                myChannelMetaData.removeAll(rmList);
                mEditList.addAll(myChannelMetaData);
            }
            MyChannelEditAdapter myEditAdapter = new MyChannelEditAdapter(this, mEditList);
            mEditListView.setAdapter(myEditAdapter);
            myEditAdapter.notifyDataSetChanged();
        }
        DTVTLogger.end();
    }

    /**
     * 登録結果返し.
     *
     * @param result 登録状態
     */
    @Override
    public void onMyChannelRegisterCallback(final String result) {
        switch (result) {
            case META_RESPONSE_STATUS_OK:
                //そのまま通信してデータ取得
                loadData();
                break;
            default:
                showFailedDialog(getResources().getString(R.string
                        .my_channel_list_setting_failed_dialog_content_register));
                break;
        }
    }

    /**
     * 動作失敗ダイアログ.
     *
     * @param content 動作失敗文言
     */
    private void showFailedDialog(final String content) {
        CustomDialog customDialog = new CustomDialog(this, ERROR);
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
        switch (result) {
            case META_RESPONSE_STATUS_OK:
                //そのまま通信してデータ取得
                loadData();
                break;
            default:
                showFailedDialog(getResources().getString(R.string
                        .my_channel_list_setting_failed_dialog_content_unregister));
                break;
        }
    }

    /**
     * マイ番組表設定登録.
     *
     * @param position ポジション
     * @param channel  　チャンネル情報
     */
    private void executeMyChannelListRegister(final int position, final ChannelInfo channel) {
        mMyChannelDataProvider = new MyChannelDataProvider(this);
        mMyChannelDataProvider.getMyChannelRegisterStatus(channel.getServiceId(),
                channel.getTitle(), WebApiBasePlala.MY_CHANNEL_R_VALUE_G,
                WebApiBasePlala.MY_CHANNEL_ADULT_TYPE_ADULT, position + 1);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        return !checkRemoteControllerView() && super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.header_layout_menu) {
            super.onClick(view);
        }
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
                int position = data.getExtras().getInt(POSITION);
                ChannelInfo channel = new ChannelInfo();
                channel.setServiceId(info.getString(SERVICE_ID));
                channel.setTitle(info.getString(TITLE));
                //MYチャンネル登録実行
                executeMyChannelListRegister(position, channel);
                DTVTLogger.end();
                break;
            default:
                break;
        }
    }

    /**
     * マイ番組表サービスIDを纏めて取得する.
     *
     * @return  マイ番組表サービスIDリスト
     */
    private String[] getServiceIds() {
        StringBuilder sb = new StringBuilder();
        for (MyChannelMetaData myChannelData : mEditList) {
            if (myChannelData.getServiceId() != null) {
                sb.append(myChannelData.getServiceId());
                sb.append(COMMA);
            }
        }
        return sb.toString().split(MyChannelEditAdapter.COMMA);
    }

    /**
     * 解除確認ダイアログ表示.
     *
     * @param bundle 　削除する情報
     */
    private void showDialogToConfirmUnRegistration(final Bundle bundle) {
        CustomDialog customDialog = new CustomDialog(this, CONFIRM);
        customDialog.setContent(this.getResources().getString(R.string.my_channel_list_setting_dialog_content_unregister));
        customDialog.setConfirmText(R.string.positive_response);
        customDialog.setCancelText(R.string.negative_response);
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                if (isOK && bundle != null) {
                    //解除通信
                    mMyChannelDataProvider.getMyChannelDeleteStatus(bundle.getString(SERVICE_ID_MY_CHANNEL_LIST));
                }
            }
        });
        customDialog.showDialog();
    }

    @Override
    public void onAddChannelItem() {
        //画面遷移
        Intent intent = new Intent(this, SelectChannelActivity.class);
        intent.putExtra(SERVICE_IDS, getServiceIds());
        startActivityForResult(intent, 0);
    }

    /**
     * 解除ボタン.
     *
     * @param bundle 削除する情報
     */
    @Override
    public void onRemoveChannelItem(final Bundle bundle) {
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
            loadData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //マイ番組表設定通信を止める
        StopMyProgramListDataConnect stopMyConnect = new StopMyProgramListDataConnect();
        stopMyConnect.execute(mMyChannelDataProvider);
    }
}