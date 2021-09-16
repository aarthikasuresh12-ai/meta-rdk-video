SUMMARY = "This recipe compiles and installs hdmicec component."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/hdmicec;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git/ccec/include/ccec/driver"

inherit autotools
ALLOW_EMPTY_${PN} = "1"
