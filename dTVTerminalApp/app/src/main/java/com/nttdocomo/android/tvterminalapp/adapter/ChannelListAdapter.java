/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;

import java.util.List;

public class ChannelListAdapter extends BaseAdapter {

    public enum ChListDataType{
        CH_LIST_DATA_TYPE_INVALID,
        CH_LIST_DATA_TYPE_BS,
        CH_LIST_DATA_TYPE_TER,
        CH_LIST_DATA_TYPE_HIKARI,
        CH_LIST_DATA_TYPE_DTV,
    }

    private Context mContext;
    private List mData;
    private int mLayoutId;
    private ThumbnailProvider mThumbnailProvider;
    private ChListDataType mChListDataType;

    public ChannelListAdapter(Context context, List data, int id) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = id;
        this.mThumbnailProvider = new ThumbnailProvider(context);
    }

    public void setChListDataType(ChListDataType type){
        mChListDataType=type;
    }

    @Override
    public int getCount() {
        if(null==mData) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        if(null==mData) {
            return null;
        }
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
            holder.mThumbnail = view.findViewById(R.id.channel_list_item_thumb_iv);
            holder.mChannelName = view.findViewById(R.id.channel_list_item_title_tv);
            convertView = view;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String chName=null;
        String thumbnail=null;
        if(null!=mData) {
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                    DlnaRecVideoItem bsItem = (DlnaRecVideoItem) mData.get(position);
                    if(null!=bsItem){
                        chName=bsItem.mTitle;
                        thumbnail=bsItem.mUpnpIcon;
                    }

                    break;
                case CH_LIST_DATA_TYPE_TER:

                    break;
                case CH_LIST_DATA_TYPE_HIKARI:

                    break;
                case CH_LIST_DATA_TYPE_DTV:

                    break;
                case CH_LIST_DATA_TYPE_INVALID:
                    break;
            }
        }

        if(null != holder.mChannelName && null!=chName){
            holder.mChannelName.setText(chName);
        }

        if(null!=holder.mThumbnail && null!=thumbnail){
            holder.mThumbnail.setTag(thumbnail);
            Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.mThumbnail, thumbnail);
            if(null!=bp){
                holder.mThumbnail.setImageBitmap(bp);
            }
        }

        return convertView;
    }

    class ViewHolder {
        ImageView mThumbnail;
        TextView mChannelName;
    }
}

