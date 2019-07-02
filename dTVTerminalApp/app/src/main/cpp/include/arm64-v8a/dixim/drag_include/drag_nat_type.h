/*
 * Copyright (c) 2015 DigiOn, Inc. All rights reserved.
 */
/**
 * @file drag_nat_type.h
 * @brief define nat types for drag external functions.
 */
#ifndef DRAG_NAT_TYPE_H
#define DRAG_NAT_TYPE_H

#ifdef __cplusplus
extern "C" {
#endif

/**
 * This enumeration describes the NAT types, as specified by RFC 3489
 * Section 5, NAT Variations.
 * Usage of internet gateway device will also reported.
 */
typedef enum {

  /**
   * NAT type is unknown because the detection has not been performed.
   */
  DRAG_NAT_TYPE_UNKNOWN = 0,

  /**
   * NAT type is unknown because there is failure in the detection
   * process, possibly because server does not support RFC 3489.
   */
  DRAG_NAT_TYPE_ERROR = 1,

  /**
   * NAT type is Open.
   */
  DRAG_NAT_TYPE_OPEN = 2,

  /**
   * NAT type is Blocked.
   */
  DRAG_NAT_TYPE_BLOCKED = 3,

  /**
   * NAT typ is Symmetric UDP
   */
  DRAG_NAT_TYPE_SYMMETRIC_UDP = 4,

  /**
   * NAT type is Full Cone.
   */
  DRAG_NAT_TYPE_FULL_CONE = 5,

  /**
   * NAT type is Symmetric.
   */
  DRAG_NAT_TYPE_SYMMETRIC = 6,

  /**
   * NAT type is Restricted.
   */
  DRAG_NAT_TYPE_RESTRICTED = 7,

  /**
   * NAT type is Port Restricted.
   */
  DRAG_NAT_TYPE_PORT_RESTRICTED = 8,

  /**
   * NAT type is Open.
   */
  DRAG_NAT_TYPE_RFC3489 = 0x0f,

  /**
   * Internet gateway device (IGD) is found and I will use it.
   * This value is added to aforesaid values if IGD will be used.
   */
  DRAG_NAT_TYPE_WITH_IGD = 0x8000000,

  /**
   * I will use TURN server to connect the peer.
   * This value is added to aforesaid values if TURN will be used.
   */
  DRAG_NAT_TYPE_WITH_TURN = 0x4000000,

  /**
   * The nat type value is forced by application.
   * This value is added to aforesaid values if the application forces
   * to use the value as detected nat type.
   */
  DRAG_NAT_TYPE_IS_FORCED = 0x2000000,

} drag_nat_type; 


#ifdef __cplusplus
}
#endif

#endif
