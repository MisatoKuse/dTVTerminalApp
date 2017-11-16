/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dav_capability.h
 *  @brief The dav_capability interface provides methods for capability
 *  based DIDL-Lite res element selection.
 *
 * \section sec1 Definitions of the terms and notations
 *  - RESOURCE(S) \n
 *      Indicates res or albumArtURI included in dav_didl_object property.
 *  - Non-DLNA RESOURCE \n
 *      Indicates res RESOURCE that has no pn-param in res\@protocolInfo.
 *  - {capability - unsupported - profile} \n
 *      Indicates the reference to XML tag. (The example above refers to the profile tag whose parent is unsupported.)
 *
 *
 *  \section sec2 Overview
 *  dav_capability mainly provides the following 3 features:
 *      - Feature to determine whether RESOURCE can be handled by renderer, based on the renderer capability. \n
 *          dav_capability_is_supported()
 *      - Feature to evaluate RESOURCE based on the renderer capability and to sort RESOURCES with the evaluation result. \n
 *         dav_capability_sort()
 *      - Feature to generate sink protocol info that is to be exposed on CMS based on the renderer capability.\n
 *         dav_capability_make_sink_protocol_info()
 *
 *
 *  \section sec3 Description method of the renderer capability
 *  \subsection subsec3_1 - XML format
 *  The renderer capability should be written in XML format that is complied with below:
 *  XML information should be set to dav_capability in advance by using dav_capability_set_capability() or dav_capability_set_capability_file().

 *  \verbatim
<capability>
    <unsupported>
        <profile></profile>
    </unsupported>
    <video>
        <priority>
            <originality />
            <operability />
            <resolution algorithm="" />
            <consistency />
            <bitrate />
            <scanMethod method="" />
        </priority>
        <format nonDlnaContentsSupport="">
            <priority></priority>
            <protocol></protocol>
            <mimeType></mimeType>
            <supportedParam>
                <pn></pn>
                <op></op>
                <ps></ps>
                <flags></flags>
            </supportedParam>
            <requiredParam>
                <pn></pn>
                <op></op>
                <ps></ps>
                <flags></flags>
            </requiredParam>
            <limit>
                <name></name>
                <range>
                    <maximum></maximum>
                    <minimum></minimum>
                </range>
            </limit>
            <limit>
                <name></name>
                <value></value>
            </limit>
        </format>
    </video>
    <image>
        <priority>
            <originality />
            <resolution algorithm="" />
            <consistency />
        </priority>
        <format>
            ...
        </format>
    </image>
    <thumbnail>
        <priority>
            <originality />
            <resolution algorithm="" />
        </priority>
        <format>
            ...
        </format>
    </thumbnail>
    <audio>
        <priority>
            <originality />
            <operability />
            <sampleFrequency />
            <bitsPerSample />
            <nrAudioChannels />
            <consistency />
        </priority>
        <format>
            ...
        </format>
    </audio>
    <text>
        <priority>
            <originality />
            <operability />
            <consistency />
        </priority>
        <format>
            ...
        </format>
    </text>
</capability>
\endverbatim
 *
 *  \subsection subsec3_2 XML overview
 * XML consists of the MEDIAs - video, image, thumbnail, audio, and text.
 * The information regarding the media that renderer supports should be described in each MEDIA item.
 * More than one FORMATs can be written in MEDIA.
 * FORMATs exist in one mime-type (or more than one similar mime-types).
 * For example, the image MEDIA can contain FORMATs with regard to jpeg and png.
 * The information about mime-type that is supposed by renderer should be described in each FORMAT item.
 *
 *  \subsection subsec3_3 Details of XML
 *  - Element or attribute having R in [] cannot be omitted.
 *  - Element or attribute having M in [] can have more than one tags having a same name.
 *  - The value of the element or attribute having D in [] should be an integer bigger than zero.
 *  - The value of the element or attribute having C in [] should be in CSV format.
 *  - The value of the element or attribute having E in [] should be empty.
 *
 *  \verbatim
