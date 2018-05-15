/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.detail;

import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digion.dixim.android.secureplayer.MediaPlayerController;
import com.digion.dixim.android.secureplayer.MediaPlayerDefinitions;
import com.digion.dixim.android.secureplayer.SecureVideoView;
import com.digion.dixim.android.secureplayer.SecuredMediaPlayerController;
import com.digion.dixim.android.secureplayer.helper.CaptionDrawCommands;
import com.digion.dixim.android.util.ExternalDisplayHelper;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.ProcessSettingFile;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.ContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentDetailDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsChannelFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaHikariChListItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaProvHikariChList;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.struct.CalendarComparator;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.MediaVideoInfo;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DAccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SendOperateLog;

import java.io.File;
import java.io.IOException;
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
public class ContentDetailActivity extends BaseActivity implements
        View.OnClickListener,
        TabItemLayout.OnClickTabTextListener,
        ContentsDetailDataProvider.ApiDataProviderCallback,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        MediaPlayerController.OnStateChangeListener,
        MediaPlayerController.OnFormatChangeListener,
        MediaPlayerController.OnPlayerEventListener,
        MediaPlayerController.OnErrorListener,
        MediaPlayerController.OnCaptionDataListener,
        RemoteControllerView.OnStartRemoteControllerUIListener,
        DtvContentsDetailFragment.RecordingReservationIconListener,
        DtvContentsChannelFragment.ChangedScrollLoadListener {

    /**
     * エラータイプ.
     */
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
    /** アスペクト比(4:3)の4.*/
    private static final int SCREEN_RATIO_WIDTH_4 = 4;
    /** アスペクト比(4:3)の3.*/
    private static final int SCREEN_RATIO_HEIGHT_3 = 3;
    /**先頭のメタデータを取得用.*/
    private static final int FIRST_VOD_META_DATA = 0;

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
    /** サムネイルプロバイダー .*/
    private ThumbnailProvider mThumbnailProvider = null;
    /** サムネイル取得処理ストップフラグ .*/
    private boolean mIsDownloadStop = false;
    /** コンテンツ詳細フラグメントファクトリー.*/
    private DtvContentsDetailFragmentFactory mFragmentFactory = null;
    /** ビューページャアダプター.*/
    private ContentsDetailPagerAdapter mContentsDetailPagerAdapter;
    /**購入済みVODレスポンス.*/
    private PurchasedVodListResponse mResponse = null;
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
    /**サムネイルアイコン、メッセージレイアウト.*/
    private LinearLayout mContractLeadingView = null;

    /**レコメンド情報キー.*/
    public static final String RECOMMEND_INFO_BUNDLE_KEY = "recommendInfoKey";
    /**ぷらら情報キー.*/
    public static final String PLALA_INFO_BUNDLE_KEY = "plalaInfoKey";
    /**DTVコンテンツサービスID.*/
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    /**DアニメコンテンツサービスID.*/
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    /**DTVチャンネルコンテンツサービスID.*/
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    /**DTVひかりコンテンツサービスID.*/
    public static final int DTV_HIKARI_CONTENTS_SERVICE_ID = 44;
    /**コンテンツ詳細予約済みID.*/
    public static final String CONTENTS_DETAIL_RESERVEDID = "1";
    /**モバイル視聴不可.*/
    private static final String MOBILEVIEWINGFLG_FLAG_ZERO = "0";

    /** 日付インディーズ.*/
    private int mDateIndex = 0;
    /** 日付リスト.*/
    private String[] mDateList = null;
    /* コンテンツ詳細 end */

    /**DTVバージョン.*/
    private static final int DTV_VERSION_STANDARD = 52000;
    /**レスポンス(1).*/
    private static final String METARESPONSE1 = "1";
    /**レスポンス(2).*/
    private static final String METARESPONSE2 = "2";
    /**予約済みタイプ(4).*/
    private static final String RESERVED4_TYPE4 = "4";
    /**予約済みタイプ(7).*/
    private static final String RESERVED4_TYPE7 = "7";
    /**予約済みタイプ(8).*/
    private static final String RESERVED4_TYPE8 = "8";
    /**DTVパッケージ名.*/
    private static final String DTV_PACKAGE_NAME = "jp.co.nttdocomo.dtv";
    /**エラーメッセージ.*/
    private String mErrorMessage;
    /*DTV起動*/

    /**dアニメストアパッケージ名.*/
    private static final String DANIMESTORE_PACKAGE_NAME = "com.nttdocomo.android.danimeapp";
    /**dアニメストアバージョン.*/
    private static final int DANIMESTORE_VERSION_STANDARD = 132;
    /*dアニメストア起動*/

    /**dTVチャンネル起動.*/
    private static final String DTVCHANNEL_PACKAGE_NAME = "com.nttdocomo.dch";
    /**dTVチャンネルバージョン.*/
    private static final int DTVCHANNEL_VERSION_STANDARD = 15;
    /**dTVチャンネルカテゴリー放送.*/
    private static final String DTV_CHANNEL_CATEGORY_BROADCAST = "01";
    /**dTVチャンネルカテゴリー見逃し.*/
    private static final String DTV_CHANNEL_CATEGORY_MISSED = "02";
    /**dTVチャンネルカテゴリー関連.*/
    private static final String DTV_CHANNEL_CATEGORY_RELATION = "03";
    /*dTVチャンネル起動*/

    /*ひかりTV起動*/
    /**他サービス起動リクエストコード.*/
    private static final int START_APPLICATION_REQUEST_CODE = 0;
    /** カテゴリID(01).*/
    public static final String H4D_CATEGORY_TERRESTRIAL_DIGITAL = "01";
    /** カテゴリID(02).*/
    public static final String H4D_CATEGORY_SATELLITE_BS = "02";
    /** カテゴリID(03).*/
    public static final String H4D_CATEGORY_IPTV = "03";
    /** カテゴリID(04).*/
    public static final String H4D_CATEGORY_DTV_CHANNEL_BROADCAST = "04";
    /** カテゴリID(05).*/
    public static final String H4D_CATEGORY_DTV_CHANNEL_MISSED = "05";
    /** カテゴリID(06).*/
    public static final String H4D_CATEGORY_DTV_CHANNEL_RELATION = "06";
    /** カテゴリID(08).*/
    public static final String H4D_CATEGORY_HIKARITV_VOD = "08";
    /** カテゴリID(10).*/
    public static final String H4D_CATEGORY_HIKARI_DTV_SVOD = "10";
    /** disp_type(tv_program).*/
    public static final String TV_PROGRAM = "tv_program";
    /** disp_type(video_program).*/
    public static final String VIDEO_PROGRAM = "video_program";
    /** disp_type(video_package).*/
    public static final String VIDEO_PACKAGE = "video_package";
    /** disp_type(WIZARD).*/
    public static final String WIZARD = "wizard";
    /** disp_type(video_series).*/
    public static final String VIDEO_SERIES = "video_series";
    /** disp_type(subscription_package).*/
    public static final String SUBSCRIPTION_PACKAGE = "subscription_package";
    /** disp_type(series_svod).*/
    public static final String SERIES_SVOD = "series_svod";
    /** dtv(1).*/
    public static final String DTV_FLAG_ONE = "1";
    /** dtv(0).*/
    public static final String DTV_FLAG_ZERO = "0";
    /** bvflg(1).*/
    private static final String BVFLG_FLAG_ONE = "1";
    /** bvflg(0).*/
    private static final String BVFLG_FLAG_ZERO = "0";
    /** tv_service(0).*/
    public static final String TV_SERVICE_FLAG_HIKARI = "1";
    /** tv_service(1).*/
    public static final String TV_SERVICE_FLAG_DCH_IN_HIKARI = "2";
    /** contents_type(0).*/
    public static final String CONTENT_TYPE_FLAG_ZERO = "0";
    /** contents_type(1).*/
    public static final String CONTENT_TYPE_FLAG_ONE = "1";
    /** contents_type(2).*/
    public static final String CONTENT_TYPE_FLAG_TWO = "2";
    /** contents_type(3).*/
    public static final String CONTENT_TYPE_FLAG_THREE = "3";
    /** flg(0).*/
    private static final int FLAG_ZERO = 0;
    /*ひかりTV起動*/

    /* player start */
    /**コントローラービューを非表示になるまでの待ち時間.*/
    private static final long HIDE_IN_3_SECOND = 3 * 1000;
    /**再発火.*/
    private static final int REFRESH_VIDEO_VIEW = 0;
    /** 巻き戻す10s.*/
    private static final int REWIND_SECOND = 10 * 1000;
    /**早送り30s.*/
    private static final int FAST_SECOND = 30 * 1000;
    /**ローカルファイルパスー.*/
    private static final String LOCAL_FILE_PATH = "file://";
    /**再生シークバー.*/
    private SeekBar mVideoSeekBar = null;
    /**SecureVideoView.*/
    private SecureVideoView mSecureVideoPlayer = null;
    /**プレイヤーコントローラ.*/
    private SecuredMediaPlayerController mPlayerController = null;
    /**再生するビデオ属性.*/
    private MediaVideoInfo mCurrentMediaInfo = null;
    /**プログレースRelativeLayout.*/
    private RelativeLayout mProgressLayout = null;
    /**録画コントローラビューRelativeLayout.*/
    private RelativeLayout mRecordCtrlView = null;
    /**ビデオコントローラバーRelativeLayout.*/
    private RelativeLayout mVideoCtrlBar = null;
    /**ビデオ再生停止TextView.*/
    private FrameLayout mVideoPlayPause = null;
    /**ビデオカレント時刻.*/
    private TextView mVideoCurTime = null;
    /**FrameLayout.*/
    private FrameLayout mFrameLayout = null;
    /**video全長.*/
    private TextView mVideoTotalTime = null;
    /**タイトル.*/
    private TextView mTvTitle = null;
    /**巻き戻しImageView.*/
    private ImageView mVideoRewind10 = null;
    /**早送りImageView.*/
    private ImageView mVideoFast30 = null;
    /**TvLogo.*/
    private ImageView mTvLogo = null;
    /**全画面再生.*/
    private ImageView mVideoFullScreen = null;
    /**ハンドラー.*/
    private static final Handler sCtrlHandler = new Handler(Looper.getMainLooper());
    /**画面操作検知.*/
    private GestureDetector mGestureDetector = null;
    /**端末画面Width.*/
    private int mScreenWidth = 0;
    /**再生開始可否.*/
    private boolean mCanPlay = false;
    /**操作アイコン表示か.*/
    private boolean mIsHideOperate = true;
    /**プレイヤー生成フラグ.*/
    private boolean mIsOncreateOk = false;
    /**録画予約コンテンツ詳細情報.*/
    private RecordingReservationContentsDetailInfo mRecordingReservationContentsDetailInfo = null;
    /**録画予約ダイアログ.*/
    private CustomDialog mRecordingReservationCustomtDialog = null;

    /*private static final int RECORDING_RESERVATION_DIALOG_INDEX_0 = 0; // 予約録画する
    private static final int RECORDING_RESERVATION_DIALOG_INDEX_1 = 1; // キャンセル*/

    /** プレイヤー横画面時のシークバーの下マージン.*/
    private static final int SEEKBAR_BOTTOM_MARGIN = 4;
    /** プレイヤー横画面時のコントロールバーの下マージン.*/
    private static final int MEDIA_CONTROL_BAR_UNDER_MARGIN = 32;
    /** プレイヤー横画面時のシークバーの時間の左右マージン.*/
    private static final int SEEKBAR_TIME_LATERAL_MARGIN = 18;
    /** プレイヤー横画面時のフルスクリーンボタンの右マージン.*/
    private static final int FULL_SCREEN_BUTTON_RIGHT_MARGIN = 16;
    /** 他サービスフラグ.*/
    private boolean mIsOtherService = false;
    /** 年齢.*/
    private int mAge = 0;

    /** 対象コンテンツのチャンネルデータ.*/
    private ChannelInfo mChannel = null;
    /** 視聴可能期限.*/
    private long mEndDate = 0L;
    /** 一ヶ月(30日).*/
    public static final int ONE_MONTH = 30;
    /** サムネイルにかけるシャドウのアルファ値.*/
    private static final float THUMBNAIL_SHADOW_ALPHA = 0.5f;
    /** 外部出力制御.*/
    private ExternalDisplayHelper mExternalDisplayHelper;
    /** 外部出力制御判定フラグ.*/
    private boolean mExternalDisplayFlg = false;
    /** 操作履歴送信.*/
    private SendOperateLog mSendOperateLog = null;
    /** 二回目リモコン送信防止.*/
    private boolean mIsSend = false;
    /** ヘッダーチェック.*/
    private boolean mIsFromHeader = false;
    /** 放送中フラグ.*/
    private boolean mIsVideoBroadcast = false;
    /** チャンネル日付.*/
    private String mChannelDate = null;
    /** サービスID(ぷらら).*/
    private String mServiceId = null;
    /** 視聴可否ステータス.*/
    private ContentUtils.ViewIngType mViewIngType = null;

    /**
     *　コントロールビューを非表示にする.
     */
    private final Runnable mHideCtrlViewThread = new Runnable() {

        /**
         * run
         */
        @Override
        public void run() {
            DTVTLogger.start();
            //外部出力制御の場合実行しない
            if (!mExternalDisplayFlg) {
                hideCtrlView();
            }
            DTVTLogger.end();
        }
    };
    /* player end */

    /**
     * UIを更新するハンドラー.
     */
    private final Handler viewRefresher = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            DTVTLogger.start();
            super.handleMessage(msg);
            if (null == mPlayerController) {
                switch (mDisplayState) {
                    case PLAYER_ONLY:
                        break;
                    default:
                        getScheduleDetailData();
                        break;
                }
                DTVTLogger.end();
                return;
            }

            int currentPosition = mPlayerController.getCurrentPosition();
            int totalDur = mPlayerController.getDuration();
            mVideoCurTime.setText(DateUtils.time2TextViewFormat(currentPosition));
            mVideoTotalTime.setText(DateUtils.time2TextViewFormat(totalDur));
            mVideoSeekBar.setMax(totalDur);
            mVideoSeekBar.setProgress(currentPosition);
            //録画コンテンツを終端まで再生した後は、シークバーを先頭に戻し、先頭で一時停止状態とする
            if (currentPosition == totalDur) {
                setProgress0();
            }
            viewRefresher.sendEmptyMessageDelayed(REFRESH_VIDEO_VIEW, 500);
            DTVTLogger.end();
        }
    };
    /**
     * ハンドラー.
     */
    private final Handler loadHandler = new Handler();
    // endregion

    //region Activity LifeCycle
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeBlack);
        setContentView(R.layout.dtv_contents_detail_main_layout);
        DTVTLogger.start();
        setStatusBarColor(R.color.contents_header_background);
        // プログレスバー表示中でもxボタンクリック可能にする
        findViewById(R.id.base_progress_rl).setClickable(false);
        showProgressBar(true);
        initView();
    }

    @Override
    protected void onResume() {
        DTVTLogger.start();
        super.onResume();
        switch (mDisplayState) {
            case PLAYER_ONLY:
                if (!mIsOncreateOk) {
                    DTVTLogger.end();
                    return;
                }
                initSecurePlayer();
                setPlayerEvent();
                setUserAgeInfo();
                break;
            case PLAYER_AND_CONTENTS_DETAIL:
                if (!mIsOncreateOk) {
                    DTVTLogger.end();
                    return;
                }
                initSecurePlayer();
                setPlayerEvent();
                setUserAgeInfo();
            case CONTENTS_DETAIL_ONLY:
                //BG復帰時にクリップボタンの更新を行う
                DtvContentsDetailFragment dtvContentsDetailFragment = getDetailFragment();
                ContentsDetailDataProvider contentsDetailDataProvider = new ContentsDetailDataProvider(this);
                dtvContentsDetailFragment.mOtherContentsDetailData = contentsDetailDataProvider.checkClipStatus(dtvContentsDetailFragment.mOtherContentsDetailData);
                dtvContentsDetailFragment.resumeClipButton();
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //外部出力制御
        if (mExternalDisplayHelper != null) {
            mExternalDisplayHelper.onPause();
        }
        finishPlayer();
        switch (mDisplayState) {
            case PLAYER_ONLY:
                break;
            case PLAYER_AND_CONTENTS_DETAIL:
            case CONTENTS_DETAIL_ONLY:
                //通信を止める
                if (mContentsDetailDataProvider != null) {
                    StopContentDetailDataConnect stopContentDetailDataConnect = new StopContentDetailDataConnect();
                    stopContentDetailDataConnect.execute(mContentsDetailDataProvider);
                }
                if (mScaledDownProgramListDataProvider != null) {
                    StopScaledProListDataConnect stopScaledProListDataConnect = new StopScaledProListDataConnect();
                    stopScaledProListDataConnect.execute(mScaledDownProgramListDataProvider);
                }
                if (mSendOperateLog != null) {
                    mSendOperateLog.stopConnection();
                }
                stopThumbnailConnect();
                //FragmentにContentsAdapterの通信を止めるように通知する
                DtvContentsChannelFragment channelFragment = getChannelFragment();
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
        if (mExternalDisplayHelper != null) {
            mExternalDisplayHelper.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        //外部出力制御
        mExternalDisplayHelper = null;
        super.onDestroy();
    }
    //endregion

    //region BaseActivity
    @Override
    public void onStartCommunication() {
        DTVTLogger.start();
        super.onStartCommunication();
        switch (mDisplayState) {
            case PLAYER_ONLY:
                break;
            case PLAYER_AND_CONTENTS_DETAIL:
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
                DtvContentsChannelFragment channelFragment = getChannelFragment();
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

    //region private method
    /**
     * ビュー初期化.
     */
    private void initView() {
        mIntent = getIntent();
        mThumbnailRelativeLayout = findViewById(R.id.dtv_contents_detail_layout);
        mContractLeadingView = findViewById(R.id.contract_leading_view);
        Object object = mIntent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
        if (object instanceof RecordedContentsDetailData) { //プレイヤーで再生できるコンテンツ
            mDisplayState = PLAYER_ONLY;
            UserInfoUtils.PairingState pairingState = UserInfoUtils.getPairingState(this, getStbStatus());
            //宅内のみ再生準備
            if (pairingState.equals(UserInfoUtils.PairingState.INSIDE_HOUSE)) {
                initPlayer();
                //外部出力および画面キャプチャ制御
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                mExternalDisplayHelper = createExternalDisplayHelper();
            } else {
                setRemotePlayArrow();
            }
        }

        //ヘッダーの設定
        String sourceClass = mIntent.getStringExtra(DTVTConstants.SOURCE_SCREEN);
        if (sourceClass != null && !sourceClass.isEmpty()) {
            //赤ヘッダーである遷移元クラス名を保持
            setSourceScreenClass(sourceClass);
            enableHeaderBackIcon(false);
        } else {
            //詳細画面から詳細画面への遷移時は戻るアイコンを表示
            enableHeaderBackIcon(true);
        }
        setHeaderColor(false);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        changeGlobalMenuIcon(false);
        setStatusBarColor(false);
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        initContentsView();
    }

    /**
     * initView player.
     */
    private void initSecurePlayer() {
        DTVTLogger.start();
        setCanPlay(false);
        mPlayerController = new SecuredMediaPlayerController(this, true, true, true);
        mPlayerController.setOnStateChangeListener(this);
        mPlayerController.setOnFormatChangeListener(this);
        mPlayerController.setOnPlayerEventListener(this);
        mPlayerController.setOnErrorListener(this);
        mPlayerController.setCaptionDataListener(this);
        mPlayerController.setCurrentCaption(0); // start caption.
        boolean result = DlnaUtils.getActivationState(this);
        if (!result) {
            showProgressBar(false);
            showMessage(getString(R.string.activation_failed_msg));
            return;
        } else {
            String privateHomePath = DlnaUtils.getPrivateDataHomePath(this);
            int ret = mPlayerController.dtcpInit(privateHomePath);
            if (ret != MediaPlayerDefinitions.SP_SUCCESS) {
                showProgressBar(false);
                showMessage(getString(R.string.dtcp_init_failed_msg));
                return;
            }
        }
        showProgressBar(false);
        preparePlayer();
        DTVTLogger.end();
    }
    //endregion

    // region player handle
    /**
     * Playerの初期化.
     */
    private void initPlayer() {
        mSecureVideoPlayer = findViewById(R.id.dtv_contents_detail_main_layout_player_view);
        mScreenWidth = getWidthDensity();
        boolean ok = setCurrentMediaInfo();
        if (!ok) {
            errorExit();
            mIsOncreateOk = false;
            finish();
            DTVTLogger.end();
            return;
        }
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(final MotionEvent e) {
                DTVTLogger.start();
                if (e.getY() > (float) mRecordCtrlView.getHeight() / 3
                        && e.getY() < mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight() / 3) {
                    if (e.getX() < (float) (mScreenWidth / 2 - mVideoPlayPause.getWidth() / 2)
                            && e.getX() > (float) mScreenWidth / 6) { //10秒戻し
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= REWIND_SECOND;
                        if (pos < 0) {
                            pos = 0;
                            setProgress0();
                        }
                        mPlayerController.seekTo(pos);
                        mIsHideOperate = false;
                    }
                    if (e.getX() > mScreenWidth / 2 + mVideoPlayPause.getWidth() / 2
                            && e.getX() < mScreenWidth - mScreenWidth / 6) { //30秒送り
                        int pos = mPlayerController.getCurrentPosition();
                        pos += FAST_SECOND;
                        //pos = pos > mPlayerController.getDuration() ? mPlayerController.getDuration() : pos;
                        int allDu = mPlayerController.getDuration();
                        if (pos >= allDu) {
                            setProgress0();
                            pos = 0;
                        }
                        mPlayerController.seekTo(pos);
                        mIsHideOperate = false;
                    }
                }
                DTVTLogger.end();
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onFling(final MotionEvent e1, final MotionEvent e2,
                                   final float velocityX, final float velocityY) {
                DTVTLogger.start();
                if (e1.getY() > (float) mRecordCtrlView.getHeight() / 3
                        && e2.getY() > (float) mRecordCtrlView.getHeight() / 3
                        && e2.getY() < (float) (mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight() / 3)
                        && e1.getY() < (float) (mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight() / 3)) {
                    if (e1.getX() > e2.getX() && e1.getX() < (float) (mScreenWidth / 2 - mVideoPlayPause.getWidth() / 2)) {
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= REWIND_SECOND;
                        pos = pos < 0 ? 0 : pos;
                        mPlayerController.seekTo(pos);
                        mIsHideOperate = false;
                    } else if (e1.getX() < e2.getX() && e1.getX() > (float) (mScreenWidth / 2 + mVideoPlayPause.getWidth() / 2)) {
                        int pos = mPlayerController.getCurrentPosition();
                        pos += FAST_SECOND;
                        pos = pos > mPlayerController.getDuration() ? mPlayerController.getDuration() : pos;
                        mPlayerController.seekTo(pos);
                        mIsHideOperate = false;
                    }
                }
                DTVTLogger.end();
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        mIsOncreateOk = true;
        DTVTLogger.end();
    }
    /**
     * prepair player.
     */
    private void preparePlayer() {
        DTVTLogger.start();
        final Map<String, String> additionalHeaders = new HashMap<>();
        try {
            mPlayerController.setDataSource(mCurrentMediaInfo, additionalHeaders, 0);
        } catch (IOException e) {
            DTVTLogger.debug(e);
            setCanPlay(false);
            DTVTLogger.end();
            return;
        }
        mPlayerController.setScreenOnWhilePlaying(true);
        mSecureVideoPlayer.init(mPlayerController);
        initPlayerView();
        setCanPlay(true);
        playStart();
        DTVTLogger.end();
    }

    /**
     * リモート視聴以外はそのまま再生を行う。リモート視聴の場合は再生可否のチェックを行う.
     */
    private void playStart() {
        if (!mCurrentMediaInfo.isRemote()) {
            //リモート視聴ではないので、そのまま実行する
            playStartOrigin();
            return;
        }

        //リモート視聴なので、設定ファイルの内容に応じて判定を行う
        ProcessSettingFile processSettingFile = new ProcessSettingFile(this, false);
        //リモート視聴の明示
        processSettingFile.setIsRemote(true);
        //コールバック指定付きで処理を開始する
        processSettingFile.controlAtSettingFile(new ProcessSettingFile.ProcessSettingFileCallBack() {
            @Override
            public void onCallBack(final boolean dialogSwitch) {
                //今回は確実に通常ダイアログなので、dialogSwitchの内容は無視していい
                //設定ファイルの内容に問題は無かったので、再生を行う
                playStartOrigin();
            }
        });
    }

    /**
     * 元の再生開始.
     * (リモート視聴の再生開始可否の為に、リネーム後に前チェックを追加)
     */
    private void playStartOrigin() {
        DTVTLogger.start();
        synchronized (this) {
            if (mCanPlay) {
                playButton(true);
                mPlayerController.start();
            }
        }
        DTVTLogger.end();
    }

    /**
     * playing pause.
     */
    private void playPause() {
        DTVTLogger.start();
        synchronized (this) {
            if (mCanPlay) {
                playButton(false);
                mPlayerController.pause();
            }
        }
        DTVTLogger.end();
    }

    /**
     * set player event.
     */
    private void setPlayerEvent() {
        DTVTLogger.start();
        if (null == mRecordCtrlView) {
            DTVTLogger.end();
            return;
        }
        mRecordCtrlView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                DTVTLogger.start();
                if (mVideoPlayPause.getVisibility() == View.VISIBLE) {
                    mGestureDetector.onTouchEvent(motionEvent);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (mVideoCtrlBar.getVisibility() == View.VISIBLE) {
                        if (mIsHideOperate) {
                            hideCtrlView();
                        }
                    } else {
                        if (!mIsVideoBroadcast) {
                            mVideoPlayPause.setVisibility(View.VISIBLE);
                            mVideoRewind10.setVisibility(View.VISIBLE);
                            mVideoFast30.setVisibility(View.VISIBLE);
                            mProgressLayout.setVisibility(View.VISIBLE);
                            mVideoTotalTime.setVisibility(View.VISIBLE);
                            mVideoCurTime.setVisibility(View.VISIBLE);
                        }
                        mVideoCtrlBar.setVisibility(View.VISIBLE);
                        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            mTvTitle.setVisibility(View.VISIBLE);
                            mTvLogo.setVisibility(View.VISIBLE);
                        }
                        mSecureVideoPlayer.setBackgroundResource(R.mipmap.thumb_material_mask_overlay_gradation);
                        //setPlayEvent();
                    }
                    hideCtrlViewAfterOperate();
                }
                DTVTLogger.end();
                return true;
            }
        });
        DTVTLogger.end();
    }
    // endregion

    /**
     * hide video ctrl mView.
     */
    private void hideCtrlView() {
        DTVTLogger.start();

        if (mSecureVideoPlayer == null) {
            //設定ファイルによるアプリ動作停止の場合、下記の物がヌルになっている可能性がある。その場合は処理は行わない
            DTVTLogger.end("mSecureVideoPlayer is null");
            return;
        }

        mVideoPlayPause.setVisibility(View.INVISIBLE);
        mVideoRewind10.setVisibility(View.INVISIBLE);
        mVideoFast30.setVisibility(View.INVISIBLE);
        mVideoCtrlBar.setVisibility(View.INVISIBLE);
        mTvTitle.setVisibility(View.INVISIBLE);
        mTvLogo.setVisibility(View.INVISIBLE);
        mSecureVideoPlayer.setBackgroundResource(0);
        DTVTLogger.end();
    }

    /**
     * hide ctrl mView.
     */
    private void hideCtrlViewAfterOperate() {
        DTVTLogger.start();
        sCtrlHandler.removeCallbacks(mHideCtrlViewThread);
        sCtrlHandler.postDelayed(mHideCtrlViewThread, HIDE_IN_3_SECOND);
        mIsHideOperate = true;
        DTVTLogger.end();
    }

    /**
     * シークバーを先頭に戻す.
     */
    private void setProgress0() {
        DTVTLogger.start();
        mVideoSeekBar.setProgress(0);
        playButton(true);
        DTVTLogger.end();
    }

    /**
     * play button function.
     * @param isPlay 再生(一時停止)ボタン
     */
    private void playButton(final boolean isPlay) {
        DTVTLogger.start();
        if (null == mVideoPlayPause) {
            return;
        }
        View child0 = mVideoPlayPause.getChildAt(0);
        View child1 = mVideoPlayPause.getChildAt(1);
        if (isPlay) {
            child0.setVisibility(View.GONE);
            child1.setVisibility(View.VISIBLE);
        } else {
            child0.setVisibility(View.VISIBLE);
            child1.setVisibility(View.GONE);
        }
        DTVTLogger.end();
    }

    /**
     * set current player info.
     *
     * @return whether succeed
     */
    @SuppressWarnings("OverlyLongMethod")
    private boolean setCurrentMediaInfo() {
        DTVTLogger.start();
        RecordedContentsDetailData datas = mIntent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
        if (null == datas) {
            DTVTLogger.end();
            return false;
        }
        String url = datas.getResUrl();
        String icon = datas.getUpnpIcon();
        setPlayerLogoThumbnail(icon);
        if (null == url || 0 == url.length()) {
            showMessage(getApplicationContext().getString(R.string.contents_player_bad_contents_info));
            DTVTLogger.end();
            return false;
        }
        long size = Integer.parseInt(datas.getSize());
        String durationStr = datas.getDuration();
        long duration = getDuration(durationStr);
        String type = datas.getVideoType();
        int bitRate = Integer.parseInt(datas.getBitrate());
        String title = datas.getTitle();
        setTitleText(title);
        Uri uri = Uri.parse(url);
        String contentFormat = "CONTENTFORMAT";
        String type2;
        int isDtcp = type.indexOf("application/x-dtcp1");
        if (isDtcp > 0) {
            String http = "http-get:*:";
            int startPos = http.length() - 1;
            int endPos = type.indexOf(contentFormat);
            if (endPos > 0 && startPos < endPos && startPos < type.length() && endPos < type.length()) {
                String startFormat = type.substring(startPos + 1, endPos);
                String endFormat = type.substring(endPos);
                endFormat = endFormat.substring(0, endFormat.indexOf(":"));
                contentFormat = startFormat + endFormat;
                type2 = "application/x-dtcp1";
            } else {
                DTVTLogger.debug("setCurrentMediaInfo failed");
                return false;
            }
        } else {
            type2 = type;
        }
        mIsVideoBroadcast = datas.isIsLive();
        boolean isSupportedByteSeek = false;
        boolean isSupportedTimeSeek = false;
        boolean isAvailableConnectionStalling = false;
        boolean isRemote = false;
        if (mIsVideoBroadcast) {
            isAvailableConnectionStalling = true;
        } else {
            isSupportedByteSeek = true;
            isSupportedTimeSeek = true;
            //録画番組ローカル再生
            if ((ContentsAdapter.DOWNLOAD_STATUS_COMPLETED == datas.getDownLoadStatus())) {
                //ローカルファイルパス
                String dlFile = datas.getDlFileFullPath();
                File file = new File(dlFile);
                if (!file.exists()) {
                    DTVTLogger.debug(file  + " not exists");
                    onError("再生するファイルは存在しません");
                    return false;
                }
                uri = Uri.parse(LOCAL_FILE_PATH + dlFile);
            } else {
                isAvailableConnectionStalling = true;
            }
        }
        mCurrentMediaInfo = new MediaVideoInfo(
                uri,                             //メディアのURI
                type2,                           //"application/x-dtcp1", "video/mp4", メディアのMimeType
                size,                            //メディアのサイズ(Byte)
                duration,                        //メディアの総再生時間(ms)
                bitRate,                         //メディアのビットレート(Byte/sec)
                isSupportedByteSeek,             //DLNAのByteシークをサポートしているか
                isSupportedTimeSeek,             //DLNAのTimeシークをサポートしているか
                isAvailableConnectionStalling,   //DLNAのAvailableConnectionStallingかどうか
                mIsVideoBroadcast,                //放送中コンテンツかどうか
                isRemote,                        //リモートアクセスコンテンツかどうか
                title,                           //メディアのタイトル
                contentFormat                    //DIDLのres protocolInfoの3番目のフィールド
        );
        DTVTLogger.end();
        return true;
    }

    /**
     * 機能：「duration="0:00:42.000"」/「duration="0:00:42"」からmsへ変換.
     *
     * @param durationStr durationStr
     * @return duration in ms
     */
    private long getDuration(final String durationStr) {
        DTVTLogger.start();
        long ret = 0;
        String[] strs1 = durationStr.split(":");
        if (3 == strs1.length) {
            ret = 60 * 60 * 1000 * Integer.parseInt(strs1[0]) + 60 * 1000 * Integer.parseInt(strs1[1]);
            String ss = strs1[2];
            int i = ss.indexOf('.');
            if (i <= 0) {
                ret += 1000L * Integer.parseInt(ss);
            } else {
                String ss1 = ss.substring(0, i);
                String ss2 = ss.substring(i + 1, ss.length());
                ret += 1000L * Integer.parseInt(ss1);
                try {
                    ret += Integer.parseInt(ss2);
                } catch (Exception e2) {
                    DTVTLogger.debug("TvPlayerActivity::getDuration(), skip ms");
                }
            }
        }
        DTVTLogger.end();
        return ret;
    }

    /**
     * showMessage.
     *
     * @param msg エラーダイアログに表示する文言
     */
    private void showMessage(final String msg) {
        DTVTLogger.start();
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        customDialog.setContent(msg);
        customDialog.showDialog();
        DTVTLogger.end();
    }

    /**
     * errorExit.
     */
    private void errorExit() {
        DTVTLogger.start();
        // TODO エラー表示の要修正
        showMessage(getString(R.string.contents_player_bad_contents_info));
        setCanPlay(false);
        DTVTLogger.end();
    }

    /**
     * set can play.
     *
     * @param state state
     */
    private void setCanPlay(final boolean state) {
        DTVTLogger.start();
        synchronized (this) {
            mCanPlay = state;
        }
        DTVTLogger.end();
    }

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
            int ageReq = mDetailData.getAge();
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
        Fragment currentFragment = mFragmentFactory.createFragment(1);
        return (DtvContentsChannelFragment) currentFragment;
    }

    /**
     * コンテンツ詳細データ取得.
     */
    private void getChannelDetailData() {
        DtvContentsChannelFragment channelFragment = getChannelFragment();
        channelFragment.loadComplete();
        if (mChannel != null) {
            channelFragment.setChannelDataChanged(mChannel);
            mDateIndex = 0;
            getChannelDetailByPageNo();
        }
    }

    /**
     * ページングでチャンネルデータ取得.
     */
    private void getChannelDetailByPageNo() {
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
                //TODO channelNoが未指定の場合。エラー定義未決定のため一旦エラーを表示して戻る
                DTVTLogger.error("No channel number");
                Toast.makeText(this, "No channel number", Toast.LENGTH_SHORT).show();
                channelLoadCompleted();
                return;
            }
            mScaledDownProgramListDataProvider.getProgram(channelNos, mDateList);
        } else {
            channelLoadCompleted();
        }
    }

    /**
     * チャンネルロード完了.
     */
    private void channelLoadCompleted() {
        DtvContentsChannelFragment channelFragment = getChannelFragment();
        channelFragment.loadComplete();
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
    private void setThumbnailText(final String content) {
        UserState userState = UserInfoUtils.getUserState(this);

        if (userState.equals(UserState.LOGIN_NG) && !mIsOtherService) {
            loginNgDisplay();
        } else {
            if (UserInfoUtils.isContract(this) || mIsOtherService) {
                setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
                TextView startAppIcon = findViewById(R.id.view_contents_button_text);
                startAppIcon.setVisibility(View.GONE);
                if (content.isEmpty()) {
                    mThumbnailBtn.setVisibility(View.GONE);
                    mContractLeadingView.setVisibility(View.GONE);
                } else {
                    mThumbnailBtn.setVisibility(View.VISIBLE);
                    startAppIcon.setVisibility(View.VISIBLE);
                    startAppIcon.setText(content);
                }
            } else {
                noAgreementDisplay();
            }
        }
    }

    /**
     * 未ログイン状態のサムネイル描画.
     */
    private void loginNgDisplay() {
        TextView contractLeadingText = findViewById(R.id.contract_leading_text);
        Button contractLeadingButton = findViewById(R.id.contract_leading_button);
        DTVTLogger.debug("userState:---" + UserState.LOGIN_NG);
        String message = getString(R.string.main_setting_logon_request_error_message);
        String buttonText = getString(R.string.contents_detail_login_button);
        contractLeadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DAccountUtils.startDAccountApplication(ContentDetailActivity.this);
            }
        });
        mContractLeadingView.setVisibility(View.VISIBLE);
        contractLeadingText.setText(message);
        contractLeadingButton.setText(buttonText);
    }

    /**
     * 未契約状態のサムネイル描画.
     */
    private void noAgreementDisplay() {
        UserInfoUtils.PairingState pairingState = UserInfoUtils.getPairingState(this, getStbStatus());
        TextView contractLeadingText = findViewById(R.id.contract_leading_text);
        Button contractLeadingButton = findViewById(R.id.contract_leading_button);
        String message = "";
        String buttonText = "";
        switch (pairingState) {
            case INSIDE_HOUSE:
            case OUTSIDE_HOUSE:
                message = getString(R.string.contents_detail_no_agreement);
                buttonText = getString(R.string.contents_detail_contract_leading_button);
                break;
            case NO_PAIRING:
                message = getString(R.string.contents_detail_hikari_tv_agreement);
                buttonText = getString(R.string.contents_detail_contract_leading_button);
                break;
            default:
                break;
        }
        contractLeadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (getStbStatus()) {
                    contentDetailRemoteController();
                } else {
                    startBrowser(UrlConstants.WebUrl.CONTRACT_URL);
                }
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
        if (!TextUtils.isEmpty(title)) {
            setTitleText(title);
        }
        setThumbnail();
        if (!TextUtils.isEmpty(url)) {
            if (mThumbnailProvider == null) {
                mThumbnailProvider = new ThumbnailProvider(this);
            }
            if (!mIsDownloadStop) {
                mThumbnail.setTag(url);
                Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mThumbnail, url);
                if (bitmap != null) {
                    //サムネイル取得失敗時は取得失敗画像をセットする
                    mThumbnail.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     * サムネイル画像を表示する.
     */
    private void setThumbnail() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getWidthDensity(), getWidthDensity() / SCREEN_RATIO_WIDTH_16 * SCREEN_RATIO_HEIGHT_9);
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.error_movie);
        mThumbnail.setLayoutParams(layoutParams);
        mThumbnail.setImageBitmap(bitmap);
    }

    /**
     * データの初期化.
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod", "EnumSwitchStatementWhichMissesCases"})
    private void initContentData() {
        mFrameLayout = findViewById(R.id.header_watch_by_tv);
        // タブ数を先に決定するため、コンテンツ詳細のデータを最初に取得しておく
        mDetailData = mIntent.getParcelableExtra(RECOMMEND_INFO_BUNDLE_KEY);
        if (mDetailData != null) {
            int serviceId = mDetailData.getServiceId();
            if (serviceId == DTV_CONTENTS_SERVICE_ID
                    || serviceId == D_ANIMATION_CONTENTS_SERVICE_ID
                    || serviceId == DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                // 他サービス(dtv/dtvチャンネル/dアニメ)フラグを立てる
                mIsOtherService = true;
            }
            // STBに接続している　「テレビで視聴」が表示
            if (getStbStatus()) {
                if (mIsOtherService) {
                    if (serviceId == D_ANIMATION_CONTENTS_SERVICE_ID) {
                        // リモコンUIのリスナーを設定
                        createRemoteControllerView(true);
                        mIsControllerVisible = true;
                        mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_bottom_corner_d_anime, null));
                        setStartRemoteControllerUIListener(this);
                        //「serviceId」が「15」(dTVコンテンツ)の場合
                    } else if (serviceId == DTV_CONTENTS_SERVICE_ID) {
                        // 「reserved1」が「1」STB視聴不可
                        if (!CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved1())) {
                            createRemoteControllerView(true);
                            mIsControllerVisible = true;
                            mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_bottom_corner_dtv, null));
                            setStartRemoteControllerUIListener(this);
                        }
                    } else if (serviceId == DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                        createRemoteControllerView(true);
                        mIsControllerVisible = true;
                        mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
                        setStartRemoteControllerUIListener(this);
                    }
                } else {
                    createRemoteControllerView(true);
                }
            }

            //コンテンツタイプ取得
            ContentUtils.ContentsType type = ContentUtils.getRecommendContentsType(mDetailData);

            switch (type) {
                case PURE_DTV:
                    //「mobileViewingFlg」が「0」の場合モバイル視聴不可
                    if (MOBILEVIEWINGFLG_FLAG_ZERO.equals(mDetailData.getMobileViewingFlg())) {
                        //モバイル視聴不可なので、"テレビで視聴できます"を表示
                        setThumbnailText(getResources().getString(
                                R.string.contents_detail_thumbnail_text));
                    } else {
                        //モバイル視聴可なので、"dTVで視聴"を表示
                        setThumbnailText(getResources().getString(
                                R.string.dtv_content_service_start_text));
                    }
                    break;
                case PURE_DTV_CHANNEL:
                case PURE_DTV_CHANNEL_MISS:
                case PURE_DTV_CHANNEL_RELATION:
                    setThumbnailText(getResources().getString(R.string.dtv_channel_service_start_text));
                    break;
                case D_ANIME_STORE:
                    setThumbnailText(getResources().getString(R.string.d_anime_store_content_service_start_text));
                    break;
                default:
                    break;
            }
            setTitleAndThumbnail(mDetailData.getTitle(), mDetailData.getThumb());
        } else {  //plalaサーバーから
            mDetailData = mIntent.getParcelableExtra(PLALA_INFO_BUNDLE_KEY);
            if (getStbStatus()) {
                createRemoteControllerView(true);
            }
        }
        boolean isTV = false;
        if (mIsOtherService) {
            String date = "";
            ContentUtils.ContentsType contentsType = ContentUtils.
                    getContentsTypeByRecommend(mDetailData.getServiceId(), mDetailData.getCategoryId());
            if (contentsType == ContentUtils.ContentsType.TV) {
                isTV = true;
                //番組(m/d（曜日）h:ii - h:ii)
                date = DateUtils.getContentsDateString(mDetailData.getmStartDate(), mDetailData.getmEndDate());
                // コンテンツ詳細(TVの場合は、チャンネルタブを追加設定する)
                mTabNames = getResources().getStringArray(R.array.contents_detail_tabs_tv_ch);
            } else {
                if (contentsType == ContentUtils.ContentsType.VOD) {
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
                mTabNames = getResources().getStringArray(R.array.contents_detail_tab_vod);
            }
            mDetailData.setChannelDate(date);
        } else {
            // ディフォルトはチャンネルタブを付いて、コールバック来たら、再設定
            mTabNames = getResources().getStringArray(R.array.contents_detail_tabs_tv_ch);
        }

        mFragmentFactory = new DtvContentsDetailFragmentFactory();
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
                if (position == 1) {
                    getChannelFragment().initLoad();
                }
                loadHandler.postDelayed(loadRunnable, 1200);
            }
        });
        //レコメンド（serviceId 44）若しくはぷららの場合
        if (!mIsOtherService) {
            findViewById(R.id.remote_control_view).setVisibility(View.INVISIBLE);
            viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
        } else {
            mServiceId = mDetailData.getChannelId();
            if (isTV && !TextUtils.isEmpty(mServiceId)) {
                showProgressBar(true);
                getChannelInfo();
            } else {
                sendOperateLog();
                showProgressBar(false);
            }
        }
    }

    /**
     * データ取得用Runnable.
     */
    private final Runnable loadRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager.getCurrentItem() == 0) {
                getScheduleDetailData();
            } else {
                getChannelDetailData();
            }
        }
    };

    /**
     * initPlayerView mView.
     */
    private void initPlayerView() {
        DTVTLogger.start();
        RelativeLayout playerViewLayout;
        playerViewLayout = findViewById(R.id.dtv_contents_detail_main_layout_player_rl);
        playerViewLayout.setVisibility(View.VISIBLE);
        playerViewLayout.removeView(mRecordCtrlView);
        playerViewLayout.getKeepScreenOn();
        mRecordCtrlView = (RelativeLayout) View.inflate(this, R.layout.tv_player_ctrl_video_record, null);
        mVideoPlayPause = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_player_pause_fl);
        RelativeLayout mVideoCtrlRootView = mRecordCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_video_root);
        mVideoRewind10 = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_10_tv);
        mVideoFast30 = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_30_tv);
        mVideoCtrlBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_control_bar_iv);
        mVideoCurTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_cur_time_tv);
        mVideoFullScreen = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv);
        mVideoTotalTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_total_time_tv);
        mVideoSeekBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
        mProgressLayout = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_progress_ll);
        TextView nowTextView = mRecordCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_now_on_air_tv);
        mTvTitle = playerViewLayout.findViewById(R.id.tv_player_main_layout_video_ctrl_player_title);
        mTvLogo = playerViewLayout.findViewById(R.id.tv_player_main_layout_video_ctrl_player_logo);
        mTvTitle.setText(getTitleText());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getWidthDensity(), getHeightDensity());
        setScreenSize(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, layoutParams);
        nowTextView.setVisibility(View.GONE);
        mVideoPlayPause.setOnClickListener(this);
        mVideoFullScreen.setOnClickListener(this);
        mVideoCtrlRootView.setOnClickListener(this);
        setSeekBarListener(mVideoSeekBar);
        playerViewLayout.addView(mRecordCtrlView);
        //初期化の時点から、handlerにmsgを送る
        viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
        hideCtrlView();
        if (mPlayerController != null) {
            if (mPlayerController.isPlaying()) {
                mVideoPlayPause.getChildAt(0).setVisibility(View.GONE);
                mVideoPlayPause.getChildAt(1).setVisibility(View.VISIBLE);
            }
        }
        DTVTLogger.end();
    }

    /**
     * set thumbnail(player).
     *
     * @param url URL
     */
    private void setPlayerLogoThumbnail(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (mThumbnailProvider == null) {
                mThumbnailProvider = new ThumbnailProvider(this);
            }
            mTvLogo.setTag(url);
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mTvLogo, url);
            if (bitmap != null) {
                mTvLogo.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * set screen size.
     *
     * @param mIsLandscape 端末の縦横判定
     * @param playerParams LayoutParams
     */
    private void setScreenSize(final boolean mIsLandscape, final LinearLayout.LayoutParams playerParams) {
        DTVTLogger.start();
        if (mIsLandscape) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            setTitleVisibility(false);
            playerParams.height = getScreenHeight();
            if (mPlayerController != null) {
                int ratio = mPlayerController.getVideoAspectRatio();
                if (ratio == SecureVideoView.RATIO_4x3) {
                    playerParams.width = (getHeightDensity() * SCREEN_RATIO_WIDTH_4 / SCREEN_RATIO_HEIGHT_3);
                    playerParams.gravity = Gravity.CENTER_HORIZONTAL;
                } else {
                    playerParams.width = getScreenWidth();
                }
            }
            if (playerParams.height > playerParams.width) {
                playerParams.height = getWidthDensity();
                if (mPlayerController != null) {
                    int widthRatio = mPlayerController.getVideoAspectRatioWidth();
                    int heightRatio = mPlayerController.getVideoAspectRatioHeight();
                    playerParams.width = getWidthDensity() / heightRatio * widthRatio;
                }
            }
            mScreenWidth = playerParams.width;
            setPlayerProgressView(true);
            setRemoteControllerViewVisibility(View.GONE);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            mScreenWidth = getWidthDensity();
            if (mPlayerController != null) {
                int ratio = mPlayerController.getVideoAspectRatio();
                if (ratio == SecureVideoView.RATIO_4x3) {
                    playerParams.height = (getWidthDensity() * SCREEN_RATIO_HEIGHT_3 / SCREEN_RATIO_WIDTH_4);
                } else {
                    playerParams.height = (getWidthDensity() * SCREEN_RATIO_HEIGHT_9 / SCREEN_RATIO_WIDTH_16);
                }
            }
            if (getHeightDensity() < getWidthDensity()) {
                playerParams.width = getHeightDensity();
                if (mPlayerController != null) {
                    int widthRatio = mPlayerController.getVideoAspectRatioWidth();
                    int heightRatio = mPlayerController.getVideoAspectRatioHeight();
                    playerParams.height = getHeightDensity() / widthRatio * heightRatio;
                }
            }
            setTitleVisibility(true);
            setPlayerProgressView(false);
            setRemoteControllerViewVisibility(View.VISIBLE);
        }
        mThumbnailRelativeLayout.setLayoutParams(playerParams);
        DTVTLogger.end();
    }

    /**
     * プレイヤーの場合スクロールできない.
     *
     * @param isLandscape 端末の縦横判定
     */
    @SuppressWarnings("OverlyLongMethod")
    private void setPlayerProgressView(final boolean isLandscape) {
        if (isLandscape) {
            //端末横向き
            if (mIsVideoBroadcast) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(0, 0, (int) getDensity() * FULL_SCREEN_BUTTON_RIGHT_MARGIN,
                        (int) getDensity() * FULL_SCREEN_BUTTON_RIGHT_MARGIN);
                mVideoFullScreen.setLayoutParams(layoutParams);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, (int) getDensity() * MEDIA_CONTROL_BAR_UNDER_MARGIN);
                mProgressLayout.setLayoutParams(layoutParams);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins((int) getDensity() * SEEKBAR_TIME_LATERAL_MARGIN, 0, 0, 0);
                mVideoCurTime.setLayoutParams(layoutParams);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(0, 0, (int) getDensity() * FULL_SCREEN_BUTTON_RIGHT_MARGIN, 0);
                mVideoFullScreen.setLayoutParams(layoutParams);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.START_OF, R.id.tv_player_ctrl_now_on_air_full_screen_iv);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(0, 0, (int) getDensity() * SEEKBAR_TIME_LATERAL_MARGIN, 0);
                mVideoTotalTime.setLayoutParams(layoutParams);

                mVideoCtrlBar.removeView(mVideoCurTime);
                mVideoCtrlBar.removeView(mVideoFullScreen);
                mVideoCtrlBar.removeView(mVideoTotalTime);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.END_OF, R.id.tv_player_ctrl_now_on_air_cur_time_tv);
                layoutParams.addRule(RelativeLayout.START_OF, R.id.tv_player_ctrl_now_on_air_total_time_tv);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(0, 0, 0, (int) getDensity() * SEEKBAR_BOTTOM_MARGIN);
                mVideoSeekBar.setLayoutParams(layoutParams);

                mProgressLayout.addView(mVideoCurTime);
                mProgressLayout.addView(mVideoFullScreen, 2);
                mProgressLayout.addView(mVideoTotalTime, 3);
            }
        } else {
            //端末縦向き
            if (mProgressLayout.getChildCount() > 1) {
                mProgressLayout.removeViewAt(0);
                mProgressLayout.removeViewAt(1);
                mProgressLayout.removeViewAt(1);
                mVideoCtrlBar.addView(mVideoCurTime);
                mVideoCtrlBar.addView(mVideoFullScreen);
                mVideoCtrlBar.addView(mVideoTotalTime);
            }
        }
    }

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
     * スクリーンのHeightを取得.
     *
     * @return Height
     */
    private int getScreenHeight() {
        return getHeightDensity() + getNavigationBarHeight(true);
    }

    /**
     * スクリーンのWidthを取得.
     *
     * @return Width
     */
    private int getScreenWidth() {
        return getWidthDensity() + getNavigationBarHeight(false);
    }


    /**
     * set seek bar listener.
     *
     * @param seekBar シークバー
     */
    private void setSeekBarListener(final SeekBar seekBar) {
        DTVTLogger.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
                mVideoCurTime.setText(DateUtils.time2TextViewFormat(i));
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
                DTVTLogger.start();
                viewRefresher.removeMessages(REFRESH_VIDEO_VIEW);
                DTVTLogger.end();
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                DTVTLogger.start();
                int progress = seekBar.getProgress();
                mPlayerController.seekTo(progress);
                viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
                DTVTLogger.end();
            }
        });
        DTVTLogger.end();
    }

    /**
     * 詳細Viewの初期化.
     */
    private void initContentsView() {
        //サムネイル取得
        mThumbnail = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail);
        mThumbnailBtn = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail_btn);
        mThumbnailBtn.setOnClickListener(this);
        switch (mDisplayState) {
            case PLAYER_ONLY:
                setPlayerScroll();
                break;
            case PLAYER_AND_CONTENTS_DETAIL:
                setPlayerScroll();
            case CONTENTS_DETAIL_ONLY:
                mViewPager = findViewById(R.id.dtv_contents_detail_main_layout_vp);
                initContentData();
                initTab();
                break;
            default:
                break;
        }
    }

    /**
     * プレイヤーの場合スクロールできない.
     */
    private void setPlayerScroll() {
        LinearLayout parentLayout = findViewById(R.id.dtv_contents_detail_main_layout_ll);
        LinearLayout scrollLayout = findViewById(R.id.contents_detail_scroll_layout);
        scrollLayout.removeViewAt(0);
        parentLayout.addView(mThumbnailRelativeLayout, 0);
        setThumbnailInvisible();
    }

    /**
     * サムネイル画像の非表示.
     */
    private void setThumbnailInvisible() {
        findViewById(R.id.rl_dtv_contents_detail_tab).setVisibility(View.GONE);
        mThumbnail.setVisibility(View.GONE);
        mThumbnailBtn.setVisibility(View.GONE);
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
            Fragment fragment = mFragmentFactory.createFragment(position);
            //Fragmentへデータを渡す
            Bundle args = new Bundle();
            args.putParcelable(RECOMMEND_INFO_BUNDLE_KEY, mDetailData);
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
                mDetailFullData.getAvail_start_date(),
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
     * ビューページャの再設定.
     */
    private void setViewPagerTab() {
        mTabNames = getResources().getStringArray(R.array.contents_detail_tab_vod);
        mTabLayout.resetTabView(mTabNames);
        mFragmentFactory.delFragment();
        mViewPager.addOnPageChangeListener(null);
        mContentsDetailPagerAdapter.notifyDataSetChanged();
    }
    //region ContentsDetailDataProvider.ApiDataProviderCallback
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onContentsDetailInfoCallback(final VodMetaFullData contentsDetailInfo, final boolean clipStatus) {

        //詳細情報取得して、更新する
        if (contentsDetailInfo != null) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            mDetailFullData = contentsDetailInfo;
            if (TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
                //tv_programの場合
                if (TV_SERVICE_FLAG_HIKARI.equals(mDetailFullData.getmTv_service())) {
                    //tv_serviceは0の場合
                    setRecordingData(detailFragment);
                } else if (TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(mDetailFullData.getmTv_service())) {
                    //tv_serviceは1の場合
                    if (CONTENT_TYPE_FLAG_ONE.equals(mDetailFullData.getmContent_type())
                            || CONTENT_TYPE_FLAG_TWO.equals(mDetailFullData.getmContent_type())) {
                        //contents_typeは1又は2の場合
                        if (comparisonStartTime()) {
                            //VOD配信日時(vod_start_date) > 現在時刻
                            setRecordingData(detailFragment);
                        }
                    } else {
                        //contents_typeは0又は未設定の場合
                        setRecordingData(detailFragment);
                    }
                }
            }
            String dispType = mDetailFullData.getDisp_type();
            String searchOk = mDetailFullData.getmSearch_ok();
            String dTv = mDetailFullData.getDtv();
            String dTvType = mDetailFullData.getDtvType();
            detailFragment.mOtherContentsDetailData.setTitle(mDetailFullData.getTitle());
            if (DTV_FLAG_ONE.equals(dTv)) {
                setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmDtv_thumb_640_360());
            } else {
                setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmThumb_640_360());
            }
            detailFragment.mOtherContentsDetailData.setVodMetaFullData(contentsDetailInfo);
            detailFragment.mOtherContentsDetailData.setDetail(mDetailFullData.getSynop());
            // コンテンツ状態を反映
            detailFragment.mOtherContentsDetailData.setClipStatus(clipStatus);
            detailFragment.mOtherContentsDetailData.setClipExec(ClipUtils.isCanClip(
                    UserInfoUtils.getUserState(this), dispType, searchOk, dTv, dTvType));
            detailFragment.mOtherContentsDetailData.setDispType(dispType);
            detailFragment.mOtherContentsDetailData.setSearchOk(searchOk);
            detailFragment.mOtherContentsDetailData.setDtv(dTv);
            detailFragment.mOtherContentsDetailData.setDtvType(dTvType);
            detailFragment.mOtherContentsDetailData.setCrId(mDetailFullData.getCrid());
            detailFragment.mOtherContentsDetailData.setEventId(mDetailFullData.getmEvent_id());
            detailFragment.mOtherContentsDetailData.setTitleId(mDetailFullData.getTitle_id());
            detailFragment.mOtherContentsDetailData.setRvalue(mDetailFullData.getR_value());
            detailFragment.mOtherContentsDetailData.setCopy(mDetailFullData.getmCopy());
            detailFragment.mOtherContentsDetailData.setM4kflg(mDetailFullData.getM4kflg());
            detailFragment.mOtherContentsDetailData.setAdinfoArray(mDetailFullData.getmAdinfo_array());
            detailFragment.mOtherContentsDetailData.setmStartDate(String.valueOf(mDetailFullData.getPublish_start_date()));
            String date = null;
            ContentUtils.ContentsType contentsType = ContentUtils.getContentsTypeByPlala(mDetailFullData.getDisp_type(),
                    mDetailFullData.getmTv_service(), mDetailFullData.getmContent_type(), mDetailFullData.getAvail_end_date(),
                    mDetailFullData.getmVod_start_date(), mDetailFullData.getmVod_end_date(), mDetailFullData.getEstFlag(),
                    mDetailFullData.getmChsvod());
            if (contentsType == ContentUtils.ContentsType.TV) {
                //番組(m/d（曜日）h:ii - h:ii)
                date = DateUtils.getContentsDateString(mDetailFullData.getPublish_start_date(), mDetailFullData.getPublish_end_date());
            } else {
                setViewPagerTab();
                if (DateUtils.isBefore(mDetailFullData.getAvail_start_date())) {
                    //配信前 m/d（曜日）から
                    date = DateUtils.getContentsDateString(this, mDetailFullData.getAvail_start_date(), true);
                } else {
                    if (contentsType == ContentUtils.ContentsType.VOD) {
                        //VOD(m/d（曜日）まで)
                        date = DateUtils.getContentsDetailVodDate(this, mDetailFullData.getAvail_end_date());
                    } else if (contentsType == ContentUtils.ContentsType.DCHANNEL_VOD_OVER_31) {
                        //VOD(見逃し)
                        date = StringUtils.getConnectStrings(
                                getString(R.string.contents_detail_hikari_d_channel_miss_viewing));
                    } else if (contentsType == ContentUtils.ContentsType.DCHANNEL_VOD_31) {
                        //VOD(見逃し | m/d（曜日）まで)
                        date = DateUtils.getContentsDetailVodDate(this, mDetailFullData.getmVod_end_date());
                        date = StringUtils.getConnectStrings(
                                getString(R.string.contents_detail_hikari_d_channel_miss_viewing_separation),
                                date);
                    }
                }
            }
            detailFragment.mOtherContentsDetailData.setChannelDate(date);
            detailFragment.noticeRefresh();
            String[] credit_array = mDetailFullData.getmCredit_array();
            if (credit_array != null && credit_array.length > 0) {
                mContentsDetailDataProvider.getRoleListData();
            }
            if (!TextUtils.isEmpty(mDetailFullData.getmService_id())) {
                mServiceId = mDetailFullData.getmService_id();
                getChannelInfo();
            }
            if (DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
                if (getStbStatus()) {
                    createRemoteControllerView(true);
                    mIsControllerVisible = true;
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
                    setStartRemoteControllerUIListener(this);
                }
                String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(this));
                UserState userState = UserInfoUtils.getUserState(this);
                //ひかりTV未契約の場合
                if (!UserInfoUtils.CONTRACT_INFO_H4D.equals(contractInfo)) {
                    DTVTLogger.debug("contractInfo:---" + contractInfo);
                    mThumbnailBtn.setVisibility(View.GONE);
                    mContractLeadingView.setVisibility(View.VISIBLE);
                    setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
                    TextView contractLeadingText = findViewById(R.id.contract_leading_text);
                    Button contractLeadingButton = findViewById(R.id.contract_leading_button);
                    String message;
                    String buttonText;
                    if (userState.equals(UserState.LOGIN_NG)) {
                        loginNgDisplay();
                    } else {
                        noAgreementDisplay();
                    }
                } else { //ひかりTV契約者の場合
                    // サムネイル表示メッセージ取得
                    String thumbnailMessage = StringUtils.getContentsDetailThumbnailString(
                            detailFragment.mOtherContentsDetailData, this, mDetailFullData.getContentsType());
                    setThumbnailText(thumbnailMessage);
                }
            } else { //レコメンドサーバー以外のひかりTV
                // サムネイル表示メッセージ取得
                String thumbnailMessage = StringUtils.getContentsDetailThumbnailString(
                        detailFragment.mOtherContentsDetailData, this, mDetailFullData.getContentsType());
                setThumbnailText(thumbnailMessage);
                if (getStbStatus()) {
                    createRemoteControllerView(true);
                    mIsControllerVisible = true;
                    setStartRemoteControllerUIListener(this);
                }
            }
        } else {
            setViewPagerTab();
            showErrorDialog(ErrorType.contentDetailGet);
            // 他サービス
            if (!mIsOtherService) {
                setThumbnail();
            }
        }
        sendOperateLog();
        showProgressBar(false);
        if (getStbStatus()) {
            findViewById(R.id.remote_control_view).setVisibility(View.VISIBLE);
        }
    }

    //endregion
    @Override
    public void onRoleListCallback(final ArrayList<RoleListMetaData> roleListInfo) {
        //スタッフ情報取得して、更新する
        if (roleListInfo != null) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            if (mDetailFullData != null) {
                String[] credit_array = mDetailFullData.getmCredit_array();
                List<String> staffList = getRoleList(credit_array, roleListInfo);
                if (staffList.size() > 0) {
                    detailFragment.mOtherContentsDetailData.setStaffList(staffList);
                    detailFragment.refreshStaff();
                }
            }
        } else {
            showErrorDialog(ErrorType.roleListGet);
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
        showProgressBar(false);
        if (channels == null || channels.isEmpty()) {
            showErrorDialog(ErrorType.channelListGet);
            return;
        }

        //チャンネル情報取得して、更新する
        if (!TextUtils.isEmpty(mServiceId)) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            for (int i = 0; i < channels.size(); i++) {
                ChannelInfo channel = channels.get(i);
                if (mServiceId.equals(channel.getServiceId())) {
                    mChannel = channel;
                    String channelName = channel.getTitle();
                    if (detailFragment.mOtherContentsDetailData != null) {
                        detailFragment.mOtherContentsDetailData.setChannelName(channelName);
                    }
                    break;
                }
            }
            detailFragment.refreshChannelInfo();
        }
        if (mDetailFullData != null) {
            checkWatchContents();
        }
        //コンテンツの視聴可否判定に基づいてUI操作を行う
        if (mViewIngType != null) {
            changeUIBasedContractInfo();
        }
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        if (channelsInfo != null && channelsInfo.getChannels() != null) {
            List<ChannelInfo> channels = channelsInfo.getChannels();
            sort(channels);
            if (channels.size() > 0) {
                if (mViewPager.getCurrentItem() == 1) {
                    DtvContentsChannelFragment channelFragment = getChannelFragment();
                    ChannelInfo channelInfo = channels.get(0);
                    ArrayList<ScheduleInfo> scheduleInfos = channelInfo.getSchedules();
                    if (mDateIndex == 1 && channelFragment.mContentsData != null) {
                        channelFragment.mContentsData.clear();
                    }
                    boolean isFirst = false;
                    for (ScheduleInfo scheduleInfo : scheduleInfos) {
                        String endTime = scheduleInfo.getEndTime();
                        String startTime = scheduleInfo.getStartTime();
                        String start = startTime.substring(0, 10) + startTime.substring(11, 19);
                        String end = endTime.substring(0, 10) + endTime.substring(11, 19);
                        if (!isLastDate(end)) {
                            if (mDateList != null) {
                                ContentsData contentsData = new ContentsData();
                                if (!isFirst) {
                                    if (mDateIndex == 1) {
                                        if (isNowOnAir(start, end)) {
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
                                channelFragment.mContentsData.add(contentsData);
                            }
                        }
                    }
                    if (mDateIndex == 1) {
                        getChannelDetailByPageNo();
                    } else {
                        channelFragment.setNotifyDataChanged();
                    }
                }
            }
            channelLoadCompleted();
        } else {
            showErrorDialog(ErrorType.tvScheduleListGet);
        }
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
     * NowOnAir判定.
     *
     * @param startTime 開始時刻
     * @param endTime 終了時刻
     * @return 現在放送しているかどうか
     */
    private boolean isNowOnAir(final String startTime, final String endTime) {
        Date startDate = new Date();
        Date endDate = new Date();
        Date nowDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
        Calendar c = Calendar.getInstance();
        try {
            startDate = format.parse(startTime);
            endDate = format.parse(endTime);
            nowDate = c.getTime();
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return (nowDate.compareTo(startDate) != -1 && nowDate.compareTo(endDate) != 1);
    }

    /**
     * ソートを行う.
     *
     *  @param endTime 終了時刻
     *  @return 過去の番組
     */
    private boolean isLastDate(final String endTime) {
        Date endDate = new Date();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
        Calendar c = Calendar.getInstance();
        try {
            endDate = sdf.parse(endTime);
            now = c.getTime();
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return (endDate.compareTo(now) == -1);
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
        Fragment currentFragment = mFragmentFactory.createFragment(0);
        return (DtvContentsDetailFragment) currentFragment;
    }

    //region View.OnClickListener
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onClick(final View v) {
        DTVTLogger.start();
        switch (v.getId()) {
            case R.id.tv_player_ctrl_video_record_player_pause_fl:
                if (!mPlayerController.isPlaying()) {
                    playStart();
                } else {
                    playPause();
                }
                if (!mExternalDisplayFlg) {
                    hideCtrlViewAfterOperate();
                } else {
                    //外部出力制御の場合
                    initPlayerView();
                    setPlayerEvent();
                    mExternalDisplayFlg = false;
                }
                break;
            case R.id.tv_player_ctrl_now_on_air_full_screen_iv:
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                initPlayerView();
                setPlayerEvent();
                hideCtrlViewAfterOperate();
                break;
            case R.id.dtv_contents_detail_main_layout_thumbnail_btn:
                if (mViewIngType.equals(ContentUtils.ViewIngType.DISABLE_WATCH_AGREEMENT_DISPLAY)) {
                    //未契約時は契約導線を表示
                    leadingContract();
                    return;
                }
                //DTVの場合
                if (mDetailData != null && mDetailData.getServiceId() == DTV_CONTENTS_SERVICE_ID) {
                    CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                    startAppDialog.setContent(getResources().getString(R.string.dtv_content_service_start_dialog));
                    startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                        @Override
                        public void onOKCallback(final boolean isOK) {
                            int localVersionCode = getVersionCode(DTV_PACKAGE_NAME);
                            //端末にDTVアプリはすでに存在した場合
                            if (isAppInstalled(ContentDetailActivity.this, DTV_PACKAGE_NAME)) {
                                //バージョンチェック
                                if (localVersionCode < DTV_VERSION_STANDARD) {
                                    mErrorMessage = getResources().getString(R.string.dtv_content_service_update_dialog);
                                    showErrorDialog(mErrorMessage);
                                } else {
                                    //RESERVED4は4の場合
                                    if (RESERVED4_TYPE4.equals(mDetailData.getReserved4())) {
                                        startApp(UrlConstants.WebUrl.WORK_START_TYPE + mDetailData.getContentsId());
                                        //RESERVED4は7,8の場合
                                    } else if (RESERVED4_TYPE7.equals(mDetailData.getReserved4())
                                            || RESERVED4_TYPE8.equals(mDetailData.getReserved4())) {
                                        startApp(UrlConstants.WebUrl.SUPER_SPEED_START_TYPE + mDetailData.getContentsId());
                                        //その他の場合
                                    } else {
                                        startApp(UrlConstants.WebUrl.TITTLE_START_TYPE + mDetailData.getContentsId());
                                    }
                                }
                                //DTVアプリ存在しない場合
                            } else {
                                toGooglePlay(UrlConstants.WebUrl.GOOGLEPLAY_DOWNLOAD_URL);
                            }
                        }
                    });
                    startAppDialog.showDialog();
                } else if (mDetailData != null && mDetailData.getServiceId() == D_ANIMATION_CONTENTS_SERVICE_ID) {
                    CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                    startAppDialog.setContent(getResources().getString(R.string.d_anime_store_content_service_start_dialog));
                    startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                        @Override
                        public void onOKCallback(final boolean isOK) {
                            int localVersionCode = getVersionCode(DANIMESTORE_PACKAGE_NAME);
                            //端末にdアニメストアアプリはすでに存在した場合
                            if (isAppInstalled(ContentDetailActivity.this, DANIMESTORE_PACKAGE_NAME)) {
                                //バージョンチェック
                                if (localVersionCode < DANIMESTORE_VERSION_STANDARD) {
                                    mErrorMessage = getResources().getString(R.string.d_anime_store_content_service_update_dialog);
                                    showErrorDialog(mErrorMessage);
                                } else {
                                    startApp(UrlConstants.WebUrl.DANIMESTORE_START_URL + mDetailData.getContentsId());
                                }
                            } else {
                                toGooglePlay(UrlConstants.WebUrl.DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL);
                            }
                        }
                    });
                    startAppDialog.showDialog();
                } else if (mDetailData != null && mDetailData.getServiceId() == DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                    final CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                    startAppDialog.setContent(getResources().getString(R.string.dtv_channel_service_start_dialog));
                    startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                        @Override
                        public void onOKCallback(final boolean isOK) {
                            if (isAppInstalled(ContentDetailActivity.this, DTVCHANNEL_PACKAGE_NAME)) {
                                //バージョンコードは15
                                int localVersionCode = getVersionCode(DTVCHANNEL_PACKAGE_NAME);
                                if (localVersionCode < DTVCHANNEL_VERSION_STANDARD) {
                                    mErrorMessage = getResources().getString(R.string.dtv_channel_service_update_dialog);
                                    showErrorDialog(mErrorMessage);
                                } else {    //テレビ再生  「categoryId」が「01」の場合
                                    if (DTV_CHANNEL_CATEGORY_BROADCAST.equals(mDetailData.getCategoryId())) {
                                        startApp(UrlConstants.WebUrl.DTVCHANNEL_TELEVISION_START_URL + mDetailData.getChannelId());
                                        DTVTLogger.debug("channelId :----" + mDetailData.getChannelId());
                                        //ビデオ再生  「categoryId」が「02」または「03」の場合
                                    } else if (DTV_CHANNEL_CATEGORY_MISSED.equals(mDetailData.getCategoryId())
                                            || DTV_CHANNEL_CATEGORY_RELATION.equals(mDetailData.getCategoryId())) {
                                        startApp(UrlConstants.WebUrl.DTVCHANNEL_VIDEO_START_URL + mDetailData.getContentsId());
                                        DTVTLogger.debug("ContentId :----" + mDetailData.getContentsId());
                                    }
                                }
                            } else {
                                toGooglePlay(UrlConstants.WebUrl.DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL);
                            }
                        }
                    });
                    startAppDialog.showDialog();
                } else {
                    //ぷらら
                    if (mDetailFullData != null) {
                        //ひかりTV中にDTVの場合 DREM-895
                        if (VIDEO_PROGRAM.equals(mDetailFullData.getDisp_type())
                                || VIDEO_SERIES.equals(mDetailFullData.getDisp_type())
                                || WIZARD.equals(mDetailFullData.getDisp_type())
                                || VIDEO_PACKAGE.equals(mDetailFullData.getDisp_type())
                                || SUBSCRIPTION_PACKAGE.equals(mDetailFullData.getDisp_type())
                                || SERIES_SVOD.equals(mDetailFullData.getDisp_type())) {
                            CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                            startAppDialog.setContent(getResources().getString(R.string.dtv_content_service_start_dialog));
                            startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                                @Override
                                public void onOKCallback(final boolean isOK) {
                                    int localVersionCode = getVersionCode(DTV_PACKAGE_NAME);
                                    //端末にDTVアプリはすでに存在した場合
                                    if (isAppInstalled(ContentDetailActivity.this, DTV_PACKAGE_NAME)) {
                                        //バージョンチェック
                                        if (localVersionCode < DTV_VERSION_STANDARD) {
                                            mErrorMessage = getResources().getString(R.string.dtv_content_service_update_dialog);
                                            showErrorDialog(mErrorMessage);
                                        } else {
                                            if (METARESPONSE1.equals(mDetailFullData.getDtvType())) {
                                                startApp(UrlConstants.WebUrl.WORK_START_TYPE + mDetailFullData.getTitle_id());
                                            } else if (METARESPONSE2.equals(mDetailFullData.getDtvType())) {
                                                startApp(UrlConstants.WebUrl.SUPER_SPEED_START_TYPE + mDetailFullData.getTitle_id());
                                            } else {
                                                startApp(UrlConstants.WebUrl.TITTLE_START_TYPE + mDetailFullData.getTitle_id());
                                            }
                                        }
                                        //DTVアプリ存在しない場合
                                    } else {
                                        toGooglePlay(UrlConstants.WebUrl.GOOGLEPLAY_DOWNLOAD_URL);
                                    }
                                }
                            });
                            startAppDialog.showDialog();
                        } else if (TV_PROGRAM.equals(mDetailFullData.getDisp_type())) { //ひかりTV中にdtvチャンネルの場合 DREM-1100
                            final CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                            startAppDialog.setContent(getResources().getString(R.string.dtv_channel_service_start_dialog));
                            startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                                @Override
                                public void onOKCallback(final boolean isOK) {
                                    if (isAppInstalled(ContentDetailActivity.this, DTVCHANNEL_PACKAGE_NAME)) {
                                        //バージョンコードは15
                                        int localVersionCode = getVersionCode(DTVCHANNEL_PACKAGE_NAME);
                                        if (localVersionCode < DTVCHANNEL_VERSION_STANDARD) {
                                            mErrorMessage = getResources().getString(R.string.dtv_channel_service_update_dialog);
                                            showErrorDialog(mErrorMessage);
                                        } else {
                                            if (TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(mDetailFullData.getmTv_service())) {
                                                //「contents_type」が「0」または未設定
                                                if (CONTENT_TYPE_FLAG_ZERO.equals(mDetailFullData.getmContent_type())
                                                        || null == mDetailFullData.getmContent_type()) {
                                                    DTVTLogger.debug("contentsType :----" + mDetailFullData.getmContent_type());
                                                    startApp(UrlConstants.WebUrl.DTVCHANNEL_TELEVISION_START_URL + mDetailFullData.getmService_id());
                                                    DTVTLogger.debug("chno :----" + mDetailFullData.getmService_id());
                                                    //ビデオ再生 「disp_type」が「tv_program」かつ「contents_type」が「1」または「2」または「3」
                                                } else if (CONTENT_TYPE_FLAG_ONE.equals(mDetailFullData.getmContent_type())
                                                        || CONTENT_TYPE_FLAG_TWO.equals(mDetailFullData.getmContent_type())
                                                        || CONTENT_TYPE_FLAG_THREE.equals(mDetailFullData.getmContent_type())) {
                                                    startApp(UrlConstants.WebUrl.DTVCHANNEL_VIDEO_START_URL + mDetailFullData.getCrid());
                                                    DTVTLogger.debug("crid :----" + mDetailFullData.getCrid());
                                                }
                                            }
                                        }
                                    } else {
                                        toGooglePlay(UrlConstants.WebUrl.DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL);
                                    }
                                }
                            });
                            startAppDialog.showDialog();
                        }
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
     * 機能：APP起動.
     *
     * @param url URL
     */
    private void startApp(final String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, START_APPLICATION_REQUEST_CODE);
    }

    /**
     * 機能：中継アプリは端末にインストールするかどうかの判断.
     *
     * @param context コンテキスト
     * @param packageName 中継アプリのパッケージ名
     * @return 中継アプリがインストールされているか
     */
    private boolean isAppInstalled(final Context context, final String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
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
    private int getVersionCode(final String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            DTVTLogger.debug(e);
        }
        return -1;
    }
    //region RemoteControllerView
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onStartRemoteControl(final boolean isFromHeader) {
        mIsFromHeader = isFromHeader;
        DTVTLogger.start();
        // サービスIDにより起動するアプリを変更する
        if (mDetailData != null && !mIsSend && !isFromHeader) {
            setRelayClientHandler();
            switch (mDetailData.getServiceId()) {
                case DTV_CONTENTS_SERVICE_ID: // dTV
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mDetailData.getContentsId());
                    break;
                case D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mDetailData.getContentsId());
                    break;
                case DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
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
                case DTV_HIKARI_CONTENTS_SERVICE_ID://ひかりTV
                default:
                    if (mDetailFullData == null) {
                        break;
                    }
                    startHikariApplication();
                    break;
            }
        }
        super.onStartRemoteControl(isFromHeader);
        DTVTLogger.end();

    } // end of onStartRemoteControl

    /**
     * STBのサービスアプリ起動（ひかり）.
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private void startHikariApplication() {
        if (!mIsFromHeader) {
            setRemoteProgressVisible(View.VISIBLE);
        }
        if (VIDEO_PROGRAM.equals(mDetailFullData.getDisp_type())) {
            if (DTV_FLAG_ZERO.equals(mDetailFullData.getDtv()) || TextUtils.isEmpty(mDetailFullData.getDtv())
                    || FLAG_ZERO == mDetailFullData.getDtv().trim().length()) {
                String[] liinfArray = mDetailFullData.getmLiinf_array();
                String puid = mDetailFullData.getPuid();
                if (BVFLG_FLAG_ONE.equals(mDetailFullData.getBvflg())) {
                    requestStartApplicationHikariTvCategoryHikaritvVod(mDetailFullData.getPuid(),
                            mDetailFullData.getCid(), mDetailFullData.getCrid());
                } else if (BVFLG_FLAG_ZERO.equals(mDetailFullData.getBvflg()) || TextUtils.isEmpty(mDetailFullData.getBvflg())) {
                    //liinfを"|"区切りで分解する
                    if (mResponse == null) {
                        if (!mIsFromHeader) {
                            setRemoteProgressVisible(View.GONE);
                        }
                        return;
                    }
                    ArrayList<ActiveData> activeDatas = mResponse.getVodActiveData();
                    //ひかりアプリ起動フラグ、くるくる処理ちゃんと消えるため
                    boolean isStarted = false;
                    for (String liinf : liinfArray) {
                        String[] column = liinf.split(Pattern.quote("|"), 0);
                        for (ActiveData activeData : activeDatas) {
                            String licenseId = activeData.getLicenseId();
                            //メタレスポンスのpuid、liinf_arrayのライセンスID（パイプ区切り）と
                            // 購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較して一致した場合
                            if (licenseId.equals(column[0]) || licenseId.equals(puid)) {
                                //一致した「active_list」の「valid_end_date」> 現在時刻の場合
                                if (activeData.getValidEndDate() > DateUtils.getNowTimeFormatEpoch()) {
                                    // license_idが複数ある場合は「valid_end_date」が一番長い「license_id」を指定する
                                    long longestDate = activeData.getValidEndDate();
                                    for (ActiveData activeDataEndDate : activeDatas) {
                                        if (longestDate < activeDataEndDate.getValidEndDate()) {
                                            longestDate = activeDataEndDate.getValidEndDate();
                                            licenseId = activeDataEndDate.getLicenseId();
                                        }
                                    }
                                    isStarted = true;
                                    requestStartApplicationHikariTvCategoryHikaritvVod(licenseId,
                                            mDetailFullData.getCid(), mDetailFullData.getCrid());
                                }
                            }
                        }
                    }
                    if (!isStarted) {
                        if (!mIsFromHeader) {
                            setRemoteProgressVisible(View.GONE);
                        }
                    }
                } else {
                    if (!mIsFromHeader) {
                        setRemoteProgressVisible(View.GONE);
                    }
                }
            } else if (DTV_FLAG_ONE.equals(mDetailFullData.getDtv())) {
                //ひかりTV内dTVのVOD,「episode_id」を通知する
                requestStartApplicationHikariTvCategoryDtvVod(mDetailFullData.getEpisode_id());
            } else {
                if (!mIsFromHeader) {
                    setRemoteProgressVisible(View.GONE);
                }
            }
            //「disp_type」が「video_series」の場合
        } else if (VIDEO_SERIES.equals(mDetailFullData.getDisp_type())) {
            // ひかりTV内VOD(dTV含む)のシリーズ
            requestStartApplicationHikariTvCategoryDtvSvod(mDetailFullData.getCrid());
            //「disp_type」が「tv_program」の場合
        } else if (TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
            //「tv_service」が「1」の場合 ひかりTVの番組
            if (TV_SERVICE_FLAG_HIKARI.equals(mDetailFullData.getmTv_service())) {
                //ひかりTVの番組
                requestStartApplicationHikariTvCategoryTerrestrialDigital(mDetailFullData.getmChno());
                //「tv_service」が「2」の場合
            } else if (TV_SERVICE_FLAG_DCH_IN_HIKARI.equals(mDetailFullData.getmTv_service())) {
                //「contents_type」が「0」または未設定の場合  ひかりTV内dTVチャンネルの番組
                if (CONTENT_TYPE_FLAG_ZERO.equals(mDetailFullData.getmContent_type())
                        || TextUtils.isEmpty(mDetailFullData.getmContent_type())) {
                    //中継アプリに「chno」を通知する
                    requestStartApplicationHikariTvCategoryDtvchannelBroadcast(mDetailFullData.getmChno());
                } else if (CONTENT_TYPE_FLAG_ONE.equals(mDetailFullData.getmContent_type())
                        || CONTENT_TYPE_FLAG_TWO.equals(mDetailFullData.getmContent_type())
                        || CONTENT_TYPE_FLAG_THREE.equals(mDetailFullData.getmContent_type())) {
                    //「vod_start_date」> 現在時刻の場合  ひかりTV内dTVチャンネルの番組(見逃し、関連VOD予定だが未配信)
                    if (DateUtils.getNowTimeFormatEpoch() < mDetailFullData.getmVod_start_date()) {
                        //中継アプリに「chno」を通知する
                        requestStartApplicationHikariTvCategoryDtvchannelBroadcast(mDetailFullData.getmChno());
                        //「vod_start_date」 <= 現在時刻 < 「vod_end_date」の場合  //「tv_cid」を通知する
                    } else if (DateUtils.getNowTimeFormatEpoch() >= mDetailFullData.getmVod_start_date()
                            && DateUtils.getNowTimeFormatEpoch() < mDetailFullData.getmVod_end_date()) {
                        // ひかりTV内dTVチャンネル 見逃し
                        requestStartApplicationHikariTvCategoryDtvchannelMissed(mDetailFullData.getmTv_cid());
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

    /**
     * onPlayerEvent.
     *
     * @param mediaPlayerController メディアプレイヤーコントローラ
     * @param event イベント
     * @param arg arg
     */
    @Override
    public void onPlayerEvent(final MediaPlayerController mediaPlayerController, final int event, final long arg) {
        DTVTLogger.start();
        switch (event) {
            case MediaPlayerDefinitions.PE_OPENED:
                playButton(true);
                break;
            case MediaPlayerDefinitions.PE_COMPLETED:
                playButton(false);
                break;
            case MediaPlayerDefinitions.PE_START_NETWORK_CONNECTION:
            case MediaPlayerDefinitions.PE_START_AUTHENTICATION:
            case MediaPlayerDefinitions.PE_START_BUFFERING:
            case MediaPlayerDefinitions.PE_START_RENDERING:
                break;
            case MediaPlayerDefinitions.PE_FIRST_FRAME_RENDERED:
                showProgressBar(false);
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    @Override
    public void onEndRemoteControl() {
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
                case DTV_CONTENTS_SERVICE_ID:
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_dtv, null));
                break;
                case D_ANIMATION_CONTENTS_SERVICE_ID:
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_d_anime, null));
                    break;
                case DTV_CHANNEL_CONTENTS_SERVICE_ID:
                case DTV_HIKARI_CONTENTS_SERVICE_ID:
                default:
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
                    break;
            }
        }
        super.onEndRemoteControl();
    }
    //endregion

    /**
     * ユーザ年齢をセット.
     */
    private void setUserAgeInfo() {
        mAge = UserInfoUtils.getUserAgeInfoWrapper(SharedPreferencesUtils.getSharedPreferencesUserInfo(this));
    }

    /**
     * 年齢制限ダイアログを表示.
     */
    private void showDialogToConfirmClose() {
        mPlayerController.stop();
        CustomDialog closeDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        closeDialog.setContent(getApplicationContext().getString(R.string.contents_detail_parental_check_fail));
        closeDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                contentsDetailCloseKey(null);
            }
        });
        closeDialog.setCancelable(false);
        closeDialog.showDialog();
    }
    //region MediaPlayerController
    /**
     * onStateChanged.
     *
     * @param mediaPlayerController controller
     * @param i                     i
     */
    @Override
    public void onStateChanged(final MediaPlayerController mediaPlayerController, final int i) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * onFormatChanged.
     *
     * @param mediaPlayerController controller
     */
    @Override
    public void onFormatChanged(final MediaPlayerController mediaPlayerController) {
        DTVTLogger.start();
        //パレンタルチェック
        if (mAge < mediaPlayerController.getParentalRating()) {
            showDialogToConfirmClose();
        }
        //外部出力制御
        mExternalDisplayHelper.onResume();
        DTVTLogger.end();
    }

    /**
     * onError.
     *
     * @param mediaPlayerController controller
     * @param i                     i
     * @param l                     l
     */
    @Override
    public void onError(final MediaPlayerController mediaPlayerController, final int i, final long l) {
        DTVTLogger.start();
    }

    /**
     * onCaptionData.
     *
     * @param mediaPlayerController メディアプレイヤーコントローラ
     * @param captionDrawCommands キャプション描画コマンド
     */
    @Override
    public void onCaptionData(final MediaPlayerController mediaPlayerController, final CaptionDrawCommands captionDrawCommands) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * onSuperData.
     *
     * @param mediaPlayerController controller
     * @param captionDrawCommands   commands
     */
    @Override
    public void onSuperData(final MediaPlayerController mediaPlayerController, final CaptionDrawCommands captionDrawCommands) {
        DTVTLogger.start();
        DTVTLogger.end();
    }
    //endregion
    /**
     * Playを停止.
     */
    private void finishPlayer() {
        if (mPlayerController != null) {
            setCanPlay(false);
            mPlayerController.setCaptionDataListener(null);
            mPlayerController.release();
            mPlayerController = null;
        }
    }
    /**
     * サムネイル取得処理を止める.
     */
    private void stopThumbnailConnect() {
        DTVTLogger.start();
        mIsDownloadStop = true;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.stopConnect();
        }
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
                // TODO エラー番号が判明次第エラーダイアログのタイトルを設定して表示
