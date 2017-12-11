

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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

    private int mDownY;
    private int mMoveY;
    private int mMovedY;
    private View mChild;
    private Scroller mScroller;
    private int mScrollHeight;
    private boolean mIsTop = false;
    private float mVisibilityHeight;
    private List<View> viewList = new ArrayList<View>();
    ViewPager mViewPager;
    private ViewPagerAdapter mRemokonAdapter;
    private FrameLayout mFrameLayout;
    private RemoteControllerSendKeyAction remoteControllerSendKeyAction;
    private GestureDetector mGestureDetector = null;
    private long mSysTime;//システムTime
    private final long CLICK_MAX_TIME = 100;
    private boolean mIsClick = false;//クリックなのかスワイプなのか
    private LinearLayout mBottomLinearLayout, mTopLinearLayout;
    private OnStartRemoteControllerUIListener mStartUIListener= null;

    /**
     * このViewがtopに表示された際に通知するリスナー
     */
    public interface OnStartRemoteControllerUIListener {
        void onStartRemoteControl();
    }

    public RemoteControllerView(Context context) {
        this(context, null);
    }

    public RemoteControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RecycleListView);
        //TODO
        if (context instanceof DtvContentsDetailActivity) {

            mVisibilityHeight = ta.getDimension(R.styleable.RemoteControllerView_visibility_height,
                    getResources().getDimension(R.dimen.remote_controller_header_view_high));
        } else {
            mVisibilityHeight = ta.getDimension(R.styleable.RemoteControllerView_visibility_height,
                    0);
        }
        ta.recycle();
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //スクロールできる最大の距離
        mScrollHeight = (int) (mChild.getMeasuredHeight() - mVisibilityHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //リモコンの表示領域を設定する
        mChild.layout((getMeasuredWidth()-mChild.getMeasuredWidth()) / 2, mScrollHeight, getMeasuredWidth(), mChild.getMeasuredHeight() + mScrollHeight);
    }

    /**
     * viewの初期化
     *
     * @param context
     */
    public void init(Context context) {
        mScroller = new Scroller(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChild = getChildAt(0);
    }

    private void setIsClick(boolean is) {
        synchronized (this) {
            mIsClick = is;
        }
    }

    /**
     * @param event タッチevent
     * @return
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
                if (mDownY < mScrollHeight - mVisibilityHeight && !mIsTop) {
                    return false;
                }
                //viewPagerを初期化する
                if (viewList.size() == 0) {
                    setPager();
                }
                return true;

            case MotionEvent.ACTION_MOVE:

                mMoveY = (int) event.getY();
                //mDeY:移動した距離
                //下に移動の場合,mDeY < 0；上に移動の場合,mDeY > 0;
                int deY = mDownY - mMoveY;
                //上に移動するときの処理
                if (deY > 0) {
                    //移動した距離を加算する,移動できる範囲内に制限する
                    mMovedY += deY;
                    if (mMovedY > mScrollHeight) {
                        mMovedY = mScrollHeight;
                    }
                    if (mMovedY < mScrollHeight) {
                        scrollBy(0, deY);
                        mDownY = mMoveY;
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
                    mDownY = mMoveY;
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
                    mScroller.startScroll(0, -getScrollY(), 0, mScrollHeight);
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
                    if(mIsTop) {
                        closeRemoteControllerUI();
                        mIsTop = false;
                    }
                }
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
        startRemoteControl();
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

        viewList.add(inflate1);
        viewList.add(inflate);

        mViewPager = (ViewPager) findViewById(R.id.remokon_viewpager);
        mRemokonAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(mRemokonAdapter);
        mViewPager.addOnPageChangeListener(this);

        mGestureDetector = new GestureDetector(this.getContext(), mGestureListener);
        mViewPager.setOnTouchListener(mOnTouchListener);

        setRemoteControllerViewAction();
        startRemoteControl();
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
            return viewList.size();
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
            ((ViewPager) container).addView(viewList.get(position));
            return viewList.get(position);
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

    /**
     * リモコンUI画面を閉じる処理
     */
    public void closeRemoteControllerUI() {
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
    }

    /**
     * isTopのboolean値を返す
     */
    public boolean isTopRemoteControllerUI() {
        return mIsTop;
    }

    public void startRemoteUI() {
        //viewPagerを初期化する
        if (viewList.size() == 0) {
            setPager();
        }
        mScroller.startScroll(0, 0, 0, (int) (mScrollHeight + mVisibilityHeight));
        invalidate();
        setHeaderContent();
    }

    /**
     * リスナーを設定
     * @param listener
     */
    public void setOnStartRemoteControllerUI(OnStartRemoteControllerUIListener listener) {
        DTVTLogger.debug("Set StartRemoteControllerUIListener");
        mStartUIListener = listener;
    }

    /**
     * リスナーが設定されている場合、通知処理を実行
     */
    private void startRemoteControl() {
        if(mStartUIListener != null) {
            DTVTLogger.debug("StartUIListener.onStartRemoteControl");
            mStartUIListener.onStartRemoteControl();
        }
    }

    /**
     * dアプリ起動リクエスト処理を呼び出し
     * @param type
     * @param contentsId
     */
    public void sendStartApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES type, String contentsId) {
        DTVTLogger.start();
        remoteControllerSendKeyAction.getRelayClient().startApplicationRequest(type,contentsId);
        DTVTLogger.end();
    }
}
