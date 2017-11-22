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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

public class TvPlayerActivity extends BaseActivity implements View.OnClickListener {

    private VideoView mPlayerView;
    private RelativeLayout mCtrlView;
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
    private static final int REFRESH_VIEW = 0;
    private long lastTime;
    private static final int NOW_ON_AIR = 0;
    private static final int VIDEO_RECORDING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_main_layout);
        initView();
        setCtrlEvent();
        tempUriData();
        //初期化の時点から、handlerにmsgを送る
        viewRefresher.sendEmptyMessage(REFRESH_VIEW);
    }

    private void setCtrlEvent() {

        mPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                switch (me.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mCtrlView.setVisibility(View.VISIBLE);
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

        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                time2TextViewFormat(mCurTime, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                viewRefresher.removeMessages(REFRESH_VIEW);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mPlayerView.seekTo(progress);
                viewRefresher.sendEmptyMessage(REFRESH_VIEW);
            }
        });
    }

    private void tempUriData() {
        mPlayerView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        MediaController ctr = new MediaController(this);
        mPlayerView.setMediaController(ctr);
        ctr.setMediaPlayer(mPlayerView);
        mPlayerView.start();
    }

    private void initView() {
        mPlayerView = findViewById(R.id.tv_player_main_layout_player_vv);//表示されるため、一時VideoViewを使っている
        //mCtrlView = findViewById(R.id.tv_player_main_layout_ctrl_root_rl);
        mPlayerViewLayout = findViewById(R.id.tv_player_main_layout_player_rl);
        mCtrlView = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.tv_player_main_layout_video_ctrl, null, false);
        mplayPause = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_pause_iv);
        mNowOnAir = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_player_now_on_air_tv);
        mForLast = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_back_iv);
        mForNext = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_forward_iv);
        mRewindLeft = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_rewind_left_iv);
        mFastRight = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_fast_right_iv);
        mReplay = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_replay_iv);
        mCurTime = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_cur_time_tv);
        mFullScreen = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_full_screen_iv);
        mTotalDur = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_total_during_tv);
        mRapid = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_rapid_tv);
        mProgress = mCtrlView.findViewById(R.id.tv_player_main_layout_video_ctrl_seek_bar_sb);
        mplayPause.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mForLast.setOnClickListener(this);
        mForNext.setOnClickListener(this);
        mRewindLeft.setOnClickListener(this);
        mFastRight.setOnClickListener(this);
        RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
        playerParams.height = getHeightDensity() * 4 / 11;
        mPlayerView.setLayoutParams(playerParams);
        mCtrlView.setLayoutParams(playerParams);
        mPlayerViewLayout.addView(mCtrlView);

        //mCtrlView.setVisibility(View.GONE);
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
            case R.id.tv_player_main_layout_video_ctrl_player_pause_iv:
                // TODO: 2017/11/21 表示テストのため VideoViewApiを使う
                if (mPlayerView.isPlaying()) {
                    mPlayerView.pause();
                    mplayPause.setImageResource(R.mipmap.ic_tvplayer_player_play);
                } else {
                    mPlayerView.start();
                    mplayPause.setImageResource(R.mipmap.ic_tvplayer_player_pause);
                    if (mReplay.getVisibility() == View.VISIBLE) {
                        mReplay.setVisibility(View.INVISIBLE);
                        mCurTime.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.tv_player_main_layout_video_ctrl_full_screen_iv:
                Toast.makeText(this, "フルスクリーンに変更されています", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_player_main_layout_video_ctrl_back_iv:
                break;
            case R.id.tv_player_main_layout_video_ctrl_forward_iv:
                break;
            case R.id.tv_player_main_layout_video_ctrl_rewind_left_iv:
                /*int rewindPos = mPlayerView.getCurrentPosition();
                rewindPos -= 10*1000;
                mPlayerView.seekTo(rewindPos);*/
                break;
            case R.id.tv_player_main_layout_video_ctrl_fast_right_iv:
                /*int fastPos = mPlayerView.getCurrentPosition();
                fastPos += 30*1000;
                mPlayerView.seekTo(fastPos);*/
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
            if (msg.what == REFRESH_VIEW) {
                int currentPosition = mPlayerView.getCurrentPosition();
                int totalDur = mPlayerView.getDuration();
                time2TextViewFormat(mCurTime, currentPosition);
                time2TextViewFormat(mTotalDur, totalDur);
                mProgress.setMax(totalDur);
                mProgress.setProgress(currentPosition);
                //録画コンテンツを終端まで再生した後は、シークバーを先頭に戻し、先頭で一時停止状態とする
                if (currentPosition == totalDur) {
                    mProgress.setProgress(0);
                    mCurTime.setVisibility(View.INVISIBLE);
                    mReplay.setVisibility(View.VISIBLE);
                    // TODO: 2017/11/21 pause/playIcon変更
                }
                //3秒以上無操作であれば消える
                if (System.currentTimeMillis() - lastTime > 3 * 1000
                        && mCtrlView.getVisibility() == View.VISIBLE) {
                    mCtrlView.setVisibility(View.INVISIBLE);
                }
                viewRefresher.sendEmptyMessageDelayed(REFRESH_VIEW, 500);
            }
        }
    };
}
