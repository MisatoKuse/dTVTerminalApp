package proj.activity.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class SearchTopActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_backHome;
    private RelativeLayout mrelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_top_main_layout);
        bt_backHome = (Button) findViewById(R.id.bt_backHome);
        mrelativeLayout = findViewById(R.id.Search_result);
        bt_backHome.setOnClickListener(this);
        mrelativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Search_result:
                startActivity(ChannelDetailPlayerActivity.class, null);
                break;
            case R.id.bt_backHome:
                finish();
                break;
            default:
                break;
        }
    }
}


