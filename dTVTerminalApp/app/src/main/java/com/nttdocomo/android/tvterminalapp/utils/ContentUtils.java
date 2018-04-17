/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.Calendar;
import java.util.Date;

/**
 * コンテンツ共通Utilクラス.
 */
public class ContentUtils {

    public enum ContentsType {
        /**
         * テレビ.
         */
        TV,
        /**
         * ビデオ.
         */
        VOD,
        /**
         * ひかりTV内dch_見逃し(３２以上).
         */
        DCHANNEL_VOD_OVER_31,
        /**
         * ひかりTV内dch_見逃し(３1以内).
         */
        DCHANNEL_VOD_31,
        /**
         * レンタル.
         */
        RENTAL,
        /**
         * プレミアム.
         */
        PREMIUM,
        /**
         * その他.
         */
        OTHER
    }

    /**
     * 多階層コンテンツであるか判定する.
     * @param contentsData コンテンツデータ
     * @return 多階層
     */
    public static boolean isChildContentList(@Nullable final ContentsData contentsData) {
        return null != contentsData && contentsData.hasChildContentList();
    }

    /**
     * 番組、VODの判断(レコメンド).
     *
     * @param serviceId disp_type
     * @param categoryId tv_service
     * @return VOD、TV、その他
     */
    public static ContentsType getContentsTypeByRecommend(final int serviceId, final String categoryId) {
        ContentsType cType = ContentsType.OTHER;
        switch (serviceId) {
            //dTV
            case ContentDetailActivity.DTV_CONTENTS_SERVICE_ID:
                if (ContentDetailActivity.H4D_CATEGORY_TERRESTRIAL_DIGITAL.equals(categoryId)
                        || ContentDetailActivity.H4D_CATEGORY_SATELLITE_BS.equals(categoryId)) {
                    cType = ContentsType.VOD;
                }
                break;
            //dアニメストア
            case ContentDetailActivity.D_ANIMATION_CONTENTS_SERVICE_ID:
                if (ContentDetailActivity.H4D_CATEGORY_TERRESTRIAL_DIGITAL.equals(categoryId)) {
                    cType = ContentsType.VOD;
                }
                break;
            //dTVチャンネル
            case ContentDetailActivity.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                if (ContentDetailActivity.H4D_CATEGORY_TERRESTRIAL_DIGITAL.equals(categoryId)) {
                    cType = ContentsType.TV;
                } else if (ContentDetailActivity.H4D_CATEGORY_SATELLITE_BS.equals(categoryId)
                        || ContentDetailActivity.H4D_CATEGORY_IPTV.equals(categoryId)) {
                    cType = ContentsType.VOD;
                }
                break;
            //ひかりTV for docomo
            case ContentDetailActivity.DTV_HIKARI_CONTENTS_SERVICE_ID:
                if (ContentDetailActivity.H4D_CATEGORY_IPTV.equals(categoryId)
                        || ContentDetailActivity.H4D_CATEGORY_DTV_CHANNEL_BROADCAST.equals(categoryId)) {
                    cType = ContentsType.TV;
                } else if (ContentDetailActivity.H4D_CATEGORY_DTV_CHANNEL_MISSED.equals(categoryId)
                        || ContentDetailActivity.H4D_CATEGORY_DTV_CHANNEL_RELATION.equals(categoryId)
                        || ContentDetailActivity.H4D_CATEGORY_HIKARITV_VOD.equals(categoryId)
                        || ContentDetailActivity.H4D_CATEGORY_HIKARI_DTV_SVOD.equals(categoryId)) {
                    cType = ContentsType.VOD;
                }
                break;
            default:
                break;
        }
        return cType;
    }

    // これ聞く->週間番組ランキングがOhterになっている
    /**
     * 番組、VODの判定.
     *
     * @param dispType disp_type
     * @param tvService tv_service
     * @param contentsType contents_type
     * @param availEndDate 配信日時(avail_end_date)
     * @param vodStartDate VOD配信日時(vod_start_date)
     * @param vodEndDate VOD配信日時(vod_end_date)
     * @return VOD、TV、DCHANNEL_VOD_OVER_31、DCHANNEL_VOD_31、その他
     */
    public static ContentsType getContentsTypeByPlala(final String dispType, final String tvService,
                                                                final String contentsType, final long availEndDate,
                                                                final long vodStartDate, final long vodEndDate) {
        ContentsType cType = ContentsType.OTHER;
        if (ContentDetailActivity.VIDEO_PROGRAM.equals(dispType)
                || ContentDetailActivity.VIDEO_SERIES.equals(dispType)) {
            //ひかりTV_VOD、ひかりTV内dTV
            cType = DateUtils.getContentsTypeByAvailEndDate(availEndDate);
        } else if (ContentDetailActivity.TV_PROGRAM.equals(dispType)) {
            if (ContentDetailActivity.TV_SERVICE_FLAG_HIKARI.equals(tvService)) {
                //ひかりTV_番組
                cType = ContentsType.TV;
            } else if (ContentDetailActivity.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(tvService)) {
                if (ContentDetailActivity.CONTENT_TYPE_FLAG_THREE.equals(contentsType)) {
                    //ひかりTV内dTVチャンネル_関連VOD
                    cType = DateUtils.getContentsTypeByAvailEndDate(availEndDate);
                } else if (ContentDetailActivity.CONTENT_TYPE_FLAG_ONE.equals(contentsType)
                        || ContentDetailActivity.CONTENT_TYPE_FLAG_TWO.equals(contentsType)) {
                    Calendar cal = Calendar.getInstance();
                    Date nowDate = cal.getTime();
                    cal.setTimeInMillis(vodStartDate * 1000);
                    Date startDate = cal.getTime();
                    if (startDate.compareTo(nowDate) != 1) {
                        //ひかりTV内dTVチャンネル_見逃し
                        if (DateUtils.isOver31Day(vodEndDate)) {
                            //ひかりTV内dTVチャンネル_見逃し(32日以上)
                            cType = ContentsType.DCHANNEL_VOD_OVER_31;
                        } else {
                            //ひかりTV内dTVチャンネル_見逃し(31日以内)
                            cType = ContentsType.DCHANNEL_VOD_31;
                        }
                    } else {
                        //ひかりTV内dTVチャンネル_番組
                        cType = ContentsType.TV;
                    }
                } else {
                    //ひかりTV内dTVチャンネル_番組
                    cType = ContentsType.TV;
                }
            }
        }
        return cType;
    }

