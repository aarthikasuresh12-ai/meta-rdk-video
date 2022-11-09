inherit syslog-ng-config-gen
SYSLOG-NG_FILTER = "telemetry2_0"
SYSLOG-NG_SERVICE_telemetry2_0 = "telemetry2_0.service"
SYSLOG-NG_DESTINATION_telemetry2_0 = "telemetry2_0.txt.0"
SYSLOG-NG_LOGRATE_telemetry2_0 = "high"

PLATFORM ?= "STB"
CFLAGS += " -DENABLE_RDKV_SUPPORT -DENABLE_PS_PROCESS_SEARCH "
CFLAGS += "${@bb.utils.contains('PLATFORM', 'STB', ' -DENABLE_STB_SUPPORT ', '', d)}"

do_install_append () {

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/telemetry2_0.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/telemetry2_0.path ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} += "telemetry2_0.path"
SYSTEMD_SERVICE_${PN} += "telemetry2_0.service"
