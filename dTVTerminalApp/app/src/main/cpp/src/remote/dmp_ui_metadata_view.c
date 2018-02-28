/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include "dmp_ui_metadata_view.h"
#include <du_str.h>
#include <du_alloc.h>
#include <dav_dc_date.h>
#include <stdio.h>

static void simple_help() {
    puts("{help, (q)uit, (b)ack}");
    printf("\n");
}

static void command_back(dmp_ui_metadata_view* mv) {
    dmp_ui* ui = mv->_ui;

    dmp_ui_change_view_to_browse(ui);
    return;
}

static void print_value(const du_uchar* caption, const du_uchar* value) {
    printf("%s: %s\n", caption, value);
}

static void print_property(dav_didl_object* object, const dav_didl_name* name, const du_uchar* caption) {
    dav_didl_object_property* property;
    const du_uchar* value;

    property = dav_didl_object_property_list_get_property(object->prop_list, name, 0);
    if (!property) {
        value = DU_UCHAR_CONST("");
    } else {
        value = property->value;
    }
    print_value(caption, value);
}

static void print_attribute(dav_didl_object_property* property, const dav_didl_name* name, const du_uchar* caption) {
    const du_uchar* value;

    if (!property) {
        value = DU_UCHAR_CONST("");
    } else {
        value = dav_didl_object_attribute_list_get_attribute_value(property->attr_list, name);
        if (!value) value = DU_UCHAR_CONST("");
    }
    print_value(caption, value);
}

static void print_dc_date(dav_didl_object* object) {
    dav_didl_object_property* property;
    du_uchar dc_date[DAV_DC_DATE_SIZE];

    property = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_dc_date(), 0);
    if (!property || !dav_dc_date_normalize(property->value, dc_date)) {
        dc_date[0] = 0;
    }
    dc_date[10] = ' ';
    dc_date[19] = 0;
    print_value(DU_UCHAR_CONST("Date"), dc_date);
}

static void show_metadata_simply(dav_didl_object* object) {
    dav_didl_object_property* property;

    if (object->name == dav_didl_element_container()) {
        puts("[Container]");
        print_property(object, dav_didl_element_dc_title(), DU_UCHAR_CONST("Title"));
    } else if (object->name == dav_didl_element_item()) {
        if (dav_didl_object_derived_from(object, dav_didl_class_audio_item())) {
            puts("[Music]");
            print_property(object, dav_didl_element_dc_title(), DU_UCHAR_CONST("Title"));
            print_property(object, dav_didl_element_upnp_album(), DU_UCHAR_CONST("Album"));
            print_dc_date(object);
            property = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_res(), 0);
            print_attribute(property, dav_didl_attribute_duration(), DU_UCHAR_CONST("Duration"));
            print_property(object, dav_didl_element_dc_creator(), DU_UCHAR_CONST("Artist"));
            print_property(object, dav_didl_element_upnp_genre(), DU_UCHAR_CONST("Genre"));
            print_property(object, dav_didl_element_upnp_original_track_number(), DU_UCHAR_CONST("Truck Number"));
        } else if (dav_didl_object_derived_from(object, dav_didl_class_video_item())) {
            puts("[Video]");
            print_property(object, dav_didl_element_dc_title(), DU_UCHAR_CONST("Title"));
            print_property(object, dav_didl_element_upnp_album(), DU_UCHAR_CONST("Album"));
            print_dc_date(object);
            property = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_res(), 0);
            print_attribute(property, dav_didl_attribute_duration(), DU_UCHAR_CONST("Duration"));
            print_property(object, dav_didl_element_upnp_genre(), DU_UCHAR_CONST("Genre"));
            print_property(object, dav_didl_element_dc_description(), DU_UCHAR_CONST("Description"));
            print_property(object, dav_didl_element_upnp_channel_name(), DU_UCHAR_CONST("Channel Name"));
            print_property(object, dav_didl_element_upnp_channel_nr(), DU_UCHAR_CONST("Channel Number"));
        } else if (dav_didl_object_derived_from(object, dav_didl_class_image_item())) {
            puts("[Photo]");
            print_property(object, dav_didl_element_dc_title(), DU_UCHAR_CONST("Title"));
            print_property(object, dav_didl_element_upnp_album(), DU_UCHAR_CONST("Album"));
            print_dc_date(object);
        } else {
            puts("[Item]");
            print_property(object, dav_didl_element_dc_title(), DU_UCHAR_CONST("Title"));
            print_property(object, dav_didl_element_upnp_album(), DU_UCHAR_CONST("Album"));
            print_dc_date(object);
        }
    }
}

