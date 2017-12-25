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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.model.search.SearchContentInfo;

import java.util.List;

public class SearchResultBaseAdapter extends BaseAdapter {

    private Context mContext = null;
    private List mData = null;
    //private int layoutid;
    private ThumbnailProvider mThumbnailProvider=null;

    public SearchResultBaseAdapter(Context context, List data, int id) {
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
        SearchContentInfo searchContentInfo = (SearchContentInfo) mData.get(position);
        ViewHolder holder;
        if(null==view){
            view = View.inflate(mContext, R.layout.item_search_result_televi, null);
            holder = new ViewHolder();
            holder.iv_thumbnail = view.findViewById(R.id.iv_thumbnail);
            holder.tv_title = view.findViewById(R.id.tv_title);
            holder.tv_des = view.findViewById(R.id.tv_des);
            holder.bt_clip = view.findViewById(R.id.bt_clip);
            holder.bt_clip.setVisibility(View.GONE);

            holder.bt_clip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //クリップボタンのイベントを親に渡す
//                    ((ListView) parent).performItemClick(mView, position, R.id.item_common_result_clip_tv);
                    //TODO:親に処理を渡すか検討中
                    ((BaseActivity) mContext).sendClipRequest(ClipDataProvider.setClipData());
                }
            });
            float mWidth = (float)mContext.getResources().getDisplayMetrics().widthPixels / 3;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)mWidth,(int)mWidth/2);
            holder.iv_thumbnail.setLayoutParams(layoutParams);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        if(null != holder.tv_title){
            holder.tv_title.setText(searchContentInfo.title);
        }

        if(null != holder.tv_des){
            holder.tv_des.setText("");
        }

        if(searchContentInfo.clipFlag){

        }

        if(null!=holder.iv_thumbnail){

            holder.iv_thumbnail.setTag(searchContentInfo.contentPictureUrl);
            Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, searchContentInfo.contentPictureUrl);
            if(null!=bp){
                holder.iv_thumbnail.setImageBitmap(bp);
            }
        }

        return view;
    }

    static class ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_title;
        TextView tv_des;
        Button bt_clip;
    }
}

