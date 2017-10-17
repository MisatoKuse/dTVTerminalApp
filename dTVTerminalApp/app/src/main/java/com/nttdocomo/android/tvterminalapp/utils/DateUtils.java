package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class DateUtils {

    //VodClipList取得日付キー
    public static final String VOD_LAST_INSERT = "VodLastInsert";

    //DailyRankList取得日付キー
    public static final String DAILY_RANK_LAST_INSERT = "DailyRankLastInsert";

    //ChannelList取得日付キー
    public static final String CHANNEL_LAST_INSERT = "ChannelLastInsert";

    //TvScheduleList取得日付キー
    public static final String TvSchedule_LAST_INSERT = "TvScheduleLastInsert";

    //UserInfo取得日付キー
    public static final String USER_INFO_LAST_INSERT = "UserInfoLastInsert";

    //WeeklyRank取得日付キー
    public static final String WEEKLY_RANK_LAST_INSERT = "WeeklyRankLastInsert";

    //SharedPreferences用データ
    private static final String DATA_SAVE = "DataSave";

    //日付フォーマット
    private static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";

    //DB保存期限
    private static final int LIMIT_HOUR = 1;

    private Context mContext;

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
        if (limit.compareTo(now) == 1) {
            isExpired = true;
        }
        return isExpired;
    }
}
