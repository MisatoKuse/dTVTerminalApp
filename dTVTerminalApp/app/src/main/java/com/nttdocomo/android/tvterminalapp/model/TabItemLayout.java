/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.search.FragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.model.search.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.model.search.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchDubbedType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchFilterTypeMappable;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchGenreType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchContentInfo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class TabItemLayout extends RelativeLayout {

    private Context mContext = null;
    private int mPageNumber = 0;
    private OnClickTabTextListener mOnClickTabTextListener = null;
    private String[] mTabNames = null;
    private LinearLayout mLinearLayout = null;
    private HorizontalScrollView mTabScrollView = null;

    /**
     * コンストラクタ
     * @param context
     */
    public TabItemLayout(Context context) {
        this(context, null);
    }

    public TabItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.tab_item_layout, this, true);
        mTabScrollView = this.findViewById(R.id.tab_show_layout_scroll_area);
        mLinearLayout = findViewById(R.id.tab_show_layout_text_area);
    }

    /**
     * タブ押下通知リスナーIF
     */
    public interface OnClickTabTextListener {
        void onClickTab(int position);
    }

    /**
     * タブ押下時のリスナーを設定
     * @param listener
     */
    public void setTabClickListener(OnClickTabTextListener listener) {
        mOnClickTabTextListener = listener;
    }

    /**
     * tabに関連Viewの初期化
     */
    public void initTabVIew(String[] tabNames) {
        DTVTLogger.start("tabNames.length = " + tabNames.length);
        mLinearLayout.removeAllViews();
        mTabNames = tabNames;
        float density = mContext.getResources().getDisplayMetrics().density;

        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            // タブのマージン
            if(i != mTabNames.length - 1) {
                params.setMargins(
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_left),
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_top),
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_right),
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_bottom));
            } else {
                params.setMargins(
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_left),
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_top),
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_right_last),
                        mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_margin_bottom));
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(((mContext.getResources().getDimension(R.dimen.list_tab_text_size) / density)));
            if(i == 0) {
                tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_select_text_color));
            } else {
                tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.common_tab_unselect_text_color));
            }
            tabTextView.setGravity(Gravity.TOP|Gravity.CENTER);
            tabTextView.setPadding(
                    mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_padding_left),
                    mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_padding_right),
                    mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_padding_top),
                    mContext.getResources().getDimensionPixelSize(R.dimen.list_tab_padding_bottom));
//
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.indicating);
            } else {
                tabTextView.setBackgroundResource(0);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    mPageNumber = position;
                    setTab(position);
                    if(mOnClickTabTextListener != null) {
                        mOnClickTabTextListener.onClickTab(position);
                    }
                }
            });
            mLinearLayout.setBackgroundResource(R.drawable.indicating_no);
            mLinearLayout.addView(tabTextView);
        }
        DTVTLogger.end();
    }

    /**
     * タブ名を取得
     * @return
     */
    public String getSelecteTabName() {
        TextView textView = (TextView)mLinearLayout.getChildAt(mPageNumber);
        return textView.getText().toString();
    }

    /**
     * インジケーター設置
     * @param position
     */
    public void setTab(int position) {
        DTVTLogger.start();
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView textView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    scrollOffsetCheck(textView);
                    textView.setBackgroundResource(R.drawable.indicating);
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
     * 選択したタブを画面サイズに合わせて中央寄せに持ってくる
     * @param textView
     */
    private void scrollOffsetCheck(TextView textView){
        int left = textView.getLeft();
        int width = textView.getMeasuredWidth();
        int toX = left + width / 2 - (int)(mContext.getResources().getDisplayMetrics().density) / 2;
        mTabScrollView.smoothScrollTo(toX, 0);
    }

    @Override
    public void setVisibility(int visibility) {
        findViewById(R.id.tab_show_layout_area).setVisibility(visibility);
    }
}
