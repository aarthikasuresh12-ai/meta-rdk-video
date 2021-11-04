SUMMARY = "TR69 Host Interface"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=99e7c83e5e6f31c2cbb811e186972945"

PV = "${RDK_RELEASE}"

SRCREV_tr69hostif = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/tr69hostif;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=tr69hostif"
SRCREV_FORMAT = "tr69hostif"

S = "${WORKDIR}/git"

DEPENDS = "iarmbus iarmmgrs e2fsprogs iksemel libsoup-2.4 libsyswrapper yajl \
           devicesettings procps glib-2.0 \
           storagemanager cjson libtinyxml2\
	  "
DEPENDS_append = " rdk-logger libparodus parodus "
DEPENDS_append_client = " tr69agent-headers netsrvmgr"

DEPENDS += "safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

inherit pkgconfig breakpad-logmapper

CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CXXFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"


DEPENDS += " rbus "

LDFLAGS_append += " -lrbus "
CXXFLAGS_append += " -I${includedir}/rbus "

RDEPENDS_${PN} += "devicesettings bash"
RDEPENDS_${PN} += "${PN}-conf"

RDEPENDS_${PN}_append_client += " netsrvmgr"
EXTRA_OECONF += "--disable-silent-rules --enable-InterfaceStack --enable-IPv6 --enable-notification --enable-yocto --enable-SpeedTest"
EXTRA_OECONF_append_hybrid = " --enable-snmpAdapter"

#Enable sd_notify
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--enable-systemd-notify', '', d)}"
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'WIFI_CLIENT_ROAMING', ' --enable-client-roaming', '', d)}"
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'NEW_HTTP_SERVER_DISABLE', '--disable-new-http-server', '', d)}"

PACKAGECONFIG ??= ""
PACKAGECONFIG_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'wifi','wifi', '',d)}"
PACKAGECONFIG[wifi] = "--enable-wifi,,,"
PACKAGECONFIG[xre] = "--enable-xre,,"
PACKAGECONFIG[moca] = "--enable-moca,,virtual/mocadriver"
PACKAGECONFIG[moca2] = "--enable-moca2,,virtual/mocadriver"
PACKAGECONFIG[rf4ce] = "--enable-rf4ce,,"
PACKAGECONFIG_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth','bluetooth', '',d)}"
PACKAGECONFIG[bluetooth] = "--enable-bt,,bluetooth-mgr,bluetooth-mgr"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth',' bluetooth-mgr', '',d)}"
RDEPENDS_${PN}_append  = " ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth',' bluetooth-mgr', '',d)}"

# Check the device Type n Enable the sd card support
PACKAGECONFIG[sdcard] = "--enable-sdcard,--disable-sdcard"
PACKAGECONFIG_append_client = " sdcard"
PACKAGECONFIG_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'storage_sdc','sdcard', '',d)}"

PACKAGECONFIG[emmc] = "--enable-emmc,--disable-emmc"

INCLUDE_DIRS += "\
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/ds \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/ds-hal \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/ds-rpc \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/iarmbus \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/iarmmgrs/tr69Bus \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/iarmmgrs/mfr \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/iarmmgrs/power \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/iarmmgrs-hal \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include \
	-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/rdk/tr69agent \
	"

CPPFLAGS += "${INCLUDE_DIRS}"
CPPFLAGS_append_client = " -DMEDIA_CLIENT "
# C++11 is required
CXXFLAGS += "-std=c++11"
CXXFLAGS += " -DYAJL_V2"

EXTRA_OECONF_append = " --enable-morty"

inherit autotools systemd pkgconfig

