/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include <dav_urn.h>
#include <dupnp_urn.h>
#include <du_uri.h>
#include <du_mime_type.h>
#include <du_uchar_array.h>
#include <du_uint32_array.h>
#include <du_str.h>
#include <du_byte.h>
#include <du_alloc.h>
#include <du_log.h>
#include <expat.h>
#include "dvcdsc_parser.h"
#include "../LocalRegistration/ddps/ddps_urn.h"

#define SEPARATOR '|'

typedef enum {
    STATE_DEVICE,
    STATE_ICON,
    STATE_SERVICE
} parser_state;

typedef struct parser_info {
    const du_uchar* location;
    dvcdsc_device_array* device_array;
    du_uchar_array url_base;
    du_uchar_array value1;
    du_uchar_array value2;
    du_uchar_array value3;
    du_uchar_array value4;

    du_int32 skip;
    du_int32 depth;
    du_uint32_array device_stack;
    parser_state state;
    du_uchar_array text;
    du_bool need_space;
    du_bool v2;
} parser_info;

#define dupnp_urn_dlpa_device1() DU_UCHAR_CONST("urn:schemas-dlpa-jp:device-1-0")

static du_bool parser_info_init(parser_info* pi, dvcdsc_device_array* device_array, const du_uchar* location) {
    du_byte_zero((du_uint8*)pi, sizeof(parser_info));
    pi->location = location;
    pi->device_array = device_array;
    du_uchar_array_init(&pi->url_base);
    du_uchar_array_init(&pi->value1);
    if (!du_uchar_array_allocate(&pi->value1, 256)) goto error;
    du_uchar_array_init(&pi->value2);
    if (!du_uchar_array_allocate(&pi->value2, 256)) goto error;
    du_uchar_array_init(&pi->value3);
    if (!du_uchar_array_allocate(&pi->value3, 256)) goto error;
    du_uchar_array_init(&pi->value4);
    if (!du_uchar_array_allocate(&pi->value4, 256)) goto error;
    du_uchar_array_init(&pi->text);
    if (!du_uchar_array_allocate(&pi->text, 256)) goto error;
    du_uint32_array_init(&pi->device_stack);
    pi->state = STATE_DEVICE;
    pi->v2 = 0;
    return 1;

error:
    du_uchar_array_free(&pi->value1);
    du_uchar_array_free(&pi->value2);
    du_uchar_array_free(&pi->value3);
    du_uchar_array_free(&pi->value4);
    du_uchar_array_free(&pi->text);
    return 0;
}

static void parser_info_free(parser_info* pi) {
    du_uchar_array_free(&pi->url_base);
    du_uchar_array_free(&pi->value1);
    du_uchar_array_free(&pi->value2);
    du_uchar_array_free(&pi->value3);
    du_uchar_array_free(&pi->value4);
    du_uchar_array_free(&pi->text);
    du_uint32_array_free(&pi->device_stack);
}

static void clear_values(parser_info* pi) {
    du_uchar_array_truncate(&pi->value1);
    du_uchar_array_truncate(&pi->value2);
    du_uchar_array_truncate(&pi->value3);
    du_uchar_array_truncate(&pi->value4);
}

static du_bool truncate_text(parser_info* pi) {
    if (!du_uchar_array_truncate(&pi->text)) return 0;
    pi->need_space = 0;
    return 1;
}

static du_bool abs_name_equal(const XML_Char* abs_name, const du_uchar* ns, const du_uchar* name) {
    du_uint32 len;

    len = du_str_len(ns);
    if (!du_str_start(DU_UCHAR_CONST(abs_name), ns)) return 0;
    if (abs_name[len] != SEPARATOR) return 0;
    if (du_str_diff(DU_UCHAR_CONST(abs_name + len + 1), name)) return 0;
    return 1;
}

