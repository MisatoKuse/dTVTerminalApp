/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.dlna.DlnaContentRecordedDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsInfo;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaProvDownload;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadListener;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadService;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 録画番組.
 */
public class RecordedListActivity extends BaseActivity implements View.OnClickListener,
        TabItemLayout.OnClickTabTextListener,

        DlnaContentRecordedDataProvider.CallbackListener {

    /** タブ名. */
    private String[] mTabNames = null;
    /** タブレイアウト. */
    private TabItemLayout mTabLayout = null;
    /** viewpager. */
    private ViewPager mViewPager = null;
    /** 進捗バー. */
    private ProgressBar progressBar;
    /** 遷移先（メニュー）. */
    private Boolean mIsMenuLaunch = false;

    private DlnaContentRecordedDataProvider mDlnaContentRecordedDataProvider;
    /** Fragment作成クラス. */
    private RecordedFragmentFactory mRecordedFragmentFactory = null;
    /** 値渡すキー. */
    public static final String RECORD_LIST_KEY = "recordListKey";
    /** 日付フォーマット. */
    public static final String sMinus = "-";
    /** タブ名. */
    public static final String RLA_FragmentName_All = "all";
    /** リスト0件メッセージ. */
    private TextView mNoDataMessage;
    /** すべて. */
    private static final int ALL_RECORD_LIST = 0;
    /** ダウンロード済み. */
    private static final int DOWNLOAD_OVER = 1;
    /**
     * エラーを返すハンドラー.
     */
    private Handler mHandler = new Handler();
    /**
     * タブインデックス　すべて.
     */
    private static final int TAB_INDEX_ALL = 0;
    /**
     * タブインデックス　ダウンロード済み.
     */
    private static final int TAB_INDEX_DOWONLOAD_COMPLETED = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list_main_layout);

        setTitleText(getString(R.string.nav_menu_item_recorder_program));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        registReceiver();
        initView();
        initTabVIew();
        setPagerAdapter();
        mDlnaContentRecordedDataProvider = new DlnaContentRecordedDataProvider();
        DTVTLogger.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendScreenViewForPosition(getCurrentPosition());
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        showProgressBar();
        mNoDataMessage.setVisibility(View.GONE);
        initDl();
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDlnaContentRecordedDataProvider.stopListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadReceiver);
        boolean isRunning = isDownloadServiceRunning();
        if (!isRunning) {
            DlnaProvDownload.uninitGlobalDl();
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合ホーム画面に戻る
                    contentsDetailBackKey(null);
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClickTab(final int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void callback(DlnaObject[] objs) {
        setProgressBarGone();
        if (objs.length == 0) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }
        ArrayList<DlnaRecVideoItem> dstList = new ArrayList<>();
        for(DlnaObject dlnaObject: objs) {
            DlnaRecVideoItem item = new DlnaRecVideoItem();
            item.mItemId = dlnaObject.mObjectId;
            item.mSize = "0";
            item.mResUrl = dlnaObject.mResUrl;
            item.mResolution = dlnaObject.mResolution;
            item.mBitrate = "0";
            item.mDuration = dlnaObject.mDuration;
            item.mTitle = dlnaObject.mTitle;
            item.mVideoType = dlnaObject.mVideoType;
            item.mClearTextSize = "0";
            dstList.add(item);
        }
        setVideoBrows(dstList);
    }

    /**
     * 初期化処理.
     */
    private void initDl() {
        boolean isRunning = isDownloadServiceRunning();
        if (!isRunning) {
            boolean res = DlnaProvDownload.initGlobalDl(DownloaderBase.getDownloadPath(this));
            if (!res) {
                DTVTLogger.debug("initGlobalDl failed");
            }
        }
    }

    /**
     * 画面描画.
     */
    private void initView() {
        DTVTLogger.start();
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mViewPager = findViewById(R.id.record_list_main_layout_viewpagger);
        switch (StbConnectionManager.shared().getConnectionStatus()) {
            case NONE_LOCAL_REGISTRATION:
                mTabNames = getResources().getStringArray(R.array.record_list_tab_name_only_dl);
                break;
            default:
                mTabNames = getResources().getStringArray(R.array.record_list_tab_names);
                break;
        }

        mRecordedFragmentFactory = new RecordedFragmentFactory();
        progressBar = findViewById(R.id.record_list_main_layout_progress);
        mNoDataMessage = findViewById(R.id.recorded_list_no_items);
    }

    /**
     * タブの指定が持ち出しリストの時、持ち出しリストの生成を行う.
     */
    private void setPagerAdapter() {
        DTVTLogger.start();
        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                setTab(position);
                sendScreenViewForPosition(position);
            }
        });
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する
     */
    private void sendScreenViewForPosition(int position) {
        switch (position) {
            case TAB_INDEX_ALL:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_recording_list));
                break;
            case TAB_INDEX_DOWONLOAD_COMPLETED:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_recording_list_takeout));
                break;
        }
    }

    /**
     * 機能
     * タブの設定.
     */
    private void initTabVIew() {
        DTVTLogger.start();

        mTabLayout = new TabItemLayout(this);
        mTabLayout.setTabClickListener(this);
        mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.RECORDED_LIST_ACTIVITY);
        RelativeLayout tabRelativeLayout = findViewById(R.id.rl_recorded_list_tab);
        tabRelativeLayout.addView(mTabLayout);

        DTVTLogger.end();
    }

    @Override
    public void onConnectErrorCallback(final int errorCode) {
        String errorMsg = getString(R.string.common_text_remote_fail_msg);
        String format = getString(R.string.common_text_remote_fail_error_code_format);
        showErrorDialog(errorMsg.replace(format, String.valueOf(errorCode)));
    }

    /**
     * 機能
     * タブを切り替え.
     *
     * @param position タブインデックス
     */
    private void setTab(final int position) {
        DTVTLogger.start();
        if (mTabLayout != null) {
            mTabLayout.setTab(position);
        }
        showProgressBar();
        mNoDataMessage.setVisibility(View.GONE);
        switch (mViewPager.getCurrentItem()) {
            case ALL_RECORD_LIST:
                getData();
                break;
            case DOWNLOAD_OVER:
                setRecordedTakeOutContents();
                break;
            default:
                break;
        }
    }

    /**
     * プロセスバーを表示する.
     */
    private void showProgressBar() {
        //オフライン時は表示しない
        if (!NetWorkUtils.isOnline(this)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 持ち出しリスト生成.
     */
    @SuppressWarnings("OverlyLongMethod")
    public void setRecordedTakeOutContents() {
        DTVTLogger.start();
        RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment();
        List<ContentsData> list = baseFragment.getContentsData();
        baseFragment.mContentsList = new ArrayList<>();
        if (list != null) {
            list.clear();
            List<Map<String, String>> resultList = getDownloadListFromDb();
            if (resultList != null && resultList.size() > 0) {
                for (Map<String, String> hashMap : resultList) {
                    String downloadStatus = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                    if (!TextUtils.isEmpty(downloadStatus)) {
                        String path = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_SAVE_URL);
                        String itemId = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                        String title = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_TITLE);
                        String url = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_URL);
                        String duration = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DURATION);
                        String totalSize = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_SIZE);
                        String resolution = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_RESOLUTION);
                        String upnpIcon = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_UPNP_ICON);
                        String bitrate = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_BITRATE);
                        String videoType = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_TYPE);

                        String fullPath = path + File.separator + itemId;
                        File file = new File(fullPath);
                        if (file.exists()) {
                            ContentsData contentsData = new ContentsData();
                            contentsData.setTitle(title);
                            contentsData.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                            contentsData.setDlFileFullPath(fullPath);
                            list.add(contentsData);
                            RecordedContentsDetailData detailData = new RecordedContentsDetailData();
                            detailData.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                            detailData.setDlFileFullPath(fullPath);
                            detailData.setItemId(itemId);
                            detailData.setUpnpIcon(upnpIcon);
                            detailData.setSize(totalSize);
                            detailData.setResUrl(url);
                            detailData.setResolution(resolution);
                            detailData.setBitrate(bitrate);
                            detailData.setDuration(duration);
                            detailData.setTitle(title);
                            detailData.setVideoType(videoType);
                            detailData.setClearTextSize(totalSize);
                            baseFragment.mContentsList.add(detailData);
                        }
                    }
                }
            } else {
                mNoDataMessage.setVisibility(View.VISIBLE);
            }
            baseFragment.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
    }

    /**
     * DMSデバイスを取り始める.
     */
    private void getData() {
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        // 未ペアリング時
        if (dlnaDmsItem.mControlUrl.isEmpty()) {
            showGetDataFailedToast();
            mNoDataMessage.setVisibility(View.VISIBLE);
            setProgressBarGone();
        } else {
            mDlnaContentRecordedDataProvider.listen(this);
            mDlnaContentRecordedDataProvider.browse(this);
            clearFragment(0);
        }
    }

    /**
     * フラグメント生成.
     *
     * @param position ポジション
     * @return フラグメント
     */
    private RecordedBaseFragment getCurrentRecordedBaseFragment(final int... position) {
        DTVTLogger.start();
        int tabIndex;
        if (position != null && position.length > 0) {
            tabIndex = position[0];
        } else {
            tabIndex = mViewPager.getCurrentItem();
        }
        RecordedBaseFragment f = mRecordedFragmentFactory.createFragment(tabIndex);
        if (0 == tabIndex && null != f) {
            f.setFragmentName(RLA_FragmentName_All);
        }
        return f;
    }

    /**
     * ポジションを取得する.
     *
     * @return ポジション
     */
    public int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

    /**
     * フラグメントをクリア.
     *
     * @param tabNo タブナンバー
     */
    private void clearFragment(final int tabNo) {
        DTVTLogger.start();
        if (null != mViewPager) {
            RecordedBaseFragment baseFragment = mRecordedFragmentFactory.createFragment(tabNo);
            baseFragment.clear();
            baseFragment.notifyDataSetChanged();
        }
    }

    /**
     * 進捗バー閉じる.
     */
    private void setProgressBarGone() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * ダウンロードが動作しているか.
     *
     * @return ダウンロードが動作しているか
     */
    private boolean isDownloadServiceRunning() {
        return DownloadService.isDownloadServiceRunning();
    }

    /**
     * ダウンロードリスト取得.
     *
     * @return リスト
     */
    private List<Map<String, String>> getDownloadListFromDb() {
        DownloadDataProvider downloadDataProvider = new DownloadDataProvider(this);
        return downloadDataProvider.getDownloadListData();
    }

    /**
     * VideoBrowsの設定.
     *
     * @param dlnaRecVideoItems 録画ビデオアイテム
     */
    @SuppressWarnings("OverlyLongMethod")
    private void setVideoBrows(final ArrayList<DlnaRecVideoItem> dlnaRecVideoItems) {
        final RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
        baseFragment.setFragmentName(RLA_FragmentName_All);
        baseFragment.mContentsList = new ArrayList<>();
        List<Map<String, String>> resultList = getDownloadListFromDb();
        setTakeOutContentsToAll(dlnaRecVideoItems, resultList);
        List<ContentsData> listData = baseFragment.getContentsData();
        if (null != listData) {
            listData.clear();
        }
        if (baseFragment.mQueueIndex == null) {
            baseFragment.mQueueIndex = new ArrayList<>();
        }
        baseFragment.mQueueIndex.clear();
        setDownLoadQue(baseFragment, dlnaRecVideoItems, resultList);
        final boolean hideDownloadBtn = StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.NONE_LOCAL_REGISTRATION;
        for (int i = 0; i < dlnaRecVideoItems.size(); i++) {
            DlnaRecVideoItem itemData = dlnaRecVideoItems.get(i);
            RecordedContentsDetailData detailData = new RecordedContentsDetailData();
            detailData.setItemId(itemData.mItemId);
            detailData.setUpnpIcon(itemData.mUpnpIcon);
            detailData.setSize(itemData.mSize);
            detailData.setResUrl(itemData.mResUrl);
            detailData.setResolution(itemData.mResolution);
            detailData.setBitrate(itemData.mBitrate);
            detailData.setDuration(itemData.mDuration);
            detailData.setTitle(itemData.mTitle);
            detailData.setVideoType(itemData.mVideoType);
            detailData.setClearTextSize(itemData.mClearTextSize);
            if (resultList != null && resultList.size() > 0) {
                for (Map<String, String> hashMap : resultList) {
                    String itemId = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                    String path = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_SAVE_URL);
                    String fullPath = path + File.separator + itemId;
                    if (!TextUtils.isEmpty(itemId)) {
                        String allItemId = itemData.mItemId;
                        if (!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)) {
                            allItemId = DownloaderBase.getFileNameById(itemData.mItemId);
                        }
                        if (itemId.equals(allItemId)) {
                            String downloadStatus = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                            if (!TextUtils.isEmpty(downloadStatus)) {
                                detailData.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                                detailData.setDlFileFullPath(fullPath);
                            } else {
                                detailData.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                            }
                        }
                    }
                }
            }
            baseFragment.mContentsList.add(detailData);
            setNotifyData(baseFragment, itemData, i, detailData.getDlFileFullPath(), hideDownloadBtn);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                baseFragment.notifyDataSetChanged();
                if (baseFragment.mQueueIndex.size() > 0) {
                    baseFragment.bindServiceFromBackground();
                }
            }
        });
    }

    /**
     * ダウンロードキューセット.
     *
     * @param baseFragment baseFragment
     * @param dlnaRecVideoItems アイテム
     * @param resultList コンテンツリスト
     */
    private void setDownLoadQue(final RecordedBaseFragment baseFragment,
                                final ArrayList<DlnaRecVideoItem> dlnaRecVideoItems, final List<Map<String, String>> resultList) {
        if (resultList == null) {
            return;
        }

        for (Map<String, String> hashMap : resultList) {
            String itemId = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
            for (int t = 0; t < dlnaRecVideoItems.size(); t++) {
                String allItemId = dlnaRecVideoItems.get(t).mItemId;
                if (!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)) {
                    allItemId = DownloaderBase.getFileNameById(dlnaRecVideoItems.get(t).mItemId);
                }
                if (itemId.equals(allItemId)) {
                    String downloadStatus = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                    if (TextUtils.isEmpty(downloadStatus)) {
                        baseFragment.mQueueIndex.add(t);
                        break;
                    }
                }
            }
        }// for resultList
    }

    /**
     * 表示の更新.
     *
     * @param baseFragment baseFragment
     * @param dlnaRecVideoItem 録画ビデオアイテム
     * @param i i
     * @param fullDlPath フルパス
     */
    private void setNotifyData(final RecordedBaseFragment baseFragment, final DlnaRecVideoItem dlnaRecVideoItem,
                               final int i, final String fullDlPath, final boolean hideDownloadBtn) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
        // TODO 年齢取得未実装の為、固定値を返却
        boolean isAge = true;
        ContentsData contentsData = new ContentsData();
        if (isAge) {
            contentsData.setTitle(dlnaRecVideoItem.mTitle);
            contentsData.setAllowedUse(dlnaRecVideoItem.mAllowedUse);
            String selectDate = "";
            if (!TextUtils.isEmpty(dlnaRecVideoItem.mDate)) {
                String time = dlnaRecVideoItem.mDate.replaceAll("-", "/").replace("T", "");
                try {
                    Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                    calendar.setTime(sdf.parse(time));
                    StringUtils util = new StringUtils();
                    String[] strings = {String.valueOf(calendar.get(Calendar.MONTH)), "/",
                            String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), " (",
                            DateUtils.STRING_DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK)], ") ",
                            String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)), ":",
                            String.valueOf(calendar.get(Calendar.MINUTE))};
                    int mon = Integer.parseInt(strings[0]);
                    ++mon;
                    strings[0] = String.valueOf(mon);
                    selectDate = StringUtils.getConnectString(strings);
                } catch (ParseException e) {
                    DTVTLogger.debug(e);
                }
            }
            //duration && channel name begin
            String mins = dlnaRecVideoItem.getDurationInMinutes();

            String channelName = dlnaRecVideoItem.mChannelName;
            StringBuilder sb = new StringBuilder();
            sb.append(selectDate);
            if (null != mins) {
                sb.append("（");
                sb.append(mins);
                sb.append("分）");
            }
            if (null != channelName && !channelName.isEmpty()) {
                sb.append(" ");
                sb.append(sMinus);
                sb.append(" ");
                sb.append(channelName);
            }
            contentsData.setDownloadBtnHide(hideDownloadBtn);
            //duration && channel name end
            contentsData.setTime(sb.toString());
            contentsData.setDownloadFlg(baseFragment.mContentsList.get(i).getDownLoadStatus());
            contentsData.setDlFileFullPath(fullDlPath);
            List<ContentsData> l = baseFragment.getContentsData();
            if (null != l) {
                l.add(contentsData);
            }
        }
    }

    /**
     * レシーバー登録.
     */
    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.DOWNLOAD_ON_PROGRESS);
        filter.addAction(DownloadService.DOWNLOAD_ON_SUCCESS);
        filter.addAction(DownloadService.DOWNLOAD_ON_FAIL);
        filter.addAction(DownloadService.DOWNLOAD_ON_LOW_STORAGE_SPACE);
        filter.addAction(DownloadService.DOWNLOAD_ON_CANCEL_ALL);
        filter.addAction(DownloadService.DOWNLOAD_ON_CANCEL);
        filter.addAction(DownloadService.DOWNLOAD_ON_START);
        filter.addAction(DownloadService.DOWNLOAD_DL_DATA_PROVIDER_AVAILABLE);
        filter.addAction(DownloadService.DOWNLOAD_DL_DATA_PROVIDER_UNAVAILABLE);
        LocalBroadcastManager.getInstance(this).registerReceiver(downloadReceiver, filter);
    }

    /**
     * ダウンロード状態通知.
     */
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (DownloadService.DOWNLOAD_ON_PROGRESS.equals(intent.getAction())) {
                int progress = intent.getIntExtra(DownloadService.DOWNLOAD_PARAM_INT, 0);
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onDownloadProgressByBg(progress);
            } else if (DownloadService.DOWNLOAD_ON_SUCCESS.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DOWNLOAD_PARAM_STRING);
                baseFragment.onDownloadSuccessByBg(fullPath);
            } else if (DownloadService.DOWNLOAD_ON_FAIL.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DOWNLOAD_PARAM_STRING);
                int error = intent.getIntExtra(DownloadService.DOWNLOAD_PARAM_INT, DownloadListener.DownLoadError.DLError_NoError.ordinal());
                baseFragment.onDownloadFailByBg(fullPath);
            } else if (DownloadService.DOWNLOAD_ON_LOW_STORAGE_SPACE.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DOWNLOAD_PARAM_STRING);
                baseFragment.onLowStorageSpaceByBg(fullPath);
            }  else if (DownloadService.DOWNLOAD_ON_CANCEL_ALL.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DOWNLOAD_PARAM_STRING);
                baseFragment.onCancelAll(fullPath);
            }  else if (DownloadService.DOWNLOAD_ON_CANCEL.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DOWNLOAD_PARAM_STRING);
                baseFragment.onCancelByBg(fullPath);
            } else if (DownloadService.DOWNLOAD_ON_START.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onStartBg();
            } else if (DownloadService.DOWNLOAD_DL_DATA_PROVIDER_AVAILABLE.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onDlDataProviderAvailable();
            }  else if (DownloadService.DOWNLOAD_DL_DATA_PROVIDER_UNAVAILABLE.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onDlDataProviderUnavailable();
            }
        }
    };

    /**
     * 検索結果タブ専用アダプター.
     */
    private class MainAdapter extends FragmentStatePagerAdapter {
        /**
         * コンストラクタ.
         *
         * @param fm FragmentManager
         */
        MainAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            synchronized (this) {
                DTVTLogger.start();
                RecordedBaseFragment f = mRecordedFragmentFactory.createFragment(position);
                if (0 == position && null != f) {
                    f.setFragmentName(RLA_FragmentName_All);
                }
                return f;
            }
        }

        @Override
        public int getCount() {
            DTVTLogger.start();
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            DTVTLogger.start();
            return mTabNames[position];
        }
    }

    /**
     * [すべて]のListと[持ち出し]Listをマージ.
     *
     * @param list すべてのリスト
     * @param takeoutList 持ち出しリスト
     * @return list　生成した[すべて]一覧に表示するリスト
     */
    private ArrayList<DlnaRecVideoItem> setTakeOutContentsToAll(final ArrayList<DlnaRecVideoItem> list,
                                                                final List<Map<String, String>> takeoutList) {
        // 返却するリスト
        ArrayList<DlnaRecVideoItem> allList = list;
        if (allList == null) {
            allList = new ArrayList<>();
        }
        if (takeoutList != null) {
            for (Map<String, String> hashMap : takeoutList) {
                String itemId = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                boolean isExist = false;
                if (!TextUtils.isEmpty(itemId)) {
                    for (DlnaRecVideoItem dlnaRecVideoItem : allList) {
                        String allItemId = DownloaderBase.getFileNameById(dlnaRecVideoItem.mItemId);
                        if (itemId.equals(allItemId)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        String bitrate = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_BITRATE);
                        String duration = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DURATION);
                        String title = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_TITLE);
                        String totalSize = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_SIZE);
                        DlnaRecVideoItem dlnaRecVideoItem = new DlnaRecVideoItem();
                        dlnaRecVideoItem.mItemId = itemId;
                        dlnaRecVideoItem.mClearTextSize = totalSize;
                        dlnaRecVideoItem.mTitle = title;
                        dlnaRecVideoItem.mDuration = duration;
                        dlnaRecVideoItem.mBitrate = bitrate;
                        allList.add(dlnaRecVideoItem);
                    }
                }
            }
        }
        return allList;
    }

    /**
     * フラグメント作成.
     *
     * @param dlPath dlPaht
     * @param thiz thiz
     */
    public void onNoticeActDel(final String dlPath, final RecordedBaseFragment thiz) {
        if (null == mRecordedFragmentFactory) {
            return;
        }
        RecordedBaseFragment fra = mRecordedFragmentFactory.createFragment(0);
        if (null != fra) {
            if (fra == thiz) {
                return;
            }
            fra.setFragmentName(RLA_FragmentName_All);
            fra.delItemData(dlPath);
        }
    }

}
