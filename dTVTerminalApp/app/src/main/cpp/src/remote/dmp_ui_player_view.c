/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include "dmp_ui_player_view.h"
#include "player.h"
#include <du_str.h>
#include <du_csv.h>
#include <du_byte.h>
#include <du_alloc.h>
#include <stdio.h>

static du_bool is_action_available(du_uint32 available_actions, player_action_flag flag) {
    return (available_actions >> flag) & 1;
}

static void print_actions(du_uint32 actions) {
    printf("{help, (b)ack");
    if (is_action_available(actions, PLAYR_ACTION_FLAG_PLAY)) {
        printf(", play");
    }
    if (is_action_available(actions, PLAYR_ACTION_FLAG_DOWNLOAD)) {
        printf(", download");
    }
    if (is_action_available(actions, PLAYR_ACTION_FLAG_PAUSE)) {
        printf(", pause");
    }
    if (is_action_available(actions, PLAYR_ACTION_FLAG_STOP)) {
        printf(", stop");
    }
    if (is_action_available(actions,PLAYR_ACTION_FLAG_SEEK)) {
        printf(", seek [hh:mm:ss]");
    }
    printf(", info}\n");
}

static void help(dmp_ui_player_view* pv) {
    dmp_ui_clear_screen();
    dmp_ui_print_bar();
    puts("available commands:");
    print_actions(pv->_available_actions);
}

static void command_back(dmp_ui_player_view* pv) {
    dmp_ui* ui = pv->_ui;

    player_action_stop(pv->_p);
    dmp_ui_change_view_to_browse(ui);
}

static void command_play(dmp_ui_player_view* pv) {
    if (!is_action_available(pv->_available_actions, PLAYR_ACTION_FLAG_PLAY)) goto error;

    if (!player_action_play(pv->_p)) goto error;
    return;

error:
    puts("error");
}

static void command_download(dmp_ui_player_view* pv) {
    if (!is_action_available(pv->_available_actions, PLAYR_ACTION_FLAG_DOWNLOAD)) goto error;

    if (!player_action_download(pv->_p)) goto error;
    return;

error:
    puts("error");
}

static void command_pause(dmp_ui_player_view* pv) {
    if (!is_action_available(pv->_available_actions, PLAYR_ACTION_FLAG_PAUSE)) goto error;

    if (!player_action_pause(pv->_p)) goto error;
    return;

error:
    puts("error");
}

static void command_stop(dmp_ui_player_view* pv) {
    if (!is_action_available(pv->_available_actions, PLAYR_ACTION_FLAG_STOP)) goto error;

    if (!player_action_stop(pv->_p)) goto error;
    return;

error:
    puts("error");
}

static void command_seek(dmp_ui_player_view* pv, const du_uchar* terget_time) {
    du_uint32 msec;

    if (!is_action_available(pv->_available_actions, PLAYR_ACTION_FLAG_SEEK)) goto error;

    if (!dav_didl_duration_scan(terget_time, &msec)) goto error;
    if (!player_action_seek(pv->_p, msec)) goto error;
    return;

error:
    puts("error");
}

const du_uchar* get_unknown() {
    return DU_UCHAR_CONST("UNKNOWN");
}

static void show_position_info(player_position_info* info) {
    du_uchar time[DAV_DIDL_DURATION_STR_SIZE];
    const du_uchar* print_value;

    puts("");
    if (info->is_current_position_ms_available) {
        time[dav_didl_duration_fmt(time, info->current_position_ms)] = 0;
        print_value = time;
    } else {
        print_value = get_unknown();
    }
    printf("Current: %s\n", print_value);

    if (info->is_duration_ms_available) {
        time[dav_didl_duration_fmt(time, info->duration_ms)] = 0;
        print_value = time;
    } else {
        print_value = get_unknown();
    }
    printf("Duration: %s\n", print_value);

    if (du_uchar_array_length(&info->uri)) {
        print_value = du_uchar_array_get(&info->uri);
    } else {
        print_value = get_unknown();
    }
    printf("URI: %s\n", print_value);
    puts("");
}

