TDK_TARGETDIR="${@bb.utils.contains('DISTRO_FEATURES','ENABLE_IPK','/opt/TDK','${localstatedir}/TDK',d)}"
GSTREAMER_TEST_BINPATH="${TDK_TARGETDIR}/opensourcecomptest/gst-plugin-base/"
PACKAGECONFIG_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', ' opus ', '', d)}"
def build_gst_test_suites(d):
    build_gst_tests = bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', '1', '0', d)
    return build_gst_tests
do_compile_append() {
    if [ "${@build_gst_test_suites(d)}" -eq '1' ]
    then
        cd ${B}/tests/check
        make build-checks
    fi
}
do_install_append () {
    if [ "${@build_gst_test_suites(d)}" -eq '1' ]
    then
        install -d ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/adder ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/appsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/appsrc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/audioconvert ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/audiointerleave ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/audiomixer ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/audiorate ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/audioresample ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/audiotestsrc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/compositor ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/decodebin ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/encodebin ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/multifdsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/multisocketsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/opus ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/overlaycomposition ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/playbin ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/playbin-complex ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/playsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/rawaudioparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/rawvideoparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/streamsynchronizer ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/subparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/textoverlay ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/urisourcebin ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/videoconvert ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/videorate ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/videoscale* ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/videotestsrc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/.libs/volume ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/vorbisdec ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/vorbistag ${D}/${GSTREAMER_TEST_BINPATH}/
    fi
}
PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', ' ${PN}-test ', '', d)}"
FILES_${PN}-test += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', '${GSTREAMER_TEST_BINPATH}', '', d)}"

