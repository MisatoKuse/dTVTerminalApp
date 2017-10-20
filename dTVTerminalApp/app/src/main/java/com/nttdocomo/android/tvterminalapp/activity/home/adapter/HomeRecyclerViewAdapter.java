/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser.VIDEORANK_LIST_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser.VIDEORANK_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser.VIDEORANK_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE;

import java.util.List;
import java.util.Map;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>
{
    private LayoutInflater mInflater;
    private List<Map<String,String>> mContentList;
    private Context context;
    //サムネイル取得プロバイダー
    private ThumbnailProvider thumbnailProvider;
    //もっと見るフッター
    private View mFooterView;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;

    public HomeRecyclerViewAdapter(Context context, List<Map<String,String>> mContentList)
    {
        mInflater = LayoutInflater.from(context);
        this.mContentList = mContentList;
        this.context = context;
        thumbnailProvider = new ThumbnailProvider(context);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
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
            return mContentList.size();
        }else{
            return mContentList.size()+1;
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
        //コンテンツキャッシュを幅さ、長さ初期化
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels / 3 * 2;
        float heightPixels = widthPixels / 1.8f;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                (int)widthPixels,
                (int)heightPixels);
        viewHolder.mImage.setLayoutParams(lp);
        viewHolder.mContent = view.findViewById(R.id.home_main_recyclerview_item_tv_content);
        viewHolder.mTime = view.findViewById(R.id.home_main_recyclerview_item_tv_time);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        if(getItemViewType(i) == TYPE_FOOTER){
            return;
        }
        String date = mContentList.get(i).get(VIDEORANK_LIST_START_DATE);
        String title = mContentList.get(i).get(VIDEORANK_LIST_TITLE);
        if(TextUtils.isEmpty(title)){
            title = mContentList.get(i).get(RECOMMENDCHANNEL_LIST_TITLE);
        }
        String thumbnail = mContentList.get(i).get(VIDEORANK_LIST_THUMB);
        if(TextUtils.isEmpty(thumbnail)){
            thumbnail = mContentList.get(i).get(RECOMMENDCHANNEL_LIST_CTPICURL1);
        }
        if(!TextUtils.isEmpty(title)){
            viewHolder.mContent.setVisibility(View.VISIBLE);
            viewHolder.mContent.setText(title);
        }else {
            viewHolder.mContent.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(date)){
            viewHolder.mTime.setVisibility(View.VISIBLE);
            viewHolder.mTime.setText(date);
        }else{
            viewHolder.mTime.setVisibility(View.GONE);
        }
        viewHolder.mImage.setImageResource(R.drawable.test_image);
        //URLによって、サムネイル取得
        if (!TextUtils.isEmpty(thumbnail)) {
            viewHolder.mImage.setTag(thumbnail);
            Bitmap bitmap = thumbnailProvider.getThumbnailImage(viewHolder.mImage, thumbnail);
            if (bitmap != null) {
                viewHolder.mImage.setImageBitmap(bitmap);
            }
        }
        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setClass(context,TvPlayerActivity.class);
                context.startActivity(mIntent);
            }
        });
    }

    /**
     * コンテンツビューを初期化
     */
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View itemView)
        {
            super(itemView);
            if (itemView == mFooterView){
            }
        }
        ImageView mImage;
        TextView mContent;
        TextView mTime;
    }
}