capability
    [R] Root element

    unsupported
        [] Unsupported item by renderer is described in the descendant of this tag.

        profile
            [C] Profile IDs which are not supported by renderer and are defined by DLNA are described in csv format.
            When RESOURCE is a DLNA content,
            renderer recognizes it as unsupported if the value of this profile is included in pn-param of fourth field in res@protocolInfo.

    video
        [] renderer capability and selecting/sorting criteria of RESOURCES for video are described in the descendant of this tag.

        priority
            [] sorting criteria of RESOURCES for video is described in the child of this tag.
For details of the sorting method, refer to "Sorting RESOURCES" section.

            originality
                [E] Indicates whether or not RESOURCE is converted (not original).
                The evaluated value of the RESOURCE whose ci-param values is "0" (original) is higher than the one of the RESOURCE whose ci-param values is "1" (not original).

            operability
                [E] Indicates whether or not RESOURCE supports byte seek, time seek, and plays peed.
The more seek modes the RESOUCE supports, the higher the evaluated value of the RESOUCE will be.

            resolution
                [E] Indicates the scaling rate and the height of resolution when displaying RESOUCE by renderer.
                Evaluation methods for RESOURCEs depend on the algorithms specified in the the algorithm attribute described below.
                When a RESOURCE has no profile ID, it will be evaluated with res@resolution value 640 x 480 (JPEG_SM).

                resolution@algorithm
                    [] Specifies algorithm for evaluation. The following three algorithms can be specified:
                    When not specified, "larger nearest" will be selected for evaluation.
                    "nearest"
                        The closer the res@resolution of the RESOURCE to the resolution set by the user, the higher the evaluated value of the RESOURCE will be.
                    "larger nearest"
                        Same as "nearest", except the following:
                        - When a RESOURCE  has resolution that is bigger than the one set by the user, its evaluated value will be higher than the one for a  RESOURCE  whose resolution is smaller than the one set by the user.
                    "largest"
                        The bigger the res@resolution of RESOURCE, the higher the evaluated value of the RESOURCE will be.
                    * "the resolution set by the user", mentioned above, means:
                    When MEDIA is image or video, the value set by dav_capability_set_resolution().
                    When MEDIA is thumbnail, the value set by dav_capability_set_resolution_for_thumbnail().

            consistency
                [E] Indicates the consistency with the format that RESOURCE is associated with.
                When {capability - * - format - supportedParam - pn } is defined in the associated format, the evaluated value will be the largest if RESOURCE's pn-param exists in the definition; If not, the evaluated value will be the smallest.
                When {capability - * - format - supportedParam - pn } is NOT defined in the associated format, the evaluated value will be the largest if pn-param exists in the RESOURCE; If not, the evaluated value will be the smallest.

            bitrate
                [E] Item regarding the value of RESOURCE's res@bitrate.
                The bigger the value of RESOURCE's res@bitrate is, the higher the evaluated value of the RESOURCE will be.

            scanMethod
                [E] Item regarding the value of RESOURCE's res@dixim:scanMethod.
                Evaluation methods for RESOURCEs depend on the method specified in the the method attribute described below.

                scanMethod@method
                    [R] Specifies method for evaluation. The following two methods can be specified:

                    "progressive"
                        The evaluated value of the RESOURCE whose res@dixim:scanMethod values is "progressive" is higher than the one of the RESOURCE whose res@dixim:scanMethod values is "interlace".

                    "interlace"
                        The evaluated value of the RESOURCE whose res@dixim:scanMethod values is "interlace" is higher than the one of the RESOURCE whose res@dixim:scanMethod values is "progressive".

        format
            [M] Information about the profile (or mime-type) supported by renderer is described in the descendant of this tag.

            Each RESOURCE can be associated with one FORMAT.
            When RESOURCE is not associated with any FORMAT, renderer regards it as unsupported.
            For the conditions to associate RESOURCE with FORMAT, refer to the description for mimeType of its child or supportedParam tag.

            format@nonDlnaContentsSupport
                [] When permitting Non-DLNA RESOURCE to associate with this FORMAT, set "1".
                If not, set "0".

            priority
                [RD] Priority used for sorting RESOURCEs.
                The bigger the value is, the higher the priority is.
                For the RESOURCEs having the same the evaluated value obtained by {capability - video - priority}, the RESOURCE having higher priority will be regarded that it has the higher evaluated value.

            protocol
                [RC] Protocol that this FORMAT has.
                When the first field of res@protocolInfo of a RESOURCE is different from this value, the RESOURCE cannot be associated with this FORMAT.
                When more than one values are describe in CSV format, only the first value will be used for outputting sink protocol info.

            mimeType
                [RC] mime-type that this FORMAT has.
                When meeting the conditions below, a RESOURCE can be associated with this FORMAT:
        1. pn-param does not exist in the fourth field of res@protocolInfo of a RESOURCE.
        2. content type of the third field of protocolInfo is included in mimeType value.


            supportedParam
                [] Describes the Renderer capability for this FORMAT in the param format specified  by DLNA.
                This value is used to determine whether the RESOURCE can be associated with this FORMAT. Also, it is used to output sink protocol info as well.

                pn
                    [CM] Describes the profile ID that is supported by renderer in pn-param format specified by DLNA.
                    When meeting the conditions below, a RESOURCE can be associated with this FORMAT:
                      1. pn-param exists in the fourth field of res@protocolInfo of a RESOURCE.
                      2. c pn-param is included in this value.

                      When dlna:profileID attribute exists in albumArtURI RESOURCE and this attribute is included in its pn value, the albumArtURI RESOURCE can be associated with this FORMAT.
                      When this value is specified, it will be reflected to pn-param of sink protocol info that corresponds to this FORMAT.

                    pn@name
                        [] Describes the kind of target profile.
                        When omitting this, the profile will be handled as DLNA.ORG_PN.

                op
                    [] Describes the operation supported by renderer in op-param format specified by DLNA.
                    When this value is specified, it will be reflected to op-param of sink protocol info that corresponds to this FORMAT.

                ps
                    [CD] Describes the play speed supported by renderer in ps-param format specified by DLNA.
                    When this value is specified, it will be reflected to ps-param of sink protocol info that corresponds to this FORMAT.

                flags
                    [] Describes the flags supported by renderer in flags-param format specified by DLNA.
                    When this value is specified, it will be reflected to flags-param of sink protocol info that corresponds to this FORMAT.

            requiredParam
                [] Describes the capability of renderer that corresponds to this FORMAT in the param format specified by DLNA.
                The format is same as supportedParam except the followings:
                - If a RESOURCE does not meet the set value, it will not be associated with this FORMAT.
                - The set value is not used to output sink protocol info.

                pn
                    [C] Describes the profile ID that is mandatory for renderer in the pn-param format specified by DLNA.

                op
                    []Describes the operation that is mandatory for renderer in the op-param format specified by DLNA.
                    If any positions of the RESOURCE, where this value is 1, do not match with the position where RESOURCE’s op-param is 1, the RESOURCE cannot be associated with this FORMAT.

                ps
                    [CD] Describes the play speed that is mandatory for renderer in the ps-param format specified by DLNA.
                    A RESOURCE whose ps-param value does not include this value cannot be associated with this FORMAT.

                flags
                    [] Describes the flags that is mandatory for renderer in the flags-param format specified by DLNA.
                    If any bit positions of the RESOURCE, where this value is 1, do not match with the bit position where RESOURCE's flags-param is 1, the RESOURCE cannot be associated with this FORMAT.

            limit
                [M] Describes the limit value of capability of the renderer corresponding to this FORMAT in RESOURCE's attribute.
                The set value is used to determine whether the RESOURCE can be associated with this FORMAT.
                The child tag should always include range or value.

                name
                    [R] Item name. The following values are allowed.
                    "bitrate"
                        Limitation on res@bitrate.
                    "colordepth"
                        Limitation on res@colorDepth.
                    "duration"
                        Limitation on res@duration.
                    "height"
                        Limitation on height in res@resolution.
                    "nraudiochannels"
                        Limitation on res@nrAudioChannels.
                    "samplefrequency"
                        Limitation on res@sampleFrequency.
                    "size"
                        Limitation on res@size.
                    "width"
                        Limitation on width in res@resolution.
                    "scanMethod"
                        Limitation on res@dixim:scanMode.
                        Set 1 to the value if res@dixim:scanMode is "progressive".
                        Set 2 to the value if res@dixim:scanMode is "interlace".

                range
                    maximum
                        [D] Maximum value of the attribute that corresponds to limit's item name (name).

                    minimum
                        [D] Minimum value of the attribute that corresponds to limit's item name (name).

                value
                    [DC] Describes the values that are acceptable for the attribute that corresponds to limit's item name (name) in CSV format.


    image
        [] renderer capability and selecting/sorting criteria of RESOURCES for image are described in the descendant of this tag.

        priority
            [] sorting criteria of RESOURCES for image is described in the child of this tag.
