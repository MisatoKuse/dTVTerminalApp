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
add_library(libdixim_av STATIC IMPORTED)
set_target_properties(libdixim_av
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_av.a)

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
add_library(libdixim_upnp STATIC IMPORTED)
set_target_properties(libdixim_upnp
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_upnp.a)

# ライブラリを使う
add_library(libdixim_util STATIC IMPORTED)
set_target_properties(libdixim_util
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libdixim_util.a)

# ライブラリを使う
add_library(libexpat STATIC IMPORTED)
set_target_properties(libexpat
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libexpat.a)

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

# ライブラリを使う
add_library(libxml2 STATIC IMPORTED)
set_target_properties(libxml2
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libxml2.a)

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
     )

# 目標ライブラリをリンク
target_link_libraries(
    dtvtlib
    libcunit
    libdixim_av
    libdixim_char_util
    libdixim_hdrl
    libdixim_igd
    libdixim_low_power
    libdixim_media_detector
    libdixim_mrd
    libdixim_printer
    libdixim_upnp
    libdixim_util
    libexpat
    libonig
    libuchardet
    libxml2
    )