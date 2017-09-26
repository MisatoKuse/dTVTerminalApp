package proj.activity.Launch;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import proj.activity.Home.HomeActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

/**
 * Created by ryuhan on 2017/09/25.
 */

public class STBParingInvitationActivity extends BaseActivity implements View.OnClickListener {

    private static boolean mIsFirstDisplay=true;

    private Button mUseWithoutPairingSTBParingInvitationActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_paring_main_layout);

        setContents();
    }

    private void setContents() {

        /**
         * ペアリング勧誘
         * ※一度表示されたら以降表示されない
         */
        if(!mIsFirstDisplay){
            Bundle b= new Bundle();
            b.putString("state", LaunchActivity.mStateToHomePairingNg);
            startActivity(HomeActivity.class, b);
            return;
        }

        TextView title= (TextView)findViewById(R.id.button0StbParingInvitationActivity);
        title.setText(getScreenTitle());

        mUseWithoutPairingSTBParingInvitationActivity=(Button)findViewById(R.id.useWithoutPairingSTBParingInvitationActivity);
        mUseWithoutPairingSTBParingInvitationActivity.setOnClickListener(this);

        mIsFirstDisplay=false;
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_stb_pair_invitation_title);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mUseWithoutPairingSTBParingInvitationActivity)) {
            onUseWithoutPairingButton();
        }
    }

    private void onUseWithoutPairingButton() {
        Bundle b=new Bundle();
        b.putString("state", LaunchActivity.mStateToHomePairingNg);
        startActivity(HomeActivity.class, b);
    }
}
