SUMMARY = "media-utils"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"
PN = "media-utils"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/media_utils;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

PROVIDES = "virtual/media-utils"
RPROVIDES_${PN} = "virtual/media-utils"

DEPENDS = "media-utils-headers glib-2.0"

inherit autotools pkgconfig

CFLAGS_append = " \
    -I${STAGING_INCDIR}/media-utils \
    -I${STAGING_INCDIR}/media-utils/audioCapture \
    "
