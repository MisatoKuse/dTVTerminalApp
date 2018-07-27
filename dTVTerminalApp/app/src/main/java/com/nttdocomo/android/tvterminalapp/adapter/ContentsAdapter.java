/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.RatingBarLayout;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.util.List;

/**
 * コンテンツ一覧系共通リストアダプター.
 */
public class ContentsAdapter extends BaseAdapter implements OnClickListener {

    //region variable
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
    private final LayoutInflater mInflater;
    /**
     * タブのタイプ.
     */
    private TabTypeItem mTabType;

    /**
     * サムネイルmarginleft.
     */
    private final static int THUMBNAIL_MARGIN_LEFT = 16;
    /**
     * サムネイルmargintop.
     */
    private final static int THUMBNAIL_MARGIN_END = 10;
    /**
     * サムネイルmarginbottom.
     */
    private final static int THUMBNAIL_MARGIN_BOTTOM = 10;
    /**
     * status　margintop.
     */
    private final static int STATUS_MARGIN_TOP17 = 17;
    /**
     * status　margintop.
     */
    private final static int STATUS_MARGIN_TOP10 = 10;

    /**
     * 番組タイトル margintop.
     */
    private final static int TITLE_MARGIN_TOP17 = 17;

    /**
     * 番組タイトル margintop.
     */
    private final static int TITLE_MARGIN_TOP20 = 20;

    /**
     * 番組タイトル margintop.
     */
    private final static int TITLE_MARGIN_TOP30 = 30;

    /**
     * クリップアイコンmargintop.
     */
    private final static int CLIP_MARGIN_TOP35 = 35;
    /**
     * 時刻テキストサイズ.
     */
    private final static int TIME_TEXT_SIZE = 12;
    /**
     * margin0.
     */
    private final static int THUMBNAIL_MARGIN0 = 0;
    /**
     * コピー残り回数.
     */
    private final static int ALLOWED_USE = 0;

    /**
     * 受付アイコンマージン.
     */
    private final static int RECEPTION_MARGIN_TOP30 = 30;
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
     * ダウンロード禁止判定フラグ.
     */
    private boolean isDownloadStop = false;
    /**
     * 再利用のビュー最大count.
     */
    private int mMaxItemCount = 0;
    //endregion variable

    /**
     * 機能
     * 共通アダプター使う.
     */
    public enum ActivityTypeItem {
        /**今日のテレビランキング.*/
        TYPE_DAILY_RANK,
        /**週間テレビランキング.*/
        TYPE_WEEKLY_RANK,
        /**ビデオランキング.*/
        TYPE_VIDEO_RANK,
        /**レンタル.*/
        TYPE_RENTAL_RANK,
        /**プレミアムビデオ.*/
        TYPE_PREMIUM_VIDEO_LIST,
        /**録画予約一覧.*/
        TYPE_RECORDING_RESERVATION_LIST,
        /**ビデオコンテンツ一覧.*/
        TYPE_VIDEO_CONTENT_LIST,
        /**録画番組一覧.*/
        TYPE_RECORDED_LIST,
        /**視聴中ビデオ一覧.*/
        TYPE_WATCHING_VIDEO_LIST,
        /**TVタブ(クリップ).*/
        TYPE_CLIP_LIST_MODE_TV,
        /**ビデオタブ(クリップ).*/
        TYPE_CLIP_LIST_MODE_VIDEO,
        /**検索.*/
        TYPE_SEARCH_LIST,
        /**コンテンツ詳細チャンネル一覧.*/
        TYPE_CONTENT_DETAIL_CHANNEL_LIST,
        /**おすすめ番組・ビデオ.*/
        TYPE_RECOMMEND_LIST
    }

    /**
     * 機能
     * 共通タブーを使う.
     */
    public enum TabTypeItem {
        /**
         * テレビ.
         */
        TAB_TV,
        /**
         * ビデオ.
         */
        TAB_VIDEO,
        /**
         * dTV.
         */
        TAB_D_TV,
        /**
         * dTVチャンネル.
         */
        TAB_D_CHANNEL,
        /**
         * dアニメ.
         */
        TAB_D_ANIMATE,
        /**
         * デフォルト.
         */
        TAB_DEFAULT,
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
        mThumbnailProvider = new ThumbnailProvider(mContext, ThumbnailDownloadTask.ImageSizeType.LIST);
    }

    /**
     * タブ種別設定.
     *
     * @param tabTypeItem タブ種別
     */
    public void setTabTypeItem(final TabTypeItem tabTypeItem) {
        this.mTabType = tabTypeItem;
    }

