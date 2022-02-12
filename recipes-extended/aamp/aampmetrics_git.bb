SUMMARY = "RDK AAMP Metrics library"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"
AAMP_METRICS_RELEASE_TAG_NAME = "4.1.1.0"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/aampmetrics;protocol=${CMF_GIT_PROTOCOL};branch=${AAMP_METRICS_RELEASE_TAG_NAME};nobranch=1"

SRCREV = "${AUTOREV}"

DEPENDS = "cjson"

S = "${WORKDIR}/git/"

inherit cmake coverity

FILES_SOLIBSDEV = ""
SOLIBS = ".so"
