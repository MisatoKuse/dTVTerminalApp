/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.nttdocomo.android.idmanager.IDimServiceAppCallbacks;
import com.nttdocomo.android.idmanager.IDimServiceAppService;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;

/**
 * dアカウント連携・OTT取得.
 */
public class DaccountGetOTT {

    /**
     * コンテキストの控え.
     */
    private Context mContext = null;

    /**
     * コールバックの控え.
     */
    private DaccountGetOttCallBack mDaccountGetOttCallBack = null;

    /**
     * dアカウント設定アプリの接続用のクラス.
     */
    private IDimServiceAppService mService = null;

    /**
     * 結果を返すコールバック.
     */
    public interface DaccountGetOttCallBack {
        /**
         * OTT取得結果を返す.
         *
         * @param result          結果コード 0ならば成功
         * @param id              id
         * @param oneTimePassword OTT
         */
        void getOttCallBack(int result, String id, String oneTimePassword);
    }

    /**
     * 各コールバックの動作を定義する.
     */
    private final IDimServiceAppCallbacks callback = new IDimServiceAppCallbacks.Stub() {
        @Override
        public void onCompleteGetOneTimePassword(final int appReqId, final int result, final String id,
                                                 final String oneTimePassword, final String appCheckKey) throws RemoteException {
            //dアカウント設定アプリと切断する
            daccountServiceEnd();

            String resultId;
            String resultOneTimePassword;

            //ヌルなら空文字に変更
            if (id == null) {
                resultId = "";
            } else {
                resultId = id;
            }
            if (oneTimePassword == null) {
                resultOneTimePassword = "";
            } else {
                resultOneTimePassword = oneTimePassword;
            }

            if (mDaccountGetOttCallBack != null) {
                //結果を呼び出し元に返す
                mDaccountGetOttCallBack.getOttCallBack(result, resultId, resultOneTimePassword);
            }

            DTVTLogger.end();
        }

        // 以下のメソッドはJavaのインターフェースの仕様により宣言を強要されているだけで、ここで使われることはない
        @Override
        public void onCompleteCheckService(final int appReqId, final int result, final String version,
                                           final String protocolVersion) throws RemoteException {
        }

        @Override
        public void onCompleteRegistService(final int appReqId, final int result) throws RemoteException {
        }

        @Override
        public void onCompleteGetAuthToken(final int appReqId, final int result, final String id,
                                           final String token, final String appCheckKey) throws RemoteException {
        }

        @Override
        public void onCompleteGetIdStatus(final int appReqId, final int result, final String id,
                                          final boolean isDefault, final boolean hasMsn,
                                          final boolean authStatus) throws RemoteException {
        }
    };

    /**
     * dアカウント設定アプリ接続・切断処理のコールバック.
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(final ComponentName name) {
            //切断されたのでヌルを格納
            mService = null;
        }

        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            //dアカウント設定アプリとの接続に成功したので、中のメソッドを呼び出せるようにする
            mService = IDimServiceAppService.Stub.asInterface(service);

            //結果
            int result;

            //複数呼び出し時の識別用・通常はゼロとする
            int appReqId = 0;

            //呼び出し用のパラメータの設定
            String serviceKey = DaccountConstants.getDaccountServiceKey();
            //自動再認証OFFの設定
            int option = IDimDefines.CertOption.RESERVE;
            String appCheckKey = "";
            DTVTLogger.debug("compName=" + name);

            try {
                //OTT取得処理を呼び出す(IDは無設定の場合、既にdアカウント設定アプリに設定された物が
                //使用されるので、空欄とします）
                result = mService.getOneTimePassword(appReqId, "", serviceKey, option,
                        appCheckKey, callback);
            } catch (RemoteException e) {
                DTVTLogger.debug(e);
                //例外が発生した場合は、自前で内部エラーにする
                result = IDimDefines.RESULT_INTERNAL_ERROR;
            }

            //結果コードを判定
            if (IDimDefines.REQUEST_ACCEPTED != result && mDaccountGetOttCallBack != null) {
                //正常以外の結果ならば、コールバックを呼んで終わらせる
                mDaccountGetOttCallBack.getOttCallBack(result, "", "");
            }
        }
    };

    /**
     * コンストラクタ.
     */
    public DaccountGetOTT() {
    }

    /**
     * OTT取得処理を開始する.
     *
     * @param context                コンテキスト
     * @param daccountGetOttCallBack 結果を返すコールバック
     */
    public synchronized void execDaccountGetOTT(final Context context,
                                                final DaccountGetOttCallBack daccountGetOttCallBack) {
        DTVTLogger.start();

        //コンテキストとコールバックの取得
        mContext = context;
        mDaccountGetOttCallBack = daccountGetOttCallBack;

        //OTT取得処理の開始
        bindDimServiceAppService();
    }

    /**
     * dアカウントアプリをバインドする.
     */
    private void bindDimServiceAppService() {
        //dアカウント設定アプリを指定して、接続を試みる
        Intent intent = new Intent();
        intent.setClassName(
                DaccountConstants.D_ACCOUNT_ID_MANAGER,
                DaccountConstants.D_ACCOUNT_SERVICE);
        boolean ans = mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        if (!ans && mDaccountGetOttCallBack != null) {
            //正常以外の結果ならば、コールバックを呼んで終わらせる
            mDaccountGetOttCallBack.getOttCallBack(IDimDefines.RESULT_INTERNAL_ERROR, "", "");
        }
    }

    /**
     * dアカウントアプリを切り離す.
     */
    void daccountServiceEnd() {
        if (mService != null) {
            mContext.unbindService(mServiceConnection);
            mService = null;
            DTVTLogger.debug("DaccountGetOTTUnbind");
        }
    }
}
