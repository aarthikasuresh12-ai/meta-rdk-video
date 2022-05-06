FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://stress-ng.conf "

do_install_append() {
install -d  ${D}${sysconfdir}
install -m 755 ${WORKDIR}/stress-ng.conf ${D}${sysconfdir}
}
