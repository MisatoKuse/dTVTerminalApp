/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.ThumbnailCacheManager;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.util.LinkedHashMap;

/**
 * サムネイル取得用プロパイダ.
 */
public class ThumbnailProvider {

	/**
	 * キャッシュマネージャー.
	 */
	public ThumbnailCacheManager mThumbnailCacheManager;
	/**
	 * Queue.
	 */
	public int currentQueueCount = 0;
	/**
	 * Queueの最大数.
	 */
	private static final int MAX_QUEUE_COUNT = 4;
	/**
	 * URLリスト.
	 */
	private LinkedHashMap<String, ImageView> mListURL = null;
	/**
	 * コンテキスト.
	 */
	private Context mContext;
	/**
	 * サムネイルダウンロードのタスク.
	 */
	private ThumbnailDownloadTask mDownloadTask = null;
	/**
	 * ダウンロードキャンセルフラグ.
	 */
	private boolean mIsCancel = false;

	/**
	 * コンストラクタ.
	 *
	 * @param context コンテキスト
	 */
	public ThumbnailProvider(final Context context) {
		mThumbnailCacheManager = new ThumbnailCacheManager(context);
		mListURL = new LinkedHashMap<>();
		mThumbnailCacheManager.initMemCache();

		//コンテキストの退避
		mContext = context;
	}

	/**
	 * 画像の取得.
	 *
	 * @param imageView 取得した画像を表示するimageView
	 * @param imageUrl サムネイル取得先URL
     * @return サムネイル画像
	 */
	public Bitmap getThumbnailImage(final ImageView imageView, final String imageUrl) {

		if (TextUtils.isEmpty(imageUrl) || imageUrl.trim().length() == 0) {
            DTVTLogger.debug("imageUrl is null");
			return null;
		}

		// メモリから取得
		Bitmap bitmap = mThumbnailCacheManager.getBitmapFromMem(imageUrl);

		if (bitmap != null) {
			DTVTLogger.debug("image exists in memory");
			return bitmap;
		}

		if (!mIsCancel) {
			// サーバからQueueで取得
            imageView.setTag(imageUrl);
            //queue処理を追加
            if (MAX_QUEUE_COUNT > currentQueueCount) {
                ++currentQueueCount;
                mDownloadTask = new ThumbnailDownloadTask(imageView, this, mContext);
                mDownloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
            } else {
				//url重複がある場合
                if (mListURL.containsKey(imageUrl)) {
                    mListURL.remove(imageUrl);
                }
                mListURL.put(imageUrl, imageView);
            }
		} else {
            DTVTLogger.error("ThumbnailProvider is stopping connection");
        }
		return null;
	}

	/**
	 * queueチェック.
	 */
	public void checkQueueList() {
		if (mIsCancel) {
			return;
		}
		if (MAX_QUEUE_COUNT > currentQueueCount
				&& mListURL.size() > 0) {
			++currentQueueCount;
			String imageUrl = mListURL.entrySet().iterator().next().getKey();
			ImageView imageView = mListURL.get(imageUrl);
			new ThumbnailDownloadTask(imageView, this, mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
			mListURL.remove(imageUrl);
		}

	}

	/**
	 * ダウンロード処理を停止する.
	 */
	public void stopConnect() {
		DTVTLogger.start();
		mIsCancel = true;
		if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
			mDownloadTask.stopAllConnections();
		}
	}

	/**
	 * 止めたダウンロード処理を再度可能な状態にする.
	 */
	public void enableConnect() {
		mIsCancel = false;
	}
}
