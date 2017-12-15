/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.utils.ColorUtils;

import java.util.List;


public class MenuListAdapter extends BaseAdapter {

    private Context mContext = null;
    private List mData = null;
    private List mDataCounts = null;

    public MenuListAdapter(Context context, List data, List dataCounts) {
        this.mContext = context;
        this.mData = data;
        this.mDataCounts = dataCounts;
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
            holder.tv_arrow = itemProgramVIew.findViewById(R.id.iv_arrow);
            holder.dazn_icon = itemProgramVIew.findViewById(R.id.dazn_icon);
            view = itemProgramVIew;
            view.setTag(holder);
        } else {
            holder = (ProgramViewHolder) view.getTag();
        }

        String title = (String) mData.get(i);
        holder.tv_title.setText(title);
        setTextView(title, holder.tv_title);
        setImageView(title, holder.tv_arrow);
        setDAZNIcon(title, holder.dazn_icon);
        int cnt = (int) mDataCounts.get(i);
        if (MenuDisplay.INT_NONE_COUNT_STATUS != cnt) {
            holder.tv_count.setText(String.valueOf(cnt));
            holder.tv_count.setVisibility(View.VISIBLE);
        } else {
            holder.tv_count.setText("");
        }

        return view;
    }

    /**
     * TitleNameにより、TextView設定を変更する
     *
     * @param title    タイトル
     * @param textView タイトルView
     */
    private void setTextView(String title, TextView textView) {
        ColorUtils colorUtils = new ColorUtils(mContext);
        int intCustomTitleLeftMargin = (int) (mContext.getResources().getDimensionPixelSize(
                R.dimen.global_menu_list_item_sub_title_left_margin));
        int intTitleLeftMargin = (int) (mContext.getResources().getDimensionPixelSize(
                R.dimen.global_menu_list_item_default_title_left_margin));
        int intDAZNLeftMargin = (int) (mContext.getResources().getDimensionPixelSize(
                R.dimen.global_menu_list_item_dazn_title_left_margin));
//        int intNoneMargin = 0;
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        if (title != null) {
            if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv_none_action))) {
                //ひかりTVメインの設定
                colorUtils.setTextViewColor(textView, R.color.hikari_tv_title);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_premium_tv_app_start_common))) {
                //テレビアプリを起動するの設定
                colorUtils.setTextViewColor(textView, R.color.stb_start_title);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_home))
                    || title.equals(mContext.getString(R.string.nav_menu_item_recommend_program_video))
                    || title.equals(mContext.getString(R.string.nav_menu_item_keyword_search))
                    || title.equals(mContext.getString(R.string.nav_menu_item_notice))
                    || title.equals(mContext.getString(R.string.nav_menu_item_setting))) {
                //通常アイテムの設定
                colorUtils.setTextViewColor(textView, R.color.white_text);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv))) {
                //STB(ひかりTV)カスタマイズ
                colorUtils.setTextViewColor(textView, R.color.hikari_tv_title);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                marginLayoutParams.setMargins(intCustomTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dtv_channel))) {
                //STB(dTVチャンネル)カスタマイズ
                colorUtils.setTextViewColor(textView, R.color.dtv_channel_title);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                marginLayoutParams.setMargins(intCustomTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dtv))) {
                //STB(dTV)カスタマイズ
                colorUtils.setTextViewColor(textView, R.color.dtv_title);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                marginLayoutParams.setMargins(intCustomTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_d_animation))) {
                //STB(dアニメ)カスタマイズ
                colorUtils.setTextViewColor(textView, R.color.d_animation_title);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                marginLayoutParams.setMargins(intCustomTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dazn))) {
                //STB(DAZN)カスタマイズ
                colorUtils.setTextViewColor(textView, R.color.white_text);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                marginLayoutParams.setMargins(intDAZNLeftMargin, 0, 0, 0);
            } else {
                //その他サブアイテムのカスタマイズ
                colorUtils.setTextViewColor(textView, R.color.white_text);
                marginLayoutParams.setMargins(intCustomTitleLeftMargin, 0, 0, 0);
            }
            textView.setLayoutParams(marginLayoutParams);
        }
    }

    /**
     * ImageViewの設定
     *
     * @param title     タイトル
     * @param imageView アイコンView
     */
    private void setImageView(String title, ImageView imageView) {
        if (title != null) {
            if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv_none_action))) {
                //ひかりTVメインの設定
                imageView.setVisibility(View.GONE);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_premium_tv_app_start_common))) {
                //テレビアプリを起動するの設定
                imageView.setVisibility(View.GONE);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_home))
                    || title.equals(mContext.getString(R.string.nav_menu_item_recommend_program_video))
                    || title.equals(mContext.getString(R.string.nav_menu_item_keyword_search))
                    || title.equals(mContext.getString(R.string.nav_menu_item_notice))
                    || title.equals(mContext.getString(R.string.nav_menu_item_setting))) {
                //通常アイテムの設定
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.drawable.menu_forward);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv))) {
                //STB(ひかりTV)カスタマイズ
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.mipmap.ic_stb_status_icon_white);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dtv_channel))) {
                //STB(dTVチャンネル)カスタマイズ
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.mipmap.ic_stb_status_icon_white);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dtv))) {
                //STB(dTV)カスタマイズ
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.mipmap.ic_stb_status_icon_white);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_d_animation))) {
                //STB(dアニメ)カスタマイズ
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.mipmap.ic_stb_status_icon_white);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dazn))) {
                //STB(DAZN)カスタマイズ
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.mipmap.ic_stb_status_icon_white);
            } else {
                //その他サブアイテムのカスタマイズ
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.drawable.menu_forward);
            }
        }
    }

    /**
     * DAZNアイコン設定
     *
     * @param title     タイトル
     * @param imageView DAZNアイコン
     */
    private void setDAZNIcon(String title, ImageView imageView) {
        if (title.equals(mContext.getString(R.string.nav_menu_item_dazn))) {
            //DAZNアイコン表示
            imageView.setVisibility(View.VISIBLE);
        } else {
            //非表示
            imageView.setVisibility(View.GONE);
        }
    }

    static class ProgramViewHolder {
        TextView tv_title;
        TextView tv_count;
        ImageView tv_arrow;
        ImageView dazn_icon;
    }
}
