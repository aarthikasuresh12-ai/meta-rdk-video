SUMMARY = "Bluetooth Low Energy RCU daemon"
PROVIDES = "sky-bluetoothrcu"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PV = "${RDK_RELEASE}"

DEPENDS = " qtbase qtbase-native qtwebsockets systemd bluetooth-mgr"
RDEPENDS_${PN} += "qtwebsockets libstdc++ glibc dbus qtbase libudev bluetooth-mgr"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/blercudaemon;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};nobranch=1;name=blercudaemon"
SRCREV_FORMAT = "blercudaemon"
SRCREV_blercudaemon = "6c8d1837038b14be081fb300147d9f64437dd9e2"

SRC_URI_append = " file://0001-AMLOGIC-279-Start-RCU-Daemon-after-btmgr.patch;patchdir=${WORKDIR}/git"

inherit cmake pkgconfig systemd

S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DRDK=TRUE"

# (For now) build the debug version of the daemon and include tools
EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Debug"

# Yocto / OpenEmbedded requires this
EXTRA_OECMAKE += "-DOE_QMAKE_PATH_EXTERNAL_HOST_BINS=${STAGING_BINDIR_NATIVE}/qt5"

CXXFLAGS += "-fPIC"

SYSTEMD_SERVICE_${PN}  = "sky-bluetoothrcu.service"

# These are the files that end up in the rootfs
FILES_${PN} += "${systemd_system_unitdir}/sky-bluetoothrcu.service"
FILES_${PN} += "${sysconfdir}/dbus-1/system.d/com.sky.blercu.conf"
FILES_${PN} += "${sbindir}/BleRcuDaemon"

# The following is installed only on debug builds
FILES_${PN} += "${bindir}/BleRcuConsole"


# Add a new user to run the daemon as - this is still WiP
inherit extrausers useradd
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-g 1200 blercu"
USERADD_PARAM_${PN} = "-u 1200 -g blercu -G input -d /nonexistent -r -s /bin/nologin blercu"

