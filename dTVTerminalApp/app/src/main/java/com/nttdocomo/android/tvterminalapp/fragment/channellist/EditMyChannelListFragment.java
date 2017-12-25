/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.adapter.EditMyChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;

import java.util.ArrayList;

public class EditMyChannelListFragment extends EditChannelListBaseFragment {

    public ListView mEditListView;
    public ArrayList<MyChannelMetaData> editDatas;
    EditMyChannelListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = new RelativeLayout(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(layoutParams);
        mEditListView = new ListView(mContext);
        mEditListView.setLayoutParams(layoutParams);
        rootView.addView(mEditListView);
        return rootView;
    }

    @Override
    protected void loadData() {
        if (editDatas == null) {
            editDatas = new ArrayList<>();
        }
        mAdapter = new EditMyChannelListAdapter(mContext, editDatas);
        mEditListView.setAdapter(mAdapter);
    }

    public void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }
}