package proj.activity.Temp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import proj.activity.Launch.DAccountRegConfirmationActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class DAccountAppliActivity extends BaseActivity implements View.OnClickListener {

    //private static boolean mIsLoginOk = false;

    private final String mInfo= "dアカウントログイン済み";
    private Button mLoginDAccountAppliAcivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daccount_appli);

        setContents();
    }

    private void setContents() {

        mLoginDAccountAppliAcivity=(Button)findViewById(R.id.loginDAccountAppliAcivity);

        /*
        if(mIsLoginOk){
            mLoginDAccountAppliAcivity.setText("dアカウントログイン済み");
            return;
        }
        */

        mLoginDAccountAppliAcivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mLoginDAccountAppliAcivity)){
            //mIsLoginOk=true;
            if(mInfo.equals(mLoginDAccountAppliAcivity.getText())){
                DAccountRegConfirmationActivity.dAccountLogin();
                startActivity(DAccountRegConfirmationActivity.class, null);
                return;
            }
            mLoginDAccountAppliAcivity.setText(mInfo);
        }
    }

    /*
    public boolean getDAccountLoginState(){
        return mIsLoginOk;
    }
    */
}
