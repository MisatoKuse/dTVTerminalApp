/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_alloc interface provides methods for memory management
 *  ( such as allocation, free, resize of memory ).
 *
 *  To allocate memory, du_alloc() and du_alloc_zero() are used.
 *  du_alloc_zero() allocates memory and initializes it to zero.
 *  du_alloc_re() changes the size of an allocated memory.
 *  du_alloc_free() deallocates a memory that was previously allocated
 *  by a call to du_alloc() du_alloc_zero() or reallocated by a
 *  call to du_alloc_re().
 */

#ifndef DU_ALLOC_H
#define DU_ALLOC_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  An interface definition of a handler for allocating memory.
 *
 *  du_alloc_alloc_handler is an application-defined callback function for
 *  allocating memory area called in du_alloc().
 *
 *  @param[in] size memory size to allocate.
 *  @return  a pointer to the allocated memory area.
 *           NULL if the request fails.
 *  @see du_alloc_set_alloc_handler()
 */
typedef void* (*du_alloc_alloc_handler)(du_uint32 size);

/**
 *  An interface definition of a handler for freeing memory.
 *
 *  du_alloc_free_handler is an application-defined callback function for
 *  freeing memory area called in du_alloc_free().
 *
 *  @param[in] a a pointer of the memory area to free.
 *  @see du_alloc_set_free_handler()
 */
typedef void (*du_alloc_free_handler)(void* a);

/**
 *  Allocates memory area.
 *
 *  @param[in] size memory size to allocate.
 *  @return a pointer to the allocated memory if function succeeds.
 *          NULL if function fails.
 *  @post A pointer to the allocated memory must be freed by du_alloc_free().
 */
extern void* du_alloc(du_uint32 size);

/**
 *  Allocates memory area and initializes it to zero.
 *
 *  @param[in] size memory size to allocate.
 *  @return a pointer to the allocated memory if function succeeds.
 *          NULL if function fails.
 *  @post A pointer to the allocated memory must be freed by du_alloc_free().
 */
extern void* du_alloc_zero(du_uint32 size);

/**
 *  Changes the size of the memory area.
 *
 *  Changes the size of the memory area pointeed to by @p a to @p n bytes.
 *  The contents will be unchanged to the @p m bytes,
 *  newly allocated memory will not be initialized.
 *
 *  @param[in] a pointer of the memory block to resize.
 *  @param[in] m byte size to preserve value.
 *  @param[in] n new byte size .
 *  @return a pointer to the reallocated memory.
 *          NULL if the request fails.
 *  @pre @p a must be allocated by du_alloc(), du_alloc_zero() or du_alloc_re().
 *  @post A pointer to the allocated memory must be freed by du_alloc_free().
 */
extern void* du_alloc_re(void* a, du_uint32 m, du_uint32 n);

/**
 *  Frees the memory area.
 *
 *  If @p a is NULL, no operation is performed.
 *
 *  @param[in] a a pointer of the memory area to free.
 *  @pre @p a must be allocated by du_alloc(), du_alloc_zero() or du_alloc_re().
 */
extern void du_alloc_free(void* a);

/**
 *  Registers a function for allocating memory area.
 *
 *  The registered function is called in du_alloc()
 *  for allocationg memory area.
 *
 *  @param[in] handler the function for allocationg memory area.
 *  @remark If du_alloc_alloc_handler is not registered, malloc() is used
 *          for allocating memory.
 *  @see du_alloc_alloc_handler
 */
extern void du_alloc_set_alloc_handler(du_alloc_alloc_handler handler);

/**
 *  Registers a function for freeing memory area.
 *
 *  The registered function is called in du_alloc_free()
 *  for freeing memory area.
 *
 *  @param[in] handler the function for freeing memory area.
 *  @remark If du_alloc_free_handler is not registered, free() is used
 *          for freeing memory.
 *  @see du_alloc_free_handler
 */
extern void du_alloc_set_free_handler(du_alloc_free_handler handler);

#ifdef __cplusplus
}
#endif

#endif
