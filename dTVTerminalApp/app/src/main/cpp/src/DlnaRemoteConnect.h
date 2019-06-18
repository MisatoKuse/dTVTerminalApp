//
//  DlnaRemoteConnect.h
//  dTVTerminal
//

#ifndef DlnaRemoteConnect_h
#define DlnaRemoteConnect_h

#include <stdio.h>
#include <functional>
#include "DlnaDefine.h"
#include "LocalRegistration/local_registration.h"

class DlnaRemoteConnect {
public:
    DlnaRemoteConnect();
    ~DlnaRemoteConnect();
    // callback
public:
    static std::function<void(eDiragConnectStatus status, du_uint32 errorCode)> DiragConnectStatusChangeCallback;
    static std::function<void(bool result, int resultType, const du_uchar* errorCode)> LocalRegistrationCallback;
    static std::function<void(ddtcp_ret ddtcpSinkAkeEndRet)> DdtcpSinkAkeEndCallback;

    /** NW環境変更時にコール*/
    bool restartDirag(DMP *d);

    bool startDirag(DMP *d);
    void stopDirag();

    bool requestLocalRegistration(DMP *d, const du_uchar* udn, const du_uchar* registerName);
    const char* getRemoteDeviceExpireDate(const du_uchar* udn, char *date, int length);
    void connectRemote(const du_uchar* udn);
    void disconnectRemote(const du_uchar* udn);
    void finalizeDirag();
private:
    const du_uchar* dmp_get_dms_type(void);
    const du_uchar* dmp_get_user_agent(void);

#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_NONE 0
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_SOCKET 1
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_DEVICE_OVER 2
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_SOAP 3
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_HTTP 4
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_OTHER 5
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_REQUEST 6
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_DTCP_OTHER 7
#define LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_DTCP_DEVICE_OVER 8

#define SOAP_ERROR_DEVICE_OVER_1 "801"
#define SOAP_ERROR_DEVICE_OVER_2 "803"

#define LOCAL_REGISTRATION_REQUEST_ERROR_UDN_NULL "99997"
#define LOCAL_REGISTRATION_REQUEST_ERROR_DEVICE_NAME_NULL "99998"
#define LOCAL_REGISTRATION_ERROR_OTHER "99999"

};

#endif /* DlnaRemoteConnect_h */
