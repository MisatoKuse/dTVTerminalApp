/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.nttdocomo.android.tvterminalapp.service.download.DownloadParam;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 録画一覧フラグメント.
 */
public class RecordedBaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        ContentsAdapter.DownloadCallback {

    private Context mContext;
    private List<ContentsData> mContentsData;
    public List<RecordedContentsDetailData> mContentsList;
    private ContentsAdapter mContentsAdapter = null;
    private DlDataProvider mDlDataProvider = null;
    private DownloadParam downloadParam;
    private final List<DlData> que = new ArrayList<>();
    public List<Integer> queIndex = new ArrayList<>();
    //private Handler mHandler;
    private static final int mPercentToUpdateUi = 1;
    private Activity activity;
	private Set<String> mChannelNameCache;
    private boolean mCanBeCanceled = false;
    private String mFragmentName = null;

    @Override
    public Context getContext() {
        DTVTLogger.start();
        this.mContext = getActivity();
        return mContext;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        DTVTLogger.start();
        initData();
        return initView();
    }

    /**
     * モックデータ.
     */
    private void initData() {
        DTVTLogger.start();
        mContentsData = new ArrayList<>();
		mChannelNameCache = new HashSet<>();
    }

    private View mRecordedFragmentView;
    private ListView mRecordedListView;

    /**
     * Viewの初期化.
     *
     * @return view
     */
    private View initView() {
        DTVTLogger.start();
        if (null == mRecordedFragmentView) {
            mRecordedFragmentView = View.inflate(getActivity(),
                    R.layout.record_contents_list_layout, null);
            mRecordedListView = mRecordedFragmentView.findViewById(R.id.recorded_contents_result);

            mRecordedListView.setOnScrollListener(this);
            mRecordedListView.setOnItemClickListener(this);

            getContext();
        }

        mContentsAdapter = new ContentsAdapter(getContext(),
                mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_RECORDED_LIST, this);
        mRecordedListView.setAdapter(mContentsAdapter);

        return mRecordedFragmentView;
    }

