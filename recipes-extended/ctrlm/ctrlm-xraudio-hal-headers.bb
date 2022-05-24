DESCRIPTION = "Control Manager xraudio hal component"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2a944942e1496af1886903d274dedb13"

SECTION = "base"

SRC_URI  = "${CMF_GIT_ROOT}/rdk/components/generic/xraudio-hal_ctrlm;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=ctrlm-xraudio-hal-headers"
SRCREV_ctrlm-xraudio-hal-headers = "${AUTOREV}"

FILES_${PN} += "${includedir}/ctrlm_xraudio_hal.h"
FILES_${PN} += "${includedir}/ctrlm_xraudio_hal_priv.h"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
   install -d ${D}${includedir}
   install -m 644 ${S}/src/ctrlm_xraudio_hal.h      ${D}${includedir}
   install -m 644 ${S}/src/ctrlm_xraudio_hal_priv.h ${D}${includedir}
}
ALLOW_EMPTY_${PN} = "1"
