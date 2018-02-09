/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.List;

import static com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter.ActivityTypeItem.TYPE_RECORDING_RESERVATION_LIST;

/**
 * コンテンツ一覧系共通リストアダプター.
 */
public class ContentsAdapter extends BaseAdapter implements OnClickListener {

    /**
     * 各Activityインスタンス.
     */
    private Context mContext = null;
    /**
     * リスト用データソース.
     */
    private List<ContentsData> mListData = null;
    /**
     * サムネイル取得用プロバイダー.
     */
    private ThumbnailProvider mThumbnailProvider = null;
    /**
     * 表示項目のタイプ.
     */
    private ActivityTypeItem mType;
    /**
     * ビューの生成.
     */
    private LayoutInflater mInflater;
    /**
     * 評価基準値.
     */
    private final static int NUM_STARS_TOTAL = 5;
    /**
     * サムネイル幅さ display3分の1.
     */
    private final static int THUMBNAIL_WIDTH = 3;
    /**
     * サムネイル高さ サムネイル幅さ2分の1.
     */
    private final static int THUMBNAIL_HEIGHT = 2;
    /**
     * サムネイルmarginleft.
     */
    private final static int THUMBNAIL_MARGINLEFT = 16;
    /**
     * サムネイルmargintop.
     */
    private final static int THUMBNAIL_MARGINEND = 10;
    /**
     * サムネイルmarginright.
     */
    private final static int THUMBNAIL_MARGINRIGHT = 16;
    /**
     * サムネイルmarginbottom.
     */
    private final static int THUMBNAIL_MARGINBOTTOM = 10;
    /**
     * status　margintop.
     */
    private final static int STATUS_MARGINTOP17 = 17;
    /**
     * status　margintop.
     */
    private final static int STATUS_MARGINTOP10 = 10;

    /**
     * 番組タイトル margintop.
     */
    private final static int TITLE_MARGINTOP17 = 17;

    /**
     * クリップアイコンmargintop.
     */
    private final static int CLIP_MARGINTOP35 = 35;
    /**
     * 時刻テキストサイズ.
     */
    private final static int TIME_TEXT_SIZE = 12;
    /**
     * margin0.
     */
    private final static int THUMBNAIL_MARGIN0 = 0;
    /**
     * ライン高さ.
     */
    private final static int LINE_HEIGHT = 1;
    /**
     * コピー残り回数.
     */
    private final static int ALLOWED_USE = 0;

    /**
     * 受付アイコンマージン.
     */
    private final static int RECEPTION_MARGINTOP30 = 30;

    /**
     * クリップ登録済み判定用.
     */
    private final static String ACTIVE_CLIP_DISPLAY = "1";

    /**
     * クリップ未登録判定用.
     */
    private final static String OPACITY_CLIP_DISPLAY = "0";

    /**
     * ダウンロードStatus種別.
     */
    public final static int DOWNLOAD_STATUS_ALLOW = 0;

    /**
     * ダウンロードStatus種別.
     */
    public final static int DOWNLOAD_STATUS_LOADING = 1;

    /**
     * ダウンロードStatus種別.
     */
    public final static int DOWNLOAD_STATUS_COMPLETED = 2;
    /**
     * アイテムposition.
     */
    private final static int CONTENT_POSITION_ONE = 0;
    /**
     * アイテムposition.
     */
    private final static int CONTENT_POSITION_TWO = 1;
    /**
     * アイテムposition.
     */
    private final static int CONTENT_POSITION_THREE = 2;

    /**
     * ダウンロードcallback.
     */
    private DownloadCallback mDownloadCallback;

