SUMMARY = "audiocapturemgr recipe"

SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/audiocapturemgr;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=audiocapturemgr"
PV = "${RDK_RELEASE}"

SRCREV_audiocapturemgr = "${AUTOREV}"

SRCREV_FORMAT = "audiocapturemgr"
DEPENDS = "virtual/media-utils iarmbus iarmmgrs libunpriv"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS += "safec-common-wrapper"
LDFLAGS_append = "-lprivilege"
CXXFLAGS += " -DDROP_ROOT_PRIV"

S = "${WORKDIR}/git"
EXTRA_OECONF = " --enable-testapp "
export RDK_FSROOT_PATH = '${STAGING_DIR_TARGET}'

inherit autotools pkgconfig systemd breakpad-logmapper

CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CXXFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

do_install_append() {
   install -d ${D}${systemd_unitdir}/system
   install -m 0644 ${S}/conf/audiocapturemgr.service ${D}${systemd_unitdir}/system
}

FILES_${PN} += "${systemd_unitdir}/system/*"
SYSTEMD_SERVICE_${PN} = "audiocapturemgr.service"
# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "audiocapturemgr"
BREAKPAD_LOGMAPPER_LOGLIST = "audiocapturemgr.log"
