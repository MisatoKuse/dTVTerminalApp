/*
 * Copyright (c) 2014 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVCDSC_SOFTWARE_CODE_PARSER_H
#define DUPNP_DVCDSC_SOFTWARE_CODE_PARSER_H

#include <dupnp_impl.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum dupnp_dvcdsc_software_code_state {
    DUPNP_DVCDSC_SOFTWARE_CODE_STATE_UNKNOWN,
    DUPNP_DVCDSC_SOFTWARE_CODE_STATE_SOFTWARE_CODE_NOT_FOUND,
    DUPNP_DVCDSC_SOFTWARE_CODE_STATE_VALID_SOFTWARE_CODE_FOUND,
    DUPNP_DVCDSC_SOFTWARE_CODE_STATE_INVALID_SOFTWARE_CODE_FOUND,
} dupnp_dvcdsc_software_code_state;

typedef struct dupnp_dvcdsc_software_code {
    dupnp_dvcdsc_software_code_state state;
    du_str_array software_code_array;
} dupnp_dvcdsc_software_code;

extern du_bool dupnp_dvcdsc_software_code_init(dupnp_dvcdsc_software_code* sc);

extern void dupnp_dvcdsc_software_code_free(dupnp_dvcdsc_software_code* sc);

extern du_bool dupnp_dvcdsc_software_code_calc_sum(const du_str_array* header, const du_uchar* udn, const du_uchar* software_code, du_uchar_array* sum);

extern du_bool dupnp_dvcdsc_software_code_parser_parse(const du_uchar* xml, du_uint32 bytes, const du_str_array* response_header, dupnp_dvcdsc_software_code* sc);

#ifdef __cplusplus
}
#endif

#endif
