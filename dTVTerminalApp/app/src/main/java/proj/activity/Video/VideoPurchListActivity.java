package proj.activity.Video;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.activity.Player.TvPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

public class VideoPurchListActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_purch_list_main_layout);
        initView();
    }

    private void initView(){
        mRelativeLayout = findViewById(R.id.purch_video_list_main_layout_ll1);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TvPlayerActivity.class,null);
            }
        });
    }
}
