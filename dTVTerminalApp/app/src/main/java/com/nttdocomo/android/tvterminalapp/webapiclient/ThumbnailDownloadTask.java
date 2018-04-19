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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * サムネイル画像取得タスク.
 */
public class ThumbnailDownloadTask extends AsyncTask<String, Integer, Bitmap> {
    /** サムネイルのURL. */
    private String imageUrl;
    /** 取得したサムネイルを表示するImageView. */
    private ImageView imageView;
    /** サムネイルプロバイダー. */
    private ThumbnailProvider thumbnailProvider;
    /** SSLチェック用コンテキスト. */
    private Context mContext;
    /** 通信停止用コネクション蓄積. */
    private volatile static List<HttpURLConnection> mUrlConnections = null;
    /** 通信停止フラグ. */
    private boolean mIsStop = false;

    /**
     * サムネイルダウンロードのコンストラクタ.
     *
     * @param imageView イメージビュー
     * @param thumbnailProvider サムネイルプロバイダー
     * @param context コンテキスト
     */
    public ThumbnailDownloadTask(final ImageView imageView, final ThumbnailProvider thumbnailProvider,
                                 final Context context) {
        this.imageView = imageView;
        this.thumbnailProvider = thumbnailProvider;

        //コンテキストの退避
        mContext = context;

        //コネクション蓄積が存在しなければ作成する
        if (mUrlConnections == null) {
            mUrlConnections = new ArrayList<>();
        }
    }

    @Override
    protected Bitmap doInBackground(final String... params) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            imageUrl = params[0];
            Bitmap bitmap = thumbnailProvider.mThumbnailCacheManager.getBitmapFromDisk(imageUrl);
            if (bitmap != null) {
                thumbnailProvider.mThumbnailCacheManager.putBitmapToMem(imageUrl, bitmap);
                return bitmap;
            }
            if (isCancelled() || mIsStop) {
                return null;
            }
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

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                //コネクトに成功したので、控えておく
                addUrlConnections(urlConnection);
            }

            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            bitmap = BitmapFactory.decodeStream(in);
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
    protected void onPostExecute(final Bitmap result) {
        super.onPostExecute(result);
        if (imageView != null) {
            if (result != null) {
                // 画像のpositionをズレないよう
                if (imageView.getTag() != null && imageUrl.equals(imageView.getTag())) {
                    imageView.setImageBitmap(result);
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

    /**
     * 削除に備えてコネクション蓄積.
     *
     * @param mUrlConnection コネクション
     */
    private synchronized void addUrlConnections(final HttpURLConnection mUrlConnection) {
        //通信が終わり、ヌルが入れられる場合に備えたヌルチェック
        if (mUrlConnections == null) {
            //既に削除されていたので、再度確保を行う
            mUrlConnections = new ArrayList<>();
        }

        //HTTPコネクションを追加する
        mUrlConnections.add(mUrlConnection);
    }

    /**
     * 全ての通信を遮断する.
     */
     public synchronized void stopAllConnections() {
         mIsStop = true;
         if (mUrlConnections == null) {
             return;
         }

         //全てのコネクションにdisconnectを送る
         Iterator<HttpURLConnection> iterator = mUrlConnections.iterator();
         while (iterator.hasNext()) {
             final HttpURLConnection stopConnection = iterator.next();

             Thread thread = new Thread(new Runnable() {
                 @Override
                 public void run() {
                     if (stopConnection != null) {
                         stopConnection.disconnect();
                     }
                 }
             });
             thread.start();
         }
         mUrlConnections.clear();
     }
}
