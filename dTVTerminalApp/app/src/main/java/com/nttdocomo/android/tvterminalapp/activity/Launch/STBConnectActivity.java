/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Launch;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimerTask;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.Home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.BaseActivity;


public class STBConnectActivity extends BaseActivity {

    private Button mInfoSTBConnectActivity=null;
    private boolean isStbConnected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_connect_main_layout);

        setContents();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_stb_connect_title);
    }

    private void setContents() {
        TextView title= (TextView)findViewById(R.id.titleSTBConnectActivity);
        title.setText(getScreenTitle());


        mInfoSTBConnectActivity= (Button)findViewById(R.id.infoSTBConnectActivity);

        handler.postDelayed(runnable, 2500);
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                if(!isStbConnected) {
                    mInfoSTBConnectActivity.setText("STBとペアリングしました。\n(ホーム画面に自動遷移)");
                    isStbConnected=true;
                    handler.postDelayed(this, 2500);
                } else {
                    handler.removeCallbacks(runnable);
                    Bundle b=new Bundle();
                    b.putString("state", LaunchActivity.mStateToHomePairingOk);
                    startActivity(HomeActivity.class, b);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
}
