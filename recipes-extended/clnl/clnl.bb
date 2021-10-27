SUMMARY = "Code Load n Lock is a library used to load and lock ELF files or sections into memory"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "1.0+git${SRCPV}"

SRCREV_clnl = "${AUTOREV}"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/clnl/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=clnl"

S = "${WORKDIR}/git"

FILES_${PN}_append = " ${includedir}/clnl.h"

inherit autotools coverity pkgconfig
