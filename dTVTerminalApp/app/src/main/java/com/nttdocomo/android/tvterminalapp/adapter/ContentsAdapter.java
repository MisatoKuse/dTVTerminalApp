/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
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
        TYPE_VIDEO_CONTENT_LIST // ビデオコンテンツ一覧
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
            // TODO レイアウトパターンを設定
//            view = setViewPattern(parent);
//            holder = setListItemPattern(holder,view);
            view = mInflater.inflate(R.layout.item_common_result, parent, false);
            holder = new ViewHolder();
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
            //ディスプレイ基づいて、画像の長さと幅さを設定
            setView(holder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        setData(holder);
        //各アイアムデータ取得してから、設定
        ContentsData listContentInfo = listData.get(position);
        if (!TextUtils.isEmpty(listContentInfo.getRank())) {//ランク
            holder.tv_rank.setText(listContentInfo.getRank());
        }
        if (!TextUtils.isEmpty(listContentInfo.getTime())) {//時間
            holder.tv_time.setText(listContentInfo.getTime());
        }
        if (!TextUtils.isEmpty(listContentInfo.getTitle())) {//タイトル
            String title = listContentInfo.getTitle() + mContext.getResources().getString(R.string.common_ranking_enter);
            holder.tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(listContentInfo.getRatStar())) {//評価
            holder.rb_rating.setNumStars(NUM_STARS_TOTAL);
            holder.rb_rating.setRating(Float.parseFloat(listContentInfo.getRatStar()));
            holder.tv_rating_num.setText(listContentInfo.getRatStar());
        }
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
        return view;
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

    private View setViewPattern(View view, ViewGroup parent) {
        // TODO 録画予約一覧以外のパターンの識別も可能にする
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
            default:
                break;
        }
        return view;
    }

    /**
     * Itemのパターンを設定
     */
    private ViewHolder setListItemPattern(ViewHolder holder, View view) {
        // TODO 録画予約一覧以外の画面のパターンの識別も可能にする
        // TODO 別パターンとしてレイアウトを実装・inflate
        switch (type) {
            case TYPE_DAILY_RANK: // 今日のテレビランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                holder = new VhPtnRecodingReservation();
                break;
            default:
                break;
        }
        return holder;
    }

    /**
     * データの設定
     */
    private void setData(ViewHolder holder) {
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
                break;
            default:
                break;
        }
    }

    /**
     * ビュー管理クラス(Base・ランキング一覧)
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
    }

    /**
     * ビュー管理クラス（録画予約）
     */
    private static class VhPtnRecodingReservation extends ViewHolder {
        //録画予約ステータス
        TextView tv_recording_reservation;
    }
}
