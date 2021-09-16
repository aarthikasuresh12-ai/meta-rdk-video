SUMMARY = "LEDMGR no OP sample implementation"

SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/ledmgr;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
PV = "${RDK_RELEASE}+gitr${SRCPV}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/src/extended/noop"
DEPENDS="devicesettings iarmmgrs ledmgr-headers"

PROVIDES = "virtual/ledmgr-extended"

inherit autotools pkgconfig coverity

do_install_append () {
# Install header required by the 'generic' ledmgr
    install -d ${D}${includedir}/ledmgr
    install -m 0644 ${S}/ledmgr.hpp ${D}${includedir}/ledmgr
}
