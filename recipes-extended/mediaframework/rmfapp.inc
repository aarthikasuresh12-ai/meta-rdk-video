RMFAPP_MEDIACLIENT_DEPENDS="rdk-logger rmfgeneric"
RMFAPP_MEDIACLIENT_DEPENDS+="${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0', 'gstreamer', d)}"

RMFAPP_HYBRID_DEPENDS="virtual/sectionfilter-hal "