    /**
     * レンタル プレミアムビデオ判定.
     * @param dispType disp_type
     * @param estFlg estflg
     * @param chSod chsod
     * @return RENTAL、PREMIUM
     */
    public static ContentsType getContentsTypeRental(final String dispType, final String estFlg, final String chSod) {
        ContentsType cType = ContentsType.OTHER;
        final String ZERO_FLAG = "0";
        if (ContentDetailActivity.VIDEO_PROGRAM.equals(dispType)
                || ContentDetailActivity.VIDEO_PACKAGE.equals(dispType)) {
            //レンタル
            if (estFlg == null || estFlg.isEmpty() || estFlg.equals(ZERO_FLAG)) {
                cType = ContentsType.RENTAL;
            }
        } else if (ContentDetailActivity.SUBSCRIPTION_PACKAGE.equals(dispType)
                || ContentDetailActivity.SERIES_SVOD.equals(dispType)) {
            if (chSod == null || chSod.isEmpty() || chSod.equals(ZERO_FLAG)) {
                cType = ContentsType.PREMIUM;
            }
        }
        return cType;
    }
    /**
     * コンテンツ配信期限の表示 DREM-2011
     * コンテンツ配信前表示(対向ぷららサーバ) DREM-2047
     *
     * 表示用のテキストをセットする
     * @param holder          ビューの集合
     * @param listContentInfo 行データー
     * @return true 配信期限の表示する場合
     */
    public static boolean setPeriodText(final Context context, final ContentsAdapter.ViewHolder holder, final ContentsData listContentInfo) {
        final String dispType = listContentInfo.getDispType();
        final String tvService = listContentInfo.getTvService();
        final String contentsType = listContentInfo.getContentsType();
        final long availEndDate = listContentInfo.getAvailEndDate();
        final long vodStartDate = listContentInfo.getVodStartDate();
        final long vodEndDate = listContentInfo.getVodEndDate();

        final ContentsType periodContentsType = getContentsTypeByPlala(dispType, tvService, contentsType, availEndDate, vodStartDate, vodEndDate);

        String viewingPeriod = "";
        switch (periodContentsType) {
            case VOD:// VOD
            case DCHANNEL_VOD_31: // ひかりTV内dch_見逃し(３1以内).
                if (DateUtils.isBefore(vodStartDate)) { //から
                    viewingPeriod = DateUtils.getContentsDetailVodDate(context, vodStartDate);
                    viewingPeriod = StringUtils.getConnectStrings(
                            context.getString(R.string.common_date_format_start_str), viewingPeriod);
                } else { //まで //"見逃し"は他のどころで対応している
                    if (periodContentsType ==  ContentUtils.ContentsType.VOD) {
                        //VOD(m/d（曜日）まで)
                        viewingPeriod = DateUtils.getContentsDetailVodDate(context, availEndDate);
                    } else if (periodContentsType == ContentUtils.ContentsType.DCHANNEL_VOD_31) {
                        //VOD(m/d（曜日）まで)
                        viewingPeriod = DateUtils.getContentsDetailVodDate(context, vodEndDate);
                        viewingPeriod = StringUtils.getConnectStrings(viewingPeriod, context.getString(R.string.home_contents_pipe),
                                context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                    }
                }
                break;
            default:
                break;
        }


        // 実際の
        switch (periodContentsType) {
            case VOD:
            case DCHANNEL_VOD_31:
                if (!TextUtils.isEmpty(viewingPeriod)) {
                    holder.tv_time.setVisibility(View.VISIBLE);
                    SpannableString spannableString = new SpannableString(viewingPeriod);
                    int subStart = 0;
                    int subEnd = 0;
                    if (viewingPeriod.contains(context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing))) {
                        subStart = viewingPeriod.indexOf(context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                        subEnd = spannableString.length();
                    }
                    //「見逃し」は黄色文字で表示する
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.contents_detail_video_miss_color)),
                            subStart, subEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tv_time.setText(spannableString);
                }
                return true;
            case DCHANNEL_VOD_OVER_31:
                break;
            case TV:
            case OTHER:
                break;
            default:
                break;
        }
        return false;
    }
}