    /**
     * 機能
     * 共通アダプター使う.
     */
    public enum ActivityTypeItem {
        /**
         * 今日のテレビランキング.
         */
        TYPE_DAILY_RANK,
        /**
         * 週間テレビランキング.
         */
        TYPE_WEEKLY_RANK,
        /**
         * ビデオランキング.
         */
        TYPE_VIDEO_RANK,
        /**
         * レンタル.
         */
        TYPE_RENTAL_RANK,
        /**
         * 録画予約一覧.
         */
        TYPE_RECORDING_RESERVATION_LIST,
        /**
         * ビデオコンテンツ一覧.
         */
        TYPE_VIDEO_CONTENT_LIST,
        /**
         * 録画番組一覧.
         */
        TYPE_RECORDED_LIST,
        /**
         * 視聴中ビデオ一覧.
         */
        TYPE_WATCHING_VIDEO_LIST,
        /**
         * TVタブ(クリップ).
         */
        TYPE_CLIP_LIST_MODE_TV,
        /**
         * ビデオタブ(クリップ).
         */
        TYPE_CLIP_LIST_MODE_VIDEO,
        /**
         * 検索.
         */
        TYPE_SEARCH_LIST,
        /**
         * // コンテンツ詳細チャンネル一覧.
         */
        TYPE_CONTENT_DETAIL_CHANNEL_LIST
    }

