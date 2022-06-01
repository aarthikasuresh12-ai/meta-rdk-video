SUMMARY = "This receipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=bc14e971d0e95ae5ca07a810dcd5893b"

PV = "${RDK_RELEASE}+git${SRCPV}"

require rmfapp.inc

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git/rmfApp"

DEPENDS = "${RMFAPP_MEDIACLIENT_DEPENDS}"
DEPENDS_append_hybrid = " ${RMFAPP_HYBRID_DEPENDS}"

inherit autotools pkgconfig coverity

EXTRA_OECONF += "${@bb.utils.contains('COMBINED_FEATURES', 'rdk-playersinkbin', '--enable-mediaplayersink', '', d)}"

ENABLE_GST1 = "--enable-gstreamer1=${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'yes', 'no', d)}"
EXTRA_OECONF += "${ENABLE_GST1}"

export RDK_FSROOT_PATH = '='
export FSROOT = '='

EXTRA_OECONF_append_hybrid = " --enable-dvr"

RMFAPP_HYBRID_DEPENDS += " virtual/dvrmgr-hal dvrmgr"
CFLAGS_append_hybrid = " -I${STAGING_INCDIR}/rdk/podmgr"
CXXFLAGS_append_hybrid = " -I${STAGING_INCDIR}/rdk/podmgr"

