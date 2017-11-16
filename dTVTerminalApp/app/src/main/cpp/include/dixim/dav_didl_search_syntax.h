/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_search_syntax.h
 *  @brief The dav_didl_search_syntax interface provides methods for maminupating searching
 *  syntax data ( such as initialization, setting values).
 *  Searching conditions are specified by the search criteria.
 *  The search criteria is defined by the following syntax specification in Extended Backus-Naur Form (EBNF).<br><br>
 *      searchCriteria ::= searchEquation | asterisk <br>
 *      searchEquation ::= relEquation | searchEquation wChar+ logOperator wChar+ searchEquation | '(' wChar* searchEquation wChar* ')' <br>
 *      logOperator ::= 'and' | 'or' <br>
 *      relEquation ::= property name wChar+ binOperator wChar+ quotedValue | property name wChar+ existOperator wChar+ boolValue <br>
 *      binOperator ::= relOperator | stringOperator <br>
 *      relOperator ::= '=' | '!=' | '<' | '<=' | '>' | '>=' <br>
 *      stringOperator ::= 'contains' | 'doesNotContain' | 'derivedfrom' | 'startsWith' <br>
 *      existOperator ::= 'exists' <br>
 *      boolValue ::= 'true' | 'false' <br>
 *      quotedValue ::= dQuote escapedQuote dQuote <br>
 *      wChar ::= space | hTab | lineFeed | vTab | formFeed | return <br>
 *      escapedQuote ::= (*doube-quote escaped string *) <br>
 *      hTab ::= (* UTF-8 code 0x09, horizontal tab character *) <br>
 *      lineFeed ::= (* UTF-8 code 0x0A, line feed character *) <br>
 *      vTab ::= (* UTF-8 code 0x0B, vertical tab character *) <br>
 *      formFeed ::= (* UTF-8 code 0x0C, form feed character *) <br>
 *      return ::= (* UTF-8 code 0x0D, carriage return character *) <br>
 *      space ::= ' ' (* UTF-8 code 0x20, space character *) <br>
 *      dQuote ::= '"' (* UTF-8 code 0x22, double quote character *) <br>
 *      asterisk ::= '*' (* UTF-8 code 0x2A, asterisk character *) <br>
 *  <br>
 *  The search criteria consist of one or more search equations.
 *  The search equations may be combined using the 'and', and 'or' logOperator.
 *  The asterisk means matching all objects.
 *  The existOperator is used to check for existence of specified property.
 *  For Example, the criteria <br>
 *   'upnp:actor exists true <br>
 *  checks for existence of object which has upnp:acror property.
 *  The 'derivedfrom' stringOperator is used to search the objects which inherit specified class.
 *  For Example, the criteria <br>
 *   'upnp:class drivedfrom "object.item" <br>
 *  searches the "object.item" object and the objects which inherit "object.item" object.<br>
 *  "startsWith" operator is used for checking whether the lhs string starts with the rhs string.<br>
 *  The relOperator with escapedQuote of decimal digits leading signs is used to compare the numerical value. All alphabetic characters of operators and properies are case-insensitive.<br>
 *  The following examples show how to set the search criteria.<br>
 *  @li all photos which date is after May 5, 2003.<br>
 *   upnp:class = "object.item.imageItem.photo" and dc:date \> "2006-5-05" and item\@refID exists false<br>
 *  @li The jenre is JAZZ and the title is Stardust.<br>
 *   upnp:genre = "JAZZ" and dc:title = "Stardust" and item\@refID exists false<br>
 *  @li all music.<br>
 *   upnp:class = "object.item.audioItem.musicTrack" and item\@refID exists false<br>
 *  @li music artist.<br>
 *   upnp:class = "object.container.person.musicArtist"<br>
 *  @li music janre.<br>
 *   upnp:class = "object.container.genre.musicGenre"<br>
 *  @li music album.<br>
 *   upnp:class = "object.container.album.musicAlbum"<br>
 *  @li all videos.<br>
 *   upnp:class = "object.item.videoItem" and item\@refID exists false<br>
 *  @li video janre.<br>
 *   upnp:class = "object.container.genre.videoGenre"<br>
 *  @li actors of video.<br>
 *   upnp:class = "object.container.person"<br>
 *  @li all photos.<br>
 *   upnp:class = "object.item.imageItem" and item\@refID exists false<br>
 *
 */

