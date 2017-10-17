package com.nttdocomo.android.tvterminalapp.activity.Ranking;

import android.os.Bundle;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.activity.Player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class RankingTopActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_top_main_layout);
    }

    /**
     * コンテンツ詳細への遷移
     */
    public void contentsDetailButton(View view) {
        startActivity(TvPlayerActivity.class, null);
    }

    /**
     * 今日のテレビランキングへの遷移
     */
    public void dailyRankingButton(View view) {
        startActivity(DailyTvRankingActivity.class, null);
    }

    /**
     * 週刊テレビランキングへの遷移
     */
    public void weeklyTvRankingButton(View view) {
        startActivity(WeeklyTvRankingActivity.class, null);
    }

    /**
     * ビデオランキングへの遷移
     */
    public void videoRankingButton(View view) {
        startActivity(VideoRankingActivity.class, null);
    }
}
