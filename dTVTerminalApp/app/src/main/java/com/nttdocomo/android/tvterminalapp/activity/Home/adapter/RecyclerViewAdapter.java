/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.Player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.beans.HomeBeanContent;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private LayoutInflater mInflater;
    private List<HomeBeanContent> mListDatas;
    private Context context;
    private ThumbnailProvider imageLoader;
//    private int index;
    private View mFooterView;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;

    public RecyclerViewAdapter(Context context, List<HomeBeanContent> mListDatas, int index)
    {
        mInflater = LayoutInflater.from(context);
        this.mListDatas = mListDatas;
        this.context = context;
        imageLoader = new ThumbnailProvider(context);
    }

    public View getmFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


    @Override
    public int getItemCount()
    {
        if(mFooterView == null){
            return mListDatas.size();
        }else{
            return mListDatas.size()+1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ViewHolder(mFooterView);
        }
        View view = mInflater.inflate(R.layout.home_main_layout_recyclerview_item,viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImage = view.findViewById(R.id.home_main_recyclerview_item_iv);
        viewHolder.mTxt = view.findViewById(R.id.home_main_recyclerview_item_tv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        if(getItemViewType(i) == TYPE_FOOTER){
            return;
        }
        viewHolder.mImage.setImageResource(R.mipmap.ic_launcher);
        if (!TextUtils.isEmpty(mListDatas.get(i).getContentSrcURL())) {
            viewHolder.mImage.setTag(mListDatas.get(i).getContentSrcURL());
            Bitmap bitmap = imageLoader.getThumbnailImage(viewHolder.mImage, mListDatas.get(i).getContentSrcURL());
            if (bitmap != null) {
                viewHolder.mImage.setImageBitmap(bitmap);
            }
        }
        if(!TextUtils.isEmpty(mListDatas.get(i).getContentName())){
            viewHolder.mTxt.setText(mListDatas.get(i).getContentName());
        }
        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setClass(context,TvPlayerActivity.class);
                context.startActivity(mIntent);
            }
        });
        //viewHolder.mImg.setImageResource(mDatas.get(i));
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View itemView)
        {
            super(itemView);
            if (itemView == mFooterView){
                return;
            }
        }
        ImageView mImage;
        TextView mTxt;
    }
}