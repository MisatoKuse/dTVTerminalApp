/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.content.Context;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digion.dixim.android.activation.ActivationClientDefinition;
import com.digion.dixim.android.secureplayer.MediaPlayerController;
import com.digion.dixim.android.secureplayer.MediaPlayerDefinitions;
import com.digion.dixim.android.secureplayer.SecureVideoView;
import com.digion.dixim.android.secureplayer.SecuredMediaPlayerController;
import com.digion.dixim.android.secureplayer.helper.CaptionDrawCommands;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.common.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.UserInfoInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.DtvContentsDetailDataProvider;
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
import com.nttdocomo.android.tvterminalapp.model.detail.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.model.player.MediaVideoInfo;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.view.ContentsDetailViewPager;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SendOperateLog;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DtvContentsDetailActivity extends BaseActivity implements DtvContentsDetailDataProvider.ApiDataProviderCallback,
        View.OnClickListener, MediaPlayerController.OnStateChangeListener, MediaPlayerController.OnFormatChangeListener,
        MediaPlayerController.OnPlayerEventListener, MediaPlayerController.OnErrorListener, MediaPlayerController.OnCaptionDataListener,
        RemoteControllerView.OnStartRemoteControllerUIListener, DtvContentsDetailFragment.RecordingReservationIconListener {

    private static final int SCREEN_RATIO_WIDTH = 16;
    private static final int SCREEN_RATIO_HEIGHT = 9;
    //先頭のメタデータを取得用
    private static final int FIRST_VOD_META_DATA = 0;

    /* コンテンツ詳細 start */
    private LinearLayout mTabLinearLayout = null;
    private ContentsDetailViewPager mViewPager = null;
    private OtherContentsDetailData mDetailData = null;
    private VodMetaFullData mDetailFullData = null;
    private DtvContentsDetailDataProvider mDetailDataProvider = null;
    private DtvContentsDetailFragmentFactory mFragmentFactory = null;

    private String[] mTabNames = null;
    private String[] mDate = {"日", "月", "火", "水", "木", "金", "土"};
    private boolean mIsPlayer = false;
    private boolean mIsControllerVisible = false;
    private Intent mIntent = null;
    private Toast mToast = null;

    private LinearLayout mThumbnailBtn = null;
    private RelativeLayout mThumbnailRelativeLayout = null;
    private ImageView mThumbnail = null;

    public static final String RECOMMEND_INFO_BUNDLE_KEY = "recommendInfoKey";
    public static final String PLALA_INFO_BUNDLE_KEY = "plalaInfoKey";
    private static final String DATE_FORMAT = "yyyy/MM/ddHH:mm:ss";
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    private static final int CONTENTS_DETAIL_TAB_TEXT_SIZE = 15;
    private static final int CONTENTS_DETAIL_TAB_OTHER_MARGIN = 0;

    /* コンテンツ詳細 end */
    /*DTV起動*/
    private static final int DTV_VERSION_STANDARD = 52000;
    private static final String RESERVED4_TYPE4 = "4";
    private static final String RESERVED4_TYPE7 = "7";
    private static final String RESERVED4_TYPE8 = "8";
    private static final String GOOGLEPLAY_DOWNLOAD_URL = "http://play.google.com/store/apps/details?id=jp.co.nttdocomo.dtv";
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
    /* player start */
    private static final long HIDE_IN_3_SECOND = 3 * 1000;
    private static final int REFRESH_VIDEO_VIEW = 0;
    private final static int ACTIVATION_REQUEST_CODE = 1;
    private final static int ACTIVATION_REQUEST_MAX_CNT = 3;
    private static final int REWIND_SECOND = 10 * 1000;
    private static final int FAST_SECOND = 30 * 1000;

    private SeekBar mVideoSeekBar = null;
    private SecureVideoView mSecureVideoPlayer = null;
    private SecuredMediaPlayerController mPlayerController = null;
    private MediaVideoInfo mCurrentMediaInfo = null;

    private RelativeLayout mPlayerViewLayout;
    private RelativeLayout mRecordCtrlView = null;
    private RelativeLayout mVideoCtrlBar = null;
    private RelativeLayout mVideoCtrlRootView;
    private FrameLayout mVideoPlayPause = null;
    private TextView mVideoCurTime = null;
    private TextView mVideoTotalTime = null;
    private TextView mTvTitle = null;
    private TextView mVideoRewind10 = null;
    private TextView mVideoFast30 = null;
    private ImageView mTvLogo = null;
    private ImageView mVideoFullScreen = null;
    private ImageView mVideoRewind = null;
    private ImageView mVideoFast = null;

    private Handler mCtrlHandler = new Handler(Looper.getMainLooper());
    private GestureDetector mGestureDetector = null;

    private int mActivationTimes = 0;
    private int mScreenWidth = 0;
    private boolean mCanPlay = false;
    private boolean mIsHideOperate = true;
    private boolean mIsOncreateOk = false;
    private RecordingReservationContentsDetailInfo mRecordingReservationContentsDetailInfo = null;
    private CustomDialog mRecordingReservationCustomtDialog = null;
    private final int RECORDING_RESERVATION_DIALOG_INDEX_0 = 0; // 予約録画する
    private final int RECORDING_RESERVATION_DIALOG_INDEX_1 = 1; // キャンセル
    // 他サービスフラグ
    boolean mIsOtherService = false;

    //視聴判定
    /**
     * 対象コンテンツが視聴期限以内かどうか.
     */
    private boolean mIsLimitThirtyDay = false;
    /**
     * 対象コンテンツが視聴期限以内かどうか(VOD).
     */
    private boolean mIsVodLimitThirtyDay = false;
    /**
     * 対象コンテンツのチャンネルデータ.
     */
    private Channel mChannel = null;
    /**
     * 一ヶ月(30日).
     */
    private static final int ONE_MONTH = 30;

    private Runnable mHideCtrlViewThread = new Runnable() {

        /**
         * run
         */
        @Override
        public void run() {
            DTVTLogger.start();
            hideCtrlView(View.INVISIBLE);
            DTVTLogger.end();
        }
    };
    /* player end */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtv_contents_detail_main_layout);
        DTVTLogger.start();
        setStatusBarColor(R.color.contents_header_background);
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
        }
        DTVTLogger.end();
    }

    /**
     * ビュー初期化.
     */
    private void initView() {
        mIntent = getIntent();
        mThumbnailRelativeLayout = findViewById(R.id.dtv_contents_detail_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getWidthDensity(),
                (getWidthDensity() * SCREEN_RATIO_HEIGHT / SCREEN_RATIO_WIDTH));
        mThumbnailRelativeLayout.setLayoutParams(layoutParams);
        Object object = mIntent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
        if (object instanceof RecordedContentsDetailData) {
            mIsPlayer = true;
            // リモコンUIのリスナーを設定
            createRemoteControllerView(true);
            mIsControllerVisible = true;
            setStartRemoteControllerUIListener(this);
            initPlayer();
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
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        initContentsView();
    }

    /**
     * UIを更新するハンドラー.
     */
    private Handler viewRefresher = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DTVTLogger.start();
            super.handleMessage(msg);
            if (null == mPlayerController) {
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
     * @param textView
     * @param millisecond
     */
    public void time2TextViewFormat(TextView textView, int millisecond) {
        DTVTLogger.start();
        int second = millisecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String str;
        if (hh != 0) {
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format("%02d:%02d", mm, ss);
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
        mPlayerController.setWakeMode(this, PowerManager.FULL_WAKE_LOCK);
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
        DTVTLogger.end();
        return false;
    }

    /**
     * activate.
     */
    private void activate() {
        DTVTLogger.start();
        if (mActivationTimes >= ACTIVATION_REQUEST_MAX_CNT) {
            DTVTLogger.end();
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(DtvContentsDetailActivity.this.getPackageName(),
                ActivationClientDefinition.activationActivity);
        intent.putExtra(ActivationClientDefinition.EXTRA_DEVICE_KEY,
                EnvironmentUtil.getPrivateDataHome(DtvContentsDetailActivity.this,
                        EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER));
        startActivityForResult(intent, ACTIVATION_REQUEST_CODE);
        ++mActivationTimes;
        DTVTLogger.end();
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
            public boolean onTouch(View view, MotionEvent motionEvent) {
                DTVTLogger.start();
                if (mVideoPlayPause.getVisibility() == View.VISIBLE) {
                    mGestureDetector.onTouchEvent(motionEvent);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (mVideoPlayPause.getVisibility() == View.VISIBLE) {
                        if (mIsHideOperate) {
                            hideCtrlView(View.INVISIBLE);
                        }
                    } else {
                        mVideoPlayPause.setVisibility(View.VISIBLE);
                        mVideoRewind10.setVisibility(View.VISIBLE);
                        mVideoRewind.setVisibility(View.VISIBLE);
                        mVideoFast30.setVisibility(View.VISIBLE);
                        mVideoFast.setVisibility(View.VISIBLE);
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
     *
     * @param invisible
     */
    private void hideCtrlView(int invisible) {
        DTVTLogger.start();
        mVideoPlayPause.setVisibility(invisible);
        mVideoRewind10.setVisibility(invisible);
        mVideoRewind.setVisibility(invisible);
        mVideoFast30.setVisibility(invisible);
        mVideoFast.setVisibility(invisible);
        mVideoCtrlBar.setVisibility(invisible);
        mTvTitle.setVisibility(invisible);
        mTvLogo.setVisibility(invisible);
        DTVTLogger.end();
    }

    /**
     * hide ctrl mView.
     */
    private void hideCtrlViewAfterOperate() {
        DTVTLogger.start();
        if (mCtrlHandler != null) {
            mCtrlHandler.removeCallbacks(mHideCtrlViewThread);
            mCtrlHandler.postDelayed(mHideCtrlViewThread, HIDE_IN_3_SECOND);
        }
        mIsHideOperate = true;
        DTVTLogger.end();
    }

    private void initPlayer() {
        mSecureVideoPlayer = findViewById(R.id.dtv_contents_detail_main_layout_player_view);
        mScreenWidth = getWidthDensity();
        initPlayerData();
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
            public boolean onSingleTapUp(MotionEvent e) {
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
                        showMessage("←10");
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
                        showMessage("30→");
                    }
                }
                DTVTLogger.end();
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
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
                        showMessage("←10");
                    } else if (e1.getX() < e2.getX() && e1.getX() > (float) (mScreenWidth / 2 + mVideoPlayPause.getWidth() / 2)) {
                        int pos = mPlayerController.getCurrentPosition();
                        pos += FAST_SECOND;
                        pos = pos > mPlayerController.getDuration() ? mPlayerController.getDuration() : pos;
                        mPlayerController.seekTo(pos);
                        mIsHideOperate = false;
                        showMessage("30→");
                    }
                }
                DTVTLogger.end();
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        mIsOncreateOk = true;
        DTVTLogger.end();
    }

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
        if (null == url || 0 == url.length()) {
            showMessage("プレイヤーは「null」コンテンツを再生できない");
            DTVTLogger.end();
            return false;
        }
        long size = Integer.parseInt(datas.getSize());

        String durationStr = datas.getDuration();
        long duration = getDuration(durationStr);

        String type = datas.getVideoType();

        int bitRate = Integer.parseInt(datas.getBitrate());
        if (0 == size) {
            size = duration * bitRate;
        }
        String title = datas.getTitle();
        setTitleText(title);
        Uri uri = Uri.parse(url);
        String contentFormat = "contentFormat";
        String type2 = "";
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
        mCurrentMediaInfo = new MediaVideoInfo(
                uri,           //uri
                type2,         //"application/x-dtcp1", "video/mp4", RESOURCE_MIMETYPE
                0,             //SIZE
                duration,      //DURATION
                0,             //BITRATE
                true,         //IS_SUPPORTED_BYTE_SEEK
                true,         //IS_SUPPORTED_TIME_SEEK
                true,         //IS_AVAILABLE_CONNECTION_STALLING
                true,         //IS_LIVE_MODE
                false,        //IS_REMOTE
                title,         //TITLE
                contentFormat
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
        boolean ok = true;
        String[] strs1 = durationStr.split(":");
        String[] strs2;
        if (3 == strs1.length) {
            try {
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
            } catch (Exception e) {
                ok = false;
            }
        }
        if (!ok) {
            ret = 0;
        }
        DTVTLogger.end();
        return ret;
    }

    /**
     * showMessage.
     *
     * @param msg
     */
    private void showMessage(String msg) {
        DTVTLogger.start();
        if (null == mToast) {
            mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
        DTVTLogger.end();
    }

    /**
     * errorExit.
     */
    private void errorExit() {
        DTVTLogger.start();
        // TODO エラー表示の要修正
        showMessage("ビデオ情報はただしくないです。もう一回試してください。");
        setCanPlay(false);
        DTVTLogger.end();
    }

    /**
     * set can play.
     *
     * @param state state
     */
    private void setCanPlay(boolean state) {
        DTVTLogger.start();
        synchronized (this) {
            mCanPlay = state;
        }
        DTVTLogger.end();
    }

    /**
     * コンテンツ詳細データ取得.
     */
    private void getContentsData() {
        mDetailDataProvider = new DtvContentsDetailDataProvider(this);
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

    /**
     * インテントデータ取得.
     */
    private void setIntentDetailData() {
        if (mDetailData == null) {
            mDetailData = mIntent.getParcelableExtra(PLALA_INFO_BUNDLE_KEY);
            if (mDetailData != null) {
                String displayType = mDetailData.getDisplayType();
                String contentsType = mDetailData.getContentsType();
                if ("tv_program".equals(displayType)) {
                    if (!"1".equals(contentsType) && !"2".equals(contentsType)
                            && !"3".equals(contentsType)) {
                        //TODO  放送中の場合
                    }
                }
            }
        } else {
            int serviceId = mDetailData.getServiceId();
            String categoryId = mDetailData.getCategoryId();
            if (serviceId == OtherContentsDetailData.DTV_HIKARI_CONTENTS_SERVICE_ID) {
                if (OtherContentsDetailData.HIKARI_CONTENTS_CATEGORY_ID_DTB.equals(categoryId)
                        || OtherContentsDetailData.HIKARI_CONTENTS_CATEGORY_ID_BS.equals(categoryId)
                        || OtherContentsDetailData.HIKARI_CONTENTS_CATEGORY_ID_IPTV.equals(categoryId)) {
                    //TODO  放送中の場合
                }
            } else if (serviceId == OtherContentsDetailData.DTV_CONTENTS_SERVICE_ID) {
                mThumbnailBtn.setVisibility(View.VISIBLE);
                TextView startAppIcon = findViewById(R.id.view_contents_button_text);
                startAppIcon.setVisibility(View.VISIBLE);
                startAppIcon.setText(getResources().getString(R.string.dtv_content_service_start_text));
            } else if (serviceId == OtherContentsDetailData.D_ANIMATION_CONTENTS_SERVICE_ID) {
                mThumbnailBtn.setVisibility(View.VISIBLE);
                TextView startAppIcon = findViewById(R.id.view_contents_button_text);
                startAppIcon.setVisibility(View.VISIBLE);
                startAppIcon.setText(getResources().getString(R.string.d_anime_store_content_service_start_text));
            }
            setTitleAndThumbnail(mDetailData.getTitle(), mDetailData.getThumb());
        }
        getContentsData();
    }

    /**
     * データの初期化.
     */
    private void setTitleAndThumbnail(String title, String url) {
        if (!TextUtils.isEmpty(title)) {
            setTitleText(title);
        }
        if (!TextUtils.isEmpty(url)) {
            ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(this);
            mThumbnail.setImageResource(R.drawable.test_image);
            mThumbnail.setTag(url);
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mThumbnail, url);
            if (bitmap != null) {
                mThumbnail.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * データの初期化.
     */
    private void initContentData() {

        // タブ数を先に決定するため、コンテンツ詳細のデータを最初に取得しておく
        mDetailData = mIntent.getParcelableExtra(RECOMMEND_INFO_BUNDLE_KEY);
        if (mDetailData != null) {
            int serviceId = mDetailData.getServiceId();
            if (serviceId == OtherContentsDetailData.DTV_CONTENTS_SERVICE_ID
                    || serviceId == OtherContentsDetailData.D_ANIMATION_CONTENTS_SERVICE_ID
                    || serviceId == OtherContentsDetailData.DTV_CHANNEL_CONTENTS_SERVICE_ID) {
                // 他サービス(dtv/dtvチャンネル/dアニメ)フラグを立てる
                mIsOtherService = true;
                if (serviceId == OtherContentsDetailData.DTV_CONTENTS_SERVICE_ID
                        || serviceId == OtherContentsDetailData.D_ANIMATION_CONTENTS_SERVICE_ID) {
                    // リモコンUIのリスナーを設定
                    createRemoteControllerView(true);
                    mIsControllerVisible = true;
                    setStartRemoteControllerUIListener(this);
                }
            }
        }

        if (mIsOtherService) {
            // コンテンツ詳細(他サービスの時は、タブ一つに設定する)
            mTabNames = getResources().getStringArray(R.array.other_service_contents_detail_tabs);
        } else {
            mTabNames = getResources().getStringArray(R.array.other_contents_detail_tabs);
        }

        mFragmentFactory = new DtvContentsDetailFragmentFactory();
        ContentsDetailPagerAdapter contentsDetailPagerAdapter
                = new ContentsDetailPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentsDetailPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                setTab(position);
            }
        });
        setIntentDetailData();
    }

    /**
     * initView function.
     */
    private void initPlayerData() {
        DTVTLogger.start();
        mActivationTimes = 0;
        DTVTLogger.end();
    }

    /**
     * initPlayerView mView.
     */
    private void initPlayerView() {
        DTVTLogger.start();
        RelativeLayout videoCtrlRootView = null;
        RelativeLayout playerViewLayout = null;
        playerViewLayout = findViewById(R.id.dtv_contents_detail_main_layout_player_rl);
        playerViewLayout.removeView(mRecordCtrlView);
        mRecordCtrlView = (RelativeLayout) LayoutInflater.from(this)
                .inflate(R.layout.tv_player_ctrl_video_record, null, false);
        mVideoPlayPause = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_player_pause_fl);
        mVideoCtrlRootView = mRecordCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_video_root);
        mVideoRewind10 = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_10_tv);
        mVideoRewind = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_rewind_iv);
        mVideoFast30 = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_30_tv);
        mVideoFast = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_fast_iv);
        mVideoCtrlBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_control_bar_iv);
        mVideoCurTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_cur_time_tv);
        mVideoFullScreen = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv);
        mVideoTotalTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_total_time_tv);
        mVideoSeekBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
        TextView nowTextView = mRecordCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_now_on_air_tv);
        mTvTitle = playerViewLayout.findViewById(R.id.tv_player_main_layout_video_ctrl_player_title);
        mTvLogo = playerViewLayout.findViewById(R.id.tv_player_main_layout_video_ctrl_player_logo);
        setPlayerLogoThumbnail("https://www.hikaritv.net/resources/hikari/pc/images/ch_logo/ch220/101.png");
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
        hideCtrlView(View.INVISIBLE);
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
     */
    private void setPlayerLogoThumbnail(String url) {
        if (!TextUtils.isEmpty(url)) {
            ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(this);
            mTvLogo.setImageResource(R.drawable.test_image);
            mTvLogo.setTag(url);
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mTvLogo, url);
            if (bitmap != null) {
                mTvLogo.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * set screen size.
     */
    private void setScreenSize(boolean mIsLandscape, LinearLayout.LayoutParams playerParams) {
        DTVTLogger.start();
        if (mIsLandscape) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            setTitleVisibility(false);
            playerParams.height = getScreenHeight();
            playerParams.width = getScreenWidth();
            mScreenWidth = getScreenWidth();
            setPlayerProgressView(true);
            setRemoteControllerViewVisibility(View.GONE);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            mScreenWidth = getWidthDensity();
            playerParams.height = (getWidthDensity() * SCREEN_RATIO_HEIGHT / SCREEN_RATIO_WIDTH);
            setTitleVisibility(true);
            setPlayerProgressView(false);
            setRemoteControllerViewVisibility(View.VISIBLE);
        }
        mThumbnailRelativeLayout.setLayoutParams(playerParams);
        DTVTLogger.end();
    }

    /**
     * プレイヤーの場合スクロールできない.
     */
    private void setPlayerProgressView(boolean isLandscape) {
        RelativeLayout progressLayout = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_progress_ll);
        if (isLandscape) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) getDensity() * 10, 0, (int) getDensity() * 10, 0);
            progressLayout.setLayoutParams(layoutParams);
            layoutParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mVideoCurTime.setLayoutParams(layoutParams);
            layoutParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mVideoFullScreen.setLayoutParams(layoutParams);
            layoutParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.START_OF, R.id.tv_player_ctrl_now_on_air_full_screen_iv);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.setMargins(0, 0, (int) getDensity() * 10, 0);
            mVideoTotalTime.setLayoutParams(layoutParams);
            mVideoCtrlBar.removeView(mVideoCurTime);
            mVideoCtrlBar.removeView(mVideoFullScreen);
            mVideoCtrlBar.removeView(mVideoTotalTime);
            layoutParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.END_OF, R.id.tv_player_ctrl_now_on_air_cur_time_tv);
            layoutParams.addRule(RelativeLayout.START_OF, R.id.tv_player_ctrl_now_on_air_total_time_tv);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.setMargins((int) getDensity() * 10, 0, (int) getDensity() * 10, 0);
            mVideoSeekBar.setLayoutParams(layoutParams);
            progressLayout.addView(mVideoCurTime);
            progressLayout.addView(mVideoFullScreen, 2);
            progressLayout.addView(mVideoTotalTime, 3);
        } else {
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

    private boolean isNavigationBarShow(boolean isHeight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
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
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    private int getNavigationBarHeight(boolean isHeight) {
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

    private int getScreenHeight() {
        return getHeightDensity() + getNavigationBarHeight(true);
    }

    private int getScreenWidth() {
        return getWidthDensity() + getNavigationBarHeight(false);
    }


    /**
     * set seek bar listener.
     *
     * @param seekBar
     */
    private void setSeekBarListener(SeekBar seekBar) {
        DTVTLogger.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DTVTLogger.start();
                time2TextViewFormat(mVideoCurTime, i);
                DTVTLogger.end();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                DTVTLogger.start();
                viewRefresher.removeMessages(REFRESH_VIDEO_VIEW);
                DTVTLogger.end();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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
        mTabLinearLayout = findViewById(R.id.dtv_contents_detail_main_layout_tab);
        mThumbnail = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail);
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
        parentLayout.addView(mThumbnailRelativeLayout, 1);
        setThumbnailInvisible();
    }

    /**
     * サムネイル画像の非表示.
     */
    private void setThumbnailInvisible() {
        mTabLinearLayout.setVisibility(View.GONE);
        mThumbnail.setVisibility(View.GONE);
        mThumbnailBtn.setVisibility(View.GONE);
    }

    /**
     * tabのレイアウトを設定.
     */
    private void initTab() {
        for (int i = 0; mTabNames.length > i; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins((int) getResources().getDimension(R.dimen.contents_tab_side_margin),
                    CONTENTS_DETAIL_TAB_OTHER_MARGIN, CONTENTS_DETAIL_TAB_OTHER_MARGIN, CONTENTS_DETAIL_TAB_OTHER_MARGIN);
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(CONTENTS_DETAIL_TAB_TEXT_SIZE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.rectangele);
                tabTextView.setTextColor(Color.GRAY);
            } else {
                tabTextView.setTextColor(Color.WHITE);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // タップによるタブ切り替え
                    int position = (int) view.getTag();
                    mViewPager.setCurrentItem(position);
                    setTab(position);
                }
            });
            mTabLinearLayout.addView(tabTextView);
        }
    }

    /**
     * インジケータ設置.
     *
     * @param position タブポジション
     */
    private void setTab(int position) {
        if (mTabLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mTabLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.rectangele);
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setBackgroundResource(0);
                    mTextView.setTextColor(Color.WHITE);
                }
            }
        }
    }

    /**
     * コンテンツ詳細用ページャアダプター.
     */
    private class ContentsDetailPagerAdapter extends FragmentStatePagerAdapter {
        ContentsDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragmentFactory.createFragment(position);
            //Fragmentへデータを渡す
            Bundle args = new Bundle();
            args.putParcelable(RECOMMEND_INFO_BUNDLE_KEY, mDetailData);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }

    @Override
    public void onContentsDetailInfoCallback(ArrayList<VodMetaFullData> contentsDetailInfo) {
        RecordingReservationContentsDetailInfo recordingReservationContentsDetailInfo = null;
        //詳細情報取得して、更新する
        if (contentsDetailInfo != null) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            mDetailFullData = contentsDetailInfo.get(0);
            // TODO ぷららサーバからmDetailDataを受け取った場合の処理を修正する可能性あり（画面間の受け渡しデータの値が検レコサーバと異なる場合など）
            if (!mIsOtherService) {
                DTVTLogger.debug("mIsOtherService:false");
                detailFragment.mOtherContentsDetailData.setTitle(mDetailFullData.getTitle());
                setTitleAndThumbnail(mDetailFullData.getTitle(), mDetailFullData.getmDtv_thumb_448_252());
                // リモート録画予約情報を生成
                mRecordingReservationContentsDetailInfo = new RecordingReservationContentsDetailInfo(
                        mDetailFullData.getmService_id(),
                        mDetailFullData.getTitle(),
                        mDetailFullData.getAvail_start_date(),
                        mDetailFullData.getDur(),
                        mDetailFullData.getR_value());
                mRecordingReservationContentsDetailInfo.setEventId(mDetailFullData.getmEvent_id());
                if (comparisonStartTime()) {
                    detailFragment.changeVisiblityRecordingReservationIcon(View.VISIBLE);
                    detailFragment.setRecordingReservationIconListener(this);
                } else {
                    detailFragment.changeVisiblityRecordingReservationIcon(View.INVISIBLE);
                    detailFragment.setRecordingReservationIconListener(null);
                }
            }
            detailFragment.mOtherContentsDetailData.setVodMetaFullData(contentsDetailInfo.get(FIRST_VOD_META_DATA));
            detailFragment.mOtherContentsDetailData.setDetail(mDetailFullData.getSynop());
            detailFragment.noticeRefresh();
            String[] credit_array = mDetailFullData.getmCredit_array();
            if (credit_array != null && credit_array.length > 0) {
                mDetailDataProvider.getRoleListData();
            }
            if (!TextUtils.isEmpty(mDetailFullData.getmService_id())) {
                mDetailDataProvider.getChannelList(1, 1, "", 1);
            }
            //if (UserState.LOGIN_NG != getUserState()){//ログイン状態でしか送信しない
                new SendOperateLog(mDetailData, mDetailFullData).sendOpeLog();
            //}
        }
    }

    @Override
    public void onRoleListCallback(ArrayList<RoleListMetaData> roleListInfo) {
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
    private List<String> getRoleList(String[] credit_array, ArrayList<RoleListMetaData> roleListInfo) {
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
                                staffList.add(roleListMetaData.getName());
                                staffList.add(creditName);
                            } else {
                                String[] oldData = ids.toString().split(",");
                                for (int k = 0; k < oldData.length; k++) {
                                    if (creditId.equals(oldData[k])) {
                                        staffList.set(k * 2 + 1, staffList.get(k * 2 + 1) + "," + creditName);
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
    public void channelListCallback(ArrayList<Channel> channels) {
        if (channels != null) {
            //チャンネル情報取得して、更新する
            if (!TextUtils.isEmpty(mDetailFullData.getmService_id())) {
                DtvContentsDetailFragment detailFragment = getDetailFragment();
                for (int i = 0; i < channels.size(); i++) {
                    Channel channel = channels.get(i);
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
        }
    }

    /**
     * 時刻.
     *
     * @param start 開始時刻
     * @param end   　終了時刻
     * @return データ整形
     */
    private String getDateFormat(String start, String end) {
        String channelDate = "";
        if (!TextUtils.isEmpty(start)) {
            start = start.replaceAll("-", "/");
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
            StringBuilder startBuilder = new StringBuilder();
            startBuilder.append(start.substring(0, 10));
            startBuilder.append(start.substring(11, 19));
            try {
                Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                calendar.setTime(sdf.parse(startBuilder.toString()));
                StringUtil util = new StringUtil(this);
                String[] strings = {String.valueOf(calendar.get(Calendar.MONTH)), "/",
                        String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), " (",
                        mDate[calendar.get(Calendar.DAY_OF_WEEK) - 1], ") ",
                        start.substring(11, 16), " - ",
                        end.substring(11, 16)};
                channelDate = util.getConnectString(strings);
            } catch (ParseException e) {
                DTVTLogger.debug(e);
            }
        }
        return channelDate;
    }

    /**
     * 詳細tabを取得.
     */
    private DtvContentsDetailFragment getDetailFragment() {
        Fragment currentFragment = mFragmentFactory.createFragment(0);
        return (DtvContentsDetailFragment) currentFragment;
    }

    @Override
    public void onClick(View v) {
        DTVTLogger.start();
        switch (v.getId()) {
            case R.id.tv_player_ctrl_video_record_player_pause_fl:
                if (!mPlayerController.isPlaying()) {
                    playStart();
                } else {
                    playPause();
                }
                hideCtrlViewAfterOperate();
                break;
            case R.id.tv_player_ctrl_now_on_air_full_screen_iv:
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    initPlayerView();
                    setPlayerEvent();
                } else {
                    Toast.makeText(this, "フルスクリーンに変更されています", Toast.LENGTH_SHORT).show();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    initPlayerView();
                    setPlayerEvent();
                }
                hideCtrlViewAfterOperate();
                break;
            case R.id.dtv_contents_detail_main_layout_thumbnail_btn:
                //DTVの場合
                if (mDetailData.getServiceId() == DTV_CONTENTS_SERVICE_ID) {
                    CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                    startAppDialog.setTitle(getResources().getString(R.string.dtv_content_service_start_dialog));
                    startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                        @Override
                        public void onOKCallback(boolean isOK) {
                            int localVersionCode = getVersionCode(DTV_PACKAGE_NAME);
                            //端末にDTVアプリはすでに存在した場合
                            if (isAppInstalled(DtvContentsDetailActivity.this, DTV_PACKAGE_NAME)) {
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
                                Uri uri = Uri.parse(GOOGLEPLAY_DOWNLOAD_URL);
                                Intent installIntent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(installIntent);
                            }
                        }
                    });
                    startAppDialog.showDialog();

                    break;
                } else if (mDetailData.getServiceId() == D_ANIMATION_CONTENTS_SERVICE_ID) {
                    CustomDialog startAppDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                    startAppDialog.setTitle(getResources().getString(R.string.d_anime_store_content_service_start_dialog));
                    startAppDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                        @Override
                        public void onOKCallback(boolean isOK) {
                            int localVersionCode = getVersionCode(DANIMESTORE_PACKAGE_NAME);
                            //端末にdアニメストアアプリはすでに存在した場合
                            if (isAppInstalled(DtvContentsDetailActivity.this, DANIMESTORE_PACKAGE_NAME)) {
                                //バージョンチェック
                                if (localVersionCode < DANIMESTORE_VERSION_STANDARD) {
                                    errorMessage = getResources().getString(R.string.d_anime_store_content_service_update_dialog);
                                    showErrorDialog(errorMessage);
                                } else {
                                    startApp(DANIMESTORE_START_URL);
                                }
                            } else {
                                Uri uri = Uri.parse(DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL);
                                Intent installIntent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(installIntent);
                            }
                        }
                    });
                    startAppDialog.showDialog();
                    break;
                }
            default:
                super.onClick(v);
        }
        DTVTLogger.end();
    }

    /**
     * 機能：APP起動.
     *
     * @param url
     */
    private void startApp(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 機能：中継アプリは端末にインストールするかどうかの判断.
     *
     * @param packageName 中継アプリのパッケージ名
     */
    public boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList();
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
     */
    private int getVersionCode(String packageName) {
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
    public void onStartRemoteControl() {
        DTVTLogger.start();
        // サービスIDにより起動するアプリを変更する
        if (mDetailData != null) {
            setRelayClientHandler();
            switch (mDetailData.getServiceId()) {
                case DTV_CONTENTS_SERVICE_ID: // dTV
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mDetailData.getContentId());
                    break;
                case D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mDetailData.getContentId());
                    break;
                case DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
                    requestStartApplication(
                            RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL, mDetailData.getContentId());
                    break;
                default:
                    break;
            }
        }
        super.onStartRemoteControl();
        DTVTLogger.end();
    }

    /**
     * onPlayerEvent.
     *
     * @param mediaPlayerController
     * @param event
     * @param arg
     */
    @Override
    public void onPlayerEvent(MediaPlayerController mediaPlayerController, int event, long arg) {
        DTVTLogger.start();
        switch (event) {
            case MediaPlayerDefinitions.PE_OPENED:
                playButton();
                break;
            case MediaPlayerDefinitions.PE_COMPLETED:
                pauseButton();
                break;
            case MediaPlayerDefinitions.PE_START_NETWORK_CONNECTION:
                break;
            case MediaPlayerDefinitions.PE_START_AUTHENTICATION:
                showMessage("認証開始");
                break;
            case MediaPlayerDefinitions.PE_START_BUFFERING:
                showMessage("バッファー開始");
                break;
            case MediaPlayerDefinitions.PE_START_RENDERING:
                showMessage("再生開始");
                break;
            case MediaPlayerDefinitions.PE_FIRST_FRAME_RENDERED:
                //showMessage("PE_FIRST_FRAME_RENDERED");
                break;
        }
        DTVTLogger.end();
    }

    @Override
    public void onEndRemoteControl() {
        // nop.
        super.onEndRemoteControl();
    }

    /**
     * onStateChanged.
     *
     * @param mediaPlayerController controller
     * @param i                     i
     */
    @Override
    public void onStateChanged(MediaPlayerController mediaPlayerController, int i) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * onFormatChanged.
     *
     * @param mediaPlayerController controller
     */
    @Override
    public void onFormatChanged(MediaPlayerController mediaPlayerController) {
        DTVTLogger.start();
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
    public void onError(MediaPlayerController mediaPlayerController, int i, long l) {
        DTVTLogger.start();
        showMessage("" + i);
        DTVTLogger.end();
    }

    /**
     * onCaptionData.
     *
     * @param mediaPlayerController
     * @param captionDrawCommands
     */
    @Override
    public void onCaptionData(MediaPlayerController mediaPlayerController, CaptionDrawCommands captionDrawCommands) {
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
    public void onSuperData(MediaPlayerController mediaPlayerController, CaptionDrawCommands captionDrawCommands) {
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
        finishPlayer();
        if (null != mToast) {
            mToast.cancel();
        }
    }

    @Override
    public void recordingReservationResult(RemoteRecordingReservationResultResponse response) {
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
    public void onClickRecordingReservationIcon(View view) {
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
     * @return
     */
    private boolean comparisonStartTime() {
        long nowTimeEpoch = DateUtils.getNowTimeFormatEpoch();
        long canRecordingReservationTime =
                mRecordingReservationContentsDetailInfo.getStartTime() - (DateUtils.EPOCH_TIME_ONE_HOUR * 2);
        if (nowTimeEpoch >= canRecordingReservationTime) {
            // 開始時間2時間前を過ぎている場合
            return false;
        }
        return true;
    }

    /**
     * 録画予約成功時ダイアログ表示.
     */
    private void showCompleteDialog() {
        CustomDialog completeRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        completeRecordingReservationDialog.setTitle(getResources().getString(R.string.recording_reservation_complete_dialog_title));
        completeRecordingReservationDialog.setContent(getResources().getString(R.string.recording_reservation_complete_dialog_msg));
        completeRecordingReservationDialog.setConfirmText(R.string.recording_reservation_complete_dialog_ok);
        // Cancelable
        completeRecordingReservationDialog.setCancelable(false);
        completeRecordingReservationDialog.showDialog();
    }

    /**
     * 録画予約失敗時エラーダイアログ表示.
     */
    private CustomDialog createErrorDialog() {
        CustomDialog failedRecordingReservationDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        failedRecordingReservationDialog.setContent(getResources().getString(R.string.recording_reservation_failed_dialog_msg));
        failedRecordingReservationDialog.setCancelText(R.string.recording_reservation_failed_dialog_confirm);
        // Cancelable
        failedRecordingReservationDialog.setCancelable(false);
        return failedRecordingReservationDialog;
    }

    private CustomDialog.ApiItemSelectCallback mItemSelectCallback = new CustomDialog.ApiItemSelectCallback() {
        @Override
        public void onItemSelectCallback(AlertDialog dialog, int position) {
            // リスト選択時の処理
            //TODO 定期予約実装時 1 は "繰り返し録画予約する"になる
            switch (position) {
                case RECORDING_RESERVATION_DIALOG_INDEX_0: // 録画予約するをタップ
                    mRecordingReservationContentsDetailInfo.setLoopTypeNum(
                            RecordingReservationContentsDetailInfo.REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_0);
                    createRecordingReservationConfirmDialog();
                    dialog.dismiss();
                    break;
                case RECORDING_RESERVATION_DIALOG_INDEX_1: // キャンセルをタップ
                    DTVTLogger.debug("Cancel RecordingReservation");
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 録画予約確認ダイアログを表示.
     */
    private CustomDialog createRecordingReservationConfirmDialog() {
        CustomDialog recordingReservationConfirmDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        recordingReservationConfirmDialog.setTitle(getResources().getString(R.string.recording_reservation_confirm_dialog_title));
        recordingReservationConfirmDialog.setContent(getResources().getString(R.string.recording_reservation_confirm_dialog_msg));
        recordingReservationConfirmDialog.setConfirmText(R.string.recording_reservation_confirm_dialog_confirm);
        recordingReservationConfirmDialog.setCancelText(R.string.recording_reservation_confirm_dialog_cancel);
        // Cancelable
        recordingReservationConfirmDialog.setCancelable(false);
        recordingReservationConfirmDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
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
        final String CONTRACT_STATUS_NONE = "none";
        final String CONTRACT_STATUS_DTV = "001";
        final String CONTRACT_STATUS_H4D = "002";
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

        //DBに保存されているUserInfoから契約情報を確認する
        UserInfoInsertDataManager dataManager = new UserInfoInsertDataManager(this);
        dataManager.readUserInfoInsertList();
        String contractInfo = StringUtil.getUserContractInfo(dataManager.getmUserData());
        DTVTLogger.debug("contractInfo: " + contractInfo);

        if (contractInfo == null || contractInfo.isEmpty() || CONTRACT_STATUS_NONE.equals(contractInfo)) {
            //契約情報が未設定、または"none"の場合は視聴不可
            Toast.makeText(this, "視聴不可(未契約)", Toast.LENGTH_SHORT).show();
        } else if (CONTRACT_STATUS_DTV.equals(contractInfo)) {
            //契約情報が"001"の場合
            DTVTLogger.debug("disp_type: " + mDetailFullData.getDisp_type());
            //"disp_type"の値を確認する
            if (TV_PROGRAM.equals(mDetailFullData.getDisp_type())) {
                DTVTLogger.debug("tv_service: " + mDetailFullData.getmTv_service());
                //"tv_service"の値を確認する
                if (IS_TV_SERVICE_FLAG.equals(mDetailFullData.getmTv_service())) {
                    if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                        Toast.makeText(this, "視聴可能. 30日以内:" + mIsLimitThirtyDay, Toast.LENGTH_SHORT).show();
                    } else if (mDetailFullData.getAvail_start_date() > DateUtils.getNowTimeFormatEpoch()) {
                        Toast.makeText(this, "視聴不可(放送時間外のため再生導線を非表示)", Toast.LENGTH_SHORT).show();
                    }  else if (mDetailFullData.getAvail_end_date() <= DateUtils.getNowTimeFormatEpoch()) {
                        if (checkVodDate(mDetailFullData.getmVod_start_date(), mDetailFullData.getmVod_end_date())) {
                            Toast.makeText(this, "視聴可能. 30日以内:" + mIsVodLimitThirtyDay, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
        } else if (CONTRACT_STATUS_H4D.equals(contractInfo)) {
            //契約情報が"002"の場合
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
                                        Toast.makeText(this, "視聴可能", Toast.LENGTH_SHORT).show();
                                    } else if (mDetailFullData.getAvail_start_date() > DateUtils.getNowTimeFormatEpoch()
                                            || DateUtils.getNowTimeFormatEpoch() >= mDetailFullData.getAvail_end_date()) {
                                        //視聴期間外のため視聴不可
                                        Toast.makeText(this, "視聴不可(放送時間外のため再生導線を非表示)", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (CH_TYPE_PREMIUM.equals(mChannel.getChType())) {
                                    DTVTLogger.debug("premium");
                                    //購入済みチャンネル一覧を取得
                                    mDetailDataProvider.getChListData();
                                    //onRentalChListCallbackで続きの判定を行う
                                }
                            }
                        }
                        Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
                    } else if (IS_TV_SERVICE_FLAG.equals(mDetailFullData.getmTv_service())) {
                        //"tv_service"が1の場合（dチャンネル）
                        if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                            Toast.makeText(this, "視聴可能. 30日以内:" + mIsVodLimitThirtyDay, Toast.LENGTH_SHORT).show();
                        } else if (mDetailFullData.getAvail_start_date() > DateUtils.getNowTimeFormatEpoch()) {
                            Toast.makeText(this, "視聴不可(放送時間外のため再生導線を非表示)", Toast.LENGTH_SHORT).show();
                        } else if (mDetailFullData.getAvail_end_date() <= DateUtils.getNowTimeFormatEpoch()) {
                            if (checkVodDate(mDetailFullData.getmVod_start_date(), mDetailFullData.getmVod_end_date())) {
                                Toast.makeText(this, "視聴可能. 30日以内:" + mIsVodLimitThirtyDay, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
                    break;
                case VIDEO_PROGRAM:
                    //"dtv"の値を確認する
                    DTVTLogger.debug("dtv: " + mDetailFullData.getDtv());
                    if (IS_DTV_FLAG.equals(mDetailFullData.getDtv())) {
                        //視聴可能期限の確認
                        if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                            Toast.makeText(this, "視聴可能. 30日以内:" + mIsLimitThirtyDay, Toast.LENGTH_SHORT).show();
                        } else {
                            //視聴可能期限内ではないので視聴不可
                            Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
                        }
                    } else if (NOT_DTV_FLAG.equals(mDetailFullData.getDtv())) {
                        //"dtv"の値が0の場合、"bvflg"の値を確認する
                        DTVTLogger.debug("bvflg: " + mDetailFullData.getBvflg());
                        if (IS_BV_FLAG.equals(mDetailFullData.getBvflg())) {
                            //視聴可能期限の確認
                            if (checkWatchDate(mDetailFullData.getAvail_start_date(), mDetailFullData.getAvail_end_date())) {
                                Toast.makeText(this, "視聴可能. 30日以内:" + mIsLimitThirtyDay, Toast.LENGTH_SHORT).show();
                            } else {
                                //視聴可能期限内ではないので視聴不可
                                Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //bvflgが1ではないので視聴不可
                            Toast.makeText(this, "視聴不可", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case SUBSCRIPTION_PACKAGE:
                    //レンタルコンテンツ(購入済みVOD)一覧を取得
                    mDetailDataProvider.getVodListData();
                    //onRentalVodListCallbackで続きの判定を行う
                    break;
                default:
                    break;
            }
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
        //購入済みVOD取得からの戻り(視聴可否判定)
        ArrayList<ActiveData> activeDatas = response.getVodActiveData();

        String[] liinfArray = mDetailFullData.getmLiinf_array();
        String puid = mDetailFullData.getPuid();
        for (String liinf : liinfArray) {
            //liinfを"|"区切りで分解する
            String[] column = liinf.split("|", 0);
            for (ActiveData activeData : activeDatas) {
                String license_id = activeData.getLicenseId();
                if (license_id.equals(column[1]) || license_id.equals(puid)) {
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
                        if (activeData.getValidEndDate() - DateUtils.getNowTimeFormatEpoch()
                                < DateUtils.EPOCH_TIME_ONE_DAY * ONE_MONTH) {
                            mIsLimitThirtyDay = true;
                        }
                        //視聴可能
                        Toast.makeText(this, "視聴可能. 30日以内:" + mIsLimitThirtyDay, Toast.LENGTH_SHORT).show();
                    } else {
                        //視聴期限範囲外のため視聴不可だが他のactive_listをチェックするためここでは何もしない
                        DTVTLogger.debug("Out of date range");
                    }
                }
            }
        }
        Toast.makeText(this, "視聴不可(契約導線を表示)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalChListCallback(final PurchasedChListResponse response) {
        //購入済みCH一覧取得からの戻り
        DTVTLogger.start();
        ChannelList channelList = response.getChannelListData();
        List<HashMap<String, String>> chList = channelList.getClList();

        if (checkChServiceIdListSame(chList)) {
            //購入CHと対象CHのservice_idが一致
            ArrayList<ActiveData> activeDatas = response.getChActiveData();
            //購入済みCHのactive_list内のlicense_idと、対象CHのpuid, sub_puid, CHPACK-puid, CHPACK-sub_puidを比較
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
                        if (activeData.getValidEndDate() - DateUtils.getNowTimeFormatEpoch()
                                < DateUtils.EPOCH_TIME_ONE_DAY * ONE_MONTH) {
                            mIsLimitThirtyDay = true;
                        }
                        //視聴可能
                        Toast.makeText(this, "視聴可能. 30日以内:" + mIsLimitThirtyDay, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //license_id不一致のため視聴不可だが他のlicense_idをチェックするためここでは何もしない
                    DTVTLogger.debug("Mismatch of active_list");
                }
            }
        }
        Toast.makeText(this, "視聴不可(契約導線を表示)", Toast.LENGTH_SHORT).show();
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
            String serviceId = hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID);
            if (serviceId != null && serviceId.isEmpty()) {
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

    /* test code begin */
    public void testButton(View v) {
        //TODO 関連コンテンツボタン押下時の動作。後で削除する
        startActivity(DtvContentsDetailActivity.class, null);
    }
    /* test code end */
}