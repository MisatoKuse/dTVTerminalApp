/**
 * Copyright (C) 2013-2014 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.idmanager;

/**
 * サービスアプリ向け共通ID管理アプリ外部コールバックインタフェース.
 *
 * 共通ID管理アプリが処理結果を通知する為にサービスアプリ側に実装が必要なインタフェースです.
 * 共通ID管理アプリは処理が完了すると、各処理に対応したここで定義するインタフェースを呼び出してサービスアプリに処理の完了と結果を通知します.
 */
interface IDimServiceAppCallbacks {

    /**
     * サービス確認完了.
     *
     * @param appReqId アプリ要求ID.checkService呼び出し時に指定した値が設定されます.
     * @param result 処理結果.
     * @param version アプリバージョン.
     * @param protocolVersion プロトコルバージョン.
     * @throws android.os.RemoteException
     */
    void onCompleteCheckService(int appReqId, int result, String version, String protocolVersion);

    /**
     * サービス登録完了.
     *
     * @param appReqId アプリ要求ID.registService呼び出し時に指定した値が設定されます.
     * @param result 処理結果.
     * @throws android.os.RemoteException
     */
    void onCompleteRegistService(int appReqId, int result);

    /**
     * 認証トークン払い出し完了.
     *
     * @param appReqId アプリ要求ID.getAuthToken呼び出し時に指定した値が設定されます.
     * @param result 処理結果.
     * @param id ID.トークン払い出し対象のID.
     * @param token 認証トークン.サービスサーバに向けて送信するための認証トークン.
     * @param appCheckKey アプリチェックキー.入力にアプリチェックキーがない場合に返却.
     * @throws android.os.RemoteException
     */
    void onCompleteGetAuthToken(int appReqId, int result, String id, String token, String appCheckKey);

    /**
     * 個別ID状態問い合わせ完了.
     *
     * @param appReqId アプリ要求ID.getIdStatus呼び出し時に指定した値が設定されます.
     * @param result 処理結果.
     * @param id 問い合わせ対象のID.
     * @param isDefault idがデフォルトIDとして機能する場合はtrue、それ以外はfalse.
     * @param hasMsn msnにMSN情報が設定されている場合はtrue、それ以外はfalse.
     * @param authStatus idの認証状態が有効である場合はtrue、無効である場合はfalse.
     * @throws android.os.RemoteException
     */
    void onCompleteGetIdStatus(int appReqId, int result, String id, boolean isDefault, boolean hasMsn, boolean authStatus);

    /**
     * ワンタイムトークン払い出し完了.
     *
     * @param appReqId アプリ要求ID.getOneTimePassword呼び出し時に指定した値が設定されます.
     * @param result 処理結果.
     * @param id ワンタイムトークン払い出し対象のID.
     * @param oneTimePassword ワンタイムトークン.
     * @param appCheckKey アプリチェックキー.入力にアプリチェックキーがない場合に返却.
     * @throws android.os.RemoteException
     */
    void onCompleteGetOneTimePassword(int appReqId, int result, String id, String oneTimePassword, String appCheckKey);

}
