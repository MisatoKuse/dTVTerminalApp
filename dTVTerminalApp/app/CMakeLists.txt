#
# Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
#


#
# プロジェクトJNI用Makefile
# 機能： DLNAのライブラリとJNIのソースファイルをコンパイルし、リンクし、プロジェクト用のライブラリを作る
#

# CMake fileバージョン情報
cmake_minimum_required(VERSION 3.4.1)

# ヘッダファイrうライブラリを指定
include_directories( ${CMAKE_SOURCE_DIR}/src/main/cpp/include/dixim)

# ライブラリを使う
add_library(libcunit STATIC IMPORTED)
set_target_properties(libcunit
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libcunit.a)

# ライブラリを使う
add_library(libdixim_char_util STATIC IMPORTED)
set_target_properties(libdixim_char_util
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_char_util.a)

# ライブラリを使う
add_library(libdixim_hdrl STATIC IMPORTED)
set_target_properties(libdixim_hdrl
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_hdrl.a)

# ライブラリを使う
add_library(libdixim_igd STATIC IMPORTED)
set_target_properties(libdixim_igd
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_igd.a)

# ライブラリを使う
add_library(libdixim_low_power STATIC IMPORTED)
set_target_properties(libdixim_low_power
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_low_power.a)

# ライブラリを使う
add_library(libdixim_media_detector STATIC IMPORTED)
set_target_properties(libdixim_media_detector
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_media_detector.a)

# ライブラリを使う
add_library(libdixim_mrd STATIC IMPORTED)
set_target_properties(libdixim_mrd
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_mrd.a)

# ライブラリを使う
add_library(libdixim_printer STATIC IMPORTED)
set_target_properties(libdixim_printer
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_printer.a)



# ライブラリを使う
add_library(libonig STATIC IMPORTED)
set_target_properties(libonig
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libonig.a)

# ライブラリを使う
add_library(libuchardet STATIC IMPORTED)
set_target_properties(libuchardet
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libuchardet.a)

#==============================  dtcp begin  ==============================

add_library(libdixim_dtcp STATIC IMPORTED)
set_target_properties(libdixim_dtcp
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp.a)

add_library(libdixim_dtcp_bridge STATIC IMPORTED)
  set_target_properties(libdixim_dtcp_bridge
PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_bridge.a)

add_library(libdixim_dtcp_util_ttl_no_check STATIC IMPORTED)
set_target_properties(libdixim_dtcp_util_ttl_no_check
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_util_ttl_no_check.a)

add_library(libnmcc_db STATIC IMPORTED)
set_target_properties(libnmcc_db
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libnmcc_db.a)

add_library(libts2tts STATIC IMPORTED)
set_target_properties(libts2tts
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libts2tts.a)

add_library(libcheck_wlan_wep STATIC IMPORTED)
set_target_properties(libcheck_wlan_wep
    PROPERTIES IMPORTED_LOCATION
    ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libcheck_wlan_wep.a)

add_library(libcheck_wlan_wep_no_check STATIC IMPORTED)
set_target_properties(libcheck_wlan_wep_no_check
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libcheck_wlan_wep_no_check.a)

#==============================  dtcp end  ==============================
add_library(libmedia_dtcp STATIC IMPORTED)
set_target_properties(libmedia_dtcp
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libmedia_dtcp.a)

add_library(libdixim_dtcp_source STATIC IMPORTED)
set_target_properties(libdixim_dtcp_source
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_source.a)

add_library(libdixim_dtcp_sink STATIC IMPORTED)
set_target_properties(libdixim_dtcp_sink
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_sink.a)

add_library(libdixim_dtcp_util STATIC IMPORTED)
set_target_properties(libdixim_dtcp_util
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_util.a)

add_library(libdixim_dtcp_private_data_io STATIC IMPORTED)
set_target_properties(libdixim_dtcp_private_data_io
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_private_data_io.a)

add_library(libactc STATIC IMPORTED)
set_target_properties(libactc
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libactc.a)

add_library(libdixim_hwif STATIC IMPORTED)
set_target_properties(libdixim_hwif
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_hwif.a)

