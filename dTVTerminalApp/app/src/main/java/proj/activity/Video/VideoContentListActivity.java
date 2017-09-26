package proj.activity.Video;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.activity.Player.RecordedVideoPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class VideoContentListActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_content_main_layout);
        initView();
    }

    private void initView(){
        mRelativeLayout = findViewById(R.id.video_content_main_layout_ll1);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ChannelDetailPlayerActivity.class,null);
            }
        });
    }
}