For details of the sorting method, refer to "Sorting RESOURCES" section.

            originality
                [E] Refer to {capability - video - priority - originality}.

            resolution
                [E] Refer to {capability - video - priority - resolution}.

            consistency
                [E] Refer to {capability - video - priority - consistency}.

        format
            Refer to {capability - video - format}.

    thumbnail
        [] renderer capability and selecting/sorting criteria of RESOURCES for thumbnail are described in the descendant of this tag.

        priority
            [] sorting criteria of RESOURCES for thumbnail is described in the child of this tag.
For details of the sorting method, refer to "Sorting RESOURCES" section.

            originality
                [E] Refer to {capability - video - priority - originality}.

            resolution
                [E] Refer to {capability - video - priority - resolution}.

            consistency
                [E] Refer to {capability - video - priority - consistency}.

        format
            Refer to {capability - video - format}.

    audio
        [] renderer capability and selecting/sorting criteria of RESOURCES for audio are described in the descendant of this tag.

        priority
            [] sorting criteria of RESOURCES for audio is described in the child of this tag.
For details of the sorting method, refer to "Sorting RESOURCES" section.

            originality
                Refer to {capability - video - priority - originality}.

            operability
                Refer to {capability - video - priority - operability}.

            sampleFrequency
                [E] Item regarding the value of RESOURCE s res@sampleFrequency.
                The bigger the value of RESOURCE's res@sampleFrequency is, the higher the evaluated value of the RESOURCE will be.

            bitsPerSample
                [E] Item regarding the value of RESOURCE s res@bitsPerSample.
                The bigger the value of RESOURCE's res@bitsPerSample is, the higher the evaluated value of the RESOURCE will be.

            nrAudioChannels
                [E] Item regarding the value of RESOURCE s res@nrAudioChannels.
                The bigger the value of RESOURCE's es@nrAudioChannels is, the higher the evaluated value of the RESOURCE will be.

            consistency
                [E] Refer to {capability - video - priority - consistency}.

        format
            Refer to {capability - video - format}.

    text
        [] renderer capability and selecting/sorting criteria of RESOURCES for text are described in the descendant of this tag.

        priority
            [] sorting criteria of RESOURCES for text is described in the child of this tag.
