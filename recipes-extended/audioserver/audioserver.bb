SUMMARY = "This receipe compiles the audioserver component"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"


DEPENDS = "audioserver-soc"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0', 'gstreamer', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0-plugins-base', 'gstreamer-plugins-base', d)}"
RDEPENDS_${PN} += "audioserver-gstplugin-generic"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/audioserver/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=audioserver"
S = "${WORKDIR}/git/audioserver"
PV = "${RDK_RELEASE}"
SRCREV_audioserver = "${AUTOREV}"
SRCREV_FORMAT = "audioserver"

inherit autotools pkgconfig systemd

export STAGING_DIR_TARGET

FILES_${PN} += "/usr/share/audioservr/*"

do_install_append() {
   install -d ${D}${systemd_unitdir}/system ${D}${sysconfdir}
   install -m644 ${S}/conf/audioserver.service ${D}${systemd_unitdir}/system
}

FILES_${PN} += "${systemd_unitdir}/system/audioserver.service"
SYSTEMD_SERVICE_${PN}  = "audioserver.service"
