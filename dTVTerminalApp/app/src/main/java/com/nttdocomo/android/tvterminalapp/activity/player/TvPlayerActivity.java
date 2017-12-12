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
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.model.player.MediaVideoInfo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TvPlayerActivity extends BaseActivity implements View.OnClickListener, MediaPlayerController.OnStateChangeListener, MediaPlayerController.OnFormatChangeListener, MediaPlayerController.OnPlayerEventListener, MediaPlayerController.OnErrorListener, MediaPlayerController.OnCaptionDataListener {

    private static final int REFRESH_TV_VIEW = 1;
    private static final long HIDE_IN_3_SECOND = 3*1000;
    private static final int REFRESH_VIDEO_VIEW = 0;
    private static final int NOW_ON_AIR_MODE = 1;
    private static final int VIDEO_RECORDING_MODE = 2;
    private final static int ACTIVATION_REQUEST_CODE = 1;
    private final static int ACTIVATION_REQUEST_MAX_CNT= 3;
    private static final int REWIND_SECOND = 10*1000;
    private static final int FAST_SECOND = 30*1000;
    private RelativeLayout mPlayerViewLayout;
    private int curMode = VIDEO_RECORDING_MODE;
    private RelativeLayout mTvCtrlView;
    private RelativeLayout mRecordCtrlView;
    private ImageView mTvBack;
    private ImageView mTvForward;
    private ImageView mTvReplay;
    private ImageView mTvFullScreen;
    //private TextView mTvRapid;
    private SeekBar mTvSeekBar;
    private FrameLayout mVideoPlayPause;
    private TextView mVideoCurTime;
    private ImageView mVideoFullScreen;
    //private TextView mVideoRapid;
    private TextView mVideoTotalTime;
    private SeekBar mVideoSeekBar;
    private SecureVideoView mSecureVideoPlayer;
    private SecuredMediaPlayerController mPlayerController;
    private boolean mCanPlay=false;
    private MediaVideoInfo mCurrentMediaInfo;
    private int mActivationTimes=0;
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
    private boolean isHideOperate = true;
    private boolean mIsOncreateOk=false;
    private Runnable mHideCtrlViewThread = new Runnable() {

        /**
         * run
         */
        @Override
        public void run() {
            DTVTLogger.start();
            if(getCurMode() == NOW_ON_AIR_MODE){
                hideTvCtrlView(View.INVISIBLE);
            }else if(getCurMode() == VIDEO_RECORDING_MODE){
                hideVideoCtrlView(View.INVISIBLE);
            }
            DTVTLogger.end();
        }
    };

    /**
     * UIを更新するハンドラー
     * */
    private Handler viewRefresher = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DTVTLogger.start();
            super.handleMessage(msg);
            if(null==mPlayerController){
                DTVTLogger.end();
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
                    setProgress0();
                }
                viewRefresher.sendEmptyMessageDelayed(REFRESH_VIDEO_VIEW, 500);
            }
            DTVTLogger.end();
        }
    };

    private void setProgress0(){
        DTVTLogger.start();
        mVideoSeekBar.setProgress(0);
        playButton();
        DTVTLogger.end();
    }

    /**
     * creator
     * @param savedInstanceState status
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DTVTLogger.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_main_layout);
        mSecureVideoPlayer = findViewById(R.id.tv_player_main_layout_player_vv);
        mScreenWidth = getWidthDensity();
        initDatas();
        boolean ok=setCurrentMediaInfo();
        if(!ok){
            errorExit();
            mIsOncreateOk=false;
            finish();
            DTVTLogger.end();
            return;
        }
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                DTVTLogger.start();
                if(e.getY()>mRecordCtrlView.getHeight()/3
                        &&e.getY()<mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight()/3){
                    if(e.getX()< mScreenWidth /2-mVideoPlayPause.getWidth()/2
                            &&e.getX()>mScreenWidth/6){//10秒戻し
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= REWIND_SECOND;
                        if(pos<0){
                            pos=0;
                            setProgress0();
                        }
                        mPlayerController.seekTo(pos);
                        isHideOperate = false;
                        showMessage("←10");
                    }
                    if(e.getX()>mScreenWidth /2+mVideoPlayPause.getWidth()/2
                            &&e.getX()<mScreenWidth-mScreenWidth/6){//30秒送り
                        int pos = mPlayerController.getCurrentPosition();
                        pos += FAST_SECOND;
                        //pos = pos > mPlayerController.getDuration() ? mPlayerController.getDuration() : pos;
                        int allDu= mPlayerController.getDuration();
                        if(pos >= allDu){
                            setProgress0();
                            pos= 0;
                        }
                        mPlayerController.seekTo(pos);
                        isHideOperate = false;
                        showMessage("30→");
                    }
                }
                DTVTLogger.end();
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                DTVTLogger.start();
                if(e1.getY()>mRecordCtrlView.getHeight()/3
                        &&e2.getY()>mRecordCtrlView.getHeight()/3
                        &&e2.getY()<mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight()/3
                        &&e1.getY()<mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight()/3){
                    if(e1.getX()>e2.getX() && e1.getX()<mScreenWidth /2-mVideoPlayPause.getWidth()/2){
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= REWIND_SECOND;
                        pos = pos < 0 ? 0 : pos;
                        mPlayerController.seekTo(pos);
                        isHideOperate = false;
                        showMessage("←10");
                    }else if(e1.getX()<e2.getX() && e1.getX()>mScreenWidth /2+mVideoPlayPause.getWidth()/2){
                        int pos = mPlayerController.getCurrentPosition();
                        pos += FAST_SECOND;
                        pos = pos > mPlayerController.getDuration() ? mPlayerController.getDuration() : pos;
                        mPlayerController.seekTo(pos);
                        isHideOperate = false;
                        showMessage("30→");
                    }
                }
                DTVTLogger.end();
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        mIsOncreateOk=true;
        DTVTLogger.end();
    }

    /**
     * init function
     */
    private void initDatas() {
        DTVTLogger.start();
        mActivationTimes=0;
        DTVTLogger.end();
    }

    /**
     * set can play
     * @param state state
     */
    private void setCanPlay(boolean state){
        DTVTLogger.start();
        synchronized (this){
            mCanPlay= state;
        }
        DTVTLogger.end();
    }

    /**
     * start playing
     */
    private void playStart(){
        DTVTLogger.start();
        synchronized (this) {
            if(mCanPlay){
                playButton();
                mPlayerController.start();
            }
        }
        DTVTLogger.end();
    }

    /**
     * play button function
     */
    private void playButton(){
        DTVTLogger.start();
        if(null==mVideoPlayPause){
            return;
        }
        View child0= mVideoPlayPause.getChildAt(0);
        View child1= mVideoPlayPause.getChildAt(1);
        if(null!=child0){
            child0.setVisibility(View.GONE);
        }
        if(null!=child1){
            child1.setVisibility(View.VISIBLE);
        }
        DTVTLogger.end();
    }

    /**
     * pause button function
     */
    private void pauseButton(){
        DTVTLogger.start();
        if(null==mVideoPlayPause){
            return;
        }
        mVideoPlayPause.getChildAt(0).setVisibility(View.VISIBLE);
        mVideoPlayPause.getChildAt(1).setVisibility(View.GONE);
        DTVTLogger.end();
    }

    /**
     * palying pause
     */
    private void playPause(){
        DTVTLogger.start();
        synchronized (this) {
            if(mCanPlay){
                pauseButton();
                mPlayerController.pause();
            }
        }
        DTVTLogger.end();
    }

    /**
     * init player
     */
    private void initSecureplayer() {
        DTVTLogger.start();
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
            DTVTLogger.end();
            return;
        }

        preparePlayer();
        DTVTLogger.end();
    }

    /**
     * prepair player
     */
    private void preparePlayer() {
        DTVTLogger.start();
        final Map<String, String> additionalHeaders = new HashMap<>();
        try {
            mPlayerController.setDataSource(mCurrentMediaInfo, additionalHeaders, 0);
        } catch (IOException e) {
            e.printStackTrace();
            setCanPlay(false);
            DTVTLogger.end();
            return;
        }
        mPlayerController.setScreenOnWhilePlaying(true);
        mSecureVideoPlayer.init(mPlayerController);
        initView();
        setCanPlay(true);
        playStart();
        DTVTLogger.end();
    }

    /**
     * is activated
     * @return if it is activated
     */
    private boolean isActivited(){
        DTVTLogger.start();
        String path=getPrivateDataHome();
        File dir= new File(path);
        if(!dir.exists()){
            boolean ok=dir.mkdir();
            if(!ok){
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
     * activate
     */
    private void activate(){
        DTVTLogger.start();
        if(mActivationTimes>=ACTIVATION_REQUEST_MAX_CNT){
            DTVTLogger.end();
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
        DTVTLogger.end();
    }

    /**
     * activity result event function
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DTVTLogger.start();
        if (requestCode == ACTIVATION_REQUEST_CODE) {
            DTVTLogger.debug("TvPlayerActivity::onActivityResult(), activation resultCode = " + resultCode);
            if(Activity.RESULT_OK == resultCode){
                String path=getPrivateDataHome();
                int ret= mPlayerController.dtcpInit(path);
                if (ret != MediaPlayerDefinitions.SP_SUCCESS) {
                    Log.d("", "SecureMediaPlayerController init failed");
                    DTVTLogger.debug("TvPlayerActivity::onActivityResult(), SecureMediaPlayerController init failed");
                    showMessage("DLNA Player初期化失敗しました");
                    super.onActivityResult(requestCode, resultCode, data);
                    finish();
                    DTVTLogger.end();
                    return;
                } else {
                    preparePlayer();
                    showMessage("DLNAをActivateしまして、プレイヤーをもう一回実行してください。");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
        DTVTLogger.end();
    }

    /**
     * 機能：プレバイトデータフォルダを戻す
     * @return プレバイトデータフォルダ
     */
    private String getPrivateDataHome() {
        DTVTLogger.start();
        String ret= EnvironmentUtil.getPrivateDataHome(this,
                EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
        DTVTLogger.end();
        return ret;
    }

    /**
     * set current player info
     * @return whether succeedered
     */
    private boolean setCurrentMediaInfo() {
        DTVTLogger.start();
        Intent intent = getIntent();
        RecordedContentsDetailData datas = intent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
        if(null==datas){
            DTVTLogger.end();
            return false;
        }
        String url = datas.getResUrl();
        if(null==url || 0==url.length()){
            showMessage("プレイヤーは「null」コンテンツを再生できない");
            DTVTLogger.end();
            return false;
        }
        long size =Integer.parseInt(datas.getSize());

        String durationStr=datas.getDuration();
        long duration =getDuration(durationStr);

        int bitRate = Integer.parseInt(datas.getBitrate());
        if(0==size){
            size= duration*bitRate;
        }

        String title= datas.getTitle();

        //本番ソース begin
        Uri uri = Uri.parse(url);
        mCurrentMediaInfo = new MediaVideoInfo(
                uri,                //uri
                "video/mp4",      //RESOURCE_MIMETYPE
                size,               //SIZE
                duration,           //DURATION
                bitRate,            //BITRATE
                true,               //IS_SUPPORTED_BYTE_SEEK
                true,               //IS_SUPPORTED_TIME_SEEK
                true,               //IS_AVAILABLE_CONNECTION_STALLING
                true,               //IS_LIVE_MODE
                true,               //IS_REMOTE
                title,           //TITLE
                "contentFormat"   //CONTENT_FORMAT
        );
        //本番ソース end
        //test being
//        Uri uri = Uri.parse("http://192.168.11.5:58890/web/video/pvr?id=15124802860000000001&quality=mobile");
//        mCurrentMediaInfo = new MediaVideoInfo(
//                uri,                //uri
//                "application/x-dtcp1",      //RESOURCE_MIMETYPE
//                size,               //SIZE
//                1312000,           //DURATION
//                0,            //BITRATE
//                true,               //IS_SUPPORTED_BYTE_SEEK
//                true,               //IS_SUPPORTED_TIME_SEEK
//                true,               //IS_AVAILABLE_CONNECTION_STALLING
//                true,               //IS_LIVE_MODE
//                true,               //IS_REMOTE
//                title,           //TITLE
//                "contentFormat"   //CONTENT_FORMAT
//        );
        //test end
        DTVTLogger.end();
        return true;
    }

    /**
     * 機能：「duration="0:00:42.000"」/「duration="0:00:42"」からmsへ変換
     * @param durationStr durationStr
     * @return duration in ms
     */
    private long getDuration(final String durationStr){
        DTVTLogger.start();
        long ret=0;
        boolean ok=true;

        String[] strs1=durationStr.split(":");
        String[] strs2;
        if(3== strs1.length){
            try{
                ret = 60 * 60 * 1000 * Integer.parseInt(strs1[0]) + 60 * 1000 * Integer.parseInt(strs1[1]);

                String ss= strs1[2];
                int i= ss.indexOf('.');
                if(i<=0){
                    ret += 1000*Integer.parseInt(ss);
                } else {
                    String ss1 = ss.substring(0, i);
                    String ss2 = ss.substring(i+1, ss.length());
                    ret += 1000*Integer.parseInt(ss1);

                    try{
                        ret += Integer.parseInt(ss2);
                    }catch (Exception e2){
                        DTVTLogger.debug("TvPlayerActivity::getDuration(), skip ms");
                    }
                }
            }catch (Exception e){
                ok=false;
            }
        }

        if(!ok){
            ret=0;
        }

        DTVTLogger.end();
        return ret;
    }

    /**
     * init view
     */
    private void initView() {
        DTVTLogger.start();
        mPlayerViewLayout = findViewById(R.id.tv_player_main_layout_player_rl);
        RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) mSecureVideoPlayer.getLayoutParams();
        playerParams.height = getHeightDensity() * 4 / 11;
        mSecureVideoPlayer.setLayoutParams(playerParams);
        if(getCurMode() == NOW_ON_AIR_MODE){//リニア放送中
            initViewOnAir();
//            mTvCtrlView = (RelativeLayout) LayoutInflater.from(this)
//                    .inflate(R.layout.tv_player_ctrl_now_on_air, null, false);
//            mTvNow = mTvCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_now_on_air_tv);
//            mTvBack = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_back_iv);
//            mTvForward = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_forward_iv);
//            mTvReplay = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_replay_iv);
//            mTvFullScreen = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv);
//            //mTvRapid = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_rapid_tv);
//            mTvSeekBar = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
//            mTvFullScreen.setOnClickListener(this);
//            mTvReplay.setOnClickListener(this);
//            mTvBack.setOnClickListener(this);
//            mTvForward.setOnClickListener(this);
//            mTvCtrlView.setLayoutParams(playerParams);
//            mPlayerViewLayout.addView(mTvCtrlView);
//            //初期化の時点から、handlerにmsgを送る
//            viewRefresher.sendEmptyMessage(REFRESH_TV_VIEW);
//            hideTvCtrlView(View.INVISIBLE);
        }else if(getCurMode() == VIDEO_RECORDING_MODE){//録画
            initViewVideo();
//            mRecordCtrlView = (RelativeLayout) LayoutInflater.from(this)
//                    .inflate(R.layout.tv_player_ctrl_video_record, null, false);
//            mVideoPlayPause = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_player_pause_fl);
//            mVideoCtrlRootView = mRecordCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_video_root);
//            mVideoRewind10 = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_10_tv);
//            mVideoRewind = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_rewind_iv);
//            mVideoFast30 = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_30_tv);
//            mVideoFast = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_fast_iv);
//            mVideoCtrlBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_control_bar_iv);
//            mVideoCurTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_cur_time_tv);
//            mVideoFullScreen = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv);
//            //mVideoRapid = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_rapid_tv);
//            mVideoTotalTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_total_time_tv);
//            mVideoSeekBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
//            mVideoPlayPause.setOnClickListener(this);
//            mVideoFullScreen.setOnClickListener(this);
//            //mVideoRapid.setOnClickListener(this);
//            mVideoCtrlRootView.setOnClickListener(this);
//            setVideoSeekBarListener(mVideoSeekBar);
//            mRecordCtrlView.setLayoutParams(playerParams);
//            mPlayerViewLayout.addView(mRecordCtrlView);
//            //初期化の時点から、handlerにmsgを送る
//            viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
//            hideVideoCtrlView(View.INVISIBLE);
        }
        pauseButton();
        DTVTLogger.end();
    }

    private void initViewOnAir(){
        mPlayerViewLayout = findViewById(R.id.tv_player_main_layout_player_rl);
        RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) mSecureVideoPlayer.getLayoutParams();
        playerParams.height = getHeightDensity() * 4 / 11;
        mSecureVideoPlayer.setLayoutParams(playerParams);
        mTvCtrlView = (RelativeLayout) LayoutInflater.from(this)
                .inflate(R.layout.tv_player_ctrl_now_on_air, null, false);
        mTvNow = mTvCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_now_on_air_tv);
        mTvBack = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_back_iv);
        mTvForward = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_forward_iv);
        mTvReplay = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_replay_iv);
        mTvFullScreen = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv);
        //mTvRapid = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_rapid_tv);
        mTvSeekBar = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
        mTvFullScreen.setOnClickListener(this);
        mTvReplay.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
        mTvForward.setOnClickListener(this);
        mTvCtrlView.setLayoutParams(playerParams);
        mPlayerViewLayout.addView(mTvCtrlView);
        //初期化の時点から、handlerにmsgを送る
        viewRefresher.sendEmptyMessage(REFRESH_TV_VIEW);
        hideTvCtrlView(View.INVISIBLE);
    }

    private void initViewVideo(){
        mPlayerViewLayout = findViewById(R.id.tv_player_main_layout_player_rl);
        RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) mSecureVideoPlayer.getLayoutParams();
        playerParams.height = getHeightDensity() * 4 / 11;
        mSecureVideoPlayer.setLayoutParams(playerParams);
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
        //mVideoRapid = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_rapid_tv);
        mVideoTotalTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_total_time_tv);
        mVideoSeekBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
        mVideoPlayPause.setOnClickListener(this);
        mVideoFullScreen.setOnClickListener(this);
        //mVideoRapid.setOnClickListener(this);
        mVideoCtrlRootView.setOnClickListener(this);
        setVideoSeekBarListener(mVideoSeekBar);
        mRecordCtrlView.setLayoutParams(playerParams);
        mPlayerViewLayout.addView(mRecordCtrlView);
        //初期化の時点から、handlerにmsgを送る
        viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
        hideVideoCtrlView(View.INVISIBLE);
    }

    /**
     * set seek bar listener
     * @param seekBar
     */
    private void setVideoSeekBarListener(SeekBar seekBar) {
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
     * onResume
     */
    @Override
    protected void onResume() {
        DTVTLogger.start();
        super.onResume();

        if(!mIsOncreateOk){
            DTVTLogger.end();
            return;
        }

        initSecureplayer();
        setPlayerEvent();
        DTVTLogger.end();
    }

    /**
     * set player event
     */
    private void setPlayerEvent() {
        DTVTLogger.start();
        if(getCurMode() == VIDEO_RECORDING_MODE){
            if(null == mRecordCtrlView){
                DTVTLogger.end();
                return;
            }
            mRecordCtrlView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    DTVTLogger.start();
                    if(mVideoPlayPause.getVisibility() == View.VISIBLE){
                        mGestureDetector.onTouchEvent(motionEvent);
                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        if(mVideoPlayPause.getVisibility() == View.VISIBLE){
                            if(isHideOperate){
                                hideVideoCtrlView(View.INVISIBLE);
                            }
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
                    DTVTLogger.end();
                    return true;
                }
            });
        }else if(getCurMode() == NOW_ON_AIR_MODE){
            if(null == mTvCtrlView){
                DTVTLogger.end();
                return;
            }
            mTvCtrlView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    DTVTLogger.start();
                    if(mTvNow.getVisibility() == View.VISIBLE){
                        //チャンネル遷移GestureDetector
                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        if(mTvNow.getVisibility() == View.VISIBLE){
                            if(isHideOperate){
                                hideTvCtrlView(View.INVISIBLE);
                            }
                        }else {
                            mTvNow.setVisibility(View.VISIBLE);
                            mTvBack.setVisibility(View.VISIBLE);
                            mTvForward.setVisibility(View.VISIBLE);
                            mTvReplay.setVisibility(View.VISIBLE);
                            //mTvRapid.setVisibility(View.VISIBLE);
                            mTvSeekBar.setVisibility(View.VISIBLE);
                            mTvFullScreen.setVisibility(View.VISIBLE);
                            //setPlayEvent();
                        }
                        hideCtrlViewAfterOperate();
                    }
                    DTVTLogger.end();
                    return true;
                }
            });
        }

        DTVTLogger.end();
    }

    /**
     * hide ctrl view
     */
    private void hideCtrlViewAfterOperate() {
        DTVTLogger.start();
        if(mCtrlHandler != null){
            mCtrlHandler.removeCallbacks(mHideCtrlViewThread);
        }
        mCtrlHandler.postDelayed(mHideCtrlViewThread,HIDE_IN_3_SECOND);
        isHideOperate = true;
        DTVTLogger.end();
    }

    /**
     * onDestroy
     */
    @Override
    protected void onDestroy() {
        DTVTLogger.start();
        super.onDestroy();
        if(mIsOncreateOk){
            if(null!=mPlayerController){
                mPlayerController.setCaptionDataListener(null);
                mPlayerController.release();
                mPlayerController = null;
            }
        }
        DTVTLogger.end();
    }

    /**
     * onConfigurationChanged
     * @param newConfig new param
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        DTVTLogger.start();
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横処理
        } else {
            //縦処理
        }
        DTVTLogger.end();
    }

    /**
     * おすすめへの遷移
     *
     * @param view
     */
    public void recommendButton(View view) {
        DTVTLogger.start();
        startActivity(RecommendPlayerActivity.class, null);
        DTVTLogger.end();
    }

    /**
     * on click event function
     * @param v
     */
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
                Toast.makeText(this, "フルスクリーンに変更されています", Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
            case R.id.tv_player_ctrl_now_on_air_replay_iv:
                Toast.makeText(this, "タップで頭出し再生", Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
//            case R.id.tv_player_ctrl_now_on_air_rapid_tv:
//                Toast.makeText(this,"タップで倍速で再生", Toast.LENGTH_SHORT).show();
//                hideCtrlViewAfterOperate();
//                break;
            case R.id.tv_player_ctrl_now_on_air_back_iv:
                Toast.makeText(this, "前のCH", Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
            case R.id.tv_player_ctrl_now_on_air_forward_iv:
                Toast.makeText(this, "次のCH", Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * hide video ctrl view
     * @param invisible
     */
    private void hideVideoCtrlView(int invisible) {
        DTVTLogger.start();
        mVideoPlayPause.setVisibility(invisible);
        mVideoRewind10.setVisibility(invisible);
        mVideoRewind.setVisibility(invisible);
        mVideoFast30.setVisibility(invisible);
        mVideoFast.setVisibility(invisible);
        mVideoCtrlBar.setVisibility(invisible);
        DTVTLogger.end();
    }

    /**
     * hide tv ctrl view
     * @param invisible
     */
    private void hideTvCtrlView(int invisible) {
        DTVTLogger.start();
        mTvNow.setVisibility(invisible);
        mTvBack.setVisibility(invisible);
        mTvForward.setVisibility(invisible);
        mTvReplay.setVisibility(invisible);
        //mTvRapid.setVisibility(invisible);
        mTvSeekBar.setVisibility(invisible);
        mTvFullScreen.setVisibility(invisible);
        DTVTLogger.end();
    }

    /**
     * ミリ秒をtextViewで表示形(01,05...)に変更
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
     * 現在時点のモードを取得する
     * @return
     */
    public int getCurMode() {
        DTVTLogger.start();
        DTVTLogger.end();
        return curMode;
    }

    /**
     * 外部でモード設定
     * @param curMode
     * NOW_ON_AIR_MODE = 1;
     * VIDEO_RECORDING_MODE = 2;
     */
    public void setCurMode(int curMode){
        DTVTLogger.start();
        this.curMode = curMode;
        DTVTLogger.end();
    }

    /**
     * onStateChanged
     * @param mediaPlayerController controller
     * @param i i
     */
    @Override
    public void onStateChanged(MediaPlayerController mediaPlayerController, int i) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * onFormatChanged
     * @param mediaPlayerController controller
     */
    @Override
    public void onFormatChanged(MediaPlayerController mediaPlayerController) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * onPlayerEvent
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
                showMessage("PE_START_AUTHENTICATION");
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

    /**
     * showMessage
     * @param msg
     */
    private void showMessage(String msg){
        DTVTLogger.start();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        DTVTLogger.end();
    }

    /**
     * errorExit
     */
    private void errorExit(){
        DTVTLogger.start();
        showMessage("ビデオ情報はただしくないです。もう一回試してください。");
        setCanPlay(false);
        DTVTLogger.end();
    }

    /**
     * onError
     * @param mediaPlayerController controller
     * @param i i
     * @param l l
     */
    @Override
    public void onError(MediaPlayerController mediaPlayerController, int i, long l) {
        DTVTLogger.start();
        showMessage("" + i);
        DTVTLogger.end();
    }

    /**
     * onCaptionData
     * @param mediaPlayerController
     * @param captionDrawCommands
     */
    @Override
    public void onCaptionData(MediaPlayerController mediaPlayerController, CaptionDrawCommands captionDrawCommands) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * onSuperData
     * @param mediaPlayerController controller
     * @param captionDrawCommands commands
     */
    @Override
    public void onSuperData(MediaPlayerController mediaPlayerController, CaptionDrawCommands captionDrawCommands) {
        DTVTLogger.start();
        DTVTLogger.end();
    }
}
