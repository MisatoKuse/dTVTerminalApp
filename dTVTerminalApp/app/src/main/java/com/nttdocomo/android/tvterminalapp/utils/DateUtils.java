/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日時関連の共通処理を記載する.
 */
public class DateUtils {

    /**
     * Context.
     */
    private Context mContext = null;

    /**
     * VodClipList取得日付キー.
     */
    public static final String VOD_LAST_INSERT = "VodLastInsert";

    /**
     * TV_LAST_INSERT.
     */
    public static final String TV_LAST_INSERT = "TvLastInsert";

    /**
     * DailyRankList取得日付キー.
     */
    public static final String DAILY_RANK_LAST_INSERT = "DailyRankLastInsert";

    /**
     * ChannelList取得日付キー.
     */
    public static final String CHANNEL_LAST_INSERT = "ChannelLastInsert";

    /**
     * おすすめ番組取得日付キー.
     */
    public static final String RECOMMEND_CH_LAST_INSERT = "RecommendChLastInsert";

    /**
     * おすすめビデオ取得日付キー.
     */
    public static final String RECOMMEND_VD_LAST_INSERT = "RecommendVdLastInsert";

    /**
     * TvScheduleList取得日付キー.
     */
    public static final String TV_SCHEDULE_LAST_INSERT = "TvScheduleLastInsert";

    /**
     * UserInfo取得日付キー.
     */
    public static final String USER_INFO_LAST_INSERT = "UserInfoLastInsert";

    /**
     * WeeklyRank取得日付キー.
     */
    public static final String WEEKLY_RANK_LAST_INSERT = "WeeklyRankLastInsert";

    /**
     * VideoRank取得日付キー.
     */
    public static final String VIDEO_RANK_LAST_INSERT = "VideoRankLastInsert";

    /**
     * おすすめdTV取得日付キー.
     */
    public static final String RECOMMEND_DTV_LAST_INSERT = "RecommendDTVLastInsert";

    /**
     * おすすめdチャンネル取得日付キー.
     */
    public static final String RECOMMEND_DCHANNEL_LAST_INSERT = "RecommendDCHLastInsert";

    /**
     * おすすめdTV取得日付キー.
     */
    public static final String RECOMMEND_DANIME_LAST_INSERT = "RecommendDAnimeLastInsert";

    /**
     * 視聴中ビデオ一覧日付キー.
     */
    public static final String WATCHING_VIDEO_LIST_LAST_INSERT = "WatchingVideoListLastInsert";

    /**
     * 購入済みVOD取得日付キー.
     */
    public static final String RENTAL_VOD_LAST_UPDATE = "RentalVodLastUpdate";

    /**
     * 購入済みチャンネル取得日付キー.
     */
    public static final String RENTAL_CHANNEL_LAST_UPDATE = "RentalChannelLastUpdate";

    /**
     * チャンネル取得日付キー.
     */
    public static final String CHANNEL_LAST_UPDATE = "ChannelLastUpdate";

    /**
     * DLNAブラウズ取得日付キー.
     */
    public static final String DLNA_BROWSE_UPDATE = "DlnaBrowseLastUpdate";

    /**
     * ロールリスト取得日付キー.
     */
    public static final String ROLELIST_LAST_UPDATE = "RoleListLastUpdate";

    /**
     * 番組表取得日付キー.
     */
    public static final String TVSCHEDULE_LAST_UPDATE = "TvScheduleLastUpdate";

    /**
     * SharedPreferences用データ.
     */
    private static final String DATA_SAVE = "DataSave";

    /**
     * 日付フォーマット.
     */
    private static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";

    /**
     * 日付フォーマット.
     */
    private static final String DATE_PATTERN_UNTIL_MINUTE = "yyyy/MM/dd HH:mm";

    /**
     * 日付フォーマット.
     */
    private static final String DATE_PATTERN_RECORDING_RESERVATION = "M/d (E) HH:mm";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_YYYY_MM_DD = "yyyy/MM/dd";
    /**
     * 日付フォーマット.
     */
    public static final String DATE_NOMARK_YYYYMMDD = "yyyyMMdd";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_YYYY_MM_DDHHMMSS = "yyyy/MM/ddHH:mm:ss";

    /**
     * 日付フォーマット.
     */
    private static final String DATE_PATTERN_HYPHEN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日付フォーマット.
     */
    private static final String DATE_MDE = "M/d (E)";
    /**
     * 日付フォーマット.
     */
    private static final String DATE_HYPHEN = " - ";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_YYYYMMDDE = "yyyy年MM月dd日 (E)";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_YYYYMMDD = "yyyy-MM-dd";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_YYYY_MM_DD_J = "yyyy年MM月dd日";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_HHMMSS = "HHmmss";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_STANDARD_START = "04:00:00";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_STANDARD_END = "03:00:00";

    /**
     * DB保存期限.
     */
    private static final int LIMIT_HOUR = 1;

    /**
     * 曜日配列.
     */
    public static final String[] STRING_DAY_OF_WEEK = {null, "日", "月", "火", "水", "木", "金", "土"};

