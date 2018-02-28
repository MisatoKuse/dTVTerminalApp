/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <du_str.h>
#include <du_byte.h>
#include <du_alloc.h>
#include "dvcdsc_icon.h"

void dvcdsc_icon_init(dvcdsc_icon* icon) {
    du_byte_zero((du_uint8*)icon, sizeof(dvcdsc_icon));
}

void dvcdsc_icon_free(dvcdsc_icon* icon) {
    if (!icon) return;
    du_alloc_free(icon->_buffer);
    dvcdsc_icon_init(icon);
}

du_bool dvcdsc_icon_set(dvcdsc_icon* icon, const du_uchar* mimetype, du_uint32 width, du_uint32 height, const du_uchar* url) {
    du_uint32 len1;
    du_uint32 len2;
    du_uint32 i = 0;

    du_alloc_free(icon->_buffer);
    len1 = du_str_len(mimetype);
    len2 = du_str_len(url);

    icon->_buffer = (du_uchar*)du_alloc(len1 + len2 + 2);
    if (!icon->_buffer) return 0;

    icon->mimetype = icon->_buffer;
    i = du_byte_copy(icon->_buffer, len1, mimetype);
    icon->_buffer[i++] = 0;

    icon->url = icon->_buffer + i;
    i += du_byte_copy(icon->_buffer + i, len2, url);
    icon->_buffer[i++] = 0;

    icon->width = width;
    icon->height = height;
    return 1;
}

du_bool dvcdsc_icon_is_empty(dvcdsc_icon* icon) {
    return !icon->_buffer;
}
