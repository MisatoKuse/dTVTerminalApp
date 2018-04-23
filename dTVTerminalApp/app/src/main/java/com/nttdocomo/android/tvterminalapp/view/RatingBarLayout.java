/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.text.DecimalFormat;

/**
 * レーティングバーレイアウト.
 */
public class RatingBarLayout extends LinearLayout {

    /**
     * コンストラクタ.
     */
    private Context mContext = null;
    /**
     * 最大評価値.
     */
    private final static int numStars = 5;
    /**
     * 評価最小基準値.
     */
    private final static float BASE_VALUE = 0.1f;
    /**
     * フォーマット.
     */
    private final static String FORMAT = "0.0";
    /**
     * The x coordinate of the first pixel in source.
     */
    private final static int PIXEL0 = 0;
    /**
     * フォーマット(number = 0).
     */
    private final static String FORMAT_NUMBER_0 = "-";
    /**
     * 区別フラグ（一覧と詳細）.
     */
    private boolean isMini = true;
    /**
     * 一覧画面のナンバーのテキストサイズ.
     */
    private static final int MINI_NUMBER_TEXT_SIZE = 12;
    /**
     * コンテンツ詳細画面のナンバーのテキストサイズ.
     */
    private static final int NUMBER_TEXT_SIZE = 14;
    /**
     * 開始index.
     */
    private static final int START_NUMBER_START_INDEX = 1;

    /**
     * コンストラクタ.
     *
     * @param context コンテクスト
     */
    public RatingBarLayout(final Context context) {
        this(context, null);
    }

    /**
     * コンストラクタ.
     * @param context コンテクスト
     * @param attrs attrs
     */
    public RatingBarLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * コンストラクタ.
     * @param context コンテクスト
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public RatingBarLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 画面区別を設定 (stars).
     *
     * @param isMini 画面区別フラグ.
     */
    public void setMiniFlg(final boolean isMini) {
        this.isMini = isMini;
    }

    /**
     * ratingを設定 (stars).
     *
     * @param rating stars.
     */
    public void setRating(final float rating) {
        this.removeAllViews();
        float resetRating = Float.parseFloat(StringUtils.toRatString(String.valueOf(rating)));
        setProgress(resetRating);
    }

    /**
     * stars進捗を設定.
     *
     * @param progress stars進捗.
     */
    private synchronized void setProgress(final float progress) {
        int fullStar = (int) (progress);
        float dec = Float.parseFloat(new DecimalFormat(FORMAT).format(progress - fullStar));
        if (fullStar == 0 && dec < BASE_VALUE) {
            setProgressNumbers(FORMAT_NUMBER_0);
            return;
        }
        for (int i = START_NUMBER_START_INDEX; i <= numStars; i++) {
            if (i == fullStar + START_NUMBER_START_INDEX && dec >= BASE_VALUE) {
                setProgressStars(dec);
            } else {
                setFullStars(i <= fullStar);
            }
        }
        setProgressNumbers(String.valueOf(progress));
    }

    /**
     * 進捗ナンバーを設定.
     *
     * @param progressNumbers 進捗ナンバー
     */
    private void setProgressNumbers(final String progressNumbers) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getNumWidth(),
                LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.rating_star_numbers_margin), PIXEL0, PIXEL0, PIXEL0);
        textView.setText(progressNumbers);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white_text));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, isMini ? MINI_NUMBER_TEXT_SIZE : NUMBER_TEXT_SIZE);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.TOP);
        this.addView(textView);
    }

    /**
     * 全starを設定.
     *
     * @param progressStars 進捗パーセント
     */
    private void setProgressStars(final float progressStars) {
        RelativeLayout mRelativeLayout = new RelativeLayout(mContext);
        mRelativeLayout.addView(getFullStarsIcon());
        int percentWidth = (int) (getStarHeightWidth() * progressStars);
        mRelativeLayout.addView(getProgressStarIcon(percentWidth));
        this.addView(mRelativeLayout);
    }

    /**
     * 全starを設定.
     *
     * @param isBackground 背景フラグ
     */
    private void setFullStars(final boolean isBackground) {
        ImageView imageView = getFullStarsIcon();
        if (isBackground) {
            imageView.setImageResource(R.mipmap.rate_star_active);
        } else {
            imageView.setImageResource(R.mipmap.rate_star_normal);
        }
        this.addView(imageView);
    }

    /**
     * starの高さ、幅さ取得.
     * @return starの高さ、幅さ
     */
    private float getStarHeightWidth() {
        if (isMini) {
            return getResources().getDimension(R.dimen.rating_star_mini_width_height);
        } else {
            return getResources().getDimension(R.dimen.rating_star_width_height);
        }
    }

    /**
     * numberの幅さ取得.
     * @return numberの幅さ
     */
    private int getNumWidth() {
        if (isMini) {
            return (int) getResources().getDimension(R.dimen.rating_star_mini_numbers_width);
        } else {
            return (int) getResources().getDimension(R.dimen.rating_star_numbers_width);
        }
    }

    /**
     * 全starのIcon取得.
     * @return  全starのIcon
     */
    private ImageView getFullStarsIcon() {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) getStarHeightWidth(),
                (int) getStarHeightWidth()
        );
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(R.mipmap.rate_star_normal);
        return imageView;
    }

    /**
     * 進捗starのIcon取得.
     *
     * @param progressStarWidth 進捗star幅さ
     * @return 進捗starのIcon
     */
    private ImageView getProgressStarIcon(final int progressStarWidth) {
        ImageView imageView = new ImageView(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                progressStarWidth,
                (int) getStarHeightWidth()
        );
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.rate_star_active);
        float ratio = getStarHeightWidth() / bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, PIXEL0, PIXEL0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(resizeBitmap);
        return imageView;
    }
}
