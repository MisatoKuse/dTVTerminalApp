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
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

import java.util.List;

import static com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter.ActivityTypeItem.TYPE_RECORDING_RESERVATION_LIST;
import static com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter.ActivityTypeItem.TYPE_STB_SELECT_LIST;

public class ContentsAdapter extends BaseAdapter implements OnClickListener {

    //各Activityインスタンス
    private Context mContext = null;
    //リスト用データソース
    private List<ContentsData> mListData = null;
    //サムネイル取得用プロバイダー
    private ThumbnailProvider mThumbnailProvider = null;
    //表示項目のタイプ
    private ActivityTypeItem mType;
    //ビューの生成
    private LayoutInflater mInflater;
    //評価基準値
    private final static int NUM_STARS_TOTAL = 5;
    //サムネイル幅さ display3分の1
    private final static int THUMBNAIL_WIDTH = 3;
    //サムネイル高さ サムネイル幅さ2分の1
    private final static int THUMBNAIL_HEIGHT = 2;
    //サムネイルmarginleft
    private final static int THUMBNAIL_MARGINLEFT = 15;
    //サムネイルmargintop
    private final static int THUMBNAIL_MARGINTOP = 10;
    //サムネイルmarginright
    private final static int THUMBNAIL_MARGINRIGHT = 8;
    //サムネイルmarginbottom
    private final static int THUMBNAIL_MARGINBOTTOM = 10;
    //margin0
    private final static int THUMBNAIL_MARGIN0 = 0;
    //ライン高さ
    private final static int LINE_HEIGHT = 1;
    //コピー残り回数
    private final static int ALLOWED_USE = 0;

    // クリップ登録済み判定用
    private final static String ACTIVE_CLIP_DISPLAY = "1";

    // クリップ未登録判定用
    private final static String OPACITY_CLIP_DISPLAY = "0";

    public final static int DOWNLOAD_STATUS_ALLOW = 0;

    public final static int DOWNLOAD_STATUS_LOADING = 1;

    public final static int DOWNLOAD_STATUS_COMPLETED = 2;

    private DownloadCallback mDownloadCallback;

    /**
     * 機能
     * 共通アダプター使う
     */
    public enum ActivityTypeItem {
        TYPE_DAILY_RANK,          //今日のテレビランキング
        TYPE_WEEKLY_RANK,         //週間テレビランキング
        TYPE_VIDEO_RANK,          //ビデオランキング
        TYPE_RENTAL_RANK,          //レンタル
        TYPE_RECORDING_RESERVATION_LIST, // 録画予約一覧
        TYPE_VIDEO_CONTENT_LIST, // ビデオコンテンツ一覧
        TYPE_RECORDED_LIST, // 録画番組一覧
        TYPE_STB_SELECT_LIST // STB選択デバイスリスト
    }