For details of the sorting method, refer to "Sorting RESOURCES" section.

            originality
                [E] Refer to {capability - video - priority - originality}.

            operability
                [E] Refer to {capability - video - priority - operability}.

            consistency
                [E] Refer to {capability - video - priority - consistency}.

        format
            Refer to {capability - video - format}.
    \endverbatim
 *
 *
 *  \section sec4 How to determine whether or not RESOURCE can be handled by renderer.
 *  Providing RESOURCE and the target MEDIA to dav_capability_is_supported() allows you to determine whether or not the RESOURCE is supported by renderer.
 *
 *  \subsection sec4_1 res
 *  When a res RESOURCE meets one or more conditions below, it will be determined that the res RESOURCE is not supported by renderer; dav_capability_is_supported() will return false.
 *
 *  - bitrate set by dav_capability_set_max_bitrate() is zero.
 *  - No FORMAT that can be associated with the RESOURCE exists.
 *  - The MEDIA specified by the argument of dav_capability_is_supported() differs from the MEDIA of the FORAMT that is associated with the RESOURCE.
 *  - The following attributes of the RESOURCE are zero:
 *      - res\@duration
 *      - res\@bitrate
 *      - res\@resolution
 *      - res\@size
 *  - When RESOURCE's MEDIA is audio or video and res\@bitrate exits in the RESOURCE, RESOURCE's res\@bitrate is lower than the bitrate set by dav_capability_set_max_bitrate().
 *
 *
 *  \subsection sec4_2 albumArtURI
 *  When an albumArtURI RESOURCE meets one or more conditions below, it will be determined the albumArtURI RESOURCE is not supported by renderer; dav_capability_is_supported() will return false.
 *
 *  - bitrate set by dav_capability_set_max_bitrate() is zero.
 *  - MEDIA specified by dav_capability_is_supported() is not thumbnail.
 *  - albumArtURI RESOURCE's profileID is included in {capability - unsupported - format}.
 *
 *
 *  \section sec5 Sorting RESOURCES
 *  Using dav_capability_sort() allows you to evaluate the RESOURCE included in dav_didl_object by using the evaluation method based on renderer capability and to sort RESOURCES.
 *  Sorting RESOURCES means to sort the RESOURCES included in dav_didl_object in descending order of the evaluated values of the RESOURCES.
 *  When more than one RESOURCEs exit for one content, this sorting helps renderer to select one RESOURCE to play.
 *  RESOURCEs that are not supported by dav_capability_is_supported() will not be sorted.
 *
 *  The following shows how to evaluate RESOURCE:
 *
 *  (1) Evaluation for each MEDIA
 *  Using {capability - (sorting target MEDIA) - priority}, evaluated values are calculated by evaluating each item of the children. Evaluation is done sequentially starting from the upmost child.
 *  The evaluated value obtained from a lower child is always smaller than the value from an upper child.
 *
 *  (2) Evaluation for each FORMAT
 *  When RESOURCES obtain an equivalent value by the calculation described above (1), evaluation for each FORMAT is performed to differentiate the values of these RESOURCES.
 *  The following tag's value is used for evaluation.
 *  {capability - (sorting target MEDIA) - format - priority}
 *
 *
 *  \section sec6 Generating sink protocol info
 *  Executing dav_capability_make_sink_protocol_info() allows you to generate sink protocol info based on renderer capability.
 *
 *  sink protocol info is generated following the rules below:
 *  - With the exceptions below, the number of protocol info that is generated for one FORMAT will be the number of the elements of {capability - * - format - supportedParam - pn}.
 *      - When the attribute value of nonDlnaContentsSupport is "1", one protocol info having no pn-param can be added to protocol info above.
 *      - For the FORMAT whose {capability - audio - format - mimeType} value is audio/L16, more than one mimetypes' protocol info will be generated depending on {capability - audio - format - limit}.
 *  - {capability - * - format - protocol} is used for protocol info's first field. \n
 *     When not defined, an asterisk ("*") is used.
 *  - protocol info's second field's is always an asterisk ("*").
 *  - The first element of {capability - * - format - mimeType} is used for protocol info's third field. \n
 *     When not defined, an asterisk ("*") is used.
 *  - All of {capability - * - format - supportedParam - pn} is used for pn-param of protocol info's fourth field. \n
 *     When not defined, it will not be described.
 *  - {capability - * - format - supportedParam - op} is used for op-param of protocol info's fourth field. \n
 *     When not defined, it will not be described.
 *  - {capability - * - format - supportedParam - ps} is used for ps-param of protocol info's fourth field. \n
 *     When not defined, it will not be described.
 *  - {capability - * - format - supportedParam - flags} is used for flags-param of protocol info's fourth field. \n
 *    When not defined, it will not be described.
 */

