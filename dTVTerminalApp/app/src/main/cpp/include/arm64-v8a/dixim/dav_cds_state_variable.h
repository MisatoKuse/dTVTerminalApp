/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dav_cds_state_variable.h
 *  @brief The dav_cds_state_variable interface provides methods for parsing value of state variable of
 *   ContentDirectory. This interface provides methods for parsing value of state variable of ContentDirectory.
 *  @see  ContentDirectory:3 Service Template Version 1.01 For UPnP. Version 1.0 section 2.5
 */

#include <dav_cds_state_variable_last_change_array.h>

#ifndef DAV_CDS_STATE_VARIABLE_H
#define DAV_CDS_STATE_VARIABLE_H

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Parses a LastChange state variable value, stores each element of LastChange to <em>arr</em>.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the state variable value.
 * @param[in] xml_len length of xml.
 * @param[out] arr pointer to the destination dav_cds_state_variable_last_change_array structure
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>arr</em> is a pointer to a <em>dav_cds_state_variable_last_change_array</em> initialized by
 * the <b>dav_cds_state_variable_last_change_array_init</b> function.
 */
extern du_bool dav_cds_state_variable_parse_last_change(const du_uchar* xml, du_uint32 xml_len, dav_cds_state_variable_last_change_array* arr);

#ifdef __cplusplus
}
#endif

#endif
