/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_TYPE_H
#define DUPNP_TYPE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Converts a boolean value to the string representation.
 * @param[in] value boolean value.
 * @return "1" if value is true. Otherwise "0".
 */
extern const du_uchar* dupnp_type_boolean_to_str(du_bool value);

/**
 * Converts a string representation of boolean value to the boolean value.
 * @param[in] str_value a string representation of boolean value, "1", "0", "true", "false", "yes", "no".
 * @param[out] value the boolean value.
 * @return true if the str_value is valid string. Otherwise false.
 */
extern du_bool dupnp_type_boolean_from_str(const du_uchar* str_value, du_bool* value);

#ifdef __cplusplus
}
#endif

#endif
