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
import com.nttdocomo.android.idmanager.IDimServiceAppServiceCustom;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * dアカウント連携・サービス登録
 */
public class DaccountRegistService {
    /**
     * 結果を返すコールバック
     */
    public interface DaccountRegistServiceCallBack {
        /**
         * サービス登録結果を返す
         *
         * @param result 結果コード 0ならば成功
         */
        void registServiceCallBack(int result);
    }

    //コンテキストの控え
    private Context mContext = null;

    //コールバックの控え
    private DaccountRegistServiceCallBack mDaccountRegistServiceCallBack;

    //dアカウント設定アプリの接続用のクラス(サービス設定用)
    private IDimServiceAppServiceCustom mServiceCustom;

    /**
     * 各コールバックの動作を定義する
     */
    private final IDimServiceAppCallbacks callback = new IDimServiceAppCallbacks.Stub() {
        @Override
        public void onCompleteRegistService(int appReqId, int result) throws RemoteException {
            //dアカウント設定アプリと切断する
            daccountServiceEnd();

            //結果を呼び出し元に返す
            mDaccountRegistServiceCallBack.registServiceCallBack(result);

            DTVTLogger.end();
        }

        // 以下のメソッドはJavaのインターフェースの仕様により宣言を強要されているだけで、ここで使われることはない
        @Override
        public void onCompleteGetOneTimePassword(int appReqId, int result, String id,
                                                 String oneTimePassword,
                                                 String appCheckKey) throws RemoteException {
        }

        @Override
        public void onCompleteCheckService(int appReqId, int result, String version,
                                           String protocolVersion) throws RemoteException {
        }

        @Override
        public void onCompleteGetAuthToken(int appReqId, int result, String id, String token,
                                           String appCheckKey) throws RemoteException {
        }

        @Override
        public void onCompleteGetIdStatus(int appReqId, int result, String id,
                                          boolean isDefault, boolean hasMsn,
                                          boolean authStatus) throws RemoteException {
        }
    };

    /**
     * dアカウント設定アプリ接続・切断処理のコールバック
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //切断されたのでヌルを格納
            mServiceCustom = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //dアカウント設定アプリとの接続に成功したので、中のメソッドを呼び出せるようにする
            mServiceCustom = IDimServiceAppServiceCustom.Stub.asInterface(service);

            //結果
            int result;

            //複数呼び出し時の識別用・通常はゼロとする
            int appReqId = 0;

            //呼び出し用のパラメータの設定
            DTVTLogger.debug("compName=" + name);
            String serviceKey = DaccountConstants.SERVICE_KEY;

            //各ブロードキャストレシーバーの名前を指定する
            String setDefIdReceiver = "com.nttdocomo.android.tvterminalapp.SetDefIdReceiver";
            String userAuthReceiver = "com.nttdocomo.android.tvterminalapp.UserAuthReceiver";
            String deleteIdReceiver = "com.nttdocomo.android.tvterminalapp.DelIdReceiver";
            String invalidateIdReceiver = "com.nttdocomo.android.tvterminalapp.InvIdReceiver";

            try {
                //サービス登録を呼び出す
                result = mServiceCustom.registServiceWithReceiver(appReqId, serviceKey,
                        setDefIdReceiver, userAuthReceiver, deleteIdReceiver,
                        invalidateIdReceiver, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
                //例外が発生した場合は、自前で内部エラーにする
                result = IDimDefines.RESULT_INTERNAL_ERROR;
            }

            //結果コードを判定
            if (IDimDefines.REQUEST_ACCEPTED != result) {
                //正常以外の結果ならば、コールバックを呼んで終わらせる
                mDaccountRegistServiceCallBack.registServiceCallBack(result);
            }
        }
    };

    /**
     * コンストラクタ
     */
    public DaccountRegistService() {
    }

    /**
     * サービス登録処理を開始する
     *
     * @param context               コンテキスト
     * @param daccountRegistService 結果を返すコールバック
     */
    public synchronized void execRegistService(Context context,
                                   DaccountRegistServiceCallBack daccountRegistService) {
        DTVTLogger.start();

        //コンテキストとコールバックの取得
        mContext = context;
        mDaccountRegistServiceCallBack = daccountRegistService;

        //サービス登録処理の開始
        bindDimServiceAppService();
    }

    /**
     * dアカウントアプリをバインドする
     */
    private void bindDimServiceAppService() {
        //dアカウント設定アプリを指定して、接続を試みる
        Intent intent = new Intent();
        intent.setClassName(
                DaccountConstants.D_ACCOUNT_ID_MANAGER,
                DaccountConstants.D_ACCOUNT_SERVICE);
        intent.setAction(DaccountConstants.REGIST_SERVICE_ACTION);
        boolean ans = mContext.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        if (!ans && mDaccountRegistServiceCallBack != null) {
            //正常以外の結果ならば、コールバックを呼んで終わらせる
            mDaccountRegistServiceCallBack.registServiceCallBack(
                    IDimDefines.RESULT_INTERNAL_ERROR);
        }
    }

    /**
     * dアカウントアプリを切り離す
     */
    private void daccountServiceEnd() {
        mContext.unbindService(mServiceConnection);
    }

}