#ifndef DAV_CAPABILITY_H
#define DAV_CAPABILITY_H

#include <dav_didl_object.h>
#include <dav_didl_object_property_array.h>
#include <dav_capability_priority.h>
#include <dav_capability_format_array.h>
#include <dav_capability_cache.h>
#include <dav_protocol_info.h>
#include <dav_content_features.h>
#include <du_str_array.h>
#include <du_uint32_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Enumeration of media class.
 */
typedef enum {
    DAV_CAPABILITY_UNKNOWN, /*< Unknown */
    DAV_CAPABILITY_VIDEO, /*< Video */
    DAV_CAPABILITY_IMAGE, /*< Image */
    DAV_CAPABILITY_AUDIO, /*< Audio */
    DAV_CAPABILITY_THUMBNAIL, /*< Thumbnail */
    DAV_CAPABILITY_TEXT, /*< Text */
} dav_capability_class;


typedef struct dav_capability_resolution {
    du_uint32 width;
    du_uint32 height;
} dav_capability_resolution;

/**
 * This structure contains the information of capability.
 */
typedef struct dav_capability {
    du_uint32 max_bits_per_second;
    dav_capability_resolution resol;
    dav_capability_resolution thumb_resol;
    du_str_array unsupported_profile_array;
    dav_capability_format_array format_array;

    dav_capability_priority priority[5];

    dav_capability_cache _cache;

    du_uint8 select_exif_res_from_dixim_dms;
} dav_capability;