#ifndef DAV_DIDL_SEARCH_SYNTAX_H
#define DAV_DIDL_SEARCH_SYNTAX_H

#include <du_type.h>

#include <dav_didl.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_uchar dav_didl_search_op;

typedef enum dav_didl_search_syntax_type {
    DAV_DIDL_SEARCH_SYNTAX_NONE,
    DAV_DIDL_SEARCH_SYNTAX_REL_EXP,
    DAV_DIDL_SEARCH_SYNTAX_BIN_OP,
    DAV_DIDL_SEARCH_SYNTAX_BOOL_VAL
} dav_didl_search_syntax_type;

typedef struct dav_didl_search_syntax_rel_exp {
    const dav_didl_search_op* op;
    const dav_didl_name* elem_name;
    const dav_didl_name* attr_name;
    du_uchar* value;
} dav_didl_search_syntax_rel_exp;

typedef struct dav_didl_search_syntax {
    dav_didl_search_syntax_type type;
    union {
        dav_didl_search_syntax_rel_exp rel_exp;
        const dav_didl_name* bin_op;
        du_bool bool_val;
    } syntax;
} dav_didl_search_syntax;

/**
 * Initializes a dav_didl_search_syntax data area.
 * @param[out] x  pointer to the dav_didl_search_syntax data structure.
 */
extern void dav_didl_search_syntax_init(dav_didl_search_syntax* x);

/**
 * Switches <em>x</em> to unused state.
 * @param[in,out] x pointer to the dav_didl_search_syntax structure.
 */
extern void dav_didl_search_syntax_free(dav_didl_search_syntax* x);

/**
 * Frees the region used by <em>x</em> and
 * switches <em>x</em> to unused state.
 * @param[in,out] x pointer to the dav_didl_search_syntax structure.
 */
extern void dav_didl_search_syntax_free_object(dav_didl_search_syntax* x);

/**
 * Sets a search criteria (a operator, an element name, an attribute name,
 * and a value ) in <em>x</em> to retrieve data.
 * @param[out] x pointer to the dav_didl_search_syntax structure.
 * @param[in] op A string that contains the name of operator.
 * @param[in] elem_name A string that contains the name of element.
 * @param[in] attr_name A string that contains the name of attribute.
 * @param[in] value A string that contains the value.
 * @return  true if the function succeeds.
 *          false if the  function fails.
 */
extern du_bool dav_didl_search_syntax_set_rel_exp(dav_didl_search_syntax* x, const dav_didl_search_op* op, const dav_didl_name* elem_name, const dav_didl_name* attr_name, du_uchar* value);

/**
 * Sets a bin_operator in <em>x</em> to retrieve data.
 * @param[out] x pointer to the dav_didl_search_syntax structure.
 * @param[in] bin_op A string that contains the name of bin_operator.
 * @return  true if the function succeeds.
 *          false if the  function fails.
 * @remark <em>bin_op</em> is one of the following strings.<br>
 * "=","!=","<","<=",">",">=","contains","doesNotContain","derivedfrom","startsWith"
 */
extern du_bool dav_didl_search_syntax_set_bin_op(dav_didl_search_syntax* x, const dav_didl_name* bin_op);

/**
 * Sets a value of type du_bool in <em>x</em> to retrieve data.
 * @param[out] x pointer to the dav_didl_search_syntax structure.
 * @param[in] bool_val a value of type du_bool.
 * @return  true if the function succeeds.
 *          false if the  function fails.
 */
extern du_bool dav_didl_search_syntax_set_bool_val(dav_didl_search_syntax* x, du_bool bool_val);

#ifdef __cplusplus
}
#endif

#endif
