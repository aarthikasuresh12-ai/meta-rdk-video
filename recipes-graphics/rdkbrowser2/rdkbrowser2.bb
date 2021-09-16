DESCRIPTION = "This receipe compiles RDKBrowser2 component"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS += "pxcore-libnode ${WPEWEBKIT} rdk-logger"

RDEPENDS_${PN} += "bash"

require recipes-graphics/pxcore-libnode/pxcore-libnode-env.inc

inherit pkgconfig

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/rdkbrowser2;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

BROWSER ?= "wpe"

inherit pkgconfig

# Setup the conditional environment
setup_cond_env() {
    # Uncomment the following line to make the rdkbrowsertest
#    test "$USE_PXCORE_SHARED_LIBS" = "TRUE" && export BUILD_RDKBROWSERTEST=1

    # Uncomment the following line to make the rdkbrowsertest only (do not make the rdkbrowser2)
#    test -n "$BUILD_RDKBROWSERTEST" && export BUILD_RDKBROWSERTEST_ONLY=1

    return 0
}

EXTRA_OEMAKE += "SYSROOT_INCLUDES_DIR=\"${STAGING_INCDIR}\""
EXTRA_OEMAKE += "SYSROOT_LIBS_DIR=\"${STAGING_LIBDIR}\""
EXTRA_OEMAKE += "USE_PXCORE_SHARED_LIBS=TRUE"

EXTRA_OEMAKE_remove = " MAKEFLAGS= -e"
EXTRA_OEMAKE += "ENABLE_RDK_LOGGER=1"
EXTRA_OEMAKE += "ENABLE_WEB_AUTOMATION=1"
EXTRA_OEMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'HAS_SYSTEMD_JOURNAL=1', '', d)}"
INSANE_SKIP_${PN}_append_morty = " ldflags"

do_compile () {
   setup_cond_env
   if [ -z "$BUILD_RDKBROWSERTEST" -o -z "$BUILD_RDKBROWSERTEST_ONLY" ]; then
      oe_runmake -C ${S} -f Makefile_${BROWSER}
   fi
   if [ "$BUILD_RDKBROWSERTEST" ]; then
      oe_runmake -C ${S} -f Makefile_test
   fi
}

do_install () {
   setup_cond_env
   export INSTALL_PATH=${D}
   if [ -z "$BUILD_RDKBROWSERTEST" -o -z "$BUILD_RDKBROWSERTEST_ONLY" ]; then
      oe_runmake -C ${S} -f Makefile_${BROWSER} install
   fi
   if [ "$BUILD_RDKBROWSERTEST" ]; then
      oe_runmake -C ${S} -f Makefile_test install
   fi

   install -m 0755 ${S}/scripts/WPELauncher.sh ${D}${bindir}
   install -m 0755 ${S}/scripts/rdkbrowser2.sh ${D}${bindir}
   install -d ${D}/lib/rdk/
   install -m 0755 ${S}/scripts/launch_rdkbrowser2_server.sh ${D}/lib/rdk/
}

do_clean () {
   setup_cond_env
   if [ -z "$BUILD_RDKBROWSERTEST" -o -z "$BUILD_RDKBROWSERTEST_ONLY" ]; then
      oe_runmake -C ${S} -f Makefile_${BROWSER} clean
   fi
   if [ "$BUILD_RDKBROWSERTEST" ]; then
      oe_runmake -C ${S} -f Makefile_test clean
   fi
}

do_cleanall () {
   oe_runmake -C ${S} -f Makefile_${BROWSER} distclean
}

FILES_${PN} += "${base_libdir}/rdk/*"
