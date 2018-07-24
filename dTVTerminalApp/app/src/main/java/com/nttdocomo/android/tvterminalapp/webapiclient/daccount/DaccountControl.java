/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.datamanager.ThumbnailCacheManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadDataProvider;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * dアカウント連携・サービス登録状況の確認からワンタイムパスワードの取得までを行うクラス.
 */
public class DaccountControl implements
        DaccountGetOtt.DaccountGetOttCallBack,
        DaccountRegistService.DaccountRegistServiceCallBack,
        DaccountCheckService.DaccountCheckServiceCallBack {

    /**
     * コンテキストの控え.
     */
    private Context mContext = null;

    /**
     * 戻り値控え.
     */
    private int mResult = 0;

    /**
     * コールバックの控え.
     */
    private DaccountControlCallBack mDaccountControlCallBack = null;

    /**
     * 単回実行フラグ.
     */
    private DaccountControlOnce mOnceControl = null;

    /**
     * DaccountRegistServiceクラスのインスタンス.
     */
    private DaccountRegistService mDaccountRegistService = null;
    /**
     * DaccountGetOTTクラスのインスタンス.
     */
    private DaccountGetOtt mDaccountGetOtt = null;
    /**
     * DaccountCheckServiceクラスのインスタンス.
     */
    private DaccountCheckService mDaccountCheckService = null;
    /**
     * dアカウント処理中フラグ.
     */
    private boolean mDAccountBusy = false;

    /**
     * 実行クラスの識別用固定値.
     */
    public enum CheckLastClassEnum {
        /**
         * サービス登録チェック.
         */
        CHECK_SERVICE,
        /**
         * サービス登録.
         */
        REGIST_SERVICE,
        /**
         * ワンタイムパスワード登録.
         */
        ONE_TIME_PASS_WORD,
    }

    /**
     * 実行クラス識別値控え.
     */
    private CheckLastClassEnum mResultClass;

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * OTTチェックが終了しているかどうか.
     */
    private boolean mIsOTTCheckFinished = false;

    /**
     * コールバックは失敗と成功しか返さないので、エラーの値が欲しい場合はこれで取得.
     *
     * @return OTTチェックの終了の値
     */
    public boolean getIsOTTCheckFinished() {
        return mIsOTTCheckFinished;
    }

    /**
     * コールバックは失敗と成功しか返さないので、エラーの値が欲しい場合はこれで取得.
     *
     * @return エラーの値
     */
    public int getResult() {
        //必要な場合に戻り値を取得できるようにする
        return mResult;
    }

    /**
     * BGからFG遷移時は自前のコールバックを使用するので、後続処理用にエラー値を設定する.
     *
     * @param result エラーの値
     */
    public void setResult(int result) {
        //エラーの値をセット
        mResult = result;
    }

    /**
     * エラーの値は同じ値でも実行する要求によって異なる場合があるので、実行クラス識別値を取得.
     *
     * @return 実行したクラスの識別値
     */
    public CheckLastClassEnum getmResultClass() {
        return mResultClass;
    }

    /**
     * dアカウント取得状態のゲッター.
     *
     * @return 何らかのdアカウントの処理が行われているならばtrue
     */
    public boolean isDAccountBusy() {
        return mDAccountBusy;
    }
    /**
     * dアカウント取得状態のセッター.
     *
     * @param busy 処理が終わった場合はfalseをセット
     */
    public void setDAccountBusy(final boolean busy) {
        mDAccountBusy = busy;
    }

    /**
     * 結果を返すコールバック.
     */
    public interface DaccountControlCallBack {
        /**
         * サービス登録結果を返す.
         *
         * @param result trueならば成功
         */
        void daccountControlCallBack(boolean result);
    }

    /**
     * DaccountGetOTTクラスのゲッター.
     *
     * @return DaccountGetOTTクラス
     */
    public DaccountGetOtt getmDaccountGetOtt() {
        return mDaccountGetOtt;
    }

    /**
     * dアカウントに対して、サービス登録を行う.
     *
     * @param context                       コンテキスト
     * @param daccountControlCallBackSource 結果を返すコールバック
     */
    @SuppressWarnings("OverlyLongMethod")
    public void registService(final Context context, final DaccountControlCallBack daccountControlCallBackSource) {
        DTVTLogger.start();
        if (mIsCancel) {
            return;
        }

        //初回実行である事を設定する
        if (context != null) {
            SharedPreferencesUtils.setFirstExecStart(context);
        }

        //初回起動の場合を判定
        if (context != null && SharedPreferencesUtils.isFirstDaccountGetProcess(context)) {
            //初回の場合はダイアログを表示するので、処理中フラグを立てる
            mDAccountBusy = true;
        }

        //コールバックがヌルならば何もできないので帰る
        if (daccountControlCallBackSource == null) {
            DTVTLogger.end("no callback");

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);

            return;
        }
        mDaccountControlCallBack = daccountControlCallBackSource;

        if (DaccountUtils.checkDAccountIsExist() == null) {
            //dアカウント設定アプリが存在しないので帰る。ここで帰れば、単体実行フラグがセットされず、別のアクティビティの実行時に自動的に実行される。
            //後ほど、dアカウント設定アプリがダウンロードされた場合、その直後のアクティビティの起動時に呼び出されるので、意図的にダウンロード直後に処理を
            //挿入する必要はない。
            DTVTLogger.end("not install idmanager");
            mIsOTTCheckFinished = true;
            mDaccountControlCallBack.daccountControlCallBack(false);
            return;
        }

        mOnceControl = DaccountControlOnce.getInstance();
        if (mOnceControl.isExecOnce()) {
            //既にサービスを登録済みとなる。
            DTVTLogger.debug("registService already exec");

            //設定画面でdアカウントを切り替えた際は、ホーム画面の情報切り替えの為に、サービス登録済みでもコールバックが必要となる
            //登録済みでも、dアカウントの取得に失敗している場合があったので、存在の有無で処理を変更する
            if (TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferencesDaccountId(context))
                    || TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferencesOneTimePass(context))) {
                DTVTLogger.debug("”execDaccountGetOTT” Daccount get retry");

                //認証画面の表示状況のインスタンスの取得
                OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.INSTANCE;

                //dアカウントは取れていないので、再取得
                mContext = context;
                mDaccountGetOtt = new DaccountGetOtt();
                mDaccountGetOtt.execDaccountGetOTT(mContext, ottGetAuthSwitch.isNowAuth(), this);
                //クラスの識別値を設定
                mResultClass = CheckLastClassEnum.ONE_TIME_PASS_WORD;
            } else {
                DTVTLogger.debug("Daccount true return");
                mIsOTTCheckFinished = true;
                //dアカウントは取れているので、trueを返す
                mDaccountControlCallBack.daccountControlCallBack(true);
            }

            return;
        }

        //次回の実行を阻止するためフラグをセット
        mOnceControl.setExecOnce(true, mDaccountGetOtt, mContext);

        //ヌルではないコンテキストは後で使いまわす為に控える
        if (context == null) {
            mIsOTTCheckFinished = true;
            //コンテキストが無いので、エラーで帰る
            mDaccountControlCallBack.daccountControlCallBack(false);
            DTVTLogger.end("no context");

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);

            return;
        }
        mContext = context;

        //現在残っているワンタイムパスワードは無効になるのでクリアしておく
        SharedPreferencesUtils.setSharedPreferencesOneTimePass(mContext,"");

        //サービス確認の要求を行う
        mDaccountCheckService = new DaccountCheckService();
        mDaccountCheckService.execDaccountCheckService(context, this);

        //クラスの識別値を控える
        mResultClass = CheckLastClassEnum.CHECK_SERVICE;
    }


    @Override
    public void checkServiceCallBack(final int result) {
        if (mIsCancel) {
            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);
            return;
        }
        //戻り値を控える
        mResult = result;
        // dtvtアプリのサービスが端末に登録されているかどうかの結果
        if (result != IDimDefines.RESULT_NOT_AVAILABLE
                && result != IDimDefines.RESULT_INCOMPATIBLE_ENVIRONMENT
                && result != IDimDefines.RESULT_INTERNAL_ERROR) {
            //結果が利用不可や動作対象外や内部エラー以外は、サービス登録要求を呼び出す
            //（この時点でサービス未登録ならば登録すればいいので、ワーディングリストのサービス未登録とは別件）
            mDaccountRegistService = new DaccountRegistService();
            mDaccountRegistService.execRegistService(mContext, this);
            //クラスの識別値を控える
            mResultClass = CheckLastClassEnum.REGIST_SERVICE;
        } else {
            mIsOTTCheckFinished = true;
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);

            DTVTLogger.end();
        }
    }

    @Override
    public void registServiceCallBack(final int result) {
        if (mIsCancel) {
            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);
            return;
        }
        //戻り値を控える
        mResult = result;

        //サービス登録の結果
        if (result == IDimDefines.RESULT_COMPLETE
                || result == IDimDefines.RESULT_RESULT_REGISTERED) {
            //認証画面の表示状況のインスタンスの取得
            OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.INSTANCE;

            // 登録結果が登録/更新成功か、既に登録済みならば、ワンタイムパスワードの取得を行う
            DTVTLogger.debug("”execDaccountGetOTT” registServiceCallBack");
            mDaccountGetOtt = new DaccountGetOtt();
            mDaccountGetOtt.execDaccountGetOTT(mContext, ottGetAuthSwitch.isNowAuth(), this);
            //クラスの識別値を控える
            mResultClass = CheckLastClassEnum.ONE_TIME_PASS_WORD;
        } else {
            mIsOTTCheckFinished = true;
            //実行失敗なので、エラーを返す
            // ここでサービス登録に失敗するのが、ワーディングリストの「サービス未登録」になる
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);

            DTVTLogger.end();
        }
    }

    @Override
    public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
        if (mIsCancel) {
            return;
        }
        //戻り値を控える
        mResult = result;

        //ワンタイムパスワードの取得結果(ワンタイムパスワードの有無が重要なので、resultの判定は無用)
        //ワーディングリストのdアカウント取得失敗の下記の問題はここでの判定となる
        //認証状態無効・タイムアウト・ユーザー中断・ユーザー状態異常・サーバエラー・ネットワークエラー・内部エラー
        //本来はresultが以下の値であることの検査が必要だが、各エラーに固有のエラーメッセージは無いので、現状では行わない
        // RESULT_INVALID_ID/RESULT_USER_TIMEOUT/RESULT_USER_CANCEL/RESULT_USER_INVALID_STATE/
        // RESULT_SERVER_ERROR/RESULT_NETWORK_ERROR/RESULT_INTERNAL_ERROR
        if (oneTimePassword == null || oneTimePassword.isEmpty()) {
            mIsOTTCheckFinished = true;
            //ワンタイムトークン取得失敗
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);

            DTVTLogger.end("not get one time password");
            return;
        }

        //古いIDを取得する
        String oldId = SharedPreferencesUtils.getSharedPreferencesDaccountId(mContext);

        if (oldId == null || oldId.isEmpty()) {
            //古いIDが無いのは初回の実行なので、oldIDにidの内容を入れて、処理を通す
            oldId = id;

            //保存する
            SharedPreferencesUtils.setSharedPreferencesDaccountId(mContext, id);
        }

        //返ってきたIDを今のIDと比較する
        if (!id.equals(oldId)) {
            //IDが変わったので、保存する
            SharedPreferencesUtils.setSharedPreferencesDaccountId(mContext, id);

            //キャッシュクリアを呼ぶ
            //IDが変更されていた場合は、キャッシュクリアを呼ぶ
            DaccountControl.cacheClear(mContext, mDaccountGetOtt);

            mIsOTTCheckFinished = true;
            //エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false, mDaccountGetOtt, mContext);

            DTVTLogger.end("change id");
            return;
        }

        //ワンタイムパスワードを控える
        SharedPreferencesUtils.setSharedPreferencesOneTimePass(mContext, oneTimePassword);

        //処理が正常に終わったので、フラグは初期化
        mDAccountBusy = false;

        mIsOTTCheckFinished = true;
        //実行に成功したので、trueを返す
        mDaccountControlCallBack.daccountControlCallBack(true);
        DTVTLogger.end();
    }

    /**
     * 最後に実行したのがサービスチェックかどうかのチェック.
     *
     * @return そうならばtrue
     */
    public boolean isCheckService() {
        return mResultClass == CheckLastClassEnum.CHECK_SERVICE;

    }

    /**
     * 最後に実行したのがサービス登録かどうかのチェック.
     *
     * @return そうならばtrue
     */
    public boolean isRegistService() {
        return mResultClass == CheckLastClassEnum.REGIST_SERVICE;

    }

    /**
     * 最後に実行したのがワンタイムパスワード取得かどうかのチェック.
     *
     * @return そうならばtrue
     */
    public boolean isOneTimePass() {
        return mResultClass == CheckLastClassEnum.ONE_TIME_PASS_WORD;

    }

    /**
     * dアカウントのユーザー切り替え時に行うキャッシュ等のクリア処理.
     * <p>
     * (既存メソッドと互換の物）
     *
     * @param context コンテキスト
     */
    public static void cacheClear(final Context context) {
        //ワンタイムトークン取得クラスは未使用なのでヌル
        cacheClear(context, null);
    }

    /**
     * dアカウントのユーザー切り替え時に行うキャッシュ等のクリア処理.
     * <p>
     * (次回OTT取得許可付き)
     *
     * @param context        コンテキスト
     * @param daccountGetOtt ワンタイムトークン取得クラス
     */
    private static void cacheClear(final Context context, final DaccountGetOtt daccountGetOtt) {
        DTVTLogger.start();
        DaccountControlOnce onceControl = DaccountControlOnce.getInstance();

        //プリファレンスユーティリティの配下のデータを、ユーザー切り替え後も残す一部を除き削除
        SharedPreferencesUtils.clearAlmostSharedPreferences(context);
        //ダウンロードしたコンテンツを削除
        DownloadDataProvider.cancelAll();
        DownloadDataProvider.deleteAllDownLoadContents(context);
        if (StbConnectionManager.shared().getConnectionStatus() != StbConnectionManager.ConnectionStatus.NONE_PAIRING) {
            DlnaManager.shared().StopDmp();
            StbConnectionManager.shared().setConnectionStatus(StbConnectionManager.ConnectionStatus.NONE_PAIRING);
        }

        //日付ユーティリティの配下のプリファレンスを削除
        DateUtils.clearDataSave(context);

        //サムネイルのキャッシュファイルを削除する
        ThumbnailCacheManager.clearThumbnailCache(context);

        // 再起動フラグの設定
        SharedPreferencesUtils.setSharedPreferencesRestartFlag(context, true);

        //次回実行する為にフラグをリセット
        onceControl.setExecOnce(false, daccountGetOtt, context);

        DTVTLogger.end();
    }


    /**
     * 通信停止タスク実行.
     */
    public void stopCommunication() {
        mIsCancel = true;
        StopDaccountConnect stopConnect = new StopDaccountConnect();
        stopConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
    }

    /**
     * 通信停止処理実行.
     */
    private void stopConnect() {
        if (mDaccountCheckService != null) {
            mDaccountCheckService.daccountServiceEnd();
            DTVTLogger.debug("DaccountCheckServiceStop");
        }
        if (mDaccountGetOtt != null) {
            mDaccountGetOtt.daccountServiceEnd();
            DTVTLogger.debug("DaccountGetOTTStop");
        }
        if (mDaccountRegistService != null) {
            mDaccountRegistService.daccountServiceEnd();
            DTVTLogger.debug("DaccountRegistServiceStop");
        }
    }

    /**
     * dアカウント制御をアプリ起動時に1回だけ行うための制御クラス.
     */
    private static class DaccountControlOnce {
        /**
         * 初回のみ実行用のインスタンス.
         */
        private static final DaccountControlOnce sDaccountControlOnce = new DaccountControlOnce();

        /**
         * 実行状況を保持する.
         */
        private static boolean sExecOnce = false;

        /**
         * 初回実行かどうかを検知する.
         *
         * @return 実行済みならばtrue
         */
        boolean isExecOnce() {
            return sExecOnce;
        }

        /**
         * 実行フラグを設定する.
         *
         * @param execOnce       設定を行う実行フラグの値
         * @param daccountGetOtt OTT取得処理のインスタンス
         * @param context コンテキスト
         */
        void setExecOnce(final boolean execOnce, final DaccountGetOtt daccountGetOtt,
                         final Context context) {
            DaccountControlOnce.sExecOnce = execOnce;

            //OTT取得と無関係なところから呼ばれた場合はヌルなので、判定する
            if (daccountGetOtt != null) {
                //次のOTT取得を許可する
                daccountGetOtt.allowNext(context);
            }
        }

        /**
         * 内容は無いが、これが無いと余計なインスタンスが作成される.
         */
        private DaccountControlOnce() {
        }

        /**
         * 自分のインスタンスを返す.
         *
         * @return 自分のインスタンス
         */
        public static DaccountControlOnce getInstance() {
            return sDaccountControlOnce;
        }
    }

    /**
     * 通信停止用AsyncTaskクラス.
     */
    private class StopDaccountConnect extends AsyncTask<DaccountControl, Void, Void> {
        /**
         * コンストラクタ.
         */
        StopDaccountConnect() {
            DTVTLogger.start();
        }

        @Override
        protected Void doInBackground(final DaccountControl... dataProviders) {
            DTVTLogger.start();
            //通信を行っている処理を止める
            if (dataProviders != null) {
                for (DaccountControl contentsDetailDataProvider : dataProviders) {
                    if (contentsDetailDataProvider != null) {
                        contentsDetailDataProvider.stopConnect();
                    }
                }
            }
            return null;
        }
    }
}
