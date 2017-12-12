/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.jni.DlnaHikariChListItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListItem;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;

import java.util.ArrayList;
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
    //サムネイル幅さ display7分の1
    private float THUMBNAIL_WIDTH = 5;
    //サムネイル高さ サムネイル幅さ2分の1
    private static final int THUMBNAIL_HEIGHT = 2;

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
            holder.mThumbnailLayout = view.findViewById(R.id.channel_list_item_thumb_ll);
            holder.mThumbnail = view.findViewById(R.id.channel_list_item_thumb_iv);
            holder.mChannelName = view.findViewById(R.id.channel_list_item_title_tv);
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                case CH_LIST_DATA_TYPE_TER:
                    holder.mThumbnailLayout.setVisibility(View.GONE);
                    break;
                case CH_LIST_DATA_TYPE_HIKARI:
                case CH_LIST_DATA_TYPE_DTV:
                    holder.mThumbnailLayout.setVisibility(View.VISIBLE);
                    holder.mThumbnail.setVisibility(View.VISIBLE);
                    break;
                case CH_LIST_DATA_TYPE_INVALID:
                    break;
            }
            convertView = view;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArrayList<String> nameThumbnail=new ArrayList();
        getDatas(nameThumbnail, position);
        String chName=nameThumbnail.get(0);
        String thumbnail=nameThumbnail.get(1);

        if(null != holder.mChannelName && null!=chName){
            holder.mChannelName.setText(chName);
        }
        holder.mThumbnail.setBackgroundResource(R.drawable.test_image);
        if(null!=holder.mThumbnail && null!=thumbnail){
            holder.mThumbnail.setTag(thumbnail);
            Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.mThumbnail, thumbnail);
            if(null!=bp){
                holder.mThumbnail.setImageBitmap(bp);
            }
        }

        return convertView;
    }

    private void getDatas(ArrayList<String> nameThumbnailOut, int position){
        String chName=null;
        String thumbnail=null;
        if(null!=mData) {
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                    DlnaRecVideoItem bsItem = (DlnaRecVideoItem) mData.get(position);
                    if (null != bsItem) {
                        chName = bsItem.mTitle;
                        //thumbnail = bsItem.mUpnpIcon;
                        thumbnail = null;
                    }
                    break;
                case CH_LIST_DATA_TYPE_TER:
                    DlnaRecVideoItem terItem = (DlnaRecVideoItem) mData.get(position);
                    if (null != terItem) {
                        chName = terItem.mTitle;
                        //thumbnail = terItem.mUpnpIcon;
                        thumbnail = null;
                    }
                    break;
                case CH_LIST_DATA_TYPE_HIKARI:
                    Channel ch= (Channel)mData.get(position);;
                    if (null != ch) {
                        chName = ch.getTitle();
                        thumbnail = ch.getThumbnail();
                    }
                    break;
                case CH_LIST_DATA_TYPE_DTV:
                    Channel ch2= (Channel)mData.get(position);;
                    if (null != ch2) {
                        chName = ch2.getTitle();
                        thumbnail = ch2.getThumbnail();
                    }
                    break;
                case CH_LIST_DATA_TYPE_INVALID:
                    break;
            }
        }
        nameThumbnailOut.add(chName);
        nameThumbnailOut.add(thumbnail);
    }

    class ViewHolder {
        ImageView mThumbnail;
        LinearLayout mThumbnailLayout;
        TextView mChannelName;
    }
}

