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
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
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
    private int mScreenWidth = 0;
    //ディスプレイ高さ
    private int mScreenHeight = 0;
    //サムネイル取得プロバイダー
    private ThumbnailProvider mThumbnailProvider = null;
    //現在時刻
    private String mCurDate = null;
    //現在時刻フォマード
    private static final String CUR_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";
    private List<ItemViewHolder> mItemViews = new ArrayList<>();
    //番組データ
    private List<Channel> mProgramList = null;

    public TvProgramListAdapter(Activity mContext, ArrayList<Channel> mProgramList) {
        this.mProgramList = mProgramList;
        this.mContext = mContext;
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        mThumbnailProvider = new ThumbnailProvider(mContext);
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
                    ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(j));
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
        mCurDate = sdf.format(c.getTime());
    }

    /**
     * 機能
     * 同じビュー重複利用しないよう
     */
    private ItemViewHolder getUnused(){
        for(ItemViewHolder view: mItemViews){
            if(!view.mInUsage){
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
        public View mView = null;
        private boolean mInUsage = false;

        TextView mStartM = null;
        TextView mContent = null;
        ImageView mThumbnail = null;
        RelativeLayout.LayoutParams mLayoutParams = null;
        ImageView mClipButton = null;

        void setUsing(){
            mInUsage =true;
        }

        ItemViewHolder(final Schedule schedule) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item_panel, null, false);
            mStartM = mView.findViewById(R.id.tv_program_item_panel_clip_tv);
            mContent = mView.findViewById(R.id.tv_program_item_panel_content_des_tv);
            mThumbnail = mView.findViewById(R.id.tv_program_item_panel_content_thumbnail_iv);
            mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            mInUsage =false;
            mClipButton = mView.findViewById(R.id.tv_program_item_panel_clip_iv);
            mClipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //クリップボタンのイベントを親に渡す
//                    ((ListView) parent).performItemClick(mView, position, R.id.item_common_result_clip_tv);
                    //TODO:親に処理を渡すか検討中
                    ((BaseActivity) mContext).sendClipRequest(schedule.getClipRequestData());
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.layout = (RelativeLayout) view;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((mScreenWidth - mScreenWidth / 9) / 2,
                mScreenHeight / 3 * 24);
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
                    itemViewHolder = new ItemViewHolder(itemSchedule.get(i));
                    if(i == itemSchedule.size()-1){
                        isLast = true;
                    }
                    setView(itemViewHolder, itemSchedule.get(i), isLast);
                }
                itemViewHolder.setUsing();
                holder.layout.addView(itemViewHolder.mView);
                itemViewHolder.mThumbnail.setImageResource(R.drawable.test_image);
                //URLによって、サムネイル取得
                String thumbnailURL = itemSchedule.get(i).getImageUrl();
                if (!TextUtils.isEmpty(thumbnailURL)) {
                    itemViewHolder.mThumbnail.setTag(thumbnailURL);
                    Bitmap bitmap = mThumbnailProvider.getThumbnailImage(itemViewHolder.mThumbnail, thumbnailURL);
                    if (bitmap != null) {
                        itemViewHolder.mThumbnail.setImageBitmap(bitmap);
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
            itemViewHolder.mStartM.setText(startTime.substring(14,16));
        }
        itemViewHolder.mContent.setText(itemSchedule.getTitle());
        String end = endTime.substring(0, 10) + endTime.substring(11, 19);
        Date date1 = new Date();
        Date date2 = new Date();
        SimpleDateFormat format = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
        try {
            date1 = format.parse(end);
            date2 = format.parse(mCurDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float marginTop = itemSchedule.getMarginTop();
        float myHeight = itemSchedule.getMyHeight();
        itemViewHolder.mLayoutParams.height = (int)(myHeight * (mScreenHeight / 3));
        itemViewHolder.mView.setLayoutParams(itemViewHolder.mLayoutParams);
        itemViewHolder.mView.setY(marginTop * (mScreenHeight / 3));

        if(isLast){
            if(date1.compareTo(date2) == -1){
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_end_gray);
                itemViewHolder.mView.setTag(0);
            } else {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_rectangele_end);
                itemViewHolder.mView.setTag(1);
            }
        } else {
            if(date1.compareTo(date2) == -1){
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_start_gray);
                itemViewHolder.mView.setTag(0);
            } else {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_rectangele_start);
                itemViewHolder.mView.setTag(1);
            }
        }
        itemViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((int)view.getTag() == 1){
                    Intent intent = new Intent();
                    intent.setClass(mContext, DtvContentsDetailActivity.class);
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