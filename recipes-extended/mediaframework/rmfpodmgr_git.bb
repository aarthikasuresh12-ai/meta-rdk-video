SUMMARY = "This receipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=bc14e971d0e95ae5ca07a810dcd5893b"

require recipes-extended/mediaframework/rmfpod.inc

SRCREV = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=rmfgeneric"
S = "${WORKDIR}/git/qamsource/podmgr"

export OPENSSL_CFLAGS = "-I=${includedir}/openssl-0.9"
export OPENSSL_LDFLAGS = "-lcrypto-0.9"
export FSROOT = '='
export LIBPREFIX = "=${libdir}"
export NETSNMP_LIBS = "${LIBPREFIX}/libnetsnmpagent.la ${LIBPREFIX}/libnetsnmphelpers.la ${LIBPREFIX}/libnetsnmpmibs.la ${LIBPREFIX}/libnetsnmp.la"
export SNMP_LIBS = "-lsnmpinterface ${NETSNMP_LIBS}"
export RMF_OSAL_LIBS = "-lrmfosal -lrmfosalutils -lrmfproxyservice"
export QAM_LIBS = "-loobsimanager -loobsicache -linbsimanager -lsectionfilter"
export SF_LIBS = "-linbsectionfilter -loobsectionfilter"
export TRH_LIBS = " -ltrh "

CFLAGS += "-I=${includedir}/rdk/iarmmgrs-hal -I=${includedir}/rdk/podmgr"
CXXFLAGS += "${CFLAGS}"

DEPENDS= "rmfosal rdk-logger rmfgenericheaders rmfpodmgrheaders openssl-0.9 iarmmgrs-hal-headers trh"

# enables generic mediaplayer sink
inherit autotools coverity
EXTRA_OECONF = "--enable-yocto"

do_install_append () {
	# if changes in header install is needed please do them in
	# rmfpodmgrheaders recipe not here
	rm -rf ${D}${includedir}
}
