SUMMARY = "Evergreen Cobalt Core library."
HOMEPAGE = "https://cobalt.googlesource.com/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := "${THISDIR}/evergreen:"
DEPENDS += "unzip-native breakpad-native"
OVERRIDES_append = ":${TARGET_FPU}"

CRX_FILE_arm_hard = "cobalt_evergreen_2.5.5_arm-hardfp_sbversion-13_release_20220715235857.crx"
DBG_FILE_arm_hard = "libcobalt_2.5.5_unstripped_arm-hardfp_sbversion-13_release_7f8fc3e6a2ce12ab.tar.gz"
CRX_FILE_SHA256SUM_arm_hard = "910860cddf42c15601efe630756238eb022d4fd5486e86d651cc34c9569c3a0a"
DBG_FILE_SHA256SUM_arm_hard = "e4d72e472266a0bd87ce25f0b805ece33a7ee265e8659872ac35f457d3e82add"

CRX_FILE_aarch64 = "cobalt_evergreen_2.5.5_arm64_sbversion-13_release_20220715235857.crx"
DBG_FILE_aarch64 = "libcobalt_2.5.5_unstripped_arm64_sbversion-13_release_68023fb0e666d3b2.tar.gz"
CRX_FILE_SHA256SUM_aarch64 = "a39a7b722c2d1da782a4d458f523dd208f74c53bb3c7478b31611a6fd354bb98"
DBG_FILE_SHA256SUM_aarch64 = "13f3c59fb804edc2ff25c95a1182fc335baf7283c56d1a08e61a05606799b8b9"

PV = "2.5.5"
YT_BASE_URI = "https://github.com/youtube/cobalt/releases/download/22.lts.5"
SRC_URI  = "${YT_BASE_URI}/${CRX_FILE};name=cobalt"
SRC_URI += "${YT_BASE_URI}/${DBG_FILE};name=cobalt_debug;subdir=debug_syms"
SRC_URI += "file://LICENSE"
SRC_URI[cobalt.sha256sum] = "${CRX_FILE_SHA256SUM}"
SRC_URI[cobalt_debug.sha256sum] = "${DBG_FILE_SHA256SUM}"

COBALT_APP_DIR = "/content/data/app/cobalt"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_preunpack_cleanup() {
    bbnote "cleanup debug syms"
    rm -rf ${WORKDIR}/debug_syms
}
addtask preunpack_cleanup after do_fetch before do_unpack

do_install() {
    install -d "${D}${datadir}${COBALT_APP_DIR}"

    err_code=0

    set +e
    unzip -q -o -d "${D}${datadir}${COBALT_APP_DIR}" "${WORKDIR}/${CRX_FILE}" || err_code=$?
    set -e

    case $err_code in
     0) bbnote "All good";;
     1) bbwarn "Ignore unzip warnings";;
     *) bbfatal "Unzip failed, exit code: $err_code"
    esac

    # use system provided certs
    rm -rf ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs
    mkdir -p ${D}${datadir}${COBALT_APP_DIR}/content/ssl/
    ln -s /etc/ssl/certs ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs

    install -d "${D}${datadir}${COBALT_APP_DIR}/lib/.debug/"
    install -m 0755 ${WORKDIR}/debug_syms/tmp/cobalt-evergreen-snapshot/*/symbols/libcobalt.so ${D}${datadir}${COBALT_APP_DIR}/lib/.debug/
}

PACKAGE_PREPROCESS_FUNCS += "dump_debug_symbols"
dump_debug_symbols () {
    machine_dir="${@d.getVar('MACHINE', True)}"
    mkdir -p ${TMPDIR}/deploy/breakpad_symbols/$machine_dir

    binary="${D}${datadir}${COBALT_APP_DIR}/lib/.debug/libcobalt.so"
    bbnote "Dumping symbols from $binary"
    dump_syms "${binary}" > "${TMPDIR}/deploy/breakpad_symbols/$machine_dir/$(basename "$binary").sym" || echo "dump_syms finished with errorlevel $?"
}

FILES_${PN}  = "${datadir}${COBALT_APP_DIR}/content/*"
FILES_${PN} += "${datadir}${COBALT_APP_DIR}/manifest.json"
FILES_${PN} += "${datadir}${COBALT_APP_DIR}/lib/libcobalt.so"
FILES_${PN}-dbg += "${datadir}${COBALT_APP_DIR}/lib/.debug/libcobalt.so"
FILES_SOLIBSDEV = ""

INSANE_SKIP_${PN} += "dev-so libdir"
INSANE_SKIP_${PN}-dbg += "dev-so libdir"

PROVIDES = "virtual/cobalt-evergreen"
RPROVIDES_${PN} = "virtual/cobalt-evergreen"
