package proj.activity.TvProgram;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import proj.activity.Home.HomeActivity;
import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.activity.Player.TvPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class WeekTvProgramListActivity extends BaseActivity implements View.OnClickListener {
    private Button btnBack,btnMode,btnChannelInfo,btnTvInfo,btnVideoDialog;
    private boolean flag= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_tv_program_list_main_layout);
        initView();
    }

    private void initView() {
        btnBack = (Button)findViewById(R.id.btn_back);
        btnMode = (Button)findViewById(R.id.btn_mode);
        btnChannelInfo = (Button)findViewById(R.id.btn_channel_info);
        btnTvInfo = (Button)findViewById(R.id.btn_tv_info);
        btnVideoDialog = (Button)findViewById(R.id.btn_video_dialog);
        btnBack.setOnClickListener(this);
        btnMode.setOnClickListener(this);
        btnChannelInfo.setOnClickListener(this);
        btnTvInfo.setOnClickListener(this);
        btnVideoDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:
                startActivity(HomeActivity.class,null);
                break;
            case R.id.btn_mode:
                if (flag) {
                    btnMode.setText("週間番組表拡大版へ");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            800,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    btnMode.setLayoutParams(lp);
                    flag = false;
                }else{
                    btnMode.setText("週間番組表縮小版へ");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    btnMode.setLayoutParams(lp);
                    flag = true;
                }
                break;
            case R.id.btn_channel_info:
                startActivity(ChannelDetailPlayerActivity.class,null);
                break;
            case R.id.btn_tv_info:
                startActivity(TvPlayerActivity.class,null);
                break;
            case R.id.btn_video_dialog:
                new AlertDialog.Builder(WeekTvProgramListActivity.this)
                        .setTitle("録画よやくしますか？")
                        .setPositiveButton("録画予約します",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;


        }
    }
}