package proj.activity.Player;

import android.os.Bundle;
import android.view.View;

import proj.activity.Other.RemoteControlActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class TvPlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_main_layout);
    }

    /**
     * リモコン画面への遷移
     *
     * @param view
     */
    public void remoteControlButton(View view) {
        startActivity(RemoteControlActivity.class, null);
    }

    /**
     * おすすめへの遷移
     *
     * @param view
     */
    public void recommendButton(View view) {
        startActivity(RecommendPlayerActivity.class, null);
    }
}
