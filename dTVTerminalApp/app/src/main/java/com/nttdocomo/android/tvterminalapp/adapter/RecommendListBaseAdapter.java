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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.common.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.Date;
import java.util.List;

public class RecommendListBaseAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<ContentsData> mData = null;
    //private int layoutid;
    private ThumbnailProvider mThumbnailProvider = null;

    //選択タブ名・仕様変更に自動対応するため敢えて文字列
    private int mSelectedTab = 0;

    //選択タブ番号
    private final int TV_TAB = 0;
    private final int VIDEO_TAB = 1;
    private final int DTV_TAB = 2;
    private final int DTV_CHANNEL_TAB = 3;
    private final int DANIME_TAB = 4;

    //最大表示行数2行
    private final int MAX_2LINE = 2;

    //最大表示行数1行
    private final int MAX_1LINE = 1;

    //日付用固定値
    private final String DATE_SEPARATOR = " - ";
    //改行
    private final String RETURN_TEXT = "\n";

    public void setSelectedTab(int selectedTab) {
        mSelectedTab = selectedTab;
    }

    public RecommendListBaseAdapter(Context context, List data, int id) {
        this.mContext = context;
        this.mData = data;
        //this.layoutid = id;
        mThumbnailProvider = new ThumbnailProvider(mContext);

        //選択タブ初期化
        mSelectedTab = 0;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ContentsData recommendContentInfo =  mData.get(position);
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
            String titleText = "";
            if(mSelectedTab == VIDEO_TAB) {
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
        holder.iv_thumbnail.setImageResource(R.drawable.test_image);
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
    private ViewHolder getViewItem(View view) {
        ViewHolder holder = new ViewHolder();
        //既存のビューの取得処理
        holder.iv_thumbnail = view.findViewById(R.id.recommend_iv_thumbnail);
        holder.tv_title = view.findViewById(R.id.recommend_title);
        holder.tv_des = view.findViewById(R.id.recommend_des);
        holder.iv_clip = view.findViewById(R.id.recommend_iv_clip);

        //追加になった日付行のビューの取得
        holder.tvDateLine = view.findViewById(R.id.recommend_time_and_now_on_air);
        holder.tvDate = view.findViewById(R.id.recommend_time);
        holder.tvNowonAir = view.findViewById(R.id.recommend_now_on_air);
        holder.tvRatingBar = view.findViewById(R.id.recommend_rating_star);
        holder.tvVideoMarging = view.findViewById(R.id.recommend_tv_video_margin);
        holder.otherMargine = view.findViewById(R.id.recommend_dtv_danime_margin);
        return holder;
    }

    /**
     * 表示非表示項目の制御.
     *
     * @param holder ビューの構造体
     * @param recommendContentInfo 1行分のデータ
     */
    private void visibleControl(ViewHolder holder,final ContentsData recommendContentInfo) {
        final ImageView clipButton = holder.iv_clip;

        //ひかりコンテンツのみクリップボタンを表示する
        if (recommendContentInfo.getServiceId().equals(SearchServiceType.ServiceId.HIKARI_TV_FOR_DOCOMO)) {
            holder.iv_clip.setVisibility(View.VISIBLE);

            //各行の最大表示行数を2行にする
            holder.tv_title.setMaxLines(MAX_2LINE);

            holder.iv_clip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    ((BaseActivity) mContext).sendClipRequest(recommendContentInfo.getRequestData(), clipButton);
                }
            });

        } else {
            holder.iv_clip.setVisibility(View.GONE);

            //各行の最大表示行数を1行にする
            holder.tv_title.setMaxLines(MAX_1LINE);
        }

        //日時欄とレーティングの星は通常非表示にする
        holder.tvDateLine.setVisibility(View.GONE);
        holder.tvRatingBar.setVisibility(View.GONE);

        //タブ毎の表示切替
        switch(mSelectedTab) {
            case TV_TAB:
                //テレビタブ
                //TVタブなので、日付行を表示する
                holder.tvDateLine.setVisibility(View.VISIBLE);
                holder.tvVideoMarging.setVisibility(View.VISIBLE);

                //開始・終了日付の取得
                long startDate = DateUtils.getEpochTimeLink(recommendContentInfo.getStartViewing());
                long endDate = DateUtils.getEpochTimeLink(recommendContentInfo.getEndViewing());

                //日付の表示
                String answerText = StringUtils.getConnectStrings(
                        DateUtils.getRecordShowListItem(startDate),
                        DATE_SEPARATOR);

                holder.tvDate.setText(answerText);

                //現在時刻が開始と終了の間ならば、"Now On Air"と表示する
                if(DateUtils.isBetweenNowTime(startDate,endDate)) {
                    //収まっていたので、Now On Airを表示
                    holder.tvNowonAir.setText(R.string.now_on_air);
                    //文字をNow On Airの色にする
                    holder.tvNowonAir.setTextColor(
                            ContextCompat.getColor(mContext,R.color.recommend_list_now_on_air));
                } else {
                    //チャンネル名取得
                    String channelName = getChannelName(recommendContentInfo);

                    //チャンネル名を表示
                    holder.tvNowonAir.setText(channelName);
                    //文字を灰色にする
                    holder.tvNowonAir.setTextColor(
                            ContextCompat.getColor(mContext,R.color.gray_text));
                }
                break;
            case VIDEO_TAB:
                holder.tvVideoMarging.setVisibility(View.VISIBLE);

                //ビデオタブでは、レーティングの星を表示する
                holder.tvRatingBar.setVisibility(View.VISIBLE);
                //TODO: ダミー値
                holder.tvRatingBar.setRating(3.5f);
                break;
            case DTV_TAB:
            case DTV_CHANNEL_TAB:
            case DANIME_TAB:
                holder.otherMargine.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    /**
     * チャンネル名取得.
     * TODO: 仕様不定の為仮実装
     *
     * @param recommendContentInfo 1行分のデータ
     * @return チャンネル名
     */
    private String getChannelName(final ContentsData recommendContentInfo) {
        String channelName = recommendContentInfo.getChannelName();

        if(channelName != null && !channelName.isEmpty()) {
            //チャンネル名が存在していれば使用する
            return channelName;
        }

        String channelId = recommendContentInfo.getChannelId();

        //TODO:チャンネル名の出所が不明なので、ダミー値を指定
        return "ダミー";
    }

    static class ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_title;
        TextView tv_des;
        ImageView iv_clip;

        //テレビの放送日付行全体
        LinearLayout tvDateLine;
        //テレビの放送日付
        TextView tvDate;
        //現在放送中表示
        TextView tvNowonAir;
        //レーティングの星
        RatingBar tvRatingBar;
        //テレビ・ビデオタブ用のマージン
        View tvVideoMarging;
        //その他タブ用マージン
        View otherMargine;
    }
}

