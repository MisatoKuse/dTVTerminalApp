/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.digion.dixim.android.secureplayer.SecureVideoView;
import com.digion.dixim.android.secureplayer.SecuredMediaPlayerController;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

public class TvPlayerActivity extends BaseActivity implements View.OnClickListener {

    private static final int REFRESH_TV_VIEW = 1;
    private VideoView mPlayerView;
    private ImageView mplayPause;
    private RelativeLayout mPlayerViewLayout;
    private TextView mNowOnAir;
    private ImageView mForLast;
    private ImageView mForNext;
    private RelativeLayout mRewindLeft;
    private RelativeLayout mFastRight;
    private ImageView mReplay;
    private TextView mCurTime;
    private ImageView mFullScreen;
    private TextView mTotalDur;
    private TextView mRapid;
    private SeekBar mProgress;
    private static final int REFRESH_VIDEO_VIEW = 0;
    private long lastTime;
    private static final int NOW_ON_AIR_MODE = 1;
    private static final int VIDEO_RECORDING_MODE = 2;
    private int curMode = VIDEO_RECORDING_MODE;
    private RelativeLayout mTvCtrlView;
    private RelativeLayout mRecordCtrlView;
    private ImageView mTvBack;
    private ImageView mTvForwar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_main_layout);
        mSecureVideoPlayer = findViewById(R.id.tv_player_main_layout_player_vv);
        SecuredMediaPlayerController mPlayerController = new SecuredMediaPlayerController(this,true,true,true);
        mSecureVideoPlayer.init(mPlayerController);
        mPlayerController.start();

    }

    private void setCtrlEvent(final RelativeLayout ctrlView) {
        mPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                switch (me.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ctrlView.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        lastTime = System.currentTimeMillis();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initView() {
        mPlayerView = findViewById(R.id.tv_player_main_layout_player_vv);//表示されるため、一時VideoViewを使っている
        mPlayerViewLayout = findViewById(R.id.tv_player_main_layout_player_rl);
        RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
        playerParams.height = getHeightDensity() * 4 / 11;
        mPlayerView.setLayoutParams(playerParams);
        if(getCurMode() == NOW_ON_AIR_MODE){//リニア放送中
            mTvCtrlView = (RelativeLayout) LayoutInflater.from(this)
                    .inflate(R.layout.tv_player_ctrl_now_on_air, null, false);
            mTvBack = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_back_iv);
            mTvForwar = mTvCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_forward_iv);
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
            setCtrlEvent(mTvCtrlView);
        }else if(getCurMode() == VIDEO_RECORDING_MODE){//録画
            mRecordCtrlView = (RelativeLayout) LayoutInflater.from(this)
                    .inflate(R.layout.tv_player_ctrl_video_record, null, false);
            mVideoPlayPause = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_video_record_player_pause_fl);
            mVideoCurTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_cur_time_tv);
            mVideoFullScreen = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_full_screen_iv);
            mVideoRapid = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_rapid_tv);
            mVideoTotalTime = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_total_time_tv);
            mVideoSeekBar = mRecordCtrlView.findViewById(R.id.tv_player_ctrl_now_on_air_seek_bar_sb);
            mVideoPlayPause.setOnClickListener(this);
            mVideoFullScreen.setOnClickListener(this);
            mVideoRapid.setOnClickListener(this);
            setVideoSeekBarListener(mVideoSeekBar);
            mRecordCtrlView.setLayoutParams(playerParams);
            mPlayerViewLayout.addView(mRecordCtrlView);
            //初期化の時点から、handlerにmsgを送る
            viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
            setCtrlEvent(mRecordCtrlView);
        }
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
                mPlayerView.seekTo(progress);
                viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 2017/11/22 横縦処理
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                // TODO: 2017/11/21 表示テストのため VideoViewApiを使う
                break;
            case R.id.tv_player_ctrl_now_on_air_full_screen_iv:
                Toast.makeText(this, "フルスクリーンに変更されています", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_player_ctrl_now_on_air_replay_iv:
                Toast.makeText(this, "タップで頭出し再生", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_player_ctrl_now_on_air_rapid_tv:
                Toast.makeText(this,"タップで倍速で再生",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
            if(msg.what == REFRESH_TV_VIEW){//NOW ON AIR
                int currentPosition = mPlayerView.getCurrentPosition();
                int totalDur = mPlayerView.getDuration();
                //time2TextViewFormat(mCurTime, currentPosition);
                //time2TextViewFormat(mTotalDur, totalDur);
                mTvSeekBar.setMax(totalDur);
                mTvSeekBar.setProgress(currentPosition);
                //3秒以上無操作であれば消える
                if (System.currentTimeMillis() - lastTime > 3 * 1000
                        && mTvCtrlView.getVisibility() == View.VISIBLE) {
                    mTvCtrlView.setVisibility(View.INVISIBLE);
                }
                viewRefresher.sendEmptyMessageDelayed(REFRESH_TV_VIEW, 500);
            }
            if (msg.what == REFRESH_VIDEO_VIEW) {//録画
                int currentPosition = mPlayerView.getCurrentPosition();
                int totalDur = mPlayerView.getDuration();
                time2TextViewFormat(mVideoCurTime, currentPosition);
                time2TextViewFormat(mVideoTotalTime, totalDur);
                mVideoSeekBar.setMax(totalDur);
                mVideoSeekBar.setProgress(currentPosition);
                //録画コンテンツを終端まで再生した後は、シークバーを先頭に戻し、先頭で一時停止状態とする
                if (currentPosition == totalDur) {
                    mVideoSeekBar.setProgress(0);
                    /*mVideoCurTime.setVisibility(View.INVISIBLE);
                    mReplay.setVisibility(View.VISIBLE);*/
                    // TODO: 2017/11/21 pause/playIcon変更
                }
                //3秒以上無操作であれば消える
                if (System.currentTimeMillis() - lastTime > 3 * 1000
                        && mRecordCtrlView.getVisibility() == View.VISIBLE) {
                    mRecordCtrlView.setVisibility(View.INVISIBLE);
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
}
