/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;

import java.util.ArrayList;


/**
 * チャンネルを選択するリスト画面.
 */
public class SelectChannelActivity extends BaseActivity implements ScaledDownProgramListDataProvider
        .ApiDataProviderCallback, AdapterView.OnItemClickListener {

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
     * チャンネルタイトル.
     */
    private static final String TITLE = "title";
    /**
     * チャンネルリスト選択リスト.
     */
    private ListView mSelectListView;
    /**
     * チャンネルリストコレクション.
     */
    private ArrayList<ChannelInfo> mSelectList;
    /**
     * マイ番組表サービスID.
     */
    private String[] mServiceIds = null;
    /**
     * マイ番組表サービスIDキー.
     */
    private static final String SERVICE_IDS = "service_ids";
    /**
     * ひかりTVチャンネルデータプロバイダー.
     */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppThemeBlack);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_channel_main_layout);
        //Headerの設定
        setHeaderColor(false);
        setTitleText(getString(R.string.my_channel_list_setting_select_channel));
        enableHeaderBackIcon(false);
        enableGlobalMenuIcon(true);
        changeGlobalMenuIcon(false);
        setStatusBarColor(false);
        initView();
        //ここに存在したloadDataメソッドは、処理の重複なので、削除を行いました。
    }

    /**
     * データ初期化.
     */
    private void loadData() {
        mSelectList = new ArrayList<>();
        mServiceIds = getIntent().getStringArrayExtra(SERVICE_IDS);
        mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
    }

    /**
     * view 初期化.
     */
    private void initView() {
        mSelectListView = findViewById(R.id.select_channel_main_layout_edit_lv);
        mSelectListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.header_layout_menu) {
            finish();
        }
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        //何もしない
    }

    /**
     * チャンネルリストデータ取得.
     *
     * @param channels 　画面に渡すチャンネル情報
     */
    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        if (channels != null) {
            if (mSelectList == null) {
                mSelectList = new ArrayList<>();
            }
            mSelectList.clear();
            ArrayList<ChannelInfo> rmChannels = new ArrayList<>();
            for (String mServiceId : mServiceIds) {
                for (int j = 0; j < channels.size(); j++) {
                    if (mServiceId != null) {
                        if (mServiceId.equals(channels.get(j).getServiceId())) {
                            rmChannels.add(channels.get(j));
                        }
                    }
                }
            }
            //重複登録不可とする
            channels.removeAll(rmChannels);
            mSelectList.addAll(channels);
            ChannelListAdapter mChannelListAdapter = new ChannelListAdapter(
                    this, mSelectList, R.layout.channel_list_item);
            mChannelListAdapter.setChListDataType(ChannelListActivity.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
            mSelectListView.setAdapter(mChannelListAdapter);
            mChannelListAdapter.notifyDataSetChanged();
        } else {
            //情報がヌルなので、ネットワークエラーメッセージを取得する
            String message = mScaledDownProgramListDataProvider.
                    getChannelError().getApiErrorMessage(getApplicationContext());

            //メッセージの有無で処理を分ける
            if (TextUtils.isEmpty(message)) {
                //メッセージが無いので、「取得に失敗」のダイアログを表示。OKボタンで本画面は終了
                showDialogToClose(this);
            } else {
                //メッセージがあるので、該当メッセージのダイアログを表示。OKボタンで本画面は終了
                showDialogToClose(this, message);
            }
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
        ChannelInfo channel = mSelectList.get(position);
        Intent intent = new Intent();
        Bundle info = new Bundle();
        info.putString(SERVICE_ID, channel.getServiceId());
        info.putString(TITLE, channel.getTitle());
        intent.putExtra(CHANNEL_INFO, info);
        intent.putExtra(POSITION, position);
        SelectChannelActivity.this.setResult(RESULT_OK, intent);
        SelectChannelActivity.this.finish();
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        //チャンネルリスト通信許可
        if (mScaledDownProgramListDataProvider != null) {
            mScaledDownProgramListDataProvider.enableConnect();
        }
        if (mSelectListView != null) {
            mSelectListView.invalidateViews();
        }
        if (mSelectList == null || mSelectList.size() == 0) {
            loadData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //チャンネルリスト通信を止める
        StopScaledProListDataConnect stopTvConnect = new StopScaledProListDataConnect();
        stopTvConnect.execute(mScaledDownProgramListDataProvider);
    }
}