    /**
     * ビデオジャンル一覧データ.
     */
    public static final String VIDEO_GENRE_LIST_LAST_INSERT = "video_genre_list_last_insert";

    /**
     * 日曜日の固定値.
     */
    public static final int DAY_OF_WEEK_SUNDAY = 1;
    /**
     * 月曜日の固定値.
     */
    public static final int DAY_OF_WEEK_MONDAY = 2;
    /**
     * 火曜日の固定値.
     */
    public static final int DAY_OF_WEEK_TUESDAY = 3;
    /**
     * 水曜日の固定値.
     */
    public static final int DAY_OF_WEEK_WEDNESDAY = 4;
    /**
     * 木曜日の固定値.
     */
    public static final int DAY_OF_WEEK_THURSDAY = 5;
    /**
     * 金曜日の固定値.
     */
    public static final int DAY_OF_WEEK_FRIDAY = 6;
    /**
     * 土曜日の固定値.
     */
    public static final int DAY_OF_WEEK_SATURDAY = 7;

    /**
     * 1日のエポック秒(用途ができたので、public化).
     */
    public static final long EPOCH_TIME_ONE_DAY = 86400;

    /**
     * 1時間のエポック秒.
     */
    public static final long EPOCH_TIME_ONE_HOUR = 3600;
    /**
     * 30分のエポック秒.
     */
    public final static int EPOCH_TIME_THIRTY_MINUTES = 1800;

    /**
     * 配信期限(avail_end_date/vod_end_date)の判定基準.
     */
    private static final int AVAILABLE_BASE_DAY = 31;
    /**
     * 配信期限一週間.
     */
    private static final int PUBLISH_BASE_DAY = 7;
    /**
     * " ".
     */
    private static final String DATE_FORMAT_BLANK = " ";

    /**
     * 最初時.
     */
    public static final int START_TIME = 4;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public DateUtils(final Context context) {
        this.mContext = context;
    }

