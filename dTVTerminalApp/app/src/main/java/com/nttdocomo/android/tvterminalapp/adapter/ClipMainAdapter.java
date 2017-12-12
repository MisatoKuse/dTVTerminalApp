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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipContentInfo;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipContentInfo;

import java.util.List;

public class ClipMainAdapter extends BaseAdapter {

    public enum Mode{
        CLIP_LIST_MODE_TV,
        CLIP_LIST_MODE_VIDEO;
    }

    private Mode mMode= Mode.CLIP_LIST_MODE_TV;

    private Context mContext = null;
    private List mData = null;
    private int layoutid;
    private ThumbnailProvider mThumbnailProvider;

    public ClipMainAdapter(Context context, List data, int id, ThumbnailProvider thumbnailProvider) {
        this.mContext = context;
        this.mData = data;
        this.layoutid = id;
        this.mThumbnailProvider = thumbnailProvider;
    }

    public void setMode(Mode mode){
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
            view = View.inflate(mContext, layoutid, null);
            holder.iv_clip_video_thumbnail = view.findViewById(R.id.iv_clip_video_thumbnail);
            holder.tv_clip_des = view.findViewById(R.id.tv_clip_des);
            holder.rb_clip_video_rating = view.findViewById(R.id.rb_clip_video_rating);
            holder.rb_clip_video_rating_count = view.findViewById(R.id.rb_clip_video_rating_count);
            holder.bt_video_clip = view.findViewById(R.id.bt_video_clip);
            convertView = view;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (mMode) {
            case CLIP_LIST_MODE_TV:
                TvClipContentInfo.TvClipContentInfoItem tvClipContentInfoItem
                        = (TvClipContentInfo.TvClipContentInfoItem) mData.get(position);

                if(null != holder.tv_clip_des){
                    holder.tv_clip_des.setText(tvClipContentInfoItem.mTitle);
                }

                if(null!=holder.iv_clip_video_thumbnail){
                    holder.iv_clip_video_thumbnail.setTag(tvClipContentInfoItem.mContentPictureUrl);
                    if(null!=tvClipContentInfoItem.mContentPictureUrl && 0<tvClipContentInfoItem.mContentPictureUrl.length()){
                        Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.iv_clip_video_thumbnail, tvClipContentInfoItem.mContentPictureUrl);
                        if(null!=bp){
                            holder.iv_clip_video_thumbnail.setImageBitmap(bp);
                        }
                    }
                }

                if(null != holder.rb_clip_video_rating){
                    holder.rb_clip_video_rating.setVisibility(View.INVISIBLE);
                    holder.rb_clip_video_rating_count.setVisibility(View.INVISIBLE);
                }

                if(null != holder.rb_clip_video_rating_count){
                    //holder.rb_clip_video_rating_count.setVisibility(View.INVISIBLE);
                }

                break;
            case CLIP_LIST_MODE_VIDEO:
                VodClipContentInfo.VodClipContentInfoItem clipContentInfoItem
                        = (VodClipContentInfo.VodClipContentInfoItem) mData.get(position);

                if(null != holder.tv_clip_des){
                    holder.tv_clip_des.setText(clipContentInfoItem.mTitle);
                }

                if(null!=holder.iv_clip_video_thumbnail){
                    holder.iv_clip_video_thumbnail.setTag(clipContentInfoItem.mContentPictureUrl);
                    if(null!=clipContentInfoItem.mContentPictureUrl && 0<clipContentInfoItem.mContentPictureUrl.length()){
                        Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.iv_clip_video_thumbnail, clipContentInfoItem.mContentPictureUrl);
                        if(null!=bp){
                            holder.iv_clip_video_thumbnail.setImageBitmap(bp);
                        }
                    }
                }

                if(null != holder.rb_clip_video_rating){
                    holder.rb_clip_video_rating.setNumStars(5);
                    if(null!=clipContentInfoItem.mRating){
                        try {
                            holder.rb_clip_video_rating.setRating(Float.parseFloat(clipContentInfoItem.mRating));
                        }catch (Exception e) {
                            DTVTLogger.debug("ClipMainAdapter.getView, msg=" + e.getCause());
                        }
                    }
                }

                if(null != holder.rb_clip_video_rating_count){
                    //holder.rb_clip_video_rating_count.setVisibility(View.VISIBLE);
                    holder.rb_clip_video_rating_count.setText(clipContentInfoItem.mRating);
                }

                break;
        }

        return convertView;
    }

    public Mode getMode(){
        return mMode;
    }

    class ViewHolder {
        ImageView iv_clip_video_thumbnail;
        TextView tv_clip_des;
        TextView rb_clip_video_rating_count;
        RatingBar rb_clip_video_rating;
        Button bt_video_clip;
    }
}

