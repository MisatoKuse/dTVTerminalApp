/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dav_content_features.h
 *  @brief The dav_content_features interface provides some methods for manipulating
 *  dav_content_features structure data (such as parsing, getting parameter string).
 *  For more information about content features , see DLNA Guideline 2006 Oct <br>
 *  section 7.4.26 MT HTTP Header: contentFeatures.dlna.org.
 */

#ifndef DAV_CONTENT_FEATURES_H
#define DAV_CONTENT_FEATURES_H

#include <du_type.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * This structure contains the information of content feature.
 */
typedef struct dav_content_features {
    du_str_array _param_array;
} dav_content_features;

/**
 * Initializes a dav_content_features structure area.
 * @param[out] x  pointer to the dav_content_features structure.
 */
extern void dav_content_features_init(dav_content_features* x);

/**
 * Frees the region of <em>x</em>.
 * @param[out] x  pointer to the dav_content_features structure.
 */
extern void dav_content_features_free(dav_content_features* x);

/**
 * Reset all information stored in <em>x</em>.
 * @param[in] x  pointer to the dav_content_features structure.
 */
extern void dav_content_features_reset(dav_content_features* x);

/**
 * Parses <em>content_features</em> string and stores the information in <em>x</em>.
 * @param[out] x pointer to dav_content_features structure.
 * @param[in] content_features content features string.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_content_features_parse(dav_content_features* x, const du_uchar* content_features);

/**
 * Gets the string array of content feature parameters stored in <em>x</em>.
 * @param[out] x pointer to dav_content_features structure.
 * @return  pointer to the string array of content feature parameters.
 */
extern du_str_array* dav_content_features_get_param_array(dav_content_features* x);

/**
 * Returns contentFeatures pn_param name string.
 * This string is "DLNA.ORG_PN".
 * @return  "DLNA.ORG_PN" string.
 */
extern const du_uchar* dav_content_features_name_pn_param(void);

/**
 * Returns contentFeatures pn_param name string.
 * This string is "DLNA.ORG_OP".
 * @return  "DLNA.ORG_OP" string.
 */
extern const du_uchar* dav_content_features_name_op_param(void);

/**
 * Returns contentFeatures ps_param name string.
 * This string is "DLNA.ORG_PS".
 * @return  "DLNA.ORG_PS" string.
 */
extern const du_uchar* dav_content_features_name_ps_param(void);

/**
 * Returns contentFeatures ci_param name string.
 * This string is "DLNA.ORG_CI".
 * @return  "DLNA.ORG_CI" string.
 */
extern const du_uchar* dav_content_features_name_ci_param(void);

/**
 * Returns contentFeatures flags_param name string.
 * This string is "DLNA.ORG_FLAGS".
 * @return  "DLNA.ORG_FLAGS" string.
 */
extern const du_uchar* dav_content_features_name_flags_param(void);

/**
 * Returns contentFeatures maxsp_param name string.
 * This string is "DLNA.ORG_MAXSP".
 * @return  "DLNA.ORG_MAXSP" string.
 */
extern const du_uchar* dav_content_features_name_maxsp_param(void);

#ifdef __cplusplus
}
#endif

#endif
