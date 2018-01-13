

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.model.remotecontroller.RemoteControllerSendKeyAction;
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
    private long mSysTime;//システムTime
    private boolean mIsClick = false;//クリックなのかスワイプなのか
    private boolean mIsFirstVisible = false; //最初から表示されているか否か

    private View mChild = null;
    private Scroller mScroller = null;
    private List<View> mViewList = new ArrayList<>();
    private ViewPager mViewPager = null;
    private FrameLayout mFrameLayout = null;
    private RemoteControllerSendKeyAction remoteControllerSendKeyAction = null;
    private GestureDetector mParentGestureDetector = null;
    private GestureDetector mGestureDetector = null;
    private LinearLayout mBottomLinearLayout, mTopLinearLayout = null;
    private OnStartRemoteControllerUIListener mStartUIListener = null;

    private static final long CLICK_MAX_TIME = 100;

    /**
     * このViewがtopに表示された際に通知するリスナー
     */
    public interface OnStartRemoteControllerUIListener {
        void onStartRemoteControl();

        void onEndRemoteControl();
    }

    public RemoteControllerView(Context context) {
        this(context, null);
    }

    public RemoteControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (context instanceof DtvContentsDetailActivity) {
            mHeaderHeight = 50;
        } else {
            mHeaderHeight = 0;
        }
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
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
     * viewの初期化
     *
     * @param context コンテキスト
     */
    public void init(Context context) {
        mScroller = new Scroller(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChild = getChildAt(0);
        mParentGestureDetector = new GestureDetector(this.getContext(), mGestureListener);
    }

    private void setIsClick(boolean is) {
        synchronized (this) {
            mIsClick = is;
        }
    }

    /**
     * リモコンの初期表示状態を指定する
     * @param visible true:ヘッダーが表示されている false:ヘッダーが表示されていない
     */
    public void setIsFirstVisible(Boolean visible) {
        mIsFirstVisible = visible;
    }

    /**
     * @param event タッチevent
     * @return Touchイベントを下位に伝えるかどうか
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DTVTLogger.start("view_event:" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //システム時間を取得する
                mSysTime = System.currentTimeMillis();
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
                Long curTime = System.currentTimeMillis();
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
                    setHeaderContent();
                    break;
                }
                //指が離すまで子ビュ―の移動した距離は子ビューの1/4に超えた場合、
                // 自動的にTOPまで移動する
                if (mMovedY > mScrollHeight / 4 && !mIsTop) {
                    mScroller.startScroll(0, getScrollY(), 0, (mScrollHeight - getScrollY()));
                    invalidate();
                    setHeaderContent();
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
                    mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_bottom_corner, null));
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
    private void setHeaderContent() {
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
            startRemoteControl();
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
        View inflate1 = lf.inflate(R.layout.remote_controller_player_ui_layout, null);
        View inflate = lf.inflate(R.layout.remote_controller_channel_ui_layout, null);
        ViewPagerAdapter remokonAdapter = null;

        mViewList.add(inflate1);
        mViewList.add(inflate);

        mViewPager = findViewById(R.id.remocon_viewpager);
        remokonAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(remokonAdapter);
        mViewPager.addOnPageChangeListener(this);

        mGestureDetector = new GestureDetector(this.getContext(), mGestureListener);
        mViewPager.setOnTouchListener(mOnTouchListener);

        setRemoteControllerViewAction();
        if (mIsTop) {
            startRemoteControl();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }
    }

    /**
     * リモコンUIのボタン設定
     */
    private void setRemoteControllerViewAction() {
        remoteControllerSendKeyAction = new RemoteControllerSendKeyAction(this.getContext());
        remoteControllerSendKeyAction.initRemoteControllerPlayerView(this);
        remoteControllerSendKeyAction.initRemoteControllerChannelView(this);
    }

    /**
     * リモコンUI画面 onFling処理
     */
    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
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
     * リモコンUI画面のonFlingを取得する
     */
    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    };

    private OnTouchListener mParentOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            DTVTLogger.start();
            return mParentGestureDetector.onTouchEvent(event);
        }
    };

    /**
     * リモコンUI画面を閉じる処理
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
            mFrameLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.remote_watch_by_tv_bottom_corner, null));
            remoteControllerSendKeyAction.cancelTimer();
            if (null != mStartUIListener) {
                mStartUIListener.onEndRemoteControl();
            }
            setDefaultPage();
        }
    }

    /**
     * isTopのboolean値を返す
     */
    public boolean isTopRemoteControllerUI() {
        return mIsTop;
    }

    public void startRemoteUI() {
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
        setHeaderContent();
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
    private void startRemoteControl() {
        if (mStartUIListener != null && mIsTop) {
            DTVTLogger.debug("StartUIListener.onStartRemoteControl");
            mStartUIListener.onStartRemoteControl();
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