//            switch (response.getErrorNo()) {
//                case "0": // トークン不一致
//                    dialog.setTitle(getResources().getString(R.string.recording_reservation_failed_dialog_token));
//                    break;
//                case "1": // その他異常
//                    dialog.setTitle(getResources().getString(R.string.recording_reservation_failed_dialog_other));
//                    break;
//                case "2": // リクエストパラメータ
//                    dialog.setTitle(getResources().getString(R.string.recording_reservation_failed_dialog_param));
//                    break;
//                case "3": // 登録件数超過
//                    dialog.setTitle(getResources().getString(R.string.recording_reservation_failed_dialog_over));
//                    break;
//                case "4": // 未契約エラー
//                    dialog.setTitle(getResources().getString(R.string.recording_reservation_failed_dialog_no_agreement));
//                    break;
//                default: // 基本的には通らない
//                    break;
//            }
                dialog.setTitle(getResources().getString(R.string.recording_reservation_failed_dialog_msg));
                dialog.showDialog();
            } else {
                // 成功
                showCompleteDialog();
            }
        } else {
            // コンテンツ詳細取得データに失敗があった場合
            CustomDialog dialog = createErrorDialog();
            dialog.setTitle(getResources().getString(R.string.recording_reservation_failed_dialog_param));
            dialog.showDialog();
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickRecordingReservationIcon(final View view) {
        //未契約時は契約導線を表示
        if (mViewIngType.equals(ContentUtils.ViewIngType.DISABLE_WATCH_AGREEMENT_DISPLAY)) {
            leadingContract();
            return;
        }
        // リスト表示用のアラートダイアログを表示
        if (mRecordingReservationCustomtDialog == null) {
            DTVTLogger.debug("Create Dialog");
            // TODO 繰り返し予約処理追加時に切り替え
//            mRecordingReservationCustomtDialog = createRecordingReservationDialog();
            mRecordingReservationCustomtDialog = createRecordingReservationConfirmDialog();
        }
        mRecordingReservationCustomtDialog.showDialog();
    }

//    /**
//     * リモート録画予約ダイアログを生成.
//     */
//    // TODO 繰り返し予約処理追加時に使用
//    private CustomDialog createRecordingReservationDialog() {
//        DTVTLogger.start();
//        CustomDialog recDialog = new CustomDialog(this, CustomDialog.DialogType.SELECT);
//        // Callback
//        recDialog.setItemSelectCallback(mItemSelectCallback);
//        // Title
//        recDialog.setTitle(getResources().getString(R.string.recordiong_reservation_menu_dialog_title));
//        // SelectItem
//        String[] itemArray = getResources().getStringArray(R.array.recording_reservation_menu_dialog_item);
//        List<String> list = new ArrayList<String>();
//        for(String str : itemArray) {
//            list.add(str);
//        }
//        recDialog.setSelectData(list);
//        // Cancelable
//        recDialog.setCancelable(false);
//        recDialog.setCancelVisiblity(View.GONE);
//        DTVTLogger.end();
//
//        return recDialog;
//    }

    /**
     * 開始時間と現在時刻の比較.
     *
     * @return 放送開始まで2時間以上あるかどうか
     */
    private boolean comparisonStartTime() {
        long nowTimeEpoch = DateUtils.getNowTimeFormatEpoch();
        long canRecordingReservationTime = 0;
        if (mRecordingReservationContentsDetailInfo != null) {
            canRecordingReservationTime =
                    mRecordingReservationContentsDetailInfo.getStartTime() - (DateUtils.EPOCH_TIME_ONE_HOUR * 2);
        }
        return !(nowTimeEpoch >= canRecordingReservationTime);
    }

    /**
     * 録画予約成功時ダイアログ表示.
     */
    private void showCompleteDialog() {
        CustomDialog completeRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        completeRecordingReservationDialog.setContent(getResources().getString(R.string.recording_reservation_complete_dialog_msg));
        completeRecordingReservationDialog.setConfirmText(R.string.recording_reservation_complete_dialog_ok);
        // Cancelable
        completeRecordingReservationDialog.setCancelable(false);
        completeRecordingReservationDialog.showDialog();
    }

    /**
     * 録画予約失敗時エラーダイアログ表示.
     *
     * @return 録画予約失敗エラーダイアログ
     */
    private CustomDialog createErrorDialog() {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.recording_reservation_failed_dialog_msg));
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        return failedRecordingReservationDialog;
    }

