/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
import com.nttdocomo.android.tvterminalapp.service.download.DlData;
import com.nttdocomo.android.tvterminalapp.service.download.DlDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadListener;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadParam;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nttdocomo.android.tvterminalapp.view.CustomDialog.DialogType.CONFIRM;

public class RecordedBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        ContentsAdapter.DownloadCallback {

    public Context mActivity;
    public List<ContentsData> mContentsData;
    public List<RecordedContentsDetailData> mContentsList;
    private View mLoadMoreView;
    private ContentsAdapter mContentsAdapter = null;
    public DlDataProvider mDlDataProvider = null;
    private DownloadParam downloadParam;
    public List<DlData> que = new ArrayList<>();
    public List<Integer> queIndex = new ArrayList<>();
    private Handler mHandler;
    private final int mPercentToUpdateUi = 1;
    private Activity activity;
    private String mProgress;
	private Set<String> mChannelNameCache;
    private boolean mCanBeCanceled = false;
    private String mFragmentName = null;

    @Override
    public Context getContext() {
        DTVTLogger.start();
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        DTVTLogger.start();
        try{
            mHandler=new Handler();
        } catch (Exception e){
            DTVTLogger.debug(e);
        }
        initData();
        return initView();
    }

    //モックデータ
    private void initData() {
        DTVTLogger.start();
        mContentsData = new ArrayList<>();
		mChannelNameCache = new HashSet<>();
    }

    private View mRecordedFragmentView;
    private ListView mRecordedListview;

    public View initView() {
        DTVTLogger.start();
        if (null == mRecordedFragmentView) {
            mRecordedFragmentView = View.inflate(getActivity()
                    , R.layout.record_contents_list_layout, null);
            mRecordedListview = mRecordedFragmentView.findViewById(R.id.recorded_contents_result);

            mRecordedListview.setOnScrollListener(this);
            mRecordedListview.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mActivity).inflate(R.layout.search_load_more, null);
        }

        mContentsAdapter = new ContentsAdapter(getContext(),
                mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_RECORDED_LIST, this);
        mRecordedListview.setAdapter(mContentsAdapter);

