SUMMARY = "TVsettings HAL headers"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "${CMF_RDK_COMPONENTS_ROOT_GIT}/opensource/tvsettings;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};"

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
    install -d ${D}${includedir}/rdk/tv-hal
    install -m 0644 ${S}/hal/include/*.h ${D}${includedir}/rdk/tv-hal
}
