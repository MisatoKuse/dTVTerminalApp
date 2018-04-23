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

/**
 * 番組表チャンネルアダプター.
 */
public class ProgramChannelAdapter extends
        RecyclerView.Adapter<ProgramChannelAdapter.ViewHolder> {
    /**コンテキスト.*/
    private final TvProgramListActivity mContext;
    /**LayoutInflater.*/
    private final LayoutInflater mInflater;
    /**チャンネル情報.*/
    private final ArrayList<ChannelInfo> channelList;
    /**スクリーンWidth.*/
    private int mScreenWidth = 0;
    /**タイムラインWidth.*/
    private static final int TIME_LINE_WIDTH = 44;

    /**
     * コンストラクタ.
     * @param context コンテキスト
     * @param channelList チャンネル一覧
     */
    public ProgramChannelAdapter(final Context context, final ArrayList<ChannelInfo> channelList) {
        mInflater = LayoutInflater.from(context);
        this.channelList = channelList;
        this.mContext = (TvProgramListActivity) context;
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.tv_program_channel_list_item_layout,
                viewGroup, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (mScreenWidth - mContext.dip2px(TIME_LINE_WIDTH)) / 2,
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

    /**
     * コンストラクタ.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * ビューホルダー.
         * @param view view
         */
        ViewHolder(final View view) {
            super(view);
        }

        /**
         * チャンネル名.
         */
        TextView channelText;
    }

}