static void command_info(dmp_ui_player_view* pv) {
    player_position_info info;

    player_position_info_init(&info);
    if (!player_get_position_info(pv->_p, &info)) goto error;
    show_position_info(&info);
    player_position_info_free(&info);
    return;

error:
    player_position_info_free(&info);
    puts("error");
}

void command_player(void* user_data, const du_uint argc, const du_uchar* argv[]) {
    dmp_ui_player_view* pv = (dmp_ui_player_view*)user_data;
    const du_uchar* command = argv[0];

    if (du_str_equal(command, DU_UCHAR_CONST("b")) || du_str_equal(command, DU_UCHAR_CONST("back"))) {
        command_back(pv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("play"))) {
        command_play(pv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("download"))) {
                command_download(pv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("pause"))) {
        command_pause(pv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("stop"))) {
        command_stop(pv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("seek"))) {
        const du_uchar* target_time;

        if (argc < 2) goto help;
        target_time = argv[1];
        command_seek(pv, target_time);
    } else if (du_str_equal(command, DU_UCHAR_CONST("info"))) {
        command_info(pv);
    } else {
        goto help;
    }
    return;

help:
    help(pv);
}

static void update_screen(void* _data) {
    dmp_ui_player_view* pv = (dmp_ui_player_view*)_data;

    player_get_current_available_actions(pv->_p, &pv->_available_actions, &pv->_play_spped);
    help(pv);
}

static void print_state(player_state state) {
    const du_uchar* state_name;

    switch (state) {
    case PLAYER_STATE_NO_MEDIA_PRESENT:
        state_name = DU_UCHAR_CONST("NO_MEDIA_PRESENT");
        break;
    case PLAYER_STATE_STOPPED:
        state_name = DU_UCHAR_CONST("STOPPED");
        break;
    case PLAYER_STATE_PLAYING:
        state_name = DU_UCHAR_CONST("PLAYING");
        break;
    case PLAYER_STATE_DOWNLOADING:
        state_name = DU_UCHAR_CONST("DOWNLAODING");
        break;
    case PLAYER_STATE_PAUSED_PLAYBACK:
        state_name = DU_UCHAR_CONST("PAUSED_PLAYBACK");
        break;
    case PLAYER_STATE_TRANSITIONING:
        state_name = DU_UCHAR_CONST("TRANSITIONING");
        break;
    default:
        return;
    }

    printf("Player State: %s\n", state_name);
}

static void state_change_handler(player_state state, player_status status, void* handler_arg) {
    dmp_ui_player_view* pv = (dmp_ui_player_view*)handler_arg;
    dmp_ui* ui = pv->_ui;

    if (dmp_ui_get_current_view(ui) != dmp_ui_get_player_view(ui)) return;

    player_get_current_available_actions(pv->_p, &pv->_available_actions, &pv->_play_spped);
    print_state(state);
}

du_bool dmp_ui_player_view_init(dmp_ui_view* view, dmp_ui* ui, player* p) {
    dmp_ui_player_view* pv = 0;

    pv = du_alloc_zero(sizeof(dmp_ui_player_view));
    if (!pv) return 0;
    pv->_ui = ui;
    pv->_p = p;

    du_uchar_array_init(&pv->_play_spped);

    if (!player_set_state_change_handler(p, state_change_handler, pv)) goto error;

    view->_user_data = pv;
    view->_command = command_player;
    view->_update_screen = update_screen;
    return 1;

error:
    du_uchar_array_free(&pv->_play_spped);
    du_alloc_free(pv);
    return 0;
}

void dmp_ui_player_view_free(dmp_ui_view* view) {
    dmp_ui_player_view* pv = view->_user_data;

    du_uchar_array_free(&pv->_play_spped);
    du_alloc_free(pv);
}
