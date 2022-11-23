SUMMARY = "Build Evergreen Cobalt Core library from source code. Use for debug and development puroposes."
HOMEPAGE = "https://cobalt.googlesource.com/"

LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://src/LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d \
"

TOOLCHAINS_DIR = "starboard-toolchains"
CLANG_BUILD_REVISION = "365097-f7e52fbd-8"
CLANG_BUILD_SUBDIR = "${TOOLCHAINS_DIR}/x86_64-linux-gnu-clang-chromium-${CLANG_BUILD_REVISION}"

SRC_URI  = "git://cobalt.googlesource.com/cobalt.git;protocol=https;name=cobalt;branch=23.lts.stable"
SRC_URI += "https://commondatastorage.googleapis.com/chromium-browser-clang/Linux_x64/clang-${CLANG_BUILD_REVISION}.tgz;subdir=${CLANG_BUILD_SUBDIR};name=clang"

PV = "3.2.1"
SRCREV_cobalt = "23.lts.2"
do_fetch[vardeps] += " SRCREV_FORMAT SRCREV_cobalt"

SRC_URI[clang.sha256sum] = "3adf7a2338e86c202527f6bfaa945697eb2189b8965f7598d9d1159a195dbd51"

DEPENDS  = "ninja-native bison-native openssl-native gn-native ccache-native"
DEPENDS += " python3-six-native python3-urllib3-native"

S = "${WORKDIR}/git"

inherit python3native pkgconfig breakpad-wrapper

BREAKPAD_BIN = "lib*.so*"

SB_VERSION = "13"
COBALT_ARCH_arm = "arm-${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'hardfp', 'softfp', d)}"
COBALT_ARCH_aarch64 = "arm64"
COBALT_PLATFORM ?= "evergreen-${COBALT_ARCH}"
COBALT_BUILD_TYPE ?= "${@bb.utils.contains('DISTRO_FEATURES', 'cobalt-qa', 'qa', 'gold', d)}"
COBALT_APP_DIR = "/content/data/app/cobalt"

GN_ARGS_EXTRA ?= ""
GN_ARGS_EXTRA_append = " sb_api_version=${SB_VERSION}"

libdir = "${datadir}${COBALT_APP_DIR}/lib"

export _STARBOARD_TOOLCHAINS_DIR_KEY = "${WORKDIR}/${TOOLCHAINS_DIR}"
export NINJA_STATUS='%p '
export PYTHONPATH="${S}"

do_unpack_extra() {
    bbnote "Update toolchain build revision"
    echo -n ${CLANG_BUILD_REVISION} > ${WORKDIR}/${CLANG_BUILD_SUBDIR}/cr_build_revision
}
addtask unpack_extra after do_unpack before do_patch

# Cobalt GN requires '/usr/bin/env python' to work
do_configure[prefuncs] += "setup_python_link"
setup_python_link() {
    if [ ! -e ${STAGING_BINDIR_NATIVE}/python3-native/python ]; then
      (cd ${STAGING_BINDIR_NATIVE}/python3-native && ln -s python3 python)
    fi
}

do_configure() {
    ${PYTHON} cobalt/build/gn.py -c ${COBALT_BUILD_TYPE} -p ${COBALT_PLATFORM} --overwrite_args
    echo "${GN_ARGS_EXTRA}" | tr ' ' '\n' >> out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/args.gn
}

do_compile[progress] = "percent"
do_compile() {
    ninja -C out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE} cobalt
}

do_install() {
    install -d ${D}${datadir}${COBALT_APP_DIR}/lib/
    cp -arv --no-preserve=ownership out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/content ${D}${datadir}${COBALT_APP_DIR}/
    cp -arv --no-preserve=ownership out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/libcobalt.so ${D}${datadir}${COBALT_APP_DIR}/lib/

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
