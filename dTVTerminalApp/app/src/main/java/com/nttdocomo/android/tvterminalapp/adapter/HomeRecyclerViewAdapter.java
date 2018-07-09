/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.app.Activity;
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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Home画面に表示するコンテンツリストのアダプタ.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    /**
     * Inflater.
     */
    private final LayoutInflater mInflater;
    /**
     * ホーム画面に表示するコンテンツデータのリスト.
     */
    private final List<ContentsData> mContentList;
    /**
     * コンテキスト.
     */
    private final Activity mContext;
    /**
     * サムネイル取得プロバイダー.
     */
    private final ThumbnailProvider mThumbnailProvider;
    /**
     * ItemClickCallback.
     */
    private ItemClickCallback mItemClickCallback;
    /**
     * コンテンツ種別を判別するためのインデックス.
     */
    private final int mIndex;
    /**
     * もっと見るフッター.
     */
    private View mFooterView;
    /**
     * チャンネル一覧.
     */
    private ChannelList mChannelList = null;
    /**
     * ダウンロード禁止判定フラグ.
     */
    private boolean mIsDownloadStop = false;
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
    private final static int HOME_CONTENTS_SORT_CHANNEL = 11;
    /**
     * カテゴリ おすすめ番組(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_RECOMMEND_PROGRAM = HOME_CONTENTS_SORT_CHANNEL + 1;
    /**
     * カテゴリ おすすめビデオ(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_RECOMMEND_VOD = HOME_CONTENTS_SORT_CHANNEL + 2;
    /**
     * カテゴリ 今日のテレビランキング(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_TODAY = HOME_CONTENTS_SORT_CHANNEL + 3;
    /**
     * カテゴリ ビデオランキング(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_VIDEO = HOME_CONTENTS_SORT_CHANNEL + 4;
    /**
     * 視聴中ビデオ(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_WATCHING_VIDEO = HOME_CONTENTS_SORT_CHANNEL + 5;
    /**
     * カテゴリ クリップ[テレビ](ホーム).
     */
    private final static int HOME_CONTENTS_SORT_TV_CLIP = HOME_CONTENTS_SORT_CHANNEL + 6;
    /**
     * カテゴリ クリップ[ビデオ](ホーム).
     */
    private final static int HOME_CONTENTS_SORT_VOD_CLIP = HOME_CONTENTS_SORT_CHANNEL + 7;
    /**
     * プレミアムビデオ(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_PREMIUM_VIDEO = HOME_CONTENTS_SORT_CHANNEL + 8;
    /**
     * レンタルビデオ(ホーム).
     */
    private final static int HOME_CONTENTS_SORT_RENTAL_VIDEO = HOME_CONTENTS_SORT_CHANNEL + 9;
    /**
     * カテゴリ 今日のテレビランキング(ランキングトップ画面).
     */
    public final static int RANKING_CONTENTES_TODAY_SORT = 25;
    /**
     * カテゴリ 週間テレビランキング(ランキングトップ画面).
     */
    private final static int RANKING_CONTENTES_WEEK_SORT = RANKING_CONTENTES_TODAY_SORT + 1;
    /**
     * カテゴリ ビデオテレビランキング(ランキングトップ画面).
     */
    private final static int RANKING_CONTENTES_VIDEO_SORT = RANKING_CONTENTES_TODAY_SORT + 2;

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
     * サービスアイコン　カテゴリーID判定条件(ひかりTVのみ).
     */
    private final static String[] categoryId_Hikari = {"01", "02", "03", "04", "05", "06", "07", "08"};
    /**
     * サービスアイコン　カテゴリーID判定条件(ひかりTV、dTV).
     */
    private final static String categoryId_Hikari_dtv = "10";
    /**
     * 再利用のビュー最大count.
     */
    private int mMaxItemCount = 0;

    /**
     * Itemクリック動作でコールバック.
     */
    public interface ItemClickCallback {
        /**
         * アイテムクリックコールバック.
         *
         * @param contentsData クリックしたデータ
         * @param detailData 遷移渡すデータ
         */
        void onItemClickCallBack(ContentsData contentsData, OtherContentsDetailData detailData);
    }

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
        mThumbnailProvider = new ThumbnailProvider(context, ThumbnailDownloadTask.ImageSizeType.HOME_LIST);
    }

    /**
     * コールバックを設定.
     *
     * @param mItemClickCallback コールバック
     */
    public void setOnItemClickCallBack(final ItemClickCallback mItemClickCallback) {
        this.mItemClickCallback = mItemClickCallback;
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
    public void setChannnelList(final ChannelList channelList) {
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
        viewHolder.mHyphen = view.findViewById(R.id.home_main_recyclerview_item_hyphen);
        viewHolder.mChannel = view.findViewById(R.id.home_main_recyclerview_item_tv_channel_or_missed);
        viewHolder.mNew = view.findViewById(R.id.home_main_recyclerview_item_iv_new);
        viewHolder.mRankNum = view.findViewById(R.id.home_main_recyclerview_item_iv_rank_num);
        viewHolder.mServiceIconFirst = view.findViewById(R.id.home_main_recyclerview_item_iv_service_icon_first);
        viewHolder.mServiceIconSecond = view.findViewById(R.id.home_main_recyclerview_item_iv_service_icon_second);
        mMaxItemCount++;
        return viewHolder;
    }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == TYPE_FOOTER) {
            return;
        }
        final ContentsData contentsData = mContentList.get(i);
        String title = contentsData.getTitle();
        String rankNum = contentsData.getRank();
        Boolean newFlag = newContentsCheck(contentsData);
        if (viewHolder.mTime != null) {
            viewHolder.mTime.setVisibility(View.GONE);
        }
        if (viewHolder.mHyphen != null) {
            // チャンネル名表示があった場合に
            viewHolder.mHyphen.setVisibility(View.GONE);
        }
        if (viewHolder.mChannel != null) {
            viewHolder.mChannel.setVisibility(View.GONE);
        }
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
                setRecommendServiceIcon(contentsData, viewHolder);
                break;
            case HOME_CONTENTS_SORT_RECOMMEND_VOD:
                setRecommendVodInfo(contentsData, viewHolder);
                setRecommendServiceIcon(contentsData, viewHolder);
                break;
            case HOME_CONTENTS_SORT_TODAY:
            case RANKING_CONTENTES_TODAY_SORT:
            case RANKING_CONTENTES_WEEK_SORT:
            case HOME_CONTENTS_SORT_TV_CLIP:
            case HOME_CONTENTS_SORT_WATCHING_VIDEO:
            case HOME_CONTENTS_SORT_VOD_CLIP:
            case HOME_CONTENTS_SORT_PREMIUM_VIDEO:
            case HOME_CONTENTS_SORT_RENTAL_VIDEO:
            case RANKING_CONTENTES_VIDEO_SORT:
            case HOME_CONTENTS_SORT_VIDEO:
                //配信期限の設定
                ContentUtils.setPeriodText(mContext, viewHolder.mTime, contentsData);
                ContentUtils.setChannelNameOrMissedText(mContext, viewHolder.mHyphen, viewHolder.mChannel, contentsData);
                break;
            default:
                if (viewHolder.mTime != null) {
                    viewHolder.mTime.setVisibility(View.GONE);
                }
                if (viewHolder.mHyphen != null) {
                    viewHolder.mHyphen.setVisibility(View.GONE);
                }
                if (viewHolder.mChannel != null) {
                    viewHolder.mChannel.setVisibility(View.GONE);
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
        if (newFlag) {
            viewHolder.mNew.setVisibility(View.VISIBLE);
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
            if (!mIsDownloadStop) {
                viewHolder.mImage.setTag(thumbnail);
                mThumbnailProvider.setMaxQueueCount(mMaxItemCount);
                Bitmap bitmap = mThumbnailProvider.getThumbnailImage(viewHolder.mImage, thumbnail);
                if (bitmap != null) {
                    viewHolder.mImage.setImageBitmap(bitmap);
                }
            }
        } else {
            //URLがない場合はサムネイル取得失敗の画像を表示
            viewHolder.mImage.setImageResource(R.mipmap.error_scroll);
        }
        String recommendFlg;
        if (mIndex == HOME_CONTENTS_SORT_RECOMMEND_PROGRAM || mIndex == HOME_CONTENTS_SORT_RECOMMEND_PROGRAM + 1) {
            recommendFlg = ContentUtils.RECOMMEND_INFO_BUNDLE_KEY;
        } else {
            recommendFlg = ContentUtils.PLALA_INFO_BUNDLE_KEY;
        }
        final OtherContentsDetailData detailData = DataConverter.getOtherContentsDetailData(contentsData, recommendFlg);
        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mItemClickCallback != null) {
                    mItemClickCallback.onItemClickCallBack(contentsData, detailData);
                }
            }
        });
    }

    /**
     * レコメンドしているコンテンツに対してサービスアイコンを表示する.
     *
     * @param contentsData コンテンツデータ
     * @param viewHolder ViewHolder
     */
    private void setRecommendServiceIcon(final ContentsData contentsData, final ViewHolder viewHolder) {
        String serviceId = contentsData.getServiceId();
        viewHolder.mServiceIconFirst.setVisibility(View.GONE);
        viewHolder.mServiceIconSecond.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(serviceId) && DataBaseUtils.isNumber(serviceId)) {
            String categoryId = contentsData.getCategoryId();
            switch (Integer.parseInt(serviceId)) {
                //ひかりTV
                case ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID:
                    List<String> list = Arrays.asList(categoryId_Hikari);
                    if (list.contains(categoryId)) {
                        viewHolder.mServiceIconFirst.setVisibility(View.VISIBLE);
                        viewHolder.mServiceIconFirst.setImageResource(R.mipmap.label_service_hikari);
                    } else if (categoryId_Hikari_dtv.equals(categoryId)) {
                        viewHolder.mServiceIconFirst.setVisibility(View.VISIBLE);
                        viewHolder.mServiceIconSecond.setVisibility(View.VISIBLE);
                        viewHolder.mServiceIconSecond.setImageResource(R.mipmap.label_service_dtv_white);
                        viewHolder.mServiceIconFirst.setImageResource(R.mipmap.label_service_hikari);
                    }
                    break;
                //dTV
                case ContentUtils.DTV_CONTENTS_SERVICE_ID:
                    viewHolder.mServiceIconFirst.setVisibility(View.VISIBLE);
                    viewHolder.mServiceIconFirst.setImageResource(R.mipmap.label_service_dtv);
                    break;
                //アニメ
                case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID:
                    viewHolder.mServiceIconFirst.setVisibility(View.VISIBLE);
                    viewHolder.mServiceIconFirst.setImageResource(R.mipmap.label_service_danime);
                    break;
                //dTVチャンネル
                case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                    viewHolder.mServiceIconFirst.setVisibility(View.VISIBLE);
                    viewHolder.mServiceIconFirst.setImageResource(R.mipmap.label_service_dch);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Now On Airの2段目に表示する情報を設定する.
     *
     * @param contentsData コンテンツデータ
     * @param viewHolder ViewHolder
     */
    private void setNowOnAirInfo(final ContentsData contentsData, final ViewHolder viewHolder) {
        String startTime = String.valueOf(DateUtils.getEpochTime(contentsData.getPublishStartDate()) / 1000);
        String endTime = String.valueOf(DateUtils.getEpochTime(contentsData.getPublishEndDate()) / 1000);
        String channelName = contentsData.getChannelName();
        ContentUtils.ContentsType contentsType = ContentUtils.getContentsTypeByPlala(contentsData.getDispType(),
                contentsData.getTvService(), contentsData.getContentsType(), contentsData.getAvailEndDate(),
                contentsData.getVodStartDate(), contentsData.getVodEndDate(), contentsData.getEstFlg(),
                contentsData.getChsVod());
        String date;
        //channelNameが取得成功したときのみ区切り文字を追加する
        if (channelName != null && !channelName.isEmpty()) {
            channelName = StringUtils.getConnectStrings(channelName, mContext.getString(R.string.home_contents_pipe));
        } else {
            channelName = "";
        }

        if (contentsType == ContentUtils.ContentsType.TV || contentsType == ContentUtils.ContentsType.OTHER) {
            date = structDateStrings(DateUtils.formatEpochToStringOpeLog(Long.parseLong(startTime)),
                    DateUtils.formatEpochToStringOpeLog(Long.parseLong(endTime)), channelName);
        } else {
            date = StringUtils.getConnectStrings(channelName,
                    DateUtils.addDateLimit(mContext, contentsData, contentsType));
        }

        //表示情報がすべて取得できないときは非表示にする
        if (date == null || date.isEmpty()) {
            viewHolder.mTime.setVisibility(View.GONE);
        } else {
            viewHolder.mTime.setVisibility(View.VISIBLE);
            viewHolder.mTime.setText(date);
        }
    }

    /**
     * おすすめ番組の2段目に表示する情報を設定する.
     *
     * @param contentsData コンテンツデータ
     * @param viewHolder ViewHolder
     */
    private void setRecommendInfo(final ContentsData contentsData, final ViewHolder viewHolder) {
        String startViewing = contentsData.getStartViewing();
        String channelName = contentsData.getChannelName();
        String date = DateUtils.getContentsDateString(startViewing);
        if (!TextUtils.isEmpty(channelName)) {
            viewHolder.mTime.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(date)) {
                viewHolder.mTime.setText(StringUtils.getConnectStrings(date, mContext.getString(R.string.home_contents_hyphen),
                        channelName));
            } else {
                viewHolder.mTime.setText(channelName);
            }
        } else if (!TextUtils.isEmpty(date)) {
            viewHolder.mTime.setVisibility(View.VISIBLE);
            viewHolder.mTime.setText(date);
        }
    }

    /**
     * おすすめビデオの2段目に表示する情報を設定する.
     *
     * @param contentsData コンテンツデータ
     * @param viewHolder ViewHolder
     */
    private void setRecommendVodInfo(final ContentsData contentsData, final ViewHolder viewHolder) {
        String date = "";
        //配信前　m/d（曜日）から
        if (DateUtils.isBefore(contentsData.getStartViewing())) {
            date = DateUtils.getContentsDateString(mContext, contentsData.getStartViewing(), true);
        } else {
            // m/d（曜日）まで
            if (DateUtils.isIn31Day(contentsData.getEndViewing())) {
                date = DateUtils.getContentsDateString(mContext, contentsData.getEndViewing(), false);
            }
        }
        if (!TextUtils.isEmpty(date)) {
            viewHolder.mTime.setVisibility(View.VISIBLE);
            viewHolder.mTime.setText(date);
        }
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

       return StringUtils.getConnectStrings(channelName, startHour, mContext.getString(R.string.home_contents_colon), startMinute,
                mContext.getString(R.string.home_contents_hyphen), endHour,
                mContext.getString(R.string.home_contents_colon), endMinute);
    }

    @Override
    public void onViewRecycled(final ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        if (viewHolder.mImage != null) {
            //サムネイルの取得が遅い時、前のViewが残っている事がある現象の対処
            viewHolder.mImage.setImageResource(R.mipmap.loading_scroll);
        }
    }

    /**
     * 開始時間と現在時刻の比較.
     * 配信開始から1週間以内のコンテンツを判定する
     * @param contentsData 配信開始 "yyyy/MM/dd HH:mm:ss"
     * @return 配信開始から1週間以内かどうか
     */
    private boolean newContentsCheck(final ContentsData contentsData) {
        switch (mIndex) {
            case HOME_CONTENTS_SORT_CHANNEL:
            case HOME_CONTENTS_SORT_RECOMMEND_PROGRAM:
            case HOME_CONTENTS_SORT_TODAY:
            case HOME_CONTENTS_SORT_VIDEO:
            case HOME_CONTENTS_SORT_TV_CLIP:
            case RANKING_CONTENTES_TODAY_SORT:
            case RANKING_CONTENTES_WEEK_SORT:
            case RANKING_CONTENTES_VIDEO_SORT:
                return false;
            case HOME_CONTENTS_SORT_RECOMMEND_VOD:
                String startViewing = contentsData.getStartViewing();
                return !TextUtils.isEmpty(startViewing) && DateUtils.isInOneWeek(startViewing);
            default:
                String startPublishDate = contentsData.getPublishStartDate();
                if (!TextUtils.isEmpty(startPublishDate) && DataBaseUtils.isNumber(startPublishDate)) {
                    long startDate = Long.parseLong(startPublishDate);
                    return DateUtils.isInOneWeek(startDate);
                } else {
                    return false;
                }
        }
    }

    /**
     * コンテンツビューを初期化.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 各コンテンツを表示するViewHolder.
         *
         * @param itemView コンテンツView
         */
        ViewHolder(final View itemView) {
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
         * 区切り文字（ハイフン）.
         */
        TextView mHyphen;
        /**
         * チャンネル名、見逃し.
         */
        TextView mChannel;
        /**
         * Newアイコン.
         */
        ImageView mNew;
        /**
         * 順位アイコン.
         * ※ランキングコンテンツonly
         */
        TextView mRankNum;
        /**
         * おすすめサービスアイコン.
         */
        ImageView mServiceIconFirst;
        /**
         * おすすめサービスアイコン.
         */
        ImageView mServiceIconSecond;
    }

    /**
     * コンテンツのServiceIDとServiceIDが一致するチャンネル名を取得する.
     *
     * @param serviceId コンテンツのServiceID
     * @return チャンネル名
     */
    private String getChannelName(final String serviceId) {
        if (serviceId == null) {
            return "";
        }
        if (mChannelList != null) {
            List<HashMap<String, String>> list = mChannelList.getChannelList();
            for (HashMap<String, String> hashMap : list) {
                if (!TextUtils.isEmpty(hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID))) {
                    if (serviceId.equals(hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID))) {
                        return hashMap.get(JsonConstants.META_RESPONSE_TITLE);
                    }
                }
            }
        }
        return "";
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsDownloadStop = true;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.stopConnect();
            mThumbnailProvider.removeAllMemoryCache();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsDownloadStop = false;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.enableConnect();
        }
    }
}