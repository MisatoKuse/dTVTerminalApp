/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_svcdsc_state_variable.h
 *  @brief The dupnp_svcdsc_state_variable defines dupnp_svcdsc_state_variable data structure.
 *  @see dupnp_svcdsc.h
 */

#ifndef DUPNP_SVCDSC_STATE_VARIABLE_H
#define DUPNP_SVCDSC_STATE_VARIABLE_H

#include <du_type.h>
#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * dupnp_svcdsc_state_variable structure contains the state variable information
 * given by StateVariable of service description.
 * @remark Following member is optional variable, and set to zero if
 *  the information is not specified in StateVariable of service description.<br>
 *   default_value, minimum, maximum, step .
 */
typedef struct dupnp_svcdsc_state_variable {
    const du_uchar* name; /**< name of state variable. */
    du_bool send_events;  /**< defines whether event messages will be generated when
    the value of this state variable changes. */
    const du_uchar* data_type; /**< data type of this state variable. */
    const du_uchar* default_value; /**< initial value of this state variable. */

    du_bool data_type_is_string; /**< true if data type is string. false otherwise.  */

    /**
     * This structure defines the list of legal string values or bounds for legal numeric values
     *  of StateVariable of service description.<br>
     * If data type is string (data_type_is_string is true), allowed_value is available,
     * otherwise allowed_value_range is available.
     */
    union {
       /**
        * allowed_value structure defines list of legal string values.
        */
        struct {
            du_uint32 count;  /**< number of string values in list. */
            const du_uchar** list; /**< list of legal string values. */
        } allowed_value;

       /**
        * allowed_value_range structure defines bounds for numeric values and
        *   resolution for numeric values.
        */
        struct {
            const du_uchar* minimum; /**< inclusive lower bound. */
            const du_uchar* maximum; /**< inclusive upper bound. */
            const du_uchar* step;    /**< size of an increment operation. */
        } allowed_value_range;
    };

    du_uchar_array _ua;
    du_str_array _sa;
    du_uchar* _buf;
} dupnp_svcdsc_state_variable;

extern du_bool dupnp_svcdsc_state_variable_init(dupnp_svcdsc_state_variable* x);

extern void dupnp_svcdsc_state_variable_free(dupnp_svcdsc_state_variable* x);

extern du_bool dupnp_svcdsc_state_variable_pack(dupnp_svcdsc_state_variable* x);

extern du_bool dupnp_svcdsc_state_variable_set_name(dupnp_svcdsc_state_variable* x, const du_uchar* name);

extern void dupnp_svcdsc_state_variable_set_send_events(dupnp_svcdsc_state_variable* x, const du_uchar* send_events);

extern du_bool dupnp_svcdsc_state_variable_set_data_type(dupnp_svcdsc_state_variable* x, const du_uchar* data_type);

extern du_bool dupnp_svcdsc_state_variable_set_default_value(dupnp_svcdsc_state_variable* x, const du_uchar* default_value);

extern du_bool dupnp_svcdsc_state_variable_set_minimum(dupnp_svcdsc_state_variable* x, const du_uchar* minimum);

extern du_bool dupnp_svcdsc_state_variable_set_maximum(dupnp_svcdsc_state_variable* x, const du_uchar* maximum);

extern du_bool dupnp_svcdsc_state_variable_set_step(dupnp_svcdsc_state_variable* x, const du_uchar* step);

extern du_bool dupnp_svcdsc_state_variable_set_allowed_value(dupnp_svcdsc_state_variable* x, const du_uchar* allowed_value);

#ifdef __cplusplus
}
#endif

#endif
