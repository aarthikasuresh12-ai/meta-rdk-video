SUMMARY = "Network Manager based on network services like wifi, moca and bluetooth in the home network."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/netsrvmgr;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=netsrvmgr"
SRCREV_netsrvmgr = "${AUTOREV}"

S = "${WORKDIR}/git"

CXXFLAGS +=" -DYOCTO_BUILD"
DEPENDS += "safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

inherit pkgconfig syslog-ng-config-gen
SYSLOG-NG_FILTER = "netsrvmgr"
SYSLOG-NG_SERVICE_netsrvmgr = "netsrvmgr.service"
SYSLOG-NG_DESTINATION_netsrvmgr = "netsrvmgr.log"
SYSLOG-NG_LOGRATE_netsrvmgr = "high"

CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CXXFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -DSAFEC_RDKV', '', d)}"

CXXFLAGS += "-I${STAGING_INCDIR}/rdk/iarmbus -DENABLE_SD_NOTIFY -I${STAGING_INCDIR}/rdk/iarmmgrs-hal"
CFLAGS += "-I${STAGING_INCDIR}/rdk/iarmbus -DENABLE_SD_NOTIFY -I${STAGING_INCDIR}/rdk/iarmmgrs-hal"
LDFLAGS += "-lsystemd -lsqlite3 -lsecure_wrapper"

DEPENDS = "glib-2.0 iarmbus iarmmgrs dbus tr69hostif-headers cjson authservice nlmonitor libnl sqlite3 libsyswrapper rdk-logger breakpad"
DEPENDS_append = " ${@bb.utils.contains('MACHINE_EXTRA_RDEPENDS', 'virtual/mocadriver', 'moca-hal-tools', '', d)}"

# Add support for rfcapi
DEPENDS += "rfc"
CFLAGS += "-I${STAGING_INCDIR}/wdmp-c"
CXXFLAGS += "-I${STAGING_INCDIR}/wdmp-c"
LDFLAGS += "-lrfcapi"

PACKAGECONFIG += " ${@bb.utils.contains('MACHINE_EXTRA_RDEPENDS', 'virtual/mocadriver', 'moca', '', d)}"
PACKAGECONFIG += "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wifi', '', d)}"
PACKAGECONFIG += "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'lostfound', '', d)}"
PACKAGECONFIG[moca] = "--enable-rdk-moca-hal=yes,--enable-rdk-moca-hal=no,,"
PACKAGECONFIG[wifi] = "--enable-rdk-wifi-hal=yes,--enable-rdk-wifi-hal=no,virtual/wifi-hal,virtual/wifi-hal"
PACKAGECONFIG[lostfound] = "--enable-lost-found=yes,--enable-lost-found=no,lostandfound,lostandfound"
EXTRA_OECONF_append_client = " --enable-nlmonitor --enable-iarm --enable-route-support"
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'WIFI_CLIENT_ROAMING', ' --enable-client-roaming', '', d)}"
BREAKPAD_BIN_append = " netsrvmgr"
inherit autotools systemd pkgconfig coverity

export RDK_FSROOT_PATH = "${STAGING_DIR_TARGET}"

export PNI_CONFIG_DISABLE_PNI ?= "false"
export PNI_CONFIG_DISABLE_CONNECTIVITY_TEST ?= "false"
export PNI_CONFIG_ALLOW_PNI_TO_DISABLE_WIFI ?= "true"

CPPFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', bb.utils.contains('PNI_CONFIG_DISABLE_PNI', 'true', '-DDISABLE_PNI', '', d), '', d)}"

do_install_append() {
        install -d ${D}${bindir}
        install -d ${D}${sysconfdir}
        install -d ${D}${systemd_unitdir}/system

        install -m 0644 ${S}/conf/netsrvmgr.conf ${D}${sysconfdir}
        install -m 0644 ${S}/conf/netsrvmgr_Telemetry_LoggingParams.json ${D}${sysconfdir}
        
        install -m 0644 ${S}/netsrvmgr.service ${D}${systemd_unitdir}/system

        if ${@bb.utils.contains('DISTRO_FEATURES', 'USE_WIFI_HAL_GENERIC', 'true', 'false', d)}; then
            sed -i "/bssid=/d" ${D}${systemd_unitdir}/system/netsrvmgr.service 
            sed -i "/key_mgmt=OPEN/d" ${D}${systemd_unitdir}/system/netsrvmgr.service
        fi

        install -d ${D}${includedir}
        install -m 0644 ${S}/src/services/wifi/include/wifiSrvMgrIarmIf.h ${D}${includedir}

        if ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'true', 'false', d)}; then
            install -m 0644 ${S}/pni_controller.service ${D}${systemd_unitdir}/system
            install -m 0644 ${S}/pni_settings_loader.service ${D}${systemd_unitdir}/system

            sed -i "s/\(CONFIG_DISABLE_PNI=\).*/\1$PNI_CONFIG_DISABLE_PNI/" ${D}${systemd_unitdir}/system/pni_controller.service
            sed -i "s/\(CONFIG_DISABLE_CONNECTIVITY_TEST=\).*/\1$PNI_CONFIG_DISABLE_CONNECTIVITY_TEST/" ${D}${systemd_unitdir}/system/pni_controller.service
            sed -i "s/\(CONFIG_ALLOW_PNI_TO_DISABLE_WIFI=\).*/\1$PNI_CONFIG_ALLOW_PNI_TO_DISABLE_WIFI/" ${D}${systemd_unitdir}/system/pni_controller.service

            install -d ${D}${base_libdir}/rdk
            install -m 0755 ${S}/pni_controller.sh ${D}${base_libdir}/rdk/
            install -m 0755 ${S}/wifi_addr_reset.sh ${D}${base_libdir}/rdk/
        fi
}

PACKAGE_BEFORE_PN += "${PN}-pni-controller ${PN}-pni-settings-loader"

RDEPENDS_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '${PN}-pni-controller ${PN}-pni-settings-loader', '', d)}"
RDEPENDS_${PN}-pni-controller += "bash"
RDEPENDS_${PN} += "bash"

SYSTEMD_PACKAGES += "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '${PN}-pni-controller ${PN}-pni-settings-loader', '', d)}"

SYSTEMD_SERVICE_${PN} += "netsrvmgr.service"
FILES_${PN} += "${bindir}/*"
FILES_${PN} += "${sysconfdir}/netsrvmgr.conf"
FILES_${PN} += "${sysconfdir}/netsrvmgr_Telemetry_LoggingParams.json"
FILES_${PN} += "${systemd_unitdir}/system/netsrvmgr.service"
FILES_${PN} += "${base_libdir}/rdk/wifi_addr_reset.sh"

SYSTEMD_SERVICE_${PN}-pni-controller += "pni_controller.service"
SYSTEMD_AUTO_ENABLE_${PN}-pni-controller = "disable"
FILES_${PN}-pni-controller += "${systemd_unitdir}/system/pni_controller.service"
FILES_${PN}-pni-controller += "${base_libdir}/rdk/pni_controller.sh"

SYSTEMD_SERVICE_${PN}-pni-settings-loader = "pni_settings_loader.service"
SYSTEMD_AUTO_ENABLE_${PN}-pni-settings-loader = "enable"
FILES_${PN}-pni-settings-loader += "${systemd_unitdir}/system/pni_settings_loader.service"
