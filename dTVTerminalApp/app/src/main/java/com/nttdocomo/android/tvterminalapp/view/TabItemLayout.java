/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

public class TabItemLayout extends HorizontalScrollView {

    private Context mContext = null;
    private OnClickTabTextListener mOnClickTabTextListener = null;
    private String[] mTabNames = null;
    private LinearLayout mLinearLayout = null;
    private ActivityType mActivityType = null;
    // tabのTextViewのパラメータ値
    private LinearLayout.LayoutParams mTabTextViewLayoutParams = null;
    // tabの最後のViewのパラメータ値
    private LinearLayout.LayoutParams mTabTextViewLastDataLayoutParams = null;
    private float mTabTextSize = 0;
    private float mTabWidth = 0;

    public enum ActivityType {
        SEARCH_ACTIVITY,
        CLIP_LIST_ACTIVITY,
        RECORDED_LIST_ACTIVITY,
        VIDEO_RANKING_ACTIVITY,
        WEEKLY_RANKING_ACTIVITY,
        RECOMMEND_LIST_ACTIVITY,
        CHANNEL_LIST_ACTIVITY,
        PROGRAM_LIST_ACTIVITY,
        DTV_CONTENTS_DETAIL_ACTIVITY,
    }

    /**
     * コンストラクタ.
     *
     * @param context
     */
    public TabItemLayout(final Context context) {
        this(context, null);
    }

    public TabItemLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItemLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * タブ押下通知リスナーIF.
     */
    public interface OnClickTabTextListener {
        void onClickTab(int position);
    }

    /**
     * タブ押下時のリスナーを設定.
     *
     * @param listener
     */
    public void setTabClickListener(final OnClickTabTextListener listener) {
        mOnClickTabTextListener = listener;
    }

    /**
     * tabの初期設定.
     *
     * @param tabNames
     */
    public void initTabView(final String[] tabNames, final ActivityType type) {
        setActivityType(type);
        addTabInnerView();
        getParameters();
        resetTabView(tabNames);
    }

    /**
     * tabのパラメータを取得.
     */
    private void getParameters() {
        mTabTextViewLayoutParams = getTabTextViewParameter(false);
        mTabTextViewLastDataLayoutParams = getTabTextViewParameter(true);
        mTabTextSize = getTextSizeParam();
    }

