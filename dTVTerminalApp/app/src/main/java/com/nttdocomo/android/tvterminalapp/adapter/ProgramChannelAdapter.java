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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;

import java.util.ArrayList;

public class ProgramChannelAdapter extends
        RecyclerView.Adapter<ProgramChannelAdapter.ViewHolder> {
    private final TvProgramListActivity mContext;
    private final LayoutInflater mInflater;
    private final ArrayList<ChannelInfo> channelList;
    private int screenWidth = 0;
    private static final int TIME_LINE_WIDTH = 44;


    public ProgramChannelAdapter(Context context, ArrayList<ChannelInfo> channelList) {
        mInflater = LayoutInflater.from(context);
        this.channelList = channelList;
        this.mContext = (TvProgramListActivity) context;
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
                (screenWidth - mContext.dip2px(TIME_LINE_WIDTH))/2,
                mContext.dip2px(TIME_LINE_WIDTH));
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.channelText = view.findViewById(R.id.tv_program_channel_list_item_layout_content);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (viewHolder.channelText != null) {
            viewHolder.channelText.setText(channelList.get(i).getTitle());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
        }

        TextView channelText;
    }

}
