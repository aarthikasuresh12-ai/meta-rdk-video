SUMMARY = "TVSettings"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "${CMF_RDK_COMPONENTS_ROOT_GIT}/opensource/tvsettings;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};"
SRCREV = "${AUTOREV}"

PV = "git${SRCPV}"

S = "${WORKDIR}/git"
SAVEDDIR := "${THISDIR}"

#To remove --as-needed compile option which is causing issue with linking
ASNEEDED = ""

inherit cmake systemd

DEPENDS = "glib-2.0 iarmbus tvsettings-hal-headers virtual/tvsettings-hal hdmicec wpeframework wpeframework-clientlibraries tvsettings-plugins"
RDEPENDS_${PN} += " devicesettings"

do_install() {
   install -d ${D}${includedir}/rdk/tv
   install -m 0644 ${S}/rdk-tv/include/*.hpp ${D}${includedir}/rdk/tv

   install -d ${D}${libdir}
   install -m 0755 ${B}/rdk-tv/*.so ${D}${libdir}/
}

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg"
CXXFLAGS_append = " -I${STAGING_INCDIR}/WPEFramework"

FILES_${PN} += "${libdir}/lib*.so"
FILES_${PN}-dbg +="${libdir}/.debug/*"
FILES_${PN} += "${bindir}/*"

INSANE_SKIP_${PN} = "dev-so"