        return mRecordedFragmentView;
    }

    public void notifyDataSetChanged() {
        DTVTLogger.start();
        if (null != mContentsAdapter) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    public void setSelection(int itemNo) {
        DTVTLogger.start();
        if (null != mRecordedListview) {
            mRecordedListview.setSelection(itemNo);
        }
    }

    public void clear() {
        DTVTLogger.start();
        if(null!=mContentsData){
            mContentsData.clear();
        }
    }

    public List<ContentsData> getContentsData() {
        DTVTLogger.start();
        return mContentsData;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (null != mActivity) {
            if(que != null && que.size() > 0){
                showMessage("ダウンロード中のため、再生できません");
            } else {
                if(activity != null){
                    Intent intent = new Intent(mActivity, ContentDetailActivity.class);
                    intent.putExtra(DTVTConstants.SOURCE_SCREEN, activity.getComponentName().getClassName());
                    intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, mContentsList.get(i));
                    startActivity(intent);
                }
            }
        }
    }

    public void onDlDataProviderAvailable() {
        try {
            mDlDataProvider.setUiRunning(true);
            if(!mDlDataProvider.isDownloading()){
                mDlDataProvider.setDlParam(downloadParam);
                mDlDataProvider.start();
            }
        } catch (Exception e){
            DTVTLogger.debug(e);
        }
    }

    public void onDlDataProviderUnavailable() {
        mDlDataProvider = null;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(RecordedListActivity.RLA_FragmentName_All.equals(mFragmentName) && null != mDlDataProvider){
            mDlDataProvider.setUiRunning(false);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(RecordedListActivity.RLA_FragmentName_All.equals(mFragmentName)){
            if (mDlDataProvider == null) {
                try {
                    mDlDataProvider = DlDataProvider.getInstance(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("DlDataProvider初期化失敗しまして、ダウンロードできません。");
                    return;
                }
            }
            mDlDataProvider.setUiRunning(true);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }

    }

    public void onCancelAll(final String fullPath) {
        unqueueAllExceptForFirst();
        onCancelByBg(fullPath);
    }

    private void setSuccessStatus(String fullPath){
        if(queIndex.size() > 0){
            View view = mRecordedListview.getChildAt(queIndex.get(0)-mRecordedListview.getFirstVisiblePosition());
            if (view != null) {
                view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_normal_download_check);
                setDownloadStatusClear(view.findViewById(R.id.item_common_result_clip_tv));
				restoreChannelAndTime();
            }
            ContentsData cd = null;
            int idx = queIndex.get(0);
            try {
                cd = mContentsData.get(idx);
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
            if(null != cd){
                cd.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                cd.setDlFileFullPath(fullPath);
                cd.setDownloadStatus("");
                setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_COMPLETED, fullPath);
            }
        }
        setNextDownLoad();
    }

	private void restoreChannelAndTime(){
        if(null == queIndex || 0==queIndex.size() || null==mRecordedListview){
            return;
        }
        int idx = queIndex.get(0)-mRecordedListview.getFirstVisiblePosition();
        if(idx < 0 || idx >= mRecordedListview.getChildCount()){
            return;
        }
        View view = mRecordedListview.getChildAt(idx);
        if(null==view){
            return;
        }
        TextView timeView = view.findViewById(R.id.item_common_result_content_time);
        if (null != timeView) {
            String timeOnly = (String) timeView.getText();
            String timeAndChannel = getChannelTimeContent(timeOnly);
            if(null!=timeAndChannel) {
                timeView.setText(timeAndChannel);
            }
        }
    }
    private void setCancelStatus(String fullPath){
        setCancelStatusOnly(fullPath, 0);
        setNextDownLoad();
    }

    private void setCancelStatusOnly(String fullPath, int index){
        if(null == queIndex || -1 < index - queIndex.size()) {
            return;
        }
        View view = null;
        int idx = queIndex.get(index);
        try {
            view = mRecordedListview.getChildAt(idx - mRecordedListview.getFirstVisiblePosition());
            view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_normal_download);
            setDownloadStatusClear(view.findViewById(R.id.item_common_result_clip_tv));
            restoreChannelAndTime();
            mContentsData.get(idx).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
            mContentsData.get(idx).setDownloadStatus("");
            setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_ALLOW, fullPath);
        } catch (Exception e){
            DTVTLogger.debug(e);
        }

    }

    private void setNextDownLoad(){
        unqueue(0);
        if(que.size() > 0){
            boolean isOk = prepareDownLoad(queIndex.get(0));
            if(!isOk){
                return;
            }
            try {
                mDlDataProvider.setDlParam(downloadParam);
                mDlDataProvider.start();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setContentListStatusContent(int index, int status, String path) {
        if(null==mContentsList || mContentsList.size()<=index ||0>index){
            return;
        }

        RecordedContentsDetailData item= mContentsList.get(index);
        if(null==item){
            return;
        }
        item.setDownLoadStatus(status);
        mContentsList.get(index).setDlFileFullPath(path);
    }


    //@Override
    public void onLowStorageSpaceByBg(final String fullPath) {
        if(activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage("容量は足りないので、ダウンロードできませんでした。");
                    setCancelStatus(fullPath);
                }
            });
        }
    }

    private void setDownloadStatus(int index, int progress){
        if(index>=mContentsData.size()){
            return;
        }
        View view = mRecordedListview.getChildAt(index-mRecordedListview.getFirstVisiblePosition());
        TextView textView = null;
        if (view != null) {
            textView = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
        }
        switch (mContentsData.get(index).getDownloadFlg()){
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (textView != null) {
                    view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_active_cancel);
                }
                mContentsData.get(index).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                break;
            case ContentsAdapter.DOWNLOAD_STATUS_LOADING :
                mProgress = getResources().getString(R.string.record_download_status) + progress + getResources().getString(R.string.record_download_percent_mark);
                if (textView != null) {
                    TextView timeView = view.findViewById(R.id.item_common_result_content_time);
                    if (null != timeView) {
                        String timeAndChannel = (String) timeView.getText();
                        mChannelNameCache.add(timeAndChannel);
                        String timeOnly = getCutChannelTimeContent(timeAndChannel);
                        timeView.setText(timeOnly);
                    }

                    view.findViewById(R.id.item_common_result_recorded_content_hyphen).setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setTextColor(ContextCompat.getColor(mActivity, R.color.record_download_status_color));
                    textView.setText(mProgress);
                }
                mContentsData.get(index).setDownloadStatus(mProgress);
                break;
            default:
                break;
        }
    }

	private String getCutChannelTimeContent(String timeAndChannel){
        if(null == timeAndChannel || timeAndChannel.isEmpty()){
            return null;
        }
        int p = timeAndChannel.lastIndexOf(RecordedListActivity.sMinus);
        if(0>p){
            return timeAndChannel;
        }
        return timeAndChannel.substring(0, p);
    }

    private String getChannelTimeContent(String onlyTime){
        if(null == onlyTime || onlyTime.isEmpty()){
            return null;
        }
        for(String timeChannel: mChannelNameCache){
            if(timeChannel.contains(onlyTime)){
                return timeChannel;
            }
        }
        return null;
    }

    @Override
    public void downloadClick(View view) {
        int index = (int)view.getTag();
        switch (mContentsData.get(index).getDownloadFlg()) {
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (que.size() == 0) {
                    boolean isOk = prepareDownLoad(index);
                    if(!isOk){
                        return;
                    }
                    mCanBeCanceled = false;
                }
                DlData dlData = setDlData(index);
                if(dlData != null){
                    try {
                        mDlDataProvider.setDlData(dlData);
                    } catch (Exception e){
                        DTVTLogger.debug(e);
                        showMessage("DB insert Fail");
                        return;
                    }
                    mCanBeCanceled = false;
                    enqueue(index, dlData);
                    setDownloadStatus(index, 0);
                    mDlDataProvider.start();
                }
                break;
            case ContentsAdapter.DOWNLOAD_STATUS_LOADING :
                showDialogToConfirmUnDownload(false, view);
                break;
            case ContentsAdapter.DOWNLOAD_STATUS_COMPLETED :
                showDialogToConfirmUnDownload(true, view);
                break;
            default:
                break;
        }
    }

    private DlData setDlData(int index){
        if(null==mContentsList || index>=mContentsList.size()){
            return null;
        }
        DlData dlData = new DlData();
        RecordedContentsDetailData itemData = mContentsList.get(index);
        dlData.setItemId(DownloaderBase.getFileNameById(itemData.getItemId()));
        dlData.setSaveFile(DownloaderBase.getDownloadPath(getContext()));
        dlData.setTotalSize(itemData.getClearTextSize());
        dlData.setTitle(itemData.getTitle());
        dlData.setUrl(itemData.getResUrl());
        dlData.setBitrate(itemData.getBitrate());
        dlData.setPort(String.valueOf(DlnaDmsItem.getPortFromProtocal(itemData.getVideoType())));
        dlData.setDuration(itemData.getDuration());
        dlData.setVideoType(itemData.getVideoType());
        dlData.setUpnpIcon(itemData.getUpnpIcon());
        DlnaDmsItem dmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getContext());
        dlData.setHost(dmsItem.mIPAddress);
        dlData.setPercentToNotify(String.valueOf(mPercentToUpdateUi));

        String xml=getXmlToDl(itemData.getItemId());
        if(null==xml){
            showMessage("Xmlパラメーター取得失敗しまして、ダウンロードできません。");
            return null;
        }
        dlData.setXmlToDl(xml);
        return dlData;
    }

    private String getXmlToDl(String itemId){
        return DlnaInterface.getXmlToDl(itemId);
    }

    private boolean prepareDownLoad(int index) {
        Context context = getActivity();
        if (null == context) {
            return false;
        }
        String dlPath = DownloaderBase.getDownloadPath(context);
        RecordedContentsDetailData item;
        try {
            item = mContentsList.get(index);
        } catch (Exception e) {
            DTVTLogger.debug(e);
            showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
            return false;
        }
        if (null == item ) {
            showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
            return false;
        }
        String dlFileName = DownloaderBase.getFileNameById(item.getItemId());
        DlnaDmsItem dmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
        if (null == dmsItem.mUdn || dmsItem.mUdn.isEmpty()) {
            showMessage("DMS情報取得失敗しまして、ダウンロードできません。");
            return false;
        }
        String ip = dmsItem.mIPAddress;
        if (null == ip || ip.isEmpty()) {
            showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
            return false;
        }
        String port = item.getVideoType();
        int portInt = DlnaDmsItem.getPortFromProtocal(port);
        if (portInt < 0) {
            showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
            return false;
        }
        String url = item.getResUrl();
        String clearTextSize = item.getClearTextSize();
        int clearTextSizeInt;
        try {
            clearTextSizeInt = Integer.parseInt(clearTextSize);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("DlDataProvider初期化失敗しまして、ダウンロードできません。");
            return false;
        }
        downloadParam = new DtcpDownloadParam();
        DtcpDownloadParam dtcpDownloadParam = (DtcpDownloadParam) downloadParam;
        dtcpDownloadParam.setContext(context);
        dtcpDownloadParam.setSavePath(dlPath);
        dtcpDownloadParam.setSaveFileName(dlFileName);
        dtcpDownloadParam.setDtcp1host(ip);
        dtcpDownloadParam.setDtcp1port(portInt);
        dtcpDownloadParam.setUrl(url);
        dtcpDownloadParam.setCleartextSize(clearTextSizeInt);
        dtcpDownloadParam.setItemId(item.getItemId());
        dtcpDownloadParam.setPercentToNotify(mPercentToUpdateUi);
        String xml=getXmlToDl(item.getItemId());
        if(null==xml || 0 == xml.length()){
            showMessage("Xmlパラメーター取得失敗しまして、ダウンロードできません。");
            return false;
        }
        dtcpDownloadParam.setXmlToDl(xml);
        mDlDataProvider.beginProvider(getActivity());

        return true;
    }

    public void bindServiceFromBackgroud(boolean serviceIsRun){
        if(null == activity){
            activity = getActivity();
        }
        if(null == activity){
            return;
        }
        mDlDataProvider.beginProvider(activity);
        if(queIndex != null && queIndex.size() > 0){
            que.clear();
            for(int i=0;i < queIndex.size(); i++){
                DlData dlData = setDlData(queIndex.get(i));
                que.add(dlData);
            }
            prepareDownLoad(queIndex.get(0));
        }
    }

    public void onDownladProgressByBg(int percent){
        if(queIndex.size() > 0){
            mCanBeCanceled = true;
            setDownloadStatus(queIndex.get(0), percent);
        }
    }

    public void onDownladSuccessByBg(String fullPath){
        mCanBeCanceled = false;
        setSuccessStatus(fullPath);
    }

    public void onDownladFailByBg(String fullPath, final DownloadListener.DLError error){
        mCanBeCanceled = false;
        if(mDlDataProvider != null){
            mDlDataProvider.cancelDownLoadStatus(fullPath);
        }
        setNextDownLoad();
    }

    /**
     * show Message
     * @param msg
     */
    private void showMessage(String msg) {
        try{
            DTVTLogger.start();
            Context context=getActivity();
            if(null == context){
                DTVTLogger.end();
                return;
            }
            Toast.makeText(context, getResources().getString(R.string.record_download_error_message), Toast.LENGTH_SHORT).show();
            DTVTLogger.end();
        } catch (Exception e){
            DTVTLogger.debug(e);
        }
    }

    /**
     * true  コンテンツを削除するダイアログ表示
     * false ダウンロードを取りやめるダイアログ表示
     */
    private void showDialogToConfirmUnDownload(final boolean completed, final View view) {
        CustomDialog customDialog = new CustomDialog(getContext(), CONFIRM);
        if (completed){
            customDialog.setTitle(getResources().getString(R.string.record_download_delete_title));
        } else {
            customDialog.setTitle(getResources().getString(R.string.record_download_cancel_title));
            customDialog.setConfirmText(R.string.record_download_cancel_confirm);
        }
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                if (isOK) {
                    if (!completed){
                        if(null!=mDlDataProvider && null!=queIndex && 0<queIndex.size()){
                            int num = (int) view.getTag();
                            boolean isCurDl= queIndex.get(0) == num;
                            if (isCurDl) {
                                if(!mCanBeCanceled){
                                    showMessage("ダウンロードは初期化していますので、キャンセルできませんでした。");
                                    return;
                                }
                                cancelCurrentDl();
                            } else {
                                for (int i = 0; i < queIndex.size(); i++) {
                                    if(num == queIndex.get(i)){
                                        String path = getCurrentDlFullPath(i);
                                        mDlDataProvider.cancelDownLoadStatus(path);
                                        unqueue(i);
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        int index = (int)view.getTag();
                        RecordedContentsDetailData itemData = mContentsList.get(index);
                        StringBuilder path=new StringBuilder();
                        path.append(DownloaderBase.getDownloadPath(getContext()));
                        path.append(File.separator);
                        String itemId = itemData.getItemId();
                        if(!TextUtils.isEmpty(itemId) && !itemId.startsWith(DownloaderBase.sDlPrefix)){
                            itemId = DownloaderBase.getFileNameById(itemId);
                        }
                        path.append(itemId);
                        if (mDlDataProvider == null) {
                            try {
                                mDlDataProvider = DlDataProvider.getInstance(activity);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showMessage("DlDataProvider初期化失敗しまして、ダウンロードできません。");
                            }
                        }
                        mDlDataProvider.cancelDownLoadStatus(path.toString());
                        noticeActDel(path.toString());
                    }
                    setDownloadStatusClear(view);
                    view.setBackgroundResource(R.mipmap.icon_circle_normal_download);
                    mContentsData.get((Integer)view.getTag()).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    mContentsData.get((Integer)view.getTag()).setDownloadStatus("");
                    if(((RecordedListActivity)activity).getCurrentPosition() == 1){
                        ((RecordedListActivity)activity).setRecordedTakeOutContents();
                    }
                    int idx=(Integer)view.getTag();
                    setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_ALLOW, "");
                }
            }
        });
        customDialog.showDialog();
    }

    private void cancelCurrentDl(){
        mDlDataProvider.cancel();
        String path = getCurrentDlFullPath(0);
        mDlDataProvider.cancelDownLoadStatus(path);
    }

    private String getCurrentDlFullPath(int idx){
        if(null==que || idx>=que.size()){
            return null;
        }
        StringBuilder path=new StringBuilder();
        path.append(que.get(idx).getSaveFile());
        path.append(File.separator);
        path.append(que.get(idx).getItemId());
        return path.toString();
    }
    /**
     * false ダウンロードステータスをクリア
     */
    private void setDownloadStatusClear(View view) {
        if(null == view) {
            return;
        }
        TextView textView = ((RelativeLayout) view.getParent().getParent()).findViewById(R.id.item_common_result_recorded_content_channel_name);
        try {
            if (TextUtils.isEmpty(mContentsData.get((Integer) view.getTag()).getRecordedChannelName())) {
                ((RelativeLayout) view.getParent().getParent()).findViewById(R.id.item_common_result_recorded_content_hyphen).setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(mContentsData.get((Integer) view.getTag()).getRecordedChannelName());
                textView.setTextColor(ContextCompat.getColor(mActivity, R.color.content_time_text));
            }
        } catch (Exception e){
            DTVTLogger.debug(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(RecordedListActivity.RLA_FragmentName_All.equals(mFragmentName)) {
            if (mDlDataProvider != null) {
                mDlDataProvider.setUiRunning(false);
                if (mDlDataProvider.getIsRegistered()) {
                    try {
                        mDlDataProvider.endProvider();
                    } catch (Exception e){
                        DTVTLogger.debug(e);
                    }
                }
                if (0 == que.size()) {
                    if(!mDlDataProvider.isDownloading()){
                        mDlDataProvider.stopService();
                    }
                } else {
                    mDlDataProvider.setQue(que);
                }
            }
        }
    }

    /**
     * enqueue
     * @param index index
     * @param dlData dlData
     */
    private void enqueue(int index, DlData dlData){
        if(null == que || null == queIndex){
            return;
        }
        que.add(dlData);
        queIndex.add(index);
    }

    /**
     * unqueue
     * @param index index
     */
    private void unqueue(int index){
        if(null != que && index < que.size() && -1< index){
            que.remove(index);
        }
        if(null != queIndex && index < queIndex.size() && -1< index){
            queIndex.remove(index);
        }
    }

    /**
     * unqueueAllExceptForFirst
     */
    private void unqueueAllExceptForFirst(){
        if(null == que || null == activity || null == mDlDataProvider){
            return;
        }
        if(1 == que.size()){
            mDlDataProvider.cancel();
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fullPath = null;
                try {
                    for (int i = 0; i < que.size(); ++i) {
                        fullPath = getCurrentDlFullPath(i);
                        if (!TextUtils.isEmpty(fullPath)) {
                            if (0 == i) {
                                mDlDataProvider.cancel();
                            }
                            setCancelStatusOnly(fullPath, i);
                            mDlDataProvider.cancelDownLoadStatus(fullPath);
                        }
                    }
                    for (int i = 0; i < que.size(); ++i) {
                        que.remove(i);
                    }
                    for (int i = 0; i < queIndex.size(); ++i) {
                        queIndex.remove(i);
                    }
                } catch (Exception e){
                    DTVTLogger.debug(e);
                }
            }
        });
    }

    /**
     * 「持ち出し」タッブに使われる
     * @param fullPaht fullPaht
     */
    private void noticeActDel(String fullPaht) {
        if(null != activity) {
            ((RecordedListActivity)activity).onNoticeActDel(fullPaht, this);
        }
    }

    /**
     * delItemData
     * @param fullPaht fullPaht
     */
    public void delItemData(String fullPaht) {
        if(null == fullPaht || fullPaht.isEmpty()) {
            return;
        }
        if(null != mContentsData){
            for(int i=0;i<mContentsData.size();++i) {
                ContentsData cd=mContentsData.get(i);
                if(fullPaht.equals(cd.getDlFileFullPath())) {
                    cd.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    break;
                }
            }
        }
        if(null != mContentsList){
            for(int i=0;i<mContentsList.size();++i) {
                RecordedContentsDetailData rcd = mContentsList.get(i);
                if(fullPaht.equals(rcd.getDlFileFullPath())) {
                    rcd.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    break;
                }
            }
        }
    }

    /**
     * checkIsDownloading
     * @return yn
     */
    public boolean checkIsDownloading(){
        if(null == que) {
            return false;
        }
        return 0<que.size();
    }

    public void setFragmentName(String name) {
        mFragmentName = name;
    }

    public void onCancelByBg(String fullPath) {
        mCanBeCanceled = false;
        if(activity != null){
            showMessage("ダウンロードはキャンセルしました。");
            setCancelStatus(fullPath);
        }
    }

    public void onStartBg() {
        if(null==activity){
            return;
        }
        if(mDlDataProvider != null) {
            mDlDataProvider.setUiRunning(true);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(queIndex.size() > 0){
                    setDownloadStatus(queIndex.get(0) , 0);
                }
            }
        });
    }
}
