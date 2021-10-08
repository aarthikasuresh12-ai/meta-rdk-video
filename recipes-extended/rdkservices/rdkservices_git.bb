SUMMARY = "RDK services plugins"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=16cf2209d4e903e4d5dcd75089d7dfe2"

PR = "r1"
PV = "3.0+git${SRCPV}"

S = "${WORKDIR}/git"
inherit cmake pkgconfig

SRC_URI = "git://github.com/rdkcentral/rdkservices.git;protocol=git;branch=main \
           file://0001-Fix-DisplayInfo-build-with-SDK-14.4.patch \
           file://0002-RDK-28534-Comcast-Security-Agent-changes.patch \
           file://index.html \
           file://osmc-devinput-remote.json \
           file://thunder_acl.json \
           file://rdkshell_post_startup.conf \
           file://0003-set-OCDM-sharepath-to-tmp-OCDM.patch \
           ${@bb.utils.contains('DISTRO_FEATURES', 'systimemgr','file://0001-Added-Support-for-querying-and-publishing-the-Secure.patch','',d)} \
           file://0001-RDK-31882-Add-GstCaps-parsing-in-OCDM-to-rdkservices.patch \
           file://0001-add_gstcaps_forcobalt_mediatype.patch \
           file://0001-Add-monitoring-of-cloned-callsigns.patch \
           file://rdkservices.ini \
           file://0001-support-for-focus-events.patch \
          "

# Oct 08, 2021
SRCREV = "92b0ed86dda3d2ce845395f5ab61d1ef09bb3a1e"

TOOLCHAIN = "gcc"
EXTRA_OECMAKE += "-DCMAKE_SYSROOT=${STAGING_DIR_HOST}"

DEPENDS += "wpeframework-clientlibraries devicesettings iarmbus iarmmgrs tts storagemanager nopoll audiocapturemgr hdmicec ctrlm-headers"
DEPENDS +=  " trower-base64 rfc telemetry "
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetooth-mgr', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell', ' rdkshell ', '', d)}"
DEPENDS += " breakpad-wrapper"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'systimemgr',     'systimemgrinetrface', '', d)}"

TARGET_LDFLAGS += " -Wl,--no-as-needed -ltelemetry_msgsender -Wl,--as-needed "
EXTRA_OECMAKE += "-DBUILD_ENABLE_TELEMETRY_LOGGING=ON"

RDEPENDS_${PN} += "devicesettings"

CXXFLAGS += " -I${STAGING_DIR_TARGET}${includedir}/wdmp-c/ "
CXXFLAGS += " -I${STAGING_DIR_TARGET}${includedir}/trower-base64/ "
CXXFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'build_rfc_enabled',"-DRFC_ENABLED ","", d)}"

# More complicated plugins are moved seperate includes
include include/compositor.inc
include include/ocdm.inc
include include/power.inc
include include/remotecontrol.inc
include include/snapshot.inc
include include/webkitbrowser.inc
include include/packager.inc
include include/texttospeech.inc

WPEFRAMEWORK_LOCATIONSYNC_URI ?= "http://jsonip.metrological.com/?maf=true"

PACKAGECONFIG ?= " \
    ${WPE_SNAPSHOT} \
    activitymonitor avinput continuewatching controlservice datacapture devicediagnostics \
    displaysettings framerate hdcpprofile hdmicec hdmiinput loggingpreferences \
    messenger network remoteactionmapping screencapture securityagent stateobserver \
    systemservices timer tracecontrol userpreferences warehouse monitor locationsync texttospeech persistent_store\
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetoothcontrol', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencdm',              'opencdmi', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'playready_nexus_svp',  'opencdmi_prnx_svp', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'widevine_nexus_svp',   'opencdmi_wv_svp', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'clearkey',             'opencdmi_ck', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell',             'rdkshell', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'DOBBY_CONTAINERS',     'ocicontainer', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wifimanager', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'enable_maintenance_manager', 'maintenancemanager', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'fireboltmediaplayer', 'fireboltmediaplayer', '', d)} \
"
EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell', ' -DPLUGIN_RDKSHELL=ON ', ' -DPLUGIN_RDKSHELL=OFF ', d)}"
EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell_disable_autostart', ' -DPLUGIN_RDKSHELL_AUTOSTART=false ', ' -DPLUGIN_RDKSHELL_AUTOSTART=true ', d)}"
EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell_ra', ' -DPLUGIN_RDKSHELL_AUTOSTART=true ', ' ', d)}"

