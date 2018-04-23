

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

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;

import java.util.ArrayList;
import java.util.List;


public class RemoteControllerView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private Context mContext = null;

    private int mDownY = 0;
    private int mMovedY = 0;
    private int mScrollHeight = 0;
    private int mHeaderHeight = 0;
    private boolean mIsTop = false;
    private float mVisibilityHeight = 0;
    private long mSysTime; //システムTime
    private boolean mIsClick = false; //クリックなのかスワイプなのか
    private boolean mIsFirstVisible = false; //最初から表示されているか否か

    private View mChild = null;
    private Scroller mScroller = null;
    private List<View> mViewList = new ArrayList<>();
    private ViewPager mViewPager = null;
    private FrameLayout mFrameLayout = null;
    private RemoteControllerSendKeyAction remoteControllerSendKeyAction = null;
    private GestureDetector mParentGestureDetector = null;
    private GestureDetector mGestureDetector = null;
    private RelativeLayout mBottomLinearLayout, mTopLinearLayout = null;
    private OnStartRemoteControllerUIListener mStartUIListener = null;
    /**
     * 640 基準値（幅さ）.
     */
    private static final int BASE_WIDTH = 360;
    /**
     * 640 基準値（高さ）.
     */
    private static final int BASE_HEIGHT = 640;
    /**
     * 8 基準値（左右padding）.
     */
    private static final int BASE_LEFT_RIGHT_PADDING = 8;
    /**
     * 80 基準値（タイトル高さ）.
     */
    private static final int BASE_TITLE = 80;
    /**
     * 0 基準値（paddingTop 0）.
     */
    private static final int BASE_PADDING_TOP = 0;
    /**
     * 2 基準値（両側）.
     */
    private static final int BASE_LEFT_RIGHT = 2;

    private static final long CLICK_MAX_TIME = 100;

    /**
     * このViewがtopに表示された際に通知するリスナー.
     */
    public interface OnStartRemoteControllerUIListener {
        void onStartRemoteControl(boolean isFromHeader);

        void onEndRemoteControl();
    }

    public RemoteControllerView(final Context context) {
        this(context, null);
    }

    public RemoteControllerView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

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
            // TODO: [結合](他サービス起動時のAPI変更)中継アプリ側の結合試験
            // 「ひかりTVコンテンツ詳細にテレビで視聴する」が出ないための暫定対応
