/*
 * Copyright (c) 2013 DigiOn, Inc. All rights reserved.
 */
/**
 * @file drag_error.h
 * @brief error codes occur in drag.
 */
#ifndef DRAG_ERROR_H
#define DRAG_ERROR_H

#include <du_type.h>
#include <ratun_manager_error.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_uint32 drag_error_code;

enum {
  /** no error occured */
  DRAG_ERROR_NONE = 0,
  /** origin for LR related errors, this value should not returned. */
  DRAG_ERROR_LR_BASE = 3000,
  //< general LR errors
  DRAG_ERROR_LR_GENERAL,
  //< request from v1 client but dms has tuner
  DRAG_ERROR_LR_V1_REQUEST_AND_HAS_TUNER,
  //< request from v2 but exceeds limits
  DRAG_ERROR_LR_V2_EXCEEDS_LIMITS,
  //< we can't find target device on local network
  DRAG_ERROR_LR_DEVICE_NOT_FOUND,
  //< NTP error
  DRAG_ERROR_LR_NTP_GET_TIME,
  //< setup connection
  DRAG_ERROR_LR_SETUP_CONN,
  //< setup ssl connection - certification invalid etc
  DRAG_ERROR_LR_SETUP_SSL_CONN,
  /** origin for interface related errors, this value should not returned. */
  DRAG_ERROR_BASE = 4000,
  /** unspecified error */
  DRAG_ERROR_UNSPECIFIED_INTERNAL,
  /** command is not acceptable situation */
  DRAG_ERROR_TIMING_NOT_READY,
  /** command is not acceptable situation */
  DRAG_ERROR_TEMPORARY_UNEXECUTABLE,
};

#ifdef __cplusplus
}
#endif

#endif

