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
interface IDimServiceAppService {

    /**
     * サービス確認要求.
     *
     * @param appReqId アプリ要求ID.コールバック時にここで指定された値を引き渡します.
     * @param serviceKey サービス識別キー.確認対象サービスのサービス識別キー.
     * @param callback コールバックサービス.チェックが完了するとここで指定された
     * コールバックインタフェースのonCompleteCheckServiceが呼び出されます.
     * @return 要求受付結果.
     * @throws android.os.RemoteException
     */
    int checkService(int appReqId, String serviceKey, IDimServiceAppCallbacks callback);

    /**
     * サービス登録要求(通知先設定なし).
     *
     * @param appReqId アプリ要求ID.コールバック時にここで指定された値を引き渡します.
     * @param serviceKey サービス識別キー.登録対象サービスのサービス識別キー.
     * @param callback コールバックサービス.チェックが完了するとここで指定された
     * コールバックインタフェースのonCompleteRegistServiceが呼び出されます.
     * @return 要求受付結果.
     * @throws android.os.RemoteException
     */
    int registService(int appReqId, String serviceKey, IDimServiceAppCallbacks callback);

    /**
     * 認証トークン払い出し要求.
     *
     * @param appReqId アプリ要求ID.コールバック時にここで指定された値を引き渡します.
     * @param id ID.トークン払い出し対象のID.
     * @param serviceKey サービス識別キー.認証トークンを利用するサービスのサービス識別キー.
     * @param option 再認証オプション.再認証動作を指定するパラメータ.
     * @param appCheckKey アプリチェックキー.2回目以降のトークン払い出し要求に付与する.
     * @param callback コールバックサービス.チェックが完了するとここで指定された
     * コールバックインタフェースのonCompleteGetAuthTokenが呼び出されます.
     * @return 要求受付結果.
     * @throws android.os.RemoteException
     */
    int getAuthToken(int appReqId, String id, String serviceKey, int option, String appCheckKey, IDimServiceAppCallbacks callback);

    /**
     * 個別ID状態問い合わせ.
     *
     * @param appReqId アプリ要求ID.コールバック時にここで指定された値を引き渡します.
     * @param id ID.問い合わせ対象のID.
     * @param serviceKey サービス識別キー.問い合わせ元サービスのサービス識別キー.
     * @param tokenStatusCode token認証ステータス詳細コード.
     * @param callback コールバックサービス.チェックが完了するとここで指定された
     * コールバックインタフェースのonCompleteGetIdStatusが呼び出されます.
     * @return 要求受付結果.
     * @throws android.os.RemoteException
     */
    int getIdStatus(int appReqId, String id, String serviceKey, String tokenStatusCode, IDimServiceAppCallbacks callback);

    /**
     * ワンタイムトークン払い出し要求.
     *
     * @param appReqId アプリ要求ID.コールバック時にここで指定された値を引き渡します.
     * @param id ID.ワンタイムトークン払い出し対象のID.
     * @param serviceKey サービス識別キー.ワンタイムトークンを利用するサービスのサービス識別キー.
     * @param option 再認証オプション.再認証動作を指定するパラメータ.
     * @param appCheckKey アプリチェックキー.2回目以降のトークン払い出し要求に付与する.
     * @param callback コールバックサービス.チェックが完了するとここで指定された
     * コールバックインタフェースのonCompleteGetOneTimePasswordが呼び出されます.
     * @return 要求受付結果.
     * @throws android.os.RemoteException
     */
    int getOneTimePassword(int appReqId, String id, String serviceKey, int option, String appCheckKey, IDimServiceAppCallbacks callback);

    /**
     * サービスアプリ向けID管理アップデート要求.
     *
     * @param protocolVer 依頼元サービスが認識しているID管理アプリのプロトコルバージョン.
     * @param serviceKey サービス識別キー.ID管理アプリのアップデートを要求するサービスのサービス識別キー.
     * @throws android.os.RemoteException
     */
    void updateIdManager(String protocolVer, String serviceKey);

}