PACKAGECONFIG[activitymonitor]      = "-DPLUGIN_ACTIVITYMONITOR=ON,-DPLUGIN_ACTIVITYMONITOR=OFF,"
PACKAGECONFIG[avinput]              = "-DPLUGIN_AVINPUT=ON,-DPLUGIN_AVINPUT=OFF,"
PACKAGECONFIG[bluetoothcontrol]     = "-DPLUGIN_BLUETOOTH=ON -DPLUGIN_BLUETOOTH_AUTOSTART=true,-DPLUGIN_BLUETOOTH=OFF,,bluez5"
PACKAGECONFIG[continuewatching]     = "-DPLUGIN_CONTINUEWATCHING=ON,-DPLUGIN_CONTINUEWATCHING=OFF,"
PACKAGECONFIG[controlservice]       = "-DPLUGIN_CONTROLSERVICE=ON,-DPLUGIN_CONTROLSERVICE=OFF,"
PACKAGECONFIG[datacapture]          = "-DPLUGIN_DATACAPTURE=ON, -DPLUGIN_DATACAPTURE=OFF,"
PACKAGECONFIG[devicediagnostics]    = "-DPLUGIN_DEVICEDIAGNOSTICS=ON,-DPLUGIN_DEVICEDIAGNOSTICS=OFF,"
PACKAGECONFIG[deviceinfo]           = "-DPLUGIN_DEVICEINFO=ON,-DPLUGIN_DEVICEINFO=OFF,"
PACKAGECONFIG[deviceidentification] = "-DPLUGIN_DEVICEIDENTIFICATION=ON,-DPLUGIN_DEVICEIDENTIFICATION=OFF,"
PACKAGECONFIG[dictionary]           = "-DPLUGIN_DICTIONARY=ON,-DPLUGIN_DICTIONARY=OFF,"
PACKAGECONFIG[displayinfo]          = "-DPLUGIN_DISPLAYINFO=ON  -DUSE_DEVICESETTINGS=1,-DPLUGIN_DISPLAYINFO=OFF,"
PACKAGECONFIG[displaysettings]      = "-DPLUGIN_DISPLAYSETTINGS=ON,-DPLUGIN_DISPLAYSETTINGS=OFF,"
PACKAGECONFIG[framerate]            = "-DPLUGIN_FRAMERATE=ON,-DPLUGIN_FRAMERATE=OFF,"
PACKAGECONFIG[hdcpprofile]          = "-DPLUGIN_HDCPPROFILE=ON,-DPLUGIN_HDCPPROFILE=OFF,"
PACKAGECONFIG[hdmicec]              = "-DPLUGIN_HDMICEC=ON,-DPLUGIN_HDMICEC=OFF,"
PACKAGECONFIG[hdmicec2]             = "-DPLUGIN_HDMICEC2=ON,-DPLUGIN_HDMICEC2=OFF,"
PACKAGECONFIG[hdmiinput]            = "-DPLUGIN_HDMIINPUT=ON,-DPLUGIN_HDMIINPUT=OFF,"
PACKAGECONFIG[compositeinput]       = "-DPLUGIN_COMPOSITEINPUT=ON,-DPLUGIN_COMPOSITEINPUT=OFF,"
PACKAGECONFIG[ioconnector]          = "-DPLUGIN_IOCONNECTOR=ON,-DPLUGIN_IOCONNECTOR=OFF,"
PACKAGECONFIG[jsonrpcplugin]        = "-DPLUGIN_JSONRPC=ON,-DPLUGIN_JSONRPC=OFF,"
PACKAGECONFIG[locationsync]         = "-DPLUGIN_LOCATIONSYNC=ON \
                                       -DPLUGIN_LOCATIONSYNC_URI=${WPEFRAMEWORK_LOCATIONSYNC_URI} \
                                      ,-DPLUGIN_LOCATIONSYNC=OFF,"
