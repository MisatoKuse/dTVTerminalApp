package proj.activity.Ranking;

import android.os.Bundle;
import android.view.View;

import proj.activity.Player.TvPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

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
}
