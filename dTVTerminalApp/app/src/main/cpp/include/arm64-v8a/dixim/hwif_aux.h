// -*- C++ -*-
#ifndef _INCLUDE_HWIF_AUX
#define _INCLUDE_HWIF_AUX

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
  void* private_data_home;
  void* vm;
  void* obj;
  void* mac_address_method_id;
} dixim_hwif_private_data_io;

#ifdef __cplusplus
}
#endif

#endif
