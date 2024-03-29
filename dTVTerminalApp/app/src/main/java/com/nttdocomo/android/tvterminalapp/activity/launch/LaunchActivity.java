/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.ProcessSettingFile;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import com.nttdocomo.android.tvterminalapp.BuildConfig;

/**
 * アプリ起動時に最初に呼び出されるActivity.
 */
public class LaunchActivity extends BaseActivity implements View.OnClickListener {
    /** 最初の待ち時間の2秒. */
    private static final long FIRST_WAIT_TIME = 2000L;
    /** タイムアウトの待ち時間の8秒(最初の2秒と合わせて10秒). */
    private static final long TIME_OUT_WAIT_TIME = 8000L;
    /** 初回起動判定Flag. */
    private static boolean sIsFirstRun = true;
    /** 次のアクティビティ情報. */
    private Intent mNextActivity = null;
//    /** アプリ起動直後のdアカウントエラーの状況. */
//    private boolean mDaccountStatus = false;
    /** 次の画面で設定画面エラーを出すならtrueにする. */
    private boolean mIsSettingErrorNextAvctivity = false;

    /** 待ち時間タイマー用ハンドラー. */
    private Handler mTimerHandler = null;
    /** 待ち時間タイマー用ランナブル. */
    private Runnable mTimerRunnable = null;
    /**
     * タイムアウトタイマー用ハンドラー.
     * (dアカウントなどの処理終了時の即時次画面遷移の判定にも使うので、ヌルを明示)
     */
    private Handler mTimeoutHandler = null;
    /** タイムアウトタイマー用ランナブル. */
    private Runnable mTimeoutRunnable = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_main_layout);
        setTitleText(getString(R.string.str_launch_title));
        enableHeaderBackIcon(false);
        setTheme(R.style.AppThemeBlack);
        setStatusBarColor(R.color.contents_header_background);
        setTitleVisibility(false);

        //現在のdアカウントダイアログの状況を取得
