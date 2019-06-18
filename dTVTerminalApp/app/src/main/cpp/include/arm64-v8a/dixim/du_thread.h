/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_thread interface provides methods for thread management
 *   ( creates and controls a thread, sets/gets its priority).
 */

#ifndef DU_THREAD_H
#define DU_THREAD_H

#include <du_type.h>
#include <du_thread_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Enumeration of thread priorities.
 */
typedef enum {
    /**
     *   Never used.
     */
    DU_THREAD_PRIORITY_UNKNOWN,

    /**
     *   Time critical priority.
     */
    DU_THREAD_PRIORITY_CRITICAL,

    /**
     *   Highest priority.
     */
    DU_THREAD_PRIORITY_HIGHEST,

    /**
     *   High priority.
     */
    DU_THREAD_PRIORITY_HIGH,

    /**
     *   Normal priority.
     */
    DU_THREAD_PRIORITY_NORMAL,

    /**
     *   Low priority.
     */
    DU_THREAD_PRIORITY_LOW,

    /**
     *   Lowest priority.
     */
    DU_THREAD_PRIORITY_LOWEST,

    /**
     *   IDLE priority.
     */
    DU_THREAD_PRIORITY_IDLE,

} du_thread_priority;


/**
 *  Initializes a du_thread data area.
 *  @param[out] thread  pointer to the du_thread structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_init(du_thread* thread);

/**
 *  Creates a new thread.
 *  The new thread applies the function start_routine passing
 *  it arg as argument.
 *  @param[out] thread pointer to a du_thread data.
 *  @param[in] stack_size stack size for new thread or 0. If stack_size is 0,
 *  default stack size will be used.
 *  @param[in] start_routine pointer to a routine that begins execution of new thread.
 *  @param[in] arg pointer to argument to be passed to start_routine or NULL.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_create(du_thread* thread, du_uint32 stack_size, void (*start_routine)(void*), void* arg);

/**
 *  Sets the priority value for the specified thread.
 *  @param[in] thread pointer to a du_thread data.
 *  @param[in] priority priority value for the thread.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_set_priority(du_thread* thread, du_thread_priority priority);

/**
 *  Sets the priority value for the specified thread.
 *  @param[in] thread pointer to a du_thread data.
 *  @param[in] priority priority value for the thread.
 *  The priority levels range from -10 (lowest priority) to 10 (highest priority).
 *  Zero proirity level means normal (default) priority level.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_set_priority_numrical(du_thread* thread, du_int32 priority);

/**
 *  Gets the priority value of the specified thread.
 *  @param[in] thread pointer to a du_thread data.
 *  @param[in] priority pointer to the variable to receive a priority value for the thread.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_get_priority(du_thread* thread, du_thread_priority* priority);

/**
 *  Puts a running thread thread in the detached state.
 *  This guarantees that the memory resources consumed by thread will be
 *  freed immediately when thread terminates.
 *  @param[in] thread pointer to a du_thread data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_detach(du_thread* thread);

/**
 *  Waits for termination of another thread.
 *  <b>du_thread_join</b> suspends the execution of the calling thread
 *  until the thread identified by thread terminates.
 *  @param[in] thread pointer to a du_thread data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_join(du_thread* thread);

/**
 *  Suspends the execution of the current thread for at least the specified interval.
 *  @param[in] msec minimum time interval for which execution is to be suspended,
 *   in milliseconds.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_thread_sleep(du_uint32 msec);

/**
 *  Gets thread structure of the calling thread.
 *  @return  du_thread data of calling thread.
 */
extern du_thread du_thread_self(void);

/**
 *  Yield the processor.
 */
extern void du_thread_yield(void);

/**
 *  Checkes whether the system thread is NPTL.
 *  @return  true if the system thread is NPTL, othewise false.
 *  @remark This API always returns false except on Linux.
 */
extern du_bool du_thread_is_nptl(void);

#ifdef __cplusplus
}
#endif

#endif
