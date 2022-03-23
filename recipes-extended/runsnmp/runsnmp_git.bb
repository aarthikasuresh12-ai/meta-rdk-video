SUMMARY = "This recipe compiles runsnmp code base."
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=bc14e971d0e95ae5ca07a810dcd5893b"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

PV = "${RDK_RELEASE}+git${SRCPV}"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git/snmp/runsnmp"

CXXFLAGS += " \
	-I=${includedir}/rdk/snmp/ipcclient \
"

export RDK_FSROOT_PATH = '${STAGING_DIR_TARGET}'
export FSROOT = "${STAGING_DIR_TARGET}"

export IARMBUS_LIBS="-lIARMBus"

#export NETSNMP_LIBS="-lnetsnmpmibs -lnetsnmpagent -lnetsnmp -lnetsnmptrapd -lnetsnmphelpers -lsnmpinterface -lrmfosal -lpodserver -loobsimanager -loobsicache -lsectionfilter -linbsectionfilter -loobsectionfilter -lrmfproxyservice -linbsimanager -lrmfosalutils -lIARMBus"

DEPENDS = "net-snmp halsnmp rmfgeneric iarmbus "
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', '--enable-stubcode=yes', '--enable-stubcode=no', d)}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', '--enable-podmgr=no', '--enable-podmgr=yes', d)}"
EXTRA_OECONF += "--enable-yocto"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'nopod', ' ', 'snmpmanager-priv', d)}"
DEPENDS += "breakpad-wrapper"

inherit autotools pkgconfig systemd coverity syslog-ng-config-gen
SYSLOG-NG_FILTER = "ocapri"
SYSLOG-NG_SERVICE_ocapri += "runsnmp.service"
SYSLOG-NG_DESTINATION_ocapri = "ocapri_log.txt"
SYSLOG-NG_LOGRATE_ocapri = "very-high"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/runsnmp.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/udpsvd.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} = "runsnmp.service"
SYSTEMD_SERVICE_${PN} += "udpsvd.service"
FILES_${PN} += "${systemd_unitdir}/system/runsnmp.service"
FILES_${PN} += "${systemd_unitdir}/system/udpsvd.service"
