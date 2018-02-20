/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

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
     * ロールリスト取得日付キー.
     */
    public static final String ROLELIST_LAST_UPDATE = "RoleListLastUpdate";

    /**
     * 番組表取得日付キー.
     */
    public static final String TVSCHEDULE_LAST_UPDATE = "TvScheduleLastUpdate";

    /**
     * レンタル一覧取得日付キー.
     */
    public static final String RENTAL_LIST_LAST_INSERT = "RentalListLastInsert";

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
    private static final String DATE_PATTERN_RECORDING_RESERVATION = "M/d (E) HH:mm";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_YYYY_MM_DD = "yyyy/MM/dd";

    /**
     * 日付フォーマット.
     */
    private static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";

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
    public static final String DATE_MDE = "M/d (E)";

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
    public static final String DATE_Y = "年";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_M = "月";

    /**
     * 日付フォーマット.
     */
    public static final String DATE_D = "日";

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
     * マイ番組表取得日付キー.
     */
    public static final String MY_CHANNEL_LIST_LAST_INSERT = "MyChannelListLastInsert";

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
     * 1日のエポック秒.
     */
    public static final long EPOCH_TIME_ONE_DAY = 86400;

    /**
     * 1時間のエポック秒.
     */
    public static final long EPOCH_TIME_ONE_HOUR = 3600;

    /**
     * 1週間のエポック秒.
     */
    public static final long EPOCH_TIME_ONE_WEEK = EPOCH_TIME_ONE_DAY * 7;

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

        // TODO:DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        // TODO:UTCタイムスタンプで良い.無駄に複雑化させているだけ.
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

        // TODO:DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        // TODO:文字列でなくUTCのタイムスタンプでよい.無駄に複雑にしている.
        //現在日時を取得
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYY_MM_DD, Locale.JAPAN);
        saveDataToSharePre(key, sdf.format(c.getTime()));
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
        // TODO:DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        // TODO:文字列でなくUTCのタイムスタンプでよい.無駄に複雑にしている.
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
        long epochTime = 0;
        if (null != strDate) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
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
        return (getEpochTime(strDate)) / 1000;
    }

    /**
     * エポック秒に変換する(yyyy-MM-dd HH:mm:ss形式).
     *
     * @param strDate 現在日付
     * @return エポック秒
     */
    public static long getHyphenEpochTime(final String strDate) {
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
    public static boolean isBetweenNowTime(long... timeArray) {
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
}