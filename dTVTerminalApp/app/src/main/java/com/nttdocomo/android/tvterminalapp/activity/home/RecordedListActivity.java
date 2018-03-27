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
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaProvDownload;
import com.nttdocomo.android.tvterminalapp.service.download.DlDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadListener;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadService;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
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
        DlnaRecVideoListener, TabItemLayout.OnClickTabTextListener {

    /**
     * タブ名.
     */
    private String[] mTabNames = null;
    /**
     * タブレイアウト.
     */
    private TabItemLayout mTabLayout = null;
    /**
     * viewpager.
     */
    private ViewPager mViewPager = null;
    /**
     * 進捗バー.
     */
    private ProgressBar progressBar;
    /**
     * 遷移先（メニュー）.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * DLNA 関連クラス.
     */
    private DlnaProvRecVideo mDlnaProvRecVideo = null;
    /**
     * Fragment作成クラス.
     */
    private RecordedFragmentFactory mRecordedFragmentFactory = null;
    /**
     * 値渡すキー.
     */
    public static final String RECORD_LIST_KEY = "recordListKey";
    /**
     * 日付フォーマット.
     */
    public static final String sMinus = "-";
    /**
     * タブ名.
     */
	public static final String RLA_FragmentName_All = "all";
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.record_list_main_layout);
        setTitleText(getString(R.string.nav_menu_item_recorder_program));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        registReceiver();
        initView();
        initTabVIew();
        setPagerAdapter();

        DTVTLogger.end();
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
        mTabNames = getResources().getStringArray(R.array.record_list_tab_names);
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
            }
        });
    }

    /**
     * 機能
     * タブの設定.
     */
    private void initTabVIew() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.RECORDED_LIST_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_recorded_list_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(final int position) {
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            DTVTLogger.start("position = " + position);
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
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
        showProgressBar(true);
        mNoDataMessage.setVisibility(View.GONE);
        switch (mViewPager.getCurrentItem()) {
            case 0:
                getData();
                break;
            case 1:
                setRecordedTakeOutContents();
                break;
            default:
                break;
        }
    }

    /**
     * プロセスバーを表示する.
     *
     * @param showProgressBar プロセスバーを表示するかどうか
     */
    private void showProgressBar(final boolean showProgressBar) {
        if (showProgressBar) {
            //オフライン時は表示しない
            if (!NetWorkUtils.isOnline(this)) {
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
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
                    String downloadStatus = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                    if (!TextUtils.isEmpty(downloadStatus)) {
                        String path = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL);
                        String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                        String title = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_TITLE);
                        String url = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_URL);
                        String duration = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DURATION);
                        String totalSize = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_SIZE);
                        String resolution = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_RESOLUTION);
                        String upnpIcon = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_UPNP_ICON);
                        String bitrate = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_BITRATE);
                        String videoType = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_TYPE);

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
        showProgressBar(false);
    }

    @Override
    protected void onPause() {
        if (mDlnaProvRecVideo != null) {
            mDlnaProvRecVideo.stopListen();
        }
        super.onPause();
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
            if (mDlnaProvRecVideo == null) {
                mDlnaProvRecVideo = new DlnaProvRecVideo();
            }
            if (mDlnaProvRecVideo.start(dlnaDmsItem, this)) {
                clearFragment(0);
                boolean res = mDlnaProvRecVideo.browseRecVideoDms();
                if (!res) {
                    DTVTLogger.debug("browseRecVideoDms false");
                }
            } else {
                showGetDataFailedToast();
                mNoDataMessage.setVisibility(View.VISIBLE);
                setProgressBarGone();
            }
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

    @Override
    public void onVideoBrows(final DlnaRecVideoInfo curInfo) {
        if (curInfo != null && curInfo.getRecordVideoLists() != null) {
            setVideoBrows(curInfo.getRecordVideoLists());
        } else {
            showGetDataFailedToast();
            mNoDataMessage.setVisibility(View.VISIBLE);
        }
        setProgressBarGone();
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
        DlDataProvider dlDataProvider = new DlDataProvider(this);
        return dlDataProvider.getDownloadListData();
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
        if (resultList != null) {
            for (Map<String, String> hashMap : resultList) {
                String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                for (int t = 0; t < dlnaRecVideoItems.size(); t++) {
                    String allItemId = dlnaRecVideoItems.get(t).mItemId;
                    if (!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)) {
                        allItemId = DownloaderBase.getFileNameById(dlnaRecVideoItems.get(t).mItemId);
                    }
                    if (itemId.equals(allItemId)) {
                        String downloadStatus = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                        if (TextUtils.isEmpty(downloadStatus)) {
                            baseFragment.queIndex.add(t);
                            break;
                        }
                    }
                }
            }
        }
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
        if (baseFragment.queIndex == null) {
            baseFragment.queIndex = new ArrayList<>();
        }
        baseFragment.queIndex.clear();
        setDownLoadQue(baseFragment, dlnaRecVideoItems, resultList);
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
                    String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                    String path = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL);
                    String fullPath = path + File.separator + itemId;
                    if (!TextUtils.isEmpty(itemId)) {
                        String allItemId = itemData.mItemId;
                        if (!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)) {
                            allItemId = DownloaderBase.getFileNameById(itemData.mItemId);
                        }
                        if (itemId.equals(allItemId)) {
                            String downloadStatus = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
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
            setNotifyData(baseFragment, itemData, i, detailData.getDlFileFullPath());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                baseFragment.notifyDataSetChanged();
                if (baseFragment.queIndex.size() > 0) {
                    baseFragment.bindServiceFromBackground();
                }
            }
        });
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
                               final int i, final String fullDlPath) {
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

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        if (progressBar != null) {
            showProgressBar(true);
            mNoDataMessage.setVisibility(View.GONE);
        }
        initDl();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
        boolean isRunning = isDownloadServiceRunning();
        if (!isRunning) {
            DlnaProvDownload.uninitGlobalDl();
        }
    }

    /**
     * レシーバー登録.
     */
    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.DONWLOAD_OnProgress);
        filter.addAction(DownloadService.DONWLOAD_OnSuccess);
        filter.addAction(DownloadService.DONWLOAD_OnFail);
        filter.addAction(DownloadService.DONWLOAD_OnLowStorageSpace);
        filter.addAction(DownloadService.DONWLOAD_OnCancelAll);
        filter.addAction(DownloadService.DONWLOAD_OnCancel);
        filter.addAction(DownloadService.DONWLOAD_OnStart);
        filter.addAction(DownloadService.DONWLOAD_DlDataProviderAvailable);
        filter.addAction(DownloadService.DONWLOAD_DlDataProviderUnavailable);
        registerReceiver(downloadReceiver, filter);
    }

    /**
     * ダウンロード状態通知.
     */
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
	    public void onReceive(final Context context, final Intent intent) {
            if (DownloadService.DONWLOAD_OnProgress.equals(intent.getAction())) {
                int progress = intent.getIntExtra(DownloadService.DONWLOAD_ParamInt, 0);
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onDownloadProgressByBg(progress);
            } else if (DownloadService.DONWLOAD_OnSuccess.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_ParamString);
                baseFragment.onDownloadSuccessByBg(fullPath);
            } else if (DownloadService.DONWLOAD_OnFail.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_ParamString);
                int error = intent.getIntExtra(DownloadService.DONWLOAD_ParamInt, DownloadListener.DLError.DLError_NoError.ordinal());
                baseFragment.onDownloadFailByBg(fullPath);
            } else if (DownloadService.DONWLOAD_OnLowStorageSpace.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_ParamString);
                baseFragment.onLowStorageSpaceByBg(fullPath);
            }  else if (DownloadService.DONWLOAD_OnCancelAll.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_ParamString);
                baseFragment.onCancelAll(fullPath);
            }  else if (DownloadService.DONWLOAD_OnCancel.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_ParamString);
                baseFragment.onCancelByBg(fullPath);
            } else if (DownloadService.DONWLOAD_OnStart.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onStartBg();
            } else if (DownloadService.DONWLOAD_DlDataProviderAvailable.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onDlDataProviderAvailable();
            }  else if (DownloadService.DONWLOAD_DlDataProviderUnavailable.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.onDlDataProviderUnavailable();
            }
        }
    };

    @Override
    public void onDeviceLeave(final DlnaDMSInfo curInfo, final String leaveDmsUdn) {
        DTVTLogger.start();
    }

    /**
     * エラーを返すハンドラー.
     */
    private Handler mHandler = new Handler();
    @Override
    public void onError(final String msg) {
        DTVTLogger.start(msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showMessage(getApplicationContext().getString(R.string.common_get_data_failed_message));
                    }
                });
                mNoDataMessage.setVisibility(View.VISIBLE);
                setProgressBarGone();
            }
        });
    }

    /**
     * showMessage.
     * @param msg msg
     */
    private void showMessage(final String msg) {
        DTVTLogger.start();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        DTVTLogger.end();
    }

    @Override
    public String getCurrentDmsUdn() {
        DTVTLogger.start();
        return null;
    }

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
                String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
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
                        String bitrate = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_BITRATE);
                        String duration = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DURATION);
                        String title = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_TITLE);
                        String totalSize = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_SIZE);
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

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合はアプリ終了ダイアログを表示
                    showTips();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
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