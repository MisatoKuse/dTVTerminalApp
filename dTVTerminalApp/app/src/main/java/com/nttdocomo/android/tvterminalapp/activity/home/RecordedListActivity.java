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
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.dlna.DlnaContentRecordedDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadData;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadListener;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadService;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.DownloadComparator;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 録画番組.
 */
public class RecordedListActivity extends BaseActivity implements View.OnClickListener,
        TabItemLayout.OnClickTabTextListener, RecordedBaseFragment.ScrollListenerCallBack,

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
    /** プロバイダー. */
    private DlnaContentRecordedDataProvider mDlnaContentRecordedDataProvider;
    /** Fragment作成クラス. */
    private RecordedFragmentFactory mRecordedFragmentFactory = null;
    /** 値渡すキー. */
    public static final String RECORD_LIST_KEY = "recordListKey";
    /** 日付フォーマット. */
    public static final String sMinus = "-";
    /** タブ名. */
    public static final String RLA_FragmentName_All = "all";
    /** string　format. */
    private static final String STR_SPACE = " ";
    /** リスト0件メッセージ. */
    private TextView mNoDataMessage;
    /** すべて. */
    private static final int ALL_RECORD_LIST = 0;
    /** ダウンロード済み. */
    private static final int DOWNLOAD_OVER = 1;
    /** 前回のタブポジション.*/
    private static final String START_TAB_POSITION = "startTabPosition";
    /** ハンドラー(ScrollView関連). */
    private final Handler mHandler = new Handler();
    /** 上にスクロール. */
    private boolean mIsLoading = false;
    /** ロード終了. */
    private boolean mIsEndPage = false;
    /** ページングインデックス.*/
    private int mRequestIndex;
    /**ダウンロードXML取得フォーマット.*/
    private static final String TAG_ITEM_START = "<item id=\"";
    /**ダウンロードXML取得フォーマット.*/
    private static final String TAG_ITEM_END = "</item>";
    /**ダウンロードXML取得フォーマット.*/
    private static final String TAG_DIDL_START = "<DIDL-Lite";
    /**ダウンロードXML取得フォーマット.*/
    private static final String TAG_DIDL_END = "</DIDL-Lite>";
    /**前回のDLNAコンテンツリスト.*/
    private ArrayList<DlnaRecVideoItem> mDlnaRecVideoItems = null;
    /**前回のDLNAコンテンツリストキーワード.*/
    private static final String ITEMS_MEMORY = "itemsMemory";
    /**年齢.*/
    private int mAgeReq;
    /**年齢フォマット.*/
    private static final String FORMAT_0X = "0x";
    /**年齢フォマット.*/
    private static final int FORMAT_16_NUM = 16;
    /**年齢フォマット.*/
    private static final int FORMAT_16_START_POSITION = 2;

    @SuppressWarnings("OverlyLongMethod")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list_main_layout);
        setTitleText(getString(R.string.nav_menu_item_recorder_program));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);
        registReceiver();
        initView();
        initTabView();
        setPagerAdapter();
        if (savedInstanceState != null) {
            int startPageNo = savedInstanceState.getInt(START_TAB_POSITION);
            mDlnaRecVideoItems = (ArrayList<DlnaRecVideoItem>) savedInstanceState.getSerializable(ITEMS_MEMORY);
            savedInstanceState.clear();
            mViewPager.setCurrentItem(startPageNo);
            mTabLayout.setTab(startPageNo);
        } else {
            int startPageNo;
            switch (StbConnectionManager.shared().getConnectionStatus()) {
                case NONE_PAIRING:
                case NONE_LOCAL_REGISTRATION:
                case HOME_OUT:
                    startPageNo = DOWNLOAD_OVER;
                    break;
                case HOME_OUT_CONNECT:
                case HOME_IN:
                default:
                    startPageNo = ALL_RECORD_LIST;
                    break;
            }
            mViewPager.setCurrentItem(startPageNo);
            mTabLayout.setTab(startPageNo);
        }
        mDlnaContentRecordedDataProvider = new DlnaContentRecordedDataProvider();
        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(this);
        mAgeReq = userInfoDataProvider.getUserAge();
        showProgressBar();
        mNoDataMessage.setVisibility(View.GONE);
        DlnaManager.shared().clearQue();
        downloadStatusCheck();
        getData();
        DTVTLogger.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        sendScreenViewForPosition(getCurrentPosition(), false);
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(START_TAB_POSITION, getCurrentPosition());
        outState.putSerializable(ITEMS_MEMORY, mDlnaRecVideoItems);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDlnaContentRecordedDataProvider.stopListen();
        if (mIsLoading) {
            final RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
            baseFragment.loadComplete();
            mIsLoading = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadReceiver);
        boolean isRunning = isDownloadServiceRunning();
        if (!isRunning) {
//            DlnaProvDownload.uninitGlobalDl();
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (closeDrawerMenu()) {
                    return false;
                }
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
        requestInit();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onBrowseCallback(final DlnaObject[] objs, final boolean isComplete) {
        if (mViewPager.getCurrentItem() != ALL_RECORD_LIST) {
            return;
        }
        setProgressBarGone();
        ArrayList<DlnaRecVideoItem> dstList = new ArrayList<>();
        for (DlnaObject dlnaObject: objs) {
            DlnaRecVideoItem item = new DlnaRecVideoItem();
            item.mItemId = modifyObjectId(dlnaObject.mObjectId);
            item.mSize = dlnaObject.mSize;
            item.mResUrl = dlnaObject.mResUrl;
            item.mResolution = dlnaObject.mResolution;
            item.mBitrate = dlnaObject.mBitrate;
            item.mDuration = dlnaObject.mDuration;
            item.mTitle = dlnaObject.mTitle;
            item.mVideoType = dlnaObject.mVideoType;
            item.mClearTextSize = dlnaObject.mCleartextSize;
            item.mXml = dlnaObject.mXml;
            item.mChannelName = dlnaObject.mChannelName;
            item.mDate = dlnaObject.mDate;
            item.mRating = dlnaObject.mRating;
            dstList.add(item);
        }
        mDlnaRecVideoItems = dstList;
        if (isComplete) {
            mIsEndPage = true;
        }
        setVideoBrows(dstList, false);
        mRequestIndex = mRequestIndex + dstList.size();
    }

    @Override
    public void onBrowseErrorCallback() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setProgressBarGone();
                showGetDataFailedToast();
                setVideoBrows(null, false);
                if (mNoDataMessage.getVisibility() == View.VISIBLE) {
                    mNoDataMessage.setText(getString(R.string.common_get_data_failed_message));
                }
            }
        });
    }

    @Override
    public void onRemoteConnectTimeOutCallback() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setProgressBarGone();
                showGetDataFailedToast();
                setVideoBrows(null, false);
                if (mNoDataMessage.getVisibility() == View.VISIBLE) {
                    mNoDataMessage.setText(getString(R.string.common_get_data_failed_message));
                }
            }
        });
    }

    /**
     * リモート視聴の場合もオブジェクトID統一するようを編集.
     *
     * @param objectId オブジェクトID
     * @return 編集後のオブジェクトID
     */
    private String modifyObjectId(final String objectId) {
        if (TextUtils.isEmpty(objectId)) {
            return objectId;
        }
        return objectId.replace(DlnaUtils.IMAGE_QUALITY_HIGH_URL, DlnaUtils.IMAGE_QUALITY_DEFAULT_URL)
                .replace(DlnaUtils.IMAGE_QUALITY_MIDDLE_URL, DlnaUtils.IMAGE_QUALITY_DEFAULT_URL)
                .replace(DlnaUtils.IMAGE_QUALITY_LOW_URL, DlnaUtils.IMAGE_QUALITY_DEFAULT_URL);
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
                requestInit();
                setTab(position);
                sendScreenViewForPosition(position, true);
            }
        });
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     * @param position タブインデックス
     * @param isFromTab true:タブ切り替え false:onResume
     */
    private void sendScreenViewForPosition(final int position, final boolean isFromTab) {
        String screenName = null;
        switch (position) {
            case ALL_RECORD_LIST:
                screenName = getString(R.string.google_analytics_screen_name_recording_list);
                break;
            case DOWNLOAD_OVER:
                screenName = getString(R.string.google_analytics_screen_name_recording_list_takeout);
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(screenName)) {
            if (!isFromTab && mIsFromBgFlg) {
                super.sendScreenView(screenName, ContentUtils.getParingAndLoginCustomDimensions(RecordedListActivity.this));
            } else {
                SparseArray<String> customDimensions  = new SparseArray<>();
                customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
                super.sendScreenView(screenName, customDimensions);
            }
        }
    }

    /**
     * 機能
     * タブの設定.
     */
    private void initTabView() {
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
        setProgressBarGone();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showGetDataFailedToast();
                setVideoBrows(null, false);
                if (mNoDataMessage.getVisibility() == View.VISIBLE) {
                    mNoDataMessage.setText(getString(R.string.common_get_data_failed_message));
                }
            }
        });
    }

    /**
     * タブを切り替え.
     * @param position タブインデックス
     */
    private void setTab(final int position) {
        DTVTLogger.start();
        if (mTabLayout != null) {
            mTabLayout.setTab(position);
        }
        showProgressBar();
        mNoDataMessage.setVisibility(View.GONE);
        getData();
    }

    /**
     * プロセスバーを表示する.
     */
    private void showProgressBar() {
        //オフライン時は表示しない
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 持ち出しリスト生成.
     */
    @SuppressWarnings("OverlyLongMethod")
    public void setRecordedTakeOutContents() {
        DTVTLogger.start();
        final RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment();
        List<ContentsData> list = baseFragment.getContentsData();

        baseFragment.clearContentsList();

        if (list != null) {
            list.clear();
            List<Map<String, String>> resultList = getDownloadListFromDb();
            List<String> pathList = DownloaderBase.getDownloadPathList(RecordedListActivity.this);
            if (resultList != null && resultList.size() > 0) {
                for (Map<String, String> hashMap : resultList) {
                    String downloadStatus = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                    if (!TextUtils.isEmpty(downloadStatus)) {
                        String itemId = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                        String title = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_TITLE);
                        String url = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_URL);
                        String duration = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DURATION);
                        String totalSize = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_SIZE);
                        String resolution = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_RESOLUTION);
                        String upnpIcon = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_UPNP_ICON);
                        String bitrate = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_BITRATE);
                        String videoType = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_TYPE);
                        String channelName = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_CHANNELNAME);
                        String date = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DATE);

                        String fullPath = "";
                        boolean isExists = false;
                        for (String dlFilePath : pathList) {
                            File file = new File(dlFilePath + File.separator + itemId);
                            if (file.exists()) {
                                fullPath = dlFilePath + File.separator + itemId;
                                isExists = true;
                                break;
                            }
                        }
                        if (isExists && !checkDataIsExist(itemId, baseFragment)) {
                            ContentsData contentsData = new ContentsData();
                            contentsData.setTitle(title);
                            contentsData.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                            contentsData.setDlFileFullPath(fullPath);
                            DlnaRecVideoItem dlnaRecVideoItem = new DlnaRecVideoItem();
                            dlnaRecVideoItem.mDuration = duration;
                            dlnaRecVideoItem.mDate = date;
                            dlnaRecVideoItem.mChannelName = channelName;
                            contentsData.setTime(getDownloadTitle(dlnaRecVideoItem));
                            contentsData.setPublishEndDate(date);
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
                            detailData.setRecordedChannelName(channelName);
                            detailData.setDate(date);
                            detailData.setTitle(title);
                            detailData.setVideoType(videoType);
                            detailData.setClearTextSize(totalSize);
                            baseFragment.addContentsList(detailData);
                        }
                    }
                }
            } else {
                if (DOWNLOAD_OVER == mViewPager.getCurrentItem()) {
                    mNoDataMessage.setVisibility(View.VISIBLE);
                    mNoDataMessage.setText(getString(R.string.common_empty_data_message));
                }
            }
            if (list.size() > 0) {
                Collections.sort(list, new DownloadComparator());
                Collections.sort(baseFragment.getContentsList(), new DownloadComparator());
            }
        }
        if (DOWNLOAD_OVER == mViewPager.getCurrentItem()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    baseFragment.notifyDataSetChanged();
                    if (baseFragment.getContentsData().size() == 0) {
                        mNoDataMessage.setVisibility(View.VISIBLE);
                        mNoDataMessage.setText(getString(R.string.common_empty_data_message));
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    }

    /**
     * ページング処理を初期化する.
     */
    public void requestInit() {
        mRequestIndex = 0;
        mDlnaRecVideoItems = null;
        mIsEndPage = false;
    }

    /**
     * DMSデバイスを取り始める.
     */
    public void getData() {
        if (mRequestIndex == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressBar != null && progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        mHandler.post(new Runnable() {
            @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
            @Override
            public void run() {
                switch (mViewPager.getCurrentItem()) {
                    case ALL_RECORD_LIST:
                        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(RecordedListActivity.this);
                        // 未ペアリング時
                        if (dlnaDmsItem.mControlUrl.isEmpty()) {
                            clearFragment(0);
                            setProgressBarGone();
                            setVideoBrows(null, false);
                        } else {
                            switch (StbConnectionManager.shared().getConnectionStatus()) {
                                case HOME_OUT:
                                case HOME_IN:
                                case HOME_OUT_CONNECT:
                                    if (mRequestIndex == 0 && !mIsLoading) {
                                        clearFragment(0);
                                        setRecordedTakeOutContents();
                                    }
                                    mDlnaContentRecordedDataProvider.listen(RecordedListActivity.this);
                                    mDlnaContentRecordedDataProvider.browse(RecordedListActivity.this, mRequestIndex);
                                    break;
                                case NONE_LOCAL_REGISTRATION:
                                    if (mDlnaRecVideoItems != null) {
                                        setRecordedTakeOutContents();
                                        setVideoBrows(mDlnaRecVideoItems, true);
                                    } else {
                                        clearFragment(0);
                                        setRecordedTakeOutContents();
                                        setVideoBrows(null, false);
                                    }
                                    setProgressBarGone();
                                    break;
                                case NONE_PAIRING:
                                default:
                                    clearFragment(0);
                                    setVideoBrows(null, false);
                                    setProgressBarGone();
                                    break;
                            }
                        }
                        break;
                    case DOWNLOAD_OVER:
                        //ダウンロードキューを保存する
                        RecordedBaseFragment fragment = getCurrentRecordedBaseFragment(0);
                        if (fragment != null) {
                            DownloadDataProvider downloadDataProvider = fragment.getDownloadDataProvider();
                            List<DownloadData> downloadQueue = fragment.getDownloadQueue();
                            if (downloadDataProvider != null && downloadQueue != null && downloadQueue.size() > 0) {
                                downloadDataProvider.setQue(downloadQueue);
                            }
                        }
                        setRecordedTakeOutContents();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * タブサイズ取得.
     * @return タブポジション
     */
    public int getTabPosition() {
        return mViewPager.getCurrentItem();
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecordedBaseFragment baseFragment = mRecordedFragmentFactory.createFragment(tabNo);
                    baseFragment.clear();
                    baseFragment.setIsUiRunning(false);
                    baseFragment.notifyDataSetChanged();
                }
            });
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
     * 接続ステータス取得.
     * @return true:宅外　false:宅内
     */
    private boolean getConnectionStatus() {
        return StbConnectionManager.shared().getConnectionStatus() != StbConnectionManager.ConnectionStatus.HOME_IN;
    }

    /**
     * xmlパラメータ取得.
     * @param xmlDidl xml
     * @param itemId アイテムID
     * @return ダウンロードパラメータ
     */
    private String getXmlToDl(final String xmlDidl, final String itemId) {
        String xml = xmlDidl;
        if (!TextUtils.isEmpty(xml)) {
            int startDp = xml.indexOf(TAG_DIDL_START);
            if (startDp == -1) {
                return null;
            }
            int startIt = xml.indexOf(TAG_ITEM_START);
            if (startIt == -1) {
                return null;
            }
            String result = xml.substring(startDp, startIt);
            String begin = TAG_ITEM_START + itemId;
            int startSelectIt = xml.indexOf(begin);
            if (startSelectIt == -1) {
                return null;
            }
            xml = xml.substring(startSelectIt);
            int endIt = xml.indexOf(TAG_ITEM_END);
            if (endIt == -1) {
                return null;
            }
            result = result + xml.substring(0, endIt);
            return result + TAG_ITEM_END + TAG_DIDL_END;
        }
        return null;
    }

    /**
     * 年齢制限チェック.
     *
     * @param rating 年齢情報
     * @return true:制限された
     */
    private boolean checkIsOverRating(final String rating) {
        if (TextUtils.isEmpty(rating)) {
            return false;
        }
        int resAge;
        if (rating.contains(FORMAT_0X)) { // 16進数
            try {
                resAge = Integer.parseInt(rating.substring(FORMAT_16_START_POSITION), FORMAT_16_NUM);
            } catch (NumberFormatException e) {
                DTVTLogger.debug(e);
                return false;
            }
        } else { // 10進数
            try {
                resAge = Integer.parseInt(rating);
            } catch (NumberFormatException e) {
                DTVTLogger.debug(e);
                return false;
            }
        }
        return mAgeReq < resAge;
    }

    /**
     * VideoBrowsの設定.
     *
     * @param dlnaRecVideoItems 録画ビデオアイテム
     * @param isFromMemory 録画ビデオアイテム
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private void setVideoBrows(final ArrayList<DlnaRecVideoItem> dlnaRecVideoItems, final boolean isFromMemory) {
        DTVTLogger.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                if (dlnaRecVideoItems != null) {
                    baseFragment.setFragmentName(RLA_FragmentName_All);
                    List<Map<String, String>> resultList = getDownloadListFromDb();
                    baseFragment.clearQueueIndex();
                    boolean hideDownloadBtn = getConnectionStatus();
                    if (isFromMemory) {
                        hideDownloadBtn = false;
                    }
                    for (int i = 0; i < dlnaRecVideoItems.size(); i++) {
                        DlnaRecVideoItem itemData = dlnaRecVideoItems.get(i);
                        if (checkIsOverRating(itemData.mRating)) {
                            continue;
                        }
                        String checkItemId = itemData.mItemId;
                        if (!TextUtils.isEmpty(checkItemId) && !checkItemId.startsWith(DownloaderBase.sDlPrefix)) {
                            checkItemId = DownloaderBase.getFileNameById(checkItemId);
                        }
                        if (checkDataIsExist(checkItemId, baseFragment)) {
                            continue;
                        }
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
                        detailData.setXml(getXmlToDl(dlnaRecVideoItems.get(0).mXml, itemData.mItemId));
                        detailData.setRecordedChannelName(itemData.mChannelName);
                        detailData.setDate(itemData.mDate);
                        if (hideDownloadBtn) {
                            detailData.setIsRemote(true);
                        }
                        if (resultList != null && resultList.size() > 0) {
                            for (Map<String, String> hashMap : resultList) {
                                String itemId = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                                if (!TextUtils.isEmpty(itemId)) {
                                    String allItemId = itemData.mItemId;
                                    if (!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)) {
                                        allItemId = DownloaderBase.getFileNameById(itemData.mItemId);
                                    }
                                    if (itemId.equals(allItemId)) {
                                        String downloadStatus = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                                        if (TextUtils.isEmpty(downloadStatus)) {
                                            detailData.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                                        }
                                    }
                                }
                            }
                        }
                        baseFragment.addContentsList(detailData);
                        setNotifyData(baseFragment, itemData, detailData.getDlFileFullPath(), detailData.getDownLoadStatus(), hideDownloadBtn);
                    }
                    setDownLoadQue(baseFragment, baseFragment.getContentsList(), resultList);
                }
                if (baseFragment.getContentsData().size() == 0) {
                    mNoDataMessage.setVisibility(View.VISIBLE);
                    mNoDataMessage.setText(getString(R.string.common_empty_data_message));
                    return;
                }
                baseFragment.setIsUiRunning(true);
                baseFragment.notifyDataSetChanged();
                if (mRequestIndex != 0) {
                    baseFragment.loadComplete();
                    mIsLoading = false;
                }
                if (baseFragment.getQueueIndexSize() > 0) {
                    baseFragment.bindServiceFromBackground();
                }
            }
        });
        DTVTLogger.end();
    }

    /**
     * ダウンロードキューセット.
     *
     * @param baseFragment baseFragment
     * @param dlnaRecVideoItems アイテム
     * @param resultList コンテンツリスト
     */
    private void setDownLoadQue(final RecordedBaseFragment baseFragment,
                                final List<RecordedContentsDetailData> dlnaRecVideoItems, final List<Map<String, String>> resultList) {
        if (resultList == null) {
            return;
        }

        for (Map<String, String> hashMap : resultList) {
            String itemId = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
            for (int t = 0; t < dlnaRecVideoItems.size(); t++) {
                String allItemId = dlnaRecVideoItems.get(t).getItemId();
                if (!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)) {
                    allItemId = DownloaderBase.getFileNameById(dlnaRecVideoItems.get(t).getItemId());
                }
                if (itemId.equals(allItemId)) {
                    String downloadStatus = hashMap.get(DataBaseConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                    if (TextUtils.isEmpty(downloadStatus)) {
                        baseFragment.addQueueIndex(t);
                        break;
                    }
                }
            }
        } // for resultList
    }

    /**
     * 表示の更新.
     *
     * @param baseFragment baseFragment
     * @param dlnaRecVideoItem 録画ビデオアイテム
     * @param fullDlPath フルパス
     * @param downloadStatus ダウンロードステータス
     * @param hideDownloadBtn ダウンロードボタン表示非表示
     */
    private void setNotifyData(final RecordedBaseFragment baseFragment, final DlnaRecVideoItem dlnaRecVideoItem,
                               final String fullDlPath, final int downloadStatus, final boolean hideDownloadBtn) {
        ContentsData contentsData = new ContentsData();
        contentsData.setTitle(dlnaRecVideoItem.mTitle);
        contentsData.setAllowedUse(dlnaRecVideoItem.mAllowedUse);
        contentsData.setDownloadBtnHide(hideDownloadBtn);
        //duration && channel name end
        contentsData.setTime(getDownloadTitle(dlnaRecVideoItem));
        contentsData.setDownloadFlg(downloadStatus);
        contentsData.setDlFileFullPath(fullDlPath);
        List<ContentsData> l = baseFragment.getContentsData();
        if (null != l) {
            l.add(contentsData);
        }
    }

    /**
     * レシーバー登録.
     * @param dlnaRecVideoItem 録画ビデオアイテム
     * @return タイトル
     */
    private String getDownloadTitle(final DlnaRecVideoItem dlnaRecVideoItem) {
        String selectDate = DateUtils.getDownloadDateFormat(dlnaRecVideoItem.mDate, this);
        //duration && channel name begin
        String mins = dlnaRecVideoItem.getDurationInMinutes();

        String channelName = dlnaRecVideoItem.mChannelName;
        StringBuilder sb = new StringBuilder();
        sb.append(selectDate);
        if (null != mins) {
            sb.append(getString(R.string.common_contents_front_bracket));
            sb.append(mins);
            sb.append(getString(R.string.common_contents_min));
            sb.append(getString(R.string.common_contents_end_bracket));
        }
        if (null != channelName && !channelName.isEmpty()) {
            sb.append(STR_SPACE);
            sb.append(sMinus);
            sb.append(STR_SPACE);
            sb.append(channelName);
        }
        return sb.toString();
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
    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
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
                    f.setScrollListener(RecordedListActivity.this);
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
            if (fra.equals(thiz)) {
                return;
            }
            fra.setFragmentName(RLA_FragmentName_All);
            fra.delItemData(dlPath);
        }
    }

    @Override
    public void onScroll(final RecordedBaseFragment fragment, final AbsListView absListView,
                         final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(final RecordedBaseFragment fragment, final AbsListView absListView, final int scrollState) {
        if (mViewPager.getCurrentItem() == DOWNLOAD_OVER || mIsEndPage) {
            return;
        }
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == fragment.getDataCount() - 1
                    && !mIsLoading) {
                if (mViewPager.getCurrentItem() == ALL_RECORD_LIST) {
                    if (fragment.getContentsListSize() > 0 && (fragment.getContentsListElement(0).isRemote() == getConnectionStatus())) {
                        mIsLoading = true;
                        mNoDataMessage.setVisibility(View.GONE);
                        fragment.loadStart();
                        getData();
                    } else {
                        mRequestIndex = 0;
                        setTab(0);
                    }
                }
            }
        }
    }

    /**
     * コンテンツ存在チェック.
     *
     * @param itemID コンテンツID
     * @param baseFragment フラグメント
     * @return 存在の場合true
     */
    private boolean checkDataIsExist(final String itemID, final RecordedBaseFragment baseFragment) {
        boolean isExist = false;
        if (TextUtils.isEmpty(itemID)) {
            isExist = true;
            return isExist;
        }
        for (int i = 0; i < baseFragment.getContentsListSize(); i++) {
            RecordedContentsDetailData detailDataInList = baseFragment.getContentsListElement(i);
            if (itemID.equals(detailDataInList.getItemId())) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
}
