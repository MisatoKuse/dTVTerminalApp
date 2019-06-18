/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_caltime interface provides methods converting a time value
 *  to/from a du_caltime structure. The du_caltime structure represents
 *  a date and time using individual members for the month, day, year, weekday,
 *  hour, minute, second.
 */

#ifndef DU_CALTIME_H
#define DU_CALTIME_H

#include <du_time_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure represents a date and time using individual members
 *  for the month, day, year, weekday, hour, minute, second.
 */
typedef struct du_caltime {
    /**
     *  Specifies the year.
     */
    du_uint32 year;

    /**
     *  Specifies the month. January = 1, February = 2, and so on.
     */
    du_uint32 month;

    /**
     *  Specifies the day of the month.
     */
    du_uint32 day;

    /**
     *  Specifies the hour.
     */
    du_uint32 hour;

    /**
     *  Specifies the minute.
     */
    du_uint32 minute;

    /**
     *  Specifies the second.
     */
    du_uint32 second;

    /**
     *  Specifies the day of the week. Sunday = 0, Monday = 1, and so on.
     */
    du_uint32 wday;
} du_caltime;

/**
 *  Converts a time t value to a du_caltime broken-down time representation
 *  expressed in Coordinated Universal Time (UTC) and stores it in the location
 *  given by caltime.
 *
 *  @param[out] caltime pointer to the du_caltime structure.
 *  @param[in] t pointer to the time value.
 *  @remarks The du_time t is represented as seconds elapsed since midnight
 *  (00:00:00), January 1, 1970, coordinated universal time (UTC).
 */
extern void du_caltime_utc_from_time(du_caltime* caltime, du_time* t);

/**
 *  Converts a time t value to a du_caltime broken-time representation,
 *  expressed relative to the user's specified time zone and stores it in
 *  the location given by caltime.
 *
 *  @param[out] caltime pointer to the du_caltime structure.
 *  @param[in] t pointer to the time value.
 *  @remarks The du_time t is represented as seconds elapsed since midnight
 *  (00:00:00), January 1, 1970, coordinated universal time (UTC).
 */
extern void du_caltime_local_from_time(du_caltime* caltime, du_time* t);

/**
 *  Converts a du_caltime broken-down time structure,
 *  expressed as Coordinated Universal Time (UTC), to a time value
 *  and stores it in the location given by  t.
 *
 *  @param[in] caltime pointer to the du_caltime structure.
 *  @param[out] t pointer to the time value.
 *  @retval 1 the function succeeds.
 *  @retval 0 the function fails.
 *  @remarks The du_time t is represented as seconds elapsed since midnight
 *  (00:00:00), January 1, 1970, coordinated universal time (UTC).
 */
extern du_bool du_caltime_utc_to_time(du_caltime* caltime, du_time* t);

/**
 *  Converts a du_caltime broken-down time structure,
 *  expressed as local time, to a time value and stores it in the location
 *  given by t.
 *
 *  @param[in] caltime pointer to the du_caltime structure.
 *  @param[out] t pointer to the time value.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @remarks The du_time t is represented as seconds elapsed since midnight
 *  (00:00:00), January 1, 1970, coordinated universal time (UTC).
 */
extern du_bool du_caltime_local_to_time(du_caltime* caltime, du_time* t);

/**
 *  Gets the offset time value (sec) to be added to the local time to get
 *  Coordinated Universal Time (UTC) and stores it in the location given by sec.
 *  The offset is positive if the local time zone is west of the Prime Meridian
 *  and negative if it is east.
 *  All translations between UTC time and local time are based
 *  on the following formula:
 *  @verbatim
 *     UTC = local time + offset
 *  @endverbatim
 *
 *  @param[out] sec pointer to the offset time value.
 *  @retval 1 the function succeeds.
 *  @retval 0 the function fails.
 */
extern du_bool du_caltime_get_offset(du_int32* sec);

#ifdef __cplusplus
}
#endif

#endif
