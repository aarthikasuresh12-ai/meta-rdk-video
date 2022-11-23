SUMMARY = "Evergreen Cobalt Core library."
HOMEPAGE = "https://cobalt.googlesource.com/"

LICENSE = "BSD-3-Clause"
# See https://github.com/youtube/cobalt/blob/master/LICENSE for governing license.
# This license has been stored locally as COBALT_LICENSE
LIC_FILES_CHKSUM = "file://../COBALT_LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := "${THISDIR}/evergreen:"
DEPENDS += "unzip-native breakpad-native"
OVERRIDES_append = ":${TARGET_FPU}"

CRX_FILE_arm_hard = "cobalt_evergreen_3.2.1_arm-hardfp_sbversion-13_release_20221104005818.crx"
DBG_FILE_arm_hard = "libcobalt_3.2.1_unstripped_arm-hardfp_sbversion-13_release_ede2f6ed34a18e55.tar.gz"
CRX_FILE_SHA256SUM_arm_hard = "67da553015e5f90e6ee9279615b342e6be311dcfb4d1cb54128d57d9d924499b"
DBG_FILE_SHA256SUM_arm_hard = "8c7dcca961ce2564ba9f46382cd209feed535a2e170511530d51eaa5a7d06eaa"

CRX_FILE_aarch64 = "cobalt_evergreen_3.2.1_arm64_sbversion-13_release_20221104005818.crx"
DBG_FILE_aarch64 = "libcobalt_3.2.1_unstripped_arm64_sbversion-13_release_278d4e0e0c594fc4.tar.gz"
CRX_FILE_SHA256SUM_aarch64 = "4d4bb8f6adf6bda22d7d3e9388b082f0ec15893e87ca446906877141f4775e30"
DBG_FILE_SHA256SUM_aarch64 = "38b0baac2340288078c94d95738bc130ce6c420b68306d7b7c940088f21344e5"

PV = "3.2.1"
YT_BASE_URI = "https://github.com/youtube/cobalt/releases/download/23.lts.2"
SRC_URI  = "${YT_BASE_URI}/${CRX_FILE};name=cobalt"
SRC_URI += "${YT_BASE_URI}/${DBG_FILE};name=cobalt_debug;subdir=debug_syms"
SRC_URI += "file://COBALT_LICENSE"
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
