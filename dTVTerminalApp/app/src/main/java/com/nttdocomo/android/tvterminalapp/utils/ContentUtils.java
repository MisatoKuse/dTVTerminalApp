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
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * コンテンツ共通Utilクラス.
 */
public class ContentUtils {
    /** レコメンドのCategoryId 01. **/
    private static final String RECOMMEND_CATEGORY_ID_ONE = "01";
    /** レコメンドのCategoryId 02. **/
    private static final String RECOMMEND_CATEGORY_ID_TWO = "02";
    /** レコメンドのCategoryId 03. **/
    private static final String RECOMMEND_CATEGORY_ID_THREE = "03";
    /** レコメンドのCategoryId 04. **/
    private static final String RECOMMEND_CATEGORY_ID_FOUR = "04";
    /** レコメンドのCategoryId 05. **/
    private static final String RECOMMEND_CATEGORY_ID_FIVE = "05";
    /** レコメンドのCategoryId 06. **/
    private static final String RECOMMEND_CATEGORY_ID_SIX = "06";
    /** レコメンドのCategoryId 07. **/
    private static final String RECOMMEND_CATEGORY_ID_SEVEN = "07";
    /** レコメンドのCategoryId 08. **/
    private static final String RECOMMEND_CATEGORY_ID_EIGHT = "08";
    /** レコメンドのCategoryId 10. **/
    private static final String RECOMMEND_CATEGORY_ID_TEN = "10";
    /** チャンネルタイプ kihon_ch.*/
    private static final String CH_TYPE_KIHON = "kihon_ch";
    /** チャンネルタイプ basic_ch.*/
    private static final String CH_TYPE_BASIC = "basic_ch";
    /** チャンネルタイプ trial_free.*/
    private static final String CH_TYPE_TRIAL = "trial_free";
    /** チャンネルタイプ premium_ch.*/
    private static final String CH_TYPE_PREMIUM = "premium_ch";
    /** DTVフラグ 1.*/
    public static final String IS_DTV_FLAG = "1";
    /** DTVフラグ 0.*/
    private static final String NOT_DTV_FLAG = "0";
    /** BVフラグ 1.*/
    private static final String IS_BV_FLAG = "1";
    /**
     * コンテンツタイプ.
     */
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
         * 地デジ.
         */
        DIGITAL_TERRESTRIAL_BROADCASTING,
        /**
         * BS.
         */
        BROADCASTING_SATELLITE,
        /**
         * ひかりTV(番組).
         */
        HIKARI_TV,
        /**
         * ひかりTV(Now On Air).
         */
        HIKARI_TV_NOW_ON_AIR,
        /**
         * ひかり内dTVCh(番組).
         */
        HIKARI_IN_DCH_TV,
        /**
         * ひかりTV(VOD).
         */
        HIKARI_TV_VOD,
        /**
         * ひかり内dTVCh.
         */
        HIKARI_IN_DCH,
        /**
         * ひかり内dTV.
         */
        HIKARI_IN_DTV,
        /**
         * ひかり内dTVCh(見逃し).
         */
        HIKARI_IN_DCH_MISS,
        /**
         * ひかり内dTVCh(関連).
         */
        HIKARI_IN_DCH_RELATION,
        /**
         * ひかり(録画).
         */
        HIKARI_RECORDED,
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
         * PureDTV.
         */
        PURE_DTV,
        /**
         * PureDTVCh.
         */
        PURE_DTV_CHANNEL,
        /**
         * PureDTVCh(見逃し).
         */
        PURE_DTV_CHANNEL_MISS,
        /**
         * PureDTVCh(関連番組).
         */
        PURE_DTV_CHANNEL_RELATION,
        /**
         * dアニメストア.
         */
        D_ANIME_STORE,
        /**
         * その他.
         */
        OTHER
    }

    /**
     * 視聴可否種別.
     */
    public enum ViewIngType {
        /**
         * 視聴可能.
         */
        ENABLE_WATCH,
        /**
         * 視聴可能(期限30日以内なので視聴可能期限表示).
         */
        ENABLE_WATCH_LIMIT_THIRTY,
        /**
         * 視聴可能(期限30日以内なので視聴可能期限表示) ※複数期限があり、期限が一番長いのを基準にする場合.
         */
        ENABLE_WATCH_LIMIT_THIRTY_LONGEST,
        /**
         * 視聴可能(期限30日超、視聴期限非表示).
         */
        ENABLE_WATCH_LIMIT_THIRTY_OVER,
        /**
         * 視聴不可(再生導線非表示).
         */
        DISABLE_WATCH_AND_PLAY,
        /**
         * 視聴不可契約導線表示.
         */
        DISABLE_WATCH_AGREEMENT_DISPLAY,
        /**
         * 視聴不可契約導線表示(購入CH判定時).
         */
        DISABLE_CHANNEL_WATCH_AGREEMENT_DISPLAY,
        /**
         * 視聴不可契約導線表示(購入VOD判定時).
         */
        DISABLE_VOD_WATCH_AGREEMENT_DISPLAY,
        /**
         * 視聴不可.
         */
        DISABLE_WATCH,
        /**
         * 購入済みCH判定開始.
         */
        PREMIUM_CHECK_START,
        /**
         * 購入済みVOD判定開始.
         */
        SUBSCRIPTION_CHECK_START,
        /**
         * 視聴可否判定外ステータス ※サーバレスポンスが正常ならこの状態にはならない想定.
         */
        NONE_STATUS
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
    @SuppressWarnings("OverlyComplexMethod")
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
     * @param estflg ESTフラグ
     * @param chsvod CHSVOD(chsvod)
     * @return VOD、TV、DCHANNEL_VOD_OVER_31、DCHANNEL_VOD_31、その他
     */
    public static ContentsType getContentsTypeByPlala(final String dispType, final String tvService,
                                                                final String contentsType, final long availEndDate,
                                                                final long vodStartDate, final long vodEndDate, final String estflg,
                                                                final String chsvod) {
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
                        } else if (DateUtils.isIn31Day(vodEndDate)) {
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
        } else {
            cType = getContentsTypeRental(dispType, estflg, chsvod);
            if (cType == ContentsType.PREMIUM || cType == ContentsType.RENTAL) {
                cType = DateUtils.getContentsTypeByAvailEndDate(availEndDate);
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
    private static ContentsType getContentsTypeRental(final String dispType, final String estFlg, final String chSod) {
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
     * コンテンツ配信期限の表示 DREM-2011.
     * コンテンツ配信前表示(対向ぷららサーバ) DREM-2047
     *
     * 表示用のテキストをセットする
     * @param context         コンテクスト
     * @param textView        テキストビュー
     * @param listContentInfo 行データー
     */
    @SuppressWarnings({"EnumSwitchStatementWhichMissesCases", "OverlyComplexMethod", "OverlyLongMethod"})
    public static void setPeriodText(final Context context, final TextView textView, final ContentsData listContentInfo) {
        final String dispType = listContentInfo.getDispType();
        final String tvService = listContentInfo.getTvService();
        final String contentsType = listContentInfo.getContentsType();
        final String estFlg = listContentInfo.getEstFlg();
        final String chsVod = listContentInfo.getChsVod();
        final long availEndDate = listContentInfo.getAvailEndDate();
        final long vodStartDate = listContentInfo.getVodStartDate();
        final long vodEndDate = listContentInfo.getVodEndDate();

        final ContentsType periodContentsType = getContentsTypeByPlala(dispType, tvService, contentsType, availEndDate, vodStartDate, vodEndDate,
                estFlg, chsVod);

        String viewingPeriod = "";
        switch (periodContentsType) {
            case VOD:// VOD
            case DCHANNEL_VOD_OVER_31: // ひかりTV内dch_見逃し(３２日以上).
            case DCHANNEL_VOD_31: // ひかりTV内dch_見逃し(３1日以内).
                if (DateUtils.isBefore(vodStartDate)) { //から
                    viewingPeriod = DateUtils.getContentsDetailVodDate(context, vodStartDate);
                    viewingPeriod = StringUtils.getConnectStrings(
                            context.getString(R.string.common_date_format_start_str), viewingPeriod);
                } else { //まで、見逃し
                    if (periodContentsType ==  ContentsType.VOD) {
                        //VOD(m/d（曜日）まで)
                        viewingPeriod = DateUtils.getContentsDetailVodDate(context, availEndDate);
                    } else if (periodContentsType == ContentsType.DCHANNEL_VOD_OVER_31) {
                        //「見逃し」
                        viewingPeriod = context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing);
                    } else if (periodContentsType == ContentsType.DCHANNEL_VOD_31) {
                        //VOD(m/d（曜日）まで)
                        viewingPeriod = DateUtils.getContentsDetailVodDate(context, vodEndDate);
                        //「見逃し」
                        viewingPeriod = StringUtils.getConnectStrings(viewingPeriod, context.getString(R.string.home_contents_hyphen),
                                context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                    }
                }
                break;
            case TV:// TV
                //番組(m/d（曜日）h:ii - h:ii)
                long start = 0;
                String startDate = listContentInfo.getPublishStartDate();
                if (!TextUtils.isEmpty(startDate) && DataBaseUtils.isNumber(startDate)) {
                    start = Long.parseLong(listContentInfo.getPublishStartDate());
                }
                if (!TextUtils.isEmpty(listContentInfo.getChannelName())) {
                    viewingPeriod = StringUtils.getConnectStrings(DateUtils.getContentsDateString(start),
                            context.getString(R.string.home_contents_hyphen),
                            listContentInfo.getChannelName());
                } else {
                    viewingPeriod = DateUtils.getContentsDateString(start);
                }
                break;
            default:
                break;
        }

        // 表示
        switch (periodContentsType) {
            case TV:
            case VOD:
            case DCHANNEL_VOD_OVER_31:
            case DCHANNEL_VOD_31:
                if (!TextUtils.isEmpty(viewingPeriod)) {
                    textView.setVisibility(View.VISIBLE);
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
                    textView.setText(spannableString);
                }
            case OTHER:
                break;
            default:
                break;
        }
    }

    /**
     * コンテンツ種別判定(ひかり).
     *
     * @param metaFullData コンテンツ詳細データ(ひかり)
     * @return ContentsType
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static ContentsType getHikariContentsType(final VodMetaFullData metaFullData) {
        //配信種別
        String dispType = metaFullData.getDisp_type();
        //TVサービス
        String tvService = metaFullData.getmTv_service();
        //コンテンツ種別
        String contentsType = metaFullData.getmContent_type();
        //DTVフラグ
        String dTvFlag = metaFullData.getDtv();
        //Vod配信日時
        long vodStartDate = metaFullData.getmVod_start_date();
        //現在時刻
        long current = DateUtils.getNowTimeFormatEpoch();
        //Now On Air フラグ
        boolean isNowOnAir =
                metaFullData.getPublish_start_date() <= current && current < metaFullData.getPublish_end_date();

        if (dispType == null) {
            return ContentsType.OTHER;
        } else {
            switch (dispType) {
                case ContentDetailActivity.VIDEO_PROGRAM:
                case ContentDetailActivity.VIDEO_SERIES:
                    //dispType=video_program || video_series
                    if (dTvFlag == null) {
                        //dTvFlag=0 || 未設定 -> ひかりTV_VOD
                        return ContentsType.HIKARI_TV_VOD;
                    } else {
                        switch (dTvFlag) {
                            case ContentDetailActivity.DTV_FLAG_ONE:
                                //dTvFlag=1 -> ひかりTV内dTV
                                return ContentsType.HIKARI_IN_DTV;
                            default:
                                //dTvFlag=0 || 未設定 -> ひかりTV_VOD
                                return ContentsType.HIKARI_TV_VOD;
                        }
                    }
                case ContentDetailActivity.TV_PROGRAM:
                    //dispType=tv_program
                    if (tvService == null) {
                        //tvService=other
                        return ContentsType.OTHER;
                    } else {
                        switch (tvService) {
                            case ContentDetailActivity.CONTENT_TYPE_FLAG_ONE:
                                //tv_service=1 -> ひかりTV_番組
                                if (isNowOnAir) {
                                    return ContentsType.HIKARI_TV_NOW_ON_AIR;
                                } else {
                                    return ContentsType.HIKARI_TV;
                                }
                            case ContentDetailActivity.CONTENT_TYPE_FLAG_TWO:
                                //tv_service=2
                                if (contentsType == null) {
                                    //contentsType=other -> ひかりTV_番組
                                    if (isNowOnAir) {
                                        return ContentsType.HIKARI_TV_NOW_ON_AIR;
                                    } else {
                                        return ContentsType.HIKARI_TV;
                                    }
                                } else {
                                    switch (contentsType) {
                                        case ContentDetailActivity.CONTENT_TYPE_FLAG_ONE:
                                        case ContentDetailActivity.CONTENT_TYPE_FLAG_TWO:
                                            if (vodStartDate <= current) {
                                                //VOD配信日時(vod_start_date) <= 現在時刻 -> ひかりTV内dTVチャンネル_見逃し
                                                return ContentsType.HIKARI_IN_DCH_MISS;
                                            } else {
                                                //VOD配信日時(vod_start_date) > 現在時刻 -> ひかりTV内dTVチャンネル_番組
                                                return ContentsType.HIKARI_IN_DCH_TV;
                                            }
                                        case ContentDetailActivity.CONTENT_TYPE_FLAG_THREE:
                                            //contentsType=3 -> ひかりTV内dTVチャンネル_関連VOD
                                            return ContentsType.HIKARI_IN_DCH_RELATION;
                                        default:
                                            //contentsType=other -> ひかりTV_番組
                                            if (isNowOnAir) {
                                                return ContentsType.HIKARI_TV_NOW_ON_AIR;
                                            } else {
                                                return ContentsType.HIKARI_TV;
                                            }
                                    }
                                }
                            default:
                                //tvService = other
                                return ContentsType.OTHER;
                        }
                    }
                default:
                    //dispType = other
                    return ContentsType.OTHER;
            }
        }
    }

    /**
     * コンテンツ種別判定(レコメンド).
     *
     * @param detailData コンテンツ詳細データ(レコメンド)
     * @return ContentsType
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static ContentsType getRecommendContentsType(final OtherContentsDetailData detailData) {
        int serviceId = detailData.getServiceId();
        String categoryId = detailData.getCategoryId();
        switch (serviceId) {
            //serviceId = 44
            case ContentDetailActivity.DTV_HIKARI_CONTENTS_SERVICE_ID:
                //serviceId = 44
                if (categoryId == null) {
                    return ContentsType.OTHER;
                } else {
                    switch (categoryId) {
                        //categoryId = 01
                        case RECOMMEND_CATEGORY_ID_ONE:
                            //地デジ
                            return ContentsType.DIGITAL_TERRESTRIAL_BROADCASTING;
                        //categoryId = 02
                        case RECOMMEND_CATEGORY_ID_TWO:
                            //BS
                            return ContentsType.BROADCASTING_SATELLITE;
                        //categoryId = 03
                        case RECOMMEND_CATEGORY_ID_THREE:
                            //IPTV
                            return ContentsType.HIKARI_TV;
                        //categoryId = 04
                        case RECOMMEND_CATEGORY_ID_FOUR:
                            //dTVチャンネル　放送
                            return ContentsType.HIKARI_IN_DCH;
                        //categoryId = 05
                        case RECOMMEND_CATEGORY_ID_FIVE:
                            //dTVチャンネル　VOD（見逃し）
                            return ContentsType.HIKARI_IN_DCH_MISS;
                        //categoryId = 06
                        case RECOMMEND_CATEGORY_ID_SIX:
                            //dTVチャンネル　VOD（関連番組）
                            return ContentsType.HIKARI_IN_DCH_RELATION;
                        //categoryId = 07
                        case RECOMMEND_CATEGORY_ID_SEVEN:
                            //録画
                            return ContentsType.HIKARI_RECORDED;
                        //categoryId = 08
                        case RECOMMEND_CATEGORY_ID_EIGHT:
                            //ひかりTV VOD
                            return ContentsType.HIKARI_TV_VOD;
                        //categoryId = 10
                        case RECOMMEND_CATEGORY_ID_TEN:
                            //dTV SVOD
                            return ContentsType.HIKARI_IN_DTV;
                        default:
                            return ContentsType.OTHER;
                    }
                }
                //serviceId = 15
            case ContentDetailActivity.DTV_CONTENTS_SERVICE_ID:
                if (categoryId == null) {
                    return ContentsType.OTHER;
                } else {
                    switch (categoryId) {
                        //categoryId = 01
                        case RECOMMEND_CATEGORY_ID_ONE:
                            //categoryId = 02
                        case RECOMMEND_CATEGORY_ID_TWO:
                            //Pure Dtv
                            return ContentsType.PURE_DTV;
                        default:
                            return ContentsType.OTHER;
                    }
                }
                //serviceId = 43
            case ContentDetailActivity.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                if (categoryId == null) {
                    return ContentsType.OTHER;
                } else {
                    switch (categoryId) {
                        //categoryId = 01
                        case RECOMMEND_CATEGORY_ID_ONE:
                            //dTVチャンネル　放送
                            return ContentsType.PURE_DTV_CHANNEL;
                        //categoryId = 02
                        case RECOMMEND_CATEGORY_ID_TWO:
                            //dTVチャンネル　VOD（見逃し）
                            return ContentsType.PURE_DTV_CHANNEL_MISS;
                        //categoryId = 03
                        case RECOMMEND_CATEGORY_ID_THREE:
                            //dTVチャンネル　VOD（関連番組）
                            return ContentsType.PURE_DTV_CHANNEL_RELATION;
                        default:
                            return ContentsType.OTHER;
                    }
                }
                //serviceId = 17
            case ContentDetailActivity.D_ANIMATION_CONTENTS_SERVICE_ID:
                if (categoryId == null) {
                    return ContentsType.OTHER;
                } else {
                    switch (categoryId) {
                        //categoryId = 01
                        case RECOMMEND_CATEGORY_ID_ONE:
                            //dアニメストア
                            return ContentsType.D_ANIME_STORE;
                        default:
                            return ContentsType.OTHER;
                    }
                }
        }
        return ContentsType.OTHER;
    }

    /**
     * 視聴可否ステータスを取得.
     *
     * @param contractInfo 　契約情報
     * @param metaFullData Vodメタデータ
     * @param channelInfo  Channelメタデータ
     * @return 視聴可否ステータス
     */
    public static ViewIngType getViewingType(final String contractInfo, final VodMetaFullData metaFullData, final ChannelInfo channelInfo) {
        if (contractInfo == null || contractInfo.isEmpty() || UserInfoUtils.CONTRACT_INFO_NONE.equals(contractInfo)) {
            //契約情報が未設定、または"none"の場合は視聴不可(契約導線を表示)
            DTVTLogger.debug("Unviewable(Not contract)");
            return ViewIngType.DISABLE_WATCH_AGREEMENT_DISPLAY;
        } else if (UserInfoUtils.CONTRACT_INFO_DTV.equals(contractInfo)) {
            return ContentUtils.contractInfoOne(metaFullData);
        } else if (UserInfoUtils.CONTRACT_INFO_H4D.equals(contractInfo)) {
            return ContentUtils.contractInfoTwo(metaFullData, channelInfo);
        } else {
            return ViewIngType.NONE_STATUS;
        }
    }

    /**
     * 視聴可否判定、契約情報が"001"の場合.
     *
     * @param metaFullData Vodメタデータ
     * @return 視聴可否判定結果
     */
    @SuppressWarnings("OverlyComplexMethod")
    private static ViewIngType contractInfoOne(final VodMetaFullData metaFullData) {
        DTVTLogger.debug("disp_type: " + metaFullData.getDisp_type());
        //メタレスポンス「disp_type」が「tv_program」
        if (ContentDetailActivity.TV_PROGRAM.equals(metaFullData.getDisp_type())) {
            DTVTLogger.debug("tv_service: " + metaFullData.getmTv_service());
            String tvService = metaFullData.getmTv_service();
            //メタレスポンス「tv_service」が「2」
            if (tvService != null && ContentDetailActivity.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(tvService)) {
                long publishStartDate = metaFullData.getPublish_start_date();
                long publishEndDate = metaFullData.getPublish_end_date();
                long nowDate = DateUtils.getNowTimeFormatEpoch();
                //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」
                if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                    //「publish_end_date」が現在日時から1か月以内
                    if (DateUtils.isLimitThirtyDay(publishEndDate)) {
                        //視聴可能期限(「publish_end_dateまで」)を表示
                        return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                    } else {
                        //視聴可能期限は非表示
                        return ViewIngType.ENABLE_WATCH;
                    }
                    //メタレスポンス「publish_start_date」 >= 現在時刻
                } else if (publishStartDate >= nowDate) {
                    //視聴不可(放送時間外のため再生導線を非表示)
                    DTVTLogger.debug("Unviewable(Hide playing method because outside broadcasting time)");
                    return ViewIngType.DISABLE_WATCH_AND_PLAY;
                    //メタレスポンス「publish_end_date」 <= 現在時刻
                } else if (publishEndDate <= nowDate) {
                    long vodStartDate = metaFullData.getmVod_start_date();
                    long vodEndDate = metaFullData.getmVod_end_date();
                    //「vod_start_date」が未設定
                    if (vodStartDate == 0) {
                        //視聴不可
                        return ViewIngType.DISABLE_WATCH;
                        //「vod_start_date」 <= 現在時刻 < 「vod_end_date」
                    } else if (vodStartDate <= nowDate && nowDate < vodEndDate) {
                        //「vod_end_date」が現在時刻から1か月以内の場合視聴可能期限(「vod_end_dateまで」)を表示
                        if (DateUtils.isLimitThirtyDay(vodEndDate)) {
                            return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                        } else {
                            return ViewIngType.ENABLE_WATCH;
                        }
                        //「vod_end_date」 <= 現在時刻
                    } else if (vodEndDate <= nowDate) {
                        //視聴不可
                        return ViewIngType.DISABLE_WATCH;
                    } else {
                        //視聴可否判定外ステータス
                        return ViewIngType.DISABLE_WATCH;
                    }
                } else {
                    //視聴可否判定外ステータス
                    return ViewIngType.NONE_STATUS;
                }
                //メタレスポンス「tv_service」が「1」または未設定
            } else if (tvService == null || tvService.equals(ContentDetailActivity.TV_SERVICE_FLAG_HIKARI)) {
                //視聴不可(視聴導線を非表示)
                return ViewIngType.DISABLE_WATCH;
            } else {
                //視聴可否判定外ステータス
                return ViewIngType.NONE_STATUS;
            }
        } else {
            //視聴可否判定外ステータス
            return ViewIngType.NONE_STATUS;
        }
    }

    /**
     * 視聴可否判定、契約情報が"002"の場合.
     * 仕様が複雑なため、確認を簡単にするため、仕様の文章と連動する記載とする.
     *
     * @param metaFullData Vodメタデータ
     * @param channelInfo チャンネル情報
     * @return 視聴可否判定結果
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private static ViewIngType  contractInfoTwo(final VodMetaFullData metaFullData, final ChannelInfo channelInfo) {

        //チャンネル情報がnullなら判定不可
        if (channelInfo == null) {
            return ViewIngType.NONE_STATUS;
        }

        String dispType = metaFullData.getDisp_type();
        String vodServiceId = metaFullData.getmService_id();
        String channelServiceId = channelInfo.getServiceId();
        String tvService = metaFullData.getmTv_service();
        String chType = channelInfo.getChannelType();
        long publishStartDate = metaFullData.getPublish_start_date();
        long publishEndDate = metaFullData.getPublish_end_date();
        long nowDate = DateUtils.getNowTimeFormatEpoch();
        //メタレスポンス「disp_type」が「tv_program」
        switch (dispType) {
            case ContentDetailActivity.TV_PROGRAM:
                //メタレスポンス「tv_service」が「1」
                if (ContentDetailActivity.TV_SERVICE_FLAG_HIKARI.equals(tvService)) {
                    //メタレスポンスの「service_id」とCH一覧取得IFで取得したチャンネルの「service_id」で番組に紐づくチャンネルを特定する
                    if (vodServiceId.equals(channelServiceId)) {
                        //チャンネルメタレスポンス「ch_type」が「kihon_ch」、「basic_ch」、「trial_free」
                        if (CH_TYPE_KIHON.equals(chType)
                                || CH_TYPE_BASIC.equals(chType)
                                || CH_TYPE_TRIAL.equals(chType)) {
                            //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」
                            if (publishStartDate <= nowDate
                                    && nowDate < publishEndDate) {
                                //視聴可能
                                return ViewIngType.ENABLE_WATCH;
                                //メタレスポンス「publish_start_date」 > 現在時刻 または 現在時刻 >= 「publish_end_date」
                            } else if (publishStartDate > nowDate
                                    || nowDate >= publishEndDate) {
                                //視聴不可(放送時間外のため再生導線を非表示)
                                return ViewIngType.DISABLE_WATCH_AND_PLAY;
                            } else {
                                //視聴可否判定外
                                return ViewIngType.NONE_STATUS;
                            }
                            //チャンネルメタレスポンス「ch_type」が「premium_ch」
                        } else if (CH_TYPE_PREMIUM.equals(chType)) {
                            //購入済み判定開始
                            return ViewIngType.PREMIUM_CHECK_START;
                        } else {
                            //視聴可否判定外
                            return ViewIngType.NONE_STATUS;
                        }
                    } else {
                        //取得したチャンネル情報が不正の場合
                        return ViewIngType.NONE_STATUS;
                    }
                    //メタレスポンス「tv_service」が「2」
                } else if (ContentDetailActivity.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(tvService)) {
                    //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」
                    if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                        //「publish_end_date」が現在日時から1か月以内
                        if (DateUtils.isLimitThirtyDay(publishEndDate)) {
                            //視聴可能期限(「publish_end_dateまで」)を表示
                            return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                            //「publish_end_date」が現在日時から1か月以上先
                        } else {
                            //視聴可能期限は非表示
                            return ViewIngType.ENABLE_WATCH;
                        }
                        //メタレスポンス「publish_start_date」 >= 現在時刻
                    } else if (publishStartDate >= nowDate) {
                        //視聴不可(放送時間外のため再生導線を非表示)
                        return ViewIngType.DISABLE_WATCH_AND_PLAY;
                        //メタレスポンス「publish_end_date」 <= 現在時刻
                    } else if (publishEndDate <= nowDate) {
                        long vodStartDate = (metaFullData.getmVod_start_date());
                        long vodEndDate = (metaFullData.getmVod_end_date());
                        //「vod_start_date」が未設定
                        if (vodStartDate == 0) {
                            //視聴不可
                            return ViewIngType.DISABLE_WATCH;
                            //「vod_start_date」 <= 現在時刻 < 「vod_end_date」
                        } else if (vodStartDate <= nowDate && nowDate < vodEndDate) {
                            //「vod_end_date」が現在時刻から1か月以内の場合視聴可能期限(「vod_end_dateまで」)を表示
                            if (DateUtils.isLimitThirtyDay(vodEndDate)) {
                                return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                                //視聴可能
                            } else {
                                return ViewIngType.ENABLE_WATCH;
                            }
                            //「vod_end_date」 <= 現在時刻
                        } else if (vodEndDate <= nowDate) {
                            //視聴不可
                            return ViewIngType.DISABLE_WATCH;
                        }
                    } else {
                        //視聴可否範囲外
                        return ViewIngType.NONE_STATUS;
                    }
                } else {
                    //視聴可否範囲外
                    return ViewIngType.NONE_STATUS;
                }
            case ContentDetailActivity.VIDEO_PROGRAM:
                //"dtv"の値を確認する
                String dTv = metaFullData.getDtv();
                DTVTLogger.debug("dtv: " + dTv);
                if (dTv != null && IS_DTV_FLAG.equals(dTv)) {
                    //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」の場合
                    if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                        //「publish_end_date」が現在日時から1か月以内
                        if (DateUtils.isLimitThirtyDay(publishEndDate)) {
                            //視聴可能期限(「publish_end_dateまで」)を表示
                            return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                        } else {
                            //「publish_end_date」が現在日時から1か月以上先
                            //視聴可能期限は非表示
                            return ViewIngType.ENABLE_WATCH;
                        }
                        //メタレスポンス「publish_start_date」 > 現在時刻 または 現在時刻 >= 「publish_end_date」の場合
                    } else if (publishStartDate > nowDate || nowDate >= publishEndDate) {
                        //視聴不可
                        return ViewIngType.DISABLE_WATCH;
                    }
                    //メタレスポンス「dtv」が「0」か未設定の場合
                } else if (dTv == null || dTv.equals(NOT_DTV_FLAG)) {
                    String bvFlg = metaFullData.getBvflg();
                    //「bvflg」が「1」
                    DTVTLogger.debug("bvflg: " + bvFlg);
                    if (IS_BV_FLAG.equals(bvFlg)) {
                        //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」
                        if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                            //「publish_end_date」が現在日時から1か月以内
                            if (DateUtils.isLimitThirtyDay(publishEndDate)) {
                                //視聴可能期限(「publish_end_dateまで」)を表示
                                return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                                //「publish_end_date」が現在日時から1か月以上先
                            } else {
                                //視聴可能期限は非表示
                                return ViewIngType.ENABLE_WATCH;
                            }
                        }
                    } else {
                        //bvflgが1ではないので視聴不可
                        DTVTLogger.debug("Unviewable(bvflg != 1)");
                        return ViewIngType.DISABLE_WATCH;
                    }
                } else {
                    return ViewIngType.NONE_STATUS;
                }
                //メタレスポンス「disp_type」が「subscription_package」
            case ContentDetailActivity.SUBSCRIPTION_PACKAGE:
                return ViewIngType.SUBSCRIPTION_CHECK_START;
            default:
                return ViewIngType.NONE_STATUS;
        }
    }

    /**
     * 購入済みチャンネルのActiveListから最も未来のvalid_end_dateを取得する.
     *
     * @param response     購入済みチャンネルレスポンス
     * @param channelInfo  チャンネルデータ
     * @return valid_end_date
     */
    public static long getRentalChannelValidEndDate(final PurchasedChannelListResponse response, final ChannelInfo channelInfo) {
        ChannelList channelList = response.getChannelListData();
        HashMap<String, String> chList = checkChServiceIdListSame(channelList.getChannelList(), channelInfo);

        //最長のvalid_end_dateを格納する
        long vodLimitDate = 0;
        //購入済みチャンネル一覧取得IF「metadata_list」のチャンネルメタレスポンス「service_id」を番組に紐づくチャンネルの「service_id」と比較
        //一致した場合
        if (chList != null) {
            //対象チャンネルのpuid、sub_puid、CHPACK-puid、CHPACK-sub_puidと購入済みチャンネル一覧取得
            String puId = channelInfo.getPurchaseId();
            String subPuId = channelInfo.getSubPurchaseId();
            String chPackPuId = channelInfo.getChannelPackPurchaseId();
            String chPackSubPuId = channelInfo.getChannelPackSubPurchaseId();
            ArrayList<ActiveData> activeDataList = response.getChActiveData();
            long nowDate = DateUtils.getNowTimeFormatEpoch();
            //複数のvalidEndDateから期限が最も未来のものを抽出する()
            for (ActiveData activeData : activeDataList) {
                String licenseId = activeData.getLicenseId();
                //IF「active_list」の「license_id」と比較して一致した場合
                if (licenseId.equals(puId) || licenseId.equals(subPuId) || licenseId.equals(chPackPuId) || licenseId.equals(chPackSubPuId)) {
                    //一致した「active_list」の「valid_end_date」> 現在時刻の場合（一件でも条件を満たせば視聴可能）
                    long validEndDate = activeData.getValidEndDate();
                    if (validEndDate > nowDate) {
                        //有効期限が最も未来の日付を取得
                        if (vodLimitDate < validEndDate) {
                            vodLimitDate = validEndDate;
                        }
                    }
                }
            }
        }
        return vodLimitDate;
    }
    /**
     * 購入済みチャンネル視聴可否判定.
     *
     * @param metaFullData Vodメタデータ
     * @param validEndDate  valid_end_date
     * @return 購入済みチャンネル視聴可否ステータス
     */
    @SuppressWarnings("OverlyComplexMethod")
    public static ViewIngType getRentalChannelViewingType(
            final VodMetaFullData metaFullData, final long validEndDate) {

        long nowDate = DateUtils.getNowTimeFormatEpoch();

        //getRentalChannelValidEndDateでvalidEndDateを取得した場合、次の条件に一致しないものは validEndDate = 0 で返却されるため、視聴可否判定にvalidEndDateを使用する
        //購入済みチャンネル一覧取得IF「metadata_list」のチャンネルメタレスポンス「service_id」を番組に紐づくチャンネルの「service_id」と比較
        //対象チャンネルのpuid、sub_puid、CHPACK-puid、sub_puidと購入済みチャンネル一覧取得IF「active_list」の「license_id」と比較
        if (validEndDate > 0) {
            long publishStartDate = metaFullData.getPublish_start_date();
            long publishEndDate = metaFullData.getPublish_end_date();
            //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」
            if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                //視聴可能
                //一致した「active_list」の「valid_end_date」が現在日時から1か月以内（複数の場合は期限が一番長いのを基準にする）
                if (DateUtils.isLimitThirtyDay(validEndDate)) {
                    return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                    //一致した「active_list」の「valid_end_date」が現在日時から1か月以上先
                } else {
                    //視聴可能期限は表示しない
                    return ViewIngType.ENABLE_WATCH;
                }
                //メタレスポンス「publish_start_date」 > 現在時刻 または 現在時刻 >= 「publish_end_date」
            } else {
                //視聴不可(放送時間外のため再生導線を非表示)
                return ViewIngType.DISABLE_WATCH_AND_PLAY;
            }
            //対象チャンネルのpuid、sub_puid、CHPACK-puid、sub_puidと購入済みチャンネル一覧取得IF「active_list」の「license_id」と比較して一致しなかった場合
        } else {
            //すべて不一致の場合は視聴不可(契約導線を表示する)
            DTVTLogger.debug("Unviewable(CH purchased info mismatch)");
            return ViewIngType.DISABLE_CHANNEL_WATCH_AGREEMENT_DISPLAY;
        }
    }

    /**
     * 購入済みCH一覧のservice_idと対象のCHのservice_idが一致するか確認.
     *
     * @param chList 購入済みCHリスト
     * @param channelInfo チャンネルデータ
     * @return true:一致 false:不一致
     */
    private static HashMap<String, String> checkChServiceIdListSame(final List<HashMap<String, String>> chList, final ChannelInfo channelInfo) {

        //CHのservice_id一覧を取得
        for (HashMap<String, String> hashMap : chList) {
            String serviceId = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID);
            if (serviceId != null && !serviceId.isEmpty()) {
                if (serviceId.equals(channelInfo.getServiceId())) {
                    //service_idが一致
                    return hashMap;
                }
            }
        }
        return null;
    }

    /**
     * 購入済みVodデータのActiveListから最も未来のvalid_end_dateを取得する.
     *
     * @param metaFullData Vodメタデータ
     * @param activeList Activeリスト
     * @return valid_end_date
     */
    public static long getRentalVodValidEndDate(
            final VodMetaFullData metaFullData, final ArrayList<ActiveData> activeList) {
        //購入済みVOD取得からの戻り(視聴可否判定)
        String[] liinfArray = metaFullData.getmLiinf_array();
        String puid = metaFullData.getPuid();

        //最長のvalid_end_dateを格納する
        long vodLimitDate = 0;
        //現在Epoch秒
        long nowDate = DateUtils.getNowTimeFormatEpoch();

        for (String liinf : liinfArray) {
            //liinfを"|"区切りで分解する
            String[] column = liinf.split(Pattern.quote("|"), 0);
            for (ActiveData activeData : activeList) {
                String license_id = activeData.getLicenseId();
                //対象VODのpuid、liinf_arrayのライセンスID（パイプ区切り）と購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較して一致した場合
                if (license_id.equals(column[0]) || license_id.equals(puid)) {
                    long validEndDate = activeData.getValidEndDate();
                    //一致した「active_list」の「valid_end_date」> 現在時刻の場合（一件でも条件を満たせば視聴可能）
                    if (activeData.getValidEndDate() > nowDate) {
                        if (vodLimitDate < validEndDate) {
                            vodLimitDate = validEndDate;
                        }
                    }
                }
            }
        }
        return vodLimitDate;
    }

    /**
     * 購入済みVod視聴可否判定.
     *
     * @param metaFullData Vodメタデータ
     * @param validEndDate valid_end_date
     * @return 購入済みVod視聴可否ステータス
     */
    public static ViewIngType getRentalVodViewingType(
            final VodMetaFullData metaFullData, final long validEndDate) {

        //現在Epoch秒
        long nowDate = DateUtils.getNowTimeFormatEpoch();
        long publishStartDate = metaFullData.getPublish_start_date();
        long publishEndDate = metaFullData.getPublish_end_date();

        //getRentalValidEndDateでvalidEndDateを取得した場合、次の条件に一致しないものは validEndDate = 0 で返却されるため、視聴可否判定にvalidEndDateを使用する
        //対象VODのpuid、liinf_arrayのライセンスID（パイプ区切り）と購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較

        //一致した「active_list」の「valid_end_date」> 現在時刻の場合（一件でも条件を満たせば視聴可能）
        if (validEndDate > nowDate) {
            //メタレスポンス「avail_start_date」 <= 現在時刻 < 「avail_end_date」
            if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                //一致した「active_list」の「valid_end_date」が現在日時から1か月以内（複数の場合は期限が一番長いのを基準にする
                if (DateUtils.isLimitThirtyDay(validEndDate)) {
                    //視聴可能期限(「valid_end_dateまで」)を表示
                    return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                    //一致した「active_list」の「valid_end_date」が現在日時から1か月以上先
                } else {
                    //視聴可能期限は非表示
                    return ViewIngType.ENABLE_WATCH;
                }
                //一致した「active_list」の「valid_end_date」<= 現在時刻の場合
            } else {
                //視聴不可(契約導線を表示する)
                return ViewIngType.DISABLE_VOD_WATCH_AGREEMENT_DISPLAY;
            }
            //一致した「active_list」の「valid_end_date」> 現在時刻の場合（一件でも条件を満たせば視聴可能）が不一致
        } else {
            //視聴不可(契約導線を表示する)
            return ViewIngType.DISABLE_VOD_WATCH_AGREEMENT_DISPLAY;
        }
    }
}