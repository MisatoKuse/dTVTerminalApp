//
//  DlnaRemoteConnect.h
//  dTVTerminal
//

#ifndef DlnaRemoteConnect_h
#define DlnaRemoteConnect_h

#include <stdio.h>
#include <functional>
#include "DlnaDefine.h"

class DlnaRemoteConnect {
public:
    DlnaRemoteConnect();
    ~DlnaRemoteConnect();
    
    // callback
public:
    static std::function<void(eDiragConnectStatus status)> DiragConnectStatusChangeCallback;
    static std::function<void(bool result, eLocalRegistrationResultType resultType)> LocalRegistrationCallback;
    static std::function<void(ddtcp_ret ddtcpSinkAkeEndRet)> DdtcpSinkAkeEndCallback;

    /** NW環境変更時にコール*/
    bool restartDirag(DMP *d);

    bool startDirag(DMP *d);
    bool stopDirag();

    bool requestLocalRegistration(DMP *d, const du_uchar* udn, const du_uchar* registerName);
    
    const char* getRemoteDeviceExpireDate(const du_uchar* udn);
    
    void connectRemote(const du_uchar* udn);
    void finalizeDirag();
    
private:
    const du_uchar* dmp_get_dms_type(void);
    const du_uchar* dmp_get_user_agent(void);
};

#endif /* DlnaRemoteConnect_h */
