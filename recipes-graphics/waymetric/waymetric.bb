
SUMMARY = "This receipe compiles the waymetric component"

LICENSE = "Apache-2.0"
LICENSE_LOCATION ?= "${S}/LICENSE"
LIC_FILES_CHKSUM = "file://${LICENSE_LOCATION};md5=09b658f7398abbac507b10feada73aac"

PV = "1.0+gitr${SRCPV}"
SRCREV = "${AUTOREV}"

CMF_GIT_ROOT ?= "git://code.rdkcentral.com/r"
CMF_GIT_PROTOCOL ?= "https"
CMF_GIT_MASTER_BRANCH ?= "master"

SRC_URI = "${CMF_GIT_ROOT}/components/opensource/waymetric;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_MASTER_BRANCH};name=waymetric"
SRCREV_FORMAT = "waymetric"

S = "${WORKDIR}/git"

DEPENDS = "wayland virtual/egl"

INSANE_SKIP_${PN} = "ldflags"

inherit autotools pkgconfig

acpaths = "-I cfg"