//            mHeaderHeight = 0;
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChild = getChildAt(0);
        mParentGestureDetector = new GestureDetector(this.getContext(), mGestureListener);
    }

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
                //viewPagerを初期化する
                if (mViewList.size() == 0) {
                    setPager();
                }
                return true;

            case MotionEvent.ACTION_MOVE:
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
                    mScroller.startScroll(0, -getScrollY(), 0, mScrollHeight - getScrollY());
                    postInvalidate();
                    setHeaderContent(false);
                    break;
                }
                //指が離すまで子ビュ―の移動した距離は子ビューの1/4に超えた場合、
                // 自動的にTOPまで移動する
                if (mMovedY > mScrollHeight / 4 && !mIsTop) {
                    mScroller.startScroll(0, getScrollY(), 0, (mScrollHeight - getScrollY()));
                    invalidate();
                    setHeaderContent(false);
                } else {
                    //1/4に満たしていない場合、元の位置に戻る
                    //表示するコンテンツを設定する
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
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

    //子ビュ―が一番上まで移動した場合表示するコンテンツを設定する
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
        if (mIsTop) {
            startRemoteControl(isFromHeader);
        }
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

    public void setPager() {
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
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    (int) (width - (BASE_LEFT_RIGHT_PADDING * BASE_LEFT_RIGHT * density)), //padding除く
                    (int) (height - (BASE_TITLE * density))); //タイトル除く
            mViewPager.setLayoutParams(params);
            RelativeLayout.LayoutParams childLayoutParams = new RelativeLayout.LayoutParams(
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

    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view == object;
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
    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
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
    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    };

    private OnTouchListener mParentOnTouchListener = new OnTouchListener() {
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
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
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
     */
    public boolean isTopRemoteControllerUI() {
        return mIsTop;
    }

    public void startRemoteUI(final boolean isHeader) {
        //viewPagerを初期化する
        if (mViewList.size() == 0) {
            setPager();
        }

        DTVTLogger.debug("getScrollY:"+getScrollY());
        if (mIsFirstVisible) {
            mScroller.startScroll(0, 0, 0, mScrollHeight);
        } else {
            mScroller.startScroll(0, 0, 0, (int) (mScrollHeight + mVisibilityHeight));
        }
        invalidate();
        setHeaderContent(isHeader);
    }

    /**
     * リスナーを設定
     *
     * @param listener リスナー
     */
    public void setOnStartRemoteControllerUI(OnStartRemoteControllerUIListener listener) {
        DTVTLogger.debug("Set StartRemoteControllerUIListener");
        mStartUIListener = listener;
    }

    /**
     * リスナーが設定されている場合、通知処理を実行
     */
    private void startRemoteControl(boolean isFromHeader) {
        if (mStartUIListener != null && mIsTop) {
            DTVTLogger.debug("StartUIListener.onStartRemoteControl");
            mStartUIListener.onStartRemoteControl(isFromHeader);
        }
    }

    /**
     * dアプリ起動リクエスト処理を呼び出し
     *
     * @param type       アプリ起動要求種別
     * @param contentsId コンテンツID
     */
    public void sendStartApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES type, String contentsId) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationRequest(type, contentsId, mContext);
        DTVTLogger.end();
    }


    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・dTVチャンネル・カテゴリー分類に対応
     *
     * @param type dTVチャンネル
     * @param serviceCategoryType カテゴリー分類
     * @param crid
     * @param chno チャンネル番号
     */
    public void sendStartApplicationDtvChannelRequest(
            RemoteControlRelayClient.STB_APPLICATION_TYPES type,
            RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES serviceCategoryType,
            String crid, String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationDtvChannelRequest(type, serviceCategoryType, crid, chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVの番組（地デジ）
     *
     * @param chno
     */
    public void sendStartApplicationHikariTvCategoryTerrestrialDigitalRequest(String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryTerrestrialDigitalRequest(chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVの番組（BS）
     *
     * @param chno
     */
    public void sendStartApplicationHikariTvCategorySatelliteBsRequest(String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategorySatelliteBsRequest(chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVの番組（IPTV）
     *
     * @param chno
     */
    public void sendStartApplicationHikariTvCategoryIptvRequest(String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryIptvRequest(chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTVのVOD
     *
     * @param licenseId
     * @param cid
     * @param crid
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
     * @param chno
     */
    public void sendStartApplicationHikariTvCategoryDtvchannelBroadcastRequest(String chno) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvchannelBroadcastRequest(chno, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内 dTVチャンネル VOD（見逃し）
     *
     * @param tvCid
     */
    public void sendStartApplicationHikariTvCategoryDtvchannelMissedRequest(String tvCid) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvchannelMissedRequest(tvCid, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内 dTVチャンネル VOD（関連番組）
     *
     * @param tvCid
     */
    public void sendStartApplicationHikariTvCategoryDtvchannelRelationRequest(String tvCid) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvchannelRelationRequest(tvCid, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内 dTVのVOD
     *
     * @param episodeId
     */
    public void sendStartApplicationHikariTvCategoryDtvVodRequest(String episodeId) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvVodRequest(episodeId, mContext);
        DTVTLogger.end();
    }

    /**
     * 中継アプリ起動リクエスト処理を呼び出し.
     * ・ひかりTV内VOD(dTV含む)のシリーズ
     *
     * @param crid
     */
    public void sendStartApplicationHikariTvCategoryDtvSvodRequest(String crid) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationHikariTvCategoryDtvSvodRequest(
                crid, mContext);
        DTVTLogger.end();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        DTVTLogger.start();
        mParentGestureDetector.onTouchEvent(ev);
        this.setOnTouchListener(mParentOnTouchListener);
        return !mIsTop;
    }

    /**
     * リモコンUI画面を閉じた際にplayer操作画面に戻す
     */
    public void setDefaultPage() {
        mViewPager.setCurrentItem(0);
    }
}