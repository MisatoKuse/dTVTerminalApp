/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.other.RemoteControlActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.view.ContentsDetailViewPager;

public class DtvContentsDetailActivity extends BaseActivity {
    private String[] mTabNames;
    private DtvContentsDetailFragmentFactory mContentsDetailFragmentFactory = null;
    private HorizontalScrollView mTabScrollView;
    private LinearLayout mLinearLayout;
    private ContentsDetailViewPager mViewPager;
    private OtherContentsDetailData mDetailData;

    public static final String DTV_INFO_BUNDLE_KEY = "dTVInfoKey";
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    private static final int CONTENTS_DETAIL_TAB_TEXT_SIZE = 15;
    private static final int CONTENTS_DETAIL_TAB_LEFT_MARGIN = 30;
    private static final int CONTENTS_DETAIL_TAB_OTHER_MARGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtv_contents_detail_main_layout);
        ImageView mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        final Intent intent = getIntent();
        mDetailData = intent.getParcelableExtra(DTV_INFO_BUNDLE_KEY);
        DTVTLogger.start();
        setStatusBarColor(R.color.contents_header_background);
        setNoTitle();
        initData();
        initView();
    }

    /**
     * データの初期化
     */
    private void initData() {
        final Resources resources = getResources();
        mTabNames = resources.getStringArray(R.array.other_contents_detail_tabs);
        mContentsDetailFragmentFactory = new DtvContentsDetailFragmentFactory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mTabNames.length; ++i) { // タブの数だけ処理を行う
            DtvContentsDetailBaseFragment b = mContentsDetailFragmentFactory.createFragment(i);
            if (null != b) {
                b.mContentsDetailData.clear();
            }
        }
    }

    /**
     * Viewの初期化
     */
    private void initView() {
        TextView headerTitle = findViewById(R.id.header_layout_title);
        TextView txtContentsText = findViewById(R.id.view_contents_button_text);

        //「〇〇で視聴」を生成
        StringUtil stringUtil = new StringUtil(this);
        String strServiceName = stringUtil.getContentsServiceName(mDetailData.getServiceId());
        String strings[] = {strServiceName, getString(R.string.contents_viewing_text)};

        //サムネイル取得
        ImageView imageView = findViewById(R.id.dtv_contents_detail_thumbnail);
        ViewHolder holder = new ViewHolder();
        holder.wl_thumbnail = findViewById(R.id.dtv_contents_detail_thumbnail);
        ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(this);
        Bitmap bp = mThumbnailProvider.getThumbnailImage(holder.wl_thumbnail, mDetailData.getThumb());
        imageView.setImageBitmap(bp);

        txtContentsText.setText(stringUtil.getConnectString(strings));
        headerTitle.setText(mDetailData.getTitle());
        mTabScrollView = findViewById(R.id.contents_detail_tab_strip_scroll);

        mViewPager = findViewById(R.id.contents_detail_result);
        ContentsDetailPagerAdapter contentsDetailPagerAdapter
                = new ContentsDetailPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentsDetailPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                setTab(position);
            }
        });
        initTabData();
    }

    /**
     * tabのレイアウトを設定
     */
    private void initTabData() {
        mTabScrollView.removeAllViews();
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setBackgroundColor(Color.BLACK);
        mLinearLayout.setGravity(Gravity.START);
        mTabScrollView.addView(mLinearLayout);

        for (int i = 0; mTabNames.length > i; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins(CONTENTS_DETAIL_TAB_LEFT_MARGIN, CONTENTS_DETAIL_TAB_OTHER_MARGIN,
                        CONTENTS_DETAIL_TAB_OTHER_MARGIN, CONTENTS_DETAIL_TAB_OTHER_MARGIN);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(CONTENTS_DETAIL_TAB_TEXT_SIZE);
            tabTextView.setBackgroundColor(Color.BLACK);
            if (mTabNames.length <= 1 && i == 0) {
                tabTextView.setTextColor(Color.GRAY);
            } else {
                tabTextView.setTextColor(Color.WHITE);
            }
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            if (i == 0 && mTabNames.length <= 1) {
                tabTextView.setBackgroundResource(R.drawable.disable_indicating);
            } else if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.indicating);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // タップによるタブ切り替え
                    int position = (int) view.getTag();
                    mViewPager.setCurrentItem(position);
                    setTab(position);
                }
            });
            mLinearLayout.addView(tabTextView);
        }
    }

    /**
     * インジケータ設置
     *
     * @param position タブポジション
     */
    private void setTab(int position) {
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.indicating);
                } else {
                    mTextView.setBackgroundResource(R.drawable.indicating_no);
                }
            }
        }
    }

    /**
     * リモコン画面への遷移
     *
     * @param view
     */
    public void remoteControlButton(View view) {
        startActivity(RemoteControlActivity.class, null);
    }

    /**
     * コンテンツ詳細用ページャアダプター
     */
    private class ContentsDetailPagerAdapter extends FragmentStatePagerAdapter {
        ContentsDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DtvContentsDetailBaseFragment fragment =
                    mContentsDetailFragmentFactory.createFragment(position);
            //Fragmentへデータを渡す
            Bundle args = new Bundle();
            args.putParcelable(DTV_INFO_BUNDLE_KEY, mDetailData);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }

    private static class ViewHolder {
        ImageView wl_thumbnail;
    }
}
