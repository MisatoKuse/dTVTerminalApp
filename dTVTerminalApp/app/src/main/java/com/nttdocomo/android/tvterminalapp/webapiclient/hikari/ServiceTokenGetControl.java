/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.struct.OneTimeTokenData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOTT;

/**
 * サービストークン取得クラス.
 */
public class ServiceTokenGetControl {
    //コンテキスト
    private final Context mContext;

    //トークンデータ
    private OneTimeTokenData mOneTimeTokenData;

    //コールバック
    private NextProcessInterface mNextProcess;

    /**
     * コンストラクタ.
     */
    public ServiceTokenGetControl(Context context) {
        mContext = context;
    }

    /**
     * サービストークン取得統合処理.
     *
     * @param nextProcess 次に実行する処理のコールバック
     */
    public void getToken(NextProcessInterface nextProcess) {
        //コールバックのセット
        mNextProcess = nextProcess;

        //先行して、サービストークンの取得を行うため、期限内かどうかをみる
        mOneTimeTokenData = SharedPreferencesUtils.getOneTimeTokenData(mContext);

        //期限内ならば、そのトークンを使用するので、そのまま実行を行う
        if (mOneTimeTokenData.getOneTimeTokenGetTime() > DateUtils.getNowTimeFormatEpoch()) {
            nextProcess();
            return;
        }

        //dアカウントのワンタイムパスワードの取得を行う
        DaccountGetOTT getOtt = new DaccountGetOTT();
        getOtt.execDaccountGetOTT(mContext, new DaccountGetOTT.DaccountGetOttCallBack() {
            @Override
            public void getOttCallBack(int result, String id, String oneTimePassword) {
                gotOtt(result, id, oneTimePassword);
            }
        });

        //以後の処理はワンタイムパスワード取得後のコールバックで行う
    }

    /**
     * パスワードの取得後の処理
     *
     * @param result          結果コード
     * @param id              dアカウント
     * @param oneTimePassword パスワード
     */
    private void gotOtt(int result, String id, String oneTimePassword) {
        if (result != 0) {
            //パスワードの取得に失敗したので、サービストークンは取得できない。次に進む
            nextProcess();
            return;
        }

        //サービストークンを取得してから実行を行う
        ServiceTokenClient serviceTokenClient = new ServiceTokenClient(mContext);
        serviceTokenClient.getServiceTokenApi(oneTimePassword,
                new ServiceTokenClient.TokenGetCallback() {
                    @Override
                    public void onTokenGot(boolean success) {
                        //成功していれば、トークンはプリファレンスに入っているので読み込む・失敗でも空文字なので、先の判定で弾かれる
                        mOneTimeTokenData = SharedPreferencesUtils.getOneTimeTokenData(mContext);

                        //次へ進む
                        nextProcess();
                        return;
                    }
                }
        );
    }

    /**
     * 次の処理を呼び出す
     */
    private void nextProcess() {
        //コールバックがヌルではないならば呼び出す
        if (mNextProcess != null) {
            mNextProcess.onTokenGot();
        }
    }

    /**
     * 終了した場合に呼ばれるコールバックのインターフェース
     */
    public interface NextProcessInterface {
        /**
         * 終了した場合に呼ばれるコールバック.
         */
        void onTokenGot();
    }
}
