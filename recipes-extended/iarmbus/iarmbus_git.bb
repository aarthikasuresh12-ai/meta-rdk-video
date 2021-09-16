SUMMARY = "IARM-Bus is a platform agnostic IPC interface."
DESCRIPTION = "It allows applications to communicate\
with each other by sending Events or invoking Remote Procedure\
Calls. The common programming APIs offered by the RDK IARM-Bus\
interface is independent of the operating system or the underlying IPC\
mechanism."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV_iarmbus = "${AUTOREV}"
SRCREV_FORMAT = "iarmbus"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/iarmbus;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=iarmbus"

S = "${WORKDIR}/git"

CFLAGS += "-DENABLE_SD_NOTIFY"
LDFLAGS += "-lsystemd"

DEPENDS="libxml2 dbus glib-2.0"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

inherit autotools pkgconfig systemd coverity

DEPENDS += "safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"


CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"



do_install_append() {
	install -d ${D}${includedir}/rdk/iarmbus
	install -m 0644 ${S}/core/include/*.h ${D}${includedir}/rdk/iarmbus
	install -m 0644 ${S}/core/*.h ${D}${includedir}/rdk/iarmbus
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/conf/iarmbusd.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} = "iarmbusd.service"
FILES_${PN} += "${systemd_unitdir}/system/iarmbusd.service"
