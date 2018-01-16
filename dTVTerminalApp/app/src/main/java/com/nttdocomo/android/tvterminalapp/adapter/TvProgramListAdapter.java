/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.Schedule;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.widget.ImageView.ScaleType.FIT_XY;

/**
 * 番組表アダプター.
 */
public class TvProgramListAdapter extends RecyclerView.Adapter<TvProgramListAdapter.MyViewHolder> {

    private static final int THUMBNAIL_HEIGHT = 69;
    private static final int THUMBNAIL_WIDTH = 122;
    private TvProgramListActivity mContext = null;
    //ディスプレイ幅さ
    private int mScreenWidth = 0;
    //ディスプレイ高さ
    private int mScreenHeight = 0;
    //サムネイル取得プロバイダー
    private ThumbnailProvider mThumbnailProvider = null;
    //現在時刻
    private String mCurDate = null;

    //年齢パレンタル情報
    private int mAgeReq = 8;
    //現在時刻フォマード
    private static final String CUR_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";
    private List<ItemViewHolder> mItemViews = new ArrayList<>();
    //番組データ
    private List<Channel> mProgramList = null;
    //サムネイルとエピソードタイトルを含むスペース
    private int mThumbEpiSpace;
    //エピソードタイトルを含むスペース
    private int mEpiSpace;
    private boolean isShowThumb;

