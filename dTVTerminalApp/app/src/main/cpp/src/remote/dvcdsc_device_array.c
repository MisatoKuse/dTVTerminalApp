/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <du_limits.h>
#include <du_str.h>
#include <dupnp_urn.h>
#include "dvcdsc_device_array.h"

#define LAST_POSITION DU_UINT32_MAX

static du_bool free_objects(dvcdsc_device_array* x, du_uint32 start_pos, du_uint32 end_pos) {
    du_uint32 i;
    du_uint32 len;
    dvcdsc_device* p;

    len = dvcdsc_device_array_length(x);

    if (len == 0 || len == start_pos) return 1;
    if (end_pos == LAST_POSITION) end_pos = len - 1;
    if (len <= end_pos || end_pos < start_pos) return 0;

    p = dvcdsc_device_array_get(x) + start_pos;
    for (i = start_pos; i <= end_pos; ++i) {
        dvcdsc_device_free(p++);
    }

    return 1;
}

typedef struct criteria {
    const du_uchar* udn;
    const du_uchar* device_type;
} criteria;

static int udn_comparator(const void* criteria_, const void* dd, const void* comp_arg) {
    const criteria* c = criteria_;
    const dvcdsc_device* d = dd;

    if (!du_str_equal(c->udn, d->udn)) return 1;
    if (!dupnp_urn_version_le(c->device_type, d->device_type)) return 1;
    return 0;
}

du_uint32 dvcdsc_device_array_find_by_udn_and_device_type(const dvcdsc_device_array* x, const du_uchar* udn, const du_uchar* device_type) {
    criteria c = { udn, device_type };

    return du_array_find2(x, &c, udn_comparator, 0);
}

du_bool dvcdsc_device_array_truncate_length_object(dvcdsc_device_array* x, du_uint32 len) {
    if (!x) return 0;

    if (!free_objects(x, len, LAST_POSITION)) return 0;

    return du_array_truncate_length(x, len);
}

du_bool dvcdsc_device_array_truncate_object(dvcdsc_device_array* x) {
    return dvcdsc_device_array_truncate_length_object(x, 0);
}

void dvcdsc_device_array_free_object(dvcdsc_device_array* x) {
    if (!x) return;

    dvcdsc_device_array_truncate_object(x);
    du_array_free(x);
}

du_bool dvcdsc_device_array_remove_object(dvcdsc_device_array* x, du_uint32 pos) {
    if (!x) return 0;

    if (!free_objects(x, pos, pos)) return 0;

    return du_array_remove(x, pos);
}
