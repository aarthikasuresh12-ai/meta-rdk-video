SUMMARY = "Sys mon tool hostdataconverter recipe"

DESCRIPTION = "Sys mon tool hostdataconverter recipe"

SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/sys_mon_tools/hostdataconverter;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
S = "${WORKDIR}/git"

DEPENDS = "rmfgeneric"

inherit autotools coverity
