#ifndef DTCP_HPP
#define DTCP_HPP

#include <string>
#include <du_log.h>
#include <dupnp.h>
#include <ddtcp.h>
#include <ddtcp_source.h>
#include <ddtcp_private.h>
#include "DlnaDownload.h"
#include "../DTVTLogger.h"

#ifdef DTCP_PLUS
#include <ddtcp_plus.h>
#include <ddtcp_plus_source.h>
#endif

namespace dixim {
    namespace dmsp {
        namespace dtcp {

            static const du_uchar* LOG_CATEGORY = DU_UCHAR_CONST("dtcp");

            struct dtcp {
                ddtcp d;
                ddtcp_source_listen listen;
#ifdef DTCP_PLUS
                ddtcp_source_listen listen_ra;
#endif

                du_uint16 ake_port;
#ifdef DTCP_PLUS
                du_uint16 ake_ra_port;
#endif
                std::string private_data_home;

                du_bool running;

                ddtcp_ret last_error_code;

                dtcp() : listen(0), running(0), last_error_code(DDTCP_RET_SUCCESS) {
                    ddtcp_ret ret;

                    ret = ddtcp_create_dtcp(&d);
                    if (DDTCP_FAILED(ret)) { du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("Failed to create dtcp: ret=%x."), ret); goto error; }
                    ret = ddtcp_set_source_connection_timeout(d, 1000*10);
                    if (DDTCP_FAILED(ret)) { du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("Failed to set listen max ake connection: ret=%x."), ret); goto error2; }

#ifdef DTCP_PLUS
                    ret = ddtcp_enable_remote_access_source(d);
        if (DDTCP_FAILED(ret)) { du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("Failed to enable remote access: ret=%x."), ret); goto error2; }
#endif

                    ret = ddtcp_set_ecc_timing_back();

                    return;

                    error2:
                    ddtcp_destroy_dtcp(&d);
                    error:
                    throw 0;
                }

                typedef struct {
                    void* private_data_home;
                    void* vm;
                    void* obj;
                    void* mac_address_method_id;
                } dixim_hwif_private_data_io;

                du_bool start(JavaVM *vm, jobject instance, dtvt::DlnaDownload *myDlnaClass) {
                    DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start enter");
                    JNIEnv *env = NULL;
                    int status = vm->GetEnv((void **) &env, JNI_VERSION_1_6);
                    if (status < 0) {
                        status = vm->AttachCurrentThread(&env, NULL);
                        if (status < 0 || NULL == env) {
                            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, status < 0 || NULL == env");
                            return false;
                        }
                    }

                    jobject objTmp = env->NewGlobalRef(instance);
                    jclass clazz = env->GetObjectClass(objTmp);
                    jmethodID mid = env->GetMethodID(clazz, "getUniqueId",
                                                     "()Ljava/lang/String;");
                    if (env->ExceptionCheck()) {
                        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, env->ExceptionCheck");
                        return false;
                    }

                    jstring strObj = (jstring) env->CallObjectMethod(objTmp,
                                                                     mid);
                    if (env->ExceptionCheck()) {
                        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, jstring strObj");
                        return false;
                    }

                    void *private_data;
                    dixim_hwif_private_data_io hwif;
                    hwif.private_data_home = (void *) private_data_home.c_str();
                    hwif.vm = vm;
                    //hwif.obj=(void*)myDlnaClass;
                    hwif.obj = objTmp;
                    hwif.mac_address_method_id = mid;
                    private_data = &hwif;

                    ddtcp_ret ret = DDTCP_RET_SUCCESS;

                    du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("+++ start"));

                    if (running) {
                        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start, running");
                        return 1;
                    }

                    //ret = ddtcp_set_additional_param(d, DDTCP_ADDITINAL_PARAM_TYPE_PRIVATE_DATA_IO, (void*)private_data_home.c_str());
                    ret = ddtcp_set_additional_param(d, DDTCP_ADDITINAL_PARAM_TYPE_PRIVATE_DATA_IO,
                                                     (void *) private_data);
                    if (DDTCP_FAILED(ret)) {
                        du_log_ev(LOG_CATEGORY,
                                  DU_UCHAR_CONST("Failed to set additional param: ret=%x."), ret);
                        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, ddtcp_set_additional_param");
                        goto error;
                    }

                    ret = ddtcp_startup(d);
                    if (DDTCP_FAILED(ret)) {
                        du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("Failed to startup: ret=%x."), ret);
                        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, ddtcp_startup");
                        goto error;
                    }

                    ret = ddtcp_source_listen_ake(d, du_ip_str_any4(), ake_port, &listen);
                    if (DDTCP_FAILED(ret)) {
                        du_log_ev(LOG_CATEGORY,
                                  DU_UCHAR_CONST("Failed to source listen ake: ret=%x."), ret);
                        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, ddtcp_source_listen_ake");
                        goto error2;
                    }

#ifdef DTCP_PLUS
                    ret = ddtcp_source_listen_ra_ake(d, du_ip_str_any4(), ake_ra_port, 0, 0, 0, 0, &listen_ra);
        if (DDTCP_FAILED(ret)) {
            du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("Failed to source listen ra ake: ret=%x."), ret);
            goto error3;
        }
#endif

                    running = 1;
                    du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("--- start"));
                    DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start exit true");
                    return 1;

#ifdef DTCP_PLUS
                    ddtcp_source_close_listen_ake(&listen_ra);
    error3:
#endif
                    ddtcp_source_close_listen_ake(&listen);
                    error2:
                    ddtcp_shutdown(d);
                    error:
                    last_error_code = ret;
                    du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("!!! start"));
                    DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start exit false");
                    return 0;
                }

                void stop() {
                    DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.stop enter");
                    du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("+++ stop"));

                    if (!running) return;

#ifdef DTCP_PLUS
                    ddtcp_source_close_listen_ake(&listen_ra);
#endif
                    ddtcp_source_close_listen_ake(&listen);
                    ddtcp_shutdown(d);
                    running = 0;

                    du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("--- stop"));
                    DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.stop exit");
                }

                ~dtcp() {
                    ddtcp_destroy_dtcp(&d);
                }
            };

        }   // dtcp
    }   // dmsp
}   // dixim

#endif
