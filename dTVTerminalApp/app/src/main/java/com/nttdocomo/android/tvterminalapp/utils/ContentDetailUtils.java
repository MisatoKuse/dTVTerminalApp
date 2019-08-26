/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.CalendarComparator;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.StbMetaInfoResponseData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * コンテンツ詳細Utilクラス.
 */
public class ContentDetailUtils {

    /** STR_FORMAT.*/
    private static final String STR_SPLIT_FORMAT = "\\|";
    /** STR_COMMA.*/
    private static final String STR_COMMA = ",";
    /**DTVパッケージ名.*/
    private static final String DTV_PACKAGE_NAME = "jp.co.nttdocomo.dtv";
    /**DTV schema url 変換.*/
    private static final String DTV_URL_FORMAT_CHANGE = "XXXX";
    /**DTV 新バージョン.*/
    private static final int DTV_VERSION_NEW = 60000;
    /**DTVバージョン.*/
    private static final int DTV_VERSION_STANDARD = 52000;
    /**dアニメストアパッケージ名.*/
    private static final String DANIMESTORE_PACKAGE_NAME = "com.nttdocomo.android.danimeapp";
    /**dアニメストアバージョン.*/
    private static final int DANIMESTORE_VERSION_STANDARD = 132;
    /**dTVチャンネルパッケージ名.*/
    private static final String DTVCHANNEL_PACKAGE_NAME = "com.nttdocomo.dch";
    /**dTVチャンネルバージョン.*/
    private static final int DTVCHANNEL_VERSION_STANDARD = 15;
    /**DAZNパッケージ名.*/
    private static final String DAZN_PACKAGE_NAME = "com.dazn";
    /**DAZNバージョン.*/
    private static final int DAZN_VERSION_STANDARD = 129825;
    /**dTVチャンネルカテゴリー放送.*/
    public static final String DTV_CHANNEL_CATEGORY_BROADCAST = "01";
    /**dTVチャンネルカテゴリー見逃し.*/
    public static final String DTV_CHANNEL_CATEGORY_MISSED = "02";
    /**dTVチャンネルカテゴリー関連.*/
    public static final String DTV_CHANNEL_CATEGORY_RELATION = "03";
    /**レスポンス(1).*/
    private static final String METARESPONSE1 = "1";
    /**レスポンス(2).*/
    private static final String METARESPONSE2 = "2";
    /**レスポンス(3).*/
    private static final String METARESPONSE3 = "3";
    /** 作品IDの長さ.*/
    private static final int CONTENTS_ID_VALID_LENGTH = 8;
    /**予約済みタイプ(4).*/
    private static final String RESERVED4_TYPE4 = "4";
    /**予約済みタイプ(7).*/
    private static final String RESERVED4_TYPE7 = "7";
    /**予約済みタイプ(8).*/
    private static final String RESERVED4_TYPE8 = "8";
    /** 16進数から10進数への変換時の指定値. */
    private static final int SOURCE_HEXADECIMAL = 16;
    /** サービスIDをひかりTV用のチャンネル番号に変換する際の倍率. */
    private static final int CONVERT_SEARVICE_ID_TO_CHANNEL_NUMBER = 10;
    /** リモコンキーを地デジ・BS用のチャンネル番号に変換する際の倍率. */
    private static final int CONVERT_REMOCON_KEY_TO_CHANNEL_NUMBER = 10000;
    /** チャンネルNoを地デジ・BS用のチャンネル番号に変換する際の倍率.*/
    private static final int CONVERT_CHANNEL_NO_TO_CHANNEL_NUMBER = 10;
    /** 値渡すキー. */
    public static final String RECORD_LIST_KEY = "recordListKey";
    /** プレイヤー前回のポジション.*/
    public static final String PLAY_START_POSITION = "playStartPosition";
    /** 前回リモートコントローラービュー表示フラグ.*/
    public static final String REMOTE_CONTROLLER_VIEW_VISIBILITY = "visibility";
    /** 再生停止フラグ.*/
    public static final String IS_PLAYER_PAUSED = "isPlayerPaused";
    /** 前回ViewPagerのタブ位置.*/
    public static final String VIEWPAGER_INDEX = "viewPagerIndex";
    /**コンテンツ詳細予約済みID.*/
    public static final String CONTENTS_DETAIL_RESERVEDID = "1";
    /** bvflg(1).*/
    public static final String BVFLG_FLAG_ONE = "1";
    /** bvflg(0).*/
    public static final String BVFLG_FLAG_ZERO = "0";
    /** アスペクト比(16:9)の16.*/
    public static final int SCREEN_RATIO_WIDTH_16 = 16;
    /** アスペクト比(16:9)の9.*/
    public static final int SCREEN_RATIO_HEIGHT_9 = 9;
    /** ひかり放送中光コンテンツ再生失敗時にリトライを行うエラーコードの開始値.*/
    public static final int RETRY_ERROR_START = 2000;
    /**モバイル視聴不可.*/
    public static final String MOBILEVIEWINGFLG_FLAG_ZERO = "0";
    /** 番組詳細 or 作品情報タブ.*/
    public static final int CONTENTS_DETAIL_INFO_TAB_POSITION = 0;
    /** チャンネルタブ.*/
    public static final int CONTENTS_DETAIL_CHANNEL_EPISODE_TAB_POSITION = 1;
    /**他サービス起動リクエストコード.*/
    public static final int START_APPLICATION_REQUEST_CODE = 0;
    /** 画面すべてのクリップボタンを更新.*/
    public static final int CLIP_BUTTON_ALL_UPDATE = 0;
    /** チャンネルリストのクリップボタンをのみを更新.*/
    public static final int CLIP_BUTTON_CHANNEL_UPDATE = 1;
    /**コンテンツ詳細のみ.*/
    public final static int CONTENTS_DETAIL_ONLY = 0;
    /**プレイヤーのみ.*/
    public final static int PLAYER_ONLY = 1;
    /**プレイヤーとコンテンツ詳細.*/
    public final static int PLAYER_AND_CONTENTS_DETAIL = 2;
    /** サムネイルにかけるシャドウのアルファ値.*/
    public static final float THUMBNAIL_SHADOW_ALPHA = 0.7f;
    /** 多チャンネル放送.*/
    private static final int PLATFORM_TYPE_H4D = 1;
    /** 地デジ.*/
    private static final int PLATFORM_TYPE_TTB = 2;
    /** BS.*/
    private static final int PLATFORM_TYPE_BS = 3;
    /** エピソードタイトル.*/
    public static final String EPISODE_TITLE = "episode_title";
    /** エピソード情報.*/
    public static final String EPISODE_MESSAGE = "episode_message";
    /** 他サービス.*/
    public static final String EPISODE_OTHERSERVICE = "other_service";

