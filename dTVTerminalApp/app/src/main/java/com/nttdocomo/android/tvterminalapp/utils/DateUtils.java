/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    //VodClipList取得日付キー
    public static final String VOD_LAST_INSERT = "VodLastInsert";

    //TV_LAST_INSERT
    public static final String TV_LAST_INSERT = "TvLastInsert";

    //DailyRankList取得日付キー
    public static final String DAILY_RANK_LAST_INSERT = "DailyRankLastInsert";

    //ChannelList取得日付キー
    public static final String CHANNEL_LAST_INSERT = "ChannelLastInsert";

    //おすすめ番組取得日付キー
    public static final String RECOMMEND_CH_LAST_INSERT = "RecommendChLastInsert";

    //おすすめビデオ取得日付キー
    public static final String RECOMMEND_VD_LAST_INSERT = "RecommendVdLastInsert";

    //TvScheduleList取得日付キー
    public static final String TvSchedule_LAST_INSERT = "TvScheduleLastInsert";

    //UserInfo取得日付キー
    public static final String USER_INFO_LAST_INSERT = "UserInfoLastInsert";

    //WeeklyRank取得日付キー
    public static final String WEEKLY_RANK_LAST_INSERT = "WeeklyRankLastInsert";

    //VideoRank取得日付キー
    public static final String VIDEO_RANK_LAST_INSERT = "VideoRankLastInsert";

    //おすすめdTV取得日付キー
    public static final String RECOMMEND_DTV_LAST_INSERT = "RecommendDTVLastInsert";

    //おすすめdTV取得日付キー
    public static final String RECOMMEND_DCHANNEL_LAST_INSERT = "RecommendDCHLastInsert";

    //おすすめdTV取得日付キー
    public static final String RECOMMEND_DANIME_LAST_INSERT = "RecommendDAnimeLastInsert";

    //チャンネル取得日付キー
    public static final String CHANNEL_LAST_UPDATE = "ChannelLastUpdate";

    //番組表取得日付キー
    public static final String TVSCHEDULE_LAST_UPDATE = "TvScheduleLastUpdate";

    //レンタル一覧取得日付キー
    public static final String RENTAL_LIST_LAST_INSERT = "RentalListLastInsert";

    //SharedPreferences用データ
    private static final String DATA_SAVE = "DataSave";

    //日付フォーマット
    private static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";

    //DB保存期限
    private static final int LIMIT_HOUR = 1;

    private Context mContext;

    /**
     * 曜日の固定値
     */
    public static final int DAY_OF_WEEK_SUNDAY = 1;
    public static final int DAY_OF_WEEK_MONDAY = 2;
    public static final int DAY_OF_WEEK_TUESDAY = 3;
    public static final int DAY_OF_WEEK_WEDNESDAY = 4;
    public static final int DAY_OF_WEEK_THURSDAY = 5;
    public static final int DAY_OF_WEEK_FRIDAY = 6;
    public static final int DAY_OF_WEEK_SATURDAY = 7;


    /**
     * コンテキスト
     *
     * @param mContext
     */
    public DateUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 現在日時に1日加算した後に永続化
     *
     * @param key
     */
    public void addLastDate(
            String key) {

        // TODO:DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        //現在日時を取得
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, LIMIT_HOUR);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

        //データ永続化
        SharedPreferences data = mContext.getSharedPreferences(DATA_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(key, sdf.format(c.getTime()));
        editor.apply();
    }

    /**
     * 前回取得日時を返却
     *
     * @param key
     * @return
     */
    public String getLastDate(String key) {
        SharedPreferences data = mContext.getSharedPreferences(DATA_SAVE, Context.MODE_PRIVATE);
        String date = data.getString(key, "");
        return date;
    }

    /**
     * 日付が期限内か判定
     *
     * @param str
     * @return
     */
    public boolean isBeforeLimitDate(String str) {
        // TODO:DBには取得日時を格納しておき、現在時刻よりもデータが未来の場合,キャッシュ切れと判断すべき
        Calendar limit = new GregorianCalendar();
        //現在日時を取得
        Calendar now = Calendar.getInstance();

        //文字列からCalender型に変換
        if (str == null) {
            limit = null;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
                limit.setTime(sdf.parse(str.replace("-", "/")));
            } catch (ParseException e) {
                limit = null;
            }
        }

        //
        boolean isExpired = false;
        if (limit.compareTo(now) < 0) {
            isExpired = true;
        }
        return isExpired;
    }

    /**
     * エポック秒を YYYY/MM/DD かつString値に変換
     */
    public static String formatEpochToString(long epochTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        return dateFormat.format(new Date(epochTime * 1000));
    }

    /**
     * 現在日時エポック秒を取得
     */
    public static long getNowTimeFormatEpoch() {
        Calendar nowTime = Calendar.getInstance();
        return (nowTime.getTimeInMillis() / 1000);
    }

    /**
     * 現在日の0時00分00秒をエポック秒で取得
     */
    public static long getTodayStartTimeFormatEpoch() {
        Calendar nowTime = Calendar.getInstance();
        nowTime.set(nowTime.YEAR, nowTime.MONTH, nowTime.DATE, 0, 0, 0);
        nowTime.clear(nowTime.MILLISECOND);
        return (nowTime.getTimeInMillis() / 1000);
    }

    /**
     * 現在の曜日を取得
     *
     * @return 日:1 ～ 土:7
     */
    public static int getTodayDayOfWeek() {
        Calendar nowTime = Calendar.getInstance();
        return nowTime.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 指定日（エポック秒：秒単位）から曜日を取得
     *
     * @param epochTime
     * @return 日:1 ～ 土:7
     */
    public static int getDayOfWeek(long epochTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epochTime * 1000);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 月曜日までの日数を取得
     *
     * @param dayOfWeek
     * @return
     */
    public static int getNumberOfDaysUntilMonday(int dayOfWeek) {
        if (DateUtils.DAY_OF_WEEK_MONDAY < dayOfWeek) {
            return (DateUtils.DAY_OF_WEEK_MONDAY + 7) - dayOfWeek;
        } else {
            return DateUtils.DAY_OF_WEEK_MONDAY - dayOfWeek;
        }
    }

    /**
     * 日曜日までの日数を取得
     *
     * @param dayOfWeek
     * @return
     */
    public static int getNumberOfDaysUntilSUNDAY(int dayOfWeek) {
        if (DateUtils.DAY_OF_WEEK_SUNDAY < dayOfWeek) {
            return (DateUtils.DAY_OF_WEEK_SUNDAY + 7) - dayOfWeek;
        } else {
            return DateUtils.DAY_OF_WEEK_SUNDAY - dayOfWeek;
        }
    }
}
