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

import com.digion.dixim.android.activation.helper.ActivationHelper;
import com.digion.dixim.android.secureplayer.MediaPlayerController;
import com.digion.dixim.android.secureplayer.MediaPlayerDefinitions;
import com.digion.dixim.android.secureplayer.SecureVideoView;
import com.digion.dixim.android.secureplayer.SecuredMediaPlayerController;
import com.digion.dixim.android.secureplayer.helper.CaptionDrawCommands;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.digion.dixim.android.util.ExternalDisplayHelper;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopContentDetailDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopScaledProListDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsChannelFragment;
import com.nttdocomo.android.tvterminalapp.struct.CalendarComparator;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.UserInfoInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.ContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.MediaVideoInfo;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
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
public class ContentDetailActivity extends BaseActivity implements ContentsDetailDataProvider.ApiDataProviderCallback,
        View.OnClickListener, MediaPlayerController.OnStateChangeListener, MediaPlayerController.OnFormatChangeListener,
        MediaPlayerController.OnPlayerEventListener, MediaPlayerController.OnErrorListener, MediaPlayerController.OnCaptionDataListener,
        RemoteControllerView.OnStartRemoteControllerUIListener, DtvContentsDetailFragment.RecordingReservationIconListener,
        TabItemLayout.OnClickTabTextListener, ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        DtvContentsChannelFragment.ChangedScrollLoadListener {

    /**
     * アスペクト比(16:9)の16.
     */
    private static final int SCREEN_RATIO_WIDTH_16 = 16;
    /**
     * アスペクト比(16:9)の9.
     */
    private static final int SCREEN_RATIO_HEIGHT_9 = 9;
    /**
     * アスペクト比(4:3)の4.
     */
    private static final int SCREEN_RATIO_WIDTH_4 = 4;
    /**
     * アスペクト比(4:3)の3.
     */
    private static final int SCREEN_RATIO_HEIGHT_3 = 3;


    //先頭のメタデータを取得用
    private static final int FIRST_VOD_META_DATA = 0;

    /* コンテンツ詳細 start */
    private TabItemLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private OtherContentsDetailData mDetailData = null;
    private VodMetaFullData mDetailFullData = null;
    private ContentsDetailDataProvider mDetailDataProvider = null;
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    private ThumbnailProvider mThumbnailProvider = null;
    private boolean isDownloadStop = false;
    private DtvContentsDetailFragmentFactory mFragmentFactory = null;
    private PurchasedVodListResponse response = null;

    private String[] mTabNames = null;
    private boolean mIsPlayer = false;
    private boolean mIsControllerVisible = false;
    private Intent mIntent = null;

    private LinearLayout mThumbnailBtn = null;
    private RelativeLayout mThumbnailRelativeLayout = null;
    private ImageView mThumbnail = null;
    private ImageView mThumbnailShadow = null;

    public static final String RECOMMEND_INFO_BUNDLE_KEY = "recommendInfoKey";
    public static final String PLALA_INFO_BUNDLE_KEY = "plalaInfoKey";
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    private static final int DTV_HIKARI_CONTENTS_SERVICE_ID = 44;
    private static final String CONTENTS_DETAIL_RESERVEDID = "1";
    private static final String MOBILEVIEWINGFLG_FLAG_ZERO = "0";
    private int mDateIndex = 0;
    private String[] dateList = null;
    private boolean isVod = false;

    /* コンテンツ詳細 end */
    /*DTV起動*/
    private static final int DTV_VERSION_STANDARD = 52000;
    private static final String METARESPONSE1 = "1";
    private static final String METARESPONSE2 = "2";
    private static final String RESERVED4_TYPE4 = "4";
    private static final String RESERVED4_TYPE7 = "7";
    private static final String RESERVED4_TYPE8 = "8";
    private static final String GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=jp.co.nttdocomo.dtv";
    private static final String DTV_PACKAGE_NAME = "jp.co.nttdocomo.dtv";
    private static final String SUPER_SPEED_START_TYPE = "dmktvideosc:///openLiveTitle?deliveryTitleId=";
    private static final String WORK_START_TYPE = "dmktvideosc:///openEpisode?episodeId=";
    private static final String TITTLE_START_TYPE = "dmktvideosc:///openTitle?titleId=";
    private String errorMessage;
    /*DTV起動*/

    /*dアニメストア起動*/
    private static final String DANIMESTORE_PACKAGE_NAME = "com.nttdocomo.android.danimeapp";
    private static final String DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.nttdocomo.android.danimeapp";
    private static final String DANIMESTORE_START_URL = "danimestore://openWebView?url=[URL]";
    private static final int DANIMESTORE_VERSION_STANDARD = 132;
    /*dアニメストア起動*/

    /*dTVチャンネル起動*/
    private static final String DTVCHANNEL_PACKAGE_NAME = "com.nttdocomo.dch";
    private static final int DTVCHANNEL_VERSION_STANDARD = 15;
    private static final String DTVCHANNEL_TELEVISION_START_URL = "dch://android.dch.nttdocomo.com/viewing?chno=";
    private static final String DTVCHANNEL_VIDEO_START_URL = "dch://android.dch.nttdocomo.com/viewing_video?crid=";
    private static final String DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.nttdocomo.dch";
    private static final String DTV_CHANNEL_CATEGORY_BROADCAST = "01";
    private static final String DTV_CHANNEL_CATEGORY_MISSED = "02";
    private static final String DTV_CHANNEL_CATEGORY_RELATION = "03";
    /*dTVチャンネル起動*/

    /*ひかりTV起動*/
    private static final String H4D_CATEGORY_TERRESTRIAL_DIGITAL = "01";
    private static final String H4D_CATEGORY_SATELLITE_BS = "02";
    private static final String H4D_CATEGORY_IPTV = "03";
    private static final String H4D_CATEGORY_DTV_CHANNEL_BROADCAST = "04";
    private static final String H4D_CATEGORY_DTV_CHANNEL_MISSED = "05";
    private static final String H4D_CATEGORY_DTV_CHANNEL_RELATION = "06";
    private static final String TV_PROGRAM = "tv_program";
    private static final String VIDEO_PROGRAM = "video_program";
    private static final String VIDEO_SERIES = "video_series";
    private static final String DTV_FLAG_ONE = "1";
    private static final String DTV_FLAG_ZERO = "0";
    private static final String BVFLG_FLAG_ONE = "1";
    private static final String BVFLG_FLAG_ZERO = "0";
    private static final String TV_SERVICE_FLAG_ZERO = "0";
    private static final String TV_SERVICE_FLAG_ONE = "1";
    private static final String CONTENT_TYPE_FLAG_ZERO = "0";
    private static final String CONTENT_TYPE_FLAG_ONE = "1";
    private static final String CONTENT_TYPE_FLAG_TWO = "2";
    private static final String CONTENT_TYPE_FLAG_THREE = "3";
    private static final int FLAG_ZERO = 0;
    /*ひかりTV起動*/

    /* player start */
    private static final long HIDE_IN_3_SECOND = 3 * 1000;
    private static final int REFRESH_VIDEO_VIEW = 0;
    private static final int REWIND_SECOND = 10 * 1000;
    private static final int FAST_SECOND = 30 * 1000;
    private static final String LOCAL_FILE_PATH = "file://";

    private SeekBar mVideoSeekBar = null;
    private SecureVideoView mSecureVideoPlayer = null;
    private SecuredMediaPlayerController mPlayerController = null;
    private MediaVideoInfo mCurrentMediaInfo = null;

    private RelativeLayout mRecordCtrlView = null;
    private RelativeLayout mVideoCtrlBar = null;
    private FrameLayout mVideoPlayPause = null;
    private TextView mVideoCurTime = null;
    private FrameLayout mFrameLayout = null;
    private TextView mVideoTotalTime = null;
    private TextView mTvTitle = null;
    private ImageView mVideoRewind10 = null;
    private ImageView mVideoFast30 = null;
    private ImageView mTvLogo = null;
    private ImageView mVideoFullScreen = null;

    private static final Handler sCtrlHandler = new Handler(Looper.getMainLooper());
    private GestureDetector mGestureDetector = null;

    private int mScreenWidth = 0;
    private boolean mCanPlay = false;
    private boolean mIsHideOperate = true;
    private boolean mIsOncreateOk = false;
    private RecordingReservationContentsDetailInfo mRecordingReservationContentsDetailInfo = null;
    private CustomDialog mRecordingReservationCustomtDialog = null;
    /*private static final int RECORDING_RESERVATION_DIALOG_INDEX_0 = 0; // 予約録画する
    private static final int RECORDING_RESERVATION_DIALOG_INDEX_1 = 1; // キャンセル*/
    /**
     * プレイヤー横画面時のシークバーの下マージン.
     */
    private static final int SEEKBAR_BOTTOM_MARGIN = 4;
    /**
     * プレイヤー横画面時のコントロールバーの下マージン.
     */
    private static final int MEDIA_CONTROL_BAR_UNDER_MARGIN = 32;
    /**
     * プレイヤー横画面時のシークバーの時間の左右マージン.
     */
    private static final int SEEKBAR_TIME_LATERAL_MARGIN = 18;
    /**
     * プレイヤー横画面時のフルスクリーンボタンの右マージン.
     */
    private static final int FULL_SCREEN_BUTTON_RIGHT_MARGIN = 16;
    /**
     * 他サービスフラグ.
     */
    private boolean mIsOtherService = false;
    /**
     * 年齢.
     */
    private int mAge = 0;

    //視聴判定
    /**
     * 対象コンテンツが視聴期限以内かどうか.
     */
    private boolean mIsLimitThirtyDay = false;
    /**
     * 対象コンテンツが視聴期限以内かどうか(VOD情報).
     */
    private boolean mIsVodLimitThirtyDay = false;
    /**
     * 対象コンテンツのチャンネルデータ.
     */
    private ChannelInfo mChannel = null;
    /**
     * 視聴可能期限.
     */
    private long mEndDate = 0L;
    /**
     * 一ヶ月(30日).
     */
    private static final int ONE_MONTH = 30;
    /**
     * 視聴不可(再生導線非表示).
     */
    private static final int DISABLE_WATCH_NO_PLAY = -4;
    /**
     * 視聴不可(契約導線を表示(CH)).
     */
    private static final int DISABLE_WATCH_LEAD_CONTRACT_CH = -3;
    /**
     * 視聴不可(契約導線を表示(VOD)).
     */
    private static final int DISABLE_WATCH_LEAD_CONTRACT_VOD = -2;
    /**
     * 視聴不可(契約導線を表示).
     */
    private static final int DISABLE_WATCH_LEAD_CONTRACT = -1;
    /**
     * 視聴不可判定未実施.
     */
    private static final int ENABLE_WATCH_NO_DEFINE = 0;
    /**
     * 視聴可能(視聴可能期限無し).
     */
    private static final int ENABLE_WATCH_NO_LIMIT = 1;
    /**
     * 視聴可能(視聴可能期限30日以内).
     */
    private static final int ENABLE_WATCH_WITH_LIMIT = 2;
    /**
     * 視聴可能かどうか.
     */
    private int mIsEnableWatch = ENABLE_WATCH_NO_DEFINE;
    /**
     * 外部出力制御.
     */
    private ExternalDisplayHelper mExternalDisplayHelper;
    private boolean mExternalDisplayFlg = false;
    /**
     * アクティベーション.
     */
    private Handler mActivationHandler = null;
    private ActivationHelper mActivationHelper;
    private String mDeviceKey;
    private ActivationThread mActivationThread;
    /**
     * 操作履歴送信.
     */
    private SendOperateLog mSendOperateLog = null;

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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeBlack);
        setContentView(R.layout.dtv_contents_detail_main_layout);
        DTVTLogger.start();
        setStatusBarColor(R.color.contents_header_background);
        showProgressBar(true);
        initView();
    }

    @Override
    protected void onResume() {
        DTVTLogger.start();
        super.onResume();
        if (mIsPlayer) {
            if (!mIsOncreateOk) {
                DTVTLogger.end();
                return;
            }
            initSecurePlayer();
            setPlayerEvent();
            setUserAgeInfo();
        }
        DTVTLogger.end();
    }

    @Override
    public void onStartCommunication() {
        DTVTLogger.start();
        super.onStartCommunication();
        if (!mIsPlayer) {
            if (mDetailDataProvider != null) {
                mDetailDataProvider.enableConnect();
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
        }
        DTVTLogger.end();
    }

    /**
     * ビュー初期化.
     */
    private void initView() {
        mIntent = getIntent();
        mThumbnailRelativeLayout = findViewById(R.id.dtv_contents_detail_layout);
        Object object = mIntent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
        if (object instanceof RecordedContentsDetailData) {
            mIsPlayer = true;
            initPlayer();
            //外部出力および画面キャプチャ制御
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            mExternalDisplayHelper = createExternalDisplayHelper();
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
     * UIを更新するハンドラー.
     */
    private final Handler viewRefresher = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            DTVTLogger.start();
            super.handleMessage(msg);
            if (null == mPlayerController) {
                if (!mIsPlayer) {
                    getScheduleDetailData();
                }
                DTVTLogger.end();
                return;
            }

            int currentPosition = mPlayerController.getCurrentPosition();
            int totalDur = mPlayerController.getDuration();
            time2TextViewFormat(mVideoCurTime, currentPosition);
            time2TextViewFormat(mVideoTotalTime, totalDur);
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
     * ミリ秒をtextViewで表示形(01,05...)に変更.
     *
     * @param textView textView
     * @param millisecond ミリ秒
     */
    private void time2TextViewFormat(final TextView textView, final int millisecond) {
        DTVTLogger.start();
        int second = millisecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String str;
        if (hh != 0) {
            str = String.format(Locale.getDefault(),"%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format(Locale.getDefault(), "%02d:%02d", mm, ss);
        }
        textView.setText(str);
        DTVTLogger.end();
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
        boolean ret = isActivited();
        if (!ret) {
            DTVTLogger.debug("TvPlayerActivity::initSecurePlayer(), return false"); //SP_SECUREPLAYER_NEED_ACTIVATION_ERROR = 1001;
            DTVTLogger.end();
            return;
        }
        preparePlayer();
        DTVTLogger.end();
    }

    /**
     * is activated.
     *
     * @return if it is activated
     */
    private boolean isActivited() {
        DTVTLogger.start();
        String path = getPrivateDataHome();
        File dir = new File(path);
        if (!dir.exists()) {
            boolean ok = dir.mkdir();
            if (!ok) {
                DTVTLogger.debug("TvPlayerActivity::isActivited(), Make dir " + path + " failed");
                DTVTLogger.end();
                return false;
            }
        }
        int ret = mPlayerController.dtcpInit(path);
        if (ret == MediaPlayerDefinitions.SP_SUCCESS) {
            DTVTLogger.end();
            return true;
        } else {
            activate();
        }
        showProgressBar(false);
        DTVTLogger.end();
        return false;
    }

    /**
     * アクティベーションダイアログ表示.
     */
    private void activate() {
        DTVTLogger.start();
        mActivationHandler = new Handler();
        mDeviceKey = EnvironmentUtil.getPrivateDataHome(ContentDetailActivity.this, EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
        if (TextUtils.isEmpty(mDeviceKey)) {
            return;
        }
        mActivationHelper = new ActivationHelper(this, mDeviceKey);
        mActivationThread = new ActivationThread();
        CustomDialog customDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
		customDialog.setTitle(getResources().getString(R.string.activation_confirm_dialog_title));
		customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
			@Override
			public void onOKCallback(final boolean isOK) {
				if (isOK) {
					runActivation();
				}
			}
		});
		customDialog.showDialog();
        DTVTLogger.end();
    }

    /**
     * アクティベーションThread.
     */
    private class ActivationThread extends Thread {

        @Override
        public void run() {
            int ret = RESULT_FIRST_USER + 2;
            if (ContentDetailActivity.this.mActivationHelper != null) {
                ret = ContentDetailActivity.this.mActivationHelper.activation(ContentDetailActivity.this.mDeviceKey);
            }
            final int result = ret;
            if (ContentDetailActivity.this.mActivationHandler == null) {
                return;
            }
            ContentDetailActivity.this.mActivationHandler.post(new Runnable() {
                @Override
                public void run() {
                    ContentDetailActivity.this.onThreadFinish(result);
                }
            });
        }
    }

    /**
     * アクティベーション処理開始.
     */
    private void runActivation() {
        showProgressBar(true);
        if (mActivationThread != null) {
            mActivationThread.start();
        }
    }

    /**
     * アクティベーション処理終了.
     *
     * @param result 処理結果
     */
    private void onThreadFinish(final int result) {
        showProgressBar(false);
        if (result == ActivationHelper.ACTC_OK) {
            onResume();
        } else {
            showErrorDialog(getResources().getString(R.string.activation_failed_error));
        }
    }

    /**
     * 機能：プレバイトデータフォルダを戻す.
     *
     * @return プレバイトデータフォルダ
     */
    private String getPrivateDataHome() {
        DTVTLogger.start();
        String ret = EnvironmentUtil.getPrivateDataHome(this,
                EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
        DTVTLogger.end();
        return ret;
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
     * start playing.
     */
    private void playStart() {
        DTVTLogger.start();
        synchronized (this) {
            if (mCanPlay) {
                playButton();
                mSecureVideoPlayer.setBackgroundResource(0);
                mThumbnailShadow.setVisibility(View.GONE);
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
                pauseButton();
                mPlayerController.pause();
                mSecureVideoPlayer.setBackgroundResource(R.mipmap.thumb_material_mask_overlay_gradation);
            }
        }
        DTVTLogger.end();
    }

    /**
     * pause button function.
     */
    private void pauseButton() {
        DTVTLogger.start();
        if (null == mVideoPlayPause) {
            return;
        }
        mVideoPlayPause.getChildAt(0).setVisibility(View.VISIBLE);
        mVideoPlayPause.getChildAt(1).setVisibility(View.GONE);
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
                    if (mVideoPlayPause.getVisibility() == View.VISIBLE) {
                        if (mIsHideOperate) {
                            hideCtrlView();
                        }
                    } else {
                        mVideoPlayPause.setVisibility(View.VISIBLE);
                        mVideoRewind10.setVisibility(View.VISIBLE);
                        mVideoFast30.setVisibility(View.VISIBLE);
                        mVideoCtrlBar.setVisibility(View.VISIBLE);
                        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            mTvTitle.setVisibility(View.VISIBLE);
                            mTvLogo.setVisibility(View.VISIBLE);
                        }
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

    /**
     * hide video ctrl mView.
     */
    private void hideCtrlView() {
        DTVTLogger.start();
        mVideoPlayPause.setVisibility(View.INVISIBLE);
        mVideoRewind10.setVisibility(View.INVISIBLE);
        mVideoFast30.setVisibility(View.INVISIBLE);
        mVideoCtrlBar.setVisibility(View.INVISIBLE);
        mTvTitle.setVisibility(View.INVISIBLE);
        mTvLogo.setVisibility(View.INVISIBLE);
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
     * シークバーを先頭に戻す.
     */
    private void setProgress0() {
        DTVTLogger.start();
        mVideoSeekBar.setProgress(0);
        playButton();
        DTVTLogger.end();
    }

    /**
     * play button function.
     */
    private void playButton() {
        DTVTLogger.start();
        if (null == mVideoPlayPause) {
            return;
        }
        View child0 = mVideoPlayPause.getChildAt(0);
        View child1 = mVideoPlayPause.getChildAt(1);
        if (null != child0) {
            child0.setVisibility(View.GONE);
        }
        if (null != child1) {
            child1.setVisibility(View.VISIBLE);
        }
        DTVTLogger.end();
    }

    /**
     * set current player info.
     *
     * @return whether succeed
     */
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
        String contentFormat = "contentFormat";
        String type2;
        int isDtcp = type.indexOf("application/x-dtcp1");
        if (isDtcp > 0) {
            String http = "http-get:*:";
            int startPos = http.length() - 1;
            int endPos = type.indexOf(":DLNA.ORG_OP");
            if (startPos > 0 && endPos > 0 && startPos < endPos && startPos < type.length() && endPos < type.length()) {
                contentFormat = type.substring(startPos + 1, endPos);
                type2 = "application/x-dtcp1";
            } else {
                DTVTLogger.debug("setCurrentMediaInfo failed");
                return false;
            }
        } else {
            type2 = type;
        }

        boolean mIsLocalPlaying = (ContentsAdapter.DOWNLOAD_STATUS_COMPLETED == datas.getDownLoadStatus());
        if (!mIsLocalPlaying) {
            mCurrentMediaInfo = new MediaVideoInfo(
                    uri,           //uri
                    type2,         //"application/x-dtcp1", "video/mp4", RESOURCE_MIMETYPE
                    size,          //SIZE
                    duration,      //DURATION
                    bitRate,       //BITRATE
                    true,         //IS_SUPPORTED_BYTE_SEEK
                    true,         //IS_SUPPORTED_TIME_SEEK
                    true,         //IS_AVAILABLE_CONNECTION_STALLING
                    true,         //IS_LIVE_MODE
                    false,        //IS_REMOTE
                    title,         //TITLE
                    contentFormat
            );
        } else {
            String dlFile = datas.getDlFileFullPath();

            File f = new File(dlFile);
//            if (f.isDirectory()) {
//                File[] fs = f.listFiles();
//            }
            if (!f.exists()) {
                DTVTLogger.debug(f  + " not exists");
                onError("再生するファイルは存在しません");
                return false;
            }
            //test b
//            File f2=new File("/data/user/0/com.nttdocomo.android.tvterminalapp/files/cm_work_dmp/");
//            File[] f2s= f2.listFiles();
//            long fSize= getFileSize(f);
//
//            File f3=new File("/data/user/0/com.nttdocomo.android.tvterminalapp/files/cm_work_player/");
//            File[] f3s= f3.listFiles();
//            File[] fs= f.listFiles();
//
//            File hwidPlayer=new File("/data/user/0/com.nttdocomo.android.tvterminalapp/files/cm_work_player/hwid");
//            File hwidDmp= new File("/data/user/0/com.nttdocomo.android.tvterminalapp/files/cm_work_dmp/hwid");
//            String hwidPlayerS = ReadFile(hwidPlayer);
//            String hwidDmpS = ReadFile(hwidDmp);
//            boolean isSame=true;
//            if(hwidPlayerS.length()!=hwidDmpS.length()){
//                isSame=false;
//            }else {
//                isSame= hwidPlayerS.equals(hwidDmpS);
//            }
//
//            File db_postPlayer=new File("/data/user/0/com.nttdocomo.android.tvterminalapp/files/cm_work_player/db_post");
//            File db_postDmp= new File("/data/user/0/com.nttdocomo.android.tvterminalapp/files/cm_work_dmp/db_post");
//            String db_postPlayerS = ReadFile(db_postPlayer);
//            String db_postDmpS = ReadFile(db_postDmp);
//            if(db_postPlayerS.length()!=db_postDmpS.length()){
//                isSame=false;
//            }else {
//                isSame= db_postPlayerS.equals(db_postDmpS);
//            }

            //test e
            uri = Uri.parse(LOCAL_FILE_PATH + dlFile);
            //long ss = (long) Integer.parseInt(datas.getClearTextSize());
            mCurrentMediaInfo = new MediaVideoInfo(
                    uri,           //uri
                    type2,         //"application/x-dtcp1", "video/mp4", RESOURCE_MIMETYPE
                    size,          //SIZE
                    duration,      //DURATION
                    bitRate,       //BITRATE
                    true,         //IS_SUPPORTED_BYTE_SEEK
                    true,         //IS_SUPPORTED_TIME_SEEK
                    false,         //IS_AVAILABLE_CONNECTION_STALLING
                    false,         //IS_LIVE_MODE
                    false,        //IS_REMOTE
                    title,         //TITLE
                    contentFormat
            );
        }

        DTVTLogger.end();
        return true;
    }

//    /**
//     * TODO: 削除予定(テスト用処理).
//     * @param file file
//     * @return data
//     */
//    public String ReadFile(final File file) {
//        FileInputStream inStream = null;
//        ByteArrayOutputStream outStream = null;
//        byte[] data;
//        try {
//            inStream = new FileInputStream(file);
//            byte[] buffer = new byte[1024];
//            int len;
//            outStream = new ByteArrayOutputStream();
//            while ((len = inStream.read(buffer)) != -1) {
//                outStream.write(buffer, 0, len);
//            }
//
//            data = outStream.toByteArray();
//        } catch (Exception e) {
//            DTVTLogger.debug(e);
//            return null;
//        } finally {
//            try {
//                if (outStream != null) {
//                    outStream.close();
//                }
//                if (inStream != null) {
//                    inStream.close();
//                }
//            } catch (IOException e) {
//                DTVTLogger.debug(e);
//            }
//        }
//
//        return new String(data);
//    }
//    //test e

//    /**
//     * ファイルサイズ取得.
//     *
//     * @param file file
//     * @return ファイルサイズ
//     */
//    private static long getFileSize(final File file) {
//        if (file == null) {
//            return 0;
//        }
//        long size = 0;
//        if (file.exists()) {
//            FileInputStream fis;
//            try {
//                fis = new FileInputStream(file);
//                size = fis.available();
//            } catch (IOException e) {
//                DTVTLogger.debug(e);
//                return 0;
//            }
//        }
//        return size;
//    }

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
        CustomDialog customDialog = new CustomDialog(getApplicationContext(), CustomDialog.DialogType.ERROR);
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
        showMessage(getApplicationContext().getString(R.string.contents_player_bad_contents_info));
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
        mDetailDataProvider = new ContentsDetailDataProvider(this);
        String[] cRid;
        if (mDetailData != null) {
            DTVTLogger.debug("contentId:" + mDetailData.getContentId());
            cRid = new String[1];
            cRid[cRid.length - 1] = mDetailData.getContentId();
            int ageReq = mDetailData.getAge();
            mDetailDataProvider.getContentsDetailData(cRid, "", ageReq);
        } else {
            // TODO Home画面から受け取れるようになったら消す
            cRid = new String[1];
            cRid[cRid.length - 1] = "682017101601";
            mDetailDataProvider.getContentsDetailData(cRid, "", 1);
        }
    }

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
        int[] channelNos = new int[mChannel.getChNo()];
        dateList = null;
        if (mDateIndex <= 6) { //一週間以内
            dateList = new String[1];
        }
        if (dateList != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat todaySdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD, Locale.JAPAN);
                String today = todaySdf.format(calendar.getTime()) + DateUtils.DATE_STANDARD_START;
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
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
                dateList[0] = sdf.format(calendar.getTime());
                mDateIndex++;
            } catch (ParseException e) {
                channelLoadCompleted();
                DTVTLogger.debug(e);
            }
            if (channelNos.length == 0 || channelNos[0] == -1) {
                //TODO channelNoが未指定の場合。エラー定義未決定のため一旦エラーを表示して戻る
                DTVTLogger.error("No channel number");
                Toast.makeText(this, "No channel number", Toast.LENGTH_SHORT).show();
                return;
            }
            mScaledDownProgramListDataProvider.getProgram(channelNos, dateList, 1);
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

    @Override
    public void onChannelLoadMore() {
        getChannelDetailByPageNo();
    }

    @Override
    public void onUserVisibleHint() {
        loadHandler.removeCallbacks(loadRunnable);
    }

    /**
     * サムネイルエリア文字表示.
     *
     * @param content 表示内容
     */
    private void setThumbnailText(final String content) {
        mThumbnailBtn.setVisibility(View.VISIBLE);
        setThumbnailShadow();
        TextView startAppIcon = findViewById(R.id.view_contents_button_text);
        startAppIcon.setVisibility(View.VISIBLE);
        startAppIcon.setText(content);
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
        if (!TextUtils.isEmpty(url)) {
            if (mThumbnailProvider == null) {
                mThumbnailProvider = new ThumbnailProvider(this);
            }
            if (!isDownloadStop) {
                mThumbnail.setTag(url);
                setThumbnail();
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
        mThumbnailShadow.setLayoutParams(layoutParams);
        mThumbnailShadow.setVisibility(View.VISIBLE);
        mThumbnail.setImageBitmap(bitmap);
    }

    /**
     * データの初期化.
     */
    private void initContentData() {
        mFrameLayout = findViewById(R.id.header_watch_by_tv);
        // タブ数を先に決定するため、コンテンツ詳細のデータを最初に取得しておく
        mDetailData = mIntent.getParcelableExtra(RECOMMEND_INFO_BUNDLE_KEY);
        if (mDetailData != null) {
            int serviceId = mDetailData.getServiceId();
            if (serviceId == OtherContentsDetailData.DTV_CONTENTS_SERVICE_ID
                    || serviceId == OtherContentsDetailData.D_ANIMATION_CONTENTS_SERVICE_ID
                    || serviceId == OtherContentsDetailData.DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                // 他サービス(dtv/dtvチャンネル/dアニメ)フラグを立てる
                mIsOtherService = true;
            }
//            STBに接続している　「テレビで視聴」が表示
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
                }
            }

            //「mobileViewingFlg」が「0」の場合モバイル視聴不可
            if (MOBILEVIEWINGFLG_FLAG_ZERO.equals(mDetailData.getMobileViewingFlg())) {
                setThumbnailText(getResources().getString(R.string.contents_detail_thumbnail_text));
                mThumbnailBtn.setEnabled(false);
            } else {
                //  dアニメの場合
                if (serviceId == D_ANIMATION_CONTENTS_SERVICE_ID) {
                    setThumbnailText(getResources().getString(R.string.d_anime_store_content_service_start_text));
                    //ｄTVの場合
                } else if (serviceId == DTV_CONTENTS_SERVICE_ID) {
                    //DTVコンテンツ　「reserved2」が「1」　Androidのモバイル視聴不可
                    if (CONTENTS_DETAIL_RESERVEDID.equals(mDetailData.getReserved2())) {
                        setThumbnailText(getResources().getString(R.string.contents_detail_thumbnail_text));
                        mThumbnailBtn.setEnabled(false);
                    } else {
                        setThumbnailText(getResources().getString(R.string.dtv_content_service_start_text));
                    }
                    //dtvチャンネルの場合
                } else if (serviceId == DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                    setThumbnailText(getResources().getString(R.string.dtv_channel_service_start_text));
                }
            }
            setTitleAndThumbnail(mDetailData.getTitle(), mDetailData.getThumb());
        } else {  //plalaサーバーから
            mDetailData = mIntent.getParcelableExtra(PLALA_INFO_BUNDLE_KEY);
        }
        if (mIsOtherService) {
            // コンテンツ詳細(他サービスの時は、タブ一つに設定する)
            mTabNames = getResources().getStringArray(R.array.other_service_contents_detail_tabs);
        } else {
            if (mDetailData != null && DTV_FLAG_ONE.equals(mDetailData.getDtv())) {
                if (VIDEO_SERIES.equals(mDetailData.getDispType()) || VIDEO_PROGRAM.equals(mDetailData.getDispType())) {
                    // VOD
                    isVod = true;
                    mTabNames = getResources().getStringArray(R.array.video_contents_detail_tabs);
                } else {
                    // コンテンツ詳細(他サービスの時は、タブ一つに設定する)
                    mTabNames = getResources().getStringArray(R.array.other_contents_detail_tabs);
                }
            } else {
                // コンテンツ詳細(他サービスの時は、タブ一つに設定する)
                mTabNames = getResources().getStringArray(R.array.other_contents_detail_tabs);
            }
        }

        mFragmentFactory = new DtvContentsDetailFragmentFactory();
        ContentsDetailPagerAdapter contentsDetailPagerAdapter
                = new ContentsDetailPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentsDetailPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                if (position == 1) {
                    if (isVod) {
                        return;
                    }
                    getChannelFragment().initLoad();
                }
                loadHandler.postDelayed(loadRunnable, 1200);
            }
        });
        //レコメンド（serviceId 44）若しくはぷららの場合
        if (!mIsOtherService) {
            viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
        } else {
            showProgressBar(false);
        }
    }

    /**
     * ハンドラー.
     */
    private final Handler loadHandler = new Handler();
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
     * set thumbnail.
     *
     * @param url URL
     */
    private void setPlayerLogoThumbnail(final String url) {
        if (!TextUtils.isEmpty(url)) {
            ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(this);
            mTvLogo.setImageResource(R.drawable.error_list);
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
    private void setPlayerProgressView(final boolean isLandscape) {
        RelativeLayout progressLayout = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_progress_ll);
        if (isLandscape) {
            //端末横向き
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, (int) getDensity() * MEDIA_CONTROL_BAR_UNDER_MARGIN);
            progressLayout.setLayoutParams(layoutParams);

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

            progressLayout.addView(mVideoCurTime);
            progressLayout.addView(mVideoFullScreen, 2);
            progressLayout.addView(mVideoTotalTime, 3);
        } else {
            //端末縦向き
            if (progressLayout.getChildCount() > 1) {
                progressLayout.removeViewAt(0);
                progressLayout.removeViewAt(1);
                progressLayout.removeViewAt(1);
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
                DTVTLogger.start();
                time2TextViewFormat(mVideoCurTime, i);
                DTVTLogger.end();
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
        mThumbnailShadow = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail_shadow);
        mThumbnailBtn = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail_btn);
        mThumbnailBtn.setOnClickListener(this);
        if (mIsPlayer) {
            setPlayerScroll();
        } else {
            mViewPager = findViewById(R.id.dtv_contents_detail_main_layout_vp);
            initContentData();
            initTab();
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
        detailFragment.changeVisibilityRecordingReservationIcon(View.VISIBLE);
        detailFragment.setRecordingReservationIconListener(this);
    }

    @Override
    public void onContentsDetailInfoCallback(final ArrayList<VodMetaFullData> contentsDetailInfo, final boolean clipStatus) {
        //詳細情報取得して、更新する
        if (contentsDetailInfo != null && contentsDetailInfo.size() > 0) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            mDetailFullData = contentsDetailInfo.get(0);
            if (TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
                //tv_programの場合
                if (TV_SERVICE_FLAG_ZERO.equals(mDetailFullData.getmTv_service())) {
                    //tv_serviceは0の場合
                    setRecordingData(detailFragment);
                } else if (TV_SERVICE_FLAG_ONE.equals(mDetailFullData.getmTv_service())) {
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
            detailFragment.mOtherContentsDetailData.setTitle(mDetailFullData.getTitle());
            setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmDtv_thumb_448_252());
            detailFragment.mOtherContentsDetailData.setVodMetaFullData(contentsDetailInfo.get(FIRST_VOD_META_DATA));
            detailFragment.mOtherContentsDetailData.setDetail(mDetailFullData.getSynop());
            // コンテンツ状態を反映
            detailFragment.mOtherContentsDetailData.setClipStatus(clipStatus);
            detailFragment.mOtherContentsDetailData.setDispType(mDetailFullData.getDisp_type());
            detailFragment.mOtherContentsDetailData.setSearchOk(mDetailFullData.getmSearch_ok());
            detailFragment.mOtherContentsDetailData.setDtv(mDetailFullData.getDtv());
            detailFragment.mOtherContentsDetailData.setDtvType(mDetailFullData.getDtvType());

            detailFragment.noticeRefresh();
            String[] credit_array = mDetailFullData.getmCredit_array();
            if (credit_array != null && credit_array.length > 0) {
                mDetailDataProvider.getRoleListData();
            }
            if (!TextUtils.isEmpty(mDetailFullData.getmService_id())) {
                if (mScaledDownProgramListDataProvider == null) {
                    mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(this);
                }
                mScaledDownProgramListDataProvider.getChannelList(0, 0, "", 1);
            }
            //ログイン状態でしか送信しない
            if (!TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferencesDaccountId(this))) {
                if (mSendOperateLog == null) {
                    mSendOperateLog = new SendOperateLog(getApplicationContext());
                }
                mSendOperateLog.sendOpeLog(mDetailData, mDetailFullData);
            }
            if (DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
                if (getStbStatus()) {
                    createRemoteControllerView(true);
                    mIsControllerVisible = true;
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
                    setStartRemoteControllerUIListener(this);
                }
                UserInfoInsertDataManager dataManager = new UserInfoInsertDataManager(this);
                dataManager.readUserInfoInsertList();
                String contractInfo = UserInfoUtils.getUserContractInfo(dataManager.getmUserData());
                //ひかりTV未契約の場合
                if (contractInfo == null || contractInfo.isEmpty() || UserInfoUtils.CONTRACT_INFO_NONE.equals(contractInfo)) {
                    DTVTLogger.debug("contractInfo:---" + contractInfo);
                    mThumbnailBtn.setVisibility(View.GONE);
                    LinearLayout contractLeadingView = findViewById(R.id.contract_leading_view);
                    contractLeadingView.setVisibility(View.VISIBLE);
                    TextView contractLeadingText = findViewById(R.id.contract_leading_text);
                    contractLeadingText.setText(getResources().getString(R.string.contents_detail_contract_text));
                    Button contractLeadingButton = findViewById(R.id.contract_leading_button);
                    contractLeadingButton.setText(getResources().getString(R.string.contents_detail_contract_leading_button));
                    contractLeadingButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            if (getStbStatus()) {
                                contentDetailRemoteController();
                            } else {
                                showErrorDialog(getResources().getString(R.string.main_setting_connect_error_message));
                            }
                        }
                    });
                } else { //ひかりTV契約者の場合
                    //ひかりTV中にDTVの場合
                    if (VIDEO_PROGRAM.equals(mDetailData.getDispType())
                            || VIDEO_SERIES.equals(mDetailData.getDispType())) {
                        if (METARESPONSE1.equals(mDetailFullData.getDtv())) {
                            setThumbnailText(getResources().getString(R.string.dtv_content_service_start_text));
                        }
                    }
                    //ひかりTV中にdtvチャンネルの場合
                    if (TV_PROGRAM.equals(mDetailData.getDispType())) {
                        if (H4D_CATEGORY_DTV_CHANNEL_RELATION.equals(mDetailData.getCategoryId())
                                || H4D_CATEGORY_DTV_CHANNEL_BROADCAST.equals(mDetailData.getCategoryId())
                                || H4D_CATEGORY_DTV_CHANNEL_MISSED.equals(mDetailData.getCategoryId())) {
                            setThumbnailText(getResources().getString(R.string.dtv_channel_service_start_text));
                        }
                    }
                }
            } else { //レコメンドサーバー以外のひかりTV
                if (getStbStatus()) {
                    createRemoteControllerView(true);
                    mIsControllerVisible = true;
                    setStartRemoteControllerUIListener(this);
                }
            }
        } else {
            //データ取得失敗時
            if (!mIsOtherService) {
                setThumbnail();
            }
        }
        showProgressBar(false);
    }

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

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        if (channels != null) {
            //チャンネル情報取得して、更新する
            if (!TextUtils.isEmpty(mDetailFullData.getmService_id())) {
                DtvContentsDetailFragment detailFragment = getDetailFragment();
                for (int i = 0; i < channels.size(); i++) {
                    ChannelInfo channel = channels.get(i);
                    if (mDetailFullData.getmService_id().equals(channel.getServiceId())) {
                        mChannel = channel;
                        String channelName = channel.getTitle();
                        detailFragment.mOtherContentsDetailData.setChannelName(channelName);
                        String channelStartDate = channel.getStartDate();
                        String channelEndDate = channel.getEndDate();
                        detailFragment.mOtherContentsDetailData.setChannelDate(getDateFormat(channelStartDate, channelEndDate));
                        break;
                    }
                }
                detailFragment.refreshChannelInfo();
            }
            //コンテンツの視聴可否判定を行う
            checkWatchContents();
            //コンテンツの視聴可否判定に基づいてUI操作を行う
            if (mIsEnableWatch != ENABLE_WATCH_NO_DEFINE) {
                changeUIBasedContractInfo();
            }
        }
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        if (channelsInfo != null && channelsInfo.getChannels() != null) {
            ArrayList<ChannelInfo> channels = channelsInfo.getChannels();
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
                            if (dateList != null) {
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
                                contentsData.setRequestData(scheduleInfo.getClipRequestData());
                                contentsData.setThumURL(scheduleInfo.getImageUrl());
                                contentsData.setTime(scheduleInfo.getStartTime().substring(11, 16));
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
        }
    }

    /**
     * 日付を取得.
     *
     * @return 日付
     */
    private String getDate() {
        String subTitle = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
        try {
            Calendar calendar = Calendar.getInstance(Locale.JAPAN);
            calendar.setTime(sdf.parse(dateList[0]));
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            subTitle = (month + 1) + "/" + day + "（" +
                    DateUtils.STRING_DAY_OF_WEEK[week] + "）";
        } catch (ParseException e) {
            DTVTLogger.debug(e);
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
    private void sort(final ArrayList<ChannelInfo> channels) {
        for (ChannelInfo channel : channels) {
            Collections.sort(channel.getSchedules(), new CalendarComparator());
        }
    }

    /**
     * 時刻.
     *
     * @param start 開始時刻
     * @param end   　終了時刻
     * @return データ整形
     */
    private String getDateFormat(final String start, final String end) {
        String channelDate = "";
        if (!TextUtils.isEmpty(start)) {
            String replaceStart = start.replaceAll("-", "/");
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
            StringBuilder startBuilder = new StringBuilder();
            startBuilder.append(replaceStart.substring(0, 10));
            startBuilder.append(replaceStart.substring(11, 19));
            try {
                Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                calendar.setTime(sdf.parse(startBuilder.toString()));
                String[] strings = {String.valueOf(calendar.get(Calendar.MONTH)), "/",
                        String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), " (",
                        DateUtils.STRING_DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK)], ") ",
                        start.substring(11, 16), " - ",
                        end.substring(11, 16)};
                channelDate = StringUtils.getConnectString(strings);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
        }
        return channelDate;
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
                    initPlayerView();
                    setPlayerEvent();
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    initPlayerView();
                    setPlayerEvent();
                }
                hideCtrlViewAfterOperate();
                break;
            case R.id.dtv_contents_detail_main_layout_thumbnail_btn:
                if (mIsEnableWatch == DISABLE_WATCH_LEAD_CONTRACT) {
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
                                    errorMessage = getResources().getString(R.string.dtv_content_service_update_dialog);
                                    showErrorDialog(errorMessage);
                                } else {
                                    //RESERVED4は4の場合
                                    if (RESERVED4_TYPE4.equals(mDetailData.getReserved4())) {
                                        startApp(WORK_START_TYPE + mDetailData.getContentId());
                                        //RESERVED4は7,8の場合
                                    } else if (RESERVED4_TYPE7.equals(mDetailData.getReserved4())
                                            || RESERVED4_TYPE8.equals(mDetailData.getReserved4())) {
                                        startApp(SUPER_SPEED_START_TYPE + mDetailData.getContentId());
                                        //その他の場合
                                    } else {
                                        startApp(TITTLE_START_TYPE + mDetailData.getContentId());
                                    }
                                }
                                //DTVアプリ存在しない場合
                            } else {
                                toGooglePlay(GOOGLEPLAY_DOWNLOAD_URL);
                            }
                        }
                    });
                    startAppDialog.showDialog();

                    break;
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
                                    errorMessage = getResources().getString(R.string.d_anime_store_content_service_update_dialog);
                                    showErrorDialog(errorMessage);
                                } else {
                                    startApp(DANIMESTORE_START_URL);
                                }
                            } else {
                                toGooglePlay(DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL);
                            }
                        }
                    });
                    startAppDialog.showDialog();
                    break;
                } else if (mDetailData != null && mDetailData.getServiceId() == DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                    final CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                    startAppDialog.setContent(getResources().getString(R.string.dtv_channel_service_start_dialog));
                    startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                        @Override
                        public void onOKCallback(final boolean isOK) {
                            int localVersionCode = getVersionCode(DTVCHANNEL_PACKAGE_NAME);
                            if (isAppInstalled(ContentDetailActivity.this, DTVCHANNEL_PACKAGE_NAME)) {
                                //バージョンコードは15
                                if (localVersionCode < DTVCHANNEL_VERSION_STANDARD) {
                                    errorMessage = getResources().getString(R.string.dtv_channel_service_update_dialog);
                                    showErrorDialog(errorMessage);
                                } else {    //テレビ再生  「categoryId」が「01」の場合
                                    if (DTV_CHANNEL_CATEGORY_BROADCAST.equals(mDetailData.getCategoryId())) {
                                        startApp(DTVCHANNEL_TELEVISION_START_URL + mDetailData.getChannelId());
                                        DTVTLogger.debug("channelId :----" + mDetailData.getChannelId());
                                        //ビデオ再生  「categoryId」が「02」または「03」の場合
                                    } else if (DTV_CHANNEL_CATEGORY_MISSED.equals(mDetailData.getCategoryId())
                                            || DTV_CHANNEL_CATEGORY_RELATION.equals(mDetailData.getCategoryId())) {
                                        startApp(DTVCHANNEL_VIDEO_START_URL + mDetailData.getContentId());
                                        DTVTLogger.debug("ContentId :----" + mDetailData.getContentId());
                                    }
                                }
                            } else {
                                toGooglePlay(DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL);
                            }
                        }
                    });
                    startAppDialog.showDialog();
                    break;
                } else if (mDetailData != null && mDetailData.getServiceId() == DTV_HIKARI_CONTENTS_SERVICE_ID) {
                    //ひかりTV内DTV
                    if (METARESPONSE1.equals(mDetailFullData.getDtv())) {
                        String dtvType = mDetailFullData.getDtvType();
                        if (dtvType != null && dtvType.equals(METARESPONSE1)) {
                            startApp(WORK_START_TYPE + mDetailFullData.getTitle_id());
                        } else if (dtvType != null && dtvType.equals(METARESPONSE2)) {
                            startApp(SUPER_SPEED_START_TYPE + mDetailFullData.getTitle_id());
                        } else {
                            startApp(TITTLE_START_TYPE + mDetailFullData.getTitle_id());
                        }
                    }
                    //ひかりTV内DTVチャンネル
                    // テレビ再生「disp_type」が「tv_program」
                    if (TV_PROGRAM.equals(mDetailData.getDispType())) {
                        if (TV_SERVICE_FLAG_ONE.equals(mDetailData.getTvService())) {
                            //「contents_type」が「0」または未設定
                            if (CONTENT_TYPE_FLAG_ZERO.equals(mDetailData.getContentsType())
                                    || null == mDetailData.getContentsType()) {
                                DTVTLogger.debug("contentsType :----" + mDetailData.getContentsType());
                                startApp(DTVCHANNEL_TELEVISION_START_URL + mDetailFullData.getmChno());
                                DTVTLogger.debug("chno :----" + mDetailFullData.getmChno());
                                //ビデオ再生 「disp_type」が「tv_program」かつ「contents_type」が「1」または「2」または「3」
                            } else if (CONTENT_TYPE_FLAG_ONE.equals(mDetailData.getContentsType())
                                    || CONTENT_TYPE_FLAG_TWO.equals(mDetailData.getContentsType())
                                    || CONTENT_TYPE_FLAG_THREE.equals(mDetailData.getContentsType())) {
                                startApp(DTVCHANNEL_VIDEO_START_URL + mDetailFullData.getCrid());
                                DTVTLogger.debug("crid :----" + mDetailFullData.getCrid());
                            }
                        }
                    }
                }
                break;
            default:
                super.onClick(v);
        }
        DTVTLogger.end();
    }

    /**
     * 機能：APP起動.
     *
     * @param url URL
     */
    private void startApp(final String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    @Override
    public void onStartRemoteControl(final boolean isFromHeader) {
        DTVTLogger.start();
        // サービスIDにより起動するアプリを変更する
        if (mDetailData != null) {
            setRelayClientHandler();
            switch (mDetailData.getServiceId()) {
                case DTV_CONTENTS_SERVICE_ID: // dTV
                    if (!isFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mDetailData.getContentId());
                    break;
                case D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                    if (!isFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mDetailData.getContentId());
                    break;
                case DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
                    if (!isFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    //番組の場合
                    if (DTV_CHANNEL_CATEGORY_BROADCAST.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(
                                RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_BROADCAST,
                                mDetailData.getContentId(), mDetailData.getChannelId());
                        //VOD(見逃し)の場合
                    } else if (DTV_CHANNEL_CATEGORY_MISSED.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(
                                RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_MISSED,
                                mDetailData.getContentId(), mDetailData.getChannelId());
                        //VOD(関連)の場合
                    } else if (DTV_CHANNEL_CATEGORY_RELATION.equals(mDetailData.getCategoryId())) {
                        requestStartApplicationDtvChannel(
                                RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL,
                                RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_RELATION,
                                mDetailData.getContentId(), mDetailData.getChannelId());
                    }
                    break;
                case OtherContentsDetailData.DTV_HIKARI_CONTENTS_SERVICE_ID://ひかりTV
                   if(mDetailFullData == null){
                        break;
                    }
                    String[] liinfArray = mDetailFullData.getmLiinf_array();
                    String puid = mDetailFullData.getPuid();
                    if (!isFromHeader) {
                        setRemoteProgressVisible(View.VISIBLE);
                    }
                    if (VIDEO_PROGRAM.equals(mDetailData.getDispType())) {
                        if (DTV_FLAG_ZERO.equals(mDetailData.getDtv()) || TextUtils.isEmpty(mDetailData.getDtv())
                                || FLAG_ZERO == mDetailData.getDtv().trim().length()) {
                            if (BVFLG_FLAG_ONE.equals(mDetailFullData.getBvflg())) {
                                requestStartApplicationHikariTvCategoryHikaritvVod(mDetailFullData.getPuid(),
                                        mDetailFullData.getCid(), mDetailFullData.getCrid());
                            } else if (BVFLG_FLAG_ZERO.equals(mDetailFullData.getBvflg()) || null == mDetailFullData.getBvflg()) {
                                //liinfを"|"区切りで分解する
                                if(response == null){
                                    break;
                                }
                                ArrayList<ActiveData> activeDatas = response.getVodActiveData();
                                for (String liinf : liinfArray) {
                                    String[] column = liinf.split(Pattern.quote("|"), 0);
                                    for (ActiveData activeData : activeDatas) {
                                        String licenseId = activeData.getLicenseId();
                                        //メタレスポンスのpuid、liinf_arrayのライセンスID（パイプ区切り）と購入済みＶＯＤ一覧取得IF「active_list」の「license_id」と比較して一致した場合
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
                                                requestStartApplicationHikariTvCategoryHikaritvVod(licenseId,
                                                        mDetailFullData.getCid(), mDetailFullData.getCrid());
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (DTV_FLAG_ONE.equals(mDetailData.getDtv())) {
                            //ひかりTV内dTVのVOD,「episode_id」を通知する
                            requestStartApplicationHikariTvCategoryDtvVod(mDetailFullData.getEpisode_id());
                        }
                        //「disp_type」が「video_series」の場合
                    } else if (VIDEO_SERIES.equals(mDetailData.getDispType())) {
                        // ひかりTV内VOD(dTV含む)のシリーズ
                        requestStartApplicationHikariTvCategoryDtvSvod(mDetailFullData.getCrid());
                        //「disp_type」が「video_program」の場合
                    } else if (TV_PROGRAM.equals(mDetailData.getDispType())) {
                        //「tv_service」が「0」の場合 ひかりTVの番組
                        if (TV_SERVICE_FLAG_ZERO.equals(mDetailFullData.getmTv_service())) {
                            //ひかりTVの番組 地デジ
                            if (H4D_CATEGORY_TERRESTRIAL_DIGITAL.equals(mDetailData.getCategoryId())) {
                                requestStartApplicationHikariTvCategoryTerrestrialDigital(mDetailFullData.getmChno());
                                //ひかりTVの番組 BS
                            } else if (H4D_CATEGORY_SATELLITE_BS.equals(mDetailData.getCategoryId())) {
                                requestStartApplicationHikariTvCategorySatelliteBs(mDetailFullData.getmChno());
                                //ひかりTVの番組 IPTV
                            } else if (H4D_CATEGORY_IPTV.equals(mDetailData.getCategoryId())) {
                                requestStartApplicationHikariTvCategoryIptv(mDetailFullData.getmChno());
                            }
                            //「tv_service」が「1」の場合
                        } else if (TV_SERVICE_FLAG_ONE.equals(mDetailFullData.getmTv_service())) {
                            //「contents_type」が「0」または未設定の場合  ひかりTV内dTVチャンネルの番組
                            if (CONTENT_TYPE_FLAG_ZERO.equals(mDetailData.getContentsType())
                                    || null == mDetailFullData.getmContent_type()) {
                                //中継アプリに「chno」を通知する
                                requestStartApplicationHikariTvCategoryDtvchannelBroadcast(mDetailFullData.getmChno());
                            } else if (CONTENT_TYPE_FLAG_ONE.equals(mDetailData.getChannelId())
                                    || CONTENT_TYPE_FLAG_TWO.equals(mDetailData.getChannelId())
                                    || CONTENT_TYPE_FLAG_THREE.equals(mDetailData.getChannelId())) {
                                //「vod_start_date」> 現在時刻の場合  ひかりTV内dTVチャンネルの番組(見逃し、関連VOD予定だが未配信)
                                if (DateUtils.getNowTimeFormatEpoch() < mDetailFullData.getmVod_start_date()) {
                                    //中継アプリに「chno」を通知する
                                    requestStartApplicationHikariTvCategoryDtvchannelBroadcast(mDetailFullData.getmChno());
                                    //「vod_start_date」 <= 現在時刻 < 「vod_end_date」の場合  //「tv_cid」を通知する
                                } else if (DateUtils.getNowTimeFormatEpoch() >= mDetailFullData.getmVod_start_date()
                                        && DateUtils.getNowTimeFormatEpoch() < mDetailFullData.getmVod_end_date()) {
                                    // ひかりTV内dTVチャンネル 見逃し
                                    if (H4D_CATEGORY_DTV_CHANNEL_MISSED.equals(mDetailData.getCategoryId())) {
                                        requestStartApplicationHikariTvCategoryDtvchannelMissed(mDetailFullData.getmTv_cid());
                                        // ひかりTV内dTVチャンネル 関連VOD
                                    } else if (H4D_CATEGORY_DTV_CHANNEL_RELATION.equals(mDetailData.getCategoryId())) {
                                        requestStartApplicationHikariTvCategoryDtvchannelRelation(mDetailFullData.getmTv_cid());
                                    }
                                }
                            }
                        }
                    }

                    break;
                default:
                    break;
            }
        }
        super.onStartRemoteControl(isFromHeader);
        DTVTLogger.end();
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
                playButton();
                break;
            case MediaPlayerDefinitions.PE_COMPLETED:
                pauseButton();
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
        if(mDetailData != null){
            if (DTV_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
                mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_bottom_corner_dtv, null));
            } else if (D_ANIMATION_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
                mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_bottom_corner_d_anime, null));
            } else if (DTV_CHANNEL_CONTENTS_SERVICE_ID == mDetailData.getServiceId()
                    || DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
                mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_bottom_corner_dtvchannel_and_hikari, null));
            }
        }
        super.onEndRemoteControl();
    }

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
     * ユーザ年齢をセット.
     */
    private void setUserAgeInfo() {
        UserInfoInsertDataManager dataManager = new UserInfoInsertDataManager(this);
        dataManager.readUserInfoInsertList();
        mAge = UserInfoUtils.getUserAgeInfoWrapper(dataManager.getmUserData());
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

    @Override
    protected void onPause() {
        super.onPause();
        //外部出力制御
        if (mExternalDisplayHelper != null) {
            mExternalDisplayHelper.onPause();
        }
        finishPlayer();
        if (!mIsPlayer) {
            //通信を止める
            if (mDetailDataProvider != null) {
                StopContentDetailDataConnect stopContentDetailDataConnect = new StopContentDetailDataConnect();
                stopContentDetailDataConnect.execute(mDetailDataProvider);
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
        }
    }

    /**
     * サムネイル取得処理を止める.
     */
    private void stopThumbnailConnect() {
        DTVTLogger.start();
        isDownloadStop = true;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.stopConnect();
        }
    }

    /**
     * 止めたサムネイル取得処理を再度取得可能な状態にする.
     */
    private void enableThumbnailConnect() {
        DTVTLogger.start();
        isDownloadStop = false;
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
        if (mIsEnableWatch == DISABLE_WATCH_LEAD_CONTRACT) {
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
        long canRecordingReservationTime =
                mRecordingReservationContentsDetailInfo.getStartTime() - (DateUtils.EPOCH_TIME_ONE_HOUR * 2);
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
        CustomDialog recordingReservationConfirmDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        recordingReservationConfirmDialog.setContent(getResources().getString(R.string.recording_reservation_confirm_dialog_msg));
        recordingReservationConfirmDialog.setConfirmText(R.string.recording_reservation_confirm_dialog_confirm);
        recordingReservationConfirmDialog.setCancelText(R.string.recording_reservation_confirm_dialog_cancel);
        // Cancelable
        recordingReservationConfirmDialog.setCancelable(false);
        recordingReservationConfirmDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                DTVTLogger.debug("Request RecordingReservation");
                DTVTLogger.debug(mRecordingReservationContentsDetailInfo.toString());
                mDetailDataProvider.requestRecordingReservation(mRecordingReservationContentsDetailInfo);
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
    private void checkWatchContents() {
//        final String CONTRACT_STATUS_DTV = "001";
//        final String CONTRACT_STATUS_H4D = "002";

        //視聴可否判定がまだ行われていない状態に設定
        mIsEnableWatch = ENABLE_WATCH_NO_DEFINE;

        //DBに保存されているUserInfoから契約情報を確認する
        UserInfoInsertDataManager dataManager = new UserInfoInsertDataManager(this);
        dataManager.readUserInfoInsertList();
        String contractInfo = UserInfoUtils.getUserContractInfo(dataManager.getmUserData());
        DTVTLogger.debug("contractInfo: " + contractInfo);

        if (contractInfo == null || contractInfo.isEmpty() || UserInfoUtils.CONTRACT_INFO_NONE.equals(contractInfo)) {
            //契約情報が未設定、または"none"の場合は視聴不可(契約導線を表示)
            mIsEnableWatch = DISABLE_WATCH_LEAD_CONTRACT;
            DTVTLogger.debug("Unviewable(Not contract)");
        } else if (UserInfoUtils.CONTRACT_INFO_DTV.equals(contractInfo)) {
            contractInfoOne();
        } else if (UserInfoUtils.CONTRACT_INFO_H4D.equals(contractInfo)) {
            contractInfoTwo();
        }
    }

    /**
     * 視聴可否判定、契約情報が"001"の場合.
     */
    private void contractInfoOne() {
        final String TV_PROGRAM = "tv_program";
        final String NOT_TV_SERVICE_FLAG = "0";
        final String IS_TV_SERVICE_FLAG = "1";

        DTVTLogger.debug("disp_type: " + mDetailFullData.getDisp_type());
        //"disp_type"の値を確認する
        if (TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
            DTVTLogger.debug("tv_service: " + mDetailFullData.getmTv_service());
            //"tv_service"の値を確認する
            if (IS_TV_SERVICE_FLAG.equals(mDetailFullData.getmTv_service())) {
                if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                    if (mIsLimitThirtyDay) {
                        //視聴可能(期限まで30日以内)
                        mEndDate = mDetailFullData.getAvail_end_date();
                        mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                    } else {
                        //視聴可能
                        mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                    }
                    DTVTLogger.debug("Viewable. Within 30 days:" + mIsLimitThirtyDay);
                    return;
                } else if (mDetailFullData.getAvail_start_date() >= DateUtils.getNowTimeFormatEpoch()) {
                    //視聴不可(視聴導線を非表示)
                    mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                    DTVTLogger.debug("Unviewable(Hide playing method because outside broadcasting time)");
                    return;
                }  else if (mDetailFullData.getAvail_end_date() <= DateUtils.getNowTimeFormatEpoch()) {
                    if (checkVodDate(mDetailFullData.getmVod_start_date(), mDetailFullData.getmVod_end_date())) {
                        if (mIsVodLimitThirtyDay) {
                            //視聴可能(期限まで30日以内)
                            mEndDate = mDetailFullData.getmVod_end_date();
                            mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                        } else {
                            //視聴可能
                            mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                        }
                        DTVTLogger.debug("Viewable. Within 30 days:" + mIsVodLimitThirtyDay);
                        return;
                    } else {
                        //視聴不可(視聴導線を非表示)
                        mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                        DTVTLogger.debug("Unviewable(Outside the VOD deadline");
                        return;
                    }
                }
            } else if (NOT_TV_SERVICE_FLAG.equals(mDetailFullData.getmTv_service())) {
                //視聴不可(視聴導線を非表示)
                mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                DTVTLogger.debug("Unviewable(tv_service = 0)");
                return;
            }
        }
        //コンテンツの詳細情報が不正
        mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
        DTVTLogger.debug("Unviewable(Unknown disp_type)");
    }

    /**
     * 視聴可否判定、契約情報が"002"の場合.
     */
    private void  contractInfoTwo() {
        final String TV_PROGRAM = "tv_program";
        final String VIDEO_PROGRAM = "video_program";
        final String SUBSCRIPTION_PACKAGE = "subscription_package";
        final String CH_TYPE_KIHON = "kihon_ch";
        final String CH_TYPE_BASIC = "basic_ch";
        final String CH_TYPE_TRIAL = "trial_free";
        final String CH_TYPE_PREMIUM = "premium_ch";
        final String IS_TV_SERVICE_FLAG = "1";
        final String NOT_TV_SERVICE_FLAG = "0";
        final String IS_DTV_FLAG = "1";
        final String NOT_DTV_FLAG = "0";
        final String IS_BV_FLAG = "1";

        DTVTLogger.debug("disp_type: " + mDetailFullData.getDisp_type());
        //"disp_type"の値を確認する
        switch (mDetailFullData.getDisp_type()) {
            case TV_PROGRAM:
                DTVTLogger.debug("tv_service: " + mDetailFullData.getmTv_service());
                //"tv_service"の値を確認する
                if (NOT_TV_SERVICE_FLAG.equals(mDetailFullData.getmTv_service())) {
                    //"tv_service"が0の場合（ひかりTV多ch）
                    if (mChannel != null) {
                        DTVTLogger.debug("service_id: " + mDetailFullData.getmService_id());
                        DTVTLogger.debug("CH_service_id: " + mChannel.getServiceId());
                        DTVTLogger.debug("CH_ch_type: " + mChannel.getChType());
                        if (mChannel.getServiceId().equals(mDetailFullData.getmService_id())) {
                            if (CH_TYPE_KIHON.equals(mChannel.getChType())
                                    || CH_TYPE_BASIC.equals(mChannel.getChType())
                                    || CH_TYPE_TRIAL.equals(mChannel.getChType())) {
                                if (mDetailFullData.getAvail_start_date() <= DateUtils.getNowTimeFormatEpoch()
                                        && DateUtils.getNowTimeFormatEpoch() < mDetailFullData.getAvail_end_date()) {
                                    mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                                    DTVTLogger.debug("Viewable");
                                    return;
                                } else if (mDetailFullData.getAvail_start_date() > DateUtils.getNowTimeFormatEpoch()
                                        || DateUtils.getNowTimeFormatEpoch() >= mDetailFullData.getAvail_end_date()) {
                                    //視聴期間外のため視聴不可
                                    mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                                    DTVTLogger.debug("Unviewable(Hide playing method because outside broadcasting time)");
                                    return;
                                }
                            } else if (CH_TYPE_PREMIUM.equals(mChannel.getChType())) {
                                //購入済みチャンネル一覧を取得
                                mDetailDataProvider.getChListData();
                                //onRentalChListCallbackで続きの判定を行う
                                return;
                            }
                        }
                    }
                    //取得したチャンネル情報が不正の場合
                    mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                    DTVTLogger.debug("Unviewable(Incorrect channel info)");
                } else if (IS_TV_SERVICE_FLAG.equals(mDetailFullData.getmTv_service())) {
                    //"tv_service"が1の場合（dチャンネル）
                    if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                        if (mIsLimitThirtyDay) {
                            mEndDate = mDetailFullData.getAvail_end_date();
                            mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                        } else {
                            //視聴可能
                            mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                        }
                        DTVTLogger.debug("Viewable. Within 30 days:" + mIsVodLimitThirtyDay);
                        return;
                    } else if (mDetailFullData.getAvail_start_date() >= DateUtils.getNowTimeFormatEpoch()) {
                        //視聴不可(視聴導線を非表示)
                        mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                        DTVTLogger.debug("Unviewable(Hide playing method because outside broadcasting time)");
                        return;
                    } else if (mDetailFullData.getAvail_end_date() <= DateUtils.getNowTimeFormatEpoch()) {
                        if (checkVodDate(mDetailFullData.getmVod_start_date(), mDetailFullData.getmVod_end_date())) {
                            if (mIsVodLimitThirtyDay) {
                                //視聴可能(期限まで30日以内)
                                mEndDate = mDetailFullData.getmVod_end_date();
                                mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                            } else {
                                //視聴可能
                                mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                            }
                            DTVTLogger.debug("Viewable. Within 30 days:" + mIsVodLimitThirtyDay);
                            return;
                        } else {
                            //視聴可能期限内ではないので視聴不可
                            mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                            DTVTLogger.debug("Unviewable(Outside VOD broadcasting time)");
                            return;
                        }
                    }
                }
                //コンテンツの詳細情報が不正
                mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                DTVTLogger.debug("Unviewable(Incorrect contents detail info)");
                break;
            case VIDEO_PROGRAM:
                //"dtv"の値を確認する
                DTVTLogger.debug("dtv: " + mDetailFullData.getDtv());
                if (IS_DTV_FLAG.equals(mDetailFullData.getDtv())) {
                    //視聴可能期限の確認
                    if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                        if (mIsLimitThirtyDay) {
                            //視聴可能(期限まで30日以内)
                            mEndDate = mDetailFullData.getAvail_end_date();
                            mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                        } else {
                            //視聴可能
                            mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                        }
                        DTVTLogger.debug("Viewable. Within 30 days:" + mIsLimitThirtyDay);
                        return;
                    } else {
                        //視聴可能期限内ではないので視聴不可
                        mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                        DTVTLogger.debug("Unviewable(Outside broadcasting time)");
                        return;
                    }
                } else if (NOT_DTV_FLAG.equals(mDetailFullData.getDtv())) {
                    //"dtv"の値が0の場合、"bvflg"の値を確認する
                    DTVTLogger.debug("bvflg: " + mDetailFullData.getBvflg());
                    if (IS_BV_FLAG.equals(mDetailFullData.getBvflg())) {
                        //視聴可能期限の確認
                        if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                            if (mIsLimitThirtyDay) {
                                //視聴可能(期限まで30日以内)
                                mEndDate = mDetailFullData.getAvail_end_date();
                                mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                            } else {
                                //視聴可能
                                mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                            }
                            DTVTLogger.debug("Viewable. Within 30 days:" + mIsLimitThirtyDay);
                            return;
                        } else {
                            //視聴可能期限内ではないので視聴不可
                            mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                            DTVTLogger.debug("Unviewable(Outside broadcasting time)");
                            return;
                        }
                    } else {
                        //bvflgが1ではないので視聴不可
                        mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                        DTVTLogger.debug("Unviewable(bvflg != 1)");
                        return;
                    }
                }
                //コンテンツの詳細情報が不正
                mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                DTVTLogger.debug("Unviewable(Incorrect dtv value)");
                break;
            case SUBSCRIPTION_PACKAGE:
                //レンタルコンテンツ(購入済みVOD)一覧を取得
                mDetailDataProvider.getVodListData();
                //onRentalVodListCallbackで続きの判定を行う
                break;
            default:
                mIsEnableWatch = DISABLE_WATCH_NO_PLAY;
                break;
        }
    }

    /**
     * vodコンテンツが視聴可能な期間内であるかを返す.
     *
     * @param startDate 視聴可能開始日時
     * @param endDate 視聴可能期限
     * @return true:視聴可能 false:視聴不可
     */
    private Boolean checkVodDate(final Long startDate, final Long endDate) {
        Long now = DateUtils.getNowTimeFormatEpoch();

        if (null == startDate || startDate == 0) {
            //開始日時未設定時は視聴不可
            return false;
        } else if (endDate <= now) {
            //視聴可能期限を超えているため視聴不可
            return false;
        } else if (startDate <= now && now < endDate) {
            //視聴可能期限まで一ヶ月以内かどうか
            if (endDate - now < DateUtils.EPOCH_TIME_ONE_DAY * ONE_MONTH) {
                mIsVodLimitThirtyDay = true;
            }
            return true;
        }
        return false;
    }

    /**
     * コンテンツが視聴可能な期間であるかを返す.
     *
     * @param startDate 視聴可能開始日時
     * @param endDate 視聴可能期限
     * @return true:視聴可能 false:視聴不可
     */
    private Boolean checkWatchDate(final Long startDate, final Long endDate) {
        final int ONE_MONTH = 30;

        Long now = DateUtils.getNowTimeFormatEpoch();
        //視聴可能期限内かどうか
        if (startDate <= now && now < endDate) {
            //視聴可能期限まで一ヶ月以内かどうか
            if (endDate - now < DateUtils.EPOCH_TIME_ONE_DAY * ONE_MONTH) {
                mIsLimitThirtyDay = true;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRentalVodListCallback(final PurchasedVodListResponse response) {
        DTVTLogger.start();
        this.response = response;
        //購入済みVOD取得からの戻り(視聴可否判定)
        ArrayList<ActiveData> activeDatas = response.getVodActiveData();
        String[] liinfArray = mDetailFullData.getmLiinf_array();
        String puid = mDetailFullData.getPuid();
        for (String liinf : liinfArray) {
            //liinfを"|"区切りで分解する
            String[] column = liinf.split(Pattern.quote("|"), 0);
            for (ActiveData activeData : activeDatas) {
                String license_id = activeData.getLicenseId();
                if (license_id.equals(column[0]) || license_id.equals(puid)) {
                    //購入済みVODのlicense_idと、対象コンテンツのpuidかliinf_arrayのライセンスIDが一致
                    if (activeData.getValidEndDate() > DateUtils.getNowTimeFormatEpoch()) {
                        //視聴可能期限内なので、activeData全体から最長の期限を探す
                        long activeDataDate = activeData.getValidEndDate();
                        for (ActiveData activeDataEndDate : activeDatas) {
                            if (activeDataDate < activeDataEndDate.getValidEndDate()) {
                                activeDataDate = activeDataEndDate.getValidEndDate();
                            }
                        }
                        //視聴可能期限まで一ヶ月以内かどうか
                        if (activeDataDate - DateUtils.getNowTimeFormatEpoch()
                                < DateUtils.EPOCH_TIME_ONE_DAY * ONE_MONTH) {
                            mIsLimitThirtyDay = true;
                            //視聴可能(期限まで30日以内)
                            mEndDate = activeDataDate;
                            mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                        } else {
                            //視聴可能
                            mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                        }
                        DTVTLogger.debug("Viewable. Within 30 days:" + mIsLimitThirtyDay);
                        changeUIBasedContractInfo();
                        return;
                    } else {
                        //視聴期限範囲外のため視聴不可だが他のactive_listをチェックするためここでは何もしない
                        DTVTLogger.debug("Outside broadcasting time(Now confirming)");
                    }
                }
            }
        }
        //視聴不可(契約導線を表示) STB側で契約が必要なパターンの可能性あり
        mIsEnableWatch = DISABLE_WATCH_LEAD_CONTRACT_VOD;
        DTVTLogger.debug("Unviewable(VOD purchased info mismatch)");
        changeUIBasedContractInfo();
    }

    @Override
    public void onRentalChListCallback(final PurchasedChListResponse response) {
        //購入済みCH一覧取得からの戻り
        DTVTLogger.start();
        ChannelList channelList = response.getChannelListData();
        List<HashMap<String, String>> chList = channelList.getChannelList();

        if (checkChServiceIdListSame(chList)) {
            //購入CHと対象CHのservice_idが一致
            ArrayList<ActiveData> activeDatas = response.getChActiveData();
            //購入済みCHのactive_list内のlicense_idと、対象CHのp_uid, sub_puid, CHPACK-puid, CHPACK-sub_puidを比較
            for (ActiveData activeData : activeDatas) {
                if (activeData.getLicenseId().equals(mChannel.getPuId())
                        || activeData.getLicenseId().equals(mChannel.getSubPuId())
                        || activeData.getLicenseId().equals(mChannel.getChPackPuId())
                        || activeData.getLicenseId().equals(mChannel.getChPackSubPuId())) {
                    if (activeData.getValidEndDate() > DateUtils.getNowTimeFormatEpoch()) {
                        //視聴可能期限内なので、activeData全体から最長の期限を探す
                        long activeDataDate = activeData.getValidEndDate();
                        for (ActiveData activeDataEndDate : activeDatas) {
                            if (activeDataDate < activeDataEndDate.getValidEndDate()) {
                                activeDataDate = activeDataEndDate.getValidEndDate();
                            }
                        }
                        //視聴可能期限まで一ヶ月以内かどうか
                        if (activeDataDate - DateUtils.getNowTimeFormatEpoch()
                                < DateUtils.EPOCH_TIME_ONE_DAY * ONE_MONTH) {
                            mIsLimitThirtyDay = true;
                            //視聴可能(期限まで30日以内)
                            mEndDate = activeDataDate;
                            mIsEnableWatch = ENABLE_WATCH_WITH_LIMIT;
                        } else {
                            //視聴可能
                            mIsEnableWatch = ENABLE_WATCH_NO_LIMIT;
                        }
                        DTVTLogger.debug("Viewable. Within 30 days:" + mIsLimitThirtyDay);
                        changeUIBasedContractInfo();
                        return;
                    }
                } else {
                    //license_id不一致のため視聴不可だが他のlicense_idをチェックするためここでは何もしない
                    DTVTLogger.debug("Outside broadcasting time(Now confirming)");
                }
            }
        }
        //視聴不可(契約導線を表示) STB側で契約が必要なパターンの可能性あり
        mIsEnableWatch = DISABLE_WATCH_LEAD_CONTRACT_CH;
        DTVTLogger.debug("Unviewable(CH purchased info mismatch)");
        changeUIBasedContractInfo();
    }

    /**
     * 購入済みCH一覧のservice_idと対象のCHのservice_idが一致するか確認.
     *
     * @param chList 購入済みCHリスト
     * @return true:一致 false:不一致
     */
    private boolean checkChServiceIdListSame(final List<HashMap<String, String>> chList) {
        List<String> chServiceIds = new ArrayList<>();

        //CHのservice_id一覧を取得
        for (HashMap<String, String> hashMap : chList) {
            String serviceId = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID);
            if (serviceId != null && !serviceId.isEmpty()) {
                chServiceIds.add(serviceId);
            }
        }

        //service_idが一致するか
        for (String serviceId : chServiceIds) {
            if (serviceId.equals(mChannel.getServiceId())) {
                //service_idが一致
                return true;
            }
        }
        return false;
    }

    /**
     * 視聴可否判定に基づいてUIの操作などを行う.
     */
    private void changeUIBasedContractInfo() {
        DtvContentsDetailFragment detailFragment = getDetailFragment();
        switch (mIsEnableWatch) {
            case ENABLE_WATCH_NO_LIMIT:
                //視聴可能なので何もしない
                break;
            case ENABLE_WATCH_WITH_LIMIT:
                //「〇〇日まで」を表示
                if (mEndDate != 0L) {
                    detailFragment.displayEndDate(mEndDate);
                }
                break;
            case DISABLE_WATCH_LEAD_CONTRACT:
                //再生、クリップ、録画、評価、ダウンロード、番組表編集 の操作時に契約導線を表示

                //TODO 再生、評価はコンテンツ毎の詳細画面の表示が行われてから対応する。

                //クリップ押下時に契約導線を表示するため、Fragmentに未契約状態であることを通知する
                detailFragment.setContractInfo(false);
                break;
            case DISABLE_WATCH_LEAD_CONTRACT_VOD:
                //契約導線を表示 (VOD)
                LinearLayout vodLayout = findViewById(R.id.contract_leading_view);
                TextView vodTextView = findViewById(R.id.contract_leading_text);
                Button vodButton = findViewById(R.id.contract_leading_button);
                // 宅内の場合契約導線表示
                if (getStbStatus()) {
                    vodLayout.setVisibility(View.VISIBLE);
                    vodTextView.setText(getString(R.string.contents_detail_contract_text_vod));
                    vodButton.setText(getString(R.string.contents_detail_contract_button_vod));
                    vodButton.setAllCaps(false);
                } else {
                    //宅外の場合は契約ボタンを表示しない
                    vodButton.setVisibility(View.GONE);
                }
                //サムネイルにシャドウをかける
                setThumbnailShadow();

                //サムネイル上のdTVで視聴、dアニメストアで視聴を非表示
                if (mThumbnailBtn != null) {
                    mThumbnailBtn.setVisibility(View.GONE);
                }
                break;
            case DISABLE_WATCH_LEAD_CONTRACT_CH:
                //契約導線を表示 (CH)
                LinearLayout chLayout = findViewById(R.id.contract_leading_view);
                TextView chTextView = findViewById(R.id.contract_leading_text);
                Button chButton = findViewById(R.id.contract_leading_button);
                // 宅内の場合契約導線表示
                if (getStbStatus()) {
                    chLayout.setVisibility(View.VISIBLE);
                    chTextView.setText(getString(R.string.contents_detail_contract_text_ch));
                    chButton.setText(getString(R.string.contents_detail_contract_button_ch));
                    chButton.setAllCaps(false);
                } else {
                    //宅外の場合は契約ボタンを表示しない
                    chButton.setVisibility(View.GONE);
                }
                //サムネイルにシャドウをかける
                setThumbnailShadow();

                //サムネイル上のdTVで視聴、dアニメストアで視聴を非表示
                if (mThumbnailBtn != null) {
                    mThumbnailBtn.setVisibility(View.GONE);
                }
                break;
            case DISABLE_WATCH_NO_PLAY:
                //再生導線を非表示にする

                //TODO 再生、評価はコンテンツ毎の詳細画面の表示が行われてから対応する

                //クリップを非表示
                detailFragment.changeClipButtonVisibility(false);

                //録画予約ボタンを非表示
                detailFragment.changeVisibilityRecordingReservationIcon(View.INVISIBLE);

                //サムネイル上のdTVで視聴、dアニメストアで視聴を非表示
                if (mThumbnailBtn != null) {
                    mThumbnailBtn.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
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
     */
    private void setThumbnailShadow() {
        if (mThumbnail != null) {
            mThumbnailShadow.setVisibility(View.VISIBLE);
        }
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgessBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgessBar) {
        if (showProgessBar) {
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
}
