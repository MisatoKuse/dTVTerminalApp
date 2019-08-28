

/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.utils.ContentDetailUtils;
import com.nttdocomo.android.tvterminalapp.utils.GoogleAnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

/**リモートコントローラービュークラス.*/
public class RemoteControllerView extends RelativeLayout implements ViewPager.OnPageChangeListener {
    /** コンテキスト.*/
    private Context mContext = null;
    /** タッチDOWNされたY座標とビューTOPの距離.*/
    private int mDownY = 0;
    /** 移動した距離.*/
    private int mMovedY = 0;
    /** スクロールできる最大範囲.*/
    private int mScrollHeight = 0;
    /** ヘッダーの高さ.*/
    private int mHeaderHeight = 0;
    /** TOPまで表示されてるか.*/
    private boolean mIsTop = false;
    /** 最初に見える高さ.*/
    private float mVisibilityHeight = 0;
    /** システムTime.*/
    private long mSysTime;
    /** クリックなのかスワイプなのか.*/
    private boolean mIsClick = false;
    /** 最初から表示されているか否か.*/
    private boolean mIsFirstVisible = false;
    /** コンテンツ詳細リモコンタイプ.*/
    private ContentDetailUtils.RemoteControllerType mRemoteControllerType;
    /** 子ビュー.*/
    private View mChild = null;
    /** Scroller.*/
    private Scroller mScroller = null;
    /** リストビュー.*/
    private final List<View> mViewList = new ArrayList<>();
    /** ViewPager.*/
    private ViewPager mViewPager = null;
    /** FrameLayout.*/
    private FrameLayout mFrameLayout = null;
    /** SendKeyAction.*/
    private RemoteControllerSendKeyAction remoteControllerSendKeyAction = null;
    /** GestureDetector.*/
    private GestureDetector mParentGestureDetector = null;
    /** GestureDetector.*/
    private GestureDetector mGestureDetector = null;
    /** RelativeLayout.*/
    private RelativeLayout mBottomLinearLayout, mTopLinearLayout = null;
    /** リスナー.*/
    private OnStartRemoteControllerUIListener mStartUIListener = null;
    /** 640 基準値（幅さ）.*/
    private static final int BASE_WIDTH = 360;
    /** 640 基準値（高さ）.*/
    private static final int BASE_HEIGHT = 640;
    /** 8 基準値（左右padding）.*/
    private static final int BASE_LEFT_RIGHT_PADDING = 8;
    /** 80 基準値（タイトル高さ）.*/
    private static final int BASE_TITLE = 80;
    /** 0 基準値（paddingTop 0）.*/
    private static final int BASE_PADDING_TOP = 0;
    /** 2 基準値（両側）.*/
    private static final int BASE_LEFT_RIGHT = 2;
    /** クリック判定閾値時間(ms).*/
    private static final long CLICK_MAX_TIME = 100;

    /**
     * このViewがtopに表示された際に通知するリスナー.
     */
    public interface OnStartRemoteControllerUIListener {
        /**
         *リモートコントローラーUI表示開始コールバック.
         * @param isFromHeader へだータップされて起動するのか
         */
        void onStartRemoteControl(boolean isFromHeader);

        /**
         * リモートコントローラーUI表示閉じる.
         */
        void onEndRemoteControl();

        /**
         * 宅外利用時のエラー.
         */
        void onErrorRemoteControl(ContentDetailUtils.RemoteControllerType remoteControllerType);
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public RemoteControllerView(final Context context) {
        this(context, null);
    }

    /**
     * コンストラクタ.
     * @param context コンテキスト
     * @param attrs  attrs
     */
    public RemoteControllerView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * コンストラクタ.
     * @param context コンテキスト
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public RemoteControllerView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (context instanceof ContentDetailActivity) {
            mHeaderHeight = 52;
        } else {
            mHeaderHeight = 0;
        }
        init(context);
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);

