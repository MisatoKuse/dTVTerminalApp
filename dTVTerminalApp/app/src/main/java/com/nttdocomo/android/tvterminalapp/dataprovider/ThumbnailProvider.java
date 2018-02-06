/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.ThumbnailCacheManager;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.util.ArrayList;
import java.util.List;

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
	private static final int MAXQUEUECOUNT = 4;
	/**
	 * ImageViewリスト.
	 */
	private List<ImageView> listImageView = new ArrayList<>();
	/**
	 * URLリスト.
	 */
	private List<String> listURL = new ArrayList<>();
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
	private boolean isCancel = false;

	/**
	 * コンストラクタ.
	 *
	 * @param context コンテキスト
	 */
	public ThumbnailProvider(final Context context) {
		mThumbnailCacheManager = new ThumbnailCacheManager(context);
		mThumbnailCacheManager.initMemCache();

		//コンテキストの退避
		mContext = context;
	}

	/**
	 * 画像の取得.
	 * 
	 * @param imageView 取得した画像を表示するimageView
	 * @param imageUrl サムネイル取得先URL
	 */
	public Bitmap getThumbnailImage(final ImageView imageView, final String imageUrl) {
		// メモリから取得
		Bitmap bitmap = mThumbnailCacheManager.getBitmapFromMem(imageUrl);

		if (bitmap != null) {
			DTVTLogger.debug("image exists in memory");
			return bitmap;
		}

		// ディスクから取得
		bitmap = mThumbnailCacheManager.getBitmapFromDisk(imageUrl);
		if (bitmap != null) {
			DTVTLogger.debug("image exists in file");
			// メモリにプッシュする
			mThumbnailCacheManager.putBitmapToMem(imageUrl, bitmap);
			return bitmap;
		}

		if (!isCancel) {
			// サーバからQueueで取得
			if (!TextUtils.isEmpty(imageUrl)) {
				DTVTLogger.debug("download start..... url=" + imageUrl);
				if (TextUtils.isEmpty(imageUrl)) {
					return null;
				}
				imageView.setTag(imageUrl);
				//queue処理を追加
				if (MAXQUEUECOUNT > currentQueueCount) {
					++currentQueueCount;
					mDownloadTask = new ThumbnailDownloadTask(imageView, this, mContext);
					mDownloadTask.execute(imageUrl);
				} else {
					listImageView.add(imageView);
					listURL.add(imageUrl);
				}
			}
		}
		return null;
	}

	/**
	 * queueチェック.
	 */
	public void checkQueueList() {
		DTVTLogger.start();
		DTVTLogger.debug("" + isCancel);
		if (!isCancel) {
			if (MAXQUEUECOUNT > currentQueueCount) {
				if (listImageView.size() > 0) {
					++currentQueueCount;
					new ThumbnailDownloadTask(listImageView.get(0), this, mContext).execute(listURL.get(0));
					listImageView.remove(0);
					listURL.remove(0);
				}
			}
		}
	}

	/**
	 * ダウンロード処理を停止する.
	 */
	public void stopConnect() {
		DTVTLogger.start();
		isCancel = true;
		if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
			mDownloadTask.stopAllConnections();
		}
	}
}
