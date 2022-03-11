#components used in TDK for validating Gstreamer opensource plugins

RDEPENDS_packagegroup-tdk += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', '\
  gstreamer1.0-plugins-base-videotestsrc \
  gstreamer1.0-plugins-base-audiotestsrc \
  gstreamer1.0-plugins-base-test \
  aamp-bin \
  ', '', d)}"
