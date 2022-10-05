SUMMARY = "Soc-specific implementations for video applications"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRCREV = "${AUTOREV}"

DEPENDS += " gstreamer1.0 gstreamer1.0-plugins-base"
AUDIOMIXER_NOT_SUPPORTED = "${@bb.utils.contains('DISTRO_FEATURES', 'disable_audio_mixer', "true", "", d)}"
EXTRA_OECMAKE += " \
    -DAUDIOMIXER_NOT_SUPPORTED =${AUDIOMIXER_NOT_SUPPORTED} \
"
SRC_URI = "${RDK_GENERIC_ROOT_GIT}/gstreamer-netflix-platform/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH}"

S = "${WORKDIR}/git"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0 -I${STAGING_INCDIR}/gstreamer-1.0 -I${STAGING_DIR_TARGET}/${libdir}/glib-2.0/include/ "

do_compile () {
    oe_runmake -C ${S} -f Makefile                            
}

do_install() {
        install -d ${D}/${libdir}
        install -d ${D}/usr/include
        install -m 0755 ${S}/librdkgstreamerutils.so ${D}/${libdir}
        install -m 0644 ${S}/rdk_gstreamer_utils.h  ${D}/usr/include
}


INSANE_SKIP_${PN} = "dev-so"
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"

INSANE_SKIP_${PN} += "ldflags textrel"


