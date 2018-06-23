/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewItemDecoration;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.GenreListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.HomeDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.RentalDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.WatchListenVideoListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopHomeDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopUserInfoDataConnect;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalChListWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ホーム画面表示.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener,
        GenreListDataProvider.ApiDataProviderCallback,
        GenreListDataProvider.GenreListMapCallback,
        ContentsDetailDataProvider.ApiDataProviderCallback,
        WatchListenVideoListDataProvider.WatchListenVideoListProviderCallback,
        RentalChListWebClient.RentalChListJsonParserCallback,
        RentalDataProvider.ApiDataProviderCallback,
        HomeDataProvider.ApiDataProviderCallback, UserInfoDataProvider.UserDataProviderCallback,
        HomeRecyclerViewAdapter.ItemClickCallback {

    /**
     * 表示するコンテンツを内包するLinearLayout.
     */
    private LinearLayout mLinearLayout = null;
    /**
     * 未契約者導線.
     */
    private LinearLayout mAgreementRl = null;
    /**
     * ホーム画面のスクロール部.
     */
    private ScrollView mScrollView = null;
    /**
     * エラーダイアログが表示されているかのフラグ.
     */
    private boolean mIsCloseDialog = false;
    /**
     * NOW ON AIR用チャンネル一覧.
     */
    private final ChannelList mChannelList = null;
    /**
     * コンテンツ一覧数.
     */
    private final static int HOME_CONTENTS_LIST_COUNT = 10;
    /**
     * ヘッダのmargin.
     */
    private final static int HOME_CONTENTS_LIST_START_INDEX = 1;
    /**
     * UIの上下表示順(NOW ON AIR).
     */
    public final static int HOME_CONTENTS_SORT_CHANNEL = HOME_CONTENTS_LIST_START_INDEX;
    /**
     * UIの上下表示順(おすすめ番組).
     */
    public final static int HOME_CONTENTS_SORT_RECOMMEND_PROGRAM = HOME_CONTENTS_LIST_START_INDEX + 1;
    /**
     * UIの上下表示順(おすすめビデオ).
     */
    public final static int HOME_CONTENTS_SORT_RECOMMEND_VOD = HOME_CONTENTS_LIST_START_INDEX + 2;
    /**
     * UIの上下表示順(今日のテレビランキング).
     */
    public final static int HOME_CONTENTS_SORT_TODAY = HOME_CONTENTS_LIST_START_INDEX + 3;
    /**
     * UIの上下表示順(ビデオランキング).
     */
    public final static int HOME_CONTENTS_SORT_VIDEO = HOME_CONTENTS_LIST_START_INDEX + 4;
    /**
     * UIの上下表示順(視聴中ビデオ).
     */
    public final static int HOME_CONTENTS_SORT_WATCHING_VIDEO = HOME_CONTENTS_LIST_START_INDEX + 5;
    /**
     * UIの上下表示順(クリップ[テレビ]).
     */
    public final static int HOME_CONTENTS_SORT_TV_CLIP = HOME_CONTENTS_LIST_START_INDEX + 6;
    /**
     * UIの上下表示順(クリップ[ビデオ]).
     */
    public final static int HOME_CONTENTS_SORT_VOD_CLIP = HOME_CONTENTS_LIST_START_INDEX + 7;
    /**
     * UIの上下表示順(プレミアム).
     */
    public final static int HOME_CONTENTS_SORT_PREMIUM = HOME_CONTENTS_LIST_START_INDEX + 8;
    /**
     * UIの上下表示順(レンタル).
     */
    public final static int HOME_CONTENTS_SORT_RENTAL = HOME_CONTENTS_LIST_START_INDEX + 9;
    /**
     * エラー情報の取得用に追加（ジャンル一覧）.
     */
    public final static int HOME_CONTENTS_LIST_PER_GENRE =
            HOME_CONTENTS_LIST_START_INDEX + 10;
    /**
     * エラー情報の取得用に追加（デイリーランク）.
     */
    public final static int HOME_CONTENTS_DAILY_RANK_LIST =
            HOME_CONTENTS_LIST_START_INDEX + 11;
    /**
     * エラー情報の取得用に追加（番組表）.
     */
    public final static int HOME_CONTENTS_TV_SCHEDULE =
            HOME_CONTENTS_LIST_START_INDEX + 12;
    /**
     * HomeDataProvider.
     */
    private HomeDataProvider mHomeDataProvider = null;
    /**
     * UserInfoDataProvider.
     */
    private UserInfoDataProvider mUserInfoDataProvider = null;
    /**
     * 検索完了フラグ.
     */
    private boolean mIsSearchDone = false;

    /**
     * 情報取得に失敗したときのフラグ.
     */
    private boolean mPartDataGetFailed = false;

    /**
     * ホーム画面表示時にdアカウントが取得できていなかった場合に、取得後にユーザー情報を取得しに行くフラグ.
     */
    private boolean mUserInfoGetRequest = false;

    /**
     * dアカウントの取得が行えない事が確定した場合はtrueに変更する.
     */
    private boolean mIsDaccountGetNg = false;
    /**
     * ユーザーがスクロール操作を行ったならばtrueにして、以後のスクロール位置補正をスキップさせる.
     */
    private boolean mAlreadyScroll = false;
    /**
     * ユーザーのスクロールを検知する為の、以前のスクロール位置.
     */
    private int mOldScrollPosition = 0;

    /**
     * ホーム画面でOTTチェック処理フラグ
     */
    private boolean isOttChecked = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_main_layout);
        setTitleText(getString(R.string.str_app_title));
        enableHeaderBackIcon(false);
        enableStbStatusIcon(true);
        setStatusBarColor(true);
        initView();
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
            mIsCloseDialog = true;
            CustomDialog failedRecordingReservationDialog = new CustomDialog(HomeActivity.this, CustomDialog.DialogType.ERROR);
            failedRecordingReservationDialog.setContent(message);
            failedRecordingReservationDialog.setConfirmText(confirmTextId);
            // Cancelable
            failedRecordingReservationDialog.setCancelable(false);
            failedRecordingReservationDialog.showDialog();

            failedRecordingReservationDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    //OKボタン押下
                    showProgessBar(false);
                    mIsCloseDialog = false;
                }
            });

            failedRecordingReservationDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
                @Override
                public void otherDismissCallback() {
                    //ボタンタップ以外でダイアログが閉じた場合
                    showProgessBar(false);
                    mIsCloseDialog = false;
                }

                @Override
                public void allDismissCallback() {
                    //NOP
                }
            });
        }
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgessBar プロセスバーを表示するかどうか
     */
    private void showProgessBar(final boolean showProgessBar) {
        mIsSearchDone = !showProgessBar;
        enableGlobalMenuIcon(!showProgessBar);
        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        RelativeLayout relativeLayout = findViewById(R.id.home_main_layout_progress_bar_Layout);
        if (showProgessBar) {
            mLinearLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

            //プログレスの解除＝新情報の追加なので、スクロール位置の補正を行う
            if (!mAlreadyScroll) {
                //既にユーザー操作によるスクロールがまだ行われていない場合は、補正を行う
                mScrollView.setScrollY(0);
            }
        }
    }

    @Override
    protected void onResume() {
        //TODO: dアカウントチェック、serviceToken取得、契約情報取得一連化実装対応必要ですが、暫定的にdアカウントチェックのみ実装
        if (!isOttChecked) {
            isOttChecked = true;
            setDaccountControl();
        }

        //ユーザー情報取得開始(super.onResumeで行われるdアカウントの取得よりも先に行う事で、
        // dアカウントが未取得だった場合のユーザー情報再取得への流れを明確化する)
        getUserInfoStart();

        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_home));
        DlnaManager.shared().Start(getApplicationContext());
    }

    /**
     * ホーム画面表示時のユーザー情報取得部.
     *
     * (複数個所から呼ばれることになったので、をonResumeから分離)
     */
    private void getUserInfoStart() {
        DTVTLogger.start();

        //ユーザー情報リクエストをリセット
        mUserInfoGetRequest = false;

        mUserInfoDataProvider = new UserInfoDataProvider(this, this);

        //アプリ起動時のデータ取得ユーザ情報未取得又は時間切れ又はonCreateから開始した場合はユーザ情報取得から
        if (mUserInfoDataProvider.isUserInfoTimeOut()
                && !TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferencesDaccountId(this))) {
            if (networkCheck()) {
                getUserInfo();
            } else {
                if (!mIsSearchDone) {
                    //起動時はプログレスダイアログを表示
                    requestHomeData();
                }
            }
        } else {
            DTVTLogger.debug("getDaccount="
                    + SharedPreferencesUtils.getSharedPreferencesDaccountId(this));
            DTVTLogger.debug("userInfo Timeout?=" + mUserInfoDataProvider.isUserInfoTimeOut());

            if (!mIsSearchDone) {
                //dアカウントが取れていないので、取得後のコールバックにユーザー情報取得を依頼する
                mUserInfoGetRequest = true;

                //起動時はプログレスダイアログを表示
                requestHomeData();
            }
        }

        DTVTLogger.end();
    }

    @Override
    protected void onDaccountOttGetComplete(final boolean result) {
        //dアカウントの取得が終わった際に呼ばれるコールバック
        super.onDaccountOttGetComplete(result);

        DTVTLogger.start("this is home. result = " + result
                + "/mUserInfoGetRequest = " + mUserInfoGetRequest);
        DTVTLogger.debug("onDaccountOttGetComplete daccount = "
                + SharedPreferencesUtils.getSharedPreferencesDaccountId(
                getApplicationContext()));

        //ユーザー情報取得依頼をチェック
        if (mUserInfoGetRequest && result) {
            //依頼が出ているので、dアカウントの取得に成功していればユーザー情報の取得を開始
            getUserInfo();
        } else if (!result) {
            DTVTLogger.debug("onDaccountOttGetComplete result=false");

            //dアカウントが取得できない事が確定したので、バナーの表示を行う
            mIsDaccountGetNg = true;

            //バナー表示の更新の為、UIタスクに処理を移譲
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DTVTLogger.debug("onDaccountOttGetComplete call showHomeBanner");
                    showHomeBanner();
                }
            });
        }

        //ユーザー情報取得依頼フラグをクリア（ユーザー情報取得側でも行っているが、dアカウント取得に失敗した時の為にここでもクリア）
        mUserInfoGetRequest = false;

        DTVTLogger.end();
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        //通信を再開
        if (mHomeDataProvider != null) {
            mHomeDataProvider.enableConnect();
        }
        if (mUserInfoDataProvider != null) {
            mUserInfoDataProvider.enableConnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //通信を止める
        if (mHomeDataProvider != null) {
            StopHomeDataConnect stopHomeDataConnect = new StopHomeDataConnect();
            stopHomeDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mHomeDataProvider);
        }

        if (mUserInfoDataProvider != null) {
            StopUserInfoDataConnect stopUserInfoDataConnect = new StopUserInfoDataConnect();
            stopUserInfoDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInfoDataProvider);
        }
    }

    /**
     * ホーム画面用データ取得開始.
     */
    private void requestHomeData() {
        //Home画面用データを取得
        if (networkCheck()) {
            showProgessBar(true);
            showHomeBanner();
            mHomeDataProvider = new HomeDataProvider(this);
            mHomeDataProvider.getHomeData();
        }
    }

    /**
     * PR画像、契約導線表示切替.
     */
    private void showHomeBanner() {
        UserState userState = UserInfoUtils.getUserState(this);
        switch (userState) {
            case LOGIN_OK_CONTRACT_NG:
                DTVTLogger.debug("showHomeBanner_LOGIN_OK_CONTRACT_NG");
                mAgreementRl.setVisibility(View.VISIBLE);
                break;
            case CONTRACT_OK_PAIRING_NG:
                DTVTLogger.debug("showHomeBanner_CONTRACT_OK_PAIRING_NG");
            case CONTRACT_OK_PARING_OK:
                DTVTLogger.debug("showHomeBanner_CONTRACT_OK_PARING_OK");
                mAgreementRl.setVisibility(View.GONE);
                break;
            case LOGIN_NG:
                DTVTLogger.debug("showHomeBanner_LOGIN_NG");
                //dアカウント取得前と取得失敗の場合
                if (mIsDaccountGetNg) {
                    DTVTLogger.debug("showHomeBanner_mIsDaccountGetNg");
                    //バナーのテキストを書き換える
                    TextView textView = findViewById(R.id.home_main_layout_top_wire_area_text);
                    textView.setText(getString(R.string.home_no_login_info_text));
                    TextView loginButton = findViewById(R.id.home_main_layout_kytv);
                    loginButton.setText(getString(R.string.home_login_button));
                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            //Dアカウント設定
                            DaccountUtils.startDAccountApplication(HomeActivity.this);
                        }
                    });
                    //dアカウントが取得できない事が確定したので、PR画像のバナーを表示する
                    mAgreementRl.setVisibility(View.VISIBLE);
                    break;
                }
                //確定前はバナーを表示しないので、ここでbreakは行わない
            default:
                DTVTLogger.debug("showHomeBanner_default");
                //情報の取得前は各バナーは表示しないように変更
                mAgreementRl.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(final View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.home_main_layout_kytv:
                if (isFastClick()) {
                    startBrowser(UrlConstants.WebUrl.PR_URL);
                }
                break;
            default:
                break;
        }
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
        mAgreementRl = findViewById(R.id.home_main_layout_kyrl);
        mAgreementRl.setVisibility(View.GONE);
        agreementTextView.setOnClickListener(this);

        //スクロールビューの操作を行う
        initScrollView();

        //各コンテンツのビューを作成する
        for (int i = HOME_CONTENTS_LIST_START_INDEX; i < HOME_CONTENTS_LIST_COUNT + HOME_CONTENTS_LIST_START_INDEX; i++) {
            final View view = setContentsView(i);
            mLinearLayout.addView(view);
            RecyclerView recyclerView = view.findViewById(R.id.home_main_item_recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new HomeRecyclerViewItemDecoration(this));
        }
    }

    /**
     * ホーム画面の主要情報を表示するスクロールビューの初期化処理を行う.
     */
    private void initScrollView() {
        //スクロール実行フラグの初期化
        mAlreadyScroll = false;

        //スクロールビューの取得
        mScrollView = findViewById(R.id.home_main_layout_scroll_view);

        //スクロール検知の実装
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //スクロールの縦位置を取得
                        mOldScrollPosition = view.getScrollY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 現在の縦位置と以前の縦位置を比較して違いがあれば、スクロールが発生
                        if (mOldScrollPosition != view.getScrollY()) {
                            DTVTLogger.debug("scrolling");
                            //スクロールを行ったスイッチをセット。以後のスクロール位置補正は、最上段にはならない
                            mAlreadyScroll = true;
                        }

                        //今の位置を代入する
                        mOldScrollPosition = view.getScrollY();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void setCountTextView(final String typeContentName, final View view, final int tag) {
        super.setCountTextView(typeContentName, view, tag);
        TextView countTextView = view.findViewById(R.id.home_main_item_type_tx_count);
        //各一覧を遷移すること
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startTo(tag);
            }
        });
        //コンテンツカウントを設定（20）
        if (typeContentName.equals(getString(R.string.home_label_now_on_air))) {
            countTextView.setText(getString(R.string.home_now_on_air_channel_list));
        }
    }
    @Override
    protected String getContentTypeName(final int tag) {
        super.getContentTypeName(tag);
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
            case HOME_CONTENTS_SORT_PREMIUM:
                typeName = getResources().getString(R.string.nav_menu_item_premium_video);
                break;
            case HOME_CONTENTS_SORT_RENTAL:
                typeName = getResources().getString(R.string.rental_title);
                break;
            default:
                break;
        }
        return typeName;
    }

    @Override
    protected void saveAdapter(final int index, final HomeRecyclerViewAdapter horizontalViewAdapter) {
        super.saveAdapter(index, horizontalViewAdapter);
        if (index == HOME_CONTENTS_SORT_CHANNEL && mChannelList != null) {
            //Now On Airのデータセット時に、チャンネルデータが既にある場合にはアダプタに渡す.
            horizontalViewAdapter.setChannnelList(mChannelList);
        }
    }
    @Override
    protected void startTo(final int index) {
        super.startTo(index);
        Bundle bundle;
        switch (index) {
            case HOME_CONTENTS_SORT_CHANNEL:
                //チャンネルリスト一覧へ遷移
                startActivity(ChannelListActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_RECOMMEND_PROGRAM:
                startActivity(RecommendActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_RECOMMEND_VOD:
                //おすすめへ遷移
                bundle = new Bundle();
                bundle.putInt(RecommendActivity.RECOMMEND_LIST_START_PAGE, RecommendActivity.RECOMMEND_LIST_PAGE_NO_OF_VOD);
                startActivity(RecommendActivity.class, bundle);
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
                bundle = new Bundle();
                bundle.putInt(ClipListActivity.CLIP_LIST_START_PAGE, ClipListActivity.CLIP_LIST_PAGE_NO_OF_VOD);
                startActivity(ClipListActivity.class, bundle);
                break;
            case HOME_CONTENTS_SORT_PREMIUM:
                //プレミアムビデオ一覧へ遷移
                startActivity(PremiumVideoActivity.class, null);
                break;
            case HOME_CONTENTS_SORT_RENTAL:
                //レンタル一覧へ遷移
                startActivity(RentalListActivity.class, null);
                break;
            default:
                break;
        }
    }

    /**
     * コンテンツ情報取得ハンドラ.
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            setRecyclerView((List) msg.obj, msg.what, mLinearLayout);
            showProgessBar(false);
        }
    };

    @Override
    public void tvScheduleListCallback(final List<ContentsData> channelList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (channelList != null && channelList.size() > 0) {
                    Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_CHANNEL, channelList);
                    mHandler.sendMessage(msg);
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_CHANNEL);
                }
            }
        });
    }

    @Override
    public void dailyRankListCallback(final List<ContentsData> dailyRankList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (dailyRankList != null && dailyRankList.size() > 0) {
                    Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_TODAY, dailyRankList);
                    mHandler.sendMessage(msg);
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_TODAY);
                }
            }
        });
    }

    @Override
    public void tvClipListCallback(final List<ContentsData> tvClipList) {
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        if (tvClipList != null && tvClipList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_TV_CLIP, tvClipList);
            mHandler.sendMessage(msg);
        } else {
            showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_TV_CLIP);
        }
    }

    @Override
    public void vodClipListCallback(final List<ContentsData> vodClipList) {
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        if (vodClipList != null && vodClipList.size() > 0) {
            Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_VOD_CLIP, vodClipList);
            mHandler.sendMessage(msg);
        } else {
            showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_VOD_CLIP);
        }
    }

    @Override
    public void videoRankCallback(final List<ContentsData> videoRankList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (videoRankList != null && videoRankList.size() > 0) {
                    Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_VIDEO, videoRankList);
                    mHandler.sendMessage(msg);
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_VIDEO);
                }
            }
        });
    }

    @Override
    public void watchingVideoCallback(final List<ContentsData> watchingVideoList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (watchingVideoList != null && watchingVideoList.size() > 0) {
                    Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_WATCHING_VIDEO, watchingVideoList);
                    mHandler.sendMessage(msg);
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_WATCHING_VIDEO);
                }
            }
        });
    }

    @Override
    public void recommendChannelCallback(final List<ContentsData> redChList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (redChList != null && redChList.size() > 0) {
                    Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_RECOMMEND_PROGRAM, redChList);
                    mHandler.sendMessage(msg);
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_RECOMMEND_PROGRAM);
                }
            }
        });
    }

    @Override
    public void recommendVideoCallback(final List<ContentsData> redVdList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (redVdList != null && redVdList.size() > 0) {
                    Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_RECOMMEND_VOD, redVdList);
                    mHandler.sendMessage(msg);
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_RECOMMEND_VOD);
                }
            }
        });
    }

    /**
     * ユーザ情報取得処理.
     */
    private void getUserInfo() {
        //dアカウント取得後のコールバックからも呼ばれるようになったので、UIスレッド対応
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //ユーザー情報の変更検知
                mUserInfoDataProvider.getUserInfo();
                //ユーザー情報取得開始を行ったので、フラグはクリア
                mUserInfoGetRequest = false;
            }
        });
    }

    /**
     * 契約情報取得失敗ダイアログ.
     */
    private void getUserInfoErrorDialog() {
        if (!mIsCloseDialog) {
            mIsCloseDialog = true;
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
                    mIsCloseDialog = false;
                    getUserInfo();
                }
            });

            failedRecordingReservationDialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
                @Override
                public void onCancelCallback() {
                    //ユーザ情報なし(未契約表示)
                    mIsCloseDialog = false;
                    requestHomeData();
                }
            });

            failedRecordingReservationDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
                @Override
                public void allDismissCallback() {
                    //NOP
                }

                @Override
                public void otherDismissCallback() {
                    //ボタンタップ以外でダイアログが閉じた場合は閉じる選択と想定
                    //ユーザ情報なし(未契約表示)
                    mIsCloseDialog = false;
                    requestHomeData();
                }
            });
        }
    }

    /**
     * ユーザ情報取得処理.
     *
     * @param tag  api区別
     */
    private void showErrorDialogByErrorStatus(final int tag) {
        ErrorState errorState = mHomeDataProvider.getError(tag);
        if (errorState != null && errorState.getErrorType() != DtvtConstants.ErrorType.SUCCESS) {
            String message = errorState.getApiErrorMessage(this);
            if (!TextUtils.isEmpty(message)) {
                showGetDataFailedDialog(message);
            }
        }
    }

    /**
     * データ取得失敗ダイアログ.
     * @param message  エラーメッセージ
     */
    private void showGetDataFailedDialog(final String message) {
        //一度表示されたら表示しない
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorDialog(message, R.string.custom_dialog_ok);
            }
        });
    }

    /**
     * network状態確認.
     *
     * @return ネットワーク状態フラグ
     */
    private boolean networkCheck() {
        if (!NetWorkUtils.isOnline(this)) {
            String message = getResources().getString(R.string.network_nw_error_message);
            errorDialog(message, R.string.custom_dialog_ok);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void userInfoListCallback(final boolean isDataChange, final List<UserInfoList> userList) {
        if (!DataBaseUtils.isCachingRecord(this, DataBaseConstants.USER_INFO_LIST_TABLE_NAME)) {
            //UserInfoテーブルにデータがないため初回取得と判定
            if (userList == null || userList.size() < 1) {
                // 初回起動時または1度もH4d契約情報取得に成功していない状態で、
                // H4d契約情報取得に失敗した場合は「ひかりTV for docomoの契約情報取得に失敗しました。」
                // エラーダイアログ、「閉じる」「リトライ」ボタンを表示すること。
                //契約情報取得失敗
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
    public void onRentalChListJsonParsed(final PurchasedChannelListResponse RentalChListResponse) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void rentalListCallback(final List<ContentsData> rentalList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (rentalList != null) {
                    if (rentalList.size() > 0) {
                        Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_RENTAL, rentalList);
                        mHandler.sendMessage(msg);
                    }
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_RENTAL);
                }
            }
        });
    }

    @Override
    public void premiumListCallback(final List<ContentsData> premiumVideoList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //callbackが帰ってきたらProgressDialogを消す
                showProgessBar(false);
                if (premiumVideoList != null) {
                    if (premiumVideoList.size() > 0) {
                        Message msg = Message.obtain(mHandler, HOME_CONTENTS_SORT_PREMIUM, premiumVideoList);
                        mHandler.sendMessage(msg);
                    }
                } else {
                    showErrorDialogByErrorStatus(HOME_CONTENTS_SORT_PREMIUM);
                }
            }
        });
    }

    @Override
    public void onItemClickCallBack(final ContentsData contentsData, final OtherContentsDetailData detailData) {
        if (ContentUtils.isChildContentList(contentsData)) {
            startChildContentListActivity(contentsData);
        } else {
            Intent intent = new Intent(this, ContentDetailActivity.class);
            ComponentName componentName = this.getComponentName();
            intent.putExtra(DtvtConstants.SOURCE_SCREEN, componentName.getClassName());
            intent.putExtra(detailData.getRecommendFlg(), detailData);
            startActivity(intent);
        }
    }

    @Override
    public void rentalListNgCallback() {
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void watchListenVideoListCallback(final List<ContentsData> clipContentInfo) {
        showPartDataGetFailedDialog(clipContentInfo);
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void genreListCallback(@Nullable final List<GenreCountGetMetaData> listData) {
        //callbackが帰ってきたらProgressDialogを消す
        showPartDataGetFailedDialog(listData);
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void genreListMapCallback(final Map<String, VideoGenreList> map, final List<String> firstGenreIdList) {
        showPartDataGetFailedDialog(firstGenreIdList);
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onContentsDetailInfoCallback(final VodMetaFullData contentsDetailInfo, final boolean clipStatus) {
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onRoleListCallback(final ArrayList<RoleListMetaData> roleListInfo) {
        showPartDataGetFailedDialog(roleListInfo);
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void recordingReservationResult(final RemoteRecordingReservationResultResponse response) {
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onRentalVodListCallback(final PurchasedVodListResponse response) {
        showPartDataGetFailedDialog(response);
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public void onRentalChListCallback(final PurchasedChannelListResponse response) {
        showPartDataGetFailedDialog(response);
        //callbackが帰ってきたらProgressDialogを消す
        showProgessBar(false);
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }
    /**
     * 一部データ取得失敗時に表示するエラーダイアログ.
     * @param object データ情報.
     */
    private void showPartDataGetFailedDialog(final Object object) {
        if (!mIsCloseDialog && object == null && !mPartDataGetFailed) {
            mPartDataGetFailed = true;
            errorDialog(getString(R.string.get_contents_data_error_message), R.string.common_text_retry);
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DlnaManager.shared().StopDmp();
            finish();
        }
        return false;
    }
}