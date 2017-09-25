package proj.activity.Player;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import proj.dtvterminalapp.R;

public class RecommendPlayerActivity extends ChannelDetailPlayerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_detail_player_main_layout);
        //launchModeにsingleTaskを設定しているため、画面の処理はonResumeに記載すること
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "おすすめ画面", Toast.LENGTH_SHORT).show();
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
