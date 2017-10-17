/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;

import java.util.List;


public class MenuListAdapter extends BaseAdapter {

    private Context mContext=null;
    private List mData=null;
    private List mDataCounts=null;

    public MenuListAdapter(Context context, List data, List dataCounts) {
        this.mContext = context;
        this.mData = data;
        this.mDataCounts=dataCounts;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemProgramVIew = null;
        ProgramViewHolder holder = null;
        if (view == null) {
            holder = new ProgramViewHolder();
            itemProgramVIew = View.inflate(mContext, R.layout.nav_item_lv_menu_program, null);
            holder.tv_title = itemProgramVIew.findViewById(R.id.tv_title);
            holder.tv_count = itemProgramVIew.findViewById(R.id.tv_count);
            view = itemProgramVIew;
            view.setTag(holder);
        } else {
            holder = (ProgramViewHolder) view.getTag();
        }

        holder.tv_title.setText((CharSequence) mData.get(i));
        int cnt=(int)mDataCounts.get(i);
        if(-1!=cnt){
            holder.tv_count.setText(cnt+"");
        } else {
            holder.tv_count.setText("");
        }

        return view;
    }

   static class ProgramViewHolder {
        TextView tv_title;
        TextView tv_count;
    }
}
