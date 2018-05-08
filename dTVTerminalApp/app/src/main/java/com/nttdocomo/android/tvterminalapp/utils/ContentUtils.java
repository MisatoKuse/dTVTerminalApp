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
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.Calendar;
import java.util.Date;

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
                if (!TextUtils.isEmpty(startDate) && DBUtils.isNumber(startDate)) {
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
        boolean isNowOnAir = DateUtils.isNowOnAirDate(
                String.valueOf(metaFullData.getPublish_start_date()), String.valueOf(metaFullData.getPublish_end_date()), true);
        if (dispType == null) {
            return ContentsType.OTHER;
        } else {
            switch (dispType) {
                case ContentDetailActivity.VIDEO_PROGRAM:
                case ContentDetailActivity.VIDEO_SERIES:
                    //dispType=video_program || video_series
                    if (dTvFlag == null) {
                        return ContentsType.OTHER;
                    } else {
                        switch (dTvFlag) {
                            case ContentDetailActivity.DTV_FLAG_ONE:
                                //dTvFlag=1 -> ひかりTV内dTV
                                return ContentsType.HIKARI_TV_VOD;
                            default:
                                //dTvFlag=0 || 未設定 -> ひかりTV_VOD
                                return ContentsType.HIKARI_IN_DTV;
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
                                                return ContentsType.HIKARI_IN_DCH;
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
    public static ContentsType getRecommendContentsType(OtherContentsDetailData detailData) {
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
}
