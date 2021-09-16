SUMMARY = "This recipe compiles utility used for mfr utilities"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}"
SRCREV_mfr-utils = "${AUTOREV}"

S = "${WORKDIR}/git"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/sys_mon_tools/mfr_utils;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=mfr-utils"

PROVIDES="mfr-utils"

inherit pkgconfig autotools systemd

DEPENDS="mfr-library dvrmgr-hal-broadcom rmfpodmgrheaders iarmmgrs-hal-headers"
RDEPENDS_mfr-utils_dunfell = "mfr-library"

SRCREV_FORMAT ?= "mfr-utils"
do_fetch[vardeps] += "SRCREV_FORMAT"

do_install() {
        install -d ${D}${bindir}  
        install -m 0755 ${B}/mfr_util ${D}${bindir}
}

FILES_${PN} += "${bindir}/mfr_util"








