SUMMARY = "This recipe compiles the rdk media player application that uses rtRemote and handles qam, hls, and dash stream types"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
DEPENDS += "glib-2.0 curl rdk-logger rmfgeneric pxcore-libnode breakpad jansson aamp"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0 gstreamer1.0-plugins-base', 'gstreamer gst-plugins-base', d)}"
DEPENDS += "breakpad-wrapper safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
require recipes-graphics/pxcore-libnode/pxcore-libnode-env.inc
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/rdkmediaplayer;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git/"
BREAKPAD_BIN = " rdkmediaplayer"
CXXFLAGS += " -I${STAGING_INCDIR}/pxcore"

# O2 flags in gcc 9.2 causes runtime issues
CXXFLAGS_append_dunfell = " -fno-reorder-blocks -fno-align-loops"

# O2 flags in gcc 9.2 causes runtime issues for arm architecture
CXXFLAGS_append_dunfell_arm = " -fno-cse-follow-jumps"

EXTRA_OECONF += " --enable-rdk-logger --enable-breakpad"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'sage_svp', " --enable-svp-rdkmediaplayer", "", d)}"
inherit autotools pkgconfig coverity
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CXXFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
