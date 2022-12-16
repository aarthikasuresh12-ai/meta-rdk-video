SUMMARY = "RDK AAMP Gstreamer Plugin"
SECTION = "console/utils"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0f7f71545168884445a1a032e120e3d1"
PV = "${RDK_RELEASE}"
SRCREV_gstplug-rdk-aamp = "${AUTOREV}"
SRCREV_FORMAT = "gstplug-rdk-aamp"

DEPENDS += "curl aamp"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0  gstreamer1.0-plugins-base', 'gstreamer gst-plugins-base', d)}"
DEPENDS +=  "${@bb.utils.contains('DISTRO_FEATURES', 'rdk_svp', 'gst-svp-ext', '', d)}"
NO_RECOMMENDATIONS = "1"

AAMP_GST_PLUG_IN_RELEASE_TAG_NAME ?= "4.11.1.0"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/gst-plugins-rdk-aamp;protocol=${CMF_GIT_PROTOCOL};branch=${AAMP_GST_PLUG_IN_RELEASE_TAG_NAME};nobranch=1;name=gstplug-rdk-aamp"

S = "${WORKDIR}/git/"

require aamp-common.inc

PACKAGES = "${PN} ${PN}-dbg"

FILES_${PN} +="${libdir}/gstreamer-1.0/lib*.so"
FILES_${PN}-dbg += "${includedir}/*"
FILES_${PN}-dbg +="${libdir}/gstreamer-1.0/.debug/*"

INSANE_SKIP_${PN} = "dev-so"

