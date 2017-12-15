/**
 * Copyright (C) 2013-2014 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.idmanager;

import com.nttdocomo.android.idmanager.IDimServiceAppCallbacks;

/**
 * サービスアプリ向け共通ID管理アプリ外部インタフェース.
 *
 * 共通ID管理アプリがサービスアプリ向けに公開している外部インタフェースです.
 * 全てのインタフェースは非同期で実行され、呼出し後、サービスアプリに即座に処理を返却します.
 * この時、各メソッドの戻り値が処理中を表す場合、共通ID管理アプリは非同期で各処理を実施しています.
 * 非同期で実行された処理が完了すると、各インタフェースのcallback引数に指定されたコールバックインタフェースの所定のメソッドを呼び出します.
 */
interface IDimServiceAppServiceCustom {

    /**
     * サービス登録要求(通知先設定あり).
     *
     * @param appReqId アプリ要求ID.コールバック時にここで指定された値を引き渡します.
     * @param serviceKey サービス識別キー.登録対象サービスのサービス識別キー.
     * @param setDefIdReceiver デフォルトID設定通知を受信するComponent名.
     * @param userAuthReceiver ユーザ認証通知を受信するComponent名.
     * @param deleteIdReceiver ID解除通知を受信するComponent名.
     * @param invalidateIdReceiver ID認証状態無効化通知を受信するComponent名.
     * @param callback コールバックサービス.チェックが完了するとここで指定された
     * コールバックインタフェースのonCompleteRegistServiceが呼び出されます.
     * @return 要求受付結果.
     * @throws android.os.RemoteException
     */
    int registServiceWithReceiver(int appReqId, String serviceKey, String setDefIdReceiver, String userAuthReceiver, String deleteIdReceiver, String invalidateIdReceiver, IDimServiceAppCallbacks callback);

}
