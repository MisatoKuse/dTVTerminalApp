package com.nttdocomo.android.tvterminalapp.activity.Player;

import android.os.Bundle;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class RecordedVideoPlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorded_video_player_main_layout);
    }
}
