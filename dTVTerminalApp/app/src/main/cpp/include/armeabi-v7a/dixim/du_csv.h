/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_csv interface provides methods for array of csv(Comma Separated Values : UPnP style)
 *  data manipulation ( such as make, split, append ).
 *  <b>du_csv_cat</b> appends string to a csv data.
 *  At the end of appending operations by using of <b>du_csv_cat</b>, call
 *  <b>du_csv_end</b> function.
 */

#ifndef DU_CSV_H
#define DU_CSV_H

#include <du_type.h>
#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Appends string data value to a destination ca array,
 *  allocating more space if necessary.
 *  @param[in,out] ca pointer to the destination du_uchar_array structure.
 *  @param[in] value a string to be appended to ca.
 *  @return  true if, and only if, all data of ca are appended successfully.
 */
extern du_bool du_csv_cat(du_uchar_array* ca, const du_uchar* value);

/**
 *  Ends the appending mode of ca.
 *  This function is called after an application uses the <b>du_csv_cat</b> function.
 *  @param[in] ca pointer to the du_uchar_array structure.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 */
extern du_bool du_csv_end(du_uchar_array* ca);

/**
 *  Splits the UPnP style csv string into substrings in a du_str_array array value_array.
 *  @param[in] csv the UPnP style csv format string.
 *  @param[out] value_array pointer to the du_str_array structure to store the substrings.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @remark value_array is a pointer to a du_str_array initialized by
 *  the <b>du_str_array_init</b> function.
 */
extern du_bool du_csv_split(const du_uchar* csv, du_str_array* value_array);

/**
 *  Concatenates a comma character between each element of a value_array,
 *  yielding a single concatenated the UPnP style csv format string csv.
 *  @param[in] value_array pointer to the value array of substrings.
 *  @param[out] csv the concatenated UPnP style csv format string.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @remark csv is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_csv_make(const du_str_array* value_array, du_uchar_array* csv);

/**
 *  Concatenates a comma character between each element of a value_array,
 *  yielding a single concatenated the UPnP style csv format string csv.
 *  @param[in] value_array pointer to the value array of substrings.
 *  @param[in] len the length of array.
 *  @param[out] csv the concatenated UPnP style csv format string.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @remark csv is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_csv_make2(const du_uchar** value_array, du_uint32 len, du_uchar_array* csv);

/**
 *  Returns a pointer to the last substring in csv string.
 *  @param[in] csv the UPnP style csv format string.
 *  @return
 *    a pointer to the last substring in in csv.
 */
extern const du_uchar* du_csv_get_last_value(const du_uchar* csv);

/**
 *  Removes the last substring in the UPnP style csv format string csv.
 *  @param[in,out] csv the csv format string.
 */
extern void du_csv_remove_last_value(du_uchar* csv);

#ifdef __cplusplus
}
#endif

#endif
