/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * おすすめ番組・ビデオ用アダプタ.
 */
public class RecommendListBaseAdapter extends BaseAdapter {

    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * 表示するコンテンツデータのリスト.
     */
    private List<ContentsData> mData = null;
    /**
     * サムネイル取得用プロパイダ.
     */
    private ThumbnailProvider mThumbnailProvider = null;

    /**
     * チャンネル情報.
     */
    private List<Map<String, String>> mChannelMap;

    /**
     * 選択タブ名・仕様変更に自動対応するため敢えて文字列.
     */
    private int mSelectedTab = 0;

    /**
     * TVタブ.
     */
    private final static int TV_TAB = 0;
    /**
     * ビデオタブ.
     */
    private final static int VIDEO_TAB = 1;
    /**
     * DTVタブ.
     */
    private final static int DTV_TAB = 2;
    /**
     * DTVチャンネルタブ.
     */
    private final static int DTV_CHANNEL_TAB = 3;
    /**
     * Dアニメタブ.
     */
    private final static int DANIME_TAB = 4;

    /**
     * 最大表示行数2行.
     */
    private final static int MAX_2LINE = 2;

    /**
     * 日付用固定値.
     */
    private final static String DATE_SEPARATOR = " - ";
    /**
     * 改行.
     */
    private final static String RETURN_TEXT = "\n";

    /**
     * 選択タブを設定する.
     *
     * @param selectedTab 選択するタブ
     */
    public void setSelectedTab(final int selectedTab) {
        mSelectedTab = selectedTab;
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param data    コンテンツデータ
     */
    public RecommendListBaseAdapter(Context context, List data) {
        this.mContext = context;
        this.mData = data;
        mThumbnailProvider = new ThumbnailProvider(mContext);

        //選択タブ初期化
        mSelectedTab = 0;
    }

    /**
     * チャンネル情報を受け取る.
     *
     * @param channelMap チャンネル情報
     */
    public void setChannel(List<Map<String, String>> channelMap) {
        //受け取ったチャンネル情報を蓄積する
        mChannelMap = channelMap;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(final int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }


    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        if (mData.size() < position) {
            //データが存在しないのでスキップ
            return null;
        }

        final ContentsData recommendContentInfo = mData.get(position);
        ViewHolder holder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.item_recommend_list, null);
            holder = getViewItem(view);

            float mWidth = (float) mContext.getResources().getDisplayMetrics().widthPixels / 3;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    (int) mWidth, (int) mWidth / 2);
            holder.iv_thumbnail.setLayoutParams(layoutParams);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //項目の表示非表示を制御する
        visibleControl(holder, recommendContentInfo);

        if (null != holder.tv_title) {
            String titleText;
            if (mSelectedTab == VIDEO_TAB) {
                //ビデオタブなので、文字表示を常に2行以上にすることで、レーティングの星の位置を固定する
                titleText = StringUtils.getConnectStrings(recommendContentInfo.getTitle(),
                        RETURN_TEXT);
            } else {
                titleText = recommendContentInfo.getTitle();
            }

            holder.tv_title.setText(titleText);
        }

        if (null != holder.tv_des) {
            holder.tv_des.setText("");
        }

