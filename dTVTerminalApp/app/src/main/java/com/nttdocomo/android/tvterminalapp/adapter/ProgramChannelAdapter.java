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

import java.util.ArrayList;

/**
 * 番組表チャンネルアダプター.
 */
public class ProgramChannelAdapter extends
        RecyclerView.Adapter<ProgramChannelAdapter.ViewHolder> {
    /**コンテキスト.*/
    private TvProgramListActivity mContext = null;
    /**LayoutInflater.*/
    private LayoutInflater mInflater = null;
    /**チャンネル情報.*/
    private ArrayList<String> mChannelList = null;
    /**スクリーンWidth.*/
    private int mScreenWidth = 0;
    /**タイムラインWidth.*/
    private static final int TIME_LINE_WIDTH = 44;
    /**タイムラインHeight.*/
    private static final int TIME_LINE_HEIGHT = 56;

    /**
     * コンストラクタ.
     * @param context コンテキスト
     * @param channelList チャンネル一覧
     */
    public ProgramChannelAdapter(final Context context, final ArrayList<String> channelList) {
        mInflater = LayoutInflater.from(context);
        this.mChannelList = channelList;
        this.mContext = (TvProgramListActivity) context;
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.tv_program_channel_list_item_layout,
                viewGroup, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (mScreenWidth - mContext.dip2px(TIME_LINE_WIDTH)) / 2,
                mContext.dip2px(TIME_LINE_HEIGHT));
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.channelText = view.findViewById(R.id.tv_program_channel_list_item_layout_content);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (viewHolder.channelText != null) {
            viewHolder.channelText.setText(mChannelList.get(i));
        }
    }

    /**
     * ガベージコレクションされやすくするために、ヌルを格納する.
     */
    public void removeData() {
        if (mChannelList != null) {
            mChannelList.clear();
            mChannelList = null;
        }

        mContext = null;
        mInflater = null;
    }

    /**
     * チャンネル名リストを返却.
     *
     * @return チャンネル名リスト
     */
    public ArrayList<String> getChannelList() {
        return mChannelList;
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