    /**
     * コンストラクタ
     *
     * @param mContext Activity
     * @param listData リストデータ
     * @param type     　項目コントロール
     */
    public ContentsAdapter(Context mContext, List<ContentsData> listData, ActivityTypeItem type) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mListData = listData;
        this.mType = type;
        mThumbnailProvider = new ThumbnailProvider(mContext);
    }

    public ContentsAdapter(Context mContext, List<ContentsData> listData, ActivityTypeItem type, DownloadCallback callback) {
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
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        ViewHolder holder = null;

        //ビューの再利用
        if (view == null) {
            holder = new ViewHolder();
            view = setViewPattern(parent);
            holder = setListItemPattern(holder, view);

            //ディスプレイ基づいて、画像の長さと幅さを設定
            setView(holder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        setShowDataVisibility(holder);
        //各アイテムデータを取得
        final ContentsData listContentInfo = mListData.get(position);
        // アイテムデータを設定する
        setContentsData(holder, listContentInfo);

        if (!ActivityTypeItem.TYPE_RECORDED_LIST.equals(mType)) {
            //クリップボタン処理を設定する
            final ClipRequestData requestData = listContentInfo.getRequestData();
            final ImageView clipButton = view.findViewById(R.id.item_common_result_clip_tv);
            listContentInfo.setClipButton(clipButton);
            clipButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
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
            setDownloadStatus(holder,listContentInfo, position);
        }

        return view;
    }

    /**
     * 各コンテンツデータを設定
     *
     * @param holder
     * @param listContentInfo
     */
    private void setContentsData(ViewHolder holder, ContentsData listContentInfo) {
        DTVTLogger.start();
        setRankData(holder, listContentInfo);
        setTitleData(holder, listContentInfo);
        setTimeData(holder, listContentInfo);
        setThumbnailData(holder, listContentInfo);
        setRatStarData(holder, listContentInfo);
        setRecodingReservationStatusData(holder, listContentInfo);
        setChannelName(holder, listContentInfo);
        setRedordedDownloadIcon(holder, listContentInfo);
        setDeviceName(holder, listContentInfo);
    }

    /**
     * データ設定（STBデバイス名）
     */
    private void setDeviceName(ViewHolder holder, ContentsData listContentInfo) {
        if (holder.stb_device_name != null) {
            holder.stb_device_name.setText(listContentInfo.getDeviceName());
        }
    }

    /**
     * データの設定（ランク）
     */
    private void setRankData(ViewHolder holder, ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getRank())) {//ランク
            holder.tv_rank.setText(listContentInfo.getRank());
        }
    }

    /**
     * データの設定（タイトル）
     */
    private void setTitleData(ViewHolder holder, ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getTitle())) {//タイトル
            String title = listContentInfo.getTitle() + mContext.getResources().getString(R.string.common_ranking_enter);
            holder.tv_title.setText(title);
        }
    }

    /**
     * データの設定（開始時刻）
     */
    private void setTimeData(ViewHolder holder, ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getTime())) {//時間
            switch (mType) {
                case TYPE_DAILY_RANK: // 今日のテレビランキング
                case TYPE_WEEKLY_RANK: // 週間ランキング
                    holder.tv_time.setText(DateUtils.getRecordShowListItem(Long.parseLong(listContentInfo.getTime())));
                    break;
                case TYPE_VIDEO_RANK: // ビデオランキング
                case TYPE_RENTAL_RANK: // レンタル一覧
                case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
                case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                case TYPE_RECORDED_LIST: // 録画番組一覧
                case TYPE_STB_SELECT_LIST: //STBデバイス名一覧
                    holder.tv_time.setText(listContentInfo.getTime());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * データの設定（評価）
     */
    private void setRatStarData(ViewHolder holder, ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getRatStar())) {//評価
            holder.rb_rating.setNumStars(NUM_STARS_TOTAL);
            holder.rb_rating.setRating(Float.parseFloat(listContentInfo.getRatStar()));
            holder.tv_rating_num.setText(listContentInfo.getRatStar());
        }
    }

    /**
     * データの設定（サムネイル）
     */
    private void setThumbnailData(ViewHolder holder, ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getThumURL())) {//サムネイル
            holder.rl_thumbnail.setVisibility(View.VISIBLE);
            holder.iv_thumbnail.setTag(listContentInfo.getThumURL());
            Bitmap thumbnailImage = mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, listContentInfo.getThumURL());
            if (thumbnailImage != null) {
                holder.iv_thumbnail.setImageBitmap(thumbnailImage);
            }
        } else {
            holder.rl_thumbnail.setVisibility(View.GONE);
        }
    }

    /**
     * データの設定（録画予約ステータス）
     */
    private void setRecodingReservationStatusData(ViewHolder holder, ContentsData listContentInfo) {
        DTVTLogger.start("status = " + listContentInfo.getRecordingReservationStatus());
        if (holder.tv_recording_reservation != null) {// 録画予約ステータス
            int status = listContentInfo.getRecordingReservationStatus();
            switch (status) {
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_REFLECTS_WAITING:
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_DURING_REFLECT:
                    // 受付中
                    holder.tv_recording_reservation.setVisibility(View.VISIBLE);
                    holder.tv_recording_reservation.setTextColor
                            (ContextCompat.getColor(mContext, R.color.recording_reservation_status_text_color_red));
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_white));
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accepting);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_REFLECT_FAILURE:
                    // 受付失敗
                    holder.tv_recording_reservation.setVisibility(View.VISIBLE);
                    holder.tv_recording_reservation.setTextColor
                            (ContextCompat.getColor(mContext, R.color.recording_reservation_status_text_color_white));
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_red));
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accept_failure);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT:
                    // 受信完了
                    holder.tv_recording_reservation.setVisibility(View.GONE);
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_black));
                    break;
                default:
                    holder.tv_recording_reservation.setVisibility(View.GONE);
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_black));
                    break;
            }
            DTVTLogger.end();
        }
    }

    /**
     * ダウンロードアイコンの設定（録画番組用チャンネル名）
     */
    private void setRedordedDownloadIcon(ViewHolder holder, ContentsData listContentInfo) {
        DTVTLogger.start();
        //TODO:録画予約一覧等、クリップボタンを表示しない画面はここで外す
        if (!mType.equals(TYPE_RECORDING_RESERVATION_LIST) && !mType.equals(TYPE_STB_SELECT_LIST)) {
            BaseActivity baseActivity = new BaseActivity();
            if (holder.tv_clip != null) {
                //Boolean contentsFlag = baseActivity.getDownloadContentsFalag();
                int downloadFlg = listContentInfo.getDownloadFlg();
                if (downloadFlg != -1) {
                    // ダウンロード済み
                    holder.tv_clip.setVisibility(View.VISIBLE);
                    holder.tv_clip.setBackgroundColor
                            (ContextCompat.getColor(mContext, R.color.ranking_list_border));
                } else {
                    // 未ダウンロード
                    int allowedUse = listContentInfo.getAllowedUse();
                    if (ALLOWED_USE == allowedUse) {
                        // ダウンロード回数無 && ダウンロード禁止
                        holder.tv_clip.setVisibility(View.GONE);
                    } else {
                        // ダウンロード回数の残数有り
                        holder.tv_clip.setVisibility(View.VISIBLE);
                        holder.tv_clip.setBackgroundColor
                                (ContextCompat.getColor(mContext, R.color.recording_reservation_status_background_red));
                    }
                }
            }
        }

        if(!ActivityTypeItem.TYPE_RECORDED_LIST.equals(mType)){
            setClipButton(holder, listContentInfo);
        }

        DTVTLogger.end();
    }

    /**
     * データの設定（チャンネル名）
     */
    private void setChannelName(ViewHolder holder, ContentsData listContentInfo) {
        DTVTLogger.start();
        if (!TextUtils.isEmpty(listContentInfo.getChannelName())) {//ランク
            holder.tv_recorded_hyphen.setVisibility(View.VISIBLE);
            holder.tv_recorded_ch_name.setVisibility(View.VISIBLE);
            holder.tv_recorded_ch_name.setText(listContentInfo.getChannelName());
        }
    }

    /**
     * ビューの設定
     */
    private void setView(ViewHolder holder) {
        DTVTLogger.start();
        DisplayMetrics DisplayMetrics = mContext.getResources().getDisplayMetrics();
        float density = DisplayMetrics.density;
        float mWidth = (float) DisplayMetrics.widthPixels / THUMBNAIL_WIDTH;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) mWidth, (int) mWidth / THUMBNAIL_HEIGHT);
        layoutParams.setMargins((int) density * THUMBNAIL_MARGINLEFT, (int) density * THUMBNAIL_MARGINTOP, (int) density * THUMBNAIL_MARGINRIGHT, (int) density * THUMBNAIL_MARGINBOTTOM);
        holder.rl_thumbnail.setLayoutParams(layoutParams);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) density * LINE_HEIGHT);
        layoutParams.setMargins((int) density * THUMBNAIL_MARGINLEFT, THUMBNAIL_MARGIN0, THUMBNAIL_MARGIN0, THUMBNAIL_MARGIN0);
        holder.tv_line.setLayoutParams(layoutParams);
    }

    private View setViewPattern(ViewGroup parent) {
        DTVTLogger.start();
        // TODO 録画予約一覧以外のパターンも共通項目以外を抽出し、修正する
        View view = null;
        switch (mType) {
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
            case TYPE_RECORDED_LIST: // 録画番組一覧
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
            case TYPE_STB_SELECT_LIST: //STBデバイス名一覧
                view = mInflater.inflate(R.layout.item_common_result, parent, false);
            default:
                break;
        }
        return view;
    }

    /**
     * 共通Itemの設定
     */
    private ViewHolder setCommonListItem(ViewHolder holder, View view) {
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
     * Itemのパターンを設定
     */
    private ViewHolder setListItemPattern(ViewHolder holder, View view) {
        DTVTLogger.start();
        // TODO 録画予約一覧以外のパターンも共通項目以外を抽出し、修正する
        holder = setCommonListItem(holder, view);
        switch (mType) {
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                holder.tv_recording_reservation =
                        view.findViewById(R.id.item_common_result_recording_reservation_status);
                holder.tv_recorded_hyphen = view.findViewById(R.id.item_common_result_recorded_content_hyphen);
                holder.tv_recorded_ch_name = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
                break;
            case TYPE_RECORDED_LIST: // 録画番組一覧
                holder.tv_recorded_hyphen = view.findViewById(R.id.item_common_result_recorded_content_hyphen);
                holder.tv_recorded_ch_name = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
                break;
            case TYPE_STB_SELECT_LIST:  //STBデバイス名一覧
                holder.stb_device_name = view.findViewById(R.id.item_common_result_device_name);
                view.findViewById(R.id.item_common_result_sort_button).setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return holder;
    }

    /**
     * データの設定
     */
    private void setShowDataVisibility(ViewHolder holder) {
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
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
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
            case TYPE_STB_SELECT_LIST: //STBデバイス名一覧
                holder.ll_rating.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                holder.rl_thumbnail.setVisibility(View.GONE);
                holder.iv_thumbnail.setVisibility(View.GONE);
                holder.rb_rating.setVisibility(View.GONE);
                holder.tv_rating_num.setVisibility(View.GONE);
                holder.tv_rank.setVisibility(View.GONE);
                holder.tv_time.setVisibility(View.GONE);
                holder.tv_title.setVisibility(View.GONE);
                holder.tv_line.setVisibility(View.GONE);
                holder.stb_device_name.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * クリップ表示処理
     *
     * @param holder          クリップアイコン
     * @param listContentInfo
     */
    private void setClipButton(ViewHolder holder, ContentsData listContentInfo) {
        if (holder.tv_clip != null) {
            String clipType = listContentInfo.getRequestData().getType();
            //ひかりコンテンツ判定
            if (StringUtil.isHikariContents(clipType) || StringUtil.isHikariInDtvContents(clipType)) {
                //TODO:録画予約一覧等、クリップボタンを表示しない画面はここで外す
                if (!mType.equals(TYPE_RECORDING_RESERVATION_LIST) && !mType.equals(TYPE_STB_SELECT_LIST)) {
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
     * ダウンロードステータス設定
     */
    private void setDownloadStatus(ViewHolder holder, ContentsData listContentInfo, int position) {
        if (holder.tv_clip != null && listContentInfo != null) {
            holder.tv_clip.setTag(position);
            holder.tv_clip.setOnClickListener(this);
            switch (listContentInfo.getDownloadFlg()) {
                case DOWNLOAD_STATUS_ALLOW:
                    holder.tv_clip.setBackgroundResource(R.mipmap.icon_circle_normal_download);
                    break;
                case DOWNLOAD_STATUS_LOADING:
                    holder.tv_clip.setBackgroundResource(R.mipmap.icon_circle_active_pause);
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
                holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.d_animation_title));
            } else {
                holder.tv_recorded_ch_name.setText(listContentInfo.getRecordedChannelName());
                holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.content_time_text));
                if (TextUtils.isEmpty(listContentInfo.getRecordedChannelName())){
                    holder.tv_recorded_hyphen.setVisibility(View.GONE);
                    holder.tv_recorded_ch_name.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * ビュー管理クラス
     */
    private static class ViewHolder {
        //サムネイル親レイアウト
        RelativeLayout rl_thumbnail;
        //サムネイル
        ImageView iv_thumbnail;
        //評価の親レイアウト
        LinearLayout ll_rating;
        //評価（星）
        RatingBar rb_rating;
        //評価（count）
        TextView tv_rating_num;
        //ラベル
        TextView tv_rank;
        //サブタイトル
        TextView tv_time;
        //メインタイトル
        TextView tv_title;
        //操作アイコンボタン
        ImageView tv_clip;
        //ライン
        TextView tv_line;
        // 録画予約ステータス
        TextView tv_recording_reservation = null;
        // チャンネル名
        TextView tv_channel_name = null;
        //ハイフン
        TextView tv_recorded_hyphen;
        // 録画番組用チャンネル名
        TextView tv_recorded_ch_name;
        // STBデバイス名
        TextView stb_device_name = null;
    }

    public interface DownloadCallback {
         public void downloadClick(View v);
    }

    @Override
    public void onClick(View v) {

        mDownloadCallback.downloadClick(v);
    }
}
