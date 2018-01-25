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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.common.SearchServiceType;

import java.util.List;

public class RecommendListBaseAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<ContentsData> mData = null;
    //private int layoutid;
    private ThumbnailProvider mThumbnailProvider = null;

    public RecommendListBaseAdapter(Context context, List data, int id) {
        this.mContext = context;
        this.mData = data;
        //this.layoutid = id;
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
        final ContentsData recommendContentInfo =  mData.get(position);
        ViewHolder holder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.item_recommend_list, null);
            holder = new ViewHolder();
            holder.iv_thumbnail = view.findViewById(R.id.recommend_iv_thumbnail);
            holder.tv_title = view.findViewById(R.id.recommend_title);
            holder.tv_des = view.findViewById(R.id.recommend_des);
            holder.iv_clip = view.findViewById(R.id.recommend_iv_clip);

            final ImageView clipButton = holder.iv_clip;
            //ひかりコンテンツのみクリップボタンを表示する
            if (recommendContentInfo.getServiceId().equals(SearchServiceType.ServiceId.HIKARI_TV_FOR_DOCOMO)) {
                holder.iv_clip.setVisibility(View.VISIBLE);
                holder.iv_clip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、画像比較でクリップ済/未を判定する
                        Bitmap clipButtonBitmap = ((BitmapDrawable) clipButton.getBackground()).getBitmap();
                        Bitmap activeClipBitmap = ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(),
                                R.mipmap.icon_circle_active_clip, null)).getBitmap();
                        if (clipButtonBitmap.equals(activeClipBitmap)) {
                            recommendContentInfo.getRequestData().setClipStatus(true);
                        } else {
                            recommendContentInfo.getRequestData().setClipStatus(false);
                        }
                        //クリップボタンイベント
                        ((BaseActivity) mContext).sendClipRequest(recommendContentInfo.getRequestData(), clipButton);
                    }
                });
            } else {
                holder.iv_clip.setVisibility(View.GONE);
            }

            float mWidth = (float) mContext.getResources().getDisplayMetrics().widthPixels / 3;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) mWidth, (int) mWidth / 2);
            holder.iv_thumbnail.setLayoutParams(layoutParams);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (null != holder.tv_title) {
            holder.tv_title.setText(recommendContentInfo.getTitle());
        }

        if (null != holder.tv_des) {
            holder.tv_des.setText("");
        }

        String searchOk = recommendContentInfo.getSearchOk();
        if (searchOk != null && searchOk.length() > 1) {
            //TODO:クリップボタン状態変更
        }
        holder.iv_thumbnail.setImageResource(R.drawable.test_image);
        if (null != holder.iv_thumbnail) {

            String thumbUrl = recommendContentInfo.getThumURL();
            holder.iv_thumbnail.setTag(thumbUrl);
            Bitmap bp = mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, thumbUrl);
            if (null != bp) {
                holder.iv_thumbnail.setImageBitmap(bp);
            }
        }

        return view;
    }

    static class ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_title;
        TextView tv_des;
        ImageView iv_clip;
    }
}

