SUMMARY = "This receipe compiles TRM Manager code base."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/trm;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

export TRM_NUMBER_OF_TUNERS ?= "4"

S = "${WORKDIR}/git/diag"
DEPENDS = "jansson iarmbus trm-common safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', '', d)}"

inherit autotools systemd pkgconfig syslog-ng-config-gen
SYSLOG-NG_FILTER = "trmmgr"
SYSLOG-NG_SERVICE_trmmgr = "trmmgr.service"
SYSLOG-NG_DESTINATION_trmmgr = "trmmgr.log"
SYSLOG-NG_LOGRATE_trmmgr = "medium"

CXXFLAGS_prepend = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec` ', '-fPIC', d)}"
CXXFLAGS_prepend_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec` ', '-fPIC', d)}"

CXXFLAGS_prepend = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API ', d)}"
LDFLAGS_prepend = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec` ', '', d)}"

do_install() {
	install -d ${D}${bindir}
        install -d ${D}${includedir}/rdk/trm
        install -d ${D}${includedir}/rdk/trm/trm
	install -m 0755 ${B}/TRMMgr ${D}${bindir}
        install -m 0644 ${S}/../diag/include/*.h ${D}${includedir}/rdk/trm/
        install -m 0644 ${S}/../common/include/trm/*.h ${D}${includedir}/rdk/trm/trm
	install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/../conf/trmmgr.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} = "trmmgr.service"
FILES_${PN} += "${systemd_unitdir}/system/trmmgr.service"

