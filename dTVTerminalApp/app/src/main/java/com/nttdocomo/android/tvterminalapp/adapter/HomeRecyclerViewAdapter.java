/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Home画面に表示するコンテンツリストのアダプタ.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    /**
     * Inflater.
     */
    private LayoutInflater mInflater;
    /**
     * ホーム画面に表示するコンテンツデータのリスト.
     */
    private List<ContentsData> mContentList;
    /**
     * コンテキスト.
     */
    private Activity mContext;
    /**
     * サムネイル取得プロバイダー.
     */
    private ThumbnailProvider mThumbnailProvider;
    /**
     * コンテンツ種別を判別するためのインデックス.
     */
    private int mIndex;
    /**
     * もっと見るフッター.
     */
    private View mFooterView;
    /**
     * チャンネル一覧.
     */
    private ChannelList mChannelList = null;
    /**
     * 最大表示件数.
     */
    private static final int MAX_COUNT = 10;
    /**
     * ヘッダー.
     */
    private static final int TYPE_HEADER = 0;
    /**
     * フッター.
     */
    private static final int TYPE_FOOTER = 1;
    /**
     * コンテンツ.
     */
    private static final int TYPE_NORMAL = 2;
    /**
     * カテゴリ NOW ON AIR(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_CHANNEL = 12;
    /**
     * カテゴリ おすすめ番組(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_RECOMMEND_PROGRAM = HOME_CONTENTS_SORT_CHANNEL + 1;
    /**
     * カテゴリ 今日のテレビランキング(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_TODAY = HOME_CONTENTS_SORT_CHANNEL + 3;
    /**
     * カテゴリ 今日のテレビランキング(ランキングトップ画面).
     */
    private final static int RANKING_CONTENTES_TODAY_SORT = 20;
    /**
     * カテゴリ 週間テレビランキング(ランキングトップ画面)
     */
    private final static int RANKING_CONTENTES_WEEK_SORT = RANKING_CONTENTES_TODAY_SORT + 1;

    /**
     * ランキング 1位.
     */
    private final static String RANKING_NUMBER_ONE = "1";
    /**
     * ランキング 2位.
     */
    private final static String RANKING_NUMBER_TWO = "2";
    /**
     * ランキング 3位.
     */
    private final static String RANKING_NUMBER_THREE = "3";
    /**
     * 放送時刻の開始位置(月の十の位).
     */
    private final static int START_TIME_MONTH_BEGIN = 4;
    /**
     * 放送時刻の月の区切り.
     */
    private final static int START_TIME_MONTH_SEPARATE = 5;
    /**
     * 放送時刻の終了位置(月の一の位).
     */
    private final static int START_TIME_MONTH_END = 6;
    /**
     * 放送時刻の開始位置(日).
     */
    private final static int START_TIME_DAY_BEGIN = 6;
    /**
     * 放送時刻の日の区切り.
     */
    private final static int START_TIME_DAY_SEPARATE = 7;
    /**
     * 放送時刻の終了位置(日).
     */
    private final static int START_TIME_DAY_END = 8;
    /**
     * 放送時刻の開始位置(時).
     */
    private final static int START_TIME_HOUR_BEGIN = 8;
    /**
     * 放送時刻の時の区切り.
     */
    private final static int START_TIME_HOUR_SEPARATE = 9;
    /**
     * 放送時刻の終了位置(時).
     */
    private final static int START_TIME_HOUR_END = 10;
    /**
     * 放送時刻の開始位置(分).
     */
    private final static int START_TIME_MINUTE_BEGIN = 10;
    /**
     * 放送時刻の分の区切り.
     */
    private final static int START_TIME_MINUTE_SEPARATE = 11;
    /**
     * 放送時刻の終了位置(分).
     */
    private final static int START_TIME_MINUTE_END = 12;
    /**
     * ゼロ.
     */
    private final static String ZERO = "0";

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param contentsDataList 表示するコンテンツリスト
     * @param index アダプタを使用しているRecyclerViewを識別するための値
     */
    public HomeRecyclerViewAdapter(final Activity context, final List<ContentsData> contentsDataList, final int index) {
        mInflater = LayoutInflater.from(context);
        this.mContentList = contentsDataList;
        this.mContext = context;
        this.mIndex = index;
        mThumbnailProvider = new ThumbnailProvider(context);
    }

    /**
     * 「すべてを見る」のViewをセット.
     *
     * @param footerView 「すべてを見る」View
     */
    public void setFooterView(final View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(final int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        int count = mContentList.size();
        if (count > MAX_COUNT) {
            count = MAX_COUNT;
        }
        if (mFooterView == null) {
            return count;
        } else {
            return count + 1;
        }
    }

    /**
     * チャンネル一覧情報をセットする.
     *
     * @param channelList チャンネル一覧
     */
    public void setCHannnelList(ChannelList channelList) {
        this.mChannelList = channelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ViewHolder(mFooterView);
        }
        View view = mInflater.inflate(R.layout.home_main_layout_recyclerview_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImage = view.findViewById(R.id.home_main_recyclerview_item_iv);
        viewHolder.mContent = view.findViewById(R.id.home_main_recyclerview_item_tv_content);
        viewHolder.mTime = view.findViewById(R.id.home_main_recyclerview_item_tv_time);
        viewHolder.mNew = view.findViewById(R.id.home_main_recyclerview_item_iv_new);
        viewHolder.mRankNum = view.findViewById(R.id.home_main_recyclerview_item_iv_rank_num);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == TYPE_FOOTER) {
            return;
        }
        ContentsData contentsData = mContentList.get(i);
        String date = contentsData.getTime();
        String title = contentsData.getTitle();
        String rankNum = contentsData.getRank();
        String startTime = contentsData.getLinearStartDate();
        Boolean newFlag = newContentsCheck(startTime);

        if (TextUtils.isEmpty(title)) {
            title = contentsData.getTitle();
        }
        String thumbnail = contentsData.getThumURL();
        if (TextUtils.isEmpty(thumbnail)) {
            thumbnail = contentsData.getThumURL();
        }

        //表示するカテゴリに応じてサムネイル上に表示する情報のレイアウトを変更する
        switch (mIndex) {
            case HOME_CONTENTS_SORT_CHANNEL:
                //NOW ON AIR (1行目:タイトル 2行目:CH名+放送時間)
                setNowOnAirInfo(contentsData, viewHolder);
                break;
            case HOME_CONTENTS_SORT_RECOMMEND_PROGRAM:
                //おすすめ番組 (1行目:タイトル 2行目:放送時間)
                setRecommendInfo(contentsData, viewHolder);
                break;
            case HOME_CONTENTS_SORT_TODAY:
            case RANKING_CONTENTES_TODAY_SORT:
            case RANKING_CONTENTES_WEEK_SORT:
                //今日のテレビランキング/週間テレビランキング (1行目:タイトル 2行目:放送時間)
                setTvRankingInfo(contentsData, viewHolder);
                break;
            default:
                //上記以外 (タイトルが2行)
                if (viewHolder.mContent != null) {
                    viewHolder.mContent.setMaxLines(2);
                }
                if (viewHolder.mTime != null) {
                    viewHolder.mTime.setVisibility(View.GONE);
                }
                break;
        }

        //コンテンツのタイトルを表示
        if (!TextUtils.isEmpty(title)) {
            viewHolder.mContent.setVisibility(View.VISIBLE);
            viewHolder.mContent.setText(title);
        } else {
            viewHolder.mContent.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(startTime)) {
            if (newFlag) {
                viewHolder.mNew.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mNew.setVisibility(View.GONE);
            }
        } else {
            viewHolder.mNew.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(rankNum)) {
            viewHolder.mRankNum.setVisibility(View.VISIBLE);
            switch (rankNum) {
                case RANKING_NUMBER_ONE:
                    viewHolder.mRankNum.setBackgroundResource(R.drawable.label_ranking_1);
                    viewHolder.mRankNum.setTextColor(ContextCompat.getColor(mContext, R.color.black_text));
                    break;
                case RANKING_NUMBER_TWO:
                    viewHolder.mRankNum.setBackgroundResource(R.drawable.label_ranking_2);
                    break;
                case RANKING_NUMBER_THREE:
                    viewHolder.mRankNum.setBackgroundResource(R.drawable.label_ranking_3);
                    break;
                default:
                    viewHolder.mRankNum.setBackgroundResource(R.drawable.label_ranking_other);
                    break;
            }
            viewHolder.mRankNum.setText(rankNum);
        } else {
            viewHolder.mRankNum.setVisibility(View.GONE);
        }

        //URLによって、サムネイル取得
        if (!TextUtils.isEmpty(thumbnail)) {
            viewHolder.mImage.setTag(thumbnail);
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(viewHolder.mImage, thumbnail);
            if (bitmap != null) {
                viewHolder.mImage.setImageBitmap(bitmap);
            }
        } else {
            //URLがない場合はサムネイル取得失敗の画像を表示
            viewHolder.mImage.setImageResource(R.mipmap.error_scroll);
        }
        String recommendFlg = "";
        if (mIndex == HOME_CONTENTS_SORT_RECOMMEND_PROGRAM || mIndex == HOME_CONTENTS_SORT_RECOMMEND_PROGRAM + 1) {
            recommendFlg = ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY;
        } else {
            recommendFlg = ContentDetailActivity.PLALA_INFO_BUNDLE_KEY;
        }
        final OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(contentsData, recommendFlg);
        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(mContext, ContentDetailActivity.class);
                ComponentName componentName = mContext.getComponentName();
                intent.putExtra(DTVTConstants.SOURCE_SCREEN, componentName.getClassName());
                intent.putExtra(detailData.getRecommendFlg(), detailData);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * Now On Airの2段目に表示する情報を設定する.
     *
     * @param contentsData コンテンツデータ
     * @param viewHolder ViewHolder
     */
    private void setNowOnAirInfo(final ContentsData contentsData, final ViewHolder viewHolder) {
        String startTime = contentsData.getLinearStartDate();
        String endTime = contentsData.getLinearEndDate();
        String channelName = contentsData.getChannelName();
        if (TextUtils.isEmpty(startTime) || !DBUtils.isNumber(startTime)) {
            //TODO 放送開始時間が取得できなかった場合の仕様は現在未決定のため仮の時間を設定する.
            startTime = "1516766741";
        }
        if (TextUtils.isEmpty(endTime) || !DBUtils.isNumber(endTime)) {
            //TODO 放送終了時間が取得できなかった場合の仕様は現在未決定のため仮の時間を設定する.
            endTime = String.valueOf(DateUtils.getNowTimeFormatEpoch());
        }
        if (TextUtils.isEmpty(channelName)) {
            //TODO 放送終了時間が取得できなかった場合の仕様は現在未決定のため仮の時間を設定する.
            channelName = "CH名";
        }

        String date = structDateStrings(DateUtils.formatEpochToStringOpeLog(Long.parseLong(startTime)),
                DateUtils.formatEpochToStringOpeLog(Long.parseLong(endTime)), channelName);

        viewHolder.mTime.setText(date);
    }

    /**
     * おすすめ番組の2段目に表示する情報を設定する.
     *
     * @param contentsData コンテンツデータ
     * @param viewHolder ViewHolder
     */
    private void setRecommendInfo(final ContentsData contentsData, final ViewHolder viewHolder) {
        String startViewing = contentsData.getStartViewing();
        if (TextUtils.isEmpty(startViewing) || !DBUtils.isNumber(startViewing)) {
            //TODO 放送開始時間が取得できなかった場合の仕様は現在未決定のため仮の時間を設定する.
            startViewing = "20180130123456";
        }

        viewHolder.mTime.setText(structDateStrings(startViewing));
    }

    /**
     * 今日のテレビランキングの2段目に表示する情報を設定する.
     *
     * @param contentsData コンテンツデータ
     * @param viewHolder ViewHolder
     */
    private void setTvRankingInfo(final ContentsData contentsData, final ViewHolder viewHolder) {
        String availStartDate = contentsData.getLinearStartDate();
        if (TextUtils.isEmpty(availStartDate) || !DBUtils.isNumber(availStartDate)) {
            //TODO 放送開始時間が取得できなかった場合の仕様は現在未決定のため仮の時間を設定する.
            availStartDate = "1516966741";
        }

        String date = structDateStrings(DateUtils.formatEpochToStringOpeLog(Long.parseLong(availStartDate)));
        viewHolder.mTime.setText(date);
    }

    /**
     * yyyyMMddHHmmss形式から表示するデータを整形する(Now On Air).
     *
     * @param startTime 放送開始時間のyyyyMMddHHmmssデータ
     * @param endTime 放送終了時間のyyyyMMddHHmmssデータ
     * @param channelName チャンネル名
     * @return 整形した日付データ
     */
    private String structDateStrings(final String startTime, final String endTime, final String channelName) {
        String timeHourTensPlace = startTime.substring(START_TIME_HOUR_BEGIN, START_TIME_HOUR_SEPARATE);
        String timeHourOnePlace = startTime.substring(START_TIME_HOUR_SEPARATE, START_TIME_HOUR_END);
        String startHour;
        if (timeHourTensPlace.equals(ZERO)) {
            startHour = timeHourOnePlace;
        } else {
            startHour = StringUtils.getConnectStrings(timeHourTensPlace, timeHourOnePlace);
        }

        timeHourTensPlace = endTime.substring(START_TIME_HOUR_BEGIN, START_TIME_HOUR_SEPARATE);
        timeHourOnePlace = endTime.substring(START_TIME_HOUR_SEPARATE, START_TIME_HOUR_END);
        String endHour;
        if (timeHourTensPlace.equals(ZERO)) {
            endHour = timeHourOnePlace;
        } else {
            endHour = StringUtils.getConnectStrings(timeHourTensPlace, timeHourOnePlace);
        }

        String timeMinuteTensPlace = startTime.substring(START_TIME_MINUTE_BEGIN, START_TIME_MINUTE_SEPARATE);
        String timeMinuteOnePlace = startTime.substring(START_TIME_MINUTE_SEPARATE, START_TIME_MINUTE_END);
        String startMinute = StringUtils.getConnectStrings(timeMinuteTensPlace, timeMinuteOnePlace);

        timeMinuteTensPlace = endTime.substring(START_TIME_MINUTE_BEGIN, START_TIME_MINUTE_SEPARATE);
        timeMinuteOnePlace = endTime.substring(START_TIME_MINUTE_SEPARATE, START_TIME_MINUTE_END);
        String endMinute = StringUtils.getConnectStrings(timeMinuteTensPlace, timeMinuteOnePlace);

        return StringUtils.getConnectStrings(channelName, mContext.getString(R.string.home_contents_pipe),
                startHour, mContext.getString(R.string.home_contents_colon), startMinute,
                mContext.getString(R.string.home_contents_hyphen), endHour,
                mContext.getString(R.string.home_contents_colon), endMinute);
    }

    /**
     * yyyyMMddHHmmss形式から表示するデータを整形する(おすすめ番組、今日のテレビランキング).
     *
     * @param dateString yyyyMMddHHmmssデータ
     * @return 整形した日付データ
     */
    private String structDateStrings(final String dateString) {
        //曜日を取得する
        String dayOfWeek = DateUtils.getStringDayOfWeek(DateUtils.getDayOfWeek(DateUtils.getEpochTimeLink(dateString)));

        //表示するデータを整形する
        String timeMonthTensPlace = dateString.substring(START_TIME_MONTH_BEGIN, START_TIME_MONTH_SEPARATE);
        String timeMonthOnePlace = dateString.substring(START_TIME_MONTH_SEPARATE, START_TIME_MONTH_END);
        String month;
        if (timeMonthTensPlace.equals(ZERO)) {
            month = timeMonthOnePlace;
        } else {
            month = StringUtils.getConnectStrings(timeMonthTensPlace, timeMonthOnePlace);
        }

        String timeDayTensPlace = dateString.substring(START_TIME_DAY_BEGIN, START_TIME_DAY_SEPARATE);
        String timeDayOnePlace = dateString.substring(START_TIME_DAY_SEPARATE, START_TIME_DAY_END);
        String day;
        if (timeDayTensPlace.equals(ZERO)) {
            day = timeDayOnePlace;
        } else {
            day = StringUtils.getConnectStrings(timeDayTensPlace, timeDayOnePlace);
        }

        String timeHourTensPlace = dateString.substring(START_TIME_HOUR_BEGIN, START_TIME_HOUR_SEPARATE);
        String timeHourOnePlace = dateString.substring(START_TIME_HOUR_SEPARATE, START_TIME_HOUR_END);
        String hour;
        if (timeHourTensPlace.equals(ZERO)) {
            hour = timeHourOnePlace;
        } else {
            hour = StringUtils.getConnectStrings(timeHourTensPlace, timeHourOnePlace);
        }

        String timeMinuteTensPlace = dateString.substring(START_TIME_MINUTE_BEGIN, START_TIME_MINUTE_SEPARATE);
        String timeMinuteOnePlace = dateString.substring(START_TIME_MINUTE_SEPARATE, START_TIME_MINUTE_END);
        String minute = StringUtils.getConnectStrings(timeMinuteTensPlace, timeMinuteOnePlace);

        return StringUtils.getConnectStrings(month, mContext.getString(R.string.home_contents_slash), day,
                mContext.getString(R.string.home_contents_front_bracket), dayOfWeek,
                mContext.getString(R.string.home_contents_back_bracket), hour,
                mContext.getString(R.string.home_contents_colon), minute);
    }

    @Override
    public void onViewRecycled(final ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        if (viewHolder.mImage != null) {
            //サムネイルの取得が遅い時、前のViewが残っている事がある現象の対処
            viewHolder.mImage.setImageResource(android.R.color.transparent);
        }
    }

    /**
     *  開始時間と現在時刻の比較.
     * 配信開始から1週間以内のコンテンツを判定する
     * @param startDate 配信開始 "yyyy/MM/dd HH:mm:ss"
     * @return 配信開始から1週間以内かどうか
     */
    private boolean newContentsCheck(final String startDate) {
        // 現在時刻
        long nowTimeEpoch = DateUtils.getNowTimeFormatEpoch();
        long startTime = DateUtils.getSecondEpochTime(startDate);
        // 現在時刻 - 開始日時
        long differenceTime = nowTimeEpoch - startTime;
        if (differenceTime <= DateUtils.EPOCH_TIME_ONE_WEEK) {
            // 一週間未満の差であればtrue
            return true;
        }
        return false;
    }

    /**
     * コンテンツビューを初期化.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 各コンテンツを表示するViewHolder.
         *
         * @param itemView コンテンツView
         */
        public ViewHolder(final View itemView) {
            super(itemView);
        }
        /**
         * サムネイル.
         */
        ImageView mImage;
        /**
         * コンテンツタイトル.
         */
        TextView mContent;
        /**
         * コンテンツ日時.
         */
        TextView mTime;
        /**
         * Newアイコン.
         */
        ImageView mNew;
        /**
         * 順位アイコン.
         * ※ランキングコンテンツonly
         */
        TextView mRankNum;
    }
}