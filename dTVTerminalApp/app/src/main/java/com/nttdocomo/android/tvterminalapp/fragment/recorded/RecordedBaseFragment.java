/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
import com.nttdocomo.android.tvterminalapp.service.download.DlData;
import com.nttdocomo.android.tvterminalapp.service.download.DlDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DlDataProviderListener;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadParam;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadService;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.nttdocomo.android.tvterminalapp.common.CustomDialog.DialogType.CONFIRM;

public class RecordedBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        ContentsAdapter.DownloadCallback, DlDataProviderListener {

    public Context mActivity;
    public List<ContentsData> mContentsData;
    public List<RecordedContentsDetailData> mContentsList;
    private View mLoadMoreView;
    private ContentsAdapter mContentsAdapter = null;
    public DlDataProvider mDlDataProvider = null;
    private DownloadParam downloadParam;
    public List<DlData> que = new ArrayList<>();
    public List<Integer> queIndex = new ArrayList<>();
    private Handler mHandler = new Handler();
    private final int mPercentToUpdateUi = 1;
    private Activity activity;
    private boolean mIsJustCanceled=false;

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
        initData();
        return initView();
    }

    //モックデータ
    private void initData() {
        DTVTLogger.start();
        mContentsData = new ArrayList<>();
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

    public void displayLoadMore(boolean b) {
        DTVTLogger.start();
        if (null != mRecordedListview && null != mLoadMoreView) {
            if (b) {
                mRecordedListview.addFooterView(mLoadMoreView);
            } else {
                mRecordedListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void clear() {
        DTVTLogger.start();
        mContentsData.clear();
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
                    Intent intent = new Intent(mActivity, DtvContentsDetailActivity.class);
                    intent.putExtra(DTVTConstants.SOURCE_SCREEN, activity.getComponentName().getClassName());
                    intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, mContentsList.get(i));
                    startActivity(intent);
                }
            }
        }
    }

    private static final int DOWNLAD_PROGRESS = 1;

    private Handler downLoadStatusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DOWNLAD_PROGRESS:
                    if(queIndex.size() > 0){
                        int percent = (int) msg.obj;
                        setDownloadStatus(queIndex.get(0), percent);
                    }
                    break;
            }
        }
    };

    @Override
    public void onStart(int totalFileByteSize) {
        if(null==activity){
            return;
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

    @Override
    public void dlDataProviderAvailable() {
        try {
            mDlDataProvider.setDlParam(downloadParam);
            mDlDataProvider.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void dlDataProviderUnavailable() {

    }
    @Override
    public void onProgress(int receivedBytes, int percent) {
        final int newPercent = percent;
        if(activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(queIndex.size() > 0){
                        setDownloadStatus(queIndex.get(0), newPercent);
                    }
                }
            });
        }
//        Message msg = Message.obtain(downLoadStatusHandler, DOWNLAD_PROGRESS, percent);
//        downLoadStatusHandler.sendMessage(msg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }

    }

    @Override
    public void onFail(final DLError error, final String savePath) {
        mHandler.post(
                new Runnable(){
                    @Override
                    public void run() {
                        switch (error){
                            case DLError_NetLost:
                                showMessage("ネットワークは接続していないので、ダウンロードは出来ませんでした");
                                break;
                            case DLError_DmsLost:
                                showMessage("DMSと接続していないので、ダウンロードは出来ませんでした");
                                break;
                            case DLError_DmsError:
                                showMessage("DMSはエラーになっているので、ダウンロードは出来ませんでした");
                                break;
                            case DLError_StorageNoSpace:
                                showMessage("ストレージ容量不足なので、ダウンロードは出来ませんでした");
                                break;
                            case DLError_Download:
                                if(mIsJustCanceled){
                                    mIsJustCanceled=false;
                                    return;
                                } else {
                                    showMessage("ダウンロードエラーは発生しました");
                                }
                                break;
                            case DLError_ParamError:
                                showMessage("ダウンロードパーラメータエラーは発生しました");
                                break;
                            case DLError_Unactivated:
                                showMessage("アクティベーションはまだですので、ダウンロードは出来ませんでした");
                                break;
                            case DLError_CopyKeyFileFailed:
                                showMessage("キーファイルコピーエラーが発生しましたので、ダウンロードは出来ませんでした");
                                break;
                            case DLError_Other:
                                showMessage("他のエラーが発生しましたので、ダウンロードは出来ませんでした");
                                break;
                            case DLError_NoError:

                                break;
                        }
                        if(mDlDataProvider != null){
                            mDlDataProvider.cancelDownLoadStatus(savePath);
                        }
                        setNextDownLoad();
                    }
                });
    }

    @Override
    public void onSuccess(String fullPath) {
        final String fullPath2=fullPath;
        if(activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DTVTLogger.debug("HandlerThread:"+Thread.currentThread().getId());
                    setSuccessStatus(fullPath2);
                }
            });
        }
    }

    private void setSuccessStatus(String fullPath){
        if(queIndex.size() > 0){
            View view = mRecordedListview.getChildAt(queIndex.get(0)-mRecordedListview.getFirstVisiblePosition());
            if (view != null) {
                view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_normal_download_check);
                setDownloadStatusClear(view.findViewById(R.id.item_common_result_clip_tv));
            }
            mContentsData.get(queIndex.get(0)).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
            mContentsData.get(queIndex.get(0)).setDownloadStatus("");
            setContentListStatusContent(queIndex.get(0), ContentsAdapter.DOWNLOAD_STATUS_COMPLETED, fullPath);
        }
        setNextDownLoad();
    }

    private void setNextDownLoad(){
        if(que.size() > 0){
            que.remove(0);
            queIndex.remove(0);
        }
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
        } else {
            if(mDlDataProvider != null){
                if(DownloadService.BINDSTATUS == DownloadService.BINDED){
                    mActivity.unbindService(mDlDataProvider);
                }
                mDlDataProvider.isRegistered = false;
                mDlDataProvider.stopService();
                DownloadService.BINDSTATUS = DownloadService.UNBINED;
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

    @Override
    public void onCancel() {
        if(!mIsJustCanceled){
            mIsJustCanceled=true;
        }
        if(activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage("ダウンロードはキャンセルしました。");
                }
            });
        }
    }

    @Override
    public void onLowStorageSpace() {

    }

    @Override
    public void onDownLoadListCallBack(List<DlData> list) {

    }

    private void setDownloadStatus(int index, int progress){
        View view = mRecordedListview.getChildAt(index-mRecordedListview.getFirstVisiblePosition());
        TextView textView = null;
        if (view != null) {
            textView = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
        }
        switch (mContentsData.get(index).getDownloadFlg()){
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (textView != null) {
                    view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_active_pause);
                }
                mContentsData.get(index).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                break;
            case ContentsAdapter.DOWNLOAD_STATUS_LOADING :
                if (textView != null) {
                    view.findViewById(R.id.item_common_result_recorded_content_hyphen).setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setTextColor(ContextCompat.getColor(mActivity, R.color.d_animation_title));
                    textView.setText("ダウンロード中 " + progress + "%");
                }
                mContentsData.get(index).setDownloadStatus("ダウンロード中 " + progress + "%");
                break;
            default:
                break;
        }
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
                    que.add(dlData);
                    queIndex.add(index);
                    setDownloadStatus(index, 0);
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
        if(null==xml){
            showMessage("Xmlパラメーター取得失敗しまして、ダウンロードできません。");
            return false;
        }
        dtcpDownloadParam.setXmlToDl(xml);
        if (mDlDataProvider == null) {
            try {
                mDlDataProvider = new DlDataProvider(activity, this);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("DlDataProvider初期化失敗しまして、ダウンロードできません。");
                return false;
            }
        }
        if(DownloadService.BINDSTATUS == DownloadService.UNBINED){
            mDlDataProvider.beginProvider();
        } else {
//            mDlDataProvider.rebind();
        }
        return true;
    }

    public void bindServiceFromBackgroud(boolean serviceIsRun){
        if(serviceIsRun){
            DownloadService.BINDSTATUS = DownloadService.BACKGROUD;
        } else {
            DownloadService.BINDSTATUS = DownloadService.UNBINED;
        }
        if(queIndex != null && queIndex.size() > 0){
            que.clear();
            for(int i=0;i < queIndex.size(); i++){
                DlData dlData = setDlData(queIndex.get(i));
                que.add(dlData);
            }
            prepareDownLoad(queIndex.get(0));
        }
    }

    public void setDownladProgressByBg(int percent){
        if(queIndex.size() > 0){
            setDownloadStatus(queIndex.get(0), percent);
        }
    }

    public void setDownladSuccessByBg(String fullPath){
         setSuccessStatus(fullPath);
    }

    public void setDownladFailByBg(String fullPath){
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
        DTVTLogger.start();
        Context context=getActivity();
        if(null == context){
            DTVTLogger.end();
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        DTVTLogger.end();
    }

    /**
     * true  コンテンツを削除するダイアログ表示
     * false ダウンロードを取りやめるダイアログ表示
     */
    private void showDialogToConfirmUnDownload(final boolean completed, final View view) {
        CustomDialog customDialog = new CustomDialog(getContext(), CONFIRM);
        if (completed){
            customDialog.setTitle("コンテンツを削除しますか？");
        } else {
            customDialog.setTitle("ダウンロードを取りやめますか？");
        }
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                if (isOK) {
                    if (!completed){
                        for (int i = 0; i < queIndex.size(); i++) {
                            if ((int) view.getTag() == queIndex.get(0)) {
                                mDlDataProvider.cancel();
                                if (queIndex.size() > 1) {
                                    prepareDownLoad(queIndex.get(1));
                                }
                            }
                            if ((int) view.getTag() == queIndex.get(i)) {
                                StringBuilder path=new StringBuilder();
                                path.append(que.get(i).getSaveFile());
                                path.append(File.separator);
                                path.append(que.get(i).getItemId());
                                mDlDataProvider.cancelDownLoadStatus(path.toString());
                                que.remove(i);
                                queIndex.remove(i);
                                if(queIndex.size() > 0){
                                    try {
                                        mDlDataProvider.setDlParam(downloadParam);
                                        mDlDataProvider.start();
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                break;
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
                                mDlDataProvider = new DlDataProvider(activity, RecordedBaseFragment.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showMessage("DlDataProvider初期化失敗しまして、ダウンロードできません。");
                            }
                        }
                        mDlDataProvider.cancelDownLoadStatus(path.toString());
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

    /**
     * false ダウンロードステータスをクリア
     */
    private void setDownloadStatusClear(View view) {
        TextView textView = ((RelativeLayout) view.getParent().getParent()).findViewById(R.id.item_common_result_recorded_content_channel_name);
        if (TextUtils.isEmpty(mContentsData.get((Integer)view.getTag()).getRecordedChannelName())){
            ((RelativeLayout) view.getParent().getParent()).findViewById(R.id.item_common_result_recorded_content_hyphen).setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(mContentsData.get((Integer)view.getTag()).getRecordedChannelName());
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.content_time_text));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDlDataProvider != null){
            if(mDlDataProvider.isRegistered){
                mActivity.unbindService(mDlDataProvider);
            }
            mDlDataProvider.setQue(que);
        }
    }

}
