/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *    The dmedia_detector interface provides some methods for monitoring file system events.
 *   @remark Note that kernel should support the inotify feature on linux.
 *           inotify is implemented in the kernel above 2.6.13.
 */

#ifndef DMEDIA_DETECTOR_H
#define DMEDIA_DETECTOR_H

#include "du_type.h"
#include "du_thread.h"
#include "du_str_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains the information of the media detector.
 */
typedef struct dmedia_detector dmedia_detector;

/**
 *  An interface definition of a handler.
 *  <b>dmedia_detector_event_handler</b> function is an application-defined
 *    callback function when the media detector is detected filesystem changes.
 *  @param[in] path root path which changed own path or any the descendant pathes.
 *  @param[in] arg a parameter for the handler function.
 */
typedef void (*dmedia_detector_event_handler)(const du_uchar* path, void* arg);

/**
 *  Creates and initializes dmedia_detector.
 *  @param[out] x pointer to the dmedia_detector data structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dmedia_detector_init(dmedia_detector* x);

/**
 *  Frees the region used by dmedia_detector.
 *  @param[in,out] x pointer to the dmedia_detector structure.
 */
extern void dmedia_detector_free(dmedia_detector* x);

/**
 *  Starts asynchronously monitoring some events on file system until dmedia_detector_stop() is called.
 *  After calling this API, a thread is internally generated for monitoring events.
 *  If you want monitor the events manually(synchronously), you can must call dmedia_detector_wait() instead of this API.
 *  @param[in,out] x pointer to the dmedia_detector data structure.
 *  @param[in] stack_size the size of the stack used in dmedia_detector in byte.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark Don't call dmedia_detector_wait() after calling this API.
 *  if stack_size equals zero, uses the default stack size given by OS.
 */
extern du_bool dmedia_detector_start(dmedia_detector* x, du_uint32 stack_size);

/**
 *  Stops monitoring some events on file system.
 *  The thread generated by calling dmedia_detector_start() is destructed.
 *  @param[in,out] x pointer to the dmedia_detector data structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark You have to call this API only after calling dmedia_detector_start().
 */
extern du_bool dmedia_detector_stop(dmedia_detector* x);

/**
 *  Gets pointer to du_thread.
 *  @param[in] x  pointer to the dmedia_detector data structure.
 *  @return  pointer to the du_thread stored in x.
 *  @remark You can call this API after calling dmedia_detector_start() and before calling dmedia_detector_stop() only.
 */
extern du_thread dmedia_detector_get_thread(dmedia_detector* x);

/**
 *  Sets the maximum rate of eventing.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @param[in] maximum_rate_ms maximum rate of eventing in milliseconds.
 */
extern void dmedia_detector_set_maximum_rate(dmedia_detector* x, du_uint32 maximum_rate_ms);

/**
 *  Gets the maximum rate of eventing.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @return maximum rate of eventing in milliseconds.
 */
extern du_uint32 dmedia_detector_get_maximum_rate(dmedia_detector* x);

/**
 *  Registers a <b>dmedia_detector_event_handler</b> function in dmedia_detector.
 *  The <b>dmedia_detector_event_handler</b> function is an application-defined
 *    callback function invoked when the media detector is detected filesystem changes.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @param[in] handler the <b>dmedia_detector_event_handler</b> function to store in upnp.
 *  @param[in] arg pointer to the user defined data area.
 */
extern void dmedia_detector_set_event_handler(dmedia_detector* x, dmedia_detector_event_handler handler, void* arg);

/**
 *  Waits for some events on file system.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @param[in] timeout_ms the length of time in milliseconds which
 *    <b>dmedia_detector_wait</b> will wait for events before returning.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dmedia_detector_wait(dmedia_detector* x, du_uint32 timeout_ms);

/**
 *  Cancels waiting for some events on file system.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dmedia_detector_cancel(dmedia_detector* x);

/**
 *  Appends a specified path to monitoring path list of media detector.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @param[in] path path to append.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark  When descendant or ancestor of specified path exists in path list of media detector,
 *            you can't append the path.
 */
extern du_bool dmedia_detector_add_path(dmedia_detector* x, const du_uchar* path);

/**
 *  Removes a specified path from monitoring path list of media detector.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @param[in] path path to remove.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dmedia_detector_remove_path(dmedia_detector* x, const du_uchar* path);

/**
 *  Gets monitoring path list of media detector.
 *  @param[in] x pointer to the dmedia_detector structure.
 *  @param[out] path_array monitoring path list.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dmedia_detector_get_path_list(dmedia_detector* x, du_str_array* path_array);

#ifdef __cplusplus
}
#endif

#include "dmedia_detector_os.h"

#endif
