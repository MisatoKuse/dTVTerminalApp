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
	public final ThumbnailCacheManager thumbnailCacheManager;
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
	private LinkedHashMap<String, ImageView> mListUrl = null;
	/**
	 * コンテキスト.
	 */
	private final Context mContext;
	/**
	 * サムネイルダウンロードのタスク.
	 */
	private ThumbnailDownloadTask mDownloadTask = null;
	/**
	 * ダウンロードキャンセルフラグ.
	 */
	private boolean mIsCancel = false;
	/**
	 * 画像サイズ種類.
	 */
	private final ThumbnailDownloadTask.ImageSizeType mImageSizeType;
	/**
	 * コンストラクタ.
	 * @param imageSizeType 画像サイズ種類
	 * @param context コンテキスト
	 */
	public ThumbnailProvider(final Context context, final ThumbnailDownloadTask.ImageSizeType imageSizeType) {
		thumbnailCacheManager = new ThumbnailCacheManager(context);
		mListUrl = new LinkedHashMap<>();
		thumbnailCacheManager.initMemCache();
		mImageSizeType = imageSizeType;
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
		Bitmap bitmap = thumbnailCacheManager.getBitmapFromMem(imageUrl);

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
                mDownloadTask = new ThumbnailDownloadTask(imageView, this, mContext, mImageSizeType);
                mDownloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
            } else {
				//url重複がある場合
                if (mListUrl.containsKey(imageUrl)) {
					mListUrl.remove(imageUrl);
                }
				mListUrl.put(imageUrl, imageView);
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
				&& mListUrl.size() > 0) {
			++currentQueueCount;
			String imageUrl = mListUrl.entrySet().iterator().next().getKey();
			ImageView imageView = mListUrl.get(imageUrl);
			new ThumbnailDownloadTask(imageView, this, mContext, mImageSizeType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
			mListUrl.remove(imageUrl);
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

	/**
	 * メモリキャッシュを解放させる.
	 */
	public void removeMemoryCache() {
		thumbnailCacheManager.removeAll();
	}
}
