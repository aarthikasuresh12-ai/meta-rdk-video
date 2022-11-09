SUMMARY = "WPEFramework client libraries"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f1dffbfd5c2eb52e0302eb6296cc3711"
PR = "r0"

require include/thunder.inc

SRC_URI = "git://github.com/rdkcentral/ThunderClientLibraries.git;protocol=git;branch=R2;name=wpeframework-clientlibraries \
           file://0001-RDK-28534-Security-Agent-Utility-and-Logging-ClientLibs.patch \
           file://0021-move-the-SOC-specific-svp-transformation-to-gst-svp.patch \
           file://0001-link-secapi-crypto.patch \
           file://0026-Send_empty_buffer_on_noBoundKeyMsg.patch \
           file://0002-openssl-api-based-on-openssl-version.patch \
           file://0001-RDK-31882-Add-GstCaps-parsing-in-OCDM-to-wpeframework-clientlibraries.patch \
           file://0001-RDK-29803-SoC-Independent-SVP-support.patch \
           file://0001-IPersistent_API_Cobalt.patch \
           file://0008-added-opencdm_gstreamer_transform_caps-method.patch \
           file://0009-DELIA-51340.patch \
           file://opencdm_system_supports_server_certificate.patch \
           "

# Jul 20, 2022; R2-v1.11 HASH; https://github.com/rdkcentral/ThunderClientLibraries/commit/d3e3d6c2be6516dd6b4ccfc04b129f8725b59baa
SRCREV_wpeframework-clientlibraries = "d3e3d6c2be6516dd6b4ccfc04b129f8725b59baa"


# ----------------------------------------------------------------------------

include include/compositor.inc

DEPENDS = " \
    wpeframework-interfaces \
    wpeframework-tools-native \
    ${@bb.utils.contains('DISTRO_FEATURES', 'compositor', '${WPE_COMPOSITOR_DEP}', '', d)} \
"

RDEPENDS_${PN}_append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'sage_svp', ' gst-svp-ext', '', d)}"
RDEPENDS_${PN}_append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'rdk_svp', ' gst-svp-ext', '', d)}"
RDEPENDS_${PN}_append_dunfell += " wpeframework"

#Cryptography library 
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_icrypto_openssl','openssl', 'virtual/secapi', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_icrypto_openssl','', 'secapi-netflix', d)}"
DEPENDS +=  "${@bb.utils.contains('DISTRO_FEATURES', 'enable_icrypto_openssl',"", bb.utils.contains('DISTRO_FEATURES', 'netflix_cryptanium', 'virtual/secapi-crypto', "", d), d)}"
CRYPTOGRAPHY_IMPLEMENTATION = "${@bb.utils.contains('DISTRO_FEATURES', 'enable_icrypto_openssl','OpenSSL', 'SecApi', d)}"

def get_cdmi_adapter(d):
    if bb.utils.contains("DISTRO_FEATURES", "nexus_svp", "true", "false", d) == "true":
        return "opencdmi_brcm_svp"
    elif bb.utils.contains("DISTRO_FEATURES", "sage_svp", "true", "false", d) == "true":
        return "opencdmi_comcast_svp"
    elif bb.utils.contains("DISTRO_FEATURES", "rdk_svp", "true", "false", d) == "true":
        return "opencdmi_rdk_svp"
    else:
        return "opencdm_gst"
    fi


WPE_CDMI_ADAPTER_IMPL = "${@get_cdmi_adapter(d)}"

PACKAGECONFIG ?= " \
    release \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencdm', 'opencdm ${WPE_CDMI_ADAPTER_IMPL}', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'provisioning', 'provisionproxy', '', d)} \
    securityagent \
    cryptography \
    "

PACKAGECONFIG_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'compositor', 'compositorclient', '', d)}"

PACKAGECONFIG[compositorclient] = "-DCOMPOSITORCLIENT=ON,-DCOMPOSITORCLIENT=OFF"
PACKAGECONFIG[provisionproxy]   = "-DPROVISIONPROXY=ON,-DPROVISIONPROXY=OFF,libprovision"
PACKAGECONFIG[securityagent]    = "-DSECURITYAGENT=ON, -DSECURITYAGENT=OFF"
PACKAGECONFIG[cryptography]     = "-DCRYPTOGRAPHY=ON, -DCRYPTOGRAPHY=OFF,"

# OCDM
PACKAGECONFIG[opencdm]          = "-DCDMI=ON,-DCDMI=OFF,"
PACKAGECONFIG[opencdm_gst]      = '-DCDMI_ADAPTER_IMPLEMENTATION="gstreamer",,gstreamer1.0'
PACKAGECONFIG[opencdmi_prnx_svp]= '-DCDMI_BCM_NEXUS_SVP=ON -DCDMI_ADAPTER_IMPLEMENTATION="broadcom-svp",,'
PACKAGECONFIG[opencdmi_brcm_svp]= '-DCDMI_BCM_NEXUS_SVP=ON -DCDMI_ADAPTER_IMPLEMENTATION="broadcom-svp",,gstreamer-plugins-soc'
PACKAGECONFIG[opencdmi_comcast_svp]= '-DCDMI_BCM_NEXUS_SVP=ON -DCDMI_ADAPTER_IMPLEMENTATION="broadcom-svp-secbuf",,'
PACKAGECONFIG[opencdmi_rdk_svp]= '-DCDMI_ADAPTER_IMPLEMENTATION="rdk",,'

# ----------------------------------------------------------------------------

EXTRA_OECMAKE += " \
    -DBUILD_SHARED_LIBS=ON \
    -DBUILD_REFERENCE=${SRCREV} \
    -DPLUGIN_COMPOSITOR_IMPLEMENTATION=${WPE_COMPOSITOR_IMPL} \
    -DPLUGIN_COMPOSITOR_SUB_IMPLEMENTATION=${WPE_COMPOSITOR_SUB_IMPL} \
    -DPYTHON_EXECUTABLE=${STAGING_BINDIR_NATIVE}/python3-native/python3 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'netflix_cryptanium', '-DSECAPI_ENGINE_CRYPTANIUM=1', '', d)} \
    -DCRYPTOGRAPHY_IMPLEMENTATION=${CRYPTOGRAPHY_IMPLEMENTATION}\
    ${@bb.utils.contains('DISTRO_FEATURES', 'enable_firebolt_compliance_tdk', '-DBUILD_CRYPTOGRAPHY_TESTS=ON', '', d)} \
"

# ----------------------------------------------------------------------------

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so ${datadir}/WPEFramework/* ${PKG_CONFIG_DIR}/*.pc"
FILES_${PN}-dev += "${libdir}/cmake/*"

INSANE_SKIP_${PN} += "dev-so"
INSANE_SKIP_${PN}-dbg += "dev-so"

# ----------------------------------------------------------------------------

RDEPENDS_${PN}_append_rpi = " ${@bb.utils.contains('MACHINE_FEATURES', 'vc4graphics', '', 'userland', d)}"

CXXFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'netflix_cryptanium', " -I${STAGING_INCDIR}/secapi-crypto/sec_api/headers", "", d)}"

# Avoid settings ADNEEDED in LDFLAGS as this can cause the libcompositor.so to drop linking to libEGL/libGLES
# which might not be needed at first glance but will cause problems higher up in the change, there for lets drop -Wl,--as-needed
# some distros, like POKY (morty) enable --as-needed by default (e.g. https://git.yoctoproject.org/cgit/cgit.cgi/poky/tree/meta/conf/distro/include/as-needed.inc?h=morty)
ASNEEDED = ""
