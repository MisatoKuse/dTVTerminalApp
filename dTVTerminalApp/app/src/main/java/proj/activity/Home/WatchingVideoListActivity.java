package proj.activity.Home;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import proj.activity.Player.RecordedVideoPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class WatchingVideoListActivity extends BaseActivity {

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_video_list_main_layout);
    }

    private void initView(){
        mLinearLayout = findViewById(R.id.watch_video_list_main_layout_ll1);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RecordedVideoPlayerActivity.class,null);
            }
        });
    }
}