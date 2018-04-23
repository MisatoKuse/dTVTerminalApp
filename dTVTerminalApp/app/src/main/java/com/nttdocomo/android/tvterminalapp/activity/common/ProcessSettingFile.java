/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.BuildConfig;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.SettingFileMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.SettingFileResponse;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.SettingFileWebClient;

/**
 * 設定ファイルを読み込み、それに応じて、アプリ実行中断かアプリバージョンアップへの誘導を行う.
 * <p>
 * 必ずベースアクティビティから呼ばれる
 */
public class ProcessSettingFile {
    /**
     * ベースアクティビティ.
     */
    private final BaseActivity mActivity;
    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * 設定ファイルの内容.
     */
    private SettingFileMetaData mSettingData;
    /**
     * 設定ファイル読み込みAPI.
     */
    private SettingFileWebClient mSettingFileWebClient = null;
    /**
     * コールバック用のインスタンス.
     */
    private ProcessSettingFileCallBack mProcessSettingFileCallBack = null;
    /**
     * 処理中フラグ.
     */
    private boolean mBusy = false;
    /**
     * グーグルプレイ起動フラグ.
     */
    private boolean mGooglePlay = false;
    /**
     * リモート視聴ならばtrue.
     */
    private boolean mIsRemote = false;
    /**
     * ダイアログ表示を行わないならばtrue.
     */
    private boolean mIsNoDialog = false;
    /**
     * リモート視聴の際、問題が無かった場合に視聴を介する為のコールバック.
     */
    public interface ProcessSettingFileCallBack {
        /**
         * 処理終了後のコールバック.
         * @param error エラーがあるならばtrue
         */
        void onCallBack(boolean error);
    }

    /**
     * 設定ファイル処理が処理中かどうかを見る.
     *
     * @return 処理中ならばtrue
     */
    public boolean isBusy() {
        return mBusy;
    }

    /**
     * リモート視聴フラグのセット.
     *
     * @param mIsRemote リモート視聴ならばtrue
     */
    public void setIsRemote(final boolean mIsRemote) {
        this.mIsRemote = mIsRemote;
    }

    /**
     * コンストラクタ.
     *
     * @param activity BaseActivity アクティビティ
     * @param noDialogSwitch ダイアログ表示有無(ダイアログを表示するならfalse)
     */
    public ProcessSettingFile(final BaseActivity activity, final boolean noDialogSwitch) {
        //アクティビティの退避
        mActivity = activity;

        //コンテキストの取得
        mContext = mActivity.getApplicationContext();

        //情報クラスの初期化
        mSettingData = new SettingFileMetaData();

        //ダイアログ表示のスイッチ設定
        mIsNoDialog = noDialogSwitch;
    }