    /** エラータイプ.*/
    public enum ErrorType {
        /** コンテンツ詳細取得.*/
        contentDetailGet,
        /** スタッフリスト取得.*/
        roleListGet,
        /** レンタルチャンネル取得.*/
        rentalChannelListGet,
        /** レンタルVod取得.*/
        rentalVoidListGet,
        /** チャンネルリスト取得.*/
        channelListGet,
        /** 番組データ取得.*/
        tvScheduleListGet,
        /** あらすじ取得.*/
        recommendDetailGet
    }

    /**
     * タブ表示出しわけ.
     */
    public enum TabType {
        /**作品情報のみ.*/
        VOD,
        /**番組詳細のみ.*/
        TV_ONLY,
        /**番組詳細＆チャンネル.*/
        TV_CH,
        /**作品情報＆エピソード.*/
        VOD_EPISODE
    }

    /**
     * アプリ種別(Pure系).
     */
    public enum StartAppServiceType {
        /**h4d内DTV.*/
        H4D_DTV,
        /**h4d内dTVチャンネル.*/
        H4D_DTV_CH,
        /**DTV.*/
        DTV,
        /**dTVチャンネル.*/
        DTV_CH,
        /**DAZN.*/
        DAZN,
        /**dアニメ.*/
        DANIME
    }

    /** コンテンツタイプ(Google Analytics用).*/
    public enum ContentTypeForGoogleAnalytics {
        /** テレビ.*/
        TV,
        /** ビデオ.*/
        VOD,
        /** その他.*/
        OTHER
    }

    /**
     * ひかりTVのNowOnAir用のチャンネル情報を算出する.
     * @param channelInfo チャンネルメタ情報
     * @param tvService  TVサービス
     * @return 変換後チャンネル番号
     */
    public static int convertChannelNumber(final ChannelInfo channelInfo, final String tvService) {
        int serviceIdDecimal = 0;

        switch (tvService) {
            case ContentUtils.TV_SERVICE_FLAG_HIKARI: // 多チャンネル
                // サービスIDを取得
                String serviceId = channelInfo.getServiceId();
                // チャンネル番号を「service_id」から算出
                try {
                    //サービスIDを16進数に変換する
                    serviceIdDecimal = Integer.parseInt(serviceId, SOURCE_HEXADECIMAL);
                } catch (NumberFormatException exception) {
                    // フェールセーフ用
                    DTVTLogger.debug(exception);
                }
                serviceIdDecimal *= CONVERT_SEARVICE_ID_TO_CHANNEL_NUMBER;
                break;
            case ContentUtils.TV_SERVICE_FLAG_TTB: //　地デジ
            case ContentUtils.TV_SERVICE_FLAG_BS: //　BS
                // remoconKeyを使ってチャンネル番号を計算
                if (channelInfo.getRemoconKey() != null) {
                    try {
                        int remoconKey = Integer.parseInt(channelInfo.getRemoconKey());
                        serviceIdDecimal = remoconKey * CONVERT_REMOCON_KEY_TO_CHANNEL_NUMBER
                                + channelInfo.getChannelNo() * CONVERT_CHANNEL_NO_TO_CHANNEL_NUMBER;
                    } catch (NumberFormatException exception) {
                        // フェールセーフ用
                        DTVTLogger.debug(exception);
                    }
                } else {
                    // remoconKeyが無い場合
                    // チャンネル番号を「chno」から算出
                    serviceIdDecimal = channelInfo.getChannelNo() * CONVERT_CHANNEL_NO_TO_CHANNEL_NUMBER;
                }
                break;
            default: // dTVチャンネル
                break;
        }

        return serviceIdDecimal;
    }

    /**
     * STB接続状態を取得.
     * @return STB接続状態
     */
    public static boolean getStbStatus() {
        return StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN;
    }

    /**
     * 録画再生カスタムディメンション取得.
     * @param context コンテキスト
     * @param title タイトル名
     * @return スクリーン名
     */
    public static SparseArray<String> getRecordPlayerCustomDimensions(final Context context, final String title) {
        String contentsType2 = context.getString(R.string.google_analytics_custom_dimension_contents_type2_record);
        String serviceName = context.getString(R.string.google_analytics_custom_dimension_service_h4d);
        String contentsType1 = context.getString(R.string.google_analytics_custom_dimension_contents_type1_h4d);
        return ContentUtils.getCustomDimensions(null, serviceName, contentsType1, contentsType2, title);
    }

