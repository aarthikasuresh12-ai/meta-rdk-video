SUMMARY = "Comcast Injected Bundle library for WPE Webkit"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS += "${WPEWEBKIT}"

DEPENDS += "${@bb.utils.contains("DISTRO_FEATURES", "build_rne", "", " ave-jsbindings-jspsdk", d)}"
DEPENDS += "${@bb.utils.contains("DISTRO_FEATURES", "build_rne", "", " ave-aveplayer", d)}"

DEPENDS += " jansson"
DEPENDS += " xupnp"
DEPENDS += " rdkat"
DEPENDS += "aamp"
DEPENDS += " rsync-native"
RDEPENDS_${PN} += "devicesettings"
RDEPENDS_${PN} += "rdkat"
DEPENDS_remove_daisy = " rsync-native "

inherit cmake

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/injectedbundle;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DCMAKE_NO_SYSTEM_FROM_IMPORTED=ON "
EXTRA_OECMAKE_remove = " MAKEFLAGS= -e"
EXTRA_OECMAKE += "-DENABLE_RDK_LOGGER=1"
EXTRA_OECMAKE_rpi += "-DENABLE_AAMP_JSBINDING=1"

#no AVE for any Firebolt builds (pi3 included)
EXTRA_OECMAKE += "${@bb.utils.contains("DISTRO_FEATURES", "build_rne", "", "-DENABLE_AVE=0", d)}"

do_install () {
   install -m 0755 -d ${D}/${libdir}
   rsync -rlv ${B}/*.so* ${D}/${libdir}
   install -m 0755 -d ${D}/usr/share/injectedbundle
   rsync -rlv ${S}/*.js ${D}/usr/share/injectedbundle/
}

INSANE_SKIP_${PN} = "dev-so"

FILES_SOLIBSDEV = ""

FILES_${PN} += "${libdir}/*.so"
FILES_${PN} += "/usr/share/injectedbundle/*.js"
