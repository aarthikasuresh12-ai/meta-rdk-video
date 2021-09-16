SUMMARY = "Devicesettings HAL definition"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "${RDK_RELEASE}+git${SRCPV}"

PV = "${RDK_RELEASE}+gitr${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/devicesettings;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

S = "${WORKDIR}/git"

# this is a HAL package only, nothing to build
do_compile[noexec] = "1"
do_configure[noexec] = "1"

# also get rid of the default dependency added in bitbake.conf
# since there is no 'main' package generated (empty)
RDEPENDS_${PN}-dev = ""
# to include the headers in the SDK
ALLOW_EMPTY_${PN} = "1"

do_install() {
    install -d ${D}${includedir}/rdk/ds-hal
    install -m 0644 ${S}/hal/include/*.h ${D}${includedir}/rdk/ds-hal
    rm -f ${D}${includedir}/rdk/ds-hal/*_sample.h
}

do_install[vardepsexclude] += "S"

