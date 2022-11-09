SUMMARY = "Build Evergreen Cobalt Core library from source code. Use for debug and development puroposes."
HOMEPAGE = "https://cobalt.googlesource.com/"

LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://src/LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d \
    file://../depot_tools/LICENSE;md5=c2c05f9bdd5fc0b458037c2d1fb8d95e \
"

TOOLCHAINS_DIR = "starboard-toolchains"
CLANG_BUILD_REVISION = "365097-f7e52fbd-8"
CLANG_BUILD_SUBDIR = "${TOOLCHAINS_DIR}/x86_64-linux-gnu-clang-chromium-${CLANG_BUILD_REVISION}"

SRC_URI  = "git://cobalt.googlesource.com/cobalt.git;protocol=https;name=cobalt;branch=22.lts.stable"
SRC_URI += "git://cobalt.googlesource.com/depot_tools.git;protocol=https;destsuffix=depot_tools;name=depottools"
SRC_URI += "https://commondatastorage.googleapis.com/chromium-browser-clang/Linux_x64/clang-${CLANG_BUILD_REVISION}.tgz;subdir=${CLANG_BUILD_SUBDIR};name=clang"

PV = "2.5.5"
SRCREV_cobalt = "22.lts.5"
SRCREV_depottools = "913305037df7027dc118253b7c2d3655d181c612"
SRCREV_FORMAT = "cobalt_depottools"
do_fetch[vardeps] += " SRCREV_FORMAT SRCREV_cobalt SRCREV_depottools"

SRC_URI[clang.sha256sum] = "3adf7a2338e86c202527f6bfaa945697eb2189b8965f7598d9d1159a195dbd51"

DEPENDS = "python-native ninja-native bison-native openssl-native"

# By default we want to use Yocto to download toolchain. So, below is commented out
# to trigger download failure if Cobalt build script decides to refresh toolchain
# DEPENDS += " python-requests-native"

S = "${WORKDIR}/git"

inherit pythonnative pkgconfig breakpad-wrapper

BREAKPAD_BIN = "lib*.so*"

SB_VERSION = "13"
COBALT_ARCH_arm = "arm-${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'hardfp', 'softfp', d)}"
COBALT_ARCH_aarch64 = "arm64"
COBALT_PLATFORM ?= "evergreen-${COBALT_ARCH}-sbversion-${SB_VERSION}"
COBALT_BUILD_TYPE ?= "${@bb.utils.contains('DISTRO_FEATURES', 'cobalt-qa', 'qa', 'gold', d)}"
COBALT_APP_DIR = "/content/data/app/cobalt"

libdir = "${datadir}${COBALT_APP_DIR}/lib"

PATH_prepend = "${WORKDIR}/depot_tools:"

export STARBOARD_TOOLCHAINS_DIR = "${WORKDIR}/${TOOLCHAINS_DIR}"
export NINJA_STATUS='%p '

do_unpack_extra() {
    bbnote "Update toolchain build revision"
    echo -n ${CLANG_BUILD_REVISION} > ${WORKDIR}/${CLANG_BUILD_SUBDIR}/cr_build_revision
}
addtask unpack_extra after do_unpack before do_patch

do_configure() {
    ${S}/src/cobalt/build/gyp_cobalt -C ${COBALT_BUILD_TYPE} ${COBALT_PLATFORM}
}

do_compile[progress] = "percent"
do_compile() {
    ninja -C ${S}/src/out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE} cobalt
}

do_install() {
    install -d ${D}${datadir}${COBALT_APP_DIR}
    cp -arv --no-preserve=ownership ${S}/src/out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/content ${D}${datadir}${COBALT_APP_DIR}/
    cp -arv --no-preserve=ownership ${S}/src/out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/lib ${D}${datadir}${COBALT_APP_DIR}/

    # use system provided certs
    rm -rf ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs
    mkdir -p ${D}${datadir}${COBALT_APP_DIR}/content/ssl/
    ln -s /etc/ssl/certs ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs
}

FILES_${PN}  = "${datadir}${COBALT_APP_DIR}/content/*"
FILES_${PN} += "${datadir}${COBALT_APP_DIR}/lib/libcobalt.so"
FILES_${PN}-dbg += "${datadir}${COBALT_APP_DIR}/lib/.debug/libcobalt.so"
FILES_SOLIBSDEV = ""

INSANE_SKIP_${PN} += "dev-so"
INSANE_SKIP_${PN}-dbg += "dev-so"

PROVIDES = "virtual/cobalt-evergreen"
RPROVIDES_${PN} = "virtual/cobalt-evergreen"
