package com.nttdocomo.android.tvterminalapp.activity.Player;

import android.os.Bundle;

/**
 * Created by hitue-fsi on 2017/09/22.
 */


import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;


public class VideoPlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_main_layout);
    }
}
