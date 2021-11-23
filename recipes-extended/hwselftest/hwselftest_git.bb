SUMMARY = "Hardware Self test"
DESCRIPTION = "Hardware-Selftest and Diagnostic Tool"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
PV = "${RDK_RELEASE}"

SRCREV_hwselftest = "${AUTOREV}"
SRCREV_FORMAT = "hwselftest"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/hwselftest;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=hwselftest"
S = "${WORKDIR}/git"

CXXFLAGS += "-I=${includedir}/directfb -I=${includedir}/rdk/iarmmgrs-hal -I=${includedir}/rdk/ds -I=${includedir}/rdk/ds-rpc -I=${includedir}/rdk/ds-hal -I=${includedir}/rdk/iarmbus -I=${includedir}/wdmp-c"
CFLAGS += "-I=${includedir}/directfb -I=${includedir}/rdk/iarmbus -I=${includedir}/rdk/iarmmgrs-hal -I=${includedir}/wdmp-c"
LDFLAGS += "-lwebsockets -ltelemetry_msgsender"

EXTRA_OECONF += "--enable-agent-build --enable-client-build --enable-tr69profile-build --enable-cli-build --sysconfdir /etc/hwselftest"

DEPENDS = "libwebsockets nopoll jansson net-snmp devicesettings iarmbus iarmmgrs rmfgeneric directfb tr69hostif-headers breakpad breakpad-wrapper xupnp rfc storagemanager telemetry"

RDEPENDS_${PN} += "jquery devicesettings bash"
RDEPENDS_${PN}_append_dunfell = " mfr-library"

inherit autotools pkgconfig systemd syslog-ng-config-gen
SYSLOG-NG_FILTER = "hwselftest"
SYSLOG-NG_SERVICE_hwselftest = "hwselftest.service hwselftest-runner.service"
SYSLOG-NG_DESTINATION_hwselftest = "hwselftest.log"
SYSLOG-NG_LOGRATE_hwselftest = "low"


do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/agent/rootfs/lib/systemd/system/hwselftest.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/agent/rootfs/usr/bin/hwst_log.sh ${D}${bindir}
    install -m 0644 ${S}/cli/rootfs/lib/systemd/system/hwselftest-runner.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/agent/rootfs/lib/systemd/system/hwselftest-proxy.socket ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/agent/rootfs/lib/systemd/system/hwselftest-proxy.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/agent/rootfs/lib/systemd/system/hwselftest-socket-restart.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/agent/rootfs/lib/systemd/system/hwselftest-init.service ${D}${systemd_unitdir}/system
    install -d ${D}${includedir}
    install -m 0644 ${S}/tr69profile/DeviceInfo_hwHealthTest.h ${D}${includedir}
    install -d ${D}${bindir}
    install -m 0755 ${S}/cli/rootfs/usr/bin/hwst_check_free_dram.sh ${D}${bindir}
    install -m 0755 ${S}/cli/rootfs/usr/bin/hwst_check_free_cpu.sh ${D}${bindir}
    install -m 0755 ${S}/cli/rootfs/usr/bin/hwst_periodic_trigger.sh ${D}${bindir}
    install -m 0755 ${S}/agent/rootfs/usr/bin/hwst_agent_start.sh ${D}${bindir}
    install -m 0755 ${S}/agent/rootfs/usr/bin/hwst_init.sh ${D}${bindir}
    install -m 0755 ${S}/agent/rootfs/usr/bin/hwst_defaultRoute.sh ${D}${bindir}
}

FILES_${PN} += "/var/www/*"
FILES_${PN} += "${systemd_unitdir}/system/hwselftest.service"
FILES_${PN} += "${systemd_unitdir}/system/hwselftest-runner.service"
FILES_${PN} += "${systemd_unitdir}/system/hwselftest-proxy.socket"
FILES_${PN} += "${systemd_unitdir}/system/hwselftest-proxy.service"
FILES_${PN} += "${systemd_unitdir}/system/hwselftest-socket-restart.service"
FILES_${PN} += "${systemd_unitdir}/system/hwselftest-init.service"

SYSTEMD_SERVICE_${PN} = "hwselftest.service"
SYSTEMD_SERVICE_${PN} += "hwselftest-runner.service"
SYSTEMD_SERVICE_${PN} += "hwselftest-proxy.service"
SYSTEMD_SERVICE_${PN} += "hwselftest-proxy.socket"
SYSTEMD_SERVICE_${PN} += "hwselftest-socket-restart.service"
SYSTEMD_SERVICE_${PN} += "hwselftest-init.service"