PACKAGECONFIG[loggingpreferences]   = "-DPLUGIN_LOGGINGPREFERENCES=ON,-DPLUGIN_LOGGINGPREFERENCES=OFF,"
PACKAGECONFIG[messenger]            = "-DPLUGIN_MESSENGER=ON,-DPLUGIN_MESSENGER=OFF,"
PACKAGECONFIG[monitor]              = "-DPLUGIN_MONITOR=ON ${MONITOR_PLUGIN_ARGS},-DPLUGIN_MONITOR=OFF,"
PACKAGECONFIG[network]              = "-DPLUGIN_NETWORK=ON,-DPLUGIN_NETWORK=OFF,"
PACKAGECONFIG[persistent_store]     = "-DPLUGIN_PERSISTENTSTORE=ON,,sqlite3 glib-2.0"
PACKAGECONFIG[playerinfo]           = "-DPLUGIN_PLAYERINFO=ON -DUSE_DEVICESETTINGS=1,-DPLUGIN_PLAYERINFO=OFF,"
PACKAGECONFIG[rdkshell]             = "-DPLUGIN_RDKSHELL=ON,-DPLUGIN_RDKSHELL=OFF,rdkshell,"
PACKAGECONFIG[remoteactionmapping]  = "-DPLUGIN_REMOTEACTIONMAPPING=ON,-DPLUGIN_REMOTEACTIONMAPPING=OFF,"
PACKAGECONFIG[screencapture]        = "-DPLUGIN_SCREENCAPTURE=ON,-DPLUGIN_SCREENCAPTURE=OFF,"
PACKAGECONFIG[securityagent]        = "-DPLUGIN_SECURITYAGENT=ON,-DPLUGIN_SECURITYAGENT=OFF,"
PACKAGECONFIG[stateobserver]        = "-DPLUGIN_STATEOBSERVER=ON,-DPLUGIN_STATEOBSERVER=OFF,"
PACKAGECONFIG[systemdconnector]     = "-DPLUGIN_SYSTEMDCONNECTOR=ON,-DPLUGIN_SYSTEMDCONNECTOR=OFF,"
PACKAGECONFIG[maintenancemanager]   = "-DPLUGIN_MAINTENANCEMANAGER=ON,-DPLUGIN_MAINTENANCEMANAGER=OFF,"
PACKAGECONFIG[systemservices]       = "-DPLUGIN_SYSTEMSERVICES=ON,-DPLUGIN_SYSTEMSERVICES=OFF,"
PACKAGECONFIG[timer]                = "-DPLUGIN_TIMER=ON,-DPLUGIN_TIMER=OFF,"
PACKAGECONFIG[timesync]             = "-DPLUGIN_TIMESYNC=ON,-DPLUGIN_TIMESYNC=OFF,"
PACKAGECONFIG[tracecontrol]         = "-DPLUGIN_TRACECONTROL=ON,-DPLUGIN_TRACECONTROL=OFF,"
PACKAGECONFIG[userpreferences]      = "-DPLUGIN_USERPREFERENCES=ON,-DPLUGIN_USERPREFERENCES=OFF,"
PACKAGECONFIG[virtualinput]         = "-DPLUGIN_COMPOSITOR_VIRTUALINPUT=ON,-DPLUGIN_COMPOSITOR_VIRTUALINPUT=OFF,"
PACKAGECONFIG[warehouse]            = "-DPLUGIN_WAREHOUSE=ON,-DPLUGIN_WAREHOUSE=OFF,"
PACKAGECONFIG[webproxy]             = "-DPLUGIN_WEBPROXY=ON,-DPLUGIN_WEBPROXY=OFF,"
PACKAGECONFIG[webserver]            = "-DPLUGIN_WEBSERVER=ON \
                                       -DPLUGIN_WEBSERVER_PORT="${PLUGIN_WEBSERVER_PORT}" \
                                       -DPLUGIN_WEBSERVER_PATH="${PLUGIN_WEBSERVER_PATH}" \
                                      ,-DPLUGIN_WEBSERVER=OFF,"
