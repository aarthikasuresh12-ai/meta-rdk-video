SUMMARY = "RDK Diagnostics - snmp2json"
LICENSE = "Apache-2.0 & BSD-3-Clause & MIT-CMU & MIT & ISC"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=0ba6e3b8a0a9718aabfbce5cff78c6d4"

PV = "${RDK_RELEASE}+git${SRCPV}"

DEPENDS = "glib-2.0"
DEPENDS_append_hybrid = " net-snmp"

RDEPENDS_${PN} += "jquery"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/diagnostics;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git/QAM_device"

inherit coverity pkgconfig

do_configure_prepend() {
  rm -f ${S}/Makefile
}


inherit autotools

FILES_${PN} += "snmp2json "
