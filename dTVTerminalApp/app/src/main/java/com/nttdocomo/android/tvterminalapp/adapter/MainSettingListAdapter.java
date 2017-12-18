/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.utils.MainSettingUtils;

import java.util.ArrayList;
import java.util.List;

public class MainSettingListAdapter extends BaseAdapter {

    private final Context mContext;
    private final int mItemLayoutResource;
    private List<MainSettingUtils> mSettingList;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource レイアウトファイルのID
     * @param settingList 設定画面に表示する情報のリスト
     */
    public MainSettingListAdapter (Context context, int resource, List<MainSettingUtils> settingList) {
        mContext = context;
        mItemLayoutResource = resource;
        mSettingList = settingList;
    }

    @Override
    public int getCount() {
        return mSettingList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSettingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, mItemLayoutResource, null);
            holder.mText = view.findViewById(R.id.setting_main_text);
            holder.mStateText = view.findViewById(R.id.setting_state_text);
            holder.mForwardImage = view.findViewById(R.id.setting_forward_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MainSettingUtils mainSettingUtils = (MainSettingUtils) getItem(position);
        holder.mText.setText(mainSettingUtils.getText());
        holder.mStateText.setText(mainSettingUtils.getStateText());
        if (!mainSettingUtils.isArrow()) {
            //TODO バージョン情報を右寄せ。バージョン情報用のViewを作成する？
            holder.mForwardImage.setVisibility(View.INVISIBLE);
        } else {
            holder.mForwardImage.setVisibility(View.VISIBLE);
        }
        if (mainSettingUtils.isCategory()) {
            holder.mText.setTextColor(ContextCompat.getColor(mContext, R.color.setting_category_color_white));
            holder.mText.setTextSize(14);
        } else {
            holder.mText.setTextColor(ContextCompat.getColor(mContext, R.color.setting_text_color_white));
            holder.mText.setTextSize(16);
        }

        return view;
    }

    static class ViewHolder {
        private TextView mText;
        private TextView mStateText;
        private ImageView mForwardImage;
    }
}
