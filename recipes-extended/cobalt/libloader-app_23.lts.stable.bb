SUMMARY = "Evergreen Cobalt loader_app library."
HOMEPAGE = "https://cobalt.googlesource.com/"

LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d \
    file://../starboard/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

require starboard_revision.inc

TOOLCHAIN = "gcc"

SRC_URI  = "git://cobalt.googlesource.com/cobalt.git;protocol=https;name=cobalt;branch=23.lts.stable"
SRC_URI += "${STARBOARD_SRC_URI};protocol=${CMF_GIT_PROTOCOL};destsuffix=starboard;name=starboard;branch=master"
SRC_URI += "file://23/0001-Include-RDK-platforms.patch"
SRC_URI += "file://23/0002-Fix-crashpad-build.patch"

PR = "2"
SRCREV_cobalt = "23.lts.${PR}"
SRCREV_starboard = "${STARBOARD_SRCREV}"

SRCREV_FORMAT = "cobalt_starboard"

do_fetch[vardeps] += " SRCREV_FORMAT SRCREV_cobalt SRCREV_starboard"
S = "${WORKDIR}/git"

DEPENDS += "virtual/egl essos wpeframework-clientlibraries gstreamer1.0 gstreamer1.0-plugins-base"
DEPENDS += " ninja-native bison-native openssl-native gn-native ccache-native"
DEPENDS += " python3-six-native python3-urllib3-native"

RDEPENDS_${PN} += "gstreamer1.0-plugins-base-app gstreamer1.0-plugins-base-playback"

TUNE_CCARGS_remove = "-fno-omit-frame-pointer -fno-optimize-sibling-calls"

COBALT_PLATFORM ?= ""
COBALT_PLATFORM_arm ?= "rdk-arm"
COBALT_PLATFORM_rpi ?= "rdk-rpi"
COBALT_BUILD_TYPE ?= "${@bb.utils.contains('DISTRO_FEATURES', 'cobalt-qa', 'qa', 'gold', d)}"

PACKAGECONFIG ?= "cryptography ${COBALT_BUILD_TYPE}"
PACKAGECONFIG_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'opencdm', 'opencdm', '', d)}"
PACKAGECONFIG_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'thunder_security_disable', '', 'securityagent', d)}"

PACKAGECONFIG[opencdm]       = "rdk_enable_ocdm=true,rdk_enable_ocdm=false,,"
PACKAGECONFIG[securityagent] = "rdk_enable_securityagent=true,rdk_enable_securityagent=false,,"
PACKAGECONFIG[cryptography]  = "rdk_enable_cryptography=true,rdk_enable_cryptography=false,,"
PACKAGECONFIG[qa]            = ",,nodejs-native,"
PACKAGECONFIG[gold]          = ""
PACKAGECONFIG[nplb]          = ""

GN_ARGS_EXTRA ?= ""
GN_ARGS_EXTRA_append = " sb_is_evergreen_compatible=true sb_evergreen_compatible_enable_lite=true"
GN_ARGS_EXTRA_append = " rdk_arm_call_convention=\"${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'hardfp', 'softfp', d)}\""
GN_ARGS_EXTRA_append = " ${PACKAGECONFIG_CONFARGS}"

inherit python3native pkgconfig breakpad-wrapper

BREAKPAD_BIN = "lib*.so*"

export PYTHONPATH="${S}"

do_unpack_extra() {
    bbnote "copy starboard"
    cp -ar "${WORKDIR}/starboard/src/third_party/starboard" "${S}/third_party/"
}
addtask unpack_extra after do_patch before do_configure

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
    export NINJA_STATUS='%p '
    ninja -C out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE} loader_app crashpad_handler
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/crashpad_handler ${D}${bindir}

    install -d ${D}${libdir}
    install -m 0755 out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/libloader_app.so ${D}${libdir}

    install -d ${D}${datadir}/content
    cp -arv --no-preserve=ownership out/${COBALT_PLATFORM}_${COBALT_BUILD_TYPE}/content ${D}${datadir}/
}

# NPLB
do_configure_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'nplb', 'true', 'false', d)}; then
        ${PYTHON} cobalt/build/gn.py -c devel  -p ${COBALT_PLATFORM} --overwrite_args
        echo "${GN_ARGS_EXTRA}" | tr ' ' '\n' >> out/${COBALT_PLATFORM}_devel/args.gn
    fi
}
do_compile_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'nplb', 'true', 'false', d)}; then
        export PYTHONHTTPSVERIFY=0
        ninja -C out/${COBALT_PLATFORM}_devel nplb nplb_evergreen_compat_tests
    fi
}
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'nplb', 'true', 'false', d)}; then
        install -m 0755 out/${COBALT_PLATFORM}_devel/nplb ${D}${bindir}
        install -m 0755 out/${COBALT_PLATFORM}_devel/nplb_evergreen_compat_tests ${D}${bindir}
        cp -arv --no-preserve=ownership out/${COBALT_PLATFORM}_devel/content ${D}${datadir}/
    fi
}

FILES_${PN}  = "${bindir}/*"
FILES_${PN} += "${libdir}/libloader_app.so"
FILES_${PN} += "${datadir}/content/*"

FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} += "dev-so"
INSANE_SKIP_${PN}-dbg += "dev-so"
