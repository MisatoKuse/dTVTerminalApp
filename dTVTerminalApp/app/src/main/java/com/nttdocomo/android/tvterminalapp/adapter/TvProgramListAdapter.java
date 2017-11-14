/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.app.Activity;
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
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TvProgramListAdapter extends RecyclerView.Adapter<TvProgramListAdapter.MyViewHolder> {

    private Activity mContext = null;
    //ディスプレイ幅さ
    private int screenWidth = 0;
    //ディスプレイ高さ
    private int screenHeight = 0;
    //サムネイル取得プロバイダー
    private ThumbnailProvider thumbnailProvider;
    //現在時刻
    private String curDate = null;
    //現在時刻フォマード
    private static final String CUR_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";
    private List<ItemViewHolder> mItemViews = new ArrayList<>();
    //番組データ
    private List<Channel> mProgramList = null;

    public TvProgramListAdapter(Activity mContext, ArrayList<Channel> mProgramList) {
        this.mProgramList = mProgramList;
        this.mContext = mContext;
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        thumbnailProvider = new ThumbnailProvider(mContext);
        getCurTime();
        for(int i=0;i<mProgramList.size();i++){
            Channel itemChannel =  mProgramList.get(i);
            boolean isLast = false;
            if (itemChannel != null && itemChannel.getSchedules() != null) {
                ArrayList<Schedule> itemSchedules=  itemChannel.getSchedules();
                for (int j = 0; j < itemSchedules.size(); j++) {
                    Schedule itemSchedule = itemSchedules.get(j);
                    if(j == itemSchedules.size()-1){
                        isLast = true;
                    }
                    ItemViewHolder itemViewHolder = new ItemViewHolder();
                    setView(itemViewHolder, itemSchedule, isLast);
                    mItemViews.add(itemViewHolder);
                }
            }
        }
    }

    /**
     * 機能
     * 現在時刻取得
     */
    private void getCurTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
        curDate = sdf.format(c.getTime());
    }

    private ItemViewHolder getUnused(){
        for(ItemViewHolder view: mItemViews){
            if(!view.inUsage){
                return view;
            }
        }
        return null;
    }

    /**
     * 機能
     * 番組詳細ビュー共通化
     */
    private class ItemViewHolder {
        public View view;
        private boolean inUsage;

        TextView startM;
        TextView content;
        ImageView thumbnail;
        RelativeLayout.LayoutParams layoutParams;

        void setUsing(){
            inUsage=true;
        }

        ItemViewHolder() {
            view = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item_panel, null, false);
            startM = view.findViewById(R.id.tv_program_item_panel_clip_tv);
            content = view.findViewById(R.id.tv_program_item_panel_content_des_tv);
            thumbnail = view.findViewById(R.id.tv_program_item_panel_content_thumbnail_iv);
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            inUsage=false;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.layout = (RelativeLayout) view;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((screenWidth - screenWidth / 9) / 2,
                screenHeight / 3 * 24);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.layout.setLayoutParams(layoutParams);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Channel itemChannel = mProgramList.get(position);
        if(holder.layout != null){
            holder.layout.removeAllViews();
        }
        if (itemChannel != null && itemChannel.getSchedules() != null) {
            ArrayList<Schedule> itemSchedule =  itemChannel.getSchedules();
            boolean isLast = false;
            for (int i = 0; i < itemSchedule.size(); i++) {
                ItemViewHolder itemViewHolder =getUnused();
                if(itemViewHolder == null){
                    itemViewHolder = new ItemViewHolder();
                    if(i == itemSchedule.size()-1){
                        isLast = true;
                    }
                    setView(itemViewHolder, itemSchedule.get(i), isLast);
                }
                itemViewHolder.setUsing();
                holder.layout.addView(itemViewHolder.view);
                itemViewHolder.thumbnail.setImageResource(R.drawable.test_image);
                //URLによって、サムネイル取得
                String thumbnailURL = itemSchedule.get(i).getImageUrl();
                if (!TextUtils.isEmpty(thumbnailURL)) {
                    itemViewHolder.thumbnail.setTag(thumbnailURL);
                    Bitmap bitmap = thumbnailProvider.getThumbnailImage(itemViewHolder.thumbnail, thumbnailURL);
                    if (bitmap != null) {
                        itemViewHolder.thumbnail.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    /**
     * 機能
     * ビューを設定
     */
    private void setView(ItemViewHolder itemViewHolder, Schedule itemSchedule, boolean isLast){
        String startTime = itemSchedule.getStartTime();
        String endTime = itemSchedule.getEndTime();
        if(!TextUtils.isEmpty(startTime)){
            itemViewHolder.startM.setText(startTime.substring(14,16));
        }
        itemViewHolder.content.setText(itemSchedule.getTitle());
        String end = endTime.substring(0, 10) + endTime.substring(11, 19);
        Date date1 = new Date();
        Date date2 = new Date();
        SimpleDateFormat format = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
        try {
            date1 = format.parse(end);
            date2 = format.parse(curDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float marginTop = itemSchedule.getMarginTop();
        float myHeight = itemSchedule.getMyHeight();
        itemViewHolder.layoutParams.height = (int)(myHeight * (screenHeight / 3));
        itemViewHolder.view.setLayoutParams(itemViewHolder.layoutParams);
        itemViewHolder.view.setY(marginTop * (screenHeight / 3));

        if(isLast){
            if(date1.compareTo(date2) == -1){
                itemViewHolder.view.setBackgroundResource(R.drawable.program_end_gray);
                itemViewHolder.view.setTag(0);
            } else {
                itemViewHolder.view.setBackgroundResource(R.drawable.program_rectangele_end);
                itemViewHolder.view.setTag(1);
            }
        } else {
            if(date1.compareTo(date2) == -1){
                itemViewHolder.view.setBackgroundResource(R.drawable.program_start_gray);
                itemViewHolder.view.setTag(0);
            } else {
                itemViewHolder.view.setBackgroundResource(R.drawable.program_rectangele_start);
                itemViewHolder.view.setTag(1);
            }
        }
        itemViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((int)view.getTag() == 1){
                    Intent intent = new Intent();
                    intent.setClass(mContext, TvPlayerActivity.class);
                    mContext.startActivityForResult(intent,0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProgramList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        MyViewHolder(View view) {
            super(view);
        }

        RelativeLayout layout;
    }
}