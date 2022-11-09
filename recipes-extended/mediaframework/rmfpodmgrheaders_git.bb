SUMMARY = "This receipe compiles rmfcore code base. This has interface clasess that would be necessary for all the mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://../../../LICENSE;md5=1a7c83d1696f84e35c7f8efcc4ca9d72"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/mediaframework;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=rmfgeneric"
SRCREV = "${AUTOREV}"

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', '--enable-stubcode=yes', '--enable-stubcode=no', d)}"

ALLOW_EMPTY_${PN} = "1"

S ="${WORKDIR}/git/qamsource/podmgr/inc"

DEPENDS+= "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', ' ', 'rmfpodmgrheaders-priv', d)}"
# enables generic mediaplayer sink
inherit autotools

do_install_append () {
	install -d ${D}${includedir}/rdk/podmgr

	#Prevent over-writing of same files installed by rmfgenericheaders from snmp/ipcutils/utils.
	excl_list="cVector.h utilityMacros.h persistentconfig.h cmhash.h cmThreadBase.h si_util.h vlEnv.h"
	for f in ${excl_list}
	do
		rm -f  ${D}${includedir}/${f}
	done
	mv ${D}${includedir}/*.h ${D}${includedir}/rdk/podmgr
}
