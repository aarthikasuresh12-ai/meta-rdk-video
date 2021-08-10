include westeros.inc

SUMMARY = "This receipe compiles the westeros compositor gstreamer sink element"

LICENSE = "LGPLv2.1"

S = "${WORKDIR}/git/westeros-sink"

LICENSE_LOCATION = "${S}/../LICENSE"

inherit autotools pkgconfig

DEPENDS = "wayland westeros essos virtual/westeros-soc"

#For sky
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'build_for_sky', " -DSKY_BUILD", "", d)}"
DEPENDS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'westeros_sink_software_decode', ' libav', '', d)}"
CFLAGS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'westeros_sink_software_decode', ' -DENABLE_SW_DECODE', '', d)}"
LDFLAGS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'westeros_sink_software_decode', ' -lavcodec -lavutil', '', d)}"
SRC_URI += "file://0001-westeros-sink-1080p.patch;patchdir=../../"
