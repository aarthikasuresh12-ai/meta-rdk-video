#components used in TDK for validating Gstreamer opensource plugins

RDEPENDS_packagegroup-tdk += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', '\
  gstreamer1.0-plugins-base-test \
  gstreamer1.0-plugins-bad-test \
  gstreamer1.0-plugins-good-test \
  gstreamer1.0-plugins-base-dbg \
  gstreamer1.0-plugins-bad-dbg \
  gstreamer1.0-plugins-good-dbg \
  ', '', d)}"
