/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.detail;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
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
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbSelectActivity;
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
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;
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
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopSearchDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopSendOperateLog;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopThumbnailConnect;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsChannelFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaContentMultiChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.struct.CalendarComparator;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
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
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchContentInfo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

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
        SearchDataProvider.SearchDataProviderListener {

    /** エラータイプ.*/
    private enum ErrorType {
        /** コンテンツ詳細取得.*/
        contentDetailGet,
        /** スタッフリスト取得.*/
        roleListGet,
        /** レンタルチャンネル取得.*/
        rentalChannelListGet,
        /** レンタルVod取得.*/
        rentalVoidListGet,
        /** チャンネルリスト取得.*/
        channelListGet,
        /** 番組データ取得.*/
        tvScheduleListGet,
    }
    // region variable
    /** アスペクト比(16:9)の16.*/
    private static final int SCREEN_RATIO_WIDTH_16 = 16;
    /** アスペクト比(16:9)の9.*/
    private static final int SCREEN_RATIO_HEIGHT_9 = 9;
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
    /** 検索データプロバイダー .*/
    private SearchDataProvider mSearchDataProvider = null;
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
    /** ビューページャアダプター.*/
    private ContentsDetailPagerAdapter mContentsDetailPagerAdapter;
    /**購入済みVODレスポンス.*/
    private PurchasedVodListResponse mPurchasedVodListResponse = null;
    /** タブー名.*/
    private String[] mTabNames = null;
    /**表示状態.*/
    private int mDisplayState = 0;
    /**コンテンツ詳細のみ.*/
    private final static int CONTENTS_DETAIL_ONLY = 0;
    /**プレイヤーのみ.*/
    private final static int PLAYER_ONLY = 1;
    /**プレイヤーとコンテンツ詳細.*/
    private final static int PLAYER_AND_CONTENTS_DETAIL = 2;
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
    /**コンテンツ詳細予約済みID.*/
    public static final String CONTENTS_DETAIL_RESERVEDID = "1";
    /**モバイル視聴不可.*/
    private static final String MOBILEVIEWINGFLG_FLAG_ZERO = "0";
    /** 日付インディーズ.*/
    private int mDateIndex = 0;
    /** 日付リスト.*/
    private String[] mDateList = null;
    /**DTVバージョン.*/
    private static final int DTV_VERSION_STANDARD = 52000;
    /**レスポンス(1).*/
    private static final String METARESPONSE1 = "1";
    /**レスポンス(2).*/
    private static final String METARESPONSE2 = "2";
    /**レスポンス(3).*/
    private static final String METARESPONSE3 = "3";
    /**予約済みタイプ(4).*/
    private static final String RESERVED4_TYPE4 = "4";
    /**予約済みタイプ(7).*/
    private static final String RESERVED4_TYPE7 = "7";
    /**予約済みタイプ(8).*/
    private static final String RESERVED4_TYPE8 = "8";
    /**DTVパッケージ名.*/
    private static final String DTV_PACKAGE_NAME = "jp.co.nttdocomo.dtv";

    /**dアニメストアパッケージ名.*/
    private static final String DANIMESTORE_PACKAGE_NAME = "com.nttdocomo.android.danimeapp";
    /**dアニメストアバージョン.*/
    private static final int DANIMESTORE_VERSION_STANDARD = 132;

    /**dTVチャンネルパッケージ名.*/
    private static final String DTVCHANNEL_PACKAGE_NAME = "com.nttdocomo.dch";
    /**dTVチャンネルバージョン.*/
    private static final int DTVCHANNEL_VERSION_STANDARD = 15;
    /**dTVチャンネルカテゴリー放送.*/
    private static final String DTV_CHANNEL_CATEGORY_BROADCAST = "01";
    /**dTVチャンネルカテゴリー見逃し.*/
    private static final String DTV_CHANNEL_CATEGORY_MISSED = "02";
    /**dTVチャンネルカテゴリー関連.*/
    private static final String DTV_CHANNEL_CATEGORY_RELATION = "03";
    /** 作品IDの長さ.*/
    private static final int CONTENTS_ID_VALID_LENGTH = 8;

    /**他サービス起動リクエストコード.*/
    private static final int START_APPLICATION_REQUEST_CODE = 0;

    /** bvflg(1).*/
    private static final String BVFLG_FLAG_ONE = "1";
    /** bvflg(0).*/
    private static final String BVFLG_FLAG_ZERO = "0";
    /** flg(0).*/
    private static final int FLAG_ZERO = 0;
    /** 16進数から10進数への変換時の指定値. */
    private static final int SOURCE_HEXADECIMAL = 16;
    /** サービスIDをひかりTV用のチャンネル番号に変換する際の倍率. */
    private static final int CONVERT_SEARVICE_ID_TO_CHANNEL_NUMBER = 10;
    /** 画面すべてのクリップボタンを更新.*/
    private static final int CLIP_BUTTON_ALL_UPDATE = 0;
    /** チャンネルリストのクリップボタンをのみを更新.*/
    private static final int CLIP_BUTTON_CHANNEL_UPDATE = 1;
    /** 番組詳細 or 作品情報タブ.*/
    private static final int CONTENTS_DETAIL_INFO_TAB_POSITION = 0;
    /** チャンネルタブ.*/
    private static final int CONTENTS_DETAIL_CHANNEL_TAB_POSITION = 1;

    /* player start */
    /**FrameLayout.*/
    private FrameLayout mFrameLayout = null;
    /**録画予約コンテンツ詳細情報.*/
    private RecordingReservationContentsDetailInfo mRecordingReservationContentsDetailInfo = null;
    /**録画予約ダイアログ.*/
    private CustomDialog mRecordingReservationCustomtDialog = null;

    /*private static final int RECORDING_RESERVATION_DIALOG_INDEX_0 = 0; // 予約録画する
    private static final int RECORDING_RESERVATION_DIALOG_INDEX_1 = 1; // キャンセル*/
    /** 他サービスフラグ.*/
    private boolean mIsOtherService = false;
    /** titleKind取得フラグ.*/
    private boolean mIsTitleKind = false;
    /** 対象コンテンツのチャンネルデータ.*/
    private ChannelInfo mChannel = null;
    /** 視聴可能期限.*/
    private long mEndDate = 0L;
    /** Vod視聴可能期限.*/
    private long mVodEndDate = 0L;
    /** Vod視聴可能期限文字列.*/
    private String mVodEndDateText = null;
    /** 一ヶ月(30日).*/
    public static final int ONE_MONTH = 30;
    /** サムネイルにかけるシャドウのアルファ値.*/
    private static final float THUMBNAIL_SHADOW_ALPHA = 0.5f;
    /** 操作履歴送信.*/
    private SendOperateLog mSendOperateLog = null;
    /** 二回目リモコン送信防止.*/
    private boolean mIsSend = false;
    /** ヘッダーチェック.*/
    private boolean mIsFromHeader = false;
    /** チャンネル日付.*/
    private String mChannelDate = null;
    /** サービスID(ぷらら).*/
    private String mServiceId = null;
    /** 視聴可否ステータス.*/
    private ContentUtils.ViewIngType mViewIngType = null;
    /** コンテンツ種別.*/
    private ContentUtils.ContentsType mContentsType = null;
    /** プレイヤーレイアウト.*/
    private PlayerViewLayout mPlayerViewLayout;
    /** プレイヤー前回のポジション.*/
    private static final String SAVEDVARIABLE_PLAY_START_POSITION = "playStartPosition";
    /** プレイヤー前回のポジション.*/
    private int mPlayStartPosition;
    /** 再生停止フラグ.*/
    private boolean mIsPlayerPaused = false;
    /** 前回リモートコントローラービュー表示フラグ.*/
    private boolean mVisibility = false;
    /** 前回リモートコントローラービュー表示フラグ.*/
    private static final String REMOTE_CONTROLLER_VIEW_VISIBILITY = "visibility";
    /** ひかり放送中光コンテンツ再生失敗時にリトライを行うエラーコードの開始値.*/
    private static final int RETRY_ERROR_START = 2000;
    /** ディスプレイ幅.*/
    private int mWidth;
    /** ディスプレイ高さ.*/
    private int mHeight;
    /** ナビゲーションバー含むディスプレイ高さ.*/
    private int mScreenNavHeight;
    /* player end */
    /** ハンドラー.*/
    private final Handler loadHandler = new Handler();
    /** titleKindハンドラー.*/
    private Handler mTitleKindHandler = null;
    /** titleKind Runnable.*/
    private Runnable mTitleKindRunnable = null;
    /** チャンネルリストフラグメント.*/
    private DtvContentsChannelFragment mChannelFragment = null;
    /** 再生用データ.*/
    private RecordedContentsDetailData mPlayerData;
    /** 前回ViewPagerのタブ位置.*/
    private int mViewPagerIndex = DEFAULT_TAB_INDEX;
    /** 前回ViewPagerのタブ位置.*/
    private static final int DEFAULT_TAB_INDEX = -1;
    /** 前回ViewPagerのタブ位置.*/
    private static final String VIEWPAGER_INDEX = "viewPagerIndex";
    /** コンテンツ種別1のコンテンツ種別名のひかりTVタイプ.*/
    private ContentUtils.HikariType mHikariType = null;
    /** 放送視聴可否.*/
    private boolean mIsH4dPlayer = false;

    /** コンテンツタイプ(Google Analytics用).*/
    private enum ContentTypeForGoogleAnalytics {
        /** テレビ.*/
        TV,
        /** ビデオ.*/
        VOD,
        /** その他.*/
        OTHER
    }

    /** コンテンツタイプ(Google Analytics用).*/
    private ContentTypeForGoogleAnalytics contentType = null;
    // endregion

    //region Activity LifeCycle
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPlayStartPosition = savedInstanceState
                    .getInt(SAVEDVARIABLE_PLAY_START_POSITION);
            mVisibility = savedInstanceState.getBoolean(REMOTE_CONTROLLER_VIEW_VISIBILITY);
            mViewPagerIndex = savedInstanceState.getInt(VIEWPAGER_INDEX);
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
            case PLAYER_ONLY:
                if (mIsFromBgFlg) {
                    super.sendScreenView(getString(R.string.google_analytics_screen_name_player),
                            ContentUtils.getParingAndLoginCustomDimensions(ContentDetailActivity.this));
                }
                break;
            case PLAYER_AND_CONTENTS_DETAIL:
                if (mIsFromBgFlg) {
                    String screenName = null;
                    if (mIsH4dPlayer) {
                        screenName = getString(R.string.google_analytics_screen_name_player);
                    } else {
                        if (mViewPager != null) {
                            String tabName = mTabNames[mViewPager.getCurrentItem()];
                            screenName = getScreenNameMap().get(tabName);
                        }
                    }
                    if (screenName != null) {
                        super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(ContentDetailActivity.this));
                    }
                }
                checkOnResumeClipStatus();
                break;
            case CONTENTS_DETAIL_ONLY:
                if (mIsFromBgFlg && contentType != null && mViewPager != null) {
                    String tabName = mTabNames[mViewPager.getCurrentItem()];
                    String screenName = getScreenNameMap().get(tabName);
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
                sendScreenViewForPosition(CONTENTS_DETAIL_INFO_TAB_POSITION);
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
                        if (mPlayerViewLayout.initSecurePlayer(mPlayStartPosition)) {
                            showPlayerView();
                        }
                    } else {
                        setRemotePlayArrow(mPlayerData);
                    }
                }
            } else {
                if (mPlayerViewLayout.initSecurePlayer(mPlayStartPosition)) {
                    showPlayerView();
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
            checkClipStatus(CLIP_BUTTON_ALL_UPDATE);
        } else {
            mIsFirstDisplay = false;
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPlayerViewLayout != null) {
            if (mPlayerViewLayout.getCurrentPosition() < 0) {
                mPlayerViewLayout.setPlayStartPosition(0);
            }
            outState.putInt(SAVEDVARIABLE_PLAY_START_POSITION, mPlayerViewLayout.getCurrentPosition());
        }
        outState.putBoolean(REMOTE_CONTROLLER_VIEW_VISIBILITY, mIsControllerVisible);
        if (mViewPager != null) {
            outState.putInt(VIEWPAGER_INDEX, mViewPager.getCurrentItem());
        }
    }

    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayerViewLayout != null) {
            mPlayStartPosition = mPlayerViewLayout.onPause();
            mIsPlayerPaused = true;
        }
        if (mTitleKindHandler != null) {
            mTitleKindHandler.removeCallbacks(mTitleKindRunnable);
        }
        DtvContentsChannelFragment channelFragment;
        switch (mDisplayState) {
            case PLAYER_ONLY:
                break;
            case PLAYER_AND_CONTENTS_DETAIL:
                //通信を止める
                if (mContentsDetailDataProvider != null) {
                    StopContentDetailDataConnect stopContentDetailDataConnect = new StopContentDetailDataConnect();
                    stopContentDetailDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mContentsDetailDataProvider);
                }
                if (mScaledDownProgramListDataProvider != null) {
                    StopScaledProListDataConnect stopScaledProListDataConnect = new StopScaledProListDataConnect();
                    stopScaledProListDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mScaledDownProgramListDataProvider);
                }
                if (mSendOperateLog != null) {
                    StopSendOperateLog stopSendOperateLog = new StopSendOperateLog();
                    stopSendOperateLog.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSendOperateLog);
                }
                stopThumbnailConnect();
                //FragmentにContentsAdapterの通信を止めるように通知する
                channelFragment = getChannelFragment();
                if (channelFragment != null) {
                    channelFragment.stopContentsAdapterCommunication();
                }
                break;
            case CONTENTS_DETAIL_ONLY:
                //通信を止める
                if (mContentsDetailDataProvider != null) {
                    StopContentDetailDataConnect stopContentDetailDataConnect = new StopContentDetailDataConnect();
                    stopContentDetailDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mContentsDetailDataProvider);
                }
                if (mScaledDownProgramListDataProvider != null) {
                    StopScaledProListDataConnect stopScaledProListDataConnect = new StopScaledProListDataConnect();
                    stopScaledProListDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mScaledDownProgramListDataProvider);
                }
                if (mSearchDataProvider != null) {
                    StopSearchDataConnect stopSearchDataConnect = new StopSearchDataConnect();
                    stopSearchDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSearchDataProvider);
                }
                if (mSendOperateLog != null) {
                    StopSendOperateLog stopSendOperateLog = new StopSendOperateLog();
                    stopSendOperateLog.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSendOperateLog);
                }
                stopThumbnailConnect();
                //FragmentにContentsAdapterの通信を止めるように通知する
                channelFragment = getChannelFragment();
                if (channelFragment != null) {
                    channelFragment.stopContentsAdapterCommunication();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //外部出力制御
        if (mPlayerViewLayout != null) {
            mPlayerViewLayout.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        //外部出力制御
        if (mPlayerViewLayout != null) {
            mPlayerViewLayout.onDestory();
        }
        super.onDestroy();
    }
    //endregion

    //region BaseActivity
    @SuppressWarnings("OverlyComplexMethod")
    @Override
    public void onStartCommunication() {
        DTVTLogger.start();
        super.onStartCommunication();
        DtvContentsChannelFragment channelFragment = null;
        switch (mDisplayState) {
            case PLAYER_ONLY:
                if (mPlayerViewLayout != null) {
                    mPlayerViewLayout.enableThumbnailConnect();
                }
                break;
            case PLAYER_AND_CONTENTS_DETAIL:
                if (mPlayerViewLayout != null) {
                    mPlayerViewLayout.enableThumbnailConnect();
                }
                if (mContentsDetailDataProvider != null) {
                    mContentsDetailDataProvider.enableConnect();
                }
                if (mScaledDownProgramListDataProvider != null) {
                    mScaledDownProgramListDataProvider.enableConnect();
                }
                if (mSearchDataProvider != null) {
                    mSearchDataProvider.enableConnect();
                }
                if (mSendOperateLog != null) {
                    mSendOperateLog.enableConnection();
                }
                enableThumbnailConnect();
                //FragmentにContentsAdapterの通信を止めるように通知する
                channelFragment = getChannelFragment();
                if (channelFragment != null) {
                    channelFragment.enableContentsAdapterCommunication();
                }
                break;
            case CONTENTS_DETAIL_ONLY:
                if (mContentsDetailDataProvider != null) {
                    mContentsDetailDataProvider.enableConnect();
                }
                if (mScaledDownProgramListDataProvider != null) {
                    mScaledDownProgramListDataProvider.enableConnect();
                }
                if (mSendOperateLog != null) {
                    mSendOperateLog.enableConnection();
                }
                enableThumbnailConnect();
                //FragmentにContentsAdapterの通信を止めるように通知する
                channelFragment = getChannelFragment();
                if (channelFragment != null) {
                    channelFragment.enableContentsAdapterCommunication();
                }
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }
    //endregion

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

    //region private method
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
                getWidthDensity(), getWidthDensity() / SCREEN_RATIO_WIDTH_16 * SCREEN_RATIO_HEIGHT_9);
        mThumbnail.setLayoutParams(layoutParams);
        mThumbnailRelativeLayout = findViewById(R.id.dtv_contents_detail_layout);
        mContractLeadingView = findViewById(R.id.contract_leading_view);
        Object object = mIntent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
        if (object instanceof RecordedContentsDetailData) { //プレイヤーで再生できるコンテンツ
            mDisplayState = PLAYER_ONLY;
            RecordedContentsDetailData playerData = mIntent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
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
            String contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_live);
            if (!playerData.isIsLive()) {
                contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_record);
            }
            String serviceName = getString(R.string.google_analytics_custom_dimension_service_h4d);
            String contentsType1 = getString(R.string.google_analytics_custom_dimension_contents_type1_h4d);
            super.sendScreenView(getString(R.string.google_analytics_screen_name_player),
                    ContentUtils.getCustomDimensions(null, serviceName, contentsType1, contentsType2, playerData.getTitle()));
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
        setStatusBarColor(false);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        initContentsView();
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setTabVisibility(false);
        }
    }
    //endregion

    /**
     * コンテンツ詳細データ取得.
     */
    private void getScheduleDetailData() {
        mContentsDetailDataProvider = new ContentsDetailDataProvider(this);
        String[] cRid;
        if (mDetailData != null) {
            DTVTLogger.debug("contentId:" + mDetailData.getContentsId());
            cRid = new String[1];
            cRid[cRid.length - 1] = mDetailData.getContentsId();
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(this);
            int ageReq = userInfoDataProvider.getUserAge();
            mContentsDetailDataProvider.getContentsDetailData(cRid, "", ageReq);
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
     *
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
     *
     * @param channelInfo チャンネル情報
     */
    private void getChannelDetailData(final ChannelInfo channelInfo) {
        DTVTLogger.start();
        DtvContentsChannelFragment channelFragment = getChannelFragment();
        channelFragment.setLoadInit();
        if (channelInfo != null) {
            channelFragment.setChannelDataChanged(channelInfo);
            mDateIndex = 0;
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
        int[] channelNos = new int[]{mChannel.getChannelNo()};
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
                if (calendar.getTime().compareTo(date) == -1) {
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
                mDateIndex++;
            } catch (ParseException e) {
                channelLoadCompleted();
                DTVTLogger.debug(e);
            }
            if (channelNos.length == 0 || channelNos[0] == -1) {
                DTVTLogger.error("No channel number");
                channelLoadCompleted();
                return;
            }
            mScaledDownProgramListDataProvider.getProgram(channelNos, mDateList, false);
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
    //region ChangedScrollLoadListener
    @Override
    public void onChannelLoadMore() {
        getChannelDetailByPageNo();
    }

    @Override
    public void onUserVisibleHint() {
        loadHandler.removeCallbacks(loadRunnable);
    }
    //endregion

    /**
     * サムネイルエリア文字表示.
     *
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
                if (mDetailFullData != null
                        && mDetailFullData.getContentsType().equals(ContentUtils.ContentsType.HIKARI_TV_VOD)) {

                    imageView.setVisibility(View.GONE);

                    // 連携アイコン非表示のためクリック抑止
                    mThumbnailBtn.setClickable(false);
                } else if (content.equals(getResources().getString(R.string.contents_detail_thumbnail_text_unable_viewing))
                        || content.equals(getResources().getString(R.string.contents_detail_thumbnail_text))) {
                    imageView.setVisibility(View.GONE);

                    // 連携アイコン非表示のためクリック抑止
                    mThumbnailBtn.setClickable(false);
                }
                mThumbnailBtn.setVisibility(View.VISIBLE);
                setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
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
        setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
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
     *
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
                mThumbnail.setTag(url);
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
        mContentsDetailFragment = getDetailFragment();
        mFrameLayout = findViewById(R.id.header_watch_by_tv);
        // タブ数を先に決定するため、コンテンツ詳細のデータを最初に取得しておく
        mDetailData = mIntent.getParcelableExtra(ContentUtils.RECOMMEND_INFO_BUNDLE_KEY);
        if (mDetailData == null) {
            mDetailData = mIntent.getParcelableExtra(ContentUtils.SEARCH_INFO_BUNDLE_KEY);
        }
        if (mDetailData != null) {
            int serviceId = mDetailData.getServiceId();
            if (ContentUtils.isOtherService(serviceId)) {
                // 他サービス(dtv/dtvチャンネル/dアニメ)フラグを立てる
                mIsOtherService = true;
                contentType = ContentTypeForGoogleAnalytics.OTHER;
            }
            // STBに接続している　「テレビで視聴」が表示
            if (getStbStatus() || mVisibility) {
                if (mIsOtherService) {
                    switch (serviceId) {
                        case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID:
                            // リモコンUIのリスナーを設定
                            createRemoteControllerView(true);
                            mIsControllerVisible = true;
                            mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.remote_watch_by_tv_bottom_corner_d_anime, null));
                            setStartRemoteControllerUIListener(this);
                            break;
                        case ContentUtils.DTV_CONTENTS_SERVICE_ID: //「serviceId」が「15」(dTVコンテンツ)の場合
                            // 「reserved1」が「1」STB視聴不可
                            if (!CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved1())) {
                                createRemoteControllerView(true);
                                mIsControllerVisible = true;
                                mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                                        R.drawable.remote_watch_by_tv_bottom_corner_dtv, null));
                                setStartRemoteControllerUIListener(this);
                            }
                            break;
                        case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                            createRemoteControllerView(true);
                            mIsControllerVisible = true;
                            mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
                            setStartRemoteControllerUIListener(this);
                            break;
                        default:
                            break;
                    }
                } else {
                    createRemoteControllerView(true);
                    mIsControllerVisible = true;
                }
            }

            //コンテンツタイプ取得
            ContentUtils.ContentsType type = mDetailData.getContentCategory();

            DTVTLogger.debug("display thumbnail contents type = " + type);
            DTVTLogger.debug("display thumbnail viewing type = recommend always enable");
            mContentsType = type;
            //他サービスアプリスマホ連携表示
            switch (type) {
                case PURE_DTV:
                    if (CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved1())
                            && CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved2())) {
                        // 「reserved1」が「1」STB視聴不可
                        // 「reserved2」が「1」Android視聴不可
                        // どちらも不可なので"お使いの端末では視聴できません"を表示
                        setThumbnailText(getResources().getString(
                                R.string.contents_detail_thumbnail_text_unable_viewing));
                    } else if (CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved2())) {
                        // 「reserved2」が「1」Android視聴不可
                        // モバイル視聴不可なので、"テレビで視聴できます"を表示(ペアリングは無関係)
                        setThumbnailText(getResources().getString(
                                R.string.contents_detail_thumbnail_text));
                    } else if (MOBILEVIEWINGFLG_FLAG_ZERO.equals(mDetailData.getMobileViewingFlg())) {
                        //「mobileViewingFlg」が「0」の場合モバイル視聴不可
                        //モバイル視聴不可なので、"テレビで視聴できます"を表示(ペアリングは無関係)
                        setThumbnailText(getResources().getString(
                                R.string.contents_detail_thumbnail_text));
                    } else {
                        //モバイル視聴可なので、"dTVで視聴"を表示
                        setThumbnailText(getResources().getString(
                                R.string.dtv_content_service_start_text));
                    }
                    setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
                    break;
                case PURE_DTV_CHANNEL:
                case PURE_DTV_CHANNEL_MISS:
                case PURE_DTV_CHANNEL_RELATION:
                    // "dTVチャンネルで視聴"
                    setThumbnailText(getResources().getString(R.string.dtv_channel_service_start_text));
                    setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
                    break;
                case D_ANIME_STORE:
                    setThumbnailDelay(getResources().getString(R.string.d_anime_store_content_service_start_text));
                    setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
                    break;
                default:
                    // その他はひかりコンテンツ.ここでは何も表示させない
                    break;
            }
            if (mIsOtherService) {
                // 他サービスはここでサムネイルとタイトルを表示しておく
                setTitleAndThumbnail(mDetailData.getTitle(), mDetailData.getThumb());
            }
        } else {  //plalaサーバーから
            mDetailData = mIntent.getParcelableExtra(ContentUtils.PLALA_INFO_BUNDLE_KEY);
            if (getStbStatus() || mVisibility) {
                createRemoteControllerView(true);
                mIsControllerVisible = true;
            }
        }
        if (mIsOtherService) {
            String date = "";
            ContentUtils.ContentsType contentsType = ContentUtils.
                    getContentsTypeByRecommend(mDetailData.getServiceId(), mDetailData.getCategoryId());
            //日付表示は一覧系画面と同じように判定する
            if (contentsType == ContentUtils.ContentsType.TV) {
                //番組(m/d（曜日）h:ii - h:ii)
                date = DateUtils.getContentsDateString(mDetailData.getmStartDate(), mDetailData.getmEndDate());
                // コンテンツ詳細(TVの場合、タブ一つに設定する)
                mTabNames = getResources().getStringArray(R.array.contents_detail_tab_other_service_tv);
                setContentsType(ContentUtils.ContentsType.TV);
                contentType = ContentTypeForGoogleAnalytics.TV;
            } else {
                if (contentsType == ContentUtils.ContentsType.VOD) {
                    contentType = ContentTypeForGoogleAnalytics.VOD;
                    if (DateUtils.isBefore(mDetailData.getmStartDate())) {
                        //配信前 m/d（曜日）から
                        date = DateUtils.getContentsDateString(this, mDetailData.getmStartDate(), true);
                    } else {
                        //VOD(m/d（曜日）まで)
                        if (DateUtils.isIn31Day(mDetailData.getmEndDate())) {
                            date = DateUtils.getContentsDetailVodDate(this, mDetailData.getmEndDate());
                        }
                    }
                }
                // コンテンツ詳細(VODの場合、タブ一つに設定する)
                mTabNames = getResources().getStringArray(R.array.contents_detail_tab_other_service_vod);
            }
            mDetailData.setChannelDate(date);
        } else {
            // ディフォルトはチャンネルタブを付いて、コールバック来たら、再設定
            mTabNames = getResources().getStringArray(R.array.contents_detail_tabs_tv_ch);
        }

        mContentsDetailPagerAdapter
                = new ContentsDetailPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mContentsDetailPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                sendScreenViewForPosition(position);
                if (position == 1) {
                    getChannelFragment().initLoad();
                    showChannelProgressBar(true);
                    loadHandler.post(loadRunnable);
                } else {
                    //詳細タブで既に詳細データ取得が完了していればリクエストしない
                    if (mDetailFullData == null) {
                        loadHandler.post(loadRunnable);
                    }
                }
            }
        });
        //レコメンド（serviceId 44）若しくはぷららの場合
        if (!mIsOtherService) {
            findViewById(R.id.remote_control_view).setVisibility(View.INVISIBLE);
            getScheduleDetailData();
        } else {
            sendOperateLog();
            if (!mDetailData.getIsTranslateFromSearchFlag()) {
                showProgressBar(true);
                getContentDetailInfoFromSearchServer();
            } else {
                showProgressBar(false);
            }
        }
        DTVTLogger.end();
    }

    /**
     * titleKind(作品種別) が取得できるまでサムネイル表示を待つ処理.
     *
     * @param thumbnailText サムネイル表示文言
     */
    private void setThumbnailDelay(final String thumbnailText) {
        if (mDetailData != null) {
            mTitleKindHandler = new Handler();
            mTitleKindRunnable = new Runnable() {
                @Override
                public void run() {
                    //検索画面からの遷移ではtitleKind取得済み
                    //おすすめ画面からの遷移ではあらすじ取得と一緒にtitleKind取得
                    if (mDetailData.getIsTranslateFromSearchFlag() || mIsTitleKind) {
                        DTVTLogger.debug("d anime store setThumbnailText : titleKind = " + mDetailData.getTitleKind());
                        setThumbnailText(thumbnailText);
                        return;
                    }
                    mTitleKindHandler.postDelayed(this, 300);
                }
            };
            mTitleKindHandler.post(mTitleKindRunnable);
        }
    }

    /**
     * データ取得用Runnable.
     */
    private final Runnable loadRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager.getCurrentItem() == CONTENTS_DETAIL_INFO_TAB_POSITION) {
                getScheduleDetailData();
            } else {
                getChannelDetailData(mChannel);
            }
        }
    };

    /**
     * ナビゲーションバーが表示されているか.
     *
     * @param isHeight 端末が縦向きかどうか
     * @return true:表示されている false:表示されていない
     */
    private boolean isNavigationBarShow(final boolean isHeight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            if (isHeight) {
                return realSize.y != size.y;
            } else {
                return realSize.x != size.x;
            }
        } else {
            boolean menu = ViewConfiguration.get(this).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

    /**
     * ナビゲーションバーの高さを取得.
     *
     * @param isHeight ナビゲーションバーを表示するかどうか
     * @return 高さ.
     */
    private int getNavigationBarHeight(final boolean isHeight) {
        if (!isNavigationBarShow(isHeight)) {
            return 0;
        }
        int resourceId;
        if (isHeight) {
            resourceId = getResources().getIdentifier("navigation_bar_height",
                    "dimen", "android");
        } else {
            resourceId = getResources().getIdentifier("navigation_bar_width",
                    "dimen", "android");
        }
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
     *
     * @return Height
     */
    private int getScreenHeight() {
        initDisplayMetrics();
        return getHeightDensity() + getNavigationBarHeight(true);
    }

    /**
     * 詳細Viewの初期化.
     */
    private void initContentsView() {
        switch (mDisplayState) {
            case PLAYER_AND_CONTENTS_DETAIL:
                mViewPager = findViewById(R.id.dtv_contents_detail_main_layout_vp);
                initContentData();
                initTab();
                break;
            case CONTENTS_DETAIL_ONLY:
                mViewPager = findViewById(R.id.dtv_contents_detail_main_layout_vp);
                initContentData();
                initTab();
                break;
            case PLAYER_ONLY:
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
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
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
            screenName = getScreenNameMap().get(tabName);
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
            if (!mIsOtherService) {
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
     * スクリーン名マップを取得する.
     * @return スクリーン名マップ
     */
    private HashMap<String, String> getScreenNameMap() {
        HashMap<String, String> screenNameMap = new HashMap<>();
        screenNameMap.put(getString(R.string.contents_detail_tab_contents_info), getString(R.string.google_analytics_screen_name_content_detail_h4d_vod_program_detail));

        if (contentType == ContentTypeForGoogleAnalytics.VOD) {
            screenNameMap.put(getString(R.string.contents_detail_tab_program_detail), getString(
                    R.string.google_analytics_screen_name_content_detail_h4d_vod_program_detail));
        } else {
            screenNameMap.put(getString(R.string.contents_detail_tab_program_detail), getString(
                    R.string.google_analytics_screen_name_content_detail_h4d_broadcast_program_detail));
        }
        screenNameMap.put(getString(R.string.contents_detail_tab_channel), getString(
                R.string.google_analytics_screen_name_content_detail_h4d_broadcast_channel));
        screenNameMap.put(getString(R.string.contents_detail_tab_episode), getString(R.string.google_analytics_screen_name_content_detail_h4d_vod_episode));

        return screenNameMap;
    }

    /**
     * コンテンツ詳細用ページャアダプター.
     */
    private class ContentsDetailPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * コンストラクタ.
         *
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
     *
     * @param detailFragment フラグメント
     */
    private void setRecordingData(final DtvContentsDetailFragment detailFragment) {
        // リモート録画予約情報を生成
        mRecordingReservationContentsDetailInfo = new RecordingReservationContentsDetailInfo(
                mDetailFullData.getmService_id(),
                mDetailFullData.getTitle(),
                mDetailFullData.getPublish_start_date(),
                mDetailFullData.getDur(),
                mDetailFullData.getR_value());
        mRecordingReservationContentsDetailInfo.setEventId(mDetailFullData.getmEvent_id());
        detailFragment.setRecordingReservationIconListener(this);
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

        if (mSearchDataProvider == null) {
            mSearchDataProvider = new SearchDataProvider();
        }

        if (mDetailData != null) {
            mSearchDataProvider.getContentDetailInfo(mDetailData.getContentsId(), String.valueOf(mDetailData.getServiceId()), this);
        }

    }

    /**
     * ビューページャの再設定.
     */
    private void setViewPagerTab() {
        if (mTabNames == null || mTabNames.length == 2) {
            mTabNames = getResources().getStringArray(R.array.contents_detail_tab_vod);
            mTabLayout.resetTabView(mTabNames);
            mFragmentFactory.delFragment();
            mViewPager.addOnPageChangeListener(null);
            mContentsDetailPagerAdapter.notifyDataSetChanged();
        }
    }
    //region ContentsDetailDataProvider.ApiDataProviderCallback
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod", "EnumSwitchStatementWhichMissesCases"})
    @Override
    public void onContentsDetailInfoCallback(final VodMetaFullData contentsDetailInfo, final boolean clipStatus) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DTVTLogger.start();
                if (isFinishing()) {
                    //既に終了していた場合は、以後の処理はスキップ
                    DTVTLogger.debug("already finising");
                    return;
                }

                //DBに保存されているUserInfoから契約情報を確認する
                String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(ContentDetailActivity.this));
                DTVTLogger.debug("contractInfo: " + contractInfo);

                //詳細情報取得して、更新する
                if (contentsDetailInfo != null) {
                    DtvContentsDetailFragment detailFragment = getDetailFragment();
                    mDetailFullData = contentsDetailInfo;
                    //メタデータ取得時にコンテンツ種別を取得する
                    mContentsType = ContentUtils.getHikariContentsType(mDetailFullData);
                    //DBに保存されているUserInfoから契約情報を確認する
                    contentType = ContentTypeForGoogleAnalytics.TV;
                    if (ContentUtils.TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
                        //tv_programの場合
                        if (ContentUtils.TV_SERVICE_FLAG_HIKARI.equals(mDetailFullData.getmTv_service())) {
                            //tv_serviceは1の場合(ひかりTV多ch)
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
                                    contentType = ContentTypeForGoogleAnalytics.VOD;
                                }
                            } else if (ContentUtils.CONTENT_TYPE_FLAG_ZERO.equals(mDetailFullData.getmContent_type())) {
                                // contents_typeが0は番組扱い
                                setRecordingData(detailFragment);
                            } else if (ContentUtils.CONTENT_TYPE_FLAG_THREE.equals(mDetailFullData.getmContent_type())) {
                                // contents_typeが3は関連VOD
                                contentType = ContentTypeForGoogleAnalytics.VOD;
                            } else {
                                // contents_typeがその他(異常値)はVOD扱いしておく
                                DTVTLogger.debug("contents_type value Error!" + mDetailFullData.getmContent_type());
                                contentType = ContentTypeForGoogleAnalytics.VOD;
                            }
                        } else {
                            //tv_serviceがその他(異常値)はVOD扱いしておく
                            DTVTLogger.debug("tv_service value Error!" + mDetailFullData.getmTv_service());
                            setRecordingData(detailFragment);
                        }
                    } else {
                        // disp_typeがtv_program以外なら一律VOD
                        contentType = ContentTypeForGoogleAnalytics.VOD;
                    }
                    String screenName;
                    String contentsType2;
                    if (contentType == ContentTypeForGoogleAnalytics.VOD) {
                        screenName = getString(R.string.google_analytics_screen_name_content_detail_h4d_vod_program_detail);
                        contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_void);
                    } else {
                        screenName = getString(R.string.google_analytics_screen_name_content_detail_h4d_broadcast_program_detail);
                        contentsType2 = getString(R.string.google_analytics_custom_dimension_contents_type2_live);
                    }
                    UserState userState = UserInfoUtils.getUserState(ContentDetailActivity.this);
                    String loginStatus;
                    if (UserState.LOGIN_NG.equals(userState)) {
                        loginStatus = getString(R.string.google_analytics_custom_dimension_login_ng);
                    } else {
                        loginStatus = getString(R.string.google_analytics_custom_dimension_login_ok);
                    }
                    mHikariType = ContentUtils.getHikariType(mDetailFullData);
                    String serviceName = getString(R.string.google_analytics_custom_dimension_service_h4d);
                    String contentsType1 = ContentUtils.getContentsType1(ContentDetailActivity.this, mHikariType);
                    ContentDetailActivity.super.sendScreenView(screenName,
                            ContentUtils.getCustomDimensions(loginStatus, serviceName, contentsType1, contentsType2, mDetailFullData.getTitle()));

                    String dispType = mDetailFullData.getDisp_type();
                    String searchOk = mDetailFullData.getmSearch_ok();
                    String dTv = mDetailFullData.getDtv();
                    String dTvType = mDetailFullData.getDtvType();
                    OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
                    detailData.setTitle(mDetailFullData.getTitle());
                    if (ContentUtils.DTV_FLAG_ONE.equals(dTv)) {
                        setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmDtv_thumb_640_360());
                    } else {
                        setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmThumb_640_360());
                    }
                    detailData.setVodMetaFullData(contentsDetailInfo);
                    detailData.setDetail(mDetailFullData.getSynop());
                    // コンテンツ状態を反映
                    detailData.setClipStatus(clipStatus);
                    detailData.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dTv, dTvType));
                    detailData.setDispType(dispType);
                    detailData.setSearchOk(searchOk);
                    detailData.setDtv(dTv);
                    detailData.setDtvType(dTvType);
                    detailData.setCrId(mDetailFullData.getCrid());
                    detailData.setEventId(mDetailFullData.getmEvent_id());
                    detailData.setTitleId(mDetailFullData.getTitle_id());
                    detailData.setRvalue(mDetailFullData.getR_value());
                    detailData.setRating(mDetailFullData.getRating());
                    detailData.setCopy(mDetailFullData.getmCopy());
                    detailData.setM4kflg(mDetailFullData.getM4kflg());
                    detailData.setAdinfoArray(mDetailFullData.getmAdinfo_array());
                    detailData.setContentCategory(mDetailFullData.getContentsType());
                    String date = "";
                    ContentUtils.ContentsType contentsType = ContentUtils.getContentsTypeByPlala(mDetailFullData.getDisp_type(),
                            mDetailFullData.getmTv_service(), mDetailFullData.getmContent_type(),
                            mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date(),
                            mDetailFullData.getmVod_start_date(), mDetailFullData.getmVod_end_date(),
                            mDetailFullData.getEstFlag(), mDetailFullData.getmChsvod());
                    if (contentsType == ContentUtils.ContentsType.TV) {
                        //番組(m/d（曜日）h:ii - h:ii)
                        date = DateUtils.getContentsDateString(mDetailFullData.getPublish_start_date(), mDetailFullData.getPublish_end_date());
                        mVodEndDateText = date;
                        setContentsType(ContentUtils.ContentsType.TV);
                    } else {
                        if (contentsType == ContentUtils.ContentsType.DCHANNEL_VOD_OVER_31
                                || contentsType == ContentUtils.ContentsType.DCHANNEL_VOD_31) {
                            //見逃しは vod_start_date を使用する
                            detailData.setmStartDate(String.valueOf(mDetailFullData.getmVod_start_date()));
                        } else {
                            //VODは avail_start_date を使用する
                            detailData.setmStartDate(String.valueOf(mDetailFullData.getAvail_start_date()));
                        }
                        setViewPagerTab();
                        if (DateUtils.isBefore(mDetailFullData.getAvail_start_date())) {
                            //配信前 m/d（曜日）から
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
                        if (getStbStatus() || mVisibility) {
                            createRemoteControllerView(true);
                            mIsControllerVisible = true;
                            mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
                            setStartRemoteControllerUIListener(ContentDetailActivity.this);
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
                        if (getStbStatus() || mVisibility) {
                            createRemoteControllerView(true);
                            mIsControllerVisible = true;
                            setStartRemoteControllerUIListener(ContentDetailActivity.this);
                        }
                    }
                } else {
                    setViewPagerTab();
                    showErrorDialog(ErrorType.contentDetailGet);
                    // 他サービス
                    if (!mIsOtherService) {
                        mThumbnail.setImageResource(R.mipmap.error_movie);
                    }
                }
                sendOperateLog();
                if (getStbStatus() || mVisibility) {
                    findViewById(R.id.remote_control_view).setVisibility(View.VISIBLE);
                }
                if (mDetailFullData != null && !TextUtils.isEmpty(mDetailFullData.getmService_id())) {
                    mServiceId = mDetailFullData.getmService_id();
                    //チャンネル情報取得(取得後に視聴可否判定)
                    getChannelInfo();
                } else {
                    setViewPagerTab();
                    mViewIngType = ContentUtils.getViewingType(contractInfo, mDetailFullData, mChannel);
                    //コンテンツ種別ごとの視聴可否判定を実行
                    getViewingTypeRequest(mViewIngType);
                }
                DTVTLogger.end();
            }
        });
        DTVTLogger.end();
    }

    /**
     * コンテンツ種別ごとに視聴可否リクエストを投げる.
     *
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

    //endregion
    @Override
    public void onRoleListCallback(final ArrayList<RoleListMetaData> roleListInfo) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DTVTLogger.start();
                //スタッフ情報取得して、更新する
                if (roleListInfo != null) {
                    DtvContentsDetailFragment detailFragment = getDetailFragment();
                    if (mDetailFullData != null) {
                        String[] credit_array = mDetailFullData.getmCredit_array();
                        List<String> staffList = getRoleList(credit_array, roleListInfo);
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
                    showErrorDialog(ErrorType.roleListGet);
                }
                DTVTLogger.end();
            }
        });
        DTVTLogger.end();
    }

    /**
     * データ取得がすべて終了していたらIndicatorを非表示にする(対象はContentsDetailDataProviderのみ).
     *
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

    /**
     * ロールリスト取得.
     *
     * @param credit_array スタッフ情報
     * @param roleListInfo ロールリスト情報
     * @return ロールリスト
     */
    private List<String> getRoleList(final String[] credit_array, final ArrayList<RoleListMetaData> roleListInfo) {
        List<String> staffList = new ArrayList<>();
        StringBuilder ids = new StringBuilder();
        for (String aCredit_array : credit_array) {
            String[] creditInfo = aCredit_array.split("\\|");
            if (creditInfo.length == 4) {
                String creditId = creditInfo[2];
                String creditName = creditInfo[3];
                if (!TextUtils.isEmpty(creditId)) {
                    for (int j = 0; j < roleListInfo.size(); j++) {
                        RoleListMetaData roleListMetaData = roleListInfo.get(j);
                        if (creditId.equals(roleListMetaData.getId())) {
                            if (!ids.toString().contains(creditId + ",")) {
                                ids.append(creditId);
                                ids.append(",");
                                staffList.add(roleListMetaData.getName() + File.separator);
                                staffList.add(creditName);
                            } else {
                                String[] oldData = ids.toString().split(",");
                                for (int k = 0; k < oldData.length; k++) {
                                    if (creditId.equals(oldData[k])) {
                                        staffList.set(k * 2 + 1, staffList.get(k * 2 + 1) + "、" + creditName);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return staffList;
    }
    //region ScaledDownProgramListDataProvider.ApiDataProviderCallback
    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @SuppressWarnings("OverlyComplexMethod")
            @Override
            public void run() {
                DTVTLogger.start();
                if (mViewPagerIndex >= 0) {
                    mViewPager.setCurrentItem(mViewPagerIndex);
                    mViewPagerIndex = DEFAULT_TAB_INDEX;
                }
                if (channels == null || channels.isEmpty()) {
                    setViewPagerTab();
                    showErrorDialog(ErrorType.channelListGet);
                    return;
                }

                //DBに保存されているUserInfoから契約情報を確認する
                String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(ContentDetailActivity.this));
                DTVTLogger.debug("contractInfo: " + contractInfo);

                if (mContentsType != null) {
                    //チャンネル情報取得して、更新する
                    if (!TextUtils.isEmpty(mServiceId)) {
                        DtvContentsDetailFragment detailFragment = getDetailFragment();
                        for (int i = 0; i < channels.size(); i++) {
                            ChannelInfo channel = channels.get(i);
                            if (mServiceId.equals(channel.getServiceId())) {
                                mChannel = channel;
                                //チャンネル情報取得完了前にタブ切替されていた場合はここでチャンネルタブ表示処理を開始する
                                if (mViewPager.getCurrentItem() == CONTENTS_DETAIL_CHANNEL_TAB_POSITION) {
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
                    setViewPagerTab();
                }
                if (mDetailFullData != null) {
                    checkWatchContents(mViewIngType);
                }
                //コンテンツ種別ごとの視聴可否判定を実行
                getViewingTypeRequest(mViewIngType);
                DTVTLogger.end();
            }
        });
        DTVTLogger.end();
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final int[] chNo) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
            @Override
            public void run() {
                DTVTLogger.start();
                if (channelsInfo != null && channelsInfo.getChannels() != null) {
                    List<ChannelInfo> channels = channelsInfo.getChannels();
                    sort(channels);
                    if (channels.size() > 0) {
                        if (mViewPager.getCurrentItem() == CONTENTS_DETAIL_CHANNEL_TAB_POSITION) {
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
                                                    //NOW ON AIR の判断
                                                    contentsData.setChannelName(getString(R.string.home_label_now_on_air));
                                                }
                                            }
                                            contentsData.setSubTitle(getDate());
                                            isFirst = true;
                                        }
                                        contentsData.setTitle(scheduleInfo.getTitle());
                                        contentsData.setContentsId(scheduleInfo.getCrId());
                                        contentsData.setRequestData(scheduleInfo.getClipRequestData());
                                        contentsData.setThumURL(scheduleInfo.getImageUrl());
                                        contentsData.setTime(DateUtils.getContentsDetailChannelHmm(scheduleInfo.getStartTime()));
                                        contentsData.setClipExec(scheduleInfo.isClipExec());
                                        contentsData.setDispType(scheduleInfo.getDispType());
                                        contentsData.setDtv(scheduleInfo.getDtv());
                                        contentsData.setTvService(scheduleInfo.getTvService());
                                        contentsData.setServiceId(scheduleInfo.getServiceId());
                                        contentsData.setEventId(scheduleInfo.getEventId());
                                        contentsData.setCrid(scheduleInfo.getCrId());
                                        contentsData.setTitleId(scheduleInfo.getTitleId());
                                        getChannelFragment().addContentsData(contentsData);
                                    }
                                }
                            }
                            if (mDateIndex == 1) {
                                getChannelDetailByPageNo();
                            } else {
                                checkClipStatus(CLIP_BUTTON_CHANNEL_UPDATE);
                            }
                        }
                    }
                    channelLoadCompleted();
                } else {
                    mChannelFragment = getChannelFragment();
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
                DTVTLogger.end();
            }
        });
        DTVTLogger.end();
    }

    /**
     * 日付を取得.
     *
     * @return 日付
     */
    private String getDate() {
        String subTitle = null;
        if (mChannelDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
            try {
                Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                calendar.setTime(sdf.parse(mChannelDate));
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                subTitle = (month + 1) + getString(R.string.home_contents_slash) + day
                        + getString(R.string.home_contents_front_bracket)
                        + DateUtils.STRING_DAY_OF_WEEK[week]
                        + getString(R.string.home_contents_back_bracket);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
        }
        return subTitle;
    }

    /**
     * ソートを行う.
     *
     * @param channels チャンネル
     */
    private void sort(final List<ChannelInfo> channels) {
        for (ChannelInfo channel : channels) {
            Collections.sort(channel.getSchedules(), new CalendarComparator());
        }
    }

    /**
     * 詳細tabを取得.
     *
     * @return 現在表示しているfragment
     */
    private DtvContentsDetailFragment getDetailFragment() {
        if (mContentsDetailFragment == null) {
            Fragment currentFragment = getFragmentFactory().createFragment(0);
            mContentsDetailFragment = (DtvContentsDetailFragment) currentFragment;
        }
        return mContentsDetailFragment;
    }

    //region View.OnClickListener
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onClick(final View v) {
        DTVTLogger.start();
        switch (v.getId()) {
            case R.id.dtv_contents_detail_main_layout_thumbnail_btn:
                if (mViewIngType != null
                        && mViewIngType.equals(ContentUtils.ViewIngType.DISABLE_WATCH_AGREEMENT_DISPLAY)) {
                    //未契約時は契約導線を表示
                    leadingContract();
                    return;
                }

                if (mDetailData != null && mDetailData.getServiceId() == ContentUtils.DTV_CONTENTS_SERVICE_ID) {
                    //dTV起動
                    startDtvApp(mDetailData);
                } else if (mDetailData != null && mDetailData.getServiceId() == ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID) {
                    //dアニメ起動
                    startDAnimeApp(mDetailData);
                } else if (mDetailData != null && mDetailData.getServiceId() == ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                    //dチャンネル起動.
                    startDtvChannelApp(mDetailData);
                } else if (mDetailFullData != null) {
                    //ひかりTV内DTVの場合
                    if (ContentUtils.VIDEO_PROGRAM.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.VIDEO_SERIES.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.WIZARD.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.VIDEO_PACKAGE.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.SUBSCRIPTION_PACKAGE.equals(mDetailFullData.getDisp_type())
                            || ContentUtils.SERIES_SVOD.equals(mDetailFullData.getDisp_type())) {
                        //dTV起動
                        startDtvApp(mDetailFullData);
                    } else if (ContentUtils.TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
                        startDtvChannelApp(mDetailFullData);

                    }
                }
                break;
            default:
                super.onClick(v);
        }
        DTVTLogger.end();
    }
    //endregion

    /**
     * 機能：dTVAPP起動（検レコサーバ）.
     *
     * @param detailData 検レコサーバメタデータ
     */
    private void startDtvApp(final OtherContentsDetailData detailData) {
        if (isAppInstalled(ContentDetailActivity.this, DTV_PACKAGE_NAME)) {
            CustomDialog startAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            startAppDialog.setContent(getResources().getString(R.string.dtv_content_service_start_dialog));
            startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    long localVersionCode = getVersionCode(DTV_PACKAGE_NAME);
                    if (localVersionCode < DTV_VERSION_STANDARD) {
                        String errorMessage = getResources().getString(R.string.dtv_content_service_update_dialog);
                        showErrorDialog(errorMessage);
                    } else {
                        boolean execResult = true;
                        try {
                            // contentsId が16桁の場合に下8桁を使用する。※前提条件:contentsId は8桁または16桁である
                            String contentsId = detailData.getContentsId().substring(
                                    detailData.getContentsId().length() -CONTENTS_ID_VALID_LENGTH);
                            DTVTLogger.debug("Reserved4["+ detailData.getReserved4() + "] contentsId:" +detailData.getContentsId() +" lower 8 digits:"+ contentsId);
                            //タイトルタイプの別
                            //4:音楽コンテンツ
                            if (RESERVED4_TYPE4.equals(detailData.getReserved4())) {
                                execResult = startApp(UrlConstants.WebUrl.WORK_START_TYPE + contentsId);
                                //7,8:ライブ配信コンテンツ
                            } else if (RESERVED4_TYPE7.equals(detailData.getReserved4())
                                    || RESERVED4_TYPE8.equals(detailData.getReserved4())) {
                                execResult = startApp(UrlConstants.WebUrl.SUPER_SPEED_START_TYPE + contentsId);
                                //その他の場合
                            } else {
                                execResult = startApp(UrlConstants.WebUrl.TITTLE_START_TYPE + contentsId);
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            DTVTLogger.debug(e);
                        }

                        //実行時に実行に失敗していた場合は、メッセージを表示する
                        if(!execResult) {
                            //DTVのエラーを表示
                            execFailDialog(DTV_PACKAGE_NAME);
                        }
                    }
                }
            });
            startAppDialog.showDialog();
        } else {
            CustomDialog installAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            installAppDialog.setContent(getResources().getString(R.string.dtv_content_service_application_not_install));
            installAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    toGooglePlay(UrlConstants.WebUrl.DTV_GOOGLEPLAY_DOWNLOAD_URL);
                }
            });
            installAppDialog.showDialog();
        }
    }

    /**
     * 機能：dアニメAPP起動（検レコサーバ）.
     *
     * @param detailData 検レコサーバメタデータ
     */
    private void startDAnimeApp(final OtherContentsDetailData detailData) {
        if (isAppInstalled(ContentDetailActivity.this, DANIMESTORE_PACKAGE_NAME)) {
            CustomDialog startAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            startAppDialog.setContent(getResources().getString(R.string.d_anime_store_content_service_start_dialog));
            startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    long localVersionCode = getVersionCode(DANIMESTORE_PACKAGE_NAME);
                    boolean execResult = true;

                    //バージョンチェック
                    if (localVersionCode < DANIMESTORE_VERSION_STANDARD) {
                        String errorMessage = getResources().getString(R.string.d_anime_store_content_service_update_dialog);
                        showErrorDialog(errorMessage);
                    } else {
                        if (mDetailData != null) {
                            if (mDetailData.getTitleKind() != null && mDetailData.getTitleKind().equals(SearchDataProvider.D_ANIME_STORE_SONG_CONTENTS)) {
                                //音楽：1
                                execResult = startApp(UrlConstants.WebUrl.D_ANIME_SONG_STORE_START_URL + detailData.getContentsId());
                            } else {
                                //映像：0 ※協議の結果 1 以外は映像コンテンツとして扱う
                                execResult = startApp(UrlConstants.WebUrl.DANIMESTORE_START_URL + detailData.getContentsId());
                            }
                        }
                    }

                    //実行時に実行に失敗していた場合は、メッセージを表示する
                    if (!execResult) {
                        //dアニメのエラーを表示
                        execFailDialog(DANIMESTORE_PACKAGE_NAME);
                    }
                }
            });
            startAppDialog.showDialog();
        } else {
            CustomDialog installAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            installAppDialog.setContent(getResources().getString(R.string.d_anime_store_application_not_install_dialog));
            installAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    toGooglePlay(UrlConstants.WebUrl.DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL);
                }
            });
            installAppDialog.showDialog();
        }
    }

    /**
     * 機能：dTVチャンネルAPP起動（検レコサーバ）.
     *
     * @param detailData 検レコサーバメタデータ
     */
    private void startDtvChannelApp(final OtherContentsDetailData detailData) {
        if (isAppInstalled(ContentDetailActivity.this, DTVCHANNEL_PACKAGE_NAME)) {
            final CustomDialog startAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            startAppDialog.setContent(getResources().getString(R.string.dtv_channel_service_start_dialog));
            startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    //バージョンコードは15
                    long localVersionCode = getVersionCode(DTVCHANNEL_PACKAGE_NAME);
                    if (localVersionCode < DTVCHANNEL_VERSION_STANDARD) {
                        String errorMessage = getResources().getString(R.string.dtv_channel_service_update_dialog);
                        showErrorDialog(errorMessage);
                    } else {
                        boolean execResult = true;

                        //テレビ再生  「categoryId」が「01」の場合
                        if (DTV_CHANNEL_CATEGORY_BROADCAST.equals(detailData.getCategoryId())) {
                            execResult = startApp(UrlConstants.WebUrl.DTVCHANNEL_TELEVISION_START_URL + detailData.getChannelId());
                            DTVTLogger.debug("channelId :----" + detailData.getChannelId());
                            //ビデオ再生  「categoryId」が「02」または「03」の場合
                        } else if (DTV_CHANNEL_CATEGORY_MISSED.equals(detailData.getCategoryId())
                                || DTV_CHANNEL_CATEGORY_RELATION.equals(detailData.getCategoryId())) {
                            execResult = startApp(UrlConstants.WebUrl.DTVCHANNEL_VIDEO_START_URL + detailData.getContentsId());
                            DTVTLogger.debug("ContentId :----" + detailData.getContentsId());
                        }

                        //実行時に実行に失敗していた場合は、メッセージを表示する
                        if(!execResult) {
                            //dTVチャンネルのエラーを表示
                            execFailDialog(DTVCHANNEL_PACKAGE_NAME);
                        }
                    }
                }
            });
            startAppDialog.showDialog();
        } else {
            CustomDialog installAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            installAppDialog.setContent(getResources().getString(R.string.dtv_channel_service_application_not_install_dialog));
            installAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    toGooglePlay(UrlConstants.WebUrl.DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL);
                }
            });
            installAppDialog.showDialog();
        }
    }

    /**
     * 機能：dTVAPP起動（ぷららサーバ）.
     *
     * @param detailData ぷららサーバメタデータ
     */
    private void startDtvApp(final VodMetaFullData detailData) {
        //端末にDTVアプリはすでに存在した場合
        if (isAppInstalled(ContentDetailActivity.this, DTV_PACKAGE_NAME)) {
            CustomDialog startAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            startAppDialog.setContent(getResources().getString(R.string.dtv_content_service_start_dialog));
            startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    long localVersionCode = getVersionCode(DTV_PACKAGE_NAME);
                    if (localVersionCode < DTV_VERSION_STANDARD) {
                        String errorMessage = getResources().getString(R.string.dtv_content_service_update_dialog);
                        showErrorDialog(errorMessage);
                    } else {
                        boolean execResult = true;

                        DTVTLogger.debug("dtv_type[" + detailData.getDtvType() + "] title_id:" + detailData.getTitle_id() + " episode_id:" + detailData.getEpisode_id());
                        if (METARESPONSE1.equals(detailData.getDtvType())) {
                            execResult = startApp(UrlConstants.WebUrl.WORK_START_TYPE + detailData.getEpisode_id());
                            DTVTLogger.debug("Start title with the specified episode_id:" + detailData.getEpisode_id()
                                    + " URLScheme:" + UrlConstants.WebUrl.WORK_START_TYPE + detailData.getEpisode_id());
                        } else if (METARESPONSE2.equals(detailData.getDtvType())) {
                            execResult = startApp(UrlConstants.WebUrl.SUPER_SPEED_START_TYPE + detailData.getTitle_id());
                            DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id()
                                    + " URLScheme:" + UrlConstants.WebUrl.SUPER_SPEED_START_TYPE + detailData.getTitle_id());
                        } else if (METARESPONSE3.equals(detailData.getDtvType())) {
                            String episodeId = detailData.getEpisode_id();

                            if (episodeId == null || episodeId.isEmpty()) {
                                execResult = startApp(UrlConstants.WebUrl.TITTLE_START_TYPE + detailData.getTitle_id());
                                DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id()
                                        + " URLScheme:" + UrlConstants.WebUrl.TITTLE_START_TYPE + detailData.getTitle_id());
                            } else {
                                // ※作品IDが設定されていた場合は、タイトル詳細を作品ID指定で起動させる
                                execResult = startApp(String.format(UrlConstants.WebUrl.TITTLE_EPISODE_START_TYPE,
                                        detailData.getTitle_id(), episodeId));
                                DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id() + " episode_id:" + episodeId
                                        + " URLScheme:" + String.format(UrlConstants.WebUrl.TITTLE_EPISODE_START_TYPE, detailData.getTitle_id(), episodeId));
                            }
                        } else {
                            execResult = startApp(UrlConstants.WebUrl.TITTLE_START_TYPE + detailData.getTitle_id());
                            DTVTLogger.debug("Start title with the specified title_id:" + detailData.getTitle_id()
                                    + " URLScheme:" + UrlConstants.WebUrl.TITTLE_START_TYPE + detailData.getTitle_id());
                        }

                        //実行時に実行に失敗していた場合は、メッセージを表示する
                        if(!execResult) {
                            //dTVのエラーを表示
                            execFailDialog(DTV_PACKAGE_NAME);
                        }
                    }
                }
            });
            startAppDialog.showDialog();
        } else {
            //DTVアプリ存在しない場合
            CustomDialog installAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            installAppDialog.setContent(getResources().getString(R.string.dtv_content_service_application_not_install));
            installAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    toGooglePlay(UrlConstants.WebUrl.DTV_GOOGLEPLAY_DOWNLOAD_URL);
                }
            });
            installAppDialog.showDialog();
        }
    }

    /**
     * 機能：dTVチャンネルAPP起動（ぷららサーバ）.
     *
     * @param detailData ぷららサーバメタデータ
     */
    private void startDtvChannelApp(final VodMetaFullData detailData) {
        //ひかりTV内dtvチャンネルの場合
        if (isAppInstalled(ContentDetailActivity.this, DTVCHANNEL_PACKAGE_NAME)) {
            final CustomDialog startAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            startAppDialog.setContent(getResources().getString(R.string.dtv_channel_service_start_dialog));
            startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    long localVersionCode = getVersionCode(DTVCHANNEL_PACKAGE_NAME);
                    if (localVersionCode < DTVCHANNEL_VERSION_STANDARD) {
                        String errorMessage = getResources().getString(R.string.dtv_channel_service_update_dialog);
                        showErrorDialog(errorMessage);
                    } else {
                        if (ContentUtils.TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(detailData.getmTv_service())) {
                            boolean execResult = true;
                            boolean isVodContent = true;
                            String contentType = detailData.getmContent_type();

                            //「contents_type」が未設定:番組
                            if (contentType == null) {
                                isVodContent = false;
                            } else switch (contentType) {
                                case ContentUtils.CONTENT_TYPE_FLAG_ONE: //「contents_type」が「1」:見逃しVOD or 番組
                                case ContentUtils.CONTENT_TYPE_FLAG_TWO: //「contents_type」が「2」:見逃しVOD or 番組
                                    long vodStartDate = detailData.getmVod_start_date();
                                    long now = DateUtils.getNowTimeFormatEpoch();

                                    if (vodStartDate <= now) {
                                        break;
                                    }

                                    isVodContent = false;
                                    break;
                                case ContentUtils.CONTENT_TYPE_FLAG_THREE: // 「contents_type」が「3」:関連VOD
                                    // isVodContentの初期値が「true」のため、break
                                    break;
                                default: //「contents_type」が「0」:番組
                                    isVodContent = false;
                                    break;
                            }

                            // VODコンテンツの場合、dTVチャンネルアプリの詳細画面を表示する
                            if (isVodContent) {
                                execResult = startApp(UrlConstants.WebUrl.DTVCHANNEL_VIDEO_START_URL + detailData.getCrid());
                                DTVTLogger.debug("crid :----" + detailData.getCrid());

                            } else { // 番組コンテンツの場合、dTVチャンネルアプリのTOP画面を表示する
                                DTVTLogger.debug("contentsType :----" + detailData.getmContent_type());
                                execResult = startApp(UrlConstants.WebUrl.DTVCHANNEL_TELEVISION_START_URL + detailData.getmService_id());
                                DTVTLogger.debug("chno :----" + detailData.getmService_id());
                            }

                            //実行時に実行に失敗していた場合は、メッセージを表示する
                            if(!execResult) {
                                //dTVチャンネルのエラーを表示
                                execFailDialog(DTVCHANNEL_PACKAGE_NAME);
                            }
                        }
                    }
                }
            });
            startAppDialog.showDialog();
        } else {
            CustomDialog installAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
            installAppDialog.setContent(getResources().getString(R.string.dtv_channel_service_application_not_install_dialog));
            installAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    toGooglePlay(UrlConstants.WebUrl.DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL);
                }
            });
            installAppDialog.showDialog();
        }
        DTVTLogger.end();
    }

    /**
     * 指定されたパッケージ名のアプリの実行に失敗しているので、ダウンロードダイアログを表示する.
     *
     * @param packageName 実行に失敗したパッケージ名
     */
    void execFailDialog(String packageName) {
        String message = "";
        String downloadUrlBuffer = "";

        //指定されたパッケージ名で処理を振り分ける
        if (packageName.equals(DTV_PACKAGE_NAME)) {
            //DTVの実行に失敗したので、メッセージとURLを指定
            message = getResources().getString(R.string.dtv_content_service_application_not_install);
            downloadUrlBuffer = UrlConstants.WebUrl.DTV_GOOGLEPLAY_DOWNLOAD_URL;
        } else if (packageName.equals(DANIMESTORE_PACKAGE_NAME)) {
            //Dアニメの実行に失敗したので、メッセージとURLを指定
            message = getResources().getString(R.string.d_anime_store_application_not_install_dialog);
            downloadUrlBuffer = UrlConstants.WebUrl.DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL;
        } else if (packageName.equals(DTVCHANNEL_PACKAGE_NAME)) {
            //DTVチャンネルの実行に失敗したので、メッセージとURLを指定
            message = getResources().getString(R.string.dtv_channel_service_application_not_install_dialog);
            downloadUrlBuffer = UrlConstants.WebUrl.DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL;
        } else {
            DTVTLogger.debug("Other package:" + packageName);
        }

        //コールバックに値を伝える為にファイナル化
        final String downloadUrl = downloadUrlBuffer;

        //再ダウンロードダイアログを表示
        CustomDialog installAppDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
        installAppDialog.setContent(message);
        installAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                toGooglePlay(downloadUrl);
            }
        });
        installAppDialog.showDialog();
    }

    /**
     * 機能：APP起動.
     *
     * @param url URL
     */
    private boolean startApp(final String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivityForResult(intent, START_APPLICATION_REQUEST_CODE);
        } catch (ActivityNotFoundException exception) {
            //Androidのバグと思われる原因により、インストール情報の取得ができない場合がある。その場合は、この例外が発生する
            DTVTLogger.debug(exception);
            return false;
        }

        return true;
    }

    /**
     * 機能：中継アプリは端末にインストールするかどうかの判断.
     *
     * @param context コンテキスト
     * @param packageName 中継アプリのパッケージ名
     * @return 中継アプリがインストールされているか
     */
    private boolean isAppInstalled(final Context context, final String packageName) {
        PackageManager packageManager = null;
        List<PackageInfo> pinfo = null;
        try {
            packageManager = context.getPackageManager();
            pinfo = packageManager.getInstalledPackages(0);
        } catch (RuntimeException exception) {
            //Androidのバグと思われる原因により、稀に本例外が発生する。情報が取得できないので、アプリ有りの扱いとする
            //本メソッドは現状DTV等他のアプリの起動時に使用する。アプリが本当に存在しなければ起動に失敗し、ダウンロードを促すダイアログを表示する
            return true;
        }
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 機能：ローカルバージョン情報を取得する.
     *
     * @param packageName 中継アプリのパッケージ名
     * @return 中継アプリのパージョン情報
     */
    @TargetApi(Build.VERSION_CODES.P)
    @SuppressWarnings("deprecation")
    private long getVersionCode(final String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return packageInfo.getLongVersionCode();
            }
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            DTVTLogger.debug(e);
        }
        return -1;
    }
    //region RemoteControllerView
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
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mDetailData.getContentsId());
                    break;
                case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mDetailData.getContentsId());
                    break;
                case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    //番組の場合
                    if (DTV_CHANNEL_CATEGORY_BROADCAST.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(
                                RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_BROADCAST,
                                mDetailData.getContentsId(), mDetailData.getChannelId());
                        //VOD(見逃し)の場合
                    } else if (DTV_CHANNEL_CATEGORY_MISSED.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(
                                RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_MISSED,
                                mDetailData.getContentsId(), mDetailData.getChannelId());
                        //VOD(関連)の場合
                    } else if (DTV_CHANNEL_CATEGORY_RELATION.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(
                                RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_RELATION,
                                mDetailData.getContentsId(), mDetailData.getChannelId());
                    } else {
                        if (!mIsFromHeader) {
                            setRemoteProgressVisible(View.GONE);
                        }
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
                && mThumbnailContractButtonClicked ) {
            DTVTLogger.debug("contract button clicked.");
            //「契約する」ボタンの動線でのサービスアプリ連携判定
            switch (mDetailData.getServiceId()) {
                case ContentUtils.DTV_CONTENTS_SERVICE_ID: // dTV
                case ContentUtils.D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                case ContentUtils.DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
                    //処理なし
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

    } // end of onStartRemoteControl

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
     * STBのサービスアプリ起動（ひかり）.
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private void startHikariApplication() {
        if (!mIsFromHeader) {
            setRemoteProgressVisible(View.VISIBLE);
        }
        if (ContentUtils.VIDEO_PROGRAM.equals(mDetailFullData.getDisp_type())) {
            if (ContentUtils.DTV_FLAG_ZERO.equals(mDetailFullData.getDtv()) || TextUtils.isEmpty(mDetailFullData.getDtv())
                    || FLAG_ZERO == mDetailFullData.getDtv().trim().length()) {
                String[] liinfArray = mDetailFullData.getmLiinf_array();
                String puid = mDetailFullData.getPuid();
                if (BVFLG_FLAG_ONE.equals(mDetailFullData.getBvflg())) {
                    requestStartApplicationHikariTvCategoryHikaritvVod(mDetailFullData.getPuid(),
                            mDetailFullData.getCid(), mDetailFullData.getCrid());
                } else if (BVFLG_FLAG_ZERO.equals(mDetailFullData.getBvflg()) || TextUtils.isEmpty(mDetailFullData.getBvflg())) {
                    //liinfを"|"区切りで分解する
                    if (mPurchasedVodListResponse == null) {
                        if (!mIsFromHeader) {
                            setRemoteProgressVisible(View.GONE);
                        }
                        return;
                    }
                    // 購入済みVOD一覧
                    ArrayList<ActiveData> activeDatas = mPurchasedVodListResponse.getVodActiveData();
                    //最長のvalid_end_dateを格納する
                    long vodLimitDate = 0;
                    //「valid_end_date」が一番長い「license_id」
                    String validLicenseId = "";
                    //現在Epoch秒
                    long nowDate = DateUtils.getNowTimeFormatEpoch();
                    //購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較して一致した場合:true
                    boolean isLicensedRentalVod = false;
                    DTVTLogger.debug(String.format("start application HikariTvCategoryHikaritvVod: bvflg=0, puid=%s", puid));
                    for (String liinf : liinfArray) {
                        String[] column = liinf.split(Pattern.quote("|"), 0);
                        DTVTLogger.debug(String.format("liinf: column[0]:%s", column[0]));
                        for (ActiveData activeData : activeDatas) {
                            String licenseId = activeData.getLicenseId();
                            DTVTLogger.debug(String.format("activeData: licenseId:%s validEndDate:%s", licenseId, activeData.getValidEndDate()));
                            //メタレスポンスのpuid、liinf_arrayのライセンスID（パイプ区切り）と
                            // 購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較して一致した場合
                            if (licenseId.equals(column[0]) || licenseId.equals(puid)) {
                                DTVTLogger.debug(String.format("licenseId=%s match! column[0]=%s puid=%s", licenseId, column[0], puid));
                                long validEndDate = activeData.getValidEndDate();
                                //一致した「active_list」の「valid_end_date」> 現在時刻の場合（一件でも条件を満たせば視聴可能）
                                if (validEndDate > nowDate) {
                                    isLicensedRentalVod = true;
                                    // license_idが複数ある場合は「valid_end_date」が一番長い「license_id」を指定する
                                    if (vodLimitDate < validEndDate) {
                                        DTVTLogger.debug(String.format("validEndDate=%s", validEndDate));
                                        vodLimitDate = validEndDate;
                                        validLicenseId = licenseId;
                                    }
                                }
                            }
                        }
                    }
                    //購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較して一致した場合
                    if (isLicensedRentalVod) {
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
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.GONE);
                    }
                }
            } else if (ContentUtils.DTV_FLAG_ONE.equals(mDetailFullData.getDtv())) {
                setHikariType(ContentUtils.HikariType.HIKARITV_IN_DTV);
                //ひかりTV内dTVのVOD,「episode_id」,「crid」を通知する
                requestStartApplicationHikariTvCategoryDtvVod(mDetailFullData.getEpisode_id(),
                        mDetailFullData.getCrid());
            } else {
                if (!mIsFromHeader) {
                    setRemoteProgressVisible(View.GONE);
                }
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
                requestStartApplicationHikariTvCategoryTerrestrialDigital(mDetailFullData.getmChno());
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
                        if (!mIsFromHeader) {
                            setRemoteProgressVisible(View.GONE);
                        }
                    }
                } else {
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.GONE);
                    }
                }
            } else {
                if (!mIsFromHeader) {
                    setRemoteProgressVisible(View.GONE);
                }
            }
        } else {
            if (!mIsFromHeader) {
                setRemoteProgressVisible(View.GONE);
            }
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
    //endregion

    /**
     * プレイヤーエラーダイアログを表示.
     *
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
     *
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
    //region MediaPlayerController

    /**
     * サムネイル取得処理を止める.
     */
    private void stopThumbnailConnect() {
        DTVTLogger.start();
        mIsDownloadStop = true;
        if (mThumbnailProvider != null) {
            StopThumbnailConnect thumbnailConnect = new StopThumbnailConnect();
            thumbnailConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mThumbnailProvider);
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
    }

    @Override
    public void recordingReservationResult(final RemoteRecordingReservationResultResponse response) {
        DTVTLogger.start();
        if (response != null) {
            if (RemoteRecordingReservationResultResponse
                    .REMOTE_RECORDING_RESERVATION_RESULT_RESPONSE_STATUS_NG.equals(response.getStatus())) {
                // サーバからのエラー通知
                DTVTLogger.debug("error" + response.getErrorNo());
                CustomDialog dialog = createErrorDialog();
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
            // コンテンツ詳細取得データに失敗があった場合
            CustomDialog dialog = createErrorDialog();
            dialog.showDialog();
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickRecordingReservationIcon(final View view) {
        //未契約時は契約導線を表示
        if (mViewIngType != null
                && mViewIngType.equals(ContentUtils.ViewIngType.DISABLE_WATCH_AGREEMENT_DISPLAY)) {
            leadingContract();
            return;
        }

        //詳細画面の表示中に制限時間以内になってしまったかどうかの検査
        if (!checkRecordTime()) {
            //録画は不能になったので帰る
            return;
        }

        // リスト表示用のアラートダイアログを表示
        if (mRecordingReservationCustomtDialog == null) {
            DTVTLogger.debug("Create Dialog");
            mRecordingReservationCustomtDialog = createRecordingReservationConfirmDialog();
        }
        mRecordingReservationCustomtDialog.showDialog();
    }

    /**
     * 詳細画面を長時間開いているうちに制限時間を経過したかどうかの判定.
     *
     * @return 録画可能ならばtrue
     */
    private boolean checkRecordTime() {
        if (mDetailFullData != null) {
            ContentUtils.ContentsType contentsType =
                    ContentUtils.getHikariContentsType(mDetailFullData);
            //取得を行ったコンテンツ種別が、録画ボタン表示対象以外かどうかの確認
            //(H4d契約は成立しなければボタンは表示されないので、ここでは見なくて良いでしょう)
            if (!(contentsType == ContentUtils.ContentsType.HIKARI_TV
               || contentsType == ContentUtils.ContentsType.HIKARI_IN_DCH_TV)) {
                //録画ボタン表示対象の種別以外ならば、録画可能時間外や放送中等なので、録画不能のダイアログを出して、falseを返す
                CustomDialog dialog = createErrorDialog();
                dialog.showDialog();
                return false;
            }
        }
        //制限時間経過以外ならばtrue
        return true;
    }

    /**
     * 開始時間と現在時刻の比較.
     *
     * @return 放送開始まで2時間以上あるかどうか
     */
    private boolean comparisonStartTime(RecordingReservationContentsDetailInfo recordInfo) {
        if (recordInfo == null) {
            return false;
        }
        long nowTimeEpoch = DateUtils.getNowTimeFormatEpoch();
        long canRecordingReservationTime = 0;
        if (recordInfo != null) {
            canRecordingReservationTime =
                    recordInfo.getStartTime() - (DateUtils.EPOCH_TIME_ONE_HOUR * 2);
        }
        DTVTLogger.debug("comparisonStartTime nowTimeEpoch:" + " canRecordingReservationTime:" + canRecordingReservationTime);
        return !(nowTimeEpoch >= canRecordingReservationTime);
    }

    /**
     * 録画予約失敗時エラーダイアログ表示.
     *
     * @return 録画予約失敗エラーダイアログ
     */
    private CustomDialog createErrorDialog() {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.recording_reservation_failed_dialog_msg));
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        return failedRecordingReservationDialog;
    }

    /**
     * 録画予約確認ダイアログを表示.
     *
     * @return 録画予約確認ダイアログ
     */
    private CustomDialog createRecordingReservationConfirmDialog() {
        CustomDialog recordingReservationConfirmDialog =
                new CustomDialog(ContentDetailActivity.this, CustomDialog.DialogType.CONFIRM);
        //タイトル指定
        recordingReservationConfirmDialog.setTitle(getResources().getString(
                R.string.recording_reservation_confirm_dialog_title));
        //本文指定
        recordingReservationConfirmDialog.setContent(getResources().getString(
                R.string.recording_reservation_confirm_dialog_msg));
        recordingReservationConfirmDialog.setConfirmText(
                R.string.recording_reservation_confirm_dialog_confirm);
        recordingReservationConfirmDialog.setCancelText(
                R.string.recording_reservation_confirm_dialog_cancel);
        // Cancelable
        recordingReservationConfirmDialog.setCancelable(false);
        recordingReservationConfirmDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                DTVTLogger.debug("Request RecordingReservation");
                DTVTLogger.debug(mRecordingReservationContentsDetailInfo.toString());
                if (!mContentsDetailDataProvider.requestRecordingReservation(
                        mRecordingReservationContentsDetailInfo)) {
                    //APIの実行が行えなかった場合は即座にfalseが返却されるので、エラーとする
                    CustomDialog dialog = createErrorDialog();
                    dialog.showDialog();
                }
            }
        });
        return recordingReservationConfirmDialog;
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
     *
     * @param viewIngType 視聴可否種別
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void checkWatchContents(final ContentUtils.ViewIngType viewIngType) {
        switch (viewIngType) {
            case ENABLE_WATCH_LIMIT_THIRTY:
            case ENABLE_WATCH_LIMIT_THIRTY_001:
                //期限まで30日以内表示内容設定
                mEndDate = mDetailFullData.getPublish_end_date();
                displayLimitDate();
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
                DTVTLogger.start();
                if (response == null) {
                    showErrorDialog(ErrorType.rentalVoidListGet);
                    return;
                }
                mPurchasedVodListResponse = response;
                ArrayList<ActiveData> vodActiveData = response.getVodActiveData();
                mEndDate = ContentUtils.getRentalVodValidEndDate(mDetailFullData, vodActiveData);
                displayLimitDate();
                DTVTLogger.debug("get rental vod end date:" + mEndDate);
                mViewIngType = ContentUtils.getRentalVodViewingType(mDetailFullData, mEndDate);
                DTVTLogger.debug("get rental vod viewing type:" + mViewIngType);
                changeUIBasedContractInfo();
                responseResultCheck(mViewIngType, mContentsType);
                DTVTLogger.end();
            }
        });
        DTVTLogger.end();
    }

    @Override
    public void onRentalChListCallback(final PurchasedChannelListResponse response) {
        DTVTLogger.start();
        //購入済みCH一覧取得からの戻り
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DTVTLogger.start();
                if (response == null) {
                    showErrorDialog(ErrorType.rentalChannelListGet);
                    return;
                }

                mEndDate = ContentUtils.getRentalChannelValidEndDate(response, mChannel);
                displayLimitDate();
                DTVTLogger.debug("get rental vod end date:" + mEndDate);
                mViewIngType = ContentUtils.getRentalChannelViewingType(mDetailFullData, mEndDate);
                DTVTLogger.debug("get rental vod viewing type:" + mViewIngType);
                changeUIBasedContractInfo();
                responseResultCheck(mViewIngType, mContentsType);
                DTVTLogger.end();
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
                msg = getString(R.string.contents_detail_out_house_player_error_msg);
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
        if (errorCode >= RETRY_ERROR_START) {
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
     *
     * @param visible 表示要否
     */
    private void setTabVisibility(final boolean visible) {
        switch (mDisplayState) {
            case CONTENTS_DETAIL_ONLY:
            case PLAYER_AND_CONTENTS_DETAIL:
                if (visible) {
                    findViewById(R.id.dtv_contents_detail_main_layout_vp).setVisibility(View.VISIBLE);
                    findViewById(R.id.rl_dtv_contents_detail_tab).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.dtv_contents_detail_main_layout_vp).setVisibility(View.GONE);
                    findViewById(R.id.rl_dtv_contents_detail_tab).setVisibility(View.GONE);
                }
                break;
            case PLAYER_ONLY:
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
                        //Threadクラスからのコールバックのため、UIスレッド化する
                        runOnUiThread(new Runnable() {
                            @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
                            @Override
                            public void run() {
                                showRemotePlayingProgress(false);
                                if (dlnaObject != null) {
                                    //リトライの時の為に控えておく
                                    mDlnaObject = dlnaObject;
                                    //player start
                                    //放送中ひかりTVコンテンツの時は自動再生する
                                    playAutoContents();
                                } else {
                                    setThumbnailMessage(getString(R.string.contents_detail_player_no_channel), "", true, false);
                                }
                            }
                        });
                    }

                    @Override
                    public void multiChannelErrorCallback() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showRemotePlayingProgress(false);
                                showGetDataFailedToast();
                            }
                        });
                    }

                    @Override
                    public void onConnectErrorCallback(final int errorCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showRemotePlayingProgress(false);
                                showGetDataFailedToast();
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
                                showErrorDialog(getString(R.string.contents_player_bad_contents_info));
                                setRemotePlayArrow(null);
                            }
                        });
                    }
                });
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (dlnaDmsItem != null) {
            //この場合に使用するチャンネル番号を取得する
            int convertedChannelNumber = convertChannelNumber(mChannel);
            //変換後のチャンネルIDを使用して呼び出す
            provider.findChannelByChannelNo(String.valueOf(convertedChannelNumber));
        } else {
            showRemotePlayingProgress(false);
            DTVTLogger.error("dlnaDmsItem == null");
        }
    }

    /**
     * 放送中ひかりコンテンツ再生.
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    void playAutoContents() {
        mDisplayState = PLAYER_AND_CONTENTS_DETAIL;
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
                if(mNotRemoteRetry) {
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
     *
     * @param contentsType コンテンツ種別
     * @param viewIngType  視聴可否判定結果
     * @param detailUserType ユーザタイプ
     */
    @SuppressWarnings({"EnumSwitchStatementWhichMissesCases", "OverlyComplexMethod", "OverlyLongMethod"})
    private void displayThumbnail(final ContentUtils.ContentsType contentsType, final ContentUtils.ViewIngType viewIngType, final ContentUtils.ContentsDetailUserType detailUserType) {
        DTVTLogger.start();
        //Pure系コンテンツはサムネイル表示済みのため何もしない
        if (ContentUtils.isPureContents(contentsType)) {
            return;
        }
        if (detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_LOGOUT)) {
            loginNgDisplay();
            Button button = setThumbnailMessage(getString(R.string.contents_detail_login_message), getString(R.string.contents_detail_login_button), true, true);
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
        if (contentsType == null
                || viewIngType == null
                || ContentUtils.isSkipViewingType(viewIngType, contentsType)
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
                        Button button = setThumbnailMessage(getString(R.string.contents_detail_hikari_channel_agreement), getString(R.string.contents_detail_contract_leading_button), true, true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                thumbnailContractButtonAction();
                            }
                        });
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
                } else if (detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_CONTRACT)) {
                    Button button = setThumbnailMessage(getString(R.string.contents_detail_pairing_request), getString(R.string.contents_detail_pairing_button), true, true);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            //ペアリング設定
                            Intent intent = new Intent(getApplicationContext(), StbSelectActivity.class);
                            intent.putExtra(StbSelectActivity.FROM_WHERE, StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Setting.ordinal());
                            startActivity(intent);
                        }
                    });
                } else {
                    Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement), getString(R.string.contents_detail_contract_leading_button), true, true);
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
                    Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement), getString(R.string.contents_detail_contract_leading_button), true, true);
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
                    Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement), getString(R.string.contents_detail_contract_leading_button), true, true);
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
     * ひかり未契約の場合視聴可否を視聴不可に変更.
     * @param detailUserType コンテンツ詳細ユーザタイプ
     * @param contentsType コンテンツタイプ
     * @param viewIngType 視聴可否種別
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void shapeViewType(final ContentUtils.ContentsDetailUserType detailUserType,
                              final ContentUtils.ContentsType contentsType,
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
        String screenName = getString(R.string.google_analytics_screen_name_player);
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
     *
     * @param detailUserType ユーザ状態
     * @param viewIngType    視聴可否種別
     */
    private void hikariTvThumbnailDisplay(final ContentUtils.ContentsDetailUserType detailUserType, final ContentUtils.ViewIngType viewIngType) {
        if (detailUserType.equals(ContentUtils.ContentsDetailUserType.NO_PAIRING_HIKARI_CONTRACT)
                && ContentUtils.isEnableDisplay(viewIngType)) {
            Button button = setThumbnailMessage(getString(R.string.contents_detail_pairing_request), getString(R.string.contents_detail_pairing_button), true, true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //ペアリング設定
                    Intent intent = new Intent(getApplicationContext(), StbSelectActivity.class);
                    intent.putExtra(StbSelectActivity.FROM_WHERE, StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Setting.ordinal());
                    startActivity(intent);
                }
            });
        } else {
            Button button = setThumbnailMessage(getString(R.string.contents_detail_no_agreement), getString(R.string.contents_detail_contract_leading_button), true, true);
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
        if (getStbStatus()) {
            contentDetailRemoteController();
        } else {
            startBrowser(UrlConstants.WebUrl.CONTRACT_URL);
        }
    }

    /**
     * サムネイル画像上の表示設定.
     *
     * @param message         テキストエリアに表示するメッセージ
     * @param buttonText      ボタン上に表示するテキスト
     * @param isContractView  契約導線表示フラグ
     * @param isDisplayButton 赤ボタン表示フラグ
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
            setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
        } else {
            mContractLeadingView.setVisibility(View.GONE);
            mThumbnailBtn.setVisibility(View.VISIBLE);
            TextView contractLeadingText = findViewById(R.id.view_contents_button_text);
            contractLeadingText.setText(message);
            contractLeadingText.setVisibility(View.VISIBLE);

            setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
        }
        return button;
    }

    /**
     * ひかりTVのNowOnAir用のチャンネル情報を算出する.
     *
     * @param channelInfo チャンネルメタ情報
     * @return 変換後チャンネル番号
     */
    private int convertChannelNumber(final ChannelInfo channelInfo) {
        //サービスIDを取得
        String serviceId = channelInfo.getServiceId();

        //サービスIDを10進数にした物を格納する
        int serviceIdDecimal = 0;

        try {
            //サービスIDを10進数に変換する
            serviceIdDecimal = Integer.parseInt(serviceId, SOURCE_HEXADECIMAL);
        } catch (NumberFormatException exception) {
            //メタ情報に誤りが無ければ、ここに来る事は無い。フェールセーフ用
            DTVTLogger.debug(exception);
        }

        //10進変換後のサービスIDを10倍する
        serviceIdDecimal *= CONVERT_SEARVICE_ID_TO_CHANNEL_NUMBER;

        return serviceIdDecimal;
    }

    /**
     * リモート視聴用の再生ボタン表示.
     *
     * @param playData 再生用情報
     */
    private void setRemotePlayArrow(final RecordedContentsDetailData playData) {
        DTVTLogger.start();
        //再生ボタンは宅外かつ契約があるときのみ表示
        if (UserInfoUtils.isContract(this)) {
            if (mPlayerViewLayout != null) {
                mPlayerViewLayout.setVisibility(View.GONE);
            }
            setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
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
                                    showErrorDialog(getString(R.string.contents_detail_out_house_player_error_msg));
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

        setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
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
                //ブラウザを起動
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
        DTVTLogger.start();
        //TODO 現在動作未定 jekinsビルドエラー発生したため、とりあえずパラメータを削除しません
        Uri uri = Uri.parse("https://www.nttdocomo.co.jp/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * サムネイル画像にシャドウをかける(アルファをかける).
     * @param alpha アルファ値
     */
    private void setThumbnailShadow(final float alpha) {
        if (mThumbnail != null) {
            mThumbnail.setAlpha(alpha);
        }
    }

    /**
     * プロセスバーを表示する.
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        //プログレスのヌルチェック
        View view = findViewById(R.id.contents_detail_scroll_layout);
        if (view == null) {
            return;
        }

        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            view.setVisibility(View.INVISIBLE);
            setRemoteProgressVisible(View.VISIBLE);
        } else {
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
            mChannelFragment.showProgress(showProgressBar);
        } else {
            mChannelFragment.showProgress(showProgressBar);
        }
    }

    /**
     * ユーザ操作履歴送信.
     * ログイン状態でしか送信しない
     */
    private void sendOperateLog() {
        if (!TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferencesDaccountId(this)) && mSendOperateLog == null) {
            mSendOperateLog = new SendOperateLog(getApplicationContext());
            mSendOperateLog.sendOpeLog(mDetailData, mDetailFullData);
        }
    }

    /**
     * エラーダイアログを表示する.
     * @param errorType エラータイプ
     */
    @SuppressWarnings("OverlyLongMethod")
    private void showErrorDialog(final ErrorType errorType) {
        DTVTLogger.start();
        //エラーキャッチ時は必ずIndicatorを非表示にする
        showProgressBar(false);
        showChannelProgressBar(false);
        ErrorState errorState = null;
        CustomDialog.ApiOKCallback okCallback = null;

        //状況に合わせてエラーの取得場所を選択し、ダイアログ表示を行う
        switch (errorType) {
            case contentDetailGet:
                //エラー値の取得元を切り替え
                if (mContentsDetailDataProvider != null) {
                    //コンテンツ詳細プロバイダーが存在すれば、そこからエラー値を取得する
                    errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.contentsDetailGet);
                } else if (mSearchDataProvider != null) {
                    //検索データプロバイダーが存在すれば、そこからエラー値を取得する
                    errorState = mSearchDataProvider.getError();
                } else {
                    //どちらのデータプロバイダーも無ければ、何もできないので帰る（ここに来るケースは無い筈）
                    DTVTLogger.debug(
                            "mScaledDownProgramListData mSearchData both null:"
                                    + errorType);
                    return;
                }
                okCallback = new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        finish();
                    }
                };
                break;
            case roleListGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.roleList);
                break;
            case rentalChannelListGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.rentalChList);
                break;
            case rentalVoidListGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.rentalVodList);
                break;
            case channelListGet:
                errorState = mScaledDownProgramListDataProvider.getChannelError();
                break;
            case tvScheduleListGet:
                errorState = mScaledDownProgramListDataProvider.getmTvScheduleError();
                break;
            default:
                break;
        }

        //ダイアログの準備
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
            //OKボタンによる詳細画面表示終了の為、ダイアログの枠外のタッチは無視する
            customDialog.setOnTouchOutside(false);
        }
        customDialog.showDialog();
        DTVTLogger.end();
    }

    /**
     * STB接続状態を取得.
     *
     * @return STB接続状態
     */
    private boolean getStbStatus() {
        return StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN;
    }

    /**
     * クリップボタンの更新.
     *
     * @param targetId 更新対象
     */
    private void checkClipStatus(final int targetId) {
        DTVTLogger.start();
        ClipKeyListDataManager manager = new ClipKeyListDataManager(ContentDetailActivity.this);
        List<Map<String, String>> mapList = manager.selectClipAllList();

        switch (targetId) {
            case CLIP_BUTTON_ALL_UPDATE:
                checkDetailClipStatus(mapList);
                checkChannelClipStatus(mapList);
                break;
            case CLIP_BUTTON_CHANNEL_UPDATE:
                checkChannelClipStatus(mapList);
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * 詳細情報のクリップボタン更新.
     *
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
     *
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

    @Override
    public void onSearchDataProviderFinishOk(final ResultType<TotalSearchContentInfo> resultType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TotalSearchContentInfo result = resultType.getResultType();

                if (result.getContentsDataList().size() > 0) {
                    ContentsData info = result.getContentsDataList().get(0);
                    DtvContentsDetailFragment detailFragment = getDetailFragment();
                    mDetailData.setTitleKind(info.getTitleKind());
                    OtherContentsDetailData detailData = detailFragment.getOtherContentsDetailData();
                    if (detailData != null) {
                        detailData.setDescription1(info.getDescription1());
                        detailData.setDescription2(info.getDescription2());
                        detailData.setDescription3(info.getDescription3());
                        detailData.setDetail(info.getSynopFromDescription());
                        detailFragment.setOtherContentsDetailData(detailData);
                        detailFragment.refreshDescription();
                    }
                }
                // titleKind 取得完了フラグを立てる
                mIsTitleKind = true;
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onSearchDataProviderFinishNg(final ResultType<SearchResultError> resultType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // titleKind 取得完了フラグを立てる
                mIsTitleKind = true;
                showErrorDialog(ErrorType.contentDetailGet);
            }
        });
    }

    @Override
    public void showClipToast(final int msgId) {
        super.showClipToast(msgId);
        //クリップ処理が終わった時点で、コンテンツ詳細、チャンネルリストのクリップ状態を更新する
        checkClipStatus(CLIP_BUTTON_ALL_UPDATE);
    }

    @Override
    public void clipKeyResult() {
        //Nop 仕様により実装のみ
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && mPlayerViewLayout != null) {
                    mPlayerViewLayout.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv).callOnClick();
                    return false;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
