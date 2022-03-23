include westeros.inc

SUMMARY = "Essos is a library making it simple to create applications that run either as native EGL or Wayland clients."
LICENSE = "Apache-2.0"
LICENSE_LOCATION = "${S}/../LICENSE"

S = "${WORKDIR}/git/essos"

DEPENDS = "wayland virtual/egl libxkbcommon"

REQUIRED_DISTRO_FEATURES += "wayland"

inherit autotools pkgconfig distro_features_check

PACKAGECONFIG ??= "westeros resmgr"
PACKAGECONFIG[westeros] = "--disable-essoswesterosfree,--enable-essoswesterosfree,westeros-simpleshell virtual/westeros-soc"
PACKAGECONFIG[resmgr] = "--disable-essosresmgrfree,--enable-essosresmgrfree"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples += "${bindir}/*"
