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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.model.search.SearchServiceType;

import java.util.List;

public class SearchResultBaseAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<ContentsData> mData = null;
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
        final ContentsData searchContentInfo =  mData.get(position);
        ViewHolder holder;
        if(null==view){
            view = View.inflate(mContext, R.layout.item_search_result_televi, null);
            holder = new ViewHolder();
            holder.iv_thumbnail = view.findViewById(R.id.iv_thumbnail);
            holder.tv_title = view.findViewById(R.id.tv_title);
            holder.tv_des = view.findViewById(R.id.tv_des);
            holder.iv_clip = view.findViewById(R.id.iv_clip);

            //ひかりコンテンツのみクリップボタンを表示する
            if (searchContentInfo.getServiceId().equals(SearchServiceType.ServiceId.HIKARI_TV_FOR_DOCOMO)) {

                holder.iv_clip.setVisibility(View.VISIBLE);

                holder.iv_clip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // クリップ処理
                        ((BaseActivity) mContext).sendClipRequest(searchContentInfo.getRequestData());
                    }
                });
            } else {
                holder.iv_clip.setVisibility(View.GONE);
            }
            float mWidth = (float)mContext.getResources().getDisplayMetrics().widthPixels / 3;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)mWidth,(int)mWidth/2);
            holder.iv_thumbnail.setLayoutParams(layoutParams);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        if(null != holder.tv_title){
            holder.tv_title.setText(searchContentInfo.getTitle());
        }

        if(null != holder.tv_des){
            holder.tv_des.setText("");
        }

        String searchOk = searchContentInfo.getSearchOk();
        if(searchOk != null && searchOk.length() > 0){
            //TODO:クリップボタン状態変更
        }

        if(null!=holder.iv_thumbnail){

            String thumbUrl = searchContentInfo.getThumURL();
            holder.iv_thumbnail.setTag(thumbUrl);
            Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, thumbUrl);
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
        ImageView iv_clip;
    }
}