        String searchOk = recommendContentInfo.getSearchOk();
        if (searchOk != null && searchOk.length() > 1) {
            //TODO:クリップボタン状態変更
        }
        //サムネイル取得までは画像の表示を行わないため透明画像をセットしておく
        holder.iv_thumbnail.setImageResource(R.drawable.transparent);
        if (null != holder.iv_thumbnail) {
            String thumbUrl = recommendContentInfo.getThumURL();
            holder.iv_thumbnail.setTag(thumbUrl);
            Bitmap bp = mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, thumbUrl);
            if (null != bp) {
                holder.iv_thumbnail.setImageBitmap(bp);
            }
        }

        return view;
    }

    /**
     * リストビューの1行に含まれる各ビューの取得.
     *
     * @param view 元の行ビュー
     * @return 取得後の各ビューを含んだ構造体
     */
    private ViewHolder getViewItem(final View view) {
        ViewHolder holder = new ViewHolder();
        //既存のビューの取得処理
        holder.iv_thumbnail = view.findViewById(R.id.recommend_iv_thumbnail);
        holder.tv_title = view.findViewById(R.id.recommend_title);
        holder.tv_des = view.findViewById(R.id.recommend_des);
        holder.iv_clip = view.findViewById(R.id.recommend_iv_clip);

        //追加になった日付行のビューの取得
        holder.tvDateLine = view.findViewById(R.id.recommend_time_and_now_on_air);
        holder.tvDate = view.findViewById(R.id.recommend_time);
        holder.tvNowOnAir = view.findViewById(R.id.recommend_now_on_air);
        holder.tvVideoMargin = view.findViewById(R.id.recommend_tv_video_margin);
        holder.otherMargin = view.findViewById(R.id.recommend_dtv_danime_margin);
        return holder;
    }

    /**
     * 表示非表示項目の制御.
     *
     * @param holder               ビューの構造体
     * @param recommendContentInfo 1行分のデータ
     */
    private void visibleControl(final ViewHolder holder, final ContentsData recommendContentInfo) {
        final ImageView clipButton = holder.iv_clip;

        //各行の最大表示行数を2行にする
        holder.tv_title.setMaxLines(MAX_2LINE);

        //ひかりコンテンツのみクリップボタンを表示する
        if (recommendContentInfo.getServiceId().equals(SearchServiceType.ServiceId.HIKARI_TV_FOR_DOCOMO)) {
            holder.iv_clip.setVisibility(View.VISIBLE);

            holder.iv_clip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、画像比較でクリップ済/未を判定する
                    Bitmap clipButtonBitmap = ((BitmapDrawable) clipButton.getBackground()).getBitmap();
                    Bitmap activeClipBitmap = ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(),
                            R.mipmap.icon_circle_active_clip, null)).getBitmap();
                    if (clipButtonBitmap.equals(activeClipBitmap)) {
                        recommendContentInfo.getRequestData().setClipStatus(true);
                    } else {
                        recommendContentInfo.getRequestData().setClipStatus(false);
                    }
                    //クリップボタンイベント
                    ((BaseActivity) mContext).sendClipRequest(
                            recommendContentInfo.getRequestData(), clipButton);
                }
            });

        } else {
            //クリップを非表示にする
            holder.iv_clip.setVisibility(View.GONE);
        }

        //日時欄は通常非表示にする
        holder.tvDateLine.setVisibility(View.GONE);

        //タブ毎の表示切替
        switch (mSelectedTab) {
            case TV_TAB:
                //テレビタブ
                //TVタブなので、日付行を表示する
                holder.tvDateLine.setVisibility(View.VISIBLE);
                holder.tvVideoMargin.setVisibility(View.VISIBLE);

                //開始・終了日付の取得
                long startDate = DateUtils.getEpochTimeLink(recommendContentInfo.getStartViewing());
                long endDate = DateUtils.getEpochTimeLink(recommendContentInfo.getEndViewing());

                //日付の表示
                String answerText = StringUtils.getConnectStrings(
                        DateUtils.getRecordShowListItem(startDate),
                        DATE_SEPARATOR);

                holder.tvDate.setText(answerText);

                //現在時刻が開始と終了の間ならば、"Now On Air"と表示する
                if (DateUtils.isBetweenNowTime(startDate, endDate)) {
                    //収まっていたので、Now On Airを表示
                    holder.tvNowOnAir.setText(R.string.now_on_air);
                    //文字をNow On Airの色にする
                    holder.tvNowOnAir.setTextColor(
                            ContextCompat.getColor(mContext, R.color.recommend_list_now_on_air));
                } else {
                    //チャンネル名取得
                    String channelName = getChannelName(recommendContentInfo);

                    //チャンネル名を表示
                    holder.tvNowOnAir.setText(channelName);
                    //文字を灰色にする
                    holder.tvNowOnAir.setTextColor(
                            ContextCompat.getColor(mContext, R.color.gray_text));
                }
                break;
            case VIDEO_TAB:
            case DTV_TAB:
            case DTV_CHANNEL_TAB:
            case DANIME_TAB:
                //データが少ないので、大サイズのマージンを設定
                holder.otherMargin.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    /**
     * チャンネル名取得.
     *
     * @param recommendContentInfo 1行分のデータ
     * @return チャンネル名
     */
    private String getChannelName(final ContentsData recommendContentInfo) {
        String channelId = recommendContentInfo.getChannelId();
        String channelName = "";
        if (channelId != null && !channelId.isEmpty() && mChannelMap != null) {
            //チャンネル名検索
            for (Map<String, String> channel : mChannelMap) {
                //キーを見つける
                String key = (String) channel.get(JsonConstants.META_RESPONSE_SERVICE_ID);
                if (key.equals(channelId)) {
                    //キーを見つけた
                    channelName = (String) channel.get(JsonConstants.META_RESPONSE_TITLE);
                    break;
                }
            }

            return channelName;
        }

        //チャンネルリストが取れないので、空文字
        return "";
    }

    /**
     * ViewHolder.
     */
    static class ViewHolder {
        /**
         * サムネイル.
         */
        ImageView iv_thumbnail;
        /**
         * タイトル.
         */
        TextView tv_title;
        /**
         * description.
         */
        TextView tv_des;
        /**
         * クリップボタン.
         */
        ImageView iv_clip;

        /**
         * テレビの放送日付行全体.
         */
        LinearLayout tvDateLine;
        /**
         * テレビの放送日付.
         */
        TextView tvDate;
        /**
         * 現在放送中表示.
         */
        TextView tvNowOnAir;
        /**
         * テレビ・ビデオタブ用のマージン.
         */
        View tvVideoMargin;
        /**
         * その他タブ用マージン.
         */
        View otherMargin;
    }
}

