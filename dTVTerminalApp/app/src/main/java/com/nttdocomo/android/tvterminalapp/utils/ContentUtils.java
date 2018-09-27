/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * コンテンツ共通Utilクラス.
 */
public class ContentUtils {
    /** レコメンドのCategoryId 01. **/
    public static final String RECOMMEND_CATEGORY_ID_ONE = "01";
    /** レコメンドのCategoryId 02. **/
    public static final String RECOMMEND_CATEGORY_ID_TWO = "02";
    /** レコメンドのCategoryId 03. **/
    public static final String RECOMMEND_CATEGORY_ID_THREE = "03";
    /** レコメンドのCategoryId 04. **/
    public static final String RECOMMEND_CATEGORY_ID_FOUR = "04";
    /** レコメンドのCategoryId 05. **/
    public static final String RECOMMEND_CATEGORY_ID_FIVE = "05";
    /** レコメンドのCategoryId 06. **/
    public static final String RECOMMEND_CATEGORY_ID_SIX = "06";
    /** レコメンドのCategoryId 07. **/
    public static final String RECOMMEND_CATEGORY_ID_SEVEN = "07";
    /** レコメンドのCategoryId 08. **/
    public static final String RECOMMEND_CATEGORY_ID_EIGHT = "08";
    /** レコメンドのCategoryId 10. **/
    public static final String RECOMMEND_CATEGORY_ID_TEN = "10";
    /**DTVコンテンツサービスID.*/
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    /**DアニメコンテンツサービスID.*/
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    /**DTVチャンネルコンテンツサービスID.*/
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    /**DTVひかりコンテンツサービスID.*/
    public static final int DTV_HIKARI_CONTENTS_SERVICE_ID = 44;
    /**レコメンド情報キー.*/
    public static final String RECOMMEND_INFO_BUNDLE_KEY = "recommendInfoKey";
    /**検索情報キー.*/
    public static final String SEARCH_INFO_BUNDLE_KEY = "searchInfoKey";
    /**ぷらら情報キー.*/
    public static final String PLALA_INFO_BUNDLE_KEY = "plalaInfoKey";
    /** disp_type(tv_program).*/
    public static final String TV_PROGRAM = "tv_program";
    /** disp_type(video_program).*/
    public static final String VIDEO_PROGRAM = "video_program";
    /** disp_type(video_package).*/
    public static final String VIDEO_PACKAGE = "video_package";
    /** disp_type(video_series).*/
    public static final String VIDEO_SERIES = "video_series";
    /** disp_type(subscription_package).*/
    public static final String SUBSCRIPTION_PACKAGE = "subscription_package";
    /** disp_type(series_svod).*/
    public static final String SERIES_SVOD = "series_svod";
    /** tv_service(0).*/
    public static final String TV_SERVICE_FLAG_HIKARI = "1";
    /** tv_service(1).*/
    public static final String TV_SERVICE_FLAG_DCH_IN_HIKARI = "2";
    /** contents_type(0).*/
    public static final String CONTENT_TYPE_FLAG_ZERO = "0";
    /** contents_type(1).*/
    public static final String CONTENT_TYPE_FLAG_ONE = "1";
    /** contents_type(2).*/
    public static final String CONTENT_TYPE_FLAG_TWO = "2";
    /** contents_type(3).*/
    public static final String CONTENT_TYPE_FLAG_THREE = "3";
    /** dtv(1).*/
    public static final String DTV_FLAG_ONE = "1";
    /** dtv(0).*/
    public static final String DTV_FLAG_ZERO = "0";
    /** チャンネルタイプ kihon_ch.*/
    private static final String CH_TYPE_KIHON = "kihon_ch";
    /** チャンネルタイプ basic_ch.*/
    private static final String CH_TYPE_BASIC = "basic_ch";
    /** チャンネルタイプ trial_free.*/
    private static final String CH_TYPE_TRIAL = "trial_free";
    /** チャンネルタイプ premium_ch.*/
    private static final String CH_TYPE_PREMIUM = "premium_ch";
    /** BVフラグ 1.*/
    private static final String IS_BV_FLAG = "1";
    /** disp_type(WIZARD).*/
    public static final String WIZARD = "wizard";

