/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nttdocomo.android.ocsplib.OcspURLConnection;
import com.nttdocomo.android.ocsplib.OcspUtil;
import com.nttdocomo.android.ocsplib.exception.OcspParameterException;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.utils.BitmapDecodeUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserAgentUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * サムネイル画像取得タスク.
 */
public class ThumbnailDownloadTask extends AsyncTask<String, Integer, Bitmap> {
    /**画像サイズ種類.*/
    public enum ImageSizeType {
        /**コンテンツ詳細.*/
        CONTENT_DETAIL,
        /**ホーム.*/
        HOME_LIST,
        /**番組表.*/
        TV_PROGRAM_LIST,
        /**録画一覧.*/
        LIST,
        /**チャンネル一覧.*/
        CHANNEL,
    }
    /** サムネイルのURL. */
    private String mImageUrl;
    /** 取得したサムネイルを表示するImageView. */
    private final ImageView mImageView;
    /** サムネイルプロバイダー. */
    private final ThumbnailProvider mThumbnailProvider;
    /** SSLチェック用コンテキスト. */
    private final Context mContext;
    /** 通信停止用コネクション蓄積. */
    private volatile List<HttpURLConnection> mUrlConnections = null;
    /** 通信停止フラグ. */
    private boolean mIsStop = false;
    /**画像サイズ種類.*/
    private final ImageSizeType mImageSizeType;
    /**
     * サムネイルダウンロードのコンストラクタ.
     *
     * @param imageView イメージビュー
     * @param thumbnailProvider サムネイルプロバイダー
     * @param context コンテキスト
     * @param type 画像サイズ種類
     */
    public ThumbnailDownloadTask(final ImageView imageView, final ThumbnailProvider thumbnailProvider,
                                 final Context context, final ImageSizeType type) {
        mImageView = imageView;
        mThumbnailProvider = thumbnailProvider;
        mImageSizeType = type;
        mContext = context.getApplicationContext();
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
            mImageUrl = params[0];
            if (TextUtils.isEmpty(mImageUrl)) {
                return null;
            }
            Bitmap bitmap = null;
            if (mImageView.getTag() != null) {
                if (mImageSizeType != ImageSizeType.CONTENT_DETAIL) {
                    bitmap = mThumbnailProvider.thumbnailCacheManager.getBitmapFromDisk(mImageUrl, mImageSizeType);
                }
                if (bitmap != null) {
                    mThumbnailProvider.thumbnailCacheManager.putBitmapToMem(mImageUrl, bitmap);
                    return bitmap;
                }
            }

            //圏外等の判定
            if ((mContext != null && !NetWorkUtils.isOnline(mContext))
                    || mContext == null) {

                return null;
            }

            synchronized (this) {
                if (isCancelled() || mIsStop) {
                    return null;
                }
                URL url = new URL(mImageUrl);
                urlConnection = (HttpURLConnection) url.openConnection();

                // UserAgentを設定
                urlConnection.setRequestProperty(DtvtConstants.USER_AGENT, UserAgentUtils.getCustomUserAgent());
                DTVTLogger.debug("Set UserAgent:" + UserAgentUtils.getCustomUserAgent());

                //SSL証明書失効チェックライブラリの初期化を行う
                OcspUtil.init(mContext);

                //SSL証明書失効チェックを行う
                OcspURLConnection ocspURLConnection = new OcspURLConnection(urlConnection);
                ocspURLConnection.connect();

                int statusCode = urlConnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    //コネクトに成功したので、控えておく
                    addUrlConnections(urlConnection);
                }

                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
                if (mImageSizeType == ImageSizeType.TV_PROGRAM_LIST) {
                    bitmap = BitmapDecodeUtils.compressBitmap(mContext, in, ImageSizeType.TV_PROGRAM_LIST);
                } else if (mImageSizeType == ImageSizeType.CONTENT_DETAIL) {
                    bitmap = BitmapDecodeUtils.compressBitmap(mContext, in, ImageSizeType.CONTENT_DETAIL);
                } else {
                    bitmap = BitmapDecodeUtils.compressBitmap(mContext, in, ImageSizeType.HOME_LIST);
                }

                if (bitmap != null) {
                    // ディスクにプッシュする（詳細画面除外）
                    if (mImageSizeType != ImageSizeType.CONTENT_DETAIL) {
                        if (mImageView.getTag() != null) {
                            mThumbnailProvider.thumbnailCacheManager.saveBitmapToDisk(mImageUrl, bitmap);
                        }
                    }
                    Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    if (mImageSizeType != ImageSizeType.TV_PROGRAM_LIST) {
                        copy = BitmapDecodeUtils.createScaleBitmap(mContext, copy, mImageSizeType);
                    }
                    // メモリにプッシュする（詳細画面除外）
                    if (mImageSizeType != ImageSizeType.CONTENT_DETAIL) {
                        if (mImageView.getTag() != null) {
                            mThumbnailProvider.thumbnailCacheManager.putBitmapToMem(mImageUrl, copy);
                        }
                    }
                }
            }
            return bitmap;
        } catch (SSLHandshakeException e) {
            DTVTLogger.warning("SSLHandshakeException");
            //　SSL証明書が失効していたので、通信中止
            DTVTLogger.debug(e);
        } catch (SSLPeerUnverifiedException e) {
            DTVTLogger.warning("SSLPeerUnverifiedException");
            // SSLチェックライブラリの初期化が行われていないので、通信中止
            DTVTLogger.debug(e);
        } catch (OcspParameterException e) {
            DTVTLogger.warning("OcspParameterException");
            // SSLチェックの初期化に失敗しているので、通信中止（通常は発生しないとの事）
            DTVTLogger.debug(e);
        } catch (IOException e) {
//            DTVTLogger.debug(e); //現在サーバーからの不正で大量の例外になるため、コメントアウト
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
        if (mImageView != null) {
            if (TextUtils.isEmpty(mImageUrl)) {
                setErrorImageResource(mImageView);
            } else {
                Object imageTag = mImageView.getTag();
                if (imageTag == null) {
                    imageTag = mImageView.getTag(R.id.tag_key);
                }
                if (result != null) {
                    // 画像のpositionをズレないよう
                    if (imageTag != null && mImageUrl.equals(imageTag.toString())) {
                        mImageView.setImageBitmap(result);
                    }
                } else { // 画像取得失敗
                    if (imageTag != null && mImageUrl.equals(imageTag.toString())) {
                        setErrorImageResource(mImageView);
                    }
                }
            }
        }
        mThumbnailProvider.decrementCurrentQueueCount();
        mThumbnailProvider.checkQueueList();
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
        if (mUrlConnections == null) {
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (ThumbnailDownloadTask.this) {
                    mIsStop = true;
                    //全てのコネクションにdisconnectを送る
                    for (int i = 0; i < mUrlConnections.size(); i++) {
                        final HttpURLConnection stopConnection = mUrlConnections.get(i);
                        if (stopConnection != null) {
                            stopConnection.disconnect();
                        }
                    }
                    mUrlConnections.clear();
                }
            }
        });
        thread.start();
    }

    /**
     * 画像取得失敗時のエラー画像Resource.
     * @param dst ImageView
     */
    private void setErrorImageResource(final ImageView dst) {
        int resId = R.mipmap.error_scroll;
        switch (mImageSizeType) {
            case CONTENT_DETAIL:
                resId = R.mipmap.error_movie;
                break;
            case HOME_LIST:
                resId = R.mipmap.error_scroll;
                break;
            case TV_PROGRAM_LIST:
                resId = R.mipmap.error_ch_mini;
                break;
            case LIST:
                resId = R.mipmap.error_list;
                break;
            case CHANNEL:
                resId = R.mipmap.error_ch_mini;
                break;
        }
        dst.setImageResource(resId);
    }

    /**
     * メモリーキャッシュをクリアして、ガベージコレクションされやすくする.
     */
    public void removeMemoryCache() {
        mThumbnailProvider.removeMemoryCache();
    }
}