static du_bool set_value(du_uchar_array* value, du_uchar_array* text) {
    if (!du_uchar_array_copy(value, text)) return 0;
    if (!du_uchar_array_cat0(value)) return 0;
    return 1;
}

static du_bool to_absolute_url(du_uchar_array* url, const du_uchar* location, du_uchar_array* tmp) {
    if (!du_uchar_array_copy(tmp, url)) return 0;
    if (!du_uri_resolve(location, du_uchar_array_get(tmp), url)) return 0;
    if (!du_uchar_array_cat0(url)) return 0;
    return 1;
}

static dvcdsc_device* get_current_device(parser_info* pi) {
    du_uint32 i;

    i = du_uint32_array_length(&pi->device_stack) - 1;
    i = du_uint32_array_get_pos(&pi->device_stack, i);
    return dvcdsc_device_array_get_pos(pi->device_array, i);
}

static void device_start_handler(parser_info* pi, const XML_Char* name) {
    if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("URLBase"))) {
        if (pi->depth != 2) goto skip;
    } else if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("device"))) {
        dvcdsc_device device;
        du_uint32 i;

        if (pi->depth % 2 != 0) goto skip;
        if (pi->depth == 2 && dvcdsc_device_array_length(pi->device_array)) goto skip;
        dvcdsc_device_init(&device);
        if (!dvcdsc_device_array_cato(pi->device_array, &device)) goto error;
        i = dvcdsc_device_array_length(pi->device_array) - 1;
        if (!du_uint32_array_cato(&pi->device_stack, i)) goto error;
    } else if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("deviceType"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("friendlyName"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("UDN"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(name, dupnp_urn_dlna_device1(), DU_UCHAR_CONST("X_DLNADOC"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(name, dupnp_urn_digion_device1(), DU_UCHAR_CONST("CAP"))||
	       abs_name_equal(name, dupnp_urn_dlpa_device1(), DU_UCHAR_CONST("CAP"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(name, dupnp_urn_digion_device1(), DU_UCHAR_CONST("RSRegiSocket"))||
	       abs_name_equal(name, dupnp_urn_dlpa_device1(), DU_UCHAR_CONST("RSRegiSocket"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("iconList"))) {
        if (pi->depth % 2 != 1) goto skip;
        pi->state = STATE_ICON;
    } else if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("serviceList"))) {
        if (pi->depth % 2 != 1) goto skip;
        pi->state = STATE_SERVICE;
    } else if (abs_name_equal(name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("deviceList"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else {
        goto skip;
    }
    { if (du_str_start(DU_UCHAR_CONST(name), dupnp_urn_dlpa_device1())) pi->v2 = 1; }
    return;

skip:
    pi->skip = pi->depth;
    return;

error:
    pi->depth = -1;
}

static du_bool validate_device(dvcdsc_device* device) {
    if (dupnp_urn_version_ge(device->device_type, dav_urn_msd(1))) {
        if (dvcdsc_service_is_empty(&device->cds)) return 0;
    } else if (dupnp_urn_version_ge(device->device_type, dav_urn_mrd(1))) {
        return 0;
    }
    return 1;
}

static du_bool device_end_handler(parser_info* pi, const XML_Char* abs_name) {
    dvcdsc_device* device;
    du_uint32 i;

    if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("URLBase"))) {
        if (du_uchar_array_length(&pi->text)) {
            if (!set_value(&pi->url_base, &pi->text)) return 0;
            pi->location = du_uchar_array_get(&pi->url_base);
        }
        return 1;
    }

    device = get_current_device(pi);
    if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("device"))) {
        i = du_uint32_array_length(&pi->device_stack) - 1;
        if (!dvcdsc_device_pack(device) || !validate_device(device)) {
            du_uint32 j;

            du_log_w(0, DU_UCHAR_CONST("dvcdsc_device_parser: invalid device description"));
            j = du_uint32_array_get_pos(&pi->device_stack, i);
            dvcdsc_device_array_remove_object(pi->device_array, j);
        }
        du_uint32_array_truncate_length(&pi->device_stack, i);
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("deviceType"))) {
        if (!du_uchar_array_cat0(&pi->text)) return 0;
        if (!dvcdsc_device_set_device_type(device, du_uchar_array_get(&pi->text))) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("friendlyName"))) {
        if (!du_uchar_array_cat0(&pi->text)) return 0;
        if (!dvcdsc_device_set_friendly_name(device, du_uchar_array_get(&pi->text))) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("UDN"))) {
        if (!du_uchar_array_cat0(&pi->text)) return 0;
        if (!dvcdsc_device_set_udn(device, du_uchar_array_get(&pi->text))) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_dlna_device1(), DU_UCHAR_CONST("X_DLNADOC"))) {
        if (!du_uchar_array_cat0(&pi->text)) return 0;
        if (!dvcdsc_device_set_dlnadoc(device, du_uchar_array_get(&pi->text))) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_digion_device1(), DU_UCHAR_CONST("CAP"))||
	       abs_name_equal(abs_name, dupnp_urn_dlpa_device1(), DU_UCHAR_CONST("CAP"))) {
        if (!du_uchar_array_cat0(&pi->text)) return 0;
        if (!dvcdsc_device_set_diximcap(device, du_uchar_array_get(&pi->text))) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_digion_device1(), DU_UCHAR_CONST("RSRegiSocket"))||
	       abs_name_equal(abs_name, dupnp_urn_dlpa_device1(), DU_UCHAR_CONST("RSRegiSocket"))) {
        if (!du_uchar_array_cat0(&pi->text)) return 0;
        if (!dvcdsc_device_set_rs_regi_socket(device, du_uchar_array_get(&pi->text))) return 0;
    }
    return 1;
}

