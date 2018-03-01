/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.thread;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

/**
 * DbThreadクラス
 * 機能： DB操作は時間かかる操作なので、UI Thread以外の新しいThreadで実行するクラスである.
 * <p>
 * 注意点: 一つのDB操作は一つのDbThreadオブジェクトを使うこと推薦.
 */
public class DbThread extends Thread {

    /**
     * ハンドラ.
     */
    private Handler mHandle = null;
    /**
     * callbackInterface.
     */
    private DbOperation mDbOperationFinish = null;
    /**
     * OperationId.
     */
    private int mOperationId = 0;

    /**
     * callbackInterface.
     */
    public interface DbOperation {
        /**
         * DB操作完了する時実行される.
         *
         * @param isSuccessful DB操作結果
         * @param resultSet    ResultSetがある場合、ResultSetを戻す。ResultSetはない場合、nullを戻す
         * @param operationId  多Threadオブジェクトを使用する時、Threadオブジェクトを区別する
         */
        void onDbOperationFinished(boolean isSuccessful, final List<Map<String, String>> resultSet, int operationId);

        /**
         * DB操作をThread中で実行、操作内容はクラス外で決める(e.g. "select * from xxx where yy=zz ...,   delete from xxx").
         *
         * @param operationId operationId
         * @return DB取得結果
         */
        List<Map<String, String>> dbOperation(final int operationId);
    }

    /**
     * @param handle      非同期処理ハンドラー.
     * @param lis         　操作
     * @param operationId 多Threadオブジェクトを使用する時、Threadオブジェクトを区別する
     */
    public DbThread(@NonNull final Handler handle, final DbOperation lis, final int operationId) {

        mHandle = handle;
        mDbOperationFinish = lis;
        mOperationId = operationId;
    }

    @Override
    public void run() {
        List<Map<String, String>> ret = null;

        if (null != mDbOperationFinish) {
            ret = mDbOperationFinish.dbOperation(mOperationId);
        }

        final List<Map<String, String>> finalRet = ret;
        mHandle.post(new Runnable() {

            @Override
            public void run() {
                if (null != mDbOperationFinish) {
                    if (finalRet == null || finalRet.size() < 1 || finalRet.get(0).isEmpty()) {
                        mDbOperationFinish.onDbOperationFinished(false, null, mOperationId);
                    } else {
                        mDbOperationFinish.onDbOperationFinished(true, finalRet, mOperationId);
                    }
                }
            }
        });
    }
}