    /**
     * コンストラクタ(録画一覧専用)リファクタ対象.
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
        mThumbnailProvider = new ThumbnailProvider(mContext, ThumbnailDownloadTask.ImageSizeType.LIST);
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
        ViewHolder holder;
        //各アイテムデータを取得
        final ContentsData listContentInfo = mListData.get(position);
        View contentView = view;

        if (contentView == null) {
            if (listContentInfo.hasChildContentList()) {
                holder = new ViewHolder();
                contentView = mInflater.inflate(R.layout.item_wizard_cell, parent, false);
                setWizardItem(holder, contentView);
            } else {
                holder = new ViewHolder();
                contentView = mInflater.inflate(R.layout.item_common_result, parent, false);
                setListItemPattern(holder, contentView);
                mMaxItemCount++;
            }
        } else {
            holder = (ViewHolder) contentView.getTag();
            if (listContentInfo.hasChildContentList()) {
                if (!holder.isCommonContent) {
                    setWizardItem(holder, contentView);
                } else {
                    holder = new ViewHolder();
                    contentView = mInflater.inflate(R.layout.item_wizard_cell, parent, false);
                    setWizardItem(holder, contentView);
                }
            } else {
                if (holder.isCommonContent) {
                    setListItemPattern(holder, contentView);
                } else {
                    holder = new ViewHolder();
                    contentView = mInflater.inflate(R.layout.item_common_result, parent, false);
                    setListItemPattern(holder, contentView);
                    mMaxItemCount++;
                }
            }
        }

        // アイテムデータを設定する
        if (listContentInfo.hasChildContentList()) {
            setTitleData(holder, listContentInfo);
        } else {
            setShowDataVisibility(holder);
            setContentsData(holder, listContentInfo, contentView);
            setClipButtonItem(position, holder, contentView, listContentInfo);
            setMarginLayout(position, holder, contentView);
        }

        return contentView;
    }

    /**
     * 非多階層アイテムのクリップボタン処理設定.
     *
     * @param position リスト位置
     * @param holder 設定済みViewHolder
     * @param contentView ビュー
     * @param listContentInfo アイテムデータ
     */
    private void setClipButtonItem(final int position, final ViewHolder holder, final View contentView, final ContentsData listContentInfo) {
        if (!ActivityTypeItem.TYPE_RECORDED_LIST.equals(mType)) {
            //クリップボタン処理を設定する
            final ClipRequestData requestData = listContentInfo.getRequestData();
            final ImageView clipButton = contentView.findViewById(R.id.item_common_result_clip_tv);
            listContentInfo.setClipButton(clipButton);
            //クリップ活性時のみリスナー登録する
            if (UserInfoUtils.getClipActive(mContext)) {
                clipButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        //クリップ登録／解除実行中の2度押しによる誤動作の防止
                        if (!((BaseActivity) mContext).isClipRunTime()) {
                            if (listContentInfo.isClipStatus()) {
                                requestData.setClipStatus(true);
                            } else {
                                requestData.setClipStatus(false);
                            }
                            ((BaseActivity) mContext).sendClipRequest(requestData, clipButton);
                        }
                    }
                });
            }
        } else {
            setDownloadStatus(holder, listContentInfo, position);
        }
    }

    /**
     * 非多階層アイテムのレイアウト調整.
     *
     * @param position リスト位置
     * @param holder 設定済みViewHolder
     * @param contentView ビュー
     */
    @SuppressWarnings("OverlyLongMethod")
    private void setMarginLayout(final int position, final ViewHolder holder, final View contentView) {
        RelativeLayout.LayoutParams layoutParamsClip = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int textMargin;
        int clipMargin;
        switch (mType) {
            case TYPE_DAILY_RANK:
            case TYPE_WEEKLY_RANK:
            case TYPE_CLIP_LIST_MODE_TV: //TVタブ(クリップ)
            case TYPE_CONTENT_DETAIL_CHANNEL_LIST:
                textMargin = STATUS_MARGIN_TOP17;
                clipMargin = CLIP_MARGIN_TOP35;
                setTextMargin(textMargin, holder, contentView);
                setClipMargin(clipMargin, contentView);
                break;
            case TYPE_VIDEO_RANK:
            case TYPE_VIDEO_CONTENT_LIST:
            case TYPE_WATCHING_VIDEO_LIST:
            case TYPE_CLIP_LIST_MODE_VIDEO:
                textMargin = TITLE_MARGIN_TOP17;
                setTextMargin(textMargin, holder, contentView);
                layoutParamsClip.addRule(RelativeLayout.ALIGN_PARENT_END, R.id.parent_relative_layout);
                layoutParamsClip.addRule(RelativeLayout.CENTER_VERTICAL);
                contentView.findViewById(R.id.item_common_result_show_status_area).setLayoutParams(layoutParamsClip);
                break;
            case TYPE_RENTAL_RANK:
            case TYPE_PREMIUM_VIDEO_LIST: //プレミアムビデオ
                textMargin = STATUS_MARGIN_TOP10;
                setTextMargin(textMargin, holder, contentView);
                layoutParamsClip.addRule(RelativeLayout.ALIGN_PARENT_END, R.id.parent_relative_layout);
                layoutParamsClip.addRule(RelativeLayout.CENTER_VERTICAL);
                contentView.findViewById(R.id.item_common_result_show_status_area).setLayoutParams(layoutParamsClip);
                break;
            case TYPE_RECORDING_RESERVATION_LIST:
                //録画予約一覧用余白設定
                textMargin = STATUS_MARGIN_TOP17;
                setTextAllMargin(THUMBNAIL_MARGIN_LEFT, textMargin, THUMBNAIL_MARGIN_BOTTOM,
                        textMargin, holder, contentView);
                break;
            //ENUMの値をswitch分岐すると、全ての値を書かないとアナライザーがエラーを出すので、caseを追加
            case TYPE_RECORDED_LIST:
                break;
            case TYPE_RECOMMEND_LIST://おすすめ番組・ビデオ
            case TYPE_SEARCH_LIST://検索
                setTabContentMargin(holder, contentView);
                break;
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
    }

    /**
     * タブのマージン設定.
     *
     * @param holder holder
     * @param contentView contentView
     */
    private void setTabContentMargin(final ViewHolder holder, final View contentView) {
        int textMargin;
        int clipMargin;
        switch (mTabType) {
            case TAB_TV:
                textMargin = STATUS_MARGIN_TOP10;
                clipMargin = CLIP_MARGIN_TOP35;
                setTextMargin(textMargin, holder, contentView);
                setClipMargin(clipMargin, contentView);
                break;
            case TAB_VIDEO:
            case TAB_D_CHANNEL:
            case TAB_D_ANIMATE:
            case TAB_D_TV:
                textMargin = TITLE_MARGIN_TOP20;
                clipMargin = CLIP_MARGIN_TOP35;
                setTextMargin(textMargin, holder, contentView);
                setClipMargin(clipMargin, contentView);
                break;
            case TAB_DEFAULT:
            default:
                break;
        }
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
        layoutParams.setMarginStart(THUMBNAIL_MARGIN_LEFT * (int) density);
        layoutParams.addRule(RelativeLayout.START_OF, R.id.item_common_result_show_status_area);
        layoutParams.addRule(RelativeLayout.END_OF, R.id.item_common_result_thumbnail_rl);
        if (holder.tv_clip.getVisibility() == View.GONE) {
            layoutParams.setMarginEnd(THUMBNAIL_MARGIN_END * (int) density);
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
     * @param contentView contentView
     */
    private void setContentsData(final ViewHolder holder, final ContentsData listContentInfo, final View contentView) {
        DTVTLogger.start();
        setRankData(holder, listContentInfo);
        setTitleData(holder, listContentInfo);
        setTimeData(holder, listContentInfo);
        setThumbnailData(holder, listContentInfo);
        setRatStarData(holder, listContentInfo);
        setRecodingReservationStatusData(holder, listContentInfo);
        setChannelName(holder, listContentInfo, contentView);
        setClipIcon(holder, listContentInfo);
        setNowOnAirData(holder, listContentInfo, contentView);
        if (ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST.equals(mType)) {
            setSubTitle(holder, listContentInfo);
        }
    }

    /**
     * NOW ON AIR を設定.
     *
     * @param holder          ビューの集合
     * @param listContentInfo 行データー
     * @param contentView contentView
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void setNowOnAirData(final ViewHolder holder, final ContentsData listContentInfo, final View contentView) {
        switch (mType) {
            case TYPE_RECOMMEND_LIST://おすすめ番組・ビデオ
            case TYPE_SEARCH_LIST://検索
                switch (mTabType) {
                    case TAB_TV:
                    case TAB_D_CHANNEL:
                        checkNowOnAir(holder, listContentInfo, contentView, false);
                        break;
                    default:
                        break;
                }
                break;
            case TYPE_DAILY_RANK: // 今日の番組ランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
                checkNowOnAir(holder, listContentInfo, contentView, true);
                break;
            default:
                break;
        }
    }

    /**
     * 放送コンテンツかどうか.
     *
     * @param holder ViewHolder
     * @param listContentInfo ContentsData
     * @param isPlala   true:ぷらら false:レコメンド
     * @param contentView   contentView
     */
    @SuppressWarnings("OverlyComplexMethod")
    private void checkNowOnAir(final ViewHolder holder, final ContentsData listContentInfo, final View contentView, final boolean isPlala) {
        boolean result = false;
        if (holder.tv_recorded_ch_name != null) {
            holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.content_time_text));
        }
        if (isPlala) {
            if (ContentUtils.TV_PROGRAM.equals(listContentInfo.getDispType())) {
                if (ContentUtils.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(listContentInfo.getTvService())) {
                    if (ContentUtils.CONTENT_TYPE_FLAG_THREE.equals(listContentInfo.getContentsType())) {
                        if (DateUtils.isNowOnAirDate(listContentInfo.getPublishStartDate(),
                                listContentInfo.getPublishEndDate(), true)) {
                            result = true;
                        }
                    } else if (ContentUtils.CONTENT_TYPE_FLAG_ONE.equals(listContentInfo.getContentsType())
                            || ContentUtils.CONTENT_TYPE_FLAG_TWO.equals(listContentInfo.getContentsType())) {
                        if (DateUtils.isNowOnAirDate(listContentInfo.getPublishStartDate(),
                                listContentInfo.getPublishEndDate(), true)) {
                            result = true;
                        }
                    } else {
                        if (DateUtils.isNowOnAirDate(listContentInfo.getPublishStartDate(),
                                listContentInfo.getPublishEndDate(), true)) {
                            result = true;
                        }
                    }
                } else if (ContentUtils.TV_SERVICE_FLAG_HIKARI.equals(listContentInfo.getTvService())) {
                    if (DateUtils.isNowOnAirDate(listContentInfo.getPublishStartDate(),
                            listContentInfo.getPublishEndDate(), true)) {
                        result = true;
                    }
                }
            }
        } else {
            if (!TextUtils.isEmpty(listContentInfo.getServiceId()) && DataBaseUtils.isNumber(listContentInfo.getServiceId())) {
                int serviceId = Integer.parseInt(listContentInfo.getServiceId());
                String categoryId = listContentInfo.getCategoryId();
                if (ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID == serviceId
                        && (ContentUtils.RECOMMEND_CATEGORY_ID_THREE.equals(categoryId)
                        || ContentUtils.RECOMMEND_CATEGORY_ID_FOUR.equals(categoryId))) {
                    if (DateUtils.isNowOnAirDate(listContentInfo.getStartViewing(),
                            listContentInfo.getEndViewing(), false)) {
                        result = true;
                    }
                } else if (ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID == serviceId
                        && ContentUtils.RECOMMEND_CATEGORY_ID_ONE.equals(categoryId)) {
                    if (DateUtils.isNowOnAirDate(listContentInfo.getStartViewing(),
                            listContentInfo.getEndViewing(), false)) {
                        result = true;
                    }
                }
            }
        }
        if (result) {
            if (holder.tv_recorded_ch_name == null) {
                holder.tv_recorded_ch_name = contentView.findViewById(R.id.item_common_result_recorded_content_channel_name);
            }
            if (holder.tv_time != null && !TextUtils.isEmpty(holder.tv_time.getText())) {
                holder.tv_recorded_hyphen = contentView.findViewById(R.id.item_common_result_recorded_content_hyphen);
                holder.tv_recorded_hyphen.setVisibility(View.VISIBLE);
            }
            holder.tv_recorded_ch_name.setVisibility(View.VISIBLE);
            holder.tv_recorded_ch_name.setText(R.string.now_on_air);
            //文字をNow On Airの色にする
            holder.tv_recorded_ch_name.setTextColor(
                    ContextCompat.getColor(mContext, R.color.recommend_list_now_on_air));
        }
    }

    /**
     * タブレイアウト設定.
     *
     * @param holder holder
     */
    private void setTabContentLayout(final ViewHolder holder) {
        switch (mTabType) {
            case TAB_TV:
            case TAB_VIDEO:
                holder.tv_rank.setVisibility(View.GONE);
                holder.ll_rating.setVisibility(View.GONE);
                break;
            case TAB_D_CHANNEL:
            case TAB_D_ANIMATE:
            case TAB_D_TV:
                holder.tv_rank.setVisibility(View.GONE);
                holder.ll_rating.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                break;
            case TAB_DEFAULT:
            default:
                break;
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
        } else {
            holder.tv_title.setText(mContext.getResources().getString(R.string.common_ranking_enter));
        }
    }

    /**
     * データの設定（開始時刻）.
     *
     * @param holder          　ViewHolder
     * @param listContentInfo 　ContentsData
     */
    private void setTimeData(final ViewHolder holder, final ContentsData listContentInfo) {
        holder.tv_time.setVisibility(View.GONE);
        switch (mType) {
            case TYPE_RECOMMEND_LIST://おすすめ番組・ビデオ
            case TYPE_SEARCH_LIST://検索
                setTabTimeData(holder, listContentInfo);
                break;
            case TYPE_CLIP_LIST_MODE_VIDEO: //ビデオタブ(クリップ)
                if (listContentInfo.isIsAfterLimitContents()) {
                    //期限切れコンテンツの場合は「配信終了」を表示　※VODクリップのみ
                    holder.tv_time.setVisibility(View.VISIBLE);
                    holder.tv_time.setText(mContext.getString(R.string.str_clip_subtitle_delivery_end));
                } else {
                    ContentUtils.setPeriodText(mContext, holder.tv_time, listContentInfo);
                }
                break;
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_PREMIUM_VIDEO_LIST: //プレミアムビデオ
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_CLIP_LIST_MODE_TV: //TVタブ(クリップ)
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
            case TYPE_WATCHING_VIDEO_LIST: //視聴中ビデオ一覧
            case TYPE_DAILY_RANK: // 今日の番組ランキング
            case TYPE_WEEKLY_RANK: // 週間ランキング
                ContentUtils.setPeriodText(mContext, holder.tv_time, listContentInfo);
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
            case TYPE_RECORDED_LIST: // 録画番組一覧
            case TYPE_CONTENT_DETAIL_CHANNEL_LIST: // コンテンツ詳細チャンネル一覧
                if (!TextUtils.isEmpty(listContentInfo.getTime())) {
                    holder.tv_time.setVisibility(View.VISIBLE);
                    holder.tv_time.setText(listContentInfo.getTime());
                }
            default:
                break;
        }
    }

    /**
     * タブの日付データ設定.
     *
     * @param holder holder
     * @param listContentInfo コンテンツ情報
     */
    private void setTabTimeData(final ViewHolder holder, final ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getStartViewing())) {
            //開始・終了日付の取得
            switch (mTabType) {
                case TAB_TV:
                    holder.tv_time.setVisibility(View.VISIBLE);
                    //日付の表示
                    holder.tv_time.setText(DateUtils.getContentsDateString(listContentInfo.getStartViewing()));
                    break;
                case TAB_VIDEO:
                case TAB_D_TV:
                case TAB_D_CHANNEL:
                case TAB_D_ANIMATE:
                case TAB_DEFAULT:
                    String date = "";
                    if (DataBaseUtils.isNumber(listContentInfo.getServiceId())) {
                        int serviceId = Integer.parseInt(listContentInfo.getServiceId());
                        ContentUtils.ContentsType contentsType = ContentUtils.
                                getContentsTypeByRecommend(serviceId, listContentInfo.getCategoryId());
                        if (contentsType == ContentUtils.ContentsType.TV) {
                            //番組 m/d（曜日）h:ii
                            date = DateUtils.getContentsDateString(listContentInfo.getStartViewing());
                        } else if (contentsType == ContentUtils.ContentsType.VOD) {
                            //配信前　m/d（曜日）から
                            if (DateUtils.isBefore(listContentInfo.getStartViewing())) {
                                date = DateUtils.getContentsDateString(mContext, listContentInfo.getStartViewing(), true);
                            } else {
                                // 31日以内　m/d（曜日）まで
                                if (DateUtils.isIn31Day(listContentInfo.getEndViewing())) {
                                    date = DateUtils.getContentsDateString(mContext, listContentInfo.getEndViewing(), false);
                                }
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(date)) {
                        holder.tv_time.setVisibility(View.VISIBLE);
                        holder.tv_time.setText(date);
                    }
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
            //評価値が範囲外の場合は"-"を表示、星は非表示
            float ratNumber = Float.parseFloat(listContentInfo.getRatStar());
            holder.ll_rating.setRating(ratNumber);
        } else {
            holder.ll_rating.setRating(0);
        }
    }

    /**
     * データの設定（サムネイル）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setThumbnailData(final ViewHolder holder, final ContentsData listContentInfo) {
        DTVTLogger.start();

        holder.iv_thumbnail.setImageResource(R.mipmap.loading_list);
        if (!isDownloadStop) {
            holder.rl_thumbnail.setVisibility(View.VISIBLE);
            holder.iv_thumbnail.setTag(listContentInfo.getThumURL());
            mThumbnailProvider.setMaxQueueCount(mMaxItemCount);
            Bitmap thumbnailImage = mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, listContentInfo.getThumURL());
            if (thumbnailImage != null) {
                holder.iv_thumbnail.setImageBitmap(thumbnailImage);
            }
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
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_text_color_white));
                    holder.tv_recording_reservation.setBackgroundResource(R.drawable.record_reservation_textview_round_corner_gray);
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accepting);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_REFLECT_FAILURE:
                    // 受付失敗
                    holder.tv_recording_reservation.setVisibility(View.VISIBLE);
                    holder.tv_recording_reservation.setTextColor(
                            ContextCompat.getColor(mContext, R.color.recording_reservation_status_text_color_white));
                    holder.tv_recording_reservation.setBackgroundResource(R.drawable.record_reservation_textview_round_corner_red);
                    holder.tv_recording_reservation.setText(R.string.recording_reservation_status_accept_failure);
                    break;
                case RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT:
                    // 受信完了
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
     * クリップボタンの設定.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setClipIcon(final ViewHolder holder, final ContentsData listContentInfo) {
        DTVTLogger.start();
        holder.tv_clip.setVisibility(View.GONE);
        if (!ActivityTypeItem.TYPE_RECORDED_LIST.equals(mType)) {
            if (ActivityTypeItem.TYPE_RECOMMEND_LIST.equals(mType) || ActivityTypeItem.TYPE_SEARCH_LIST.equals(mType)) {
                holder.tv_clip.setVisibility(View.GONE);
            } else {
                setClipButton(holder, listContentInfo);
            }

        }

        DTVTLogger.end();
    }

    /**
     * データの設定（チャンネル名）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     * @param contentView contentView
     */
    @SuppressWarnings("OverlyComplexMethod")
    private void setChannelName(final ViewHolder holder, final ContentsData listContentInfo, final View contentView) {
        DTVTLogger.start();

        //ヌルチェック
        if (holder.tv_recorded_ch_name == null || holder.tv_recorded_hyphen == null) {
            if (!mType.equals(ActivityTypeItem.TYPE_WATCHING_VIDEO_LIST) && !mType.equals(ActivityTypeItem.TYPE_CLIP_LIST_MODE_VIDEO)) {
                return;
            }
        }
        if (!TextUtils.isEmpty(listContentInfo.getChannelName()) && mTabType != TabTypeItem.TAB_DEFAULT && !mType .equals(ActivityTypeItem.TYPE_CLIP_LIST_MODE_VIDEO)) { //ランク
            //↓を判定条件に使っているため、直前に初期化
            if (holder.tv_recorded_hyphen == null) {
                holder.tv_recorded_hyphen = contentView.findViewById(R.id.item_common_result_recorded_content_hyphen);
            }
            if (holder.tv_recorded_ch_name == null) {
                holder.tv_recorded_ch_name = contentView.findViewById(R.id.item_common_result_recorded_content_channel_name);
            }
            ContentUtils.setChannelNameOrMissedText(mContext, holder.tv_recorded_hyphen, holder.tv_recorded_ch_name, listContentInfo, mType);
            if (mType == ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST) {
                holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.record_download_status_color));
            }
        } else if (mType.equals(ActivityTypeItem.TYPE_WATCHING_VIDEO_LIST) || mType .equals(ActivityTypeItem.TYPE_CLIP_LIST_MODE_VIDEO)) {
            if (holder.tv_recorded_hyphen == null) {
                holder.tv_recorded_hyphen = contentView.findViewById(R.id.item_common_result_recorded_content_hyphen);
            }
            if (holder.tv_recorded_ch_name == null) {
                holder.tv_recorded_ch_name = contentView.findViewById(R.id.item_common_result_recorded_content_channel_name);
            }
            ContentUtils.ContentsType contentsType = ContentUtils.setChannelNameOrMissedText(mContext, holder.tv_recorded_hyphen, holder.tv_recorded_ch_name, listContentInfo, mType);
            if (contentsType != ContentUtils.ContentsType.DCHANNEL_VOD_31) {
                holder.tv_recorded_hyphen.setVisibility(View.GONE);
                holder.tv_recorded_ch_name.setVisibility(View.GONE);
                holder.tv_time.setVisibility(View.GONE);
            }
        } else {
            if (TextUtils.isEmpty(listContentInfo.getChannelName()) && holder.tv_recorded_ch_name != null
                    && holder.tv_recorded_hyphen != null) {
                holder.tv_recorded_ch_name.setText("");
                holder.tv_recorded_hyphen.setVisibility(View.GONE);
            }
            if (mType == ActivityTypeItem.TYPE_CONTENT_DETAIL_CHANNEL_LIST && holder.tv_recorded_hyphen != null) {
                holder.tv_recorded_hyphen.setVisibility(View.GONE);
                holder.tv_recorded_ch_name.setVisibility(View.GONE);
            }
            if (mType == ActivityTypeItem.TYPE_RECOMMEND_LIST || mType == ActivityTypeItem.TYPE_SEARCH_LIST) {
                setTabHyphenVisibility(holder, listContentInfo);
            }
        }
        if (mTabType == TabTypeItem.TAB_D_CHANNEL) {
            if (!Integer.toString(ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID).equals(listContentInfo.getServiceId())
                    || !ContentUtils.RECOMMEND_CATEGORY_ID_ONE.equals(listContentInfo.getCategoryId())) {
                holder.tv_recorded_hyphen.setVisibility(View.GONE);
                holder.tv_recorded_ch_name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * ハイフンの可視設定.
     *
     * @param holder holder
     * @param listContentInfo ContentsData
     */
    private void setTabHyphenVisibility(final ViewHolder holder, final ContentsData listContentInfo) {
        switch (mTabType) {
            case TAB_TV:
            case TAB_D_CHANNEL:
                if (!TextUtils.isEmpty(listContentInfo.getChannelName())) {
                    holder.tv_recorded_hyphen.setVisibility(View.VISIBLE);
                    holder.tv_recorded_ch_name.setVisibility(View.VISIBLE);
                    holder.tv_recorded_ch_name.setText(listContentInfo.getChannelName());
                    holder.tv_recorded_ch_name.setTextColor(ContextCompat.getColor(mContext, R.color.content_time_text));
                } else {
                    holder.tv_recorded_hyphen.setVisibility(View.GONE);
                    holder.tv_recorded_ch_name.setVisibility(View.GONE);
                }
                break;
            case TAB_D_ANIMATE:
            case TAB_D_TV:
            case TAB_VIDEO:
                break;
            case TAB_DEFAULT:
                holder.tv_recorded_hyphen.setVisibility(View.GONE);
                holder.tv_recorded_ch_name.setVisibility(View.GONE);
                break;
            default:
                break;

        }
    }

    /**
     * データの設定（サブタイトル）.
     *
     * @param holder          ViewHolder
     * @param listContentInfo ContentsData
     */
    private void setSubTitle(final ViewHolder holder, final ContentsData listContentInfo) {
        if (!TextUtils.isEmpty(listContentInfo.getSubTitle())) {
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
        holder.tv_line = view.findViewById(R.id.item_common_result_line);
        return holder;
    }

    /**
     * setWizardItem.
     * @param holder holder
     * @param view view
     */
    private void setWizardItem(final ViewHolder holder, final View view) {
        holder.tv_title = view.findViewById(R.id.item_common_result_content_title);
        holder.isCommonContent = false;
        view.setTag(holder);
    }

    /**
     * Itemのパターンを設定.
     *
     * @param holder ViewHolder
     * @param view   View
     */
    private void setListItemPattern(final ViewHolder holder, final View view) {
        DTVTLogger.start();
        // TODO 録画予約一覧以外のパターンも共通項目以外を抽出し、修正する
        ViewHolder viewHolder = holder;
        view.setTag(viewHolder);
        viewHolder.isCommonContent = true;
        viewHolder = setCommonListItem(viewHolder, view);
        switch (mType) {
            case TYPE_RECOMMEND_LIST://おすすめ番組・ビデオ
            case TYPE_SEARCH_LIST://検索
                setTabContentHyphen(holder, view);
                break;
            case TYPE_VIDEO_RANK: // ビデオランキング
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_PREMIUM_VIDEO_LIST: //プレミアムビデオ
            case TYPE_VIDEO_CONTENT_LIST: // ビデオコンテンツ一覧
            case TYPE_WATCHING_VIDEO_LIST: //視聴中ビデオ一覧
            case TYPE_CLIP_LIST_MODE_VIDEO: //ビデオタブ(クリップ)
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
    }

    /**
     * コンテンツのハイフン設定.
     *
     * @param holder holder
     * @param view view
     */
    private void setTabContentHyphen(final ViewHolder holder, final View view) {
        switch (mTabType) {
            case TAB_TV:
            case TAB_D_CHANNEL:
            case TAB_DEFAULT:
                holder.tv_recorded_hyphen = view.findViewById(R.id.item_common_result_recorded_content_hyphen);
                holder.tv_recorded_ch_name = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
                break;
            case TAB_D_ANIMATE:
            case TAB_D_TV:
            case TAB_VIDEO:
                break;
            default:
                break;
        }
    }

    /**
     * データの設定.
     *
     * @param holder 設定済みViewHolder
     */
    @SuppressWarnings("OverlyLongMethod")
    private void setShowDataVisibility(final ViewHolder holder) {
        switch (mType) {
            case TYPE_RECOMMEND_LIST://おすすめ番組・ビデオ
                setTabContentLayout(holder);
                break;
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
                holder.tv_rank.setVisibility(View.GONE);
                break;
            case TYPE_CLIP_LIST_MODE_TV: //TVタブ(クリップ)
                holder.ll_rating.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                holder.tv_rank.setVisibility(View.GONE);
                break;
            case TYPE_RENTAL_RANK: // レンタル一覧
            case TYPE_PREMIUM_VIDEO_LIST: //プレミアムビデオ
                holder.tv_rank.setVisibility(View.GONE);
                holder.tv_clip.setVisibility(View.GONE);
                break;
            case TYPE_RECORDING_RESERVATION_LIST: // 録画予約一覧
                holder.tv_clip.setVisibility(View.GONE);
                holder.rl_thumbnail.setVisibility(View.GONE);
                holder.iv_thumbnail.setVisibility(View.GONE);
                holder.ll_rating.setVisibility(View.GONE);
                break;
            case TYPE_RECORDED_LIST: // 録画番組一覧
                holder.tv_rank.setVisibility(View.GONE);
                holder.rl_thumbnail.setVisibility(View.GONE);
                holder.iv_thumbnail.setVisibility(View.GONE);
                holder.ll_rating.setVisibility(View.GONE);
                break;
            case TYPE_CONTENT_DETAIL_CHANNEL_LIST: // コンテンツ詳細チャンネル一覧
                holder.tv_rank.setVisibility(View.GONE);
                holder.ll_rating.setVisibility(View.GONE);
                holder.tv_line.setVisibility(View.VISIBLE);
                break;
            case TYPE_SEARCH_LIST: //検索
                holder.tv_rank.setVisibility(View.GONE);
                holder.ll_rating.setVisibility(View.GONE);
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
            if (listContentInfo.getRequestData() != null) {
                String clipType = listContentInfo.getRequestData().getType();
                //ひかりコンテンツ判定
                if (StringUtils.isHikariContents(clipType) || StringUtils.isHikariInDtvContents(clipType)) {
                    //クリップボタン表示設定
                    if (!mType.equals(ActivityTypeItem.TYPE_RECORDING_RESERVATION_LIST)) {
                        //クリップ状態が1以外の時は、非活性クリップボタンを表示
                        if (listContentInfo.isClipExec()) {
                            if (!UserInfoUtils.getClipActive(mContext)) {
                                holder.tv_clip.setBackgroundResource(R.mipmap.icon_tap_circle_normal_clip);
                            } else {
                                //DREM-1882 期限切れコンテンツのクリップ対応により、期限切れクリップコンテンツは一律でアクティブボタンを表示
                                if (listContentInfo.isClipStatus() || listContentInfo.isIsAfterLimitContents()) {
                                    holder.tv_clip.setBackgroundResource(R.drawable.common_clip_active_selector);
                                } else {
                                    holder.tv_clip.setBackgroundResource(R.drawable.common_clip_normal_selector);
                                }
                            }
                            holder.tv_clip.setVisibility(View.VISIBLE);
                        } else {
                            holder.tv_clip.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                holder.tv_clip.setVisibility(View.GONE);
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
            holder.tv_clip.setVisibility(View.VISIBLE);
            if (listContentInfo.isDownloadBtnHide()) {
                holder.tv_clip.setVisibility(View.INVISIBLE);
            } else {
                switch (listContentInfo.getDownloadFlg()) {
                    case DOWNLOAD_STATUS_ALLOW:
                        holder.tv_clip.setBackgroundResource(R.drawable.icon_circle_normal_download_selector);
                        break;
                    case DOWNLOAD_STATUS_LOADING:
                        holder.tv_clip.setBackgroundResource(R.drawable.icon_circle_active_cancel_selector);
                        break;
                    case DOWNLOAD_STATUS_COMPLETED:
                        holder.tv_clip.setBackgroundResource(R.drawable.icon_circle_normal_download_check_selector);
                        break;
                    default:
                        break;
                }
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
    public static class ViewHolder {
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
        RatingBarLayout ll_rating;
        /**
         * ラベル.
         */
        TextView tv_rank;
        /**
         * サブタイトル.
         */
        private TextView tv_time;
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
         * ハイフン.
         */
        TextView tv_recorded_hyphen;
        /**
         * 録画番組用チャンネル名.
         */
        TextView tv_recorded_ch_name;
        /**
         * サブタイトル.
         */
        TextView tv_sub_title = null;
        /**
         * 共通コンテンツなのか.
         */
        boolean isCommonContent = true;
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

    /**
     * サムネイル取得処理を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        isDownloadStop = true;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.stopConnect();
            mThumbnailProvider.removeAllMemoryCache();
        }
    }

    /**
     * 止めたサムネイル取得処理を再度取得可能な状態にする.
     */
    public void enableConnect() {
        DTVTLogger.start();
        isDownloadStop = false;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.enableConnect();
        }
    }

    /**
     * 外部からリストを更新するときに使用する.
     *
     * @param contentsDataList コンテンツデータ
     */
    public void setListData(final List<ContentsData> contentsDataList) {
        this.mListData = contentsDataList;
        //コンテンツデータにクリップ状態更新フラグ追加
        if (mListData != null && mListData.size() > 0) {
            for (int i = 0; i < mListData.size(); i++) {
                mListData.get(i).setClipStatusUpdate(true);
            }
        }
    }

    /**
     * 再利用のビュー最大countをリセット.
     */
    public void resetMaxItemCount() {
        mMaxItemCount = 0;
    }
}
