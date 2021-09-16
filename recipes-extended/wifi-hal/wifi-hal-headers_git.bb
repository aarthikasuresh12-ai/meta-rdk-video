SUMMARY = "WiFi RDK HAL definitionis."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"


SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/wifi;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# this is a HAL package only, nothing to build
do_compile[noexec] = "1"
do_configure[noexec] = "1"

# also get rid of the default dependency added in bitbake.conf
# since there is no 'main' package generated (empty)
RDEPENDS_${PN}-dev = ""

do_install() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/include/*.h ${D}${includedir}/
}
do_install[vardepsexclude] += "S"