/**
 * Initializes a dav_capability structure area.
 * @param[out] x  pointer to the dav_capability structure.
 */
extern du_bool dav_capability_init(dav_capability* x);

/**
 * Frees the region used by <em>x</em>.
 * @param[in] x pointer to the dav_capability structure.
 */
extern void dav_capability_free(dav_capability* x);

/**
 * Reset all information stored in <em>x</em>.
 * @param[in] x  pointer to the dav_capability structure.
 */
extern du_bool dav_capability_reset(dav_capability* x);

/**
 * Sets the maximam bandwidth of current network.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] bits_per_second maximam bitrate in bits per second.
 *            0 means infinity.
 */
extern void dav_capability_set_max_bitrate(dav_capability* x, du_uint32 bits_per_second);

/**
 * Sets the resolution of current output for image and video.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] width width.
 * @param[in] height height.
 */
extern void dav_capability_set_resolution(dav_capability* x, du_uint32 width, du_uint32 height);

/**
 * Sets the resolution of current output for thumbnail.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] width width.
 * @param[in] height height.
 */
extern void dav_capability_set_resolution_for_thumbnail(dav_capability* x, du_uint32 width, du_uint32 height);

/**
 * Sets the capability of device.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] cap_xml device capability represented in XML.
 * @param[in] xml_len the size of cap_xml.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_capability_set_capability(dav_capability* x, const du_uchar* cap_xml, du_uint32 xml_len);

/**
 * Sets the capability of device using file.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] path file path.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_capability_set_capability_file(dav_capability* x, const du_uchar* path);

/**
 * Sets the capability of device by using one or more protocol infos.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] protocol_info one or more protocol in csv format.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_capability_set_capability_by_protocol_info(dav_capability* x, const du_uchar* protocol_info);

/**
 * Tests the <em>prop</em> property is supported format.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] prop property of DIDL-Lite object.
 * @param[in] cls media class.
 * @return  true if supports.
 *          false if not supports.
 */
extern du_bool dav_capability_is_supported(dav_capability* x, const dav_didl_object_property* prop, dav_capability_class cls);

