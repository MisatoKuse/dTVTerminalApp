/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipContentInfo;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingConstants;

import java.util.List;

public class RankingAdapter extends BaseAdapter {


    private int mMode = 0;

    private Context mContext = null;
    private List mData = null;
    private int mLayoutId;
    private ThumbnailProvider mThumbnailProvider;

    public RankingAdapter(Context context, List data, int id, ThumbnailProvider thumbnailProvider) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = id;
        this.mThumbnailProvider = thumbnailProvider;
    }

    public void setMode(int mode){
        mMode=mode;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, mLayoutId, null);
            // サムネイル
            holder.contents_thumbnail = view.findViewById(R.id.ranking_list_item_thumbnail);
            // ランキング
            holder.ranking_no = view.findViewById(R.id.ranking_list_item_ranking_no);
            // タイトル
            holder.contents_title = view.findViewById(R.id.ranking_list_item_title);
            // 開始時間
            holder.start_time = view.findViewById(R.id.ranking_list_item_time);
            // レート（☆表示）
            holder.video_rating = view.findViewById(R.id.ranking_list_item_video_rating);
            // レート（数値）
            holder.video_rating_count = view.findViewById(R.id.ranking_list_item_video_rating_count);
            // クリップボタン
            holder.clip_btn = view.findViewById(R.id.ranking_list_item_clip_btn);
            convertView = view;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (mMode) {
            case RankingConstants.RankingModeNo.RANKING_MODE_NO_OF_WEEKLY:
                TvClipContentInfo.TvClipContentInfoItem tvClipContentInfoItem
                        = (TvClipContentInfo.TvClipContentInfoItem) mData.get(position);

                // タイトルの設定
                if(null != holder.contents_title){
                    holder.contents_title.setText(tvClipContentInfoItem.mTitle);
                }

                // サムネイルの設定
                if(null!=holder.contents_thumbnail){
                    holder.contents_thumbnail.setTag(tvClipContentInfoItem.mContentPictureUrl);
                    Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.contents_thumbnail, tvClipContentInfoItem.mContentPictureUrl);
                    if(null!=bp){
                        holder.contents_thumbnail.setImageBitmap(bp);
                    }
                }
                // レートの表示なしの為非表示
                if(null != holder.video_rating){
                    holder.video_rating.setVisibility(View.INVISIBLE);
                    holder.video_rating_count.setVisibility(View.INVISIBLE);
                }

                // 開始時間表示あり
                if(null != holder.start_time) {
                    holder.start_time.setVisibility(View.VISIBLE);
                    holder.start_time.setText("");
                }

                break;
            case RankingConstants.RankingModeNo.RANKING_MODE_NO_OF_VIDEO:
                // TODO ビデオランキング

                break;
        }

        return convertView;
    }

    class ViewHolder {
        ImageView contents_thumbnail;
        TextView ranking_no;
        TextView contents_title;
        TextView start_time;
        TextView video_rating_count;
        RatingBar video_rating;
        Button clip_btn;
    }
}

