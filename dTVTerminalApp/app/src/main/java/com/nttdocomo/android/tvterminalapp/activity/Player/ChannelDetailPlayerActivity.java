/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Player;

import android.os.Bundle;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.activity.TvProgram.MyChannelEditActivity;
import com.nttdocomo.android.tvterminalapp.activity.TvProgram.WeekTvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

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
        startActivity(TvPlayerActivity.class, null);
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
        startActivity(TvPlayerActivity.class, null);
    }
}
