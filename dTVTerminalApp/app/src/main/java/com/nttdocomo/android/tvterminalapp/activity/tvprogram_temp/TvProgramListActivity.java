/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram_temp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nttdocomo.android.tvterminalapp.activity.home_temp.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.player_temp.ChannelDetailPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.player_temp.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

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

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder.create();
                final View view1 = View.inflate(this, R.layout.schedule_rec_dialog_layout, null);
                dialog.setView(view1,0,0,0,0);
                dialog.show();
                view1.findViewById(R.id.video_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(TvProgramListActivity.this);
                        final AlertDialog dialog = builder.create();
                        View view1 = View.inflate(TvProgramListActivity.this, R.layout.schedule_rec_dialog_layout2, null);
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
            default:
                break;
        }
    }
}