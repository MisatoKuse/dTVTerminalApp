/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_signal interface provides a method for signal handling.
 */

#ifndef DU_SIGNAL_H
#define DU_SIGNAL_H

#ifdef __cplusplus
extern "C" {
#endif

#include "du_signal_os.h"
#include "du_type.h"

/**
 *  Signal handler.
 */
typedef void (*du_signal_handler)(du_signal s);

/**
 *  Sets an action taken by a process on receipt of a specific signal.
 *  @param[in] s signal
 *  @param[in] handler the action to be associated with s.
 */
extern void du_signal_catch(du_signal s, du_signal_handler handler);

/**
 *  Ignores a signal when it is delivered to the process.
 *  @param[in] s signal
 */
extern void du_signal_ignore(du_signal s);

/**
 *  Uncatches a signal when it is delivered to the process.
 *  @param[in] s signal
 */
extern void du_signal_uncatch(du_signal s);

/**
 *  Sends a signal to the calling process.
 *  @param[in] s signal
 */
extern du_bool du_signal_raise(du_signal s);

/**
 *  Returns an ID of alarm signal.
 */
extern du_signal du_signal_id_alarm(void);

/**
 *  Returns an ID of child terminated signal.
 */
extern du_signal du_signal_id_child(void);

/**
 *  Returns an ID of continue signal.
 */
extern du_signal du_signal_id_cont(void);

/**
 *  Returns an ID of hangup signal.
 */
extern du_signal du_signal_id_hangup(void);

/**
 *  Returns an ID of interrupt signal.
 */
extern du_signal du_signal_id_int(void);

/**
 *  Returns an ID of broken pipe signal.
 */
extern du_signal du_signal_id_pipe(void);

/**
 *  Returns an ID of termination signal.
 */
extern du_signal du_signal_id_term(void);

#ifdef __cplusplus
}
#endif

#endif
