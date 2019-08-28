/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
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
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * 共通TABレイアウトクラス.
  */
public class TabItemLayout extends HorizontalScrollView {
    /**コンテキスト.*/
    private Context mContext = null;
    /**タブ押下通知リスナー.*/
    private OnClickTabTextListener mOnClickTabTextListener = null;
    /**タブ名.*/
    private String[] mTabNames = null;
    /**LinearLayout.*/
    private LinearLayout mLinearLayout = null;
    /**アクティビティタイプ.*/
    private ActivityType mActivityType = null;
    /**tabのTextViewのパラメータ値.*/
    private LinearLayout.LayoutParams mTabTextViewLayoutParams = null;
    /**tabの最後のViewのパラメータ値.*/
    private LinearLayout.LayoutParams mTabTextViewLastDataLayoutParams = null;
    /**タブTextSize.*/
    private float mTabTextSize = 0;
    /**タブWidth.*/
    private float mTabWidth = 0;

    /**
     * アクティビティタイプ種別.
     */
    public enum ActivityType {
        /**SearchActivity.*/
        SEARCH_ACTIVITY,
        /**ClipListActivity.*/
        CLIP_LIST_ACTIVITY,
        /**RecordedListActivity.*/
        RECORDED_LIST_ACTIVITY,
        /**VideoRankingActivity.*/
        VIDEO_RANKING_ACTIVITY,
        /**WeeklyTvRankingActivity.*/
        WEEKLY_RANKING_ACTIVITY,
        /**RecommendActivity.*/
        RECOMMEND_LIST_ACTIVITY,
        /**ChannelListActivity.*/
        CHANNEL_LIST_ACTIVITY,
        /**TvProgramListActivity.*/
        PROGRAM_LIST_ACTIVITY,
        /**ContentDetailActivity.*/
        CONTENTS_DETAIL_ACTIVITY
    }

    /**
     * コンストラクタ.
     *
     * @param context Context
     */
    public TabItemLayout(final Context context) {
        this(context, null);
    }

    /**
     * コンストラクタ.
     * @param context コンテキスト
     * @param attrs  attrs
     */
    public TabItemLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * コンストラクタ.
     * @param context context
     * @param attrs  attrs
     * @param defStyleAttr defStyleAttr
     */
    public TabItemLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * タブ押下通知リスナーIF.
     */
    public interface OnClickTabTextListener {
        /**
         * タブ押下callback.
         * @param position position
         */
        void onClickTab(int position);
    }

    /**
     * タブ押下時のリスナーを設定.
     *
     * @param listener OnClickTabTextListener
     */
    public void setTabClickListener(final OnClickTabTextListener listener) {
        mOnClickTabTextListener = listener;
    }

    /**
     * tabの初期設定.
     *
     * @param tabNames タブ名
     * @param type ActivityType
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
    private final ViewTreeObserver.OnGlobalLayoutListener mViewTreeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
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
     * @param tabNames タブ名
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tabTextView.setGravity(Gravity.TOP | Gravity.CENTER);
            } else {
                tabTextView.setGravity(Gravity.CENTER);
            }
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_select_text_color));
                tabTextView.setBackgroundResource(getBackgroundResourceIndicating(true));
            } else {
                tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_unselect_text_color));
                tabTextView.setBackgroundResource(0);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    int position = (int) view.getTag();
                    setTab(position);
                    if (mOnClickTabTextListener != null) {
                        mOnClickTabTextListener.onClickTab(position);
                    }
                }
            });
            if (mContext instanceof TvProgramListActivity) {
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
            }
            mLinearLayout.addView(tabTextView);
        }
        mLinearLayout.setBackgroundResource(getBackgroundResourceIndicating(false));

        DTVTLogger.end();
    }

    /**
     * インジケーター設置.
     *
     * @param position タブ番号
     */
    public void setTab(final int position) {
        DTVTLogger.start();
        if (mLinearLayout != null) {
            mLinearLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mLinearLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    setTabPosition(position);
                    return true;
                }
            });
        }
        DTVTLogger.end();
    }

    /**
     * 親ビュー描画開始してからタブの位置を反映する.
     * @param position タブポジション
     */
    private void setTabPosition(final int position) {

        for (int i = 0; i < mTabNames.length; i++) {
            TextView textView = (TextView) mLinearLayout.getChildAt(i);
            if (position == i) {
                scrollOffsetCheck(textView);
                textView.setBackgroundResource(getBackgroundResourceIndicating(true));
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_select_text_color));
            } else {
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_unselect_text_color));
                textView.setBackgroundResource(0);
            }
        }
    }

    /**
     * 選択したタブを画面サイズに合わせて中央寄せに持ってくる.
     *
     * @param textView TextView
     */
    private void scrollOffsetCheck(final TextView textView) {
        int widthDensity = mContext.getResources().getDisplayMetrics().widthPixels;
        int left = textView.getLeft();
        int width = textView.getMeasuredWidth();
        int toX = left + width / 2 - widthDensity / 2;
        this.smoothScrollTo(toX, 0);
    }

    /**
     * アクティビティタイプ設定.
     * @param activityType アクティビティタイプ
     */
    private void setActivityType(final ActivityType activityType) {
        mActivityType = activityType;
    }

    /**
     * 画面毎のTabのTextViewのレイアウトパラメータを設定する.
     *
     * @param lastData True:最後のView false:その他
     * @return レイアウトパラメータ
     */
    private LinearLayout.LayoutParams getTabTextViewParameter(final boolean lastData) {
        LinearLayout.LayoutParams params;
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
     * @return テキストサイズ
     */
    private float getTextSizeParam() {
        float density = mContext.getResources().getDisplayMetrics().density;
        float returnTextSize = 0;
        switch (mActivityType) {
            case SEARCH_ACTIVITY:
            case CLIP_LIST_ACTIVITY:
            case VIDEO_RANKING_ACTIVITY:
            case WEEKLY_RANKING_ACTIVITY:
            case RECOMMEND_LIST_ACTIVITY:
            case RECORDED_LIST_ACTIVITY:
            case CONTENTS_DETAIL_ACTIVITY:
                returnTextSize = mContext.getResources().getDimension(R.dimen.tab_text_size_15dp) / density;
                break;
            case CHANNEL_LIST_ACTIVITY:
            case PROGRAM_LIST_ACTIVITY:
                returnTextSize = mContext.getResources().getDimension(R.dimen.tab_text_size_14dp) / density;
                break;
            default:
                break;
        }

        return returnTextSize;
    }

    /**
     * タブ領域のパラメータを設定.
     */
    private void addTabInnerView() {
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
            case CONTENTS_DETAIL_ACTIVITY:
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
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        this.addView(mLinearLayout);
    }

    /**
     * 設定するインジケータの取得.
     * @param isFocus フォーカスフラグ
     * @return リソースID
     * 　　　　 コンテンツ詳細の場合、"0（リソースIDなし）"が返却される可能性あり
     */
    private int getBackgroundResourceIndicating(final boolean isFocus) {
        int resId;
        switch (mActivityType) {
            case CHANNEL_LIST_ACTIVITY:
                if (isFocus) {
                    resId = R.drawable.indicating_channel_list;
                } else {
                    resId = R.drawable.indicating_no_channel_list;
                }
                break;
            case CONTENTS_DETAIL_ACTIVITY:
                if (isFocus) {
                    resId = R.drawable.indicating_background_black;
                } else {
                    resId = 0;
                }
                break;
            case PROGRAM_LIST_ACTIVITY:
                if (isFocus) {
                    resId = R.drawable.indicating_background_black;
                } else {
                    resId = R.drawable.indicating_no_background_black;
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
