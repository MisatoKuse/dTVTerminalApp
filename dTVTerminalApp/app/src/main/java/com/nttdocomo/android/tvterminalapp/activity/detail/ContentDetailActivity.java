/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.detail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchStbActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ClipKeyListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.ContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.StbMetaInfoGetDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentDetailDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRecommendDetailDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopSendOperateLog;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopThumbnailConnect;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsChannelFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaContentMultiChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentDetailUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.view.PlayerViewLayout;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SendOperateLog;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.StbMetaInfoResponseData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * コンテンツ詳細画面 Activity.
 * 視聴・録画再生も含めて全てのコンテンツはこのActivityで表示を行う.
 * クラス名のRename禁止(dCHアプリを起動するコンポーネントは、dCHアプリ側でホワイトリスト化するとのこと)
 */
public class ContentDetailActivity extends BaseActivity implements View.OnClickListener,
        TabItemLayout.OnClickTabTextListener,
        PlayerViewLayout.PlayerStateListener,
        ContentsDetailDataProvider.ApiDataProviderCallback,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        RemoteControllerView.OnStartRemoteControllerUIListener,
        DtvContentsDetailFragment.RecordingReservationIconListener,
        DtvContentsChannelFragment.ChangedScrollLoadListener,
        StbMetaInfoGetDataProvider.StbMetaInfoGetDataProviderListener,
        DtvContentsDetailFragment.ContentsDetailFragmentListener {
    // region variable
    /** コンテンツ詳細 start HorizontalScrollView.*/
    private TabItemLayout mTabLayout = null;
    /** ViewPager.*/
    private ViewPager mViewPager = null;
    /** コンテンツ詳細データ.*/
    private OtherContentsDetailData mDetailData = null;
    /** フルメタデータ.*/
    private VodMetaFullData mDetailFullData = null;
    /** コンテンツ詳細データプロバイダー.*/
    private ContentsDetailDataProvider mContentsDetailDataProvider = null;
    /** 縮小番組表データプロバイダー .*/
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    /** STBメタデータ取得データプロバイダー .*/
    private StbMetaInfoGetDataProvider mStbMetaInfoGetDataProvider = null;
    /** サムネイルプロバイダー .*/
    private ThumbnailProvider mThumbnailProvider = null;
    /** サムネイル取得処理ストップフラグ .*/
    private boolean mIsDownloadStop = false;
    /** 初回表示フラグ .*/
    private boolean mIsFirstDisplay = true;
    /** コンテンツ詳細フラグメントファクトリー.*/
    private DtvContentsDetailFragmentFactory mFragmentFactory = null;
    /** コンテンツ詳細フラグメント.*/
    private DtvContentsDetailFragment mContentsDetailFragment = null;
    /**購入済みVODレスポンス.*/
    private PurchasedVodListResponse mPurchasedVodListResponse = null;
    /** タブー名.*/
    private String[] mTabNames = null;
    /**表示状態.*/
    private int mDisplayState = 0;
    /**リモコンコントローラービジブルか.*/
    private boolean mIsControllerVisible = false;
    /**インデント.*/
    private Intent mIntent = null;
    /**サムネイルレイアウト.*/
    private LinearLayout mThumbnailBtn = null;
    /**サムネイルRelativeLayout.*/
    private RelativeLayout mThumbnailRelativeLayout = null;
    /**サムネイルイメージビュー.*/
    private ImageView mThumbnail = null;
    /**再生アイコン.*/
    private ImageView mPlayIcon;
    /**再生前くるくる処理.*/
    private ProgressBar mProgressBar;
    /** ひかりTVリトライ用のDLNAオブジェクト. */
    private DlnaObject mDlnaObject = null;
    /** ひかりTVリトライ時にリモート扱いするかどうかのフラグ. */
    private boolean mNotRemoteRetry = false;
    /**サムネイルアイコン、メッセージレイアウト.*/
    private LinearLayout mContractLeadingView = null;
    /**「契約する」ボタンのクリック状態.*/
    private boolean mThumbnailContractButtonClicked = false;
    /** 日付インディーズ.*/
    private int mDateIndex = 0;
    /** 日付リスト.*/
    private String[] mDateList = null;
    /** flg(0).*/
    private static final int FLAG_ZERO = 0;
    /* player start */
    /**FrameLayout.*/
    private FrameLayout mFrameLayout = null;
    /**録画予約コンテンツ詳細情報.*/
    private RecordingReservationContentsDetailInfo mRecordingReservationContentsDetailInfo = null;
    /**録画予約ダイアログ.*/
    private CustomDialog mRecordingReservationCustomtDialog = null;
    /** 他サービスフラグ.*/
    private boolean mIsOtherService = false;
    /** 対象コンテンツのチャンネルデータ.*/
    private ChannelInfo mChannel = null;
    /** 視聴可能期限.*/
    private long mEndDate = 0L;
    /** Vod視聴可能期限.*/
    private long mVodEndDate = 0L;
    /** Vod視聴可能期限文字列.*/
    private String mVodEndDateText = null;
    /** 操作履歴送信.*/
    private SendOperateLog mSendOperateLog = null;
    /** 二回目リモコン送信防止.*/
    private boolean mIsSend = false;
    /** ヘッダーチェック.*/
    private boolean mIsFromHeader = false;
    /** チャンネル日付.*/
    private String mChannelDate = null;
    /** サービスIDユニーク.*/
    private String mServiceIdUniq = null;
    /** 視聴可否ステータス.*/
    private ContentUtils.ViewIngType mViewIngType = null;
    /** コンテンツ種別.*/
    private ContentUtils.ContentsType mContentsType = null;
    /** プレイヤーレイアウト.*/
    private PlayerViewLayout mPlayerViewLayout;
    /** プレイヤー前回のポジション.*/
    private int mPlayStartPosition;
    /** 再生停止フラグ.*/
    private boolean mIsPlayerPaused = false;
    /** 前回リモートコントローラービュー表示フラグ.*/
    private boolean mVisibility = false;
    /** ディスプレイ幅.*/
    private int mWidth;
    /** ディスプレイ高さ.*/
    private int mHeight;
    /** ナビゲーションバー含むディスプレイ高さ.*/
    private int mScreenNavHeight;
    /* player end */
    /** チャンネルリストフラグメント.*/
    private DtvContentsChannelFragment mChannelFragment = null;
    /** 再生用データ.*/
    private RecordedContentsDetailData mPlayerData;
    /** 前回ViewPagerのタブ位置.*/
    private int mViewPagerIndex = DEFAULT_TAB_INDEX;
    /** 前回ViewPagerのタブ位置.*/
    private static final int DEFAULT_TAB_INDEX = -1;
    /** コンテンツ種別1のコンテンツ種別名のひかりTVタイプ.*/
    private ContentUtils.HikariType mHikariType = null;
    /** 放送視聴可否.*/
    private boolean mIsH4dPlayer = false;
    /** コンテンツタイプ(Google Analytics用).*/
    private ContentDetailUtils.ContentTypeForGoogleAnalytics contentType = null;
    /** タブ表示区別.*/
    private ContentDetailUtils.TabType tabType;
    /** pure dchチャンネル名取得.*/
    private boolean mIsOtherServiceDtvChLoading = false;
    /** スクリーン名送信済区別.*/
    private boolean mIsScreenViewSent = false;
    // endregion

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPlayStartPosition = savedInstanceState.getInt(ContentDetailUtils.PLAY_START_POSITION);
            mVisibility = savedInstanceState.getBoolean(ContentDetailUtils.REMOTE_CONTROLLER_VIEW_VISIBILITY);
            mViewPagerIndex = savedInstanceState.getInt(ContentDetailUtils.VIEWPAGER_INDEX);
            mIsPlayerPaused = savedInstanceState.getBoolean(ContentDetailUtils.IS_PLAYER_PAUSED);
            savedInstanceState.clear();
        }
        setTheme(R.style.AppThemeBlack);
        setContentView(R.layout.dtv_contents_detail_main_layout);
        DTVTLogger.start();
        setStatusBarColor(R.color.contents_header_background);
        showProgressBar(true);
        // プログレスバー表示中でもxボタンクリック可能にする。※プログレスバー表示後に禁止に設定する
        findViewById(R.id.base_progress_rl).setClickable(false);
        initView();
    }

    @SuppressWarnings("OverlyLongMethod")
    @Override
    protected void onResume() {
        DTVTLogger.start();
        super.onResume();
        enableStbStatusIcon(true);
        switch (mDisplayState) {
            case ContentDetailUtils.PLAYER_ONLY:
                if (mIsFromBgFlg) {
                    super.sendScreenView(getString(R.string.google_analytics_screen_name_player_recording),
                            ContentUtils.getParingAndLoginCustomDimensions(ContentDetailActivity.this));
                }
                break;
            case ContentDetailUtils.PLAYER_AND_CONTENTS_DETAIL:
                if (mIsFromBgFlg) {
                    String screenName = null;
                    if (mViewPager != null) {
                        String tabName = mTabNames[mViewPager.getCurrentItem()];
                        screenName = ContentDetailUtils.getScreenNameMap(contentType, ContentDetailActivity.this, mIsH4dPlayer).get(tabName);
                    }
                    if (screenName != null) {
                        super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(ContentDetailActivity.this));
                    }
                }
                checkOnResumeClipStatus();
                break;
            case ContentDetailUtils.CONTENTS_DETAIL_ONLY:
                if (mIsFromBgFlg && contentType != null && mViewPager != null) {
                    String screenName;
                    if (mIsOtherService) {
                        screenName = ContentUtils.getOtherServiceScreenName(ContentDetailActivity.this, mDetailData.getServiceId());
                    } else {
                        String tabName = mTabNames[mViewPager.getCurrentItem()];
                        screenName = ContentDetailUtils.getScreenNameMap(contentType, ContentDetailActivity.this, mIsH4dPlayer).get(tabName);
                    }
                    if (screenName != null) {
                        super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(ContentDetailActivity.this));
                    }
                }
                checkOnResumeClipStatus();
                break;
            default:
                break;
        }
        if (contentType != null && !mIsFromBgFlg) {
            if (mViewPager != null) {
                sendScreenViewForPosition(mViewPager.getCurrentItem());
            } else {
                sendScreenViewForPosition(ContentDetailUtils.CONTENTS_DETAIL_INFO_TAB_POSITION);
            }
        }
        if (mIsPlayerPaused) {
            initPlayerStart(true);
            mIsPlayerPaused = false;
        }
        DTVTLogger.end();
    }

    /**
     * プレイヤー再生処理.
     * @param isFromBack bg→fg
     */
    private void initPlayerStart(final boolean isFromBack) {
        if (mPlayerViewLayout != null) {
            if (isTvPlaying()) {
                setRemotePlayArrow(mPlayerData);
            } else if (isFromBack) {
                if (mPlayerData == null || mPlayerData.isRemote()) {
                    setRemotePlayArrow(mPlayerData);
                } else {
                    if (mPlayerData.isIsLive()) {
                        mPlayerViewLayout.setVisibility(View.VISIBLE);
                        if (mPlayerViewLayout.initSecurePlayer(mPlayStartPosition)) {
                            showPlayerView();
                        } else {
                            mPlayerViewLayout.setVisibility(View.GONE);
                        }
                    } else {
                        setRemotePlayArrow(mPlayerData);
                    }
                }
            } else {
                if (mPlayerViewLayout.initSecurePlayer(mPlayStartPosition)) {
                    showPlayerView();
                } else {
                    mPlayerViewLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * プレイヤービュー表示.
     */
    private void showPlayerView() {
        showPlayIcon(false);
        mContractLeadingView.setVisibility(View.GONE);
        mThumbnail.setVisibility(View.GONE);
        mPlayerViewLayout.setPlayerEvent();
        mPlayerViewLayout.setUserAgeInfo();
        mThumbnailBtn.setVisibility(View.GONE);
    }

    /**
     * onResume時のクリップ状態チェック.
     */
    private void checkOnResumeClipStatus() {
        if (!mIsFirstDisplay) {
            checkClipStatus(ContentDetailUtils.CLIP_BUTTON_ALL_UPDATE);
        } else {
            mIsFirstDisplay = false;
        }
    }

    /**
     * 通信処理停止処理.
     */
    private void stopConnect() {
        //通信を止める
        if (mContentsDetailDataProvider != null) {
            new StopContentDetailDataConnect().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mContentsDetailDataProvider);
        }
        if (mScaledDownProgramListDataProvider != null) {
            new StopScaledProListDataConnect().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mScaledDownProgramListDataProvider);
        }
        if (mStbMetaInfoGetDataProvider != null) {
            new StopRecommendDetailDataConnect().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mStbMetaInfoGetDataProvider);
        }
        if (mSendOperateLog != null) {
            new StopSendOperateLog().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSendOperateLog);
        }
        stopThumbnailConnect();
        DtvContentsChannelFragment channelFragment = getChannelFragment();
        if (channelFragment != null) {
            channelFragment.stopContentsAdapterCommunication();
        }
    }

    /**
     * 通信処理復帰処理.
     */
    private void enableConnect() {
        if (mPlayerViewLayout != null) {
            mPlayerViewLayout.enableThumbnailConnect();
        }
        if (mContentsDetailDataProvider != null) {
            mContentsDetailDataProvider.enableConnect();
        }
        if (mScaledDownProgramListDataProvider != null) {
            mScaledDownProgramListDataProvider.enableConnect();
        }
        if (mStbMetaInfoGetDataProvider != null) {
            mStbMetaInfoGetDataProvider.enableConnect();
        }
        if (mSendOperateLog != null) {
            mSendOperateLog.enableConnection();
        }
        enableThumbnailConnect();
        //FragmentにContentsAdapterの通信を復帰するように通知する
        DtvContentsChannelFragment channelFragment = getChannelFragment();
        if (channelFragment != null) {
            channelFragment.enableContentsAdapterCommunication();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPlayerViewLayout != null) {
            if (mPlayerViewLayout.getCurrentPosition() < 0) {
                mPlayerViewLayout.setPlayStartPosition(0);
            }
            outState.putInt(ContentDetailUtils.PLAY_START_POSITION, mPlayerViewLayout.getCurrentPosition());
        }
        outState.putBoolean(ContentDetailUtils.REMOTE_CONTROLLER_VIEW_VISIBILITY, mIsControllerVisible);
        if (mViewPager != null) {
            outState.putInt(ContentDetailUtils.VIEWPAGER_INDEX, mViewPager.getCurrentItem());
        }
        outState.putBoolean(ContentDetailUtils.IS_PLAYER_PAUSED, mIsPlayerPaused);
    }

    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayerViewLayout != null) {
            mPlayStartPosition = mPlayerViewLayout.onPause();
            mIsPlayerPaused = true;
        }
        switch (mDisplayState) {
            case ContentDetailUtils.PLAYER_AND_CONTENTS_DETAIL:
            case ContentDetailUtils.CONTENTS_DETAIL_ONLY:
                stopConnect();
                break;
            case ContentDetailUtils.PLAYER_ONLY:
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayerViewLayout != null) {
            mPlayerViewLayout.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        if (mPlayerViewLayout != null) {
            mPlayerViewLayout.onDestory();
        }
        super.onDestroy();
    }

    @Override
    public void onStartCommunication() {
        DTVTLogger.start();
        super.onStartCommunication();
        switch (mDisplayState) {
            case ContentDetailUtils.PLAYER_ONLY:
                if (mPlayerViewLayout != null) {
                    mPlayerViewLayout.enableThumbnailConnect();
                }
                break;
            case ContentDetailUtils.PLAYER_AND_CONTENTS_DETAIL:
            case ContentDetailUtils.CONTENTS_DETAIL_ONLY:
                enableConnect();
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * プレイヤー初期化.
     * @param playerData 再生データ
     */
    private void initPlayer(final RecordedContentsDetailData playerData) {
        boolean isFirstTouch = false;
        if (mPlayerViewLayout == null) {
            isFirstTouch = true;
        }
        mPlayerViewLayout = findViewById(R.id.dtv_contents_detail_main_layout_player_rl);
        mPlayerViewLayout.setVisibility(View.VISIBLE);
        mPlayerViewLayout.setPlayerStateListener(this);
        mPlayerViewLayout.setScreenSize(mWidth, mHeight);
        mPlayerViewLayout.setScreenNavigationBarSize(mWidth, mScreenNavHeight);
        mPlayerViewLayout.setParentLayout(mThumbnailRelativeLayout);
        mPlayerViewLayout.setDensity(getDensity());
        mPlayerViewLayout.setPlayerEventType(PlayerViewLayout.PlayerEventType.PLAY_PAUSE_TAP);
        boolean mIsOncreateOk = mPlayerViewLayout.initMediaInfo(playerData);
        mPlayerData = playerData;
        //外部出力および画面キャプチャ制御
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        mPlayerViewLayout.createExternalDisplayHelper();
        if (mIsOncreateOk) {
            initPlayerStart(false);
        }
        if (isFirstTouch) {
            String category = getString(R.string.google_analytics_category_service_name_h4d);
            String action;
            if (playerData.isIsLive()) {
                action = getString(R.string.google_analytics_category_action_contents_play);
            } else {
                action = getString(R.string.google_analytics_category_action_record_contents_play);
            }
            String label = getTitleText().toString();
            sendEvent(category, action, label, null);
        }
    }

    /**
     * プレイヤーのみビューの設定.
     * @param playerData 再生データ
     */
    private void showPlayerOnlyView(final RecordedContentsDetailData playerData) {
        findViewById(R.id.dtv_contents_detail_player_only).setVisibility(View.VISIBLE);
        TextView mPlayerOnlyTitle = findViewById(R.id.dtv_contents_detail_player_only_title);
        TextView mPlayerOnlyChannelName = findViewById(R.id.dtv_contents_detail_player_only_channel_name);
        TextView mPlayerOnlyDate = findViewById(R.id.dtv_contents_detail_player_only_date);
        mPlayerOnlyTitle.setText(playerData.getTitle());
        mPlayerOnlyChannelName.setText(playerData.getRecordedChannelName());
        mPlayerOnlyDate.setText(DateUtils.getDownloadDateFormat(playerData.getDate(), this));
    }

    /**
     * ビュー初期化.
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void initView() {
        mIntent = getIntent();
        setScreenSize();
        mThumbnailBtn = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail_btn);
        mThumbnailBtn.setOnClickListener(this);
        mThumbnail = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getWidthDensity(), getWidthDensity() / ContentDetailUtils.SCREEN_RATIO_WIDTH_16 * ContentDetailUtils.SCREEN_RATIO_HEIGHT_9);
        mThumbnail.setLayoutParams(layoutParams);
        mThumbnailRelativeLayout = findViewById(R.id.dtv_contents_detail_layout);
        mContractLeadingView = findViewById(R.id.contract_leading_view);
        Object object = mIntent.getParcelableExtra(ContentDetailUtils.RECORD_LIST_KEY);
        if (object instanceof RecordedContentsDetailData) { //プレイヤーで再生できるコンテンツ
            mDisplayState = ContentDetailUtils.PLAYER_ONLY;
            RecordedContentsDetailData playerData = mIntent.getParcelableExtra(ContentDetailUtils.RECORD_LIST_KEY);
            if (!TextUtils.isEmpty(playerData.getTitle())) {
                setTitleText(playerData.getTitle());
            }
            if (ContentsAdapter.DOWNLOAD_STATUS_COMPLETED == playerData.getDownLoadStatus() || !playerData.isRemote()) {
                initPlayer(playerData);
            } else {
                setRemotePlayArrow(playerData);
            }
            if (!playerData.isIsLive()) {
                showPlayerOnlyView(playerData);
            }
            super.sendScreenView(getString(R.string.google_analytics_screen_name_player_recording), //録画再生カスタムディメンション送信
                    ContentDetailUtils.getRecordPlayerCustomDimensions(ContentDetailActivity.this, playerData.getTitle()));
        }
        //ヘッダーの設定
        String sourceClass = mIntent.getStringExtra(DtvtConstants.SOURCE_SCREEN);
        if (sourceClass != null && !sourceClass.isEmpty()) {
            //赤ヘッダーである遷移元クラス名を保持
            setSourceScreenClass(sourceClass);
            enableHeaderBackIcon(false);
        } else {
            //詳細画面から詳細画面への遷移時は戻るアイコンを表示
            enableHeaderBackIcon(true);
        }
        setHeaderColor(false);
        enableGlobalMenuIcon(true);
        changeGlobalMenuIcon(false);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        initContentsView();
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setTabVisibility(false);
        }
    }

    /**
     * コンテンツ詳細データ取得.
     */
    private void getContentDetailDataFromPlala() {
        mContentsDetailDataProvider = new ContentsDetailDataProvider(this);
        if (mDetailData != null) {
            DTVTLogger.debug("contentId:" + mDetailData.getContentsId());
            String[] cRid = new String[1];
            cRid[cRid.length - 1] = mDetailData.getContentsId();
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(this);
            int ageReq = userInfoDataProvider.getUserAge();
            String areaCode = UserInfoUtils.getAreaCode(this);
            mContentsDetailDataProvider.getContentsDetailData(cRid, "", ageReq, areaCode);
        } else {
            DTVTLogger.debug("contentId取得失敗しました。");
        }
    }

    /**
     * チャンネルフラグメント取得.
     * @return currentFragment
     */
    private DtvContentsChannelFragment getChannelFragment() {
        Fragment currentFragment = getFragmentFactory().createFragment(1);
        return (DtvContentsChannelFragment) currentFragment;
    }

    /**
     * フラグメントファクトリー取得.
     * @return DtvContentsDetailFragmentFactory
     */
    private DtvContentsDetailFragmentFactory getFragmentFactory() {
        if (mFragmentFactory == null) {
            mFragmentFactory = new DtvContentsDetailFragmentFactory();
        }
        return mFragmentFactory;
    }

    /**
     * コンテンツ詳細データ取得.
     * @param channelInfo チャンネル情報
     */
    private void getChannelDetailData(final ChannelInfo channelInfo) {
        DTVTLogger.start();
        DtvContentsChannelFragment channelFragment = getChannelFragment();
        channelFragment.setLoadInit();
        if (channelInfo != null) {
            channelFragment.setChannelDataChanged(channelInfo);
            getChannelDetailByPageNo();
        }
        DTVTLogger.end();
    }

    /**
     * ページングでチャンネルデータ取得.
     */
    private void getChannelDetailByPageNo() {
        DTVTLogger.start();
        if (mScaledDownProgramListDataProvider == null) {
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        }
        String[] serviceIdUniqs = new String[]{mChannel.getServiceIdUniq()};
        mDateList = null;
        if (mDateIndex <= 6) { //一週間以内
            mDateList = new String[1];
        }
        if (mDateList != null) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD, Locale.JAPAN);
            try {
                String today = sdf.format(calendar.getTime()) + DateUtils.DATE_STANDARD_START;
                sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
                Date date = sdf.parse(today);
                //AM4:00以前の場合 日付-1
                boolean is4HourPre = false;
                if (calendar.getTime().getTime() < date.getTime()) {
                    is4HourPre = true;
                }
                if (is4HourPre) {
                    calendar.add(Calendar.DAY_OF_MONTH, mDateIndex - 1);
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, mDateIndex);
                }
                mChannelDate = sdf.format(calendar.getTime());
                sdf = new SimpleDateFormat(DateUtils.DATE_NOMARK_YYYYMMDD, Locale.JAPAN);
                mDateList[0] = sdf.format(calendar.getTime());
            } catch (ParseException e) {
                channelLoadCompleted();
                DTVTLogger.debug(e);
            }
            if (serviceIdUniqs[0] == null) {
                DTVTLogger.error("No channel number");
                channelLoadCompleted();
                return;
            }
            mDateIndex++;
            mScaledDownProgramListDataProvider.setAreaCode(UserInfoUtils.getAreaCode(this));
            mScaledDownProgramListDataProvider.getProgram(serviceIdUniqs, mDateList);
        } else {
            channelLoadCompleted();
        }
        DTVTLogger.end();
    }

    /**
     * チャンネルロード完了.
     */
    private void channelLoadCompleted() {
        DtvContentsChannelFragment channelFragment = getChannelFragment();
        channelFragment.loadComplete();
        showChannelProgressBar(false);
    }
    @Override
    public void onChannelLoadMore() {
        getChannelDetailByPageNo();
    }

    @Override
    public void onUserVisibleHint(final boolean isVisibleToUser, final DtvContentsChannelFragment fragment) {
        if (!isVisibleToUser || (fragment.getContentsData() != null && fragment.getContentsData().size() > 0)) {
            return;
        }
        fragment.initLoad();
        showChannelProgressBar(true);
        getChannelDetailData(mChannel);
    }

    /**
     * サムネイルエリア文字表示.
     * @param content 表示内容
     */
    @SuppressWarnings({"EnumSwitchStatementWhichMissesCases", "OverlyLongMethod"})
    private void setThumbnailText(final String content) {
        DTVTLogger.start();
        if (UserInfoUtils.isContract(this) || mIsOtherService) {
            TextView startAppIcon = findViewById(R.id.view_contents_button_text);
            startAppIcon.setVisibility(View.GONE);
            ImageView imageView = findViewById(R.id.dtv_contents_view_button);
            if (content.isEmpty()) {
                mThumbnailBtn.setVisibility(View.GONE);
                mContractLeadingView.setVisibility(View.GONE);
            } else {
                if (mDetailFullData != null && mDetailFullData.getContentsType().equals(ContentUtils.ContentsType.HIKARI_TV_VOD)) {
                    imageView.setVisibility(View.GONE);
                    // 連携アイコン非表示のためクリック抑止
                    mThumbnailBtn.setClickable(false);
                } else if (content.equals(getString(R.string.contents_detail_thumbnail_text_unable_viewing))
                        || content.equals(getString(R.string.contents_detail_bs_premium_ch))
                        || content.equals(getString(R.string.contents_detail_thumbnail_text))) {
                    imageView.setVisibility(View.GONE);
                    // 連携アイコン非表示のためクリック抑止
                    mThumbnailBtn.setClickable(false);
                }
                mThumbnailBtn.setVisibility(View.VISIBLE);
                setThumbnailShadow();
                startAppIcon.setVisibility(View.VISIBLE);
                startAppIcon.setText(content);
            }
        }
        DTVTLogger.end();
    }

    /**
     * 未ログイン状態のサムネイル描画.
     */
    private void loginNgDisplay() {
        TextView contractLeadingText = findViewById(R.id.contract_leading_text);
        Button contractLeadingButton = findViewById(R.id.contract_leading_button);
        setThumbnailShadow();
        DTVTLogger.debug("userState:---" + UserState.LOGIN_NG);
        String message = getString(R.string.contents_detail_login_message);
        String buttonText = getString(R.string.contents_detail_login_button);
        contractLeadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DaccountUtils.startDAccountApplication(ContentDetailActivity.this);
            }
        });
        mContractLeadingView.setVisibility(View.VISIBLE);
        contractLeadingText.setText(message);
        contractLeadingButton.setText(buttonText);
    }

    /**
     * データの初期化.
     * @param title タイトル
     * @param url URL
     */
    private void setTitleAndThumbnail(final String title, final String url) {
        DTVTLogger.start();
        if (!TextUtils.isEmpty(title)) {
            setTitleText(title);
        }
        if (!TextUtils.isEmpty(url)) {
            if (mThumbnailProvider == null) {
                mThumbnailProvider = new ThumbnailProvider(this, ThumbnailDownloadTask.ImageSizeType.CONTENT_DETAIL);
            }
            if (!mIsDownloadStop) {
                if (mDetailFullData != null && (TextUtils.isEmpty(mDetailFullData.getmTv_service())
                        || ContentUtils.isBsOrTtbProgramPlala(mDetailFullData.getmTv_service()))) {
                    mThumbnail.setTag(R.id.tag_key, url);
                } else {
                    mThumbnail.setTag(url);
                }
                Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mThumbnail, url);
                if (bitmap != null) {
                    //サムネイル取得失敗時は取得失敗画像をセットする
                    mThumbnail.setImageBitmap(bitmap);
                }
            }
        } else {
            mThumbnail.setImageResource(R.mipmap.error_movie);
        }
        DTVTLogger.end();
    }

    /**
     * データの初期化.
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod", "EnumSwitchStatementWhichMissesCases"})
    private void initContentData() {
        DTVTLogger.start();
        mFrameLayout = findViewById(R.id.header_watch_by_tv);
        String contentsId = mIntent.getStringExtra(ContentUtils.PLALA_INFO_BUNDLE_KEY);
        if (ContentDetailUtils.getStbStatus() || mVisibility) {
            createRemoteControllerView(true);
            findViewById(R.id.remote_control_view).setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(contentsId)) { //ぷらら
            if (mDetailData == null) {
                mDetailData = new OtherContentsDetailData();
            }
            mDetailData.setContentsId(contentsId);
        } else {
            mDetailData = mIntent.getParcelableExtra(ContentUtils.RECOMMEND_INFO_BUNDLE_KEY);
            if (mDetailData != null) {
                if (ContentUtils.isOtherService(mDetailData.getServiceId())) { //検レコ
                    mIsOtherService = true;
                }
            } else {
                showProgressBar(false);
            }
        }
        tabType = ContentDetailUtils.TabType.TV_CH;
        mContentsDetailFragment = getDetailFragment();
        mContentsDetailFragment.setContentsDetailFragmentScrollListener(this);
        createViewPagerAdapter();
        DTVTLogger.end();
    }

    /**
     * ビューページング作成.
     */
    private void createViewPagerAdapter() {
        switch (tabType) {
            case VOD:
                mTabNames = getResources().getStringArray(R.array.contents_detail_tab_other_service_vod);
                break;
            case TV_ONLY:
                mTabNames = getResources().getStringArray(R.array.contents_detail_tab_other_service_tv);
                break;
            case TV_CH:
                mTabNames = getResources().getStringArray(R.array.contents_detail_tabs_tv_ch);
                break;
            default:
                break;
        }
        initTab();
        mViewPager.setAdapter(new ContentsDetailPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                sendScreenViewForPosition(position);
            }
        });
    }

    /**
     * ナビゲーションバーが表示されているか.
     * @return true:表示されている false:表示されていない
     */
    private boolean isNavigationBarShow() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        Point realSize = new Point();
        display.getSize(size);
        display.getRealSize(realSize);
        return realSize.y != size.y;
    }

    /**
     * ナビゲーションバーの高さを取得.
     * @return 高さ.
     */
    private int getNavigationBarHeight() {
        if (!isNavigationBarShow()) {
            return 0;
        }
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * スクリーンのサイズを設定.
     */
    private void setScreenSize() {
        boolean isLandscape = getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initDisplayMetrics();
        mWidth = getWidthDensity();
        initDisplayMetrics();
        mHeight = getHeightDensity();
        mScreenNavHeight = getScreenHeight();
        if (isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * スクリーンのHeightを取得.
     * @return Height
     */
    private int getScreenHeight() {
        initDisplayMetrics();
        return getHeightDensity() + getNavigationBarHeight();
    }

    /**
     * 詳細Viewの初期化.
     */
    private void initContentsView() {
        switch (mDisplayState) {
            case ContentDetailUtils.PLAYER_AND_CONTENTS_DETAIL:
            case ContentDetailUtils.CONTENTS_DETAIL_ONLY:
                mViewPager = findViewById(R.id.dtv_contents_detail_main_layout_vp);
                initContentData();
                break;
            case ContentDetailUtils.PLAYER_ONLY:
            default:
                break;
        }
    }

    /**
     * tabのレイアウトを設定.
     */
    private void initTab() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.DTV_CONTENTS_DETAIL_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_dtv_contents_detail_tab);
            tabRelativeLayout.setVisibility(View.VISIBLE);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != mViewPager) {
            mViewPager.setCurrentItem(position);
            if (mChannelFragment != null && position == 1) {
                if (mDetailFullData != null && (TextUtils.isEmpty(mDetailFullData.getmTv_service())
                        || ContentUtils.isBsOrTtbProgramPlala(mDetailFullData.getmTv_service()))) {
                    if (mChannelFragment.getContentsData() != null && mChannelFragment.getContentsData().size() > 0) {
                        mChannelFragment.setNotifyDataChanged();
                    }
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     * @param position 表示中タブ
     */
    private void sendScreenViewForPosition(final int position) {
        if (contentType == null) {
            return;
        }
        String screenName;
        String serviceName;
        String contentsType1;
        String contentsType2 = null;
        if (mIsOtherService) {
            screenName = ContentUtils.getOtherServiceScreenName(ContentDetailActivity.this, mDetailData.getServiceId());
            serviceName = ContentUtils.getServiceName(ContentDetailActivity.this, mDetailData.getServiceId());
            contentsType1 = ContentUtils.getContentsType1(ContentDetailActivity.this, mDetailData.getServiceId());
            switch (contentType) {
                case TV:
                    contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_live);
                    break;
                case VOD:
                    contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_void);
                    break;
                default:
                    break;
            }
        } else {
            String tabName = mTabNames[position];
            screenName = ContentDetailUtils.getScreenNameMap(contentType, ContentDetailActivity.this, mIsH4dPlayer).get(tabName);
            serviceName = getString(R.string.google_analytics_custom_dimension_service_h4d);
            contentsType1 = ContentUtils.getContentsType1(ContentDetailActivity.this, mHikariType);
            switch (contentType) {
                case TV:
                    contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_live);
                    break;
                case VOD:
                    contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_void);
                    break;
                default:
                    break;
            }
        }
        if (screenName == null) {
            return;
        }
        SparseArray<String> customDimensions = null;
        if (!TextUtils.isEmpty(contentsType1) && !TextUtils.isEmpty(contentsType2)) {
            String loginStatus = null;
            if (!mIsOtherService && !mIsH4dPlayer) {
                UserState userState = UserInfoUtils.getUserState(ContentDetailActivity.this);
                if (UserState.LOGIN_NG.equals(userState)) {
                    loginStatus = getString(R.string.google_analytics_custom_dimension_login_ng);
                } else {
                    loginStatus = getString(R.string.google_analytics_custom_dimension_login_ok);
                }
            }
            String contentName = getTitleText().toString();
            if (TextUtils.isEmpty(contentName) && mDetailFullData != null) {
                contentName = mDetailFullData.getTitle();
            }
            customDimensions = ContentUtils.getCustomDimensions(loginStatus, serviceName, contentsType1, contentsType2, contentName);
        }
        super.sendScreenView(screenName, customDimensions);
    }

    /**
     * コンテンツ詳細用ページャアダプター.
     */
    private class ContentsDetailPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * コンストラクタ.
         * @param fm FragmentManager
         */
        ContentsDetailPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            Fragment fragment = getFragmentFactory().createFragment(position);
            //Fragmentへデータを渡す
            Bundle args = new Bundle();
            args.putParcelable(ContentUtils.RECOMMEND_INFO_BUNDLE_KEY, mDetailData);
            if (position == 1) {
                ((DtvContentsChannelFragment) fragment).setScrollCallBack(ContentDetailActivity.this);
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            return mTabNames[position];
        }
    }

    /**
     * 録画予約情報を設定.
     * @param detailFragment フラグメント
     */
    private void setRecordingData(final DtvContentsDetailFragment detailFragment) {
        // リモート録画予約情報を生成
        mRecordingReservationContentsDetailInfo = ContentDetailUtils.getRecordingReservationContentsDetailInfo(mDetailFullData);
        detailFragment.setRecordingReservationIconListener(this, ContentDetailActivity.this);
    }

    /**
     * チャンネル情報を取得.
     */
    private void getChannelInfo() {
        if (mScaledDownProgramListDataProvider == null) {
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
        }
        mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
    }

    /**
     * 他サビースあらすじ取得.
     */
    private void getContentDetailInfoFromSearchServer() {
        if (mStbMetaInfoGetDataProvider == null) {
            mStbMetaInfoGetDataProvider = new StbMetaInfoGetDataProvider();
        }
        if (mDetailData != null) {
            if (mDetailData.getContentsId() == null ||  mDetailData.getCategoryId() == null) {
                // ダイアログを表示
                showProgressBar(false);
                showDialogToClose(this, getString(R.string.common_failed_get_info));
                return;
            }
            mStbMetaInfoGetDataProvider.getStbMetaInfo(mDetailData.getContentsId(),
                    String.valueOf(mDetailData.getServiceId()), mDetailData.getCategoryId(), this);
        }
    }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod", "EnumSwitchStatementWhichMissesCases"})
    @Override
    public void onContentsDetailInfoCallback(final VodMetaFullData contentsDetailInfo, final boolean clipStatus) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    DTVTLogger.debug("onContentsDetailInfoCallback already finising");
                    return;
                }
                //DBに保存されているUserInfoから契約情報を確認する
                String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(ContentDetailActivity.this));
                DTVTLogger.debug("contractInfo: " + contractInfo);
                DtvContentsDetailFragment detailFragment = getDetailFragment();
                detailFragment.setRequestFinish(true);
                //詳細情報取得して、更新する
                if (contentsDetailInfo != null) {
                    mDetailFullData = contentsDetailInfo;
                    //メタデータ取得時にコンテンツ種別を取得する
                    mContentsType = ContentUtils.getHikariContentsType(mDetailFullData);
                    //DBに保存されているUserInfoから契約情報を確認する
                    contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.TV;
                    if (ContentUtils.TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
                        if (ContentUtils.TV_SERVICE_FLAG_HIKARI.equals(mDetailFullData.getmTv_service())
                                || ContentUtils.TV_SERVICE_FLAG_TTB.equals(mDetailFullData.getmTv_service())
                                || ContentUtils.TV_SERVICE_FLAG_BS.equals(mDetailFullData.getmTv_service())) {
                            //tv_serviceは1、3、4の場合
                            setRecordingData(detailFragment);
                        } else if (ContentUtils.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(mDetailFullData.getmTv_service())) {
                            //tv_serviceは2の場合(ひかり内dチャンネル)
                            if (mDetailFullData.getmContent_type() == null) {
                                //contents_type未設定なら番組扱い
                                setRecordingData(detailFragment);
                            } else if (ContentUtils.CONTENT_TYPE_FLAG_ONE.equals(mDetailFullData.getmContent_type())
                                    || ContentUtils.CONTENT_TYPE_FLAG_TWO.equals(mDetailFullData.getmContent_type())) {
                                //contents_typeは1又は2の場合(見逃し（切り出し）or見逃し（完パケ）)
                                if (DateUtils.isBefore(mDetailFullData.getmVod_start_date())) {
                                    // 見逃し配信前（=放送コンテンツ扱い)
                                    setRecordingData(detailFragment);
                                } else {
                                    // 見逃し配信後又はdチャンネル関連VOD
                                    contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.VOD;
                                }
                            } else if (ContentUtils.CONTENT_TYPE_FLAG_ZERO.equals(mDetailFullData.getmContent_type())) {
                                // contents_typeが0は番組扱い
                                setRecordingData(detailFragment);
                            } else if (ContentUtils.CONTENT_TYPE_FLAG_THREE.equals(mDetailFullData.getmContent_type())) {
                                // contents_typeが3は関連VOD
                                contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.VOD;
                            } else {
                                // contents_typeがその他(異常値)はVOD扱いしておく
                                DTVTLogger.debug("contents_type value Error!" + mDetailFullData.getmContent_type());
                                contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.VOD;
                            }
                        } else {
                            //tv_serviceがその他(異常値)はVOD扱いしておく
                            DTVTLogger.debug("tv_service value Error!" + mDetailFullData.getmTv_service());
                            setRecordingData(detailFragment);
                        }
                    } else {
                        // disp_typeがtv_program以外なら一律VOD
                        contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.VOD;
                    }
                    mHikariType = ContentUtils.getHikariType(mDetailFullData);
                    OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
                    ContentDetailUtils.setContentsDetailData(mDetailFullData, detailData, clipStatus);
                    if (ContentUtils.DTV_FLAG_ONE.equals(mDetailFullData.getDtv())) {
                        setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmDtv_thumb_640_360());
                    } else {
                        setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmThumb_640_360());
                    }
                    String date = "";
                    ContentUtils.ContentsType contentsType = ContentUtils.getContentsTypeByPlala(mDetailFullData.getDisp_type(),
                            mDetailFullData.getmTv_service(), mDetailFullData.getmContent_type(),
                            mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date(),
                            mDetailFullData.getmVod_start_date(), mDetailFullData.getmVod_end_date(),
                            mDetailFullData.getEstFlag(), mDetailFullData.getmChsvod());
                    if (contentsType == ContentUtils.ContentsType.TV) { //番組(m/d（曜日）h:ii - h:ii)
                        date = DateUtils.getContentsDateString(mDetailFullData.getPublish_start_date(), mDetailFullData.getPublish_end_date());
                        mVodEndDateText = date;
                        setContentsType(ContentUtils.ContentsType.TV);
                    } else {
                        if (contentsType == ContentUtils.ContentsType.DCHANNEL_VOD_OVER_31 || contentsType == ContentUtils.ContentsType.DCHANNEL_VOD_31) {
                            //見逃しは vod_start_date を使用する
                            detailData.setmStartDate(String.valueOf(mDetailFullData.getmVod_start_date()));
                        } else {
                            //VODは avail_start_date を使用する
                            detailData.setmStartDate(String.valueOf(mDetailFullData.getAvail_start_date()));
                        }
                        tabType = ContentDetailUtils.TabType.VOD;
                        createViewPagerAdapter();
                        if (DateUtils.isBefore(mDetailFullData.getAvail_start_date())) { //配信前 m/d（曜日）から
                            date = DateUtils.getContentsDateString(getApplicationContext(), mDetailFullData.getAvail_start_date(), true);
                        } else {
                            switch (contentsType) {
                                case VOD:
                                    //Vodの場合は視聴可否判定結果取得後に表示判定を行うため値を保存するのみとする
                                    //VOD(m/d（曜日）まで)
                                    mVodEndDate = mDetailFullData.getAvail_end_date();
                                    date = DateUtils.getContentsDetailVodDate(getApplicationContext(), mDetailFullData.getAvail_end_date());
                                    mVodEndDateText = date;
                                    break;
                                case DCHANNEL_VOD_OVER_31:
                                    //Vodの場合は視聴可否判定結果取得後に表示判定を行うため値を保存するのみとする
                                    //VOD(見逃し)
                                    mVodEndDate = mDetailFullData.getmVod_end_date();
                                    date = StringUtils.getConnectStrings(
                                            getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                                    mVodEndDateText = date;
                                    break;
                                case DCHANNEL_VOD_31:
                                    //Vodの場合は視聴可否判定結果取得後に表示判定を行うため値を保存するのみとする
                                    //VOD(見逃し | m/d（曜日）まで)
                                    mVodEndDate = mDetailFullData.getmVod_end_date();
                                    String limit = DateUtils.getContentsDetailVodDate(getApplicationContext(), mDetailFullData.getmVod_end_date());
                                    date = StringUtils.getConnectStrings(getString(
                                            R.string.contents_detail_hikari_d_channel_miss_viewing_separation), limit);
                                    mVodEndDateText = date;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    DTVTLogger.debug("display limit date:---" + date);
                    detailData.setChannelDate(date);
                    detailFragment.setOtherContentsDetailData(detailData);
                    String[] credit_array = mDetailFullData.getmCredit_array();
                    if (credit_array != null && credit_array.length > 0) {
                        mContentsDetailDataProvider.getRoleListData();
                    }
                    if (ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
                        if (ContentDetailUtils.getStbStatus() || mVisibility) {
                            showRemoteViewControl(R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari);
                        }
                    } else { //レコメンドサーバー以外のひかりTV
                        if (mDetailFullData != null) {
                            mViewIngType = ContentUtils.getViewingType(contractInfo, mDetailFullData, mChannel);
                            checkWatchContents(mViewIngType);
                        }
                        //コンテンツの視聴可否判定に基づいてUI操作を行う
                        if (mViewIngType != null) {
                            changeUIBasedContractInfo();
                        }
                        if (ContentDetailUtils.getStbStatus() || mVisibility) {
                            showRemoteViewControl(R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari);
                        }
                    }
                } else {
                    tabType = ContentDetailUtils.TabType.VOD;
                    createViewPagerAdapter();
                    showErrorDialog(ContentDetailUtils.ErrorType.contentDetailGet);
                    mThumbnail.setImageResource(R.mipmap.error_movie);
                }
                sendOperateLog();
                if (ContentDetailUtils.getStbStatus() || mVisibility) {
                    findViewById(R.id.remote_control_view).setVisibility(View.VISIBLE);
                }
                if (mDetailFullData != null && !TextUtils.isEmpty(mDetailFullData.getServiceIdUniq())) {
                    mServiceIdUniq = mDetailFullData.getServiceIdUniq();
                    //チャンネル情報取得(取得後に視聴可否判定)
                    getChannelInfo();
                } else {
                    if (tabType == ContentDetailUtils.TabType.TV_CH) {
                        tabType = ContentDetailUtils.TabType.VOD;
                        createViewPagerAdapter();
                    }
                    mViewIngType = ContentUtils.getViewingType(contractInfo, mDetailFullData, mChannel);
                    //コンテンツ種別ごとの視聴可否判定を実行
                    getViewingTypeRequest(mViewIngType);
                }
            }
        });
        DTVTLogger.end();
    }

    /**
     * コンテンツ種別ごとに視聴可否リクエストを投げる.
     * @param viewIngType 視聴可否種別種別
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void getViewingTypeRequest(final ContentUtils.ViewIngType viewIngType) {
        DTVTLogger.start();
        //未ペアリング時は視聴可否判定をしない
        if (!StbConnectionManager.shared().getConnectionStatus().equals(StbConnectionManager.ConnectionStatus.NONE_PAIRING)) {
            //DBに保存されているUserInfoから契約情報を確認する
            String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(ContentDetailActivity.this));
            DTVTLogger.debug("contractInfo: " + contractInfo);
            switch (viewIngType) {
                case PREMIUM_CHECK_START:
                    DTVTLogger.debug("premium channel check start");
                    mContentsDetailDataProvider.getChListData();
                    break;
                case SUBSCRIPTION_CHECK_START:
                    DTVTLogger.debug("subscription vod channel check start");
                    mContentsDetailDataProvider.getVodListData();
                    break;
                default:
                    responseResultCheck(viewIngType, mContentsType);
                    break;
            }
        } else {
            //未ペアリング時はログイン導線を出すため、すべて視聴可とする
            mViewIngType = ContentUtils.ViewIngType.ENABLE_WATCH;
            responseResultCheck(viewIngType, mContentsType);
        }
        DTVTLogger.debug("get viewing type = " + viewIngType);
        DTVTLogger.end();
    }

    @Override
    public void onRoleListCallback(final ArrayList<RoleListMetaData> roleListInfo) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //スタッフ情報取得して、更新する
                if (roleListInfo != null) {
                    DtvContentsDetailFragment detailFragment = getDetailFragment();
                    if (mDetailFullData != null) {
                        String[] credit_array = mDetailFullData.getmCredit_array();
                        List<String> staffList = ContentDetailUtils.getRoleList(credit_array, roleListInfo);
                        if (staffList.size() > 0) {
                            OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
                            if (detailData != null) {
                                detailData.setStaffList(staffList);
                                detailFragment.setOtherContentsDetailData(detailData);
                                detailFragment.refreshStaff();
                            }
                        }
                    }
                    String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(ContentDetailActivity.this));
                    if (mViewIngType == null) {
                        mViewIngType = ContentUtils.getViewingType(contractInfo, mDetailFullData, null);
                    }
                    responseResultCheck(mViewIngType, mContentsType);
                } else {
                    showErrorToast(ContentDetailUtils.ErrorType.roleListGet);
                }
            }
        });
        DTVTLogger.end();
    }

    /**
     * データ取得がすべて終了していたらIndicatorを非表示にする(対象はContentsDetailDataProviderのみ).
     * @param viewIngType 視聴可否判定結果
     * @param contentsType コンテンツ種別
     */
    private void responseResultCheck(final ContentUtils.ViewIngType viewIngType, final ContentUtils.ContentsType contentsType) {
        if (mContentsDetailDataProvider == null
                || (!mContentsDetailDataProvider.isIsInContentsDetailRequest()
                    && !mContentsDetailDataProvider.isIsInRentalChListRequest()
                    && !mContentsDetailDataProvider.isIsInRentalVodListRequest()
                    && !mContentsDetailDataProvider.isIsInRoleListRequest())) {
            //ログアウト状態ならそのまま表示する
            UserState userState = UserInfoUtils.getUserState(ContentDetailActivity.this);
            StbConnectionManager.ConnectionStatus connectionStatus = StbConnectionManager.shared().getConnectionStatus();
            String contractStatus = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(this));
            ContentUtils.ContentsDetailUserType detailUserType = ContentUtils.getContentDetailUserType(userState, connectionStatus, contractStatus);
            if (contentsType != null) {
                shapeViewType(detailUserType, contentsType, viewIngType);
            }
            displayThumbnail(contentsType, mViewIngType, detailUserType);
            getDetailFragment().changeVisibilityRecordingReservationIcon(viewIngType, contentsType);
            getDetailFragment().noticeRefresh();
            showProgressBar(false);
        }
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @SuppressWarnings("OverlyComplexMethod")
            @Override
            public void run() {
                if (mViewPagerIndex >= 0) {
                    mViewPager.setCurrentItem(mViewPagerIndex);
                    mViewPagerIndex = DEFAULT_TAB_INDEX;
                }
                if (channels == null || channels.isEmpty()) {
                    if (!mIsOtherServiceDtvChLoading) {
                        if (tabType == ContentDetailUtils.TabType.TV_CH) {
                            tabType = ContentDetailUtils.TabType.VOD;
                            createViewPagerAdapter();
                        }
                    } else {
                        showProgressBar(false);
                    }
                    showErrorToast(ContentDetailUtils.ErrorType.channelListGet);
                    return;
                }
                DtvContentsDetailFragment detailFragment = getDetailFragment();
                if (mIsOtherServiceDtvChLoading) {
                    OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
                    ChannelInfo mChannelInfo = ContentDetailUtils.setPureDchChannelName(channels, mDetailData.getChannelId());
                    if (mChannelInfo != null) {
                        mChannel = mChannelInfo;
                        detailData.setChannelName(mChannelInfo.getTitle());
                        mDetailData = detailData;
                    }
                    detailFragment.noticeRefresh();
                    showProgressBar(false);
                    return;
                }
                //DBに保存されているUserInfoから契約情報を確認する
                String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(ContentDetailActivity.this));
                DTVTLogger.debug("contractInfo: " + contractInfo);
                if (mContentsType != null) {
                    //チャンネル情報取得して、更新する
                    if (!TextUtils.isEmpty(mServiceIdUniq)) {
                        for (int i = 0; i < channels.size(); i++) {
                            ChannelInfo channel = channels.get(i);
                            if (mServiceIdUniq.equals(channel.getServiceIdUniq())) {
                                mChannel = channel;
                                //チャンネル情報取得完了前にタブ切替されていた場合はここでチャンネルタブ表示処理を開始する
                                if (mViewPager.getCurrentItem() == ContentDetailUtils.CONTENTS_DETAIL_CHANNEL_TAB_POSITION) {
                                    showChannelProgressBar(true);
                                    getChannelDetailData(mChannel);
                                }
                                String channelName = channel.getTitle();
                                OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
                                if (detailData != null) {
                                    detailData.setChannelName(channelName);
                                    detailFragment.setOtherContentsDetailData(detailData);
                                }
                                break;
                            }
                        }
                        detailFragment.refreshChannelInfo();
                    }
                }
                //TV番組の視聴可否判定に入らなかったものはここで視聴可否判定する
                if (mViewIngType == null || mViewIngType.equals(ContentUtils.ViewIngType.NONE_STATUS)) {
                    mViewIngType = ContentUtils.getViewingType(contractInfo, mDetailFullData, mChannel);
                }
                if (mChannel == null) {
                    if (tabType == ContentDetailUtils.TabType.TV_CH) {
                        tabType = ContentDetailUtils.TabType.VOD;
                        createViewPagerAdapter();
                    }
                }
                if (mDetailFullData != null) {
                    checkWatchContents(mViewIngType);
                }
                //コンテンツ種別ごとの視聴可否判定を実行
                getViewingTypeRequest(mViewIngType);
            }
        });
        DTVTLogger.end();
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final String[] serviceIdUniq) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
            @Override
            public void run() {
                if (channelsInfo != null && channelsInfo.getChannels() != null) {
                    List<ChannelInfo> channels = channelsInfo.getChannels();
                    ContentDetailUtils.sort(channels);
                    if (channels.size() > 0) {
                        if (mViewPager.getCurrentItem() == ContentDetailUtils.CONTENTS_DETAIL_CHANNEL_TAB_POSITION) {
                            mChannelFragment = getChannelFragment();
                            ChannelInfo channelInfo = channels.get(0);
                            ArrayList<ScheduleInfo> scheduleInfos = channelInfo.getSchedules();
                            if (mDateIndex == 1) {
                                mChannelFragment.clearContentsData();
                            }
                            boolean isFirst = false;
                            for (int i = 0; i < scheduleInfos.size(); i++) {
                                ScheduleInfo scheduleInfo = scheduleInfos.get(i);
                                String endTime = scheduleInfo.getEndTime();
                                String startTime = scheduleInfo.getStartTime();
                                if (!ContentUtils.checkScheduleDate(startTime, endTime)) {
                                    continue;
                                }
                                if (!ContentUtils.isLastDate(endTime)) {
                                    if (mDateList != null) {
                                        ContentsData contentsData = new ContentsData();
                                        if (!isFirst) {
                                            if (mDateIndex == 1) {
                                                if (ContentUtils.isNowOnAir(startTime, endTime)) {
                                                    contentsData.setChannelName(getString(R.string.home_label_now_on_air));
                                                }
                                            }
                                            contentsData.setSubTitle(ContentDetailUtils.getDateForChannel(mChannelDate, getApplicationContext()));
                                            isFirst = true;
                                        }
                                        ContentDetailUtils.setContentsData(contentsData, scheduleInfo);
                                        getChannelFragment().addContentsData(contentsData);
                                    }
                                }
                            }
                            if (mDateIndex == 1) {
                                getChannelDetailByPageNo();
                            } else {
                                checkClipStatus(ContentDetailUtils.CLIP_BUTTON_CHANNEL_UPDATE);
                            }
                        }
                    }
                    channelLoadCompleted();
                } else {
                    mChannelFragment = getChannelFragment();
                    mDateIndex--;
                    if (mChannelFragment.getContentsData() == null || mChannelFragment.getContentsData().size() == 0) {
                        if (!NetWorkUtils.isOnline(ContentDetailActivity.this)) {
                            mChannelFragment.loadFailed();
                            showGetDataFailedToast(getString(R.string.network_nw_error_message));
                        } else {
                            mChannelFragment.loadComplete();
                        }
                    } else {
                        mChannelFragment.loadComplete();
                    }
                }
                showProgressBar(false);
            }
        });
        DTVTLogger.end();
    }

    /**
     * 詳細tabを取得.
     * @return 現在表示しているfragment
     */
    private DtvContentsDetailFragment getDetailFragment() {
        if (mContentsDetailFragment == null) {
            Fragment currentFragment = getFragmentFactory().createFragment(0);
            mContentsDetailFragment = (DtvContentsDetailFragment) currentFragment;
        }
        return mContentsDetailFragment;
    }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onClick(final View v) {
        DTVTLogger.start();
        switch (v.getId()) {
            case R.id.dtv_contents_detail_main_layout_thumbnail_btn:
                if (mViewIngType != null && mViewIngType.equals(ContentUtils.ViewIngType.DISABLE_WATCH_AGREEMENT_DISPLAY)) {
                    //未契約時は契約導線を表示
                    leadingContract();
                    return;
                }
                if (mDetailData != null && mDetailData.getServiceId() == ContentUtils.DTV_CONTENTS_SERVICE_ID) {
                    //dTV起動
                    checkAppInfo(ContentDetailUtils.StartAppServiceType.DTV);
                } else if (mDetailData != null && mDetailData.getServiceId() == ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID) {
                    //dアニメ起動
                    checkAppInfo(ContentDetailUtils.StartAppServiceType.DANIME);
                } else if (mDetailData != null && mDetailData.getServiceId() == ContentUtils.DAZN_CONTENTS_SERVICE_ID) {
                    //DAZN起動
                    checkAppInfo(ContentDetailUtils.StartAppServiceType.DAZN);
                } else if (mDetailData != null && mDetailData.getServiceId() == ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                    //dチャンネル起動.
                    checkAppInfo(ContentDetailUtils.StartAppServiceType.DTV_CH);
                } else if (mDetailFullData != null) {
                    //ひかりTV内DTVの場合
                    if (ContentUtils.VIDEO_PROGRAM.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.VIDEO_SERIES.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.WIZARD.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.VIDEO_PACKAGE.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.SUBSCRIPTION_PACKAGE.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.SERIES_SVOD.equals(mDetailFullData.getDisp_type())) {
                        //dTV起動
                        checkAppInfo(ContentDetailUtils.StartAppServiceType.H4D_DTV);
                    } else if (ContentUtils.TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
                        checkAppInfo(ContentDetailUtils.StartAppServiceType.H4D_DTV_CH);
                    }
                }
                break;
            default:
                super.onClick(v);
        }
        DTVTLogger.end();
    }

    /**
     * 機能：APP起動インストールチェック.
     * @param serviceType サービスタイプ
     * @return true インストール済み
     */
    private boolean checkIsAppInstalled(final ContentDetailUtils.StartAppServiceType serviceType) {
        if (!ContentDetailUtils.isAppInstalled(getApplicationContext(), ContentDetailUtils.getStartAppPackageName(serviceType))) {
            showUninstallDialog(ContentDetailUtils.getStartAppUnInstallMessage(serviceType, getApplicationContext()),
                    ContentDetailUtils.getStartAppGoogleUrl(serviceType));
            return false;
        }
        return true;
    }

    /**
     * 機能：APP起動インストールチェック.
     * @param serviceType サービスタイプ
     */
    private void showAppConfirmDialog(final ContentDetailUtils.StartAppServiceType serviceType) {
        String errorMessage = ContentDetailUtils.getStartAppConfirmMessage(serviceType, ContentDetailActivity.this);
        final CustomDialog startAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
        startAppDialog.setContent(errorMessage);
        startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
               String message = ContentDetailUtils.getStartAppVersionMessage(serviceType, ContentDetailActivity.this);
               if (TextUtils.isEmpty(message)) {
                   String url = "";
                   switch (serviceType) {
                       case DTV:
                           url = ContentDetailUtils.startDtvApp(mDetailData);
                           break;
                       case DTV_CH:
                           url = ContentDetailUtils.startDtvChannelApp(mDetailData);
                           break;
                       case DAZN:
                           url = ContentDetailUtils.startDAZNApp(mDetailData);
                           break;
                       case DANIME:
                           url = ContentDetailUtils.startDAnimeApp(mDetailData);
                           break;
                       case H4D_DTV:
                           url = ContentDetailUtils.startDtvApp(mDetailFullData);
                           break;
                       case H4D_DTV_CH:
                           url = ContentDetailUtils.startDtvChannelApp(mDetailFullData);
                           break;
                       default:
                           break;
                   }
                   if (!startApp(url)) {
                       showUninstallDialog(ContentDetailUtils.getStartAppUnInstallMessage(serviceType, getApplicationContext()),
                               ContentDetailUtils.getStartAppGoogleUrl(serviceType));
                   }
               } else {
                   showErrorDialog(message);
               }
            }
        });
        startAppDialog.showDialog();
    }

    /**
     * 機能：APP起動チェック.
     * @param serviceType サービスタイプ
     */
    private void checkAppInfo(final ContentDetailUtils.StartAppServiceType serviceType) {
        if (!checkIsAppInstalled(serviceType)) { //インストールチェック
            return;
        }
        showAppConfirmDialog(serviceType);
    }

    /**
     * 機能：アプリ未インストール時の表示.
     * @param message 文言
     * @param googleUrl google playアプリ先
     */
    private void showUninstallDialog(final String message, final String googleUrl) {
        CustomDialog installAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
        installAppDialog.setContent(message);
        installAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                toGooglePlay(googleUrl);
            }
        });
        installAppDialog.showDialog();
    }

    /**
     * 機能：APP起動.
     * @param url URL
     * @return true:起動成功であること
     */
    private boolean startApp(final String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivityForResult(intent, ContentDetailUtils.START_APPLICATION_REQUEST_CODE);
        } catch (ActivityNotFoundException exception) {
            //Androidのバグと思われる原因により、インストール情報の取得ができない場合がある。その場合は、この例外が発生する
            DTVTLogger.debug(exception);
            return false;
        }
        return true;
    }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod", "EnumSwitchStatementWhichMissesCases"})
    @Override
    public void onStartRemoteControl(final boolean isFromHeader) {
        mIsFromHeader = isFromHeader;
        DTVTLogger.start();
        // サービスIDにより起動するアプリを変更する
        if (mDetailData != null && !mIsSend && !isFromHeader) {
            setRelayClientHandler();
            switch (mDetailData.getServiceId()) {
                case ContentUtils.DTV_CONTENTS_SERVICE_ID: // dTV
                    showStartStbProgress(View.VISIBLE);
                    requestStartApplication(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mDetailData.getContentsId());
                    break;
                case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                    showStartStbProgress(View.VISIBLE);
                    requestStartApplication(RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mDetailData.getContentsId());
                    break;
                case ContentUtils.DAZN_CONTENTS_SERVICE_ID: // DAZN
                    showStartStbProgress(View.VISIBLE);
                    requestStartApplication(RemoteControlRelayClient.STB_APPLICATION_TYPES.DAZN, mDetailData.getContentsId());
                    break;
                case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
                    showStartStbProgress(View.VISIBLE);
                    //番組の場合
                    if (ContentDetailUtils.DTV_CHANNEL_CATEGORY_BROADCAST.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_BROADCAST,
                                mDetailData.getContentsId(), mDetailData.getChannelId());
                        //VOD(見逃し)の場合
                    } else if (ContentDetailUtils.DTV_CHANNEL_CATEGORY_MISSED.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_MISSED,
                                mDetailData.getContentsId(), mDetailData.getChannelId());
                        //VOD(関連)の場合
                    } else if (ContentDetailUtils.DTV_CHANNEL_CATEGORY_RELATION.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_RELATION,
                                mDetailData.getContentsId(), mDetailData.getChannelId());
                    } else {
                        showStartStbProgress(View.GONE);
                    }
                    break;
                case ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID://ひかりTV
                default:
                    if (mDetailFullData == null) {
                        break;
                    }
                    startHikariApplication();
                    break;
            }
        } else if (mDetailData != null && !isFromHeader
                //「契約する」ボタンの動線
                //mIsSend に因らず毎回判定する
                && mThumbnailContractButtonClicked) {
            DTVTLogger.debug("contract button clicked.");
            //「契約する」ボタンの動線でのサービスアプリ連携判定
            switch (mDetailData.getServiceId()) {
                case ContentUtils.DTV_CONTENTS_SERVICE_ID: // dTV
                case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
                    break;
                case ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID://ひかりTV
                default:
                    if (mDetailFullData == null) {
                        break;
                    }
                    //「契約する」ボタンの動線でのひかりTVのサービスアプリ連携
                    DTVTLogger.debug("contract button to start application HikariTv");
                    setRelayClientHandler();
                    startHikariApplication();
                    break;
            }
        }
        if (isTvPlaying()) {
            if (mPlayerViewLayout != null) {
                mPlayerViewLayout.onPause();
                setRemotePlayArrow(mPlayerData);
            }
        }
        super.onStartRemoteControl(isFromHeader);
        DTVTLogger.end();
    }

    /**
     * テレビで視聴中であるかどうか.
     * @return true:「テレビで視聴する」をタップした
     */
    private boolean isTvPlaying() {
        if (mIsFromHeader) {
            return false;
        }
        RemoteControllerView mRemoteControllerView = getRemoteControllerView();
        return mRemoteControllerView != null && mRemoteControllerView.isTopRemoteControllerUI();
    }

    /**
     * STBの連携のインジケーター表示.
     * @param visible 表示値
     */
    private void showStartStbProgress(final int visible) {
        if (!mIsFromHeader) {
            setRemoteProgressVisible(visible);
        }
    }

    /**
     * STBのサービスアプリ起動（ひかり）.
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private void startHikariApplication() {
        showStartStbProgress(View.VISIBLE);
        if (ContentUtils.VIDEO_PROGRAM.equals(mDetailFullData.getDisp_type())) {
            if (ContentUtils.DTV_FLAG_ZERO.equals(mDetailFullData.getDtv()) || TextUtils.isEmpty(mDetailFullData.getDtv())
                    || FLAG_ZERO == mDetailFullData.getDtv().trim().length()) {
                if (ContentDetailUtils.BVFLG_FLAG_ONE.equals(mDetailFullData.getBvflg())) {
                    requestStartApplicationHikariTvCategoryHikaritvVod(mDetailFullData.getPuid(),
                            mDetailFullData.getCid(), mDetailFullData.getCrid());
                } else if (ContentDetailUtils.BVFLG_FLAG_ZERO.equals(mDetailFullData.getBvflg()) || TextUtils.isEmpty(mDetailFullData.getBvflg())) {
                    if (mPurchasedVodListResponse == null) {
                        showStartStbProgress(View.GONE);
                        return;
                    }
                    // 購入済みVOD一覧
                    ArrayList<ActiveData> activeDatas = mPurchasedVodListResponse.getVodActiveData();
                    // ライセンスID取得
                    String validLicenseId = ContentUtils.getRentalVodValidInfo(mDetailFullData, activeDatas, false);
                    //購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較して一致した場合
                    if (!TextUtils.isEmpty(validLicenseId)) {
                        DTVTLogger.debug(String.format("requestStartApplicationHikariTvCategoryHikaritvVod(%s, %s, %s)",
                                validLicenseId, mDetailFullData.getCid(), mDetailFullData.getCrid()));
                        requestStartApplicationHikariTvCategoryHikaritvVod(validLicenseId,
                                mDetailFullData.getCid(), mDetailFullData.getCrid());
                    } else {
                        DTVTLogger.debug("license_id is not match!");
                        DTVTLogger.debug(String.format("requestStartApplicationHikariTvCategoryDtvSvod(%s)",
                                mDetailFullData.getCrid()));
                        if (ContentUtils.DTV_FLAG_ONE.equals(mDetailFullData.getDtv())) {
                            setHikariType(ContentUtils.HikariType.HIKARITV_IN_DTV);
                        }
                        // ひかりTV内VOD(dTV含む)のシリーズ
                        requestStartApplicationHikariTvCategoryDtvSvod(mDetailFullData.getCrid());
                    }
                } else {
                    showStartStbProgress(View.GONE);
                }
            } else if (ContentUtils.DTV_FLAG_ONE.equals(mDetailFullData.getDtv())) {
                setHikariType(ContentUtils.HikariType.HIKARITV_IN_DTV);
                //ひかりTV内dTVのVOD,「episode_id」,「crid」を通知する
                requestStartApplicationHikariTvCategoryDtvVod(mDetailFullData.getEpisode_id(),
                        mDetailFullData.getCrid());
            } else {
                showStartStbProgress(View.GONE);
            }
            //「disp_type」が「video_series」の場合
        } else if (ContentUtils.VIDEO_SERIES.equals(mDetailFullData.getDisp_type())) {
            if (ContentUtils.DTV_FLAG_ONE.equals(mDetailFullData.getDtv())) {
                setHikariType(ContentUtils.HikariType.HIKARITV_IN_DTV);
            }
            // ひかりTV内VOD(dTV含む)のシリーズ
            requestStartApplicationHikariTvCategoryDtvSvod(mDetailFullData.getCrid());
            //「disp_type」が「tv_program」の場合
        } else if (ContentUtils.TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
            //「tv_service」が「1」の場合 ひかりTVの番組
            if (ContentUtils.TV_SERVICE_FLAG_HIKARI.equals(mDetailFullData.getmTv_service())) {
                //ひかりTVの番組
                requestStartApplicationHikariTvCategoryIptv(mDetailFullData.getmChno());
                //「tv_service」が「2」の場合
            } else if (ContentUtils.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(mDetailFullData.getmTv_service())) {
                //「contents_type」が「0」または未設定の場合  ひかりTV内dTVチャンネルの番組
                if (ContentUtils.CONTENT_TYPE_FLAG_ZERO.equals(mDetailFullData.getmContent_type())
                        || TextUtils.isEmpty(mDetailFullData.getmContent_type())) {
                    setHikariType(ContentUtils.HikariType.HIKARITV_IN_DTV_CH);
                    //中継アプリに「chno」を通知する
                    requestStartApplicationHikariTvCategoryDtvchannelBroadcast(mDetailFullData.getmChno());
                } else if (ContentUtils.CONTENT_TYPE_FLAG_ONE.equals(mDetailFullData.getmContent_type())
                        || ContentUtils.CONTENT_TYPE_FLAG_TWO.equals(mDetailFullData.getmContent_type())
                        || ContentUtils.CONTENT_TYPE_FLAG_THREE.equals(mDetailFullData.getmContent_type())) {
                    //「vod_start_date」> 現在時刻の場合  ひかりTV内dTVチャンネルの番組(見逃し、関連VOD予定だが未配信)
                    if (DateUtils.getNowTimeFormatEpoch() < mDetailFullData.getmVod_start_date()) {
                        setHikariType(ContentUtils.HikariType.HIKARITV_IN_DTV_CH);
                        //中継アプリに「chno」を通知する
                        requestStartApplicationHikariTvCategoryDtvchannelBroadcast(mDetailFullData.getmChno());
                        //「vod_start_date」 <= 現在時刻 < 「vod_end_date」の場合 「crid」を通知する
                    } else if (DateUtils.getNowTimeFormatEpoch() >= mDetailFullData.getmVod_start_date()
                            && DateUtils.getNowTimeFormatEpoch() < mDetailFullData.getmVod_end_date()) {
                        setHikariType(ContentUtils.HikariType.HIKARITV_IN_DTV_CH);
                        // ひかりTV内dTVチャンネル 見逃し／関連番組
                        requestStartApplicationHikariTvCategoryDtvchannelMissed(mDetailFullData.getCrid());
                    } else {
                        showStartStbProgress(View.GONE);
                    }
                } else {
                    showStartStbProgress(View.GONE);
                }
            } else if (ContentUtils.isBsOrTtbProgramPlala(mDetailFullData.getmTv_service())) {
                if (TextUtils.isEmpty(mDetailFullData.getmService_id())) {
                    showStartStbProgress(View.GONE);
                    showErrorDialog(getString(R.string.common_failed_get_info));
                } else {
                    if (ContentUtils.TV_SERVICE_FLAG_TTB.equals(mDetailFullData.getmTv_service())) {
                        requestStartApplicationHikariTvCategoryTerrestrialDigital(mDetailFullData.getmService_id());
                    } else if (ContentUtils.TV_SERVICE_FLAG_BS.equals(mDetailFullData.getmTv_service())) {
                        requestStartApplicationHikariTvCategorySatelliteBs(mDetailFullData.getmService_id());
                    }
                }
            } else {
                showStartStbProgress(View.GONE);
            }
        } else {
            showStartStbProgress(View.GONE);
        }
    }

    @Override
    public void onEndRemoteControl() {
        DTVTLogger.debug(String.format("mIsFromHeader:%s", mIsFromHeader));
        if (!mIsFromHeader) {
            mIsSend = true;
            RemoteControllerView mRemoteControllerView = getRemoteControllerView();
            if (mRemoteControllerView != null) {
                TextView mTextView = mRemoteControllerView.findViewById(R.id.watch_by_tv);
                mTextView.setText(getString(R.string.remote_controller_viewpager_text_use_remote));
            }
        }
        if (mDetailData != null) {
            switch (mDetailData.getServiceId()) {
                case ContentUtils.DTV_CONTENTS_SERVICE_ID:
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_dtv, null));
                    break;
                case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID:
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_d_anime, null));
                    break;
                case ContentUtils.DAZN_CONTENTS_SERVICE_ID:
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_dazn, null));
                    break;
                case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                case ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID:
                default:
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
                    break;
            }
        }
        //「契約する」ボタンが押下された契機でリモコンが表示されていた場合は押下フラグを下げる。
        // （「契約する」ボタンの押下を判定してmIsSend に因らず毎回、ひかりTVアプリの連携起動するため）
        mThumbnailContractButtonClicked = false;
        super.onEndRemoteControl();
    }

    /**
     * プレイヤーエラーダイアログを表示.
     * (ダイアログ終了後画面が終わるタイプ)
     * @param errorMessage エラーメッセージ
     */
    private void showDialogToConfirmClose(final String errorMessage) {
        CustomDialog closeDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.ERROR);
        closeDialog.setContent(errorMessage);
        closeDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                contentsDetailCloseKey(null);
            }
        });
        closeDialog.setOnTouchOutside(false);
        closeDialog.setCancelable(false);
        closeDialog.setOnTouchBackkey(false);
        closeDialog.showDialog();
    }

    /**
     * プレイヤーエラーダイアログを表示.
     * (ダイアログ終了後画面が終わらないタイプ)
     * @param errorMessage エラーメッセージ
     */
    private void showDialogToConfirm(final String errorMessage) {
        CustomDialog closeDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.ERROR);
        closeDialog.setContent(errorMessage);
        closeDialog.setOnTouchOutside(false);
        closeDialog.setCancelable(false);
        closeDialog.showDialog();
    }

    /**
     * サムネイル取得処理を止める.
     */
    private void stopThumbnailConnect() {
        DTVTLogger.start();
        mIsDownloadStop = true;
        if (mThumbnailProvider != null) {
            new StopThumbnailConnect().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mThumbnailProvider);
            mThumbnailProvider.stopConnect();
            mThumbnailProvider.removeAllMemoryCache();
        }
        DTVTLogger.end();
    }

    /**
     * 止めたサムネイル取得処理を再度取得可能な状態にする.
     */
    private void enableThumbnailConnect() {
        DTVTLogger.start();
        mIsDownloadStop = false;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.enableConnect();
        }
        DTVTLogger.end();
    }

    @Override
    public void recordingReservationResult(final RemoteRecordingReservationResultResponse response) {
        DTVTLogger.start();
        if (response != null) {
            if (RemoteRecordingReservationResultResponse
                    .REMOTE_RECORDING_RESERVATION_RESULT_RESPONSE_STATUS_NG.equals(response.getStatus())) {
                String errorMessage;
                if (RemoteRecordingReservationResultResponse.REMOTE_RECORDING_RESERVATION_OVER_REGISTRATION
                        .equals(response.getErrorNo())) {
                    errorMessage = getString(R.string.recording_reservation_failed_dialog_over);
                } else {
                    errorMessage = getString(R.string.recording_reservation_failed_dialog_msg);
                }
                CustomDialog dialog = ContentDetailUtils.createErrorDialog(ContentDetailActivity.this, errorMessage);
                dialog.showDialog();
            } else {
                if (mToast != null) {
                    mToast.cancel();
                }
                if (!this.isFinishing()) {
                    // 成功
                    mToast = Toast.makeText(this, getResources().getString(R.string.recording_reservation_complete_dialog_msg), Toast.LENGTH_SHORT);
                    mToast.show();
                    sendEvent(getString(R.string.google_analytics_category_service_name_h4d),
                            getString(R.string.google_analytics_category_action_recording_reservation),
                            getTitleText().toString(), null);
                } else {
                    mToast = null;
                }
            }
        } else {
            CustomDialog dialog = ContentDetailUtils.createErrorDialog(ContentDetailActivity.this, getString(R.string.recording_reservation_failed_dialog_msg));
            dialog.showDialog();
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickRecordingReservationIcon(final View view) {
        //未契約時は契約導線を表示
        if (mViewIngType != null && mViewIngType.equals(ContentUtils.ViewIngType.DISABLE_WATCH_AGREEMENT_DISPLAY)) {
            leadingContract();
            return;
        }
        //詳細画面の表示中に制限時間以内になってしまったかどうかの検査
        if (!checkRecordTime()) { //録画は不能になったので帰る
            return;
        }
        // リスト表示用のアラートダイアログを表示
        if (mRecordingReservationCustomtDialog == null) {
            mRecordingReservationCustomtDialog = ContentDetailUtils.createRecordingReservationConfirmDialog(ContentDetailActivity.this);
            mRecordingReservationCustomtDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    DTVTLogger.debug("Request RecordingReservation");
                    DTVTLogger.debug(mRecordingReservationContentsDetailInfo.toString());
                    if (!mContentsDetailDataProvider.requestRecordingReservation(mRecordingReservationContentsDetailInfo)) {
                        //APIの実行が行えなかった場合は即座にfalseが返却されるので、エラーとする
                        CustomDialog dialog = ContentDetailUtils.createErrorDialog(ContentDetailActivity.this,
                                getString(R.string.recording_reservation_failed_dialog_msg));
                        dialog.showDialog();
                    }
                }
            });
        }
        mRecordingReservationCustomtDialog.showDialog();
    }

    /**
     * 詳細画面を長時間開いているうちに制限時間を経過したかどうかの判定.
     * @return 録画可能ならばtrue
     */
    private boolean checkRecordTime() {
        if (mDetailFullData != null) {
            ContentUtils.ContentsType contentsType = ContentUtils.getHikariContentsType(mDetailFullData);
            //取得を行ったコンテンツ種別が、録画ボタン表示対象以外かどうかの確認
            //(H4d契約は成立しなければボタンは表示されないので、ここでは見なくて良いでしょう)
            if (!(contentsType == ContentUtils.ContentsType.HIKARI_TV
               || contentsType == ContentUtils.ContentsType.HIKARI_IN_DCH_TV)) {
                //録画ボタン表示対象の種別以外ならば、録画可能時間外や放送中等なので、録画不能のダイアログを出して、falseを返す
                CustomDialog dialog = ContentDetailUtils.createErrorDialog(ContentDetailActivity.this,
                        getString(R.string.recording_reservation_failed_dialog_msg));
                dialog.showDialog();
                return false;
            }
        }
        //制限時間経過以外ならばtrue
        return true;
    }

    /**
     * コンテンツ詳細画面の下部にコントローラのヘッダーを表示するかどうかを返す.
     * @return true:表示する false:表示しない
     */
    public Boolean getControllerVisible() {
        return mIsControllerVisible;
    }

    /**
     * コンテンツの視聴可否判定を行う.
     * @param viewIngType 視聴可否種別
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void checkWatchContents(final ContentUtils.ViewIngType viewIngType) {
        switch (viewIngType) {
            case ENABLE_WATCH_LIMIT_THIRTY:
            case ENABLE_WATCH_LIMIT_THIRTY_001:
                //期限まで30日以内表示内容設定
                if (mEndDate == 0L) {
                    mEndDate = mDetailFullData.getPublish_end_date();
                    displayLimitDate();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRentalVodListCallback(final PurchasedVodListResponse response) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response == null) {
                    showErrorToast(ContentDetailUtils.ErrorType.rentalVoidListGet);
                    return;
                }
                mPurchasedVodListResponse = response;
                ArrayList<ActiveData> vodActiveData = response.getVodActiveData();
                String result = ContentUtils.getRentalVodValidInfo(mDetailFullData, vodActiveData, true);
                mEndDate = Long.parseLong(result);
                mVodEndDate = mDetailFullData.getAvail_end_date();
                displayRentalContentsDate(mEndDate, mVodEndDate);
                DTVTLogger.debug("get rental vod end date:" + mEndDate);
                mViewIngType = ContentUtils.getRentalVodViewingType(mDetailFullData, mEndDate);
                DTVTLogger.debug("get rental vod viewing type:" + mViewIngType);
                changeUIBasedContractInfo();
                responseResultCheck(mViewIngType, mContentsType);
            }
        });
        DTVTLogger.end();
    }

    /**
     * 購入済みコンテンツ視聴期限表示.
     * @param mEndDate 視聴可能期限
     * @param mVodEndDate Vod視聴可能期限
     */
    private void displayRentalContentsDate(final long mEndDate, final long mVodEndDate) {
        DTVTLogger.start();
        DtvContentsDetailFragment detailFragment = getDetailFragment();
        if (mEndDate != 0L && mEndDate < mVodEndDate) {
            String date = DateUtils.formatEpochToDateString(mEndDate);
            String untilDate = StringUtils.getConnectStrings(date, getString(R.string.common_until));
            OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
            if (detailData != null) {
                detailData.setChannelDate(untilDate);
                detailFragment.setOtherContentsDetailData(detailData);
            }
        }
        DTVTLogger.end();
    }

    @Override
    public void onRentalChListCallback(final PurchasedChannelListResponse response) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response == null) {
                    showErrorToast(ContentDetailUtils.ErrorType.rentalChannelListGet);
                    return;
                }
                mEndDate = ContentUtils.getRentalChannelValidEndDate(response, mChannel);
                mVodEndDate = mDetailFullData.getAvail_end_date();
                displayRentalContentsDate(mEndDate, mVodEndDate);
                DTVTLogger.debug("get rental vod end date:" + mEndDate);
                mViewIngType = ContentUtils.getRentalChannelViewingType(mDetailFullData, mEndDate);
                DTVTLogger.debug("get rental vod viewing type:" + mViewIngType);
                changeUIBasedContractInfo();
                responseResultCheck(mViewIngType, mContentsType);
            }
        });
        DTVTLogger.end();
    }

    /**
     * 短い方の視聴可能期限を表示する.
     */
    private void displayLimitDate() {
        DTVTLogger.start();
        DtvContentsDetailFragment detailFragment = getDetailFragment();
        if (mEndDate != 0L && mEndDate < mVodEndDate && (mVodEndDateText == null || mVodEndDateText.isEmpty())) {
            String date = DateUtils.formatEpochToDateString(mEndDate);
            String untilDate = StringUtils.getConnectStrings(date, getString(R.string.common_until));
            DTVTLogger.debug("display limit date:---" + untilDate);
            OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
            if (detailData != null) {
                detailData.setChannelDate(untilDate);
                detailFragment.setOtherContentsDetailData(detailData);
            }
        } else {
            DTVTLogger.debug("display limit date:---" + mVodEndDateText);
            OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
            if (detailData != null) {
                detailData.setChannelDate(mVodEndDateText);
                detailFragment.setOtherContentsDetailData(detailData);
            }
        }
        DTVTLogger.end();
    }

    @Override
    public void onErrorCallBack(final PlayerViewLayout.PlayerErrorType mPlayerErrorType) {
        showProgressBar(false);
        String msg = null;
        boolean isInit = false;
        switch (mPlayerErrorType) {
            case REMOTE:
                msg = getString(R.string.remote_connect_error_local_registration_unset, String.valueOf(DlnaUtils.ERROR_CODE_LOCAL_REGISTRATION_UNSET));
                break;
            case ACTIVATION:
                msg = getString(R.string.activation_failed_error_player);
                break;
            case EXTERNAL:
                msg = getString(R.string.contents_detail_external_display_dialog_msg);
                break;
            case AGE:
                msg = getString(R.string.contents_detail_parental_check_fail);
                break;
            case INIT_SUCCESS:
                isInit = true;
                break;
            case NONE:
            default:
                break;
        }
        if (msg != null) {
            if (mDetailFullData != null) {
                showDialogToConfirm(msg);
            } else {
                showDialogToConfirmClose(msg);
            }
            mPlayerViewLayout.removeSendMessage();
        }
        if (!isInit) {
            mPlayerViewLayout.showPlayingProgress(false);
            mPlayerViewLayout.hideCtrlView(mPlayerViewLayout.mIsShowControl);
        }
    }

    @Override
    public void onPlayerErrorCallBack(final int errorCode) {
        showProgressBar(false);
        String format = getString(R.string.contents_player_fail_error_code_format);
        String errorMsg = getString(R.string.contents_player_fail_msg);
        errorMsg = errorMsg.replace(format, String.valueOf(errorCode));
        //通信エラーの場合はリトライする
        if (errorCode >= ContentDetailUtils.RETRY_ERROR_START) {
            DTVTLogger.debug("not close");
            //自動再生コンテンツ再生準備
            setPlayRetryArrow();
            //OKで閉じないダイアログで表示
            showDialogToConfirm(errorMsg);
        } else {
            //再開不能エラーは従来通り終了するダイアログを使用する
            DTVTLogger.debug("close");
            showDialogToConfirmClose(errorMsg);
        }
        mPlayerViewLayout.removeSendMessage();
    }

    @Override
    public void onScreenOrientationChangeCallBack(final boolean isLandscape) {
        if (isLandscape) {
            setTitleVisibility(false);
            setTabVisibility(false);
            setRemoteControllerViewVisibility(View.GONE);
        } else {
            setTitleVisibility(true);
            setTabVisibility(true);
            setRemoteControllerViewVisibility(View.VISIBLE);
        }
        initDisplayMetrics();
        int width = getWidthDensity();
        initDisplayMetrics();
        int height = getHeightDensity();
        mPlayerViewLayout.setScreenSize(width, height);
    }

    /**
     * 番組情報/チャンネルタブの表示設定.
     * @param visible 表示要否
     */
    private void setTabVisibility(final boolean visible) {
        switch (mDisplayState) {
            case ContentDetailUtils.CONTENTS_DETAIL_ONLY:
            case ContentDetailUtils.PLAYER_AND_CONTENTS_DETAIL:
                if (visible) {
                    findViewById(R.id.dtv_contents_detail_main_layout_vp).setVisibility(View.VISIBLE);
                    findViewById(R.id.rl_dtv_contents_detail_tab).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.dtv_contents_detail_main_layout_vp).setVisibility(View.GONE);
                    findViewById(R.id.rl_dtv_contents_detail_tab).setVisibility(View.GONE);
                }
                break;
            case ContentDetailUtils.PLAYER_ONLY:
                if (visible) {
                    findViewById(R.id.dtv_contents_detail_player_only).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.dtv_contents_detail_player_only).setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * ひかりTV多チャンネルデータ取得.
     */
    private void getMultiChannelData() {
        //放送中番組
        DlnaContentMultiChannelDataProvider provider = new DlnaContentMultiChannelDataProvider(this,
                new DlnaContentMultiChannelDataProvider.OnMultiChCallbackListener() {
                    @Override
                    public void multiChannelFindCallback(final DlnaObject dlnaObject) {
                        runOnUiThread(new Runnable() {
                            @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
                            @Override
                            public void run() {
                                showRemotePlayingProgress(false);
                                if (dlnaObject != null) {
                                    //リトライの時の為に控えておく
                                    mDlnaObject = dlnaObject;
                                    //放送中ひかりTVコンテンツの時は自動再生する
                                    playAutoContents();
                                } else {
                                    setThumbnailMessage(getString(R.string.contents_detail_player_no_channel), "", true, false);
                                }
                            }
                        });
                    }

                    @Override
                    public void multiChannelErrorCallback(final DlnaUtils.RemoteConnectErrorType errorType, final int errorCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showRemotePlayingProgress(false);
                                if (DlnaUtils.RemoteConnectErrorType.START_DTCP.equals(errorType)
                                        || DlnaUtils.RemoteConnectErrorType.START_DIRAG.equals(errorType)) {
                                    showErrorDialog(DlnaUtils.getDlnaErrorMessage(ContentDetailActivity.this, errorType, errorCode));
                                } else {
                                    showGetDataFailedToast();
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnectErrorCallback(final int errorCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showRemotePlayingProgress(false);
                                showErrorDialog(DlnaUtils.getDlnaErrorMessage(ContentDetailActivity.this, DlnaUtils.RemoteConnectErrorType.REMOTE_CONNECT_STATUS, errorCode));
                                setRemotePlayArrow(null);
                            }
                        });
                    }

                    @Override
                    public void onRemoteConnectTimeOutCallback() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showRemotePlayingProgress(false);
                                showErrorDialog(getString(R.string.remote_connect_error_timeout));
                                setRemotePlayArrow(null);
                            }
                        });
                    }
                });
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (dlnaDmsItem != null) {
            String tvService = mDetailFullData.getmTv_service();
            //この場合に使用するチャンネル番号を取得する
            int convertedChannelNumber = ContentDetailUtils.convertChannelNumber(mChannel, tvService);
            //変換後のチャンネルIDを使用して呼び出す
            provider.findChannelByChannelNo(String.valueOf(convertedChannelNumber), tvService);
        } else {
            showRemotePlayingProgress(false);
            DTVTLogger.error("dlnaDmsItem == null");
        }
    }

    /**
     * 放送中ひかりコンテンツ再生.
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void playAutoContents() {
        mDisplayState = ContentDetailUtils.PLAYER_AND_CONTENTS_DETAIL;
        RecordedContentsDetailData data = new RecordedContentsDetailData();
        data.setUpnpIcon(mChannel.getThumbnail());
        if (mDetailFullData != null) {
            data.setTitle(mDetailFullData.getTitle());
        }
        data.setResUrl(mDlnaObject.mResUrl);
        data.setSize(mDlnaObject.mSize);
        data.setDuration(mDlnaObject.mDuration);
        data.setVideoType(mDlnaObject.mVideoType);
        data.setBitrate(mDlnaObject.mBitrate);
        data.setIsLive(true);
        switch (StbConnectionManager.shared().getConnectionStatus()) {
            case HOME_IN:
                data.setIsRemote(false);
                //リトライ時にリモートではない事を宣言する
                mNotRemoteRetry = true;
                break;
            default:
                if (mNotRemoteRetry) {
                    //リモートではない場合のリトライなので、リモートではない
                    data.setIsRemote(false);
                } else {
                    data.setIsRemote(true);
                }
                break;
        }
        initPlayer(data);
    }

    /**
     * コンテンツ種別ごとのサムネイル部分の表示.
     * @param contentsType コンテンツ種別
     * @param viewIngType  視聴可否判定結果
     * @param detailUserType ユーザタイプ
     */
    @SuppressWarnings({"EnumSwitchStatementWhichMissesCases", "OverlyComplexMethod", "OverlyLongMethod"})
    private void displayThumbnail(final ContentUtils.ContentsType contentsType, final ContentUtils.ViewIngType viewIngType,
                                  final ContentUtils.ContentsDetailUserType detailUserType) {
        DTVTLogger.start();
        //Pure系コンテンツはサムネイル表示済みのため何もしない
        if (ContentUtils.isPureContents(contentsType)) {
            return;
        }
        if (detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_LOGOUT)) {
            loginNgDisplay();
            Button button = setThumbnailMessage(getString(R.string.contents_detail_login_message),
                    getString(R.string.contents_detail_login_button), true, true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DaccountUtils.startDAccountApplication(ContentDetailActivity.this);
                }
            });
            DTVTLogger.end("logout thumbnail display");
            return;
        }
        //必要なデータが揃わないうちは何もしない
        if (contentsType == null || viewIngType == null || ContentUtils.isSkipViewingType(viewIngType, contentsType)
                || contentsType.equals(ContentUtils.ContentsType.OTHER)) {
            DTVTLogger.debug("display thumbnail must data none");
            return;
        }
        DTVTLogger.debug("display thumbnail contents type = " + contentsType);
        DTVTLogger.debug("display thumbnail viewing type = " + viewIngType);
        DTVTLogger.debug("display thumbnail contents detailUserType = " + detailUserType);
        switch (contentsType) {
            case HIKARI_TV_NOW_ON_AIR:
                //ひかり Tv for ドコモ(放送中) ひかりTV契約有 宅内
                if (detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_INSIDE_HIKARI_CONTRACT)) {
                    //※要購入の場合
                    if (ContentUtils.isContractWireDisplay(viewIngType)) {
                        Button button = setThumbnailMessage(getString(R.string.contents_detail_hikari_channel_agreement),
                                getString(R.string.contents_detail_contract_leading_button), true, true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                thumbnailContractButtonAction();
                            }
                        });
                    } else if (ContentUtils.isBsPremiumCh(viewIngType)) {
                        setThumbnailText(getString(R.string.contents_detail_bs_premium_ch));
                    } else {
                        playerSwitch();
                    }
                } else if (detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_CONTRACT)) {
                    //※要購入の場合
                    if (ContentUtils.isContractWireDisplay(viewIngType)) {
                        Button button = setThumbnailMessage(
                                getString(R.string.contents_detail_hikari_channel_agreement),
                                getString(R.string.contents_detail_contract_leading_button), true, true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                thumbnailContractButtonAction();
                            }
                        });
                    } else if (ContentUtils.isBsPremiumCh(viewIngType)) {
                        setThumbnailText(getString(R.string.contents_detail_bs_premium_ch));
                    } else {
                        playerSwitch();
                    }
                } else {
                    hikariTvThumbnailDisplay(detailUserType, viewIngType);
                }
                break;
            case HIKARI_TV:
            case HIKARI_TV_WITHIN_TWO_HOUR:
                //ひかり Tv for ドコモ(放送中、放送前) ひかりTV契約有 宅外
                if (detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_INSIDE_HIKARI_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_CONTRACT)) {
                    //HIKARI_TV_NOW_ON_AIRの場合はこの分岐の中には入ってこない想定
                    //※要購入の場合
                    if (ContentUtils.isContractWireDisplay(viewIngType)) {
                        Button button = setThumbnailMessage(
                                getString(R.string.contents_detail_hikari_channel_agreement),
                                getString(R.string.contents_detail_contract_leading_button), true, true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                thumbnailContractButtonAction();
                            }
                        });
                    } else if (ContentUtils.isBsPremiumCh(viewIngType)) {
                        setThumbnailText(getString(R.string.contents_detail_bs_premium_ch));
                    }
                    //未ペアリング契約有
                } else {
                    hikariTvThumbnailDisplay(detailUserType, viewIngType);
                }
                break;
            case HIKARI_TV_VOD:
                //ひかり Tv for ドコモ(VOD) ひかりTV契約有 宅外
                if (detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_INSIDE_HIKARI_CONTRACT)) {
                    //※要購入の場合
                    if (ContentUtils.isContractWireDisplay(viewIngType)) {
                        Button button = setThumbnailMessage(
                                getString(R.string.contents_detail_hikari_vod_agreement),
                                getString(R.string.contents_detail_tv_confirm_leading_button), true, true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                thumbnailContractButtonAction();
                            }
                        });
                    } else {
                        setThumbnailMessage(getString(R.string.contents_detail_thumbnail_text), "", true, false);
                    }
                } else if (detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_CONTRACT)) {
                    //※要購入の場合
                    if (ContentUtils.isContractWireDisplay(viewIngType)) {
                        setThumbnailMessage(getString(R.string.contents_detail_hikari_vod_agreement), "", true, false);
                    } else {
                        setThumbnailMessage(getString(R.string.contents_detail_thumbnail_text), "", true, false);
                    }
                    //未ペアリング契約有かつ視聴可否判定OK
                } else if (detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_NO_CONTRACT)) {
                    startLaunchStbActivity();
                } else {
                    Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement),
                            getString(R.string.contents_detail_contract_leading_button), true, true);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            thumbnailContractButtonAction();
                        }
                    });
                }
                break;
            case HIKARI_IN_DCH:
            case HIKARI_IN_DCH_TV_NOW_ON_AIR:
            case HIKARI_IN_DCH_TV_WITHIN_TWO_HOUR:
            case HIKARI_IN_DCH_TV:
            case HIKARI_IN_DCH_MISS:
            case HIKARI_IN_DCH_RELATION:
                //宅内契約有、宅外契約有、未ペアリング契約有
                if ((detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_INSIDE_HIKARI_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_CONTRACT))) {
                    setThumbnailMessage(getString(R.string.dtv_channel_service_start_text), "", false, true);
                } else {
                    Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement),
                            getString(R.string.contents_detail_contract_leading_button), true, true);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            thumbnailContractButtonAction();
                        }
                    });
                }
                break;
            case HIKARI_IN_DTV:
                //宅内契約有、宅外契約有、未ペアリング契約有
                if ((detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_INSIDE_HIKARI_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_CONTRACT))) {
                    setThumbnailMessage(getString(R.string.dtv_content_service_start_text), "", false, true);
                } else {
                    Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement),
                            getString(R.string.contents_detail_contract_leading_button), true, true);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            thumbnailContractButtonAction();
                        }
                    });
                }
                break;
        }
        DTVTLogger.end();
    }

    /**
     * 初期扉画面に遷移する.
     */
    private void startLaunchStbActivity() {
        Button button = setThumbnailMessage(getString(R.string.contents_detail_pairing_request),
                getString(R.string.contents_detail_pairing_button), true, true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //ペアリング設定
                Intent intent = new Intent(getApplicationContext(), LaunchStbActivity.class);
                intent.putExtra(ContentUtils.LAUNCH_STB_FROM, ContentUtils.LAUNCH_STB_CONTENT_DETAIL);
                startActivity(intent);
            }
        });
    }

    /**
     * ひかり未契約の場合視聴可否を視聴不可に変更.
     * @param detailUserType コンテンツ詳細ユーザタイプ
     * @param contentsType コンテンツタイプ
     * @param viewIngType 視聴可否種別
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void shapeViewType(final ContentUtils.ContentsDetailUserType detailUserType, final ContentUtils.ContentsType contentsType,
                               final ContentUtils.ViewIngType viewIngType) {
        switch (contentsType) {
            case HIKARI_TV_NOW_ON_AIR:
            case HIKARI_TV_VOD:
            case HIKARI_TV_WITHIN_TWO_HOUR:
            case HIKARI_IN_DCH:
            case HIKARI_IN_DCH_TV_NOW_ON_AIR:
            case HIKARI_IN_DCH_TV_WITHIN_TWO_HOUR:
            case HIKARI_IN_DCH_TV:
            case HIKARI_IN_DCH_MISS:
            case HIKARI_IN_DCH_RELATION:
            case HIKARI_IN_DTV:
            case HIKARI_TV:
                //ひかり未契約の場合視聴可否を視聴不可に変更
                if (viewIngType == ContentUtils.ViewIngType.NONE_STATUS
                        && (detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_INSIDE_HIKARI_NO_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.PAIRING_OUTSIDE_HIKARI_NO_CONTRACT)
                        || detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_NO_CONTRACT))
                        ) {
                    mViewIngType = ContentUtils.ViewIngType.DISABLE_WATCH;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 宅内宅外の状態により再生ボタン表示と自動再生を出しわける.
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void playerSwitch() {
        //安全のため再生開始直前にも宅内宅外判定を入れる
        switch (StbConnectionManager.shared().getConnectionStatus()) {
            case HOME_IN:
                getMultiChannelData();
                break;
            default:
                setRemotePlayArrow(null);
                break;
        }
        sendH4dPlayDimensions();
    }

    /**
     * h4dプレイヤーカスタムディメンション送信.
     */
    private void sendH4dPlayDimensions() {
        mIsH4dPlayer = true;
        if (mHikariType == null) {
            return;
        }
        String contentsType1 = ContentUtils.getContentsType1(ContentDetailActivity.this, mHikariType);
        if (contentsType1 == null) {
            return;
        }
        String screenName = ContentDetailUtils.getScreenNameMap(contentType, ContentDetailActivity.this, mIsH4dPlayer).
                get(mTabNames[ContentDetailUtils.CONTENTS_DETAIL_INFO_TAB_POSITION]);
        String serviceName = getString(R.string.google_analytics_custom_dimension_service_h4d);
        String contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_live);
        String contentName = getTitleText().toString();
        if (TextUtils.isEmpty(contentName) && mDetailFullData != null) {
            contentName = mDetailFullData.getTitle();
        }
        super.sendScreenView(screenName, ContentUtils.getCustomDimensions(null, serviceName, contentsType1, contentsType2, contentName));
    }

    /**
     * ひかりコンテンツのサムネイル表示.
     * @param detailUserType ユーザ状態
     * @param viewIngType    視聴可否種別
     */
    private void hikariTvThumbnailDisplay(final ContentUtils.ContentsDetailUserType detailUserType, final ContentUtils.ViewIngType viewIngType) {
        if (detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_CONTRACT)
                && ContentUtils.isEnableDisplay(viewIngType)) {
            startLaunchStbActivity();
        } else if (detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_NO_CONTRACT)) {
            startLaunchStbActivity();
        } else {
            Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement),
                    getString(R.string.contents_detail_contract_leading_button), true, true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    thumbnailContractButtonAction();
                }
            });
        }
    }

    /**
     * サムネイル上の契約するボタン内の動作.
     */
    private void thumbnailContractButtonAction() {
        mThumbnailContractButtonClicked = true;
        //STB接続がない場合(未ペアリング、宅外の場合)はブラウザ起動
        if (ContentDetailUtils.getStbStatus()) {
            contentDetailRemoteController();
        } else {
            startBrowser(UrlConstants.WebUrl.CONTRACT_URL);
        }
    }

    /**
     * サムネイル画像上の表示設定.
     * @param message         テキストエリアに表示するメッセージ
     * @param buttonText      ボタン上に表示するテキスト
     * @param isContractView  契約導線表示フラグ
     * @param isDisplayButton 赤ボタン表示フラグ
     * @return ボタン
     */
    private Button setThumbnailMessage(final String message, final String buttonText, final boolean isContractView, final boolean isDisplayButton) {
        Button button = findViewById(R.id.contract_leading_button);
        if (isContractView) {
            mThumbnailBtn.setVisibility(View.GONE);
            mContractLeadingView.setVisibility(View.VISIBLE);
            TextView contractLeadingText = findViewById(R.id.contract_leading_text);
            if (isDisplayButton) {
                button.setText(buttonText);
            } else {
                button.setVisibility(View.GONE);
            }
            contractLeadingText.setText(message);
            setThumbnailShadow();
        } else {
            mContractLeadingView.setVisibility(View.GONE);
            mThumbnailBtn.setVisibility(View.VISIBLE);
            TextView contractLeadingText = findViewById(R.id.view_contents_button_text);
            contractLeadingText.setText(message);
            contractLeadingText.setVisibility(View.VISIBLE);
            setThumbnailShadow();
        }
        return button;
    }

    /**
     * リモート視聴用の再生ボタン表示.
     * @param playData 再生用情報
     */
    private void setRemotePlayArrow(final RecordedContentsDetailData playData) {
        DTVTLogger.start();
        //再生ボタンは宅外かつ契約があるときのみ表示
        if (UserInfoUtils.isContract(this)) {
            if (mPlayerViewLayout != null) {
                mPlayerViewLayout.setVisibility(View.GONE);
            }
            setThumbnailShadow();
            showPlayIcon(true);
            mPlayIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (isFastClick()) {
                        if (mPlayerData != null
                                && (ContentsAdapter.DOWNLOAD_STATUS_COMPLETED == mPlayerData.getDownLoadStatus())) {
                            initPlayer(playData);
                        } else {
                            switch (StbConnectionManager.shared().getConnectionStatus()) {
                                case NONE_LOCAL_REGISTRATION:
                                    showErrorDialog(getString(R.string.remote_connect_error_local_registration_unset, String.valueOf(DlnaUtils.ERROR_CODE_LOCAL_REGISTRATION_UNSET)));
                                    break;
                                case HOME_OUT:
                                    if (mDetailFullData != null) {
                                        mPlayerData = null;
                                        showRemotePlayingProgress(true);
                                        getMultiChannelData();
                                    } else {
                                        if (playData != null) {
                                            initPlayer(playData);
                                        }
                                    }
                                    break;
                                case HOME_OUT_CONNECT:
                                case HOME_IN:
                                    if (playData == null) {
                                        showRemotePlayingProgress(true);
                                        getMultiChannelData();
                                    } else {
                                        initPlayer(playData);
                                    }
                                    break;
                                case NONE_PAIRING:
                                default:
                                    break;
                            }
                        }
                    }
                }
            });
        }
        showProgressBar(false);
        DTVTLogger.end();
    }

    /**
     * 放送中光コンテンツ再生リトライ用再生ボタン表示.
     */
    private void setPlayRetryArrow() {
        DTVTLogger.start();
        setThumbnailShadow();
        showPlayIcon(true);
        mPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isFastClick()) {
                    //再生情報があれば再再生を行う
                    if (mPlayerData != null) {
                        initPlayer(mPlayerData);
                    }
                }
            }
        });
        //各ウェイト表示を消す
        showProgressBar(false);
        showRemotePlayingProgress(false);
        showChannelProgressBar(false);
        if (mPlayerViewLayout != null) {
            mPlayerViewLayout.showPlayingProgress(false);
        }
        DTVTLogger.end();
    }

    /**
     * 再生中のくるくる処理.
     * @param isShow true 表示　false 非表示
     */
    private void showRemotePlayingProgress(final boolean isShow) {
        if (mProgressBar == null) {
            mProgressBar = new ProgressBar(ContentDetailActivity.this, null, android.R.attr.progressBarStyle);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mProgressBar.setLayoutParams(layoutParams);
        }
        if (isShow) {
            showPlayIcon(false);
            mThumbnailRelativeLayout.removeView(mProgressBar);
            mThumbnailRelativeLayout.addView(mProgressBar);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mThumbnailRelativeLayout.removeView(mProgressBar);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 再生中のくるくる処理.
     * @param isShow true 表示　false 非表示
     */
    private void showPlayIcon(final boolean isShow) {
        if (mPlayIcon == null) {
            mPlayIcon = new ImageView(getApplicationContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    (int) getResources().getDimension(R.dimen.contents_detail_player_media_controller_size),
                    (int) getResources().getDimension(R.dimen.contents_detail_player_media_controller_size)
            );
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mPlayIcon.setLayoutParams(layoutParams);
            mPlayIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mPlayIcon.setImageResource(R.mipmap.mediacontrol_icon_tap_play_arrow2);
        }
        if (isShow) {
            mThumbnailRelativeLayout.removeView(mPlayIcon);
            mThumbnailRelativeLayout.addView(mPlayIcon);
            mPlayIcon.setVisibility(View.VISIBLE);
        } else {
            mThumbnailRelativeLayout.removeView(mPlayIcon);
            mPlayIcon.setVisibility(View.GONE);
        }
    }

    /**
     * 視聴可否判定に基づいてUIの操作などを行う.
     */
    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod", "EnumSwitchStatementWhichMissesCases"})
    private void changeUIBasedContractInfo() {
        DTVTLogger.start();
        switch (mViewIngType) {
            case PREMIUM_CHECK_START:
                new ContentsDetailDataProvider(this).getChListData();
                break;
            case SUBSCRIPTION_CHECK_START:
                new ContentsDetailDataProvider(this).getVodListData();
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * 未契約時、契約導線を表示する.
     */
    public void leadingContract() {
        //契約誘導ダイアログを表示
        CustomDialog customDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
        customDialog.setContent(getString(R.string.contents_detail_no_agreement));
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                Uri uri = Uri.parse(UrlConstants.WebUrl.CONTRACT_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        customDialog.showDialog();
    }

    /**
     * DTV側で購入する/契約するボタン押下時の動作.
     * @param v view
     */
    public void contractButtonClick(final View v) {
        Uri uri = Uri.parse(UrlConstants.WebUrl.CONTRACT_LINK);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * サムネイル画像にシャドウをかける(アルファをかける).
     */
    private void setThumbnailShadow() {
        mThumbnail.setAlpha(ContentDetailUtils.THUMBNAIL_SHADOW_ALPHA);
    }

    /**
     * プロセスバーを表示する.
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        View view = findViewById(R.id.contents_detail_scroll_layout);
        if (view == null) {
            return;
        }
        if (showProgressBar) {
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            view.setVisibility(View.INVISIBLE);
            setRemoteProgressVisible(View.VISIBLE);
        } else {
            if (!mIsScreenViewSent && !mIsH4dPlayer && mDetailFullData != null) {
                sendScreenViewForPosition(ContentDetailUtils.CONTENTS_DETAIL_INFO_TAB_POSITION);
                mIsScreenViewSent = true;
            }
            setRemoteProgressVisible(View.INVISIBLE);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * チャンネルエリアのプロセスバーを表示する.
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showChannelProgressBar(final boolean showProgressBar) {
        if (mChannelFragment == null) {
            mChannelFragment = getChannelFragment();
            //フラグメント取得失敗時のヌルチェックを追加
            if (mChannelFragment == null) {
                return;
            }
        }
        if (showProgressBar) {
            // オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            mChannelFragment.showProgress(true);
        } else {
            mChannelFragment.showProgress(false);
        }
    }

    /**
     * ユーザ操作履歴送信.
     * ログイン状態でしか送信しない
     */
    private void sendOperateLog() {
        if (!TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferencesDaccountId(this)) && mSendOperateLog == null) {
            mSendOperateLog = new SendOperateLog(getApplicationContext());
            if (mDetailFullData != null) {
                mDetailData.setServiceId(ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID);
                mDetailData.setChannelId(mDetailFullData.getmService_id());
            }
            mSendOperateLog.sendOpeLog(mDetailData, mDetailFullData);
        }
    }

    /**
     * エラートーストを表示する.
     * @param errorType エラータイプ
     */
    private void showErrorToast(final ContentDetailUtils.ErrorType errorType) {
        DTVTLogger.start();
        showProgressBar(false);
        ErrorState errorState = null;
        switch (errorType) {
            case rentalChannelListGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.rentalChList);
                break;
            case rentalVoidListGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.rentalVodList);
                break;
            case channelListGet:
                errorState = mScaledDownProgramListDataProvider.getChannelError();
                break;
            case roleListGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.roleList);
                break;
            case contentDetailGet:
            case recommendDetailGet:
            default:
                break;
        }
        if (errorState != null) {
            showGetDataFailedToast(errorState.getErrorMessage());
        } else {
            showGetDataFailedToast();
        }
        DTVTLogger.end();
    }

    /**
     * エラーダイアログを表示する.
     * @param errorType エラータイプ
     */
    private void showErrorDialog(final ContentDetailUtils.ErrorType errorType) {
        DTVTLogger.start();
        showProgressBar(false);
        showChannelProgressBar(false);
        ErrorState errorState = null;
        CustomDialog.ApiOKCallback okCallback = null;
        //状況に合わせてエラーの取得場所を選択し、ダイアログ表示を行う
        switch (errorType) {
            case contentDetailGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.contentsDetailGet);
                okCallback = showDialogOkToBack();
                break;
            case tvScheduleListGet:
                errorState = mScaledDownProgramListDataProvider.getmTvScheduleError();
                break;
            case recommendDetailGet:
                errorState = mStbMetaInfoGetDataProvider.getError();
                okCallback = showDialogOkToBack();
                break;
            case channelListGet:
            case rentalChannelListGet:
            case rentalVoidListGet:
            case roleListGet:
            default:
                break;
        }
        CustomDialog customDialog = new CustomDialog(ContentDetailActivity.this,
                CustomDialog.DialogType.ERROR);
        //表示するダイアログの切り替え判定
        if (errorState == null || errorState.getErrorType() == DtvtConstants.ErrorType.SUCCESS) {
            //そもそも通信が行われていないか、通信自体には成功しその上でデータが無いならば、コンテンツが無い事を表示
            customDialog.setContent(getString(R.string.common_empty_data_message));
        } else {
            //契約誘導ダイアログを表示
            customDialog.setContent(errorState.getApiErrorMessage(this));
        }
        if (okCallback != null) {
            customDialog.setOkCallBack(okCallback);
        }
        customDialog.setOnTouchOutside(false);
        customDialog.setCancelable(false);
        customDialog.setOnTouchBackkey(false);
        customDialog.showDialog();
        DTVTLogger.end();
    }

    /**
     * OKボタン押下後前画面に戻る.
     * @return OKコールバック
     */
    private CustomDialog.ApiOKCallback showDialogOkToBack() {
        return new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                finish();
            }
        };
    }

    /**
     * クリップボタンの更新.
     * @param targetId 更新対象
     */
    private void checkClipStatus(final int targetId) {
        DTVTLogger.start();
        ClipKeyListDataManager manager = new ClipKeyListDataManager(ContentDetailActivity.this);
        List<Map<String, String>> mapList = manager.selectClipAllList();
        switch (targetId) {
            case ContentDetailUtils.CLIP_BUTTON_ALL_UPDATE:
                checkDetailClipStatus(mapList);
                checkChannelClipStatus(mapList);
                break;
            case ContentDetailUtils.CLIP_BUTTON_CHANNEL_UPDATE:
                checkChannelClipStatus(mapList);
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * 詳細情報のクリップボタン更新.
     * @param mapList クリップリスト(全件)
     */
    private void checkDetailClipStatus(final List<Map<String, String>> mapList) {
        DTVTLogger.start();
        DtvContentsDetailFragment dtvContentsDetailFragment = getDetailFragment();
        OtherContentsDetailData detailData = dtvContentsDetailFragment.getOtherContentsDetailData();
        if (detailData != null) {
            detailData.setClipStatus(ClipUtils.setClipStatusVodMetaData(mDetailFullData, mapList));
            dtvContentsDetailFragment.setOtherContentsDetailData(detailData);
            dtvContentsDetailFragment.noticeRefresh();
        }
        DTVTLogger.end();
    }

    /**
     * チャンネル情報のクリップボタン更新.
     * @param mapList クリップリスト(全件)
     */
    private void checkChannelClipStatus(final List<Map<String, String>> mapList) {
        DTVTLogger.start();
        DtvContentsChannelFragment dtvContentsChannelFragment = getChannelFragment();
        List<ContentsData> contentsDataList = dtvContentsChannelFragment.getContentsData();
        ContentsData contentsData;
        if (contentsDataList != null) {
            for (int i = 0; i < contentsDataList.size(); i++) {
                contentsData = contentsDataList.get(i);
                contentsDataList.get(i).setClipStatus(ClipUtils.setClipStatusContentsData(contentsData, mapList));
            }
            dtvContentsChannelFragment.setContentsData(contentsDataList);
            dtvContentsChannelFragment.setNotifyDataChanged();
        }
        DTVTLogger.end();
    }

    /**
     * テレビで視聴するボタン表示.
     * @param resourceId リソースID
     */
    private void showRemoteViewControl(final int resourceId) {
        createRemoteControllerView(true);
        mIsControllerVisible = true;
        mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), resourceId, null));
        setStartRemoteControllerUIListener(this);
    }

    /**
     * 検レコサーバのレスポンスチェック.
     */
    private void checkRecommendResponse() {
        int serviceId = mDetailData.getServiceId();
        contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.OTHER;
        if (ContentDetailUtils.getStbStatus() || mVisibility) { // STBに接続している 「テレビで視聴」が表示
            findViewById(R.id.remote_control_view).setVisibility(View.VISIBLE);
            switch (serviceId) {
                case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID:
                    showRemoteViewControl(R.drawable.remote_watch_by_tv_bottom_corner_d_anime);
                    break;
                case ContentUtils.DAZN_CONTENTS_SERVICE_ID:
                    RemoteControllerView mRemoteControllerView = getRemoteControllerView();
                    if (mRemoteControllerView != null) {
                        TextView mTextView = mRemoteControllerView.findViewById(R.id.watch_by_tv);
                        mTextView.setTextColor(ContextCompat.getColor(this, R.color.remote_watch_by_tv_bottom_dazn_text));
                        ImageView mTvIcon = mRemoteControllerView.findViewById(R.id.remote_tv_play_icon);
                        mTvIcon.setImageResource(R.mipmap.tv_black);
                        ImageView mTopIcon = mRemoteControllerView.findViewById(R.id.remote_controller_down);
                        mTopIcon.setImageResource(R.mipmap.arrow_top_black);
                    }
                    showRemoteViewControl(R.drawable.remote_watch_by_tv_bottom_corner_dazn);
                    break;
                case ContentUtils.DTV_CONTENTS_SERVICE_ID: // 「reserved1」が「1」STB視聴不可
                    if (!ContentDetailUtils.CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved1())) {
                        showRemoteViewControl(R.drawable.remote_watch_by_tv_bottom_corner_dtv);
                    }
                    break;
                case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                    showRemoteViewControl(R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari);
                    break;
                default:
                    break;
            }
        }
        //コンテンツタイプ取得
        ContentUtils.ContentsType type = mDetailData.getContentCategory();
        DTVTLogger.debug("display thumbnail contents type = " + type);
        mContentsType = type;
        switch (type) { //他サービスアプリスマホ連携表示
            case PURE_DTV:
                if (ContentDetailUtils.CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved1())
                        && ContentDetailUtils.CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved2())) {
                    // 「reserved1」が「1」STB視聴不可
                    // 「reserved2」が「1」Android視聴不可
                    // どちらも不可なので"お使いの端末では視聴できません"を表示
                    setThumbnailText(getString(R.string.contents_detail_thumbnail_text_unable_viewing));
                } else if (ContentDetailUtils.CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved2())) {
                    // 「reserved2」が「1」Android視聴不可
                    // モバイル視聴不可なので、"テレビで視聴できます"を表示(ペアリングは無関係)
                    setThumbnailText(getString(R.string.contents_detail_thumbnail_text));
                } else if (ContentDetailUtils.MOBILEVIEWINGFLG_FLAG_ZERO.equals(mDetailData.getMobileViewingFlg())) {
                    //「mobileViewingFlg」が「0」の場合モバイル視聴不可
                    //モバイル視聴不可なので、"テレビで視聴できます"を表示(ペアリングは無関係)
                    setThumbnailText(getString(R.string.contents_detail_thumbnail_text));
                } else {
                    //モバイル視聴可なので、"dTVで視聴"を表示
                    setThumbnailText(getString(R.string.dtv_content_service_start_text));
                }
                setThumbnailShadow();
                break;
            case PURE_DTV_CHANNEL:
            case PURE_DTV_CHANNEL_MISS:
            case PURE_DTV_CHANNEL_RELATION:
                setThumbnailText(getString(R.string.dtv_channel_service_start_text));
                setThumbnailShadow();
                if (!TextUtils.isEmpty(mDetailData.getChannelId())) {
                    mIsOtherServiceDtvChLoading = true;
                    getChannelInfo();
                }
                break;
            case D_ANIME_STORE:
                setThumbnailText(getString(R.string.d_anime_store_content_service_start_text));
                setThumbnailShadow();
                break;
            case DAZN:
                setThumbnailText(getString(R.string.dazn_content_service_start_text));
                setThumbnailShadow();
                break;
            default:
                break;
        }
        setTitleAndThumbnail(mDetailData.getTitle(), mDetailData.getThumb());
        String date = "";
        ContentUtils.ContentsType contentsType = ContentUtils.
                getContentsTypeByRecommend(mDetailData.getServiceId(), mDetailData.getCategoryId());
        if (contentsType == ContentUtils.ContentsType.TV) {
            if (serviceId == ContentUtils.DAZN_CONTENTS_SERVICE_ID) { //番組(m/d（曜日）h:ii)
                date = DateUtils.getContentsDateString(mDetailData.getmStartDate());
            } else { //番組(m/d（曜日）h:ii - h:ii)
                date = DateUtils.getContentsDateString(mDetailData.getmStartDate(), mDetailData.getmEndDate());
            }
            tabType = ContentDetailUtils.TabType.TV_ONLY;
            setContentsType(ContentUtils.ContentsType.TV);
            contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.TV;
        } else {
            if (contentsType == ContentUtils.ContentsType.VOD) {
                contentType = ContentDetailUtils.ContentTypeForGoogleAnalytics.VOD;
                if (DateUtils.isBefore(mDetailData.getmStartDate())) { //配信前 m/d（曜日）から
                    date = DateUtils.getContentsDateString(this, mDetailData.getmStartDate(), true);
                } else { //VOD(m/d（曜日）まで)
                    if (DateUtils.isIn31Day(mDetailData.getmEndDate())) {
                        date = DateUtils.getContentsDetailVodDate(this, mDetailData.getmEndDate());
                    }
                }
            }
            tabType = ContentDetailUtils.TabType.VOD;
        }
        mDetailData.setChannelDate(date);
        sendScreenViewForPosition(ContentDetailUtils.CONTENTS_DETAIL_INFO_TAB_POSITION);
    }

    @Override
    public void onStbMetaInfoGetDataProviderFinishOk(final ResultType<StbMetaInfoResponseData> resultType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StbMetaInfoResponseData stbMetaInfoResponseData = resultType.getResultType();
                if (stbMetaInfoResponseData.getTotalCount() == 1) { //取得成功
                    StbMetaInfoResponseData.Content content = stbMetaInfoResponseData.getContent();
                    DtvContentsDetailFragment detailFragment = getDetailFragment();
                    detailFragment.setRequestFinish(true);
                    OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
                    ContentDetailUtils.setContentsDetailData(content, detailData, mDetailData);
                    mDetailData = detailData;
                    checkRecommendResponse();
                    if (tabType != ContentDetailUtils.TabType.TV_CH) {
                        createViewPagerAdapter();
                    }
                    if (!mIsOtherServiceDtvChLoading) {
                        detailFragment.noticeRefresh();
                    }
                } else { //0件
                    showErrorDialog(ContentDetailUtils.ErrorType.recommendDetailGet);
                }
                sendOperateLog();
                if (!mIsOtherServiceDtvChLoading) {
                    showProgressBar(false);
                }
            }
        });
    }

    @Override
    public void onStbMetaInfoGetDataProviderFinishNg(final ResultType<SearchResultError> resultType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() { //取得失敗
                showErrorDialog(ContentDetailUtils.ErrorType.recommendDetailGet);
            }
        });
    }

    @Override
    public void onUserVisibleHint(final boolean isVisibleToUser, final DtvContentsDetailFragment dtvContentsDetailFragment) {
        if (!isVisibleToUser || dtvContentsDetailFragment.isRequestFinish()) {
            return;
        }
        if (mIsOtherService) {
            getContentDetailInfoFromSearchServer();
        } else {
            getContentDetailDataFromPlala();
        }
    }

    @Override
    public void showClipToast(final int msgId) {
        super.showClipToast(msgId);
        //クリップ処理が終わった時点で、コンテンツ詳細、チャンネルリストのクリップ状態を更新する
        checkClipStatus(ContentDetailUtils.CLIP_BUTTON_ALL_UPDATE);
    }

    @Override
    public void clipKeyResult() {
        //Nop 仕様により実装のみ
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && mPlayerViewLayout != null) {
                mPlayerViewLayout.tapBackKey();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}