add_library(libcipher_file STATIC IMPORTED)
set_target_properties(libcipher_file
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libcipher_file.a)

add_library(libmedia_mpeg STATIC IMPORTED)
set_target_properties(libmedia_mpeg
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libmedia_mpeg.a)

add_library(libdixim_dtcp_crypto_aes STATIC IMPORTED)
set_target_properties(libdixim_dtcp_crypto_aes
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_crypto_aes.a)

add_library(libdixim_dtcp_crypto_ecc STATIC IMPORTED)
set_target_properties(libdixim_dtcp_crypto_ecc
  PROPERTIES IMPORTED_LOCATION
 ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_crypto_ecc.a)

add_library(libdixim_dtcp_crypto_rng STATIC IMPORTED)
set_target_properties(libdixim_dtcp_crypto_rng
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_crypto_rng.a)

add_library(libdixim_dtcp_crypto_sha STATIC IMPORTED)
set_target_properties(libdixim_dtcp_crypto_sha
  PROPERTIES IMPORTED_LOCATION
 ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_dtcp_crypto_sha.a)

add_library(libtomcrypt STATIC IMPORTED)
set_target_properties(libtomcrypt
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libtomcrypt.a)

add_library(libtommath STATIC IMPORTED)
set_target_properties(libtommath
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libtommath.a)



add_library(libsecure_io STATIC IMPORTED)
set_target_properties(libsecure_io
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libsecure_io.a)

add_library(libchunked_file STATIC IMPORTED)
set_target_properties(libchunked_file
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libchunked_file.a)

# ライブラリを使う
add_library(libdixim_av STATIC IMPORTED)
set_target_properties(libdixim_av
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_av.a)

# ライブラリを使う
add_library(libdixim_upnp STATIC IMPORTED)
set_target_properties(libdixim_upnp
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_upnp.a)

# ライブラリを使う
add_library(libexpat STATIC IMPORTED)
set_target_properties(libexpat
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libexpat.a)

# ライブラリを使う
add_library(libdixim_util STATIC IMPORTED)
set_target_properties(libdixim_util
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_util.a)

# ライブラリを使う
add_library(libxml2 STATIC IMPORTED)
set_target_properties(libxml2
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libxml2.a)

find_library( # Defines the name of the path variable that stores the
              # location of the NDK library.
              log-lib
              # Specifies the name of the NDK library that
              # CMake needs to locate.
              log )

# ライブラリを使う
add_library(
     dtvtlib # dynamic lib name
     SHARED  # share
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaBSDigitalXmlParser.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaTerChXmlParser.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaHikariChXmlParser.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaJniCast.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/Dlna.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaXmlParserBase.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaDevXmlParser.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaRecVideoXmlParser.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DmsInfo.c
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/DlnaBSDigitalXmlParser.cpp
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/download/downloader.cc
     ${CMAKE_SOURCE_DIR}/src/main/cpp/src/download/dmspu_cipher_file.cc
     )

# 目標ライブラリをリンク
target_link_libraries(
    dtvtlib
    libcunit
    libdixim_char_util
    libdixim_hdrl
    libdixim_igd
    libdixim_low_power
    libdixim_media_detector
    libdixim_mrd
    libdixim_printer
    libonig
    libuchardet
    libdixim_dtcp_bridge
    libdixim_dtcp_util_ttl_no_check
    libnmcc_db
    libts2tts
    libcheck_wlan_wep
    libcheck_wlan_wep_no_check
    libmedia_dtcp
    libdixim_dtcp_source
    libdixim_dtcp_sink
    libdixim_dtcp
    libdixim_dtcp_private_data_io
    libactc #from drm lib
    libdixim_hwif #from drm lib
    libcipher_file
    libmedia_mpeg
    libdixim_dtcp_crypto_aes
    libdixim_dtcp_crypto_ecc
    libdixim_dtcp_crypto_rng
    libdixim_dtcp_crypto_sha
    libtomcrypt
    libtommath
    libdixim_dtcp_util
    libsecure_io
    libchunked_file
    libdixim_av
    libdixim_upnp
    libexpat
    libdixim_util
    libxml2
    ${log-lib}
    )