    /**
     * アダプタ更新.
     */
    public void notifyDataSetChanged() {
        DTVTLogger.start();
        if (null != mContentsAdapter) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * リスト表示位置移動.
     *
     * @param itemNo 移動先
     */
    public void setSelection(final int itemNo) {
        DTVTLogger.start();
        if (null != mRecordedListView) {
            mRecordedListView.setSelection(itemNo);
        }
    }

    /**
     * データクリア.
     */
    public void clear() {
        DTVTLogger.start();
        if (null != mContentsData) {
            mContentsData.clear();
        }
    }

    /**
     * データ取得.
     *
     * @return コンテンツデータ
     */
    public List<ContentsData> getContentsData() {
        DTVTLogger.start();
        return mContentsData;
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view,
                            final int i, final long l) {
        if (null != mContext) {
            if (que.size() > 0) {
                showMessage();
            } else {
                if (activity != null && null != mContentsList) {
                    Intent intent = new Intent(mContext, ContentDetailActivity.class);
                    intent.putExtra(DTVTConstants.SOURCE_SCREEN, activity.getComponentName().getClassName());
                    intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, mContentsList.get(i));
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * DlDataProvider使用開始.
     */
    public void onDlDataProviderAvailable() {
        mDlDataProvider.setUiRunning(true);
        if (!mDlDataProvider.isDownloading()) {
            mDlDataProvider.setDlParam(downloadParam);
            mDlDataProvider.start();
        }
    }

    /**
     * DlDataProvider使用不可時の設定.
     */
    public void onDlDataProviderUnavailable() {
        mDlDataProvider = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (RecordedListActivity.RLA_FragmentName_All.equals(mFragmentName) && null != mDlDataProvider) {
            mDlDataProvider.setUiRunning(false);
            if (mDlDataProvider.getIsRegistered()) {
                mDlDataProvider.endProvider();
            }
            if (0 == que.size()) {
                if (!mDlDataProvider.isDownloading()) {
                    mDlDataProvider.stopService();
                }
            } else {
                mDlDataProvider.setQue(que);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RecordedListActivity.RLA_FragmentName_All.equals(mFragmentName)) {
            if (mDlDataProvider == null) {
                try {
                    mDlDataProvider = DlDataProvider.getInstance(activity);
                } catch (Exception e) {
                    DTVTLogger.debug(e);
                    showMessage();
                    return;
                }
            }
            if (mDlDataProvider != null) {
                mDlDataProvider.setUiRunning(true);
            }
        }
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    /**
     * 全てのダウンロードをキャンセルする.
     *
     * @param fullPath フルパス
     */
    public void onCancelAll(final String fullPath) {
        unQueueAllExceptForFirst();
        onCancelByBg(fullPath);
    }

    /**
     * 成功ステータスを設定する.
     *
     * @param fullPath フルパス
     */
    private void setSuccessStatus(final String fullPath) {
        if (queIndex.size() > 0 && null != mRecordedListView) {
            int idx0 = queIndex.get(0) - mRecordedListView.getFirstVisiblePosition();
            View view = mRecordedListView.getChildAt(idx0);
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
            if (null != cd) {
                cd.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                cd.setDlFileFullPath(fullPath);
                cd.setDownloadStatus("");
                setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_COMPLETED, fullPath);
            }
        }
        setNextDownLoad();
    }

    /**
     * チャンネルと時間を戻す.
     */
	private void restoreChannelAndTime(){
        if (null == queIndex || 0 == queIndex.size() || null == mRecordedListView) {
            return;
        }
        int idx = queIndex.get(0) - mRecordedListView.getFirstVisiblePosition();
        if (idx < 0 || idx >= mRecordedListView.getChildCount()) {
            return;
        }
        View view = mRecordedListView.getChildAt(idx);
        if (null == view) {
            return;
        }
        TextView timeView = view.findViewById(R.id.item_common_result_content_time);
        if (null != timeView) {
            String timeOnly = (String) timeView.getText();
            String timeAndChannel = getChannelTimeContent(timeOnly);
            if (null != timeAndChannel) {
                timeView.setText(timeAndChannel);
            }
        }
    }

    /**
     * キャンセルステータス設定.
     *
     * @param fullPath フルパス
     */
    private void setCancelStatus(final String fullPath) {
        setCancelStatusOnly(fullPath, 0);
        setNextDownLoad();
    }

    /**
     * キャンセルステータス設定.
     *
     * @param fullPath フルパス
     * @param index インデックス
     */
    private void setCancelStatusOnly(final String fullPath, final int index) {
        if (null == queIndex || -1 < index - queIndex.size()) {
            return;
        }
        View view;
        int idx = queIndex.get(index);
        if (null == mRecordedListView) {
            return;
        } else {
            idx = idx - mRecordedListView.getFirstVisiblePosition();
        }
        view = mRecordedListView.getChildAt(idx);
        View tvView = view.findViewById(R.id.item_common_result_clip_tv);
        if (null != tvView) {
            tvView.setBackgroundResource(R.mipmap.icon_circle_normal_download);
            setDownloadStatusClear(tvView);
        }
        restoreChannelAndTime();
        mContentsData.get(idx).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
        mContentsData.get(idx).setDownloadStatus("");
        setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_ALLOW, fullPath);
    }

    /**
     * 次のダウンロードを設定.
     */
    private void setNextDownLoad() {
        unQueue(0);
        if (que.size() > 0) {
            boolean isOk = prepareDownLoad(queIndex.get(0));
            if (!isOk) {
                return;
            }
            mDlDataProvider.setDlParam(downloadParam);
            mDlDataProvider.start();
        }
    }

    /**
     * ContentListステータス設定.
     *
     * @param index インデックス
     * @param status ステータス
     * @param path パス
     */
    private void setContentListStatusContent(final int index, final int status, final String path) {
        if (null == mContentsList || mContentsList.size() <= index || 0 > index) {
            return;
        }

        RecordedContentsDetailData item = mContentsList.get(index);
        if (null == item) {
            return;
        }
        item.setDownLoadStatus(status);
        mContentsList.get(index).setDlFileFullPath(path);
    }

    /**
     * ストレージ容量不足.
     *
     * @param fullPath フルパス
     */
    public void onLowStorageSpaceByBg(final String fullPath) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage();
                    setCancelStatus(fullPath);
                }
            });
        }
    }

    /**
     * ダウンロードステータス設定.
     *
     * @param index インデックス
     * @param progress 進捗率
     */
    private void setDownloadStatus(final int index, final int progress) {
        String mProgress;
        if (index >= mContentsData.size() || null == mRecordedListView) {
            return;
        }
        int idx;
        idx = index - mRecordedListView.getFirstVisiblePosition();
        View view = mRecordedListView.getChildAt(idx);
        TextView textView = null;
        if (view != null) {
            textView = view.findViewById(R.id.item_common_result_recorded_content_channel_name);
        }
        switch (mContentsData.get(index).getDownloadFlg()) {
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (textView != null) {
                    view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.mipmap.icon_circle_active_cancel);
                }
                mContentsData.get(index).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                break;
            case ContentsAdapter.DOWNLOAD_STATUS_LOADING:
                mProgress = getResources().getString(R.string.record_download_status) + progress
                        + getResources().getString(R.string.record_download_percent_mark);
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
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.record_download_status_color));
                    textView.setText(mProgress);
                }
                mContentsData.get(index).setDownloadStatus(mProgress);
                break;
            default:
                break;
        }
    }

    /**
     * 文字列整形.
     *
     * @param timeAndChannel 時間とチャンネル
     * @return 整形後文字列
     */
	private String getCutChannelTimeContent(final String timeAndChannel) {
        if (null == timeAndChannel || timeAndChannel.isEmpty()) {
            return null;
        }
        int p = timeAndChannel.lastIndexOf(RecordedListActivity.sMinus);
        if (0 > p) {
            return timeAndChannel;
        }
        return timeAndChannel.substring(0, p);
    }

    /**
     * チャンネル時間取得.
     *
     * @param onlyTime 時間
     * @return timeChannel
     */
    private String getChannelTimeContent(final String onlyTime) {
        if (null == onlyTime || onlyTime.isEmpty()) {
            return null;
        }
        for (String timeChannel: mChannelNameCache) {
            if (timeChannel.contains(onlyTime)) {
                return timeChannel;
            }
        }
        return null;
    }

    @Override
    public void downloadClick(final View view) {
        int index = (int) view.getTag();
        switch (mContentsData.get(index).getDownloadFlg()) {
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (que.size() == 0) {
                    boolean isOk = prepareDownLoad(index);
                    if (!isOk) {
                        return;
                    }
                    mCanBeCanceled = false;
                }
                DlData dlData = setDlData(index);
                if (dlData != null) {
                    mDlDataProvider.setDlData(dlData);
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

    /**
     * DLデータ設定.
     *
     * @param index インデックス
     * @return DLデータ
     */
    private DlData setDlData(final int index) {
        if (null == mContentsList || index >= mContentsList.size()) {
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

        String xml = getXmlToDl(itemData.getItemId());
        if (null == xml) {
            showMessage();
            return null;
        }
        dlData.setXmlToDl(xml);
        return dlData;
    }

    /**
     * xmlパラメータ取得.
     *
     * @param itemId アイテムID
     * @return xmlパラメータ
     */
    private String getXmlToDl(final String itemId) {
        return DlnaInterface.getXmlToDl(itemId);
    }

    /**
     * ダウンロード準備.
     *
     * @param index インデックス
     * @return ダウンロード開始判定
     */
    private boolean prepareDownLoad(final int index) {
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
            showMessage();
            return false;
        }
        if (null == item) {
            showMessage();
            return false;
        }
        String dlFileName = DownloaderBase.getFileNameById(item.getItemId());
        DlnaDmsItem dmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
        if (null == dmsItem.mUdn || dmsItem.mUdn.isEmpty()) {
            showMessage();
            return false;
        }
        String ip = dmsItem.mIPAddress;
        if (null == ip || ip.isEmpty()) {
            showMessage();
            return false;
        }
        String port = item.getVideoType();
        int portInt = DlnaDmsItem.getPortFromProtocal(port);
        if (portInt < 0) {
            showMessage();
            return false;
        }
        String url = item.getResUrl();
        String clearTextSize = item.getClearTextSize();
        int clearTextSizeInt;
        try {
            clearTextSizeInt = Integer.parseInt(clearTextSize);
        } catch (Exception e) {
            showMessage();
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
        String xml = getXmlToDl(item.getItemId());
        if (null == xml || 0 == xml.length()) {
            showMessage();
            return false;
        }
        dtcpDownloadParam.setXmlToDl(xml);
        mDlDataProvider.beginProvider(getActivity());

        return true;
    }

    /**
     * サービスとバインドする.
     *
     */
    public void bindServiceFromBackground() {
        if (null == activity) {
            activity = getActivity();
        }
        if (null == activity) {
            return;
        }
        mDlDataProvider.beginProvider(activity);
        if (queIndex != null && queIndex.size() > 0) {
            que.clear();
            for (int i = 0; i < queIndex.size(); i++) {
                DlData dlData = setDlData(queIndex.get(i));
                que.add(dlData);
            }
            prepareDownLoad(queIndex.get(0));
        }
    }

    /**
     * ダウンロード進捗率変化時に呼ばれる.
     *
     * @param percent パーセント
     */
    public void onDownloadProgressByBg(final int percent) {
        if (queIndex.size() > 0) {
            mCanBeCanceled = true;
            setDownloadStatus(queIndex.get(0), percent);
        }
    }

    /**
     * ダウンロード完了時に呼ばれる.
     *
     * @param fullPath フルパス
     */
    public void onDownloadSuccessByBg(final String fullPath) {
        mCanBeCanceled = false;
        setSuccessStatus(fullPath);
    }

    /**
     * ダウンロード失敗時に呼ばれる.
     *  @param fullPath フルパス
     *
     */
    public void onDownloadFailByBg(final String fullPath) {
        mCanBeCanceled = false;
        if (mDlDataProvider != null) {
            mDlDataProvider.cancelDownLoadStatus(fullPath);
        }
        setNextDownLoad();
    }

    /**
     * show Message.
     *
     */
    private void showMessage() {
        DTVTLogger.start();
        Context context = getActivity();
        if (null == context) {
            DTVTLogger.end();
            return;
        }
        Toast.makeText(context, getResources().getString(R.string.record_download_error_message), Toast.LENGTH_SHORT).show();
        DTVTLogger.end();
    }

    /**
     * ダイアログ表示.
     *
     * @param completed true  コンテンツを削除するダイアログ表示
     *                     false ダウンロードを取りやめるダイアログ表示
     * @param view view
     */
    private void showDialogToConfirmUnDownload(final boolean completed, final View view) {
        CustomDialog customDialog = new CustomDialog(getContext(), CustomDialog.DialogType.CONFIRM);
        if (completed) {
            customDialog.setTitle(getResources().getString(R.string.record_download_delete_title));
        } else {
            customDialog.setTitle(getResources().getString(R.string.record_download_cancel_title));
            customDialog.setConfirmText(R.string.record_download_cancel_confirm);
        }
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                if (isOK) {
                    if (!completed) {
                        if (null != mDlDataProvider && null != queIndex && 0 < queIndex.size()) {
                            int num = (int) view.getTag();
                            boolean isCurDl = queIndex.get(0) == num;
                            if (isCurDl) {
                                if (!mCanBeCanceled) {
                                    showMessage();
                                    return;
                                }
                                cancelCurrentDl();
                            } else {
                                for (int i = 0; i < queIndex.size(); i++) {
                                    if (num == queIndex.get(i)) {
                                        String path = getCurrentDlFullPath(i);
                                        mDlDataProvider.cancelDownLoadStatus(path);
                                        unQueue(i);
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        int index = (int) view.getTag();
                        RecordedContentsDetailData itemData = mContentsList.get(index);
                        StringBuilder path = new StringBuilder();
                        path.append(DownloaderBase.getDownloadPath(getContext()));
                        path.append(File.separator);
                        String itemId = itemData.getItemId();
                        if (!TextUtils.isEmpty(itemId) && !itemId.startsWith(DownloaderBase.sDlPrefix)) {
                            itemId = DownloaderBase.getFileNameById(itemId);
                        }
                        path.append(itemId);
                        if (mDlDataProvider == null) {
                            try {
                                mDlDataProvider = DlDataProvider.getInstance(activity);
                            } catch (Exception e) {
                                DTVTLogger.debug(e);
                                showMessage();
                            }
                        }
                        mDlDataProvider.cancelDownLoadStatus(path.toString());
                        noticeActDel(path.toString());
                    }
                    setDownloadStatusClear(view);
                    view.setBackgroundResource(R.mipmap.icon_circle_normal_download);
                    mContentsData.get((Integer) view.getTag()).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    mContentsData.get((Integer) view.getTag()).setDownloadStatus("");
                    if (((RecordedListActivity) activity).getCurrentPosition() == 1) {
                        ((RecordedListActivity) activity).setRecordedTakeOutContents();
                    }
                    int idx = (Integer) view.getTag();
                    setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_ALLOW, "");
                }
            }
        });
        customDialog.showDialog();
    }

    /**
     * 現在のDLを取りやめる.
     */
    private void cancelCurrentDl() {
        mDlDataProvider.cancel();
        String path = getCurrentDlFullPath(0);
        mDlDataProvider.cancelDownLoadStatus(path);
    }

    /**
     * 現在のDLのフルパスを取得する.
     *
     * @param idx DL番号
     * @return フルパス
     */
    private String getCurrentDlFullPath(final int idx) {
        if (idx >= que.size()) {
            return null;
        }
        return StringUtils.getConnectStrings(que.get(idx).getSaveFile(), File.separator,
                que.get(idx).getItemId());
    }

    /**
     * ダウンロードステータスをクリア.
     *
     * @param view view
     */
    private void setDownloadStatusClear(final View view) {
        if (null == view) {
            return;
        }
        TextView textView = ((RelativeLayout) view.getParent().getParent()).findViewById(R.id.item_common_result_recorded_content_channel_name);
        if (TextUtils.isEmpty(mContentsData.get((Integer) view.getTag()).getRecordedChannelName())) {
            ((RelativeLayout) view.getParent().getParent()).findViewById(R.id.item_common_result_recorded_content_hyphen).setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(mContentsData.get((Integer) view.getTag()).getRecordedChannelName());
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.content_time_text));
        }
    }

    /**
     * enqueue.
     * @param index index
     * @param dlData dlData
     */
    private void enqueue(final int index, final DlData dlData) {
        if (null == queIndex) {
            return;
        }
        que.add(dlData);
        queIndex.add(index);
    }

    /**
     * unQueue.
     * @param index index
     */
    private void unQueue(final int index) {
        if (index < que.size() && -1 < index) {
            que.remove(index);
        }
        if (null != queIndex && index < queIndex.size() && -1 < index) {
            queIndex.remove(index);
        }
    }

    /**
     * unQueueAllExceptForFirst.
     */
    private void unQueueAllExceptForFirst() {
        if (null == activity || null == mDlDataProvider) {
            return;
        }
        if (1 == que.size()) {
            mDlDataProvider.cancel();
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fullPath;
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
            }
        });
    }

    /**
     * 「持ち出し」タッブに使われる.
     * @param fullPath fullPath
     */
    private void noticeActDel(final String fullPath) {
        if (null != activity) {
            ((RecordedListActivity) activity).onNoticeActDel(fullPath, this);
        }
    }

    /**
     * delItemData.
     * @param fullPath fullPath
     */
    public void delItemData(final String fullPath) {
        if (null == fullPath || fullPath.isEmpty()) {
            return;
        }
        if (null != mContentsData) {
            for (int i = 0; i < mContentsData.size(); ++i) {
                ContentsData cd = mContentsData.get(i);
                if (fullPath.equals(cd.getDlFileFullPath())) {
                    cd.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    break;
                }
            }
        }
        if (null != mContentsList) {
            for (int i = 0; i < mContentsList.size(); ++i) {
                RecordedContentsDetailData rcd = mContentsList.get(i);
                if (fullPath.equals(rcd.getDlFileFullPath())) {
                    rcd.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    break;
                }
            }
        }
    }

    /**
     * checkIsDownloading.
     * @return yn
     */
    public boolean checkIsDownloading() {
        return 0 < que.size();
    }

    /**
     * フラグメント名設定.
     *
     * @param name 名前
     */
    public void setFragmentName(final String name) {
        mFragmentName = name;
    }

    /**
     * ダウンロードキャンセル時に呼ばれる.
     *
     * @param fullPath フルパス
     */
    public void onCancelByBg(final String fullPath) {
        mCanBeCanceled = false;
        if (activity != null) {
            showMessage();
            setCancelStatus(fullPath);
        }
    }

    /**
     * ダウンロード開始時に呼ばれる.
     */
    public void onStartBg() {
        if (null == activity) {
            return;
        }
        if (mDlDataProvider != null) {
            mDlDataProvider.setUiRunning(true);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (queIndex.size() > 0) {
                    setDownloadStatus(queIndex.get(0), 0);
                }
            }
        });
    }
}
