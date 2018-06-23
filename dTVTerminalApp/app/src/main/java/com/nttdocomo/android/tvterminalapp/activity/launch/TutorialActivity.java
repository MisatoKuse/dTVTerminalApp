/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.SettingMenuTermsOfServiceActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * チュートリアルActivity.
 */
public class TutorialActivity extends BaseActivity implements View.OnClickListener {
    /** チュートリアルを終了またはスキップする.*/
    private Button mSkipOrFinishTutorialAcivity = null;

    /**
     * 画面の素材（上）.
     */
    private final int[] mWalkthroughsAbove = {R.mipmap.walkthrough_01,
            R.mipmap.walkthrough_02_01, R.mipmap.walkthrough_03_01,
            R.mipmap.walkthrough_04_01, R.mipmap.walkthrough_05_01 };
    /**
     * 画面の素材（下）.
     */
    private final int[] mWalkthroughsBelow = {R.mipmap.walkthrough_01,
            R.mipmap.walkthrough_02_02, R.mipmap.walkthrough_03_02,
            R.mipmap.walkthrough_04_02, R.mipmap.walkthrough_05_02 };
    /**
     * 画像編集用.
     */
    private Bitmap mExBitmap;
    /**
     * 画像縮小比率(メモリ負荷軽減するため).
     */
    private final static float RESIZE_BITMAP_RATIO = 0.8f;
    /**
     * チュートリアルビューページ.
     */
    private ViewPager mWalkthroughsViewPager = null;
    /**
     * インジケータイメージビュー(選択中).
     */
    private ImageView mCurDot;
    /**
     * チュートリアルページアダプタ.
     */
    private TutorialPagerAdapter mAdapter;
    /**
     * アダプタにセットするビューリスト.
     */
    private final List<View> mWalkthroughsViews = new ArrayList<>();
    /**
     * 選択中インジケータ表示位置計算用.
     */
    private float mOffset;
    /**
     * 選択中ページ.
     */
    private int mCurPos = 0;
    /**
     * インジケータレイアウト.
     */
    private LinearLayout mDotsLayout;
    /**
     * 規約表示テキストビュー.
     */
    private TextView mTutorialTextView;
    /**
     * 上の画像が表示してるフラグ.
     */
    private boolean mIsAbove = true;
    /**
     * 画像編集開始位置.
     */
    private final static int PIXEL0 = 0;
    /** ページン0.*/
    private final static int PAGE_1 = 0;
    /** ページン1.*/
    private final static int PAGE_2 = 1;
    /** ページン2.*/
    private final static int PAGE_3 = 2;
    /** ページン3.*/
    private final static int PAGE_4 = 3;
    /** ページン4.*/
    private final static int PAGE_5 = 4;
    /**
     * アニメーション時間（ミリ秒）.
     */
    private final static int ANIMATION_TIME_FEED_IN_OUT = 400;
    /** アニメーション待ち時間.*/
    private final static int ANIMATION_TIME_WAIT = 2000;
    /** アニメーション待ち時間.*/
    private final static int ANIMATION_TIME_MOVE_CURSOR = 300;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_main_layout);

        setContents();
        // dアカウント処理は不要
        setUnnecessaryDaccountRegistService();
    }

    /**
     * アニメーションハンドラー.
     */
    private final android.os.Handler mHandle = new android.os.Handler();
    /**アニメーション.*/
    private final Runnable mRunnableAnimation = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            if (mWalkthroughsViewPager.getCurrentItem() > 0) {
                AlphaAnimation feedInOut;
                View feedInOutView = mWalkthroughsViewPager.getChildAt(mCurPos).findViewById(R.id.tutorial_walkthroughs_above);
                if (mIsAbove) {
                    //フェードアウト
                    feedInOut = new AlphaAnimation(1, 0);
                    feedInOut.setDuration(ANIMATION_TIME_FEED_IN_OUT);
                    feedInOutView.startAnimation(feedInOut);
                    feedInOutView.setVisibility(View.INVISIBLE);
                    mIsAbove = false;
                } else {
                    //フェードイン
                    feedInOut = new AlphaAnimation(0, 1);
                    mIsAbove = true;
                    feedInOut.setDuration(ANIMATION_TIME_FEED_IN_OUT);
                    feedInOutView.startAnimation(feedInOut);
                    feedInOutView.setVisibility(View.VISIBLE);
                }
                mHandle.postDelayed(mRunnableAnimation, ANIMATION_TIME_FEED_IN_OUT + ANIMATION_TIME_WAIT);
            } else {
                mIsAbove = true;
                mHandle.removeCallbacks(mRunnableAnimation);
                return;
            }
            DTVTLogger.end();
        }
    };

    /** コンテンツ設定.*/
    private void setContents() {
        setTheme(R.style.AppThemeBlack);
        setStatusBarColor(R.color.contents_header_background);
        setTitleVisibility(false);
        mTutorialTextView = findViewById(R.id.tutorial_show_agreement);
        mTutorialTextView.setPaintFlags(mTutorialTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTutorialTextView.setOnClickListener(this);
        mSkipOrFinishTutorialAcivity = findViewById(R.id.skipOrFinishTutorialAcivity);
        mSkipOrFinishTutorialAcivity.setOnClickListener(this);
        initView();
    }

    /** ビューの初期化.*/
    private void initView() {
        mDotsLayout = findViewById(R.id.tutorial_dots_layout);
        findViewById(R.id.tutorial_dots_1).setOnClickListener(this);
        findViewById(R.id.tutorial_dots_2).setOnClickListener(this);
        findViewById(R.id.tutorial_dots_3).setOnClickListener(this);
        findViewById(R.id.tutorial_dots_4).setOnClickListener(this);
        findViewById(R.id.tutorial_dots_5).setOnClickListener(this);
        for (int i = 0; i < mWalkthroughsAbove.length; i++) {
            View walkthroughsView = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.tutorial_walkthroughs_layout, null);
            ImageView imageViewAbove = walkthroughsView.findViewById(R.id.tutorial_walkthroughs_above);
            ImageView imageViewBelow = walkthroughsView.findViewById(R.id.tutorial_walkthroughs_below);
            //３ページ目と５ページ目は上から切り取って表示
            boolean cutTop = false;
            if (i == PAGE_3 || i == PAGE_5) {
                cutTop = true;
            }
            imageViewAbove.setImageBitmap(modifyBitmap(mWalkthroughsAbove[i], cutTop));
            imageViewBelow.setImageBitmap(modifyBitmap(mWalkthroughsBelow[i], cutTop));
            mWalkthroughsViews.add(walkthroughsView);
        }

        mCurDot = findViewById(R.id.cur_dot);
        mCurDot.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mOffset = mCurDot.getWidth() + getResources().getDimensionPixelSize(R.dimen.tutorial_dots_margin);
                        return true;
                    }
                });

        mAdapter = new TutorialPagerAdapter(mWalkthroughsViews);
        mWalkthroughsViewPager = findViewById(R.id.tutorial_walkthroughs);
        mWalkthroughsViewPager.setOffscreenPageLimit(mWalkthroughsAbove.length);
        mWalkthroughsViewPager.setAdapter(mAdapter);
        mWalkthroughsViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
            }

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                //ページが切り替わったら処理を行う
                if(mCurPos != position){
                    mHandle.removeCallbacks(mRunnableAnimation);
                    mTutorialTextView.setVisibility(View.GONE);
                    mSkipOrFinishTutorialAcivity.setVisibility(View.GONE);
                    mDotsLayout.setVisibility(View.VISIBLE);
                    mCurDot.setVisibility(View.VISIBLE);
                    moveCursorTo(position);
                    if (position == mWalkthroughsAbove.length - 1) {
                        //  最後のページ
                        mTutorialTextView.setVisibility(View.VISIBLE);
                        mSkipOrFinishTutorialAcivity.setVisibility(View.VISIBLE);
                        mDotsLayout.setVisibility(View.GONE);
                        mCurDot.setVisibility(View.GONE);
                    }
                    mCurPos = position;
                    mIsAbove = true;
                    mWalkthroughsViewPager.getChildAt(mCurPos).findViewById(R.id.tutorial_walkthroughs_above).setVisibility(View.VISIBLE);
                    if (position != PAGE_1) {
                        mHandle.postDelayed(mRunnableAnimation, ANIMATION_TIME_WAIT);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(final int position) {
            }

        });
    }

    /**
     * 画像を画面サイズに合わせるよう編集.
     *
     * @param index 画像ファイルのindex.
     * @param cutTop trueの場合上から切り取って表示（下寄せ）.
     * @return Bitmap
     */
    private Bitmap modifyBitmap(final int index, final boolean cutTop) {
        clearBitmap();
        if (index < 0) {
            return null;
        }
        mExBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), index);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displaymetrics);
        float ratio = ((float) displaymetrics.widthPixels / (float) mExBitmap.getWidth());
        Matrix matrix = new Matrix();
        matrix.postScale(ratio * RESIZE_BITMAP_RATIO, ratio * RESIZE_BITMAP_RATIO);
        //画像を画面サイズに合わせて拡大縮小する
        int y = PIXEL0;
        if (cutTop) {
            y = mExBitmap.getHeight() - (int) (getHeightDensity() / ratio);
        }
        return Bitmap.createBitmap(mExBitmap, PIXEL0, y, mExBitmap.getWidth(),
                (int) (getHeightDensity() / ratio), matrix, true);
    }

    /**
     * 画像編集用メモリクリア.
     */
    private void clearBitmap() {
        if (mExBitmap != null && !mExBitmap.isRecycled()) {
            mExBitmap.recycle();
            mExBitmap = null;
        }
        System.gc();
    }

    @Override
    public void onResume() {
        super.onResume();
        clearBitmap();
        if (mCurPos != PAGE_1) {
            mHandle.postDelayed(mRunnableAnimation, ANIMATION_TIME_WAIT);
        }
        super.sendScreenView(getString(R.string.google_analytics_screen_name_tutorial));
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandle.removeCallbacks(mRunnableAnimation);
        clearBitmap();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tutorial_show_agreement:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_agreement));
                startActivity(LaunchTermsOfServiceActivity.class, null);
                break;
            case R.id.skipOrFinishTutorialAcivity:
                onSkipOrFinish();
                break;
            case R.id.tutorial_dots_1:
                mWalkthroughsViewPager.setCurrentItem(PAGE_1);
                break;
            case R.id.tutorial_dots_2:
                mWalkthroughsViewPager.setCurrentItem(PAGE_2);
                break;
            case R.id.tutorial_dots_3:
                mWalkthroughsViewPager.setCurrentItem(PAGE_3);
                break;
            case R.id.tutorial_dots_4:
                mWalkthroughsViewPager.setCurrentItem(PAGE_4);
                break;
            case R.id.tutorial_dots_5:
                mWalkthroughsViewPager.setCurrentItem(PAGE_5);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    /**
     *チュートリアル画面をスキップまたは終了するボタンがタップされた時の処理.
     * */
    private void onSkipOrFinish() {
        SharedPreferencesUtils.setSharedPreferencesIsDisplayedTutorial(this, true);
        doScreenTransition();
        finish();
    }

    /**
     * 次画面遷移.
     */
    private void doScreenTransition() {
        DTVTLogger.start();
        if (SharedPreferencesUtils.getSharedPreferencesStbConnect(this)) {
            // ペアリング済み HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, true);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            DTVTLogger.debug("ParingOK Start HomeActivity");
        } else if (SharedPreferencesUtils.getSharedPreferencesStbSelect(this)) {
            // 次回から表示しないをチェック済み
            // 未ペアリング HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, false);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            DTVTLogger.debug("ParingNG Start HomeActivity");
        } else {
            // STB選択画面へ遷移
            Intent intent = new Intent(getApplicationContext(), StbSelectActivity.class);
            intent.putExtra(StbSelectActivity.FROM_WHERE, StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Launch.ordinal());
            startActivity(intent);
            DTVTLogger.debug("Start StbSelectActivity");
        }
        DTVTLogger.end();
    }

    /**
     * 選択中インジケーターを移動する.
     *
     * @param position 移動後の位置.
     */
    private void moveCursorTo(final int position) {
        TranslateAnimation anim = new TranslateAnimation(mOffset * mCurPos,
                mOffset * position, PIXEL0, PIXEL0);
        anim.setDuration(ANIMATION_TIME_MOVE_CURSOR);
        anim.setFillAfter(true);
        mCurDot.startAnimation(anim);
    }

    /**
     * チュートリアルページアダプタ.
     */
    private static class TutorialPagerAdapter extends PagerAdapter {
        /**リストビュー.*/
        private final List<View> mViews;

        /**チュートリアルページアダプタ.
         * @param views ビュー
         */
        private TutorialPagerAdapter(final List<View> views) {
            this.mViews = views;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(final View arg0, final Object arg1) {
            return arg0.equals(arg1);
        }

        @Override
        public void restoreState(final Parcelable arg0, final ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            container.removeView(mViews.get(position));
        }

    }
}
