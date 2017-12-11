/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.DtvContentsDetailDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.view.ContentsDetailViewPager;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DtvContentsDetailActivity extends BaseActivity implements DtvContentsDetailDataProvider.ApiDataProviderCallback, RemoteControllerView.OnStartRemoteControllerUIListener {
    //tabビュー
    private LinearLayout tabLinearLayout;
    private ContentsDetailViewPager mViewPager;
    private OtherContentsDetailData mDetailData;
    private ImageView thumbnail;
    //外部ブラウザー遷移先
    private String[] mTabNames;
    private DtvContentsDetailFragmentFactory fragmentFactory = null;
    private TextView headerTextView;

    public static final String DTV_INFO_BUNDLE_KEY = "dTVInfoKey";
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    private static final int CONTENTS_DETAIL_TAB_TEXT_SIZE = 15;
    private static final int CONTENTS_DETAIL_TAB_OTHER_MARGIN = 0;
    private VodMetaFullData detailFullData;
    private DtvContentsDetailDataProvider detailDataProvider;
    private static final String DATE_FORMAT = "yyyy/MM/ddHH:mm:ss";
    private String date[] = {"日", "月", "火", "水", "木", "金", "土"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtv_contents_detail_main_layout);
        DTVTLogger.start();
        setStatusBarColor(R.color.contents_header_background);
        setNoTitle();
        initView();
        initData();
        initTab();
        // リモコンUIのリスナーを設定
        createRemoteControllerView();
        setStartRemoteControllerUIListener(this);
    }

    private void getData() {
        detailDataProvider = new DtvContentsDetailDataProvider(this);
        String[] cRid = {"1", "2"};
        detailDataProvider.getContentsDetailData(cRid, "", 1);
    }

    /**
     * インテントデータ取得
     */
    private void setIntentDetailData() {
        final Intent intent = getIntent();
        mDetailData = intent.getParcelableExtra(DTV_INFO_BUNDLE_KEY);
        if (mDetailData == null) {
            getData();
        } else {
            setTitleAndThumbnail(mDetailData.getTitle(), mDetailData.getThumb());
        }
    }

    /**
     * データの初期化
     */
    private void setTitleAndThumbnail(String title, String url) {
        if (!TextUtils.isEmpty(title)) {
            headerTextView.setText(title);
        }
        if (!TextUtils.isEmpty(url)) {
            ThumbnailProvider mThumbnailProvider = new ThumbnailProvider(this);
            thumbnail.setImageResource(R.drawable.test_image);
            thumbnail.setTag(url);
            Bitmap bitmap = mThumbnailProvider.getThumbnailImage(thumbnail, url);
            if (bitmap != null) {
                thumbnail.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * データの初期化
     */
    private void initData() {
        mTabNames = getResources().getStringArray(R.array.other_contents_detail_tabs);
        fragmentFactory = new DtvContentsDetailFragmentFactory();
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
        setIntentDetailData();
    }

    /**
     * Viewの初期化
     */
    private void initView() {
        boolean isPlayer = false;
        headerTextView = findViewById(R.id.contents_detail_header_layout_title);
        //サムネイル取得
        tabLinearLayout = findViewById(R.id.dtv_contents_detail_main_layout_tab);
        thumbnail = findViewById(R.id.dtv_contents_detail_main_layout_thumbnail);
        if (isPlayer) {
            LinearLayout parentLayout = findViewById(R.id.dtv_contents_detail_main_layout_ll);
            LinearLayout scrollLayout = findViewById(R.id.contents_detail_scroll_layout);
            RelativeLayout thumbnailRelativeLayout = findViewById(R.id.dtv_contents_detail_layout);
            scrollLayout.removeViewAt(0);
            parentLayout.addView(thumbnailRelativeLayout, 1);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getWidthDensity(),
                getWidthDensity() * 9 / 16);
        thumbnail.setLayoutParams(layoutParams);
        mViewPager = findViewById(R.id.dtv_contents_detail_main_layout_vp);
    }

    /**
     * tabのレイアウトを設定
     */
    private void initTab() {
        for (int i = 0; mTabNames.length > i; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins((int) getResources().getDimension(R.dimen.contents_tab_side_margin),
                    CONTENTS_DETAIL_TAB_OTHER_MARGIN, CONTENTS_DETAIL_TAB_OTHER_MARGIN, CONTENTS_DETAIL_TAB_OTHER_MARGIN);
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(CONTENTS_DETAIL_TAB_TEXT_SIZE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.rectangele);
                tabTextView.setTextColor(Color.GRAY);
            } else {
                tabTextView.setTextColor(Color.WHITE);
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
            tabLinearLayout.addView(tabTextView);
        }
    }

    /**
     * インジケータ設置
     *
     * @param position タブポジション
     */
    private void setTab(int position) {
        if (tabLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) tabLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.rectangele);
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setBackgroundResource(0);
                    mTextView.setTextColor(Color.WHITE);
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
        createRemoteControllerView();
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
            Fragment fragment = fragmentFactory.createFragment(position);
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

    @Override
    public void onContentsDetailInfoCallback(ArrayList<VodMetaFullData> contentsDetailInfo) {
        if (contentsDetailInfo != null) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            detailFullData = contentsDetailInfo.get(0);
            detailFragment.mOtherContentsDetailData.setTitle(detailFullData.getTitle());
            detailFragment.mOtherContentsDetailData.setDetail(detailFullData.getSynop());
            String[] credit_array = detailFullData.getmCredit_array();
            setTitleAndThumbnail(detailFullData.getTitle(), detailFullData.getmDtv_thumb_448_252());
            if (credit_array != null && credit_array.length > 0) {
                detailDataProvider.getRoleListData();
            }
            if (!TextUtils.isEmpty(detailFullData.getmService_id())) {
                detailDataProvider.getChannelList(1, 1, "", 1);
            }
            detailFragment.noticeRefresh();
        }
    }

    @Override
    public void onRoleListCallback(ArrayList<RoleListMetaData> roleListInfo) {
        if (roleListInfo != null) {
            DtvContentsDetailFragment detailFragment = getDetailFragment();
            if (detailFullData != null) {
                List<String> staffList = new ArrayList<>();
                String[] credit_array = detailFullData.getmCredit_array();
                StringBuilder ids = new StringBuilder();
                for (int i = 0; i < credit_array.length; i++) {
                    String[] creditInfo = credit_array[i].split("\\|");
                    if (creditInfo.length == 4) {
                        String creditId = creditInfo[2];
                        String creditName = creditInfo[3];
                        if (!TextUtils.isEmpty(creditId)) {
                            for (int j = 0; j < roleListInfo.size(); j++) {
                                RoleListMetaData roleListMetaData = roleListInfo.get(j);
                                if (creditId.equals(roleListMetaData.getId())) {
                                    if (!ids.toString().contains(creditId + ",")) {
                                        ids.append(creditId);
                                        ids.append(",");
                                        staffList.add(roleListMetaData.getName());
                                        staffList.add(creditName);
                                    } else {
                                        String oldData[] = ids.toString().split(",");
                                        for (int k = 0; k < oldData.length; k++) {
                                            if (creditId.equals(oldData[k])) {
                                                staffList.set(k * 2 + 1, staffList.get(k * 2 + 1) + "," + creditName);
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                detailFragment.mOtherContentsDetailData.setStaffList(staffList);
                if (staffList.size() > 0) {
                    detailFragment.refreshStaff();
                }
            }
        }
    }

    @Override
    public void channelListCallback(ArrayList<Channel> channels) {
        if (channels != null) {
            if (!TextUtils.isEmpty(detailFullData.getmService_id())) {
                DtvContentsDetailFragment detailFragment = getDetailFragment();
                for (int i = 0; i < channels.size(); i++) {
                    Channel channel = channels.get(i);
                    if (detailFullData.getmService_id().equals(channel.getServiceId())) {
                        String channelName = channel.getTitle();
                        String channelStartDate = channel.getStartDate();
                        String channelEndDate = channel.getEndDate();
                        if (!TextUtils.isEmpty(channelStartDate)) {
                            channelStartDate = channelStartDate.replaceAll("-", "/");
                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
                            StringBuilder startBuilder = new StringBuilder();
                            startBuilder.append(channelStartDate.substring(0, 10));
                            startBuilder.append(channelStartDate.substring(11, 19));
                            try {
                                Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                                calendar.setTime(sdf.parse(startBuilder.toString()));
                                StringUtil util = new StringUtil(this);
                                String[] strings = {String.valueOf(calendar.get(Calendar.MONTH)), "/",
                                        String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), " (",
                                        date[calendar.get(Calendar.DAY_OF_WEEK) - 1], ") ",
                                        channelStartDate.substring(11, 16), " - ",
                                        channelEndDate.substring(11, 16)};
                                String channelDate = util.getConnectString(strings);
                                detailFragment.mOtherContentsDetailData.setChannelDate(channelDate);
                                detailFragment.mOtherContentsDetailData.setChannelName(channelName);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                }
                detailFragment.refreshChannelInfo();
            }

        }
    }

    private DtvContentsDetailFragment getDetailFragment() {
        Fragment currentFragment = fragmentFactory.createFragment(0);
        DtvContentsDetailFragment detailFragment = (DtvContentsDetailFragment) currentFragment;
        return detailFragment;
    }

    @Override
    public void onStartRemoteControl() {
        DTVTLogger.start();
        // サービスIDにより起動するアプリを変更する
        switch (mDetailData.getServiceId()) {
            case DTV_CONTENTS_SERVICE_ID: // dTV
                requestStartApplication(
                        RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mDetailData.getContentId());
                break;
            case D_ANIMATION_CONTENTS_SERVICE_ID: // dアニメ
                requestStartApplication(
                        RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mDetailData.getContentId());
                break;
            case DTV_CHANNEL_CONTENTS_SERVICE_ID: // dチャンネル
                requestStartApplication(
                        RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL, mDetailData.getContentId());
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }
}
