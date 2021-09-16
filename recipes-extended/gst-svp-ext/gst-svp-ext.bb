SUMMARY = "GStreamer SVP Extension component"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRCREV_gst = "${AUTOREV}"
SRCREV_FORMAT = "gst"
# need gstreamer-plugins-soc for brcmsvpmeta
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'sage_svp', 'gstreamer-plugins-soc', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0', 'gstreamer', d)}"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/gst_svp_ext/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=gst"

S = "${WORKDIR}/git/"

BUILD_FLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'dynamic_svp_decryption', '-DDYNAMIC_SVP_DECRYPTION=ENABLED', '', d)}"
BUILD_FLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'rdk_svp', '-DRDK_SVP=ENABLED', '', d)}"

CXXFLAGS += "-fPIC -I${STAGING_INCDIR} -I${STAGING_INCDIR}/glib-2.0 -I${STAGING_INCDIR}/gstreamer-1.0 -I${STAGING_DIR_TARGET}/usr/lib/glib-2.0/include/ -I${WORKDIR}/git/ ${BUILD_FLAGS}"

do_compile () {
    oe_runmake -C ${S} -f Makefile PLATFORM_SVP="${PLATFORM_SVP}"
}

do_install() {
        install -d ${D}/usr/lib
        install -d ${D}/usr/include
        install -m 0755 ${S}/libgstsvpext.so        ${D}/usr/lib
        install -m 0644 ${S}/gst_svp_meta.h         ${D}/usr/include
        install -m 0644 ${S}/gst_svp_performance.h  ${D}/usr/include
        install -m 0644 ${S}/GstPerf.h              ${D}/usr/include
}


INSANE_SKIP_${PN} = "dev-so"
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so" 

INSANE_SKIP_${PN} += "ldflags"
