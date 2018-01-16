/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;

import java.util.List;

public class WatchListenVideoBaseAdapter extends BaseAdapter
        implements  AbsListView.OnScrollListener {

    private Context mContext = null;
    private List<ContentsData> mData = null;
    //    private int layoutid;
    private ThumbnailProvider mThumbnailProvider = null;

    public WatchListenVideoBaseAdapter(Context context, List data, int id) {
        this.mContext = context;
        this.mData = data;
//        this.layoutid = id;
        mThumbnailProvider = new ThumbnailProvider(mContext);
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
        final ContentsData contentsData =  mData.get(position);
        ViewHolder holder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.item_watching_video, null);
            holder = new ViewHolder();
            holder.wl_thumbnail = view.findViewById(R.id.wl_thumbnail);
            holder.wl_title = view.findViewById(R.id.wl_title);
            holder.wl_video_rating = view.findViewById(R.id.wl_video_rating);
            holder.wl_rating_count = view.findViewById(R.id.wl_rating_count);
            holder.wl_clip = view.findViewById(R.id.wl_clip);

            final ImageView clipButton = holder.wl_clip;
            holder.wl_clip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、画像比較でクリップ済/未を判定する
                    Bitmap clipButtonBitmap = ((BitmapDrawable) clipButton.getBackground()).getBitmap();
                    Bitmap activeClipBitmap = ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(),
                            R.mipmap.icon_circle_active_clip, null)).getBitmap();
                    if (clipButtonBitmap.equals(activeClipBitmap)) {
                        contentsData.getRequestData().setClipStatus(true);
                    } else {
                        contentsData.getRequestData().setClipStatus(false);
                    }
                    //クリップボタンイベント
                    ((BaseActivity) mContext).sendClipRequest(contentsData.getRequestData(), clipButton);
                }
            });
            float mWidth = (float) mContext.getResources().getDisplayMetrics().widthPixels / 3;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) mWidth, (int) mWidth / 2);
            holder.wl_thumbnail.setLayoutParams(layoutParams);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (null != holder.wl_title) {
            holder.wl_title.setText(contentsData.getTitle());
        }

//        if(null != holder.wl_des){
//            holder.wl_des.setText("");
//        }
//
//        if(watchListenVideoInfo.mClipFlag){
//            holder.wl_clip.setVisibility(View.VISIBLE);
//
//        }
        holder.wl_thumbnail.setBackgroundResource(R.mipmap.loading);

        if (null != holder.wl_thumbnail) {

            String thumbUrl = contentsData.getThumURL();
            holder.wl_thumbnail.setTag(thumbUrl);
            if (null != thumbUrl && 0 < thumbUrl.length()) {
                Bitmap bp = mThumbnailProvider.getThumbnailImage(holder.wl_thumbnail, thumbUrl);
                if (null != bp) {
                    holder.wl_thumbnail.setImageBitmap(bp);
                }
            }
        }
        if (null != holder.wl_video_rating) {

            String ratingValue = contentsData.getRatStar();
            holder.wl_video_rating.setRating(Float.parseFloat(ratingValue));
            holder.wl_rating_count.setText(ratingValue);
        }
        if (null != holder.wl_clip) {
            if (contentsData.isClipExec()) {
                if (contentsData.isClipStatus()) {
                    holder.wl_clip.setBackgroundResource(R.mipmap.icon_circle_active_clip);
                } else {
                    holder.wl_clip.setBackgroundResource(R.mipmap.icon_circle_opacity_clip);
                }
            } else {
                holder.wl_clip.setVisibility(View.INVISIBLE);
            }
        }

        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    static class ViewHolder {
        ImageView wl_thumbnail;
        TextView wl_title;
        TextView wl_rating_count;
        //        TextView wl_des;
        ImageView wl_clip;
        RatingBar wl_video_rating;
        ProgressBar wl_progressBar;
    }
}