static void icon_start_handler(parser_info* pi, const XML_Char* abs_name) {
    if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("icon"))) {
        if (pi->depth % 2 != 0) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("mimetype"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("width"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("height"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("url"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else {
        goto skip;
    }
    return;

skip:
    pi->skip = pi->depth;
}

static du_bool validate_icon(parser_info* pi) {
    const du_uchar* s;
    du_uint32 u;

    // mimetype
    s = du_uchar_array_get(&pi->value1);
    if (!du_mime_type_major_equal(s, DU_UCHAR_CONST("image"))) goto invalid;
    if (!du_mime_type_sub_equal(s, DU_UCHAR_CONST("png")) && !du_mime_type_sub_equal(s, DU_UCHAR_CONST("jpeg"))) return 0;

    // width
    s = du_uchar_array_get(&pi->value2);
    du_str_scan_uint32(s, &u);
    if (!u) goto invalid;

    // height
    s = du_uchar_array_get(&pi->value3);
    du_str_scan_uint32(s, &u);
    if (!u) goto invalid;

    // url
    if (!to_absolute_url(&pi->value4, pi->location, &pi->text)) goto invalid;
    return 1;

invalid:
    du_log_w(0, DU_UCHAR_CONST("dvcdsc_device_parser: invalid icon description"));
    return 0;
}

static du_bool set_icon(parser_info* pi) {
    dvcdsc_device* device;
    du_bool update = 0;
    const du_uchar* mimetype;
    du_uint32 width;
    du_uint32 height;
    const du_uchar* url;

    mimetype = du_uchar_array_get(&pi->value1);
    du_str_scan_uint32(du_uchar_array_get(&pi->value2), &width);
    du_str_scan_uint32(du_uchar_array_get(&pi->value3), &height);
    url = du_uchar_array_get(&pi->value4);

    device = get_current_device(pi);
    if (dvcdsc_icon_is_empty(&device->icon)) {
        update = 1;
    } else if (device->icon.width < width) {
        if (!du_mime_type_sub_equal(device->icon.mimetype, DU_UCHAR_CONST("png")) || !du_mime_type_sub_equal(mimetype, DU_UCHAR_CONST("jpeg"))) update = 1;
    }
    if (!update) return 1;
    if (!dvcdsc_icon_set(&device->icon, mimetype, width, height, url)) return 0;
    return 1;
}

static du_bool icon_end_handler(parser_info* pi, const XML_Char* abs_name) {
    if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("iconList"))) {
        pi->state = STATE_DEVICE;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("icon"))) {
        if (validate_icon(pi)) {
            if (!set_icon(pi)) return 0;
        }
        clear_values(pi);
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("mimetype"))) {
        if (!set_value(&pi->value1, &pi->text)) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("width"))) {
        if (!set_value(&pi->value2, &pi->text)) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("height"))) {
        if (!set_value(&pi->value3, &pi->text)) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("url"))) {
        if (!set_value(&pi->value4, &pi->text)) return 0;
    }
    return 1;
}