        // コンテンツ詳細画面でもコントローラのヘッダーを表示しない場合は高さを0に設定
        if (!mIsFirstVisible) {
            mHeaderHeight = 0;
        }
        // Headerを表示させる画面では、リモコンViewの下部に移動させ、50dpだけ表示させる
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mScrollHeight = (int) (mChild.getMeasuredHeight() - (mHeaderHeight * metrics.density));
        mVisibilityHeight = mHeaderHeight * metrics.density;
        //リモコンの表示領域を設定する
        mChild.layout(mChild.getLeft(), mChild.getTop() + mScrollHeight, mChild.getRight(), mChild.getBottom() + mScrollHeight);
    }

    /**
     * viewの初期化.
     *
     * @param context コンテキスト
     */
    public void init(final Context context) {
        mScroller = new Scroller(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * リモコンタイプの設定（コンテンツ詳細）
     *
     * @param remoteControllerType　リモコンタイプ
     */
    public void setRemoteControllerType(final ContentDetailUtils.RemoteControllerType remoteControllerType) {
        this.mRemoteControllerType = remoteControllerType;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChild = getChildAt(0);
        mParentGestureDetector = new GestureDetector(this.getContext(), mGestureListener);
    }

    /**
     * クリックなのかを設定.
     * @param is true or false
     */
    private void setIsClick(final boolean is) {
        synchronized (this) {
            mIsClick = is;
        }
    }

    /**
     * リモコンの初期表示状態を指定する.
     * @param visible true:ヘッダーが表示されている false:ヘッダーが表示されていない
     */
    public void setIsFirstVisible(final Boolean visible) {
        mIsFirstVisible = visible;
    }

    /**
     * @param event タッチevent.
     * @return Touchイベントを下位に伝えるかどうか
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        DTVTLogger.start("view_event:" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //イベント時間を取得する
                mSysTime = event.getEventTime();
                //タッチされたY座標とビューTOPの距離を取得する
                mDownY = (int) event.getY();
                //子ビュー以外の部分がタップされた場合Touchイベントを走らせない
                if (mDownY < mChild.getMeasuredHeight() - mVisibilityHeight && !mIsTop) {
                    return false;
                }
                setHighlight(event);
                //viewPagerを初期化する
                if (mViewList.size() == 0) {
                    setPager();
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (mRemoteControllerType == ContentDetailUtils.RemoteControllerType.DTV_VIEWING_NG
                        || mRemoteControllerType == ContentDetailUtils.RemoteControllerType.UNABLE_TO_USE) {
                    // スワイプ中に「宅外」に変わった場合、操作可能とする
                    if (mMovedY == 0) {
                        return false;
                    }
                }
                int moveY = 0;
                moveY = (int) event.getY();
                //mDeY:移動した距離
                //下に移動の場合,mDeY < 0；上に移動の場合,mDeY > 0;
                int deY = mDownY - moveY;
                //上に移動するときの処理
                if (deY > 0) {
                    //移動した距離を加算する,移動できる範囲内に制限する
                    mMovedY += deY;
                    if (mMovedY > mScrollHeight) {
                        mMovedY = mScrollHeight;
                    }
                    if (mMovedY < mScrollHeight) {
                        scrollBy(0, deY);
                        mDownY = moveY;
                        return true;
                    }
                }
                //下に移動するときの処理
                if (deY < 0) {
                    mMovedY += deY;
                    if (mMovedY < 0) {
                        mMovedY = 0;
                    }
                    if (mMovedY > 0) {
                        scrollBy(0, deY);
                    }
                    mDownY = moveY;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                setHighlight(event);
                //指が離すときの時間差を計算する
                Long curTime = event.getEventTime();
                Long diff = curTime - mSysTime;
                //クリックなのかを判断する
                if (diff < CLICK_MAX_TIME) {
                    setIsClick(true);
                } else {
                    setIsClick(false);
                }
                //子ビューがタップされた場合
                if (mDownY > mScrollHeight - mVisibilityHeight && mIsClick && !mIsTop) {
                    // 「宅外利用中」あるいは「STB視聴不可」コンテンツの場合はエラートースト表示
                    if (mRemoteControllerType == ContentDetailUtils.RemoteControllerType.DTV_VIEWING_NG
                            || mRemoteControllerType == ContentDetailUtils.RemoteControllerType.UNABLE_TO_USE) {
                        mStartUIListener.onErrorRemoteControl(mRemoteControllerType);
                        return true;
                    }
                    mScroller.startScroll(0, getScrollY(), 0, mScrollHeight - getScrollY(), DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
                    postInvalidate();
                    setHeaderContent(false);
                    break;
                }
                //指が離すまで子ビュ―の移動した距離は子ビューの1/4に超えた場合、
                // 自動的にTOPまで移動する
                if (mMovedY > mScrollHeight / 4) {
                    mScroller.startScroll(0, getScrollY(), 0, (mScrollHeight - getScrollY()), DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
                    invalidate();
                    if (!mIsTop) {
                        setHeaderContent(false);
                    }
                } else {
                    //1/4に満たしていない場合、元の位置に戻る
                    //表示するコンテンツを設定する
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
                    postInvalidate();
                    mMovedY = 0;
                    mBottomLinearLayout = findViewById(R.id.bottom_view_ll);
                    mBottomLinearLayout.setVisibility(VISIBLE);
                    mTopLinearLayout = findViewById(R.id.top_view_ll);
                    mTopLinearLayout.setVisibility(GONE);
                    mFrameLayout = findViewById(R.id.header_watch_by_tv);
                    if (mIsTop) {
                        closeRemoteControllerUI();
                        mIsTop = false;
                    }
                }
                break;
            default:
                break;
        }
        DTVTLogger.end();
        return super.onTouchEvent(event);
    }

    /**
     * ハイライト設定
     * @param event タッチイベント
     */
    private void setHighlight(final MotionEvent event) {
        if (!mIsTop) {
            // 「宅外利用中」あるいは「STB視聴不可」コンテンツの場合はハイライト設定なし
            if (mRemoteControllerType == ContentDetailUtils.RemoteControllerType.DTV_VIEWING_NG
                    || mRemoteControllerType == ContentDetailUtils.RemoteControllerType.UNABLE_TO_USE) {
                return;
            }
            FrameLayout backgroundView = findViewById(R.id.header_watch_by_tv);
            ImageView tvIcon = findViewById(R.id.remote_tv_play_icon);
            TextView textView = findViewById(R.id.watch_by_tv);
            TextView statusTextView = findViewById(R.id.remote_control_status);
            ImageView arrowTopIcon = findViewById(R.id.remote_controller_down);
            int tvIconResourceId;
            int textColorResourceId;
            int arrowTopIconResourceId;
            int backgroundResourceId;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    switch (mRemoteControllerType) {
                        case DAZN:
                            tvIconResourceId = R.mipmap.tv_dazn;
                            textColorResourceId = R.color.remote_watch_dazn_highlight_text;
                            break;
                        default:
                            tvIconResourceId = R.mipmap.tv_tap;
                            textColorResourceId = R.color.remote_watch_default_highlight_text;
                            break;
                    }
                    arrowTopIconResourceId = mRemoteControllerType.getArrowTapResourceId();
                    backgroundResourceId = mRemoteControllerType.getBackgroundTapResourceId();
                    break;
                case MotionEvent.ACTION_UP:
                    switch (mRemoteControllerType) {
                        case DAZN:
                            tvIconResourceId = R.mipmap.tv_black;
                            textColorResourceId = R.color.remote_watch_by_tv_bottom_dazn_text;
                            break;
                        default:
                            tvIconResourceId = R.mipmap.tv;
                            textColorResourceId = R.color.basic_text_color_white;
                            break;
                    }
                    arrowTopIconResourceId = mRemoteControllerType.getArrowResourceId();
                    backgroundResourceId = mRemoteControllerType.getBackgroundResourceId();
                    break;
                default:
                    return;
            }
            backgroundView.setBackground(ResourcesCompat.getDrawable(getResources(), backgroundResourceId, null));
            tvIcon.setImageResource(tvIconResourceId);
            textView.setTextColor(ResourcesCompat.getColor(getResources(), textColorResourceId, null));
            statusTextView.setTextColor(ResourcesCompat.getColor(getResources(), textColorResourceId, null));
            arrowTopIcon.setImageResource(arrowTopIconResourceId);
        }
    }

    /**
     * 子ビュ―が一番上まで移動した場合表示するコンテンツを設定する.
     * @param isFromHeader ヘッダーからUI表示
     */
    private void setHeaderContent(final boolean isFromHeader) {
        DTVTLogger.start();
        mMovedY = mScrollHeight;
        mIsTop = true;
        mTopLinearLayout = (findViewById(R.id.top_view_ll));
        mTopLinearLayout.setVisibility(VISIBLE);
        mBottomLinearLayout = findViewById(R.id.bottom_view_ll);
        mBottomLinearLayout.setVisibility(GONE);
        mFrameLayout = findViewById(R.id.header_watch_by_tv);
        mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_top_corner, null));
        GoogleAnalyticsUtils.sendScreenViewInfo(getResources().getString(R.string.google_analytics_screen_name_remote_control), null);
        startRemoteControl(isFromHeader);
        DTVTLogger.end();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * ページャー設定  .
     */
    private void setPager() {
        LayoutInflater lf = LayoutInflater.from(this.getContext());
        View inflate1 = lf.inflate(R.layout.remote_controller_player_ui_layout, this, false);
        View inflate = lf.inflate(R.layout.remote_controller_channel_ui_layout, this, false);
        ViewPagerAdapter remokonAdapter = null;

        mViewList.add(inflate1);
        mViewList.add(inflate);

        mViewPager = findViewById(R.id.remocon_viewpager);
        float width = getContext().getResources().getDisplayMetrics().widthPixels;
        float height = getContext().getResources().getDisplayMetrics().heightPixels;
        float density = getContext().getResources().getDisplayMetrics().density;
        int paddinglr = 0; //左右padding
        int paddingtb = 0; //下padding
        if (width > BASE_WIDTH * density) { //360 基準値（幅さ）
            paddinglr = (int) ((width - (BASE_WIDTH * density)) / BASE_LEFT_RIGHT);
        }
        if (height > BASE_HEIGHT * density) {
            paddingtb = (int) (height - (BASE_HEIGHT * density));
        }
        if (paddinglr != 0 || paddingtb != 0) {
            LayoutParams params = new LayoutParams(
                    (int) (width - (BASE_LEFT_RIGHT_PADDING * BASE_LEFT_RIGHT * density)), //padding除く
                    (int) (height - (BASE_TITLE * density))); //タイトル除く
            mViewPager.setLayoutParams(params);
            LayoutParams childLayoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            inflate1.setPadding(paddinglr, BASE_PADDING_TOP, paddinglr, paddingtb);
            inflate1.setLayoutParams(childLayoutParams);
            inflate.setPadding(paddinglr, BASE_PADDING_TOP, paddinglr, paddingtb);
            inflate.setLayoutParams(childLayoutParams);
        }
        remokonAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(remokonAdapter);
        mViewPager.addOnPageChangeListener(this);

        mGestureDetector = new GestureDetector(this.getContext(), mGestureListener);
        mViewPager.setOnTouchListener(mOnTouchListener);

        setRemoteControllerViewAction();
        if (mIsTop) {
            startRemoteControl(false);
        }

        findViewById(R.id.remote_controller_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                closeRemoteControllerUI();
            }
        });
    }

    /**
     * ページャー設定.
     * @param position ページ位置
     */
    private void setPagerIndex(final int position) {
        LinearLayout linearLayout = findViewById(R.id.remocon_index);
        if (linearLayout != null && linearLayout.getChildCount() > 1) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                ImageView imageView = (ImageView) linearLayout.getChildAt(i);
                if (position == i) {
                    imageView.setImageResource(R.mipmap.remote_material_paging_current);
                } else {
                    imageView.setImageResource(R.mipmap.remote_material_paging_normal);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(final int position) {
        setPagerIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
    }

    /**
     * ページャーアダプタークラス.
     */
    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }
    }

    /**
     * リモコンUIのボタン設定.
     */
    private void setRemoteControllerViewAction() {
        remoteControllerSendKeyAction = new RemoteControllerSendKeyAction(this.getContext());
        remoteControllerSendKeyAction.initRemoteControllerPlayerView(this);
        remoteControllerSendKeyAction.initRemoteControllerChannelView(this);
    }

    /**
     * リモコンUI画面 onFling処理.
     */
    private final GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
            DTVTLogger.start();

            if (e1 != null && e2 != null) {
                float flingY = e2.getY() - e1.getY();
                if (flingY > mScrollHeight / 4 && mIsTop) {
                    DTVTLogger.debug("Down");
                    closeRemoteControllerUI();
                    DTVTLogger.end();
                    return true;
                }
            }
            DTVTLogger.end();
            return false;
        }
    };

    /**
     * リモコンUI画面のonFlingを取得する.
     */
    private final OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    };
    /**
     * キーボタン上でFlingした際にonFling処理.
     */
    private final OnTouchListener mParentOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            DTVTLogger.start();
            return mParentGestureDetector.onTouchEvent(event);
        }
    };

    /**
     * リモコンUI画面を閉じる処理.
     */
    public void closeRemoteControllerUI() {
        if (mIsTop) {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
            postInvalidate();
            mMovedY = 0;
            mIsTop = false;
            mBottomLinearLayout = findViewById(R.id.bottom_view_ll);
            mBottomLinearLayout.setVisibility(VISIBLE);
            mTopLinearLayout = findViewById(R.id.top_view_ll);
            mTopLinearLayout.setVisibility(GONE);
            mFrameLayout = findViewById(R.id.header_watch_by_tv);
            remoteControllerSendKeyAction.cancelTimer();
            if (null != mStartUIListener) {
                mStartUIListener.onEndRemoteControl();
            }
            setDefaultPage();
        }
    }

    /**
     * isTopのboolean値を返す.
     * @return true or false
     */
    public boolean isTopRemoteControllerUI() {
        return mIsTop;
    }

    /**
     * リモートUI表示.
     * @param isHeader isHeader
     */
    public void startRemoteUI(final boolean isHeader) {
        //viewPagerを初期化する
        if (mViewList.size() == 0) {
            setPager();
        }

        DTVTLogger.debug("getScrollY:" + getScrollY());
        if (mIsFirstVisible) {
            mScroller.startScroll(0, 0, 0, mScrollHeight, DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
        } else {
            mScroller.startScroll(0, 0, 0, (int) (mScrollHeight + mVisibilityHeight), DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
        }
        invalidate();
        setHeaderContent(isHeader);
    }

    /**
     * リスナーを設定.
     *
     * @param listener リスナー
     */
    public void setOnStartRemoteControllerUI(final OnStartRemoteControllerUIListener listener) {
        DTVTLogger.debug("Set StartRemoteControllerUIListener");
        mStartUIListener = listener;
    }

    /**
     * リスナーが設定されている場合、通知処理を実行.
     * @param isFromHeader isFromHeader
     */
    private void startRemoteControl(final boolean isFromHeader) {
        DTVTLogger.debug(String.format("mIsTop:%s isFromHeader:%s", mIsTop, isFromHeader));
        if (mStartUIListener != null && mIsTop) {
            DTVTLogger.debug("StartUIListener.onStartRemoteControl");
            mStartUIListener.onStartRemoteControl(isFromHeader);
        }
    }

    /**
     * dアプリ起動リクエスト処理を呼び出し.
     *
     * @param type       アプリ起動要求種別
     * @param contentsId コンテンツID
     */
    public void sendStartApplicationRequest(final RemoteControlRelayClient.STB_APPLICATION_TYPES type, final String contentsId) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationRequest(type, contentsId, mContext);
        DTVTLogger.end();
    }


    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・dTVチャンネル・カテゴリー分類に対応
     *
     * @param type                dTVチャンネル
     * @param serviceCategoryType カテゴリー分類
     * @param crid                コンテンツ識別子.
     * @param chno                チャンネル番号
     */
    public void sendStartApplicationDtvChannelRequest(
            final RemoteControlRelayClient.STB_APPLICATION_TYPES type,
            final RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES serviceCategoryType,
            final String crid, final String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationDtvChannelRequest(type, serviceCategoryType, crid, chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVの番組（地デジ）
     *
     * @param serviceId サービスID
     */
    public void sendStartApplicationHikariTvCategoryTerrestrialDigitalRequest(final String serviceId) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryTerrestrialDigitalRequest(serviceId, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVの番組（BS）
     *
     * @param serviceId サービスID
     */
    public void sendStartApplicationHikariTvCategorySatelliteBsRequest(final String serviceId) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategorySatelliteBsRequest(serviceId, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVの番組（IPTV）
     *
     * @param chno チャンネルナンバー
     */
    public void sendStartApplicationHikariTvCategoryIptvRequest(final String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryIptvRequest(chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVのVOD
     *
     * @param licenseId ラインセンスID
     * @param cid       コンテンツID
     * @param crid      コンテンツ識別子.
     */
    public void sendStartApplicationHikariTvCategoryHikaritvVodRequest(final String licenseId,
                                                                       final String cid, final String crid) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryHikaritvVodRequest(
                licenseId, cid, crid, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内 dTVチャンネルの番組
     *
     * @param chno チャンネルナンバー.
     */
    public void sendStartApplicationHikariTvCategoryDtvchannelBroadcastRequest(final String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvchannelBroadcastRequest(chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内 dTVチャンネル VOD（見逃し／関連番組）
     *
     * @param crid ひかりTV内 dTVチャンネル VOD（見逃し／関連番組）コンテンツID
     */
    public void sendStartApplicationHikariTvCategoryDtvchannelMissedRequest(final String crid) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvchannelMissedRequest(crid, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内 dTVのVOD
     *
     * @param episodeId エピソードID
     * @param crid      コンテンツ識別子
     */
    public void sendStartApplicationHikariTvCategoryDtvVodRequest(final String episodeId, final String crid) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvVodRequest(episodeId, crid, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内VOD(dTV含む)のシリーズ
     *
     * @param crid ひかりTV内VOD(dTV含む)のシリーズコンテンツID
     */
    public void sendStartApplicationHikariTvCategoryDtvSvodRequest(final String crid) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvSvodRequest(
                crid, mContext);
        DTVTLogger.end();
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        DTVTLogger.start();
        mParentGestureDetector.onTouchEvent(ev);
        this.setOnTouchListener(mParentOnTouchListener);
        return !mIsTop;
    }

    /**
     * リモコンUI画面を閉じた際にplayer操作画面に戻す.
     */
    private void setDefaultPage() {
        mViewPager.setCurrentItem(0);
    }
}
