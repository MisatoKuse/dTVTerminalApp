/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.DtvContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;
import com.nttdocomo.android.tvterminalapp.model.player.MediaVideoInfo;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.view.ContentsDetailViewPager;

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
        MediaPlayerController.OnPlayerEventListener, MediaPlayerController.OnErrorListener, MediaPlayerController.OnCaptionDataListener {

    /* コンテンツ詳細 start */
    private LinearLayout tabLinearLayout;
    private ContentsDetailViewPager mViewPager;
    private OtherContentsDetailData mDetailData;
    private ImageView thumbnail;
    private String[] mTabNames;
    private DtvContentsDetailFragmentFactory fragmentFactory = null;
    private TextView headerTextView;
    private LinearLayout thumbnailBtn;
    private RelativeLayout thumbnailRelativeLayout;
    public static final String RECOMMEND_INFO_BUNDLE_KEY = "recommendInfoKey";
    public static final String PLALA_INFO_BUNDLE_KEY = "plalaInfoKey";
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    private static final int CONTENTS_DETAIL_TAB_TEXT_SIZE = 15;
    private static final int CONTENTS_DETAIL_TAB_OTHER_MARGIN = 0;
    private VodMetaFullData detailFullData;
    private DtvContentsDetailDataProvider detailDataProvider;
    private static final String DATE_FORMAT = "yyyy/MM/ddHH:mm:ss";
    private String date[] = {"日", "月", "火", "水", "木", "金", "土"};
    private boolean isPlayer = false;
    private Intent intent;
    /* コンテンツ詳細 end */
    /* player start */
    private static final long HIDE_IN_3_SECOND = 3 * 1000;
    private static final int REFRESH_VIDEO_VIEW = 0;
    private final static int ACTIVATION_REQUEST_CODE = 1;
    private final static int ACTIVATION_REQUEST_MAX_CNT = 3;
    private static final int REWIND_SECOND = 10 * 1000;
    private static final int FAST_SECOND = 30 * 1000;
    private RelativeLayout mPlayerViewLayout;
    private RelativeLayout mRecordCtrlView;
    private FrameLayout mVideoPlayPause;
    private TextView mVideoCurTime;
    private ImageView mVideoFullScreen;
    private TextView mVideoTotalTime;
    private SeekBar mVideoSeekBar;
    private SecureVideoView mSecureVideoPlayer;
    private SecuredMediaPlayerController mPlayerController;
    private boolean mCanPlay = false;
    private MediaVideoInfo mCurrentMediaInfo;
    private int mActivationTimes = 0;
    private RelativeLayout mVideoCtrlRootView;
    private TextView mVideoRewind10;
    private ImageView mVideoRewind;
    private TextView mVideoFast30;
    private ImageView mVideoFast;
    private RelativeLayout mVideoCtrlBar;
    private Handler mCtrlHandler = new Handler(Looper.getMainLooper());
    private GestureDetector mGestureDetector;
    private int mScreenWidth;
    private boolean isHideOperate = true;
    private boolean mIsOncreateOk = false;

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
        setNoTitle();
        initView();
    }

    @Override
    protected void onResume() {
        DTVTLogger.start();
        super.onResume();
        if (isPlayer) {
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
     * ビュー初期化
     */
    private void initView() {
        intent = getIntent();
        headerTextView = findViewById(R.id.contents_detail_header_layout_title);
        thumbnailRelativeLayout = findViewById(R.id.dtv_contents_detail_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getWidthDensity(),
                getWidthDensity() * 2 / 3);
        thumbnailRelativeLayout.setLayoutParams(layoutParams);
        Object object = intent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
        if (object instanceof RecordedContentsDetailData) {
            isPlayer = true;
            initPlayer();
        }
        initContentsView();
    }

    /**
     * UIを更新するハンドラー
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
     * initView player
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
        {
            mPlayerController.setCurrentCaption(0); // start caption.
        }
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
     * is activated
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
     * activate
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
     * 機能：プレバイトデータフォルダを戻す
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
        initPlayerView();
        setCanPlay(true);
        playStart();
        DTVTLogger.end();
    }

    /**
     * start playing
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
     * playing pause
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
     * pause button function
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
     * set player event
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
                        if (isHideOperate) {
                            hideCtrlView(View.INVISIBLE);
                        }
                    } else {
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
        DTVTLogger.end();
    }

    /**
     * hide video ctrl view
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
        DTVTLogger.end();
    }

    /**
     * hide ctrl view
     */
    private void hideCtrlViewAfterOperate() {
        DTVTLogger.start();
        if (mCtrlHandler != null) {
            mCtrlHandler.removeCallbacks(mHideCtrlViewThread);
        }
        mCtrlHandler.postDelayed(mHideCtrlViewThread, HIDE_IN_3_SECOND);
        isHideOperate = true;
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
                if (e.getY() > mRecordCtrlView.getHeight() / 3
                        && e.getY() < mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight() / 3) {
                    if (e.getX() < mScreenWidth / 2 - mVideoPlayPause.getWidth() / 2
                            && e.getX() > mScreenWidth / 6) {//10秒戻し
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= REWIND_SECOND;
                        if (pos < 0) {
                            pos = 0;
                            setProgress0();
                        }
                        mPlayerController.seekTo(pos);
                        isHideOperate = false;
                        showMessage("←10");
                    }
                    if (e.getX() > mScreenWidth / 2 + mVideoPlayPause.getWidth() / 2
                            && e.getX() < mScreenWidth - mScreenWidth / 6) {//30秒送り
                        int pos = mPlayerController.getCurrentPosition();
                        pos += FAST_SECOND;
                        //pos = pos > mPlayerController.getDuration() ? mPlayerController.getDuration() : pos;
                        int allDu = mPlayerController.getDuration();
                        if (pos >= allDu) {
                            setProgress0();
                            pos = 0;
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
                if (e1.getY() > mRecordCtrlView.getHeight() / 3
                        && e2.getY() > mRecordCtrlView.getHeight() / 3
                        && e2.getY() < mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight() / 3
                        && e1.getY() < mRecordCtrlView.getHeight() - mRecordCtrlView.getHeight() / 3) {
                    if (e1.getX() > e2.getX() && e1.getX() < mScreenWidth / 2 - mVideoPlayPause.getWidth() / 2) {
                        int pos = mPlayerController.getCurrentPosition();
                        pos -= REWIND_SECOND;
                        pos = pos < 0 ? 0 : pos;
                        mPlayerController.seekTo(pos);
                        isHideOperate = false;
                        showMessage("←10");
                    } else if (e1.getX() < e2.getX() && e1.getX() > mScreenWidth / 2 + mVideoPlayPause.getWidth() / 2) {
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
     * play button function
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
     * set current player info
     *
     * @return whether succeed
     */
    private boolean setCurrentMediaInfo() {
        DTVTLogger.start();
        RecordedContentsDetailData datas = intent.getParcelableExtra(RecordedListActivity.RECORD_LIST_KEY);
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

        int bitRate = Integer.parseInt(datas.getBitrate());
        if (0 == size) {
            size = duration * bitRate;
        }
        String title = datas.getTitle();
        headerTextView.setText(title);
        Uri uri = Uri.parse(url);
        mCurrentMediaInfo = new MediaVideoInfo(
                uri,                 //uri
                "video/mp4",       //RESOURCE_MIMETYPE
                size,                //SIZE
                duration,            //DURATION
                bitRate,             //BITRATE
                true,               //IS_SUPPORTED_BYTE_SEEK
                true,               //IS_SUPPORTED_TIME_SEEK
                true,               //IS_AVAILABLE_CONNECTION_STALLING
                true,               //IS_LIVE_MODE
                true,               //IS_REMOTE
                title,               //TITLE
                "contentFormat"   //CONTENT_FORMAT
        );
        DTVTLogger.end();
        return true;
    }

    /**
     * 機能：「duration="0:00:42.000"」/「duration="0:00:42"」からmsへ変換
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
                    ret += 1000 * Integer.parseInt(ss);
                } else {
                    String ss1 = ss.substring(0, i);
                    String ss2 = ss.substring(i + 1, ss.length());
                    ret += 1000 * Integer.parseInt(ss1);
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
     * showMessage
     *
     * @param msg
     */
    private void showMessage(String msg) {
        DTVTLogger.start();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        DTVTLogger.end();
    }

    /**
     * errorExit
     */
    private void errorExit() {
        DTVTLogger.start();
        showMessage("ビデオ情報はただしくないです。もう一回試してください。");
        setCanPlay(false);
        DTVTLogger.end();
    }

    /**
     * set can play
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
     * コンテンツ詳細データ取得
     */
    private void getContentsData() {
        detailDataProvider = new DtvContentsDetailDataProvider(this);
        String[] cRid = {"1", "2"};
        detailDataProvider.getContentsDetailData(cRid, "", 1);
    }

    /**
     * インテントデータ取得
     */
    private void setIntentDetailData() {
        mDetailData = intent.getParcelableExtra(RECOMMEND_INFO_BUNDLE_KEY);
        if (mDetailData == null) {
            getContentsData();
            mDetailData = intent.getParcelableExtra(PLALA_INFO_BUNDLE_KEY);
            String displayType = mDetailData.getDisplayType();
            String contentsType = mDetailData.getContentsType();
            if("tv_program".equals(displayType)){
                if(!"1".equals(contentsType) && !"2".equals(contentsType)
                        && !"3".equals(contentsType)){
                    //TODO  放送中の場合
                }
            }
        } else {
            int serviceId = mDetailData.getServiceId();
            String categoryId = mDetailData.getCategoryId();
            if(serviceId == OtherContentsDetailData.DTV_HIKARI_CONTENTS_SERVICE_ID){
                if(OtherContentsDetailData.HIKARI_CONTENTS_CATEGORY_ID_DTB.equals(categoryId)
                        || OtherContentsDetailData.HIKARI_CONTENTS_CATEGORY_ID_BS.equals(categoryId)
                        || OtherContentsDetailData.HIKARI_CONTENTS_CATEGORY_ID_IPTV.equals(categoryId)){
                    //TODO  放送中の場合
                }
            }
            setTitleAndThumbnail(mDetailData.getTitle(), mDetailData.getThumb());
        }
    }

    /**
     * データの初期化
     */
    private void setTitleAndThumbnail(String title, String url) {
        if (!TextUtils.isEmpty(title)) {
            headerTextView.setText(title);
        }
        if (!TextUtils.isEmpty(url)) {
            ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(this);
            thumbnail.setImageResource(R.drawable.test_image);
            thumbnail.setTag(url);
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(thumbnail, url);
            if (bitmap != null) {
                thumbnail.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * データの初期化
     */
    private void initContentData() {
        mTabNames = getResources().getStringArray(R.array.other_contents_detail_tabs);
        fragmentFactory = new DtvContentsDetailFragmentFactory();
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
     * initView function
     */
    private void initPlayerData() {
        DTVTLogger.start();
        mActivationTimes = 0;
        DTVTLogger.end();
    }

    /**
     * initPlayerView view
     */
    private void initPlayerView() {
        DTVTLogger.start();
        mPlayerViewLayout = findViewById(R.id.dtv_contents_detail_main_layout_player_rl);
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
        nowTextView.setVisibility(View.GONE);
        mVideoPlayPause.setOnClickListener(this);
        mVideoFullScreen.setOnClickListener(this);
        mVideoCtrlRootView.setOnClickListener(this);
        setSeekBarListener(mVideoSeekBar);
        mPlayerViewLayout.addView(mRecordCtrlView);
        //初期化の時点から、handlerにmsgを送る
        viewRefresher.sendEmptyMessage(REFRESH_VIDEO_VIEW);
        hideCtrlView(View.INVISIBLE);
        pauseButton();
        DTVTLogger.end();
    }

    /**
     * set seek bar listener
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
     * 詳細Viewの初期化
     */
    private void initContentsView() {
        //サムネイル取得
        tabLinearLayout = findViewById(R.id.dtv_contents_detail_main_layout_tab);
        thumbnail = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail);
        thumbnailBtn = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail_btn);
        if (isPlayer) {
            setPlayerScroll();
        } else {
            mViewPager = findViewById(R.id.dtv_contents_detail_main_layout_vp);
            initContentData();
            initTab();
        }
    }

    /**
     * プレイヤーの場合スクロールできない
     */
    private void setPlayerScroll(){
        LinearLayout parentLayout = findViewById(R.id.dtv_contents_detail_main_layout_ll);
        LinearLayout scrollLayout = findViewById(R.id.contents_detail_scroll_layout);
        scrollLayout.removeViewAt(0);
        parentLayout.addView(thumbnailRelativeLayout, 1);
        setThumbnailInvisible();
    }

    /**
     * サムネイル画像の非表示
     */
    private void setThumbnailInvisible() {
        tabLinearLayout.setVisibility(View.GONE);
        thumbnail.setVisibility(View.GONE);
        thumbnailBtn.setVisibility(View.GONE);
    }

    /**
     * tabのレイアウトを設定
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
            tabLinearLayout.addView(tabTextView);
        }
    }

    /**
     * インジケータ設置
     *
     * @param position タブポジション
     */
    private void setTab(int position) {
        if (tabLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) tabLinearLayout.getChildAt(i);
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
     * コンテンツ詳細用ページャアダプター
     */
    private class ContentsDetailPagerAdapter extends FragmentStatePagerAdapter {
        ContentsDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragmentFactory.createFragment(position);
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
        //詳細情報取得して、更新する
        if (contentsDetailInfo != null) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            detailFullData = contentsDetailInfo.get(0);
            detailFragment.mOtherContentsDetailData.setTitle(detailFullData.getTitle());
            detailFragment.mOtherContentsDetailData.setDetail(detailFullData.getSynop());
            String[] credit_array = detailFullData.getmCredit_array();
            setTitleAndThumbnail(detailFullData.getTitle(), detailFullData.getmDtv_thumb_448_252());
            if (credit_array != null && credit_array.length > 0) {
                detailDataProvider.getRoleListData();
            }
            if (!TextUtils.isEmpty(detailFullData.getmService_id())) {
                detailDataProvider.getChannelList(1, 1, "", 1);
            }
            detailFragment.noticeRefresh();
        }
    }

    @Override
    public void onRoleListCallback(ArrayList<RoleListMetaData> roleListInfo) {
        //スタッフ情報取得して、更新する
        if (roleListInfo != null) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            if (detailFullData != null) {
                String[] credit_array = detailFullData.getmCredit_array();
                List<String> staffList = getRoleList(credit_array, roleListInfo);
                if (staffList != null && staffList.size() > 0) {
                    detailFragment.mOtherContentsDetailData.setStaffList(staffList);
                    detailFragment.refreshStaff();
                }
            }
        }
    }

    /**
     * ロールリスト取得
     *
     * @param credit_array スタッフ情報
     * @param roleListInfo ロールリスト情報
     * @return ロールリスト
     */
    private List<String> getRoleList(String[] credit_array, ArrayList<RoleListMetaData> roleListInfo) {
        List<String> staffList = new ArrayList<>();
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < credit_array.length; i++) {
            String[] creditInfo = credit_array[i].split("\\|");
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
                                String oldData[] = ids.toString().split(",");
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
            if (!TextUtils.isEmpty(detailFullData.getmService_id())) {
                DtvContentsDetailFragment detailFragment = getDetailFragment();
                for (int i = 0; i < channels.size(); i++) {
                    Channel channel = channels.get(i);
                    if (detailFullData.getmService_id().equals(channel.getServiceId())) {
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
        }
    }

    /**
     * 時刻
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
                        date[calendar.get(Calendar.DAY_OF_WEEK) - 1], ") ",
                        start.substring(11, 16), " - ",
                        end.substring(11, 16)};
                channelDate = util.getConnectString(strings);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return channelDate;
    }

    /**
     * 詳細tabを取得
     */
    private DtvContentsDetailFragment getDetailFragment() {
        Fragment currentFragment = fragmentFactory.createFragment(0);
        DtvContentsDetailFragment detailFragment = (DtvContentsDetailFragment) currentFragment;
        return detailFragment;
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
                Toast.makeText(this, "フルスクリーンに変更されています", Toast.LENGTH_SHORT).show();
                hideCtrlViewAfterOperate();
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * onPlayerEvent
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
     * onStateChanged
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
     * onFormatChanged
     *
     * @param mediaPlayerController controller
     */
    @Override
    public void onFormatChanged(MediaPlayerController mediaPlayerController) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * onError
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
     * onCaptionData
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
     * onSuperData
     *
     * @param mediaPlayerController controller
     * @param captionDrawCommands   commands
     */
    @Override
    public void onSuperData(MediaPlayerController mediaPlayerController, CaptionDrawCommands captionDrawCommands) {
        DTVTLogger.start();
        DTVTLogger.end();
    }
}