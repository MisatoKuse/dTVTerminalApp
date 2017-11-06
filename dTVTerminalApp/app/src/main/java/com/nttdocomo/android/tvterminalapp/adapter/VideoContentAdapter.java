/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;

import java.util.List;

public class VideoContentAdapter extends BaseAdapter {
    private Context mContext = null;
    private List mData = null;

    public VideoContentAdapter(Context context, List data, int id) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.item_video_genre, null);
            holder = new ViewHolder();
            holder.genre_title = view.findViewById(R.id.genre_title);
            holder.genre_count = view.findViewById(R.id.genre_count);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (null != holder.genre_title) {
            holder.genre_title.setText("");
        }

        if (null != holder.genre_count) {
            holder.genre_count.setText("");
        }

        return view;
    }

    static class ViewHolder {
        TextView genre_title;
        TextView genre_count;
    }
}
