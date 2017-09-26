package proj.activity.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_backTop2;
    private FloatingActionButton fab_sort;
    private RelativeLayout mrelativeLayout1;
    private RelativeLayout mrelativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_result_main_layout);
        bt_backTop2 = findViewById(R.id.bt_backTop2);
        fab_sort = findViewById(R.id.fab);
        mrelativeLayout1 = findViewById(R.id.Search_result1);
        mrelativeLayout2 = findViewById(R.id.Search_result2);
        bt_backTop2.setOnClickListener(this);
        fab_sort.setOnClickListener(this);
        mrelativeLayout1.setOnClickListener(this);
        mrelativeLayout2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Search_result1:
                startActivity(ChannelDetailPlayerActivity.class, null);
                break;
            case R.id.Search_result2:
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
