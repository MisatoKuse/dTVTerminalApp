/*
 * Copyright (c) 2015 DigiOn, Inc. All rights reserved.
 */
/**
 * @file drag_cp_conn_status.h
 * @brief define connection status types for drag-cp
 */
#ifndef DRAG_CP_CONN_STATUS_H
#define DRAG_CP_CONN_STATUS_H

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Conntction status to SIP and STUN servers.
 */
typedef enum {

  /**
   * Panic situation occur. This will happen in normal usage.
   */
  DRAG_CP_SERVER_CONN_STATUS_FATAL = 0,

  /**
   * Not requested to connect to servers yet.
   */
  DRAG_CP_SERVER_CONN_STATUS_NOT_CONNECTED,

  /**
   * Tried to conntct to server or once succeeded but it gets to fail.
   */
  DRAG_CP_SERVER_CONN_STATUS_ERROR,

  /**
   * Now Trying to conntct to server.
   */
  DRAG_CP_SERVER_CONN_STATUS_CONNECTING,

  /**
   * The connection to server is established.
   */
  DRAG_CP_SERVER_CONN_STATUS_CONNECTED,
} drag_cp_server_conn_status;

/**
 * Conntction status to peer.
 */
typedef enum {

  /**
   * Panic situation occur. This will happen in normal usage.
   */
  DRAG_CP_PEER_CONN_STATUS_FATAL = 0,

  /**
   * The connection to server is not established.
   */
  DRAG_CP_PEER_CONN_STATUS_NOT_CONNECTED,

  /**
   * the connection to sip server is ok,
   * waiting start to connect of application.
   */
  DRAG_CP_PEER_CONN_STATUS_NO_CONNECTION,

  /**
   * have send to request to a peer, now process to establish is ongoing.
   */
  DRAG_CP_PEER_CONN_STATUS_CALLING,

  /**
   * The connection from peer is established.
   */
  DRAG_CP_PEER_CONN_STATUS_CONNECTED,
} drag_cp_peer_conn_status;



#ifdef __cplusplus
}
#endif

#endif
