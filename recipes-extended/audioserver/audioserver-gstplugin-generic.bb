SUMMARY = "This receipe compiles the audioserver gstreamer plugin"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${S}/COPYING.LGPL;md5=531649b135d43311157d2c982b3597e3"


DEPENDS = "audioserver-soc audioserver"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0', 'gstreamer', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0-plugins-base', 'gstreamer-plugins-base', d)}"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/audioserver/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=plugin"
S = "${WORKDIR}/git/gstplugin"
PV = "${RDK_RELEASE}"
SRCREV_plugin = "${AUTOREV}"
SRCREV_FORMAT = "plugin"

inherit autotools pkgconfig systemd

export STAGING_DIR_TARGET

FILES_${PN} += "${libdir}/gstreamer-*/*.so"
FILES_${PN}-dev += "${libdir}/gstreamer-*/*.la"
FILES_${PN}-dbg += "${libdir}/gstreamer-*/.debug/*"
FILES_${PN}-staticdev += "${libdir}/gstreamer-*/*.a "

