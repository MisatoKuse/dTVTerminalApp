/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ProgramChannelAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.TvProgramListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.CalendarComparator;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.view.ProgramRecyclerView;
import com.nttdocomo.android.tvterminalapp.view.ProgramScrollView;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 番組表表示Activity.
 */
public class TvProgramListActivity extends BaseActivity implements View.OnClickListener, ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        ProgramScrollView.OnScrollOffsetListener, MyChannelDataProvider.ApiDataProviderCallback, TabItemLayout.OnClickTabTextListener {

    /**
     * hikariタブインデックス.
     */
    private static final int INDEX_TAB_HIKARI = 1;
    /**
     * マイチャンネルタブインデックス.
     */
    private static final int INDEX_TAB_MY_CHANNEL = 0;
    /**
     * 番組パネルリサイクルビュー.
     */
    private ProgramRecyclerView mProgramRecyclerView = null;
    /**
     * メニュー起動.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * 左側タイムスクロール.
     */
    private ProgramScrollView mTimeScrollView = null;
    /**
     * チャンネルリサイクルビュー.
     */
    private RecyclerView mChannelRecyclerView = null;
    /**
     * 共通タブレイアウト.
     */
    private TabItemLayout mTabLayout = null;
    /**
     * "現在カレンダーボタン".
     */
    private ImageView mTagImageView = null;
    /**
     * 最初時.
     */
    private static final int START_TIME = 4;
    /**
     * 標準時間.
     */
    private static final int STANDARD_TIME = 24;
    /**
     * タイムライン幅.
     */
    private static final int TIME_LINE_WIDTH = 44;
    /**
     * チャンネルリサイクルビュー丈.
     */
    private static final int CH_VIEW_HEIGHT = 44;
    /**
     * 1時間帯基準値.
     */
    private static final int ONE_HOUR_UNIT = 180;
    /**
     * 選択中日付.
     */
    private String mSelectDateStr = null;
    /**
     * today.
     */
    private String mToDay = null;
    /**
     * 時間軸.
     */
    private LinearLayout mLinearLayout = null;
    /**
     * チャンネルタブネーム.
     */
    private String[] mProgramTabNames = null;
    /**
     * EXPIRE_DATE.
     */
    private static final int EXPIRE_DATE = 7;
    /**
     * タブインデックス.
     */
    private int mTabIndex = 0;
    /**
     * 番組表中身アダプター.
     */
    private TvProgramListAdapter mTvProgramListAdapter = null;
    /**
     * チャンネルアダプター.
     */
    private ProgramChannelAdapter mProgramChannelAdapter = null;
    /**
     * チャンネルインフォリスト.
     */
    private List<ChannelInfo> mChannelInfo = new ArrayList<>();
    /**
     * チャンネルインフォリスト.
     */
    private ArrayList<ChannelInfo> mChannels = new ArrayList<>();
    /**
     * ひかりチャンネルリスト.
     */
    private ArrayList<ChannelInfo> hikariChannels;
    /**
     * マイ番組表にマッピングされたデータ.
     */
    private ArrayList<ChannelInfo> mappedMyChannelList;
    /**
     * マイ番組表データ.
     */
    private ArrayList<MyChannelMetaData> myChannelDataList = new ArrayList<>();
    /**
     * レッドタイムライン.
     */
    private RelativeLayout mTimeLine;
    /**
     * NOW.
     */
    private ImageView mNowImage;
    /**
     * 毎チャンネルエラーメッセージ.
     */
    private TextView mMyChannelNoDataTxT;
    /**
     * DAY_PRE0.
     */
    private static final String DAY_PRE0 = "0";
    /**
     * DATE_LINE.
     */
    private static final String DATE_LINE = "-";
    /**
     * マイ番組表データプロバイダー.
     */
    private MyChannelDataProvider mMyChannelDataProvider;
    /**
     * 番組表データプロバイダー.
     */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_program_list_main_layout);

        //Headerの設定
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);
        initView();
        syncScroll(mChannelRecyclerView, mProgramRecyclerView);
        //タイトル矢印表示
        setTvProgramTitleArrowVisibility(true);
        //タブ設定
        setTabView();
        //タグ設定
        setTagView();
        //時間帯設定
        setLeftTimeContentsView();
        //タイトル設定
        setTitle();
        //チャンネルデータ取得
        getChannelData();
    }

    /**
     * 機能
     * ビューの初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mTimeScrollView = findViewById(R.id.tv_program_list_main_layout_time_sl);
        mChannelRecyclerView = findViewById(R.id.tv_program_list_main_layout_channel_rv);
        mProgramRecyclerView = findViewById(R.id.tv_program_list_main_layout_channeldetail_rv);
        final ProgramScrollView programScrollView = findViewById(R.id.tv_program_list_main_layout_channeldetail_sl);
        mTagImageView = findViewById(R.id.tv_program_list_main_layout_curtime_iv);
        mTimeLine = findViewById(R.id.tv_program_list_main_layout_time_line);
        mNowImage = findViewById(R.id.tv_program_list_main_layout_time_line_now);
        mMyChannelNoDataTxT = findViewById(R.id.tv_program_list_main_layout_tip_tv);

        mChannelRecyclerView.bringToFront();
        mTagImageView.bringToFront();
        mTagImageView.setOnClickListener(this);
        mTitleTextView.setOnClickListener(this);
        mTitleArrowImage.setOnClickListener(this);
        mTimeScrollView.setScrollView(programScrollView);
        programScrollView.setScrollView(mTimeScrollView);
        programScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                MotionEvent e = MotionEvent.obtain(motionEvent);
                e.setLocation(e.getX() + programScrollView.getScrollX(),
                        e.getY() - mChannelRecyclerView.getHeight());
                mProgramRecyclerView.forceToDispatchTouchEvent(e);
                return false;
            }
        });
    }

    /**
     * 機能
     * 日付の表示.
     */
    private void showDatePickDlg() {
        int curYear = 0;
        int curMonth = 0;
        int curDay = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD_J, Locale.JAPAN);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, final int year,
                                  final int monthOfYear, final int dayOfMonth) {
                setSelectedDate(year, monthOfYear, dayOfMonth);
                clearData();
                getChannelData();
            }
        };
        try {
            Date date = sdf.parse(mToDay);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            curYear = calendar.get(Calendar.YEAR);
            curMonth = calendar.get(Calendar.MONTH);
            curDay = calendar.get(Calendar.DATE);
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,
                curYear, curMonth, curDay);
        //日付の選択できる範囲を設定
        DatePicker datePicker = datePickerDialog.getDatePicker();
        GregorianCalendar gc = new GregorianCalendar();
        if (isLastDay()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            gc.setTime(calendar.getTime());
        } else {
            gc.setTime(new Date());
        }
        if (mTabIndex == mProgramTabNames.length - 1) {
            gc.add(Calendar.DAY_OF_MONTH, -EXPIRE_DATE);
        }
        datePicker.setMinDate(gc.getTimeInMillis());
        if (mTabIndex == mProgramTabNames.length - 1) {
            gc.add(Calendar.DAY_OF_MONTH, +(EXPIRE_DATE * 2));
        } else {
            gc.add(Calendar.DAY_OF_MONTH, +EXPIRE_DATE);
        }
        datePicker.setMaxDate(gc.getTimeInMillis());
        datePickerDialog.show();
    }

    /**
     * 機能
     * タイトルの日付の設定.
     *
     * @param year        年
     * @param monthOfYear 月
     * @param dayOfMonth  日
     */
    private void setSelectedDate(final int year, final int monthOfYear, final int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        StringBuilder selectDate;
        calendar.set(year, monthOfYear, dayOfMonth);
        int destMonthOfYear = monthOfYear;
        destMonthOfYear++;
        StringBuilder month = new StringBuilder();
        StringBuilder day = new StringBuilder();
        if (destMonthOfYear < 10) {
            month.append(DAY_PRE0);
        }
        month.append(destMonthOfYear);
        if (dayOfMonth < 10) {
            day.append(DAY_PRE0);
        }
        day.append(dayOfMonth);
        selectDate = new StringBuilder();
        selectDate.append(year);
        selectDate.append(DATE_LINE);
        selectDate.append(month.toString());
        selectDate.append(DATE_LINE);
        selectDate.append(day);
        mSelectDateStr = getSystemTimeAndCheckHour(selectDate.toString());
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYYMMDD, Locale.JAPAN);
        try {
            Date date = sdf.parse(mSelectDateStr);
            SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_YYYYMMDDE, Locale.JAPAN);
            String newDate = format.format(date.getTime());
            setTitleText(newDate.substring(5));
            SimpleDateFormat formatDialog = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD_J, Locale.JAPAN);
            mToDay = formatDialog.format(date.getTime());
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 機能
     * 昨日の日付であるかどうか.
     *
     * @return 日付確認結果
     */
    private boolean isLastDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat todaySdf = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN);
        int hour = Integer.parseInt(todaySdf.format(calendar.getTime()).substring(0, 2));
        return hour < START_TIME;
    }

    /**
     * 機能.
     * システム時間取得して、日付(hour)チェックを行う、1時～4時の場合は日付-1
     *
     * @param selectDay チェする日付する、ない場合システム日付
     * @return formatDay チェックした時刻を返却
     */
    private String getSystemTimeAndCheckHour(final String selectDay) {
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
     * タイトルの設定.
     */
    private void setTitle() {
        //フォーマットパターンを指定して表示する
        Calendar c = Calendar.getInstance();
        Locale.setDefault(Locale.JAPAN);
        if (isLastDay()) {
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYYMMDDE, Locale.JAPAN);
        mToDay = sdf.format(c.getTime());
        setTitleText(mToDay.substring(5));
        mSelectDateStr = getSystemTimeAndCheckHour(null);
    }

    /**
     * 機能
     * タブの設定.
     */
    private void setTabView() {
        DTVTLogger.start();
        mProgramTabNames = getResources().getStringArray(R.array.tv_program_list_tab_names);
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mProgramTabNames, TabItemLayout.ActivityType.PROGRAM_LIST_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_tv_program_list_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mProgramTabNames);
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (mTabIndex != position) {
            mTabIndex = position;
            clearData();
            getChannelData();
            DTVTLogger.end();
        }
    }

    /**
     * 機能
     * チャンネルと番組詳細を同時にスクロール設定.
     *
     * @param channelList チャンネルリサイクルビュー
     * @param programList 番組リサイクルビュー
     */
    private void syncScroll(final RecyclerView channelList, final RecyclerView programList) {
        channelList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                        programList.stopScroll();
                        programList.scrollBy(dx, dy);
                }
            }
        });

        programList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                        channelList.stopScroll();
                        channelList.scrollBy(dx, dy);
                }
            }
        });
    }

    /**
     * 機能
     * 番組タブを設定.
     */
    private void setTagView() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                dip2px(TIME_LINE_WIDTH),
                dip2px(TIME_LINE_WIDTH));
        mTagImageView.setLayoutParams(layoutParams);
        mTagImageView.setImageResource(R.drawable.tv_program_list_cur_time_btn_selector);
    }

    /**
     * 機能
     * 時間軸を設定.
     */
    private void setLeftTimeContentsView() {
        //Timeコンテント設定
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(llLayoutParams);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setBackgroundColor(Color.BLACK);
        mTimeScrollView.addView(mLinearLayout);
        for (int i = START_TIME; i < STANDARD_TIME + START_TIME; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dip2px(TIME_LINE_WIDTH),
                    dip2px(ONE_HOUR_UNIT));
            tabTextView.setLayoutParams(params);
            int curTime = i;
            if (curTime >= STANDARD_TIME) {
                curTime = curTime - STANDARD_TIME;
            }
            tabTextView.setText(String.valueOf(curTime));
            tabTextView.setPadding(0, (int) getDensity() * 8, 0, 0);
            tabTextView.setBackgroundColor(Color.BLACK);
            if (i == STANDARD_TIME + START_TIME - 1) {
                tabTextView.setBackgroundResource(R.drawable.rectangele_end);
            } else {
                tabTextView.setBackgroundResource(R.drawable.rectangele_start);
            }
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            tabTextView.setTag(curTime);
            mLinearLayout.addView(tabTextView);
        }
    }

    /**
     * 機能
     * チャンネルを設定.
     *
     * @param channels チャンネル
     */
    private void setChannelContentsView(final ArrayList<ChannelInfo> channels) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mChannelRecyclerView.setLayoutManager(linearLayoutManager);
        mProgramChannelAdapter = new ProgramChannelAdapter(this, channels);
        mChannelRecyclerView.setAdapter(mProgramChannelAdapter);
    }

    /**
     * 機能
     * チャンネルデータ取得.
     */
    private void getChannelData() {
        if (mTabIndex != INDEX_TAB_MY_CHANNEL) { //ひかり、dTVチャンネル
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            mScaledDownProgramListDataProvider.getChannelList(0, 0, "", mTabIndex);
        } else { //MY番組表
            mMyChannelDataProvider = new MyChannelDataProvider(this);
            mMyChannelDataProvider.getMyChannelList(R.layout.tv_program_list_main_layout);
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_program_list_main_layout_curtime_iv:
                //システム時間軸にスクロール
                scrollToCurTime();
                //現在時刻ラインの表示更新
                refreshTimeLine();
                break;
            case R.id.header_layout_text:
                //日付選択ダイアログ
                showDatePickDlg();
                break;
            case R.id.tv_program_list_main_layout_calendar_arrow:
                //日付選択ダイアログ(矢印)
                showDatePickDlg();
                break;
            default:
                super.onClick(v);
        }
    }

    /**
     * 機能
     * 番組表情報を設定.
     *
     * @param channelInfo 番組表情報.
     */
    private void setProgramRecyclerView(final List<ChannelInfo> channelInfo) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mProgramRecyclerView.setLayoutManager(linearLayoutManager);
        //以前のアダプタに設定されている値を取得して追加.
        if (mTvProgramListAdapter != null) {
            List<ChannelInfo> oldChannelInfo = mTvProgramListAdapter.getProgramList();
            for (ChannelInfo oldChannel : oldChannelInfo) {
                channelInfo.add(oldChannel);
            }
        }
        mTvProgramListAdapter = new TvProgramListAdapter(this, channelInfo);
        mProgramRecyclerView.setAdapter(mTvProgramListAdapter);
        mProgramRecyclerView.setItemViewCacheSize(channelInfo.size());
        mProgramRecyclerView.getRecycledViewPool().setMaxRecycledViews(mTvProgramListAdapter.getItemViewType(0), 3);
        //スクロール時、リスナー設置
        mTimeScrollView.setOnScrollOffsetListener(this);
        scrollToCurTime();
        refreshTimeLine();
    }

    /**
     * 機能
     * 番組表情報をクリア.
     */
    private void clearData() {
        mChannelRecyclerView.removeAllViews();
        mProgramRecyclerView.removeAllViews();
        mMyChannelNoDataTxT.setVisibility(View.INVISIBLE);
        mTimeLine.setVisibility(View.INVISIBLE);
        mTimeScrollView.setVisibility(View.INVISIBLE);
        mTagImageView.setVisibility(View.INVISIBLE);
        if (mChannels != null) {
            mChannels.clear();
        }
        if (mChannelInfo != null) {
            mChannelInfo.clear();
        }
        if (mProgramChannelAdapter != null) {
            mProgramChannelAdapter.notifyDataSetChanged();
        }
        if (mTvProgramListAdapter != null) {
            mTvProgramListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 機能
     * 現在時刻にスクロール.
     */
    private void scrollToCurTime() {
        String curTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
        int curClock = Integer.parseInt(curTime.substring(0, 2));
        int scrollDis;
        if (START_TIME <= curClock && curClock < STANDARD_TIME) {
            scrollDis = (curClock - START_TIME) * mLinearLayout.getHeight() / STANDARD_TIME;
        } else {
            if (curClock == 0) {
                scrollDis = (STANDARD_TIME - START_TIME) * mLinearLayout.getHeight() / STANDARD_TIME;
            } else if (curClock == 1) {
                scrollDis = (STANDARD_TIME - START_TIME + 1) * mLinearLayout.getHeight() / STANDARD_TIME;
            } else {
                scrollDis = mLinearLayout.getHeight();
            }
        }
        mTimeScrollView.smoothScrollTo(0, scrollDis);
    }

    /**
     * 分、秒を時に転換する.
     *
     * @param curMin 分
     * @param curSec 秒
     * @return 時
     */
    private float minSec2Hour(final int curMin, final int curSec) {
        int sec = curMin * 60;
        return ((float) sec + curSec) / 3600;
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        if (channelsInfo != null && channelsInfo.getChannels() != null) {
            List<ChannelInfo> channels = channelsInfo.getChannels();
            sort(channels);
            mChannelInfo = channels;
            if (mTabIndex != 0) {
                setProgramRecyclerView(mChannelInfo);
            } else { //マイ番組表
                ArrayList<ChannelInfo> myChannels = new ArrayList<>();
                try {
                    for (int i = 0; i < mappedMyChannelList.size(); i++) {
                        for (int j = 0; j < mChannelInfo.size(); j++) {
                            if (mappedMyChannelList.get(i).getChNo() == mChannelInfo.get(j).getChNo()) {
                                //チャンネル番号でマッピング
                                myChannels.add(mChannelInfo.get(j));
                            }
                        }
                    }
                    setProgramRecyclerView(myChannels);
                } catch (NullPointerException e) {
                    DTVTLogger.debug("mappedMyChannelList NullPointerException");
                }
            }
        }
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        if (mTabIndex == INDEX_TAB_MY_CHANNEL) {
            //MY番組表
            if (channels != null && channels.size() > 0) {
                showMyChannelNoItem(false);
                this.hikariChannels = channels;
                mappedMyChannelList = executeMapping();
                setChannelContentsView(mappedMyChannelList);
                loadMyChannel();
            } else {
                //ひかりTVデータなしの場合
                scrollToCurTime();
                refreshTimeLine();
            }
        } else {
            //ひかり、dTVチャンネル
            if (channels != null && channels.size() > 0) {
                showMyChannelNoItem(false);
                this.mChannels = channels;
                setChannelContentsView(mChannels);
                if (mScaledDownProgramListDataProvider == null) {
                    mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
                }
                int[] channelNos = new int[channels.size()];
                for (int i = 0; i < channels.size(); i++) {
                    channelNos[i] = channels.get(i).getChNo();
                }
                String dateStr = mSelectDateStr.replace("-", "");
                String[] dateList = {dateStr};
                //TODO 現状一括でリクエストしているため修正予定.
                mScaledDownProgramListDataProvider.getProgram(channelNos, dateList, mTabIndex);
            } else {
                scrollToCurTime();
                refreshTimeLine();
            }
        }
    }

    /**
     * My番組表ロード.
     */
    private void loadMyChannel() {
        if (mScaledDownProgramListDataProvider == null) {
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        }
        int[] channelNos = new int[mappedMyChannelList.size()];
        for (int i = 0; i < mappedMyChannelList.size(); i++) {
            channelNos[i] = mappedMyChannelList.get(i).getChNo();
        }
        if (channelNos.length != 0) {
        	//マイ番組表設定されていない場合、通信しない
            String dateStr = mSelectDateStr.replace("-", "");
            String[] dateList = {dateStr};
            mScaledDownProgramListDataProvider.getProgram(channelNos, dateList, 1);
        } else {
            //「マイ番組が設定されていません」と表示される
            if (mTabIndex == INDEX_TAB_MY_CHANNEL) {
                showMyChannelNoItem(true);
            }
        }
    }

    /**
     * マイ番組表Noチャンネル表示.
     *
     * @param isShowFlag マイ番組表の要素表示フラグ.
     */
    private void showMyChannelNoItem(final boolean isShowFlag) {
        if (isShowFlag) {
            mTimeScrollView.setVisibility(View.INVISIBLE);
            mTagImageView.setVisibility(View.INVISIBLE);
            mTimeLine.setVisibility(View.INVISIBLE);
            mMyChannelNoDataTxT.setVisibility(View.VISIBLE);
        } else {
            mTimeScrollView.setVisibility(View.VISIBLE);
            mTagImageView.setVisibility(View.VISIBLE);
            mTimeLine.setVisibility(View.VISIBLE);
            mMyChannelNoDataTxT.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * チャンネルリストからマッピングデータを抽出する.
     *
     * @return マイ番組表に表示するチャンネル情報
     */
    private ArrayList<ChannelInfo> executeMapping() {
        ArrayList<ChannelInfo> myChannels = new ArrayList<>();
        if (myChannelDataList != null) {
            for (int i = 0; i < myChannelDataList.size(); i++) {
                for (int j = 0; j < hikariChannels.size(); j++) {
                    //サービスIDでマッピング
                    if (myChannelDataList.get(i).getServiceId().equals(hikariChannels.get(j).getServiceId())) {
                        myChannels.add(hikariChannels.get(j));
                    }
                }
            }
        }
        return myChannels;
    }

    /**
     * ソートを行う.
     *
     * @param channels チャンネル情報リスト
     */
    private void sort(final List<ChannelInfo> channels) {
        for (ChannelInfo channel : channels) {

            ArrayList<ScheduleInfo> scheduleInfo = channel.getSchedules();
            boolean isContinue = false;
            for (ScheduleInfo info : scheduleInfo) {
                if (TextUtils.isEmpty(info.getStartTime())) {
                    DTVTLogger.debug("getStartTime() : null");
                    isContinue = true;
                    break;
                }
            }
            if (isContinue) {
                continue;
            }

            Collections.sort(channel.getSchedules(), new CalendarComparator());
        }
    }

    /**
     * MY番組表情報取得.
     *
     * @param myChannelMetaData 画面に渡すチャンネル番組情報
     */
    @Override
    public void onMyChannelListCallback(final ArrayList<MyChannelMetaData> myChannelMetaData) {
        if (myChannelMetaData != null && myChannelMetaData.size() > 0) {
            this.myChannelDataList = myChannelMetaData;
            //ひかりリストからチャンネル探すため
            if (mScaledDownProgramListDataProvider == null) {
                mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            }
            mScaledDownProgramListDataProvider.getChannelList(0, 0, "", INDEX_TAB_HIKARI);
        } else {
            //MY番組表情報がなければMY番組表を設定していないとする(データないので、特にタイムライン表示必要がない)
            if (mTabIndex == INDEX_TAB_MY_CHANNEL) {
                showMyChannelNoItem(true);
            }
        }
    }

    @Override
    public void onMyChannelRegisterCallback(final String result) {
        //nothing to do
    }

    @Override
    public void onMyChannelDeleteCallback(final String result) {
        //nothing to do
    }

    /**
     * スクロール時、スクロール距離を返すコールバック.
     *
     * @param offset オフセット
     */
    @Override
    public void onScrollOffset(final int offset) {
        String curTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
        int curClock = Integer.parseInt(curTime.substring(0, 2));
        int curMin = Integer.parseInt(curTime.substring(2, 4));
        int curSec = Integer.parseInt(curTime.substring(4, 6));
        float channelRvHeight = (float) mChannelRecyclerView.getHeight();
        float timeLinePosition = 0;
        if (START_TIME <= curClock && curClock < STANDARD_TIME) {
            timeLinePosition = (curClock - START_TIME) * dip2px(ONE_HOUR_UNIT) + (
                    dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec) + channelRvHeight;
        } else {
            if (0 <= curClock && curClock <= 3) {
                timeLinePosition = (STANDARD_TIME - START_TIME + curClock) * dip2px(ONE_HOUR_UNIT) + (
                        dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec) + channelRvHeight;
            }
        }
        mTimeLine.setY(timeLinePosition - (float) mNowImage.getHeight() / 2 - offset);
    }

    /**
     * 機能
     * 現在時刻ラインの表示位置を更新.
     */
    private void refreshTimeLine() {
        String curTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
        int curClock = Integer.parseInt(curTime.substring(0, 2));
        int curMin = Integer.parseInt(curTime.substring(2, 4));
        int curSec = Integer.parseInt(curTime.substring(4, 6));
        float channelRvHeight = (float) dip2px(CH_VIEW_HEIGHT);
        float timeLinePosition = 0;
        if (mTimeScrollView.getHeight() / dip2px(ONE_HOUR_UNIT) >= 3) {
            //タブレット(将来さらにチェック)
            if (START_TIME <= curClock && curClock < STANDARD_TIME) {
                timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec) + channelRvHeight;
            } else {
                if (0 <= curClock && curClock <= 3) {
                    //底から完全に見える"1時間単位"をマーナイスする
                    timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec)
                            + channelRvHeight + (mTimeScrollView.getHeight() - (START_TIME - curClock) * dip2px(ONE_HOUR_UNIT));
                }
            }
        } else {
            if (START_TIME <= curClock && curClock < STANDARD_TIME || curClock == 0 || curClock == 1) {
                timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec) + channelRvHeight;
            } else {
                if (2 <= curClock && curClock <= 3) {
                    //底から完全に見える"1時間単位"をマーナイスする
                    timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec)
                            + channelRvHeight + (mTimeScrollView.getHeight() - (START_TIME - curClock) * dip2px(ONE_HOUR_UNIT));
                }
            }
        }
        mTimeLine.setY(timeLinePosition - (float) mNowImage.getHeight() / 2);
    }


    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合はアプリ終了ダイアログを表示
                    showTips();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        //マイ番組表通信許可
        if (mMyChannelDataProvider != null) {
            mMyChannelDataProvider.enableConnect();
        }
        //番組表通信許可
        if (mScaledDownProgramListDataProvider != null) {
            mScaledDownProgramListDataProvider.enableConnect();
        }
        //サムネルタスク通信許可
        if (mTvProgramListAdapter != null) {
            mTvProgramListAdapter.enableConnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //マイ番組表通信を止める
        StopMyProgramListDataConnect stopMyConnect = new StopMyProgramListDataConnect();
        stopMyConnect.execute(mMyChannelDataProvider);
        //番組表通信を止める
        StopScaledProListDataConnect stopTvConnect = new StopScaledProListDataConnect();
        stopTvConnect.execute(mScaledDownProgramListDataProvider);
        //サムネルタスクを止める
        StopMyProgramListAdapterConnect stopAdapterConnect = new StopMyProgramListAdapterConnect();
        stopAdapterConnect.execute(mTvProgramListAdapter);
    }
}
