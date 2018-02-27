/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.ThumbnailCacheManager;
import com.nttdocomo.android.tvterminalapp.utils.DAccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * dアカウント連携・サービス登録状況の確認からワンタイムパスワードの取得までを行うクラス.
 */
public class DaccountControl implements
        DaccountGetOTT.DaccountGetOttCallBack,
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
    private DaccountGetOTT mDaccountGetOTT = null;
    /**
     * DaccountCheckServiceクラスのインスタンス.
     */
    private DaccountCheckService mDaccountCheckService = null;

    /**
     * 実行クラスの識別用固定値.
     */
    private enum CHECK_LAST_CLASS {
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
    private CHECK_LAST_CLASS mResultClass;

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバックは失敗と成功しか返さないので、エラーの値が欲しい場合はこれで取得.
     *
     * @return エラーの値
     */
    public int getmResult() {
        //必要な場合に戻り値を取得できるようにする
        return mResult;
    }

    /**
     * エラーの値は同じ値でも実行する要求によって異なる場合があるので、実行クラス識別値を取得.
     *
     * @return 実行したクラスの識別値
     */
    public CHECK_LAST_CLASS getmResultClass() {
        return mResultClass;
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
     * dアカウントに対して、サービス登録を行う.
     *
     * @param context                       コンテキスト
     * @param daccountControlCallBackSource 結果を返すコールバック
     */
    public void registService(final Context context, final DaccountControlCallBack daccountControlCallBackSource) {
        DTVTLogger.start();
        if (mIsCancel) {
            return;
        }
        if (DAccountUtils.checkDAccountIsExist() == null) {
            //dアカウント設定アプリが存在しないので帰る。ここで帰れば、単体実行フラグがセットされず、別のアクティビティの実行時に自動的に実行される。
            //後ほど、dアカウント設定アプリがダウンロードされた場合、その直後のアクティビティの起動時に呼び出されるので、意図的にダウンロード直後に処理を
            //挿入する必要はない。
            DTVTLogger.end("not install idmanager");
            return;
        }

        mOnceControl = DaccountControlOnce.getInstance();
        if (mOnceControl.isExecOnce()) {
            //既に実行済みなので帰る
            DTVTLogger.end("registService already exec");
            return;
        }

        //次回の実行を阻止するためフラグをセット
        mOnceControl.setExecOnce(true);

        //コールバックがヌルならば何もできないので帰る
        if (daccountControlCallBackSource == null) {
            DTVTLogger.end("no callback");

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);

            return;
        }
        mDaccountControlCallBack = daccountControlCallBackSource;

        //ヌルではないコンテキストは後で使いまわす為に控える
        if (context == null) {
            //コンテキストが無いので、エラーで帰る
            mDaccountControlCallBack.daccountControlCallBack(false);
            DTVTLogger.end("no context");

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);

            return;
        }
        mContext = context;

        //サービス確認の要求を行う
        mDaccountCheckService = new DaccountCheckService();
        mDaccountCheckService.execDaccountCheckService(context, this);

        //クラスの識別値を控える
        mResultClass = CHECK_LAST_CLASS.CHECK_SERVICE;
    }


    @Override
    public void checkServiceCallBack(final int result) {
        if (mIsCancel) {
            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);
            return;
        }
        //戻り値を控える
        mResult = result;
        // dtvtアプリのサービスが端末に登録されているかどうかの結果
        if (result != IDimDefines.RESULT_NOT_AVAILABLE
                && result != IDimDefines.RESULT_INCOMPATIBLE_ENVIRONMENT
                && result != IDimDefines.RESULT_INTERNAL_ERROR) {
            //結果が利用不可や動作対象外や内部エラー以外は、サービス登録要求を呼び出す
            mDaccountRegistService = new DaccountRegistService();
            mDaccountRegistService.execRegistService(mContext, this);
            //クラスの識別値を控える
            mResultClass = CHECK_LAST_CLASS.REGIST_SERVICE;
        } else {
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);

            DTVTLogger.end();
        }
    }

    @Override
    public void registServiceCallBack(final int result) {
        if (mIsCancel) {
            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);
            return;
        }
        //戻り値を控える
        mResult = result;

        //サービス登録の結果
        if (result == IDimDefines.RESULT_COMPLETE
                || result == IDimDefines.RESULT_RESULT_REGISTERED) {
            // 登録結果が登録/更新成功か、既に登録済みならば、ワンタイムパスワードの取得を行う
            mDaccountGetOTT = new DaccountGetOTT();
            mDaccountGetOTT.execDaccountGetOTT(mContext, this);
            //クラスの識別値を控える
            mResultClass = CHECK_LAST_CLASS.ONE_TIME_PASS_WORD;
        } else {
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);

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
        if (oneTimePassword == null || oneTimePassword.isEmpty()) {
            //ワンタイムトークン取得失敗
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);

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
            DaccountControl.cacheClear(mContext);

            //エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);

            //次回実行する為にフラグをリセット
            mOnceControl.setExecOnce(false);

            DTVTLogger.end("change id");
            return;
        }

        //ワンタイムパスワードを控える
        SharedPreferencesUtils.setSharedPreferencesOneTimePass(mContext, oneTimePassword);

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
        return mResultClass == CHECK_LAST_CLASS.CHECK_SERVICE;

    }

    /**
     * 最後に実行したのがサービス登録かどうかのチェック.
     *
     * @return そうならばtrue
     */
    public boolean isRegistService() {
        return mResultClass == CHECK_LAST_CLASS.REGIST_SERVICE;

    }

    /**
     * 最後に実行したのがワンタイムパスワード取得かどうかのチェック.
     *
     * @return そうならばtrue
     */
    public boolean isOneTimePass() {
        return mResultClass == CHECK_LAST_CLASS.ONE_TIME_PASS_WORD;

    }

    /**
     * dアカウントのユーザー切り替え時に行うキャッシュ等のクリア処理.
     *
     * @param context コンテキスト
     */
    static void cacheClear(final Context context) {
        DTVTLogger.start();
        DaccountControlOnce onceControl = DaccountControlOnce.getInstance();

        //キャッシュ削除タスクを呼び出す
        CacheClearTask clearTask = new CacheClearTask();
        clearTask.execute(context);

        //次回実行する為にフラグをリセット
        onceControl.setExecOnce(false);

        DTVTLogger.end();
    }

    /**
     * キャッシュ削除処理の実体.
     * おそらく必要はないが、一応ファイル削除やDB削除なので、AsyncTaskとする
     */
    private static class CacheClearTask extends AsyncTask<Context, Void, Void> {
        /**
         * 退避用コンテキスト.
         */
        Context mContext = null;

        @Override
        protected synchronized Void doInBackground(final Context... contexts) {
            DTVTLogger.start();

            //コンテキストの退避
            mContext = contexts[0];
            if (mContext == null) {
                return null;
            }

            //プリファレンスユーティリティの配下のデータを、ユーザー切り替え後も残す一部を除き削除
            SharedPreferencesUtils.clearAlmostSharedPreferences(mContext);

            //日付ユーティリティの配下のプリファレンスを削除
            DateUtils.clearDataSave(mContext);

            //サムネイルのキャッシュファイルを削除する
            ThumbnailCacheManager.clearThumbnailCache(mContext);

            //DBを丸ごと削除する
            boolean deleteDatabaseExeced = mContext.deleteDatabase(DBConstants.DATABASE_NAME);

            DTVTLogger.debug("deleteDatabase Answer = " + deleteDatabaseExeced);

            //DBを新造する・インスタンスを作ると自動で作成される
            new DBHelper(mContext);

            DTVTLogger.end();
            return null;
        }

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);
            DTVTLogger.start();

            if (mContext == null) {
                //コンテキストがヌルならば帰る
                DTVTLogger.end();
                return;
            }

            // TODO 実行確認用メッセージ・後ほど削除
            //Toast.makeText(mContext, R.string.d_account_chamge_message, Toast.LENGTH_LONG).show();

            // 再起動フラグの設定
            SharedPreferencesUtils.setSharedPreferencesRestartFlag(mContext, true);

            DTVTLogger.end();
        }
    }

    /**
     * 通信停止タスク実行.
     */
    public void stopCommunication() {
        mIsCancel = true;
        StopDaccountConnect stopConnect = new StopDaccountConnect();
        stopConnect.execute(this);
    }

    /**
     * 通信停止処理実行.
     */
    private void stopConnect() {
        if (mDaccountCheckService != null) {
            mDaccountCheckService.daccountServiceEnd();
            DTVTLogger.debug("DaccountCheckServiceStop");
        }
        if (mDaccountGetOTT != null) {
            mDaccountGetOTT.daccountServiceEnd();
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
        private static DaccountControlOnce sDaccountControlOnce = new DaccountControlOnce();

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
         * @param execOnce 設定を行う実行フラグの値
         */
        void setExecOnce(final boolean execOnce) {
            DaccountControlOnce.sExecOnce = execOnce;
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
