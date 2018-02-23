/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
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
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 番組表アダプター.
 */
public class TvProgramListAdapter extends RecyclerView.Adapter<TvProgramListAdapter.MyViewHolder> {

    private static final int THUMBNAIL_HEIGHT = 69;
    private static final int THUMBNAIL_WIDTH = 122;
    private static final int ONE_HOUR_UNIT = 180;
    private static final int TIME_LINE_WIDTH = 44;
    private static final int PADDING_TOP = 12;
    private static final int PADDING_BOTTOM = 15;
    private static final int THUMB_MARGIN_TOP_TITLE = 16;
    private static final int THUMB_MARGIN_LEFT = 30;
    private static final int PADDING_RIGHT = 8;
    private static final int EPI_MARGIN_TOP_THUMB = 4;
    private static final String HYPHEN = "-";
    private static final String MISS_CUT_OUT = "1";
    private static final String MISS_COMPLETE = "2";
    private TvProgramListActivity mContext = null;
    /**
     * ディスプレイ幅.
     */
    private int mScreenWidth = 0;
    /**
     * サムネイル取得プロバイダー.
     */
    private ThumbnailProvider mThumbnailProvider = null;
    /**
     * 現在時刻.
     */
    private String mCurDate = null;

    /**
     * 年齢パレンタル情報.
     */
    private int mAgeReq = 8;
    /**
     * 現在時刻フォマード.
     */
    private static final String CUR_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";
    /**
     * アイテムの配列.
     */
    private final List<ItemViewHolder> mItemViews = new ArrayList<>();
    /**
     * 番組データ.
     */
    private List<ChannelInfo> mProgramList = null;
    /**
     * エピソードタイトルを含むスペース.
     */
    private int mEpiSpace;
    /**
     * サムネイルを表示するかどうか.
     */
    private boolean isShowThumb;
    /**
     * クリップボタンサイズ.
     */
    private final static int CLIP_BUTTON_SIZE = 32;
    /**
     * チャンネルのWIDTH.
     */
    private final static int CHANNEL_WIDTH = 720;
    /**
     * ダウンロード禁止判定フラグ.
     */
    private boolean isDownloadStop = false;

