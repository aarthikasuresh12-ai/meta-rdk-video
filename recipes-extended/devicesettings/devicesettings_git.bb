SUMMARY = "Cross-platform library for controlling STB platform hardware configuration"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
PV = "${RDK_RELEASE}+git${SRCPV}"

PV = "${RDK_RELEASE}+gitr${SRCPV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/devicesettings;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

# devicesettings is not a 'generic' component, as some of its source
# files include .h files that come from the HAL implementation until
# this is fixed (see https://cards.linaro.org/browse/RDK-108).  Each
# BSP needs to implement virtual/devicesettings-hal when
# devicesettings become 'generic' we will remove the dependency on the
# hal, Note: we make this package machine specific since it uses a
# machine HAL
PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS="directfb iarmbus rdk-logger virtual/devicesettings-hal devicesettings-hal-headers safec-common-wrapper"
RDEPENDS_${PN} += "directfb"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec ', " ", d)}"

S = "${WORKDIR}/git"

inherit coverity

CFLAGS += "-DSAFEC_DUMMY_API"
CXXFLAGS += "-DSAFEC_DUMMY_API "

#
# ds-hal header should preceed ds/include 
# to achieve desired HAL override
#
INCLUDE_DIRS = " \
    -I${S}/config \
    -I${S}/config/include \
    -I${STAGING_DIR_TARGET}${includedir}/rdk/ds-hal \
    -I./include \
    -I${S}/rpc/include \
    -I${S}/ds/include \
    -I${S}/hal/include \
    -I${STAGING_DIR_TARGET}${includedir} \
    -I${STAGING_DIR_TARGET}${includedir}/rdk/iarmbus \
    -I${STAGING_DIR_TARGET}${includedir}/rdk/logger \
    -I${STAGING_DIR_TARGET}${includedir}/glib-2.0 \
    -I${STAGING_DIR_TARGET}${includedir}/directfb \
    -I${STAGING_DIR_TARGET}${libdir}/glib-2.0/include \
    -I${STAGING_DIR_TARGET}${includedir}/logger \
    "

# note: we really on 'make -e' to control LDFLAGS and CFLAGS from here. This is
# far from ideal, but this is to workaround the current component Makefile
LDFLAGS += "-lpthread -lglib-2.0 -L. -lIARMBus -ldl "
CFLAGS += "-fPIC -D_REENTRANT -Wall ${INCLUDE_DIRS}"
CFLAGS += "-DRDK_DSHAL_NAME="\""libds-hal.so.0\""""
CFLAGS += " -DYOCTO_BUILD"
CFLAGS += " -DDS_AUDIO_SETTINGS_PERSISTENCE"


# Shared libs created by the RDK build aren't versioned, so we need
# to force the .so files into the runtime package (and keep them
# out of -dev package).
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
TARGET_CC_ARCH += "${LDFLAGS}"
do_configure_prepend() {
	rm -rf ${S}/Makefile
}

do_compile() {

    # remove local version of hal since we use devicesettings-hal-headers,
    # and want to make sure we don't use this copy by error.
    rm -rf hal

    # and now the generic components
    oe_runmake -B -C ${S}/rpc/cli
    oe_runmake -B -C ${S}/rpc/srv
    export CFLAGS="$CFLAGS -std=c++11"
    oe_runmake -B -C ${S}/ds
#To Build Test Samples under "sample/"
    export LDFLAGS="$LDFLAGS -L${S}/ds -lds -L${S}/rpc/cli -ldshalcli"
    oe_runmake -B -C ${S}/sample
}

do_install() {

    install -d ${D}${includedir}/rdk/ds
    install -m 0644 ${S}/ds/include/*.h* ${D}${includedir}/rdk/ds
    install -m 0644 ${S}/ds/*.h* ${D}${includedir}/rdk/ds

    install -d ${D}${includedir}/rdk/ds-rpc
    install -m 0644 ${S}/rpc/include/*.h* ${D}${includedir}/rdk/ds-rpc

    install -d ${D}${libdir}
    for i in ${S}/rpc/cli/*.so ${S}/rpc/srv/*.so ${S}/ds/*.so; do
        install -m 0755 $i ${D}${libdir}
    done
}
INSANE_SKIP_${PN} = "ldflags"