    /**
     * コンテンツタイプ.
     */
    public enum ContentsType {
        /**テレビ.*/
        TV,
        /**ビデオ.*/
        VOD,
        /**地デジ.*/
        DIGITAL_TERRESTRIAL_BROADCASTING,
        /**BS.*/
        BROADCASTING_SATELLITE,
        /**ひかりTV(番組).*/
        HIKARI_TV,
        /**ひかりTV(番組)放送1時間以内.*/
        HIKARI_TV_WITHIN_TWO_HOUR,
        /**ひかりTV(Now On Air).*/
        HIKARI_TV_NOW_ON_AIR,
        /**ひかり内dTVCh(番組).*/
        HIKARI_IN_DCH_TV,
        /**ひかり内dTVCh(番組)(Now On Air).*/
        HIKARI_IN_DCH_TV_NOW_ON_AIR,
        /**ひかり内dTVCh(番組)放送1時間以内.*/
        HIKARI_IN_DCH_TV_WITHIN_TWO_HOUR,
        /**ひかりTV(VOD).*/
        HIKARI_TV_VOD,
        /**ひかり内dTVCh.(検レコメタで判定)*/
        HIKARI_IN_DCH,
        /**ひかり内dTV.*/
        HIKARI_IN_DTV,
        /**ひかり内dTVCh(見逃し).*/
        HIKARI_IN_DCH_MISS,
        /**ひかり内dTVCh(関連).*/
        HIKARI_IN_DCH_RELATION,
        /**ひかり(録画).*/
        HIKARI_RECORDED,
        /**ひかりTV内dch_見逃し(３２以上).*/
        DCHANNEL_VOD_OVER_31,
        /**ひかりTV内dch_見逃し(３1以内).*/
        DCHANNEL_VOD_31,
        /**レンタル.*/
        RENTAL,
        /**プレミアム.*/
        PREMIUM,
        /**PureDTV.*/
        PURE_DTV,
        /**PureDTVCh.*/
        PURE_DTV_CHANNEL,
        /**PureDTVCh(見逃し).*/
        PURE_DTV_CHANNEL_MISS,
        /**PureDTVCh(関連番組).*/
        PURE_DTV_CHANNEL_RELATION,
        /**dアニメストア.*/
        D_ANIME_STORE,
        /**その他.*/
        OTHER
    }

    /**
     * 視聴可否種別.
     */
    public enum ViewIngType {
        /**視聴可能.*/
        ENABLE_WATCH,
        /**視聴可能.dCHのみ契約*/
        ENABLE_WATCH_001,
        /**視聴可能(期限30日以内なので視聴可能期限表示).*/
        ENABLE_WATCH_LIMIT_THIRTY,
        /**視聴可能(期限30日以内なので視聴可能期限表示).dCHのみ契約*/
        ENABLE_WATCH_LIMIT_THIRTY_001,
        /**視聴可能(期限30日以内なので視聴可能期限表示) ※複数期限があり、期限が一番長いのを基準にする場合.*/
        ENABLE_WATCH_LIMIT_THIRTY_LONGEST,
        /**視聴可能(期限30日超、視聴期限非表示).*/
        ENABLE_WATCH_LIMIT_THIRTY_OVER,
        /**視聴不可(再生導線非表示).*/
        DISABLE_WATCH_AND_PLAY,
        /**視聴不可(再生導線非表示).dCHのみ契約*/
        DISABLE_WATCH_AND_PLAY_001,
        /**視聴不可契約導線表示.*/
        DISABLE_WATCH_AGREEMENT_DISPLAY,
        /**視聴不可契約導線表示(購入CH判定時).*/
        DISABLE_CHANNEL_WATCH_AGREEMENT_DISPLAY,
        /**視聴不可契約導線表示(購入VOD判定時).*/
        DISABLE_VOD_WATCH_AGREEMENT_DISPLAY,
        /**視聴不可.*/
        DISABLE_WATCH,
        /**購入済みCH判定開始.*/
        PREMIUM_CHECK_START,
        /**購入済みVOD判定開始.*/
        SUBSCRIPTION_CHECK_START,
        /**視聴可否判定外ステータス ※サーバレスポンスが正常ならこの状態にはならない想定.*/
        NONE_STATUS
    }

