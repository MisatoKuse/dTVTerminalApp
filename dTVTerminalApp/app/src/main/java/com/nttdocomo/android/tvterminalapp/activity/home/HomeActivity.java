/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.HomeDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

import java.util.List;

/**
 * ホーム画面表示.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener,
        HomeDataProvider.ApiDataProviderCallback, UserInfoDataProvider.UserDataProviderCallback {

    /**
     * 表示するコンテンツを内包するLinearLayout.
     */
    private LinearLayout mLinearLayout = null;
    /**
     * エラーダイアログが表示されているかのフラグ.
     */
    private boolean mIsCloseDialog = false;
    /**
     * onCreateが終了しているかのフラグ.
     */
    private boolean mIsOnCreateFinish = false;
    /**
     * 外部ブラウザー遷移先URL.
     */
    private final static String PR_URL = "https://www.hikaritv.net/video";
    /**
     * コンテンツ一覧数.
     */
    private final static int HOME_CONTENTS_LIST_COUNT = 7;
    /**
     * ヘッダのmargin.
     */
    private final static int HOME_CONTENTS_LIST_START_INDEX = 2;
    /**
     * UIの上下表示順(NOW ON AIR).
     */
    private final static int HOME_CONTENTS_SORT_CHANNEL = HOME_CONTENTS_LIST_START_INDEX;
    /**
     * UIの上下表示順(おすすめ番組).
     */
    private final static int HOME_CONTENTS_SORT_RECOMMEND_PROGRAM = HOME_CONTENTS_LIST_START_INDEX + 1;
    /**
     * UIの上下表示順(おすすめビデオ).
     */
    private final static int HOME_CONTENTS_SORT_RECOMMEND_VOD = HOME_CONTENTS_LIST_START_INDEX + 2;
    /**
     * UIの上下表示順(今日のテレビランキング).
     */
    private final static int HOME_CONTENTS_SORT_TODAY = HOME_CONTENTS_LIST_START_INDEX + 3;
    /**
     * UIの上下表示順(ビデオランキング).
     */
    private final static int HOME_CONTENTS_SORT_VIDEO = HOME_CONTENTS_LIST_START_INDEX + 4;
    /**
     * UIの上下表示順(クリップ[テレビ]).
     */
    private final static int HOME_CONTENTS_SORT_TV_CLIP = HOME_CONTENTS_LIST_START_INDEX + 5;
    /**
     * UIの上下表示順(クリップ[ビデオ]).
     */
    private final static int HOME_CONTENTS_SORT_VOD_CLIP = HOME_CONTENTS_LIST_START_INDEX + 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO Bundle内の"state"ではなくSharedPreferencesからペアリング状態を取得する
        setContentView(R.layout.home_main_layout);
        setTitleText(getString(R.string.home_header_title));
        enableHeaderBackIcon(false);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        mIsOnCreateFinish = false;
        if (!NetWorkUtils.isOnline(this)) {
            String message = getResources().getString(R.string.activity_start_network_error_message);
            errorDialog(message, R.string.custom_dialog_ok);
        } else {
            getUserInfo();
            initView();
        }
    }

    /**
     * 汎用エラーダイアログ.
     *
     * @param message エラーメッセージ
     * @param confirmTextId OKボタンに表示する文字のリソース
     */
    private void errorDialog(final String message, final int confirmTextId) {
        if (!mIsCloseDialog) {
            CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
            failedRecordingReservationDialog.setContent(message);
            failedRecordingReservationDialog.setConfirmText(confirmTextId);
            // Cancelable
            failedRecordingReservationDialog.setCancelable(false);
            failedRecordingReservationDialog.showDialog();
            mIsCloseDialog = true;
            failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(boolean isOK) {
                    initView();
                    mIsCloseDialog = false;
                }
            });
        }
    }

    /**
     * プログレスバー表示.
     */
    private void initData() {
        //プログレスダイアログを表示する
        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        mLinearLayout.setVisibility(View.VISIBLE);
        ProgressBar progressBar = new ProgressBar(HomeActivity.this, null, android.R.attr.progressBarStyle);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        mLinearLayout.addView(progressBar, params);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //アプリ起動時のデータ取得はonCreateで実施済みのためonResumeでは行わない
        if (mIsOnCreateFinish) {
            requestHomeData();
        }
        mIsOnCreateFinish = true;
    }

    /**
     * ホーム画面用データ取得開始.
     */
    private void requestHomeData() {
        //Home画面用データを取得
        HomeDataProvider homeDataProvider = new HomeDataProvider(this);
        homeDataProvider.getHomeData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_main_layout_pr:
            case R.id.home_main_layout_kytv:
                if (isFastClick()) {
                    startBrowser();
                }
                break;
            case R.id.header_layout_menu:
                //ダブルクリックを防ぐ
                if (isFastClick()) {
                    onSampleGlobalMenuButton_PairLoginOk();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isFastClick()) {
                    //メニューから起動の場合はアプリ終了ダイアログを表示
                    showTips();
                }
                break;

            default:
                break;
        }
        return false;
    }

    /**
     * 機能
     * 外部ブラウザーを起動する.
     */
    private void startBrowser() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(PR_URL);
        intent.setData(content_url);
        startActivity(intent);
    }

    /**
     * 機能
     * ビューの初期化処理.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        mLinearLayout.setVisibility(View.VISIBLE);
        TextView agreementTextView = findViewById(R.id.home_main_layout_kytv);
        LinearLayout agreementRl = findViewById(R.id.home_main_layout_kyrl);
        ImageView prImageView = findViewById(R.id.home_main_layout_pr);
        agreementTextView.setVisibility(View.VISIBLE);
        agreementRl.setVisibility(View.VISIBLE);
        prImageView.setVisibility(View.VISIBLE);
        agreementTextView.setOnClickListener(this);
        prImageView.setOnClickListener(this);

        //TODO:暫定的にサンプル画像を設定する
        prImageView.setBackgroundResource(R.mipmap.home_pr);
        //縦横比を維持したまま幅100%に拡大縮小
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.home_pr);
        int drawableWidth = drawable.getIntrinsicWidth();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displaymetrics);
        float ratio = ((float) displaymetrics.widthPixels / (float) drawableWidth);
        LinearLayout.LayoutParams imgIp = new LinearLayout.LayoutParams(
                displaymetrics.widthPixels,
                (int) (drawable.getIntrinsicHeight() * ratio));
        prImageView.setLayoutParams(imgIp);

        //各コンテンツのビューを作成する
        for (int i = HOME_CONTENTS_LIST_START_INDEX; i < HOME_CONTENTS_LIST_COUNT + HOME_CONTENTS_LIST_START_INDEX; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.home_main_layout_item, null, false);
            view.setTag(i);
            view.setVisibility(View.GONE);
            mLinearLayout.addView(view);
        }
    }

    /**
     * Viewを非表示にする.
     */
    private void clearView() {
        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        mLinearLayout.setVisibility(View.VISIBLE);
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        TextView agreementTextView = findViewById(R.id.home_main_layout_kytv);
        LinearLayout agreementRl = findViewById(R.id.home_main_layout_kyrl);
        ImageView prImageView = findViewById(R.id.home_main_layout_pr);
        menuImageView.setVisibility(View.GONE);
        agreementTextView.setVisibility(View.GONE);
        agreementRl.setVisibility(View.GONE);
        prImageView.setVisibility(View.GONE);

        //各コンテンツのビューを作成する
        for (int i = HOME_CONTENTS_LIST_START_INDEX; i < HOME_CONTENTS_LIST_COUNT + HOME_CONTENTS_LIST_START_INDEX; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.home_main_layout_item, null, false);
            view.setVisibility(View.GONE);
            mLinearLayout.addView(view);
        }
    }

    /**
     * 機能
     * コンテンツ一覧ビューを設定.
     *
     * @param contentsDataList コンテンツ情報
     * @param tag              遷移先
     */
    private void setRecyclerView(final List<ContentsData> contentsDataList, final int tag) {
        DTVTLogger.start();
        String typeContentName = getContentTypeName(tag);
        String resultCount = String.valueOf(contentsDataList.size());
        View view = mLinearLayout.getChildAt(tag);
        view.setVisibility(View.VISIBLE);
        TextView typeTextView = view.findViewById(R.id.home_main_item_type_tx);
        TextView countTextView = view.findViewById(R.id.home_main_item_type_tx_count);
        //各一覧を遷移すること
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTo(tag);
            }
        });
        RecyclerView mRecyclerView = view.findViewById(R.id.home_main_item_recyclerview);
        //コンテンツタイプを設定（NOW ON AIR）
        typeTextView.setText(typeContentName);
        //コンテンツカウントを設定（20）
        if (typeContentName.equals(getString(R.string.home_label_now_on_air))) {
            countTextView.setText(getString(R.string.home_now_on_air_channel_list));
        } else {
            countTextView.setText(resultCount);
        }
        //リサイクルビューデータ設定
        setRecyclerViewData(mRecyclerView, contentsDataList, tag);
    }

    /**
     * 機能
     * コンテンツ一覧タイトル取得.
     *
     * @param tag コンテンツ種別
     * @return コンテンツ表示名
     */
    private String getContentTypeName(final int tag) {
        String typeName = "";
        switch (tag) {
            case HOME_CONTENTS_SORT_CHANNEL:
                typeName = getResources().getString(R.string.home_label_now_on_air);
                break;
            case HOME_CONTENTS_SORT_RECOMMEND_PROGRAM:
                typeName = getResources().getString(R.string.home_label_recommend_program);
                break;
            case HOME_CONTENTS_SORT_RECOMMEND_VOD:
                typeName = getResources().getString(R.string.home_label_recommend_video);
                break;
            case HOME_CONTENTS_SORT_TODAY:
                typeName = getResources().getString(R.string.daily_tv_ranking_title);
                break;
            case HOME_CONTENTS_SORT_VIDEO:
                typeName = getResources().getString(R.string.video_ranking_title);
                break;
            case HOME_CONTENTS_SORT_TV_CLIP:
                typeName = getResources().getString(R.string.nav_menu_item_tv_clip);
                break;
            case HOME_CONTENTS_SORT_VOD_CLIP:
                typeName = getResources().getString(R.string.nav_menu_item_vod_clip);
                break;
            default:
                break;
        }
        return typeName;
    }

    /**
     * 機能
     * コンテンツ一覧データを設定.
     *
     * @param mRecyclerView    リサイクルビュー
     * @param contentsDataList コンテンツ情報
     * @param index            遷移先
     */
    private void setRecyclerViewData(final RecyclerView mRecyclerView, final List<ContentsData> contentsDataList, final int index) {
        DTVTLogger.start();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        HomeRecyclerViewAdapter mHorizontalViewAdapter = new HomeRecyclerViewAdapter(this, contentsDataList);
        mRecyclerView.setAdapter(mHorizontalViewAdapter);
        View footer = LayoutInflater.from(this).inflate(R.layout.home_main_layout_recyclerview_footer, mRecyclerView, false);
        TextView mTextView = footer.findViewById(R.id.home_main_layout_recyclerview_footer);
        //もっと見るの遷移先を設定
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTo(index);
            }
        });
        //リサイクルビューデータの設定
        mHorizontalViewAdapter.setFooterView(footer);
    }

    /**
     * 機能
     * 遷移先を設定.
     *
     * @param index 遷移先
     */
    private void startTo(final int index) {
        switch (index) {
            case HOME_CONTENTS_SORT_CHANNEL:
                //チャンネルリスト一覧へ遷移
                startActivity(ChannelListActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_RECOMMEND_PROGRAM:
            case HOME_CONTENTS_SORT_RECOMMEND_VOD:
                //おすすめへ遷移
                startActivity(RecommendActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_TODAY:
                //今日のテレビランキングへ遷移
                startActivity(DailyTvRankingActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_VIDEO:
                //ビデオランキングへ遷移
                startActivity(VideoRankingActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_TV_CLIP:
            case HOME_CONTENTS_SORT_VOD_CLIP:
                //クリップ一覧へ遷移
                startActivity(ClipListActivity.class, null);
                break;
            default:
                break;
        }
    }

    /**
     * コンテンツ情報取得ハンドラ.
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setRecyclerView((List) msg.obj, msg.what);
        }
    };

    @Override
    public void tvScheduleListCallback(List<ContentsData> channelList) {
        if (channelList != null && channelList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_CHANNEL, channelList);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void dailyRankListCallback(List<ContentsData> dailyRankList) {
        if (dailyRankList != null && dailyRankList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_TODAY, dailyRankList);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void tvClipListCallback(List<ContentsData> tvClipList) {
        if (tvClipList != null && tvClipList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_TV_CLIP, tvClipList);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void vodClipListCallback(List<ContentsData> vodClipList) {
        if (vodClipList != null && vodClipList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_VOD_CLIP, vodClipList);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void videoRankCallback(List<ContentsData> videoRankList) {
        if (videoRankList != null && videoRankList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_VIDEO, videoRankList);
            mHandler.sendMessage(msg);
        } else {

            //仮実装
            //データ取得失敗ダイアログ
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = getResources().getString(R.string.get_contents_data_error_message);
                    errorDialog(message, R.string.custom_dialog_ok);
                }
            });
        }
    }

    @Override
    public void recommendChannelCallback(List<ContentsData> redChList) {
        if (redChList != null && redChList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_RECOMMEND_PROGRAM, redChList);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void recommendVideoCallback(List<ContentsData> redVdList) {
        if (redVdList != null && redVdList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_RECOMMEND_VOD, redVdList);
            mHandler.sendMessage(msg);
        }
    }

    //検討中
//    @Override
//    public void userInfoCallback(List<Map<String, String>> userList) {
//        if (!DBUtils.isCachingRecord(this, DBConstants.USER_INFO_LIST_TABLE_NAME)) {
//            //UserInfoテーブルにデータがないため初回取得と判定
//            if (userList == null || userList.size() < 1) {
//                // 初回起動時または1度もH4d契約情報取得に成功していない状態で、
//                // H4d契約情報取得に失敗した場合は「ひかりTV for docomoの契約情報取得に失敗しました。」
//                // エラーダイアログ、「閉じる」「リトライ」ボタンを表示すること。
//                //契約情報取得失敗
//                getUserInfoErrorDialog();
//            } else {
//                //契約情報取得成功
//                initView();
//                requestHomeData();
//            }
//        } else {
//            //UserInfo取得済み
//            if(false){
//                //情報取得失敗
//            }
//        }
//    }

    /**
     * ユーザ情報取得処理.
     */
    private void getUserInfo() {
        //ユーザー情報の変更検知
        UserInfoDataProvider dataProvider = new UserInfoDataProvider(this, this);
        dataProvider.getUserInfo();
    }

    /**
     * ネットワーク接続エラーダイアログ.
     */
    private void getUserInfoErrorDialog() {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.get_user_info_error_message));
        failedRecordingReservationDialog.setConfirmText(R.string.common_text_close);
        // Cancelable
        failedRecordingReservationDialog.setCancelText(R.string.common_text_retry);
        failedRecordingReservationDialog.showDialog();
        failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                //ユーザ情報なし(未契約表示)
                initView();
                requestHomeData();
            }
        });
        failedRecordingReservationDialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                //リトライ
                clearView();
                initData();
                getUserInfo();
            }
        });
    }

    @Override
    public void userInfoListCallback(boolean isDataChange, List<UserInfoList> userList) {
        if (!DBUtils.isCachingRecord(this, DBConstants.USER_INFO_LIST_TABLE_NAME)) {
            //UserInfoテーブルにデータがないため初回取得と判定
            if (userList == null || userList.size() < 1) {
                // 初回起動時または1度もH4d契約情報取得に成功していない状態で、
                // H4d契約情報取得に失敗した場合は「ひかりTV for docomoの契約情報取得に失敗しました。」
                // エラーダイアログ、「閉じる」「リトライ」ボタンを表示すること。
                //契約情報取得失敗
                getUserInfoErrorDialog();
            } else {
                //契約情報取得成功
                initView();
            }
        } else {
            //UserInfo取得済み
            requestHomeData();
        }
    }
}