/**
 * Tests the <em>pos</em>-th property in <em>obj</em>  is supported format.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] obj object which contains res properties.
 * @param[in] pos target property position in <em>obj</em>. This must be a number started from 0.
 * @param[in] cls media class.
 * @return  true if supports.
 *          false if not supports.
 */
extern du_bool dav_capability_is_supported2(dav_capability* x, const dav_didl_object* obj, du_uint32 pos, dav_capability_class cls);

extern du_bool dav_capability_is_supported_2(dav_capability* x, const dav_didl_object_property* prop, dav_capability_class cls, dav_protocol_info* pi, dav_content_features* cf, dav_capability_format** fmt);

/**
 * Sorts the res and upnp:albumArtURI properties in order of priority.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in,out] obj object which contains res properties.
 *                res properties in <em>obj</em> are sorted in order of priority.
 * @param[in] cls media class.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_capability_sort(dav_capability* x, dav_didl_object* obj, dav_capability_class cls);

/**
 * Prioritizes res properties included in <em>obj</em> by <em>cls</em>,
 * and stores them in <em>prop_array</em> in order of their priorities.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] obj object which contains res properties.
 * @param[in] cls media class.
 * @param[out] prop_array set of the res properties in <em>obj</em>.
 *             res properties are stored in order of their priorities.
 *             Unsupported res properties are not stored.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>prop_array</em> will be disabled once the object of <em>obj</em> is freed.
 */
extern du_bool dav_capability_get_supported_properties(dav_capability* x, const dav_didl_object* obj, dav_capability_class cls, dav_didl_object_property_array* prop_array);

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
extern dav_didl_object_property* dav_capability_get_supported_property(dav_capability* x, const dav_didl_object* obj, dav_capability_class cls, du_uint32 pos);

/**
 * Generate sink protocol info that the Connection Manager Services's
 * GetProtocolInfo action needs.
 * @param[in] x pointer to the dav_capability structure.
 * @param[out] sink_protocol_info destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_capability_make_sink_protocol_info(dav_capability* x, du_uchar_array* sink_protocol_info);

/**
 * Gets the media class of <em>obj</em>.
 * If the upnp:class value is derived from object.item.audioItem, <em>cls</em> will be DAV_CAPABILITY_AUDIO.
 * If the upnp:class value is derived from object.item.videoItem, <em>cls</em> will be DAV_CAPABILITY_VIDEO.
 * If the upnp:class value is derived from object.item.imageItem, <em>cls</em> will be DAV_CAPABILITY_IMAGE.
 * If the upnp:class value is derived from object.item.textItem, <em>cls</em> will be DAV_CAPABILITY_TEXT.
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] obj object.
 * @param[out] cls media class.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function returns true only if <em>obj</em> is derved from object.item.
 */
extern du_bool dav_capability_get_media_class_from_object(dav_capability* x, const dav_didl_object* obj, dav_capability_class* cls);

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
 * @param[in] obj target object.
 * @param[out] cls media class.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function returns true only if <em>obj</em> is derved from object.container.
 */
extern du_bool dav_capability_get_media_class_from_object2(dav_capability* x, const dav_didl_object* obj, dav_capability_class* cls);

/**
 * Tweaks the dav_capability.
 *
 * This API allows to tweak the <em>x</em>.
 *
 * @param[in] x pointer to the dav_capability structure.
 * @param[in] id ID that represents tweak item.
 * @param[in] value tweak value corresponding the <em>id</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 *
 * Currently, the follwing tweak ids are supported:
 *  - SELECT_EXIF_RES_FROM_DIXIM_DMS<br/>
 *      Change a setting of how to select exif resources from DiXiM Media Server.<br/>
 *      Set <em>value</em> to (const void*)1 if select them. This is default.<br/>
 *      Set <em>value</em> to (const void*)2 if select them with lowest priority.<br/>
 *      Set <em>value</em> to NULL if not select them.
 */
extern du_bool dav_capability_tweak(dav_capability* x, const du_uchar* id, const void* value);

#ifdef __cplusplus
}
#endif

#endif