    /**
     * コンテンツ詳細ユーザタイプ.
     */
    public enum ContentsDetailUserType {
        /** ペアリング済み 宅内 ひかりTV契約者(ひかりTV契約者(子)). */
        PAIRING_INSIDE_HIKARI_CONTRACT,
        /** ペアリング済み 宅内 ひかりTV未契約者(dch/dTV/dアニメストア：契約 dch/dTV/dアニメストア：未契約). */
        PAIRING_INSIDE_HIKARI_NO_CONTRACT,
        /** ペアリング済み 宅外 ひかりTV契約者(ひかりTV契約者(子)). */
        PAIRING_OUTSIDE_HIKARI_CONTRACT,
        /** ペアリング済み 宅外 ひかりTV未契約者(dch/dTV/dアニメストア：契約 dch/dTV/dアニメストア：未契約). */
        PAIRING_OUTSIDE_HIKARI_NO_CONTRACT,
        /** 未ペアリング 未ログイン. */
        NO_PAIRING_LOGOUT,
        /** 未ペアリング ひかりTV契約者(ひかりTV契約者(子)). */
        NO_PAIRING_HIKARI_CONTRACT,
        /** 未ペアリング ひかりTV未契約者(dch/dTV/dアニメストア：契約 dch/dTV/dアニメストア：未契約). */
        NO_PAIRING_HIKARI_NO_CONTRACT,
        /** ユーザタイプ判定に必要な情報が取得できていない時に返却するステータス. */
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
            case DTV_CONTENTS_SERVICE_ID:
                if (RECOMMEND_CATEGORY_ID_ONE.equals(categoryId)
                        || RECOMMEND_CATEGORY_ID_TWO.equals(categoryId)) {
                    cType = ContentsType.VOD;
                }
                break;
            //dアニメストア
            case D_ANIMATION_CONTENTS_SERVICE_ID:
                if (RECOMMEND_CATEGORY_ID_ONE.equals(categoryId)) {
                    cType = ContentsType.VOD;
                }
                break;
            //dTVチャンネル
            case DTV_CHANNEL_CONTENTS_SERVICE_ID:
                if (RECOMMEND_CATEGORY_ID_ONE.equals(categoryId)) {
                    cType = ContentsType.TV;
                } else if (RECOMMEND_CATEGORY_ID_TWO.equals(categoryId)
                        || RECOMMEND_CATEGORY_ID_THREE.equals(categoryId)) {
                    cType = ContentsType.VOD;
                }
                break;
            //ひかりTV for docomo
            case DTV_HIKARI_CONTENTS_SERVICE_ID:
                if (RECOMMEND_CATEGORY_ID_THREE.equals(categoryId)
                        || RECOMMEND_CATEGORY_ID_FOUR.equals(categoryId)) {
                    cType = ContentsType.TV;
                } else if (RECOMMEND_CATEGORY_ID_FIVE.equals(categoryId)
                        || RECOMMEND_CATEGORY_ID_SIX.equals(categoryId)
                        || RECOMMEND_CATEGORY_ID_EIGHT.equals(categoryId)
                        || RECOMMEND_CATEGORY_ID_TEN.equals(categoryId)) {
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
     * @param availStartDate 配信開始日時(avail_start_date)
     * @param availEndDate 配信終了日時(avail_end_date)
     * @param vodStartDate VOD配信日時(vod_start_date)
     * @param vodEndDate VOD配信日時(vod_end_date)
     * @param estflg ESTフラグ
     * @param chsvod CHSVOD(chsvod)
     * @return VOD、TV、DCHANNEL_VOD_OVER_31、DCHANNEL_VOD_31、その他
     */
    @SuppressWarnings("OverlyComplexMethod")
    public static ContentsType getContentsTypeByPlala(final String dispType, final String tvService,
                                                           final String contentsType, final long availStartDate, final long availEndDate,
                                                                final long vodStartDate, final long vodEndDate, final String estflg,
                                                                final String chsvod) {
        ContentsType cType = ContentsType.OTHER;
        //VODコンテンツかつ当日日付から配信開始日又は配信終了日が31日以内のものは〇〇から or 〇〇まで表示をする
        //VODとして扱うもの→ひかりTV_VOD、ひかりTV内dTV、レンタル、プレミアムビデオ(2018/7/25現在)
        //※現状は disp_type のみで判定できるため disp_type を使用
        if (VIDEO_PROGRAM.equals(dispType)
                || VIDEO_SERIES.equals(dispType)
                || VIDEO_PACKAGE.equals(dispType)
                || SUBSCRIPTION_PACKAGE.equals(dispType)
                || SERIES_SVOD.equals(dispType)) {
            //ひかりTV_VOD、ひかりTV内dTV
            cType = DateUtils.getContentsTypeByAvailEndDate(availStartDate, availEndDate);
        } else if (TV_PROGRAM.equals(dispType)) {
            if (TV_SERVICE_FLAG_HIKARI.equals(tvService)) {
                //ひかりTV_番組
                cType = ContentsType.TV;
            } else if (TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(tvService)) {
                if (CONTENT_TYPE_FLAG_THREE.equals(contentsType)) {
                    //ひかりTV内dTVチャンネル_関連VOD
                    cType = DateUtils.getContentsTypeByAvailEndDate(availStartDate, availEndDate);
                } else if (CONTENT_TYPE_FLAG_ONE.equals(contentsType)
                        || CONTENT_TYPE_FLAG_TWO.equals(contentsType)) {
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
        final long availStartDate = listContentInfo.getAvailStartDate();
        final long availEndDate = listContentInfo.getAvailEndDate();
        final long vodStartDate = listContentInfo.getVodStartDate();
        final long vodEndDate = listContentInfo.getVodEndDate();

        final ContentsType periodContentsType = getContentsTypeByPlala(
                dispType, tvService, contentsType, availStartDate, availEndDate, vodStartDate, vodEndDate,
                estFlg, chsVod);

        String viewingPeriod = "";
        switch (periodContentsType) {
            case VOD:// VOD
                if (DateUtils.isBefore(availStartDate)) { //から
                    viewingPeriod = DateUtils.getContentsDateString(context, availStartDate, true);
                } else { //まで、見逃し
                    //VOD(m/d（曜日）まで)
                    viewingPeriod = DateUtils.getContentsDetailVodDate(context, availEndDate);
                }
                break;
            case DCHANNEL_VOD_OVER_31: // ひかりTV内dch_見逃し(３２日以上).
            case DCHANNEL_VOD_31: // ひかりTV内dch_見逃し(３1日以内).
                //見逃しには 〇〇まで表示のみ
                //見逃し(m/d（曜日）まで)
                viewingPeriod = DateUtils.getContentsDetailVodDate(context, vodEndDate);
                break;
            case TV:// TV
                //番組(m/d（曜日）h:ii - h:ii)
                long start = 0;
                String startDate = listContentInfo.getPublishStartDate();
                if (!TextUtils.isEmpty(startDate) && DataBaseUtils.isNumber(startDate)) {
                    start = Long.parseLong(listContentInfo.getPublishStartDate());
                }
                viewingPeriod = DateUtils.getContentsDateString(start);
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
                    textView.setText(viewingPeriod);
                    textView.setVisibility(View.VISIBLE);
                }
            case OTHER:
                break;
            default:
                break;
        }
    }

    /**
     * チャンネル名表示を別メソッド化.
     * @param context           コンテキスト
     * @param hyphenTextView 「-」表示ビュー
     * @param channelTextView チャンネル名表示ビュー
     * @param listContentInfo 行データ
     * @param type 機能タイプ
     */
    public static ContentsType setChannelNameOrMissedText(final Context context,
                                                  final TextView hyphenTextView,
                                                  final TextView channelTextView,
                                                  final ContentsData listContentInfo,
                                                  final ContentsAdapter.ActivityTypeItem type) {
        final String dispType = listContentInfo.getDispType();
        final String tvService = listContentInfo.getTvService();
        final String contentsType = listContentInfo.getContentsType();
        final String estFlg = listContentInfo.getEstFlg();
        final String chsVod = listContentInfo.getChsVod();
        final long availStartDate = listContentInfo.getAvailStartDate();
        final long availEndDate = listContentInfo.getAvailEndDate();
        final long vodStartDate = listContentInfo.getVodStartDate();
        final long vodEndDate = listContentInfo.getVodEndDate();

        final ContentsType periodContentsType;
        if (type == ContentsAdapter.ActivityTypeItem.TYPE_RECOMMEND_LIST || type == ContentsAdapter.ActivityTypeItem.TYPE_SEARCH_LIST) {
            periodContentsType = getContentsTypeByRecommend(Integer.parseInt(listContentInfo.getServiceId()), listContentInfo.getCategoryId());
        } else {
            periodContentsType = getContentsTypeByPlala(dispType, tvService, contentsType, availStartDate, availEndDate, vodStartDate, vodEndDate,
                estFlg, chsVod);
        }

        String viewingChannelName = "";
        if (periodContentsType == ContentsType.DCHANNEL_VOD_OVER_31 || periodContentsType == ContentsType.DCHANNEL_VOD_31) {
            //「見逃し」
            viewingChannelName = context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing);

        } else if (periodContentsType == ContentsType.TV) {
            viewingChannelName = listContentInfo.getChannelName();

        }

        // 表示
        if (periodContentsType == ContentsType.TV || periodContentsType == ContentsType.VOD
                || periodContentsType == ContentsType.DCHANNEL_VOD_OVER_31 || periodContentsType == ContentsType.DCHANNEL_VOD_31) {
            if (!TextUtils.isEmpty(viewingChannelName)) {
                hyphenTextView.setVisibility(View.VISIBLE);
                channelTextView.setVisibility(View.VISIBLE);
                SpannableString spannableString = new SpannableString(viewingChannelName);
                int subStart = 0;
                int subEnd = 0;
                if (viewingChannelName.contains(context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing))) {
                    subStart = viewingChannelName.indexOf(context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                    subEnd = spannableString.length();

                    //「見逃し」は黄色文字で表示する
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.contents_detail_video_miss_color)),
                            subStart, subEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                channelTextView.setText(spannableString);
            }
        }
        //録画予約一覧画面にチャンネル名を表示する
        if (type == ContentsAdapter.ActivityTypeItem.TYPE_RECORDING_RESERVATION_LIST && !TextUtils.isEmpty(listContentInfo.getChannelName())) {
            hyphenTextView.setVisibility(View.VISIBLE);
            channelTextView.setVisibility(View.VISIBLE);
            channelTextView.setText(listContentInfo.getChannelName());
        }
        return periodContentsType;
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
                case VIDEO_PROGRAM:
                case VIDEO_SERIES:
                    //dispType=video_program || video_series
                    if (dTvFlag == null) {
                        //dTvFlag=0 || 未設定 -> ひかりTV_VOD
                        return ContentsType.HIKARI_TV_VOD;
                    } else {
                        switch (dTvFlag) {
                            case DTV_FLAG_ONE:
                                //dTvFlag=1 -> ひかりTV内dTV
                                return ContentsType.HIKARI_IN_DTV;
                            default:
                                //dTvFlag=0 || 未設定 -> ひかりTV_VOD
                                return ContentsType.HIKARI_TV_VOD;
                        }
                    }
                case TV_PROGRAM:
                    //dispType=tv_program
                    if (tvService == null) {
                        //tvService=other
                        return ContentsType.OTHER;
                    } else {
                        switch (tvService) {
                            case TV_SERVICE_FLAG_HIKARI:
                                //tv_service=1 -> ひかりTV_番組
                                if (isNowOnAir) {
                                    return ContentsType.HIKARI_TV_NOW_ON_AIR;
                                } else if (DateUtils.isWithInTwoHour(metaFullData.getPublish_start_date())) {
                                    //配信開始が現在時刻の2時間以内のひかりTV
                                    return ContentsType.HIKARI_TV_WITHIN_TWO_HOUR;
                                } else {
                                    return ContentsType.HIKARI_TV;
                                }
                            case TV_SERVICE_FLAG_DCH_IN_HIKARI:
                                //tv_service=2
                                if (contentsType == null) {
                                    //contentsType=other -> ひかりTV_番組
                                    if (isNowOnAir) {
                                        return ContentsType.HIKARI_IN_DCH_TV_NOW_ON_AIR;
                                    } else if (DateUtils.isWithInTwoHour(metaFullData.getPublish_start_date())) {
                                        //配信開始が現在時刻の2時間以内のひかりTV
                                        return ContentsType.HIKARI_IN_DCH_TV_WITHIN_TWO_HOUR;
                                    } else {
                                        return ContentsType.HIKARI_IN_DCH_TV;
                                    }
                                } else {
                                    switch (contentsType) {
                                        case CONTENT_TYPE_FLAG_ONE:
                                        case CONTENT_TYPE_FLAG_TWO:
                                            if (vodStartDate <= current) {
                                                //VOD配信日時(vod_start_date) <= 現在時刻 -> ひかりTV内dTVチャンネル_見逃し
                                                return ContentsType.HIKARI_IN_DCH_MISS;
                                            } else if (DateUtils.isWithInTwoHour(metaFullData.getPublish_start_date())) {
                                                //現在時刻 + 2h > 放送開始日時(metaFullData.getPublish_start_date()) > 現在時刻 -> ひかりTV内dTVチャンネル_番組放送開始2時間以内
                                                return ContentsType.HIKARI_IN_DCH_TV_WITHIN_TWO_HOUR;
                                            } else {
                                                //VOD配信日時(vod_start_date) > 現在時刻 -> ひかりTV内dTVチャンネル_番組
                                                return ContentsType.HIKARI_IN_DCH_TV;
                                            }
                                        case CONTENT_TYPE_FLAG_THREE:
                                            //contentsType=3 -> ひかりTV内dTVチャンネル_関連VOD
                                            return ContentsType.HIKARI_IN_DCH_RELATION;
                                        default:
                                            //contentsType=other -> ひかりTV_番組
                                            if (isNowOnAir) {
                                                return ContentsType.HIKARI_IN_DCH_TV_NOW_ON_AIR;
                                            } else if (DateUtils.isWithInTwoHour(metaFullData.getPublish_start_date())) {
                                                //配信開始が現在時刻の2時間以内のひかりTV
                                                return ContentsType.HIKARI_IN_DCH_TV_WITHIN_TWO_HOUR;
                                            } else {
                                                return ContentsType.HIKARI_IN_DCH_TV;
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
    public static ContentsType getRecommendContentsType(final ContentsData detailData) {
        String strServiceId = detailData.getServiceId();
        int serviceId = -1;
        if (strServiceId != null && DataBaseUtils.isNumber(strServiceId)) {
            serviceId = Integer.parseInt(detailData.getServiceId());
        }
        String categoryId = detailData.getCategoryId();
        switch (serviceId) {
            //serviceId = 44
            case DTV_HIKARI_CONTENTS_SERVICE_ID:
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
            case DTV_CONTENTS_SERVICE_ID:
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
            case DTV_CHANNEL_CONTENTS_SERVICE_ID:
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
            case D_ANIMATION_CONTENTS_SERVICE_ID:
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
    public static ViewIngType getViewingType(final String contractInfo,
                                             final VodMetaFullData metaFullData,
                                             final ChannelInfo channelInfo) {
        if (metaFullData != null) {
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
        if (TV_PROGRAM.equals(metaFullData.getDisp_type())) {
            DTVTLogger.debug("tv_service: " + metaFullData.getmTv_service());
            String tvService = metaFullData.getmTv_service();
            //メタレスポンス「tv_service」が「2」
            if (tvService != null && TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(tvService)) {
                long publishStartDate = metaFullData.getPublish_start_date();
                long publishEndDate = metaFullData.getPublish_end_date();
                long nowDate = DateUtils.getNowTimeFormatEpoch();
                //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」
                if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                    //「publish_end_date」が現在日時から1か月以内
                    if (DateUtils.isLimitThirtyDay(publishEndDate)) {
                        //視聴可能期限(「publish_end_dateまで」)を表示
                        return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY_001;
                    } else {
                        //視聴可能期限は非表示
                        return ViewIngType.ENABLE_WATCH_001;
                    }
                    //メタレスポンス「publish_start_date」 >= 現在時刻
                } else if (publishStartDate >= nowDate) {
                    //視聴不可(放送時間外のため再生導線を非表示)
                    DTVTLogger.debug("Unviewable(Hide playing method because outside broadcasting time)");
                    return ViewIngType.DISABLE_WATCH_AND_PLAY_001;
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
                            return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY_001;
                        } else {
                            return ViewIngType.ENABLE_WATCH_001;
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
            } else if (tvService == null || tvService.equals(TV_SERVICE_FLAG_HIKARI)) {
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

        String dispType = metaFullData.getDisp_type();

        String channelServiceId = null;
        String chType = null;
        //tv_programの場合、チャンネル情報がnullなら判定不可
        if (TV_PROGRAM.equals(dispType) && channelInfo == null) {
            return ViewIngType.NONE_STATUS;
        } else if (channelInfo != null) {
            channelServiceId = channelInfo.getServiceId();
            chType = channelInfo.getChannelType();
        }

        String vodServiceId = metaFullData.getmService_id();
        String tvService = metaFullData.getmTv_service();
        long publishStartDate = metaFullData.getPublish_start_date();
        long publishEndDate = metaFullData.getPublish_end_date();
        long availStartDate = metaFullData.getAvail_start_date();
        long availEndDate = metaFullData.getAvail_end_date();
        long nowDate = DateUtils.getNowTimeFormatEpoch();
        //メタレスポンス「disp_type」が「tv_program」
        switch (dispType) {
            case TV_PROGRAM:
                //メタレスポンス「tv_service」が「1」
                if (TV_SERVICE_FLAG_HIKARI.equals(tvService)) {
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
                } else if (TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(tvService)) {
                    //メタレスポンス「publish_start_date」 <= 現在時刻 < 「publish_end_date」
                    if (publishStartDate <= nowDate && nowDate < publishEndDate) {
                        //視聴可能期限は非表示
                        return ViewIngType.ENABLE_WATCH;
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
            case VIDEO_PROGRAM:
            case VIDEO_SERIES:
                //"dtv"の値を確認する
                String dTv = metaFullData.getDtv();
                DTVTLogger.debug("dtv: " + dTv);
                if (dTv != null && DTV_FLAG_ONE.equals(dTv)) {
                    //メタレスポンス「availStartDate」 <= 現在時刻 < 「availEndDate」の場合
                    if (availStartDate <= nowDate && nowDate < availEndDate) {
                        //「availStartDate」が現在日時から1か月以内
                        if (DateUtils.isLimitThirtyDay(availEndDate)) {
                            //視聴可能期限(「availEndDateまで」)を表示
                            return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                        } else {
                            //「availEndDate」が現在日時から1か月以上先
                            //視聴可能期限は非表示
                            return ViewIngType.ENABLE_WATCH;
                        }
                        //メタレスポンス「availStartDate」 > 現在時刻 または 現在時刻 >= 「availEndDate」の場合
                    } else if (availStartDate > nowDate || nowDate >= availEndDate) {
                        //視聴不可
                        return ViewIngType.DISABLE_WATCH;
                    }
                    //メタレスポンス「dtv」が「0」か未設定の場合
                } else if (dTv == null || dTv.equals(DTV_FLAG_ZERO)) {
                    String bvFlg = metaFullData.getBvflg();
                    //「bvflg」が「1」
                    DTVTLogger.debug("bvflg: " + bvFlg);
                    if (IS_BV_FLAG.equals(bvFlg)) {
                        //メタレスポンス「availStartDate」 <= 現在時刻 < 「availEndDate」
                        if (availStartDate <= nowDate && nowDate < availEndDate) {
                            //「publish_end_date」が現在日時から1か月以内
                            if (DateUtils.isLimitThirtyDay(availEndDate)) {
                                //視聴可能期限(「availEndDateまで」)を表示
                                return ViewIngType.ENABLE_WATCH_LIMIT_THIRTY;
                                //「availEndDate」が現在日時から1か月以上先
                            } else {
                                //視聴可能期限は非表示
                                return ViewIngType.ENABLE_WATCH;
                            }
                        }
                    } else {
                        //bvflgが1ではないので視聴不可
                        DTVTLogger.debug("Unviewable(bvflg != 1)");
                        return ViewIngType.SUBSCRIPTION_CHECK_START;
                    }
                } else {
                    return ViewIngType.NONE_STATUS;
                }
                //メタレスポンス「disp_type」が「subscription_package」
            case SUBSCRIPTION_PACKAGE:
            case SERIES_SVOD:
            case VIDEO_PACKAGE:
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
        Map<String, String> chList = checkChServiceIdListSame(channelList.getChannelList(), channelInfo);

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
    private static Map<String, String> checkChServiceIdListSame(final List<Map<String, String>> chList, final ChannelInfo channelInfo) {

        //CHのservice_id一覧を取得
        for (Map<String, String> hashMap : chList) {
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
                if (column.length > 2 && (license_id.equals(column[0]) || license_id.equals(puid))) {
                    //２カラム目 <= sysdate <= ３カラム目 であれば視聴可能。
                    if (DateUtils.getEpochTime(column[1]) <= nowDate && nowDate <= DateUtils.getEpochTime(column[2])) {
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

    /**
     * 機能：「duration="0:00:42.000"」/「duration="0:00:42"」からmsへ変換.
     *
     * @param durationStr durationStr
     * @return duration in ms
     */
    public static long getDuration(final String durationStr) {
        DTVTLogger.start();
        long ret = 0;
        String[] strs1 = durationStr.split(":");
        if (3 == strs1.length) {
            ret = 60 * 60 * 1000 * Integer.parseInt(strs1[0]) + 60 * 1000 * Integer.parseInt(strs1[1]);
            String ss = strs1[2];
            int i = ss.indexOf('.');
            if (i <= 0) {
                ret += 1000L * Integer.parseInt(ss);
            } else {
                String ss1 = ss.substring(0, i);
                String ss2 = ss.substring(i + 1, ss.length());
                ret += 1000L * Integer.parseInt(ss1);
                try {
                    ret += Integer.parseInt(ss2);
                } catch (Exception e2) {
                    DTVTLogger.debug("TvPlayerActivity::getDuration(), skip ms");
                }
            }
        }
        DTVTLogger.end();
        return ret;
    }

    /**
     * サービスID(dTV関連)に応じたサービス名を返す.
     *
     * @param id サービスID
     * @return サービス名
     */
    public static int getContentsServiceName(final int id) {
        switch (id) {
            case DTV_CONTENTS_SERVICE_ID:
                return R.mipmap.label_service_dtv;
            case DTV_CHANNEL_CONTENTS_SERVICE_ID:
                return R.mipmap.label_service_dch;
            case D_ANIMATION_CONTENTS_SERVICE_ID:
                return R.mipmap.label_service_danime;
        }
        return R.mipmap.label_service_hikari;
    }

    /**
     * サービスID(dTV関連)に応じた他サービスであるかどうかを返す.
     *
     * @param id サービスID
     * @return true：他サービス false：ひかり
     */
    public static boolean isOtherService(final int id) {
        switch (id) {
            case DTV_CONTENTS_SERVICE_ID:
            case DTV_CHANNEL_CONTENTS_SERVICE_ID:
            case D_ANIMATION_CONTENTS_SERVICE_ID:
                return true;
            default:
                return false;
        }
    }

    /**
     * ユーザ状態(ログイン、契約、ペアリング状態の総合)を取得.
     *
     * @param userState        ユーザ状態
     * @param connectionStatus ペアリング状態
     * @param contractState    契約状態
     * @return ユーザ状態(ログイン 契約 ペアリング状態の総合)
     */
    public static ContentsDetailUserType getContentDetailUserType(@NonNull final UserState userState,
                                                                  @NonNull final StbConnectionManager.ConnectionStatus connectionStatus,
                                                                  @NonNull final String contractState) {
        if (connectionStatus.equals(StbConnectionManager.ConnectionStatus.NONE_PAIRING)) {
            if (userState.equals(UserState.LOGIN_NG)) {
                return ContentsDetailUserType.NO_PAIRING_LOGOUT;
            } else {
                switch (contractState) {
                    case UserInfoUtils.CONTRACT_INFO_H4D:
                        return ContentsDetailUserType.NO_PAIRING_HIKARI_CONTRACT;
                    case UserInfoUtils.CONTRACT_INFO_DTV:
                    case UserInfoUtils.CONTRACT_INFO_NONE:
                        return ContentsDetailUserType.NO_PAIRING_HIKARI_NO_CONTRACT;
                    default:
                        return ContentsDetailUserType.NONE_STATUS;
                }
            }
        } else {
            if (connectionStatus.equals(StbConnectionManager.ConnectionStatus.HOME_IN)) {
                switch (contractState) {
                    case UserInfoUtils.CONTRACT_INFO_H4D:
                        return ContentsDetailUserType.PAIRING_INSIDE_HIKARI_CONTRACT;
                    case UserInfoUtils.CONTRACT_INFO_DTV:
                    case UserInfoUtils.CONTRACT_INFO_NONE:
                        return ContentsDetailUserType.PAIRING_INSIDE_HIKARI_NO_CONTRACT;
                    default:
                        return ContentsDetailUserType.NONE_STATUS;
                }
            } else {
                switch (contractState) {
                    case UserInfoUtils.CONTRACT_INFO_H4D:
                        return ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_CONTRACT;
                    case UserInfoUtils.CONTRACT_INFO_DTV:
                    case UserInfoUtils.CONTRACT_INFO_NONE:
                        return ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_NO_CONTRACT;
                    default:
                        return ContentsDetailUserType.NONE_STATUS;
                }
            }
        }
    }

    /**
     * 視聴可否のみを返却する.
     *
     * @param viewIngType 視聴可否種別
     * @return 視聴可否
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public static boolean isEnableDisplay(@NonNull final ViewIngType viewIngType) {
        switch (viewIngType) {
            case ENABLE_WATCH:
            case ENABLE_WATCH_001:
            case ENABLE_WATCH_LIMIT_THIRTY:
            case ENABLE_WATCH_LIMIT_THIRTY_001:
            case ENABLE_WATCH_LIMIT_THIRTY_LONGEST:
            case ENABLE_WATCH_LIMIT_THIRTY_OVER:
            case DISABLE_WATCH_AND_PLAY:
                return true;
            default:
                return false;
        }
    }

    /**
     * 視聴可否のみを返却する.
     *
     * @param contentsType コンテンツ種別
     * @return 視聴可否
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public static boolean isHikariTvProgram(@NonNull final ContentsType contentsType) {
        switch (contentsType) {
            case HIKARI_TV:
            case HIKARI_TV_WITHIN_TWO_HOUR:
            case HIKARI_TV_NOW_ON_AIR:
                return true;
            default:
                return false;
        }
    }

    /**
     * コンテンツ種別から録画ボタン活性表示フラグを取得.
     *
     * @param contentsType コンテンツ種別
     * @return 録画ボタン活性表示フラグ
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public static boolean isRecordButtonDisplay(@NonNull final ContentsType contentsType) {
        switch (contentsType) {
            case HIKARI_TV:
            case HIKARI_IN_DCH_TV:
                return true;
            default:
                return false;
        }
    }

    /**
     * 契約導線表示状態を取得.
     *
     * @param viewIngType 視聴可否種別
     * @return 契約導線表示フラグ
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public static boolean isContractWireDisplay(@NonNull final ViewIngType viewIngType) {
        switch (viewIngType) {
            case DISABLE_WATCH_AGREEMENT_DISPLAY:
            case DISABLE_CHANNEL_WATCH_AGREEMENT_DISPLAY:
            case DISABLE_VOD_WATCH_AGREEMENT_DISPLAY:
                return true;
            default:
                return false;
        }
    }

    /**
     * 表示判定中を示すフラグを取得.
     *
     * @param viewIngType 視聴可否種別
     * @return 表示判定中フラグ
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public static boolean isSkipViewingType(@NonNull final ViewIngType viewIngType, @NonNull final ContentsType contentsType) {
        switch (viewIngType) {
            case PREMIUM_CHECK_START:
            case SUBSCRIPTION_CHECK_START:
            case NONE_STATUS:
                return true;
            default:
                return false;
        }
    }

    /**
     * Pure系コンテンツ判定.
     * @param contentsType コンテンツタイプ
     * @return 判定結果
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public static boolean isPureContents(final ContentsType contentsType) {
        if (contentsType == null) {
            // コンテンツ取得に失敗していれば判定不可
            return false;
        }
        switch (contentsType) {
            case PURE_DTV:
            case PURE_DTV_CHANNEL:
            case PURE_DTV_CHANNEL_MISS:
            case PURE_DTV_CHANNEL_RELATION:
            case D_ANIME_STORE:
                return true;
            default:
                return false;
        }
    }

    /**
     * TVコンテンツ(チャンネル名表示)種別の場合 true.
     *
     * @param contentsType コンテンツ種別
     * @return 判定結果
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public static boolean isChanelNameDisplay(final ContentsType contentsType) {
        if (contentsType == null) {
            // コンテンツ取得に失敗していれば判定不可
            return false;
        }
        switch (contentsType) {
            case TV:
            case HIKARI_TV:
            case HIKARI_TV_WITHIN_TWO_HOUR:
            case HIKARI_TV_NOW_ON_AIR:
            case HIKARI_IN_DCH_TV:
            case HIKARI_IN_DCH_TV_NOW_ON_AIR:
            case HIKARI_IN_DCH_TV_WITHIN_TWO_HOUR:
            case HIKARI_IN_DCH:
            case HIKARI_IN_DCH_MISS:
            case HIKARI_IN_DCH_RELATION:
            case HIKARI_RECORDED:
            case DCHANNEL_VOD_OVER_31:
            case DCHANNEL_VOD_31:
                return true;
            default:
                return false;
        }
    }
}
