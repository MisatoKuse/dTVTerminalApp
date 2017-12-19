/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailCacheManager;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * dアカウント連携・サービス登録状況の確認からワンタイムパスワードの取得までを行うクラス
 */
public class DaccountControl implements
        DaccountGetOTT.DaccountGetOttCallBack,
        DaccountRegistService.DaccountRegistServiceCallBack,
        DaccountCheckService.DaccountCheckServiceCallBack {

    //コンテキストの控え
    private Context mContext = null;

    //戻り値控え
    private int mResult = 0;

    //実行クラス名控え
    private String mResultClass = "";

    /**
     * コールバックは失敗と成功しか返さないので、エラーの値が欲しい場合はこれで取得
     *
     * @return エラーの値
     */
    public int getmResult() {
        //必要な場合に戻り値を取得できるようにする
        return mResult;
    }

    /**
     * エラーの値は同じ値でも実行する要求によって異なる場合があるので、実行クラス名を取得
     *
     * @return 実行したクラスの名前
     */
    public String getmResultClass() {
        return mResultClass;
    }

    /**
     * 結果を返すコールバック
     */
    public interface DaccountControlCallBack {
        /**
         * サービス登録結果を返す
         *
         * @param result trueならば成功
         */
        void daccountControlCallBack(boolean result);
    }

    //コールバックの控え
    private DaccountControlCallBack mDaccountControlCallBack;

    /**
     * dアカウントに対して、サービス登録を行う
     *
     * @param context                       コンテキスト
     * @param daccountControlCallBackSource 結果を返すコールバック
     */
    public void registService(Context context, DaccountControlCallBack daccountControlCallBackSource) {
        DTVTLogger.start();
        //コールバックがヌルならば何もできないので帰る
        if (daccountControlCallBackSource == null) {
            return;
        }
        mDaccountControlCallBack = daccountControlCallBackSource;

        //ヌルではないコンテキストは後で使いまわす為に控える
        if (context == null) {
            //コンテキストが無いので、エラーで帰る
            mDaccountControlCallBack.daccountControlCallBack(false);
            DTVTLogger.end();
            return;
        }
        mContext = context;

        //サービス確認の要求を行う
        DaccountCheckService checkService = new DaccountCheckService();
        checkService.execDaccountCheckService(context, this);

        //クラス名を控える
        mResultClass = checkService.getClass().getSimpleName();
    }


    @Override
    public void checkServiceCallBack(int result) {
        //戻り値を控える
        mResult = result;

        // dtvtアプリのサービスが端末に登録されているかどうかの結果
        if (result != IDimDefines.RESULT_NOT_AVAILABLE &&
                result != IDimDefines.RESULT_INCOMPATIBLE_ENVIRONMENT &&
                result != IDimDefines.RESULT_INTERNAL_ERROR) {
            //結果が利用不可や動作対象外や内部エラー以外は、サービス登録要求を呼び出す
            DaccountRegistService registService = new DaccountRegistService();
            registService.execRegistService(mContext, this);
            //クラス名を控える
            mResultClass = registService.getClass().getSimpleName();
        } else {
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);
            DTVTLogger.end();
        }
    }

    @Override
    public void registServiceCallBack(int result) {
        //戻り値を控える
        mResult = result;

        //サービス登録の結果
        if (result == IDimDefines.RESULT_COMPLETE ||
                result == IDimDefines.RESULT_RESULT_REGISTERED) {
            // 登録結果が登録/更新成功か、既に登録済みならば、ワンタイムパスワードの取得を行う
            DaccountGetOTT getOneTimePass = new DaccountGetOTT();
            getOneTimePass.execDaccountGetOTT(mContext, this);
            //クラス名を控える
            mResultClass = getOneTimePass.getClass().getSimpleName();
        } else {
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);
            DTVTLogger.end();
        }
    }

    @Override
    public void getOttCallBack(int result, String id, String oneTimePassword) {
        //戻り値を控える
        mResult = result;

        //ワンタイムパスワードの取得結果
        if (oneTimePassword == null) {
            //ワンタイムトークン取得失敗
            //実行失敗なので、エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);
            DTVTLogger.end();
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
            //TODO: キャッシュクリア処理は後ほど検討

            //エラーを返す
            mDaccountControlCallBack.daccountControlCallBack(false);
            DTVTLogger.end();
            return;
        }

        //ワンタイムパスワードを控える
        SharedPreferencesUtils.setSharedPreferencesOneTimePass(mContext, oneTimePassword);

        //実行に成功したので、trueを返す
        mDaccountControlCallBack.daccountControlCallBack(true);
        DTVTLogger.end();
    }

    /**
     * dアカウントのユーザー切り替え時に行うキャッシュ等のクリア処理
     * @param context コンテキスト
     */
    public static void cacheClear(Context context) {
        DTVTLogger.start();
        //キャッシュ削除タスクを呼び出す
        CacheClearTask clearTask = new CacheClearTask();
        clearTask.execute(context);
        DTVTLogger.end();
    }

    /**
     * キャッシュ削除処理の実体
     * おそらく必要はないが、一応ファイル削除やDB削除なので、AsyncTaskとする
     */
    private static class CacheClearTask extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... contexts) {
            DTVTLogger.start();

            //プリファレンスユーティリティの配下のデータを、ユーザー切り替え後も残す一部を除き削除
            SharedPreferencesUtils.clearAlmostSharedPreferences(contexts[0]);

            //日付ユーティリティの配下のプリファレンスを削除
            DateUtils.clearDataSave(contexts[0]);

            //サムネイルのキャッシュファイルを削除する
            ThumbnailCacheManager.clearThumbnailCache(contexts[0]);

            //DBを丸ごと削除する
            boolean deleteDatabaseExeced = contexts[0].deleteDatabase(DBConstants.DATABASE_NAME);

            DTVTLogger.debug("deleteDatabase Answer = " + deleteDatabaseExeced);

            //DBを新造する・インスタンスを作ると自動で作成される
            new DBHelper(contexts[0]);

            DTVTLogger.end();
            return null;
        }
    }
}
