/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayDeque;
import java.util.Queue;


/**
 * 順次OTT取得呼び出しキュー.
 */
enum OttGetQueue {
    /**
     * シングルトン制御
     */
    INSTANCE;
    private String field;

    /**
     * OTT取得開始に必要な、取得後のコールバックを蓄積するキュー.
     */
    Queue<DaccountGetOTT.DaccountGetOttCallBack> mTaskQueue = null;

    /**
     * OTT取得クラス.
     */
    DaccountGetOTT mDaccountGetOTT = null;

    //
    /**
     * 前回実行時の時間を実行するか否かの基準にする
     */
    long mTaskExecFlag = 0;

    /**
     * 最大待ち時間.
     */
    private static final long MAX_WAIT_TIME = 3000;

    /**
     * シングルトン制御用文字列
     */
    private static final String SINGLETON_TEXT = "SINGLETON_TEXT";

    /**
     * クラスのインスタンス取得
     *
     * @return インスタンス
     */
    @SuppressWarnings("SameReturnValue")
    public static OttGetQueue getInstance() {
        //enumならばこれでシングルトン制御が可能
        if (INSTANCE.field == null) {
            INSTANCE.field = SINGLETON_TEXT;
        }

        return INSTANCE;
    }

    /**
     * 実行中のOTT取得が無ければ、OTTを取得する。実行中ならば、終わるまで待つ
     *
     * @param nextTask 実行するコールバック
     */
    public void getOttAddOrExec(final Context context,
                                final DaccountGetOTT.DaccountGetOttCallBack nextTask) {
        DTVTLogger.start();
        //初回ならば各種初期化
        if (mTaskQueue == null) {
            mTaskQueue = new ArrayDeque<>();
            mDaccountGetOTT = new DaccountGetOTT();
            mTaskExecFlag = 0;
        }

        //現在時刻の方が過大ならば、実行する
        // (ただし通常は、時間経過ではなくallowNextメソッドでmTaskExecFlagをゼロにすることで動く)
        if (mTaskExecFlag + MAX_WAIT_TIME <= System.currentTimeMillis()) {
            //実行
            mDaccountGetOTT.execDaccountGetOttReal(context, nextTask);
            //現在時刻を記録して、OTTを使い終わるまでは情報をキューに蓄積させるようにする
            mTaskExecFlag = System.currentTimeMillis();
        } else {
            //情報を蓄積して、次回の実行に備える
            mTaskQueue.add(nextTask);

            //ウェイト処理を回す
            waitTask(context);
        }
        DTVTLogger.end();
    }

    /**
     * OTT取得実行待ち.
     * (基本的にはallowNextでの能動実行が行われるので、フェールセーフ処理となる)
     */
    private void waitTask(final Context context) {
        //Handlerの場合、呼び出された場所によっては例外が発生するので、threadを使用する
        Thread ottGetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DTVTLogger.start();
                try {
                    //待ち時間の分だけウェイトを入れる
                    Thread.sleep(MAX_WAIT_TIME);
                } catch (InterruptedException e) {
                    //失敗しても特に何もしない
                    DTVTLogger.debug("sleep failed");
                }
                DTVTLogger.debug("exec ott get");
                //指定時間が経過したので、OTT取得処理を実行する
                execOtt(context);
                DTVTLogger.end();
            }
        });
        //定義したスレッドを実行する
        ottGetThread.start();
    }

    /**
     * 次のOTT取得処理の実行を許可する.
     */
    public void allowNext(final Context context) {
        DTVTLogger.start();
        //OTT取得開始時刻をゼロにすることで、判定が必ずタイムアウトになるようにする
        mTaskExecFlag = 0;

        //実行すべきOTT取得がキューにたまっていた場合は実行を開始する
        execOtt(context);
        DTVTLogger.end();
    }

    /**
     * キューから情報を取得して、OTT取得処理を呼び出す
     */
    private void execOtt(final Context context) {
        DTVTLogger.start();
        DTVTLogger.debug("Queue size = " + mTaskQueue.size());
        //現在時刻の方が過大ならば、実行する
        // (ただし通常は、時間経過ではなくallowNextメソッドでmTaskExecFlagをゼロにすることで動く)
        if (mTaskExecFlag + MAX_WAIT_TIME <= System.currentTimeMillis()) {
            //OTT取得に必要なコールバックを取得する（蓄積されていなければヌルになる）
            DaccountGetOTT.DaccountGetOttCallBack nextTask = mTaskQueue.poll();

            if (nextTask != null) {
                //コールバックが取得できたので、OTT取得を呼び出す
                mDaccountGetOTT.execDaccountGetOttReal(context, nextTask);

                //現在時刻を記録して、OTTを使い終わるまでは情報をキューに蓄積させるようにする
                mTaskExecFlag = System.currentTimeMillis();
            }
        }
        DTVTLogger.end();
    }
}