static void service_start_handler(parser_info* pi, const XML_Char* abs_name) {
    if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("service"))) {
        if (pi->depth % 2 != 0) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("serviceType"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("SCPDURL"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("controlURL"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("eventSubURL"))) {
        if (pi->depth % 2 != 1) goto skip;
    } else {
        goto skip;
    }
    return;

skip:
    pi->skip = pi->depth;
    return;
}

static du_bool validate_service(parser_info* pi) {
    // service_type
    if (!dupnp_urn_version_le(dav_urn_cds(1), du_uchar_array_get(&pi->value1)) &&
        !dupnp_urn_version_le(ddps_urn_dps(1), du_uchar_array_get(&pi->value1)) &&
	!dupnp_urn_version_le(ddps_urn_dps(2), du_uchar_array_get(&pi->value1))) return 0;

    // scpd_url
    if (!to_absolute_url(&pi->value2, pi->location, &pi->text)) goto invalid;

    // control_url
    if (!to_absolute_url(&pi->value3, pi->location, &pi->text)) goto invalid;

    // event_sub_url
    if (!to_absolute_url(&pi->value4, pi->location, &pi->text)) goto invalid;
    return 1;

invalid:
    du_log_w(0, DU_UCHAR_CONST("dvcdsc_device_parser: invalid service description"));
    return 0;
}

static du_bool set_service(parser_info* pi) {
    const du_uchar* service_type;
    const du_uchar* scpd_url;
    const du_uchar* control_url;
    const du_uchar* event_sub_url;
    dvcdsc_device* device;

    service_type = du_uchar_array_get(&pi->value1);
    scpd_url = du_uchar_array_get(&pi->value2);
    control_url = du_uchar_array_get(&pi->value3);
    event_sub_url = du_uchar_array_get(&pi->value4);

    device = get_current_device(pi);
    if (dupnp_urn_version_le(dav_urn_cds(1), service_type)) {
        if (dvcdsc_service_is_empty(&device->cds)) {
            if (!dvcdsc_service_set(&device->cds, service_type, scpd_url, control_url, event_sub_url)) return 0;
        }
    } else if (!pi->v2 && dupnp_urn_version_le(ddps_urn_dps(1), service_type)) {
        if (dvcdsc_service_is_empty(&device->x_dps)) {
            if (!dvcdsc_service_set(&device->x_dps, service_type, scpd_url, control_url, event_sub_url)) return 0;
        }
    } else if (pi->v2 && dupnp_urn_version_le(ddps_urn_dps(2), service_type)) {
        if (!dvcdsc_service_set_dps_v2(&device->x_dps, pi->v2)) return 0;
        if (dvcdsc_service_is_empty(&device->x_dps)) {
            if (!dvcdsc_service_set(&device->x_dps, service_type, scpd_url, control_url, event_sub_url)) return 0;
        }
    }
    return 1;
}

static du_bool service_end_handler(parser_info* pi, const XML_Char* abs_name) {
    if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("serviceList"))) {
        pi->state = STATE_DEVICE;
        return 1;
    }

    if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("service"))) {
        if (validate_service(pi)) {
            if (!set_service(pi)) return 0;
        }
        clear_values(pi);
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("serviceType"))) {
        if (!set_value(&pi->value1, &pi->text)) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("SCPDURL"))) {
        if (!set_value(&pi->value2, &pi->text)) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("controlURL"))) {
        if (!set_value(&pi->value3, &pi->text)) return 0;
    } else if (abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("eventSubURL"))) {
        if (!set_value(&pi->value4, &pi->text)) return 0;
    }
    return 1;
}