do_install_append() {
	install -d ${D}${includedir}/rdk/tr69hostif ${D}${systemd_unitdir}/system
	install -d ${D}${sysconfdir}
	if ${@bb.utils.contains('DISTRO_FEATURES', 'NEW_HTTP_SERVER_DISABLE', 'true', 'false', d)}; then
		install -m 0644 ${S}/tr69hostif_no_new_http_server.service ${D}${systemd_unitdir}/system/tr69hostif.service
	else
		install -m 0644 ${S}/tr69hostif.service ${D}${systemd_unitdir}/system
	fi
        install -m 0644 ${S}/partners_defaults.json ${D}${sysconfdir}
	install -m 0644 ${S}/ip-iface-monitor.service ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/src/hostif/include/*.h ${D}${includedir}/rdk/tr69hostif
	install -m 0644 ${S}/conf/mgrlist.conf ${D}${sysconfdir}
        install -d ${D}${base_libdir}/rdk
        install -m 0644 ${S}/src/hostif/parodusClient/parodus.service ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/src/hostif/parodusClient/parodus.path ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/src/hostif/parodusClient/parodus_v4.path ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/src/hostif/parodusClient/parodus_v6.path ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/src/hostif/parodusClient/conf/notify_webpa_cfg.json ${D}${sysconfdir}
        install -m 0644 ${S}/src/hostif/parodusClient/conf/webpa_cfg.json ${D}${sysconfdir}
        install -m 0755 ${S}/src/hostif/parodusClient/startParodus.sh ${D}${base_libdir}/rdk
        install -m 0755 ${S}/scripts/speedtest.sh ${D}/usr/bin
        install -d ${D}${sysconfdir} ${D}${sysconfdir}/rfcdefaults
        install -m 0644 ${S}/conf/rfcdefaults/tr69hostif.ini ${D}${sysconfdir}/rfcdefaults

        # Below header files are installed by another recipe tr69hostif-headers
        rm -r ${D}${includedir}/hostIf_msgHandler.h
        rm -r ${D}${includedir}/hostIf_NotificationHandler.h
}

do_install_append_hybrid() {
        install -m 0644 ${S}/conf/tr181_snmpOID.conf ${D}${sysconfdir}
}

addtask do_validate_data_model after do_install before do_package do_packagedata do_populate_sysroot

do_validate_data_model() {
        # if /etc/data-model.xml exists, verify it is well-formed and valid as per CWMP XSD
        if [ -e "${D}${sysconfdir}/data-model.xml" ]; then
                if ${@bb.utils.contains('DISTRO_FEATURES', 'dunfell', 'true', 'false', d)}; then
                        ${STAGING_BINDIR_NATIVE}/python3-native/python3 ${S}/scripts/validateDataModel.py ${D}${sysconfdir}/data-model.xml ${S}/conf/cwmp-datamodel-1-2.xsd
                else
                        ${STAGING_BINDIR_NATIVE}/python-native/python ${S}/scripts/validateDataModel.py ${D}${sysconfdir}/data-model.xml ${S}/conf/cwmp-datamodel-1-2.xsd
		fi
        fi
}

SYSTEMD_SERVICE_${PN} = "tr69hostif.service"
FILES_${PN} += "${systemd_unitdir}/system/tr69hostif.service"

SYSTEMD_SERVICE_${PN} += "ip-iface-monitor.service"
FILES_${PN} += "${systemd_unitdir}/system/ip-iface-monitor.service"
SYSTEMD_SERVICE_${PN} += "parodus.service" 
SYSTEMD_SERVICE_${PN} += "parodus.path" 
SYSTEMD_SERVICE_${PN} += "parodus_v4.path"
SYSTEMD_SERVICE_${PN} += "parodus_v6.path"
FILES_${PN} += "${systemd_unitdir}/system/parodus.service" 
FILES_${PN} += "${systemd_unitdir}/system/parodus.path" 
FILES_${PN} += "${systemd_unitdir}/system/parodus_v4.path"
FILES_${PN} += "${systemd_unitdir}/system/parodus_v6.path"
FILES_${PN} += "${base_libdir}/*"
FILES_${PN} += "${sysconfdir}/*"

PACKAGE_BEFORE_PN += "${PN}-conf"

FILES_${PN}-conf = "${sysconfdir}/rfcdefaults/tr69hostif.ini"
# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "tr69hostif"
BREAKPAD_LOGMAPPER_LOGLIST = "tr69hostif.log"
