SUMMARY = "WPEFramework interfaces"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f1dffbfd5c2eb52e0302eb6296cc3711"
PR = "r0"

require include/thunder.inc
DEPENDS = "wpeframework wpeframework-tools-native"

SRC_URI = "git://github.com/rdkcentral/ThunderInterfaces.git;protocol=git;branch=R2;name=wpeframework-interfaces \
           file://0018-notifyclient-event-added.patch \
           file://0020-Adding-the-VoiceCommand-API-for-Netflix-plugin.patch \
           file://0001-RDK-31882-Add-GstCaps-parsing-in-OCDM-wpeframework-interfaces.patch \
           file://0001-Add-IFocus-iface.patch \
           "

# Nov 1, 2021
SRCREV_wpeframework-interfaces = "b844fff77f56b75b598768262613ed58a4f24b11"

# ----------------------------------------------------------------------------

PACKAGECONFIG ?= "release"

# ----------------------------------------------------------------------------

EXTRA_OECMAKE += " \
    -DBUILD_SHARED_LIBS=ON \
    -DBUILD_REFERENCE=${SRCREV} \
    -DPYTHON_EXECUTABLE=${STAGING_BINDIR_NATIVE}/python3-native/python3 \
"

# ----------------------------------------------------------------------------

do_install_append() {
    if ${@bb.utils.contains("DISTRO_FEATURES", "opencdm", "true", "false", d)}
    then
        install -m 0644 ${D}${includedir}/WPEFramework/interfaces/IDRM.h ${D}${includedir}/cdmi.h
    fi
}

# ----------------------------------------------------------------------------

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/* ${datadir}/WPEFramework/* ${PKG_CONFIG_DIR}/*.pc"
FILES_${PN}-dev += "${libdir}/cmake/*"
FILES_${PN}-dbg += "${libdir}/wpeframework/proxystubs/.debug/"
FILES_${PN} += "${includedir}/cdmi.h"

INSANE_SKIP_${PN} += "dev-so"
INSANE_SKIP_${PN}-dbg += "dev-so"
