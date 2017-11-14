/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_mutex interface provides methods for a mutex
 *   (such as create, free, lock, unlock).
 *   a mutex is a synchronization object that allows one thread mutually exclusive
 *   access to a resource. Mutexes are useful when only one thread at a time can be
 *   allowed to modify data or some other controlled resource.
 *   To use a du_mutex, use <b>du_mutex_create</b> to create the du_mutex when it is needed.
 *   Deleting the created du_mutex, use <b>du_mutex_free</b>.
 */

#ifndef DU_MUTEX_H
#define DU_MUTEX_H

#include "du_type.h"
#include "du_mutex_os.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Creates a new du_mutex.
 *  @param mutex a pointer of the du_mutex structure to create.
 *  @return  true if the request is done successfully.
 *           false if the request fails.
 */
extern du_bool du_mutex_create(du_mutex* mutex);

/**
 *  Destroys a mutex object, freeing the resources it might hold.
 *  @param mutex a pointer of the du_mutex structure to destroy.
 *  @return  true if the request is done successfully.
 *           false if the request fails.
 */
extern du_bool du_mutex_free(du_mutex* mutex);

/**
 *  Locks the given du_mutex. If the du_mutex is currently unlocked,
 *  it becomes locked and owned by the calling thread, and <b>du_mutex_lock</b> returns immediately.
 *  If the du_mutex is already locked by another thread, <b>du_mutex_lock</b> suspends the calling
 *  thread until the du_mutex is unlocked.
 *  @param mutex a pointer of the du_mutex structure.
 *  @return  true if the request is done successfully.
 *           false if the request fails.
 */
extern du_bool du_mutex_lock(du_mutex* mutex);

/**
 *  Trys to lock the given du_mutex. <b>du_mutex_trylock</b> behaves
 *  identically to <b>du_mutex_lock</b>, except that it does not block
 *  the calling thread if the du_mutex is already locked by another thread.
 *  @param mutex a pointer of the du_mutex structure.
 *  @return  true if the request is done successfully.
 *           false if the request fails or if the du_mutex is already locked by another thread.
 */
extern du_bool du_mutex_trylock(du_mutex* mutex);

/**
 *  Unlocks the given du_mutex. The du_mutex is assumed to be locked and owned
 *  by the calling thread.
 *  @param mutex a pointer of the du_mutex structure.
 *  @return  true if the request is done successfully.
 *           false if the request fails.
 */
extern du_bool du_mutex_unlock(du_mutex* mutex);

#ifdef __cplusplus
}
#endif

#endif
