DESCRIPTION = "Control Manager Headers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"


SECTION = "base"
DEPENDS = ""

include ctrlm.inc

SRCREV_${PN} = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/control;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=ctrlm-headers"

S = "${WORKDIR}/git"

FILES_${PN} += "${includedir}/ctrlm_ipc.h \
                ${includedir}/ctrlm_ipc_rcu.h \
                ${includedir}/ctrlm_ipc_voice.h \
                ${includedir}/ctrlm_ipc_key_codes.h \
                ${includedir}/ctrlm_ipc_device_update.h \
                ${includedir}/ctrlm_ipc_ble.h \
                ${includedir}/ctrlm_hal.h \
                ${includedir}/ctrlm_hal_ip.h \
                ${includedir}/ctrlm_hal_ble.h \
                ${includedir}/ctrlm_hal_rf4ce.h \
               "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_rcu.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_voice.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_key_codes.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_device_update.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_ble.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal_ip.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal_ble.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal_rf4ce.h ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"