    /**
     * コンストラクタ.
     *
     * @param mContext     コンテクスト
     * @param mProgramList 番組表リスト
     */
    public TvProgramListAdapter(final Activity mContext, final ArrayList<ChannelInfo> mProgramList) {
        this.mProgramList = mProgramList;
        this.mContext = (TvProgramListActivity) mContext;
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
        mAgeReq = userInfoDataProvider.getUserAge();
        mThumbnailProvider = new ThumbnailProvider(mContext);
        getCurTime();
        for (int i = 0; i < mProgramList.size(); i++) {
            ChannelInfo itemChannel = mProgramList.get(i);
            if (itemChannel != null && itemChannel.getSchedules() != null) {
                ArrayList<ScheduleInfo> itemSchedules = itemChannel.getSchedules();
                for (int j = 0; j < itemSchedules.size(); j++) {
                    ScheduleInfo itemSchedule = itemSchedules.get(j);
                    ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(j));
                    setView(itemViewHolder, itemSchedule);
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
     *
     * @return view
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
    public void setAgeReq(final int mAgeReq) {
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
        ItemViewHolder(final ScheduleInfo schedule) {
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
                public void onClick(final View view) {
                    //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、画像比較でクリップ済/未を判定する
                    Bitmap clipButtonBitmap = ((BitmapDrawable) mClipButton.getBackground()).getBitmap();
                    Bitmap activeClipBitmap = null;
                    BitmapDrawable activeClipBitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(),
                            R.mipmap.icon_circle_active_clip, null);
                    if (activeClipBitmapDrawable != null) {
                        activeClipBitmap = activeClipBitmapDrawable.getBitmap();
                    }
                    if (clipButtonBitmap.equals(activeClipBitmap)) {
                        schedule.getClipRequestData().setClipStatus(true);
                    } else {
                        schedule.getClipRequestData().setClipStatus(false);
                    }
                    //クリップボタンイベント
                    mContext.sendClipRequest(schedule.getClipRequestData(), mClipButton);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.layout = (RelativeLayout) view;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams((mScreenWidth - mContext.dip2px(TIME_LINE_WIDTH)) / 2,
                mContext.dip2px(ONE_HOUR_UNIT) * 24);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.layout.setLayoutParams(layoutParams);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChannelInfo itemChannel = mProgramList.get(position);

        if (holder.layout != null) {
            holder.layout.removeAllViews();
        }
        if (itemChannel != null && itemChannel.getSchedules() != null) {
            ArrayList<ScheduleInfo> itemSchedule = itemChannel.getSchedules();
            for (int i = 0; i < itemSchedule.size(); i++) {
                ItemViewHolder itemViewHolder = getUnused();
                if (itemViewHolder == null) {
                    itemViewHolder = new ItemViewHolder(itemSchedule.get(i));
                    setView(itemViewHolder, itemSchedule.get(i));
                }
                itemViewHolder.setUsing();
                holder.layout.addView(itemViewHolder.mView);
                itemViewHolder.mThumbnail.setImageResource(R.mipmap.error_ch_mini);
                //URLによって、サムネイル取得
                String thumbnailURL = itemSchedule.get(i).getImageUrl();
                if (!TextUtils.isEmpty(thumbnailURL) && !isDownloadStop) {
                    itemViewHolder.mThumbnail.setTag(thumbnailURL);
                    Bitmap bitmap = mThumbnailProvider.getThumbnailImage(itemViewHolder.mThumbnail, thumbnailURL);
                    if (bitmap != null) {
                        int thumbnailWidth = itemViewHolder.mView.getWidth() - mContext.dip2px(30) - mContext.dip2px(8);
                        int thumbnailHeight = mContext.dip2px(THUMBNAIL_HEIGHT) * thumbnailWidth / mContext.dip2px(THUMBNAIL_WIDTH);
                        if (thumbnailWidth > 0 && thumbnailHeight > 0) {
                            bitmap.setWidth(thumbnailWidth);
                            bitmap.setHeight(thumbnailHeight);
                        }
                        itemViewHolder.mThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
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
     */
    private void setView(final ItemViewHolder itemViewHolder, final ScheduleInfo itemSchedule) {

        //年齢制限フラグ
        boolean isParental = setParental(StringUtils.convertRValueToAgeReq(mContext, itemSchedule.getRValue()));

        String startTime = itemSchedule.getStartTime();
        String endTime = itemSchedule.getEndTime();
        if (!TextUtils.isEmpty(startTime)) {
            itemViewHolder.mStartM.setText(startTime.substring(14, 16));
        }
        String start = slash2Hyphen(startTime);
        String end = slash2Hyphen(endTime);
        Date endDate = new Date();
        Date curDate = new Date();
        Date startData = new Date();
        SimpleDateFormat format = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
        try {
            endDate = format.parse(end);
            curDate = format.parse(mCurDate);
            startData = format.parse(start);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        float marginTop = itemSchedule.getMarginTop();
        float myHeight = itemSchedule.getMyHeight();
        itemViewHolder.mLayoutParams.height = (int) (myHeight * (mContext.dip2px(ONE_HOUR_UNIT)));
        itemViewHolder.mView.setLayoutParams(itemViewHolder.mLayoutParams);
        itemViewHolder.mView.setY(marginTop * (mContext.dip2px(ONE_HOUR_UNIT)));

        String contentType = itemSchedule.getContentType();
        //放送済み
        if (endDate.compareTo(curDate) == -1) {
            watchByContentType(itemViewHolder, contentType);
        } else {
            //放送中
            if (startData.compareTo(curDate) == -1) {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_playing_gray);
            } else {
                //未放送
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_start_gray);
            }
            itemViewHolder.mView.setTag(1);
        }

        boolean isClipHide = false;
        if (!itemSchedule.isClipExec() || isParental) {
            isClipHide = true;
        }

        String title;
        if (isParental) {
            title = StringUtils.returnAsterisk(mContext);
        } else {
            title = itemSchedule.getTitle();
            itemViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if ((int) view.getTag() == 1) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ContentDetailActivity.class);
                        intent.putExtra(DTVTConstants.SOURCE_SCREEN, mContext.getComponentName().getClassName());
                        OtherContentsDetailData detailData = getOtherContentsDetailData(itemSchedule, ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
                        intent.putExtra(detailData.getRecommendFlg(), detailData);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        String detail = itemSchedule.getDetail();
        itemViewHolder.mDetail.setText(detail);
        changeProgramInfoInOrderToShow(itemViewHolder, isParental, isClipHide, itemSchedule.isClipStatus());
        itemViewHolder.mContent.setText(title);
    }

    /**
     * 視聴できるかのを判定する.
     *
     * @param itemViewHolder ViewHolder
     * @param contentType コンテンツタイプ
     */
    private void watchByContentType(final ItemViewHolder itemViewHolder, final String contentType) {
        //見逃し(あり)
        if (MISS_CUT_OUT.equals(contentType) || MISS_COMPLETE.equals(contentType)) {
            itemViewHolder.mView.setBackgroundResource(R.drawable.program_start_gray);
            itemViewHolder.mView.setTag(1);
        } else {
        //関連VOD(なし)
            itemViewHolder.mView.setBackgroundResource(R.drawable.program_end_gray);
            itemViewHolder.mStartM.setTextColor(ContextCompat.getColor(mContext, R.color.tv_program_miss_vod));
            itemViewHolder.mContent.setTextColor(ContextCompat.getColor(mContext, R.color.tv_program_miss_vod));
            itemViewHolder.mDetail.setTextColor(ContextCompat.getColor(mContext, R.color.tv_program_miss_vod));
            itemViewHolder.mThumbnail.setImageAlpha(128);
            itemViewHolder.mView.setTag(0);
        }
    }

    /**
     * タイムの形で"/"から"-"に変更する.
     *
     * @param time 時間
     * @return 整形後の時間
     */
    private String slash2Hyphen(final String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        return time.substring(0, 4) + HYPHEN + time.substring(5, 7) + HYPHEN
                + time.substring(8, 10) + time.substring(11, 19);
    }

    /**
     * 番組情報表示順変更.
     *
     * @param itemViewHolder ビューホルダー
     * @param isParental 年齢制限フラグ
     * @param isClipHide クリップの表示判定
     * @param isClipStatus クリップの状態
     */
    private void changeProgramInfoInOrderToShow(final ItemViewHolder itemViewHolder, final boolean isParental,
                                                final boolean isClipHide, final boolean isClipStatus) {
        itemViewHolder.mContent.getViewTreeObserver()//タイトル
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        displayProgramTitle(itemViewHolder, isParental);
                        return true;
                    }
                });
        itemViewHolder.mDetail.getViewTreeObserver()//詳細エピソード
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        displayProgramEpi(itemViewHolder, isParental);
                        return true;
                    }
                });
        itemViewHolder.mClipButton.getViewTreeObserver()//クリップボタン
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mClipButton.getViewTreeObserver().removeOnPreDrawListener(this);
                        displayProgramClip(itemViewHolder, isClipHide, isClipStatus);
                        return true;
                    }
                });
    }

