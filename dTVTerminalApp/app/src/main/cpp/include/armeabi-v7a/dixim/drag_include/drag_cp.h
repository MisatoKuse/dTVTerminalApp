/*
 * Copyright (c) 2012 DigiOn, Inc. All rights reserved.
 */
/**
 * @file drag_cp.h
 * @brief dragCPの機能に関するAPIを提供する。
 */
#ifndef DRAG_CP_H
#define DRAG_CP_H

#include <du_uchar.h>
#include <du_uchar_array.h>
#include <drag_dms_info_array.h>
#include <drag_error.h>
#include "drag_cp_conn_status.h"
#include "drag_nat_type.h"

#ifdef __cplusplus
extern "C" {
#endif

/** dragCPの接続ステータス */
typedef enum drag_cp_connect_status {
	/** 不明 */
    DRAG_CP_CONNECT_STATUS_UNKNOWN = 0,
	/** 接続準備環境 */
    DRAG_CP_CONNECT_STATUS_READY,
	/** 接続中 */
    DRAG_CP_CONNECT_STATUS_CONNECTED,
	/** 切断 */
    DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION,
	/** 再接続失敗 */
    DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION,
} drag_cp_connect_status;

typedef enum {
  /** 正常終了 */
  DRAG_CP_AUTH_INFO_SUCCESS = 0,
  /** 間もなく無償利用期間が終了 */
  DRAG_CP_AUTH_INFO_WILL_BE_EXPIRED = 0x0001,
  /** HW_IDが不正 */
  DRAG_CP_AUTH_INFO_INVALID_HWID = 0x1001,
  /** サービスIDが不正 */
  DRAG_CP_AUTH_INFO_INVALID_SERVICEID = 0x1002,
  /** クレデンシャルIDが不正 */
  DRAG_CP_AUTH_INFO_INVALID_CREDENTIAL = 0x1003,
  /** 署名データが不正 */
  DRAG_CP_AUTH_INFO_INVALID_SIGN = 0x1004,
  /** デバイスアクティベーション クライアント証明書不正 */
  DRAG_CP_AUTH_INFO_INVALID_CERT_A = 0x1005,
  /** デバイスアクティベーション クライアント証明書
      セキュリティーバージョン不整合(要クライアントモジュールアップデート) */
  DRAG_CP_AUTH_INFO_INVALID_CERT_A_VERSION = 0x1006,
  /** デバイス証明書不正 */
  DRAG_CP_AUTH_INFO_INVALID_CERT_D = 0x1007,
  /** サービスチケット不正 */
  DRAG_CP_AUTH_INFO_INVALID_TICKET = 0x1008,
  /** SIPサーバのサーバ証明書不正 */
  DRAG_CP_AUTH_INFO_INVALID_CERT_SIP_SERVER = 0x1009,
  /** その他、リクエスト不正 */
  DRAG_CP_AUTH_INFO_INVALID_REQUEST = 0x10ff,
  /** ベンダーに対する利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_VENDOR = 0x1101,
  /** 製品に対する利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_PRODUCT = 0x1102,
  /** デバイスに対する利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_DEVICE = 0x1103,
  /** デバイス証明書に対する利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_CERT = 0x1104,
  /** 製品に紐付くサービスに対する利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_SERVICE_P = 0x1105,
  /** デバイスに紐付くサービスに対する利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_SERVICE_D = 0x1106,
  /** 外部IDに紐付くサービスに対する利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_SERVICE_ID = 0x1107,
  /** デバイスに紐付くサービス証明書の利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_SERVICE_CERT_D = 0x1108,
  /** 外部IDに紐付くサービス証明書の利用停止 */
  DRAG_CP_AUTH_INFO_PROHIBITED_SERVICE_CERT_ID = 0x1109,
  /** デバイス証明書配信回数制限オーバー */
  DRAG_CP_AUTH_INFO_EXCEEDED_CERT_D = 0x1201,
  /** サービスチケット配信回数制限オーバー(外部ID未登録) */
  DRAG_CP_AUTH_INFO_EXCEEDED_TICKET_1 = 0x1202,
  /** サービスチケット配信回数制限オーバー(外部ID登録済) */
  DRAG_CP_AUTH_INFO_EXCEEDED_TICKET_2 = 0x1203,
  /** サービスチケット再取得の指示 */
  DRAG_CP_AUTH_INFO_REGET_TICKET = 0x1301,
  /** クレデンシャルID再取得の指示 */
  DRAG_CP_AUTH_INFO_REGET_CREDENTIAL = 0x1302,
  /** 利用登録手続きの指示(ユーザID登録手続きなど) */
  DRAG_CP_AUTH_INFO_INSTRUCT_REG_PROC = 0x1303,
  /** 利用情報更新手続きの指示(課金手続きなど) */
  DRAG_CP_AUTH_INFO_INSTRUCT_UPD_PROC = 0x1304,
  /** サーバ内部エラー */
  DRAG_CP_AUTH_INFO_ERROR = 0xffff,
} drag_cp_auth_info_status_t;

/**
 * DMPに接続状態を通知するためのハンドラー
 * @param status dragCPの接続ステータス
 * @param arg 任意に設定できる引数
 */
typedef void (*drag_cp_connect_status_handler)(drag_cp_connect_status status, void* arg);

/**
 * ratun のログ設定を行う。drag_cp_start の前に呼び出す必要がある。
 * @param path ratunログのパス
 * @param level ratunログレベル
 */
extern void drag_cp_ratun_log_init_config(const du_uchar* path, int level);

/**
 * DMPに認証時付加情報を通知するためのハンドラー
 * @param user drag_cp_set_auth_info_handlerにて指定されたユーザデータ。
 * @param status 認証サーバコード
 * @param url URL
 * @param msg 認証時付加情報メッセージ
 * @param date 有効期限(サーバから返却された場合、その他の場合NULL)
 */
typedef void (*drag_cp_auth_info_handler)(void* user, drag_cp_auth_info_status_t status, const char* url, const char* msg, const char* date);

/**
 * DMP認証時付加情報通知のためのハンドラを設定する。drag_cp_start の前に呼び出す必要がある。
 * @param handler DMPにメッセージ通知するためのハンドラー
 * @param user 任意に設定できる引数。handler呼出し時、第一引数にて渡される。
 */
extern void drag_cp_set_auth_info_handler(drag_cp_auth_info_handler handler, void* user);

/**
 * @note call this function before calling drag_cp_initialize
 */
/**
 *  interface for setting dipas cert/ticket loaders
 */
typedef du_bool (*drag_cp_cert_loader_t)(char* cert, int* certlen, char* pass, int* passlen);
typedef du_bool (*drag_cp_ticket_loader_t)(char* ticket, int* s);
extern void drag_cp_set_dipas_license_loaders(const drag_cp_cert_loader_t cl, drag_cp_ticket_loader_t tl);

/**
 * initialize dirag base system
 * @param path dragCPのコンフィグファイルへのパス
 */
extern du_bool drag_cp_initialize(const du_uchar* path);

/**
 * start dirag LR sub-system, call this function after drag_cp_initialize
 * @return TRUE if successfull, otherwise FALSE
 */
extern du_bool drag_cp_lrsys_start(void);

/**
 * start dirag remote access system, call this function after drag_cp_initialize
 * @param handler DMPに状態通知するためのハンドラー
 * @param arg drag_cp_connect_status_handlerに設定する引数
 */
extern du_bool drag_cp_rasys_start(drag_cp_connect_status_handler handler, void* arg);

/**
 * dragCPを開始する
 * @param path dragCPのコンフィグファイルへのパス
 * @param handler DMPに状態通知するためのハンドラー
 * @param arg drag_cp_connect_status_handlerに設定する引数
 * @return 1 成功 0 失敗
 * @note deprecated, use drag_cp_initialize and drag_cp_rasys_start instead.
 */
extern du_bool drag_cp_start(const du_uchar* path, drag_cp_connect_status_handler handler, void* arg);

/**
 * finalize dirag base system
 * @param none
 */
extern void drag_cp_finalize(void);

/**
 * stopping dirag LR sub-system, call this function before calling drag_cp_finalize
 * @param none
 */
extern void drag_cp_lrsys_stop(void);

/**
 * stopping dirag remote access system, call this function before calling drag_cp_finalize
 * @param none
 */
extern void drag_cp_rasys_stop(void);

/**
 * dragCPを終了する。
 * @note deprecated, use drag_cp_finalize and drag_cp_rasys_stop instead.
 */
extern void drag_cp_stop(void);

/**
 * dragCPが取得した証明書のシリアルナンバーを取得する。
 * @param serial_mumber シリアルナンバー
 * @param version DLPA version of the serial number. set 1 or 2.
 * @return 1 succeeded 0 failed
 */
extern du_bool drag_cp_service_init(du_uchar_array* serial_number, du_uint32 version);

/**
 * prepare registration
 * @param none
 * @return 1 succeeded 0 failed
 */
extern du_bool drag_cp_prepare_registration(void);

#ifdef WIN32
extern void drag_cp_service_init_release_serial_number(du_uchar_array* serial_number);
#endif

/**
 *
 */
extern void drag_cp_service_free(void);

/**
 * get the host name using to access to the internet.
 * @return the host name.
 */
extern const char* drag_cp_get_local_host_name(void);

/**
 * ローカルレジストされたDMSをリストから削除する。
 * @param udn 削除するDMSのUDN
 * @return 1 成功 0 失敗
 */
extern du_bool drag_cp_delete_dms(const du_uchar* udn);

/**
 * ローカルレジストされたDMSのリストを取得する。
 * @param array DMSのリスト
 * @return 1 成功 0 失敗
 */
extern du_bool drag_cp_get_dms_list(dms_info_array* array);

/**
 * ローカルレジストされたDMSに接続する。
 * @param udn 接続するDMSのUDN
 * @return 1 成功 0 失敗
 */
extern du_bool drag_cp_connect_to_dms(const du_uchar* udn);

/**
 * 接続されているDMSから切断する。
 * @param udn 切断するDMSのUDN
 * @return 1 成功 0 失敗
 */
extern du_bool drag_cp_disconnect_from_dms(const du_uchar* udn);

/**
 * get the code of last error occured in the drag.
 * @return zero if error did not occured, or the code of error occured at last.
 */
extern drag_error_code drag_cp_get_last_error(void);

/**
 * Get status about connection.
 * @param server Buffer to store the status about connection to server
                 about SIP. Set NULL to omit getting the status.
                 Refer drag_d_server_conn_status for detail.
 * @param peer Buffer to store the status about connection to the peer.
               Set NULL to omit getting the status.
               Refer drag_d_peer_conn_status for detail.
 * @param nat_type Buffer to store the type of 'network address translation.'
                   Set NULL to omit getting the NAT type.
                   Refer drag_nat_type for detail.
 * @return 1 succeeded 0 failed
 */
extern du_bool drag_cp_get_conn_status(drag_cp_server_conn_status* server, drag_cp_peer_conn_status* peer, drag_nat_type* nat_type);

/**
 * check whether nat-type is re-usable or not
 * @param dirag error code
 * @return 1 need to be rechecked, 0 re-usable
 */
extern du_bool drag_cp_recheck_nat_type(drag_error_code e);

/**
 * Force nat type check result to skip the check.
 * It will depend on the given value whether if dirag tunnel uses IGD or TURN.
 * To get same situation with previous or another running,
 * set gotten nat type value by get_conn_status in that one.
 * @param force Flag if force the result and skip the check.
 * @param type Forced result value. It will be depended to use IGD and TURN.
 *             Flags in value other than nat type will be ignored.
 * @return 1 succeeded 0 failed
 */
extern du_bool drag_cp_force_nat_type(du_bool force, drag_nat_type type);

/**
 * get wakeup information for the tareget DMS from diras
 * @param[in] udn udn of the target DMS
 * @param[out] addr GIP of the target DMS(network byte oder)
 * @param[out] port array of target ports(host byte order)
 * @param[out] mac array of target macs
 * @param[in] arrays_in should be set # of the input arrays (min(#ports, #macs))
 * @param[out] arrays_out # of the arrays returned
 * @return TRUE if successfull, otherwise FALSE
 * @note use same index for port,mac pair (port[0], mac[0]), ... (port[n], mac[n]) ... 
 */
extern du_bool drag_cp_get_wakeup_info(const unsigned char* udn,
				       struct in_addr* addr, unsigned short port[], unsigned char mac[][6],
				       const unsigned int arrays_in, unsigned int* arrays_out);

/**
 * getting expired time in the service ticket
 * @param[in] time pointer of the expired time to be returned
 * @return 1 if successfully getting the value, otherwise 0
 */
extern du_bool drag_cp_service_ticket_get_expired_time(du_int64* time);

/**
 * check if service ticket is available or not
 * @param[in] available pointer of the validity
 * @return 1 if successfully getting the value, otherwise 0
 */
extern du_bool drag_cp_service_ticket_is_available(du_bool* available);

/**
 * reset the service ticket
 * @param[in] force if force is set to 1, force remove and proceeds re-acquisition.
 *                  otherwise, check its expired time and  proceeds re-acquisition if required.
 * @return 1 if successfull, otherwise 0
 */
extern du_bool drag_cp_service_ticket_reset_expired(du_bool force);

/**
 * get ra white lists from the service.
 * @param[in,out] pointer to the uchar-array in which the list will be stored
 * @return 1 if successfull, otherwise 0
 */
extern du_bool drag_cp_get_ra_white_lists(du_uchar_array* ua);

/**
 * check if having valid drag certifications
 * @param none
 * @return 1 if having valid certifications, otherwise 0
 */
extern du_bool drag_cp_has_valid_v2_certs(void);

#ifdef WIN32
extern void drag_cp_release_ra_white_lists(du_uchar_array* serial_number);
#endif

/**
 * get version number string of dirag pc sdk.
 * @return pointer to the string
 */
extern const du_uchar* drag_cp_get_version_string(void);

#ifdef __cplusplus
}
#endif

#endif