//        mDaccountStatus = SharedPreferencesUtils.isFirstDaccountGetProcess(
//                getApplicationContext());

        //アプリ起動時のサービストークン削除を行う
        SharedPreferencesUtils.deleteOneTimeTokenData(getApplicationContext());
        // TODO: 暫定対応、削除必要する必要かも
        SharedPreferencesUtils.deleteUserInfoDate(getApplicationContext());
        //購入済み情報削除を行う
        SharedPreferencesUtils.deleteRentalInfoFromSharePre(getApplicationContext());

        StbConnectionManager.shared().launch(getApplicationContext());
        StbConnectionManager.shared().initializeState();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DlnaManager.shared().launch(getApplicationContext());
            }
        }).start();

        sIsFirstRun = !SharedPreferencesUtils.getSharedPreferencesIsDisplayedTutorial(this);

        if (sIsFirstRun) {
            SharedPreferencesUtils.setCacheDataVersion(this, DtvtConstants.NEW_CACHE_DATA_VERDION);
            SharedPreferencesUtils.setAppVersion(this, BuildConfig.VERSION_NAME);
        } else {
            checkCacheDataVersion();
            checkAppVersion();
        }

        //次に遷移する画面を選択する
        selectScreenTransition();
        // dアカウント処理は不要
        setUnnecessaryDaccountRegistService();
    }

    private void checkCacheDataVersion() {
        int savedCacheDataVersion = SharedPreferencesUtils.getCacheDataVersion(this);
        if (savedCacheDataVersion < DtvtConstants.NEW_CACHE_DATA_VERDION) {
            clearCacheKey();
            SharedPreferencesUtils.setCacheDataVersion(this, DtvtConstants.NEW_CACHE_DATA_VERDION);
        }
    }

    private void clearCacheKey() {
        DateUtils.clearTvScheduleDate(this);
        DateUtils.clearDataSave(this);
    }

    private void checkAppVersion() {
        String savedAppVersion = SharedPreferencesUtils.getAppVersion(this);
        if (savedAppVersion.compareTo(BuildConfig.VERSION_NAME) == -1) {
            SharedPreferencesUtils.setAppVersion(this, BuildConfig.VERSION_NAME);
        }
    }

    @Override
    protected void onResume() {
        //スプラッシュ画面の2秒表示用タイマーをセット
        setFirstTimeOut();
        super.onResume();
        super.sendScreenView(getString(R.string.google_analytics_screen_name_splash),
                ContentUtils.getParingAndLoginCustomDimensions(LaunchActivity.this));

        DTVTLogger.debug("normal exec setFirstTimeOut");
    }

    @Override
    protected void onPause() {
        DTVTLogger.start();
        if (mTimerHandler != null) {
            mTimerHandler.removeCallbacks(mTimerRunnable);
            mTimerRunnable = null;
        }
        super.onPause();
        DTVTLogger.end();
    }

    /**
     * 最初に必ず2秒待つ処理.
     */
    private void setFirstTimeOut() {
        if (mTimerRunnable != null) {
            //既に実行済みなので、帰る
            return;
        }

        //2秒経過後の処理の定義
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                //2秒経過したので、状況を確認する
                startActivityWait();

                if (!isFinishing()) {
                    //終了していなければ、次の処理を行う
                    setSrcondTimeOut();
                    findViewById(R.id.launch_progress).setVisibility(View.VISIBLE);
                }
            }
        };

        //最初の待ち時間の2秒を設定してタイマーを発動する
        mTimerHandler = new Handler();
        mTimerHandler.postDelayed(mTimerRunnable, FIRST_WAIT_TIME);
    }

    /**
     * 最初の2秒経過後の処理.
     */
    private void setSrcondTimeOut() {
        //10秒経過後の処理
        mTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                //10秒経過したので、状況を確認する
                startActivityWait();

                if (!isFinishing()) {
                    //終了していなければ、諦めて次の画面に遷移する
                    startActivity(mNextActivity);
                    finish();
                }
            }
        };

        //10秒を設定してタイマーを発動する
        mTimeoutHandler = new Handler();
        mTimeoutHandler.postDelayed(mTimeoutRunnable, TIME_OUT_WAIT_TIME);
    }

    /**
     * onPauseで消さないようにする為のオーバーライド.
     */
    @Override
    protected void dismissDialogOnPause() {
        //dissmissを行わない事を明示する為コメント化
        //dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //終了体制確認
        if (isFinishing()) {
            //終了時にタイマーが残っているならば、解除する
            if (mTimerHandler != null) {
                mTimerHandler.removeCallbacks(mTimerRunnable);
                mTimerRunnable = null;
                mTimerHandler = null;
            }
            if (mTimeoutHandler != null) {
                mTimeoutHandler.removeCallbacks(mTimeoutRunnable);
                mTimeoutRunnable = null;
                mTimeoutHandler = null;
            }
        }
    }

    @Override
    protected void onDaccountOttGetComplete(final boolean result) {
        super.onDaccountOttGetComplete(result);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //チュートリアルも含めて、次に表示する画面を選択する
                selectScreenTransition();
            }
        });
    }

    /**
     * 次画面遷移選択.
     */
    private void selectScreenTransition() {
        DTVTLogger.start();

        if (sIsFirstRun) {
            //チュートリアル画面に遷移
            mNextActivity = new Intent(getApplicationContext(), TutorialActivity.class);

        } else if (checkApplicationPrivacyPolicyVersion()) {
            // 利用規約画面表示
            mNextActivity = new Intent(getApplicationContext(), LaunchTermsOfServiceActivity.class);
            mNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (SharedPreferencesUtils.getSharedPreferencesStbConnect(this)) {
            // ペアリング済み HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, true);
            mNextActivity = new Intent(getApplicationContext(), HomeActivity.class);
            mNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            DTVTLogger.debug("ParingOK Start HomeActivity");
        } else if (SharedPreferencesUtils.getSharedPreferencesShowLaunchStbStatus(this)) {
            // 次回から表示しないをチェック済み
            // 未ペアリング HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, false);
            mNextActivity = new Intent(getApplicationContext(), HomeActivity.class);
            mNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            DTVTLogger.debug("ParingNG Start HomeActivity");
        } else {
            // 初期設定扉画面へ遷移(初期表示)
            if (!SharedPreferencesUtils.getSharedPreferencesStbLaunchFirst(this)) {
                mNextActivity = new Intent(getApplicationContext(), LaunchStbActivity.class);
            } else {
                // wifiチェック(未接続の場合)
                if (isWifiOn()) {
                    mNextActivity = new Intent(getApplicationContext(), LaunchStbActivity.class);
                } else {
                    mNextActivity = new Intent(getApplicationContext(), HomeActivity.class);
                }
            }
            mNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            DTVTLogger.debug("Start StbSelectActivity");
        }

        DTVTLogger.end();
    }

    /**
     * ダイアログが表示されている場合は、まだ画面遷移を行わないようにするスタートアクティビティ.
     */
    private void startActivityWait() {

        //設定ファイルダイアログ表示を次の画面に依頼するかを確認
        if (mIsSettingErrorNextAvctivity) {
            //次の画面に設定画面エラーの表示を依頼する
            mNextActivity.putExtra(SHOW_SETTING_FILE_DIALOG, true);
            Bundle sendData = new Bundle();
            sendData.putSerializable(SHOW_SETTING_FILE_DIALOG_DATA,
                    mCheckSetting.getSettingData());
            mNextActivity.putExtra(SHOW_SETTING_FILE_DIALOG_DATA, sendData);
        }

        //dアカウントと設定ファイルの処理が既に終わっているかどうかを確認し、終わっていた場合は遷移する
        if (execCheck()) {
            startActivity(mNextActivity);
            finish();
        }
    }

    /**
     * ダイアログの処理終了後に次の画面に遷移を行う.
     */
    @Override
    protected void startNextProcess() {
        if (execCheck() && !isFinishing()) {
            //遷移条件が整っていれば、次のアクティビティへ遷移する
            startActivity(mNextActivity);

            //GooglePlayの起動対象ならば、起動を行う
            if (mCheckSetting.isGooglePlay()) {
                toGooglePlay(UrlConstants.WebUrl.DTVT_GOOGLEPLAY_DOWNLOAD_URL);
                mCheckSetting.setGooglePlay(false);

//                if (mDaccountStatus) {
//
//                    //この場合は、dアカウントフラグのクリアを行う
//                    SharedPreferencesUtils.setFirstExecFlag(getApplicationContext(),
//                            SharedPreferencesUtils.FIRST_D_ACCOUNT_GET_BEFORE);
//                }
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
        if (getDialogQurCount() > 0) {
            //表示中のダイアログがあるならば遷移不可
            return false;
        }

        //設定ファイルの処理が続行中か、そもそも実行されていなければ遷移不可
        return !((mCheckSetting != null && mCheckSetting.isBusy())
                || mCheckSetting == null);

    }

    /**
     *アプリケーションプライバシーポリシーバージョンチェック
     *
     * @return アプリケーションプライバシーポリシー更新されていればtrue
     */
    private boolean checkApplicationPrivacyPolicyVersion() {
        DTVTLogger.start();
        //　保存されているアプリケーションプライバシーポリシーバージョン
        int applicationPrivacyPolicyVersion = SharedPreferencesUtils.getSharedPreferencesApplicationPrivacyPolicyVersion(getApplicationContext());
        // 更新されたアプリケーションプライバシーポリシーバージョン
        int newApplicationPrivacyPolicyVersion = DtvtConstants.NEW_APPLICATION_PRIVACY_POLICY_VERSION;

        DTVTLogger.end();
        return applicationPrivacyPolicyVersion < newApplicationPrivacyPolicyVersion;
    }

    @Override
    protected void restartMessageDialog(final String... message) {
        // dアカが変わってもHOME遷移させない
    }

    /**
     * 元の設定ファイル処理呼び出しを置き換える.
     */
    @Override
    protected void checkSettingFile() {
        DTVTLogger.start();

        //元の処理は使用しない事を明示する為にコメント化
        //super.checkSettingFile();

        //アプリ起動時か、BG→FG遷移時は設定ファイルの処理を呼び出す
        mCheckSetting = new ProcessSettingFile(this, true);

        //ファイルのチェックを開始する
        mCheckSetting.controlAtSettingFile(
                new ProcessSettingFile.ProcessSettingFileCallBack() {
            @Override
            public void onCallBack(final boolean dialogSw) {
                if (dialogSw) {
                    //エラーがあったので、フラグをON
                    mIsSettingErrorNextAvctivity = true;
                }
                //タイムアウトの待機中かどうかを見る
                if (mTimeoutHandler != null) {
                    //終了条件を満たしていた場合は次の画面に遷移する
                    startActivityWait();
                }
            }
        });

        DTVTLogger.end();
    }

    @Override
    protected void onReStartCommunication() {
        super.onReStartCommunication();
        setFirstTimeOut();
    }

    @Override
    protected void onStopAutoTransition() {
        super.onStopAutoTransition();
        if (mTimerHandler != null) {
            mTimerHandler.removeCallbacks(mTimerRunnable);
            mTimerRunnable = null;
        }
    }
}
