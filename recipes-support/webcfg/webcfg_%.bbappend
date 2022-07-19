inherit syslog-ng-config-gen systemd

SYSLOG-NG_FILTER = "webconfig"
SYSLOG-NG_SERVICE_webconfig = "webconfig.service"
SYSLOG-NG_DESTINATION_webconfig = "webconfig.log"
SYSLOG-NG_LOGRATE_webconfig = "high"

CFLAGS_append += "-DRDK_PERSISTENT_PATH_VIDEO"
CFLAGS_append += "-DRDK_USE_DEFAULT_INTERFACE"

do_install_append_hybrid() {
    if ${@bb.utils.contains("DISTRO_FEATURES", "webconfig_bin", "true", "false", d)}
    then
      install -d ${D}/etc
      install -d ${D}${systemd_unitdir}/system
      install -m 0644 ${WORKDIR}/partners_defaults_webcfg_video.json ${D}/etc
      install -m 0644 ${WORKDIR}/webconfig.service ${D}${systemd_unitdir}/system
      (python ${WORKDIR}/metadata_parser.py ${WORKDIR}/webconfig_video_metadata.json ${D}/etc/webconfig.properties ${MACHINE})
    fi
}

do_install_append_client() {
    if ${@bb.utils.contains("DISTRO_FEATURES", "webconfig_bin", "true", "false", d)}
    then
      install -d ${D}/etc
      install -d ${D}${systemd_unitdir}/system
      install -m 0644 ${WORKDIR}/partners_defaults_webcfg_video.json ${D}/etc
      install -m 0644 ${WORKDIR}/webconfig.service ${D}${systemd_unitdir}/system
      (python ${WORKDIR}/metadata_parser.py ${WORKDIR}/webconfig_video_metadata.json ${D}/etc/webconfig.properties ${MACHINE})
    fi
}

FILES_${PN} += "/etc/webconfig.properties"
FILES_${PN} += "/etc/partners_defaults_webcfg_video.json"
FILES_${PN} += "${systemd_unitdir}/system/webconfig.service"
SYSTEMD_SERVICE_${PN} = "webconfig.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