    /**
     * 設定ファイルの制御処理.
     * @param callBack コールバック.
     */
    public void controlAtSettingFile(final ProcessSettingFileCallBack callBack) {
        //処理に入ったので、処理中フラグをtrueにする
        mBusy = true;

        if (callBack != null) {
            //コールバックが指定されていた場合は、控えておく
            mProcessSettingFileCallBack = callBack;
        }

        //まず、前回の読み込みから1時間(java時間は桁が多いので、エポック秒は千倍する)が経過したかどうかを見る
        long lastDate = SharedPreferencesUtils.getSharedPreferencesSettingFileDate(mContext);
        if (lastDate + (DateUtils.EPOCH_TIME_ONE_HOUR * 1000) > System.currentTimeMillis()) {
            //1時間以内なので、前回の値を使用する
            mSettingData.setIsStop(
                    SharedPreferencesUtils.getSharedPreferencesSettingFileIsStop(mContext));
            mSettingData.setDescription(
                    SharedPreferencesUtils.getSharedPreferencesSettingFileStopMessage(mContext));
            mSettingData.setForceUpdateVersion(
                    SharedPreferencesUtils.getSharedPreferencesSettingFileForceUpdate(mContext));
            mSettingData.setOptionalUpdateVersion(
                    SharedPreferencesUtils.getSharedPreferencesSettingFileOptionalUpdate(
                            mContext));
            mSettingData.setIsFileReadError(
                    SharedPreferencesUtils.getSharedPreferencesSettingFileIsReadFail(
                            mContext));

            //実際の制御がすぐ開始できる
            processControl();
            return;
        }

        //データが無いか、1時間以上経過しているので、設定ファイルを読み込みを開始する
        mSettingFileWebClient = new SettingFileWebClient(mContext);
        mSettingFileWebClient.getSettingFileApi(new SettingFileWebClient.SettingFileJsonParserCallback() {
            @Override
            public void onSettingFileJsonParsed(final SettingFileResponse settingFileResponse) {
                //読み込み終了のコールバック
                if (settingFileResponse != null) {
                    //取得した値を取り込む
                    mSettingData = settingFileResponse.getSettingFile();

                    //取得した値を次回の為に格納する
                    SharedPreferencesUtils.setSharedPreferencesSettingFileIsStop(
                            mContext, mSettingData.isIsStop());
                    SharedPreferencesUtils.setSharedPreferencesSettingFileStopMessage(
                            mContext, mSettingData.getDescription());
                    SharedPreferencesUtils.setSharedPreferencesSettingFileForceUpdate(
                            mContext, mSettingData.getForceUpdateVersion());
                    SharedPreferencesUtils.setSharedPreferencesSettingFileOptionalUpdate(
                            mContext, mSettingData.getOptionalUpdateVersion());

                    //現在日時を設定する
                    SharedPreferencesUtils.setSharedPreferencesSettingFileDate(
                            mContext, System.currentTimeMillis());
                } else {
                    //ファイル未発見の場合は、ファイル取得エラーをONにする
                    mSettingData.setIsFileReadError(true);
                    //通常処理では止めないので、こちらはfalse
                    mSettingData.setIsStop(false);
                    //終了メッセージも読めていないのでクリア
                    mSettingData.setDescription("");

                    //現在日時は設定しないので、次回も読み込む
                }

                //実際の制御はコールバック後の今になる
                processControl();
            }
        });
    }

    /**
     * 単独でダイアログを表示させるために、状況を再現するメソッド.
     * @param settingMetaData 設定ファイル再現情報
     */
    public void processControlEmulate(final SettingFileMetaData settingMetaData) {
        mSettingData = settingMetaData;

        //ダイアログを表示する
        processControl();
    }

    /**
     * 設定ファイルに基いた実際の制御を行う.
     */
    private void processControl() {
        //処理は終わったので、フラグをリセット
        mBusy = false;

        //ダイアログスイッチ
        boolean dialogSwitch = false;

        //停止の有無を見る
        if (mSettingData.isIsStop()) {
            //停止処理を行う
            stopAllActivityDialog();
            //ダイアログを表示する分岐に入ったのでtrue
            dialogSwitch = true;

        } else if (mIsRemote && mSettingData.isFileReadError()) {
            //リモート視聴判定の場合は、ファイルアクセス失敗の場合もエラーを出す
            stopAllActivityDialog();
        } else if (mSettingData.getForceUpdateVersion() > BuildConfig.VERSION_CODE) {
            //強制アップデートのバージョンの方が新しいので、GooglePlayダイアログをキャンセル無しで呼び出す
            showGooglePlayDialog(false);
            //ダイアログを表示する分岐に入ったのでtrue
            dialogSwitch = true;

        } else if (mSettingData.getOptionalUpdateVersion() > BuildConfig.VERSION_CODE) {
            //ダウンロードダイアログをキャンセル付きで呼び出す
            showGooglePlayDialog(true);
            //ダイアログを表示する分岐に入ったのでtrue
            dialogSwitch = true;

        }

        //何も問題は無かったので、コールバックが指定されていればそこへ飛ぶ
        if (mProcessSettingFileCallBack != null) {
            mProcessSettingFileCallBack.onCallBack(dialogSwitch);
        }
    }

    /**
     * アプリを終了させる.
     */
    private void stopAllActivity() {
        DTVTLogger.start();
        //これで、アプリは終了する。
        mActivity.finishAffinity();
        DTVTLogger.end();
    }

