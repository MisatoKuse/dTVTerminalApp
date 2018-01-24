/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nttdocomo.android.ocsplib.OcspURLConnection;
import com.nttdocomo.android.ocsplib.OcspUtil;
import com.nttdocomo.android.ocsplib.exception.OcspParameterException;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * サムネイル画像取得タスク.
 */
public class ThumbnailDownloadTask extends AsyncTask<String, Integer, Bitmap> {
    /**
     * サムネイルのURL.
     */
    private String imageUrl;
    /**
     * 取得したサムネイルを表示するImageView.
     */
    private ImageView imageView;
    /**
     * サムネイルプロバイダー.
     */
    private ThumbnailProvider thumbnailProvider;
    /**
     * SSLチェック用コンテキスト.
     */
    private Context mContext;

    /**
     * サムネイルダウンロードのコンストラクタ.
     *
     * @param imageView イメージビュー
     * @param thumbnailProvider サムネイルプロバイダー
     * @param context コンテキスト
     */
    public ThumbnailDownloadTask(ImageView imageView, ThumbnailProvider thumbnailProvider, Context context) {
        this.imageView = imageView;
        this.thumbnailProvider = thumbnailProvider;

        //コンテキストの退避
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            imageUrl = params[0];
            URL url = new URL(imageUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            //コンテキストがあればSSL証明書失効チェックを行う
            if (mContext != null) {
                DTVTLogger.debug(imageUrl);

                //SSL証明書失効チェックライブラリの初期化を行う
                OcspUtil.init(mContext);

                //SSL証明書失効チェックを行う
                OcspURLConnection ocspURLConnection = new OcspURLConnection(urlConnection);
                ocspURLConnection.connect();
            }

            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            // ディスクに保存する
            thumbnailProvider.mThumbnailCacheManager.saveBitmapToDisk(imageUrl, bitmap);
            if (bitmap != null) {
                // メモリにプッシュする
                thumbnailProvider.mThumbnailCacheManager.putBitmapToMem(imageUrl, bitmap);
            }
            return bitmap;

        } catch (SSLHandshakeException e) {
            //　SSL証明書が失効していたので、通信中止
            DTVTLogger.debug(e);
        } catch (SSLPeerUnverifiedException e) {
            // SSLチェックライブラリの初期化が行われていないので、通信中止
            DTVTLogger.debug(e);
        } catch (OcspParameterException e) {
            // SSLチェックの初期化に失敗しているので、通信中止（通常は発生しないとの事）
            DTVTLogger.debug(e);
        } catch (IOException e) {
            DTVTLogger.debug(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                DTVTLogger.debug(e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (imageView != null) {
            if (result != null) {
                // 画像のpositionをズレないよう
                if (imageView.getTag() != null && imageUrl.equals(imageView.getTag())) {
                    imageView.setImageBitmap(result);
                    DTVTLogger.debug("download end..... url=" + imageUrl);
                }
            } else {
                // 画像取得失敗のケース
                if (imageView.getTag() != null && imageUrl.equals(imageView.getTag())) {
                    imageView.setImageResource(R.mipmap.error_scroll);
                    DTVTLogger.debug("download fail..... url=" + imageUrl);
                }
            }
        }
        --thumbnailProvider.currentQueueCount;
        thumbnailProvider.checkQueueList();
    }
}
