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
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class MyChannelEditActivity extends BaseActivity implements View.OnClickListener {

    private Button btnBack,btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_channel_edit_main_layout);
        initView();
    }

    private void initView() {
        btnBack = (Button)findViewById(R.id.btn_back);
        btnAdd = (Button)findViewById(R.id.btn_add);
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add:
                new AlertDialog.Builder(MyChannelEditActivity.this)
                        .setTitle("追加")
                        .setPositiveButton("",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })

                        .show();
                break;
        }
    }
}
