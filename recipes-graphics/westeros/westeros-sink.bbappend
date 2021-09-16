DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0-plugins-base', '', d)}"
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'westeros_sink_software_decode', ' file://essrmgr.conf ',' ',d)}"

export  SWDECODER_ENABLED = "${@bb.utils.contains('DISTRO_FEATURES','westeros_sink_software_decode','y','n',d)}"

do_install_append () {
    if [ "${SWDECODER_ENABLED}" = "y" ]; then
        install -d ${D}${sysconfdir}/default
        install -m 0644 ${WORKDIR}/essrmgr.conf ${D}${sysconfdir}/default
    fi
}

FILES_${PN}_append = " ${sysconfdir}/default/essrmgr.conf"