PACKAGECONFIG[webshell]             = "-DPLUGIN_WEBSHELL=ON,-DPLUGIN_WEBSHELL=OFF,"
PACKAGECONFIG[wifi]                 = "-DPLUGIN_WIFICONTROL=ON,-DPLUGIN_WIFICONTROL=OFF,,wpa-supplicant"
PACKAGECONFIG[wifi_rdkhal]          = "-DPLUGIN_USE_RDK_HAL_WIFI=ON,-DPLUGIN_USE_RDK_HAL_WIFI=OFF,,wifi-hal"
PACKAGECONFIG[wifimanager]          = "-DPLUGIN_WIFIMANAGER=ON,-DPLUGIN_WIFIMANAGER=OFF,netsrvmgr,"
PACKAGECONFIG[xcast]                = "-DPLUGIN_XCAST=ON,-DPLUGIN_XCAST=OFF,"
PACKAGECONFIG[ocicontainer]         = "-DPLUGIN_OCICONTAINER=ON, -DPLUGIN_OCICONTAINER=OFF, dobby, dobby"
PACKAGECONFIG[usbaccess]            = "-DPLUGIN_USBACCESS=ON,-DPLUGIN_USBACCESS=OFF,"
PACKAGECONFIG[erm]                  = "-DBUILD_ENABLE_ERM=ON,-DBUILD_ENABLE_ERM=OFF,"
PACKAGECONFIG[fireboltmediaplayer]  = "-DPLUGIN_FIREBOLTMEDIAPLAYER=ON,-DPLUGIN_FIREBOLTMEDIAPLAYER=OFF, aamp, aamp"

MONITOR_PLUGIN_ARGS                ?= " \
                                       -DPLUGIN_WEBKITBROWSER_MEMORYLIMIT=614400 \
                                       -DPLUGIN_YOUTUBE_MEMORYLIMIT=614400 \
                                       -DPLUGIN_NETFLIX_MEMORYLIMIT=307200 \
                                       -DPLUGIN_MONITOR_CLONED_APPS=ON -DPLUGIN_MONITOR_CLONED_APP_MEMORYLIMIT=657408 \
                                       -DPLUGIN_MONITOR_SEARCH_AND_DISCOVERY_MEMORYLIMIT=888832 \
                                       -DPLUGIN_MONITOR_NETFLIX_APP_MEMORYLIMIT=1048576 \
"

EXTRA_OECMAKE += " \
    -DBUILD_REFERENCE=${SRCREV} \
    -DBUILD_SHARED_LIBS=ON \
    -DSECAPI_LIB=sec_api \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systimemgr',     '-DBUILD_ENABLE_SYSTIMEMGR_SUPPORT=ON', '', d)} \
"

do_install_append() {
    install -m 0644 ${WORKDIR}/thunder_acl.json ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/rdkshell_post_startup.conf ${D}${sysconfdir}
    if ${@bb.utils.contains("DISTRO_FEATURES", "rdkshell_ra", "true", "false", d)}
    then
      install -d ${D}${sysconfdir}/rfcdefaults
      install -m 0644 ${WORKDIR}/rdkservices.ini ${D}${sysconfdir}/rfcdefaults/
    fi
}

# ----------------------------------------------------------------------------

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/wpeframework/plugins/*.so ${libdir}/*.so ${datadir}/WPEFramework/* ${sysconfdir}/thunder_acl.json"

INSANE_SKIP_${PN} += "libdir staticdev dev-so"
INSANE_SKIP_${PN}-dbg += "libdir"
