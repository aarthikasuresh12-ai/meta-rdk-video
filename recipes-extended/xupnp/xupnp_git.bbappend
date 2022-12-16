DEPENDS_append = " iarmbus iarmmgrs systemd "

EXTRA_OECONF_append += " --enable-iarmbus --enable-rfc "

EXTRA_OECONF_append += " ${@bb.utils.contains('DISTRO_FEATURES', 'xcal_device', '--enable-video-support', '', d)}"

CXXFLAGS += "-DENABLE_SD_NOTIFY"
CFLAGS += "-DENABLE_SD_NOTIFY"

LDFLAGS += "-lsystemd"

RDEPENDS_${PN} = "iarm-event-sender"

inherit syslog-ng-config-gen
SYSLOG-NG_FILTER = "xupnp xcal-device"
SYSLOG-NG_SERVICE_xupnp = "xupnp.service"
SYSLOG-NG_DESTINATION_xupnp = "xdiscovery.log"
SYSLOG-NG_LOGRATE_xupnp = "medium"
SYSLOG-NG_SERVICE_xcal-device = "xcal-device.service"
SYSLOG-NG_DESTINATION_xcal-device = "xdevice.log"
SYSLOG-NG_LOGRATE_xcal-device = "medium"

do_install_append() {
        install -d ${D}${systemd_unitdir}/system ${D}${sysconfdir} ${D}${sysconfdir}/Xupnp
        install -m 0644 ${S}/conf/systemd/xupnp.service ${D}${systemd_unitdir}/system/xupnp.service
	install -m 0644 ${S}/conf/systemd/xupnp-certs.service ${D}${systemd_unitdir}/system/xupnp-certs.service
        install -m 0644 ${S}/conf/systemd/xdiscovery.conf ${D}${sysconfdir}
        install -m 0644 ${S}/conf/systemd/xdevice.conf ${D}${sysconfdir}
        install -m 0500 ${S}/conf/addRouteToMocaBridge.sh ${D}${sysconfdir}/Xupnp/addRouteToMocaBridge.sh
        install -d ${D}${sysconfdir} ${D}${sysconfdir}/rfcdefaults
        install -m 0644 ${S}/conf/xupnp.ini ${D}${sysconfdir}/rfcdefaults
}

do_install_append_hybrid() {
        install -m 0644 ${S}/conf/systemd/xcal-device.service ${D}${systemd_unitdir}/system/xcal-device.service
        install -m 0644 ${S}/conf/systemd/xupnp-firewall.service ${D}${systemd_unitdir}/system/xupnp-firewall.service
        install -m 0644 ${S}/conf/systemd/xcal-device.path ${D}${systemd_unitdir}/system/xcal-device.path
}

do_install_append_client() {
    install -m 0644 ${S}/conf/systemd/xcal-device-client.service ${D}${systemd_unitdir}/system/xcal-device.service

    install -m 0644 ${S}/conf/systemd/xupnp-rfc.service ${D}${systemd_unitdir}/system/xupnp-rfc.service
    install -D -m 0644 ${S}/conf/systemd/xupnp-rfc-check.conf  ${D}${systemd_unitdir}/system/xupnp.service.d/xupnp-rfc-check.conf
    install -D -m 0644 ${S}/conf/systemd/xupnp-rfc-check.conf  ${D}${systemd_unitdir}/system/xcal-device.service.d/xupnp-rfc-check.conf
}

CFLAGS_client_append = " -DUSE_XUPNP_TZ_UPDATE "

SYSTEMD_SERVICE_${PN} = "xupnp.service"
SYSTEMD_SERVICE_${PN}_append = " xcal-device.service"
SYSTEMD_SERVICE_${PN}_append = " xupnp-certs.service"
SYSTEMD_SERVICE_${PN}_append_hybrid = " xcal-device.path"
SYSTEMD_SERVICE_${PN}_append_hybrid = " xupnp-firewall.service"
FILES_${PN} += "${systemd_unitdir}/system/xupnp.service"
FILES_${PN}_append = " ${systemd_unitdir}/system/xcal-device.service"
FILES_${PN}_append = " ${systemd_unitdir}/system/xupnp-certs.service"
FILES_${PN}_append = " ${sysconfdir}/Xupnp/addRouteToMocaBridge.sh"
FILES_${PN}_append_hybrid = " ${systemd_unitdir}/system/xcal-device.path"
FILES_${PN}_append_hybrid = " ${systemd_unitdir}/system/xupnp-firewall.service"

#XUPNP RFC Check
SYSTEMD_SERVICE_${PN}_append_client = " xupnp-rfc.service"
FILES_${PN}_append_client = " ${systemd_unitdir}/system/xupnp-rfc.service"
FILES_${PN}_append_client = " ${systemd_unitdir}/system//xcal-device.service.d/xupnp-rfc-check.conf"
FILES_${PN}_append_client = " ${systemd_unitdir}/system//xupnp.service.d/xupnp-rfc-check.conf"
