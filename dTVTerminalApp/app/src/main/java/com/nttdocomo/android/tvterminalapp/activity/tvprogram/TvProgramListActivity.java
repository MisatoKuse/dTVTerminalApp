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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.ChannelDetailPlayerActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ProgramChannelAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.TvProgramListAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelItemClickListener;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelsInfo;
import com.nttdocomo.android.tvterminalapp.model.program.ProgramRecyclerView;
import com.nttdocomo.android.tvterminalapp.model.program.ProgramScrollView;
import com.nttdocomo.android.tvterminalapp.model.program.Schedule;

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

public class TvProgramListActivity extends BaseActivity implements ChannelItemClickListener,
        View.OnClickListener, ScaledDownProgramListDataProvider.ApiDataProviderCallback {

    private ProgramRecyclerView programRecyclerView;
    private int screenHeight;
    private int screenWidth;
    private ProgramScrollView timeScrollView;
    private RecyclerView channelRecyclerView;
    private HorizontalScrollView tabScrollView;
    private LinearLayout tabLinearLayout;
    private ImageView tagImageView;
    private RelativeLayout changeModeLayout;
    private static final int START_TIME = 4;
    private static final int STANDARD_TIME = 24;
    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final int SCREEN_TIME_HEIGHT_PERCENT = 3;
    private static final String DATE_FORMAT = "yyyy年MM月dd日 (E)";
    private static final String DATE_SELECT_FORMAT = "yyyy-MM-dd";
    private static final String SELECT_DATE_FORMAT = "yyyy年MM月dd日";
    private static final String TIME_FORMAT = "HHmmss";
    private static final String CUR_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";
    private String selectDateStr;
    private String date[] = {"日", "月", "火", "水", "木", "金", "土"};
    private String toDay;
    private LinearLayout mLinearLayout;
    private String programTabNames[] = null;
    private static int EXPIRE_DATE = 7;
    private int tab_index = 0;
    private TvProgramListAdapter tvProgramListAdapter;
    private ProgramChannelAdapter programChannelAdapter;
    private ArrayList<Channel> channelInfo;
    private ArrayList<Channel> channels;
    private boolean isFromBackFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_program_list_main_layout);
        initView();
        syncScroll(channelRecyclerView, programRecyclerView);
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
        screenHeight = getHeightDensity();
        screenWidth = getWidthDensity();
        ImageView menuImage = findViewById(R.id.header_layout_menu);
        ImageView tvImage = findViewById(R.id.header_stb_status_icon);
        findViewById(R.id.header_layout_back).setVisibility(View.INVISIBLE);
        timeScrollView = findViewById(R.id.tv_program_list_main_layout_time_sl);
        channelRecyclerView = findViewById(R.id.tv_program_list_main_layout_channel_rv);
        tabScrollView = findViewById(R.id.tv_program_list_main_layout_tab_hs);
        programRecyclerView = findViewById(R.id.tv_program_list_main_layout_channeldetail_rv);
        final ProgramScrollView programScrollView = findViewById(R.id.tv_program_list_main_layout_channeldetail_sl);
        tagImageView = findViewById(R.id.tv_program_list_main_layout_curtime_iv);
        changeModeLayout = findViewById(R.id.tv_program_list_main_layout_changemode_rl);
        menuImage.setVisibility(View.VISIBLE);
        tvImage.setVisibility(View.VISIBLE);
        tvImage.setOnClickListener(this);
        menuImage.setOnClickListener(this);
        tagImageView.setOnClickListener(this);
        titleTextView.setOnClickListener(this);
        timeScrollView.setScrollView(programScrollView);
        programScrollView.setScrollView(timeScrollView);
        programScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MotionEvent e = MotionEvent.obtain(motionEvent);
                e.setLocation(e.getX() + programScrollView.getScrollX()
                        , e.getY() - channelRecyclerView.getHeight());
                programRecyclerView.forceToDispatchTouchEvent(e);
                return false;
            }
        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int)((double)screenWidth / (double)SCREEN_TIME_WIDTH_PERCENT * 2.0),
                (int)((double)screenWidth / (double)SCREEN_TIME_WIDTH_PERCENT * 3.5));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        changeModeLayout.setLayoutParams(layoutParams);
        changeModeLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFromBackFlag){
            //タイトル設定
            setTitle();
        }else{
            isFromBackFlag = false;
        }
    }

    /**
     * 機能
     * 日付の表示
     */
    protected void showDatePickDlg() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                setSelectedDate(year, monthOfYear, dayOfMonth);
                clearData();
                getChannelData();
            }
        };
        int curYear = 0;
        int curMonth = 0;
        int curDay = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(SELECT_DATE_FORMAT, Locale.JAPAN);
        try {
            Date date = sdf.parse(toDay);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            curYear = calendar.get(Calendar.YEAR);
            curMonth = calendar.get(Calendar.MONTH);
            curDay = calendar.get(Calendar.DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,
                curYear, curMonth, curDay);
        //日付の選択できる範囲を設定
        DatePicker datePicker = datePickerDialog.getDatePicker();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        if (tab_index == programTabNames.length - 1) {
            gc.add(Calendar.DAY_OF_MONTH, -EXPIRE_DATE);
        }
        datePicker.setMinDate(gc.getTimeInMillis());
        if (tab_index == programTabNames.length - 1) {
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
        toDay = selectDate.toString();
        selectDate = new StringBuilder();
        selectDate.append(month.toString());
        selectDate.append("月");
        selectDate.append(day);
        selectDate.append("日");
        selectDate.append(" (");
        selectDate.append(date[week - 1]);
        selectDate.append(")");
        setTitleText(selectDate.toString());
        selectDate = new StringBuilder();
        selectDate.append(year);
        selectDate.append("-");
        selectDate.append(month.toString());
        selectDate.append("-");
        selectDate.append(day);
        selectDateStr = selectDate.toString();
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
        toDay = sdf.format(c.getTime());
        setTitleText(toDay.substring(5));
        sdf = new SimpleDateFormat(DATE_SELECT_FORMAT, Locale.JAPAN);
        selectDateStr = sdf.format(c.getTime());
    }

    /**
     * 機能
     * タブの設定
     */
    private void setTabView() {
        programTabNames = getResources().getStringArray(R.array.tv_program_list_tab_names);
        tabScrollView.removeAllViews();
        tabLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                screenWidth / SCREEN_TIME_WIDTH_PERCENT + (int) getDensity() * 5);
        tabLinearLayout.setLayoutParams(layoutParams);
        tabLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        tabLinearLayout.setGravity(Gravity.CENTER);
        tabLinearLayout.setBackgroundColor(Color.BLACK);
        tabLinearLayout.setBackgroundResource(R.drawable.rectangele_all);
        tabScrollView.addView(tabLinearLayout);
        for (int i = 0; i < programTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins((int) getDensity() * 15, 0, 0, 0);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(programTabNames[i]);
            tabTextView.setBackgroundColor(Color.BLACK);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            tabTextView.setBackgroundResource(0);
            tabTextView.setTextColor(Color.WHITE);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.rectangele);
                tabTextView.setTextColor(Color.GRAY);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tab_index = (int) view.getTag();
                    setTab(tab_index);
                    clearData();
                    getChannelData();
                }
            });
            tabLinearLayout.addView(tabTextView);
        }
    }

    /**
     * 機能
     * タブを切り替え
     *
     * @param position タブインデックス
     */
    public void setTab(int position) {
        if (tabLinearLayout != null) {
            for (int i = 0; i < programTabNames.length; i++) {
                TextView mTextView = (TextView) tabLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.rectangele);
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setBackgroundResource(0);
                    mTextView.setTextColor(Color.WHITE);
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
                screenWidth / SCREEN_TIME_WIDTH_PERCENT,
                screenWidth / SCREEN_TIME_WIDTH_PERCENT);
        tagImageView.setLayoutParams(layoutParams);
        tagImageView.setImageResource(R.mipmap.ic_event_note_white_24dp);
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
        timeScrollView.addView(mLinearLayout);
        for (int i = START_TIME; i < STANDARD_TIME + START_TIME; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    screenWidth / SCREEN_TIME_WIDTH_PERCENT,
                    screenHeight / SCREEN_TIME_HEIGHT_PERCENT);
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
    private void setChannelContentsView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        channelRecyclerView.setLayoutManager(linearLayoutManager);
        programChannelAdapter = new ProgramChannelAdapter(this, channels);
        programChannelAdapter.setOnItemClickListener(this);
        channelRecyclerView.setAdapter(programChannelAdapter);
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
        ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        scaledDownProgramListDataProvider.getChannelList(1, 1, "", tab_index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_layout_menu:
                //メニュー動作
                onSampleGlobalMenuButton_PairLoginOk();
                break;
            case R.id.tv_program_list_main_layout_curtime_iv:
                //システム時間軸にスクロール
                scrollToCurTime();
                break;
            case R.id.header_layout_text:
                //日付選択ダイアログ
                showDatePickDlg();
                break;
            case R.id.header_stb_status_icon:
                //テレビアイコンをタップされたらリモコンを起動する
                if(getStbStatus()) {
                    createRemoteControllerView();
                    remoteControllerView.startRemoteUI();
                }
                break;
            case R.id.tv_program_list_main_layout_changemode_rl:
                //番組表モード切替
                // TODO: 拡大・縮小番組表切替
                break;
            default:
                break;
        }
    }

    /**
     * 機能
     * 番組表情報を設定
     */
    private void setProgramRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        programRecyclerView.setLayoutManager(linearLayoutManager);
        tvProgramListAdapter = new TvProgramListAdapter(this, channelInfo);
        programRecyclerView.setAdapter(tvProgramListAdapter);
        programRecyclerView.setItemViewCacheSize(channelInfo.size());
        programRecyclerView.getRecycledViewPool().setMaxRecycledViews(tvProgramListAdapter.getItemViewType(0), 3);
    }

    /**
     * 機能
     * 番組表情報をクリア
     */
    private void clearData(){
        channelRecyclerView.removeAllViews();
        programRecyclerView.removeAllViews();
        if (channels != null) {
            channels.clear();
        }
        if (channelInfo != null) {
            channelInfo.clear();
        }
        if (programChannelAdapter != null) {
            programChannelAdapter.notifyDataSetChanged();
        }
        if(tvProgramListAdapter!=null){
            tvProgramListAdapter.notifyDataSetChanged();
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
        timeScrollView.smoothScrollTo(0, scrollDis);
    }

    @Override
    public void channelInfoCallback(ChannelsInfo channelsInfo) {
        if (channelsInfo != null && channelsInfo.getChannels() != null) {
            ArrayList<Channel> channels = channelsInfo.getChannels();
            sort(channels);
            channelInfo = channels;
            if(tab_index !=0 ){
                setProgramRecyclerView();
            }
        }
    }

    @Override
    public void channelListCallback(ArrayList<Channel> channels) {
        if (channels != null) {
            if(tab_index !=0 ){
                this.channels = channels;
                setChannelContentsView();
            }
            ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            int channelNos[] = new int[channels.size()];
            for (int i = 0; i < channels.size(); i++) {
                channelNos[i] = channels.get(i).getChNo();
            }
            String dateList[] = {selectDateStr};
            scaledDownProgramListDataProvider.getProgram(channelNos, dateList, tab_index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isFromBackFlag = true;
    }

    /**
     * ソートを行う
     */
    private void sort(ArrayList<Channel> channels) {
        for (Channel channel : channels) {
            Collections.sort(channel.getSchedules(), new CalendarComparator());
        }
    }

    /**
     * ソート処理
     */
    private class CalendarComparator implements Comparator<Schedule>, Serializable {
        private static final long serialVersionUID = -1L;

        @Override
        public int compare(Schedule s1, Schedule s2) {
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
                e.printStackTrace();
            }
            return date1.compareTo(date2);
        }
    }
}