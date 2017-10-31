/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.thread;

import android.os.Handler;

import java.util.List;
import java.util.Map;

/*
 *  DbThreadクラス
 *      機能： DB操作は時間かかる操作なので、UI Thread以外の新しいThreadで実行するクラスである
 *
 *      注意点: 一つのDB操作は一つのDbThreadオブジェクトを使うこと推薦
 */
public class DbThread extends Thread {

    public interface DbOperation{
        /**
         * DB操作完了する時実行される
         * @param isSuccessful DB操作結果
         * @param resultSet     ResultSetがある場合、ResultSetを戻す。ResultSetはない場合、nullを戻す
         * @param operationId 多Threadオブジェクトを使用する時、Threadオブジェクトを区別する
         */
         void onDbOperationFinished(boolean isSuccessful, final List<Map<String, String>> resultSet, int operationId);

        /**
         * DB操作をThread中で実行、操作内容はクラス外で決める(e.g. "select * from xxx where yy=zz ...,   delete from xxx")
         * @return
         * @throws Exception
         */
         List<Map<String, String>> dbOperation(int operationId) throws Exception;
    }

    private Handler mHandle=null;
    private DbOperation mDbOperationFinish=null;
    private boolean mError=false;
    private int mOperationId=0;

    /**
     *
     * @param handle 非同期処理ハンドラー
     * @param lis　操作
     * @param operationId 多Threadオブジェクトを使用する時、Threadオブジェクトを区別する
     * @throws Exception
     */
    public DbThread(Handler handle, DbOperation lis, int operationId) throws Exception {
        if(null==handle ){
            throw new Exception("DbOperationFinishThread Exception, cause=(null==handle)");
        }

        mHandle= handle;
        mDbOperationFinish=lis;
        mOperationId = operationId;
    }

    @Override
    public void run() {
        mError=false;
        List<Map<String, String>> ret=null;

        if(null!=mDbOperationFinish){
            try {
                ret = mDbOperationFinish.dbOperation(mOperationId);
            } catch (Exception e) {
                e.printStackTrace();
                mError=true;
            }
        }

        final List<Map<String, String>> finalRet = ret;
        mHandle.post(new Runnable() {

            @Override
            public void run() {
                if(null!=mDbOperationFinish){
                    if(mError){
                        mDbOperationFinish.onDbOperationFinished(false, null, mOperationId);
                    } else {
                        mDbOperationFinish.onDbOperationFinished(true, finalRet, mOperationId);
                    }
                }
            }
        });
    }
}
