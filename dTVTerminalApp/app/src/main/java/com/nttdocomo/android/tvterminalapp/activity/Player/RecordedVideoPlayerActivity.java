/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Player;

import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class RecordedVideoPlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorded_video_player_main_layout);
    }
}
