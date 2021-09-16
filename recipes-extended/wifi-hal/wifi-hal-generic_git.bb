SUMMARY = "WiFi RDK HAL interface layer library."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
PV = "${RDK_RELEASE}+git${SRCPV}"

PROVIDES = "virtual/wifi-hal"
RPROVIDES_${PN} = "virtual/wifi-hal"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/wifi;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS="wpa-supplicant wifi-hal-headers rdk-logger"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'WIFI_CLIENT_ROAMING', ' cjson', '', d)}"
EXTRA_OECONF += "--disable-static --disable-silent-rules "
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'WIFI_CLIENT_ROAMING', ' --enable-client-roaming', '', d)}"

RDEPENDS_${PN} += "wpa-supplicant"
 
inherit autotools systemd pkgconfig coverity
