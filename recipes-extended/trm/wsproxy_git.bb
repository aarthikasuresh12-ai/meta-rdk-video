SUMMARY = "This receipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/trm;protocol=${CMF_GIT_PROTOCOL};module=.;branch=${CMF_GIT_BRANCH};name=wsproxy"
SRCREV_wsproxy = "${AUTOREV}"
SRCREV_FORMAT = "wsproxy"

export USE_DELIA_GATEWAY="yes"

S = "${WORKDIR}/git/wsproxy"

DEPENDS = "trm-common qtbase qtwebsockets safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', '', d)}"
DEPENDS += " rfc"

CXXFLAGS += "-DTRM_USE_SSL"
CFLAGS += "-DTRM_USE_SSL"
require recipes-qt/qt5/qt5.inc

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

EXTRA_QMAKEVARS_PRE += "DEFINES+=USE_TRM_YOCTO_BUILD"
EXTRA_QMAKEVARS_PRE += " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', 'DEFINES+=USE_SAFEC_API', '', d)}"

inherit systemd coverity pkgconfig syslog-ng-config-gen
SYSLOG-NG_FILTER = "trm"
SYSLOG-NG_SERVICE_trm = "wsproxy.service"
#The log rate and destination are mentioned at qtapp_git.bb, to avoid duplication of variables set we have commented the below variables.
#SYSLOG-NG_DESTINATION_trm = "trm.log"
#SYSLOG-NG_LOGRATE_trm = "medium"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${B}/websocket-trm-proxy ${D}${bindir}
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/../conf/wsproxy.service ${D}${systemd_unitdir}/system
}

do_configure_append() {
	rmdir --ignore-fail-on-non-empty ${S}/.git
}

SYSTEMD_SERVICE_${PN} = "wsproxy.service"
FILES_${PN} += "${systemd_unitdir}/system/wsproxy.service"
FILES_${PN}-dbg += "${bindir}/.debug/websocket-trm-proxy"
