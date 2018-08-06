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
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadData;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadDataProvider;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadParam;
import com.nttdocomo.android.tvterminalapp.service.download.DownloaderBase;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
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
public class RecordedBaseFragment extends Fragment implements AdapterView.OnItemClickListener,
        ContentsAdapter.DownloadCallback, AbsListView.OnScrollListener {
    /**コンテキスト.*/
    private Context mContext;
    /**コンテンツデータ.*/
    private List<ContentsData> mContentsData = new ArrayList<>();
    /**録画コンテンツ詳細.*/
    private final List<RecordedContentsDetailData> mContentsList = new ArrayList<>();
    /**コンテンツアダプター.*/
    private ContentsAdapter mContentsAdapter = null;
    /**データプロバイダー.*/
    private DownloadDataProvider mDownloadDataProvider = null;
    /**ダウンロードパラメータ.*/
    private DownloadParam mDownloadParam;
    /**キュー.*/
    private final List<DownloadData> mQueue = new ArrayList<>();
    /**キューインデックス.*/
    private final List<Integer> mQueueIndex = new ArrayList<>();
    //private Handler mHandler;
    /**PercentToUpdateUi.*/
    private static final int mPercentToUpdateUi = 1;
    /**activity.*/
    private Activity mActivity;
    /**チャンネル名キャッシュ.*/
	private Set<String> mChannelNameCache;
    /**フラグメント名.*/
    private String mFragmentName = null;
    /**録画フラグメントビュー.*/
    private View mRecordedFragmentView;
    /**録画リストビュー.*/
    private ListView mRecordedListView;
    /**フッタービュー.*/
    private View mFootView;
    /**コールバックリスナー.*/
    private ScrollListenerCallBack mScrollListenerCallBack;
    /**UI更新あるか.*/
    private boolean mIsUiRunning = false;
    /** Toast（連続して出さない為に保持）. */
    private Toast mToast = null;

    /**
     * コールバックリスナー.
     */
    public interface ScrollListenerCallBack {
        /**
         * スクロール時のコールバック.
         *
         * @param fragment         fragment
         * @param absListView      absListView
         * @param firstVisibleItem firstVisibleItem
         * @param visibleItemCount visibleItemCount
         * @param totalItemCount   totalItemCount
         */
        void onScroll(RecordedBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);

        /**
         * スクロール状態が変化した時のコールバック.
         *
         * @param fragment    fragment
         * @param absListView absListView
         * @param scrollState スクロール状態
         */
        void onScrollStateChanged(RecordedBaseFragment fragment, AbsListView absListView, int scrollState);
    }

    /**
     * スクロールに対するリスナーを設定する.
     *
     * @param mScrollListenerCallBack リスナー
     */
    public void setScrollListener(final ScrollListenerCallBack mScrollListenerCallBack) {
        this.mScrollListenerCallBack = mScrollListenerCallBack;
    }

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
		mChannelNameCache = new HashSet<>();
    }

    /**
     * コンテンツデータをクリアする.
     */
    public void clearContentsList() {
        mContentsList.clear();
    }

    /**
     * コンテンツデータをクリアする（Queue）.
     */
    public void clearQueueIndex() {
        mQueueIndex.clear();
    }

    /**
     * コンテンツデータのサイズを取得.
     * @return コンテンツリストサイズ
     */
    public int getContentsListSize() {
        return mContentsList.size();
    }

    /**
     * Queueのサイズを取得.
     * @return Queueサイズ
     */
    public int getQueueIndexSize() {
        return mQueueIndex.size();
    }

    /**
     * コンテンツデータを追加.
     * @param data コンテンツデータ
     */
    public void addContentsList(final RecordedContentsDetailData data) {
        mContentsList.add(data);
    }

    /**
     * コンテンツデータを取得.
     * @return コンテンツリスト
     */
    public List<RecordedContentsDetailData> getContentsList() {
        return mContentsList;
    }

    /**
     * Queueデータを追加.
     * @param index インデックス
     */
    public void addQueueIndex(final Integer index) {
        mQueueIndex.add(index);
    }

    /**
     * コンテンツアイテムデータを取得.
     * @param index インデックス
     * @return コンテンツデータ
     */
    public RecordedContentsDetailData getContentsListElement(final int index) {
        return mContentsList.get(index);
    }

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

            mRecordedListView.setOnItemClickListener(this);
            mRecordedListView.setOnScrollListener(this);

            getContext();
        }

        mContentsAdapter = new ContentsAdapter(getContext(),
                mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_RECORDED_LIST, this);
        mRecordedListView.setAdapter(mContentsAdapter);
        if (((RecordedListActivity) mActivity).getTabPosition() == 0) {
            if (null == mFootView) {
                mFootView = View.inflate(getContext(), R.layout.search_load_more, null);
            }
        } else {
            ((RecordedListActivity) mActivity).setRecordedTakeOutContents();
        }
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
        clearContentsList();
    }

    /**
     * UI状態設定.
     *
     * @param isUiRunning UI更新可否
     */
    public void setIsUiRunning(final boolean isUiRunning) {
        this.mIsUiRunning = isUiRunning;
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
    public void onItemClick(final AdapterView<?> adapterView, final View view,
                            final int i, final long l) {
        if (null != mContext) {
            if (mActivity != null) {
                RecordedContentsDetailData detailData = mContentsList.get(i);
                if (detailData.getDownLoadStatus() == ContentsAdapter.DOWNLOAD_STATUS_COMPLETED) {
                    if (!isFileExist(detailData)) {
                        showErrorDialog(getString(R.string.common_empty_data_message), R.string.common_text_close);
                        return;
                    }
                }
                Intent intent = new Intent(mContext, ContentDetailActivity.class);
                intent.putExtra(DtvtConstants.SOURCE_SCREEN, mActivity.getComponentName().getClassName());
                intent.putExtra(RecordedListActivity.RECORD_LIST_KEY, detailData);
                RecordedListActivity recordedListActivity = (RecordedListActivity) mActivity;
                recordedListActivity.startActivity(intent);
            }
        }
    }

    /**
     * ファイル存在チェック.
     *
     * @param detailData コンテンツ詳細データ
     * @return 存在の場合true
     */
    private boolean isFileExist(final RecordedContentsDetailData detailData) {
        String itemId = detailData.getItemId();
        if (!TextUtils.isEmpty(itemId) && !itemId.startsWith(DownloaderBase.sDlPrefix)) {
            itemId = DownloaderBase.getFileNameById(itemId);
        }
        List<String> pathList = DownloaderBase.getDownloadPathList(mActivity);
        boolean isExist = false;
        for (String dlFilePath : pathList) {
            File file = new File(dlFilePath + File.separator + itemId);
            if (file.exists()) {
                detailData.setDlFileFullPath(dlFilePath + File.separator + itemId);
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * DlDataProvider使用開始.
     */
    public void onDlDataProviderAvailable() {
        if (mIsUiRunning) {
            mDownloadDataProvider.setUiRunning(true);
        }
        if (!mDownloadDataProvider.isDownloading()) {
            mDownloadDataProvider.setDownloadParam(mDownloadParam);
            mDownloadDataProvider.start();
        }
    }

    /**
     * DlDataProvider使用不可時の設定.
     */
    public void onDlDataProviderUnavailable() {
        mDownloadDataProvider = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (RecordedListActivity.RLA_FragmentName_All.equals(mFragmentName) && null != mDownloadDataProvider) {
            mDownloadDataProvider.setUiRunning(false);
            mIsUiRunning = false;
            if (mDownloadDataProvider.getIsRegistered()) {
                mDownloadDataProvider.endProvider();
            }
            if (0 == mQueue.size()) {
                if (!mDownloadDataProvider.isDownloading()) {
                    mDownloadDataProvider.stopService();
                }
            } else {
                mDownloadDataProvider.setQue(mQueue);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RecordedListActivity.RLA_FragmentName_All.equals(mFragmentName)) {
            if (mDownloadDataProvider == null) {
                mDownloadDataProvider = DownloadDataProvider.getInstance(mActivity);
                if (mDownloadDataProvider == null) {
                    showMessage();
                    return;
                }
            }
            mDownloadDataProvider.setUiRunning(true);
            mIsUiRunning = true;
        }
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    /**
     * 全てのダウンロードをキャンセルする.
     *
     * @param fullPath フルパス
     */
    public void onCancelAll(final String fullPath) {
        if (null == fullPath || fullPath.isEmpty()) {
            return;
        }
        unQueueAllExceptForFirst();
        onCancelByBg(fullPath);
    }

    /**
     * 成功ステータスを設定する.
     *
     * @param fullPath フルパス
     */
    private void setSuccessStatus(final String fullPath) {
        if (mQueueIndex.size() > 0 && null != mRecordedListView) {
            int idx0 = mQueueIndex.get(0) - mRecordedListView.getFirstVisiblePosition();
            View view = mRecordedListView.getChildAt(idx0);
            if (view != null) {
                view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.drawable.icon_circle_normal_download_check_selector);
                setDownloadStatusClear(view.findViewById(R.id.item_common_result_clip_tv));
				restoreChannelAndTime();
            }
            int idx = mQueueIndex.get(0);
            if (mContentsData != null && mContentsData.size() > idx) {
                ContentsData cd = mContentsData.get(idx);
                if (null != cd) {
                    cd.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_COMPLETED);
                    cd.setDlFileFullPath(fullPath);
                    cd.setDownloadStatus("");
                    cd.setTime(getChannelTimeContent(cd.getTime()));
                    setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_COMPLETED, fullPath);
                }
            }
        }
        setNextDownLoad();
    }

    /**
     * チャンネルと時間を戻す.
     */
	private void restoreChannelAndTime() {
        if (0 == mQueueIndex.size() || null == mRecordedListView) {
            return;
        }
        int idx = mQueueIndex.get(0) - mRecordedListView.getFirstVisiblePosition();
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
        if (!mIsUiRunning) {
            return;
        }
        if (-1 < index - mQueueIndex.size()) {
            return;
        }
        View view;
        int idx = mQueueIndex.get(index);
        if (null == mRecordedListView) {
            return;
        }
        view = mRecordedListView.getChildAt(idx - mRecordedListView.getFirstVisiblePosition());
        if (view != null) {
            View tvView = view.findViewById(R.id.item_common_result_clip_tv);
            if (null != tvView) {
                tvView.setBackgroundResource(R.drawable.icon_circle_normal_download_selector);
                setDownloadStatusClear(tvView);
            }
        }
        restoreChannelAndTime();
        mContentsData.get(idx).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
        mContentsData.get(idx).setDownloadStatus("");
        mContentsData.get(idx).setTime(getChannelTimeContent(mContentsData.get(idx).getTime()));
        setContentListStatusContent(idx, ContentsAdapter.DOWNLOAD_STATUS_ALLOW, fullPath);
    }

    /**
     * 次のダウンロードを設定.
     */
    private void setNextDownLoad() {
        unQueue(0);
        if (!mIsUiRunning) {
            return;
        }
        if (mQueue.size() > 0) {
            boolean isOk = prepareDownLoad(mQueueIndex.get(0));
            if (!isOk) {
                return;
            }
            mDownloadDataProvider.setDownloadParam(mDownloadParam);
            mDownloadDataProvider.start();
        } else {
            if (mDownloadDataProvider.getIsRegistered()) {
                mDownloadDataProvider.endProvider();
            }
            mDownloadDataProvider.stopService();
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
        if (mContentsList.size() <= index || 0 > index) {
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
        if (null == fullPath || fullPath.isEmpty()) {
            return;
        }
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showErrorDialog(getString(R.string.record_download_low_storage_space_msg), R.string.custom_dialog_ok);
                    if (mDownloadDataProvider != null) {
                        mDownloadDataProvider.cancelDownLoadStatus(fullPath, false);
                    }
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
                    view.findViewById(R.id.item_common_result_clip_tv).setBackgroundResource(R.drawable.icon_circle_active_cancel_selector);
                }
                mContentsData.get(index).setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_LOADING);
                break;
            case ContentsAdapter.DOWNLOAD_STATUS_LOADING:
                mProgress = progress
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
                mContentsData.get(index).setTime(getCutChannelTimeContent(mContentsData.get(index).getTime()));
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
        return onlyTime;
    }

    /**
     * エラーダイアログ表示.
     *
     * @param msg エラーメッセージ
     * @param confirmText 確定ボタン文言
     */
    private void showErrorDialog(final String msg, final int confirmText) {
        CustomDialog resultDialog = new CustomDialog(mContext, CustomDialog.DialogType.ERROR);
        resultDialog.setOnTouchOutside(false);
        resultDialog.setCancelable(false);
        resultDialog.setContent(msg);
        resultDialog.setConfirmText(confirmText);
        resultDialog.showDialog();
    }

    @Override
    public void downloadClick(final View view) {
        boolean result = DlnaUtils.getActivationState(mContext);
        if (!result) {
            showErrorDialog(getString(R.string.activation_failed_error_download), R.string.common_text_close);
            return;
        }
        int index = (int) view.getTag();
        switch (mContentsData.get(index).getDownloadFlg()) {
            case ContentsAdapter.DOWNLOAD_STATUS_ALLOW :
                if (mQueue.size() == 0) {
                    boolean isOk = prepareDownLoad(index);
                    if (!isOk) {
                        return;
                    }
                    mDownloadDataProvider.beginProvider(getActivity(), true);
                }
                DownloadData downloadData = setDlData(index);
                if (downloadData != null) {
                    mDownloadDataProvider.setDownloadData(downloadData);
                    enqueue(index, downloadData);
                    setDownloadStatus(index, 0);
                    //mDownloadDataProvider.start();
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
    private DownloadData setDlData(final int index) {
        if (index >= mContentsList.size()) {
            return null;
        }
        DownloadData downloadData = new DownloadData();
        RecordedContentsDetailData itemData = mContentsList.get(index);
        downloadData.setItemId(DownloaderBase.getFileNameById(itemData.getItemId()));
        downloadData.setSaveFile(DownloaderBase.getDownloadPath(getContext()));
        downloadData.setTotalSize(itemData.getClearTextSize());
        downloadData.setTitle(itemData.getTitle());
        downloadData.setUrl(itemData.getResUrl());
        downloadData.setBitrate(itemData.getBitrate());
        downloadData.setPort(String.valueOf(DlnaDmsItem.getPortFromProtocal(itemData.getVideoType())));
        downloadData.setDuration(itemData.getDuration());
        downloadData.setVideoType(itemData.getVideoType());
        downloadData.setUpnpIcon(itemData.getUpnpIcon());
        downloadData.setChannelName(itemData.getRecordedChannelName());
        downloadData.setDate(itemData.getDate());
        DlnaDmsItem dmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getContext());
        downloadData.setHost(dmsItem.mIPAddress);
        downloadData.setPercentToNotify(String.valueOf(mPercentToUpdateUi));

        if (null == itemData.getXml()) {
            showMessage();
            return null;
        }
        downloadData.setXmlToDownLoad(itemData.getXml());
        return downloadData;
    }

    /**
     * ダウンロード準備.
     *
     * @param index インデックス
     * @return ダウンロード開始判定
     */
    @SuppressWarnings("OverlyLongMethod")
    private boolean prepareDownLoad(final int index) {
        Context context = getActivity();
        if (null == context) {
            return false;
        }
        String dlPath = DownloaderBase.getDownloadPath(context);
        RecordedContentsDetailData item = mContentsList.get(index);
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
        long clearTextSizeInt;
        try {
            clearTextSizeInt = Long.parseLong(clearTextSize);
        } catch (NumberFormatException e) {
            showMessage();
            return false;
        }
        mDownloadParam = new DtcpDownloadParam();
        DtcpDownloadParam dtcpDownloadParam = (DtcpDownloadParam) mDownloadParam;
        dtcpDownloadParam.setContext(context);
        dtcpDownloadParam.setSavePath(dlPath);
        dtcpDownloadParam.setSaveFileName(dlFileName);
        dtcpDownloadParam.setTitle(item.getTitle());
        dtcpDownloadParam.setDtcp1host(ip);
        dtcpDownloadParam.setDtcp1port(portInt);
        dtcpDownloadParam.setUrl(url);
        dtcpDownloadParam.setCleartextSize(clearTextSizeInt);
        dtcpDownloadParam.setItemId(item.getItemId());
        dtcpDownloadParam.setPercentToNotify(mPercentToUpdateUi);
        if (null == item.getXml()) {
            showMessage();
            return false;
        }
        dtcpDownloadParam.setXmlToDownLoad(item.getXml());
        //mDownloadDataProvider.beginProvider(getActivity());

        return true;
    }

    /**
     * サービスとバインドする.
     *
     */
    public void bindServiceFromBackground() {
        if (null == mActivity) {
            mActivity = getActivity();
        }
        if (null == mActivity) {
            return;
        }
        mDownloadDataProvider.beginProvider(mActivity, false);
        if (mQueueIndex.size() > 0) {
            mQueue.clear();
            for (int i = 0; i < mQueueIndex.size(); i++) {
                DownloadData downloadData = setDlData(mQueueIndex.get(i));
                mQueue.add(downloadData);
            }
            prepareDownLoad(mQueueIndex.get(0));
        }
        onDownloadProgressByBg(mDownloadDataProvider.getPercent());
    }

    /**
     * ダウンロード進捗率変化時に呼ばれる.
     *
     * @param percent パーセント
     */
    public void onDownloadProgressByBg(final int percent) {
        if (percent < 0) {
            return;
        }
        if (mQueueIndex.size() > 0) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDownloadStatus(mQueueIndex.get(0), percent);
                }
            });
        }
    }

    /**
     * ダウンロード完了時に呼ばれる.
     *
     * @param fullPath フルパス
     */
    public void onDownloadSuccessByBg(final String fullPath) {
        if (null == fullPath || fullPath.isEmpty()) {
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setSuccessStatus(fullPath);
            }
        });
    }

    /**
     * ダウンロード失敗時に呼ばれる.
     *  @param fullPath フルパス
     *
     */
    public void onDownloadFailByBg(final String fullPath) {
        if (null == fullPath || fullPath.isEmpty()) {
            return;
        }
        //showMessage();
        if (mDownloadDataProvider != null) {
            mDownloadDataProvider.cancelDownLoadStatus(fullPath, false);
        }
        setCancelStatus(fullPath);
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
        if (mActivity != null) {
            if (mToast != null) {
                mToast.cancel();
            }
            if (!mActivity.isFinishing()) {
                mToast = Toast.makeText(mActivity, getResources().getString(R.string.record_download_error_message), Toast.LENGTH_SHORT);
                mToast.show();
            } else {
                mToast = null;
            }

        }
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
            customDialog.setContent(getResources().getString(R.string.record_download_delete_title));
        } else {
            customDialog.setContent(getResources().getString(R.string.record_download_cancel_title));
            customDialog.setConfirmText(R.string.record_download_cancel_confirm);
        }
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @SuppressWarnings("OverlyLongMethod")
            @Override
            public void onOKCallback(final boolean isOK) {
                if (isOK) {
                    if (!completed) {
                        if (null != mDownloadDataProvider && 0 < mQueueIndex.size()) {
                            int num = (int) view.getTag();
                            boolean isCurDl = mQueueIndex.get(0) == num;
                            if (isCurDl) {
                                cancelCurrentDl();
                            } else {
                                for (int i = 0; i < mQueueIndex.size(); i++) {
                                    if (num == mQueueIndex.get(i)) {
                                        String path = getCurrentDlFullPath(i);
                                        mDownloadDataProvider.cancelDownLoadStatus(path, false);
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
                        if (mDownloadDataProvider == null) {
                            mDownloadDataProvider = DownloadDataProvider.getInstance(mActivity);
                            if (mDownloadDataProvider == null) {
                                showMessage();
                            }
                        }
                        mDownloadDataProvider.cancelDownLoadStatus(path.toString(), true);
                        noticeActDel(path.toString());
                    }
                    setDownloadStatusClear(view);
                    view.setBackgroundResource(R.drawable.icon_circle_normal_download_selector);
                    ContentsData contentsData = mContentsData.get((int) view.getTag());
                    boolean isDownload = ContentsAdapter.DOWNLOAD_STATUS_COMPLETED == contentsData.getDownloadFlg();
                    contentsData.setDownloadFlg(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                    contentsData.setDownloadStatus("");
                    if (((RecordedListActivity) mActivity).getCurrentPosition() == 1) {
                        ((RecordedListActivity) mActivity).setRecordedTakeOutContents();
                    } else {
                        if (isDownload) {
                            ((RecordedListActivity) mActivity).requestInit();
                            ((RecordedListActivity) mActivity).getData();
                        }
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
        mDownloadDataProvider.cancel();
        String path = getCurrentDlFullPath(0);
        mDownloadDataProvider.cancelDownLoadStatus(path, false);
    }

    /**
     * 現在のDLのフルパスを取得する.
     *
     * @param idx DL番号
     * @return フルパス
     */
    private String getCurrentDlFullPath(final int idx) {
        if (idx >= mQueue.size()) {
            return null;
        }
        return StringUtils.getConnectStrings(mQueue.get(idx).getSaveFile(), File.separator,
                mQueue.get(idx).getItemId());
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
        ((RelativeLayout) view.getParent().getParent()).findViewById(R.id.item_common_result_recorded_content_hyphen).setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }

    /**
     * enqueue.
     * @param index index
     * @param downloadData downloadData
     */
    private void enqueue(final int index, final DownloadData downloadData) {
        mQueue.add(downloadData);
        mQueueIndex.add(index);
    }

    /**
     * unQueue.
     * @param index index
     */
    private void unQueue(final int index) {
        if (index < mQueue.size() && -1 < index) {
            mQueue.remove(index);
        }
        if (index < mQueueIndex.size() && -1 < index) {
            mQueueIndex.remove(index);
        }
    }

    /**
     * unQueueAllExceptForFirst.
     */
    private void unQueueAllExceptForFirst() {
        if (null == mActivity || null == mDownloadDataProvider) {
            return;
        }
        if (1 == mQueue.size()) {
            mDownloadDataProvider.cancel();
            return;
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fullPath;
                for (int i = 0; i < mQueue.size(); ++i) {
                    fullPath = getCurrentDlFullPath(i);
                    if (!TextUtils.isEmpty(fullPath)) {
                        if (0 == i) {
                            mDownloadDataProvider.cancel();
                        }
                        setCancelStatusOnly(fullPath, i);
                        mDownloadDataProvider.cancelDownLoadStatus(fullPath, false);
                    }
                }
                for (int i = 0; i < mQueue.size(); ++i) {
                    mQueue.remove(i);
                }
                for (int i = 0; i < mQueueIndex.size(); ++i) {
                    mQueueIndex.remove(i);
                }
            }
        });
    }

    /**
     * 「持ち出し」タッブに使われる.
     * @param fullPath fullPath
     */
    private void noticeActDel(final String fullPath) {
        if (null != mActivity) {
            ((RecordedListActivity) mActivity).onNoticeActDel(fullPath, this);
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
        for (int i = 0; i < mContentsList.size(); ++i) {
            RecordedContentsDetailData rcd = mContentsList.get(i);
            if (fullPath.equals(rcd.getDlFileFullPath())) {
                rcd.setDownLoadStatus(ContentsAdapter.DOWNLOAD_STATUS_ALLOW);
                break;
            }
        }
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
        if (null == fullPath || fullPath.isEmpty()) {
            return;
        }
        if (mActivity != null) {
            if (mDownloadDataProvider != null) {
                mDownloadDataProvider.cancelDownLoadStatus(fullPath, false);
            }
            setCancelStatus(fullPath);
        }
    }

    /**
     * ダウンロード開始時に呼ばれる.
     */
    public void onStartBg() {
        if (null == mActivity) {
            return;
        }
        if (mDownloadDataProvider != null && mIsUiRunning) {
            mDownloadDataProvider.setUiRunning(true);
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mQueueIndex.size() > 0) {
                    setDownloadStatus(mQueueIndex.get(0), 0);
                }
            }
        });
    }

    /**
     * ローディング処理（ページング）.
     */
    public void loadComplete() {
        mRecordedListView.removeFooterView(mFootView);
    }

    /**
     * ローディング開始.
     */
    public void loadStart() {
        if (mFootView == null) {
            mFootView = View.inflate(getContext(), R.layout.search_load_more, null);
        }
        mRecordedListView.removeFooterView(mFootView);
        mRecordedListView.addFooterView(mFootView);
        mRecordedListView.setSelection(mRecordedListView.getMaxScrollAmount());
        mRecordedListView.setVisibility(View.VISIBLE);
    }

    /**
     * 表示しているコンテンツ数を返却する.
     *
     * @return コンテンツ数
     */
    public int getDataCount() {
        if (null == mContentsData) {
            return 0;
        }
        return mContentsData.size();
    }

    /**
     * データプロバイダーを返却する.
     *
     * @return データプロバイダー
     */
    public DownloadDataProvider getDownloadDataProvider() {
        return mDownloadDataProvider;
    }

    /**
     * ダウンロードキューを返却する.
     *
     * @return ダウンロードキュー
     */
    public List<DownloadData> getDownloadQueue() {
        return mQueue;
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
        if (null != mScrollListenerCallBack) {
            mScrollListenerCallBack.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(final AbsListView absListView, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        if (null != mScrollListenerCallBack) {
            mScrollListenerCallBack.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
