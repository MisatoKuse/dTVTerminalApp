/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

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

import com.digion.dixim.android.util.EnvironmentUtil;
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
import com.nttdocomo.android.tvterminalapp.service.download.DlData;
import com.nttdocomo.android.tvterminalapp.service.download.DlDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DlDataProviderListener;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadParam;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;
import com.nttdocomo.android.tvterminalapp.service.download.KariDownloadParam;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.nttdocomo.android.tvterminalapp.common.CustomDialog.DialogType.CONFIRM;

public class RecordedBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, ContentsAdapter.DownloadCallback
,DlDataProviderListener {

    public Context mActivity;
    public List<ContentsData> mContentsData;
    public List<RecordedContentsDetailData> mContentsList;
    private View mLoadMoreView;
    private ContentsAdapter mContentsAdapter = null;
    public DlDataProvider mDlDataProvider = null;
    private DownloadParam downloadParam;
    private List<DlData> que = new ArrayList<>();
    private List<View> queView = new ArrayList<>();
    private List<Integer> queIndex = new ArrayList<>();
    private Handler mHandler=new Handler();
    private final int mPercentToUpdateUi=1;

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
        mContentsData = new ArrayList();
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
            Intent intent = new Intent(mActivity, DtvContentsDetailActivity.class);
            intent.putExtra(DTVTConstants.SOURCE_SCREEN, getActivity().getComponentName().getClassName());
            intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, mContentsList.get(i));
            startActivity(intent);
        }
    }

    @Override
    public void onStart(int totalFileByteSize) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(queView.size() > 0){
                    setDownloadStatus(queView.get(0), queIndex.get(0) , 0);
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
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                if(queView.size() > 0){
                    setDownloadStatus(queView.get(0), queIndex.get(0), newPercent);
                }
                }
            });
        }
    }

    @Override
    public void onFail(final DLError error) {
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
                                showMessage("ダウンロードエラーは発生しました");
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
                    }
                });
    }

    @Override
    public void onSuccess(String fullPath) {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(queIndex.size() > 0){
                        View view = mRecordedListview.getChildAt(queIndex.get(0)-mRecordedListview.getFirstVisiblePosition());
                        if (view != null) {
                            view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_normal_download_check);
                            setDownloadStatusClear(view.findViewById(R.id.item_common_result_clip_tv));
                        }
                        mContentsData.get(queIndex.get(0)).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                        mContentsData.get(queIndex.get(0)).setDownloadStatus("");
                    }
                    if(que.size() > 0){
                        que.remove(0);
                        queView.remove(0);
                        queIndex.remove(0);
                    }
                    if(que.size() > 0){
                        boolean isOk=prepareDownLoad(queIndex.get(0));
                        if(!isOk){
                            return;
                        }
                    }
                    Toast.makeText(getActivity(),"download success",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onLowStorageSpace() {

    }

    @Override
    public void onDownLoadListCallBack(List<DlData> list) {

    }

    private void setDownloadStatus(View view, int index, int progress){
        view = mRecordedListview.getChildAt(index-mRecordedListview.getFirstVisiblePosition());
        TextView textView = null;
        if (view != null) {
            textView = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
        }
        switch (mContentsData.get(index).getDownloadFlg()){
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (textView != null) {
                    view.findViewById(R.id.item_common_result_recorded_content_hyphen).setVisibility(View.VISIBLE);
                    textView.setTextColor(ContextCompat.getColor(mActivity, R.color.d_animation_title));
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("ダウンロード中 " + progress + "%");
                    view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_active_pause);
                }
                mContentsData.get(index).setDownloadStatus("ダウンロード中 " + progress + "%");
                mContentsData.get(index).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                break;
            case ContentsAdapter.DOWNLOAD_STATUS_LOADING :
                if (textView != null) {
                    textView.setText("ダウンロード中 " + progress + "%");
                }
                mContentsData.get(index).setDownloadStatus("ダウンロード中 " + progress + "%");
                mContentsData.get(index).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                break;
            default:
                break;
        }
    }

    @Override
    public void downloadClick(View view) {
        switch (mContentsData.get((int)view.getTag()).getDownloadFlg()) {
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (que.size() == 0) {
                    boolean isOk=prepareDownLoad((int)view.getTag());
                    if(!isOk){
                        return;
                    }
                }
                DlData dlData = new DlData();
                dlData.setItemId(mContentsList.get((int) view.getTag()).getItemId());
                dlData.setSaveFile(getContext().getCacheDir().getPath());
                //dlData.setTotalSize(mContentsList.get((int) view.getTag()).getSize());
                dlData.setTotalSize(mContentsList.get((int) view.getTag()).getClearTextSize());
                dlData.setTitle(mContentsList.get((int) view.getTag()).getTitle());
                dlData.setUrl(mContentsList.get((int) view.getTag()).getResUrl());
                dlData.setBitrate(mContentsList.get((int) view.getTag()).getBitrate());
                dlData.setDuration(mContentsList.get((int) view.getTag()).getDuration());
                dlData.setVideoType(mContentsList.get((int) view.getTag()).getVideoType());
                dlData.setUpnpIcon(mContentsList.get((int) view.getTag()).getUpnpIcon());
                mDlDataProvider.setDlData(dlData);
                que.add(dlData);
                queIndex.add((int)view.getTag());
                queView.add(view);
                setDownloadStatus(view, (int)view.getTag(), 0);
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

    public static final boolean Is_KariDownloader=false;

    private boolean prepareDownLoad(int index) {
        if(Is_KariDownloader) {
            try {
                if (mDlDataProvider == null) {
                    mDlDataProvider = new DlDataProvider(getActivity(), this);
                }
                mDlDataProvider.beginProvider();

                downloadParam = new KariDownloadParam();
                KariDownloadParam karidownloadparam = (KariDownloadParam) downloadParam;
                karidownloadparam.setContext(getActivity());
                karidownloadparam.setUrl("https://www.nhk.or.jp/prog/img/2209/2209.jpg");
                //            karidownloadparam.setUrl(mContentsList.get((int)view.getTag()).getResUrl());
                karidownloadparam.setSaveFileName(mContentsList.get(index).getTitle());
                karidownloadparam.setFileSize(203697);
                //            karidownloadparam.setFileSize(50000000);
                karidownloadparam.setSavePath(getContext().getCacheDir().getPath());
            } catch (Exception e) {
                DTVTLogger.debug(e);
                return false;
            }
        } else {
            Context context=getActivity();
            if(null==context){
                return false;
            }
            String dlPath=getDownloadPath(context);

            RecordedContentsDetailData item=mContentsList.get(index);
            try {
                item = mContentsList.get(index);
            }catch (Exception e){
                DTVTLogger.debug(e);
                showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
                return false;
            }
            if(null==item){
                showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
                return false;
            }

            String dlFileName= "dtcp_test"; //todo design file name

            DlnaDmsItem dmsItem= SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
            if(null==dmsItem.mUdn || dmsItem.mUdn.isEmpty()){
                showMessage("DMS情報取得失敗しまして、ダウンロードできません。");
                return false;
            }

            String ip= dmsItem.mIPAddress;
            if(null==ip || ip.isEmpty()){
                showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
                return false;
            }

            String port= item.getVideoType();
            int portInt= DlnaDmsItem.getPortFromProtocal(port);
            if(portInt<0){
                showMessage("ダウンロードパラメーター初期化失敗しまして、ダウンロードできません。");
                return false;
            }

            String url=item.getResUrl();

            String clearTextSize=item.getClearTextSize();
            int clearTextSizeInt=0;
            try {
                clearTextSizeInt= Integer.parseInt(clearTextSize);
            } catch (Exception e){
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

            if (mDlDataProvider == null) {
                try {
                    mDlDataProvider = new DlDataProvider(getActivity(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("DlDataProvider初期化失敗しまして、ダウンロードできません。");
                    return false;
                }
            }
            mDlDataProvider.beginProvider();
        }

        return true;
    }

    /**
     * todo to do with sd card
     * @return
     */
    private String getDownloadPath(Context context){
        return EnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP); //内部ストレージ
    }

    private Toast mToast;
    /**
     * show Message
     * @param msg
     */
    private void showMessage(String msg) {
        DTVTLogger.start();
        Context context=mActivity;
        if(null==context){
            context=getActivity();
            if(null==context){
                DTVTLogger.end();
                return;
            }
        }
        if (null == mToast) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
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
                    if (true){
                        //コンテンツを削除する
                        String fileName = mContentsList.get((int) view.getTag()).getTitle();
                        File file = new File(getContext().getCacheDir().getPath() + "/" + fileName);
                        if(file.exists()){
                            if(file.delete()) {
                                Toast.makeText(getContext(), "delete success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        //ダウンロードを取りやめる
                    }
                    setDownloadStatusClear(view);
                    view.setBackgroundResource(R.mipmap.icon_circle_normal_download);
                    mContentsData.get((Integer)view.getTag()).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    mContentsData.get((Integer)view.getTag()).setDownloadStatus("");
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
    public void onDestroy() {
        super.onDestroy();
        if(mDlDataProvider != null){
            mDlDataProvider.endProvider();
            if(que.size() > 0){
                mDlDataProvider.setQue(que);
            }
        }
    }

}