//    /**
//     * 繰り返し録画予約ダイアログの項目選択時のコールバック.
//     */
//    private CustomDialog.ApiItemSelectCallback mItemSelectCallback = new CustomDialog.ApiItemSelectCallback() {
//        @Override
//        public void onItemSelectCallback(final AlertDialog dialog, final int position) {
//            // リスト選択時の処理
//            //TODO 定期予約実装時 1 は "繰り返し録画予約する"になる
//            switch (position) {
//                case RECORDING_RESERVATION_DIALOG_INDEX_0: // 録画予約するをタップ
//                    mRecordingReservationContentsDetailInfo.setLoopTypeNum(
//                            RecordingReservationContentsDetailInfo.REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_0);
//                    createRecordingReservationConfirmDialog();
//                    dialog.dismiss();
//                    break;
//                case RECORDING_RESERVATION_DIALOG_INDEX_1: // キャンセルをタップ
//                    DTVTLogger.debug("Cancel RecordingReservation");
//                    dialog.dismiss();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * 録画予約確認ダイアログを表示.
     *
     * @return 録画予約確認ダイアログ
     */
    private CustomDialog createRecordingReservationConfirmDialog() {
        CustomDialog recordingReservationConfirmDialog =
                new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
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
                mContentsDetailDataProvider.requestRecordingReservation(
                        mRecordingReservationContentsDetailInfo);
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
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void checkWatchContents() {

        //DBに保存されているUserInfoから契約情報を確認する
        String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(this));
        DTVTLogger.debug("contractInfo: " + contractInfo);

        mViewIngType = ContentUtils.getViewingType(contractInfo, mDetailFullData, mChannel);
        switch (mViewIngType) {
            case ENABLE_WATCH_LIMIT_THIRTY:
                //期限まで30日以内表示内容設定
                mEndDate = mDetailFullData.getPublish_end_date();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRentalVodListCallback(final PurchasedVodListResponse response) {
        DTVTLogger.start();
        if (response == null) {
            showErrorDialog(ErrorType.rentalVoidListGet);
            return;
        }

        ArrayList<ActiveData> vodActiveData = response.getVodActiveData();
        mEndDate = ContentUtils.getRentalVodValidEndDate(mDetailFullData, vodActiveData);
        DTVTLogger.debug("get rental vod end date:" + mEndDate);
        mViewIngType = ContentUtils.getRentalVodViewingType(mDetailFullData, vodActiveData, mEndDate);
        DTVTLogger.debug("get rental vod viewing type:" + mViewIngType);
        changeUIBasedContractInfo();
    }

    @Override
    public void onRentalChListCallback(final PurchasedChListResponse response) {
        //購入済みCH一覧取得からの戻り
        DTVTLogger.start();
        if (response == null) {
            showErrorDialog(ErrorType.rentalChannelListGet);
            return;
        }

        mEndDate = ContentUtils.getRentalChannelValidEndDate(response, mChannel);
        DTVTLogger.debug("get rental vod end date:" + mEndDate);
        mViewIngType = ContentUtils.getRentalChannelViewingType(mDetailFullData, response, mChannel, mEndDate);
        DTVTLogger.debug("get rental vod viewing type:" + mViewIngType);
        changeUIBasedContractInfo();
    }

    /**
     * ひかりTV Now On Air の時のみ自動再生する.
     */
    private void playNowOnAir() {
        if (mDetailFullData.getContentsType().equals(ContentUtils.ContentsType.HIKARI_TV_NOW_ON_AIR)) {
            UserInfoUtils.PairingState pairingState = UserInfoUtils.getPairingState(this, getStbStatus());
            //宅内のみ自動再生
            if (pairingState.equals(UserInfoUtils.PairingState.INSIDE_HOUSE)) {
                //放送中番組
                DlnaProvHikariChList provider = new DlnaProvHikariChList(new DlnaProvHikariChList.OnApiCallbackListener() {
                    @Override
                    public void itemFindCallback(@Nullable final DlnaHikariChListItem resultItem) {
                        DTVTLogger.error(" <<< ");
                        if (resultItem != null) {
                            //Threadクラスからのコールバックのため、UIスレッド化する
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DTVTLogger.error("resultItem != null");
                                    //player start
                                    //放送中ひかりTVコンテンツの時は自動再生する
                                    mDisplayState = PLAYER_AND_CONTENTS_DETAIL;
                                    RecordedContentsDetailData data;
                                    data = new RecordedContentsDetailData();
                                    data.setUpnpIcon("");
                                    data.setResUrl(resultItem.mResUrl);
                                    data.setSize(resultItem.mSize);
                                    data.setDuration(resultItem.mDuration);
                                    data.setVideoType(resultItem.mVideoType);
                                    data.setBitrate(resultItem.mBitrate);
                                    data.setTitle(resultItem.mTitle);
                                    data.setIsLive(true);
                                    mIntent.putExtra(RecordedListActivity.RECORD_LIST_KEY, data);
                                    initPlayer();
                                    //外部出力および画面キャプチャ制御
                                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                                    mExternalDisplayHelper = createExternalDisplayHelper();
                                    initSecurePlayer();
                                    setPlayerEvent();
                                    setUserAgeInfo();
                                    mPlayerController.start();
                                }
                            });
                        } else {
                            DTVTLogger.error("resultItem == null");
                        }
                    }
                });
                DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
                if (dlnaDmsItem != null) {
                    provider.start(dlnaDmsItem, String.valueOf(mChannel.getChannelNo()), DlnaUtils.getImageQualitySetting(getApplicationContext()));
                    //TODO チャンネル番号が"1010"でないとデータの取得ができないため暫定的に固定値を設定する場合は次のように実装すること
//                    provider.start(dlnaDmsItem, "1010");
                } else {
                    DTVTLogger.error("dlnaDmsItem == null");
                }
            } else {
                setRemotePlayArrow();
            }
        }
    }

    /**
     * リモート視聴用の再生ボタン表示.
     */
    private void setRemotePlayArrow() {
        UserInfoUtils.PairingState userState = UserInfoUtils.getPairingState(this, getStbStatus());
        //再生ボタンは宅内、宅外のみ表示
        if (!userState.equals(UserInfoUtils.PairingState.NO_PAIRING)) {
            mThumbnailBtn.setVisibility(View.VISIBLE);
            ImageView imageView = findViewById(R.id.dtv_contents_view_button);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.mediacontrol_icon_tap_play_arrow2);
            imageView.setImageBitmap(bmp);
            int pixelSize = getResources().getDimensionPixelSize(R.dimen.contents_detail_player_media_controller_size);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(pixelSize, pixelSize));
            //TODO クリックイベントを無効にするためにここでonClickListenerを生成(本実装ではクラス内のonClickメソッド内で実装することを想定)
            mThumbnailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //TODO リモート視聴機能(2018/5/8現在 未実装のためコメントのみ記載)
                }
            });
        }
    }
    /**
     * 視聴可否判定に基づいてUIの操作などを行う.
     */
    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod", "EnumSwitchStatementWhichMissesCases"})
    private void changeUIBasedContractInfo() {
        DtvContentsDetailFragment detailFragment = getDetailFragment();
        boolean isVisibleRecordButton = mRecordingReservationContentsDetailInfo != null;
        switch (mViewIngType) {
            case ENABLE_WATCH:
                //再生導線表示
                playNowOnAir();
                break;
            case ENABLE_WATCH_LIMIT_THIRTY:
                playNowOnAir();
                //「〇〇日まで」を表示
                if (mEndDate != 0L) {
                    detailFragment.displayEndDate(mEndDate);
                }
                break;
            case DISABLE_WATCH_AGREEMENT_DISPLAY:
                //再生、クリップ、録画、評価、ダウンロード、番組表編集 の操作時に契約導線を表示

                //TODO 再生、評価はコンテンツ毎の詳細画面の表示が行われてから対応する。

                //クリップ押下時に契約導線を表示するため、Fragmentに未契約状態であることを通知する
                detailFragment.setContractInfo(false);
                break;
            case DISABLE_VOD_WATCH_AGREEMENT_DISPLAY:
                //契約導線を表示 (VOD)
                TextView vodTextView = findViewById(R.id.contract_leading_text);
                Button vodButton = findViewById(R.id.contract_leading_button);
                // 宅内の場合契約導線表示
                if (getStbStatus()) {
                    mContractLeadingView.setVisibility(View.VISIBLE);
                    vodTextView.setText(getString(R.string.contents_detail_contract_text_vod));
                    vodButton.setText(getString(R.string.contents_detail_contract_button_vod));
                    vodButton.setAllCaps(false);
                    //サムネイルにシャドウをかける
                    setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
                } else {
                    //宅外の場合は契約ボタンを表示しない
                    vodButton.setVisibility(View.GONE);
                }

                //サムネイル上のdTVで視聴、dアニメストアで視聴を非表示
                if (mThumbnailBtn != null) {
                    mThumbnailBtn.setVisibility(View.GONE);
                }
                break;
            case DISABLE_CHANNEL_WATCH_AGREEMENT_DISPLAY:
                //契約導線を表示 (CH)
                TextView chTextView = findViewById(R.id.contract_leading_text);
                Button chButton = findViewById(R.id.contract_leading_button);
                // 宅内の場合契約導線表示
                UserInfoUtils.PairingState pairingState = UserInfoUtils.getPairingState(this, getStbStatus());
                if (pairingState.equals(UserInfoUtils.PairingState.INSIDE_HOUSE)) {
                    mContractLeadingView.setVisibility(View.VISIBLE);
                    chTextView.setText(getString(R.string.contents_detail_contract_text_ch));
                    chButton.setText(getString(R.string.contents_detail_contract_button_ch));
                    chButton.setAllCaps(false);
                    //サムネイルにシャドウをかける
                    setThumbnailShadow(THUMBNAIL_SHADOW_ALPHA);
                } else {
                    //宅外の場合は契約ボタンを表示しない
                    chButton.setVisibility(View.GONE);
                }

                //サムネイル上のdTVで視聴、dアニメストアで視聴を非表示
                if (mThumbnailBtn != null) {
                    mThumbnailBtn.setVisibility(View.GONE);
                }
                break;
            case DISABLE_WATCH_AND_PLAY:
                //再生導線を非表示にする

                //TODO 再生、評価はコンテンツ毎の詳細画面の表示が行われてから対応する

                //録画予約ボタンを非表示
                isVisibleRecordButton = false;
                //サムネイル上のdTVで視聴、dアニメストアで視聴を非表示
                if (mThumbnailBtn != null) {
                    mThumbnailBtn.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
        detailFragment.changeVisibilityRecordingReservationIcon(isVisibleRecordButton ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 未契約時、契約導線を表示する.
     */
    public void leadingContract() {
        //契約誘導ダイアログを表示
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        customDialog.setContent(getString(R.string.contents_detail_contract_text));
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                //ブラウザを起動
                //TODO URLは現在未定
                Uri uri = Uri.parse("https://www.nttdocomo.co.jp/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        customDialog.showDialog();
    }

    /**
     * DTV側で購入する/契約するボタン押下時の動作.
     *
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
     *
     * @param showProgessBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgessBar) {
        if (showProgessBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            findViewById(R.id.contents_detail_scroll_layout).setVisibility(View.INVISIBLE);
            setRemoteProgressVisible(View.VISIBLE);
        } else {
            setRemoteProgressVisible(View.INVISIBLE);
            findViewById(R.id.contents_detail_scroll_layout).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 外部出力制御リスナー.
     */
    private static final ExternalDisplayHelper.OnDisplayEventListener DISPLAY_EVENT_LISTENER = new ExternalDisplayHelper.OnDisplayEventListener() {
        @Override
        public void onResumeDefaultDisplay() {

        }
        @Override
        public void onSuspendDefaultDisplay() {

        }
        @Override
        public void onSuspendExternalDisplay() {

        }
        @Override
        public void onResumeExternalDisplay() {

        }
    };

    /**
     * 外部出力Helperの作成.
     *
     * @return 外部出力Helper
     */
    private ExternalDisplayHelper createExternalDisplayHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return ExternalDisplayHelper.create(this, DISPLAY_EVENT_LISTENER,
                    createPresentationEventListener());
        }
        return ExternalDisplayHelper
                .createForCompat(this, DISPLAY_EVENT_LISTENER);
    }

    /**
     * 外部出力イベントリスナー.
     *
     * @return イベントリスナー
     */
    private ExternalDisplayHelper.OnPresentationEventListener createPresentationEventListener() {

        return new ExternalDisplayHelper.OnPresentationEventListener() {

            @Override
            public Presentation onCreatePresentation(final Display presentationDisplay) {
                if (!mExternalDisplayFlg) {
                    mExternalDisplayFlg = true;
                    showErrorDialog(getResources().getString(R.string.contents_detail_external_display_dialog_msg));
                    mRecordCtrlView.setOnTouchListener(null);
                    hideCtrlView();
                    playPause();
                    mVideoPlayPause.setVisibility(View.VISIBLE);
                }
                return null;
            }

            @Override
            public void onDismissPresentation(final Presentation presentation) {

            }

        };
    }

    /**
     * ユーザ操作履歴送信.
     *
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
    private void showErrorDialog(final ErrorType errorType) {
        ErrorState errorState = null;
        CustomDialog.ApiOKCallback okCallback = null;
        switch (errorType) {
            case contentDetailGet:
                errorState = mContentsDetailDataProvider.getError(ContentsDetailDataProvider.ErrorType.contentsDetailGet);
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

        if (errorState == null || errorState.getErrorType() == DTVTConstants.ERROR_TYPE.SUCCESS) {
            return;
        }

        //契約誘導ダイアログを表示
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        customDialog.setContent(errorState.getErrorMessage());
        if (okCallback != null) {
            customDialog.setOkCallBack(okCallback);
        }
        customDialog.showDialog();
    }
}
