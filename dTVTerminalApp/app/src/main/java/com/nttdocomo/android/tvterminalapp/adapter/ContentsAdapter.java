/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;

import java.util.List;

public class ContentsAdapter extends BaseAdapter {

    //各Activityインスタンス
    private Context mContext = null;
    //リスト用データソース
    private List<ContentsData> listData = null;
    //サムネイル取得用プロバイダー
    private ThumbnailProvider mThumbnailProvider = null;
    //表示項目のタイプ
    private ActivityTypeItem type;
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
        this.listData = listData;
        this.type = type;
        mThumbnailProvider = new ThumbnailProvider(mContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
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
        ContentsData listContentInfo = listData.get(position);
        // アイテムデータを設定する
        setContentsData(holder, listContentInfo);

        return view;
    }

    /**
     * 各コンテンツデータを設定
     *
     * @param holder
     * @param listContentInfo
     */
    private void setContentsData(ViewHolder holder, ContentsData listContentInfo) {
        setRankData(holder, listContentInfo);
        setTitleData(holder, listContentInfo);
        setTimeData(holder, listContentInfo);
        setThumbnailData(holder, listContentInfo);
        setRatStarData(holder, listContentInfo);
        setRecodingReservationStatusData(holder, listContentInfo);
        setChannelName(holder, listContentInfo);
        setDeviceName(holder,listContentInfo);
    }

    /**
     * データ設定（STBデバイス名）
     */
    private void setDeviceName(ViewHolder holder, ContentsData listContentInfo) {
        if(holder.stb_device_name != null){
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
            holder.tv_time.setText(listContentInfo.getTime());
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
        if(holder.tv_recording_reservation != null) {// 録画予約ステータス
            int status = listContentInfo.getRecordingReservationStatus();
            switch (status) {
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_REFLECTS_WAITING:
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_DURING_REFLECT:
                    // 受付中
                    holder.tv_recording_reservation.setVisibility(View.VISIBLE);
                    holder.tv_recording_reservation.setTextColor
                            (ContextCompat.getColor(mContext,R.color.recording_reservation_status_text_color_red));
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext,R.color.recording_reservation_status_background_white));
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accepting);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_REFLECT_FAILURE:
                    // 受付失敗
                    holder.tv_recording_reservation.setVisibility(View.VISIBLE);
                    holder.tv_recording_reservation .setTextColor
                            (ContextCompat.getColor(mContext, R.color.recording_reservation_status_text_color_white));
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext,R.color.recording_reservation_status_background_red));
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accept_failure);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT:
                    // 受信完了
                    holder.tv_recording_reservation.setVisibility(View.INVISIBLE);
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext,R.color.recording_reservation_status_background_black));
                    break;
                default:
                    holder.tv_recording_reservation.setVisibility(View.INVISIBLE);
                    holder.tv_recording_reservation.setBackgroundColor
                            (ContextCompat.getColor(mContext,R.color.recording_reservation_status_background_black));
                    break;
            }
            DTVTLogger.end();
        }
    }

    /**
     * データの設定（チャンネル名）
     */
    private void setChannelName(ViewHolder holder, ContentsData listContentInfo) {
        DTVTLogger.start("ChannelName = " +listContentInfo.getChannelName());
        if(holder.tv_recording_reservation != null) {
            holder.tv_channel_name.setVisibility(View.VISIBLE);
            holder.tv_channel_name.setText(listContentInfo.getChannelName());
        }
        DTVTLogger.end();
    }

    /**
     * ビューの設定
     */
    private void setView(ViewHolder holder) {
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
        // TODO 録画予約一覧以外のパターンも共通項目以外を抽出し、修正する
        View view = null;
        switch (type) {
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
                view = mInflater.inflate(R.layout.item_common_result, parent, false);
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                view = mInflater.inflate(R.layout.item_common_result, parent, false);
                break;
            case TYPE_STB_SELECT_LIST: //STBデバイス名一覧
                view = mInflater.inflate(R.layout.item_common_result, parent, false);
                break;
            default:
                break;
        }
        return view;
    }

    /**
     * 共通Itemの設定
     */
    private ViewHolder setCommonListItem(ViewHolder holder, View view) {
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
     *  Itemのパターンを設定
     */
    private ViewHolder setListItemPattern(ViewHolder holder, View view) {
        // TODO 録画予約一覧以外のパターンも共通項目以外を抽出し、修正する
        holder = setCommonListItem(holder, view);
        switch (type) {
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                holder.tv_recording_reservation =
                        view.findViewById(R.id.item_common_result_recording_reservation_status);
                holder.tv_channel_name = view.findViewById(R.id.item_common_result_channel_name);
                break;
            case TYPE_STB_SELECT_LIST:  //STBデバイス名一覧
                holder.stb_device_name = view.findViewById(R.id.item_common_result_device_name);
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
        switch (type) {
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
                holder.ll_rating.setVisibility(View.GONE);
                break;
            case TYPE_VIDEO_RANK: // ビデオランキング
                holder.tv_time.setVisibility(View.GONE);
                break;
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
                holder.tv_rank.setVisibility(View.GONE);
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                holder.tv_clip.setVisibility(View.GONE);
                holder.rl_thumbnail.setVisibility(View.GONE);
                holder.iv_thumbnail.setVisibility(View.GONE);
                holder.rb_rating.setVisibility(View.GONE);
                holder.tv_rating_num.setVisibility(View.GONE);
                holder.tv_channel_name.setVisibility(View.VISIBLE);
                break;
            case TYPE_STB_SELECT_LIST: //STBデバイス名一覧
                holder.ll_rating.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                holder.rl_thumbnail.setVisibility(View.GONE);
                holder.iv_thumbnail.setVisibility(View.GONE);
                holder.rb_rating.setVisibility(View.GONE);
                holder.tv_rating_num.setVisibility(View.GONE);
                holder.tv_channel_name.setVisibility(View.GONE);
                holder.tv_rank.setVisibility(View.GONE);
                holder.tv_time.setVisibility(View.GONE);
                holder.tv_title.setVisibility(View.GONE);
                holder.tv_line.setVisibility(View.GONE);
                holder.tv_recording_reservation.setVisibility(View.GONE);
                holder.stb_device_name.setVisibility(View.VISIBLE);
                break;
            default:
                break;
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
        TextView tv_clip;
        //ライン
        TextView tv_line;
        // 録画予約ステータス
        TextView tv_recording_reservation = null;
        // チャンネル名
        TextView tv_channel_name = null;
        // STBデバイス名
        Button stb_device_name = null;
    }
}
