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
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfoComparator;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
     * スペース.
     */
    private static final String SPACE = " ";
    /**
     * 00.
     */
    private static final String ZERO_MILLISECOND = "00";

    /**
     * 日付タイトル.
     */

    private String mDateTitleText = null;
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
     * 番組データ.
     */
    private List<ChannelInfo> mProgramList = null;
    /**
     * クリップボタンサイズ(dp).
     */
    private final static int CLIP_BUTTON_SIZE = 16;
    /**
     * クリップボタン上マージン(dp).
     */
    private final static int CLIP_BUTTON_TOP_MARGIN_SIZE = 8;
    /**
     * コンテンツ取得エラーテキストサイズ.
     */
    private final static int GET_CONTENT_ERROR_TEXT_SIZE = 11;
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
     * クリップ非活性フラグ.
     */
    private boolean mIsClipActive;
    /**
     * 番組描画間隔. 短いとスクロールが重くなるが描画が遅くなる
     */
    private final static int PROGRAM_DRAW_DURATION_TIME = 100;
    /**
     * 最新の縦スクロールY位置（番組描画順序決定用、見えている位置から近いものを順に描画する）.
     */
    private int mProgramScrollY = 0;

    /**
     * コンストラクタ.
     *
     * @param mContext     コンテクスト
     * @param channels 番組表リスト
     * @param mDateTitleText 番組表タイトル日付
     */
    public TvProgramListAdapter(final Context mContext, final ArrayList<ChannelInfo> channels, final String mDateTitleText) {
        DTVTLogger.start();
        this.mProgramList = channels;
        this.mContext = mContext;
        this.mDateTitleText = mDateTitleText;
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
        mAgeReq = userInfoDataProvider.getUserAge();
        mThumbnailProvider = new ThumbnailProvider(mContext, ThumbnailDownloadTask.ImageSizeType.TV_PROGRAM_LIST);
        getCurTime();
        for (int i = 0; i < mProgramList.size(); i++) {
            ChannelInfo itemChannel = mProgramList.get(i);
            if (itemChannel != null) {
                ArrayList<ScheduleInfo> itemSchedules = itemChannel.getSchedules();
                DTVTLogger.debug("###Channel pos=" + i + ",ChNo:" + itemChannel.getChannelNo() + ",ChName:" + itemChannel.getTitle());
                if (itemSchedules == null || itemSchedules.size() == 0) {
                    // 空のView(背景のみ)を追加
                    ItemViewHolder itemViewHolder = new ItemViewHolder(null);
                } else {
                    for (int j = 0; j < itemSchedules.size(); j++) {
                        ScheduleInfo itemSchedule = itemSchedules.get(j);
                        ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(j));
                        setView(itemViewHolder, itemSchedule);
                    }
                }
            }
        }
        mIsClipActive = UserInfoUtils.getClipActive(mContext);
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
     * 必要になっているチャンネル番号取得.
     * @return   チャンネル番号.
     */
    public int[] getNeedProgramChannels() {
        List<Integer> chNoList = new ArrayList<>();
        for (MyViewHolder viewHolder :mMyViewHolder) {
            int chNo = viewHolder.chNo;
            for (ChannelInfo channelInfo : mProgramList) {
                // Bind済みHolderにChnoが一致するものがある時だけ取得リストに入れる
                if (chNo == channelInfo.getChannelNo()) {
                    // すでに取得済みであれば取得リストに入れない
                    if (channelInfo.getSchedules() == null) {
                        chNoList.add(chNo);
                    } else {
                        DTVTLogger.debug("###Schedules already got ChNo:" + channelInfo.getChannelNo() + " size:" + channelInfo.getSchedules().size());
                        break;
                    }
                }
            }
        }
        int[] newChNo;
        newChNo = new int[chNoList.size()];
        for (int j = 0; j < chNoList.size(); j++) {
            newChNo[j] = chNoList.get(j);
            DTVTLogger.debug("###NeedChNo:"+ newChNo[j]);
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
        private View mView = null;
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
        /**あらすじ描画スペース.*/
        int mEpiSpace = 0;
        /**サムネイルURL.*/
        String mThumbnailURL = null;
        /**
         * 各画面部品初期化.
         *
         * @param schedule 番組情報
         */
        ItemViewHolder(final ScheduleInfo schedule) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item_panel, null, false);
            mStartM = mView.findViewById(R.id.tv_program_item_panel_clip_tv);
            mContent = mView.findViewById(R.id.tv_program_item_panel_content_des_tv);
            mContent.setVisibility(View.INVISIBLE);
            mThumbnail = mView.findViewById(R.id.tv_program_item_panel_content_thumbnail_iv);
            mThumbnail.setVisibility(View.INVISIBLE);
            mDetail = mView.findViewById(R.id.tv_program_item_panel_content_detail_tv);
            mDetail.setVisibility(View.INVISIBLE);
            mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            mInUsage = false;
            mClipButton = mView.findViewById(R.id.tv_program_item_panel_clip_iv);
            mClipButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tv_program_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        DTVTLogger.debug("###Create MyViewHolder!");
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
        DTVTLogger.start();
        ChannelInfo itemChannel = mProgramList.get(position);
        DTVTLogger.debug("###onBindViewHolder Pos:" + position + ",old ChNo:" + holder.chNo + ",new ChNo:" + itemChannel.getChannelNo() + " obj:" + holder.hashCode());
        holder.chNo = itemChannel.getChannelNo();
        setSchedule(itemChannel.getSchedules(), holder);
        mMyViewHolder.add(holder);
        DTVTLogger.end();
    }

    @Override
    public void onViewRecycled(final MyViewHolder holder) {
        super.onViewRecycled(holder);
        DTVTLogger.debug("###onViewRecycled ChNo:" + holder.chNo + " Pos:" + holder.getAdapterPosition() + " obj:" + holder.hashCode());
        holder.stopAddContentViews();
        holder.layout.removeAllViewsInLayout();
        mMyViewHolder.remove(holder);
        holder.chNo = 0xFFFFFFFF;
    }

    /**
     *
     * @param itemSchedule itemSchedule
     * @param holder holder
     */
    private void setSchedule(final ArrayList<ScheduleInfo> itemSchedule, final MyViewHolder holder) {
        DTVTLogger.start();
        if (itemSchedule != null) {
            setItemView(itemSchedule, holder);
        }
        DTVTLogger.end();
    }

    /**
     * 機能
     * ビューを設定.
     *
     * @param itemViewHolder ビューホルダー
     * @param itemSchedule   番組情報
     */
    @SuppressWarnings("OverlyLongMethod")
    private void setView(final ItemViewHolder itemViewHolder, final ScheduleInfo itemSchedule) {
        String contentType = itemSchedule.getContentType();
        String title;

        boolean isClipHide = false;
        RelativeLayout layout = itemViewHolder.mView.findViewById(R.id.tv_program_item_panel_clip_rv);
        if (isFailedContent(itemSchedule)) {
            layout.setVisibility(View.GONE);
            itemViewHolder.mView.setBackgroundResource(R.drawable.program_ng_black);
            title = itemSchedule.getTitle();
            changeProgramInfoInOrderToShow(itemViewHolder, false, true, itemSchedule);
            ViewGroup.LayoutParams lp = itemViewHolder.mContent.getLayoutParams();
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)lp;
            int startMargin = mContext.getResources().getDimensionPixelSize(R.dimen.tv_program_item_panel_margin_right);
            mlp.setMargins(startMargin, 0, 0, 0);
            //マージンを設定
            itemViewHolder.mContent.setLayoutParams(mlp);
            itemViewHolder.mContent.setTextSize(GET_CONTENT_ERROR_TEXT_SIZE);
            itemViewHolder.mContent.setText(title);
        } else {
            layout.setVisibility(View.VISIBLE);
            //年齢制限フラグ
            boolean isParental = StringUtils.isParental(mAgeReq, itemSchedule.getRValue());
            itemViewHolder.mThumbnailURL = itemSchedule.getImageUrl();

            String startTime = DateUtils.formatDateCheckToEpoch(itemSchedule.getStartTime());
            String endTime = DateUtils.formatDateCheckToEpoch(itemSchedule.getEndTime());
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
            float marginTop;
            float myHeight;
            String curStartDay;
            String curEndDay;
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYYMMDD, Locale.JAPAN);
            Date date = new Date();
            try {
                curStartDay = mDateTitleText.replace("/", "-");
                date = sdf.parse(curStartDay);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            curStartDay = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            curEndDay = sdf.format(calendar.getTime());
            String standardStartTime = curStartDay + SPACE + DateUtils.DATE_STANDARD_START;
            String standardEndTime = curEndDay + SPACE +  DateUtils.DATE_STANDARD_END;
            Date startHyphenTime = DateUtils.stringToDate(standardStartTime);
            Date endHyphenTime = DateUtils.stringToDate(standardEndTime);
            String startSlashTime  = standardStartTime.replace("-", "/");
            String endSlashTime = standardEndTime.replace("-", "/");

            if (startData.getTime() < startHyphenTime.getTime()) {
                //前日の日付またぐコンテンツ
                itemViewHolder.mStartM.setText(ZERO_MILLISECOND);
                marginTop = 0;
                //日付またぐコンテンツなので、開始時間を番組表タイトル日付の4：00にする
                itemSchedule.setStartTime(startSlashTime);
            } else {
                marginTop = itemSchedule.getMarginTop();
            }
            if (endDate.getTime() > endHyphenTime.getTime()) {
                //明日の日付またぐコンテンツ.
                //日付またぐコンテンツなので、終了時間を番組表タイトル日付明日の3：59：59にする
                itemSchedule.setEndTime(endSlashTime);
            }
            myHeight = itemSchedule.getMyHeight();
            itemViewHolder.mLayoutParams.height = (int) (myHeight * (((TvProgramListActivity) mContext).dip2px(ONE_HOUR_UNIT)));
            itemViewHolder.mView.setLayoutParams(itemViewHolder.mLayoutParams);
            itemViewHolder.mView.setY(marginTop * (((TvProgramListActivity) mContext).dip2px(ONE_HOUR_UNIT)));
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

            if (!itemSchedule.isClipExec() || isParental) {
                isClipHide = true;
            }

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
                            OtherContentsDetailData detailData = getOtherContentsDetailData(itemSchedule, ContentUtils.PLALA_INFO_BUNDLE_KEY);
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
    }

    /**
     * コンテンツデータ未取得判定.
     *
     * @param itemSchedule 番組表データ
     * @return 未取得フラグ
     */
    private boolean isFailedContent(final ScheduleInfo itemSchedule) {
        if (itemSchedule.getDispType().equals(DataConverter.FAILED_CONTENT_DATA)
                || itemSchedule.getDispType().equals(DataConverter.NO_CONTENT_DATA)) {
            return true;
        } else {
            return false;
        }
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
        //将来のEpoch秒対応のため、ここでEpoch or 日付を吸収する
        String convertTime = DateUtils.formatDateCheckToEpoch(time);
        return convertTime.substring(0, 4) + HYPHEN + convertTime.substring(5, 7) + HYPHEN
                + convertTime.substring(8, 10) + convertTime.substring(11, 19);
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
                        if (mContext != null) {
                            if (!itemViewHolder.isTitleDraw) {
                                displayProgramTitle(itemViewHolder, isParental);
                                itemViewHolder.isTitleDraw = true;
                            }
                        }
                        return true;
                    }
                });
        itemViewHolder.mDetail.getViewTreeObserver()//詳細エピソード
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mDetail.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (mContext != null) {
                            if (!itemViewHolder.isEpiDraw && itemViewHolder.isTitleDraw) {
                                displayProgramEpi(itemViewHolder, isParental);
                                itemViewHolder.isEpiDraw = true;
                            }
                        }
                        return true;
                    }
                });
        itemViewHolder.mStartM.getViewTreeObserver()//クリップボタン
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemViewHolder.mStartM.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (mContext != null) {
                            if (!itemViewHolder.isClipDraw) {
                                displayProgramClip(itemViewHolder, isClipHide, itemSchedule);
                                itemViewHolder.isClipDraw = true;
                            }
                        }
                        return true;
                    }
                });
    }

    /**
     * あらすじ表示.
     *
     * @param itemViewHolder ビューホルダー
     * @param isParental 年齢制限フラグ
     */
    private void displayProgramEpi(final ItemViewHolder itemViewHolder, final boolean isParental) {
        int epiLineHeight = itemViewHolder.mDetail.getLineHeight();
        int epiLineHeightWithMargin = epiLineHeight + ((TvProgramListActivity) mContext).dip2px(EPI_MARGIN_TOP_THUMB);
        // 年齢制限に引っかかった場合はあらすじ描画無し
        if (!isParental) {
            if (itemViewHolder.mEpiSpace < epiLineHeightWithMargin) {
                // 1行分描画するスペースが無ければあらすじ描画しない
                itemViewHolder.mDetail.setVisibility(View.GONE);
            } else {
                // 1行以上描画スペースがあればあらすじ描画する.
                int maxLines = (itemViewHolder.mEpiSpace - ((TvProgramListActivity) mContext).dip2px(EPI_MARGIN_TOP_THUMB)) / epiLineHeight;
                itemViewHolder.mDetail.setMaxLines(maxLines);
                itemViewHolder.mDetail.setVisibility(View.VISIBLE);
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
        String endTime = itemSchedule.getEndTime();
        String end = slash2Hyphen(endTime);
        Date endDate = new Date();
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
        try {
            endDate = format.parse(end);
            curDate = format.parse(mCurDate);
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }

        if (!isClipHide) {
            // 番組の表示領域(上下マージン除く)
            int availableSpace = itemViewHolder.mLayoutParams.height;
            availableSpace = availableSpace - ((TvProgramListActivity) mContext).dip2px(PADDING_TOP) - ((TvProgramListActivity) mContext).dip2px(PADDING_BOTTOM);
            // 分の表示分を除く
            availableSpace = availableSpace - itemViewHolder.mStartM.getHeight();
            // クリップボタンサイズ(上マージン込み)
            int clipHeight = ((TvProgramListActivity) mContext).dip2px(CLIP_BUTTON_SIZE + CLIP_BUTTON_TOP_MARGIN_SIZE);
            if (availableSpace < clipHeight) {
                // 表示領域が足りない場合は非表示.
                itemViewHolder.mClipButton.setVisibility(View.GONE);
            } else {
                if (mIsClipActive) {
                    //未ログイン又は未契約時はクリップボタンを非活性にする
                    if (endDate.compareTo(curDate) == DATE_COMPARE_TO_LOW) {
                        // 放送終了
                        itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_tap_circle_normal_clip_schedule_end);
                    } else {
                        itemViewHolder.mClipButton.setBackgroundResource(R.mipmap.icon_tap_circle_normal_clip);
                    }
                } else {
                    if (itemSchedule.isClipStatus()) {
                        if (endDate.compareTo(curDate) == DATE_COMPARE_TO_LOW
                                && !(MISS_CUT_OUT.equals(itemSchedule.getContentType()) || MISS_COMPLETE.equals(itemSchedule.getContentType()))) {
                            // 放送終了
                            itemViewHolder.mClipButton.setBackgroundResource(R.drawable.tv_program_schedule_end_clip_active_selector);
                            itemViewHolder.mClipButton.setTag(BaseActivity.CLIP_SCHEDULE_END_ACTIVE_STATUS);
                        } else {
                            itemViewHolder.mClipButton.setBackgroundResource(R.drawable.common_clip_active_selector);
                            itemViewHolder.mClipButton.setTag(BaseActivity.CLIP_ACTIVE_STATUS);
                        }
                    } else {
                        if (endDate.compareTo(curDate) == DATE_COMPARE_TO_LOW
                                && !(MISS_CUT_OUT.equals(itemSchedule.getContentType()) || MISS_COMPLETE.equals(itemSchedule.getContentType()))) {
                            // 放送終了
                            itemViewHolder.mClipButton.setBackgroundResource(R.drawable.tv_program_schedule_end_clip_normal_selector);
                            itemViewHolder.mClipButton.setTag(BaseActivity.CLIP_SCHEDULE_END_OPACITY_STATUS);
                        } else {
                            itemViewHolder.mClipButton.setBackgroundResource(R.drawable.tv_program_schedule_clip_normal_selector);
                            itemViewHolder.mClipButton.setTag(BaseActivity.CLIP_OPACITY_STATUS);
                        }
                    }
                    itemViewHolder.mClipButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、タグでクリップ済/未を判定する
                            Object clipTag = itemViewHolder.mClipButton.getTag();
                            if (clipTag.equals(BaseActivity.CLIP_ACTIVE_STATUS) || clipTag.equals(BaseActivity.CLIP_SCHEDULE_END_ACTIVE_STATUS)) {
                                itemSchedule.getClipRequestData().setClipStatus(true);
                            } else {
                                itemSchedule.getClipRequestData().setClipStatus(false);
                            }
                            //クリップボタンイベント
                            ((BaseActivity) mContext).sendClipRequest(itemSchedule.getClipRequestData(), itemViewHolder.mClipButton);
                        }
                    });
                }
                itemViewHolder.mClipButton.setVisibility(View.VISIBLE);
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
        // 番組の表示領域(上下マージン除く)
        int availableSpace = itemViewHolder.mLayoutParams.height
                - ((TvProgramListActivity) mContext).dip2px(PADDING_TOP) - ((TvProgramListActivity) mContext).dip2px(PADDING_BOTTOM);
        // タイトル一行辺り高さ
        int titleLineHeight = itemViewHolder.mContent.getLineHeight();
        // タイトル行数
        int titleLineCount = itemViewHolder.mContent.getLineCount();
        // タイトルの高さ
        int titleSpace = titleLineHeight * titleLineCount;
        if (titleSpace > availableSpace) {
            //パネルスペースを超える長さのタイトルの場合はタイトルを表示可能行数で省略しその他は非表示
            int maxDisplayLine;
            if (availableSpace / titleLineHeight > 0) {
                maxDisplayLine = availableSpace / titleLineHeight;
            } else {
                maxDisplayLine = 1;
            }
            itemViewHolder.mContent.setMaxLines(maxDisplayLine);
            itemViewHolder.mContent.setVisibility(View.VISIBLE);
            itemViewHolder.mThumbnail.setVisibility(View.GONE);
            itemViewHolder.mDetail.setVisibility(View.GONE);
        } else if (titleSpace == availableSpace) {
            // ちょうどタイトルがぴったり収まる場合はそのままタイトル表示してその他は非表示
            itemViewHolder.mContent.setVisibility(View.VISIBLE);
            itemViewHolder.mThumbnail.setVisibility(View.GONE);
            itemViewHolder.mDetail.setVisibility(View.GONE);
        } else {
            // タイトルが余裕持って収まる場合はタイトル表示してそれ以外の表示判定（あらすじは別途）
            itemViewHolder.mContent.setVisibility(View.VISIBLE);
            //年齢制限フィルタにひっかかる場合はサムネイル無し
            if (!isParental) {
                //パネルスペースを超えない高さのタイトルの場合は、まずサムネイルが表示できるか判定
                availableSpace = availableSpace - titleSpace;
                int thumbnailHeight = ((TvProgramListActivity) mContext).dip2px(THUMBNAIL_HEIGHT)
                        + ((TvProgramListActivity) mContext).dip2px(THUMB_MARGIN_TOP_TITLE);
                int thumbAvailableWidhSpace = itemViewHolder.mView.getWidth();
                int clipWidth = (int) mContext.getResources().getDimension(R.dimen.tv_program_item_panel_clip_side);
                int baseThumbWidth = (int) mContext.getResources().getDimension(R.dimen.panel_content_thumbnail_width);
                if (availableSpace >= thumbnailHeight && (thumbAvailableWidhSpace - clipWidth) >= baseThumbWidth) {
                    //マージン含めたサムネイルの高さ以上の余白があればサムネイル表示
                    itemViewHolder.mThumbnail.setImageResource(R.mipmap.loading_ch_mini);
                    itemViewHolder.mThumbnail.setVisibility(View.VISIBLE);
                    //URLによって、サムネイル取得
                    String thumbnailURL = itemViewHolder.mThumbnailURL;
                    if (!TextUtils.isEmpty(thumbnailURL) && !mIsDownloadStop && mThumbnailProvider != null) {
                        itemViewHolder.mThumbnail.setTag(thumbnailURL);
                        Bitmap bitmap = mThumbnailProvider.getThumbnailImage(itemViewHolder.mThumbnail, thumbnailURL);
                        if (bitmap != null) {
                            itemViewHolder.mThumbnail.setImageBitmap(bitmap);
                        }
                        //サムネイルを表示した上で、あらすじを残りスペースに配置する
                        availableSpace = availableSpace - thumbnailHeight;
                    } else {
                        itemViewHolder.mThumbnail.setVisibility(View.GONE);
                    }

                } else {
                    //マージン含めたサムネイルの高さ以上の余白がなければサムネイル非表示
                    //サムネイルを非表示にした上で、あらすじを残りスペースに配置する
                    itemViewHolder.mThumbnail.setVisibility(View.GONE);
                }
            } else {
                itemViewHolder.mThumbnail.setVisibility(View.GONE);
            }
            itemViewHolder.mEpiSpace = availableSpace;
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
        //解放処理の強化により、このタイミングで番組データが存在しない場合が発生
        if (mProgramList == null) {
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
        /**描画処理中フラグ.*/
        boolean mIsRunning = false;
        /**描画完了フラグ.*/
        boolean mIsCompleted = false;
        /**描画Timer.*/
        Timer mTimer = null;

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

            if(itemNum > 0) {
                DTVTLogger.start("###setItemView start ChNo:" + itemSchedules.get(0).getChNo() + " Pos:" + MyViewHolder.this.getAdapterPosition() + " obj:" + MyViewHolder.this.hashCode());
                // 見える物から描画するために現在表示している位置から近い番組から順にソート処理する.
                // 各番組情報に最新Y位置からの概算オフセット絶対値を突っ込む.
                for (ScheduleInfo itemSchedule :itemSchedules) {
                    float offsetY = Math.abs(itemSchedule.getMarginTop() * (((TvProgramListActivity) mContext).dip2px(ONE_HOUR_UNIT)) - mProgramScrollY);
                    itemSchedule.setDrawOffset((int) offsetY);
                }
                Collections.sort(itemSchedules, new ScheduleInfoComparator());
                TimerTask task = new TimerTask() {
                    int count = 0;

                    @Override
                    public void run() {
                        // Timerのスレッド
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mTimer != null) {
                                    if (count < itemNum) {
                                        ScheduleInfo itemSchedule = itemSchedules.get(count);
                                        ItemViewHolder itemViewHolder = new ItemViewHolder(itemSchedules.get(count));
                                        setView(itemViewHolder, itemSchedule);
                                        layout.addView(itemViewHolder.mView);
//                                        DTVTLogger.debug("###addContentView! count:" + count + " ChNo:" + itemSchedule.getChNo() + " Pos:" + MyViewHolder.this.getAdapterPosition() + " obj:" + MyViewHolder.this.hashCode());
                                    }
                                    count++;
                                    if (count >= itemNum) {
                                        // UIスレッド
                                        DTVTLogger.debug("###AddContentViews Complete! count:" + count + " itemNum:" + itemNum + " ChNo:" + itemSchedules.get(0).getChNo() + " Pos:" + MyViewHolder.this.getAdapterPosition() + " obj:" + MyViewHolder.this.hashCode());
                                        mIsCompleted = true;
                                        cancel();
                                        mTimer.cancel();
                                        mTimer = null;
                                    }
                                } else {
                                    DTVTLogger.debug("###AddContentViews Cancel! ChNo:" + itemSchedules.get(0).getChNo() + " Pos:" + MyViewHolder.this.getAdapterPosition() + " obj:" + MyViewHolder.this.hashCode());
                                    cancel();
                                }
                            }
                        });
                    }
                };
                if (mTimer == null) {
                    mTimer = new Timer();
                    mTimer.schedule(task, 0, PROGRAM_DRAW_DURATION_TIME);
                }
                DTVTLogger.end();
            }
        }

        /**
         *コンテンツビュー設定ストップ.
         */
        public void stopAddContentViews() {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            mHandler.removeCallbacksAndMessages(null);
            mIsCompleted = false;
            DTVTLogger.debug("###stopAddContentViews ! ChNo:" + chNo + " Pos:" + this.getAdapterPosition()  + " obj:" + this.hashCode());
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
            mThumbnailProvider.removeAllMemoryCache();
        }
    }

    /**
     * サムネイル取得処理を再度可能な状態にする.
     */
    public void enableConnect() {
        mIsDownloadStop = false;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.enableConnect();
        }
    }

    /**
     * BG→FG復帰時のチャンネルリスト更新用.
     * @param newProgramList 番組表
     */
    public void setProgramList(final List<ChannelInfo> newProgramList) {
        DTVTLogger.start();
        // 部分的なデータが来ることになるので自身で記憶しているチャンネルリストへMappingするようにする
        // また、描画反映が必要なので、ViewHolderを調べて該当のチャンネルがあれば、描画反映する
        if (newProgramList != null) {
            for (int i = 0; i < newProgramList.size(); i++) {
                ChannelInfo newChannelInfo = newProgramList.get(i);
                for (int j = 0; j < mProgramList.size(); j++) {
                    ChannelInfo mChannelInfo = mProgramList.get(j);
                    if (newChannelInfo.getChannelNo() == mChannelInfo.getChannelNo()) {
                        if (newChannelInfo.getSchedules() != null && newChannelInfo.getSchedules().size() > 0) {
                            mProgramList.set(j, newChannelInfo);
                            for (int pos = 0; pos < mMyViewHolder.size(); pos++) {
                                if (newChannelInfo.getChannelNo() == mMyViewHolder.get(pos).chNo) {
                                    DTVTLogger.start("setItemView start ChNo:" + mChannelInfo.getChannelNo());
                                    setItemView(newProgramList.get(i).getSchedules(), mMyViewHolder.get(pos));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * 番組表子ビューの更新.
     *
     * @param itemSchedules 番組表データ
     * @param holder チャンネル枠
     */
    private void setItemView(final ArrayList<ScheduleInfo> itemSchedules, final MyViewHolder holder) {
        if (holder.mTimer == null && !holder.mIsCompleted) {
            holder.startAddContentViews(itemSchedules);
        } else {
            DTVTLogger.debug("###Ch Now Drawing ChNo:" + holder.chNo);
        }
    }

    /**
     * ガベージコレクションされやすくなるように各部にヌルを格納する.
     */
    public void removeData() {
        if (mThumbnailProvider != null) {
            mThumbnailProvider.removeMemoryCache();
            mThumbnailProvider = null;
        }

        if (mMyViewHolder != null) {
            mMyViewHolder.clear();
            mMyViewHolder = null;
        }

        if (mProgramList != null) {
            mProgramList.clear();
            mProgramList = null;
        }
        System.gc();
    }

    /**
     * 最新の縦スクロールY位置設定.
     * @param programScrollY 最新の縦スクロールY位置
     */
    public void setProgramScrollY(final int programScrollY) {
        this.mProgramScrollY = programScrollY;
    }

}
