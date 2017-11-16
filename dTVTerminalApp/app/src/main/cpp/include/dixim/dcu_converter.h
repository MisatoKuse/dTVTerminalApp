/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


#ifndef DCU_CONVERTER_H
#define DCU_CONVERTER_H

/**
 * @file
 *    The dcu_converter interface provides various methods for character code conversion.
 */

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  The enumeration of mode of conversion.
 */
typedef enum {
    /**
     *   default mode
     */
    DCU_CONVERTER_MODE_DEFAULT,

    /**
     *   translit character that look similar when a character cannot be represented in the target character set.
     */
    DCU_CONVERTER_MODE_TRANSLIT,

    /**
     *   ignore when a character cannot be represented in the target character set.
     */
    DCU_CONVERTER_MODE_IGNORE,

} dcu_converter_mode;

/**
 *  This structure contains the information of character code conversion.
 */
typedef struct dcu_converter {
    void* impl;
} dcu_converter;

/**
 *  Initializes dcu_converter.
 *  @param[out] x  pointer to the dcu_converter data structure.
 *  @param[in] to_code character code string for output.
 *  @param[in] from_code character code string of original text.
 *  @param[in] mode mode of conversion.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark You can not set arib character code string as to_code. And if you set arib caracter code string
 *  as from_code, you must set UTF-8 character code string as to_code.
 */
extern du_bool dcu_converter_init(dcu_converter* x, const du_uchar* to_code, const du_uchar* from_code, dcu_converter_mode mode);

/**
 *  Frees the region used by dcu_converter.
 *  @param[in,out] x pointer to the dcu_converter structure.
 */
extern void dcu_converter_free(dcu_converter* x);

/**
 *  Gets number of bytes of output specifying the dcu_converter.
 *  @param[in] x  pointer to the dcu_converter data structure.
 *  @param[in] in original text.
 *  @param[in] in_bytes number of bytes for original text.
 *  @param[out] out_bytes number of bytes for output.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_converter_get_out_bytes(dcu_converter* x, const du_uchar* in, du_uint32 in_bytes, du_uint32* out_bytes);

/**
 *  Gets number of bytes of output specifying the character code string.
 *  @param[in] to_code character code string for output.
 *  @param[in] from_code character code string of original text.
 *  @param[in] in original text.
 *  @param[in] in_bytes number of bytes for original text.
 *  @param[out] out_bytes number of bytes for output.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_converter_get_out_bytes2(const du_uchar* to_code, const du_uchar* from_code, const du_uchar* in, du_uint32 in_bytes, du_uint32* out_bytes);

/**
 *  Converts text specifying the dcu_converter.
 *  @param[in] x  pointer to the dcu_converter data structure.
 *  @param[in,out] in original text. For each character conversion
 *  in will be incremented.
 *  @param[in,out] in_bytes_left number of bytes for original text and returns number of bytes for the remainder of original text.
 *  @param[out] out output buffer for converted text. For each character
 *  conversion out will be incremented.
 *  @param[in,out] out_bytes_left number of bytes for out buffer and returns number of bytes for the remainder of output buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_converter_convert(dcu_converter* x, const du_uchar** in, du_uint32* in_bytes_left, du_uchar** out, du_uint32* out_bytes_left);

/**
 *  Converts text specifying the character code string.
 *  @param[in] to_code character code string for output.
 *  @param[in] from_code character code string of original text.
 *  @param[in] in original text.
 *  @param[in,out] in_bytes_left number of bytes for original text and returns number of bytes for the remainder of original text.
 *  @param[out] out output buffer for converted text.
 *  @param[in,out] out_bytes_left number of bytes for output buffer and returns number of bytes for the remainder of output buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_converter_convert2(const du_uchar* to_code, const du_uchar* from_code, const du_uchar** in, du_uint32* in_bytes_left, du_uchar** out, du_uint32* out_bytes_left);

/**
 *  Returns ascii character code string.
 *  This ascii character code string is "ASCII".
 *  @return ascii character code string.
 */
extern const du_uchar* dcu_converter_code_ascii(void);

