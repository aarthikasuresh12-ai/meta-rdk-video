#components used in TDK for validating Gstreamer opensource plugins

RDEPENDS_packagegroup-tdk += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', '\
  gstreamer1.0-plugins-base-videotestsrc \
  gstreamer1.0-plugins-base-audiotestsrc \
  gstreamer1.0-plugins-base-test \
  gstreamer1.0-plugins-bad-test \
  gstreamer1.0-plugins-good-test \
  gstreamer1.0-plugins-good-videobox \
  gstreamer1.0-plugins-good-vpx \
  gstreamer1.0-plugins-good-jpeg \
  gstreamer1.0-plugins-good-rtp \
  gstreamer1.0-plugins-good-wavpack \
  gstreamer1.0-plugins-good-flv \
  gstreamer1.0-plugins-good-wavparse \
  gstreamer1.0-plugins-bad-jpegformat \
  ', '', d)}"
