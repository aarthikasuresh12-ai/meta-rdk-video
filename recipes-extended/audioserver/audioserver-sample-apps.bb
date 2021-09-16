SUMMARY = "This receipe compiles the audioserver-sample-apps component"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

S = "${WORKDIR}/git/audioserver/sample-apps"

DEPENDS = "audioserver westeros"
DEPENDS += "${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "bluetooth-core bluetooth-mgr", "", d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0', 'gstreamer', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0-plugins-base', 'gstreamer-plugins-base', d)}"

EXTRA_OEMAKE += "${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "'CXXFLAGS=-DUSE_BT'", "", d)}"
EXTRA_OEMAKE += "${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "'LDFLAGS=-lbtrCore -lbtrMgrStreamOut'", "", d)}"

ENABLE_BTR = "--enable-btr=${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'yes', 'no', d)}"
EXTRA_OECONF += " ${ENABLE_BTR}"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/audioserver/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=audioserversampleapps"
PV = "${RDK_RELEASE}"
SRCREV_audioserversampleapps = "${AUTOREV}"
SRCREV_FORAMAT = "audioserversampleapps"

inherit autotools pkgconfig

export STAGING_DIR_TARGET

FILES_${PN} += "${libdir}/gstreamer-*/*.so"
FILES_${PN} += "/usr/share/audioserver/*"
FILES_${PN}-dev += "${libdir}/gstreamer-*/*.la"
FILES_${PN}-dbg += "${libdir}/gstreamer-*/.debug/*"
FILES_${PN}-staticdev += "${libdir}/gstreamer-*/*.a "

do_install_append() {
   install -d ${D}/usr/share/audioserver
   install -m644 ${S}/data/*.wav ${D}/usr/share/audioserver/
}

