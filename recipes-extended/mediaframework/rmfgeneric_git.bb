SUMMARY = "This recipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

require rmfgeneric.inc

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bc14e971d0e95ae5ca07a810dcd5893b"

SRCREV_rmfgeneric = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=rmfgeneric"

export RDK_FSROOT_PATH = "${STAGING_DIR_TARGET}"
export FSROOT = '='

PACKAGECONFIG_append_hybrid = " ippvsource "

EXTRA_OECONF_append_hybrid = " --enable-mediaplayersink --enable-rmfproxyservice --enable-transcoderfilter --enable-hnsink --enable-vodsource --enable-qam --enable-dumpfilesink --enable-rbi --enable-rbidaemon --enable-rbiadcache"

ENABLE_GST1 = "--enable-gstreamer1=${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'yes', 'no', d)}"
EXTRA_OECONF += "${ENABLE_GST1}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', '--enable-stubcode=yes', '--enable-stubcode=no', d)}"

EXTRA_OECONF_append_client = " --enable-mediaplayersink --enable-hnsink --enable-dumpfilesink --disable-xfs"
EXTRA_OECONF += "--enable-yocto"

DEPENDS = "rdk-logger jansson rmfosal iarmbus iarmmgrs-hal-headers rmfhalheaders curl base64 pxcore-libnode libsyswrapper"
DEPENDS += "virtual/dtcpmgr libtinyxml xfsprogs trm-common rmfgenericheaders rfc"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0', 'gstreamer', d)}"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'nopod', 'sectionfilter-hal-stub', '', d)}"
DEPENDS += " virtual/dvrmgr-hal dvrgenericheaders"
DEPENDS_remove_client = "xfsprogs"

#Enable support for rfcapi
DEPENDS += "rfc"
CXXFLAGS += "-I=${includedir}/wdmp-c"
LDFLAGS += "-lrfcapi"

DEPENDS_append_hybrid = " net-snmp trh"

RDEPENDS_${PN} += "virtual/dtcpmgr rmfosal"
RDEPENDS_${PN}_dunfell += "${VIRTUAL-RUNTIME_dtcpmgr}"
INHIBIT_PACKAGE_STRIP_FILES = "${PKGD}${libdir}/librbi.a"

inherit autotools pkgconfig systemd coverity syslog-ng-config-gen
SYSLOG-NG_FILTER_hybrid = "rbidaemon"
SYSLOG-NG_SERVICE_rbidaemon_hybrid = "rbidaemon.service"
SYSLOG-NG_DESTINATION_rbidaemon_hybrid = "rbiDaemon.log"
SYSLOG-NG_LOGRATE_rbidaemon_hybrid = "low"

CXXFLAGS += "-I=${includedir}/rdk/iarmmgrs-hal"

# TODO
# FIXME
# rmfgenericheaders is introduced as workaround to get
# around the problem mentioned in the following JIRA
# ticket DELIA-4931. Once this ticket is fixed,
# rmfgenericheaders recipe will go away. Hence, the
# dependencies.
#
do_install_append() {
	if [ -d ${D}${includedir} ]; then
		rm -rf ${D}${includedir}
	fi
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/rbi/test/rbidaemon.service ${D}${systemd_unitdir}/system
}

do_install_append_client() {
       rm -rf ${D}${base_libdir}
}

SYSTEMD_SERVICE_${PN} += "rbidaemon.service"
FILES_${PN} += "${systemd_unitdir}/system/rbidaemon.service"

SYSTEMD_SERVICE_${PN}_remove_client = "rbidaemon.service"
FILES_${PN}_remove_client  = "${systemd_unitdir}/system/rbidaemon.service"

PACKAGECONFIG_append_hybrid = " podmgr"