static void start_handler(void* arg, const XML_Char* abs_name, const XML_Char** attributes) {
    parser_info* pi = arg;

    if (pi->depth < 0) return;
    ++pi->depth;
    if (pi->skip) return;

    if (pi->depth == 1) {
        if (!abs_name_equal(abs_name, dupnp_urn_upnp_device1(), DU_UCHAR_CONST("root"))) goto error;
    } else {
        switch (pi->state) {
        case STATE_DEVICE:
            device_start_handler(pi, abs_name);
            break;
        case STATE_ICON:
            icon_start_handler(pi, abs_name);
            break;
        case STATE_SERVICE:
            service_start_handler(pi, abs_name);
            break;
        default:
            goto error;
        }
    }

    if (!truncate_text(pi)) goto error;
    return;

error:
    pi->depth = -1;
}

static void end_handler(void* arg, const XML_Char* abs_name) {
    parser_info* pi = arg;

    if (pi->depth < 0) return;
    if (pi->skip) {
        if (pi->skip == pi->depth) pi->skip = 0;
        --pi->depth;
        return;
    }

    if (pi->depth == 1) {
    } else {
        switch (pi->state) {
        case STATE_DEVICE:
            if (!device_end_handler(pi, abs_name)) goto error;
            break;
        case STATE_ICON:
            if (!icon_end_handler(pi, abs_name)) goto error;
            break;
        case STATE_SERVICE:
            if (!service_end_handler(pi, abs_name)) goto error;
            break;
        default:
            goto error;
        }
    }

    if (!truncate_text(pi)) goto error;
    --pi->depth;
    return;

error:
    pi->depth = -1;
}

static void text_handler(void* arg, const XML_Char* s, du_int len) {
    parser_info* pi = arg;
    du_uint32 i;
    du_bool char_pushed;

    if (pi->depth < 0) return;
    if (pi->skip) return;

    char_pushed = (du_uchar_array_length(&pi->text));
    for (i = 0; i < (du_uint32)len; ++i) {
        if (!char_pushed) {
            if (du_str_chr(DU_UCHAR_CONST("\r\n\t "), s[i]) != 4) continue;
            du_uchar_array_cato(&pi->text, s[i]);
            char_pushed = 1;
        } else {
            if (du_str_chr(DU_UCHAR_CONST("\r\n\t "), s[i]) != 4) {
                pi->need_space = 1;
                continue;
            } else {
                if (pi->need_space) {
                    du_uchar_array_cato(&pi->text, ' ');
                    pi->need_space = 0;
                }
                du_uchar_array_cato(&pi->text, s[i]);
            }
        }
    }
    if (du_uchar_array_failed(&pi->text)) goto error;
    return;

error:
    pi->depth = -1;
}

