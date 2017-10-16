package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.WebApiClient.ThumbnailDownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
public class ThumbnailProvider {

	// キャッシュマネージャー
	public ThumbnailCacheManager mThumbnailCacheManager;

	//Queue
	public int currentQueueCount = 0;
	private static final int MAXQUEUECOUNT = 4;

	//ImageViewリスト
	public List<ImageView> listImageView = new ArrayList<>();
	//URLリスト
	public List<String> listURL = new ArrayList<>();

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ThumbnailProvider(Context context) {
		mThumbnailCacheManager = new ThumbnailCacheManager(context);
		mThumbnailCacheManager.initMemCache();
	}

	/**
	 * 画像の取得
	 * 
	 * @param imageView
	 * @param imageUrl
	 */
	public Bitmap getThumbnailImage(ImageView imageView, String imageUrl) {
		// メモリから取得
		Bitmap bitmap = mThumbnailCacheManager.getBitmapFromMem(imageUrl);

		if (bitmap != null) {
			Log.i("dTV", "image exists in memory");
			return bitmap;
		}

		// ディスクから取得
		bitmap = mThumbnailCacheManager.getBitmapFromDisk(imageUrl);
		if (bitmap != null) {
			Log.i("dTV", "image exists in file");
			// メモリにプッシュする
			mThumbnailCacheManager.putBitmapToMem(imageUrl, bitmap);
			return bitmap;
		}

		// サーバからQueueで取得
		if (!TextUtils.isEmpty(imageUrl)) {
            Log.i("dTV", "download start..... url="+imageUrl);
            if (TextUtils.isEmpty(imageUrl)) {
                return null;
            }
            imageView.setTag(imageUrl);
            //queue処理を追加
            if (MAXQUEUECOUNT > currentQueueCount) {
                ++currentQueueCount;
                new ThumbnailDownloadTask(imageView, this).execute(imageUrl);
            } else {
                listImageView.add(imageView);
                listURL.add(imageUrl);
            }
		}
		return null;
	}

	/**
	 * queueチェック
	 *
	 */
	public void checkQueueList() {
		if (MAXQUEUECOUNT > currentQueueCount) {
			if (listImageView.size() > 0){
				++currentQueueCount;
				new ThumbnailDownloadTask(listImageView.get(0), this).execute(listURL.get(0));
				listImageView.remove(0);
				listURL.remove(0);
			}
		}
	}
}
