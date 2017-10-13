package com.nttdocomo.android.tvterminalapp.DataProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
public class ThumbnailProvider {

	// キャッシュマネージャー
	private ThumbnailCacheManager mThumbnailCacheManager;

	//Queue
	private int currentQueueCount = 0;
	private int maxQueueCount = 4;

	//ImageViewリスト
	private List<ImageView> listImageView = new ArrayList<>();
	//URLリスト
	private List<String> listURL = new ArrayList<>();

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
            if (maxQueueCount > currentQueueCount) {
                ++currentQueueCount;
                new ImageDownloadTask(imageView).execute(imageUrl);
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
	private void checkQueueList() {
		if (maxQueueCount > currentQueueCount) {
			if (listImageView.size() > 0){
				++currentQueueCount;
				new ImageDownloadTask(listImageView.get(0)).execute(listURL.get(0));
				listImageView.remove(0);
				listURL.remove(0);
			}
		}
	}

	class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {

		private String imageUrl;
		private ImageView imageView;

		public ImageDownloadTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedInputStream in = null;
			try {
				imageUrl = params[0];
				/*String key = mThumbnailUtil.hashKeyForDisk(imageUrl);
				// 取得成功した画像のストリームをdiskに保存する
				DiskLruCache.Editor editor = diskCache.edit(key);
				if (editor != null) {
					OutputStream outputStream = editor.newOutputStream(0);
					if (downloadUrlToStream(imageUrl, outputStream)) {
						editor.commit();
					} else {
						editor.abort();
					}
				}*/
                final URL url = new URL(imageUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
//				diskCache.flush();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
//                Bitmap bitmap = mThumbnailCacheManager.getBitmapFromDisk(imageUrl);
				mThumbnailCacheManager.saveBitmapToDisk(imageUrl,bitmap);
				if (bitmap != null) {
					// メモリにプッシュする
					mThumbnailCacheManager.putBitmapToMem(imageUrl, bitmap);
				}
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				try {
					if (in != null) {
						in.close();
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
            return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null && imageView!=null) {
				// 画像のpositionをズレないよう
				if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
					imageView.setImageBitmap(result);
					Log.i("dTV", "download end___tag_____="+imageUrl);
				}
			}
			Log.i("dTV", "download end________="+imageUrl);
			--currentQueueCount;
			checkQueueList();
		}
	}
}
