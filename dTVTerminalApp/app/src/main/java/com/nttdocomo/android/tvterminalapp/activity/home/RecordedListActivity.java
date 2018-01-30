/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaProvDownload;
import com.nttdocomo.android.tvterminalapp.model.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.service.download.DlDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadService;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordedListActivity extends BaseActivity implements View.OnClickListener,
        RecordedBaseFragmentScrollListener, DlnaRecVideoListener, TabItemLayout.OnClickTabTextListener {

    private String[] mTabNames = null;

    private TabItemLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private ProgressBar progressBar;
    private Boolean mIsMenuLaunch = false;

    private DlnaProvRecVideo mDlnaProvRecVideo = null;
    private RecordedFragmentFactory mRecordedFragmentFactory = null;

    private final static int TEXT_SIZE = 15;

    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final int MARGIN_ZERO = 0;
    private static final int MARGIN_LEFT_TAB = 5;

    public static final String RECORD_LIST_KEY = "recordListKey";

    //設定するマージンのピクセル数
    private static final String DATE_FORMAT = "yyyy/MM/ddHH:mm:ss";
    private String mDate[] = {"日", "月", "火", "水", "木", "金", "土"};

    private static final String sMinus = "-";

    //private boolean mIsDlOk=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        //test beging
//        DownLoadListDataManager ddd = new DownLoadListDataManager(this);
//        ddd.deleteDownloadAllContents();
        //test end
        setContentView(R.layout.record_list_main_layout);
        setTitleText(getString(R.string.nav_menu_item_recorder_program));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        initDl();
        initView();
        getData();
        initTabVIew();
        setPagerAdapter();
        DTVTLogger.end();
    }

    private void initDl() {
        boolean isRunning= isDownloadServiceRunning();
        if(isRunning){
            //mIsDlOk=true;
        }else {
            //mIsDlOk= DlnaProvDownload.initGlobalDl(DownloaderBase.getDownloadPath(this));
            DlnaProvDownload.initGlobalDl(DownloaderBase.getDownloadPath(this));
        }
    }

    /**
     * 画面描画
     */
    private void initView() {
        DTVTLogger.start();
//        if (null != mViewPager) {
//            return;
//        }
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mViewPager = findViewById(R.id.record_list_main_layout_viewpagger);
        mTabNames = getResources().getStringArray(R.array.record_list_tab_names);
        mRecordedFragmentFactory = new RecordedFragmentFactory();
        progressBar = findViewById(R.id.record_list_main_layout_progress);
    }

    /**
     * タブの指定が持ち出しリストの時、持ち出しリストの生成を行う
     */
    private void setPagerAdapter() {
        DTVTLogger.start();
        mViewPager.setAdapter(new RecordedListActivity.MainAdpater(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
            }
        });
    }

    /**
     * 機能
     * タブの設定
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
    public void onClickTab(int position) {
        DTVTLogger.start("position = " + position);
        progressBar.setVisibility(View.VISIBLE);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * 機能
     * タブを切り替え
     *
     * @param position タブインデックス
     */
    private void setTab(int position) {
        DTVTLogger.start();
        if (mTabLayout != null) {
            mTabLayout.setTab(position);
        }
        progressBar.setVisibility(View.VISIBLE);
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
     * 持ち出しリスト生成
     */
    public void setRecordedTakeOutContents() {
        DTVTLogger.start();
        RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment();
        List<ContentsData> list = baseFragment.getContentsData();
        baseFragment.mContentsList = new ArrayList<>();
        if (list != null) {
            list.clear();
            List<Map<String, String>> resultList = getDownloadListFromDb();
            if(resultList != null && resultList.size() > 0){
                for (int i = 0; i < resultList.size(); i++) {
                    Map<String, String> hashMap = resultList.get(i);
                    String downloadStatus = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                    if(!TextUtils.isEmpty(downloadStatus)){
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
                        if(file.exists()){
                            ContentsData contentsData = new ContentsData();
                            contentsData.setTitle(title);
                            contentsData.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                            contentsData.setDlFileFullPath(fullPath);
                            list.add(contentsData);
                            RecordedContentsDetailData detailData = new RecordedContentsDetailData();
                            detailData.setItemId(itemId);
                            detailData.setUpnpIcon(upnpIcon);
                            detailData.setSize(totalSize);
                            detailData.setResUrl(url);
                            detailData.setResolution(resolution);
                            detailData.setBitrate(bitrate);
                            detailData.setDuration(duration);
                            detailData.setTitle(title);
                            detailData.setVideoType(videoType);
//                            detailData.setClearTextSize(totalSize);
                            baseFragment.mContentsList.add(detailData);
                        }
                    }
                }
            }
            baseFragment.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDlnaProvRecVideo != null) {
            mDlnaProvRecVideo.stopListen();
        }
    }

    /**
     * DMSデバイスを取り始める
     */
    private void getData() {
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        // 未ペアリング時
        if (dlnaDmsItem.mControlUrl.isEmpty()) {
            Toast.makeText(this, getString(R.string.main_setting_not_paring) ,Toast.LENGTH_SHORT).show();
            setProgressBarGone();
        } else {
            if (mDlnaProvRecVideo == null) {
                mDlnaProvRecVideo = new DlnaProvRecVideo();
            }
            if (mDlnaProvRecVideo.start(dlnaDmsItem, this)) {
                mDlnaProvRecVideo.browseRecVideoDms();
            } else {
                setProgressBarGone();
            }
        }
    }

    /**
     * フラグメント生成
     */
    private RecordedBaseFragment getCurrentRecordedBaseFragment(int... position) {
        DTVTLogger.start();
        int tabIndex;
        if(position != null && position.length > 0){
            tabIndex = position[0];
        } else {
            tabIndex = mViewPager.getCurrentItem();
        }
        return mRecordedFragmentFactory.createFragment(tabIndex, this);
    }

    public int getCurrentPosition(){
        return mViewPager.getCurrentItem();
    }

    /**
     * フラグメントをクリア
     */
    public void clearAllFragment() {
        DTVTLogger.start();
        if (null != mViewPager) {
            int sum = mRecordedFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                RecordedBaseFragment baseFragment = mRecordedFragmentFactory.createFragment(i, this);
                baseFragment.clear();
            }
        }
    }

    @Override
    public void onScroll(RecordedBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        DTVTLogger.start();
    }

    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {
        if (curInfo != null && curInfo.getRecordVideoLists() != null) {
            setVideoBrows(curInfo.getRecordVideoLists());
        }
        setProgressBarGone();
    }

    /**
     * 進捗バー閉じる
     */
    private void setProgressBarGone(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean isDownloadServiceRunning(){
        ActivityManager mActivityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager.getRunningServices(Integer.MAX_VALUE);
        return serviceIsStart(mServiceList, DownloadService.class.getName());
    }

    private boolean serviceIsStart(List<ActivityManager.RunningServiceInfo> list, String className) {
        for (int i = 0; i < list.size(); i++) {
            if (className.equals(list.get(i).service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private List<Map<String, String>> getDownloadListFromDb(){
        DlDataProvider dlDataProvider = new DlDataProvider(this);
        List<Map<String, String>> resultList = null;
        try {
            resultList = dlDataProvider.getDownloadListData();
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }

    private void setDownLoadQue(RecordedBaseFragment baseFrgament, ArrayList<DlnaRecVideoItem>  dlnaRecVideoItems, List<Map<String, String>> resultList){
        if(resultList != null){
            for(int k = 0; k < resultList.size(); k++){
                Map<String, String> hashMap = resultList.get(k);
                String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                for(int t = 0; t < dlnaRecVideoItems.size(); t++){
                    String allItemId = dlnaRecVideoItems.get(t).mItemId;
                    if(!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)){
                        allItemId = DownloaderBase.getFileNameById(dlnaRecVideoItems.get(t).mItemId);
                    }
                    if(itemId.equals(allItemId)){
                        String downloadStatus = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                        if(TextUtils.isEmpty(downloadStatus)){
                            baseFrgament.queIndex.add(t);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void setVideoBrows(ArrayList<DlnaRecVideoItem>  dlnaRecVideoItems) {
        final RecordedBaseFragment baseFrgament = getCurrentRecordedBaseFragment(0);
        baseFrgament.mContentsList = new ArrayList<>();
        List<Map<String, String>> resultList = getDownloadListFromDb();
        setTakeOutContentsToAll(dlnaRecVideoItems, resultList);
        List<ContentsData> listData = baseFrgament.getContentsData();
        listData.clear();
        if(baseFrgament.queIndex == null){
            baseFrgament.queIndex = new ArrayList<>();
        }
        baseFrgament.queIndex.clear();
        setDownLoadQue(baseFrgament, dlnaRecVideoItems, resultList);
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
            if(resultList != null && resultList.size() > 0){
                for (int j = 0; j < resultList.size(); j++) {
                    Map<String, String> hashMap = resultList.get(j);
                    String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                    String path = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL);
                    String fullPath = path + File.separator + itemId;
                    if(!TextUtils.isEmpty(itemId)){
                        String allItemId = itemData.mItemId;
                        if(!TextUtils.isEmpty(allItemId) && !allItemId.startsWith(DownloaderBase.sDlPrefix)){
                            allItemId = DownloaderBase.getFileNameById(itemData.mItemId);
                        }
                        if(itemId.equals(allItemId)){
                            String downloadStatus = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                            if(!TextUtils.isEmpty(downloadStatus)){
                                detailData.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                                detailData.setDlFileFullPath(fullPath);
                            } else {
                                detailData.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                            }
                        }
                    }
                }
            }
            baseFrgament.mContentsList.add(detailData);
            setNotifyData(baseFrgament, itemData, i);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                baseFrgament.notifyDataSetChanged();
                if(baseFrgament.queIndex.size() > 0){
                    baseFrgament.bindServiceFromBackgroud(isDownloadServiceRunning());
                }
            }
        });
    }

    private void setNotifyData(RecordedBaseFragment baseFrgament, DlnaRecVideoItem dlnaRecVideoItem, int i){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
        // TODO 年齢取得未実装の為、固定値を返却
        boolean isAge = true;
        ContentsData contentsData = new ContentsData();
        int currentPageNo = mViewPager.getCurrentItem();
        if (isAge) {
            contentsData.setTitle(dlnaRecVideoItem.mTitle);
            contentsData.setAllowedUse(dlnaRecVideoItem.mAllowedUse);
            String selectDate = "";
            if(!TextUtils.isEmpty(dlnaRecVideoItem.mDate)){
                String time = dlnaRecVideoItem.mDate.replaceAll("-", "/").replace("T", "");
                try {
                    Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                    calendar.setTime(sdf.parse(time));
                    StringUtils util = new StringUtils(this);
                    String[] strings = {String.valueOf(calendar.get(Calendar.MONTH)), "/",
                            String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), " (",
                            mDate[calendar.get(Calendar.DAY_OF_WEEK) - 1], ") ",
                            String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)), ":",
                            String.valueOf(calendar.get(Calendar.MINUTE))};
                    if(null != strings && 0<strings.length){
                        int mon = Integer.parseInt(strings[0]);
                        ++mon;
                        strings[0] = String.valueOf(mon);
                    }
                    selectDate = util.getConnectString(strings);
                } catch (ParseException e) {
                    DTVTLogger.debug(e);
                }
            }
            //duration && channel name begin
            String mins = dlnaRecVideoItem.getDurationInMinutes();

            String channelName = dlnaRecVideoItem.mChannelName;
            StringBuilder sb = new StringBuilder();
            sb.append(selectDate);
            if(null != mins){
                sb.append("（");
                sb.append(mins);
                sb.append("分）");
            }
//            if(null != channelName && !channelName.isEmpty()){
//                sb.append(" ");
//                sb.append(sMinus);
//                sb.append(" ");
//                sb.append(channelName);
//            }

            //duration && channel name end
            contentsData.setTime(sb.toString());
            contentsData.setDownloadFlg(baseFrgament.mContentsList.get(i).getDownLoadStatus());
            baseFrgament.getContentsData().add(contentsData);
        } else {
            // NOP
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
        boolean isRunning= isDownloadServiceRunning();
        if(!isRunning){
            DlnaProvDownload.uninitGlobalDl();
        }
    }

    private void registReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.DONWLOAD_UPDATE);
        filter.addAction(DownloadService.DONWLOAD_SUCCESS);
        filter.addAction(DownloadService.DONWLOAD_FAIL);
        registerReceiver(downloadReceiver, filter);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.DONWLOAD_UPDATE.equals(intent.getAction())) {
                int progress = intent.getIntExtra(DownloadService.DONWLOAD_UPDATE, 0);
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                baseFragment.setDownladProgressByBg(progress);
            } else if (DownloadService.DONWLOAD_SUCCESS.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_PATH);
                baseFragment.setDownladSuccessByBg(fullPath);
            } else if (DownloadService.DONWLOAD_FAIL.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_PATH);
                baseFragment.setDownladFailByBg(fullPath);
            } else if (DownloadService.DONWLOAD_LowStorageSpace.equals(intent.getAction())) {
                RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment(0);
                String fullPath = intent.getStringExtra(DownloadService.DONWLOAD_PATH);
                baseFragment.onLowStorageSpace(fullPath);
            }
        }
    };

    @Override
    public void onDeviceLeave(DlnaDMSInfo curInfo, String leaveDmsUdn) {
        DTVTLogger.start();
    }

    private Handler mHandler = new Handler();
    @Override
    public void onError(String msg) {
        DTVTLogger.start(msg);
        final String msg2 = msg;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                showMessage(msg2);
            }
        });
        setProgressBarGone();
    }

    /**
     * showMessage
     * @param msg
     */
    private void showMessage(String msg) {
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
     * 検索結果タブ専用アダプター
     */
    private class MainAdpater extends FragmentStatePagerAdapter {

        MainAdpater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (this) {
                DTVTLogger.start();
                return mRecordedFragmentFactory.createFragment(position, RecordedListActivity.this);
            }
        }

        @Override
        public int getCount() {
            DTVTLogger.start();
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            DTVTLogger.start();
            return mTabNames[position];
        }
    }

    /**
     * [すべて]のListと[持ち出し]Listをマージ
     *
     * @return list　生成した[すべて]一覧に表示するリスト
     */
    private ArrayList<DlnaRecVideoItem> setTakeOutContentsToAll(ArrayList<DlnaRecVideoItem> list, List<Map<String, String>> takeoutList) {
        // 返却するリスト
        ArrayList<DlnaRecVideoItem> allList = list;
        if(allList == null){
            allList = new ArrayList<>();
        }
        if (takeoutList != null) {
            for (int i = 0; i < takeoutList.size(); i++) {
                Map<String, String> hashMap = takeoutList.get(i);
                String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                boolean isExist = false;
                if(!TextUtils.isEmpty(itemId)){
                    for (int j = 0; j < allList.size(); j++) {
                        String allItemId = DownloaderBase.getFileNameById(allList.get(j).mItemId);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}