/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;

import java.util.List;

public class VideoGenreAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private Context mContext = null;
    private List mContentsList = null;

    public VideoGenreAdapter(Context context, List data) {
        this.mContext = context;
        this.mContentsList = data;
    }

    @Override
    public int getCount() {
        return mContentsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mContentsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        VideoGenreAdapter.ViewHolder holder;
        VideoGenreList videoGenreList = new VideoGenreList();
        if (null == view) {
            view = View.inflate(mContext, R.layout.item_video_genre, null);
            holder = new VideoGenreAdapter.ViewHolder();
            holder.genre_title = view.findViewById(R.id.genre_title);
            holder.genre_count = view.findViewById(R.id.genre_count);

            // タップ時用にGenreIdを隠しておく
            holder.genre_id = view.findViewById(R.id.genre_id);
            view.setTag(holder);
        } else {
            holder = (VideoGenreAdapter.ViewHolder) view.getTag();
        }

        // TODO 各データのset

        if (null != holder.genre_title) {
            holder.genre_title.setText(videoGenreList.getTitle());
        }

        if (null != holder.genre_count) {
            holder.genre_count.setText(videoGenreList.getGenreId());
        }

        if (null != holder.genre_id) {
            holder.genre_id.setText(videoGenreList.getGenreId());
        }

        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    static class ViewHolder {
        TextView genre_title;
        TextView genre_count;
        TextView genre_id;
    }
}