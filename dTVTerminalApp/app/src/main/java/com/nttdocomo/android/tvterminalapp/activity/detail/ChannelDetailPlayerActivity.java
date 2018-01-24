/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.activity.tvprogram.MyChannelEditActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.WeekTvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;

public class ChannelDetailPlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_detail_player_main_layout);
    }

    /**
     * 選局ボタン
     *
     * @param view
     */
    public void selectChannelButton(View view) {
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        startActivity(intent);
    }

    /**
     * 週間番組表
     *
     * @param view
     */
    public void weeklyTvGuideButton(View view) {
        startActivity(WeekTvProgramListActivity.class, null);
    }

    /**
     * 番組表設定
     *
     * @param view
     */
    public void tvGuideSetting(View view) {
        startActivity(MyChannelEditActivity.class, null);
    }

    /**
     * 放送プログラム
     *
     * @param view
     */
    public void tvProgramButton(View view) {
        Intent intent = new Intent(this, ContentDetailActivity.class);
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, getComponentName().getClassName());
        startActivity(intent);
    }
}
