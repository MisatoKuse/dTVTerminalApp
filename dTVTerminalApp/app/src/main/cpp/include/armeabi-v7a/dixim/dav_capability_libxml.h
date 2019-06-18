/*
 * Copyright (c) 2009 DigiOn, Inc. All rights reserved.
 */

/**
 * @file dav_capability_libxml.h
 * @brief The dav_capability_libxml interface provides methods for capability
 *  based DIDL-Lite res element selection. See dav_capability document more details.
 */

#ifndef DAV_CAPABILITY_LIBXML_H
#define DAV_CAPABILITY_LIBXML_H

#include <dav_capability.h>
#include <libxml/tree.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Gets the media class of <em>obj</em>.
 * If the upnp:class value is derived from object.item.audioItem, <em>cls</em> will be DAV_CAPABILITY_AUDIO.
 * If the upnp:class value is derived from object.item.videoItem, <em>cls</em> will be DAV_CAPABILITY_VIDEO.
 * If the upnp:class value is derived from object.item.imageItem, <em>cls</em> will be DAV_CAPABILITY_IMAGE.
 * If the upnp:class value is derived from object.item.textItem, <em>cls</em> will be DAV_CAPABILITY_TEXT.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] obj object.
 * @param[out] cls media class.
 * @return  true if the function succeeds, otherwise false.
 * @remark This function returns true only if <em>obj</em> is derved from object.item.
 */
extern du_bool dav_capability_libxml_get_media_class_from_object(dav_capability* x, const xmlNodePtr obj, dav_capability_class* cls);

/**
 * Gets the media class of <em>obj</em>.
 * If the upnp:class value is derived from object.container.person.musicArtist, <em>cls</em> will be DAV_CAPABILITY_AUDIO.
 * If the upnp:class value is derived from object.container.album.musicAlbum, <em>cls</em> will be DAV_CAPABILITY_AUDIO.
 * If the upnp:class value is derived from object.container.genre.musicGenre, <em>cls</em> will be DAV_CAPABILITY_AUDIO.
 * If the upnp:class value is derived from object.container.person.movieActor, <em>cls</em> will be DAV_CAPABILITY_VIDEO.
 * If the upnp:class value is derived from object.container.album.videoAlbum, <em>cls</em> will be DAV_CAPABILITY_VIDEO.
 * If the upnp:class value is derived from object.container.genre.movieGenre, <em>cls</em> will be DAV_CAPABILITY_VIDEO.
 * If the upnp:class value is derived from object.container.album.photoAlbum, <em>cls</em> will be DAV_CAPABILITY_IMAGE.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] obj object.
 * @param[out] cls media class.
 * @return  true if the function succeeds, otherwise false.
 * @remark This function returns true only if <em>obj</em> is derved from object.container.
 */
extern du_bool dav_capability_libxml_get_media_class_from_object2(dav_capability* x, const xmlNodePtr obj, dav_capability_class* cls);

/**
 * Prioritizes res or upnp:albumArtURI properties included in <em>obj</em> by
 * <em>cls</em>, and returns the <em>pos</em>th highest-prioritized property.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] doc document.
 * @param[in] elem_pos element position starting from 0 in <em>doc</em>.
 * @param[in] obj object which contains res or upnp:albumArtURI properties.
 * @param[in] cls media class.
 * @param[in] pos position starting from 0.
 *            Specify 0 to retrieve the highest-prioritized property.
 * @return  supported <em>pos</em>th highest-prioritized property.
 *          Returns NULL when there is no supported property.
 * @remark It is recommended not to use this function because it has problems with efficiency and I/F.
 * Use dav_capability_libxml_get_supported_property2().
 */
extern xmlNodePtr dav_capability_libxml_get_supported_property(dav_capability* x, const xmlDocPtr doc, du_uint32 elem_pos, const xmlNodePtr obj, dav_capability_class cls, du_uint32 pos);

/**
 * Prioritizes res or upnp:albumArtURI properties included in <em>obj</em> by
 * <em>cls</em>, and returns the <em>pos</em>th highest-prioritized property.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] obj object which contains res or upnp:albumArtURI properties.
 * @param[in] cls media class.
 * @param[in] pos position starting from 0.
 *            Specify 0 to retrieve the highest-prioritized property.
 * @return  supported <em>pos</em>th highest-prioritized property.
 *          Returns NULL when there is no supported property.
 */
extern xmlNodePtr dav_capability_libxml_get_supported_property2(dav_capability* x, const xmlNodePtr obj, dav_capability_class cls, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
