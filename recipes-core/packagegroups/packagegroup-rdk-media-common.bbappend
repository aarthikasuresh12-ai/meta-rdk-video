
RDEPENDS_packagegroup-rdk-media-common += "\
    ${MFR} \
    ${@bb.utils.contains("DISTRO_FEATURES", "logbacktrace", "crashlog", "" , d)} \
    "

MFR_qemuall = ""
MFR ?= "${@bb.utils.contains('DISTRO_FEATURES', 'nopod', ' ', 'mfr-data', d)}"

RDEPENDS_packagegroup-rdk-media-common += "\
   essos-examples \
   ${@bb.utils.contains("DISTRO_FEATURES", "rdkshell", "rdkshell", "", d)} \
   ${@bb.utils.contains("DISTRO_FEATURES", "offline_apps", "lxapp-bt-audio  residentui", "", d)} \
   ${@bb.utils.contains("DISTRO_FEATURES", "build_rne", "rdkresidentapp wpeframework-ui", "", d)} \
   ${@bb.utils.contains('DISTRO_FEATURES', 'fwupgrader', 'rdkfwupgrader', '', d)} \
   "
