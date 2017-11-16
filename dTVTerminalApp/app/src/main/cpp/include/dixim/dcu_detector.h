/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


#ifndef DCU_DETECTOR_H
#define DCU_DETECTOR_H

/**
 * @file
 *    The dcu_detector interface provides various methods for detecting of character encoding.
 */

#include <du_type.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 */
typedef enum dcu_detector_locale {
    DCU_DETECTOR_LOCALE_NONE,
    DCU_DETECTOR_LOCALE_JA,
    DCU_DETECTOR_LOCALE_KO,
} dcu_detector_locale;

/**
 *  This structure contains the information of detecting of character encoding.
 */
typedef struct dcu_detector {
    void* _impl;
} dcu_detector;

#define DCU_DETECTOR_STR_SIZE 64

/**
 *  Initializes dcu_detector.
 *  @param[out] x  pointer to the dcu_detector data structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_detector_init(dcu_detector* x);

/**
 *  Frees the region used by dcu_detector.
 *  @param[in,out] x pointer to the dcu_detector structure.
 */
extern void dcu_detector_free(dcu_detector* x);

/**
 */
extern du_bool dcu_detector_set_locale(dcu_detector* x, dcu_detector_locale locale);

/**
 */
extern du_bool dcu_detector_set_locale2(dcu_detector_locale locale);

/**
 */
extern du_bool dcu_detector_set_locale2_by_str(const du_uchar* locale);

/**
 */
extern dcu_detector_locale dcu_detector_get_locale(dcu_detector* x);

/**
 *  Resets dcu_detector.
 *  @param[in,out] x pointer to the dcu_detector structure.
 */
extern du_bool dcu_detector_reset(dcu_detector* x);

/**
 *  Feeds text to dcu_detector.
 *  @param[in,out] x  pointer to the dcu_detector data structure.
 *  @param[in] str input text.
 *  @param[in] len length of input text.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_detector_feed(dcu_detector* x, const du_uchar* str, du_uint32 len);

/**
 *  Detects character encoding specifying the dcu_detector.
 *  @param[in] x  pointer to the dcu_detector data structure.
 *  @param[out] encoding detected character encoding.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_detector_detect(dcu_detector* x, du_uchar encoding[DCU_DETECTOR_STR_SIZE]);

/**
 *  Detects character encoding specifying the text.
 *  @param[in] str input text.
 *  @param[in] len length of input text.
 *  @param[out] encoding detected character encoding.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_detector_detect2(const du_uchar* str, du_uint32 len, du_uchar encoding[DCU_DETECTOR_STR_SIZE]);

#ifdef __cplusplus
}
#endif

#endif