    /**
     * スクリーン名マップを取得する.
     * @param contentType コンテンツタイプ
     * @param context コンテキスト
     * @param mIsH4dPlayer 放送視聴可否
     * @return スクリーン名マップ
     */
    public static HashMap<String, String> getScreenNameMap(final ContentTypeForGoogleAnalytics contentType, final Context context, final boolean mIsH4dPlayer) {
        HashMap<String, String> screenNameMap = new HashMap<>();
        screenNameMap.put(context.getString(R.string.contents_detail_tab_contents_info),
                context.getString(R.string.google_analytics_screen_name_content_detail_h4d_vod_program_detail));
        if (contentType == ContentTypeForGoogleAnalytics.VOD) {
            screenNameMap.put(context.getString(R.string.contents_detail_tab_program_detail), context.getString(
                    R.string.google_analytics_screen_name_content_detail_h4d_vod_program_detail));
        } else {
            if (mIsH4dPlayer) {
                screenNameMap.put(context.getString(R.string.contents_detail_tab_program_detail), context.getString(
                        R.string.google_analytics_screen_name_player_h4d_broadcast_program_detail));
            } else {
                screenNameMap.put(context.getString(R.string.contents_detail_tab_program_detail), context.getString(
                        R.string.google_analytics_screen_name_content_detail_h4d_broadcast_program_detail));
            }
        }
        if (mIsH4dPlayer) {
            screenNameMap.put(context.getString(R.string.contents_detail_tab_channel), context.getString(
                    R.string.google_analytics_screen_name_player_h4d_broadcast_channel));
        } else {
            screenNameMap.put(context.getString(R.string.contents_detail_tab_channel), context.getString(
                    R.string.google_analytics_screen_name_content_detail_h4d_broadcast_channel));
        }
        screenNameMap.put(context.getString(R.string.contents_detail_tab_episode),
                context.getString(R.string.google_analytics_screen_name_content_detail_h4d_vod_episode));
        return screenNameMap;
    }

    /**
     * ロールリスト取得.
     * @param credit_array スタッフ情報
     * @param roleListInfo ロールリスト情報
     * @return ロールリスト key:カテゴリ名 value:該当カテゴリのスタッフ情報
     */
    public static Map<String, String> getRoleList(final String[] credit_array, final ArrayList<RoleListMetaData> roleListInfo) {
        Map<String, String> arrayMap = new LinkedHashMap<>();
        for (String aCredit_array : credit_array) {
            String[] creditInfo = aCredit_array.split(STR_SPLIT_FORMAT);
            if (creditInfo.length == 4) {
                String creditId = creditInfo[2];
                String creditName = creditInfo[3];
                if (!TextUtils.isEmpty(creditId)) {
                    for (int j = 0; j < roleListInfo.size(); j++) {
                        RoleListMetaData roleListMetaData = roleListInfo.get(j);
                        if (creditId.equals(roleListMetaData.getId())) {
                            String categoryName = roleListMetaData.getName();
                            if (TextUtils.isEmpty(arrayMap.get(categoryName))) {
                                arrayMap.put(categoryName, creditName);
                            } else {
                                String name = arrayMap.get(categoryName);
                                arrayMap.put(categoryName, name + STR_COMMA + creditName);
                            }
                            break;
                        }
                    }
                }
            }
        }
        return arrayMap;
    }

    /**
     * スタッフ情報LayoutParams取得.
     * @param context context
     * @return LayoutParams
     */
    public static LinearLayout.LayoutParams createStaffTextViewParams(final Context context) {
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contentParams.setMargins(0, (int) context.getResources().getDimension(R.dimen.contents_detail_staff_margin_top), 0, 0);
        return contentParams;
    }

    /**
     * スタッフ情報TextView作成.
     * @param context context
     * @return textView
     */
    public static TextView createStaffTextView(final Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(ContextCompat.getColor(context, R.color.contents_detail_schedule_detail_sub_title));
        textView.setLineSpacing(context.getResources().getDimension(R.dimen.contents_detail_content_line_space), 1);
        return textView;
    }

    /**
     * チャンネル情報設定.
     * @param contentsData contentsData
     * @param scheduleInfo scheduleInfo
     */
    public static void setContentsData(final ContentsData contentsData, final ScheduleInfo scheduleInfo) {
        contentsData.setTitle(scheduleInfo.getTitle());
        contentsData.setContentsId(scheduleInfo.getCrId());
        contentsData.setRequestData(scheduleInfo.getClipRequestData());
        contentsData.setThumURL(scheduleInfo.getImageUrl());
        contentsData.setTime(DateUtils.getContentsDetailChannelHmm(scheduleInfo.getStartTime()));
        contentsData.setClipExec(scheduleInfo.isClipExec());
        contentsData.setDispType(scheduleInfo.getDispType());
        contentsData.setDtv(scheduleInfo.getDtv());
        contentsData.setTvService(scheduleInfo.getTvService());
        contentsData.setServiceId(scheduleInfo.getServiceId());
        contentsData.setEventId(scheduleInfo.getEventId());
        contentsData.setCrid(scheduleInfo.getCrId());
        contentsData.setTitleId(scheduleInfo.getTitleId());
    }

