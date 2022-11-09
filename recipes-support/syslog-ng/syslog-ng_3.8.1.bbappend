FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'syslog-ng', '', ' \
        file://syslog-ng-active.conf \
        file://syslog-ng-bootup.conf \
        file://syslog-ng-standby.conf \
        file://syslog-ecm.service \
        file://powerstate_update.path \
        file://powerstate_update.service \
        file://update_syslog_config.sh ', d)}"
SRC_URI += " \
        file://fix-assert-failure-crash-under-immense-load.patch \
        file://refactor_affile_dw_reap.patch \
        "

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'syslog-ng', 'false', 'true', d)}; 
    then
        install -d ${D}${sysconfdir}/${BPN}
        install -m 644 ${WORKDIR}/syslog-ng-bootup.conf ${D}${sysconfdir}/${BPN}/syslog-ng-bootup.conf
        install -m 644 ${WORKDIR}/syslog-ng-active.conf ${D}${sysconfdir}/${BPN}/syslog-ng-active.conf
        install -m 644 ${WORKDIR}/syslog-ng-standby.conf ${D}${sysconfdir}/${BPN}/syslog-ng-standby.conf

        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/powerstate_update.path  ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/powerstate_update.service  ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/syslog-ecm.service  ${D}${systemd_unitdir}/system

        install -d ${D}${base_libdir}/rdk
        install ${WORKDIR}/update_syslog_config.sh  ${D}${base_libdir}/rdk

        if [ -f  ${D}/lib/systemd/system/syslog-ng.service ]; then
            sed -i '/Documentation=/a\
Requires=tmp.mount' ${D}${systemd_unitdir}/system/syslog-ng.service
            sed -i '/Documentation=/a\
After=tmp.mount' ${D}${systemd_unitdir}/system/syslog-ng.service
            sed -i '/Documentation=/a\
Before=disk-check.service' ${D}${systemd_unitdir}/system/syslog-ng.service
            sed -i '/Documentation=/a\
DefaultDependencies=no' ${D}${systemd_unitdir}/system/syslog-ng.service

            sed -i '/ExecStart=/d' ${D}${systemd_unitdir}/system/syslog-ng.service
            sed -i '/Service/a\
ExecStart=/usr/sbin/syslog-ng  -F -f /tmp/syslog-ng.conf -R /tmp/lib/syslog-ng/syslog-ng.persist -c /tmp/lib/syslog-ng/syslog-ng.ctl -p /tmp/lib/syslog-ng/syslog-ng.pid' ${D}${systemd_unitdir}/system/syslog-ng.service
            sed -i '/Service/a\
ExecStartPre=/bin/cp /etc/syslog-ng/syslog-ng-bootup.conf /tmp/syslog-ng.conf' ${D}${systemd_unitdir}/system/syslog-ng.service
            sed -i '/Service/a\
ExecStartPre=/bin/mkdir -p /tmp/lib/syslog-ng' ${D}${systemd_unitdir}/system/syslog-ng.service
            sed -i '/Service/a\
ExecStartPre=/bin/mkdir -p /tmp/logs' ${D}${systemd_unitdir}/system/syslog-ng.service

            sed -i '/Service/a\
Sockets=syslog.socket' ${D}${systemd_unitdir}/system/syslog-ng.service

        fi
    fi
}

SYSTEMD_SERVICE_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'syslog-ng', '', 'syslog-ecm.service', d)}"
SYSTEMD_SERVICE_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'syslog-ng', '', 'powerstate_update.path', d)}"
SYSTEMD_SERVICE_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'syslog-ng', '', 'powerstate_update.service', d)}"

FILES_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'syslog-ng', '', '${base_libdir}/rdk/* ${systemd_unitdir}/system/* ${sysconfdir}/${BPN}/*', d)}"