static void show_metadata_verbosely(dav_didl_object* object) {
    dav_didl_object_attribute_list* attr_list;
    dav_didl_object_property_list* prop_list;
    dav_didl_object_attribute* attr;
    dav_didl_object_property* prop;
    du_uint32 i;
    du_uint32 j;

    printf("[%s]", object->name);
    if ((attr_list = object->attr_list)) {
        for (i = 0; i < attr_list->count; ++i) {
            printf(" %s=\"%s\"", attr_list->list[i].name, attr_list->list[i].value);
        }
    }

    puts("");

    if ((prop_list = object->prop_list)) {
        for (i = 0; i < prop_list->count; ++i) {
            prop = prop_list->list + i;
            printf("  [%s]", prop->name);
            if (prop->value)  printf(" %s", prop->value);
            puts("");

            if (!prop->attr_list) continue;

            for (j = 0; j < prop->attr_list->count; ++j) {
                attr = prop->attr_list->list + j;
                printf("    %s=\"%s\"\n", attr->name, attr->value);
            }
        }
    }
}

static du_bool show_metadata(dav_didl_object* object, du_bool verbose) {
    const du_uchar* id;

    dmp_ui_clear_screen();
    dmp_ui_print_bar();
    if (verbose) {
        show_metadata_verbosely(object);
    } else {
        show_metadata_simply(object);
    }
    dmp_ui_print_bar();

    id = dav_didl_object_attribute_list_get_attribute_value(object->attr_list, dav_didl_attribute_id());
    if (!id) return 0;

    printf("object id: %s\n", id);

    return 1;
}

static void help() {
    dmp_ui_print_bar();
    puts("available commands:");
    puts("  back");
    puts("  q(quit)");
    puts("  help");
    dmp_ui_print_bar();
}

void command_metadata(void* user_data, const du_uint argc, const du_uchar* argv[]) {
    dmp_ui_metadata_view* mv = (dmp_ui_metadata_view*)user_data;
    const du_uchar* command = argv[0];

    if (du_str_equal(command, DU_UCHAR_CONST("b")) || du_str_equal(argv[0], DU_UCHAR_CONST("back"))) {
         command_back(mv);
    } else {
        goto help;
    }
    return;

help:
    help();
}

extern void dmp_ui_metadata_view_set_show_verbosely(dmp_ui_view* view, du_bool show_verbosely) {
    dmp_ui_metadata_view* mv = view->_user_data;

    mv->_show_verbosely = show_verbosely;
}

void dmp_ui_metadata_view_set_object(dmp_ui_view* view, dav_didl_object* object) {
    dmp_ui_metadata_view* mv = view->_user_data;

    mv->_object = object;
}

static void update_screen(void* user_data) {
    dmp_ui_metadata_view* mv = (dmp_ui_metadata_view*)user_data;

    if (!show_metadata(mv->_object, mv->_show_verbosely)) goto error;
    simple_help();
    return;

error:
    puts("error");
}

du_bool dmp_ui_metadata_view_init(dmp_ui_view* view, dmp_ui* ui) {
    dmp_ui_metadata_view* mv = 0;

    mv = du_alloc_zero(sizeof(dmp_ui_metadata_view));
    if (!mv) return 0;
    mv->_ui = ui;

    view->_user_data = mv;
    view->_command = command_metadata;
    view->_update_screen = update_screen;
    return 1;
}

void dmp_ui_metadata_view_free(dmp_ui_view* view) {
    dmp_ui_metadata_view* mv = view->_user_data;

    du_alloc_free(mv);
}