    /**
     * チャンネル情報設定.
     * @param mDetailFullData メタデータ
     * @param detailData 詳細データ
     * @param clipStatus クリープステータス
     */
    public static void setContentsDetailData(final VodMetaFullData mDetailFullData, final OtherContentsDetailData detailData, final boolean clipStatus) {
        String dispType = mDetailFullData.getDisp_type();
        String searchOk = mDetailFullData.getmSearch_ok();
        String dTv = mDetailFullData.getDtv();
        String dTvType = mDetailFullData.getDtvType();
        detailData.setTitle(mDetailFullData.getTitle());
        detailData.setVodMetaFullData(mDetailFullData);
        detailData.setDetail(mDetailFullData.getSynop());
        // コンテンツ状態を反映
        detailData.setClipStatus(clipStatus);
        detailData.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dTv, dTvType));
        detailData.setDispType(dispType);
        detailData.setSearchOk(searchOk);
        detailData.setDtv(dTv);
        detailData.setDtvType(dTvType);
        detailData.setCrId(mDetailFullData.getCrid());
        detailData.setEventId(mDetailFullData.getmEvent_id());
        detailData.setTitleId(mDetailFullData.getTitle_id());
        detailData.setRvalue(mDetailFullData.getR_value());
        detailData.setRating(mDetailFullData.getRating());
        detailData.setCopy(mDetailFullData.getmCopy());
        detailData.setM4kflg(mDetailFullData.getM4kflg());
        detailData.setAdinfoArray(mDetailFullData.getmAdinfo_array());
        detailData.setContentCategory(mDetailFullData.getContentsType());
        detailData.setCopyRight(mDetailFullData.getCopyright());
    }

    /**
     * コンテンツ詳細情報設定.
     * @param content メタデータ
     * @param detailData 詳細データ
     * @param mDetailData 詳細データ(遷移元からもらった)
     */
    public static void setContentsDetailData(final StbMetaInfoResponseData.Content content, final OtherContentsDetailData detailData,
            final OtherContentsDetailData mDetailData) {
        if (DataBaseUtils.isNumber(content.mServiceId)) {
            detailData.setServiceId(Integer.parseInt(content.mServiceId));
        }
        detailData.setTitleKind(content.mTitleKind);
        detailData.setDescription1(content.mDescription1);
        detailData.setDescription2(content.mDescription2);
        detailData.setDescription3(content.mDescription3);
        String detail = "";
        if (!TextUtils.isEmpty(content.mDescription2)) {
            detail = content.mDescription2;
        } else if (!TextUtils.isEmpty(content.mDescription1)) {
            detail = content.mDescription1;
        } else if (!TextUtils.isEmpty(content.mDescription3)) {
            detail = content.mDescription3;
        }
        detailData.setDetail(detail);
        detailData.setReserved1(content.mReserved1);
        detailData.setReserved2(content.mReserved2);
        detailData.setReserved3(content.mReserved3);
        detailData.setReserved4(content.mReserved4);
        detailData.setReserved5(content.mReserved5);
        detailData.setMobileViewingFlg(content.mMobileViewingFlg);
        detailData.setCategoryId(content.mCategoryId);
        detailData.setTitle(content.mTitle);
        detailData.setThumb(content.mCtPicURL1);
        detailData.setmStartDate(content.mStartViewing);
        detailData.setmEndDate(content.mEndViewing);
        detailData.setChannelId(content.mChannelId);
        detailData.setContentsId(content.mContentsId);
        detailData.setContentCategory(ContentUtils.getRecommendContentsType(detailData.getServiceId(), content.mCategoryId));
        detailData.setCopyRight(content.mCopyright);
        detailData.setTotalEpisodeCount(content.mTotalEpisodeCount);
        //履歴送信用
        detailData.setRecommendFlg(mDetailData.getRecommendFlg());
        detailData.setGroupId(mDetailData.getGroupId());
        detailData.setPageId(mDetailData.getPageId());
        detailData.setRecommendOrder(mDetailData.getRecommendOrder());
        detailData.setRecommendMethodId(mDetailData.getRecommendMethodId());
    }

    /**
     * チャンネルタブの番組表日付を取得.
     * @param channelDate channelDate
     * @param context context
     * @return 日付
     */
    public static String getDateForChannel(final String channelDate, final Context context) {
        String subTitle = null;
        if (channelDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
            try {
                Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                calendar.setTime(sdf.parse(channelDate));
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                subTitle = (month + 1) + context.getString(R.string.home_contents_slash) + day
                        + context.getString(R.string.home_contents_front_bracket)
                        + DateUtils.STRING_DAY_OF_WEEK[week]
                        + context.getString(R.string.home_contents_back_bracket);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
        }
        return subTitle;
    }

    /**
     * ソートを行う.
     * @param channels チャンネル
     */
    public static void sort(final List<ChannelInfo> channels) {
        for (ChannelInfo channel : channels) {
            Collections.sort(channel.getSchedules(), new CalendarComparator());
        }
    }

    /**
     * 機能：バージョンコード情報を取得する.
     * @param packageName パッケージ名
     * @param context コンテキスト
     * @return バージョンコード
     */
    @TargetApi(Build.VERSION_CODES.P)
    @SuppressWarnings("deprecation")
    private static long getVersionCode(final String packageName, final Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return packageInfo.getLongVersionCode();
            }
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            DTVTLogger.debug(e);
        }
        return -1;
    }

    /**
     * 機能：アプリは端末にインストールするかどうかの判断.
     *
     * @param context コンテキスト
     * @param packageName アプリのパッケージ名
     * @return アプリがインストールされているか
     */
    public static boolean isAppInstalled(final Context context, final String packageName) {
        PackageManager packageManager;
        List<PackageInfo> pinfo;
        try {
            packageManager = context.getPackageManager();
            pinfo = packageManager.getInstalledPackages(0);
        } catch (RuntimeException exception) {
            //Androidのバグと思われる原因により、稀に本例外が発生する。情報が取得できないので、アプリ有りの扱いとする
            //本メソッドは現状DTV等他のアプリの起動時に使用する。アプリが本当に存在しなければ起動に失敗し、ダウンロードを促すダイアログを表示する
            return true;
        }
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 機能：dTV アプリバージョン判定.
     * @param versionCode バージョンコード
     * @return true:新バージョン false:旧バージョン
     */
    private static boolean dTVAppIsNewVersion(final long versionCode) {
        return versionCode >= DTV_VERSION_NEW;
    }

    /**
     * 機能：dTV APP起動（検レコサーバ）.
     * @param detailData 検レコサーバメタデータ
     * @param context コンテキスト
     * @return 起動URL
     */
    public static String getStartDtvAppUrl(final OtherContentsDetailData detailData, final Context context) {
        String packageName = getStartAppPackageName(StartAppServiceType.DTV);
        long localVersionCode = getVersionCode(packageName, context);
        // contentsId が16桁の場合に下8桁を使用する。※前提条件:contentsId は8桁または16桁である
        String contentsId = detailData.getContentsId().substring(detailData.getContentsId().length() - CONTENTS_ID_VALID_LENGTH);
        DTVTLogger.debug("Reserved4[" + detailData.getReserved4() + "] contentsId:" + detailData.getContentsId() + " lower 8 digits:" + contentsId);
        if (dTVAppIsNewVersion(localVersionCode)) {
            return getStartDtvAppUrlByRecommendNewVersion(detailData.getReserved4(), contentsId, detailData.getTotalEpisodeCount());
        } else {
            return getStartDtvAppUrlByRecommendOldVersion(detailData.getReserved4(), contentsId);
        }
    }

    /**
     * 機能：dTV APP起動（検レコサーバ）（旧dTVバージョン：60000未満）.
     * @param reserved4 reserved4
     * @param contentsId コンテンツId
     * @return 起動URL
     */
    private static String getStartDtvAppUrlByRecommendOldVersion(final String reserved4, final String contentsId) {
        String startUrl = "";
        try {
            //タイトルタイプの別
            //4:音楽コンテンツ
            if (RESERVED4_TYPE4.equals(reserved4)) {
                startUrl = UrlConstants.WebUrl.WORK_START_TYPE + contentsId;
                //7,8:ライブ配信コンテンツ
            } else if (RESERVED4_TYPE7.equals(reserved4)
                    || RESERVED4_TYPE8.equals(reserved4)) {
                startUrl = UrlConstants.WebUrl.SUPER_SPEED_START_TYPE + contentsId;
                //その他の場合
            } else {
                startUrl = UrlConstants.WebUrl.TITTLE_START_TYPE + contentsId;
            }
        } catch (StringIndexOutOfBoundsException e) {
            DTVTLogger.debug(e);
        }
        return startUrl;
    }

    /**
     * 機能：dTV APP起動（検レコサーバ）（新dTVバージョン：60000以上）.
     * @param reserved4 reserved4
     * @param contentsId コンテンツId
     * @param totalEpisodeCount エピソード総数
     * @return 起動URL
     */
    private static String getStartDtvAppUrlByRecommendNewVersion(final String reserved4, final String contentsId, final int totalEpisodeCount) {
        String startUrl = "";
        try {
            //タイトルタイプの別
            //4:音楽コンテンツ
            if (RESERVED4_TYPE4.equals(reserved4)) {
                startUrl = UrlConstants.WebUrl.NEW_PRODUCT_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, contentsId);
                //8:ライブ配信コンテンツ
            } else if (RESERVED4_TYPE8.equals(reserved4)) {
                startUrl = UrlConstants.WebUrl.NEW_TITLE_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, contentsId);
                //その他の場合
            } else {
                // "totalEpisodeCount"が1の場合は「単話」、それ以外は「シリーズ」
                if (totalEpisodeCount == 1) {
                    startUrl = UrlConstants.WebUrl.NEW_TITLE_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, contentsId);
                } else {
                    startUrl = UrlConstants.WebUrl.NEW_TITLE_VIEW_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, contentsId);
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            DTVTLogger.debug(e);
        }
        return startUrl;
    }

    /**
     * 機能：DAZN APP起動（検レコサーバ）.
     * @param detailData 検レコサーバメタデータ
     * @return 起動URL
     */
    public static String getStartDAZNAppUrl(final OtherContentsDetailData detailData) {
        return UrlConstants.WebUrl.DAZN_START_URL + detailData.getContentsId();
    }

    /**
     * 機能：dアニメAPP起動（検レコサーバ）.
     * @param detailData 検レコサーバメタデータ
     * @return 起動URL
     */
    public static String getStartDAnimeAppUrl(final OtherContentsDetailData detailData) {
        String startUrl = "";
        if (detailData != null) {
            if (detailData.getTitleKind() != null && detailData.getTitleKind().equals(SearchDataProvider.D_ANIME_STORE_SONG_CONTENTS)) {
                //音楽：1
                startUrl = UrlConstants.WebUrl.D_ANIME_SONG_STORE_START_URL + detailData.getContentsId();
            } else {
                //映像：0 ※協議の結果 1 以外は映像コンテンツとして扱う
                startUrl = UrlConstants.WebUrl.DANIMESTORE_START_URL + detailData.getContentsId();
            }
        }
        return startUrl;
    }

    /**
     * 機能：dTVチャンネルAPP起動（検レコサーバ）.
     * @param detailData 検レコサーバメタデータ
     * @return 起動URL
     */
    public static String getStartDtvChannelAppUrl(final OtherContentsDetailData detailData) {
        String startUrl = "";
        //テレビ再生  「categoryId」が「01」の場合
        if (DTV_CHANNEL_CATEGORY_BROADCAST.equals(detailData.getCategoryId())) {
            startUrl = UrlConstants.WebUrl.DTVCHANNEL_TELEVISION_START_URL + detailData.getChannelId();
            DTVTLogger.debug("channelId :----" + detailData.getChannelId());
            //ビデオ再生  「categoryId」が「02」または「03」の場合
        } else if (DTV_CHANNEL_CATEGORY_MISSED.equals(detailData.getCategoryId())
                || DTV_CHANNEL_CATEGORY_RELATION.equals(detailData.getCategoryId())) {
            startUrl = UrlConstants.WebUrl.DTVCHANNEL_VIDEO_START_URL + detailData.getContentsId();
            DTVTLogger.debug("ContentId :----" + detailData.getContentsId());
        }
        return startUrl;
    }

    /**
     * 機能：dTVAPP起動（エピソードシリーズ専用）（ぷららサーバ）.
     * @param titleId タイトルID
     * @param episodeId エピソードID
     * @param context コンテキスト
     * @return 起動URL
     */
    public static String getStartDtvEpisodeAppUrl(final String titleId, final String episodeId, final Context context) {
        String packageName = getStartAppPackageName(StartAppServiceType.DTV);
        long localVersionCode = getVersionCode(packageName, context);
        DTVTLogger.debug("title_id:" + titleId + " episode_id:" + episodeId);
        if (dTVAppIsNewVersion(localVersionCode)) {
            return getStartDtvEpisodeUrlByPlalaNewVersion(episodeId);
        } else {
            return getStartDtvEpisodeUrlByPlalaOldVersion(titleId, episodeId);
        }
    }

    /**
     * 機能：dTV APP起動（エピソードシリーズ専用）（ぷららサーバ）（新dTVバージョン：60000以上）.
     * @param episodeId エピソードID
     * @return 起動URL
     */
    private static String getStartDtvEpisodeUrlByPlalaNewVersion(final String episodeId) {
        return UrlConstants.WebUrl.NEW_PRODUCT_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, episodeId);
    }

    /**
     * 機能：dTV APP起動（エピソードシリーズ専用）（旧dTVバージョン：60000未満）.
     * @param titleId タイトルID
     * @param episodeId エピソードID
     * @return 起動URL
     */
    private static String getStartDtvEpisodeUrlByPlalaOldVersion(final String titleId, final String episodeId) {
        return String.format(UrlConstants.WebUrl.TITTLE_EPISODE_START_TYPE, titleId, episodeId);
    }

    /**
     * 機能：dTVAPP起動（ぷららサーバ）.
     * @param detailData ぷららサーバメタデータ
     * @param context コンテキスト
     * @return 起動URL
     */
    public static String getStartDtvAppUrl(final VodMetaFullData detailData, final Context context) {
        String packageName = getStartAppPackageName(StartAppServiceType.DTV);
        long localVersionCode = getVersionCode(packageName, context);
        DTVTLogger.debug("dtv_type[" + detailData.getDtvType() + "] title_id:" + detailData.getTitle_id() + " episode_id:" + detailData.getEpisode_id());
        if (dTVAppIsNewVersion(localVersionCode)) {
            return getStartDtvAppUrlByPlalaNewVersion(detailData);
        } else {
            return getStartDtvAppUrlByPlalaOldVersion(detailData);
        }
    }

    /**
     * 機能：dTV APP起動（検レコサーバ）（旧dTVバージョン：60000未満）.
     * @param detailData ぷららサーバメタデータ
     * @return 起動URL
     */
    private static String getStartDtvAppUrlByPlalaOldVersion(final VodMetaFullData detailData) {
        String startUrl;
        if (METARESPONSE1.equals(detailData.getDtvType())) {
            startUrl = UrlConstants.WebUrl.WORK_START_TYPE + detailData.getEpisode_id();
            DTVTLogger.debug("Start title with the specified episode_id:" + detailData.getEpisode_id() + " URLScheme:" + startUrl);
        } else if (METARESPONSE2.equals(detailData.getDtvType())) {
            startUrl = UrlConstants.WebUrl.SUPER_SPEED_START_TYPE + detailData.getTitle_id();
            DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " URLScheme:" + startUrl);
        } else if (METARESPONSE3.equals(detailData.getDtvType())) {
            String episodeId = detailData.getEpisode_id();
            if (TextUtils.isEmpty(episodeId)) {
                startUrl = UrlConstants.WebUrl.TITTLE_START_TYPE + detailData.getTitle_id();
                DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " URLScheme:" + startUrl);
            } else {
                // ※作品IDが設定されていた場合は、タイトル詳細を作品ID指定で起動させる
                startUrl = String.format(UrlConstants.WebUrl.TITTLE_EPISODE_START_TYPE, detailData.getTitle_id(), episodeId);
                DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " episode_id:" + episodeId + " URLScheme:" + startUrl);
            }
        } else {
            startUrl = UrlConstants.WebUrl.TITTLE_START_TYPE + detailData.getTitle_id();
            DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " URLScheme:" + startUrl);
        }
        return startUrl;
    }

    /**
     * 機能：dTV APP起動（検レコサーバ）（新dTVバージョン：60000以上）.
     * @param detailData ぷららサーバメタデータ
     * @return 起動URL
     */
    private static String getStartDtvAppUrlByPlalaNewVersion(final VodMetaFullData detailData) {
        String startUrl;
        if (METARESPONSE1.equals(detailData.getDtvType())) {
            startUrl = UrlConstants.WebUrl.NEW_PRODUCT_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, detailData.getEpisode_id());
            DTVTLogger.debug("Start title with the specified episode_id:" + detailData.getEpisode_id() + " URLScheme:" + startUrl);
        } else if (METARESPONSE2.equals(detailData.getDtvType())) {
            startUrl = UrlConstants.WebUrl.NEW_TITLE_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, detailData.getTitle_id());
            DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " URLScheme:" + startUrl);
        } else if (METARESPONSE3.equals(detailData.getDtvType())) {
            startUrl = UrlConstants.WebUrl.NEW_TITLE_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, detailData.getTitle_id());
            DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " URLScheme:" + startUrl);
        } else {
            if (ContentUtils.VIDEO_PROGRAM.equals(detailData.getDisp_type())) {
                startUrl = UrlConstants.WebUrl.NEW_TITLE_PLAY_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, detailData.getTitle_id());
            } else {
                startUrl = UrlConstants.WebUrl.NEW_TITLE_VIEW_START_TYPE.replace(DTV_URL_FORMAT_CHANGE, detailData.getTitle_id());
            }
            DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " URLScheme:" + startUrl);
        }
        return startUrl;
    }

    /**
     * 機能：dTVチャンネルAPP起動（ぷららサーバ）.
     * @param detailData ぷららサーバメタデータ
     * @return 起動URL
     */
    public static String getStartDtvChannelAppUrl(final VodMetaFullData detailData) {
        //ひかりTV内dtvチャンネルの場合
        String startUrl = "";
        if (ContentUtils.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(detailData.getmTv_service())) {
            boolean isVodContent = true;
            String contentType = detailData.getmContent_type();
            //「contents_type」が未設定:番組
            if (contentType == null) {
                isVodContent = false;
            } else {
                switch (contentType) {
                    case ContentUtils.CONTENT_TYPE_FLAG_ONE: //「contents_type」が「1」:見逃しVOD or 番組
                    case ContentUtils.CONTENT_TYPE_FLAG_TWO: //「contents_type」が「2」:見逃しVOD or 番組
                        long vodStartDate = detailData.getmVod_start_date();
                        long now = DateUtils.getNowTimeFormatEpoch();
                        if (vodStartDate <= now) {
                            break;
                        }
                        isVodContent = false;
                        break;
                    case ContentUtils.CONTENT_TYPE_FLAG_THREE: // 「contents_type」が「3」:関連VOD
                        // isVodContentの初期値が「true」のため、break
                        break;
                    default: //「contents_type」が「0」:番組
                        isVodContent = false;
                        break;
                }
            }
            // VODコンテンツの場合、dTVチャンネルアプリの詳細画面を表示する
            if (isVodContent) {
                startUrl = UrlConstants.WebUrl.DTVCHANNEL_VIDEO_START_URL + detailData.getCrid();
                DTVTLogger.debug("crid :----" + detailData.getCrid());

            } else { // 番組コンテンツの場合、dTVチャンネルアプリのTOP画面を表示する
                DTVTLogger.debug("contentsType :----" + detailData.getmContent_type());
                startUrl = UrlConstants.WebUrl.DTVCHANNEL_TELEVISION_START_URL + detailData.getmService_id();
                DTVTLogger.debug("chno :----" + detailData.getmService_id());
            }
        }
        return startUrl;
    }

    /**
     * 機能：APP起動用Dialog確認メッセージ.
     * @param serviceType サービスタイプ
     * @param context コンテキスト
     * @return 起動確認メッセージ
     */
    public static String getStartAppConfirmMessage(final StartAppServiceType serviceType, final Context context) {
        String errorMessage = "";
        switch (serviceType) {
            case DTV:
            case H4D_DTV:
                errorMessage = context.getString(R.string.dtv_content_service_start_dialog);
                break;
            case DTV_CH:
            case H4D_DTV_CH:
                errorMessage = context.getString(R.string.dtv_channel_service_start_dialog);
                break;
            case DAZN:
                errorMessage = context.getString(R.string.dazn_content_service_start_dialog);
                break;
            case DANIME:
                errorMessage = context.getString(R.string.d_anime_store_content_service_start_dialog);
                break;
            default:
                break;
        }
        return errorMessage;
    }

    /**
     * 機能：APP起動用パッケージ名.
     * @param serviceType サービスタイプ
     * @return パッケージ名
     */
    public static String getStartAppPackageName(final StartAppServiceType serviceType) {
        String packageName = "";
        switch (serviceType) {
            case DTV:
            case H4D_DTV:
                packageName = ContentDetailUtils.DTV_PACKAGE_NAME;
                break;
            case DTV_CH:
            case H4D_DTV_CH:
                packageName = ContentDetailUtils.DTVCHANNEL_PACKAGE_NAME;
                break;
            case DAZN:
                packageName = ContentDetailUtils.DAZN_PACKAGE_NAME;
                break;
            case DANIME:
                packageName = ContentDetailUtils.DANIMESTORE_PACKAGE_NAME;
                break;
            default:
                break;
        }
        return packageName;
    }

    /**
     * 機能：APP起動用パッケージ名.
     * @param serviceType サービスタイプ
     * @return 起動google play先
     */
    public static String getStartAppGoogleUrl(final StartAppServiceType serviceType) {
        String googleUrl = "";
        switch (serviceType) {
            case DTV:
            case H4D_DTV:
                googleUrl = UrlConstants.WebUrl.DTV_GOOGLEPLAY_DOWNLOAD_URL;
                break;
            case DTV_CH:
            case H4D_DTV_CH:
                googleUrl = UrlConstants.WebUrl.DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL;
                break;
            case DAZN:
                googleUrl = UrlConstants.WebUrl.DAZN_GOOGLEPLAY_DOWNLOAD_URL;
                break;
            case DANIME:
                googleUrl = UrlConstants.WebUrl.DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL;
                break;
            default:
                break;
        }
        return googleUrl;
    }

    /**
     * 機能：APP起動用未インストールメッセージ.
     * @param serviceType サービスタイプ
     * @param context コンテキスト
     * @return 未インストールメッセージ
     */
    public static String getStartAppUnInstallMessage(final StartAppServiceType serviceType, final Context context) {
        String noInstallMessage = "";
        switch (serviceType) {
            case DTV:
            case H4D_DTV:
                noInstallMessage = context.getString(R.string.dtv_content_service_application_not_install);
                break;
            case DTV_CH:
            case H4D_DTV_CH:
                noInstallMessage = context.getString(R.string.dtv_channel_service_application_not_install_dialog);
                break;
            case DAZN:
                noInstallMessage = context.getString(R.string.dazn_application_not_install_dialog);
                break;
            case DANIME:
                noInstallMessage = context.getString(R.string.d_anime_store_application_not_install_dialog);
                break;
            default:
                break;
        }
        return noInstallMessage;
    }

    /**
     * 機能：APP起動用未インストールメッセージ.
     * @param serviceType サービスタイプ
     * @return バージョンコード（基準値）
     */
    private static long getStartAppStandardVersionCode(final StartAppServiceType serviceType) {
        long versionCode = -1;
        switch (serviceType) {
            case DTV:
            case H4D_DTV:
                versionCode = DTV_VERSION_STANDARD;
                break;
            case DTV_CH:
            case H4D_DTV_CH:
                versionCode = DTVCHANNEL_VERSION_STANDARD;
                break;
            case DAZN:
                versionCode = DAZN_VERSION_STANDARD;
                break;
            case DANIME:
                versionCode = DANIMESTORE_VERSION_STANDARD;
                break;
            default:
                break;
        }
        return versionCode;
    }

    /**
     * 機能：APP起動用古いバージョンメッセージ.
     * @param serviceType サービスタイプ
     * @param context コンテキスト
     * @return 更新提示メッセージ
     */
    private static String getStartAppUpdateMessage(final StartAppServiceType serviceType, final Context context) {
        String versionUpMessage = "";
        switch (serviceType) {
            case DTV:
            case H4D_DTV:
                versionUpMessage = context.getString(R.string.dtv_content_service_update_dialog);
                break;
            case DTV_CH:
            case H4D_DTV_CH:
                versionUpMessage = context.getString(R.string.dtv_channel_service_update_dialog);
                break;
            case DAZN:
                versionUpMessage = context.getString(R.string.dazn_content_service_update_dialog);
                break;
            case DANIME:
                versionUpMessage = context.getString(R.string.d_anime_store_content_service_update_dialog);
                break;
            default:
                break;
        }
        return versionUpMessage;
    }

    /**
     * 機能：APP起動用古いバージョンメッセージチェック.
     * @param serviceType サービスタイプ
     * @param context コンテキスト
     * @return 更新提示メッセージ
     */
    public static String getStartAppVersionMessage(final StartAppServiceType serviceType, final Context context) {
        String versionUpMessage = "";
        String packageName = getStartAppPackageName(serviceType);
        long localVersionCode = getVersionCode(packageName, context);
        long standardVersionCode = getStartAppStandardVersionCode(serviceType);
        if (localVersionCode < standardVersionCode) {
            versionUpMessage = getStartAppUpdateMessage(serviceType, context);
        }
        return versionUpMessage;
    }

    /**
     * 機能：放送種別取得.
     * @param tvService tvService
     * @return 放送種別
     */
    private static int getPlatformType(final String tvService) {
        int result = 0;
        if (tvService != null) {
            switch (tvService) {
                case ContentUtils.TV_SERVICE_FLAG_HIKARI:
                    result = PLATFORM_TYPE_H4D;
                    break;
                case ContentUtils.TV_SERVICE_FLAG_TTB:
                    result = PLATFORM_TYPE_TTB;
                    break;
                case ContentUtils.TV_SERVICE_FLAG_BS:
                    result = PLATFORM_TYPE_BS;
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    /**
     * 機能：録画予約情報取得.
     * @param mDetailFullData mDetailFullData
     * @return 録画予約情報
     */
    public static RecordingReservationContentsDetailInfo getRecordingReservationContentsDetailInfo(final VodMetaFullData mDetailFullData) {
        RecordingReservationContentsDetailInfo mRecordingReservationContentsDetailInfo = new RecordingReservationContentsDetailInfo(
                mDetailFullData.getmService_id(),
                mDetailFullData.getTitle(),
                mDetailFullData.getPublish_start_date(),
                mDetailFullData.getDur(),
                mDetailFullData.getR_value());
        mRecordingReservationContentsDetailInfo.setEventId(mDetailFullData.getmEvent_id());
        mRecordingReservationContentsDetailInfo.setPlatformType(getPlatformType(mDetailFullData.getmTv_service()));
        return mRecordingReservationContentsDetailInfo;
    }

    /**
     * タブ名取得.
     * @param tabType タブタイプ
     * @param context context
     * @return 録画予約確認ダイアログ
     */
    public static String[] getTabNames(final ContentDetailUtils.TabType tabType, final Context context) {
        String[] mTabNames = null;
        switch (tabType) {
            case VOD:
                mTabNames = context.getResources().getStringArray(R.array.contents_detail_tab_other_service_vod);
                break;
            case TV_ONLY:
                mTabNames = context.getResources().getStringArray(R.array.contents_detail_tab_other_service_tv);
                break;
            case TV_CH:
                mTabNames = context.getResources().getStringArray(R.array.contents_detail_tabs_tv_ch);
                break;
            case VOD_EPISODE:
                mTabNames = context.getResources().getStringArray(R.array.contents_detail_tabs_vod_episode);
                break;
            default:
                break;
        }
        return mTabNames;
    }

    /**
     * 録画予約確認ダイアログを表示.
     * @param context コンテキスト
     * @return 録画予約確認ダイアログ
     */
    public static CustomDialog createRecordingReservationConfirmDialog(final Context context) {
        CustomDialog recordingReservationConfirmDialog =
                new CustomDialog(context, CustomDialog.DialogType.CONFIRM);
        recordingReservationConfirmDialog.setTitle(context.getString(
                R.string.recording_reservation_confirm_dialog_title));
        recordingReservationConfirmDialog.setContent(context.getString(
                R.string.recording_reservation_confirm_dialog_msg));
        recordingReservationConfirmDialog.setConfirmText(R.string.recording_reservation_confirm_dialog_confirm);
        recordingReservationConfirmDialog.setCancelText(R.string.recording_reservation_confirm_dialog_cancel);
        recordingReservationConfirmDialog.setCancelable(false);
        return recordingReservationConfirmDialog;
    }

    /**
     * 録画予約失敗時エラーダイアログ表示.
     * @param channels チャンネルリスト.
     * @param channelId チャンネルId.
     * @return チャンネル情報
     */
    public static ChannelInfo setPureDchChannelName(final ArrayList<ChannelInfo> channels, final String channelId) {
        ChannelInfo mChannel = null;
        String tvService = ContentUtils.getTvService(ContentUtils.ChannelServiceType.DTV_CH);
        if (!TextUtils.isEmpty(channelId) && !TextUtils.isEmpty(tvService)) {
            for (int i = 0; i < channels.size(); i++) {
                ChannelInfo channel = channels.get(i);
                if (channelId.equals(channel.getServiceId()) && tvService.equals(channel.getService())) {
                    mChannel = channel;
                    break;
                }
            }
        }
        return mChannel;
    }
}
