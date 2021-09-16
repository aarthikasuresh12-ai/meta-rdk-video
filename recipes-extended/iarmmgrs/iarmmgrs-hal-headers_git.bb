SUMMARY = "IARMmgrs HAL definition"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d8db96e7ee90f3821eb5e7e913a7b2a"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/iarmmgrs;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=iarmmgrsheaders"

S = "${WORKDIR}/git"

# this is a HAL package only, nothing to build
do_compile[noexec] = "1"

# also get rid of the default dependency added in bitbake.conf
# since there is no 'main' package generated (empty)
RDEPENDS_${PN}-dev = ""

DEPENDS += "iarmbus"

do_install() {
    install -d ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/hal/include/*.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/ir/irMgrInternal.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/ir/IrInputRemoteKeyCodes.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/power/pwrlogger.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/mfr/include/mfrMgr.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/mfr/include/mfrTypes.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/mfr/include/mfr_wifi_types.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/mfr/include/mfr_wifi_api.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/mfr/include/mfr_temperature.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/ir/include/irMgr.h ${D}${includedir}/rdk/iarmmgrs-hal
    install -m 0644 ${S}/sysmgr/include/sysMgr.h ${D}${includedir}/rdk/iarmmgrs-hal
}