    /**
     * タブの横幅を取得するためのリスナー.
     */
    ViewTreeObserver.OnGlobalLayoutListener mViewTreeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mTabWidth = mLinearLayout.getWidth();
            if (mTabWidth < mContext.getResources().getDisplayMetrics().widthPixels) {
                DTVTLogger.debug("tablayout add space");
                // indicatorを右端まで表示するために残余白をViewで埋める
                RelativeLayout layout = new RelativeLayout(mContext);
                int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels - mTabWidth);
                layout.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                layout.setBackgroundResource(0);
                mLinearLayout.addView(layout);
            }
            mLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mViewTreeListener);
            DTVTLogger.debug("TabLayout Width = " + mLinearLayout.getWidth());
        }
    };

    /**
     * tabに関連するViewの初期化.
     */
    public void resetTabView(final String[] tabNames) {
        DTVTLogger.start("tabNames.length = " + tabNames.length);
        mLinearLayout.removeAllViews();
        mTabWidth = 0;
        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(mViewTreeListener);

        mTabNames = tabNames;

        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(mContext);
            // タブ毎にパラメータを設定
            // 最後のデータかを判定
            if (i != mTabNames.length - 1) {
                tabTextView.setLayoutParams(mTabTextViewLayoutParams);
            } else {
                tabTextView.setLayoutParams(mTabTextViewLastDataLayoutParams);
            }
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTabTextSize);

            tabTextView.setGravity(Gravity.TOP | Gravity.CENTER);
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_select_text_color));
                tabTextView.setBackgroundResource(setBackgroundResourceIndicating(true));
            } else {
                tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_unselect_text_color));
                tabTextView.setBackgroundResource(0);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    setTab(position);
                    if (mOnClickTabTextListener != null) {
                        mOnClickTabTextListener.onClickTab(position);
                    }
                }
            });
            mLinearLayout.addView(tabTextView);
        }
        mLinearLayout.setBackgroundResource(setBackgroundResourceIndicating(false));

        DTVTLogger.end();
    }

    /**
     * インジケーター設置.
     *
     * @param position
     */
    public void setTab(final int position) {
        DTVTLogger.start();
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView textView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    scrollOffsetCheck(textView);
                    textView.setBackgroundResource(setBackgroundResourceIndicating(true));
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_select_text_color));
                } else {
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_unselect_text_color));
                    textView.setBackgroundResource(0);
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * 選択したタブを画面サイズに合わせて中央寄せに持ってくる.
     *
     * @param textView
     */
    private void scrollOffsetCheck(final TextView textView) {
        int widthDensity = (int) mContext.getResources().getDisplayMetrics().widthPixels;
        int left = textView.getLeft();
        int width = textView.getMeasuredWidth();
        int toX = left + width / 2 - widthDensity / 2;
        this.smoothScrollTo(toX, 0);
    }

    public void setActivityType(final ActivityType activityType) {
        mActivityType = activityType;
    }

    /**
     * 画面毎のTabのTextViewのレイアウトパラメータを設定する.
     *
     * @param lastData True:最後のView false:その他
     */
    public LinearLayout.LayoutParams getTabTextViewParameter(final boolean lastData) {
        LinearLayout.LayoutParams params = null;
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if (!lastData) {
            params.setMargins(
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_left),
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_top),
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_right),
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_bottom));
        } else {
            params.setMargins(
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_left),
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_top),
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_right_last),
                    mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_margin_bottom));
        }
        return params;
    }

    /**
     * テキストサイズを取得.
     *
     * @return
     */
    private float getTextSizeParam() {
        float density = mContext.getResources().getDisplayMetrics().density;
        float reternTextSize = 0;
        switch (mActivityType) {
            case SEARCH_ACTIVITY:
            case CLIP_LIST_ACTIVITY:
            case VIDEO_RANKING_ACTIVITY:
            case WEEKLY_RANKING_ACTIVITY:
            case RECOMMEND_LIST_ACTIVITY:
            case RECORDED_LIST_ACTIVITY:
            case DTV_CONTENTS_DETAIL_ACTIVITY:
                reternTextSize = mContext.getResources().getDimension(R.dimen.tab_text_size_15dp) / density;
                break;
            case CHANNEL_LIST_ACTIVITY:
            case PROGRAM_LIST_ACTIVITY:
                reternTextSize = mContext.getResources().getDimension(R.dimen.tab_text_size_14dp) / density;
                break;
            default:
                break;
        }

        return reternTextSize;
    }

    /**
     * タブ領域のパラメータを設定.
     */
    private void addTabInnerView() {
        int density = (int) mContext.getResources().getDisplayMetrics().density;
        mLinearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = null;
        switch (mActivityType) {
            case SEARCH_ACTIVITY:
            case VIDEO_RANKING_ACTIVITY:
            case WEEKLY_RANKING_ACTIVITY:
            case RECOMMEND_LIST_ACTIVITY:
                layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        (int) getResources().getDimension(R.dimen.tab_layout_area_height));
                mLinearLayout.setPadding(
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_scroll_area_padding_top),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero));
                break;
            case CLIP_LIST_ACTIVITY:
            case DTV_CONTENTS_DETAIL_ACTIVITY:
            case RECORDED_LIST_ACTIVITY:
                layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        (int) getResources().getDimension(R.dimen.tab_layout_area_height));
                mLinearLayout.setPadding(
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_scroll_area_padding_top),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero));
                break;
            case CHANNEL_LIST_ACTIVITY:
                layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        (int) getResources().getDimension(R.dimen.tab_layout_area_channel_list_height));
                mLinearLayout.setPadding(
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_scroll_area_padding_top),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero));
                break;
            case PROGRAM_LIST_ACTIVITY:
                layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        (int) getResources().getDimension(R.dimen.tab_layout_area_program_list_height));
                mLinearLayout.setPadding(
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.search_tab_scroll_area_padding_top),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero),
                        mContext.getResources().getDimensionPixelSize(R.dimen.tab_layout_padding_zero));

                break;
            default:
                // nop
                break;
        }
        ;
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        this.addView(mLinearLayout);
    }

    /**
     * 設定するインジケータの取得.
     */
    private int setBackgroundResourceIndicating(final boolean isFocus) {
        int resId = 0;
        switch (mActivityType) {
            case CHANNEL_LIST_ACTIVITY:
                if (isFocus) {
                    resId = R.drawable.indicating_channel_list;
                } else {
                    resId = R.drawable.indicating_no_channel_list;
                }
                break;
            case DTV_CONTENTS_DETAIL_ACTIVITY:
            case PROGRAM_LIST_ACTIVITY:
                if (isFocus) {
                    resId = R.drawable.indicating_background_brack;
                } else {
                    resId = R.drawable.indicating_no_background_brack;
                }
                break;
            case SEARCH_ACTIVITY:
            case CLIP_LIST_ACTIVITY:
            case RECORDED_LIST_ACTIVITY:
            case VIDEO_RANKING_ACTIVITY:
            case WEEKLY_RANKING_ACTIVITY:
            case RECOMMEND_LIST_ACTIVITY:
            default:
                if (isFocus) {
                    resId = R.drawable.indicating_common;
                } else {
                    resId = R.drawable.indicating_no_common;
                }
                break;
        }
        return resId;
    }
}
