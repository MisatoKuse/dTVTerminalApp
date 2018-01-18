/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.utils.MainSettingUtils;

import java.util.List;

/**
 * 設定画面用アダプタ.
 */
public class MainSettingListAdapter extends BaseAdapter {

    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * レイアウトID.
     */
    private final int mItemLayoutResource;
    /**
     * リストに表示する項目.
     */
    private final List<MainSettingUtils> mSettingList;

    /**
     * コンストラクタ.
     *
     * @param context     コンテキスト
     * @param resource    レイアウトファイルのID
     * @param settingList 設定画面に表示する情報のリスト
     */
    public MainSettingListAdapter(final Context context, final int resource, final List<MainSettingUtils> settingList) {
        mContext = context;
        mItemLayoutResource = resource;
        mSettingList = settingList;
    }

    @Override
    public int getCount() {
        return mSettingList.size();
    }

    @Override
    public Object getItem(final int position) {
        return mSettingList.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, mItemLayoutResource, null);
            holder.mText = view.findViewById(R.id.setting_main_text);
            holder.mStateText = view.findViewById(R.id.setting_state_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MainSettingUtils mainSettingUtils = (MainSettingUtils) getItem(position);
        holder.mText.setText(mainSettingUtils.getText());
        holder.mStateText.setText(mainSettingUtils.getStateText());

        return view;
    }

    /**
     * ViewHolder.
     */
    static class ViewHolder {
        /**
         * 項目名.
         */
        private TextView mText;
        /**
         * サブテキスト.
         */
        private TextView mStateText;
    }
}