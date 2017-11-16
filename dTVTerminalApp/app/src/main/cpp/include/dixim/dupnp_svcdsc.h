/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_svcdsc.h
 *  @brief dupnp_svcdsc interface provides methods for manipulating service
 *  description information ( such as parsing service description XML document).
 */

#ifndef DUPNP_SVCDSC_H
#define DUPNP_SVCDSC_H

#include <dupnp_svcdsc_state_variable_array.h>
#include <du_type.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * dupnp_svcdsc structure contains the action names and state variable information
 * given by service description.
 * @see dupnp_svcdsc_state_variable.h
 */
typedef struct dupnp_svcdsc {
    du_str_array action_name_array;  /**< list of action name given by service description. */
    dupnp_svcdsc_state_variable_array state_variable_array; /**< state variable information given by service description.  */
} dupnp_svcdsc;

/**
 * Creates and initializes <em>x</em>.
 * @param[out] x  pointer to the <em>dupnp_svcdsc</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_svcdsc_init(dupnp_svcdsc* x);

/**
 * Frees the region used by <em>x</em>.
 * @param[in,out] x pointer to the <em>dupnp_svcdsc</em> structure.
 */
extern void dupnp_svcdsc_free(dupnp_svcdsc* x);

/**
 * Resets the region used by <em>x</em>.
 * @param[in,out] x pointer to the <em>dupnp_svcdsc</em> structure.
 */
extern void dupnp_svcdsc_reset(dupnp_svcdsc* x);

/**
 * Parses an <em>xml</em> service description XML document and stores
 * the content in <em>x</em>.
 * @param[in,out] x pointer to a destination dupnp_svcdsc structure data.
 * @param[in] xml a string containing the document to parse.
 * @param[in] xml_len length of xml.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>x</em> is a pointer to a <em>dupnp_svcdsc</em> initialized by
 * the dupnp_svcdsc_init() function.
 * <em>x</em> remains until it is freed by dupnp_svcdsc_free().
 */
extern du_bool dupnp_svcdsc_parse(dupnp_svcdsc* x, const du_uchar* xml, du_uint32 xml_len);


/**
 * Parses a service description XML document in <em>xml_path</em> file and
 * stores the content in <em>x</em>.
 * @param[in,out] x pointer to a destination dupnp_svcdsc structure data.
 * @param[in] xml_path a path of file that containes the document to parse.
 * @return  Parsing successfully, it returns true. Otherwise it returns false.
 * @remark <em>x</em> is a pointer to a <em>dupnp_svcdsc</em> initialized by
 * the dupnp_svcdsc_init() function.
 * <em>x</em> remains until it is freed by dupnp_svcdsc_free().
 */
extern du_bool dupnp_svcdsc_parse_file(dupnp_svcdsc* x, const du_uchar* xml_path);

#ifdef __cplusplus
}
#endif

#endif
