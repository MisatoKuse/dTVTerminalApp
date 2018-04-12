/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.view.RatingBarLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * コンテンツ詳細画面表示用Fragment.
 */
public class DtvContentsDetailFragment extends Fragment {

    /** コンテクスト.*/
    private Context mActivity = null;
    /** フラグメントビュー.*/
    private View mView = null;
    /** 詳細情報.*/
    public OtherContentsDetailData mOtherContentsDetailData = null;
    /** スタッフビュー.*/
    private LinearLayout mStaffLayout = null;
    /** タイトル/詳細一部文字.*/
    private TextView mTxtTitleShortDetail = null;
    /** タイトル/詳細全文字.*/
    private TextView mTxtTitleAllDetail = null;
    /** 評価.*/
    private RatingBarLayout mRatingBar;
    /** moreボタン.*/
    private TextView mTxtMoreText = null;
    /** ヘッダー.*/
    private TextView mTextHeader = null;
    /** サブヘッダー.*/
    private TextView mTextSubHeader = null;
    /** サービスアイコン.*/
    private ImageView mImgServiceIcon = null;
    /** サービスアイコン dTV(白背景ロゴ).*/
    private ImageView mImgServiceIconDtv = null;
    /** チャンネル名.*/
    private TextView mTxtChannelName = null;
    /** チャンネル日付.*/
    private TextView mTxtChannelDate = null;
    /** 全文表示フラグ.*/
    private boolean mIsAllText = false;
    /** 契約フラグ.*/
    private boolean mIsContract = true;
    /** クリップボタン.*/
    private ImageView mClipButton = null;
    /** 録画ボタン.*/
    private ImageView mRecordButton = null;
    /** 録画リスナー.*/
    private RecordingReservationIconListener mIconClickListener = null;
    /** スタッフ文字サイズ(title).*/
    private final static int TEXT_SIZE_12 = 12;
    /** スタッフ文字サイズ(内容).*/
    private final static int TEXT_SIZE_14 = 14;
    /** スタッフ margin 0.*/
    private final static int STAFF_MARGIN_0 = 0;
    /** パラメータ名「4kflg」 1.*/
    private final static int LABEL_STATUS_4KFLG_1 = 1;
    /** adinfo_array の adtype 9.*/
    private final static int LABEL_STATUS_ADTYPE_9 = 9;
    /** adinfo_array の adtype 2.*/
    private final static int LABEL_STATUS_ADTYPE_2 = 2;
    /** パラメータ名「copy」DTCP:CopyNever.*/
    private final static String LABEL_STATUS_COPY_COPYNEVER = "DTCP:CopyNever";
    /** パラメータ名「copy」DTCP:CopyNever.*/
    private final static String LABEL_STATUS_COPY_COPYFREE = "DTCP:CopyFree";
    /** パラメータ名「copy」DTCP:CopyNever.*/
    private final static String LABEL_STATUS_COPY_COPYONCE = "DTCP:CopyOnce";
    /** パラメータ名「copy」DTCP:CopyNever.*/
    private final static String LABEL_STATUS_COPY_COPYNOMORE = "DTCP:CopyNoMore";
    /** r_value PG-12.*/
    private final static String LABEL_STATUS_R_VALUE_PG_12 = "PG-12";
    /** r_value R-12.*/
    private final static String LABEL_STATUS_R_VALUE_R_12 = "R-12";
    /** r_value R-15.*/
    private final static String LABEL_STATUS_R_VALUE_R_15 = "R-15";
    /** r_value R-18.*/
    private final static String LABEL_STATUS_R_VALUE_R_18 = "R-18";
    /** r_value R-20.*/
    private final static String LABEL_STATUS_R_VALUE_R_20 = "R-20";

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        //コンテンツ詳細表示に必要なデータを取得する
        mOtherContentsDetailData = getArguments().getParcelable(ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY);
        return initView(container);
    }

    /**
     * 各タブ画面は別々に実現して表示されること.
     *
     * @param container コンテナ
     * @return view
     */
    private View initView(final ViewGroup container) {
        DTVTLogger.start();
        if (null == mView) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dtv_contents_detail_fragment, container, false);
        }
        //サービス/提供元
        mImgServiceIcon = mView.findViewById(R.id.dtv_contents_detail_fragment_service_provider);
        mImgServiceIconDtv = mView.findViewById(R.id.dtv_contents_detail_fragment_service_provider_dtv);
        //ヘッダー
        mTextHeader = mView.findViewById(R.id.dtv_contents_detail_fragment_contents_title);
        mTextSubHeader = mView.findViewById(R.id.dtv_contents_detail_fragment_contents_sub_title);
        mTxtChannelName = mView.findViewById(R.id.dtv_contents_detail_fragment_channel_name);
        mTxtChannelDate = mView.findViewById(R.id.dtv_contents_detail_fragment_channel_date);
        //省略
        mTxtTitleShortDetail = mView.findViewById(R.id.dtv_contents_detail_fragment_detail_info);
        //評価
        mRatingBar = mView.findViewById(R.id.dtv_contents_detail_fragment_rating);
        //全表示
        mTxtTitleAllDetail = mView.findViewById(R.id.dtv_contents_detail_fragment_all_info);
        //more
        mTxtMoreText = mView.findViewById(R.id.dtv_contents_detail_fragment_more_button);
        //スタッフ情報
        mStaffLayout = mView.findViewById(R.id.dtv_contents_detail_fragment_staff);
        mTxtMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //moreボタン押下で、全文表示に切り替える
                mIsAllText = true;
                mTxtTitleShortDetail.setVisibility(View.GONE);
                mTxtTitleAllDetail.setVisibility(View.VISIBLE);
                mTxtMoreText.setVisibility(View.GONE);
            }
        });
        mRecordButton = mView.findViewById(R.id.dtv_contents_detail_fragment_rec_iv);
        mClipButton = mView.findViewById(R.id.contents_detail_clip_button);

        if (mOtherContentsDetailData != null) {
            setClipButton(mClipButton);
            setDetailData();
        } else {
            mOtherContentsDetailData = new OtherContentsDetailData();
        }
        return mView;
    }

    /**
     * バックグラウンド復帰時にクリップボタンを更新する.
     */
    public void resumeClipButton() {
        //画面生成時は mClipButton が null のため実行されない(BG 復帰時のみ実行される)
        if (mClipButton != null) {
            setClipButton(mClipButton);
        }
    }

    /**
     * クリップボタンの表示/非表示を.
     *
     * @param clipButton クリップボタン
     */
    private void setClipButton(final ImageView clipButton) {

        //他サービスならクリップボタン非表示
        if (mOtherContentsDetailData != null) {
            if (mOtherContentsDetailData.isClipExec()) {
                clipButton.setVisibility(View.VISIBLE);
                if (mOtherContentsDetailData.isClipStatus()) {
                    clipButton.setBackgroundResource(R.mipmap.icon_circle_active_clip);
                    clipButton.setTag(BaseActivity.CLIP_ACTIVE_STATUS);
                } else {
                    clipButton.setBackgroundResource(R.mipmap.icon_circle_opacity_clip);
                    clipButton.setTag(BaseActivity.CLIP_OPACITY_STATUS);
                }
            } else {
                clipButton.setVisibility(View.GONE);
            }
        } else {
            clipButton.setVisibility(View.GONE);
        }

        clipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //クリップボタンイベント
                if (mIsContract) {
                    ClipRequestData data = setClipData(mOtherContentsDetailData.getVodMetaFullData());
                    //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、タグでクリップ済/未を判定する
                    Object clipTag = mClipButton.getTag();
                    if (clipTag.equals(BaseActivity.CLIP_ACTIVE_STATUS)) {
                        data.setClipStatus(true);
                    } else {
                        data.setClipStatus(false);
                    }
                    ((BaseActivity) mActivity).sendClipRequest(data, clipButton);
                } else {
                    //未契約時は契約導線を表示するためActivityに通知
                    ((ContentDetailActivity) mActivity).leadingContract();
                }
            }
        });
    }

    /**
     * クリップリクエストに必要なデータを作成する(コンテンツ詳細用).
     *
     * @param metaFullData VODメタデータ
     * @return Clipリクエストに必要なデータ
     */
    private static ClipRequestData setClipData(final VodMetaFullData metaFullData) {
        ClipRequestData requestData = null;
        if (metaFullData != null) {
            //コンテンツ詳細は、メタデータを丸ごと持っているため、そのまま利用する
            requestData = new ClipRequestData();
            requestData.setCrid(metaFullData.getCrid());
            requestData.setServiceId(metaFullData.getmService_id());
            requestData.setEventId(metaFullData.getmEvent_id());
            requestData.setTitleId(metaFullData.getTitle_id());
            requestData.setTitle(metaFullData.getTitle());
            requestData.setRValue(metaFullData.getR_value());
            requestData.setLinearStartDate(String.valueOf(metaFullData.getAvail_start_date()));
            requestData.setLinearEndDate(String.valueOf(metaFullData.getAvail_end_date()));
            requestData.setSearchOk(metaFullData.getmSearch_ok());
            requestData.setIsNotify(metaFullData.getDisp_type(), metaFullData.getmContent_type(),
                    String.valueOf(metaFullData.getAvail_end_date()), metaFullData.getmTv_service(), metaFullData.getDtv());
        }
        return requestData;
    }

    /**
     * 各Viewにコンテンツの詳細情報を渡す.
     */
    private void setDetailData() {
        //タイトル
        mTextHeader.setText(mOtherContentsDetailData.getTitle());
        //サブタイトル
        if (!TextUtils.isEmpty(mOtherContentsDetailData.getEpititle())) {
            mTextSubHeader.setText(mOtherContentsDetailData.getEpititle());
        } else {
            mTextSubHeader.setVisibility(View.GONE);
        }
        //画面表示
        StringUtils util = new StringUtils();
        //サービスアイコン
        int serviceIcon = util.getContentsServiceName(mOtherContentsDetailData.getServiceId());
        mImgServiceIcon.setImageResource(serviceIcon);
        String dtv = mOtherContentsDetailData.getDtv();
        //dtvの場合
        if (ContentDetailActivity.DTV_FLAG_ONE.equals(dtv)) {
            mImgServiceIconDtv.setVisibility(View.VISIBLE);
            mImgServiceIconDtv.setImageResource(R.mipmap.label_service_dtv_white);
            mRatingBar.setVisibility(View.GONE);
        } else {
            //VODの場合
            if (mOtherContentsDetailData.getServiceId() == 0
                    && (ContentDetailActivity.VIDEO_SERIES.equals(mOtherContentsDetailData.getDispType())
                    || ContentDetailActivity.VIDEO_PROGRAM.equals(mOtherContentsDetailData.getDispType()))) {
                //評価
                mRatingBar.setMiniFlg(false);
                mRatingBar.setRating((float) mOtherContentsDetailData.getRating());
                mTxtChannelName.setVisibility(View.GONE);
            } else {
                mRatingBar.setVisibility(View.GONE);
            }
        }
        //チャンネル名
        if (!TextUtils.isEmpty(mOtherContentsDetailData.getChannelName())) {
            mTxtChannelName.setText(mOtherContentsDetailData.getChannelName());
        } else {
            mTxtChannelName.setVisibility(View.GONE);
        }
        String contentsDetailInfo;
        setLabelStatus();
        contentsDetailInfo = selectDetail();
        //日付
        String date = mOtherContentsDetailData.getChannelDate();
        if (!TextUtils.isEmpty(date)) {
            mTxtChannelDate.setVisibility(View.VISIBLE);
            SpannableString spannableString = new SpannableString(date);
            int subCount = 0;
            if (date.contains(getString(R.string.contents_detail_hikari_d_channel_miss_viewing))) {
                subCount = 3;
            }
            //「見逃し」は黄色文字で表示する
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.contents_detail_video_miss_color)),
                    0, subCount, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTxtChannelDate.setText(spannableString);
        } else {
            mTxtChannelDate.setVisibility(View.GONE);
        }
        if (mOtherContentsDetailData.getStaffList() != null) {
            setStaff();
        } else {
            mStaffLayout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(contentsDetailInfo)) {
            mTxtTitleShortDetail.setText(contentsDetailInfo);
            mTxtTitleAllDetail.setText(contentsDetailInfo);
        }
        setClipButton(mClipButton);
    }

    /**
     * パラメータ名「adinfo_array」を参照(音声).
     * @param labelStatusList ラベルステータス画像リスト
     */
    public void setAdtype(final List<Integer> labelStatusList) {
        String[] adinfoArray = mOtherContentsDetailData.getAdinfoArray();
        if (adinfoArray != null && adinfoArray.length > 0) {
            int adtype = 0;
            if (adinfoArray[0] != null) {
                String[] arrayInfo = adinfoArray[0].split(getString(R.string.contents_detail_adinfo_array_pipe));
                if (arrayInfo.length > 1 && DBUtils.isNumber(arrayInfo[1])) {
                    adtype = Integer.parseInt(arrayInfo[1]);
                }
            }
            if (LABEL_STATUS_ADTYPE_9 == adtype) {
                labelStatusList.add(R.mipmap.label_status_sound);
            } else if (LABEL_STATUS_ADTYPE_2 == adtype) {
                labelStatusList.add(R.mipmap.label_status_voice);
            } else if (adinfoArray.length >= 2) {
                labelStatusList.add(R.mipmap.label_status_multilingual);
            }
        }
    }

    /**
     * パラメータ名「copy」を参照(コピー制限).
     * @param labelStatusList ラベルステータス画像リスト
     */
    public void setCopy(final List<Integer> labelStatusList) {
        String copy = mOtherContentsDetailData.getCopy();
        if (copy != null) {
            switch (copy) {
                case LABEL_STATUS_COPY_COPYNEVER:
                    labelStatusList.add(R.mipmap.label_status_copy_never);
                    break;
                case LABEL_STATUS_COPY_COPYFREE:
                    labelStatusList.add(R.mipmap.label_status_copy_free);
                    break;
                case LABEL_STATUS_COPY_COPYONCE:
                    labelStatusList.add(R.mipmap.label_status_copy_once);
                    break;
                case LABEL_STATUS_COPY_COPYNOMORE:
                default:
                    break;
            }
        }
    }

    /**
     * パラメータ名「r_value」を参照(R指定).
     * @param labelStatusList ラベルステータス画像リスト
     */
    public void setRvalue(final List<Integer> labelStatusList) {
        String rValue = mOtherContentsDetailData.getRvalue();
        if (rValue != null) {
            switch (rValue) {
                case LABEL_STATUS_R_VALUE_PG_12:
                    labelStatusList.add(R.mipmap.label_status_r_pg_12);
                    break;
                case LABEL_STATUS_R_VALUE_R_12:
                    labelStatusList.add(R.mipmap.label_status_r_r_12);
                    break;
                case LABEL_STATUS_R_VALUE_R_15:
                    labelStatusList.add(R.mipmap.label_status_r_r_15);
                    break;
                case LABEL_STATUS_R_VALUE_R_18:
                    labelStatusList.add(R.mipmap.label_status_r_r_18);
                    break;
                case LABEL_STATUS_R_VALUE_R_20:
                    labelStatusList.add(R.mipmap.label_status_r_r_20);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * ラベルを設定する.
     */
    private void setLabelStatus() {
        LinearLayout labelStatus = mView.findViewById(R.id.dtv_contents_detail_fragment_label_status_ll);
        labelStatus.removeAllViews();
        List<Integer> labelStatusList = new ArrayList<>();
        //NEW アイコン
        if (DBUtils.isNumber(mOtherContentsDetailData.getmStartDate())
                && DateUtils.isOneWeek(mOtherContentsDetailData.getmStartDate())) {
            labelStatusList.add(R.mipmap.label_status_new);
        }
        //4Kアイコン
        if (LABEL_STATUS_4KFLG_1 == mOtherContentsDetailData.getM4kflg()) {
            labelStatusList.add(R.mipmap.label_status_4k);
        }
        //音声アイコン
        setAdtype(labelStatusList);
        //コピー制限アイコン
        setCopy(labelStatusList);
        //Rアイコン
        setRvalue(labelStatusList);
        if (labelStatusList.size() > 0) {
            labelStatus.setVisibility(View.VISIBLE);
            for (int i = 0; i < labelStatusList.size(); i++) {
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i != 0) {
                    if (isAdded()) {
                        imageParams.setMargins((int) getResources().getDimension(R.dimen.contents_detail_clip_margin),
                                (int) getResources().getDimension(R.dimen.contents_tab_top_margin),
                                (int) getResources().getDimension(R.dimen.contents_tab_top_margin),
                                (int) getResources().getDimension(R.dimen.contents_tab_top_margin));
                    }
                }
                Context context = getContext();
                if (context != null) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(labelStatusList.get(i));
                    imageView.setLayoutParams(imageParams);
                    labelStatus.addView(imageView);
                }
            }
        } else {
            labelStatus.setVisibility(View.GONE);
        }
    }

    /**
     * スタッフ情報を表示する.
     */
    private void setStaff() {
        List<String> staffList = mOtherContentsDetailData.getStaffList();
        mStaffLayout.setVisibility(View.VISIBLE);
        mStaffLayout.removeAllViews();
        Context context = getContext();
        if (context != null) {
            for (int i = 0; i < staffList.size(); i++) {
                TextView tabTextView = new TextView(context);
                LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                String text = staffList.get(i);
                tabTextView.setGravity(Gravity.CENTER_VERTICAL);
                tabTextView.setTextColor(ContextCompat.getColor(context, R.color.contents_detail_schedule_detail_sub_title));
                tabTextView.setLineSpacing(getResources().getDimension(R.dimen.contents_detail_content_line_space), 1);
                contentParams.setMargins(STAFF_MARGIN_0, (int) getResources().getDimension(
                        R.dimen.contents_detail_staff_margin_top), STAFF_MARGIN_0, STAFF_MARGIN_0);
                if (text.contains(File.separator)) {
                    tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_12);
                    tabTextView.setText(text.substring(0, text.length() - 1));
                } else {
                    tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_14);
                    tabTextView.setText(text);
                }
                tabTextView.setLayoutParams(contentParams);
                mStaffLayout.addView(tabTextView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mTxtTitleAllDetail.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                final int DETAIL_INFO_TEXT_MAX_LINE = 4;
                Layout layout = mTxtTitleAllDetail.getLayout();
                if (layout != null) {
                    //コンテンツ情報の文字数が省略されているときは、省略文とmoreを表示する
                    int intTextViewCount = layout.getLineCount();
                    if (intTextViewCount > DETAIL_INFO_TEXT_MAX_LINE && !mIsAllText) {
                        mTxtTitleShortDetail.setVisibility(View.VISIBLE);
                        mTxtTitleAllDetail.setVisibility(View.GONE);
                        mTxtMoreText.setVisibility(View.VISIBLE);
                    } else {
                        //コンテンツ情報の文字数が省略されていないときは、全文のみを表示する
                        mTxtTitleShortDetail.setVisibility(View.GONE);
                        mTxtTitleAllDetail.setVisibility(View.VISIBLE);
                        mTxtMoreText.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });
    }

    /**
     * 詳細情報の更新.
     */
    public void noticeRefresh() {
        setDetailData();
    }

    /**
     * スタッフ情報の更新.
     */
    public void refreshStaff() {
        setStaff();
    }

    /**
     * チャンネル情報の更新.
     */
    public void refreshChannelInfo() {
        if (!TextUtils.isEmpty(mOtherContentsDetailData.getChannelName())) {
            mTxtChannelName.setText(mOtherContentsDetailData.getChannelName());
        }
    }

    /**
     * 次の優先順位で、商品詳細を返却する.
     * 1:商品詳細2(あらすじ)
     * 2:商品詳細1(解説)
     * 3:商品詳細3(みどころ)
     *
     * @return 商品詳細文字列
     */
    private String selectDetail() {
        if (mOtherContentsDetailData.getDetail() != null
                && !mOtherContentsDetailData.getDetail().isEmpty()) {
            //"あらすじ"を返却
            return mOtherContentsDetailData.getDetail();
        } else if (mOtherContentsDetailData.getComment() != null
                && !mOtherContentsDetailData.getComment().isEmpty()) {
            //"解説"を返却
            return mOtherContentsDetailData.getComment();
        } else if (mOtherContentsDetailData.getHighlight() != null
                && !mOtherContentsDetailData.getHighlight().isEmpty()) {
            //"みどころ"を返却
            return mOtherContentsDetailData.getHighlight();
        } else {
            return "";
        }
    }

    /**
     * 録画予約アイコンの 表示/非表示 を切り替え.
     *
     * @param visibility 表示/非表示の指定
     */
    public void changeVisibilityRecordingReservationIcon(final int visibility) {
        DTVTLogger.start("setVisibility:" + visibility);
        mRecordButton.setVisibility(visibility);
        DTVTLogger.end();
    }

    /**
     * 録画予約アイコンにOnClickListenerを登録.
     *
     * @param listener リスナー
     */
    public void setRecordingReservationIconListener(final RecordingReservationIconListener listener) {
        DTVTLogger.start();
        if (listener != null) {
            DTVTLogger.debug("setOnClickListener");
            mIconClickListener = listener;
            mRecordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DTVTLogger.debug("onClick RecordingReservationIcon");
                    // onClickをActivityに通知
                    mIconClickListener.onClickRecordingReservationIcon(v);
                }
            });
        } else {
            DTVTLogger.debug("Listener is Null");
            mIconClickListener = null;
            mRecordButton.setOnClickListener(null);
        }
        DTVTLogger.end();
    }

    /**
     * 録画予約アイコンのonClickを通知するリスナー.
     */
    public interface RecordingReservationIconListener {
        /**
         * 録画予約アイコンのonClickを通知する.
         *
         * @param v View
         */
        void onClickRecordingReservationIcon(View v);
    }

    /**
     * 〇〇日までという視聴期限を表示する.
     *
     * @param endDate 視聴期限文字列
     */
    public void displayEndDate(final long endDate) {
        DTVTLogger.start();
        String date = DateUtils.formatEpochToString(endDate);

        if (mTxtChannelDate != null) {
            String untilDate = StringUtils.getConnectStrings(date, getString(R.string.contents_detail_until_date));
            mTxtChannelDate.setText(untilDate);
        }
    }

    /**
     * 契約情報を更新する.
     *
     * @param isContract 契約情報
     */
    public void setContractInfo(final boolean isContract) {
        DTVTLogger.start();
        mIsContract = isContract;
    }
}