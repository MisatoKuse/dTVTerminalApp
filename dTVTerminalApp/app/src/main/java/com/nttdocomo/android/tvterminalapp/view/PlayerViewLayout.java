/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.view;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.digion.dixim.android.secureplayer.MediaPlayerController;
import com.digion.dixim.android.secureplayer.MediaPlayerDefinitions;
import com.digion.dixim.android.secureplayer.SecureVideoView;
import com.digion.dixim.android.secureplayer.SecuredMediaPlayerController;
import com.digion.dixim.android.secureplayer.helper.CaptionDrawCommands;
import com.digion.dixim.android.util.ExternalDisplayHelper;
import com.digion.dixim.android.util.SafetyRunnable;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.common.ProcessSettingFile;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.struct.MediaVideoInfo;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * プレイヤーレイアウト.
 */
public class PlayerViewLayout extends RelativeLayout implements View.OnClickListener,
        MediaPlayerController.OnStateChangeListener, MediaPlayerController.OnFormatChangeListener,
        MediaPlayerController.OnPlayerEventListener, MediaPlayerController.OnErrorListener,
        MediaPlayerController.OnCaptionDataListener {

    /**エラータイプ.*/
    public enum PlayerErrorType {
        /**リモート.*/
        REMOTE,
        /**アクティベーション.*/
        ACTIVATION,
        /**外部出力.*/
        EXTERNAL,
        /**年齢制限.*/
        AGE,
        /**なし.*/
        NONE,
        /**初期化成功.*/
        INIT_SUCCESS
    }

    /** コンストラクタ.*/
    private Context mContext = null;
    /** アクティビティ.*/
    private Activity mActivity = null;
    /**プレイヤーコントローラ.*/
    private SecuredMediaPlayerController mPlayerController = null;
    /** SurfaceView.*/
    private SecureVideoView mSecureVideoPlayer = null;
    /** 再生するビデオ属性.*/
    private MediaVideoInfo mCurrentMediaInfo = null;
    /** 画面操作検知.*/
    private GestureDetector mGestureDetector = null;
    /** 録画コントローラビューRelativeLayout.*/
    private RelativeLayout mRecordCtrlView = null;
    /**ビデオコントローラバーRelativeLayout.*/
    private RelativeLayout mVideoCtrlBar = null;
    /**プログレースRelativeLayout.*/
    private RelativeLayout mProgressLayout = null;
    /**サムネイルRelativeLayout.*/
    private RelativeLayout mThumbnailRelativeLayout = null;
    /**ビデオ再生停止TextView.*/
    private FrameLayout mVideoPlayPause = null;
    /**再生シークバー.*/
    private SeekBar mVideoSeekBar = null;
    /**ビデオカレント時刻.*/
    private TextView mVideoCurTime = null;
    /**video全長.*/
    private TextView mVideoTotalTime = null;
    /**全画面再生.*/
    private ImageView mVideoFullScreen = null;
    /**巻き戻しImageView.*/
    private ImageView mVideoRewind10 = null;
    /**早送りImageView.*/
    private ImageView mVideoFast30 = null;
    /**再生前くるくる処理.*/
    private ProgressBar mProgressBar;
    /**NOW ON AIRアイコン.*/
    private ImageView mPortraitLayout;
    /**チャンネル情報.*/
    private RelativeLayout mLandscapeLayout;
    /**チャンネルロゴ.*/
    private ImageView mChannellogo;
    /**チャンネル名.*/
    private TextView mChanneltitle;
    /**チャンネルアイコンURL.*/
    private String mTxtChannellogo;
    /**チャンネル名.*/
    private String mTxtChanneltitle;
    /**サムネイルプロバイダー.*/
    private ThumbnailProvider mThumbnailProvider;
    /** サムネイル取得処理ストップフラグ .*/
    private boolean mIsDownloadStop = false;
    /** 放送中フラグ.*/
    private boolean mIsVideoBroadcast = false;
    /**再生開始可否.*/
    private boolean mCanPlay = false;
    /**操作アイコン表示か.*/
    private boolean mIsHideOperate = true;
    /**再生開始フラグ.*/
    private boolean mAlreadyRendered = false;
    /**完了フラグ.*/
    private boolean mIsCompleted = false;
    /** 外部出力制御.*/
    private ExternalDisplayHelper mExternalDisplayHelper;
    /** 外部出力制御判定フラグ.*/
    private boolean mExternalDisplayFlg = false;
    /**vローカルファイルパスー.*/
    private static final String LOCAL_FILE_PATH = "file://";
    /** プレイヤー横画面時のシークバーの下マージン.*/
    private static final int SEEKBAR_BOTTOM_MARGIN = 4;
    /** プレイヤー横画面時のコントロールバーの下マージン.*/
    private static final int MEDIA_CONTROL_BAR_UNDER_MARGIN = 32;
    /** プレイヤー横画面時のシークバーの時間の左右マージン.*/
    private static final int SEEKBAR_TIME_LATERAL_MARGIN = 18;
    /** プレイヤー横画面時のフルスクリーンボタンの右マージン.*/
    private static final int FULL_SCREEN_BUTTON_RIGHT_MARGIN = 16;
    /**コントローラービューを非表示になるまでの待ち時間.*/
    private static final long HIDE_IN_3_SECOND = 3 * 1000;
    /**secureplayer-core 側でもタイムアウト待ちが発生するので実質90秒ほどになる.*/
    private static final long REMOTEACCESS_RECONNECT_TIMEOUT = 1000 * 60;
    /**secureplayer-core 側でもタイムアウト待ちが発生するので実質90秒ほどになる.*/
    private static final long REMOTEACCESS_DMS_DISCONNECT_TIMEOUT = 1000 * 30;
    /**リトライ回数.*/
    private static final int OPEN_SECURE_VIDEO_RETRY_TIME = 25;
    /**再接続開始時間.*/
    private volatile long mReconnectStartTime;
    /**切断時間.*/
    private volatile long mDmsDisconnectedTime;
    /**ビデオサイズ.*/
    private long mTotalDuration = 0;
    /**前回のポジション.*/
    private int mPlayStartPosition = 0;
    /**再発火.*/
    private static final int REFRESH_VIDEO_VIEW = 0;
    /** 巻き戻す10s.*/
    private static final int REWIND_SECOND = 10 * 1000;
    /**早送り30s.*/
    private static final int FAST_SECOND = 30 * 1000;
    /** アスペクト比(16:9)の16.*/
    private static final int SCREEN_RATIO_WIDTH_16 = 16;
    /** アスペクト比(16:9)の9.*/
    private static final int SCREEN_RATIO_HEIGHT_9 = 9;
    /** アスペクト比(4:3)の4.*/
    private static final int SCREEN_RATIO_WIDTH_4 = 4;
    /** アスペクト比(4:3)の3.*/
    private static final int SCREEN_RATIO_HEIGHT_3 = 3;
    /** dpi.*/
    private float density;
    /** 端末幅さ.*/
    private int mScreenWidth = 0;
    /** 端末高さ.*/
    private int mScreenHeight = 0;
    /** 端末幅さ+nav.*/
    private int mScreenNavWidth = 0;
    /** 端末高さ+nav.*/
    private int mScreenNavHeight = 0;
    /** 年齢.*/
    private int mAge = 0;
    /**ハンドラー.*/
    private static final Handler sCtrlHandler = new Handler(Looper.getMainLooper());
    /**再生コールバック.*/
    private PlayerStateListener mPlayerStateListener;
    /**ヘッダー.*/
    private Map<String, String> additionalHeaders;
    /**
     *　コントロールビューを非表示にする.
     */
    private final Runnable mHideCtrlViewThread = new Runnable() {

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

    /**
     * 再生監視リスナー.
     */
    public interface PlayerStateListener {
        /**
         * エラーコールバック.
         * @param mPlayerErrorType エラータイプ
         */
        void onErrorCallBack(final PlayerErrorType mPlayerErrorType);
        /**
         * エラーコールバック.
         * @param errorCode エラーコード
         */
        void onPlayerErrorCallBack(final int errorCode);
        /**
         * 横、縦チェンジコールバック.
         * @param isLandscape 横
         */
        void onScreenOrientationChangeCallBack(final boolean isLandscape);
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
     * コンストラクタ.
     *
     * @param context コンテクスト
     */
    public PlayerViewLayout(final Context context) {
        this(context, null);
    }

    /**
     * コンストラクタ.
     * @param context コンテクスト
     * @param attrs attrs
     */
    public PlayerViewLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * コンストラクタ.
     * @param context コンテクスト
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public PlayerViewLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mActivity = (Activity) mContext;
    }

    /**
     * プレイヤー初期化.
     * @param mPlayerStateListener 再生コールバック
     */
    public void setPlayerStateListener(final PlayerStateListener mPlayerStateListener) {
        this.mPlayerStateListener = mPlayerStateListener;
    }

    /**
     * スクリーンサイズ.
     * @param width スクリーン幅さ
     * @param height スクリーン高さ
     */
    public void setScreenSize(final int width, final int height) {
        mScreenWidth = width;
        mScreenHeight = height;
    }

    /**
     * スクリーンサイズ + NavigationBar.
     * @param width スクリーン幅さ + NavigationBar
     * @param height スクリーン高さ + NavigationBar
     */
    public void setScreenNavigationBarSize(final int width, final int height) {
        mScreenNavWidth = width;
        mScreenNavHeight = height;
    }

    /**
     * スクリーン幅さ.
     * @return スクリーン幅
     */
    private int getWidthDensity() {
        return mScreenWidth;
    }

    /**
     * スクリーン高さ.
     * @return スクリーン高
     */
    private int getHeightDensity() {
        return mScreenHeight;
    }

    /**
     * スクリーン幅さ+ NavigationBar.
     * @return スクリーン高+ナビゲーション
     */
    private int getScreenNavHeight() {
        return mScreenNavHeight;
    }

    /**
     * スクリーン高さ+ NavigationBar.
     * @return スクリーン幅+ナビゲーション
     */
    private int getScreenNavWidth() {
        return mScreenNavWidth;
    }

    /**
     * 外部出力Helperの作成.
     */
    public void createExternalDisplayHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mExternalDisplayHelper = ExternalDisplayHelper.create(mContext, DISPLAY_EVENT_LISTENER,
                    createPresentationEventListener());
        } else {
            mExternalDisplayHelper = ExternalDisplayHelper
                    .createForCompat(mContext, DISPLAY_EVENT_LISTENER);
        }
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
                    mPlayerStateListener.onErrorCallBack(PlayerErrorType.EXTERNAL);
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
     * VideoSeekBar利用できるのチェック.
     * @return シークバー有効チェック
     */
    private boolean isSeekable() {
        return mTotalDuration > 0;
    }

    /**
     * 前回のポジションから再生開始.
     * @param point 再生開始ポジション
     * @return true:正常再生 false:異常
     */
    private boolean openVideoView(final int point) {
        int position = point;
        mAlreadyRendered = false;
        mIsCompleted = false;
        if (position < 0 || mTotalDuration < position) {
            position = 0;
            mPlayStartPosition = 0;
        }
        return openSecurePlayer(position);
    }

    /**
     * 前回のポジションから再生開始.
     * @param startMS 開始ポジション
     * @return true:正常open false:異常発生
     */
    private boolean openSecurePlayer(final int startMS) {
        if (mPlayerController == null) {
            return false;
        }
        if (mActivity.isFinishing()) {
            return false;
        }
        try {
            mPlayerController.setDataSource(mCurrentMediaInfo, additionalHeaders, startMS);
        } catch (IOException e) {
            return false;
        }
        mPlayerController.setScreenOnWhilePlaying(true);
        return true;
    }

    /**
     * リトライ処理.
     * @param errorCode エラーコード
     * @return true:リトライする false:リトライしない
     */
    private boolean needRetry(final int errorCode) {
        if (mCurrentMediaInfo == null) {
            return false;
        }
        if (!mCurrentMediaInfo.isRemote()) {
            return false;
        }
        if (errorCode != MediaPlayerDefinitions.SP_SOURCE_CONNECTION_ERROR
                && errorCode != MediaPlayerDefinitions.SP_SOURCE_DTCP_AKE_ERROR) {
            return false;
        }
        // まだ再生が始まっていない場合はリトライしない。
        if (!mAlreadyRendered) {
            return false;
        }
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        if (mReconnectStartTime == 0) {
            mReconnectStartTime = elapsedRealtime;
        }

        if (elapsedRealtime - mReconnectStartTime > REMOTEACCESS_RECONNECT_TIMEOUT) {
            return false;
        }
        if (mDmsDisconnectedTime != 0) {
            if (elapsedRealtime - mDmsDisconnectedTime > REMOTEACCESS_DMS_DISCONNECT_TIMEOUT) {
                return false;
            }
        }
        return true;
    }

    /**
     * 再生ポジション取得.
     * @return  再生ポジション
     */
    private int getCurrentPosition() {
        if (mPlayerController != null) {
            return mPlayerController.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 再生をopen.
     * @param pos 再生開始ポジション
     * @param successHandler ハンドラー
     * @param errorHandler runnable
     */
    private void tryOpenVideo(final int pos, final Runnable successHandler,
                              final Runnable errorHandler) {
        getHandler().post(new SafetyRunnable(mContext) {
            int retry = OPEN_SECURE_VIDEO_RETRY_TIME;

            @Override
            protected void main() {
                if (openVideoView(pos)) {
                    if (mSecureVideoPlayer != null) {
                        mSecureVideoPlayer.setVisibility(View.VISIBLE);
                    }
                    if (successHandler != null) {
                        successHandler.run();
                    }
                    return;
                }
                if (--retry == 0) {
                    errorHandler.run();
                } else {
                    getHandler().postDelayed(this, 200);
                }
            }
        });
    }

    /**
     * 再生中のくるくる処理.
     * @param isShow true 表示　false 非表示
     */
    public void showPlayingProgress(final boolean isShow) {
        if (mProgressBar == null) {
            mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyle);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mProgressBar.setLayoutParams(layoutParams);
        }
        if (isShow) {
            this.addView(mProgressBar);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            this.removeView(mProgressBar);
            mProgressBar.setVisibility(View.GONE);
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
     * ペアレントビューを設定.
     * @param mThumbnailRelativeLayout ペアレントビュー
     */
    public void setParentLayout(final RelativeLayout mThumbnailRelativeLayout) {
        this.mThumbnailRelativeLayout = mThumbnailRelativeLayout;
    }

    /**
     * ユーザ年齢をセット.
     */
    public void setUserAgeInfo() {
        mAge = UserInfoUtils.getUserAgeInfoWrapper(SharedPreferencesUtils.getSharedPreferencesUserInfo(mContext));
    }

    /**
     * 解像度(dpi)を設定.
     * @param density 解像度(dpi)
     */
    public void setDensity(final float density) {
        this.density = density;
    }

    @Override
    public void onCaptionData(final MediaPlayerController mediaPlayerController, final CaptionDrawCommands captionDrawCommands) {

    }

    @Override
    public void onSuperData(final MediaPlayerController mediaPlayerController, final CaptionDrawCommands captionDrawCommands) {

    }

    @Override
    public void onError(final MediaPlayerController mediaPlayerController, final int what, final long arg) {
        if (mPlayerController != null && isSeekable()) {
            if (!mIsCompleted && isSeekable() && mReconnectStartTime == 0) {
                mPlayStartPosition = getCurrentPosition();
                mIsCompleted = true;
            }
            if (needRetry(what)) {
                tryOpenVideo(mPlayStartPosition, null, new Runnable() {
                    @Override
                    public void run() {
                        mPlayerStateListener.onPlayerErrorCallBack(what);
                    }
                });
                return;
            }
        }
        mPlayerStateListener.onPlayerErrorCallBack(what);
    }

    @Override
    public void onFormatChanged(final MediaPlayerController mediaPlayerController) {
        DTVTLogger.start();
        //パレンタルチェック
        if (mAge < mediaPlayerController.getParentalRating()) {
            mPlayerController.stop();
            mPlayerStateListener.onErrorCallBack(PlayerErrorType.AGE);
        }
        //外部出力制御
        mExternalDisplayHelper.onResume();
        DTVTLogger.end();
    }

    @Override
    public void onPlayerEvent(final MediaPlayerController mediaPlayerController, final int event, final long arg) {
        switch (event) {
            case MediaPlayerDefinitions.PE_OPENED:
                playButton(true);
                break;
            case MediaPlayerDefinitions.PE_COMPLETED:
                mIsCompleted = true;
                mPlayStartPosition = 0;
                playButton(false);
                showPlayingProgress(false);
                break;
            case MediaPlayerDefinitions.PE_START_NETWORK_CONNECTION:
            case MediaPlayerDefinitions.PE_START_AUTHENTICATION:
            case MediaPlayerDefinitions.PE_START_BUFFERING:
            case MediaPlayerDefinitions.PE_START_RENDERING:
                mReconnectStartTime = 0;
                mDmsDisconnectedTime = 0;
                break;
            case MediaPlayerDefinitions.PE_FIRST_FRAME_RENDERED:
                mAlreadyRendered = true;
                mPlayerStateListener.onErrorCallBack(PlayerErrorType.NONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStateChanged(final MediaPlayerController mediaPlayerController, final int i) {

    }

    @Override
    public void onClick(final View v) {
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
                if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                initPlayerView();
                setPlayerEvent();
                hideCtrlViewAfterOperate();
                break;
        }
    }

    /**
     * リモート視聴以外はそのまま再生を行う。リモート視聴の場合は再生可否のチェックを行う.
     */
    private void playStart() {
        if (!mCurrentMediaInfo.isRemote()) {
            //リモート視聴ではないので、そのまま実行する
            playStartOrigin();
            return;
        } else {
            DlnaDmsItem item = SharedPreferencesUtils.getSharedPreferencesStbInfo(mContext);
            if (item != null) {
                String remoteExpireDate = SharedPreferencesUtils.getRemoteDeviceExpireDate(mContext);
                if (TextUtils.isEmpty(remoteExpireDate)) {
                    mPlayerStateListener.onErrorCallBack(PlayerErrorType.REMOTE);
                    return;
                }
            }
        }

        //リモート視聴なので、設定ファイルの内容に応じて判定を行う
        ProcessSettingFile processSettingFile = new ProcessSettingFile((ContentDetailActivity) mActivity, false);
        //リモート視聴の明示
        processSettingFile.setIsRemote(true);
        //コールバック指定付きで処理を開始する
        processSettingFile.controlAtSettingFile(new ProcessSettingFile.ProcessSettingFileCallBack() {
            @Override
            public void onCallBack(final boolean dialogSwitch) {
                //ここは、エラーダイアログの表示と同時に呼び出される。ここでダイアログを表示する場合、アプリ終了ダイアログのみとなる。
                //終了ダイアログの表示中に動画が再生される事が無いように、playStartOriginの呼び出しはdialogSwitchがfalseの場合のみとなる。
                if (!dialogSwitch) {
                    playStartOrigin();
                }
            }
        });
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
     * 元の再生開始.
     * (リモート視聴の再生開始可否の為に、リネーム後に前チェックを追加)
     */
    private void playStartOrigin() {
        mIsCompleted = false;
        synchronized (this) {
            if (mCanPlay) {
                playButton(true);
                mPlayerController.start();
                showPlayingProgress(true);
            }
        }
    }

    /**
     * プレイヤービュー初期化.
     */
    private void initSecureVideoView() {
        mSecureVideoPlayer = new SecureVideoView(mContext);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mSecureVideoPlayer.setLayoutParams(layoutParams);
        this.addView(mSecureVideoPlayer);
        this.setVisibility(View.VISIBLE);
    }

    /**
     * initPlayerView mView.
     */
    private void initPlayerView() {
        this.removeView(mRecordCtrlView);
        this.getKeepScreenOn();
        mRecordCtrlView = (RelativeLayout) View.inflate(mContext, R.layout.tv_player_ctrl_video_record, null);
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                mScreenWidth, mScreenHeight);
        initLiveLayout();
        setScreenSize(mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, layoutParams);
        mVideoPlayPause.setOnClickListener(this);
        mVideoFullScreen.setOnClickListener(this);
        mVideoCtrlRootView.setOnClickListener(this);
        setSeekBarListener(mVideoSeekBar);
        this.addView(mRecordCtrlView);
        //初期化の時点から、handlerにmsgを送る
        viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
        hideCtrlView();
        if (mPlayerController != null) {
            if (mPlayerController.isPlaying()) {
                mVideoPlayPause.getChildAt(0).setVisibility(View.GONE);
                mVideoPlayPause.getChildAt(1).setVisibility(View.VISIBLE);
            }
        }
    }

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
        mPortraitLayout.setVisibility(INVISIBLE);
        mLandscapeLayout.setVisibility(INVISIBLE);
        mSecureVideoPlayer.setBackgroundResource(0);
        DTVTLogger.end();
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
            mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            playerParams.height = getScreenNavHeight();
            if (mPlayerController != null) {
                int ratio = mPlayerController.getVideoAspectRatio();
                if (ratio == SecureVideoView.RATIO_4x3) {
                    playerParams.width = (getHeightDensity() * SCREEN_RATIO_WIDTH_4 / SCREEN_RATIO_HEIGHT_3);
                    playerParams.gravity = Gravity.CENTER_HORIZONTAL;
                } else {
                    playerParams.width = getScreenNavWidth();
                }
            }
            if (playerParams.height > playerParams.width) {
                playerParams.height = getScreenNavWidth();
                if (mPlayerController != null) {
                    int widthRatio = mPlayerController.getVideoAspectRatioWidth();
                    int heightRatio = mPlayerController.getVideoAspectRatioHeight();
                    playerParams.width = getScreenNavWidth() / heightRatio * widthRatio;
                }
                if (playerParams.width < getScreenNavHeight()) {
                    playerParams.gravity = Gravity.CENTER_HORIZONTAL;
                } else {
                    playerParams.width = getScreenNavHeight();
                    int widthRatio = mPlayerController.getVideoAspectRatioWidth();
                    int heightRatio = mPlayerController.getVideoAspectRatioHeight();
                    playerParams.height = getScreenNavHeight() / widthRatio * heightRatio;
                    if (playerParams.height < getScreenNavWidth()) {
                        playerParams.setMargins(0, (getScreenNavWidth() - playerParams.height) / 2, 0, 0);
                    }
                }
            }
            mScreenWidth = playerParams.width;
            setPlayerProgressView(true);
            mPlayerStateListener.onScreenOrientationChangeCallBack(true);
        } else {
            mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
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
            if (playerParams.width < getScreenNavWidth()) {
                playerParams.width = getScreenNavWidth();
                int ratio = mPlayerController.getVideoAspectRatio();
                if (ratio == SecureVideoView.RATIO_4x3) {
                    playerParams.height = (getScreenNavWidth() * SCREEN_RATIO_HEIGHT_3 / SCREEN_RATIO_WIDTH_4);
                } else {
                    playerParams.height = (getScreenNavWidth() * SCREEN_RATIO_HEIGHT_9 / SCREEN_RATIO_WIDTH_16);
                }
            }
            setPlayerProgressView(false);
            mPlayerStateListener.onScreenOrientationChangeCallBack(false);
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
                layoutParams.setMargins(0, 0, (int) density * FULL_SCREEN_BUTTON_RIGHT_MARGIN,
                        (int) density * FULL_SCREEN_BUTTON_RIGHT_MARGIN);
                mVideoFullScreen.setLayoutParams(layoutParams);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, (int) density * MEDIA_CONTROL_BAR_UNDER_MARGIN);
                mProgressLayout.setLayoutParams(layoutParams);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins((int) density * SEEKBAR_TIME_LATERAL_MARGIN, 0, 0, 0);
                mVideoCurTime.setLayoutParams(layoutParams);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(0, 0, (int) density * FULL_SCREEN_BUTTON_RIGHT_MARGIN, 0);
                mVideoFullScreen.setLayoutParams(layoutParams);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.START_OF, R.id.tv_player_ctrl_now_on_air_full_screen_iv);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(0, 0, (int) density * SEEKBAR_TIME_LATERAL_MARGIN, 0);
                mVideoTotalTime.setLayoutParams(layoutParams);

                mVideoCtrlBar.removeView(mVideoCurTime);
                mVideoCtrlBar.removeView(mVideoFullScreen);
                mVideoCtrlBar.removeView(mVideoTotalTime);

                layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.END_OF, R.id.tv_player_ctrl_now_on_air_cur_time_tv);
                layoutParams.addRule(RelativeLayout.START_OF, R.id.tv_player_ctrl_now_on_air_total_time_tv);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(0, 0, 0, (int) density * SEEKBAR_BOTTOM_MARGIN);
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
     * NOW ON AIR レイアウト初期化.
     */
    private void initLiveLayout() {
        RelativeLayout mLiveLayout = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_now_on_air_rl);
        mPortraitLayout = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_now_on_air_portrait);
        mLandscapeLayout = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_now_on_air_landscape);
        mChannellogo = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_chanel_logo);
        mChanneltitle = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_chanel_title);
        mLiveLayout.setVisibility(VISIBLE);
    }

    /**
     * NOW ON AIR レイアウト表示.
     *
     * @param isLandscape 端末の縦横判定
     */
    private void showLiveLayout(final boolean isLandscape) {
        if (isLandscape) {
            mPortraitLayout.setVisibility(INVISIBLE);
            mLandscapeLayout.setVisibility(VISIBLE);
            setLogoAndTitle(mTxtChannellogo, mTxtChanneltitle);
        } else {
            mLandscapeLayout.setVisibility(INVISIBLE);
            if (mIsVideoBroadcast) {
                mPortraitLayout.setVisibility(VISIBLE);
            }
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
    public void enableThumbnailConnect() {
        DTVTLogger.start();
        mIsDownloadStop = false;
        if (mThumbnailProvider != null) {
            mThumbnailProvider.enableConnect();
        }
    }

    /**
     * チャンネル情報設定.
     * @param url ロゴ
     * @param title タイトル名
     */
    private void setLogoAndTitle(final String url, final String title) {
        if (!TextUtils.isEmpty(title)) {
            mChanneltitle.setText(title);
        }
        if (!TextUtils.isEmpty(url)) {
            if (mThumbnailProvider == null) {
                mThumbnailProvider = new ThumbnailProvider(mContext, ThumbnailDownloadTask.ImageSizeType.CHANNEL);
            }
            if (!mIsDownloadStop) {
                mChannellogo.setTag(url);
                Bitmap bitmap = mThumbnailProvider.getThumbnailImage(mChannellogo, url);
                if (bitmap != null) {
                    //サムネイル取得失敗時は取得失敗画像をセットする
                    mChannellogo.setImageBitmap(bitmap);
                }
            }
        } else {
            mChannellogo.setImageResource(R.mipmap.error_ch_mini);
        }
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
     * initView player.
     * @param playStartPosition 再生開始ポジション
     */
    public void initSecurePlayer(final int playStartPosition) {
        DTVTLogger.start();
        setCanPlay(false);
        mPlayerController = new SecuredMediaPlayerController(mContext, true, true, true);
        mPlayerController.setOnStateChangeListener(this);
        mPlayerController.setOnFormatChangeListener(this);
        mPlayerController.setOnPlayerEventListener(this);
        mPlayerController.setOnErrorListener(this);
        mPlayerController.setCaptionDataListener(this);
        mPlayerController.setCurrentCaption(0); // start caption.
        mReconnectStartTime = 0;
        mDmsDisconnectedTime = 0;
        boolean result = DlnaUtils.getActivationState(mContext);
        if (!result) {
            mPlayerStateListener.onErrorCallBack(PlayerErrorType.ACTIVATION);
            return;
        } else {
            String privateHomePath = DlnaUtils.getPrivateDataHomePath(mContext);
            int ret = mPlayerController.dtcpInit(privateHomePath);
            if (ret != MediaPlayerDefinitions.SP_SUCCESS) {
                mPlayerStateListener.onErrorCallBack(PlayerErrorType.ACTIVATION);
                return;
            }
        }
        mPlayerStateListener.onErrorCallBack(PlayerErrorType.INIT_SUCCESS);
        preparePlayer(playStartPosition);
        DTVTLogger.end();
    }

    /**
     * 再生準備開始.
     * @param playStartPosition 再生開始ポジション
     */
    private void preparePlayer(final int playStartPosition) {
        DTVTLogger.start();
        additionalHeaders = new HashMap<>();
        try {
            mPlayerController.setDataSource(mCurrentMediaInfo, additionalHeaders, playStartPosition);
        } catch (IOException e) {
            DTVTLogger.debug(e);
            setCanPlay(false);
            DTVTLogger.end();
            return;
        }
        mPlayerController.setScreenOnWhilePlaying(true);
        //プレイヤービュー
        initSecureVideoView();
        mSecureVideoPlayer.init(mPlayerController);
        initPlayerView();
        setCanPlay(true);
        playStart();
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
     * シークバーを先頭に戻す.
     */
    private void setProgress0() {
        DTVTLogger.start();
        mVideoSeekBar.setProgress(0);
        playButton(true);
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
     * set player event.
     */
    public void setPlayerEvent() {
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
                        if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            showLiveLayout(true);
                        } else {
                            showLiveLayout(false);
                        }
                        int orientation = getResources().getConfiguration().orientation;
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                            mSecureVideoPlayer.setBackgroundResource(R.mipmap.movie_material_mask_overlay_gradation_portrait);
                        } else {
                            mSecureVideoPlayer.setBackgroundResource(R.mipmap.movie_material_mask_overlay_gradation_landscape);
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
     * シークバーリスナーを設定.
     *
     * @param seekBar シークバー
     */
    private void setSeekBarListener(final SeekBar seekBar) {
        DTVTLogger.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                mIsCompleted = false;
                mVideoCurTime.setText(DateUtils.time2TextViewFormat(progress));
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
     * 再生パラメータ設定.
     * @param playerData 再生用のデータ
     * @return true 再生待ち　false:再生できない
     */
    private boolean setCurrentMediaInfo(final RecordedContentsDetailData playerData) {
        String url = playerData.getResUrl();
        mTxtChannellogo = playerData.getUpnpIcon();
        //setPlayerLogoThumbnail(icon);
        long size = Integer.parseInt(playerData.getSize());
        String durationStr = playerData.getDuration();
        long duration = ContentUtils.getDuration(durationStr);
        String type = playerData.getVideoType();
        if (TextUtils.isEmpty(playerData.getBitrate())) {
            playerData.setBitrate("0");
        }
        int bitRate = Integer.parseInt(playerData.getBitrate());
        mTxtChanneltitle = playerData.getTitle();
        //setTitleText(title);
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
        mIsVideoBroadcast = playerData.isIsLive();
        boolean isSupportedByteSeek = false;
        boolean isSupportedTimeSeek = false;
        boolean isAvailableConnectionStalling = false;
        boolean isRemote = playerData.isRemote();
        if (mIsVideoBroadcast) {
            isAvailableConnectionStalling = true;
        } else {
            isSupportedByteSeek = true;
            isSupportedTimeSeek = true;
            //録画番組ローカル再生
            if ((ContentsAdapter.DOWNLOAD_STATUS_COMPLETED == playerData.getDownLoadStatus())) {
                //ローカルファイルパス
                String dlFile = playerData.getDlFileFullPath();
                File file = new File(dlFile);
                if (!file.exists()) {
                    DTVTLogger.debug(file  + " not exists");
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
                mTxtChanneltitle,                //メディアのタイトル
                contentFormat                    //DIDLのres protocolInfoの3番目のフィールド
        );
        mTotalDuration = mCurrentMediaInfo.getDurationMs();
        DTVTLogger.end();
        return true;
    }

    /**
     * 再生パラメータ設定.
     * @param playerData 再生用のデータ
     * @return 再生の初期化（true:成功、false:失敗）
     */
    public boolean initMediaInfo(final RecordedContentsDetailData playerData) {
        boolean result = setCurrentMediaInfo(playerData);
        if (result) {
            setPlayerTouchEvent();
        }
        mReconnectStartTime = 0;
        mDmsDisconnectedTime = 0;
        return result;
    }

    /**
     * タッチイベントの初期化.
     */
    private void setPlayerTouchEvent() {
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
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
    }

    /**
     * Playを停止.
     */
    public void onPause() {
        //外部出力制御
        if (mExternalDisplayHelper != null) {
            mExternalDisplayHelper.onPause();
        }
        if (mPlayerController != null) {
            setCanPlay(false);
            mPlayerController.setCaptionDataListener(null);
            mPlayerController.release();
            mPlayerController = null;
        }
        showPlayingProgress(false);
        stopThumbnailConnect();
    }

    /**
     * 外部出力制御を停止.
     */
    public void onStop() {
        if (mExternalDisplayHelper != null) {
            mExternalDisplayHelper.onStop();
        }
    }

    /**
     * 外部出力制御を消す.
     */
    public void onDestory() {
        mExternalDisplayHelper = null;
    }

    /**
     * 前回の再生位置取得.
     *
     * @return 前回の再生位置
     */
    public int getPlayStartPosition() {
        return mPlayStartPosition;
    }

    /**
     * 前回の再生位置設定.
     *
     * @param playStartPosition 前回の再生位置
     */
    public void setPlayStartPosition(final int playStartPosition) {
        this.mPlayStartPosition = playStartPosition;
    }


}
