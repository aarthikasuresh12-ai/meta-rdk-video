
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', ' bzip2 wavpack libvpx ', '', d)}"
PACKAGECONFIG_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', ' wavpack vpx ', '', d)}"

TDK_TARGETDIR="${@bb.utils.contains('DISTRO_FEATURES','ENABLE_IPK','/opt/TDK','${localstatedir}/TDK',d)}"
GSTREAMER_TEST_BINPATH="${TDK_TARGETDIR}/opensourcecomptest/gst-plugin-good/"
GSTREAMER_TEST_FILESPATH="${GSTREAMER_TEST_BINPATH}/inputfiles/"

def build_gst_test_suites(d):
    build_gst_tests = bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', '1', '0', d)
    return build_gst_tests

do_install_append () {
    if [ "${@build_gst_test_suites(d)}" -eq '1' ]
    then
        install -d ${D}/${GSTREAMER_TEST_BINPATH}/
        install -d ${D}/${GSTREAMER_TEST_FILESPATH}/
        install -D -p -m 755 ${B}/tests/check/elements* ${D}/${GSTREAMER_TEST_BINPATH}/
        #Installing input files required for the test suites
	install -D -p -m 755 ${S}/tests/files/audiotestsrc.flac ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/audiotestsrc.wav ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/cbr_stream.mp3 ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/gradient.j2k ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/h264.rtp ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/h265.rtp ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-407349-1.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-407349-2.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-447000-wcop.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-577468-unsynced-tag.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-588148-unsynced-v24.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/image.jpg ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/pcm16sine.flv ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/pinknoise-vorbis.mkv ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/splitvideo00.ogg ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/splitvideo01.ogg ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/splitvideo02.ogg ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/stream.mp2 ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/test-cert.pem ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/test-key.pem ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/vbr_stream.mp3 ${D}/${GSTREAMER_TEST_FILESPATH}/
    fi
}

PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', ' ${PN}-test ', '', d)}"
FILES_${PN}-test += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_gst_testing', '${GSTREAMER_TEST_BINPATH}', '', d)}"

