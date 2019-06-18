/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dmrd_cms_stub.h
 *  @brief The dmsd_cms interface provides some methods for CMS
 * (such as getting query interface).
 * @see http://www.upnp.org/standardizeddcps/documents/ConnectionManager1.0.pdf
 */

#ifndef DMRD_CMS_STUB_H
#define DMRD_CMS_STUB_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Gets the pointer to a query interface function for CMS.
 * @return pointer to a query interface function for CMS.
 */
extern dupnp_dvc_service_query_interface dmrd_cms_stub_get_query_interface(void);

#ifdef __cplusplus
}
#endif

#endif
