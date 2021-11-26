SUMMARY = "LED manager recipe"

SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/ledmgr;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=generic"

PV = "${RDK_RELEASE}+gitr${SRCPV}"

SRCREV_generic = "${AUTOREV}"

SRCREV_FORMAT = "ledmgr_generic"

S = "${WORKDIR}/git"

DEPENDS="virtual/ledmgr-extended iarmbus iarmmgrs devicesettings libunpriv"
RDEPENDS_${PN} += "devicesettings"
export RDK_FSROOT_PATH = '${STAGING_DIR_TARGET}'

LDFLAGS += "-lprivilege"

inherit autotools pkgconfig systemd

do_install_append() {
   install -d ${D}${systemd_unitdir}/system
   install -m 0644 ${S}/conf/ledmgr.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} = "ledmgr.service"
