TDK_TARGETDIR="${@bb.utils.contains('DISTRO_FEATURES','ENABLE_IPK','/opt/TDK','${localstatedir}/TDK',d)}"
GSTREAMER_TEST_BINPATH="${TDK_TARGETDIR}/opensourcecomptest/gst-plugin-base/"

def build_gst_test_suites(d):
    build_gst_tests = bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', '1', '0', d)
    return build_gst_tests

do_install_append () {
    if [ "${@build_gst_test_suites(d)}" -eq '1' ]
    then
        install -d ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements* ${D}/${GSTREAMER_TEST_BINPATH}/
    fi
}

PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', ' ${PN}-test ', '', d)}"
FILES_${PN}-test += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', '${GSTREAMER_TEST_BINPATH}', '', d)}"