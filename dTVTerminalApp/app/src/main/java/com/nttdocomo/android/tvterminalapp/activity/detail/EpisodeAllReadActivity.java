/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.detail;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.utils.ContentDetailUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;

/**
 * エピソードあらすじ情報.
 */
public class EpisodeAllReadActivity extends BaseActivity {

    /** タイトル名.*/
    private String mTitle;
    /** 他サービス.*/
    private boolean mIsOtherService;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setContentView(R.layout.dtv_contents_episode_read_main_layout);
        setTheme(R.style.AppThemeBlack);
        setStatusBarColor(R.color.contents_header_background);
        initView();
    }

    /**
     * ビュー初期化表示.
     */
    private void initView() {
        setHeaderColor(false);
        enableGlobalMenuIcon(true);
        changeGlobalMenuIcon(false);
        enableHeaderBackIcon(false);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(ContentDetailUtils.EPISODE_TITLE);
        mIsOtherService = intent.getBooleanExtra(ContentDetailUtils.EPISODE_OTHERSERVICE, false);
        String message = intent.getStringExtra(ContentDetailUtils.EPISODE_MESSAGE);
        if (mTitle != null) {
            TextView episodeTitle = findViewById(R.id.contents_detail_dtv_episode_title);
            episodeTitle.setText(mTitle);
        }
        TextView episodeSynop = findViewById(R.id.contents_detail_dtv_episode_synop);
        episodeSynop.setText(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String screenName = getString(R.string.google_analytics_screen_name_dtv_read_all);
        SparseArray<String> customDimensions;
        if (mIsFromBgFlg) {
            customDimensions = ContentUtils.getParingAndLoginCustomDimensions(EpisodeAllReadActivity.this);
        } else {
            customDimensions = new SparseArray<>();
            String contentsType1;
            if (mIsOtherService) {
                customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_dtv));
                contentsType1 = getString(R.string.google_analytics_custom_dimension_contents_type1_pure_dtv);
            } else {
                customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
                contentsType1 = getString(R.string.google_analytics_custom_dimension_contents_type1_hikari_dtv);
            }
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTENTSTYPE1, contentsType1);
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTENTSTYPE2, getString(R.string.google_analytics_custom_dimension_contents_type2_void));
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTENTNAME, mTitle);
        }
        sendScreenView(screenName, customDimensions);
    }
}