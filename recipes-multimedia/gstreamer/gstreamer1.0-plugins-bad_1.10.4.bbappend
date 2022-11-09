EXTRA_OECONF_remove = "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', ' --disable-jpegformat --disable-rawparse ', '', d)}"

TDK_TARGETDIR="${@bb.utils.contains('DISTRO_FEATURES','ENABLE_IPK','/opt/TDK','${localstatedir}/TDK',d)}"
GSTREAMER_TEST_BINPATH="${TDK_TARGETDIR}/opensourcecomptest/gst-plugin-bad/"

RDEPENDS_${PN}-test = "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', ' bash ', '', d)}"

def build_gst_test_suites(d):
    build_gst_tests = bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', '1', '0', d)
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
        install -D -p -m 755 ${B}/tests/check/elements/aiffparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/asfmux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiointerleave ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiomixer ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/autoconvert ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/autovideoconvert ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/camerabin ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/compositor ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/curlfilesink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/curlftpsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/curlhttpsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/curlsmtpsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/dash_demux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/dash_isoff ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/dash_mpd ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/dataurisrc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/gdpdepay ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/gdppay ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/h263parse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/h264parse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/hls_demux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/hlsdemux_m3u8 ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/id3mux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/jpegparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mpeg4videoparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mpegtsmux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mpegvideoparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mssdemux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mxfdemux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mxfmux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/neonhttpsrc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/netsim ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/pcapparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/pnm ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rawaudioparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rawvideoparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtponvifparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtponviftimestamp ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/shm ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/videoframe-audiolevel ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/viewfinderbin ${D}/${GSTREAMER_TEST_BINPATH}/
    fi
}

PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', ' ${PN}-test ', '', d)}"
FILES_${PN}-test += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', '${GSTREAMER_TEST_BINPATH}', '', d)}"
