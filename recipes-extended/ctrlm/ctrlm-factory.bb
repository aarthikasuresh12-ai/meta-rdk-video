DESCRIPTION = "Control Manager Factory Application"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SECTION = "base"

DEPENDS = "iarmbus rdkversion ctrlm-headers rdkx-logger nopoll safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

RDEPENDS_${PN}_append = " iarmbus"

PROVIDES = "ctrlm-factory"
RPROVIDES_${PN} = "ctrlm-factory"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/control-factory/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=ctrlm-factory"

VERSION_TEST_TONES = "20220616"
SRC_URI_append = " ${RDK_ARTIFACTS_BASE_URL}/generic/components/yocto/ctrlm_factory/test_tones/test_tones_${VERSION_TEST_TONES}/2.1/test_tones_${VERSION_TEST_TONES}-2.1.tar.bz2;name=test_tones"
SRC_URI[test_tones.md5sum]    = "d9e7829785f011214ec948f417873825"
SRC_URI[test_tones.sha256sum] = "ef10d7174a8bc79aff71b30980cd1304a2a33cf10afc38049c13cb11d1a309cc"

FILES_${PN} += "${datadir}/tone_1khz.wav"

SRCREV_ctrlm-factory = "${AUTOREV}"
SRCREV_FORMAT = "ctrlm-factory"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

INCLUDE_DIRS = "\
-I=${includedir} \
-I=${includedir}/rdk/iarmbus \
-I=${libdir}/dbus-1.0/include \
-I=${includedir}/dbus-1.0 \
-I=${includedir}/nopoll \
"

CFLAGS_append   = " -std=c11 -D_POSIX_C_SOURCE=200809L -Wall -Werror ${INCLUDE_DIRS}"

CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

CXXFLAGS_append = " -std=c++11 -D_POSIX_C_SOURCE=200809L -Wall -Werror ${INCLUDE_DIRS}"

CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"

THUNDER            ?= "false"
CXXFLAGS_append     = "${@bb.utils.contains('THUNDER', 'true', ' -I=${includedir}/WPEFramework/ -I=${includedir}/WPEFramework/core/', '', d)}"
LDFLAGS_append      = "${@bb.utils.contains('THUNDER', 'true', ' -lWPEFrameworkCore -lWPEFrameworkProtocols -lWPEFrameworkPlugins -lWPEFrameworkTracing', '', d)}"
DEPENDS_append      = "${@bb.utils.contains('THUNDER', 'true', ' wpeframework', '', d)}"
EXTRA_OECONF_append = "${@bb.utils.contains('THUNDER', 'true', ' --enable-thunder', '', d)}"

THUNDER_SECURITY   ?= "true"
EXTRA_OECONF_append = "${@bb.utils.contains('THUNDER_SECURITY', 'true', ' --enable-thunder-security', '', d)}"

AUDIO_CONTROL        ?= "false"
LDFLAGS_append        = "${@bb.utils.contains('AUDIO_CONTROL', 'true', ' -lds', '', d)}"
DEPENDS_append        = "${@bb.utils.contains('AUDIO_CONTROL', 'true', ' devicesettings', '', d)}"
RDEPENDS_${PN}_append = "${@bb.utils.contains('AUDIO_CONTROL', 'true', ' devicesettings', '', d)}"
EXTRA_OECONF_append   = "${@bb.utils.contains('AUDIO_CONTROL', 'true', ' --enable-audio-control', '', d)}"
INCLUDE_DIRS_append   = "${@bb.utils.contains('AUDIO_CONTROL', 'true', ' -I=${includedir}/rdk/ds -I=${includedir}/rdk/ds-rpc -I=${includedir}/rdk/ds-hal', '', d)}"

AUDIO_PLAYBACK       ?= "false"
EXTRA_OECONF_append   = "${@bb.utils.contains('AUDIO_PLAYBACK', 'true', ' --enable-audio-playback', '', d)}"

MICROPHONE     ?= "false"
EXTRA_OECONF_append   = "${@bb.utils.contains('MICROPHONE', 'true', ' --enable-microphone', '', d)}"

CUSTOM_AUDIO_ANALYSIS ?= "false"
EXTRA_OECONF_append   = "${@bb.utils.contains('CUSTOM_AUDIO_ANALYSIS', 'true', ' --enable-custom-audio-analysis', '', d)}"

XLOG_MODULE_NAME="CTRLM"
LOGGER:="${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm_voice_sdk', 'rdkx-logger', '', d)}"
inherit ${LOGGER}

do_install_append() {
   install -d ${D}${datadir}
   if [ "${AUDIO_PLAYBACK}" = "true" ]; then
      install -m 0644 ${S}/../tone_1khz.wav ${D}${datadir}
   fi
}
