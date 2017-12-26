/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.utils.MainSettingUtils;

import java.util.List;

public class MainSettingListAdapter extends BaseAdapter {

    private final Context mContext;
    private final int mItemLayoutResource;
    private final List<MainSettingUtils> mSettingList;
    //カテゴリ時の項目名の文字サイズ
    private final static int CATEGORY_TEXT_SIZE = 14;
    //非カテゴリ時の項目名の文字サイズ
    private final static int ITEM_TEXT_SIZE = 16;
    //バージョン情報表示時のマージン
    private final static int VERSION_MARGIN = 40;

    /**
     * コンストラクタ
     *
     * @param context     コンテキスト
     * @param resource    レイアウトファイルのID
     * @param settingList 設定画面に表示する情報のリスト
     */
    public MainSettingListAdapter(Context context, int resource, List<MainSettingUtils> settingList) {
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
            //バージョン情報は">"と同じ位置に表示させる
            holder.mForwardImage.setVisibility(View.INVISIBLE);
            if (mainSettingUtils.getText().equals(mContext.getString(R.string.main_setting_list_version_info))) {
                holder.mForwardImage.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mStateText.getLayoutParams();
                params.addRule(RelativeLayout.START_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_END, 1);
                params.setMarginEnd(VERSION_MARGIN);
                holder.mStateText.setLayoutParams(params);
            }
        } else {
            holder.mForwardImage.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mStateText.getLayoutParams();
            params.addRule(RelativeLayout.START_OF, holder.mForwardImage.getId());
            params.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            holder.mStateText.setLayoutParams(params);
        }

        //カテゴリーと項目とで文字サイズを変更する
        if (mainSettingUtils.isCategory()) {
            holder.mText.setTextColor(ContextCompat.getColor(mContext, R.color.setting_category_color_white));
            holder.mText.setTextSize(CATEGORY_TEXT_SIZE);
        } else {
            holder.mText.setTextColor(ContextCompat.getColor(mContext, R.color.setting_text_color_white));
            holder.mText.setTextSize(ITEM_TEXT_SIZE);
        }
        return view;
    }

    static class ViewHolder {
        private TextView mText;
        private TextView mStateText;
        private ImageView mForwardImage;
    }
}