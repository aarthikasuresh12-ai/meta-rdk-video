SUMMARY = "This receipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1a7c83d1696f84e35c7f8efcc4ca9d72"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"

PV = "${RDK_RELEASE}+gitr${SRCPV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git"

do_compile[noexec] = "1"

do_install() {
	install -d ${D}${includedir}
	install -d ${D}${includedir}/rdk/podmgr
	install -m 0644 ${S}/init/*.h ${D}${includedir}
	install -m 0644 ${S}/core/*.h ${D}${includedir}
	install -m 0644 ${S}/dumpfilesink/*.h ${D}${includedir}
	install -m 0644 ${S}/hnsink/*.h ${D}${includedir}
	install -m 0644 ${S}/hnsource/*.h ${D}${includedir}
	install -m 0644 ${S}/ippvsource/*.h ${D}${includedir}
	install -m 0644 ${S}/mediadiscover/*.h ${D}${includedir}
	install -m 0644 ${S}/mediaplayersink/*.h ${D}${includedir}
	install -m 0644 ${S}/qamsource/common/*.h ${D}${includedir}
	install -m 0644 ${S}/qamsource/main/*.h ${D}${includedir}
	install -m 0644 ${S}/qamsource/oobsimgrstub/*.h ${D}${includedir}
	install -m 0644 ${S}/qamsource/simgr/inband/include/*.h ${D}${includedir}
	install -m 0644 ${S}/qamsource/simgr/oob/include/*.h ${D}${includedir}
	install -m 0644 ${S}/rbi/*.h ${D}${includedir}
	install -m 0644 ${S}/rmfproxyservice/*.h ${D}${includedir}
	install -m 0644 ${S}/smoothsource/*.h ${D}${includedir}
	install -m 0644 ${S}/transcoderfilter/*.h ${D}${includedir}
	install -m 0644 ${S}/vodsource/*.h ${D}${includedir}
	install -m 0644 ${S}/webkitsink/*.h ${D}${includedir}
	install -m 0644 ${S}/snmp/snmpmanager/*.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcclient/*.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/applitest.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/bits_cdl.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/cmhash.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/cmThreadBase.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/persistentconfig.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/si_util.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/utilityMacros.h ${D}${includedir}
	install -m 0644 ${S}/snmp/ipcutils/utils/vlEnv.h ${D}${includedir}/rdk/podmgr

	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', 'nopod', '', d)}" != "" ]; then
		install -m 0644 ${S}/soc_qamsource_stub/hal_include/*.h ${D}${includedir}
	fi

	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'external-cas', 'external-cas', '', d)}" != "" ]; then
		install -m 0644 ${S}/anycas/include/*.h ${D}${includedir}
		install -m 0644 ${S}/anycas/src/include/*.h ${D}${includedir}
	fi
}

ALLOW_EMPTY_${PN} = "1"

