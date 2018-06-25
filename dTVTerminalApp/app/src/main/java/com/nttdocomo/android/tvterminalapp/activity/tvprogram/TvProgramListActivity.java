/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.utils.ViewUtils;
import com.nttdocomo.android.tvterminalapp.view.ProgramRecyclerView;
import com.nttdocomo.android.tvterminalapp.view.ProgramScrollView;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * 番組表表示Activity.
 */
public class TvProgramListActivity extends BaseActivity implements
        View.OnClickListener,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        ProgramScrollView.OnScrollOffsetListener,
        MyChannelDataProvider.ApiDataProviderCallback,
        TabItemLayout.OnClickTabTextListener {
    /**
     * hikariタブインデックス.
     */
    private static final int INDEX_TAB_HIKARI = 1;
    /**
     * マイチャンネルタブインデックス.
     */
    private int mMyChannelTabNo = -1;
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
     * 標準時間.
     */
    private static final int STANDARD_TIME = 24;
    /**
     * タイムライン幅.
     */
    private static final int TIME_LINE_WIDTH = 44;
    /**
     * タイムライン高さ.
     */
    private static final int TIME_LINE_HEIGHT = 56;
    /**
     * チャンネルリサイクルビュー丈.
     */
    private static final int CH_VIEW_HEIGHT = 44;
    /**
     * 1時間帯基準値.
     */
    private static final int ONE_HOUR_UNIT = 180;

    //時間を4時に設定する値
    private static final int HOUR_4 = 4;

    /**
     * 選択中日付.
     */
    private String mSelectDateStr = null;
    /**
     * 選択中日付(JAVAのエポック秒版).
     */
    private long mSelectDate = 0;
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
    /** 初回取得チャンネル数指定. **/
    private final static int FIRST_GET_CHANNEL_NUM = 3;
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
    private ArrayList<ChannelInfo> mHikariChannels;
    /**
     * マイ番組表にマッピングされたデータ.
     */
    private ArrayList<ChannelInfo> mMappedMyChannelList;
    /**
     * マイ番組表データ.
     */
    private final ArrayList<MyChannelMetaData> mMyChannelDataList = new ArrayList<>();
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
     * タブインデックス　マイ番組表.
     */
    private static final int TAB_INDEX_MY_PROGRAM = 0;
    /**
     * タブインデックス　ひかりTV for docomo.
     */
    private static final int TAB_INDEX_HIKARI = 1;
    /**
     * タブインデックス　dTVチャンネル.
     */
    private static final int TAB_INDEX_DTV = 2;
    /**
     * マイ番組表データプロバイダー.
     */
    private MyChannelDataProvider mMyChannelDataProvider;
    /**
     * 番組表データプロバイダー.
     */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;
    /**
     * スクロールオフセット.
     */
    private int mScrollOffset = 0;
    /**
     * 前回のタブポジション.
     */
    private final static String TAB_INDEX = "tabIndex";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_program_list_main_layout);

        //Headerの設定
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(true);
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
        if (savedInstanceState != null) {
            mTabIndex = savedInstanceState.getInt(TAB_INDEX);
            mTabLayout.setTab(mTabIndex);
            savedInstanceState.clear();
        }
        //タグ設定
        setTagView();
        //時間帯設定
        setLeftTimeContentsView();
        //タイトル設定
        setTitle();
        //チャンネルデータ取得
        getClipKeyList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //BG→FG復帰時に各アイテムのクリップ状態が変更されている可能性があるためonResumeのタイミングでチェックする
        if (mTvProgramListAdapter != null) {
            List<ChannelInfo> infoList = mTvProgramListAdapter.getProgramList();
            if (infoList != null) {
                List<ChannelInfo> list;
                list = mScaledDownProgramListDataProvider.checkTvProgramClipStatus(infoList);
                mTvProgramListAdapter.setProgramList(list);
                mTvProgramListAdapter.notifyDataSetChanged();
            }
        }
        sendScreenViewForPosition(mTabIndex);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_INDEX, mTabIndex);
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
        mNoDataMessage = findViewById(R.id.tv_program_list_no_items);

        mChannelRecyclerView.bringToFront();
        mTagImageView.bringToFront();
        mTagImageView.setOnClickListener(this);
        mTitleTextView.setOnClickListener(this);
        mTitleArrowImage.setOnClickListener(this);
        mTimeScrollView.setScrollView(programScrollView);
        programScrollView.setScrollView(mTimeScrollView);
        mProgramRecyclerView.setHasFixedSize(true);
        mProgramRecyclerView.setItemViewCacheSize(20);
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
        if (DateUtils.isLastDay()) {
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

        //日付選択カレンダーの選択日付をそのまま使用する
        mSelectDateStr = selectDate.toString();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYYMMDD, Locale.JAPAN);
        try {
            Date date = sdf.parse(mSelectDateStr);
            SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_YYYYMMDDE, Locale.JAPAN);
            String newDate = format.format(date.getTime());
            setTitleText(newDate.substring(5));
            SimpleDateFormat formatDialog = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD_J, Locale.JAPAN);
            mToDay = formatDialog.format(date.getTime());

            //NOW表示との比較用に文字列化前の日付を控えておく
            mSelectDate = date.getTime();
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
    }
    /**
     * 機能
     * タイトルの設定.
     */
    private void setTitle() {
        //フォーマットパターンを指定して表示する
        Calendar c = Calendar.getInstance();
        Locale.setDefault(Locale.JAPAN);
        if (DateUtils.isLastDay()) {
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYYMMDDE, Locale.JAPAN);
        mToDay = sdf.format(c.getTime());
        setTitleText(mToDay.substring(5));
        mSelectDateStr = DateUtils.getSystemTimeAndCheckHour(null);

        //NOW表示との比較用に文字列化前の日付を控えておく
        mSelectDate = c.getTimeInMillis();
    }

    /**
     * 機能
     * タブの設定.
     */
    private void setTabView() {
        DTVTLogger.start();
        UserState userState = UserInfoUtils.getUserState(getApplicationContext());
        //未ログイン状態ではマイ番組表タブを表示しない
        if (userState.equals(UserState.LOGIN_NG)) {
            mProgramTabNames = getResources().getStringArray(R.array.tv_program_list_no_login_tab_names);
        } else {
            mProgramTabNames = getResources().getStringArray(R.array.tv_program_list_tab_names);
        }
        //マイ番組表タブ位置取得
        mMyChannelTabNo = getMyChannelTabNo(mProgramTabNames);
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(getApplicationContext());
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mProgramTabNames, TabItemLayout.ActivityType.PROGRAM_LIST_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_tv_program_list_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mProgramTabNames);
        }
        DTVTLogger.end();
    }

    /**
     * マイ番組表タブの位置を取得する.
     * @param tabNames タブ名配列
     * @return マイ番組表タブ位置
     */
    private int getMyChannelTabNo(final String[] tabNames) {
        int tabNo = -1;
        for (int i = 0; i < tabNames.length; i++) {
            if (tabNames[i].equals(getString(R.string.common_my_channel))) {
                tabNo = i;
                break;
            }
        }
        return tabNo;
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (mTabIndex != position) {
            mTabIndex = position;
            sendScreenViewForPosition(position);
            clearData();
            getChannelData();
            DTVTLogger.end();
        }
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     *
     * @param position ポジション
     */
    private void sendScreenViewForPosition(final int position) {
        switch (position) {
            case TAB_INDEX_MY_PROGRAM:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_program_list_mine));
                break;
            case TAB_INDEX_HIKARI:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_program_list_h4d));
                break;
            case TAB_INDEX_DTV:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_program_list_dtv_channel));
                break;
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

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        programList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                mScrollOffset = dx;
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    channelList.stopScroll();
                    channelList.scrollBy(dx, dy);
                }
            }

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        if (mScrollOffset > 0) {
                            if (mTvProgramListAdapter != null) {
                                String dateStr = mSelectDateStr.replace("-", "");
                                String[] dateList = {dateStr};

                                int[] chList = mTvProgramListAdapter.getNeedProgramChannels();
                                if (chList != null && chList.length > 0) {
                                    mScaledDownProgramListDataProvider.getProgram(chList, dateList);
                                }
                                mScrollOffset = 0;
                            }
                        }
                        break;
                    default:
                        break;
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
                dip2px(TIME_LINE_HEIGHT));
        mTagImageView.setLayoutParams(layoutParams);
        mTagImageView.setImageResource(R.drawable.tv_program_list_cur_time_btn_selector);
    }

    /**
     * 機能
     * 時間軸を設定.
     */
    private void setLeftTimeContentsView() {
        //Timeコンテント設定
        mLinearLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(llLayoutParams);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setBackgroundColor(Color.BLACK);
        mTimeScrollView.addView(mLinearLayout);
        float density = getDensity();
        int lineWidth = dip2px(TIME_LINE_WIDTH);
        int oneHourUnit = dip2px(ONE_HOUR_UNIT);
        for (int i = DateUtils.START_TIME; i < STANDARD_TIME + DateUtils.START_TIME; i++) {
            TextView tabTextView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(lineWidth, oneHourUnit);
            tabTextView.setLayoutParams(params);
            int curTime = i;
            if (curTime >= STANDARD_TIME) {
                curTime = curTime - STANDARD_TIME;
            }
            tabTextView.setTextSize(COMPLEX_UNIT_DIP, 14);
            tabTextView.setText(String.valueOf(curTime));
            tabTextView.setPadding(0, (int) density * 8, 0, 0);
            tabTextView.setBackgroundColor(Color.BLACK);
            if (i == STANDARD_TIME +  DateUtils.START_TIME - 1) {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mChannelRecyclerView.setLayoutManager(linearLayoutManager);
        mProgramChannelAdapter = new ProgramChannelAdapter(this, getChannelNameList(channels));
        mTvProgramListAdapter = new TvProgramListAdapter(this, channels);
        mChannelRecyclerView.setAdapter(mProgramChannelAdapter);
        mProgramRecyclerView.setAdapter(mTvProgramListAdapter);
        scrollToCurTime();
        refreshTimeLine();
    }

    /**
     * 番組表に渡すチャンネル名リストを作成する.
     *
     * @param channels 番組情報
     * @return チャンネル名リスト.
     */
    private ArrayList<String> getChannelNameList(final ArrayList<ChannelInfo> channels) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < channels.size(); i++) {
            list.add(channels.get(i).getTitle());
        }
        return list;
    }

    /**
     * クリップキーリスト取得.
     */
    private void getClipKeyList() {
        DTVTLogger.start();
        mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        mScaledDownProgramListDataProvider.getClipKeyList();
        DTVTLogger.end();
    }

    @Override
    public void clipKeyResult() {
        super.clipKeyResult();
        getChannelData();
    }

    /**
     * 機能
     * チャンネルデータ取得.
     */
    private void getChannelData() {
        DTVTLogger.start();
        if (mTabIndex != mMyChannelTabNo) { //ひかり、dTVチャンネル
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            mScaledDownProgramListDataProvider.getChannelList(0, 0, "", mTabIndex);
        } else { //MY番組表
            mMyChannelDataProvider = new MyChannelDataProvider(this);
            mMyChannelDataProvider.getMyChannelList(R.layout.tv_program_list_main_layout);
        }
        DTVTLogger.end();
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
        DTVTLogger.start();
        if (channelInfo != null) {
            DTVTLogger.debug("channelInfo size:" + channelInfo.size());
            DTVTLogger.debug("channelInfo :" + channelInfo.toString());
            if (mProgramRecyclerView.getLayoutManager() == null) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mProgramRecyclerView.setLayoutManager(linearLayoutManager);
            }
            //以前のアダプタに設定されている値を取得して追加.
//        if (mTvProgramListAdapter != null) {
//            List<ChannelInfo> oldChannelInfo = mTvProgramListAdapter.getProgramList();
//            for (ChannelInfo oldChannel : oldChannelInfo) {
//                channelInfo.add(oldChannel);
//            }
//        }
            if (channelInfo.size() > 0) {
                mTvProgramListAdapter.setProgramList(channelInfo);
                mTvProgramListAdapter.notifyDataSetChanged();
            }
            //        mProgramRecyclerView.setAdapter(mTvProgramListAdapter);
            //スクロール時、リスナー設置
            mTimeScrollView.setOnScrollOffsetListener(this);
        }
        DTVTLogger.end();
    }

    /**
     * 機能
     * 番組表情報をクリア.
     */
    private void clearData() {
        mChannelRecyclerView.setAdapter(null);
        mChannelRecyclerView.removeAllViews();
        mChannelRecyclerView.removeAllViewsInLayout();
        //チャンネルリサイクルビュー配下のビューを解放されやすくする
        ViewUtils.cleanAllViews(mChannelRecyclerView);

        mProgramRecyclerView.setAdapter(null);
        mProgramRecyclerView.removeAllViews();
        mProgramRecyclerView.removeAllViewsInLayout();
        //配下のビューを解放されやすくする
        ViewUtils.cleanAllViews(mProgramRecyclerView);

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
            mProgramChannelAdapter.removeData();
            mProgramChannelAdapter = null;
        }
        if (mTvProgramListAdapter != null) {
            mTvProgramListAdapter.notifyDataSetChanged();
            mTvProgramListAdapter.removeData();
            mTvProgramListAdapter = null;
        }

        if (mHikariChannels != null) {
            mHikariChannels.clear();
        }

        if (mChannelInfo != null) {
            mChannelInfo.clear();
        }

        if (mScaledDownProgramListDataProvider != null) {
            mScaledDownProgramListDataProvider.clearData();
            mScaledDownProgramListDataProvider = null;
        }

        //各種操作でメモリーが解放されやすくなったはずなので、ガベージコレクションに期待する（2個連続は意図した通り）
        System.gc();
        System.gc();
    }

    /**
     * 機能
     * 現在時刻にスクロール.
     */
    private void scrollToCurTime() {
        String curTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
        int curClock = Integer.parseInt(curTime.substring(0, 2));
        int scrollDis;
        if (DateUtils.START_TIME <= curClock && curClock < STANDARD_TIME) {
            scrollDis = (curClock -  DateUtils.START_TIME) * mLinearLayout.getHeight() / STANDARD_TIME;
        } else {
            if (curClock == 0) {
                scrollDis = (STANDARD_TIME -  DateUtils.START_TIME) * mLinearLayout.getHeight() / STANDARD_TIME;
            } else if (curClock == 1) {
                scrollDis = (STANDARD_TIME -  DateUtils.START_TIME + 1) * mLinearLayout.getHeight() / STANDARD_TIME;
            } else {
                scrollDis = mLinearLayout.getHeight();
            }
        }
        mTimeScrollView.smoothScrollTo(0, scrollDis);
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        //TODO :★ここでAdaptor生成するのではなく、チャンネルリストが取得できた時点でAdaptorを生成してしまう。
        //TODO :★データの管理はAdaptor任せにして、必要なデータはAdaptorからActivity側に取得要求するようにする。
        //TODO :★生成されているAdaptorへ番組データを渡す処理にする。
        //TODO :★Adaptorはチャンネルリストに対して、取得した番組情報をMappingして溜めていく。
        //TODO :★ちなみに現状、上部のチャンネルの表示はチャンネルリストを元に表示しているが、
        //TODO :★ここで受け取るデータは番組のないチャンネルはデータ上含まれないのでチャンネルと番組欄にズレが起きる。
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (channelsInfo != null && channelsInfo.getChannels() != null) {
                    List<ChannelInfo> channels = channelsInfo.getChannels();
                    channelSort(channels);
                    mChannelInfo = channels;
                    setProgramRecyclerView(channels);
                }
            }
        });
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        //TODO :★ここでAdaptor生成する
        //TODO :★Adaptorはチャンネルリストに対して、取得した番組情報をMappingして溜めていく
        //TODO :★また初回として先頭○○チャンネル分だけ番組データをリクエストする。○○はRecyclerのキャッシュと同じ分
        if (mTabIndex == mMyChannelTabNo) {
            //MY番組表
            if (channels != null && channels.size() > 0) {
//                sort(channels);
                showMyChannelNoItem(false);
                this.mHikariChannels = channels;
                //TODO 作業(https://agile.apccloud.jp/jira/browse/DREM-2508)
                //TODO ↓のchannelListをDB保存
                ArrayList<ChannelInfo> channelList = DataConverter.executeMapping(mMyChannelDataList, mHikariChannels);
                setChannelContentsView(channelList);
                loadMyChannel(channelList);
            } else {
                //ひかりTVデータなしの場合
                scrollToCurTime();
                refreshTimeLine();
            }
        } else {
            //ひかり、dTVチャンネル
            if (channels != null && channels.size() > 0) {
//                sort(channels);
                showMyChannelNoItem(false);
                this.mChannels = channels;
                setChannelContentsView(mChannels);
                if (mScaledDownProgramListDataProvider == null) {
                    mScaledDownProgramListDataProvider
                            = new ScaledDownProgramListDataProvider(this);
                }
                int[] channelNos = new int[FIRST_GET_CHANNEL_NUM];
                int channelNum = FIRST_GET_CHANNEL_NUM;
                if (channels.size() < FIRST_GET_CHANNEL_NUM) {
                    channelNum = channels.size();
                }
                for (int i = 0; i < channelNum; i++) {
                    channelNos[i] = channels.get(i).getChannelNo();
                }
                String dateStr = mSelectDateStr.replace("-", "");
                String[] dateList = {dateStr};
                mScaledDownProgramListDataProvider.getProgram(channelNos, dateList);
            } else {
                //情報がヌルなので、ネットワークエラーメッセージを取得する
                ErrorState errorState = mScaledDownProgramListDataProvider.getChannelError();
                if (errorState != null) {
                    String message = errorState.getErrorMessage();
                    //メッセージの有無で処理を分ける
                    if (!TextUtils.isEmpty(message)) {
                        showGetDataFailedToast(message);
                    }
                }
                mNoDataMessage.setVisibility(View.VISIBLE);
                scrollToCurTime();
                refreshTimeLine();
            }
        }
        DTVTLogger.end();
    }

    //TODO 作業箇所(https://agile.apccloud.jp/jira/browse/DREM-2508)
    /**
     * My番組表ロード.
     * @param channelList チャンネルリスト
     */
    private void loadMyChannel(final ArrayList<ChannelInfo> channelList) {
        if (mScaledDownProgramListDataProvider == null) {
            mScaledDownProgramListDataProvider =
                    new ScaledDownProgramListDataProvider(this);
        }
        int[] channelNos = new int[channelList.size()];
        for (int i = 0; i < channelList.size(); i++) {
            channelNos[i] = channelList.get(i).getChannelNo();
        }
        //TODO 作業(https://agile.apccloud.jp/jira/browse/DREM-2508)
        //TODO 期限見てDBからデータ取得処理
        if (channelNos.length != 0) {
            //マイ番組表設定されていない場合、通信しない
            String dateStr = mSelectDateStr.replace("-", "");
            String[] dateList = {dateStr};
            mScaledDownProgramListDataProvider.getProgram(channelNos, dateList);
        } else {
            //「マイ番組が設定されていません」と表示される
            if (mTabIndex == mMyChannelTabNo) {
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
            mNoDataMessage.setVisibility(View.GONE);
        } else {
            mTimeScrollView.setVisibility(View.VISIBLE);
            mTagImageView.setVisibility(View.VISIBLE);
            mTimeLine.setVisibility(View.VISIBLE);
            mMyChannelNoDataTxT.setVisibility(View.INVISIBLE);
        }

        //端末に設定された日時と現在番組表で設定されている日時を比較する
        if(!compareNowDate(mSelectDate)) {
            //違っていたので、これまでの条件とは無関係に、NOW表示は透明にする
            mTimeLine.setVisibility(View.INVISIBLE);

            //NOWボタンも非表示にする(レイアウト側で消える仕組みになっている)
            mTagImageView.setEnabled(false);
        } else {
            //NOWボタンを表示する
            mTagImageView.setEnabled(true);
        }

    }

    /**
     * 指定された日付が、現在の日付と一致するかどうかを見る.
     *
     * ただし、番組表の表示範囲を考慮して、指定日の午前4時から次の日の午前3時59分までの範囲かどうかの比較となる
     * @param compareDate Javaのエポック秒
     * @return 指定した日付と現在の日付が一致すればtrue
     */
    boolean compareNowDate(final long compareDate) {
        //現在の日時を取得する
        Calendar calendar = Calendar.getInstance();
        Long nowTime = calendar.getTimeInMillis();

        //比較開始日時（指定された日の午前4時のJavaのエポック秒）
        calendar.setTimeInMillis(compareDate);
        calendar.set(Calendar.HOUR_OF_DAY,HOUR_4);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long startTime = calendar.getTimeInMillis();

        //1日加算後に1ミリ秒減算する事で、比較終了日時である翌日の3時59分になる
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.MILLISECOND,-1);

        //比較終了日時（指定された日の次の日の3時59分のJavaのエポック秒）
        long endTime = calendar.getTimeInMillis();

        //比較して結果を返す
        if(startTime <= nowTime && endTime >= nowTime) {
            //指定された時間が本日の午前4時から明日の3時59分の間なので、true
            return true;
        }

        //指定時間はNOWの範囲外なのでfalse
        return false;
    }

    /**
     * MY番組表情報取得.
     *
     * @param myChannelMetaData 画面に渡すチャンネル番組情報
     */
    @Override
    public void onMyChannelListCallback(final ArrayList<MyChannelMetaData> myChannelMetaData) {
        this.mMyChannelDataList.clear();
        if (myChannelMetaData != null && myChannelMetaData.size() > 0) {
            // アプリとして編集が可能な index16 までで絞る(サーバからはindex順でレスポンスが来ているのでソートはしない)
            for (int j = 0; j < myChannelMetaData.size(); j++) {
                String index = myChannelMetaData.get(j).getIndex();
                if (index != null && !index.isEmpty()) {
                    int indexNum = 0;
                    try {
                        indexNum = Integer.parseInt(index);
                    } catch (NumberFormatException e) {
                        DTVTLogger.debug("MyChannel index is incorrect");
                        DTVTLogger.debug(e);
                        continue;
                    }
                    if (1 <= indexNum && indexNum <= WebApiBasePlala.MY_CHANNEL_MAX_INDEX) {
                        this.mMyChannelDataList.add(myChannelMetaData.get(j));
                    }
                }
            }
            //ひかりリストからチャンネル探すため
            if (mScaledDownProgramListDataProvider == null) {
                mScaledDownProgramListDataProvider =
                        new ScaledDownProgramListDataProvider(this);
            }
            mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
        } else {
            //情報がヌルなので、ネットワークエラーメッセージを取得する
            ErrorState errorState;
            if (mScaledDownProgramListDataProvider != null) {
                errorState = mScaledDownProgramListDataProvider.getChannelError();
            } else {
                errorState = mMyChannelDataProvider.getMyChannelListError();
            }
            if (errorState != null) {
                String message = errorState.getErrorMessage();
                //メッセージの有無で処理を分ける
                if (!TextUtils.isEmpty(message)) {
                    showGetDataFailedToast(message);
                }
            }
            //MY番組表情報がなければMY番組表を設定していないとする(データないので、特にタイムライン表示必要がない)
            if (mTabIndex == mMyChannelTabNo) {
                showMyChannelNoItem(true);
            } else {
                mNoDataMessage.setVisibility(View.VISIBLE);
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
        if (DateUtils.START_TIME <= curClock && curClock < STANDARD_TIME) {
            timeLinePosition = (curClock -  DateUtils.START_TIME) * dip2px(ONE_HOUR_UNIT) + (
                    dip2px(ONE_HOUR_UNIT) + 0.5f) *  DateUtils.minSec2Hour(curMin, curSec) + channelRvHeight;
        } else {
            if (0 <= curClock && curClock <= 3) {
                timeLinePosition = (STANDARD_TIME -  DateUtils.START_TIME + curClock) * dip2px(ONE_HOUR_UNIT) + (
                        dip2px(ONE_HOUR_UNIT) + 0.5f) *  DateUtils.minSec2Hour(curMin, curSec) + channelRvHeight;
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
            if (DateUtils.START_TIME <= curClock && curClock < STANDARD_TIME) {
                timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * DateUtils.minSec2Hour(curMin, curSec) + channelRvHeight;
            } else {
                if (0 <= curClock && curClock <= 3) {
                    //底から完全に見える"1時間単位"をマーナイスする
                    timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) *  DateUtils.minSec2Hour(curMin, curSec)
                            + channelRvHeight + (mTimeScrollView.getHeight() - (DateUtils.START_TIME - curClock) * dip2px(ONE_HOUR_UNIT));
                }
            }
        } else {
            if (DateUtils.START_TIME <= curClock && curClock < STANDARD_TIME || curClock == 0 || curClock == 1) {
                timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) *  DateUtils.minSec2Hour(curMin, curSec) + channelRvHeight;
            } else {
                if (2 <= curClock && curClock <= 3) {
                    //底から完全に見える"1時間単位"をマーナイスする
                    timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) *  DateUtils.minSec2Hour(curMin, curSec)
                            + channelRvHeight + (mTimeScrollView.getHeight() - (DateUtils.START_TIME - curClock) * dip2px(ONE_HOUR_UNIT));
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
                    //メニューから起動の場合ホーム画面に戻る
                    contentsDetailBackKey(null);
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
        stopMyConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMyChannelDataProvider);
        //番組表通信を止める
        StopScaledProListDataConnect stopTvConnect = new StopScaledProListDataConnect();
        stopTvConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mScaledDownProgramListDataProvider);
        //サムネルタスクを止める
        StopMyProgramListAdapterConnect stopAdapterConnect = new StopMyProgramListAdapterConnect();
        stopAdapterConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mTvProgramListAdapter);
    }
}
