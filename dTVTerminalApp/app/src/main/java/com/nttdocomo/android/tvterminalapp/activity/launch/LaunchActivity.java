/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListListener;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * アプリ起動時に最初に呼び出されるActivity.
 */
public class LaunchActivity extends BaseActivity implements View.OnClickListener,
        DlnaRecVideoListener, DlnaTerChListListener, DlnaBsChListListener {

    /**
     * 初回起動判定Flag.
     */
    private static boolean mIsFirstRun = true;
    /**
     * 次のアクティビティ情報
     */
    private Intent mNextActivity = null;
    /**
     * アプリ起動直後のdアカウントエラーの状況
     */
    private boolean mDaccountStatus = false;

    /**
     * 次の画面でdアカウントエラーを出すならtrueにする.
     */
    private boolean mIsDAccountErrorNextAvctivity = false;
    /**
     * 次の画面で設定画面エラーを出すならtrueにする.
     */
    private boolean mIsSettingErrorNextAvctivity = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.launch_main_layout);
        setTitleText(getString(R.string.str_launch_title));
        enableHeaderBackIcon(false);
        setStatusBarColor(true);
        setTheme(R.style.AppThemeBlack);
        setStatusBarColor(R.color.contents_header_background);
        setTitleVisibility(false);

        //現在のdアカウントダイアログの状況を取得
        mDaccountStatus = SharedPreferencesUtils.isFirstDaccountGetProcess(
                getApplicationContext());

        //アプリ起動時のサービストークン削除を行う
        SharedPreferencesUtils.deleteOneTimeTokenData(getApplicationContext());

        boolean isDlnaOk = startDlna();
        if (!isDlnaOk) {
            DTVTLogger.debug("BaseActivity");
            /*
             * to do: DLNA起動失敗の場合、仕様はないので、ここで将来対応
             */
        }
        mIsFirstRun = !SharedPreferencesUtils.getSharedPreferencesIsDisplayedTutorial(this);
    }

    /**
     * onPauseで消さないようにする為のオーバーライド
     */
    @Override
    protected void dismissDialogOnPause() {
        //dissmissを行わない事を明示する為コメント化
        //dismissDialog();
    }

    /**
     * 機能： Dlnaを開始.
     *
     * @return true: 成功　　false: 失敗
     */
    private boolean startDlna() {
        DlnaInterface di = DlnaInterface.getInstance();
        boolean ret;
        if (null == di) {
            ret = false;
        } else {
            ret = di.startDlna();
        }
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public String getScreenID() {
        return getString(R.string.str_launch_title);
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_launch_title);
    }

    @Override
    protected void onDaccountOttGetComplete(final boolean result) {
        super.onDaccountOttGetComplete(result);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //チュートリアルも含めて、次に表示する画面を選択する
                doScreenTransition();
            }
        });
    }

    /**
     * 次画面遷移.
     */
    private void doScreenTransition() {
        DTVTLogger.start();

        Intent intent;

        if(mIsFirstRun) {
            //チュートリアル画面に遷移
            intent = new Intent(getApplicationContext(), TutorialActivity.class);

        } else if (SharedPreferencesUtils.getSharedPreferencesStbConnect(this)) {
            // ペアリング済み HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, true);
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            DTVTLogger.debug("ParingOK Start HomeActivity");
        } else if (SharedPreferencesUtils.getSharedPreferencesStbSelect(this)) {
            // 次回から表示しないをチェック済み
            // 未ペアリング HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, false);
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            DTVTLogger.debug("ParingNG Start HomeActivity");
        } else {
            // STB選択画面へ遷移
            intent = new Intent(getApplicationContext(), STBSelectActivity.class);
            intent.putExtra(STBSelectActivity.FROM_WHERE, STBSelectActivity.STBSelectFromMode.STBSelectFromMode_Launch.ordinal());
            DTVTLogger.debug("Start STBSelectActivity");
        }

        //TODO: 作業の優先順位を鑑み、次画面ダイアログ表示の実装は保留とする
//        if(mIsDAccountErrorNextAvctivity) {
//            //次の画面にdアカウントエラーの表示を依頼する
//            //intent.putExtra()
//        }
//
//        if(mIsSettingErrorNextAvctivity) {
//            //次の画面に設定画面エラーの表示を依頼する
//        }
        startActivityWait(intent);
        DTVTLogger.end();
    }

    /**
     * ダイアログが表示されている場合は、まだ画面遷移を行わないようにするスタートアクティビティ.
     *
     * @param intent 飛び先情報のインテント
     */
    void startActivityWait(Intent intent) {
        //次の画面情報を控える
        mNextActivity = intent;

        //TODO: 暫定対応として、単純に呼び出す
        if(execCheck()) {
            startActivity(mNextActivity);
            finish();
        }
    }

    /**
     * ダイアログの処理終了後に次の画面に遷移を行う
     */
    @Override
    protected void startNextProcess() {
        if(execCheck() && !isFinishing()) {
            //遷移条件が整っていれば、次のアクティビティへ遷移する
            startActivity(mNextActivity);

            //GooglePlayの起動対象ならば、起動を行う
            if(mCheckSetting.isGooglePlay()) {
                toGooglePlay(UrlConstants.WebUrl.GOOGLEPLAY_DOWNLOAD_MY_URL);
                mCheckSetting.setGooglePlay(false);

                if(mDaccountStatus) {
                    //この場合は、dアカウントフラグのクリアを行う
                    SharedPreferencesUtils.setFirstExecFlag(getApplicationContext(),
                            SharedPreferencesUtils.FIRST_D_ACCOUNT_GET_BEFORE);
                }
            }
        }
    }

    /**
     * 次画面遷移可否チェック.
     *
     * @return 次の画面に遷移可能ならばtrue
     */
    private boolean execCheck() {
        //ダイアログの表示対象が処理中かどうかを見る
        if(getDialogQurCount() > 0) {
            //表示中のダイアログがあるならば遷移不可
            return false;
        }

        //dアカウントの処理が続行中か、そもそも実行されていなければ遷移不可
        if((mDAccountControl != null && mDAccountControl.isDAccountBusy())
                || mCheckSetting == null) {
            return false;
        }

        //設定ファイルの処理が続行中か、そもそも実行されていなければ遷移不可
        //TODO: Iemonサーバーとアクセスできない環境では、この条件が絶対に成立してしまい、先に進まないので、ひとまず無効化
//        if((mCheckSetting != null && mCheckSetting.isBusy())
//                || mCheckSetting == null) {
//            return false;
//        }

        return true;
    }

    @Override
    public void onVideoBrows(final DlnaRecVideoInfo curInfo) {

    }

    @Override
    public void onListUpdate(final DlnaTerChListInfo curInfo) {

    }

    @Override
    public void onListUpdate(final DlnaBsChListInfo curInfo) {

    }

    @Override
    public String getCurrentDmsUdn() {
        return null;
    }

    @Override
    protected void restartMessageDialog(final String... message) {
        // dアカが変わってもHOME遷移させない
    }
}