    /**
     * 現在日時に1日加算した後に永続化.
     *
     * @param key Preferencesキー
     */
    public void addLastDate(final String key) {

        // TODO :DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        //現在日時を取得
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, LIMIT_HOUR);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        saveDataToSharePre(key, sdf.format(c.getTime()));
    }

    /**
     * 現在日時に1日加算した後に永続化.
     *
     * @param key   name
     * @param value value
     */
    private void saveDataToSharePre(final String key, final String value) {
        //データ永続化
        SharedPreferences data = mContext.getSharedPreferences(DATA_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 現在日時に1日加算した後に永続化.
     *
     * @param key Preferencesキー
     */
    public void addLastProgramDate(final String key) {

        // TODO :DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        //現在日時を取得
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD);
        saveDataToSharePre(key, sdf.format(c.getTime()));
    }

    /**
     * DB更新時にすべてのデータ取得日付をクリアする.
     * @param context コンテキストファイル
     */
    public static void clearLastDate(final Context context) {
        //データ全クリア
        SharedPreferences data = context.getSharedPreferences(DATA_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.clear().commit();
        editor.apply();
    }

    /**
     * データ取得日付をクリアする.
     *
     * @param context コンテクストファイル
     * @param key     キー
     */
    public static void clearLastProgramDate(final Context context, final String key) {
        //データクリア
        SharedPreferences data = context.getSharedPreferences(DATA_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.remove(key).commit();
        editor.apply();
    }

    /**
     * 前回取得日時を返却.
     *
     * @param key ファイル名(KEY)
     * @return date 前回取得した時刻を返却
     */
    public String getLastDate(final String key) {
        SharedPreferences data = mContext.getSharedPreferences(DATA_SAVE, Context.MODE_PRIVATE);
        return data.getString(key, "");
    }

    /**
     * chNoの対象日付データの前回取得日時をエポック秒で返す.
     *
     * @param chNos ChNo
     * @param date 取得対象の日付
     * @return 前回取得日時
     */
    public String[] getChLastDate(final int[] chNos, final String date) {
        DTVTLogger.start();
        String chDate = date.replace("-", "");
        String[] chLastDates = new String[chNos.length];
        //引数データが不正であれば帰る
        if (chNos.length == 0 || TextUtils.isEmpty(chDate)) {
            return chLastDates;
        }

        String chInfoGetDate = StringUtils.getChDateInfo(chDate);

        //チャンネル情報取得日のDBフォルダが作成されているか確認する
        String filesDir = mContext.getFilesDir().getAbsolutePath();
        String dbChannelDir = StringUtils.getConnectStrings(filesDir, "/../databases/channel");
        String dbDir = StringUtils.getConnectStrings(dbChannelDir, "/", chInfoGetDate);
        File fileDir = new File(dbDir);
        if (!fileDir.isDirectory()) {
            //情報取得日のフォルダが作成されていないので、前回取得していない.
            return chLastDates;
        }

        //チャンネル番号のチャンネル情報(DB)が作成されているか確認する.
        for (int i = 0; i < chNos.length; i++) {
            File dbFile = new File(StringUtils.getConnectStrings(dbDir, "/", String.valueOf(chNos[i])));
            if (dbFile.isFile()) {
                //DBファイルが存在するので最終更新日時(DBファイルのタイムスタンプ)を取得する.
                Long lastModified = dbFile.lastModified() / 1000L;
                chLastDates[i] = String.valueOf(lastModified);
            }
        }

        return chLastDates;
    }

    /**
     * 日付が期限内か判定.
     *
     * @param str チェック日付
     * @return 日付チェック結果
     */
    public boolean isBeforeLimitDate(final String str) {
        // TODO :DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        // TODO :文字列でなくUTCのタイムスタンプでよい.無駄に複雑にしている.
        if (str == null) {
            // null渡しされた場合は取得すべきとして期限切れ判定.
            return true;
        } else {
            Calendar limit = new GregorianCalendar();
            //現在日時を取得
            Calendar now = Calendar.getInstance();
            boolean isExpired = false;
            //文字列からCalender型に変換
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
                limit.setTime(sdf.parse(str.replace("-", "/")));
                if (limit.compareTo(now) < 0) {
                    isExpired = true;
                }
            } catch (ParseException e) {
                return false;
            }
            return isExpired;
        }
    }

    /**
     * chNoの対象日付データの取得日時が、前回取得日時から24H経過しているか.
     *
     * @param lastDate chNoの対象日付データの取得日時(エポック秒)
     * @return true:経過している(データ取得を行う) false:経過していない(キャッシュを利用)
     */
    public boolean isBeforeLimitChDate(final String lastDate) {
        DTVTLogger.start();

        if (TextUtils.isEmpty(lastDate)) {
            //無効データ渡しされた場合は取得すべきとして期限切れ判定.
            return true;
        }
        //現在の日時を用意する.
        long nowTime = getNowTimeFormatEpoch();

        Long cacheLimitDate = Long.parseLong(lastDate) + EPOCH_TIME_ONE_DAY;
        return nowTime > cacheLimitDate;
    }

    /**
     * 日付が期限内か判定.
     *
     * @param lastStr 前回取得できた日付
     * @return 現在日付と前回の比較の判定
     */
    public boolean isBeforeProgramLimitDate(final String lastStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD, Locale.JAPAN);
        //日付の比較
        Calendar calendar = Calendar.getInstance();
        String nowStr = sdf.format(calendar.getTime());
        Date lastDate = new Date();
        Date nowDate = new Date();
        try {
            lastDate = sdf.parse(lastStr);
            nowDate = sdf.parse(nowStr);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        boolean isExpired = false;
        if (lastDate.compareTo(nowDate) != 0) {
            isExpired = true;
        }
        return isExpired;
    }

    /**
     * エポック秒を YYYY/MM/DD HH:mm:ss かつString値に変換.
     *
     * @param epochTime エポック秒
     * @return YYYY/MM/DD日付
     */
    public static String formatEpochToString(final long epochTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        return dateFormat.format(new Date(epochTime * 1000));
    }

    /**
     * エポック秒を YYYY/MM/DD HH:mm かつString値に変換.
     *
     * @param epochTime エポック秒
     * @return YYYY/MM/DD日付
     */
    public static String formatUntilMinuteTimeString(final long epochTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_UNTIL_MINUTE);
        return dateFormat.format(new Date(epochTime * 1000));
    }

    /**
     * エポック秒を 指定のフォーマット文字列に返却.
     * @param epochTime エポック秒
     * @param format フォーマット文字列（nullの場合、DATE_PATTERN_HYPHEN)
     * @return フォーマットされた日付
     */
    public static String formatEpochToString(final long epochTime, final String format) {
        String tmpFormat = format;
        if (tmpFormat == null) {
            tmpFormat = DATE_PATTERN_HYPHEN;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(tmpFormat);
        return dateFormat.format(new Date(epochTime * 1000));
    }

    /**
     * エポック秒を M/D(E) かつString値に変換.
     *
     * @param epochTime エポック秒
     * @return M/D(E)日付
     */
    public static String formatEpochToDateString(final long epochTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_MDE);
        return dateFormat.format(new Date(epochTime * 1000));
    }

    /**
     * エポック秒を yyyyMMddHHmmss かつString値に変換.
     *
     * @param epochTime エポック秒
     * @return YYYY/MM/DD日付
     */
    public static String formatEpochToStringOpeLog(final long epochTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS);
        return dateFormat.format(new Date(epochTime * 1000));
    }

    /**
     * エポック秒を yyyyMMddHH かつString値に変換.
     *
     * @param epochTime エポック秒
     * @return YYYYMMDD日付
     */
    public static String formatEpochToSimpleDate(final long epochTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_NOMARK_YYYYMMDD);
        return dateFormat.format(new Date(epochTime * 1000));
    }

    /**
     * 現在日時エポック秒を取得.
     *
     * @return 現在日時のエポック秒
     */
    public static long getNowTimeFormatEpoch() {
        Calendar nowTime = Calendar.getInstance();
        return (nowTime.getTimeInMillis() / 1000);
    }

    /**
     * 現在日の0時00分00秒をエポック秒で取得.
     *
     * @return エポック秒に変換した現在日の0時00分00秒
     */
    public static long getTodayStartTimeFormatEpoch() {
        Calendar nowTime = Calendar.getInstance();

        //年月日データ以外をゼロにして、本日の0時0分0秒とする
        nowTime.set(Calendar.HOUR_OF_DAY, 0);
        nowTime.set(Calendar.MINUTE, 0);
        nowTime.set(Calendar.SECOND, 0);
        nowTime.set(Calendar.MILLISECOND, 0);

        return (nowTime.getTimeInMillis() / 1000);
    }

    /**
     * 現在の曜日を取得.
     *
     * @return 日:1 ～ 土:7
     */
    public static int getTodayDayOfWeek() {
        Calendar nowTime = Calendar.getInstance();
        return nowTime.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 指定日（エポック秒：秒単位）から曜日を取得.
     *
     * @param epochTime エポック秒
     * @return 日:1 ～ 土:7
     */
    public static int getDayOfWeek(final long epochTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epochTime * 1000);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 月曜日までの日数を取得.
     *
     * @param dayOfWeek 日付
     * @return 月曜までの日数
     */
    public static int getNumberOfDaysUntilMonday(final int dayOfWeek) {
        if (DateUtils.DAY_OF_WEEK_MONDAY < dayOfWeek) {
            return (DateUtils.DAY_OF_WEEK_MONDAY + 7) - dayOfWeek;
        } else {
            return DateUtils.DAY_OF_WEEK_MONDAY - dayOfWeek;
        }
    }

    /**
     * 日曜日までの日数を取得.
     *
     * @param dayOfWeek 日付
     * @return 日曜までの日数
     */
    public static int getNumberOfDaysUntilSunday(final int dayOfWeek) {
        if (DateUtils.DAY_OF_WEEK_SUNDAY < dayOfWeek) {
            return (DateUtils.DAY_OF_WEEK_SUNDAY + 7) - dayOfWeek;
        } else {
            return DateUtils.DAY_OF_WEEK_SUNDAY - dayOfWeek;
        }
    }

    /**
     * 引数の日付(エポック秒)を M/d (DAY_OF_WEEK) hh:mm のString型に変換(録画予約一覧ListItem用).
     *
     * @param time 日付(エポック秒)
     * @return 変換日付
     */
    public static String getRecordShowListItem(final long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_RECORDING_RESERVATION);
        String text = sdf.format(new Date(cal.getTimeInMillis()));

        DTVTLogger.debug("NowTime = " + cal.toString());
        return text;
    }

    /**
     * 引数の日付(エポック秒)を M/d (DAY_OF_WEEK) hh:mm のString型に変換(ぷらら用).
     *
     * @param startTime 開始日付(エポック秒)
     * @param endTime 終了日付(エポック秒)
     * @return 変換日付
     */
    public static String getContentsDateString(final long startTime, final long endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_MDE, Locale.JAPAN);
        String date = sdf.format(new Date(cal.getTimeInMillis()));
        String startText = getHmm(cal);
        cal.setTimeInMillis(endTime * 1000);
        String endText = getHmm(cal);
        //番組(m/d（曜日）h:ii - h:ii)
        return date + DATE_FORMAT_BLANK + startText + DATE_HYPHEN + endText;
    }

    /**
     * 引数の日付を M/d (DAY_OF_WEEK) hh:mm のString型に変換(レコメンド用).
     *
     * @param startTime 開始日付(String)
     * @param endTime 終了日付(String)
     * @return 変換日付
     */
    public static String getContentsDateString(final String startTime, final String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            sdf = new SimpleDateFormat(DATE_MDE, Locale.JAPAN);
            String date = sdf.format(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            String startText = getHmm(cal);
            cal.setTime(endDate);
            String endText = getHmm(cal);
            //番組(m/d（曜日）h:ii - h:ii)
            return date + DATE_FORMAT_BLANK + startText + DATE_HYPHEN + endText;
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * 引数の日付(エポック秒)を M/d (DAY_OF_WEEK) hh:mm のString型に変換(ぷらら用).
     *
     * @param startTime 開始日付(エポック秒)
     * @return 変換日付
     */
    public static String getContentsDateString(final long startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_MDE, Locale.JAPAN);
        String date = sdf.format(new Date(cal.getTimeInMillis()));
        String startText = getHmm(cal);
        //番組(m/d（曜日）h:ii)
        return date + DATE_FORMAT_BLANK + startText;
    }

    /**
     * 引数の日付を M/d (DAY_OF_WEEK) hh:mm のString型に変換(レコメンド用).
     *
     * @param startTime 開始日付(String)
     * @return 変換日付
     */
    public static String getContentsDateString(final String startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
        try {
            Date startDate = sdf.parse(startTime);
            sdf = new SimpleDateFormat(DATE_MDE, Locale.JAPAN);
            String date = sdf.format(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            String startText = getHmm(cal);
            //番組(m/d（曜日）h:ii)
            return date + DATE_FORMAT_BLANK + startText;
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * コンテンツ配信表示(対向ぷららサーバ).
     *
     * @param context コンテクスト
     * @param availDate 配信開始/終了時間
     * @param isBefore true 配信前　false 配信期限
     * @return 日付フォーマット
     */
    public static String getContentsDateString(final Context context, final long availDate, final boolean isBefore) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(availDate * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_MDE, Locale.JAPAN);
        String date = sdf.format(new Date(cal.getTimeInMillis()));
        //m/d（曜日）から若しくはまで
        return date + (isBefore ? context.getString(R.string.common_date_format_start_str) : context.getString(R.string.common_date_format_end_str));
    }

    /**
     * コンテンツ配信表示(対向検レコサーバ).
     *
     * @param context コンテクスト
     * @param viewing 配信開始/終了時間
     * @param isBefore true 配信前　false 配信期限
     * @return 日付フォーマット
     */
    public static String getContentsDateString(final Context context, final String viewing, final boolean isBefore) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
        try {
            Date startDate = sdf.parse(viewing);
            sdf = new SimpleDateFormat(DATE_MDE, Locale.JAPAN);
            String result = sdf.format(startDate);
            //m/d（曜日）から若しくはまで
            return result + (isBefore ? context.getString(R.string.common_date_format_start_str) : context.getString(R.string.common_date_format_end_str));
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * コンテンツ配信表示(対向検レコサーバ).
     *
     * @param startView 配信開始時間
     * @return true 配信前、false 配信後
     */
    public static boolean isBefore(final String startView) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
        try {
            Date startDate = sdf.parse(startView);
            Calendar cal = Calendar.getInstance();
            Date nowDate = cal.getTime();
            return nowDate.compareTo(startDate) == -1;
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return false;
    }

    /**
     * コンテンツ配信表示(対向ぷららサーバ).
     *
     * @param startDate 配信開始時間
     * @return true 配信前、false 配信後
     */
    public static boolean isBefore(final long startDate) {
        Calendar cal = Calendar.getInstance();
        Date nowDate = cal.getTime();
        cal.setTimeInMillis(startDate * 1000);
        return nowDate.compareTo(cal.getTime()) == -1;
    }

    /**
     * 引数の日付(エポック秒)を M/d (DAY_OF_WEEK) hh:mm のString型に変換(コンテンツ詳細ぷらら用).
     *
     * @param context コンテクスト
     * @param endTime 終了日付(エポック秒)
     * @return 変換日付
     */
    public static String getContentsDetailVodDate(final Context context, final long endTime) {
        return getContentsDateString(context, endTime, false);
    }

    /**
     * 引数の日付を M/d (DAY_OF_WEEK) まで のString型に変換(コンテンツ詳細レコメンド用).
     *
     * @param context コンテクスト
     * @param endTime 終了日付
     * @return 変換日付
     */
    public static String getContentsDetailVodDate(final Context context, final String endTime) {
        return getContentsDateString(context, endTime, false);
    }

    /**
     * VODの31日以内の判断（コンテンツ詳細画面）.
     *
     * @param availEndDate availEndDate
     * @return VOD、OTHER
     */
    public static ContentUtils.ContentsType getContentsTypeByAvailEndDate(final long availEndDate) {
        ContentUtils.ContentsType cType = ContentUtils.ContentsType.OTHER;
        if (isIn31Day(availEndDate)) {
            cType = ContentUtils.ContentsType.VOD;
        }
        return cType;
    }

    /**
     * ひかりTV内dch_見逃しの31日以上判定.
     *
     * @param date 判定日付
     * @return true 32以上 false 其の他
     */
    public static boolean isOver31Day(final long date) {
        Calendar cal = Calendar.getInstance();
        Date nowDate = cal.getTime();
        cal.setTimeInMillis(date * 1000);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -AVAILABLE_BASE_DAY);
        Date pre31Date = cal.getTime();
        return nowDate.compareTo(pre31Date) == -1;
    }

    /**
     * ひかりTV内dch_見逃しの31日以内判定.
     *
     * @param date 判定日付
     * @return true 31以内 false 其の他
     */
    public static boolean isIn31Day(final long date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date nowDate = cal.getTime();
        cal.setTimeInMillis(date * 1000);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -AVAILABLE_BASE_DAY);
        Date pre31Date = cal.getTime();
        return nowDate.compareTo(pre31Date) == 1;
    }

    /**
     * 検レコの31日以内判定.
     *
     * @param date 判定日付
     * @return true 31以内 false 其の他
     */
    public static boolean isIn31Day(final String date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date nowDate = cal.getTime();
        Date startDate;
        Date endDate;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
        try {
            endDate = sdf.parse(date);
            cal.setTime(endDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DAY_OF_MONTH, -AVAILABLE_BASE_DAY);
            startDate = cal.getTime();
            return nowDate.compareTo(startDate) == 1;
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return false;
    }

    /**
     * 一週間以内判断.
     *
     * @param startPublishDate 配信開始
     * @return true 一週間以内、一週間超えた
     */
    public static boolean isInOneWeek(final String startPublishDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date nowDate = cal.getTime();
        Date startDate;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
        try {
            startDate = sdf.parse(startPublishDate);
            cal.setTime(startDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, +PUBLISH_BASE_DAY);
            Date endDate = cal.getTime();
            return nowDate.compareTo(startDate) != -1 && nowDate.compareTo(endDate) == -1;
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return false;
    }

    /**
     * 一週間以内判断.
     *
     * @param startTime 配信開始
     * @return true 一週間以内、一週間超えた
     */
    public static boolean isInOneWeek(final long startTime) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date nowDate = cal.getTime();
        cal.setTimeInMillis(startTime * 1000);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, +PUBLISH_BASE_DAY);
        Date endDate = cal.getTime();
        return nowDate.compareTo(startDate) != -1 && nowDate.compareTo(endDate) == -1;
    }

    /**
     * 放送中であるかどうか(リスト系チェック用).
     *
     * @param startTime 開始日付
     * @param endTime 終了日付
     * @param isPlala true ぷらら, false レコメンド
     * @return true 放送中, false 放送中ではない
     */
    public static boolean isNowOnAirDate(final String startTime, final String endTime, final boolean isPlala) {
        Calendar cal = Calendar.getInstance();
        Date nowDate = cal.getTime();
        Date startDate;
        Date endDate;
        if (isPlala) {
            long start = 0;
            long end = 0;
            try {
                start = Long.parseLong(startTime);
                end = Long.parseLong(endTime);
            } catch (NumberFormatException e) {
                //サーバーから送られてきたstartTime及びendTimeが、浮動小数点形式となっている場合があった。
                //その場合、long型へのパースで例外が発生する。
                //恐らくサーバー側の一時的な不具合なので、この例外処理で何もせず、startとendはゼロのままとする
                DTVTLogger.debug("NumberFormatException,pattern of the publish_date");
            }

            cal.setTimeInMillis(start * 1000);
            startDate = cal.getTime();
            cal.setTimeInMillis(end * 1000);
            endDate = cal.getTime();
            return (nowDate.compareTo(startDate) != -1 && nowDate.compareTo(endDate) != 1);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
            try {
                startDate = sdf.parse(startTime);
                endDate = sdf.parse(endTime);
                return (nowDate.compareTo(startDate) != -1 && nowDate.compareTo(endDate) != 1);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
            return false;
        }
    }

    /**
     * HHMMを取得（h:ii）.
     *
     * @param cal 日付
     * @return HHMM
     */
    public static String getHmm(final Calendar cal) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%d:%02d", hour, min);
    }

    /**
     * 詳細画面のチャンネルタブでの時間を取得.
     *
     * @param date 日付
     * @return  詳細画面のチャンネルタブでの時間
     */
    public static String getContentsDetailChannelHmm(final String date) {
        String result = "";
        if (!TextUtils.isEmpty(date)) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.JAPAN);
            try {
                Date resultDate = format.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(resultDate);
                result = getHmm(cal);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
        }
        return result;
    }

    /**
     * 引数の曜日（int型）をStringに変換する.
     *
     * @param dayOfWeek 曜日(int型)
     * @return 曜日
     */
    public static String getStringDayOfWeek(final int dayOfWeek) {
        return STRING_DAY_OF_WEEK[dayOfWeek];
    }

    /**
     * 録画予約開始時間の算出.
     * 引数日時（エポック秒）の0時00分00秒からの時間を算出
     *
     * @param startTime 日時(エポック秒)
     * @return 録画予約開始時間
     */
    public static long getCalculationRecordingReservationStartTime(final long startTime) {
        return startTime % EPOCH_TIME_ONE_DAY;
    }

    /**
     * dアカウント切り替え時に、以前のデータを削除する.
     *
     * @param context コンテキスト
     */
    public static void clearDataSave(final Context context) {
        SharedPreferences data = context.getSharedPreferences(DATA_SAVE, Context.MODE_PRIVATE);
        data.edit().clear().apply();
    }

    /**
     * エポック秒に変換する.
     *
     * @param strDate 現在日付
     * @return エポック秒
     */
    public static long getEpochTime(final String strDate) {
        //エポック秒が入った場合はそのままlong変換
        if (DataBaseUtils.isNumber(strDate)) {
            return Long.parseLong(strDate);
        }
        if (strDate == null) {
            return 0L;
        }
        if (TextUtils.isEmpty(strDate)) {
            return 0L;
        }
        long epochTime = 0;

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.JAPAN);
        Date gmt = null;
        try {
            gmt = formatter.parse(strDate);
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        if (gmt != null) {
            epochTime = gmt.getTime();
        }
        return epochTime;
    }

    /**
     * エポック秒に変換する(yyyyMMddHHmmss).
     *
     * @param strDate yyyyMMddHHmmss形式のString
     * @return エポック秒
     */
    public static long getEpochTimeLink(final String strDate) {
        long epochTime = 0;
        if (null != strDate) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_YYYY_MM_DD_HH_MM_SS);
            //APIレスポンスの値がJSTとのこと
            formatter.setTimeZone(TimeZone.getTimeZone("JST"));
            Date gmt = null;
            try {
                gmt = formatter.parse(strDate);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
            if (gmt != null) {
                epochTime = gmt.getTime();
            }
        }
        return epochTime;
    }

    /**
     * エポック秒に変換する.
     *
     * @param strDate "yyyy/MM/dd HH:mm:ss"形式の日時
     * @return エポック秒　ミリ秒→秒単位に変換後の値
     */
    public static long getSecondEpochTime(final String strDate) {
        // 既にエポック秒だったら、変換しない
        if (DataBaseUtils.isNumber(strDate)) {
            return Long.parseLong(strDate);
        } else {
            return (getEpochTime(strDate)) / 1000;
        }
    }

    /**
     * エポック秒に変換する(yyyy-MM-dd HH:mm:ss形式).
     *
     * @param strDate 現在日付
     * @return エポック秒
     */
    public static long getHyphenEpochTime(final String strDate) {
        //エポック秒が入った場合はそのままlong変換
        if (DataBaseUtils.isNumber(strDate)) {
            return Long.parseLong(strDate);
        }
        long epochTime = 0;
        if (null != strDate) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_HYPHEN);
            Date gmt = null;
            try {
                gmt = formatter.parse(strDate);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
            if (gmt != null) {
                //ミリ秒を外す
                epochTime = gmt.getTime() / 1000;
            }
        }
        return epochTime;
    }

    /**
     * 現在時刻が指定したエポック秒の範囲内に収まっているかどうかを調べる.
     *
     * @param timeArray 検査したい日時
     * @return 開始と終了の間の時間に現在の日時が収まってればtrue
     */
    public static boolean isBetweenNowTime(final long... timeArray) {
        //パラメータが一つしかないなら即座に帰る
        if (timeArray.length <= 1) {
            return false;
        }

        long startTime = timeArray[0];
        long finalTime = timeArray[0];

        //パラメータを展開する
        for (long checkTime : timeArray) {
            //より大きな値を取得
            if (finalTime < checkTime) {
                finalTime = checkTime;
            }

            //より小さな値を取得
            if (startTime > checkTime) {
                startTime = checkTime;
            }
        }

        //今の時間を取得する
        long nowTime = getNowTimeFormatEpoch();

        //現在の日時が開始と終了の範囲内に収まっているか確認
        if (startTime <= nowTime && finalTime >= nowTime) {
            //収まっているのでtrue
            return true;
        }

        //収まっていないのでfalse
        return false;
    }

    /**
     * ぷらら対抗向 日付情報の加工(まで/からをつける).
     *
     * @param context      コンテキストファイル
     * @param contentsData コンテンツデータ
     * @param contentsType コンテンツタイプ
     * @return 加工後日付
     */
    public static String addDateLimit(final Context context, final ContentsData contentsData, final ContentUtils.ContentsType contentsType) {
        String date;
        long availStartDate = contentsData.getAvailStartDate();
        if (DateUtils.isBefore(availStartDate)) {
            //配信前 m/d（曜日）から
            date = DateUtils.getContentsDateString(context, availStartDate, true);
        } else {
            date = addDateLimitVod(context, contentsData, contentsType);
        }
        return date;
    }

    /**
     * ぷらら対抗向VODコンテンツ用 日付情報の加工(まで/からをつける).
     *
     * @param context      コンテキストファイル
     * @param contentsData コンテンツデータ
     * @param contentsType コンテンツタイプ
     * @return 加工後日付
     */
    private static String addDateLimitVod(final Context context, final ContentsData contentsData, final ContentUtils.ContentsType contentsType) {
        String date = "";
        long availEndDate = contentsData.getAvailEndDate();
        switch (contentsType) {
            case VOD:
            case RENTAL:
            case PREMIUM:
                //VOD(m/d（曜日）まで)
                date = DateUtils.getContentsDetailVodDate(context, availEndDate);
                break;
            case DCHANNEL_VOD_OVER_31:
                //VOD(m/d（曜日）まで) ひかりTV内dch_見逃し(３２以上)は「見逃し」のみを表示
                date = StringUtils.getConnectStrings(
                        context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                break;
            case DCHANNEL_VOD_31:
                //VOD(m/d（曜日）まで) ひかりTV内dch_見逃し(３1以内)の場合は「m/d（曜日）まで | 見逃し」を表示
                date = DateUtils.getContentsDetailVodDate(context, contentsData.getVodEndDate());
                date = StringUtils.getConnectStrings(date, context.getString(R.string.home_contents_hyphen),
                        context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                break;
            case TV:
            case HIKARI_TV:
            case DIGITAL_TERRESTRIAL_BROADCASTING:
            case BROADCASTING_SATELLITE:
            case HIKARI_TV_NOW_ON_AIR:
            case HIKARI_IN_DCH_TV:
            case HIKARI_IN_DTV:
            case HIKARI_IN_DCH_MISS:
            case HIKARI_IN_DCH_RELATION:
            case PURE_DTV:
            case PURE_DTV_CHANNEL:
            case PURE_DTV_CHANNEL_MISS:
            case PURE_DTV_CHANNEL_RELATION:
            case D_ANIME_STORE:
            case OTHER:
            default:
                break;
        }
        return date;
    }

    /**
     * 見逃しを黄色文字に変更する.
     *
     * @param context       コンテキスト
     * @param viewingPeriod 変換文字列
     * @return 変換後文字列
     */
    public static SpannableString setMissViewingColor(final Context context, final String viewingPeriod) {
        SpannableString spannableString = new SpannableString(viewingPeriod);
        int subStart = 0;
        int subEnd = 0;
        String missView = context.getString(R.string.contents_detail_hikari_d_channel_miss_viewing);
        if (viewingPeriod.contains(missView)) {
            subStart = viewingPeriod.indexOf(missView);
            subEnd = subStart + missView.length();
        }
        //「見逃し」は黄色文字で表示する
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.contents_detail_video_miss_color)),
                subStart, subEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * ミリ秒をtextViewで表示形(01,05...)に変更.
     * @param millisecond ミリ秒
     * @return 文字列
     */
    public static String time2TextViewFormat(final int millisecond) {
        final int second = millisecond / 1000;
        final int hh = second / 3600;
        final int mm = second % 3600 / 60;
        final int ss = second % 60;
        final String str;
        if (hh != 0) {
            str = String.format(Locale.getDefault(), "%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format(Locale.getDefault(), "%02d:%02d", mm, ss);
        }
        return str;
    }
    /**
     * 機能.
     * システム時間取得して、日付(hour)チェックを行う、1時～4時の場合は日付-1
     *
     * @param selectDay チェする日付する、ない場合システム日付
     * @return formatDay チェックした時刻を返却
     */
    public static String getSystemTimeAndCheckHour(final String selectDay) {
        String formatDay;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYYMMDD, Locale.JAPAN);
        try {
            if (selectDay != null) {
                formatDay = selectDay;
            } else {
                formatDay = sdf.format(calendar.getTime());
            }
            if (isLastDay()) {
                calendar.setTime(sdf.parse(formatDay));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                formatDay = sdf.format(calendar.getTime());
            }
            return formatDay;
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 機能
     * 昨日の日付であるかどうか.
     *
     * @return 日付確認結果
     */
    public static boolean isLastDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat todaySdf = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN);
        int hour = Integer.parseInt(todaySdf.format(calendar.getTime()).substring(0, 2));
        return hour < START_TIME;
    }

    /**
     * 分、秒を時に転換する.
     *
     * @param curMin 分
     * @param curSec 秒
     * @return 時
     */
    public static float minSec2Hour(final int curMin, final int curSec) {
        int sec = curMin * 60;
        return ((float) sec + curSec) / 3600;
    }

    /**
     * 30日以内判定.
     *
     * @param activeDataDate 判定対象日時
     * @return 真偽値
     */
    public static boolean isLimitThirtyDay(final long activeDataDate) {
        return activeDataDate - DateUtils.getNowTimeFormatEpoch()
                < DateUtils.EPOCH_TIME_ONE_DAY * ContentDetailActivity.ONE_MONTH;
    }

    /**
     * 残日数計算.
     *
     * @param expireDate 満期日(yyyy-MM-dd HH:mm:ss形式)
     * @return 残日数
     */
    public static int getRemainingDays(final String expireDate) {
        Calendar expireCalendar = Calendar.getInstance();
        expireCalendar.setTimeInMillis(getHyphenEpochTime(expireDate) * 1000);
        expireCalendar.set(Calendar.HOUR_OF_DAY, 0);
        expireCalendar.set(Calendar.MINUTE, 0);
        expireCalendar.set(Calendar.SECOND, 0);
        expireCalendar.set(Calendar.MILLISECOND, 0);
        Calendar nowCal = Calendar.getInstance();

        return (int) ((expireCalendar.getTimeInMillis() - nowCal.getTimeInMillis()) / (EPOCH_TIME_ONE_DAY * 1000));
    }

    /**
     * 引数値が現在より30分以上経過又は引数値が未来の場合はtrue.
     *
     * @param lastTimeOnPause 判定対象のエポック秒
     * @return 判定結果
     */
    public static boolean isThirtyMinutesAgo(final long lastTimeOnPause) {
        long now = DateUtils.getNowTimeFormatEpoch();
        long afterThirty = lastTimeOnPause + EPOCH_TIME_THIRTY_MINUTES;
        //lastTimeOnPauseが0の時は初回表示時なのでfalseを返却すること
        return (afterThirty < now || now < lastTimeOnPause) && lastTimeOnPause != 0;
    }
}