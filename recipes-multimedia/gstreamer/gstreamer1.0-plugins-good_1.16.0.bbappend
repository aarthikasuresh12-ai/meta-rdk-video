
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', ' bzip2 wavpack libvpx ', '', d)}"
PACKAGECONFIG_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', ' wavpack vpx ', '', d)}"

TDK_TARGETDIR="${@bb.utils.contains('DISTRO_FEATURES','ENABLE_IPK','/opt/TDK','${localstatedir}/TDK',d)}"
GSTREAMER_TEST_BINPATH="${TDK_TARGETDIR}/opensourcecomptest/gst-plugin-good/"
GSTREAMER_TEST_FILESPATH="${GSTREAMER_TEST_BINPATH}/inputfiles/"

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
        install -d ${D}/${GSTREAMER_TEST_FILESPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/aacparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/ac3parse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/alpha ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/alphacolor ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/amrparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/apev2mux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/aspectratiocrop ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audioamplify ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiochebband ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiocheblimit ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiodynamic ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audioecho ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiofirfilter ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audioiirfilter ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audioinvert ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiopanorama ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiowsincband ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/audiowsinclimit ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/autodetect ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/avimux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/avisubtitle ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/capssetter ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/deinterlace ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/deinterleave ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/dtmf ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/equalizer ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/flacparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/flvdemux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/flvmux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/gdkpixbufoverlay ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/gdkpixbufsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/icydemux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/id3demux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/id3v2mux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/imagefreeze ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/interleave ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/jpegdec ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/jpegenc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/level ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/matroskademux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/matroskamux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/matroskaparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mpegaudioparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mpg123audiodec ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mulawdec ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/mulawenc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/multifile ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/qtdemux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/qtmux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rganalysis ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rglimiter ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rgvolume ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpbin ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpbin_buffer_list ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpcollision ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpfunnel ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtph261 ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtph263 ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtph264 ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtph265 ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpjitterbuffer ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpmux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtp-payloading ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpred ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtprtx ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpsession ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpssrcdemux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpstorage ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpulpfec ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/rtpvp9 ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/shapewipe ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/souphttpsrc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/spectrum ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/splitmux ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/udpsink ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/udpsrc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/videobox ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/videocrop ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/videofilter ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/videomixer ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/vp8dec ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/vp8enc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/vp9enc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/wavpackdec ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/wavpackenc ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/wavpackparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/wavparse ${D}/${GSTREAMER_TEST_BINPATH}/
        install -D -p -m 755 ${B}/tests/check/elements/y4menc ${D}/${GSTREAMER_TEST_BINPATH}/
        #Installing input files required for the test suites
	install -D -p -m 755 ${S}/tests/files/audiotestsrc.wav ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-407349-1.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-407349-2.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-447000-wcop.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-577468-unsynced-tag.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/id3-588148-unsynced-v24.tag ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/image.jpg ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/pcm16sine.flv ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/pinknoise-vorbis.mkv ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/test-cert.pem ${D}/${GSTREAMER_TEST_FILESPATH}/
	install -D -p -m 755 ${S}/tests/files/test-key.pem ${D}/${GSTREAMER_TEST_FILESPATH}/
    fi
}
PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', ' ${PN}-test ', '', d)}"
FILES_${PN}-test += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', '${GSTREAMER_TEST_BINPATH}', '', d)}"
