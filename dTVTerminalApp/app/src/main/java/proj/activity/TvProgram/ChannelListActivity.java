package proj.activity.TvProgram;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import proj.activity.Home.HomeActivity;
import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class ChannelListActivity extends BaseActivity implements View.OnClickListener {
    private Button btnBack,btnChannelInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cannel_list_main_layout);
        initView();
    }

    private void initView() {
        btnBack = (Button)findViewById(R.id.btn_back);
        btnChannelInfo = (Button)findViewById(R.id.btn_channel_info);
        btnBack.setOnClickListener(this);
        btnChannelInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                startActivity(HomeActivity.class,null);
                break;
            case R.id.btn_channel_info:
                startActivity(ChannelDetailPlayerActivity.class,null);
                break;

        }
    }
}
