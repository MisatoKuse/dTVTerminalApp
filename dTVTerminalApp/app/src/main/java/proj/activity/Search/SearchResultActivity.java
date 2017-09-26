package proj.activity.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private FloatingActionButton fab_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_result_main_layout);
        bt_toDetail1 = (Button) findViewById(R.id.bt_result1);
        bt_toDetail2 = (Button) findViewById(R.id.bt_result2);
        bt_backTop2 = (Button) findViewById(R.id.bt_backTop2);
        fab_sort = (FloatingActionButton) findViewById(R.id.fab);
        bt_toDetail1.setOnClickListener(this);
        bt_toDetail2.setOnClickListener(this);
        bt_backTop2.setOnClickListener(this);
        fab_sort.setOnClickListener(this);
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
            case R.id.fab:
                Toast.makeText(this, "人気順で並び替えました", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_backTop2:
                finish();
                break;
            default:
                break;
        }

    }
}