    /**
     * 実行停止ダイアログ.
     */
    private void stopAllActivityDialog() {
        //今回はダイアログ表示を見送るか判定
        if (mIsNoDialog) {
            return;
        }

        //ダイアログを、キャンセル無しにする
        CustomDialog dialog = new CustomDialog(mActivity, CustomDialog.DialogType.ERROR);

        String printMessage;
        //指定の文字列があるかどうかを見る
        if (TextUtils.isEmpty(mSettingData.getDescription())) {
            //無いのでデフォルト文字列を設定
            printMessage = mActivity.getString(R.string.setting_file_stop_apli);
        } else {
            //あるので設定する
            printMessage = mSettingData.getDescription();
        }

        //メッセージの設定
        dialog.setContent(printMessage);

        //OKボタンのコールバックを設定
        dialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                DTVTLogger.start();
                //自分は終わる
                stopAllActivity();
                DTVTLogger.end();
            }
        });

        //次のダイアログを呼ぶ為の処理
        dialog.setDialogDismissCallback(mActivity);

        //ダイアログを表示
        mActivity.offerDialog(dialog);
    }

    /**
     * グーグルプレイ呼び出しダイアログ.
     *
     * @param isCancel キャンセルが必要ならばtrue
     */
    private void showGooglePlayDialog(final boolean isCancel) {
        //今回はダイアログ表示を見送るか判定
        if (mIsNoDialog) {
            return;
        }

        CustomDialog dialog;
        String printMessage;

        //GooglePlayを起動した
        mGooglePlay = true;

        if (isCancel) {
            //ダイアログをキャンセル付きにする
            dialog = new CustomDialog(mActivity, CustomDialog.DialogType.CONFIRM);
            printMessage = mActivity.getString(R.string.setting_file_download);
        } else {
            //ダイアログを、キャンセル無しにする
            dialog = new CustomDialog(mActivity, CustomDialog.DialogType.ERROR);
            printMessage = mActivity.getString(R.string.setting_file_force_download);
        }

        //メッセージの設定
        dialog.setContent(printMessage);

        //OKボタンのコールバックを設定
        dialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                DTVTLogger.start();
                //ダウンロードに遷移
                mActivity.toGooglePlay(UrlConstants.WebUrl.GOOGLEPLAY_DOWNLOAD_MY_URL);
                DTVTLogger.end();
            }
        });

        if (isCancel) {
            //キャンセル付きの場合はキャンセルのコールバックを用意
            dialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
                @Override
                public void onCancelCallback() {
                    DTVTLogger.start();
                    //キャンセルが押されたので、GooglePlayフラグはOFF
                    mGooglePlay = false;
                    //ダウンロードしないので、コールバックが指定されていればそこへ飛ぶ
                    if (mProcessSettingFileCallBack != null) {
                        //今回はダイアログ表示後の処理なので、falseを指定
                        mProcessSettingFileCallBack.onCallBack(false);
                    }
                    DTVTLogger.end();
                }
            });
        }

        //次のダイアログを呼ぶ為の処理
        dialog.setDialogDismissCallback(mActivity);

        //showDialogの代わり・重複ダイアログ実現用
        mActivity.offerDialog(dialog);
    }

    /**
     * グーグルプレイ起動フラグの取得.
     *
     * @return グーグルプレイを起動していた場合はtrue
     */
    public boolean isGooglePlay() {
        return mGooglePlay;
    }

    /**
     *  googlePlayフラグ設定.
     * @param googlePlaySwitch  googlePlay起動対象フラグ
     */
    public void setGooglePlay(final boolean googlePlaySwitch) {
        mGooglePlay = googlePlaySwitch;
    }

    /**
     * 設定ファイル状況を渡す.
     * @return 設定ファイル状況
     */
    public SettingFileMetaData getSettingData() {
        return mSettingData;
    }

    /**
     * コールバック指定を後から追加する.
     *
     * @param callBack コールバック
     */
    public void setProcessSettingFileCallBack(final ProcessSettingFileCallBack callBack) {
        mProcessSettingFileCallBack = callBack;
    }
}