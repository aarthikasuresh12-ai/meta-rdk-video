
SUMMARY = "Lightnening based application for network control"
SECTION = "rdk/samples"

LICENSE = "RDK & BSD-3-Clause & MIT & Apache-2.0 & BitstreamVera"
LIC_FILES_CHKSUM = "file://${S}/../../LICENSE;md5=fac1f1de1b2231cdc801d64ac2607c6b"

SRC_URI = "${CMF_GIT_ROOT}/components/opensource/RDK_apps;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/diagnostics/dist"

#Ligtening application, no need for configuration/compilation
do_compile[noexec] = "1"
do_configure[noexec] = "1"

RDEPENDS_${PN}-dev = ""

do_install() {
   install -d ${D}/home/root/lxdiag
   cp -r ${S}/* ${D}/home/root/lxdiag/
}


FILES_${PN} += "/home/root/*"
