/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DVCDSC_ICON_H
#define DVCDSC_ICON_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dvcdsc_icon {
    const du_uchar* mimetype;
    du_uint32 width;
    du_uint32 height;
    const du_uchar* url;

    du_uchar* _buffer;
} dvcdsc_icon;

extern void dvcdsc_icon_init(dvcdsc_icon* icon);

extern du_bool dvcdsc_icon_set(dvcdsc_icon* icon, const du_uchar* mimetype, du_uint32 width, du_uint32 height, const du_uchar* url);

extern du_bool dvcdsc_icon_is_empty(dvcdsc_icon* icon);

extern void dvcdsc_icon_free(dvcdsc_icon* icon);

#ifdef __cplusplus
}
#endif

#endif
