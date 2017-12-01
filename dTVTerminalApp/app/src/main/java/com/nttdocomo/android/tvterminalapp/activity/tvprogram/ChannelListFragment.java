/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;

import java.util.ArrayList;

public class ChannelListFragment extends Fragment {

    private ArrayList<View> arrayList;

    public ChannelListFragment(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View channelListBodyView = LayoutInflater.from(getContext()).inflate(R.layout.channel_list_content, null);
        ListView listContentListView = channelListBodyView.findViewById(R.id.channel_list_content_body_lv);
        listContentListView.setAdapter(new ChannelListBodyAdapter());
        return channelListBodyView;
    }

    class ChannelListBodyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public View getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return arrayList.get(i);
        }
    }
}
