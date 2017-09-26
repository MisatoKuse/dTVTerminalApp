package proj.activity.TvProgram;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import proj.activity.Home.HomeActivity;
import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.activity.Player.TvPlayerActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class TvProgramListActivity extends BaseActivity implements View.OnClickListener {
    private Button channelInfo,btnBack;
    private Button logoButton,videoButton,btnMode;
    private boolean flag= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tv_program_list_main_layout);
        initView();
    }

    private void initView() {

        channelInfo = (Button)findViewById(R.id.btn_channel_info);
        btnBack = (Button)findViewById(R.id.btn_back);
        logoButton = (Button)findViewById(R.id.btn_tv_info);
        videoButton = (Button)findViewById(R.id.btn_video_dialog) ;
        btnMode = (Button)findViewById(R.id.btn_mode);
        btnBack.setOnClickListener(this);
        channelInfo.setOnClickListener(this);
        logoButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        btnMode.setOnClickListener(this);

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
            case R.id.btn_tv_info:
                startActivity(TvPlayerActivity.class,null);
                break;
            case R.id.btn_video_dialog:
                new AlertDialog.Builder(TvProgramListActivity.this)
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
            case R.id.btn_mode:
                if (flag) {
                    btnMode.setText("番組表拡大版へ");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            800,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    btnMode.setLayoutParams(lp);
                    flag = false;
                }else{
                    btnMode.setText("番組表縮小版へ");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    btnMode.setLayoutParams(lp);
                    flag = true;
                }
                break;

        }
    }
}