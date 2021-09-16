SUMMARY = "RDK Diagnostics"
LICENSE = "Apache-2.0 & BSD-3-Clause & MIT-CMU & MIT & ISC"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=0ba6e3b8a0a9718aabfbce5cff78c6d4"

PV = "${RDK_RELEASE}+git${SRCPV}"

DEPENDS = "glib-2.0"
DEPENDS_append_hybrid = " net-snmp"

RDEPENDS_${PN} += "jquery bash"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/diagnostics;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git/QAM_device"

inherit coverity pkgconfig

do_configure_prepend() {
  rm -f ${S}/Makefile
}
do_install_append_client() {
	install -d 0755 ${D}/var/www
	install -d 0755 ${D}/var/www/htmldiag
	install -d 0755 ${D}/var/www/htmldiag/cgi-bin
	install -d 0755 ${D}/var/www/htmldiag/cmn
	install -d 0755 ${D}/var/www/htmldiag/js
	install -d 0755 ${D}/var/www/htmldiag/json
	install -d 0755 ${D}/var/www/htmldiag/includes
        install -d 0755 ${D}/var/www/htmldiag2

        # Installing libs and scripts common to all platforms
	cp -a ${S}/../ip_device/www/htmldiag/cgi-bin/* ${D}/var/www/htmldiag/cgi-bin
	cp -a ${S}/../ip_device/www/htmldiag/cmn/* ${D}/var/www/htmldiag/cmn
	cp -a ${S}/../ip_device/www/htmldiag/js/* ${D}/var/www/htmldiag/js
	cp -a ${S}/../ip_device/www/htmldiag/json/* ${D}/var/www/htmldiag/json
   
	cp -a ${S}/../ip_device/www/htmldiag/summary_info.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/mfg_hdcp.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/mfg_sys_descr.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/system_addr.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/system_hdmi_info.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/osd_diag.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/oper_stat.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/moca.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/moca1.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/moca_connected_devices.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/moca_mesh_txrates.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/summary_other.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/remotes_ip.html ${D}/var/www/htmldiag
	cp -a ${S}/../ip_device/www/htmldiag/remotes_reboot.html ${D}/var/www/htmldiag

	cp -a ${S}/../ip_device/www/htmldiag/includes/header.html ${D}/var/www/htmldiag/includes
	cp -a ${S}/../ip_device/www/htmldiag/includes/nav.html ${D}/var/www/htmldiag/includes
	cp -a ${S}/../ip_device/www/htmldiag/includes/userbar.html ${D}/var/www/htmldiag/includes
	cp -a ${S}/../ip_device/www/htmldiag/includes/footer.html ${D}/var/www/htmldiag/includes

        cp -a ${S}/../Advanced_Diagnostics/htmldiag/ip_device/* ${D}/var/www/htmldiag2
        cp -a ${S}/../Advanced_Diagnostics/htmldiag/common ${D}/var/www/htmldiag2

    install -d 0755 ${D}/home/root/diagnostics
    cp -R --no-dereference --preserve=mode,links -v ${S}/../diagnostics/* ${D}/home/root/diagnostics

    

}

do_install_append_hybrid() {
	install -m 0755 -d ${D}/var/www/htmldiag/cgi-bin
	cp -a ${S}/www/htmldiag/* ${D}/var/www/htmldiag
	cp -a ${S}/snmp2json.sh ${D}/var/www/htmldiag/cgi-bin
        install -d 0755 ${D}/var/www/htmldiag2
        cp -a ${S}/../Advanced_Diagnostics/htmldiag/QAM_device/* ${D}/var/www/htmldiag2
        cp -a ${S}/../Advanced_Diagnostics/htmldiag/common ${D}/var/www/htmldiag2

}

FILES_${PN} += "/var/www/ \
		/var/www/htmldiag \
                /var/www/htmldiag2 \
                /home/root/diagnostics"