/**
 *  Returns ISO 8859-1 character code string.
 *  This ISO 8859-1 character code string is "ISO-8859-1".
 *  @return ISO 8859-1 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_1(void);

/**
 *  Returns ISO 8859-2 character code string.
 *  This ISO 8859-2 character code string is "ISO-8859-2".
 *  @return ISO 8859-2 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_2(void);

/**
 *  Returns ISO 8859-3 character code string.
 *  This ISO 8859-3 character code string is "ISO-8859-3".
 *  @return ISO 8859-3 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_3(void);

/**
 *  Returns ISO 8859-4 character code string.
 *  This ISO 8859-4 character code string is "ISO-8859-4".
 *  @return ISO 8859-4 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_4(void);

/**
 *  Returns ISO 8859-5 character code string.
 *  This ISO 8859-5 character code string is "ISO-8859-5".
 *  @return ISO 8859-5 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_5(void);

/**
 *  Returns ISO 8859-6 character code string.
 *  This ISO 8859-6 character code string is "ISO-8859-6".
 *  @return ISO 8859-6 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_6(void);

/**
 *  Returns ISO 8859-7 character code string.
 *  This ISO 8859-7 character code string is "ISO-8859-7".
 *  @return ISO 8859-7 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_7(void);

/**
 *  Returns ISO 8859-8 character code string.
 *  This ISO 8859-8 character code string is "ISO-8859-8".
 *  @return ISO 8859-8 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_8(void);

/**
 *  Returns ISO 8859-9 character code string.
 *  This ISO 8859-9 character code string is "ISO-8859-9".
 *  @return ISO 8859-9 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_9(void);

/**
 *  Returns ISO 8859-10 character code string.
 *  This ISO 8859-10 character code string is "ISO-8859-10".
 *  @return ISO 8859-10 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_10(void);

/**
 *  Returns ISO 8859-13 character code string.
 *  This ISO 8859-13 character code string is "ISO-8859-13".
 *  @return ISO 8859-13 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_13(void);

/**
 *  Returns ISO 8859-14 character code string.
 *  This ISO 8859-14 character code string is "ISO-8859-14".
 *  @return ISO 8859-14 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_14(void);

/**
 *  Returns ISO 8859-15 character code string.
 *  This ISO 8859-15 character code string is "ISO-8859-15".
 *  @return ISO 8859-15 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_15(void);

/**
 *  Returns ISO 8859-16 character code string.
 *  This ISO 8859-16 character code string is "ISO-8859-16".
 *  @return ISO 8859-16 character code string.
 */
extern const du_uchar* dcu_converter_code_iso_8859_16(void);

/**
 *  Returns shiftJIS character code string.
 *  This shiftJIS character code string is "SHIFT-JIS".
 *  @return shiftJIS character code string.
 */
extern const du_uchar* dcu_converter_code_shift_jis(void);

/**
 *  Returns eucJP character code string.
 *  This eucJP character code string is "EUC-JP".
 *  @return eucJP character code string.
 */
extern const du_uchar* dcu_converter_code_euc_jp(void);

/**
 *  Returns cp932 character code string.
 *  This cp932 character code string is "CP932".
 *  @return cp932 character code string.
 */
extern const du_uchar* dcu_converter_code_cp932(void);

/**
 *  Returns ISO 2022-JP character code string.
 *  This ISO 2022-JP character code string is "ISO-2022-JP".
 *  @return ISO 2022-JP character code string.
 */
extern const du_uchar* dcu_converter_code_iso_2022_jp(void);

/**
 *  Returns arib character code string.
 *  This arib character code string is "ARIB".
 *  @return arib character code string.
 */
extern const du_uchar* dcu_converter_code_arib(void);

/**
 *  Returns ISO 2022-CN character code string.
 *  This ISO 2022-CN character code string is "ISO-2022-CN".
 *  @return ISO 2022-CN character code string.
 */
extern const du_uchar* dcu_converter_code_iso_2022_cn(void);

/**
 *  Returns big5 character code string.
 *  This big5 character code string is "BIG5".
 *  @return big5 character code string.
 */
extern const du_uchar* dcu_converter_code_big5(void);

