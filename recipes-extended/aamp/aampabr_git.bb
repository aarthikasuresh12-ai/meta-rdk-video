SUMMARY = "RDK AAMP ABR library"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}"
AAMP_ABR_RELEASE_TAG_NAME = "3.8.1.0"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/aampabr;protocol=${CMF_GIT_PROTOCOL};branch=${AAMP_ABR_RELEASE_TAG_NAME};nobranch=1;name=aamp-abr"

SRCREV_aamp-abr = "${AUTOREV}"
SRCREV_FORMAT = "aamp-abr"


S = "${WORKDIR}/git/"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"


inherit cmake coverity
EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' -DCMAKE_SYSTEMD_JOURNAL=1', '', d)}"

FILES_SOLIBSDEV = ""
SOLIBS = ".so"
