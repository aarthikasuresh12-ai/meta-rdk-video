
CFLAGS += " -DENABLE_RDKV_SUPPORT "

do_install_append () {

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/telemetry2_0.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/telemetry2_0.path ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} += "telemetry2_0.path"
SYSTEMD_SERVICE_${PN} += "telemetry2_0.service"
