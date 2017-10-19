/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class VideoRankingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_ranking_main_layout);
    }

    /**
     * コンテンツ詳細への遷移
     *
     * @param view
     */
    public void contentsDetailButton(View view) {
        startActivity(TvPlayerActivity.class, null);
    }

    /**
     * クリップボタン
     *
     * @param view
     */
    public void clipButton(View view) {
        Toast.makeText(this, "クリップしました", Toast.LENGTH_SHORT).show();
    }
}
