cmake_minimum_required(VERSION 3.4.1)

include_directories( src/main/cpp/include/dlna)

add_library(libdixim_upnp STATIC IMPORTED)
set_target_properties(libdixim_upnp
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_upnp.a)

add_library(libcunit STATIC IMPORTED)
set_target_properties(libcunit
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libcunit.a)

add_library(libdixim_av STATIC IMPORTED)
set_target_properties(libdixim_av
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_av.a)

add_library(libdixim_char_util STATIC IMPORTED)
set_target_properties(libdixim_char_util
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_char_util.a)

add_library(libdixim_hdrl STATIC IMPORTED)
set_target_properties(libdixim_hdrl
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_hdrl.a)

add_library(libdixim_igd STATIC IMPORTED)
set_target_properties(libdixim_igd
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_igd.a)

add_library(libdixim_low_power STATIC IMPORTED)
set_target_properties(libdixim_low_power
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_low_power.a)

add_library(libdixim_media_detector STATIC IMPORTED)
set_target_properties(libdixim_media_detector
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_media_detector.a)

add_library(libdixim_mrd STATIC IMPORTED)
set_target_properties(libdixim_mrd
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_mrd.a)

add_library(libdixim_printer STATIC IMPORTED)
set_target_properties(libdixim_printer
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_printer.a)

add_library(libdixim_util STATIC IMPORTED)
set_target_properties(libdixim_util
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_util.a)

add_library(libexpat STATIC IMPORTED)
set_target_properties(libexpat
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libexpat.a)

add_library(libonig STATIC IMPORTED)
set_target_properties(libonig
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libonig.a)

add_library(libuchardet STATIC IMPORTED)
set_target_properties(libuchardet
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libuchardet.a)

add_library(libxml2 STATIC IMPORTED)
set_target_properties(libxml2
  PROPERTIES IMPORTED_LOCATION
  ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libxml2.a)

add_library(
             dtvtlib # dynamic lib name
             SHARED # share
             src/main/cpp/dummy.cpp
#             src/main/cpp/JniCast.cpp
#             src/main/cpp/Dlna.cpp
            )

find_library(
                log-lib
                log
            )

# linked lib targetr lib name
target_link_libraries(
                       dtvtlib
                       libdixim_upnp
                       libcunit
                       libdixim_av
                       libdixim_char_util
                       libdixim_hdrl
                       libdixim_igd
                       libdixim_low_power
                       libdixim_media_detector
                       libdixim_mrd
                       libdixim_printer
                       libdixim_util
                       libexpat
                       libonig
                       libuchardet
                       libxml2
                       ${log-lib}
                     )