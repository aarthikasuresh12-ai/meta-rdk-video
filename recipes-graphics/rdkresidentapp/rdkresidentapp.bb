DESCRIPTION = "Resident app implementation for RDK reference application"
HOMEPAGE = ""

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3cc4d276e918f48b04eb2faf952d0537"

DEPENDS = "wpeframework"
RDEPENDS_${PN} += "bash"
inherit systemd

S = "${WORKDIR}/git"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/appmanager;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=generic"
SRCREV_generic = "${AUTOREV}"
SRCREV_FORMAT = "generic"

do_install() {
   install -d ${D}${systemd_unitdir}/system
   install -m 0644 ${S}/resources/systemd/residentapp.service ${D}${systemd_unitdir}/system/residentapp.service

   install -d ${D}/lib/rdk
   install -m 0755 ${S}/residentapp/residentApp.sh ${D}/lib/rdk/residentApp.sh
}

SYSTEMD_SERVICE_${PN} = "residentapp.service"
FILES_${PN} += "${systemd_unitdir}/system/residentapp.service"
FILES_${PN} += "/lib/rdk/residentApp.sh"
