SUMMARY = "A high-performance, small-footprint platform that implements a subset of HTML5/CSS/JS to run applications, including the YouTube TV app"
HOMEPAGE = "https://cobalt.googlesource.com/"

LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://src/LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d \
    file://../depot_tools/LICENSE;md5=c2c05f9bdd5fc0b458037c2d1fb8d95e \
    file://../starboard/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

# Disable by default
DEFAULT_PREFERENCE_morty = "-1"
TOOLCHAIN = "gcc"

SRC_URI  = "git://cobalt.googlesource.com/cobalt.git;protocol=https;name=cobalt;branch=22.lts.stable"
SRC_URI += "git://cobalt.googlesource.com/depot_tools.git;protocol=https;destsuffix=depot_tools;name=depottools"
SRC_URI += "${CMF_GIT_ROOT}/rdk/components/generic/cobalt;protocol=${CMF_GIT_PROTOCOL};destsuffix=starboard;name=starboard;branch=master"
SRC_URI += "file://0001-Fix-crash-on-unfreeze-from-preloaded-state.patch"

PR = "3"
SRCREV_cobalt = "22.lts.${PR}"
SRCREV_depottools = "913305037df7027dc118253b7c2d3655d181c612"
# March 29, 2022
SRCREV_starboard = "6a16d9f214bd024720a8ced50b9f202c08dcb8b1"

SRCREV_FORMAT = "cobalt_depottools_starboard"

do_fetch[vardeps] += " SRCREV_FORMAT SRCREV_cobalt SRCREV_depottools SRCREV_starboard"
S = "${WORKDIR}/git"

DEPENDS += "virtual/egl essos wpeframework-clientlibraries gstreamer1.0 gstreamer1.0-plugins-base python-native ninja-native bison-native openssl-native"
RDEPENDS_${PN} += "gstreamer1.0-plugins-base-app gstreamer1.0-plugins-base-playback"

TUNE_CCARGS_remove = "-fno-omit-frame-pointer -fno-optimize-sibling-calls"

PACKAGECONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'opencdm', 'opencdm', '', d)}"
PACKAGECONFIG[opencdm] = ""
PACKAGECONFIG[nplb] = ""

inherit pythonnative pkgconfig

COBALT_PLATFORM ?= ""
COBALT_PLATFORM_brcmarm ?= "rdk-brcm-arm"
COBALT_PLATFORM_rpi ?= "rdk-rpi"
COBALT_PLATFORM_arm ?= "rdk-arm"
COBALT_BUILD_TYPE ?= ""${@bb.utils.contains('DISTRO_FEATURES', 'cobalt-qa', 'qa', 'gold', d)}""

PATH_prepend = "${WORKDIR}/depot_tools:"

do_unpack_extra() {
    bbnote "copy starboard"
    cp -ar "${WORKDIR}/starboard/src/third_party/starboard" "${S}/src/third_party/"
}
addtask unpack_extra after do_unpack before do_patch

do_configure() {
    export COBALT_HAS_OCDM="${@bb.utils.contains('PACKAGECONFIG', 'opencdm', '1', '0', d)}"
    export COBALT_ARM_CALLCONVENTION="${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'hardfp', 'softfp', d)}"
    ${S}/src/cobalt/build/gyp_cobalt -C qa -C gold -C devel ${COBALT_PLATFORM}
}

do_compile[progress] = "percent"
do_compile() {
    export NINJA_STATUS='%p '
    ninja -C ${S}/src/out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE} cobalt cobalt_bin
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/src/out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/cobalt_bin ${D}${bindir}

    install -d ${D}${libdir}
    install -m 0755 ${S}/src/out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/lib/libcobalt.so ${D}${libdir}

    install -d ${D}${datadir}/content
    cp -arv --no-preserve=ownership ${S}/src/out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/content ${D}${datadir}/

    # use system provided certs
    rm -rf ${D}${datadir}/content/data/ssl/certs
    mkdir -p ${D}${datadir}/content/data/ssl/
    ln -s /etc/ssl/certs ${D}${datadir}/content/data/ssl/certs
}

# NPLB
do_compile_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'nplb', 'true', 'false', d)}; then
        export PYTHONHTTPSVERIFY=0
        ninja -C ${S}/src/out/${COBALT_PLATFORM}_devel nplb
    fi
}
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'nplb', 'true', 'false', d)}; then
        install -m 0755 ${S}/src/out/${COBALT_PLATFORM}_devel/nplb ${D}${bindir}
        cp -arv --no-preserve=ownership ${S}/src/out/${COBALT_PLATFORM}_devel/content ${D}${datadir}/
    fi
}

FILES_${PN}  = "${bindir}/*"
FILES_${PN} += "${libdir}/libcobalt.so"
FILES_${PN} += "${datadir}/content/*"

FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} += "dev-so"
INSANE_SKIP_${PN}-dbg += "dev-so"