    /**
     * コンストラクタ.
     *
     * @param mContext Activity
     * @param listData リストデータ
     * @param type     　項目コントロール
     */
    public ContentsAdapter(final Context mContext, final List<ContentsData> listData, final ActivityTypeItem type) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mListData = listData;
        this.mType = type;
        mThumbnailProvider = new ThumbnailProvider(mContext);
    }

    /**
     * コンストラクタ.
     *
     * @param mContext Activity
     * @param listData リストデータ
     * @param type     　項目コントロール
     * @param callback callback
     */
    public ContentsAdapter(final Context mContext, final List<ContentsData> listData,
                           final ActivityTypeItem type, final DownloadCallback callback) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mListData = listData;
        this.mType = type;
        mThumbnailProvider = new ThumbnailProvider(mContext);
        mDownloadCallback = callback;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(final int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View view, final ViewGroup parent) {
        ViewHolder holder = null;

        View contentView = view;
        //ビューの再利用
        if (contentView == null) {
            holder = new ViewHolder();
            contentView = mInflater.inflate(R.layout.item_common_result, parent, false);
            holder = setListItemPattern(holder, contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        setShowDataVisibility(holder);
        //各アイテムデータを取得
        final ContentsData listContentInfo = mListData.get(position);
        // アイテムデータを設定する
        setContentsData(holder, listContentInfo);

        if (!ActivityTypeItem.TYPE_RECORDED_LIST.equals(mType)) {
            //クリップボタン処理を設定する
            final ClipRequestData requestData = listContentInfo.getRequestData();
            final ImageView clipButton = contentView.findViewById(R.id.item_common_result_clip_tv);
            listContentInfo.setClipButton(clipButton);
            clipButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、画像比較でクリップ済/未を判定する
                    Bitmap clipButtonBitmap = ((BitmapDrawable) clipButton.getBackground()).getBitmap();
                    Bitmap activeClipBitmap = ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(),
                            R.mipmap.icon_circle_active_clip, null)).getBitmap();
                    if (clipButtonBitmap.equals(activeClipBitmap)) {
                        requestData.setClipStatus(true);
                    } else {
                        requestData.setClipStatus(false);
                    }
                    ((BaseActivity) mContext).sendClipRequest(requestData, clipButton);
                }
            });
        } else {
            setDownloadStatus(holder, listContentInfo, position);
        }

        RelativeLayout.LayoutParams layoutParamsClip = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int textMargin;
        int clipMargin;
        switch (mType) {
            case TYPE_DAILY_RANK:
            case TYPE_WEEKLY_RANK:
            case TYPE_CLIP_LIST_MODE_TV: //TVタブ(クリップ)
            case TYPE_SEARCH_LIST:
            case TYPE_CONTENT_DETAIL_CHANNEL_LIST:
                textMargin = STATUS_MARGINTOP17;
                clipMargin = CLIP_MARGINTOP35;
                setTextMargin(textMargin, holder, contentView);
                setClipMargin(clipMargin, contentView);
                break;
            case TYPE_VIDEO_RANK:
            case TYPE_VIDEO_CONTENT_LIST:
            case TYPE_WATCHING_VIDEO_LIST:
            case TYPE_CLIP_LIST_MODE_VIDEO:
                textMargin = TITLE_MARGINTOP17;
                setTextMargin(textMargin, holder, contentView);
                layoutParamsClip.addRule(RelativeLayout.ALIGN_PARENT_END, R.id.parent_relative_layout);
                layoutParamsClip.addRule(RelativeLayout.CENTER_VERTICAL);
                contentView.findViewById(R.id.item_common_result_show_status_area).setLayoutParams(layoutParamsClip);
                break;
            case TYPE_RENTAL_RANK:
                textMargin = STATUS_MARGINTOP10;
                setTextMargin(textMargin, holder, contentView);
                layoutParamsClip.addRule(RelativeLayout.ALIGN_PARENT_END, R.id.parent_relative_layout);
                layoutParamsClip.addRule(RelativeLayout.CENTER_VERTICAL);
                contentView.findViewById(R.id.item_common_result_show_status_area).setLayoutParams(layoutParamsClip);
                break;
            case TYPE_RECORDING_RESERVATION_LIST:
                //録画予約一覧用余白設定
                textMargin = STATUS_MARGINTOP17;
                setTextAllMargin(THUMBNAIL_MARGINLEFT, textMargin, THUMBNAIL_MARGINBOTTOM,
                        textMargin, holder, contentView);
                clipMargin = RECEPTION_MARGINTOP30;
                setClipMargin(clipMargin, contentView);
                break;
            //ENUMの値をswitch分岐すると、全ての値を書かないとアナライザーがエラーを出すので、caseを追加
            case TYPE_RECORDED_LIST:
            default:
                break;
        }
        holder.tv_rank.setBackgroundResource(R.drawable.label_ranking_other);
        if (holder.tv_rank.getVisibility() == View.VISIBLE) {
            if (position == CONTENT_POSITION_ONE) {
                holder.tv_rank.setBackgroundResource(R.drawable.label_ranking_1);
            }
            holder.tv_rank.setTextColor(ContextCompat.getColor(mContext, R.color.black_text));
            if (position == CONTENT_POSITION_TWO) {
                holder.tv_rank.setBackgroundResource(R.drawable.label_ranking_2);
            }
            if (position == CONTENT_POSITION_THREE) {
                holder.tv_rank.setBackgroundResource(R.drawable.label_ranking_3);
            }
            if (position >= CONTENT_POSITION_TWO) {
                holder.tv_rank.setTextColor(ContextCompat.getColor(mContext, R.color.white_text));
            }
        }
        return contentView;
    }

    /**
     * クリップボタンレイアウト設定.
     *
     * @param clipMargin マージン
     * @param view       View
     */
    private void setClipMargin(final int clipMargin, final View view) {
        DisplayMetrics DisplayMetrics = mContext.getResources().getDisplayMetrics();
        float density = DisplayMetrics.density;

        RelativeLayout.LayoutParams layoutParamsClip = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsClip.setMargins(THUMBNAIL_MARGIN0 * (int) density, clipMargin * (int) density,
                THUMBNAIL_MARGIN0 * (int) density, THUMBNAIL_MARGIN0 * (int) density);
        layoutParamsClip.addRule(RelativeLayout.ALIGN_PARENT_END, R.id.parent_relative_layout);
        view.findViewById(R.id.item_common_result_show_status_area).setLayoutParams(layoutParamsClip);
    }

    /**
     * TextViewマージン設定.
     *
     * @param textMargin マージン
     * @param holder     ViewHolder
     * @param view       View
     */
    private void setTextMargin(final int textMargin, final ViewHolder holder, final View view) {
        DisplayMetrics DisplayMetrics = mContext.getResources().getDisplayMetrics();
        float density = DisplayMetrics.density;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(THUMBNAIL_MARGIN0 * (int) density, textMargin * (int) density,
                THUMBNAIL_MARGIN0 * (int) density, THUMBNAIL_MARGIN0 * (int) density);
        layoutParams.setMarginStart(THUMBNAIL_MARGINLEFT * (int) density);
        layoutParams.addRule(RelativeLayout.START_OF, R.id.item_common_result_show_status_area);
        layoutParams.addRule(RelativeLayout.END_OF, R.id.item_common_result_thumbnail_rl);
        if (holder.tv_clip.getVisibility() == View.GONE) {
            layoutParams.setMarginEnd(THUMBNAIL_MARGINEND * (int) density);
        } else {
            layoutParams.setMarginEnd(THUMBNAIL_MARGIN0 * (int) density);
        }
        view.findViewById(R.id.item_common_result_contents).setLayoutParams(layoutParams);
        holder.tv_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TIME_TEXT_SIZE);
    }

    /**
     * 4方向のマージンをセットする.
     *
     * @param leftMargin   左マージン
     * @param topMargin    上マージン
     * @param rightMargin  右マージン
     * @param bottomMargin 下マージン
     * @param holder       ビューの集合体
     * @param view         マージンを指定するビュー
     */
    private void setTextAllMargin(
            final int leftMargin, final int topMargin, final int rightMargin,
            final int bottomMargin, final ViewHolder holder, final View view) {
        //解像度の倍率を取得する
        DisplayMetrics DisplayMetrics = mContext.getResources().getDisplayMetrics();
        float density = DisplayMetrics.density;

        //レイアウト情報を取得
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        //マージンの数値を算出して格納する
        layoutParams.setMargins(leftMargin * (int) density, topMargin * (int) density,
                rightMargin * (int) density, bottomMargin * (int) density);

        //表示開始位置をステータスエリアに合わせる
        layoutParams.addRule(RelativeLayout.START_OF, R.id.item_common_result_show_status_area);

        //表示終了位置をアイコンに合わせる
        layoutParams.addRule(RelativeLayout.END_OF, R.id.item_common_result_thumbnail_rl);

        //レイアウトをビューに設定する
        view.findViewById(R.id.item_common_result_contents).setLayoutParams(layoutParams);

        //時間表示のフォントサイズを指定する
        holder.tv_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TIME_TEXT_SIZE);
    }

    /**
     * 各コンテンツデータを設定.
     *
     * @param holder          ビューの集合
     * @param listContentInfo 行データー
     */
    private void setContentsData(final ViewHolder holder, final ContentsData listContentInfo) {
        DTVTLogger.start();
        setRankData(holder, listContentInfo);
        setTitleData(holder, listContentInfo);
        setTimeData(holder, listContentInfo);
        setThumbnailData(holder, listContentInfo);
        setRatStarData(holder, listContentInfo);
        setRecodingReservationStatusData(holder, listContentInfo);
        setChannelName(holder, listContentInfo);
        setRedordedDownloadIcon(holder, listContentInfo);
        if(ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST.equals(mType)){
            setSubTitle(holder, listContentInfo);
        }
    }

    /**
     * データの設定（ランク）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setRankData(final ViewHolder holder, final ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getRank())) { //ランク
            holder.tv_rank.setText(listContentInfo.getRank());
        }
    }

    /**
     * データの設定（タイトル）.
     *
     * @param holder          　ViewHolder
     * @param listContentInfo 　ContentsData
     */
    private void setTitleData(final ViewHolder holder, final ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getTitle())) { //タイトル
            String title = listContentInfo.getTitle() + mContext.getResources().getString(R.string.common_ranking_enter);
            holder.tv_title.setText(title);
        }
    }

    /**
     * データの設定（開始時刻）.
     *
     * @param holder          　ViewHolder
     * @param listContentInfo 　ContentsData
     */
    private void setTimeData(final ViewHolder holder, final ContentsData listContentInfo) {
        //TODO:基本的には日付はすべてエポック秒で扱い、表示する際にYYYY/MM/DD形式に変換する方針にする(今日のテレビランキング、週間ランキングと同じ方式に統一))
        if (!TextUtils.isEmpty(listContentInfo.getTime())) { //時間
            switch (mType) {
                case TYPE_DAILY_RANK: // 今日のテレビランキング
                case TYPE_WEEKLY_RANK: // 週間ランキング
                    holder.tv_time.setText(DateUtils.getRecordShowListItem(Long.parseLong(listContentInfo.getTime())));
                    break;
                case TYPE_VIDEO_RANK: // ビデオランキング
                case TYPE_RENTAL_RANK: // レンタル一覧
                case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
                case TYPE_WATCHING_VIDEO_LIST: //視聴中ビデオ一覧
                case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                case TYPE_RECORDED_LIST: // 録画番組一覧
                case TYPE_CLIP_LIST_MODE_VIDEO: //ビデオタブ(クリップ)
                case TYPE_CLIP_LIST_MODE_TV: //TVタブ(クリップ)
                case TYPE_SEARCH_LIST:
                case TYPE_CONTENT_DETAIL_CHANNEL_LIST: // コンテンツ詳細チャンネル一覧
                    holder.tv_time.setText(listContentInfo.getTime());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * データの設定（評価）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setRatStarData(final ViewHolder holder, final ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getRatStar())) { //評価
            holder.rb_rating.setNumStars(NUM_STARS_TOTAL);
            holder.rb_rating.setRating(Float.parseFloat(listContentInfo.getRatStar()));
            holder.tv_rating_num.setText(listContentInfo.getRatStar());
        }
    }

    /**
     * データの設定（サムネイル）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setThumbnailData(final ViewHolder holder, final ContentsData listContentInfo) {
        //スクロール時にリサイクル前の画像が表示され続けないように一旦画像を消去する
        holder.iv_thumbnail.setImageResource(0);
        if (!TextUtils.isEmpty(listContentInfo.getThumURL())) { //サムネイル
            holder.rl_thumbnail.setVisibility(View.VISIBLE);
            holder.iv_thumbnail.setTag(listContentInfo.getThumURL());
            Bitmap thumbnailImage = mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, listContentInfo.getThumURL());
            if (thumbnailImage != null) {
                holder.iv_thumbnail.setImageBitmap(thumbnailImage);
            }
        } else {
            //URLがない場合はサムネイル取得失敗の画像を表示
            holder.iv_thumbnail.setBackgroundResource(R.drawable.error_list);
        }
    }

    /**
     * データの設定（録画予約ステータス）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setRecodingReservationStatusData(final ViewHolder holder, final ContentsData listContentInfo) {
        DTVTLogger.start("status = " + listContentInfo.getRecordingReservationStatus());
        if (holder.tv_recording_reservation != null) { // 録画予約ステータス
            int status = listContentInfo.getRecordingReservationStatus();
            switch (status) {
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_REFLECTS_WAITING:
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_DURING_REFLECT:
                    // 受付中
                    holder.tv_recording_reservation.setVisibility(View.VISIBLE);
                    holder.tv_recording_reservation.setTextColor(
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_text_color_red));
                    holder.tv_recording_reservation.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_white));
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accepting);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_REFLECT_FAILURE:
                    // 受付失敗
                    holder.tv_recording_reservation.setVisibility(View.VISIBLE);
                    holder.tv_recording_reservation.setTextColor(
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_text_color_white));
                    holder.tv_recording_reservation.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_red));
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accept_failure);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT:
                    // 受信完了
                    holder.tv_recording_reservation.setVisibility(View.GONE);
                    holder.tv_recording_reservation.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_black));
                    break;
                default:
                    holder.tv_recording_reservation.setVisibility(View.GONE);
                    holder.tv_recording_reservation.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_black));
                    break;
            }
            DTVTLogger.end();
        }
    }

    /**
     * ダウンロードアイコンの設定（録画番組用チャンネル名）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setRedordedDownloadIcon(final ViewHolder holder, final ContentsData listContentInfo) {
        DTVTLogger.start();
        //TODO:録画予約一覧等、クリップボタンを表示しない画面はここで外す
        if (!mType.equals(TYPE_RECORDING_RESERVATION_LIST)) {
            BaseActivity baseActivity = new BaseActivity();
            if (holder.tv_clip != null) {
                //Boolean contentsFlag = baseActivity.getDownloadContentsFalag();
                int downloadFlg = listContentInfo.getDownloadFlg();
                if (downloadFlg != -1) {
                    // ダウンロード済み
                    holder.tv_clip.setVisibility(View.VISIBLE);
                    holder.tv_clip.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.ranking_list_border));
                } else {
                    // 未ダウンロード
                    int allowedUse = listContentInfo.getAllowedUse();
                    if (ALLOWED_USE == allowedUse) {
                        // ダウンロード回数無 && ダウンロード禁止
                        holder.tv_clip.setVisibility(View.GONE);
                    } else {
                        // ダウンロード回数の残数有り
                        holder.tv_clip.setVisibility(View.VISIBLE);
                        holder.tv_clip.setBackgroundColor(
                                ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_red));
                    }
                }
            }
        }

        if (!ActivityTypeItem.TYPE_RECORDED_LIST.equals(mType)) {
            setClipButton(holder, listContentInfo);
        }

        DTVTLogger.end();
    }

    /**
     * データの設定（チャンネル名）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setChannelName(final ViewHolder holder, final ContentsData listContentInfo) {
        DTVTLogger.start();
        if (!TextUtils.isEmpty(listContentInfo.getChannelName())) { //ランク
            holder.tv_recorded_hyphen.setVisibility(View.VISIBLE);
            holder.tv_recorded_ch_name.setVisibility(View.VISIBLE);
            holder.tv_recorded_ch_name.setText(listContentInfo.getChannelName());
            if(mType == ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST){
                holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.record_download_status_color));
            }
        } else {
            //TODO: 仕様不定の為仮実装
            //TODO:チャンネル名の出所が不明なので、ダミー値を指定
            if (mType == ActivityTypeItem.TYPE_DAILY_RANK || mType == ActivityTypeItem.TYPE_WEEKLY_RANK
                    || mType == ActivityTypeItem.TYPE_CLIP_LIST_MODE_TV) {
                holder.tv_recorded_hyphen.setVisibility(View.VISIBLE);
                holder.tv_recorded_ch_name.setVisibility(View.VISIBLE);
                holder.tv_recorded_ch_name.setText("ダミー");
            }
            if(mType == ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST){
                holder.tv_recorded_hyphen.setVisibility(View.GONE);
                holder.tv_recorded_ch_name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * データの設定（サブタイトル）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setSubTitle(final ViewHolder holder, final ContentsData listContentInfo){
        if(!TextUtils.isEmpty(listContentInfo.getSubTitle())){
            holder.tv_sub_title.setVisibility(View.VISIBLE);
            holder.tv_sub_title.setText(listContentInfo.getSubTitle());
            holder.tv_sub_title.setOnClickListener(null);
        } else {
            holder.tv_sub_title.setVisibility(View.GONE);
        }
    }

    /**
     * 共通Itemの設定.
     *
     * @param holder ViewHolder
     * @param view   View
     * @return 初期化済みViewHolder
     */
    private ViewHolder setCommonListItem(final ViewHolder holder, final View view) {
        DTVTLogger.start();
        holder.rl_thumbnail = view.findViewById(R.id.item_common_result_thumbnail_rl);
        holder.iv_thumbnail = view.findViewById(R.id.item_common_result_thumbnail_iv);
        holder.tv_clip = view.findViewById(R.id.item_common_result_clip_tv);
        holder.tv_rank = view.findViewById(R.id.item_common_result_rank_num);
        holder.tv_time = view.findViewById(R.id.item_common_result_content_time);
        holder.tv_title = view.findViewById(R.id.item_common_result_content_title);
        holder.ll_rating = view.findViewById(R.id.item_common_result_content_rating);
        holder.rb_rating = view.findViewById(R.id.item_common_result_content_rating_star);
        holder.tv_rating_num = view.findViewById(R.id.item_common_result_content_rating_num);
        holder.tv_line = view.findViewById(R.id.item_common_result_line);
        return holder;
    }

    /**
     * Itemのパターンを設定.
     *
     * @param holder ViewHolder
     * @param view   View
     * @return 各画面特化ViewHolder
     */
    private ViewHolder setListItemPattern(final ViewHolder holder, final View view) {
        DTVTLogger.start();
        // TODO 録画予約一覧以外のパターンも共通項目以外を抽出し、修正する
        ViewHolder viewHolder = holder;
        viewHolder = setCommonListItem(viewHolder, view);
        switch (mType) {
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
            case TYPE_WATCHING_VIDEO_LIST: //視聴中ビデオ一覧
            case TYPE_CLIP_LIST_MODE_VIDEO: //ビデオタブ(クリップ)
            case TYPE_SEARCH_LIST:
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                viewHolder.tv_recording_reservation =
                        view.findViewById(R.id.item_common_result_recording_reservation_status);
                viewHolder.tv_recorded_hyphen = view.findViewById(R.id.item_common_result_recorded_content_hyphen);
                viewHolder.tv_recorded_ch_name = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
                break;
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
            case TYPE_RECORDED_LIST: // 録画番組一覧
            case TYPE_CLIP_LIST_MODE_TV: //TVタブ(クリップ)
                viewHolder.tv_recorded_hyphen = view.findViewById(R.id.item_common_result_recorded_content_hyphen);
                viewHolder.tv_recorded_ch_name = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
                break;
            case TYPE_CONTENT_DETAIL_CHANNEL_LIST: // コンテンツ詳細チャンネル一覧
                holder.tv_recorded_hyphen = view.findViewById(R.id.item_common_result_recorded_content_hyphen);
                holder.tv_recorded_ch_name = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
                holder.tv_sub_title = view.findViewById(R.id.item_common_result_subtitle);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    /**
     * データの設定.
     *
     * @param holder 設定済みViewHolder
     */
    private void setShowDataVisibility(final ViewHolder holder) {
        switch (mType) {
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
                holder.ll_rating.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                break;
            case TYPE_VIDEO_RANK: // ビデオランキング
                holder.tv_time.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                break;
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
            case TYPE_WATCHING_VIDEO_LIST: //視聴中ビデオ一覧
            case TYPE_CLIP_LIST_MODE_VIDEO: //ビデオタブ(クリップ)
                holder.tv_time.setVisibility(View.GONE);
                holder.tv_rank.setVisibility(View.GONE);
                break;
            case TYPE_CLIP_LIST_MODE_TV: //TVタブ(クリップ)
                holder.ll_rating.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                holder.tv_rank.setVisibility(View.GONE);
                break;
            case TYPE_RENTAL_RANK: // レンタル一覧
                holder.tv_rank.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                holder.tv_clip.setVisibility(View.GONE);
                holder.rl_thumbnail.setVisibility(View.GONE);
                holder.iv_thumbnail.setVisibility(View.GONE);
                holder.rb_rating.setVisibility(View.GONE);
                holder.tv_rating_num.setVisibility(View.GONE);
                break;
            case TYPE_RECORDED_LIST: // 録画番組一覧
                holder.tv_rank.setVisibility(View.GONE);
                holder.rl_thumbnail.setVisibility(View.GONE);
                holder.iv_thumbnail.setVisibility(View.GONE);
                holder.rb_rating.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * クリップ表示処理.
     *
     * @param holder          クリップアイコン
     * @param listContentInfo 行データー
     */
    private void setClipButton(final ViewHolder holder, final ContentsData listContentInfo) {
        if (holder.tv_clip != null) {
            String clipType = listContentInfo.getRequestData().getType();
            //ひかりコンテンツ判定
            if (StringUtils.isHikariContents(clipType) || StringUtils.isHikariInDtvContents(clipType)) {
                //TODO:録画予約一覧等、クリップボタンを表示しない画面はここで外す
                if (!mType.equals(TYPE_RECORDING_RESERVATION_LIST)) {
                    //クリップ状態が1以外の時は、非活性クリップボタンを表示
                    if (listContentInfo.isClipExec()) {
                        if (listContentInfo.isClipStatus()) {
                            holder.tv_clip.setBackgroundResource(R.mipmap.icon_circle_active_clip);
                        } else {
                            holder.tv_clip.setBackgroundResource(R.mipmap.icon_circle_opacity_clip);
                        }
                    } else {
                        holder.tv_clip.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    /**
     * ダウンロードステータス設定.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     * @param position        ポジション
     */
    private void setDownloadStatus(final ViewHolder holder, final ContentsData listContentInfo,
                                   final int position) {
        if (holder.tv_clip != null && listContentInfo != null) {
            holder.tv_clip.setTag(position);
            holder.tv_clip.setOnClickListener(this);
            switch (listContentInfo.getDownloadFlg()) {
                case DOWNLOAD_STATUS_ALLOW:
                    holder.tv_clip.setBackgroundResource(R.mipmap.icon_circle_normal_download);
                    break;
                case DOWNLOAD_STATUS_LOADING:
                    holder.tv_clip.setBackgroundResource(R.mipmap.icon_circle_active_cancel);
                    break;
                case DOWNLOAD_STATUS_COMPLETED:
                    holder.tv_clip.setBackgroundResource(R.mipmap.icon_circle_normal_download_check);
                    break;
                default:
                    break;
            }
        }
        if (holder.tv_recorded_ch_name != null && holder.tv_recorded_hyphen != null && listContentInfo != null) {
            if (!TextUtils.isEmpty(listContentInfo.getDownloadStatus())) {
                holder.tv_recorded_hyphen.setVisibility(View.VISIBLE);
                holder.tv_recorded_ch_name.setVisibility(View.VISIBLE);
                holder.tv_recorded_ch_name.setText(listContentInfo.getDownloadStatus());
                holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.record_download_status_color));
            } else {
                holder.tv_recorded_ch_name.setText(listContentInfo.getRecordedChannelName());
                holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.content_time_text));
                if (TextUtils.isEmpty(listContentInfo.getRecordedChannelName())) {
                    holder.tv_recorded_hyphen.setVisibility(View.GONE);
                    holder.tv_recorded_ch_name.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * タブ種別設定(クリップリスト用).
     *
     * @param type タブ種別
     */
    public void setMode(final ActivityTypeItem type) {
        mType = type;
    }

    /**
     * ビュー管理クラス.
     */
    private static class ViewHolder {
        /**
         * サムネイル親レイアウト.
         */
        RelativeLayout rl_thumbnail;
        /**
         * サムネイル.
         */
        ImageView iv_thumbnail;
        /**
         * 評価の親レイアウト.
         */
        LinearLayout ll_rating;
        /**
         * 評価（星）.
         */
        RatingBar rb_rating;
        /**
         * 評価（count）.
         */
        TextView tv_rating_num;
        /**
         * ラベル.
         */
        TextView tv_rank;
        /**
         * サブタイトル.
         */
        TextView tv_time;
        /**
         * メインタイトル.
         */
        TextView tv_title;
        /**
         * 操作アイコンボタン.
         */
        ImageView tv_clip;
        /**
         * ライン.
         */
        TextView tv_line;
        /**
         * 録画予約ステータス.
         */
        TextView tv_recording_reservation = null;
        /**
         * チャンネル名.
         */
        TextView tv_channel_name = null;
        /**
         * ハイフン.
         */
        TextView tv_recorded_hyphen;
        /**
         * 録画番組用チャンネル名.
         */
        TextView tv_recorded_ch_name;
        /**
         * サブタイトル
         */
        TextView tv_sub_title = null;
    }

    /**
     * ダウンロードボタンイベントコールバック.
     */
    public interface DownloadCallback {
        /**
         * ダウンロードボタンイベントコールバック.
         *
         * @param v 　View
         */
        void downloadClick(View v);
    }

    @Override
    public void onClick(final View v) {

        mDownloadCallback.downloadClick(v);
    }
}
