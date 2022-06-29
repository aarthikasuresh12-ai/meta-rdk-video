SUMMARY = "This receipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://../../../LICENSE;md5=1a7c83d1696f84e35c7f8efcc4ca9d72"

require recipes-extended/mediaframework/rmfpod.inc

SRCREV = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=rmfgeneric"

S = "${WORKDIR}/git/qamsource/podmgr/podserver"

export OPENSSL_CFLAGS = "-I=${includedir}/openssl-0.9"
export OPENSSL_LDFLAGS = "-lcrypto-0.9"
export FSROOT = '='
export NETSNMP_LIBS = "-lnetsnmpagent -lnetsnmphelpers -lnetsnmpmibs -lnetsnmp"
export SNMP_LIBS = "-lsnmpinterface ${NETSNMP_LIBS}"
export RMF_OSAL_LIBS = "-lrmfosal -lrmfosalutils -lrmfproxyservice"
export QAM_LIBS = "-loobsimanager -loobsicache -linbsimanager -lsectionfilter"
export SF_LIBS = "-linbsectionfilter -loobsectionfilter"

CFLAGS += "-I=${includedir}/rdk/iarmmgrs-hal -I=${includedir}/rdk/podmgr"
CXXFLAGS += "${CFLAGS}"

EXTRA_OECONF = "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', '--enable-stubcode=yes', '--enable-stubcode=no', d)}"
EXTRA_OECONF += "--enable-yocto"

DEPENDS= "rmfgeneric rdk-logger rmfpodmgrheaders openssl-0.9 iarmmgrs-hal-headers virtual/sectionfilter-hal"
DEPENDS+= "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', ' ', 'podserver-priv', d)}"

# enables generic mediaplayer sink
inherit autotools coverity

do_install_append () {
	# if changes in header install is needed please do them in
	# rmfpodmgrheaders recipe not here
        install -d ${D}${base_libdir}/rdk
        install -D -m 0644 ${S}/hrvScripts/restart_dhcp.sh ${D}${base_libdir}/rdk

	rm -rf ${D}${includedir}
}

FILES_${PN} += "${base_libdir}/rdk/restart_dhcp.sh"
