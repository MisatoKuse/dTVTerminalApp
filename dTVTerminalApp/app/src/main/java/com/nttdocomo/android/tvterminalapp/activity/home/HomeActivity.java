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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.DtvContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.HomeDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.RentalDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.WatchListenVideoListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalChListWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ホーム画面表示.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener,
        VideoGenreProvider.apiGenreListDataProviderCallback,
        VideoGenreProvider.GenreListMapCallback,
        DtvContentsDetailDataProvider.ApiDataProviderCallback,
        WatchListenVideoListDataProvider.WatchListenVideoListProviderCallback,
        RentalChListWebClient.RentalChListJsonParserCallback,
        RentalDataProvider.ApiDataProviderCallback,
        HomeDataProvider.ApiDataProviderCallback, UserInfoDataProvider.UserDataProviderCallback {

    /**
     * 表示するコンテンツを内包するLinearLayout.
     */
    private LinearLayout mLinearLayout = null;
    /**
     * 表示するコンテンツを内包するLinearLayout.
     */
    private RelativeLayout mRelativeLayout = null;
    /**
     * エラーダイアログが表示されているかのフラグ.
     */
    private boolean mIsCloseDialog = false;
    /**
     * onCreateが終了しているかのフラグ.
     */
    private boolean mIsOnCreateFinish = false;
    /**
     * NOW ON AIR用チャンネル一覧.
     */
    private ChannelList mChannelList = null;
    /**
     * 外部ブラウザー遷移先URL.
     */
    private final static String PR_URL = "https://www.hikaritv.net/video";
    /**
     * コンテンツ一覧数.
     */
    private final static int HOME_CONTENTS_LIST_COUNT = 8;
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
     * UIの上下表示順(視聴中ビデオ).
     */
    private final static int HOME_CONTENTS_SORT_WATCHING_VIDEO = HOME_CONTENTS_LIST_START_INDEX + 5;
    /**
     * UIの上下表示順(クリップ[テレビ]).
     */
    private final static int HOME_CONTENTS_SORT_TV_CLIP = HOME_CONTENTS_LIST_START_INDEX + 6;
    /**
     * UIの上下表示順(クリップ[ビデオ]).
     */
    private final static int HOME_CONTENTS_SORT_VOD_CLIP = HOME_CONTENTS_LIST_START_INDEX + 7;
    /**
     * Handler用の識別値(チャンネル一覧).
     */
    private final static int HOME_CONTENTS_CHANNEL_LIST = HOME_CONTENTS_LIST_COUNT + 2;
    /**
     * アダプタ内でのリスト識別用定数.
     */
    private final static int HOME_CONTENTS_DISTINCTION_ADAPTER = 10;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO Bundle内の"state"ではなくSharedPreferencesからペアリング状態を取得する
        setContentView(R.layout.home_main_layout);
        setTitleText(getString(R.string.str_app_title));
        enableHeaderBackIcon(false);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        mIsOnCreateFinish = false;
        initData();
        initView();
        getUserInfo();
    }

    /**
     * 汎用エラーダイアログ.
     *
     * @param message       エラーメッセージ
     * @param confirmTextId OKボタンに表示する文字のリソース
     */
    private void errorDialog(final String message, final int confirmTextId) {
        //重複表示防止
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
                public void onOKCallback(final boolean isOK) {
                    //OKボタン押下
                    showMainLayout();
                    mIsCloseDialog = false;
                }
            });

            failedRecordingReservationDialog.setDialogDismissCallback(new CustomDialog.DialogDismissCallback() {
                @Override
                public void onDialogDismissCallback() {
                    //ボタンタップ以外でダイアログが閉じた場合
                    showMainLayout();
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
        mLinearLayout.setVisibility(View.GONE);
        mRelativeLayout = findViewById(R.id.home_main_layout_progress_bar_Layout);
        mRelativeLayout.setVisibility(View.VISIBLE);
        ProgressBar progressBar = new ProgressBar(HomeActivity.this, null, android.R.attr.progressBarStyle);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mRelativeLayout.addView(progressBar, params);
    }

    /**
     * コンテンツ表示用レイアウトを表示する.
     */
    private void showMainLayout() {
        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        if (mLinearLayout.getVisibility() != View.VISIBLE) {
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        mRelativeLayout = findViewById(R.id.home_main_layout_progress_bar_Layout);
        if (mRelativeLayout.getVisibility() != View.GONE) {
            mRelativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //アプリ起動時のデータ取得はonCreateで実施済みのためonResumeでは行わない
        if (mIsOnCreateFinish) {
            //起動時はプログレスダイアログを表示
            initData();
            requestHomeData();
        }
        mIsOnCreateFinish = true;
    }

    /**
     * ホーム画面用データ取得開始.
     */
    private void requestHomeData() {
        networkCheck();
        //Home画面用データを取得
        HomeDataProvider homeDataProvider = new HomeDataProvider(this);
        homeDataProvider.getHomeData();
    }

    @Override
    public void onClick(final View view) {
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
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
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

        //レイアウトを非表示にする
        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        mLinearLayout.setVisibility(View.GONE);
        TextView agreementTextView = findViewById(R.id.home_main_layout_kytv);
        LinearLayout agreementRl = findViewById(R.id.home_main_layout_kyrl);
        ImageView prImageView = findViewById(R.id.home_main_layout_pr);
        agreementTextView.setOnClickListener(this);
        prImageView.setOnClickListener(this);

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
        ImageView rightArrowImageView = view.findViewById(R.id.home_main_item_right_arrow);
        //各一覧を遷移すること
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startTo(tag);
            }
        });
        rightArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startTo(tag);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.home_main_item_recyclerview);
        //コンテンツタイプを設定（NOW ON AIR）
        typeTextView.setText(typeContentName);
        //コンテンツカウントを設定（20）
        if (typeContentName.equals(getString(R.string.home_label_now_on_air))) {
            countTextView.setText(getString(R.string.home_now_on_air_channel_list));
        }
        //リサイクルビューデータ設定
        setRecyclerViewData(recyclerView, contentsDataList, tag);
    }

    /**
     * Now On Air行の情報描画のためアダプタにチャンネル一覧情報を渡す.
     *
     * @param channelList チャンネル一覧情報
     */
    private void setChannelData(final ChannelList channelList) {
        View view = mLinearLayout.getChildAt(HOME_CONTENTS_SORT_CHANNEL);
        if (channelList != null) {
            if (view != null) {
                //既にNow On Air行が描画されている場合はアダプタにチャンネルデータを渡して再描画する.
                RecyclerView recyclerView = view.findViewById(R.id.home_main_item_recyclerview);
                HomeRecyclerViewAdapter adapter = (HomeRecyclerViewAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.setCHannnelList(channelList);
                    adapter.notifyDataSetChanged();
                } else {
                    //データを渡すのに失敗した場合はHomeActivityでチャンネルデータを保持する.
                    mChannelList = channelList;
                }
            } else {
                //まだNow On Air行が描画されていない場合はHomeActivityでチャンネルデータを保持する.
                mChannelList = channelList;
            }
        }
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
            case HOME_CONTENTS_SORT_WATCHING_VIDEO:
                typeName = getResources().getString(R.string.nav_menu_item_watch_listen_video);
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
     * @param recyclerView    リサイクルビュー
     * @param contentsDataList コンテンツ情報
     * @param index            遷移先
     */
    private void setRecyclerViewData(final RecyclerView recyclerView, final List<ContentsData> contentsDataList, final int index) {
        DTVTLogger.start();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        HomeRecyclerViewAdapter horizontalViewAdapter = new HomeRecyclerViewAdapter(this, contentsDataList, index + HOME_CONTENTS_DISTINCTION_ADAPTER);
        recyclerView.setAdapter(horizontalViewAdapter);
        View footer = LayoutInflater.from(this).inflate(R.layout.home_main_layout_recyclerview_footer, recyclerView, false);
        RelativeLayout homeMore = footer.findViewById(R.id.home_main_layout_recyclerview_footer);
        //もっと見るの遷移先を設定
        homeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startTo(index);
            }
        });
        //リサイクルビューデータの設定
        horizontalViewAdapter.setFooterView(footer);
        if (index == HOME_CONTENTS_SORT_CHANNEL && mChannelList != null) {
            //Now On Airのデータセット時に、チャンネルデータが既にある場合にはアダプタに渡す.
            horizontalViewAdapter.setCHannnelList(mChannelList);
        }
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
            case HOME_CONTENTS_SORT_WATCHING_VIDEO:
                //視聴中ビデオへ遷移
                startActivity(WatchingVideoListActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_TV_CLIP:
                //クリップ一覧へ遷移
                startActivity(ClipListActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_VOD_CLIP:
                //クリップ一覧へ遷移
                final Bundle bundle = new Bundle();
                bundle.putInt(ClipListActivity.CLIP_LIST_START_PAGE, ClipListActivity.CLIP_LIST_PAGE_NO_OF_VOD);
                startActivity(ClipListActivity.class, bundle);
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
        public void handleMessage(final Message msg) {
            if (msg.what == HOME_CONTENTS_CHANNEL_LIST) {
                setChannelData((ChannelList) msg.obj);
            } else {
                setRecyclerView((List) msg.obj, msg.what);
            }
            showMainLayout();
        }
    };

    @Override
    public void tvScheduleListCallback(final List<ContentsData> channelList) {
        if (channelList != null && channelList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_CHANNEL, channelList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void dailyRankListCallback(final List<ContentsData> dailyRankList) {
        if (dailyRankList != null && dailyRankList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_TODAY, dailyRankList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void tvClipListCallback(final List<ContentsData> tvClipList) {
        if (tvClipList != null && tvClipList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_TV_CLIP, tvClipList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void vodClipListCallback(final List<ContentsData> vodClipList) {
        if (vodClipList != null && vodClipList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_VOD_CLIP, vodClipList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void videoRankCallback(final List<ContentsData> videoRankList) {
        if (videoRankList != null && videoRankList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_VIDEO, videoRankList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void watchingVideoCallback(final List<ContentsData> watchingVideoList) {
        if (watchingVideoList != null && watchingVideoList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_WATCHING_VIDEO, watchingVideoList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void recommendChannelCallback(final List<ContentsData> redChList) {
        if (redChList != null && redChList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_RECOMMEND_PROGRAM, redChList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void recommendVideoCallback(final List<ContentsData> redVdList) {
        if (redVdList != null && redVdList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_RECOMMEND_VOD, redVdList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    @Override
    public void channelListCallback(final ChannelList channelList) {
        if (channelList != null) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_CHANNEL_LIST, channelList);
            mHandler.sendMessage(msg);
        } else {
            showGetDataFailedDialog();
        }
    }

    /**
     * ユーザ情報取得処理.
     */
    private void getUserInfo() {
        networkCheck();
        //ユーザー情報の変更検知
        UserInfoDataProvider dataProvider = new UserInfoDataProvider(this, this);
        dataProvider.getUserInfo();
    }

    /**
     * 契約情報取得失敗ダイアログ.
     */
    private void getUserInfoErrorDialog() {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.get_user_info_error_message));
        failedRecordingReservationDialog.setCancelText(R.string.common_text_close);
        // Cancelable
        failedRecordingReservationDialog.setConfirmText(R.string.common_text_retry);
        failedRecordingReservationDialog.showDialog();
        failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                //リトライ
                initData();
                getUserInfo();
            }
        });

        failedRecordingReservationDialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                //ユーザ情報なし(未契約表示)
                requestHomeData();
            }
        });

        failedRecordingReservationDialog.setDialogDismissCallback(new CustomDialog.DialogDismissCallback() {
            @Override
            public void onDialogDismissCallback() {
                //ボタンタップ以外でダイアログが閉じた場合は閉じる選択と想定
                //ユーザ情報なし(未契約表示)
                requestHomeData();
            }
        });
    }

    /**
     * データ取得失敗ダイアログ.
     */
    private void showGetDataFailedDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = getResources().getString(R.string.get_contents_data_error_message);
                errorDialog(message, R.string.custom_dialog_ok);
            }
        });
    }

    /**
     * network状態確認.
     */
    private void networkCheck() {
        if (!NetWorkUtils.isOnline(this)) {
            initData();
            String message = getResources().getString(R.string.activity_start_network_error_message);
            errorDialog(message, R.string.custom_dialog_ok);
        }
    }

    @Override
    public void userInfoListCallback(final boolean isDataChange, final List<UserInfoList> userList) {
        if (!DBUtils.isCachingRecord(this, DBConstants.USER_INFO_LIST_TABLE_NAME)) {
            //UserInfoテーブルにデータがないため初回取得と判定
            if (userList == null || userList.size() < 1) {
                // 初回起動時または1度もH4d契約情報取得に成功していない状態で、
                // H4d契約情報取得に失敗した場合は「ひかりTV for docomoの契約情報取得に失敗しました。」
                // エラーダイアログ、「閉じる」「リトライ」ボタンを表示すること。
                //契約情報取得失敗
                initData();
                getUserInfoErrorDialog();
            } else {
                //契約情報取得成功
                requestHomeData();
            }
        } else {
            //UserInfo取得済み
            requestHomeData();
        }
    }

    @Override
    public void onRentalChListJsonParsed(final PurchasedChListResponse RentalChListResponse) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void rentalListCallback(final List<ContentsData> list) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void rentalListNgCallback() {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void watchListenVideoListCallback(final List<ContentsData> clipContentInfo) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void genreListCallback(final List<GenreCountGetMetaData> listData) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void genreListMapCallback(final Map<String, VideoGenreList> map, final List<String> firstGenreIdList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onContentsDetailInfoCallback(final ArrayList<VodMetaFullData> contentsDetailInfo, final boolean clipStatus) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onRoleListCallback(final ArrayList<RoleListMetaData> roleListInfo) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void recordingReservationResult(final RemoteRecordingReservationResultResponse response) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onRentalVodListCallback(final PurchasedVodListResponse response) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onRentalChListCallback(final PurchasedChListResponse response) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }
}