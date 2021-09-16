SUMMARY = "This recipe compiles and installs the sample no-op libraries for the dtcp interface."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
PV = "${RDK_RELEASE}+git${SRCPV}"

PROVIDES = "virtual/dtcpmgr"
RPROVIDES_${PN} = "virtual/dtcpmgr"

PV = "${RDK_RELEASE}+gitr${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/dtcp;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git"

inherit autotools
