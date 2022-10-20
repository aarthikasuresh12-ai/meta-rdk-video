DESCRIPTION = "This is the mock implementation for mfrlib."
SECTION = "base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/../meta-rdk/licenses/Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "iarmmgrs-hal-headers"

SRC_URI = "file://mfrlib.c"

PROVIDES = "virtual/mfrlib"
RPROVIDES_${PN} = "virtual/mfrlib"

CFLAGS += " \
	-I${PKG_CONFIG_SYSROOT_DIR}${includedir}/rdk/iarmmgrs-hal \
	-I${PKG_CONFIG_SYSROOT_DIR}${includedir}/rdk/iarmbus \
"

SOVER = "0.0.0"
TARGET_CC_ARCH += "${LDFLAGS}"

do_compile () {
	${CC} -shared -Wl,-soname,libRDKMfrLib.so.0 ${CFLAGS} ${WORKDIR}/mfrlib.c -o ${WORKDIR}/libRDKMfrLib.so.${SOVER}
}

do_install () {
	install -d ${D}${libdir}
	install -m 0755 ${WORKDIR}/libRDKMfrLib.so.${SOVER} ${D}${libdir}
	ln -s libRDKMfrLib.so.${SOVER} ${D}${libdir}/libRDKMfrLib.so
}

FILES_${PN} += "${libdir}/*"

FILES_SOLIBSDEV = ""
SOLIBS = ".so"
INSANE_SKIP_${PN} += "dev-so"
