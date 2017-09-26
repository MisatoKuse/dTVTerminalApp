package proj.activity.Ranking;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import proj.activity.Player.TvPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class DailyTvRankingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
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
