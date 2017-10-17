package com.nttdocomo.android.tvterminalapp.activity.player;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;

public class RecommendPlayerActivity extends TvPlayerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_main_layout);
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
