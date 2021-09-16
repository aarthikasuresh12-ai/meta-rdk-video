SUMMARY = "This recipe compiles rmf_mediastreamer code base."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/rmf_tools/tenableHDCP;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig systemd coverity

DEPENDS += "devicesettings iarmbus iarmmgrs"
RDEPENDS_${PN}_dunfell += " devicesettings"



SYSTEMD_SERVICE_${PN} = "hdcp.service"
FILES_${PN} += "${sysconfdir}/* ${systemd_unitdir}/system/hdcp.service"

do_install_append() {

install -d ${D}${systemd_unitdir}/system ${D}${sysconfdir}
install -m 0644 ${S}/conf/hdcp.service ${D}${systemd_unitdir}/system/hdcp.service

}
