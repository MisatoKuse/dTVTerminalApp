/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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
import com.nttdocomo.android.tvterminalapp.activity.detail.ChannelDetailPlayerActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ProgramChannelAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.TvProgramListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.view.ChannelItemClickListener;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.view.ProgramRecyclerView;
import com.nttdocomo.android.tvterminalapp.view.ProgramScrollView;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.model.TabItemLayout;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TvProgramListActivity extends BaseActivity
        implements ChannelItemClickListener,
        View.OnClickListener,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        ProgramScrollView.OnScrollOffsetListener,
        MyChannelDataProvider.ApiDataProviderCallback,
        TabItemLayout.OnClickTabTextListener {

    private static final int INDEX_TAB_HIKARI = 1;
    private static final int INDEX_TAB_MY_CHANNEL = 0;
    private static final float TAB_TITLE_SIZE = 14;
    private ProgramRecyclerView mProgramRecyclerView = null;
    private Boolean mIsMenuLaunch = false;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    private ProgramScrollView mTimeScrollView = null;
    private RecyclerView mChannelRecyclerView = null;
    private TabItemLayout mTabLayout = null;
    private LinearLayout mTabLinearLayout = null;
    private ImageView mTagImageView = null;
    private static final int START_TIME = 4;
    private static final int STANDARD_TIME = 24;
    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final int TIME_LINE_WIDTH = 44;
    private static final int ONE_HOUR_UNIT = 180;
    private static final String DATE_FORMAT = "yyyy年MM月dd日 (E)";
    private static final String DATE_SELECT_FORMAT = "yyyy-MM-dd";
    private static final String SELECT_DATE_FORMAT = "yyyy年MM月dd日";
    private static final String TIME_FORMAT = "HHmmss";
    private static final String CUR_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";
    private String mSelectDateStr = null;
    private String mDate[] = {"日", "月", "火", "水", "木", "金", "土"};
    private String mToDay = null;
    private LinearLayout mLinearLayout = null;
    private String mProgramTabNames[] = null;
    private static int EXPIRE_DATE = 7;
    private int mTabIndex = 0;
    private TvProgramListAdapter mTvProgramListAdapter = null;
    private ProgramChannelAdapter mProgramChannelAdapter = null;
    private ArrayList<ChannelInfo> mChannelInfo = new ArrayList<>();
    private ArrayList<ChannelInfo> mChannels = new ArrayList<>();
    private boolean mIsFromBackFlag = false;
    private ArrayList<ChannelInfo> hikariChannels;
    private ArrayList<ChannelInfo> mappedMyChannelList;
    private ArrayList<MyChannelMetaData> myChannelDataList;
    private RelativeLayout mTimeLine;
    private ImageView mNowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        //チャンネルデータ取得
        getChannelData();
    }

    /**
     * 機能
     * ビューの初期化
     */
    private void initView() {
        mScreenHeight = getHeightDensity();
        mScreenWidth = getWidthDensity();
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mTimeScrollView = findViewById(R.id.tv_program_list_main_layout_time_sl);
        mChannelRecyclerView = findViewById(R.id.tv_program_list_main_layout_channel_rv);
        mProgramRecyclerView = findViewById(R.id.tv_program_list_main_layout_channeldetail_rv);
        final ProgramScrollView programScrollView = findViewById(R.id.tv_program_list_main_layout_channeldetail_sl);
        mTagImageView = findViewById(R.id.tv_program_list_main_layout_curtime_iv);
        mTimeLine = findViewById(R.id.tv_program_list_main_layout_time_line);
        mNowImage = findViewById(R.id.tv_program_list_main_layout_time_line_now);

        mChannelRecyclerView.bringToFront();
        mTagImageView.bringToFront();
        mTagImageView.setOnClickListener(this);
        titleTextView.setOnClickListener(this);
        mTitleArrowImage.setOnClickListener(this);
        mTimeScrollView.setScrollView(programScrollView);
        programScrollView.setScrollView(mTimeScrollView);
        programScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MotionEvent e = MotionEvent.obtain(motionEvent);
                e.setLocation(e.getX() + programScrollView.getScrollX()
                        , e.getY() - mChannelRecyclerView.getHeight());
                mProgramRecyclerView.forceToDispatchTouchEvent(e);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsFromBackFlag) {
            //タイトル設定
            setTitle();
        } else {
            mIsFromBackFlag = false;
        }
        scrollToCurTime();
        refreshTimeLine();
    }

    /**
     * 機能
     * 日付の表示
     */
    protected void showDatePickDlg() {
        int curYear = 0;
        int curMonth = 0;
        int curDay = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(SELECT_DATE_FORMAT, Locale.JAPAN);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
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
        gc.setTime(new Date());
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
     * タイトルの日付の設定
     *
     * @param year        年
     * @param monthOfYear 月
     * @param dayOfMonth  日
     */
    private void setSelectedDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        StringBuilder selectDate = new StringBuilder();
        calendar.set(year, monthOfYear, dayOfMonth);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        monthOfYear++;
        StringBuilder month = new StringBuilder();
        StringBuilder day = new StringBuilder();
        if (monthOfYear < 10) {
            month.append("0");
        }
        month.append(monthOfYear);
        if (dayOfMonth < 10) {
            day.append("0");
        }
        day.append(dayOfMonth);
        selectDate.append(year);
        selectDate.append("年");
        selectDate.append(month.toString());
        selectDate.append("月");
        selectDate.append(day);
        selectDate.append("日");
        mToDay = selectDate.toString();
        selectDate = new StringBuilder();
        selectDate.append(month.toString());
        selectDate.append("月");
        selectDate.append(day);
        selectDate.append("日");
        selectDate.append(" (");
        selectDate.append(mDate[week - 1]);
        selectDate.append(")");
        setTitleText(selectDate.toString());
        selectDate = new StringBuilder();
        selectDate.append(year);
        selectDate.append("-");
        selectDate.append(month.toString());
        selectDate.append("-");
        selectDate.append(day);
        mSelectDateStr = selectDate.toString();
    }

    /**
     * 機能
     * タイトルの設定
     */
    private void setTitle() {
        //フォーマットパターンを指定して表示する
        Calendar c = Calendar.getInstance();
        Locale.setDefault(Locale.JAPAN);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
        mToDay = sdf.format(c.getTime());
        setTitleText(mToDay.substring(5));
        sdf = new SimpleDateFormat(DATE_SELECT_FORMAT, Locale.JAPAN);
        mSelectDateStr = sdf.format(c.getTime());
    }

    /**
     * 機能
     * タブの設定
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
    public void onClickTab(int position) {
        DTVTLogger.start("position = " + position);
        mTabIndex = position;
        clearData();
        getChannelData();
        DTVTLogger.end();
    }

    /**
     * 機能
     * タブを切り替え
     *
     * @param position タブインデックス
     */
    public void setTab(int position) {
        if (mTabLinearLayout != null) {
            for (int i = 0; i < mProgramTabNames.length; i++) {
                TextView mTextView = (TextView) mTabLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.rectangele);
                    mTextView.setTextColor(ContextCompat.getColor(this, R.color.tv_program_list_tab_checked_text));
                } else {
                    mTextView.setBackgroundResource(0);
                    mTextView.setTextColor(ContextCompat.getColor(this, R.color.tv_program_list_tab_unchecked_text));
                }
            }
        }
    }

    /**
     * 機能
     * チャンネルと番組詳細を同時にスクロール設定
     *
     * @param channelList チャンネルリサイクルビュー
     * @param programList 番組リサイクルビュー
     */
    private void syncScroll(final RecyclerView channelList, final RecyclerView programList) {
        channelList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    programList.scrollBy(dx, dy);
                }
            }
        });

        programList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    channelList.scrollBy(dx, dy);
                }
            }
        });
    }

    /**
     * 機能
     * 番組タブを設定
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
     * 時間軸を設定
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
     * 番組表を設定
     */
    private void setChannelContentsView(ArrayList<ChannelInfo> channels) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mChannelRecyclerView.setLayoutManager(linearLayoutManager);
        mProgramChannelAdapter = new ProgramChannelAdapter(this, channels);
        mProgramChannelAdapter.setOnItemClickListener(this);
        mChannelRecyclerView.setAdapter(mProgramChannelAdapter);
    }

    @Override
    public void onChannelItemClick(View view, int position) {
        startActivity(ChannelDetailPlayerActivity.class, null);
    }

    /**
     * 機能
     * チャンネルデータ取得
     */
    private void getChannelData() {
        if(mTabIndex != INDEX_TAB_MY_CHANNEL){//ひかり、dTVチャンネル
            ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            scaledDownProgramListDataProvider.getChannelList(1, 1, "", mTabIndex);
        } else {//MY番組表
            MyChannelDataProvider myChannelDataProvider = new MyChannelDataProvider(this);
            myChannelDataProvider.getMyChannelList(R.layout.tv_program_list_main_layout);
        }
    }

    @Override
    public void onClick(View v) {
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
     * 番組表情報を設定
     */
    private void setProgramRecyclerView(ArrayList<ChannelInfo> channelInfo) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mProgramRecyclerView.setLayoutManager(linearLayoutManager);
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
     * 番組表情報をクリア
     */
    private void clearData() {
        mChannelRecyclerView.removeAllViews();
        mProgramRecyclerView.removeAllViews();
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
     * 現在時刻にスクロール
     */
    private void scrollToCurTime() {
        String curTime = new SimpleDateFormat(TIME_FORMAT, Locale.JAPAN).format(new Date());
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
     * 分、秒を時に転換する
     * @param curMin
     * @param curSec
     * @return
     */
    private float minSec2Hour(int curMin, int curSec) {
        int sec = curMin * 60;
        float hour = ((float) sec + curSec) / 3600;
        return hour;
    }

    @Override
    public void channelInfoCallback(ChannelInfoList channelsInfo) {
        if (channelsInfo != null && channelsInfo.getChannels() != null) {
            ArrayList<ChannelInfo> channels = channelsInfo.getChannels();
            sort(channels);
            mChannelInfo = channels;
            if(mTabIndex !=0 ){
                setProgramRecyclerView(mChannelInfo);
            }else {//マイ番組表
                ArrayList<ChannelInfo> myChannels = new ArrayList<>();
                for(int i = 0; i< mappedMyChannelList.size(); i++){
                    for (int j=0;j<mChannelInfo.size();j++){
                        if(mappedMyChannelList.get(i).getChNo() == mChannelInfo.get(j).getChNo()){//チャンネル番号でマッピング
                            myChannels.add(mChannelInfo.get(j));
                        }
                    }
                }
                setProgramRecyclerView(myChannels);
            }
        }
    }

    @Override
    public void channelListCallback(ArrayList<ChannelInfo> channels) {
        if (channels != null) {
            if(mTabIndex == INDEX_TAB_MY_CHANNEL){//MY番組表
                this.hikariChannels = channels;
                mappedMyChannelList = executeMapping();
                setChannelContentsView(mappedMyChannelList);
                loadMyChannel();
            }else {//ひかり、dTVチャンネル
                this.mChannels = channels;
                setChannelContentsView(mChannels);
                showMyChannelNoItem(false);
                ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
                int channelNos[] = new int[channels.size()];
                for (int i = 0; i < channels.size(); i++) {
                    channelNos[i] = channels.get(i).getChNo();
                }
                String dateList[] = {mSelectDateStr};
                scaledDownProgramListDataProvider.getProgram(channelNos, dateList, mTabIndex);
            }
        }
    }

    /**
     * My番組表ロード
     */
    private void loadMyChannel() {
        ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        int channelNos[] = new int[mappedMyChannelList.size()];
        for (int i = 0; i < mappedMyChannelList.size(); i++) {
            channelNos[i] = mappedMyChannelList.get(i).getChNo();
        }
        if(channelNos.length != 0){//マイ番組表設定されていない場合、通信しない
            String dateList[] = {mSelectDateStr};
            scaledDownProgramListDataProvider.getProgram(channelNos, dateList, 1);
        }else {//「マイ番組が設定されていません」と表示される
            showMyChannelNoItem(true);
        }
    }

    /**
     * マイ番組表Noチャンネル表示
     * @param isShowFlag
     */
    private void showMyChannelNoItem(boolean isShowFlag) {
        if(isShowFlag){
            findViewById(R.id.tv_program_list_main_layout_time_sl).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_program_list_main_layout_curtime_iv).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_program_list_main_layout_time_line).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_program_list_main_layout_tip_tv).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.tv_program_list_main_layout_time_sl).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_program_list_main_layout_curtime_iv).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_program_list_main_layout_time_line).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_program_list_main_layout_tip_tv).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * チャンネルリストからマッピングデータを抽出する
     * @return
     */
    private ArrayList<ChannelInfo> executeMapping() {
        ArrayList<ChannelInfo> myChannels = new ArrayList<>();
        if(myChannelDataList != null){
            for(int i = 0; i< myChannelDataList.size(); i++){
                for(int j=0;j<hikariChannels.size();j++){
                    //サービスIDでマッピング
                    if(myChannelDataList.get(i).getServiceId().equals(hikariChannels.get(j).getServiceId())){
                        myChannels.add(hikariChannels.get(j));
                    }
                }
            }
        }
        return myChannels;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIsFromBackFlag = true;
    }

    /**
     * ソートを行う
     */
    private void sort(ArrayList<ChannelInfo> channels) {
        for (ChannelInfo channel : channels) {
            Collections.sort(channel.getSchedules(), new CalendarComparator());
        }
    }

    /**
     * MY番組表情報取得
     * @param myChannelMetaData 画面に渡すチャンネル番組情報
     */
    @Override
    public void onMyChannelListCallback(ArrayList<MyChannelMetaData> myChannelMetaData) {
        if (myChannelMetaData != null && myChannelMetaData.size() > 0) {
            this.myChannelDataList = myChannelMetaData;
            ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            scaledDownProgramListDataProvider.getChannelList(1, 1, "", INDEX_TAB_HIKARI);//ひかりリストからチャンネル選択
        }else {//MY番組表情報がなければMY番組表を設定していないとする
            showMyChannelNoItem(true);
        }
    }

    @Override
    public void onMyChannelRegisterCallback(String result) {
        //nothing to do
    }

    @Override
    public void onMyChannelDeleteCallback(String result) {
        //nothing to do
    }

    /**
     * スクロール時、スクロール距離を返すコールバック
     * @param offset
     */
    @Override
    public void onScrollOffset(int offset) {
        String curTime = new SimpleDateFormat(TIME_FORMAT, Locale.JAPAN).format(new Date());
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
     * 現在時刻ラインの表示位置を更新
     */
    private void refreshTimeLine() {
        String curTime = new SimpleDateFormat(TIME_FORMAT, Locale.JAPAN).format(new Date());
        int curClock = Integer.parseInt(curTime.substring(0, 2));
        int curMin = Integer.parseInt(curTime.substring(2, 4));
        int curSec = Integer.parseInt(curTime.substring(4, 6));
        float channelRvHeight = (float) mChannelRecyclerView.getHeight();
        float timeLinePosition = 0;
        if(mTimeScrollView.getHeight() /dip2px(ONE_HOUR_UNIT) >= 3){//タブレット(将来さらにチェック)
            if (START_TIME <= curClock && curClock < STANDARD_TIME) {
                timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec) + channelRvHeight;
            } else {
                if (0 <= curClock && curClock <= 3) {//底から完全に見える"1時間単位"をマーナイスする
                    timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec)
                            + channelRvHeight+(mTimeScrollView.getHeight()-(START_TIME-curClock)*dip2px(ONE_HOUR_UNIT));
                }
            }
        }else {
            if (START_TIME <= curClock && curClock < STANDARD_TIME || curClock == 0 || curClock == 1) {
                timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec) + channelRvHeight;
            } else {
                if (2 <= curClock && curClock <= 3) {//底から完全に見える"1時間単位"をマーナイスする
                    timeLinePosition = (dip2px(ONE_HOUR_UNIT) + 0.5f) * minSec2Hour(curMin, curSec)
                            + channelRvHeight+(mTimeScrollView.getHeight()-(START_TIME-curClock)*dip2px(ONE_HOUR_UNIT));
                }
            }
        }
        mTimeLine.setY(timeLinePosition - (float) mNowImage.getHeight() / 2);
    }

    /**
     * ソート処理
     */
    private static class CalendarComparator implements Comparator<ScheduleInfo>, Serializable {
        private static final long serialVersionUID = -1L;

        @Override
        public int compare(ScheduleInfo s1, ScheduleInfo s2) {
            StringBuilder time1 = new StringBuilder();
            time1.append(s1.getStartTime().substring(0, 10));
            time1.append(s1.getStartTime().substring(11, 19));
            StringBuilder time2 = new StringBuilder();
            time2.append(s2.getStartTime().substring(0, 10));
            time2.append(s2.getStartTime().substring(11, 19));
            SimpleDateFormat format = new SimpleDateFormat(CUR_TIME_FORMAT, Locale.JAPAN);
            Date date1 = new Date();
            Date date2 = new Date();
            try {
                date1 = format.parse(time1.toString());
                date2 = format.parse(time2.toString());
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
            return date1.compareTo(date2);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}