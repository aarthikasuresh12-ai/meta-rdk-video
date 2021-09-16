SUMMARY = "Sys mon tool key simulator recipe"

DESCRIPTION = "Sys mon tool key simulator recipe"

SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/sys_mon_tools/iarm_event_sender;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
S = "${WORKDIR}/git"

DEPENDS = "iarmbus iarmmgrs dbus glib-2.0"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'netsrvmgr', '', d)}"

CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm', '-DCTRLM_ENABLED', '', d)}"
CFLAGS += "-DPLATFORM_SUPPORTS_RDMMGR"
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '-DHAS_WIFI_SUPPORT', '', d)}"
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_maintenance_manager', '-DHAS_MAINTENANCE_MANAGER', '', d)}"

inherit autotools pkgconfig coverity
