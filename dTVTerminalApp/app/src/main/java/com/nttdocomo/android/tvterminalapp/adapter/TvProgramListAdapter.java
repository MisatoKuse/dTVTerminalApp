/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.utils.ViewUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 番組表アダプター.
 */
public class TvProgramListAdapter extends RecyclerView.Adapter<TvProgramListAdapter.MyViewHolder> {

    /**
     * サムネイル高さ.
     */
    private static final int THUMBNAIL_HEIGHT = 69;
    /**
     * サムネイル幅.
     */
    private static final int THUMBNAIL_WIDTH = 122;
    /**
     * 1時間幅(番組表表示時).
     */
    private static final int ONE_HOUR_UNIT = 180;
    /**
     * タイムライン幅.
     */
    private static final int TIME_LINE_WIDTH = 44;
    /**
     * 上部クリアランス.
     */
    private static final int PADDING_TOP = 12;
    /**
     * 下部クリアランス.
     */
    private static final int PADDING_BOTTOM = 15;
    /**
     * サムネイルタイトル用上部マージン.
     */
    private static final int THUMB_MARGIN_TOP_TITLE = 16;
    /**
     * サムネイル左側マージン.
     */
    private static final int THUMB_MARGIN_LEFT = 30;
    /**
     * 右側クリアランス.
     */
    private static final int PADDING_RIGHT = 8;
    /**
     * エピソード上部用マージン.
     */
    private static final int EPI_MARGIN_TOP_THUMB = 4;
    /**
     * ハイフン.
     */
    private static final String HYPHEN = "-";
    /**
     * 見逃し判定(あり)パラメータ.
     */
    private static final String MISS_CUT_OUT = "1";
    /**
     * 見逃し判定(あり)パラメータ.
     */
    private static final String MISS_COMPLETE = "2";
    /**
     * コンテキスト.
     */
    private Context mContext = null;
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
    private List<ItemViewHolder> mItemViews = new ArrayList<>();
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
    private boolean mIsShowThumb;
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
    private boolean mIsDownloadStop = false;
    /**
     * 設定済みViewHolder.
     */
    private List<MyViewHolder> mMyViewHolder = new ArrayList<>();
    /**
     * 放送中または未放送viewHolder Tag.
     */
    private final static int VIEW_HOLDER_TAG_ONE = 1;
    /**
     * 関連VOD(なし)viewHolder Tag.
     */
    private final static int VIEW_HOLDER_TAG_ZERO = 0;
    /**
     * 日付比較リザルト.
     */
    private final static int DATE_COMPARE_TO_LOW = -1;
    /**
     * コンストラクタ.
     *
     * @param mContext     コンテクスト
     * @param channels 番組表リスト
     */
    public TvProgramListAdapter(final Context mContext, final ArrayList<ChannelInfo> channels) {
        //TODO :★ここではチャンネルのリストだけ受け取る。番組は後から取得して、チャンネルリストに当てはめていく。
        //TODO :★各番組単位のViewは番組情報を取得したりスクロール停止したタイミングで、生成・addViewする。
        DTVTLogger.start();
        this.mProgramList = channels;
        this.mContext = mContext;
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
        mAgeReq = userInfoDataProvider.getUserAge();
        mThumbnailProvider = new ThumbnailProvider(mContext, ThumbnailDownloadTask.ImageSizeType.TV_PROGRAM_LIST);
        getCurTime();
        for (int i = 0; i < mProgramList.size(); i++) {
            ChannelInfo itemChannel = mProgramList.get(i);
            if (itemChannel != null) {
                ArrayList<ScheduleInfo> itemSchedules = itemChannel.getSchedules();
                if (itemSchedules == null || itemSchedules.size() == 0) {
                    // 空のView(背景のみ)を追加
                    ItemViewHolder itemViewHolder = new ItemViewHolder(null);
                    mItemViews.add(itemViewHolder);
                } else {
                    for (int j = 0; j < itemSchedules.size(); j++) {
                        ScheduleInfo itemSchedule = itemSchedules.get(j);
                        ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(j));
                        setView(itemViewHolder, itemSchedule);
                        mItemViews.add(itemViewHolder);
                    }
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * コンストラクタ.
     *
     * @param mContext     コンテクスト
     * @param mProgramList 番組表リスト
     */
    public TvProgramListAdapter(final Context mContext, final List<ChannelInfo> mProgramList) {
        //TODO :★ここではチャンネルのリストだけ受け取る。番組は後から取得して、チャンネルリストに当てはめていく。
        //TODO :★各番組単位のViewは番組情報を取得したりスクロール停止したタイミングで、生成・addViewする。
        DTVTLogger.start();
        this.mProgramList = mProgramList;
        this.mContext = mContext;
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
        mAgeReq = userInfoDataProvider.getUserAge();
        mThumbnailProvider = new ThumbnailProvider(mContext, ThumbnailDownloadTask.ImageSizeType.TV_PROGRAM_LIST);
        getCurTime();
        for (int i = 0; i < mProgramList.size(); i++) {
            ChannelInfo itemChannel = mProgramList.get(i);
            if (itemChannel != null) {
                ArrayList<ScheduleInfo> itemSchedules = itemChannel.getSchedules();
                if (itemSchedules == null || itemSchedules.size() == 0) {
                    // 空のView(背景のみ)を追加
                    ItemViewHolder itemViewHolder = new ItemViewHolder(null);
                    mItemViews.add(itemViewHolder);
                } else {
                    for (int j = 0; j < itemSchedules.size(); j++) {
                        ScheduleInfo itemSchedule = itemSchedules.get(j);
                        ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(j));
                        setView(itemViewHolder, itemSchedule);
                        mItemViews.add(itemViewHolder);
                    }
                }
            }
        }
        DTVTLogger.end();
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
     * チャンネル番号取得.
     * @return   チャンネル番号.
     */
    public int[] getNeedProgramChannels() {
        List<Integer> chNoList = new ArrayList<>();
        for (MyViewHolder viewHolder :mMyViewHolder) {
            int chNo = viewHolder.chNo;
            for (ChannelInfo channelInfo : mProgramList) {
                if (chNo == channelInfo.getChannelNo() && channelInfo.getSchedules() == null) {
                    chNoList.add(chNo);
                }
            }
        }
        int[] newChNo;
        newChNo = new int[chNoList.size()];
        for (int j = 0; j < chNoList.size(); j++) {
            newChNo[j] = chNoList.get(j);
        }
        return newChNo;
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
        /**レイアウトインフレーター.*/
        public View mView = null;
        /**同じビュー使用されてるか.*/
        private boolean mInUsage = false;
        /**開始時間TextView.*/
        TextView mStartM = null;
        /**コンテンツ説明TextView.*/
        TextView mContent = null;
        /**サムネイル.*/
        ImageView mThumbnail = null;
        /**RelativeLayout LayoutParams.*/
        RelativeLayout.LayoutParams mLayoutParams = null;
        /**クリップアイオン.*/
        ImageView mClipButton = null;
        /**詳細TextView.*/
        TextView mDetail = null;
        /**タイトル描画開始か.*/
        boolean isTitleDraw = false;
        /**エピソード描画開始か.*/
        boolean isEpiDraw = false;
        /**クリープアイコン描画開始か.*/
        boolean isClipDraw = false;
        /**同じビュー使用されてる.*/
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
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        mMyViewHolder.add(holder);
        DTVTLogger.debug("view Holder ===============>");
        holder.layout = (RelativeLayout) view;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams((mScreenWidth - ((TvProgramListActivity) mContext).dip2px(TIME_LINE_WIDTH)) / 2,
                ((TvProgramListActivity) mContext).dip2px(ONE_HOUR_UNIT) * 24);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.layout.setLayoutParams(layoutParams);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChannelInfo itemChannel = mProgramList.get(position);
        holder.chNo = itemChannel.getChannelNo();
        DTVTLogger.debug("mProgramList size ===============>" + mProgramList.size());
        DTVTLogger.debug("onBindViewHolder position===============>" + position + " ChNo:" + holder.chNo);
        DTVTLogger.debug("channel Name ===============>" + itemChannel.getTitle());
        setSchedule(itemChannel.getSchedules(), holder);
    }

    @Override
    public void onViewRecycled(final MyViewHolder holder) {
        super.onViewRecycled(holder);
        holder.stopAddContentViews();
        holder.layout.removeAllViewsInLayout();
        DTVTLogger.debug("onViewRecycled chNo:" + holder.chNo);
        holder.chNo = 0xFFFFFFFF;
    }

    /**
     *
     * @param itemSchedule itemSchedule
     * @param holder holder
     */
    private void setSchedule(final ArrayList<ScheduleInfo> itemSchedule, final MyViewHolder holder) {
        if (itemSchedule != null) {
            setItemView(itemSchedule, holder);


//            for (int i = 0; i < itemSchedule.size(); i++) {
//                ItemViewHolder itemViewHolder = getUnused();
//                if (itemViewHolder == null) {
//                    itemViewHolder = new ItemViewHolder(itemSchedule.get(i));
//                    setView(itemViewHolder, itemSchedule.get(i));
//                }
//                itemViewHolder.setUsing();
//                holder.layout.addView(itemViewHolder.mView);
//                itemViewHolder.mThumbnail.setImageResource(R.mipmap.error_ch_mini);
//                //URLによって、サムネイル取得
//                String thumbnailURL = itemSchedule.get(i).getImageUrl();
//                if (!TextUtils.isEmpty(thumbnailURL) && !isDownloadStop) {
//                    itemViewHolder.mThumbnail.setTag(thumbnailURL);
//                    Bitmap bitmap = mThumbnailProvider.getThumbnailImage(itemViewHolder.mThumbnail, thumbnailURL);
//                    if (bitmap != null) {
//                        int thumbnailWidth = itemViewHolder.mView.getWidth() - ((TvProgramListActivity)mContext).dip2px(30)
//                                            - ((TvProgramListActivity)mContext).dip2px(8);
//                        int thumbnailHeight = ((TvProgramListActivity)mContext).dip2px(THUMBNAIL_HEIGHT) * thumbnailWidth
//                                            / ((TvProgramListActivity)mContext).dip2px(THUMBNAIL_WIDTH);
//                        if (thumbnailWidth > 0 && thumbnailHeight > 0) {
//                            bitmap.setWidth(thumbnailWidth);
//                            bitmap.setHeight(thumbnailHeight);
//                        }
//                        itemViewHolder.mThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
//                        itemViewHolder.mThumbnail.setImageBitmap(bitmap);
//                    }
//                }
//            }
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
        boolean isParental = StringUtils.isParental(mAgeReq, itemSchedule.getRValue());

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
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        float marginTop = itemSchedule.getMarginTop();
        float myHeight = itemSchedule.getMyHeight();
        itemViewHolder.mLayoutParams.height = (int) (myHeight * (((TvProgramListActivity) mContext).dip2px(ONE_HOUR_UNIT)));
        itemViewHolder.mView.setLayoutParams(itemViewHolder.mLayoutParams);
        itemViewHolder.mView.setY(marginTop * (((TvProgramListActivity) mContext).dip2px(ONE_HOUR_UNIT)));

        String contentType = itemSchedule.getContentType();
        //放送済み
        if (endDate.compareTo(curDate) == DATE_COMPARE_TO_LOW) {
            watchByContentType(itemViewHolder, contentType);
        } else {
            //放送中
            if (startData.compareTo(curDate) == DATE_COMPARE_TO_LOW) {
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_playing_gray);
            } else {
                //未放送
                itemViewHolder.mView.setBackgroundResource(R.drawable.program_start_gray);
            }
            itemViewHolder.mView.setTag(VIEW_HOLDER_TAG_ONE);
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
                    if ((int) view.getTag() == VIEW_HOLDER_TAG_ONE) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ContentDetailActivity.class);
                        intent.putExtra(DtvtConstants.SOURCE_SCREEN, ((TvProgramListActivity) mContext).getComponentName().getClassName());
                        OtherContentsDetailData detailData = getOtherContentsDetailData(itemSchedule, ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
                        intent.putExtra(detailData.getRecommendFlg(), detailData);
                        TvProgramListActivity tvProgramListActivity = (TvProgramListActivity) mContext;
                        tvProgramListActivity.startActivity(intent);
                    }
                }
            });
        }
        String detail = itemSchedule.getDetail();
        itemViewHolder.mDetail.setText(detail);
        changeProgramInfoInOrderToShow(itemViewHolder, isParental, isClipHide, itemSchedule);
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
            itemViewHolder.mView.setBackgroundResource(R.drawable.program_missed_gray);
            itemViewHolder.mView.setTag(VIEW_HOLDER_TAG_ONE);
        } else {
            //関連VOD(なし)
            itemViewHolder.mView.setBackgroundResource(R.drawable.program_end_gray);
            itemViewHolder.mStartM.setTextColor(ContextCompat.getColor(mContext, R.color.tv_program_miss_vod));
            itemViewHolder.mContent.setTextColor(ContextCompat.getColor(mContext, R.color.tv_program_miss_vod));
            itemViewHolder.mDetail.setTextColor(ContextCompat.getColor(mContext, R.color.tv_program_miss_vod));
            itemViewHolder.mThumbnail.setImageAlpha(128);
            itemViewHolder.mView.setTag(VIEW_HOLDER_TAG_ZERO);
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
     * @param itemSchedule 番組情報
     */
    private void changeProgramInfoInOrderToShow(final ItemViewHolder itemViewHolder, final boolean isParental,
                                                final boolean isClipHide, final ScheduleInfo itemSchedule) {
        itemViewHolder.mContent.getViewTreeObserver()//タイトル
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (!itemViewHolder.isTitleDraw) {
                            displayProgramTitle(itemViewHolder, isParental);
                            itemViewHolder.isTitleDraw = true;
                        }
                        return true;
                    }
                });
        itemViewHolder.mDetail.getViewTreeObserver()//詳細エピソード
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mDetail.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (!itemViewHolder.isEpiDraw) {
                            displayProgramEpi(itemViewHolder, isParental);
                            itemViewHolder.isEpiDraw = true;
                        }
                        return true;
                    }
                });
        itemViewHolder.mClipButton.getViewTreeObserver()//クリップボタン
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mClipButton.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (!itemViewHolder.isClipDraw) {
                            displayProgramClip(itemViewHolder, isClipHide, itemSchedule);
                            itemViewHolder.isClipDraw = true;
                        }
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
            if (mEpiSpace <= ((TvProgramListActivity) mContext).dip2px(EPI_MARGIN_TOP_THUMB)) {
                itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
            } else {
                if (!mIsShowThumb) {
                    mEpiSpace = mEpiSpace - ((TvProgramListActivity) mContext).dip2px(EPI_MARGIN_TOP_THUMB) + epiLineHeight;
                } else {
                    if ((mScreenWidth - ((TvProgramListActivity) mContext).dip2px(TIME_LINE_WIDTH)) / 2 < CHANNEL_WIDTH) {
                        //チャンネル幅判断
                        mEpiSpace = mEpiSpace - ((TvProgramListActivity) mContext).dip2px(EPI_MARGIN_TOP_THUMB) - epiLineHeight;
                    } else {
                        mEpiSpace = mEpiSpace - ((TvProgramListActivity) mContext).dip2px(EPI_MARGIN_TOP_THUMB);
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
     * @param isClipHide     年齢制限フラグ
     * @param itemSchedule   クリップ状態
     */
    private void displayProgramClip(final ItemViewHolder itemViewHolder, final boolean isClipHide, final ScheduleInfo itemSchedule) {
        DTVTLogger.start();
        int clipHeight = itemViewHolder.mClipButton.getHeight();
        if (!isClipHide) {
            if (clipHeight < CLIP_BUTTON_SIZE) {
                itemViewHolder.mClipButton.setVisibility(View.INVISIBLE);
            } else {
                if (!UserInfoUtils.getClipActive(mContext)) {
                    //未ログイン又は未契約時はクリップボタンを非活性にする
                    itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_tap_circle_normal_clip);
                } else {
                    if (itemSchedule.isClipStatus()) {
                        itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_circle_active_clip_schedule_end);
                        itemViewHolder.mClipButton.setTag(BaseActivity.CLIP_ACTIVE_STATUS);
                    } else {
                        itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_circle_normal_clip_schedule_end);
                        itemViewHolder.mClipButton.setTag(BaseActivity.CLIP_OPACITY_STATUS);
                    }
                    itemViewHolder.mClipButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、タグでクリップ済/未を判定する
                            Object clipTag = itemViewHolder.mClipButton.getTag();
                            if (clipTag.equals(BaseActivity.CLIP_ACTIVE_STATUS)) {
                                itemSchedule.getClipRequestData().setClipStatus(true);
                            } else {
                                itemSchedule.getClipRequestData().setClipStatus(false);
                            }
                            //クリップボタンイベント
                            ((BaseActivity) mContext).sendClipRequest(itemSchedule.getClipRequestData(), itemViewHolder.mClipButton);
                        }
                    });

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
        DTVTLogger.start();
        int availableSpace = itemViewHolder.mView.getHeight()
                - ((TvProgramListActivity) mContext).dip2px(PADDING_TOP) - ((TvProgramListActivity) mContext).dip2px(PADDING_BOTTOM);
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
            int thumbnailWidth = itemViewHolder.mView.getWidth() - ((TvProgramListActivity) mContext)
                    .dip2px(THUMB_MARGIN_LEFT) - ((TvProgramListActivity) mContext).dip2px(PADDING_RIGHT);
            int thumbnailHeight = ((TvProgramListActivity) mContext).dip2px(THUMBNAIL_HEIGHT) * thumbnailWidth
                    / ((TvProgramListActivity) mContext).dip2px(THUMBNAIL_WIDTH);
            if (availableSpace - titleSpace <= ((TvProgramListActivity) mContext).dip2px(THUMB_MARGIN_TOP_TITLE)) {
                itemViewHolder.mThumbnail.setVisibility(View.INVISIBLE);
                itemViewHolder.mDetail.setVisibility(View.INVISIBLE);
            } else {
                if (availableSpace - titleSpace - ((TvProgramListActivity) mContext)
                        .dip2px(THUMB_MARGIN_TOP_TITLE) >= thumbnailHeight && !isParental) {
                    //サムネイル表示
                    int mThumbEpiSpace = availableSpace - titleSpace - ((TvProgramListActivity) mContext).dip2px(THUMB_MARGIN_TOP_TITLE);
                    mEpiSpace = mThumbEpiSpace - thumbnailHeight;
                    mIsShowThumb = true;
                } else {
                    //サムネイル非表示
                    itemViewHolder.mThumbnail.setVisibility(View.GONE);
                    mEpiSpace = availableSpace - titleSpace - ((TvProgramListActivity) mContext).dip2px(THUMB_MARGIN_TOP_TITLE);
                    mIsShowThumb = false;
                }
            }
        }
        DTVTLogger.end();
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
        //解放処理の強化により、このタイミングで番組データが存在しない場合が発生
        if(mProgramList == null) {
            //番組データが無いのでゼロを返す
            return 0;
        } else {
            return mProgramList.size();
        }
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
    class MyViewHolder extends RecyclerView.ViewHolder {
        /**ハンドラー.*/
        final Handler mHandler = new Handler();
        /**.*/
        boolean mIsRunning = false;

        /**
         * コンストラクタ.
         * @param view View
         */
        MyViewHolder(final View view) {
            super(view);
        }

        /**
         * コンテンツビュー設定開始.
         * @param itemSchedules  itemSchedules　itemSchedules
         */
        public void startAddContentViews(final ArrayList<ScheduleInfo> itemSchedules) {
            final int itemNum = itemSchedules.size();
            TimerTask task = new TimerTask() {
                int count = 0;
                @Override
                public void run() {
                    // Timerのスレッド
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (count < itemNum) {
                                ScheduleInfo itemSchedule = itemSchedules.get(count);
                                ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(count));
                                setView(itemViewHolder, itemSchedule);
                                layout.addView(itemViewHolder.mView);
                                itemViewHolder.mThumbnail.setImageResource(R.mipmap.error_ch_mini);
                                //URLによって、サムネイル取得
                                String thumbnailURL = itemSchedule.getImageUrl();
                                if (!TextUtils.isEmpty(thumbnailURL) && !mIsDownloadStop && mThumbnailProvider != null) {
                                    itemViewHolder.mThumbnail.setTag(thumbnailURL);
                                    Bitmap bitmap = mThumbnailProvider.getThumbnailImage(itemViewHolder.mThumbnail, thumbnailURL);
                                    if (bitmap != null) {
                                        int thumbnailWidth = itemViewHolder.mView.getWidth() - ((TvProgramListActivity) mContext).dip2px(30)
                                                - ((TvProgramListActivity) mContext).dip2px(8);
                                        int thumbnailHeight = ((TvProgramListActivity) mContext).dip2px(THUMBNAIL_HEIGHT) * thumbnailWidth
                                                / ((TvProgramListActivity) mContext).dip2px(THUMBNAIL_WIDTH);
                                        if (thumbnailWidth > 0 && thumbnailHeight > 0) {
                                            bitmap.setWidth(thumbnailWidth);
                                            bitmap.setHeight(thumbnailHeight);
                                        }
                                        itemViewHolder.mThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
                                        itemViewHolder.mThumbnail.setImageBitmap(bitmap);
                                    }
                                }
                                DTVTLogger.debug("addContentView! count:" + count + " ChNo:" + itemSchedule.getChNo());
                            }
                            count++;
                            if (count >= itemNum) {
                                // UIスレッド
                                cancel();
                            }
                        }
                    });
                }
            };
            if (!mIsRunning) {
                Timer t = new Timer();
                t.schedule(task, 0, 50);
            }
            mIsRunning = true;
        }

        /**
         *コンテンツビュー設定ストップ.
         */
        public void stopAddContentViews() {
            mHandler.removeCallbacksAndMessages(null);
            mIsRunning = false;
        }


        /**
         * RelativeLayout.
         */
        RelativeLayout layout;

        /**
         * チャンネル番号.
         */
        int chNo = 0xFFFFFFFF;
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
        detailData.setContentsId(itemSchedule.getContentsId());
        detailData.setDispType(itemSchedule.getDispType());
        detailData.setContentsType(itemSchedule.getContentType());
        detailData.setRecommendFlg(recommendFlg);
        detailData.setThumb(itemSchedule.getImageDetailUrl());

        return detailData;
    }

    /**
     * サムネイル取得処理を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsDownloadStop = true;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.stopConnect();
        }
    }

    /**
     * サムネイル取得処理を再度可能な状態にする.
     */
    public void enableConnect() {
        mIsDownloadStop = false;
    }

    /**
     * BG→FG復帰時のチャンネルリスト更新用.
     * @param newProgramList 番組表
     */
    public void setProgramList(final List<ChannelInfo> newProgramList) {
        mProgramList = newProgramList;
        // TODO　:★部分的なデータが来ることになるので自身で記憶しているチャンネルリストへMappingするようにする
        // TODO　:★また、描画反映が必要なので、ViewHolderを調べて該当のチャンネルがあれば、描画反映する
        if (newProgramList != null) {
            for (int i = 0; i < newProgramList.size(); i++) {
                ChannelInfo newChannelInfo = newProgramList.get(i);
                for (int j = 0; j < mProgramList.size(); j++) {
                    ChannelInfo mChannelInfo = mProgramList.get(j);
                    if (newChannelInfo.getChannelNo() == mChannelInfo.getChannelNo()) {
                        if (newChannelInfo.getSchedules() != null && newChannelInfo.getSchedules().size() > 0) {
                            mProgramList.set(j, newChannelInfo);
                            for (MyViewHolder myViewHolder : mMyViewHolder) {
                                if (newChannelInfo.getChannelNo() == myViewHolder.chNo) {
                                    setItemView(newProgramList.get(i).getSchedules(), myViewHolder);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 番組表子ビューの更新.
     *
     * @param itemSchedules 番組表データ
     * @param holder チャンネル枠
     */
    private void setItemView(final ArrayList<ScheduleInfo> itemSchedules, final MyViewHolder holder) {
        DTVTLogger.start("ChNo:" + holder.chNo);
        holder.startAddContentViews(itemSchedules);
    }

    /**
     * ガベージコレクションされやすくなるように各部にヌルを格納する.
     */
    public void removeData() {
        if(mThumbnailProvider != null) {
            mThumbnailProvider.removeMemoryCache();
            mThumbnailProvider = null;
        }

        if(mMyViewHolder != null) {
            mMyViewHolder.clear();
            mMyViewHolder = null;
        }

        if(mProgramList != null) {
            mProgramList.clear();
            mProgramList = null;
        }

        if(mItemViews != null) {
            for (ItemViewHolder view : mItemViews) {
                ViewUtils.cleanAllViews(view.mView);
                ViewUtils.cleanAllViews(view.mClipButton);
                ViewUtils.cleanAllViews(view.mContent);
                ViewUtils.cleanAllViews(view.mDetail);
                ViewUtils.cleanAllViews(view.mStartM);
                ViewUtils.cleanAllViews(view.mThumbnail);
                view.mView = null;
                view.mClipButton = null;
                view.mContent = null;
                view.mDetail = null;
                view.mStartM = null;
                view.mThumbnail = null;
            }

            System.gc();
        }
    }
}