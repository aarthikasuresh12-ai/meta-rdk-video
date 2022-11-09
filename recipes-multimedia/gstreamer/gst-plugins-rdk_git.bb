SUMMARY = "RDK Gstreamer plugins"
DESCRIPTION = "RDK gst-plugins. These are the plugins encoding and \
decoding gstreamer elements. Theere are plugins available for dtcp and \
aes encoding/decoding. DTCP is used for Data exchange between the home \
networking elements."

SECTION = "console/utils"

LICENSE = "LGPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=db791dc95f6a08e8e4d206839bc67ec0"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/gst-plugins-rdk;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git"

FILES_${PN} += "${libdir}/gstreamer-*/*.so"
FILES_${PN}-dev += "${libdir}/gstreamer-*/*.la"
FILES_${PN}-dbg += "${libdir}/gstreamer-*/.debug/*"
FILES_${PN}-staticdev += "${libdir}/gstreamer-*/*.a "

DEPENDS = "curl rmfgeneric libtinyxml"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0', 'gstreamer', d)}"
RDEPENDS_${PN}_dunfell += "${VIRTUAL-RUNTIME_dtcpmgr}"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS += "safec-common-wrapper"

ENABLE_GST1 = "--enable-gstreamer1=${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'yes', 'no', d)}"
EXTRA_OECONF = "${ENABLE_GST1}"

DEBIAN_NOAUTONAME_${PN} = "1"

inherit autotools pkgconfig

CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

# only enable DTCPdec by default, as it's used in all configs.
# others can be enabled when required
PACKAGECONFIG ??= "dtcpdec dtcpenc httpsink httpsrc rbifilter tee"
PACKAGECONFIG[dtcpdec] = "--enable-dtcpdec,,virtual/dtcpmgr"
PACKAGECONFIG[dtcpenc] = "--enable-dtcpenc,,virtual/dtcpmgr"
PACKAGECONFIG[httpsink] = "--enable-httpsink,,"
PACKAGECONFIG[httpsrc] = "--enable-httpsrc,,openssl,"
PACKAGECONFIG[rbifilter] = "--enable-rbifilter,,"
PACKAGECONFIG[tee] = "--enable-tee,,"
