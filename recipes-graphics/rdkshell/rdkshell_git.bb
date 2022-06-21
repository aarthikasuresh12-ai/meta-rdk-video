DESCRIPTION = "RDKShell"
HOMEPAGE = ""

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=25e1268bd2a2291ebad380ef00c2f842"

DEPENDS = "westeros essos rapidjson libjpeg-turbo libpng"

inherit cmake

PV = "1.x+git${SRCPV}"

S = "${WORKDIR}/git"

#May 26, 2022
SRC_URI = "git://github.com/rdkcentral/RDKShell;branch=master"
SRCREV = "bda7b79bf42c1e915bfcec73423f25c0af5cade5"

do_install() {
   install -d ${D}/home/root
   cp -a ${B}/rdkshell ${D}/home/root

   install -d ${D}/${libdir}
   cp -a ${B}/librdkshell.so ${D}/${libdir}

   install -d ${D}/${libdir}/plugins/westeros
   cp -a ${B}/extensions/RdkShellExtendedInput/libwesteros_plugin_rdkshell_extended_input.so.1.0.0 ${D}/${libdir}/plugins/westeros/libwesteros_plugin_rdkshell_extended_input.so

   install -d ${D}/${libdir}
   install -d ${D}${includedir}
   mkdir -p ${D}${includedir}/rdkshell

   install -m 0644 ${S}/*.h ${D}${includedir}/rdkshell
}

FILES_${PN} += "${libdir}/*.so"
FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} += "dev-so staticdev"
INSANE_SKIP_${PN}_append_morty = " ldflags"
INSANE_SKIP_${PN} += "already-stripped"
INSANE_SKIP_${PN}_append_morty = " ldflags"
DEBIAN_NOAUTONAME_${PN} = "1"
BBCLASSEXTEND = "native"

FILES_${PN} += "/home/root/rdkshell"
FILES_${PN} += "${libdir}/librdkshell.so"
FILES_${PN} += "${libdir}/plugins/westeros/libwesteros_plugin_rdkshell_extended_input.so"
