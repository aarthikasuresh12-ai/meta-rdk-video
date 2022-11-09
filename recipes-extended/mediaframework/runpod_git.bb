SUMMARY = "This recipe compiles runpod."
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://../../../LICENSE;md5=1a7c83d1696f84e35c7f8efcc4ca9d72"

require recipes-extended/mediaframework/rmfpod.inc

SRCREV = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=rmfgeneric"

S = "${WORKDIR}/git/qamsource/podmgr/runpod"

BREAKPAD_BIN = "runPod"
RDEPENDS_${PN} += "devicesettings"
INCLUDEDIR = " \
	-I=${includedir}/rdk/iarmmgrs-hal \
	-I=${includedir}/rdk/podmgr \
	-I=${includedir}/rdk/ds \
	-I=${includedir}/rdk/ds-hal \
	-I=${includedir}/rdk/ds-rpc"

CFLAGS += "${INCLUDEDIR}"
CXXFLAGS += "${INCLUDEDIR}"

CFLAGS += "-DDISABLE_DYNAMIC_LOGGING"

CFLAGS += "-DINCLUDE_BREAKPAD"
CXXFLAGS += "-DINCLUDE_BREAKPAD"

export OPENSSL_CFLAGS = "-I=${includedir}/openssl-0.9"
export OPENSSL_LDFLAGS = "-lcrypto-0.9"
export FSROOT = '='
export RMF_BUILD_OS = "DUMMY_USED"
export POD_PLATFORM = "DUMMY_USED"
export RDK_FSROOT_PATH = "${PKG_CONFIG_SYSROOT_DIR}"
export IARMBUS_LIBS = "-lIARMBus -ldbus-1 -ldirect"
export DSMGR_LIBS = "-lds -ldshalcli"
export NETSNMP_LIBS = "-lnetsnmpagent -lnetsnmphelpers -lnetsnmpmibs -lnetsnmp"

DEPENDS= "rmfosal rdk-logger rmfpodmgr rmfpodserver openssl-0.9 devicesettings virtual/sectionfilter-hal libtirpc breakpad-wrapper"

# enables generic mediaplayer sink
inherit autotools pkgconfig coverity
