LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit autotools pkgconfig

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/crashlog;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=crashlog"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV_crashlog = "${AUTOREV}"
SRCREV_FORMAT = "crashlog"

S = "${WORKDIR}/git"

