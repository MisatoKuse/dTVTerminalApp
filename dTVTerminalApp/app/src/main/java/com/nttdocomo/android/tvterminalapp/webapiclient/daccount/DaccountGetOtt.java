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
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * dアカウント連携・OTT取得.
 */
public class DaccountGetOtt {

    /**
     * コンテキストの控え.
     */
    private Context mContext = null;

    /**
     * 実行待ちキュー.
     */
    final private OttGetQueue mOttGetQueue;

    /**
     * コールバックの控え.
     */
    private DaccountGetOttCallBack mDaccountGetOttCallBack = null;

    /**
     * dアカウント設定アプリの接続用のクラス.
     */
    private IDimServiceAppService mService = null;

    /**
     * OTT取得時再認証設定するかフラグ.
     */
    private boolean isReauthorization = false;

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

            // エラー判定を行う
            if ((result == IDimDefines.RESULT_USER_CANCEL
                    || result == IDimDefines.RESULT_INVALID_ID)) {
                //キャンセルか認証状態無効だった場合は、いち早くベースアクティビティ側に知らせて、
                //ログアウト等のダイアログを海外判定のダイアログよりも優先表示する
                OttGetAuthSwitch.getInstance().setSkipPermission(true);
            }

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
            //自動再認証の設定
            int option;
            if (isReauthorization) {
                String docomoId = SharedPreferencesUtils.getSharedPreferencesDaccountId(mContext);
                if (docomoId == null || docomoId.isEmpty()) {
                    //未ログインの時に、再認証はしない
                    option = IDimDefines.CertOption.RESERVE;
                } else {
                    option = IDimDefines.CertOption.DEFAULT;

                    //自動再認証が発生する可能性があるので、パーミッションチェックをスキップする。
                    //再認証画面のキャンセルボタンのコールバックより、BaseActivityのonResumeが先に動くので、
                    //結果を待っていては、パーミッションチェックのダイアログの表示を防げないため
                    OttGetAuthSwitch.getInstance().setSkipPermission(true);
                }
            } else {
                //DEFAULTとRESERVEを能動制御するように変更したので、こちらでDEFAULTにして再認証画面を強要する必要がなくなったので、元に戻した
                option = IDimDefines.CertOption.RESERVE;
            }

            String appCheckKey = "";

            try {
                //OTT取得処理を呼び出す(IDは無設定の場合、既にdアカウント設定アプリに設定された物が
                //使用されるので、空欄とします）
                result = mService.getOneTimePassword(appReqId, "", serviceKey, option,
                        appCheckKey, callback);
            } catch (RemoteException e) {
                DTVTLogger.debug(e);
                //例外が発生した場合は、自前で内部エラーにする
                result = IDimDefines.RESULT_REMOTE_EXCEPTION;
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
    public DaccountGetOtt() {
        //重複OTT取得防止処理の設定
        mOttGetQueue = OttGetQueue.getInstance();
    }

    /**
     * コンストラクタ(context設定付き).
     * @param context context
     */
    public DaccountGetOtt(final Context context) {
        //元のコンストラクターを呼ぶ
        this();

        //指定されたコンテキストを退避
        mContext = context;
    }

    /**
     * OTT取得処理を開始する.
     *
     * (OTT取得処理重複実行防止付き)
     * @param context                コンテキスト
     * @param isReauthorization      再認証するかフラグ
     * @param daccountGetOttCallBack 結果を返すコールバック
     */
    public synchronized void execDaccountGetOTT(
            final Context context,
            final boolean isReauthorization,
            final DaccountGetOttCallBack daccountGetOttCallBack) {
        DTVTLogger.start();
        DTVTLogger.debug("DaccountGetOttCallBack = " + daccountGetOttCallBack);
        this.isReauthorization = isReauthorization;
        this.mDaccountGetOttCallBack = daccountGetOttCallBack;
        //同時に複数個実行されないようにする
        mOttGetQueue.getOttAddOrExec(context, this);
        DTVTLogger.end();
    }

    /**
     * OTT取得処理開始の実処理.
     *
     * @param context                コンテキスト
     */
    synchronized void execDaccountGetOttReal(
            final Context context) {
        DTVTLogger.start();

        //コンテキストとコールバックの取得
        mContext = context;

        //現在残っているワンタイムパスワードは無効になるのでクリアしておく
        SharedPreferencesUtils.setSharedPreferencesOneTimePass(mContext,"");

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

        //パラメータにヌルがあるならばエラーとする
        if (mContext == null || mServiceConnection == null) {
            mDaccountGetOttCallBack.getOttCallBack(IDimDefines.RESULT_INTERNAL_ERROR, "", "");
            return;
        }

        boolean ans = mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        if (!ans && mDaccountGetOttCallBack != null) {
            //正常以外の結果の場合も、unbindは必要な場合あり
            daccountServiceEnd();

            //正常以外の結果ならば、コールバックを呼んで終わらせる
            mDaccountGetOttCallBack.getOttCallBack(IDimDefines.RESULT_INTERNAL_ERROR, "", "");
        }
    }

    /**
     * dアカウントアプリを切り離す.
     */
    public void daccountServiceEnd() {
        //アンバインドに必要なパラメータがそろっているかどうかを見る(なおmServiceがヌルの場合、アンバインドは失敗する)
        if (mContext != null && mService != null && mServiceConnection != null) {
            try {
                mContext.unbindService(mServiceConnection);
                DTVTLogger.debug("DaccountGetOTTUnbind");
            } catch (IllegalArgumentException e) {
                // 何らかの理由でdアカウントアプリ側で問題が発生してアンバインドできない場合の例外。
                // 通常は発生しないはず。また発生してもこちらでは何もできない。
                DTVTLogger.debug(e);
            }
            mService = null;
        }
    }

    /**
     * 取得したOTTが使い終わった場合に呼び、次のOTT取得を許可する.
     * @param context コンテキスト
     */
    public void allowNext(final Context context) {
        //次の処理のウェイトを解除する
        mOttGetQueue.allowNext(context);
    }

    /**
     * 通信切断フラグのセット.
     *
     * @param disconnectionFlag trueならば通信切断
     */
    public void setDisconnectionFlag(final boolean disconnectionFlag) {
        if (mOttGetQueue != null) {
            //キュークラスに丸投げ
            mOttGetQueue.setDisconnectionFlag(disconnectionFlag);
        }
    }

    /**
     * 今ある通信タスクをキャンセルする.
     */
    public void cancelConnection() {
        if (mOttGetQueue != null) {
            //キュークラスに丸投げ
            mOttGetQueue.cancelConnection();
        }
    }
}
