/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dmrd_avt_stub.h
 *  @brief The dmrd_avt_stub interface provides some methods for AVT
 * (such as getting query interface).
 * @see http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf
 */

#ifndef DMRD_AVT_STUB_H
#define DMRD_AVT_STUB_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Gets the pointer to a query interface function for AVT.
 * @return pointer to a query interface function for AVT.
 */
extern dupnp_dvc_service_query_interface dmrd_avt_stub_get_query_interface(void);

/**
 * Appends instance id to AVT.
 * @return  true if, and only if, the instance id is appended successfully.
 */
extern du_bool dmrd_avt_stub_add_instance_id(du_uint32 id);

#ifdef __cplusplus
}
#endif

#endif

