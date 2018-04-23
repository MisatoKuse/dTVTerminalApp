/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;

import java.util.List;

/**
 * ビデオジャンルアダプター.
 */
public class VideoGenreAdapter extends BaseAdapter {
    /**コンテキスト.*/
    private Context mContext = null;
    /**ビデオジャンル一覧.*/
    public List<VideoGenreList> mData = null;

    /**
     * コンストラクタ.
     * @param context コンテキスト
     * @param data ビデオジャンル一覧データ
     */
    public VideoGenreAdapter(final Context context, final List<VideoGenreList> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(final int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public View getView(final int position,  View view, final ViewGroup parent) {
        VideoGenreList videoGenreList = mData.get(position);
        ViewHolder holder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.item_video_genre, null);
            holder = new ViewHolder();
            holder.genre_title = view.findViewById(R.id.genre_title);
            holder.content_count = view.findViewById(R.id.content_count);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (!TextUtils.isEmpty(videoGenreList.getTitle())) {
            holder.genre_title.setText(videoGenreList.getTitle());
        }

        if (!TextUtils.isEmpty(videoGenreList.getTitle())) {
            holder.content_count.setText(videoGenreList.getContentCount());
        }
        return view;
    }

    /**
     * ViewHolder.
     */
    static class ViewHolder {
        /**ジャンル名.*/
        TextView genre_title;
        /**コンテンツ数.*/
        TextView content_count;
    }
}