    /**
     * エピソード詳細表示.
     *
     * @param itemViewHolder ビューホルダー
     * @param isParental 年齢制限フラグ
     */
    private void displayProgramEpi(final ItemViewHolder itemViewHolder, final boolean isParental) {
        int epiLineHeight = itemViewHolder.mDetail.getLineHeight();
        if (!isParental) {
            if (mEpiSpace <= mContext.dip2px(EPI_MARGIN_TOP_THUMB)) {
                itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
            } else {
                if (!isShowThumb) {
                    mEpiSpace = mEpiSpace - mContext.dip2px(EPI_MARGIN_TOP_THUMB) + epiLineHeight;
                } else {
                    if ((mScreenWidth - mContext.dip2px(TIME_LINE_WIDTH)) / 2 < CHANNEL_WIDTH) {
                        //チャンネル幅判断
                        mEpiSpace = mEpiSpace - mContext.dip2px(EPI_MARGIN_TOP_THUMB) - epiLineHeight;
                    } else {
                        mEpiSpace = mEpiSpace - mContext.dip2px(EPI_MARGIN_TOP_THUMB);
                    }
                }
                if (mEpiSpace / epiLineHeight > 0) {
                    itemViewHolder.mDetail.setMaxLines(mEpiSpace / epiLineHeight);
                } else {
                    itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            itemViewHolder.mDetail.setVisibility(View.GONE);
        }
    }

    /**
     * クリップ表示.
     *
     * @param itemViewHolder ビューホルダー
     * @param isParental     年齢制限フラグ
     * @param isClipStatus   クリップ状態
     */
    private void displayProgramClip(final ItemViewHolder itemViewHolder, final boolean isParental, final boolean isClipStatus) {
        int clipHeight = itemViewHolder.mClipButton.getHeight();
        if (!isParental) {
            if (clipHeight < CLIP_BUTTON_SIZE) {
                itemViewHolder.mClipButton.setVisibility(View.INVISIBLE);
            } else {

                if (isClipStatus) {
                    itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_circle_opacity_clip);
                } else {
                    itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_circle_active_clip);
                }
            }
        } else {
            itemViewHolder.mClipButton.setVisibility(View.GONE);
        }
    }

