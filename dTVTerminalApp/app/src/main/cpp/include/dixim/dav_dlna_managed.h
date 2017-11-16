/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_DLNA_MANAGED_H
#define DAV_DLNA_MANAGED_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DAV_DLNA_MANAGED_SIZE 9

/**
 * flags of DLNA Managed.
 */
typedef enum {
    DAV_DLNA_MANAGED_FLAG_UNKNOWN = -1,
    DAV_DLNA_MANAGED_FLAG_OCM_UPLOAD_CONTENT = 0, // support for OCM: upload content.
    DAV_DLNA_MANAGED_FLAG_OCM_CREATE_CHILD_CONTAINER, // support for OCM: create child container.
    DAV_DLNA_MANAGED_FLAG_OCM_DESTROY_OBJECT, // support for OCM: destroy object.
    DAV_DLNA_MANAGED_FLAG_OCM_UPLOAD_CONTENT_WITH_OCM_DESTROY_ITEM_OPERATION_CAPABILITY, // support for OCM: upload content with OCM: destroy item operation capability.
    DAV_DLNA_MANAGED_FLAG_OCM_CHANGE_METADATA, // support for OCM: change metadata.
} dav_dlna_managed_flag;

/**
 * Initializes a <em>dlna_managed</em>.
 * @param[out] dlna_managed pointer to the du_uchar data to store DLNA Managed flags.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remarks <em>dlna_managed</em> size is 9 byte (dlna_managed = 8 hexdigit + null terminator).
 * This function stores "00000000" in <em>dlna_managed</em>.
 */
extern void dav_dlna_managed_init(du_uchar dlna_managed[DAV_DLNA_MANAGED_SIZE]);

/**
 * Sets the specified flag's value to true or false.
 * @param[in,out] dlna_managed pointer to the du_uchar data stored DLNA Managed flags.
 * @param[in] flag DLNA Managed flag.
 * @param[in] enabled true if specified flag's value set to true, otherwise set to false.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_dlna_managed_set_flag(du_uchar dlna_managed[DAV_DLNA_MANAGED_SIZE], dav_dlna_managed_flag flag, du_bool enable);

/**
 * Gets the specified <em>flag</em> flag value.
 * @param[in] dlna_managed pointer to the du_uchar data stored DLNA Managed flags.
 * @param[in] flag DLNA Managed flag.
 * @param[out] enabled true if specified flag's value is true, otherwise false.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_dlna_managed_get_flag(const du_uchar dlna_managed[DAV_DLNA_MANAGED_SIZE], dav_dlna_managed_flag flag, du_bool* enabled);

#ifdef __cplusplus
}
#endif

#endif
