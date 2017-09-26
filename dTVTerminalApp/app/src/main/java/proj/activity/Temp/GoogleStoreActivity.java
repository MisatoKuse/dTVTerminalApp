package proj.activity.Temp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import proj.activity.Launch.DAccountRegConfirmationActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class GoogleStoreActivity extends BaseActivity implements View.OnClickListener {

    private Button mDownloadOkGoogleStore=null;
    private String mInfo= "dアカウントダウンロード済み";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_store);

        setContents();
    }

    private void setContents() {
        mDownloadOkGoogleStore= (Button)findViewById(R.id.downloadOkGoogleStore);
        mDownloadOkGoogleStore.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.equals(mDownloadOkGoogleStore)){
            if(mInfo.equals(mDownloadOkGoogleStore.getText())){
                startActivity(DAccountRegConfirmationActivity.class, null);
            }

            mDownloadOkGoogleStore.setText(mInfo);
            DAccountRegConfirmationActivity.installDAccountAppli();
        }
    }
}
