/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;

import java.util.ArrayList;

import static com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI;

public class EditChannelListFragment extends EditChannelListBaseFragment implements AdapterView.OnItemClickListener {

    private static final int GO_FOR_MY_EDIT_CHANNEL_LIST = 0;
    public ArrayList<Channel> mData;
    public ListView mEditChannelListView;
    private ChannelListAdapter mChannelListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        RelativeLayout rootView = new RelativeLayout(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(layoutParams);
        mEditChannelListView = new ListView(mContext);
        mEditChannelListView.setOnItemClickListener(this);
        mEditChannelListView.setLayoutParams(layoutParams);
        rootView.addView(mEditChannelListView);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Channel channel = mData.get(position);
        mContext.onTapChannelListItem(position,channel);
        mContext.mViewPager.setCurrentItem(GO_FOR_MY_EDIT_CHANNEL_LIST);
    }

    @Override
    protected void loadData() {
        if (mData == null) {
            mData = new ArrayList();
        }
        mChannelListAdapter = new ChannelListAdapter(
                getContext(), mData, R.layout.channel_list_item);
        mChannelListAdapter.setChListDataType(CH_LIST_DATA_TYPE_HIKARI);
        mEditChannelListView.setAdapter(mChannelListAdapter);
    }

    /**
     * データ更新表示
     */
    public void notifyDataChanged() {
        mChannelListAdapter.notifyDataSetChanged();
    }

    /**
     * "チャンネルリスト画面"からactivityにアイテム情報を送るインターフェース
     */
    public interface ChannelListItemImpl {
        void onTapChannelListItem(int position, Channel channel);
    }
}