static du_bool parse(dvcdsc_device_array* device_array, const du_uchar* xml, du_uint32 xml_len, const du_uchar* location, du_uchar_array* url_base) {
    static const du_uint32 STEP = 1024;
    XML_Parser parser = 0;
    parser_info pi;
    du_uint32 delta;
    du_uint32 i;

    if (!parser_info_init(&pi, device_array, location)) return 0;
    parser = XML_ParserCreateNS(0, SEPARATOR);
    if (!parser) goto error;
    XML_SetUserData(parser, &pi);
    XML_SetElementHandler(parser, start_handler, end_handler);
    XML_SetCharacterDataHandler(parser, text_handler);
    for (i = 0; i < xml_len; i += delta) {
        delta = (STEP < xml_len - i) ? STEP : xml_len - i;
        if (!XML_Parse(parser, (const char*)(xml + i), delta, (i + delta == xml_len))) goto error;
        if (pi.depth < 0) goto error;
    }

    if (url_base && du_uchar_array_length(&pi.url_base)) {
        if (!du_uchar_array_copy(url_base, &pi.url_base)) goto error;
    }

    XML_ParserFree(parser);
    parser_info_free(&pi);
    return 1;

error:
    if (parser) XML_ParserFree(parser);
    parser_info_free(&pi);
    return 0;
}

du_bool dvcdsc_parser_parse(dvcdsc_device_array* device_array, const du_uchar* xml, du_uint32 xml_len, const du_uchar* location) {
    du_uchar_array url_base;

    du_uchar_array_init(&url_base);

    // solely for the purpose of getting URLBase
    if (!parse(device_array, xml, xml_len, location, &url_base)) goto error;

    // parse again to fix URLs (e.g. Control URL) if URLBase exists
    if (du_uchar_array_length(&url_base)) {
        if (!du_uchar_array_cat0(&url_base)) goto error;
        if (!dvcdsc_device_array_truncate_object(device_array)) goto error;
        if (!parse(device_array, xml, xml_len, du_uchar_array_get(&url_base), 0)) goto error;
    }

    du_uchar_array_free(&url_base);
    return 1;

error:
    du_uchar_array_free(&url_base);
    return 0;
}

/*
#include <du_file.h>
#include <stdio.h>

static void print_device(dvcdsc_device* device) {
    puts("----------");
    printf("device_type: %s\n", device->device_type);
    printf("friendly_name: %s\n", device->friendly_name);
    printf("udn: %s\n", device->udn);
    if (device->dlnadoc) printf("dlnadoc: %s\n", device->dlnadoc);

    if (device->icon.mimetype) printf("icon.mimetype: %s\n", device->icon.mimetype);
    if (device->icon.width) printf("icon.resolution: %ux%u\n", device->icon.width, device->icon.height);
    if (device->icon.url) printf("icon.url: %s\n", device->icon.url);

    printf("cds service_type: %s\n", device->cds.service_type);
    printf("cds scpd_url: %s\n", device->cds.scpd_url);
    printf("cds control_url: %s\n", device->cds.control_url);
    printf("cds event_sub_url: %s\n", device->cds.event_sub_url);
}

int main(int argc, char** argv) {
    const du_uchar* path;
    const du_uchar* location;
    dvcdsc_device_array device_array;
    du_file_status fs;
    du_file f = DU_FILE_INVALID;
    du_uchar* xml = 0;
    du_uint32 nbytes;
    du_uint32 len;
    du_uint32 i;
    dvcdsc_device* device;

    dvcdsc_device_array_init(&device_array);

    if (argc != 3) goto error;
    path = argv[1];
    location = argv[2];

    if (!du_file_get_status(path, &fs)) goto error;
    xml = (du_uchar*)du_alloc(fs.size + 1);
    if (!du_file_open_read(&f, path)) goto error;
    if (!du_file_read(f, xml, fs.size, &nbytes)) goto error;
    xml[fs.size] = 0;

    if (!dvcdsc_parser_parse(&device_array, xml, location)) goto error;
    len = dvcdsc_device_array_length(&device_array);
    printf("%u devices found\n", len);
    device = dvcdsc_device_array_get(&device_array);
    for (i = 0; i < len; ++i) {
        print_device(device + i);
    }

    du_alloc_free(xml);
    du_file_close(f);
    dvcdsc_device_array_free_object(&device_array);
    puts("ok");
    return 0;

error:
    du_alloc_free(xml);
    du_file_close(f);
    dvcdsc_device_array_free(&device_array);
    puts("error");
    return 1;
}
*/
