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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
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

public class TvProgramListActivity extends BaseActivity
        implements ChannelItemClickListener,
        View.OnClickListener,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback {

    private ProgramRecyclerView mProgramRecyclerView = null;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    private String mSelectDateStr = null;
    private String mDate[] = {"日", "月", "火", "水", "木", "金", "土"};
    private String mToDay = null;
    private String mProgramTabNames[] = null;
    private int mTabIndex = 0;
    private ArrayList<Channel> mChannelInfo = new ArrayList<>();
    private ArrayList<Channel> mChannels = new ArrayList<>();
    private boolean mIsFromBackFlag = false;

    private LinearLayout mLinearLayout = null;
    private LinearLayout mTabLinearLayout = null;
    private ImageView mTagImageView = null;
    private ProgramScrollView mTimeScrollView = null;
    private RecyclerView mChannelRecyclerView = null;
    private HorizontalScrollView mTabScrollView = null;

    private TvProgramListAdapter mTvProgramListAdapter = null;
    private ProgramChannelAdapter mProgramChannelAdapter = null;

    private static int EXPIRE_DATE = 7;
    private static final int START_TIME = 4;
    private static final int STANDARD_TIME = 24;
    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final int SCREEN_TIME_HEIGHT_PERCENT = 3;
    private static final String DATE_FORMAT = "yyyy年MM月dd日 (E)";
    private static final String DATE_SELECT_FORMAT = "yyyy-MM-dd";
    private static final String SELECT_DATE_FORMAT = "yyyy年MM月dd日";
    private static final String TIME_FORMAT = "HHmmss";
    private static final String CUR_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_program_list_main_layout);
        initView();
        syncScroll(mChannelRecyclerView, mProgramRecyclerView);
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
        RelativeLayout changeModeLayout = null;
        mScreenHeight = getHeightDensity();
        mScreenWidth = getWidthDensity();
        ImageView menuImage = findViewById(R.id.header_layout_menu);
        ImageView tvImage = findViewById(R.id.header_stb_status_icon);
        tvImage.setOnClickListener(mRemoteControllerOnClickListener);
        findViewById(R.id.header_layout_back).setVisibility(View.INVISIBLE);
        mTimeScrollView = findViewById(R.id.tv_program_list_main_layout_time_sl);
        mChannelRecyclerView = findViewById(R.id.tv_program_list_main_layout_channel_rv);
        mTabScrollView = findViewById(R.id.tv_program_list_main_layout_tab_hs);
        mProgramRecyclerView = findViewById(R.id.tv_program_list_main_layout_channeldetail_rv);
        final ProgramScrollView programScrollView = findViewById(R.id.tv_program_list_main_layout_channeldetail_sl);
        mTagImageView = findViewById(R.id.tv_program_list_main_layout_curtime_iv);
        changeModeLayout = findViewById(R.id.tv_program_list_main_layout_changemode_rl);
        menuImage.setVisibility(View.VISIBLE);
        tvImage.setVisibility(View.VISIBLE);
//        tvImage.setOnClickListener(this);
        menuImage.setOnClickListener(this);
        mTagImageView.setOnClickListener(this);
        titleTextView.setOnClickListener(this);
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int) ((double) mScreenWidth / (double) SCREEN_TIME_WIDTH_PERCENT * 2.0),
                (int) ((double) mScreenWidth / (double) SCREEN_TIME_WIDTH_PERCENT * 3.5));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        changeModeLayout.setLayoutParams(layoutParams);
        changeModeLayout.setOnClickListener(this);
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
        mProgramTabNames = getResources().getStringArray(R.array.tv_program_list_tab_names);
        mTabScrollView.removeAllViews();
        mTabLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                mScreenWidth / SCREEN_TIME_WIDTH_PERCENT + (int) getDensity() * 5);
        mTabLinearLayout.setLayoutParams(layoutParams);
        mTabLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabLinearLayout.setGravity(Gravity.CENTER);
        mTabLinearLayout.setBackgroundColor(Color.BLACK);
        mTabLinearLayout.setBackgroundResource(R.drawable.rectangele_all);
        mTabScrollView.addView(mTabLinearLayout);
        for (int i = 0; i < mProgramTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins((int) getDensity() * 15, 0, 0, 0);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mProgramTabNames[i]);
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
                    mTabIndex = (int) view.getTag();
                    setTab(mTabIndex);
                    clearData();
                    getChannelData();
                }
            });
            mTabLinearLayout.addView(tabTextView);
        }
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
                mScreenWidth / SCREEN_TIME_WIDTH_PERCENT,
                mScreenWidth / SCREEN_TIME_WIDTH_PERCENT);
        mTagImageView.setLayoutParams(layoutParams);
        mTagImageView.setImageResource(R.mipmap.ic_event_note_white_24dp);
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
                    mScreenWidth / SCREEN_TIME_WIDTH_PERCENT,
                    mScreenHeight / SCREEN_TIME_HEIGHT_PERCENT);
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
        mChannelRecyclerView.setLayoutManager(linearLayoutManager);
        mProgramChannelAdapter = new ProgramChannelAdapter(this, mChannels);
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
        ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        scaledDownProgramListDataProvider.getChannelList(1, 1, "", mTabIndex);
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
//            case R.id.header_stb_status_icon:
//                //テレビアイコンをタップされたらリモコンを起動する
//                if (getStbStatus()) {
//                    createRemoteControllerView();
//                    getRemoteControllerView().startRemoteUI();
//                }
//                break;
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
        mProgramRecyclerView.setLayoutManager(linearLayoutManager);
        mTvProgramListAdapter = new TvProgramListAdapter(this, mChannelInfo);
        mProgramRecyclerView.setAdapter(mTvProgramListAdapter);
        mProgramRecyclerView.setItemViewCacheSize(mChannelInfo.size());
        mProgramRecyclerView.getRecycledViewPool().setMaxRecycledViews(mTvProgramListAdapter.getItemViewType(0), 3);
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

    @Override
    public void channelInfoCallback(ChannelsInfo channelsInfo) {
        if (channelsInfo != null && channelsInfo.getChannels() != null) {
            ArrayList<Channel> channels = channelsInfo.getChannels();
            sort(channels);
            mChannelInfo = channels;
            /*if(tab_index !=0 ){*/
            setProgramRecyclerView();
            /*}*/
        }
    }

    @Override
    public void channelListCallback(ArrayList<Channel> channels) {
        if (channels != null) {
            if (mTabIndex != 0) {
                this.mChannels = channels;
                setChannelContentsView();
            }
            ScaledDownProgramListDataProvider scaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
            int channelNos[] = new int[channels.size()];
            for (int i = 0; i < channels.size(); i++) {
                channelNos[i] = channels.get(i).getChNo();
            }
            String dateList[] = {mSelectDateStr};
            scaledDownProgramListDataProvider.getProgram(channelNos, dateList, mTabIndex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIsFromBackFlag = true;
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
                DTVTLogger.debug(e);
            }
            return date1.compareTo(date2);
        }
    }
}