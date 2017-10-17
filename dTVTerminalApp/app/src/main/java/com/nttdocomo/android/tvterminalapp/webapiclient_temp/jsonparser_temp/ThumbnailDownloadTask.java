/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThumbnailDownloadTask extends AsyncTask<String, Integer, Bitmap> {

    private String imageUrl;
    private ImageView imageView;
    private ThumbnailProvider thumbnailProvider;

    public ThumbnailDownloadTask(ImageView imageView, ThumbnailProvider thumbnailProvider) {
        this.imageView = imageView;
        this.thumbnailProvider = thumbnailProvider;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            imageUrl = params[0];
            URL url = new URL(imageUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            // ディスクに保存する
            thumbnailProvider.mThumbnailCacheManager.saveBitmapToDisk(imageUrl,bitmap);
            if (bitmap != null) {
                // メモリにプッシュする
                thumbnailProvider.mThumbnailCacheManager.putBitmapToMem(imageUrl,bitmap);
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
                Log.i("dTV", "download end..... url="+imageUrl);
            }
        }
        --thumbnailProvider.currentQueueCount;
        thumbnailProvider.checkQueueList();
    }
}
