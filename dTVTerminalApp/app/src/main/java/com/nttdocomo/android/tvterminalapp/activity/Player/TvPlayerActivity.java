package com.nttdocomo.android.tvterminalapp.activity.Player;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.activity.Other.RemoteControlActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

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

    public void channelButton(View view){
        startActivity(ChannelDetailPlayerActivity.class, null);
    }

    /**
     * 録画予約ボタン
     *
     * @param view
     */
    public void scheduleRecButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view1 = View.inflate(this, R.layout.schedule_rec_dialog_layout, null);
        dialog.setView(view1,0,0,0,0);
        dialog.show();
        view1.findViewById(R.id.video_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TvPlayerActivity.this);
                final AlertDialog dialog = builder.create();
                View view1 = View.inflate(TvPlayerActivity.this, R.layout.schedule_rec_dialog_layout2, null);
                dialog.setView(view1,0,0,0,0);
                dialog.show();
            }
        });
        view1.findViewById(R.id.video_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}
