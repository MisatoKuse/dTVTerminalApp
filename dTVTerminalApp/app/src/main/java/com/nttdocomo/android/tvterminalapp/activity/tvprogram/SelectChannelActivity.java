/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;

import java.util.ArrayList;

import static com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI;

/**
 * チャンネルを選択するリスト画面
 */
public class SelectChannelActivity extends BaseActivity implements ScaledDownProgramListDataProvider
        .ApiDataProviderCallback
        , AdapterView.OnItemClickListener {

    /**
     * チャンネル情報キー
     */
    private static final String CHANNEL_INFOS = "channel_info";
    /**
     * 選択中チャンネルポジション
     */
    private static final String POSITION = "position";
    /**
     * チャンネルサービスID
     */
    private static final String SERVICE_ID = "service_id";
    /**
     * チャンネルタイトル
     */
    private static final String TITLE = "title";
    /**
     * チャンネルリスト選択リスト
     */
    private ListView mSelectListView;
    /**
     * チャンネルリストコレクション
     */
    private ArrayList<ChannelInfo> mSelectList;
    /**
     * マイ番組表サービスID
     */
    private String[] mServiceIds = null;
    /**
     * マイ番組表サービスIDキー
     */
    private static final String SERVICE_IDS = "service_ids";
    /**
     * ひかりTVチャンネルデータプロバイダー
     */
    private HikariTvChDataProvider mHikariTvChDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        loadData();
    }

    /**
     * データ初期化
     */
    private void loadData() {
        mServiceIds = getIntent().getStringArrayExtra(SERVICE_IDS);
        mHikariTvChDataProvider = new HikariTvChDataProvider(this);
        mHikariTvChDataProvider.getChannelList(0, 0, "");
    }

    /**
     * view 初期化
     */
    private void initView() {
        mSelectListView = findViewById(R.id.select_channel_main_layout_edit_lv);
        mSelectListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.header_layout_menu) {
            finish();
        }
    }

    @Override
    public void channelInfoCallback(ChannelInfoList channelsInfo) {
        //何もしない
    }

    /**
     * チャンネルリストデータ取得
     *
     * @param channels 　画面に渡すチャンネル情報
     */
    @Override
    public void channelListCallback(ArrayList<ChannelInfo> channels) {
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
            mChannelListAdapter.setChListDataType(CH_LIST_DATA_TYPE_HIKARI);
            mSelectListView.setAdapter(mChannelListAdapter);
            mChannelListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ChannelInfo channel = mSelectList.get(position);
        Intent intent = new Intent();
        Bundle info = new Bundle();
        info.putString(SERVICE_ID, channel.getServiceId());
        info.putString(TITLE, channel.getTitle());
        intent.putExtra(CHANNEL_INFOS, info);
        intent.putExtra(POSITION, position);
        SelectChannelActivity.this.setResult(RESULT_OK, intent);
        SelectChannelActivity.this.finish();
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        //チャンネルリスト通信許可
        if (mHikariTvChDataProvider != null) {
            mHikariTvChDataProvider.enableConnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //チャンネルリスト通信を止める
        StopScaledProListDataConnect stopTvConnect = new StopScaledProListDataConnect();
        stopTvConnect.execute(mHikariTvChDataProvider);
    }
}