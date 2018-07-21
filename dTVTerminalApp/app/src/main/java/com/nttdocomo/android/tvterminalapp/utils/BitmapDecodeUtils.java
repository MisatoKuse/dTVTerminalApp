/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 画像の縮小処理.
 */
public class BitmapDecodeUtils {
    /** デフォルト(dpi) . */
    private static final int DEFAULT_DENSITY = 240;
    /** 縮小比率(density) . */
    private static final float SCALE_FACTOR = 0.75f;
    /** アスペクト比(16:9)の16.*/
    private static final int SCREEN_RATIO_WIDTH_16 = 16;
    /** アスペクト比(16:9)の9.*/
    private static final int SCREEN_RATIO_HEIGHT_9 = 9;
    /** 色の縮小 . */
    private static final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.RGB_565;

    /**
     * 画像の処理.
     * @param context コンテキスト
     */
    private static BitmapFactory.Options getBitmapOptions(final Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //縮小可フラグ
        options.inScaled = true;
        //画像のピクセルモード（デフォルトARGB_8888）
        options.inPreferredConfig = DEFAULT_BITMAP_CONFIG;
        options.inJustDecodeBounds = false;
        //dpi
        int displayDensityDpi = context.getResources().getDisplayMetrics().densityDpi;
        //密度
        float displayDensity = context.getResources().getDisplayMetrics().density;
        if (displayDensityDpi > DEFAULT_DENSITY && displayDensity > 1.5f) {
            int density = (int) (displayDensityDpi * SCALE_FACTOR);
            //bitmapのpixelを調整する
            options.inDensity = density;
            options.inTargetDensity = density;
        }
        return options;
    }

    /**
     * ストリームをバイトに変換.
     * @param inputStream ストリーム
     * @return バイト
     */
    private static byte[] readStream(final InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            int n;
            byte[] buffer = new byte[1024];
            while ((n = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, n);
            }
            outputStream.close();
            inputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            DTVTLogger.debug(e);
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
        }
        return null;
    }

    /**
     * 画像の比率縮小処理.
     * @param context コンテキスト
     * @param is 画像ストリーム
     * @param imageSizeType 画像のサイズ
     * @return bitmap
     */
    public static Bitmap compressBitmap(final Context context, final InputStream is, final ThumbnailDownloadTask.ImageSizeType imageSizeType) {
        Bitmap bitmap = null;
        try {
            int maxWidth = 0;
            int maxHeight = 0;
            switch (imageSizeType) {
                case CONTENT_DETAIL:
                    maxWidth = context.getResources().getDisplayMetrics().widthPixels;
                    maxHeight = maxWidth / SCREEN_RATIO_WIDTH_16 * SCREEN_RATIO_HEIGHT_9;
                    break;
                case HOME_LIST:
                    maxWidth = (int) context.getResources().getDimension(R.dimen.home_contents_thumbnail_width);
                    maxHeight = (int) context.getResources().getDimension(R.dimen.home_contents_thumbnail_height);
                    break;
                case TV_PROGRAM_LIST:
                    maxWidth = (int) context.getResources().getDimension(R.dimen.panel_content_thumbnail_width);
                    maxHeight = (int) context.getResources().getDimension(R.dimen.panel_content_thumbnail_height);
                    break;
                case LIST:
                    maxWidth = (int) context.getResources().getDimension(R.dimen.watch_listen_thumbnail_high);
                    maxHeight = (int) context.getResources().getDimension(R.dimen.watch_listen_thumbnail_width);
                    break;
                case CHANNEL:
                    maxWidth = (int) context.getResources().getDimension(R.dimen.channel_list_thumbnail_width);
                    maxHeight = (int) context.getResources().getDimension(R.dimen.channel_list_thumbnail_height);
                    break;
                default:
                    break;
            }
            byte[] data = readStream(is);
            if (data != null) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(data, 0, data.length, opt);
                int height = opt.outHeight;
                int width = opt.outWidth;
                int sampleSize = computeSampleSize(width, height, maxWidth, maxHeight);
                BitmapFactory.Options options = getBitmapOptions(context);
                options.inSampleSize = sampleSize;
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            }
        } catch (NullPointerException e) {
            DTVTLogger.debug(e);
        }
        return bitmap;
    }

    /**
     * bitmapを縮小処理.
     * @param context コンテキスト
     * @param srcBitmap 縮小前のbitmap
     * @param imageSizeType 画像のサイズ
     * @return bitmap
     */
    public static Bitmap createScaleBitmap(final Context context, final Bitmap srcBitmap, final ThumbnailDownloadTask.ImageSizeType imageSizeType) {
        int dstWidth = 0;
        int dstHeight = 0;
        switch (imageSizeType) {
            case HOME_LIST:
                dstWidth = (int) context.getResources().getDimension(R.dimen.home_contents_thumbnail_width);
                dstHeight = (int) context.getResources().getDimension(R.dimen.home_contents_thumbnail_height);
                break;
            case TV_PROGRAM_LIST:
                dstWidth = (int) context.getResources().getDimension(R.dimen.panel_content_thumbnail_width);
                dstHeight = (int) context.getResources().getDimension(R.dimen.panel_content_thumbnail_height);
                break;
            case LIST:
                dstWidth = (int) context.getResources().getDimension(R.dimen.watch_listen_thumbnail_high);
                dstHeight = (int) context.getResources().getDimension(R.dimen.watch_listen_thumbnail_width);
                break;
            case CHANNEL:
                dstWidth = (int) context.getResources().getDimension(R.dimen.channel_list_thumbnail_width);
                dstHeight = (int) context.getResources().getDimension(R.dimen.channel_list_thumbnail_height);
                break;
            case CONTENT_DETAIL:
            default:
                break;
        }
        if (dstWidth > 0 && srcBitmap != null) {
            return Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, false);
        }
        return srcBitmap;
    }

    /**
     * 画像の比率縮小処理.
     * @param context コンテキスト
     * @param pathName キャッシュパス
     * @param imageSizeType 画像のサイズ
     * @return bitmap
     */
    public static Bitmap compressBitmap(final Context context, final String pathName, final ThumbnailDownloadTask.ImageSizeType imageSizeType) {
        if (context == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(pathName);
            return compressBitmap(context, is, imageSizeType);
        } catch (FileNotFoundException e) {
            DTVTLogger.debug(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
        }
        return null;
    }

    /**
     * 画像の比率縮小処理.
     * @param width bitmap幅さ
     * @param height bitmap高さ
     * @param maxWidth 画像最大幅さ
     * @param maxHeight 画像最大高さ
     */
    private static int computeSampleSize(final int width, final int height, final int maxWidth, final int maxHeight) {
        int inSampleSize = 1;
        if (height > maxHeight || width > maxWidth) {
            final int heightRate = Math.round((float) height / (float) maxHeight);
            final int widthRate = Math.round((float) width / (float) maxWidth);
            inSampleSize = heightRate < widthRate ? heightRate : widthRate;
        }
        if (inSampleSize % 2 != 0) {
            inSampleSize -= 1;
        }
        return inSampleSize <= 1 ? 1 : inSampleSize;
    }
}
