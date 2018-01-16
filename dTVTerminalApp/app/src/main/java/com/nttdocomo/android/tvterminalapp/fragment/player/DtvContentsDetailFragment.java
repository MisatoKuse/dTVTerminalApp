/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.model.search.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

import java.util.List;


public class DtvContentsDetailFragment extends Fragment {

    public Context mActivity = null;
    private View view = null;
    public OtherContentsDetailData mOtherContentsDetailData = null;
    private LinearLayout staffLayout = null;
    private LinearLayout recommendLayout = null;
    private TextView mTxtTitleShortDetail = null;
    private TextView mTxtTitleAllDetail = null;
    private TextView mTxtMoreText = null;
    private TextView headerText = null;
    private TextView txtServiceName = null;
    private TextView txtChannelName = null;
    private TextView txtChannelDate = null;
    private TextView txtChannelLabel = null;
    private boolean mIsAllText = false;
    /**
     * 契約フラグ.
     */
    private boolean mIsContract = true;
    //クリップボタン
    private ImageView mClipButton = null;
    //サムネイルmargintop
    private final static int THUMBNAIL_MARGINTOP = 10;
    //サムネイルmarginright
    private final static int THUMBNAIL_MARGINRIGHT = 8;
    //サムネイルmarginbottom
    private final static int THUMBNAIL_MARGINBOTTOM = 10;
    //サムネイル幅さ display3分の1
    private final static int THUMBNAIL_WIDTH = 3;
    //サムネイル高さ サムネイル幅2分の1
    private final static int THUMBNAIL_HEIGHT = 2;
    //margin0
    private final static int THUMBNAIL_MARGIN0 = 0;
    //ライン高さ
    private final static int LINE_HEIGHT = 1;
    private RecordingReservationIconListener mIconClickListener = null;

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        //コンテンツ詳細表示に必要なデータを取得する
        mOtherContentsDetailData = getArguments().getParcelable(DtvContentsDetailActivity.RECOMMEND_INFO_BUNDLE_KEY);
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
        if (null == view) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dtv_contents_detail_fragment, container, false);
        }
        //サービス/提供元
        txtServiceName = view.findViewById(R.id.dtv_contents_detail_fragment_service_provider);
        //ヘッダー
        headerText = view.findViewById(R.id.dtv_contents_detail_fragment_contents_title);
        txtChannelName = view.findViewById(R.id.dtv_contents_detail_fragment_channel_name);
        txtChannelDate = view.findViewById(R.id.dtv_contents_detail_fragment_channel_date);

        //省略
        mTxtTitleShortDetail = view.findViewById(R.id.dtv_contents_detail_fragment_detail_info);
        //全表示
        mTxtTitleAllDetail = view.findViewById(R.id.dtv_contents_detail_fragment_all_info);
        //more
        mTxtMoreText = view.findViewById(R.id.dtv_contents_detail_fragment_more_button);
        //スタッフ情報
        staffLayout = view.findViewById(R.id.dtv_contents_detail_fragment_staff);
        //おすすめ作品情報
        recommendLayout = view.findViewById(R.id.dtv_contents_detail_fragment_recommend_item);
        view.findViewById(R.id.dtv_contents_detail_fragment_recommend).setVisibility(View.GONE);
        mTxtMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //moreボタン押下で、全文表示に切り替える
                mIsAllText = true;
                mTxtTitleShortDetail.setVisibility(View.GONE);
                mTxtTitleAllDetail.setVisibility(View.VISIBLE);
                mTxtMoreText.setVisibility(View.GONE);
            }
        });

        mClipButton = view.findViewById(R.id.contents_detail_clip_button);
        setClipButton(mClipButton);

        if (mOtherContentsDetailData != null) {
            setDetailData();
        } else {
            mOtherContentsDetailData = new OtherContentsDetailData();
        }
        return view;
    }

    /**
     * クリップボタンの表示/非表示を.
     * @param clipButton クリップボタン
     */
    private void setClipButton(final ImageView clipButton) {

        //他サービスならクリップボタン非表示
        if (mOtherContentsDetailData != null) {
            String serviceId = String.valueOf(mOtherContentsDetailData.getServiceId());
            if (serviceId.equals(SearchServiceType.ServiceId.HIKARI_TV_FOR_DOCOMO)) {
                if (mOtherContentsDetailData.ismClipExec()){
                    if (mOtherContentsDetailData.ismClipStatus()){
                        clipButton.setBackgroundResource(R.mipmap.icon_circle_active_clip);
                    }else{
                        clipButton.setBackgroundResource(R.mipmap.icon_circle_opacity_clip);
                    }
                }else{
                    clipButton.setVisibility(View.GONE);
                }
            } else {
                clipButton.setVisibility(View.GONE);
            }
        } else {
            clipButton.setVisibility(View.VISIBLE);
        }

        clipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //クリップボタンイベント
                if (mIsContract) {
                    //同じ画面で複数回クリップ操作をした時にクリップ済/未の判定ができないため、画像比較でクリップ済/未を判定する
                    Bitmap clipButtonBitmap = ((BitmapDrawable) clipButton.getBackground()).getBitmap();
                    Bitmap activClipBitmap = ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),
                            R.mipmap.icon_circle_active_clip, null)).getBitmap();
                    if (clipButtonBitmap.equals(activClipBitmap)) {
                        mOtherContentsDetailData.getVodMetaFullData().setClipStatus(true);
                    } else {
                        mOtherContentsDetailData.getVodMetaFullData().setClipStatus(false);
                    }
                    ((BaseActivity) mActivity).sendClipRequest(setClipData(mOtherContentsDetailData.getVodMetaFullData()), clipButton);
                } else {
                    //未契約時は契約導線を表示するためActivityに通知
                    ((DtvContentsDetailActivity) mActivity).leadingContract();
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
        //コンテンツ詳細は、メタデータを丸ごと持っているため、そのまま利用する
        ClipRequestData requestData = new ClipRequestData();
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
        return requestData;
    }

    /**
     * 各Viewにコンテンツの詳細情報を渡す.
     */
    private void setDetailData() {
        headerText.setText(mOtherContentsDetailData.getTitle());
        //画面表示
        StringUtil util = new StringUtil(getContext());
        String strServiceName = util.getContentsServiceName(mOtherContentsDetailData.getServiceId());
        String contentsDetailInfo;
        txtServiceName.setText(strServiceName);
        contentsDetailInfo = selectDetail();
        boolean isFlag = false;
        if (!TextUtils.isEmpty(mOtherContentsDetailData.getChannelName())) {
            txtChannelName.setText(mOtherContentsDetailData.getChannelName());
        } else {
            isFlag = true;
        }
        if (!TextUtils.isEmpty(mOtherContentsDetailData.getChannelDate())) {
            txtChannelDate.setText(mOtherContentsDetailData.getChannelDate());
        } else {
            isFlag = true;
        }
        if (mOtherContentsDetailData.getStaffList() != null) {
            setStaff();
        } else {
            staffLayout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(contentsDetailInfo)) {
            mTxtTitleShortDetail.setText(contentsDetailInfo);
            mTxtTitleAllDetail.setText(contentsDetailInfo);
        }
    }

    /**
     * スタッフ情報を表示する.
     */
    private void setStaff() {
        List<String> staffList = mOtherContentsDetailData.getStaffList();
        staffLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < staffList.size(); i++) {
            RelativeLayout itemLayout = new RelativeLayout(getContext());
            itemLayout.setBackgroundResource(R.drawable.rectangele_contents_detail);
            TextView tabTextView = new TextView(getContext());
            RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) getResources().getDimension(R.dimen.contents_detail_tabs_height));
            contentParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) getResources().getDimension(R.dimen.contents_detail_tabs_height));
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.ic_chevron_right_white_24dp);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            imageView.setLayoutParams(imageParams);
            contentParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            tabTextView.setLayoutParams(contentParams);
            tabTextView.setText(staffList.get(i));
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            itemLayout.setTag(i);
            tabTextView.getPaint().setFakeBoldText(true);
            tabTextView.setTextColor(Color.GRAY);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO スタッフ詳細画面へ遷移　今回はやらない
                }
            });
            itemLayout.addView(tabTextView);
            itemLayout.addView(imageView);
            staffLayout.addView(itemLayout);
        }
    }

    private void setRecommendLayout() {
        recommendLayout.removeAllViews();
        ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(getContext());
        for (int i = 0; i < 2; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_common_result, null, false);
            LinearLayout ratingLayout = view.findViewById(R.id.item_common_result_content_rating);
            ImageView thumbnail = view.findViewById(R.id.item_common_result_thumbnail_iv);
            TextView line = view.findViewById(R.id.item_common_result_line);
            TextView time = view.findViewById(R.id.item_common_result_content_time);
            time.setText("test" + i);
            ratingLayout.setVisibility(View.GONE);
            DisplayMetrics DisplayMetrics = getContext().getResources().getDisplayMetrics();
            float density = DisplayMetrics.density;
            float mWidth = (float) DisplayMetrics.widthPixels / THUMBNAIL_WIDTH;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) mWidth, (int) mWidth / THUMBNAIL_HEIGHT);
            layoutParams.setMargins(THUMBNAIL_MARGIN0, (int) density * THUMBNAIL_MARGINTOP,
                    (int) density * THUMBNAIL_MARGINRIGHT, (int) density * THUMBNAIL_MARGINBOTTOM);
            thumbnail.setLayoutParams(layoutParams);
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) density * LINE_HEIGHT);
            line.setLayoutParams(layoutParams);
            line.setBackgroundResource(R.drawable.rectangele_contents_detail);
            thumbnail.setImageResource(R.drawable.test_image);
            thumbnail.setTag("https://img.hikaritv.net/thumbnail/VOD640/a032c9b.jpg");
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(thumbnail, "https://img.hikaritv.net/thumbnail/VOD640/a032c9b.jpg");
            if (bitmap != null) {
                thumbnail.setImageBitmap(bitmap);
            }
            recommendLayout.addView(view);
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
        boolean flag = false;
        if (!TextUtils.isEmpty(mOtherContentsDetailData.getChannelName())) {
            txtChannelName.setText(mOtherContentsDetailData.getChannelName());
            flag = true;
        }
        if (!TextUtils.isEmpty(mOtherContentsDetailData.getChannelDate())) {
            txtChannelDate.setText(mOtherContentsDetailData.getChannelDate());
            if (flag) {
                flag = true;
            }
        } else {
            flag = false;
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
    public void changeVisiblityRecordingReservationIcon(final int visibility) {
        DTVTLogger.start("setVisibility:" + visibility);
        view.findViewById(R.id.dtv_contents_detail_fragment_rec_iv).setVisibility(visibility);
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
            view.findViewById(R.id.dtv_contents_detail_fragment_rec_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DTVTLogger.debug("onClick RecordingReservationIcon");
                    // onClickをActivityに通知
                    mIconClickListener.onClickRecordingReservationIcon(v);
                }
            });
        } else {
            DTVTLogger.debug("Listener is Null");
            mIconClickListener = null;
            view.findViewById(R.id.dtv_contents_detail_fragment_rec_iv).setOnClickListener(null);
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

        if (txtChannelDate != null) {
            String untilDate = StringUtil.getConnectStrings(date, getString(R.string.contents_detail_until_date));
            txtChannelDate.setText(untilDate);
        }
    }

    /**
     * クリップボタンの表示/非表示を変更する.
     *
     * @param visibility true:表示 false:非表示
     */
    public void changeClipButtonVisibility(final boolean visibility) {
        DTVTLogger.start();
        if (mClipButton != null) {
            if (visibility) {
                mClipButton.setVisibility(View.VISIBLE);
            } else {
                mClipButton.setVisibility(View.GONE);
            }
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