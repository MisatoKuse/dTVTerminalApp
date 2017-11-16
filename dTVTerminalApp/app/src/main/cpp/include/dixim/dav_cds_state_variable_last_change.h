/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dav_cds_state_variable_last_change.h
 *  @brief The dav_cds_state_variable_last_change provides structure for LastChange state variable of
 *   ContentDirectory. This structure provides LastChange state variable of ContentDirectory.
 *  @see  ContentDirectory:3 Service Template Version 1.01 For UPnP. Version 1.0 section 2.5
 */

#ifndef DAV_CDS_STATE_VARIABLE_LAST_CHANGE_H
#define DAV_CDS_STATE_VARIABLE_LAST_CHANGE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Type of LastChange event.
  */
typedef enum dav_cds_state_variable_last_change_type {
    DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_UNKNOWN,    //!< Unknown
    DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_ADD,        //!< Indicates that an object was added to the CDS within the most recent event moderation period.
    DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_MOD,        //!< Indicates that an existing object was modified within the most recent event moderation period.
    DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_DEL,        //!< Indicates that an object was deleted from the CDS within the most recent event moderation period.
    DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_DONE,       //!< Indicates that a sub-tree update operation has completed within the most recent event moderation period.
} dav_cds_state_variable_last_change_type;

/**
  * This structure contains attributes of LastChange.
  */
typedef struct dav_cds_state_variable_last_change {
    dav_cds_state_variable_last_change_type type;       //!< Type of LastChange event

    du_uchar* obj_id;                                   //!< Contains the @id property of the object that was added/modified/deleted.
    du_uint32 update_id;                                //!< Contains the value of the SystemUpdateID state variable that resulted when the object was added/modified/deleted.

    du_bool st_update;                                  //!< Indicates whether or not the object was added/modified/deleted as part of a subtree update operation.
                                                        //!< This attribute is disabled if the type is DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_DONE.

    du_uchar* obj_parent_id;                            //!< Contains the @id property of the parent container to which this object was added.
                                                        //!< This attribute is enabled if the type is DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_ADD.
    du_uchar* obj_class;                                //!< Contains the value of the upnp:class property of the object was added.
                                                        //!< This attribute is enabled if the type is DAV_CDS_STATE_VARIABLE_LAST_CHANGE_TYPE_ADD.
} dav_cds_state_variable_last_change;

extern du_bool dav_cds_state_variable_last_change_init(dav_cds_state_variable_last_change* x);

extern void dav_cds_state_variable_last_change_free(dav_cds_state_variable_last_change* x);

extern du_bool dav_cds_state_variable_last_change_set_add(dav_cds_state_variable_last_change* x, const du_uchar* obj_id, du_uint32 update_id, du_bool st_done, const du_uchar* obj_parent_id, const du_uchar* obj_class);

extern du_bool dav_cds_state_variable_last_change_set_mod(dav_cds_state_variable_last_change* x, const du_uchar* obj_id, du_uint32 update_id, du_bool st_done);

extern du_bool dav_cds_state_variable_last_change_set_del(dav_cds_state_variable_last_change* x, const du_uchar* obj_id, du_uint32 update_id, du_bool st_done);

extern du_bool dav_cds_state_variable_last_change_set_done(dav_cds_state_variable_last_change* x, const du_uchar* obj_id, du_uint32 update_id);

#ifdef __cplusplus
}
#endif

#endif
