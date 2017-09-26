package proj.activity.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_toDetail1;
    private Button bt_toDetail2;
    private Button bt_backTop2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_result_main_layout);
        bt_toDetail1 = (Button) findViewById(R.id.bt_result1);
        bt_toDetail2 = (Button) findViewById(R.id.bt_result2);
        bt_backTop2= (Button) findViewById(R.id.bt_backTop2);
        bt_toDetail1.setOnClickListener(this);
        bt_toDetail2.setOnClickListener(this);
        bt_backTop2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_result1:
                startActivity(ChannelDetailPlayerActivity.class, null);
                break;
            case R.id.bt_result2:
                startActivity(ChannelDetailPlayerActivity.class, null);
                break;
            case R.id.bt_backTop2:
                finish();
                break;
            default:
                break;
        }

    }
}
