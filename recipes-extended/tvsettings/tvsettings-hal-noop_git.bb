SUMMARY = "TVSettings HAL Sample"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PROVIDES = "virtual/tvsettings-hal"
SRC_URI = "${CMF_RDK_COMPONENTS_ROOT_GIT}/opensource/tvsettings/stubs;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};"

SRCREV = "${AUTOREV}"

PV = "git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "iarmmgrs-hal-headers iarmbus tvsettings-hal-headers"

EXTRA_OEMAKE += "-e MAKEFLAGS="

INCLUDE_DIRS = " \
    -I${STAGING_DIR_TARGET}${includedir}/rdk/iarmmgrs-hal/ \
    -I${STAGING_DIR_TARGET}${includedir}/rdk/iarmbus/ \
    -I${STAGING_DIR_TARGET}${includedir}/rdk/tv-hal/ \
    "

CFLAGS += "-fPIC -D_REENTRANT -Wall ${INCLUDE_DIRS}"

do_compile() {
        oe_runmake -C ${S}/
}

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${S}/libtvsettings-hal.so ${D}${libdir}/libtvsettings-hal.so
    ln -s libtvsettings-hal.so ${D}${libdir}/libtvsettings-hal.so.0
}

INSANE_SKIP_${PN} += "dev-so"
# Shared libs created by the RDK build aren't versioned, so we need
# to force the .so files into the runtime package (and keep them
# out of -dev package).
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"

