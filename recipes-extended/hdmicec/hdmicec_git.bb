SUMMARY = "This recipe compiles and installs hdmicec component."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/hdmicec;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=hdmicec"
SRCREV_hdmicec = "${AUTOREV}"
SRCREV_FORMAT = "hdmicec"


DEPENDS = "glib-2.0 dbus iarmbus devicesettings hdmicecheader virtual/hdmicec-hal iarmmgrs-hal-headers"
RDEPENDS_${PN} = " devicesettings"

DEPENDS += "safec-common-wrapper"

ASNEEDED = ""
ALLOW_EMPTY_${PN} = "1"

S = "${WORKDIR}/git"

DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

inherit systemd autotools pkgconfig coverity breakpad-logmapper syslog-ng-config-gen
SYSLOG-NG_FILTER = "cec"
SYSLOG-NG_SERVICE_cec = "cecdaemon.service cecdevmgr.service"
SYSLOG-NG_DESTINATION_cec = "cec_log.txt"
SYSLOG-NG_LOGRATE_cec = "medium"

CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CXXFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"



do_install_append() {
        install -d ${D}${includedir}/rdk/hdmicec
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/cecdaemon.service ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/cecdaemon.path ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/cecdaemon-precheck.service ${D}${systemd_unitdir}/system
#        install -m 0644 ${S}/cecdevmgr.service ${D}${systemd_unitdir}/system
        install -d ${D}${base_libdir}/rdk
        install -m 0755 ${S}/cecdaemon_starter.sh ${D}${base_libdir}/rdk
}

SYSTEMD_SERVICE_${PN} = "cecdaemon.service"
SYSTEMD_SERVICE_${PN} += "cecdaemon.path"
SYSTEMD_SERVICE_${PN} += "cecdaemon-precheck.service"
#SYSTEMD_SERVICE_${PN} = "cecdevmgr.service"
FILES_${PN} += "${systemd_unitdir}/system/cecdaemon.service"
FILES_${PN} += "${systemd_unitdir}/system/cecdaemon.path"
FILES_${PN} += "${systemd_unitdir}/system/cecdaemon-precheck.service"
FILES_${PN} += "${base_libdir}/rdk/cecdaemon_starter.sh"
#FILES_${PN} += "${systemd_unitdir}/system/cecdevmgr.service"
# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "CecDaemonMain"
BREAKPAD_LOGMAPPER_LOGLIST = "cec_log.txt"
