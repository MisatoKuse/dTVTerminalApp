

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.model.remotecontroller.RemoteControllerSendKeyAction;

import java.util.ArrayList;
import java.util.List;


public class RemoteControllerView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private int downY, moveY, movedY;
    private View mChild;
    private Scroller mScroller;
    private int mScrollHeight;
    private boolean isTop = false;
    private float mVisibilityHeight;
    private ArrayList<View> mDots;
    private List<View> viewList = new ArrayList<View>();
    ViewPager mViewPager;
    private ViewPagerAdapter mRemokonAdapter;
    private int mCurrentItem = 0;
    private int mOldPosition;
    private TextView mTextView;
    private FrameLayout mFragmentLayout;
    private RemoteControllerSendKeyAction remoteControllerSendKeyAction;
    private ImageButton mPowerButton;
    private GestureDetector mGestureDetector = null;


    public RemoteControllerView(Context context) {
        this(context, null);
    }

    public RemoteControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RecycleListView);
        mVisibilityHeight = ta.getDimension(R.styleable.RemoteControllerView_visibility_height, 100);
        ta.recycle();

        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mScrollHeight = (int) (mChild.getMeasuredHeight() - mVisibilityHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mChild.layout(0, mScrollHeight, mChild.getMeasuredWidth(), mChild.getMeasuredHeight() + mScrollHeight);
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


    private boolean mIsClick = false;

    private void setIsClick(boolean is) {
        synchronized (this) {
            mIsClick = is;
        }
    }

    private long mSysTime;
    private final long CLICK_MAX_TIME = 100;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSysTime = System.currentTimeMillis();

                downY = (int) event.getY();
                if (downY < mScrollHeight - mVisibilityHeight && !isTop) {
                    return false;
                }
                if (viewList.size() == 0) {
                    setPager();
                }
                return true;

            case MotionEvent.ACTION_MOVE:

                moveY = (int) event.getY();
                int deY = downY - moveY;
                if (deY > 0) {
                    movedY += deY;
                    if (movedY > mScrollHeight) {
                        movedY = mScrollHeight;
                    }
                    if (movedY < mScrollHeight) {
                        scrollBy(0, deY);
                        downY = moveY;
                        return true;
                    }
                }
                if (deY < 0) {
                    movedY += deY;
                    if (movedY < 0) {
                        movedY = 0;
                    }
                    if (movedY > 0) {
                        scrollBy(0, deY);
                    }
                    downY = moveY;
                    return true;

                }

                break;
            case MotionEvent.ACTION_UP:
                Long curTime = System.currentTimeMillis();
                Long diff = curTime - mSysTime;
                if (diff < CLICK_MAX_TIME) {
                    setIsClick(true);
                } else {
                    setIsClick(false);
                }
                if (downY > mScrollHeight - mVisibilityHeight && mIsClick && !isTop) {
                    mScroller.startScroll(0, -getScrollY(), 0, mScrollHeight);
                    postInvalidate();
                    movedY = mScrollHeight;
                    isTop = true;
                    mPowerButton = (ImageButton) findViewById(R.id.remote_controller_iv_power);
                    mPowerButton.setVisibility(VISIBLE);
                    mFragmentLayout = findViewById(R.id.header_watch_by_tv);
                    mFragmentLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.color_little_gray, null));
                    mTextView = findViewById(R.id.watch_by_tv);
                    mTextView.setVisibility(INVISIBLE);
                    break;
                }
                if (movedY > mScrollHeight / 4 && !isTop) {
                    mScroller.startScroll(0, getScrollY(), 0, (mScrollHeight - getScrollY()));
                    invalidate();
                    movedY = mScrollHeight;
                    isTop = true;
                    mPowerButton = (ImageButton) findViewById(R.id.remote_controller_iv_power);
                    mPowerButton.setVisibility(VISIBLE);
                    mFragmentLayout = findViewById(R.id.header_watch_by_tv);
                    mFragmentLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.color_little_gray, null));
                    mTextView = findViewById(R.id.watch_by_tv);
                    mTextView.setVisibility(INVISIBLE);
                } else {
                    closeRemoteControllerUI();
                }
                break;
        }
        return super.onTouchEvent(event);
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
        mDots = new ArrayList<View>();

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
            float flingY = e2.getY() - e1.getY();
            if (flingY > mScrollHeight / 4 && isTop) {
                DTVTLogger.debug("Down");
                closeRemoteControllerUI();
                DTVTLogger.end();
                return true;
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
    private void closeRemoteControllerUI(){
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
        postInvalidate();
        movedY = 0;
        isTop = false;
        mPowerButton = (ImageButton) findViewById(R.id.remote_controller_iv_power);
        mPowerButton.setVisibility(INVISIBLE);
        mFragmentLayout = findViewById(R.id.header_watch_by_tv);
        mFragmentLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.color_blue, null));
        mTextView = findViewById(R.id.watch_by_tv);
        mTextView.setVisibility(VISIBLE);
        remoteControllerSendKeyAction.cancelTimer();
    }
}

