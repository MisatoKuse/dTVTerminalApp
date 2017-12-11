/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.ChannelDetailPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

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
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getStbStatus()) {
                    createRemoteControllerView();
                    getRemoteControllerView().startRemoteUI();
                }
            }
        });
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

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder.create();
                View view1 = View.inflate(this, R.layout.schedule_rec_dialog_layout, null);
                dialog.setView(view1,0,0,0,0);
                dialog.show();
                view1.findViewById(R.id.video_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WeekTvProgramListActivity.this);
                        final AlertDialog dialog = builder.create();
                        View view1 = View.inflate(WeekTvProgramListActivity.this, R.layout.schedule_rec_dialog_layout2, null);
                        dialog.setView(view1,0,0,0,0);
                        dialog.show();
                        view1.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                               dialog.dismiss();
                            }
                        });
                    }
                });
                view1.findViewById(R.id.video_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }

}