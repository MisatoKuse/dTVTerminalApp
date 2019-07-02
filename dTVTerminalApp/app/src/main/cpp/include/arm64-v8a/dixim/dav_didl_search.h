/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_search.h
 *  @brief The dav_didl_search interface provides methods for searching
 *  dav_didl_object for an occurrence of a specified search criteria.
 */

#ifndef DAV_DIDL_SEARCH_H
#define DAV_DIDL_SEARCH_H

#include <du_type.h>
#include <du_ptr_array.h>

#include <dav_didl_object.h>
#include <dav_didl_search_syntax_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * This structure contains the information of a search criteria.
 */
typedef struct dav_didl_search_criteria {
    dav_didl_search_syntax_array _syntax_list;
} dav_didl_search_criteria;

/**
 * Initializes a dav_didl_search_criteria data structure.
 * @param[out] x  pointer to the dav_didl_search_criteria data structure.
 * @return  true if the function succeeds.
 *          false if the  function fails.
 */
extern du_bool dav_didl_search_criteria_init(dav_didl_search_criteria* x);

/**
 * Frees the region used by <em>x</em>.
 * @param[in] x pointer to the dav_didl_search_criteria structure.
 * @return  true if the function succeeds.
 *          false if the  function fails.
 */
extern du_bool dav_didl_search_criteria_free(dav_didl_search_criteria* x);

/**
 * Parses a search criteria string <em>string</em> and stores the result in <em>x</em>.
 * If a parse error occurred, it returns false. Otherwise it returns true.
 * @param[out] x pointer to the dav_didl_search_criteria structure.
 * @param[in] string a string of the search criteria.
 * @return  false if a parse error occurred, otherwise true.
 * @remark <em>x</em> is a pointer to a dav_didl_search_criteria stricture initialized by
 * the <b>dav_didl_search_criteria_init</b> function.
 */
extern du_bool dav_didl_search_criteria_parse(dav_didl_search_criteria* x, const du_uchar* string);

/**
 * Searches dav_didl_object <em>obj</em> for an occurrence of
 * the search criteria specified by the dav_didl_search_criteria <em>x</em>.
 * @param[in] x pointer to the dav_didl_search_criteria structure specified the search criteria.
 * @param[in] obj pointer to the dav_didl_object to search for.
 * @return true if <em>obj</em> matches the <em>x</em> search criteria, otherwise, false.
 */
extern du_bool dav_didl_search_criteria_match(dav_didl_search_criteria* x, dav_didl_object* obj);

/**
 * Make search criteria string <em>string</em> from dav_didl_search_criteria structure.
 * @param[in] x pointer to the dav_didl_search_criteria structure specified the search criteria.
 * @param[out] string destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the  function fails.
 * @remark <em>x</em> is a pointer to a dav_didl_search_criteria stricture parsed by
 * the <b>dav_didl_search_criteria_parse</b> function.
 */
extern du_bool dav_didl_search_criteria_make_string(dav_didl_search_criteria* x, du_uchar_array* string);

#ifdef __cplusplus
}
#endif

#endif
