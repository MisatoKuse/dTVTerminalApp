/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListAdapterConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopMyProgramListDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
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
     * マイチャンネルタブインデックス.
     */
    private int mMyChannelTabNo = -1;
    /**
     * 番組パネルリサイクルビュー.
     */
    private ProgramRecyclerView mProgramRecyclerView = null;
    /**
     *  番組パネルスクロールビュー.
     */
    private ProgramScrollView mProgramScrollViewParent = null;
    /**
     * メニュー起動.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * マイ番組表非表示フラグ(未ログイン又は未契約状態).
     */
    private boolean mIsDisableMyChannel = false;
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
     * ひかりチャンネルリスト.
     */
    private ArrayList<ChannelInfo> mHikariChannels = null;
    /**
     * マイ番組表データ.
     */
    private ArrayList<MyChannelMetaData> mMyChannelDataList = null;
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
    private static final int TAB_INDEX_DTVCH = 2;
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
     * 前回のタブポジション.
     */
    private final static String TAB_INDEX = "tabIndex";
    /**
     * 番組RecyclerViewキャッシュサイズ.多いとViewが残りやすいがメモリを消費する.
     */
    private final static int PROGRAM_RECYCLER_CACHE_SIZE = 12;
    /**
     * 番組RecyclerView PreDraw領域比率(ディスプレイ横幅に対する倍数).増やすと先読み数が増えるが重くなる.
     */
    private final static float PROGRAM_RECYCLER_EXTRA_WIDTH = 1.5f;
    /**
     * NOWバーの時刻.
     */
    String mNowCurTime = null;

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
        //タイトル設定
        setTitle();
        //チャンネルデータ取得
        getClipKeyList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        enableStbStatusIcon(true);
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
        DTVTLogger.end();
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

        //時間軸表示
        mTimeScrollView = findViewById(R.id.tv_program_list_main_layout_time_sl);
        setLeftTimeContentsView();

        //チャンネル表示.
        mChannelRecyclerView = findViewById(R.id.tv_program_list_main_layout_channel_rv);

        //番組表表示.
        mProgramRecyclerView = findViewById(R.id.tv_program_list_main_layout_channeldetail_rv);
        mProgramScrollViewParent = findViewById(R.id.tv_program_list_main_layout_channeldetail_sl);

        //縦軸を相互にスクロール同期.
        mTimeScrollView.setScrollView(mProgramScrollViewParent);
        mProgramScrollViewParent.setScrollView(mTimeScrollView);

        mProgramRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // 初期表示Y位置を設定.
                mProgramScrollViewParent.getViewTreeObserver().removeOnPreDrawListener(this);
                int offsetY = calcCurTimeOffsetY();
                mTimeScrollView.setScrollY(offsetY);
                mProgramScrollViewParent.setScrollY(offsetY);
                mProgramRecyclerView.scrollTo(0, offsetY);
                refreshTimeLine();
                return false;
            }
        });

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
        mProgramRecyclerView.setHasFixedSize(true);
        mProgramRecyclerView.setItemViewCacheSize(PROGRAM_RECYCLER_CACHE_SIZE);

        mProgramScrollViewParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                MotionEvent e = MotionEvent.obtain(motionEvent);
                e.setLocation(e.getX() + mProgramScrollViewParent.getScrollX(),
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
                // NOWバー位置更新
                mNowCurTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
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
        mIsDisableMyChannel = userState.equals(UserState.LOGIN_NG) || userState.equals(UserState.LOGIN_OK_CONTRACT_NG);
        //未ログイン状態ではマイ番組表タブを表示しない
        if (mIsDisableMyChannel) {
            mProgramTabNames = getResources().getStringArray(R.array.tv_program_list_no_login_tab_names);
        } else {
            mProgramTabNames = getResources().getStringArray(R.array.tv_program_list_tab_names);
        }
        //マイ番組表タブ位置取得
        mMyChannelTabNo = getMyChannelTabNo(mProgramTabNames);
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
            cancelDataProvider();
            mTabIndex = position;
            // NOWバー位置更新
            mNowCurTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
            sendScreenViewForPosition(position);
            clearData();
            //dTVチャンネルタブ以外が選択された際、過去日付を表示していたら現在日付に補正する
            if(position != TAB_INDEX_DTVCH) {
                if (compareNowDate(mSelectDate) < 0) {
                    //タイトル再設定
                    setTitle();
                }
            }

            // 現在時刻への移動(アニメあり).スクロール中にタブ切替すると慣性スクロールが止まらず
            // setScrollYしてもかき消されるのでアニメありスクロールで上書きする.
            int offsetY = calcCurTimeOffsetY();
            mTimeScrollView.smoothScrollTo(0, offsetY);
            mProgramScrollViewParent.smoothScrollTo(0, offsetY);

            getChannelData();
        }
        DTVTLogger.end();
    }

    /**
     * DataProviderキャンセル処理.
     */
    private void cancelDataProvider() {
        DTVTLogger.start();
        if (mMyChannelDataProvider != null) {
            //データプロバイダーキャンセル処理
            mMyChannelDataProvider.stopConnect();
            mMyChannelDataProvider.setApiDataProviderCallback(null);
            //キャンセル後に mMyChannelDataProvider の使いまわしを防ぐため null を設定
            mMyChannelDataProvider = null;
        }
        if (mScaledDownProgramListDataProvider != null) {
            //データプロバイダーキャンセル処理
            mScaledDownProgramListDataProvider.stopConnect();
            mScaledDownProgramListDataProvider.setApiDataProviderCallback(null);
            //キャンセル後に mScaledDownProgramListDataProvider の使いまわしを防ぐため null を設定
            mScaledDownProgramListDataProvider = null;
        }
        DTVTLogger.end();
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
            case TAB_INDEX_DTVCH:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_program_list_dtv_channel));
                break;
            default:
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
                setProgramData(newState);
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

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                setProgramData(newState);
            }
        });
    }
    /**
     * 番組表データを画面に反映する.
     * @param newState　スクロール終わった状態
     */
    private void setProgramData(final int newState) {
        DTVTLogger.start("ScroolState:" + newState);
        switch (newState) {
            case SCROLL_STATE_IDLE:
                if (mTvProgramListAdapter != null) {
                    String dateStr = mSelectDateStr.replace("-", "");
                    String[] dateList = {dateStr};
                    int[] chList = mTvProgramListAdapter.getNeedProgramChannels();
                    if (chList != null && chList.length > 0) {
                        mScaledDownProgramListDataProvider.getProgram(chList, dateList, true);
                    }
                }
                break;
            default:
                break;
        }
        DTVTLogger.end();
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
            TextView tabTextView = new TextView(this);
            tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
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
        mTvProgramListAdapter = new TvProgramListAdapter(this, channels, mSelectDateStr);
        mChannelRecyclerView.setAdapter(mProgramChannelAdapter);
        mProgramRecyclerView.setAdapter(mTvProgramListAdapter);
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
            mScaledDownProgramListDataProvider.getChannelList(0, 0, "", typeConverter(mTabIndex));
        } else { //MY番組表
            mMyChannelDataProvider = new MyChannelDataProvider(this);
            mMyChannelDataProvider.getMyChannelList(R.layout.tv_program_list_main_layout);
        }
        DTVTLogger.end();
    }

    /**
     * 未ログイン時のリクエストパラメータ変換.
     *
     * @param tabIndex タブポジション
     * @return 変換後のタブポジション
     */
    private int typeConverter(final int tabIndex) {
        DTVTLogger.start();
        int index = tabIndex;
        UserState userState = UserInfoUtils.getUserState(getApplicationContext());
        if (mIsDisableMyChannel) {
            index++;
        }
        return index;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_program_list_main_layout_curtime_iv:
                if (!mSelectDateStr.equals(DateUtils.getSystemTimeAndCheckHour(null))) {
                    setTitle();
                    clearData();
                    getChannelData();
                }
                //現在のスクロール位置をスクロール位置に指定して、縦スクロールを止める(ScrollToでは止まらない)
                mProgramScrollViewParent.smoothScrollTo(
                        mProgramScrollViewParent.getScrollX(),
                        mProgramScrollViewParent.getScrollY());

                //横スクロールを止める
                mChannelRecyclerView.stopScroll();
                mProgramRecyclerView.stopScroll();

                //システム時間軸にスクロール
                scrollToCurTime(true);
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
                PreLoadLayoutManager preLoadLayoutManager = new PreLoadLayoutManager(
                        getApplicationContext());
                preLoadLayoutManager.setOrientation(PreLoadLayoutManager.HORIZONTAL);
                mProgramRecyclerView.setLayoutManager(preLoadLayoutManager);
            }
            if (channelInfo.size() > 0) {
                mTvProgramListAdapter.setProgramList(channelInfo);
            }
            //スクロール時、リスナー設置
            mTimeScrollView.setOnScrollOffsetListener(this);
        }
        DTVTLogger.end();
    }

    /**
     * 番組RecyclerView用の横幅拡張レイアウトManager（getExtraLayoutSpaceでの幅指定分PreLoadするようになる）.
     */
    public class PreLoadLayoutManager extends LinearLayoutManager {

        private final int mDisplayWidth;

        public PreLoadLayoutManager(Context context) {
            super(context);
            mDisplayWidth = (int) (context.getResources().getDisplayMetrics().widthPixels * PROGRAM_RECYCLER_EXTRA_WIDTH);
        }

        @Override
        protected int getExtraLayoutSpace(RecyclerView.State state) {
            return mDisplayWidth;
        }
    }

    /**
     * 機能
     * 番組表情報をクリア.
     */
    private void clearData() {
        DTVTLogger.start();
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

        if (mScaledDownProgramListDataProvider != null) {
            mScaledDownProgramListDataProvider.clearData();
            mScaledDownProgramListDataProvider = null;
        }

        //各種操作でメモリーが解放されやすくなったはずなので、ガベージコレクションに期待する（2個連続は意図した通り）
        System.gc();
        System.gc();
        DTVTLogger.end();
    }

    /**
     * 機能
     * 現在時刻にスクロール.
     *
     * @param programSwitch 番組表本体のスクロールも行うならばtrue
     */
    private void scrollToCurTime(final boolean programSwitch) {
        int scrollDis = calcCurTimeOffsetY();
        mTimeScrollView.smoothScrollTo(0, scrollDis);
        if (programSwitch) {
            //NOWボタンを押した際は、番組表本体のスクロール位置も設定しないと、失敗する場合がある
            mProgramScrollViewParent.smoothScrollTo(0, scrollDis);
        }
    }

    /**
     * 現在時刻(Hour単位、分は切り捨て)の位置を求める.
     *
     * @return  現在時刻のY位置
     */
    private int calcCurTimeOffsetY() {
        String curTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
        int curClock = Integer.parseInt(curTime.substring(0, 2));
        int scrollDis = 0;
        if (DateUtils.START_TIME <= curClock && curClock < STANDARD_TIME) {
            scrollDis = (curClock -  DateUtils.START_TIME) * mLinearLayout.getHeight() / STANDARD_TIME;
        } else if (curClock <= 3) {
            scrollDis = (STANDARD_TIME -  DateUtils.START_TIME + curClock) * mLinearLayout.getHeight() / STANDARD_TIME;
        }
        DTVTLogger.debug("scrollDis = " + scrollDis);
        return scrollDis;
    }

    /**
     *  現在時刻にスクロール.
     *
     *  既存実装との互換性を取る為の物
     */
    private void scrollToCurTime() {
        //引数無しで呼ばれた場合はfalseにする
        scrollToCurTime(false);
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final int[] chNo) {
        //ここでAdaptor生成するのではなく、チャンネルリストが取得できた時点でAdaptorを生成してしまう。
        //★データの管理はAdaptor任せにして、必要なデータはAdaptorからActivity側に取得要求するようにする。
        //★生成されているAdaptorへ番組データを渡す処理にする。
        //★Adaptorはチャンネルリストに対して、取得した番組情報をMappingして溜めていく。
        //★ここで受け取るデータは現状、番組のないチャンネルはデータ上含まれない。
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (channelsInfo != null && channelsInfo.getChannels() != null) {
                    List<ChannelInfo> channels = channelsInfo.getChannels();
                    channelSort(channels);
                    setProgramRecyclerView(channels);
                } else {
                    //Nullの時のダミーデータ生成
                    if (chNo.length > 0) {
                        ChannelInfoList channelsInfoList = new ChannelInfoList();
                        for (int aChNo : chNo) {
                            ArrayList<ScheduleInfo> scheduleInfoList = new ArrayList<>();
                            ChannelInfo channel = new ChannelInfo();
                            //チャンネル番号がある時のみデータを生成する
                            ScheduleInfo mSchedule = DataConverter.convertScheduleInfo(DataConverter.getDummyContentMap(getApplicationContext(), String.valueOf(aChNo), true), null);
                            scheduleInfoList.add(mSchedule);
                            channel.setChannelNo(aChNo);
                            channel.setTitle(mSchedule.getTitle());
                            channel.setSchedules(scheduleInfoList);
                            channelsInfoList.addChannel(channel);
                        }
                        List<ChannelInfo> channels = channelsInfoList.getChannels();
                        setProgramRecyclerView(channels);
                    }
                }
            }
        });
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        //★ここでAdaptor生成する
        //★Adaptorはチャンネルリストに対して、取得した番組情報をMappingして溜めていく
        //★また初回として先頭○○チャンネル分だけ番組データをリクエストする。○○はRecyclerのキャッシュと同じ分
        if (mTabIndex == mMyChannelTabNo) {
            //MY番組表
            if (channels != null && channels.size() > 0) {
//                sort(channels);
                showMyChannelNoItem(false, false);
                this.mHikariChannels = channels;
                //TODO 作業(https://agile.apccloud.jp/jira/browse/DREM-2508)
                //TODO ↓のchannelListをDB保存
                ArrayList<ChannelInfo> channelList = DataConverter.executeMapping(mMyChannelDataList, mHikariChannels);
                setChannelContentsView(channelList);
                loadMyChannel(channelList);
            } else {
                //ひかりTVデータなしの場合

            }
        } else {
            //ひかり、dTVチャンネル
            if (channels != null && channels.size() > 0) {
//                sort(channels);
                showMyChannelNoItem(false, false);
                setChannelContentsView(channels);
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
                    DTVTLogger.debug("###set channelList ChNo:" + channels.get(i).getChannelNo() + " ChName:" + channels.get(i).getTitle());
                }
                String dateStr = mSelectDateStr.replace("-", "");
                String[] dateList = {dateStr};
                mScaledDownProgramListDataProvider.getProgram(channelNos, dateList, true);
                //明示的にコンテンツ取得失敗メッセージを非表示にする
                mNoDataMessage.setVisibility(View.GONE);
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
                if (mProgramChannelAdapter == null) {
                    //この時点でチャンネル名表示Adapterが生成できていない場合はコンテンツ取得失敗メッセージを表示
                    mNoDataMessage.setVisibility(View.VISIBLE);
                } else {
                    ArrayList<String> displayChannels = mProgramChannelAdapter.getChannelList();
                    //既に表示されているチャンネルリストがない場合のみコンテンツ取得失敗メッセージを表示
                    if (displayChannels != null || displayChannels.size() < 1) {
                        mNoDataMessage.setVisibility(View.VISIBLE);
                    } else {
                        mNoDataMessage.setVisibility(View.GONE);
                    }
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * My番組表ロード.
     * @param channelList チャンネルリスト
     */
    private void loadMyChannel(final ArrayList<ChannelInfo> channelList) {
        DTVTLogger.start();
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
            mScaledDownProgramListDataProvider.getProgram(channelNos, dateList, true);
        }
        DTVTLogger.end();
    }

    /**
     * マイ番組表Noチャンネル表示.
     *
     * @param isShowFlag マイ番組表の要素表示フラグ.
     * @param isZeroData 0件取得フラグ.
     */
    private void showMyChannelNoItem(final boolean isShowFlag, final boolean isZeroData) {
        DTVTLogger.start();
        //一部の処理で別スレッドで実行されたことによるエラーが発生していた。UIスレッドへ処理を移譲する
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShowFlag) {
                    mTimeScrollView.setVisibility(View.INVISIBLE);
                    mTagImageView.setVisibility(View.INVISIBLE);
                    mTimeLine.setVisibility(View.INVISIBLE);
                    //0件取得か否かで表示メッセージを変更する
                    if (isZeroData) {
                        mMyChannelNoDataTxT.setText(getString(R.string.tv_program_tip));
                    } else {
                        mMyChannelNoDataTxT.setText(getString(R.string.common_failed_data_message));
                    }
                    mMyChannelNoDataTxT.setVisibility(View.VISIBLE);
                    mNoDataMessage.setVisibility(View.GONE);
                } else {
                    mTimeScrollView.setVisibility(View.VISIBLE);
                    mTagImageView.setVisibility(View.VISIBLE);
                    mTimeLine.setVisibility(View.VISIBLE);
                    mMyChannelNoDataTxT.setVisibility(View.INVISIBLE);
                }

                //端末に設定された日時と現在番組表で設定されている日時を比較する
                if (compareNowDate(mSelectDate) != 0) {
                    //違っていたので、これまでの条件とは無関係に、NOW表示は透明にする
                    mTimeLine.setVisibility(View.INVISIBLE);

                    //NOWボタンも非表示にする(レイアウト側で消える仕組みになっている)
                    mTagImageView.setEnabled(false);
                } else {
                    //NOWボタンを表示する
                    mTagImageView.setEnabled(true);
                }
            }
        });
        DTVTLogger.end();
    }

    /**
     * 指定された日付が、現在の日付と一致するかどうかを見る.
     *
     * ただし、番組表の表示範囲を考慮して、指定日の午前4時から次の日の午前3時59分までの範囲かどうかの比較となる
     * @param compareDate Javaのエポック秒
     * @return 指定した日付と現在の日付が一致すれば0、そうでない場合は指定時刻の日付4:00と現在時刻の差分ミリ秒
     * 　　　　 (過去の場合はマイナス値、未来の場合はプラス値になる)
     */
    private long compareNowDate(final long compareDate) {
        //現在の日時を取得する
        Calendar calendar = Calendar.getInstance();
        Long nowTime = calendar.getTimeInMillis();

        //比較開始日時（指定された日の午前4時のJavaのエポック秒）
        calendar.setTimeInMillis(compareDate);
        calendar.set(Calendar.HOUR_OF_DAY, HOUR_4);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        //1日加算後に1ミリ秒減算する事で、比較終了日時である翌日の3時59分になる
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);

        //比較終了日時（指定された日の次の日の3時59分のJavaのエポック秒）
        long endTime = calendar.getTimeInMillis();

        if (startTime > nowTime) {
            //現在日より未来の日付なら差分（プラス値）
            return (startTime - nowTime);
        } else if ( endTime < nowTime) {
            //現在日より過去の日付なら差分（マイナス値）
            return (endTime - nowTime);
        } else {
            //指定された時間が本日の午前4時から明日の3時59分の間なので、当日(0)
            return 0;
        }
    }

    /**
     * MY番組表情報取得.
     *
     * @param myChannelMetaData 画面に渡すチャンネル番組情報
     */
    @Override
    public void onMyChannelListCallback(final ArrayList<MyChannelMetaData> myChannelMetaData) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myChannelMetaData != null && myChannelMetaData.size() > 0) {
                    showMyChannelNoItem(false, false);
                    // アプリとして編集が可能な index16 までで絞る(サーバからはindex順でレスポンスが来ているのでソートはしない)
                    mMyChannelDataList = new ArrayList<>();
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
                                mMyChannelDataList.add(myChannelMetaData.get(j));
                            }
                        }
                    }
                    //ひかりリストからチャンネル探すため
                    if (mScaledDownProgramListDataProvider == null) {
                        mScaledDownProgramListDataProvider =
                                new ScaledDownProgramListDataProvider(TvProgramListActivity.this);
                    }
                    mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
                } else if (myChannelMetaData == null) {
                    noMyChannelDataDisplay(false);
                } else {
                    //0件取得の場合はフラグを立てる
                    noMyChannelDataDisplay(true);
                }
            }
        });
        DTVTLogger.end();
    }

    /**
     * マイ番組表取得失敗時の表示.
     *
     * @param isZeroData 0件フラグ(正常取得時)
     */
    private void noMyChannelDataDisplay(final boolean isZeroData) {
        DTVTLogger.start();
        //情報がヌルなので、ネットワークエラーメッセージを取得する
        ErrorState errorState;
        if (mScaledDownProgramListDataProvider != null) {
            errorState = mScaledDownProgramListDataProvider.getChannelError();

            //縮小番組表側のエラー情報が取得できない場合は、マイチャンネル側のエラーを取得する
            if (errorState == null && mMyChannelDataProvider != null) {
                errorState = mMyChannelDataProvider.getMyChannelListError();
            }
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
            showMyChannelNoItem(true, isZeroData);
        } else {
            mNoDataMessage.setVisibility(View.VISIBLE);
        }
        DTVTLogger.end();
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
        if (mNowCurTime != null) {
            float timeLinePosition = calcTimeLinePosition(mNowCurTime);
//            DTVTLogger.debug("onScrollOffset timeLinePosition:" + timeLinePosition + " mNowImage.getHeight():" + mNowImage.getHeight() + " offset:" + offset);
            mTimeLine.setY(timeLinePosition - offset);
        }
        if(mTvProgramListAdapter != null) {
            mTvProgramListAdapter.setProgramScrollY(mProgramScrollViewParent.getScrollY());
        }
    }

    /**
     * NOWバー位置の計算.
     *
     * @param date NOWを描画したい時刻
     */
    private float calcTimeLinePosition(String date) {
        int curClock = Integer.parseInt(date.substring(0, 2));
        int curMin = Integer.parseInt(date.substring(2, 4));
        int curSec = Integer.parseInt(date.substring(4, 6));
        float timeLinePosition = 0;
        float channelRvHeight = (float) dip2px(CH_VIEW_HEIGHT);
        mTimeScrollView.getScrollY();

        if (DateUtils.START_TIME <= curClock && curClock < STANDARD_TIME) {
            timeLinePosition = (curClock -  DateUtils.START_TIME) * dip2px(ONE_HOUR_UNIT) + (
                    dip2px(ONE_HOUR_UNIT)) *  DateUtils.minSec2Hour(curMin, 0) + channelRvHeight;
        } else if (0 <= curClock && curClock <= 3) {
            timeLinePosition = (STANDARD_TIME -  DateUtils.START_TIME + curClock) * dip2px(ONE_HOUR_UNIT) + (
                    dip2px(ONE_HOUR_UNIT)) *  DateUtils.minSec2Hour(curMin, 0) + channelRvHeight;
        }
        return timeLinePosition;
    }

    /**
     * 機能
     * 現在時刻ラインの表示位置を更新.
     */
    private void refreshTimeLine() {
        mNowCurTime = new SimpleDateFormat(DateUtils.DATE_HHMMSS, Locale.JAPAN).format(new Date());
        float timeLinePosition = calcTimeLinePosition(mNowCurTime);
        DTVTLogger.debug("onScrollOffset timeLinePosition:" + timeLinePosition + "mTimeScrollView.getScrollY:" + mTimeScrollView.getScrollY());
        mTimeLine.setY(timeLinePosition - mTimeScrollView.getScrollY());
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
        DTVTLogger.start();
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

        DTVTLogger.end();
    }

    @Override
    protected void onReStartCommunication() {
        super.onReStartCommunication();
        //チャンネル情報未取得なら取得を動作させる
        if (mTabIndex == 0 && mMyChannelDataList == null) {
            getChannelData();
        } else if (mHikariChannels == null || mHikariChannels.size() == 0) {
            getChannelData();
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
        DTVTLogger.end();
    }

    @Override
    public void onClipRegistResult() {
        DTVTLogger.start();
        ClipRequestData clipRequestData = getClipRequestData();
        ImageView clipButton = getClipButton();
        if (clipRequestData != null && clipButton != null) {
            //DB登録開始
            ClipKeyListDataProvider clipKeyListDataProvider = new ClipKeyListDataProvider(this);
            clipKeyListDataProvider.clipResultInsert(clipRequestData);

            if ((DateUtils.getHyphenEpochTime(clipRequestData.getLinearEndDate()) < DateUtils.getNowTimeFormatEpoch())
                    && (clipButton.getTag() != null && clipButton.getTag().equals(CLIP_SCHEDULE_END_OPACITY_STATUS))) {
                // 放送が終了しているためクリップボタンを放送終了用アイコンに変更
                clipButton.setBackgroundResource(R.drawable.tv_program_schedule_end_clip_active_selector);
                clipButton.setTag(CLIP_SCHEDULE_END_ACTIVE_STATUS);
            } else {
                clipButton.setBackgroundResource(R.drawable.common_clip_active_selector);
                clipButton.setTag(CLIP_ACTIVE_STATUS);
            }
            showClipToast(R.string.clip_regist_result_message);
        } else {
            // フェールセーフで元処理に戻す
            super.onClipRegistResult();
        }
        DTVTLogger.end();
    }

    @Override
    public void onClipDeleteResult() {
        DTVTLogger.start();
        ClipRequestData clipRequestData = getClipRequestData();
        ImageView clipButton = getClipButton();
        if (clipRequestData != null && clipButton != null) {
            //DB削除開始
            ClipKeyListDataProvider clipKeyListDataProvider = new ClipKeyListDataProvider(this);
            clipKeyListDataProvider.clipResultDelete(clipRequestData);

            if ((DateUtils.getHyphenEpochTime(clipRequestData.getLinearEndDate()) < DateUtils.getNowTimeFormatEpoch())
                    && (clipButton.getTag() != null && clipButton.getTag().equals(CLIP_SCHEDULE_END_ACTIVE_STATUS))) {
                // 放送が終了しているためクリップボタンを放送終了用アイコンに変更
                clipButton.setBackgroundResource(R.drawable.tv_program_schedule_end_clip_normal_selector);
                clipButton.setTag(CLIP_SCHEDULE_END_OPACITY_STATUS);
            } else {
                clipButton.setBackgroundResource(R.drawable.tv_program_schedule_clip_normal_selector);
                clipButton.setTag(CLIP_OPACITY_STATUS);
            }
            showClipToast(R.string.clip_delete_result_message);
        } else {
            // フェールセーフで元処理に戻す
            super.onClipDeleteResult();
        }
        DTVTLogger.end();
    }
}
