/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplay;

import java.util.List;

/**
 * GlobalMenuListのアダプタ.
 */
public class MenuListAdapter extends BaseAdapter {

    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * リストに表示するデータ.
     */
    private List mData = null;
    /**
     * リストに表示する件数データ.
     */
    private List mDataCounts = null;

    /**
     * テキストサイズ.
     */
    private static final int TEXT_SIZE = 14;
    /**
     * 右矢印(>)アイコンサイズ.
     */
    private static final int RIGHT_ARROW_ICON_SIZE = 30;
    /**
     * 右矢印(>)アイコンのマージン右.
     */
    private static final int RIGHT_ARROW_RIGHT_MARGIN = 4;
    /**
     * テレビアイコンサイズ.
     */
    private static final int TV_ICON_SIZE = 20;
    /**
     * テレビアイコンのマージン右.
     */
    private static final int TV_ICON_RIGHT_MARGIN = 9;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param data リストに表示するデータ
     * @param dataCounts リストに表示する件数データ
     */
    public MenuListAdapter(final Context context, final List data, final List dataCounts) {
        this.mContext = context;
        this.mData = data;
        this.mDataCounts = dataCounts;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(final int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemProgramVIew;
        ProgramViewHolder holder;
        if (view == null) {
            holder = new ProgramViewHolder();
            itemProgramVIew = View.inflate(mContext, R.layout.nav_item_lv_menu_program, null);
            holder.tv_title = itemProgramVIew.findViewById(R.id.tv_title);
            holder.tv_count = itemProgramVIew.findViewById(R.id.tv_count);
            holder.tv_arrow = itemProgramVIew.findViewById(R.id.iv_arrow);
            holder.tv_title_icon = itemProgramVIew.findViewById(R.id.tv_title_icon);
            view = itemProgramVIew;
            view.setTag(holder);
        } else {
            holder = (ProgramViewHolder) view.getTag();
        }
        String title = (String) mData.get(i);
        holder.tv_title.setText(title);
        setTextView(title, holder.tv_title);
        setImageView(title, holder.tv_arrow);
        setTitleNameImageView(title, holder.tv_title_icon);
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
     * TitleNameにより、TextView設定を変更する.
     *
     * @param title    タイトル
     * @param textView タイトルView
     */
    private void setTextView(final String title, final TextView textView) {
        int intCustomTitleLeftMargin = mContext.getResources().getDimensionPixelSize(
                R.dimen.global_menu_list_item_sub_title_left_margin);
        int intTitleLeftMargin = mContext.getResources().getDimensionPixelSize(
                R.dimen.global_menu_list_item_default_title_left_margin);
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        if (title != null) {
            if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv_none_action))) {
                //ひかりTVメインの設定
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.list_item_background));
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_premium_tv_app_start_common))) {
                //テレビアプリを起動するの設定
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.stb_start_title));
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_home))
                    || title.equals(mContext.getString(R.string.nav_menu_item_recommend_program_video))
                    || title.equals(mContext.getString(R.string.nav_menu_item_keyword_search))
                    || title.equals(mContext.getString(R.string.nav_menu_item_notice))
                    || title.equals(mContext.getString(R.string.nav_menu_item_setting))) {
                //通常アイテムの設定
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.white_text));
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv))
                    || title.equals(mContext.getString(R.string.nav_menu_item_dtv_channel))
                    || title.equals(mContext.getString(R.string.nav_menu_item_dtv))
                    || title.equals(mContext.getString(R.string.nav_menu_item_d_animation))
                    || title.equals(mContext.getString(R.string.nav_menu_item_dazn))) {
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.list_item_background));
            } else {
                //その他サブアイテムのカスタマイズ
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.white_text));
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE);
                marginLayoutParams.setMargins(intCustomTitleLeftMargin, 0, 0, 0);
            }
            textView.setLayoutParams(marginLayoutParams);
        }
    }

    /**
     * ImageViewの設定.
     *
     * @param title     タイトル
     * @param imageView アイコンView
     */
    private void setImageView(final String title, final ImageView imageView) {
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
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = dip2px(RIGHT_ARROW_ICON_SIZE);
                lp.width = dip2px(RIGHT_ARROW_ICON_SIZE);
                ((ViewGroup.MarginLayoutParams) lp).setMargins(0, 0, dip2px(RIGHT_ARROW_RIGHT_MARGIN), 0);
                imageView.setLayoutParams(lp);
                imageView.setBackgroundResource(R.mipmap.icon_gray_arrow_right);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv))
                    || title.equals(mContext.getString(R.string.nav_menu_item_dtv_channel))
                    || title.equals(mContext.getString(R.string.nav_menu_item_dtv))
                    || title.equals(mContext.getString(R.string.nav_menu_item_d_animation))
                    || title.equals(mContext.getString(R.string.nav_menu_item_dazn))) {
                //STBコンテンツItemカスタマイズ
                imageView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = dip2px(TV_ICON_SIZE);
                lp.width = dip2px(TV_ICON_SIZE);
                ((ViewGroup.MarginLayoutParams) lp).setMargins(0, 0, dip2px(TV_ICON_RIGHT_MARGIN), 0);
                imageView.setLayoutParams(lp);
                imageView.setBackgroundResource(R.mipmap.icon_normal_tv);
            } else {
                //その他サブアイテムのカスタマイズ
                imageView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = dip2px(RIGHT_ARROW_ICON_SIZE);
                lp.width = dip2px(RIGHT_ARROW_ICON_SIZE);
                ((ViewGroup.MarginLayoutParams) lp).setMargins(0, 0, dip2px(RIGHT_ARROW_RIGHT_MARGIN), 0);
                imageView.setLayoutParams(lp);
                imageView.setBackgroundResource(R.mipmap.icon_gray_arrow_right);
            }
        }
    }

    /**
     * 各サービス名アイコン.
     * ImageViewの設定
     *
     * @param title     タイトル
     * @param imageView アイコンView
     */
    private void setTitleNameImageView(final String title, final ImageView imageView) {
        if (title != null) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            int intTitleLeftMargin = mContext.getResources().getDimensionPixelSize(
                    R.dimen.global_menu_list_item_title_icon_left_margin);
            if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv_none_action))) {
                int intHikariSettingIconLeftMargin = mContext.getResources().getDimensionPixelSize(
                        R.dimen.global_menu_list_item_default_title_left_margin);
                imageView.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.logo_hikaritv);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                drawable = new BitmapDrawable(mContext.getResources(), resizedBitmap);
                imageView.setBackground(drawable);
                marginLayoutParams.setMargins(intHikariSettingIconLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_hikari_tv))) {
                imageView.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.logo_hikaritv);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                drawable = new BitmapDrawable(mContext.getResources(), resizedBitmap);
                imageView.setBackground(drawable);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dtv_channel))) {
                imageView.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.logo_dtvch);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                drawable = new BitmapDrawable(mContext.getResources(), resizedBitmap);
                imageView.setBackground(drawable);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dtv))) {
                imageView.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.logo_dtv);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                drawable = new BitmapDrawable(mContext.getResources(), resizedBitmap);
                imageView.setBackground(drawable);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_d_animation))) {
                imageView.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.logo_danime);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                drawable = new BitmapDrawable(mContext.getResources(), resizedBitmap);
                imageView.setBackground(drawable);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dtv_channel))) {
                imageView.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.logo_dtvch);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                drawable = new BitmapDrawable(mContext.getResources(), resizedBitmap);
                imageView.setBackground(drawable);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else if (title.equals(mContext.getString(R.string.nav_menu_item_dazn))) {
                imageView.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.logo_dazn);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                drawable = new BitmapDrawable(mContext.getResources(), resizedBitmap);
                imageView.setBackground(drawable);
                marginLayoutParams.setMargins(intTitleLeftMargin, 0, 0, 0);
            } else {
                //その他サブアイテムのカスタマイズ
                imageView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * ViewHolder.
     */
    static class ProgramViewHolder {
        /**
         * 項目名.
         */
        TextView tv_title;
        /**
         * 件数.
         */
        TextView tv_count;
        /**
         * 右端に表示するアイコン.
         */
        ImageView tv_arrow;
        /**
         * アプリアイコン.
         */
        ImageView tv_title_icon;
    }

    /**
     * dip -> px 変換.
     *
     * @param dip dip
     * @return px
     */
    private int dip2px(final int dip) {
        float density = mContext.getResources().getDisplayMetrics().density;
//        DTVTLogger.debug("density: " + density);
        return (int) (dip * density + 0.5f);
    }
}