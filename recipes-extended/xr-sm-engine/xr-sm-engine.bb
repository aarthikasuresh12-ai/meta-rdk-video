SUMMARY = "xrpSMEngine is a state machine engine written in C."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "1.0+git${SRCPV}"

SRCREV_xrpSMEngine = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/xrpSMEngine;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=xrpSMEngine"

S = "${WORKDIR}/git"

FILES_${PN} += "${includedir}/xrpSMEngine.h \
              "

XLOG_MODULE_NAME="XRSM"

INHERIT_BREAKPAD_WRAPPER := "${@bb.utils.contains('BBLAYERS', '${RDKROOT}/meta-rdk', 'breakpad-wrapper', '',d)}"
INHERIT_RDKXLOGGER := "${@bb.utils.contains('BBLAYERS', '${RDKROOT}/meta-rdk-voice-sdk', 'rdkx-logger', '',d)}"

inherit autotools pkgconfig coverity ${INHERIT_BREAKPAD_WRAPPER} ${INHERIT_RDKXLOGGER}

INCLUDE_DIRS = ""

CFLAGS_append = " -std=c11 -fPIC -D_REENTRANT -D_POSIX_C_SOURCE=200809L -Wall -Werror -rdynamic ${INCLUDE_DIRS} "