    //年齢制限有効フラグ
    private boolean mIsParental = false;
    /**
     * コンストラクタ.
     *
     * @param mContext     コンテクスト
     * @param mProgramList 番組表リスト
     */
    public TvProgramListAdapter(final Activity mContext, final ArrayList<Channel> mProgramList) {
        this.mProgramList = mProgramList;
        this.mContext = (TvProgramListActivity) mContext;
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
        mAgeReq = userInfoDataProvider.getUserAge();
        mThumbnailProvider = new ThumbnailProvider(mContext);
        getCurTime();
        for (int i = 0; i < mProgramList.size(); i++) {
            Channel itemChannel = mProgramList.get(i);
            boolean isLast = false;
            if (itemChannel != null && itemChannel.getSchedules() != null) {
                ArrayList<Schedule> itemSchedules = itemChannel.getSchedules();
                for (int j = 0; j < itemSchedules.size(); j++) {
                    Schedule itemSchedule = itemSchedules.get(j);
                    if (j == itemSchedules.size() - 1) {
                        isLast = true;
                    }
                    ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(j));
                    setView(itemViewHolder, itemSchedule, isLast);
                    mItemViews.add(itemViewHolder);
                }
            }
        }
    }

    /**
     * 機能
     * 現在時刻取得.
     */
    private void getCurTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
        mCurDate = sdf.format(c.getTime());
    }

    /**
     * 機能
     * 同じビュー重複利用しないよう.
     */
    private ItemViewHolder getUnused() {
        for (ItemViewHolder view : mItemViews) {
            if (!view.mInUsage) {
                return view;
            }
        }
        return null;
    }

    /**
     * 年齢情報を設定する.
     *
     * @param mAgeReq 年齢情報
     */
    public void setAgeReq(int mAgeReq) {
        this.mAgeReq = mAgeReq;
    }

    /**
     * 機能
     * 番組詳細ビュー共通化.
     */
    private class ItemViewHolder {
        public View mView = null;
        private boolean mInUsage = false;

        TextView mStartM = null;
        TextView mContent = null;
        ImageView mThumbnail = null;
        RelativeLayout.LayoutParams mLayoutParams = null;
        ImageView mClipButton = null;
        TextView mDetail = null;

        void setUsing() {
            mInUsage = true;
        }

        /**
         * 各画面部品初期化.
         *
         * @param schedule 番組情報
         */
        ItemViewHolder(final Schedule schedule) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item_panel, null, false);
            mStartM = mView.findViewById(R.id.tv_program_item_panel_clip_tv);
            mContent = mView.findViewById(R.id.tv_program_item_panel_content_des_tv);
            mThumbnail = mView.findViewById(R.id.tv_program_item_panel_content_thumbnail_iv);
            mDetail = mView.findViewById(R.id.tv_program_item_panel_content_detail_tv);
            mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            mInUsage = false;
            mClipButton = mView.findViewById(R.id.tv_program_item_panel_clip_iv);
            mClipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、画像比較でクリップ済/未を判定する
                    Bitmap clipButtonBitmap = ((BitmapDrawable) mClipButton.getBackground()).getBitmap();
                    Bitmap activClipBitmap = ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(),
                            R.mipmap.icon_circle_active_clip, null)).getBitmap();
                    if (clipButtonBitmap.equals(activClipBitmap)) {
                        schedule.getClipRequestData().setClipStatus(true);
                    } else {
                        schedule.getClipRequestData().setClipStatus(false);
                    }
                    //クリップボタンイベント
                    ((BaseActivity) mContext).sendClipRequest(schedule.getClipRequestData(), mClipButton);
                }
            });
            setComponentParameters();
        }

        /**
         * デザイン設定.
         */
        private void setComponentParameters() {
            mView.setPadding(7, 12, 8, 15);
            mContent.setTextSize(13.0f);
            mContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mDetail.setTextSize(11.0f);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.layout = (RelativeLayout) view;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((mScreenWidth - mContext.dip2px(44)) / 2,
                mContext.dip2px(180) * 24);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.layout.setLayoutParams(layoutParams);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Channel itemChannel = mProgramList.get(position);
        if (holder.layout != null) {
            holder.layout.removeAllViews();
        }
        if (itemChannel != null && itemChannel.getSchedules() != null) {
            ArrayList<Schedule> itemSchedule = itemChannel.getSchedules();
            boolean isLast = false;
            for (int i = 0; i < itemSchedule.size(); i++) {
                ItemViewHolder itemViewHolder = getUnused();
                if (itemViewHolder == null) {
                    itemViewHolder = new ItemViewHolder(itemSchedule.get(i));
                    if (i == itemSchedule.size() - 1) {
                        isLast = true;
                    }
                    setView(itemViewHolder, itemSchedule.get(i), isLast);
                }
                itemViewHolder.setUsing();
                holder.layout.addView(itemViewHolder.mView);
                itemViewHolder.mThumbnail.setImageResource(R.drawable.test_image);
                //URLによって、サムネイル取得
                String thumbnailURL = itemSchedule.get(i).getImageUrl();
                if (!TextUtils.isEmpty(thumbnailURL)) {
                    itemViewHolder.mThumbnail.setTag(thumbnailURL);
                    Bitmap bitmap = mThumbnailProvider.getThumbnailImage(itemViewHolder.mThumbnail, thumbnailURL);
                    if (bitmap != null) {
                        int thumbnailWidth = itemViewHolder.mView.getWidth() - mContext.dip2px(30) - mContext.dip2px(8);
                        int thumbnailHeight = mContext.dip2px(THUMBNAIL_HEIGHT) * thumbnailWidth / mContext.dip2px(THUMBNAIL_WIDTH);
                        if (thumbnailWidth > 0 && thumbnailHeight > 0) {
                            bitmap.setWidth(thumbnailWidth);
                            bitmap.setHeight(thumbnailHeight);
                        }
                        itemViewHolder.mThumbnail.setScaleType(FIT_XY);
                        itemViewHolder.mThumbnail.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    /**
     * 機能
     * ビューを設定.
     *
     * @param itemViewHolder ビューホルダー
     * @param itemSchedule   番組情報
     * @param isLast         末尾フラグ
     */
    private void setView(final ItemViewHolder itemViewHolder, final Schedule itemSchedule, final boolean isLast) {

        //年齢制限フラグ
        mIsParental = setParental(StringUtil.convertRValueToAgeReq(mContext, itemSchedule.getRValue()));

        String startTime = itemSchedule.getStartTime();
        String endTime = itemSchedule.getEndTime();
        if (!TextUtils.isEmpty(startTime)) {
            itemViewHolder.mStartM.setText(startTime.substring(14, 16));
        }
        String end = endTime.substring(0, 10) + endTime.substring(11, 19);
        Date date1 = new Date();
        Date date2 = new Date();
        SimpleDateFormat format = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
        try {
            date1 = format.parse(end);
            date2 = format.parse(mCurDate);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        float marginTop = itemSchedule.getMarginTop();
        float myHeight = itemSchedule.getMyHeight();
        itemViewHolder.mLayoutParams.height = (int) (myHeight * (mContext.dip2px(180)));
        itemViewHolder.mView.setLayoutParams(itemViewHolder.mLayoutParams);
        itemViewHolder.mView.setY(marginTop * (mContext.dip2px(180)));

        if (isLast) {
            if (date1.compareTo(date2) == -1) {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_end_gray);
                itemViewHolder.mView.setTag(0);
            } else {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_rectangele_end);
                itemViewHolder.mView.setTag(1);
            }
        } else {
            if (date1.compareTo(date2) == -1) {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_start_gray);
                itemViewHolder.mView.setTag(0);
            } else {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_rectangele_start);
                itemViewHolder.mView.setTag(1);
            }
        }
        if (itemViewHolder.mClipButton != null) {
            if (itemSchedule.isClipExec() || mIsParental) {
                if (itemSchedule.isClipStatus()) {
                    itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_circle_active_clip);
                } else {
                    itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_circle_opacity_clip);
                }
            } else {
                itemViewHolder.mClipButton.setVisibility(View.GONE);
            }
        }

        String title;
        if (mIsParental) {
            title = StringUtil.returnAsterisk(mContext);
        } else {
            title = itemSchedule.getTitle();
            itemViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((int) view.getTag() == 1) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, DtvContentsDetailActivity.class);
                        intent.putExtra(DTVTConstants.SOURCE_SCREEN, mContext.getComponentName().getClassName());
                        mContext.startActivityForResult(intent, 0);
                    }
                }
            });
            String detail = itemSchedule.getDetail();
            itemViewHolder.mDetail.setText(detail);
        }
        itemViewHolder.mContent.setText(title);
        changeProgramInfoInOrderToShow(itemViewHolder);
    }

    /**
     * 番組情報表示順変更.
     *
     * @param itemViewHolder ビューホルダー
     */
    private void changeProgramInfoInOrderToShow(final ItemViewHolder itemViewHolder) {
        itemViewHolder.mContent.getViewTreeObserver()//タイトル
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        displayProgramTitle(itemViewHolder);
                        return true;
                    }
                });
        itemViewHolder.mDetail.getViewTreeObserver()//詳細エピソード
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        displayProgramEpi(itemViewHolder);
                        return true;
                    }
                });
    }

    /**
     * エピソード詳細表示.
     *
     * @param itemViewHolder ビューホルダー
     */
    private void displayProgramEpi(final ItemViewHolder itemViewHolder) {
        int epiLineHeight = itemViewHolder.mDetail.getLineHeight();
        if (mEpiSpace <= mContext.dip2px(4)) {
            itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
        } else {
            if (!isShowThumb) {
                mEpiSpace = mEpiSpace - mContext.dip2px(4) + epiLineHeight;
                isShowThumb = true;
            } else {// TODO: 2018/01/16 将来タブレットに調整可能性がある
                mEpiSpace = mEpiSpace - mContext.dip2px(4) - epiLineHeight;
                isShowThumb = false;
            }
            if (mEpiSpace / epiLineHeight > 0) {
                itemViewHolder.mDetail.setMaxLines(mEpiSpace / epiLineHeight);
            } else {
                itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 番組タイトル表示.
     *
     * @param itemViewHolder ビューホルダー
     */
    private void displayProgramTitle(final ItemViewHolder itemViewHolder) {
        int availableSpace = itemViewHolder.mView.getHeight() - mContext.dip2px(12) - mContext.dip2px(15);
        int titleLineHeight = itemViewHolder.mContent.getLineHeight();
        int titleLineCount = itemViewHolder.mContent.getLineCount();
        int titleSpace = titleLineHeight * titleLineCount;
        if (titleSpace > availableSpace) {//パネルスペースを超えるタイトル
            int maxDisplayLine;
            if (availableSpace / titleLineHeight > 0) {
                maxDisplayLine = availableSpace / titleLineHeight;
            } else {
                maxDisplayLine = 1;
            }
            itemViewHolder.mContent.setMaxLines(maxDisplayLine);
            itemViewHolder.mThumbnail.setVisibility(View.INVISIBLE);
            itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
        } else if (titleSpace == availableSpace) {
            itemViewHolder.mThumbnail.setVisibility(View.INVISIBLE);
            itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
        } else {//パネルスペースを超えてないタイトル
            int thumbnailWidth = itemViewHolder.mView.getWidth() - mContext.dip2px(30) - mContext.dip2px(8);
            int thumbnailHeight = mContext.dip2px(THUMBNAIL_HEIGHT) * thumbnailWidth / mContext.dip2px(THUMBNAIL_WIDTH);
            if (availableSpace - titleSpace <= mContext.dip2px(16)) {
                itemViewHolder.mThumbnail.setVisibility(View.INVISIBLE);
                itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
            } else {
                if (availableSpace - titleSpace - mContext.dip2px(16) >= thumbnailHeight && mIsParental) {//サムネル表示
                    mThumbEpiSpace = availableSpace - titleSpace - mContext.dip2px(16);
                    mEpiSpace = mThumbEpiSpace - thumbnailHeight;
                    isShowThumb = true;
                } else {//サムネル非表示
                    itemViewHolder.mThumbnail.setVisibility(View.GONE);
                    mEpiSpace = availableSpace - titleSpace - mContext.dip2px(16);
                    isShowThumb = false;
                }
            }
        }
    }

    /**
     * 年齢制限比較結果取得.
     *
     * @param age 年齢制限
     * @return 年齢制限比較結果
     */
    private boolean setParental(final int age) {
        boolean isParental = false;
        if (mAgeReq < age) {
            isParental = true;
        }
        return isParental;
    }

    @Override
    public int getItemCount() {
        return mProgramList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * コンストラクタ.
         * @param view View
         */
        MyViewHolder(final View view) {
            super(view);
        }

        RelativeLayout layout;
    }
}