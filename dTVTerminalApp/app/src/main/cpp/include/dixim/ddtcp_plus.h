/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: $ 
 */ 

/** @file ddtcp.h
 *  @brief The ddtcp interface provides the high-level interface used by an application to integrate with DTCP-IP sink and source device functions.<br>
 *  @see ddtcp_plus_source.h ddtcp_plus_sink.h
 */

#ifndef DDTCP_PLUS_H
#define DDTCP_PLUS_H

#include <ddtcp.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Enables Remote Access functions for source device.
 * @param[out] dtcp handle to DTCP-IP instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */ 
extern ddtcp_ret ddtcp_enable_remote_access_source(ddtcp dtcp);

#ifdef __cplusplus
}
#endif

#endif