    /**
     * 番組タイトル表示.
     *
     * @param itemViewHolder ビューホルダー
     * @param isParental 年齢制限フラグ
     */
    private void displayProgramTitle(final ItemViewHolder itemViewHolder, final boolean isParental) {
        int availableSpace = itemViewHolder.mView.getHeight()
                - mContext.dip2px(PADDING_TOP) - mContext.dip2px(PADDING_BOTTOM);
        int titleLineHeight = itemViewHolder.mContent.getLineHeight();
        int titleLineCount = itemViewHolder.mContent.getLineCount();
        int titleSpace = titleLineHeight * titleLineCount;
        if (titleSpace > availableSpace) {
            //パネルスペースを超えるタイトル
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
        } else {
            //パネルスペースを超えてないタイトル
            int thumbnailWidth = itemViewHolder.mView.getWidth() - mContext
                    .dip2px(THUMB_MARGIN_LEFT) - mContext.dip2px(PADDING_RIGHT);
            int thumbnailHeight = mContext.dip2px(THUMBNAIL_HEIGHT) * thumbnailWidth / mContext.dip2px(THUMBNAIL_WIDTH);
            if (availableSpace - titleSpace <= mContext.dip2px(THUMB_MARGIN_TOP_TITLE)) {
                itemViewHolder.mThumbnail.setVisibility(View.INVISIBLE);
                itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
            } else {
                if (availableSpace - titleSpace - mContext
                        .dip2px(THUMB_MARGIN_TOP_TITLE) >= thumbnailHeight && !isParental) {
                    //サムネイル表示
                    int mThumbEpiSpace = availableSpace - titleSpace - mContext.dip2px(THUMB_MARGIN_TOP_TITLE);
                    mEpiSpace = mThumbEpiSpace - thumbnailHeight;
                    isShowThumb = true;
                } else {
                    //サムネイル非表示
                    itemViewHolder.mThumbnail.setVisibility(View.GONE);
                    mEpiSpace = availableSpace - titleSpace - mContext.dip2px(THUMB_MARGIN_TOP_TITLE);
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

    /**
     * 番組表情報を取得する.
     *
     * @return 番組表情報
     */
    public List<ChannelInfo> getProgramList() {
        if (mProgramList != null) {
            return mProgramList;
        }
        return null;
    }

    /**
     * ViewHolder.
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * コンストラクタ.
         * @param view View
         */
        MyViewHolder(final View view) {
            super(view);
        }

        /**
         * RelativeLayout.
         */
        RelativeLayout layout;
    }

    /**
     * コンテンツ詳細に必要なデータを返す.
     *
     * @param itemSchedule レコメンド情報
     * @param recommendFlg レコメンドフラグ
     * @return コンテンツ情報
     */
    private static OtherContentsDetailData getOtherContentsDetailData(final ScheduleInfo itemSchedule, final String recommendFlg) {
        OtherContentsDetailData detailData = new OtherContentsDetailData();

        //コンテンツIDの受け渡しを追加
        detailData.setContentId(itemSchedule.getContentsId());
        detailData.setDispType(itemSchedule.getDispType());
        detailData.setContentsType(itemSchedule.getContentType());
        detailData.setRecommendFlg(recommendFlg);

        return detailData;
    }

    /**
     * サムネイル取得処理を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        isDownloadStop = true;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.stopConnect();
        }
    }

    /**
     * サムネイル取得処理を再度可能な状態にする.
     */
    public void enableConnect() {
        isDownloadStop = false;
    }
}