SUMMARY = "Sectionfilter Broadcom HAL implementation"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=bc14e971d0e95ae5ca07a810dcd5893b"

PV = "${RDK_RELEASE}+git${SRCPV}"

PV = "${RDK_RELEASE}+gitr${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=rmfgeneric"

S = "${WORKDIR}/git/soc_qamsource_stub"

DEPENDS += " rmfgenericheaders rdk-logger rmfosal"

SRCREV = "${AUTOREV}"

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', '--enable-stubcode=yes', '--enable-stubcode=no', d)}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', '--enable-podmgr=no', '--enable-podmgr=yes', d)}"
PROVIDES = "virtual/sectionfilter-hal"
RPROVIDES_${PN} = "virtual/sectionfilter-hal"

inherit autotools pkgconfig coverity
