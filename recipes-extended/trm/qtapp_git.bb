SUMMARY = "This receipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/trm;module=.;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git/qtapp/srv"

DEPENDS = "trm-common qtwebsockets jansson"

inherit systemd qmake5 coverity

export TRM_NUMBER_OF_TUNERS ?= "4"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${systemd_unitdir}/system
	install -m 0755 ${B}/srv ${D}${bindir}
	install -m 0644 ${S}/../../conf/trm-srv.service ${D}${systemd_unitdir}/system
}

FILES_${PN}-dbg += "${bindir}/.debug/srv"

EXTRA_QMAKEVARS_PRE_qemux86hyb += "DEFINES+=DISABLE_LIVE_RESERVATION_REQ"

SYSTEMD_SERVICE_${PN} = "trm-srv.service"
FILES_${PN} += "${systemd_unitdir}/system/trm-srv.service"
