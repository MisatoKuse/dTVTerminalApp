package proj.activity.Home;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.activity.Player.RecordedVideoPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class RecommendActivity extends BaseActivity {

    private RelativeLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_main_layout);
        initView();
    }

    private void initView(){
        mLinearLayout = findViewById(R.id.recommend_main_layout_ll1);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ChannelDetailPlayerActivity.class,null);
            }
        });
    }
}
