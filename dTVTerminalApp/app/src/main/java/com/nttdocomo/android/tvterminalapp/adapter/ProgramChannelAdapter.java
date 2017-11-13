/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelItemClickListener;

import java.util.ArrayList;

public class ProgramChannelAdapter extends
        RecyclerView.Adapter<ProgramChannelAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Channel> channelList;
    private int screenWidth = 0;
    private ChannelItemClickListener channelItemClickListener;


    public ProgramChannelAdapter(Context context, ArrayList<Channel> channelList) {
        mInflater = LayoutInflater.from(context);
        this.channelList = channelList;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.tv_program_channel_list_item_layout,
                viewGroup, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (screenWidth - screenWidth / 9)/2,
                screenWidth / 9);
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);
        ViewHolder viewHolder = new ViewHolder(view, channelItemClickListener);
        viewHolder.channelIcon = view.findViewById(R.id.tv_program_channel_list_item_layout_icon);
        viewHolder.channelText = view.findViewById(R.id.tv_program_channel_list_item_layout_content);
        return viewHolder;
    }

    /**
     * リスナーを設定
     * @param listener リスナー
     */
    public void setOnItemClickListener(ChannelItemClickListener listener){
        this.channelItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.channelText.setText(channelList.get(i).getTitle());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChannelItemClickListener mOnItemListener;

        ViewHolder(View view, ChannelItemClickListener mOnItemListener) {
            super(view);
            this.mOnItemListener = mOnItemListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemListener != null){
                mOnItemListener.onChannelItemClick(v, getAdapterPosition());
            }
        }

        ImageView channelIcon;
        TextView channelText;
    }

}