/**
 *  Returns GB18030 character code string.
 *  This GB18030 character code string is "GB18030".
 *  @return GB18030 character code string.
 */
extern const du_uchar* dcu_converter_code_gb18030(void);

/**
 *  Returns HZ character code string.
 *  This HZ character code string is "HZ".
 *  @return HZ character code string.
 */
extern const du_uchar* dcu_converter_code_hz(void);

/**
 *  Returns ISO 2022-KR character code string.
 *  This ISO 2022-KR character code string is "ISO-2022-KR".
 *  @return ISO 2022-KR character code string.
 */
extern const du_uchar* dcu_converter_code_iso_2022_kr(void);

/**
 *  Returns eucKR character code string.
 *  This eucKR character code string is "EUC-KR".
 *  @return eucKR character code string.
 */
extern const du_uchar* dcu_converter_code_euc_kr(void);

/**
 *  Returns UTF-8 character code string.
 *  This UTF-8 character code string is "UTF-8".
 *  @return UTF-8 character code string.
 */
extern const du_uchar* dcu_converter_code_utf_8(void);

/**
 *  Returns UCS-2 character code string.
 *  This UCS-2 character code string is "UCS-2".
 *  @return UCS-2 character code string.
 */
extern const du_uchar* dcu_converter_code_ucs_2(void);

/**
 *  Returns UCS-2LE character code string.
 *  This UCS-2LE character code string is "UCS-2LE".
 *  @return UCS-2LE character code string.
 */
extern const du_uchar* dcu_converter_code_ucs_2le(void);

/**
 *  Returns UCS-2BE character code string.
 *  This UCS-2BE character code string is "UCS-2BE".
 *  @return UCS-2BE character code string.
 */
extern const du_uchar* dcu_converter_code_ucs_2be(void);

/**
 *  Returns UCS-4 character code string.
 *  This UCS-4 character code string is "UCS-4".
 *  @return UCS-4 character code string.
 */
extern const du_uchar* dcu_converter_code_ucs_4(void);

/**
 *  Returns UCS-4LE character code string.
 *  This UCS-4LE character code string is "UCS-4LE".
 *  @return UCS-4LE character code string.
 */
extern const du_uchar* dcu_converter_code_ucs_4le(void);

/**
 *  Returns UCS-4BE character code string.
 *  This UCS-4BE character code string is "UCS-4BE".
 *  @return UCS-4BE character code string.
 */
extern const du_uchar* dcu_converter_code_ucs_4be(void);

/**
 *  Returns UTF-16 character code string.
 *  This UTF-16 character code string is "UTF-16".
 *  @return UTF-16 character code string.
 */
extern const du_uchar* dcu_converter_code_utf_16(void);

/**
 *  Returns UTF-16LE character code string.
 *  This UTF-16LE character code string is "UTF-16LE".
 *  @return UTF-16LE character code string.
 */
extern const du_uchar* dcu_converter_code_utf_16le(void);

/**
 *  Returns UTF-16BE character code string.
 *  This UTF-16BE character code string is "UTF-16BE".
 *  @return UTF-16BE character code string.
 */
extern const du_uchar* dcu_converter_code_utf_16be(void);

/**
 *  Returns UTF-32 character code string.
 *  This UTF-32 character code string is "UTF-32".
 *  @return UTF-32 character code string.
 */
extern const du_uchar* dcu_converter_code_utf_32(void);

/**
 *  Returns UTF-32LE character code string.
 *  This UTF-32LE character code string is "UTF-32LE".
 *  @return UTF-32LE character code string.
 */
extern const du_uchar* dcu_converter_code_utf_32le(void);

/**
 *  Returns UTF-32BE character code string.
 *  This UTF-32BE character code string is "UTF-32BE".
 *  @return UTF-32BE character code string.
 */
extern const du_uchar* dcu_converter_code_utf_32be(void);

/**
 *  Returns UTF-7 character code string.
 *  This UTF-7 character code string is "UTF-7".
 *  @return UTF-7 character code string.
 */
extern const du_uchar* dcu_converter_code_utf_7(void);

#ifdef __cplusplus
}
#endif

#endif
