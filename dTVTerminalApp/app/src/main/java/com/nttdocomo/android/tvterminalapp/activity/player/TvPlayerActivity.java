/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.model.player.MediaVideoInfo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TvPlayerActivity extends BaseActivity implements View.OnClickListener, MediaPlayerController.OnStateChangeListener, MediaPlayerController.OnFormatChangeListener, MediaPlayerController.OnPlayerEventListener, MediaPlayerController.OnErrorListener, MediaPlayerController.OnCaptionDataListener {

    private static final int REFRESH_TV_VIEW = 1;
    private static final long HIDE_IN_3_SECOND = 3*1000;
    private RelativeLayout mPlayerViewLayout;
    private static final int REFRESH_VIDEO_VIEW = 0;
    private static final int NOW_ON_AIR_MODE = 1;
    private static final int VIDEO_RECORDING_MODE = 2;
    private int curMode = VIDEO_RECORDING_MODE;
    private RelativeLayout mTvCtrlView;
    private RelativeLayout mRecordCtrlView;
    private ImageView mTvBack;
    private ImageView mTvForward;
    private ImageView mTvReplay;
    private ImageView mTvFullScreen;
    private TextView mTvRapid;
    private SeekBar mTvSeekBar;
    private FrameLayout mVideoPlayPause;
    private TextView mVideoCurTime;
    private ImageView mVideoFullScreen;
    private TextView mVideoRapid;
    private TextView mVideoTotalTime;
    private SeekBar mVideoSeekBar;
    private SecureVideoView mSecureVideoPlayer;
    private SecuredMediaPlayerController mPlayerController;
    private boolean mCanPlay=false;
    private MediaVideoInfo mCurrentMediaInfo;
    private int mActivationTimes=0;

    private final static int ACTIVATION_REQUEST_CODE = 1;
    private final static int ACTIVATION_REQUEST_MAX_CNT= 3;
    private RelativeLayout mVideoCtrlRootView;
    private TextView mVideoRewind10;
    private ImageView mVideoRewind;
    private TextView mVideoFast30;
    private ImageView mVideoFast;
    private RelativeLayout mVideoCtrlBar;
    private Handler mCtrlHandler = new Handler(Looper.getMainLooper());
    private GestureDetector mGestureDetector;
    private int mScreenWidth;
    private TextView mTvNow;
    private Runnable mHideCtrlViewThread = new Runnable() {
        @Override
        public void run() {
            if(getCurMode() == NOW_ON_AIR_MODE){
                hideTvCtrlView(View.INVISIBLE);
            }else if(getCurMode() == VIDEO_RECORDING_MODE){
                hideVideoCtrlView(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_main_layout);
        mSecureVideoPlayer = findViewById(R.id.tv_player_main_layout_player_vv);
        mScreenWidth = getWidthDensity();
        initDatas();
        setCurrentMediaInfo();
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if(e.getY()>mRecordCtrlView.getHeight()/3
                        &&e.getY()<mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight()/3){
                    if(e.getX()< mScreenWidth /2-mVideoPlayPause.getWidth()/2
                            &&e.getX()>mScreenWidth/6){//10秒戻し
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= 10*1000;
                        mPlayerController.seekTo(pos);
                        Toast.makeText(TvPlayerActivity.this,"←10秒",Toast.LENGTH_SHORT).show();
                    }
                    if(e.getX()>mScreenWidth /2+mVideoPlayPause.getWidth()/2
                            &&e.getX()<mScreenWidth-mScreenWidth/6){//30秒送り
                        int pos = mPlayerController.getCurrentPosition();
                        pos += 30*1000;
                        mPlayerController.seekTo(pos);
                        Toast.makeText(TvPlayerActivity.this,"30→",Toast.LENGTH_SHORT).show();
                    }
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getY()>mRecordCtrlView.getHeight()/3
                        &&e2.getY()>mRecordCtrlView.getHeight()/3
                        &&e2.getY()<mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight()/3
                        &&e1.getY()<mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight()/3){
                    if(e1.getX()>e2.getX() && e1.getX()<mScreenWidth /2-mVideoPlayPause.getWidth()/2){
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= 10*1000;
                        mPlayerController.seekTo(pos);
                        Toast.makeText(TvPlayerActivity.this,"←10秒",Toast.LENGTH_SHORT).show();
                    }else if(e1.getX()<e2.getX() && e1.getX()>mScreenWidth /2+mVideoPlayPause.getWidth()/2){
                        int pos = mPlayerController.getCurrentPosition();
                        pos += 30*1000;
                        mPlayerController.seekTo(pos);
                        Toast.makeText(TvPlayerActivity.this,"30→",Toast.LENGTH_SHORT).show();
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    private void initDatas() {
        mActivationTimes=0;
    }

    private void setCanPlay(boolean state){
        synchronized (this){
            mCanPlay= state;
        }
    }

    private void playStart(){
        synchronized (this) {
            if(mCanPlay){
                playButton();
                mPlayerController.start();
            }
        }
    }

    private void playButton(){
        if(null==mVideoPlayPause){
            return;
        }
        mVideoPlayPause.getChildAt(0).setVisibility(View.GONE);
        mVideoPlayPause.getChildAt(1).setVisibility(View.VISIBLE);
    }

    private void pauseButton(){
        if(null==mVideoPlayPause){
            return;
        }
        mVideoPlayPause.getChildAt(0).setVisibility(View.VISIBLE);
        mVideoPlayPause.getChildAt(1).setVisibility(View.GONE);
    }

    private void playPause(){
        synchronized (this) {
            if(mCanPlay){
                pauseButton();
                mPlayerController.pause();
            }
        }
    }

    private void initSecureplayer() {
        setCanPlay(false);

        mPlayerController = new SecuredMediaPlayerController(this, true,  true , true);
        mPlayerController.setOnStateChangeListener(this);
        mPlayerController.setOnFormatChangeListener(this);
        mPlayerController.setOnPlayerEventListener(this);
        mPlayerController.setOnErrorListener(this);
        mPlayerController.setWakeMode(this, PowerManager.FULL_WAKE_LOCK);

        mPlayerController.setCaptionDataListener(this);
        {
            mPlayerController.setCurrentCaption(0); // start caption.
        }

        boolean ret = isActivited();
        if (!ret) {
            DTVTLogger.debug("TvPlayerActivity::initSecureplayer(), return false"); //SP_SECUREPLAYER_NEED_ACTIVATION_ERROR = 1001;
            return;
        }

        preparePlayer();
    }

    private void preparePlayer() {
        final Map<String, String> additionalHeaders = new HashMap<>();
        try {
            mPlayerController.setDataSource(mCurrentMediaInfo, additionalHeaders, 0);
        } catch (IOException e) {
            e.printStackTrace();
            setCanPlay(false);
            return;
        }
        mPlayerController.setScreenOnWhilePlaying(true);
        mSecureVideoPlayer.init(mPlayerController);
        initView();
        setCanPlay(true);
        playStart();
    }


    private boolean isActivited(){
        String path=getPrivateDataHome();
        File dir= new File(path);
        if(!dir.exists()){
            boolean ok=dir.mkdir();
            if(!ok){
                DTVTLogger.debug("TvPlayerActivity::isActivited(), Make dir " + path + " failed");
                return false;
            }
        }
        int ret = mPlayerController.dtcpInit(path);
        if (ret == MediaPlayerDefinitions.SP_SUCCESS) {
            return true;
        } else {
            activate();
        }
        return false;
    }

    private void activate(){
        if(mActivationTimes>=ACTIVATION_REQUEST_MAX_CNT){
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(TvPlayerActivity.this.getPackageName(),
                ActivationClientDefinition.activationActivity);
        intent.putExtra(ActivationClientDefinition.EXTRA_DEVICE_KEY,
                EnvironmentUtil.getPrivateDataHome(TvPlayerActivity.this,
                        EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER));
        startActivityForResult(intent,ACTIVATION_REQUEST_CODE);
        ++mActivationTimes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVATION_REQUEST_CODE) {
            DTVTLogger.debug("TvPlayerActivity::onActivityResult(), activation resultCode = " + resultCode);
            if(Activity.RESULT_OK == resultCode){
                String path=getPrivateDataHome();
                int ret= mPlayerController.dtcpInit(path);
                if (ret != MediaPlayerDefinitions.SP_SUCCESS) {
                    Log.d("", "SecureMediaPlayerController init failed");
                    DTVTLogger.debug("TvPlayerActivity::onActivityResult(), SecureMediaPlayerController init failed");
                    Toast.makeText(getApplicationContext(), "DLNA Player初期化失敗しました",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    preparePlayer();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 機能：プレバイトデータフォルダを戻す
     * @return プレバイトデータフォルダ
     */
    private String getPrivateDataHome() {
        return EnvironmentUtil.getPrivateDataHome(this,
                EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
    }

    private void setCurrentMediaInfo() {
        String url = "http://192.168.11.12:5001/get/364/20131211_103918-1280x720p60.ts";
        Uri uri = url == null ? null: Uri.parse(url);
        mCurrentMediaInfo = new MediaVideoInfo(
                uri, //uri,   dv.mp4 by camera
                "video/mp4", //extras.getString(Definitions.RESOURCE_MIMETYPE),
                94000000, //extras.getLong(Definitions.SIZE),
                41000, //extras.getLong(Definitions.DURATION),
                0, //extras.getInt(Definitions.BITRATE),
                false, //extras.getBoolean(Definitions.IS_SUPPORTED_BYTE_SEEK),
                false, //extras.getBoolean(Definitions.IS_SUPPORTED_TIME_SEEK),
                false, //extras.getBoolean(Definitions.IS_AVAILABLE_CONNECTION_STALLING),
                false, //extras.getBoolean(Definitions.IS_LIVE_MODE),
                false, //extras.getBoolean(Definitions.IS_REMOTE),
                "title",  //extras.getString(Definitions.TITLE),
                "contentFormat" //extras.getString(Definitions.CONTENT_FORMAT)
        );
    }

    private void initView() {
        mPlayerViewLayout = findViewById(R.id.tv_player_main_layout_player_rl);
        RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) mSecureVideoPlayer.getLayoutParams();
        playerParams.height = getHeightDensity() * 4 / 11;
        mSecureVideoPlayer.setLayoutParams(playerParams);
        if(getCurMode() == NOW_ON_AIR_MODE){//リニア放送中
            mTvCtrlView = (RelativeLayout) LayoutInflater.from(this)
                    .inflate(R.layout.tv_player_ctrl_now_on_air, null, false);
            mTvNow = mTvCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_now_on_air_tv);
            mTvBack = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_back_iv);
            mTvForward = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_forward_iv);
            mTvReplay = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_replay_iv);
            mTvFullScreen = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv);
            mTvRapid = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_rapid_tv);
            mTvSeekBar = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
            mTvFullScreen.setOnClickListener(this);
            mTvReplay.setOnClickListener(this);
            mTvCtrlView.setLayoutParams(playerParams);
            mPlayerViewLayout.addView(mTvCtrlView);
            //初期化の時点から、handlerにmsgを送る
            viewRefresher.sendEmptyMessage(REFRESH_TV_VIEW);
            hideTvCtrlView(View.INVISIBLE);
        }else if(getCurMode() == VIDEO_RECORDING_MODE){//録画
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
            mVideoRapid = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_rapid_tv);
            mVideoTotalTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_total_time_tv);
            mVideoSeekBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
            mVideoPlayPause.setOnClickListener(this);
            mVideoFullScreen.setOnClickListener(this);
            mVideoRapid.setOnClickListener(this);
            mVideoCtrlRootView.setOnClickListener(this);
            setVideoSeekBarListener(mVideoSeekBar);
            mRecordCtrlView.setLayoutParams(playerParams);
            mPlayerViewLayout.addView(mRecordCtrlView);
            //初期化の時点から、handlerにmsgを送る
            viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
            hideVideoCtrlView(View.INVISIBLE);
        }
        pauseButton();
    }

    private void setVideoSeekBarListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                time2TextViewFormat(mVideoCurTime, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                viewRefresher.removeMessages(REFRESH_VIDEO_VIEW);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mPlayerController.seekTo(progress);
                viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSecureplayer();
        setPlayerEvent();
        // TODO: 2017/11/22 横縦処理
    }

    private void setPlayerEvent() {
        if(getCurMode() == VIDEO_RECORDING_MODE){
            mRecordCtrlView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(mVideoPlayPause.getVisibility() == View.VISIBLE){
                        mGestureDetector.onTouchEvent(motionEvent);
                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        if(mVideoPlayPause.getVisibility() == View.VISIBLE){
                            //hideVideoCtrlView(View.INVISIBLE);
                        }else {
                            mVideoPlayPause.setVisibility(View.VISIBLE);
                            mVideoRewind10.setVisibility(View.VISIBLE);
                            mVideoRewind.setVisibility(View.VISIBLE);
                            mVideoFast30.setVisibility(View.VISIBLE);
                            mVideoFast.setVisibility(View.VISIBLE);
                            mVideoCtrlBar.setVisibility(View.VISIBLE);
                            //setPlayEvent();
                        }
                        hideCtrlViewAfterOperate();
                    }
                    return true;
                }
            });
        }else if(getCurMode() == NOW_ON_AIR_MODE){
            mTvCtrlView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(mTvNow.getVisibility() == View.VISIBLE){
                        //チャンネル遷移GestureDetector
                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        if(mTvNow.getVisibility() == View.VISIBLE){
                            //hideVideoCtrlView(View.INVISIBLE);
                        }else {
                            mTvNow.setVisibility(View.VISIBLE);
                            mTvBack.setVisibility(View.VISIBLE);
                            mTvForward.setVisibility(View.VISIBLE);
                            mTvReplay.setVisibility(View.VISIBLE);
                            mTvRapid.setVisibility(View.VISIBLE);
                            mTvSeekBar.setVisibility(View.VISIBLE);
                            mTvFullScreen.setVisibility(View.VISIBLE);
                            //setPlayEvent();
                        }
                        hideCtrlViewAfterOperate();
                    }
                    return true;
                }
            });
        }
    }

    private void hideCtrlViewAfterOperate() {
        if(mCtrlHandler != null){
            mCtrlHandler.removeCallbacks(mHideCtrlViewThread);
        }
        mCtrlHandler.postDelayed(mHideCtrlViewThread,HIDE_IN_3_SECOND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=mPlayerController){
            mPlayerController.setCaptionDataListener(null);
            mPlayerController.release();
            mPlayerController = null;
        }
        // TODO: 2017/11/22 横縦処理
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横処理
        } else {
            //縦処理
        }
    }


    /**
     * おすすめへの遷移
     *
     * @param view
     */
    public void recommendButton(View view) {
        startActivity(RecommendPlayerActivity.class, null);
    }

    @Override
    public void onClick(View v) {
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
                Toast.makeText(this, "フルスクリーンに変更されています", Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
            case R.id.tv_player_ctrl_now_on_air_replay_iv:
                Toast.makeText(this, "タップで頭出し再生", Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
            case R.id.tv_player_ctrl_now_on_air_rapid_tv:
                Toast.makeText(this,"タップで倍速で再生",Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
            default:
                break;
        }
    }

    private void hideVideoCtrlView(int invisible) {
        mVideoPlayPause.setVisibility(invisible);
        mVideoRewind10.setVisibility(invisible);
        mVideoRewind.setVisibility(invisible);
        mVideoFast30.setVisibility(invisible);
        mVideoFast.setVisibility(invisible);
        mVideoCtrlBar.setVisibility(invisible);
    }

    private void hideTvCtrlView(int invisible) {
        mTvNow.setVisibility(invisible);
        mTvBack.setVisibility(invisible);
        mTvForward.setVisibility(invisible);
        mTvReplay.setVisibility(invisible);
        mTvRapid.setVisibility(invisible);
        mTvSeekBar.setVisibility(invisible);
        mTvFullScreen.setVisibility(invisible);
    }

    /**
     * ミリ秒をtextViewで表示形(01,05...)に変更
     *
     * @param textView
     * @param millisecond
     */
    public void time2TextViewFormat(TextView textView, int millisecond) {
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
    }

    /*UIを更新するハンドラー*/
    private Handler viewRefresher = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(null==mPlayerController){
                return;
            }

            if(msg.what == REFRESH_TV_VIEW){//NOW ON AIR
                int currentPosition = mPlayerController.getCurrentPosition();
                int totalDur = mPlayerController.getDuration();
                //time2TextViewFormat(mCurTime, currentPosition);
                //time2TextViewFormat(mTotalDur, totalDur);
                mTvSeekBar.setMax(totalDur);
                mTvSeekBar.setProgress(currentPosition);
                viewRefresher.sendEmptyMessageDelayed(REFRESH_TV_VIEW, 500);
            }
            if (msg.what == REFRESH_VIDEO_VIEW) {//録画
                int currentPosition = mPlayerController.getCurrentPosition();
                int totalDur = mPlayerController.getDuration();
                time2TextViewFormat(mVideoCurTime, currentPosition);
                time2TextViewFormat(mVideoTotalTime, totalDur);
                mVideoSeekBar.setMax(totalDur);
                mVideoSeekBar.setProgress(currentPosition);
                //録画コンテンツを終端まで再生した後は、シークバーを先頭に戻し、先頭で一時停止状態とする
                if (currentPosition == totalDur) {
                    mVideoSeekBar.setProgress(0);
                }
                viewRefresher.sendEmptyMessageDelayed(REFRESH_VIDEO_VIEW, 500);
            }
        }
    };

    /**
     * 現在時点のモードを取得する
     * @return
     */
    public int getCurMode() {
        return curMode;
    }

    /**
     * 外部でモード設定
     * @param curMode
     * NOW_ON_AIR_MODE = 1;
     * VIDEO_RECORDING_MODE = 2;
     */
    public void setCurMode(int curMode){
        this.curMode = curMode;
    }

    @Override
    public void onStateChanged(MediaPlayerController mediaPlayerController, int i) {

    }

    @Override
    public void onFormatChanged(MediaPlayerController mediaPlayerController) {

    }

    @Override
    public void onPlayerEvent(MediaPlayerController mediaPlayerController, int event, long arg) {
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
                Toast.makeText(getApplicationContext(), "PE_START_AUTHENTICATION",
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayerDefinitions.PE_START_BUFFERING:
                Toast.makeText(getApplicationContext(), "PE_START_BUFFERING",
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayerDefinitions.PE_START_RENDERING:
                Toast.makeText(getApplicationContext(), "PE_START_RENDERING",
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayerDefinitions.PE_FIRST_FRAME_RENDERED:
                Toast.makeText(getApplicationContext(), "PE_FIRST_FRAME_RENDERED",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onError(MediaPlayerController mediaPlayerController, int i, long l) {

    }

    @Override
    public void onCaptionData(MediaPlayerController mediaPlayerController, CaptionDrawCommands captionDrawCommands) {

    }

    @Override
    public void onSuperData(MediaPlayerController mediaPlayerController, CaptionDrawCommands captionDrawCommands) {

    }
}
