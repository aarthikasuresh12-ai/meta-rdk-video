SUMMARY = "RDK App Manager Component"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3cc4d276e918f48b04eb2faf952d0537"
#Need to make it runtime dependency
RDEPENDS_${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'spark', 'pxcore-standalone', '', d)}"
#Noting to add to SDK
RDEPENDS_${PN}-dev = ""

def is_spark_disabled(d):
    spark_disabled_b = bb.utils.contains('DISTRO_FEATURES', 'spark', '0', '1', d)
    return spark_disabled_b

PV = "${RDK_RELEASE}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/appmanager;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=generic"
SRCREV_generic = "${AUTOREV}"
SRCREV_FORMAT = "generic"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"
# this is a just a service that utitizes pxscene, skipping build
do_compile[noexec] = "1"

RDEPENDS_${PN}-dev = ""

inherit systemd coverity syslog-ng-config-gen
SYSLOG-NG_FILTER = "appmanager"
SYSLOG-NG_SERVICE_appmanager = "appmanager.service"
SYSLOG-NG_DESTINATION_appmanager = "appmanager.log"
SYSLOG-NG_LOGRATE_appmanager = "low"

do_install(){
	
	install -d ${D}/home/root
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/resources/systemd/appmanager.service ${D}${systemd_unitdir}/system/appmanager.service
	install -m 0644 ${S}/config_files/waylandregistry.conf ${D}/home/root/waylandregistry.conf

        if [ "${@is_spark_disabled(d)}" -eq '0' ]
        then
          install -m 0644 ${S}/config_files/sparkpermissions.conf ${D}/home/root/sparkpermissions.conf
	  install -m 0644 ${S}/js_files/startup_partnerapp.js ${D}/home/root/startup_partnerapp.js
	  mkdir -p ${D}/home/root/optimus/ 
	  install -m 0644 ${S}/js_files/appCommander.js ${D}/home/root/optimus/appCommander.js
        fi

	install -d ${D}/lib/rdk
	install -m 0755 ${S}/scripts/runAppManager.sh ${D}/lib/rdk/
}

FILES_${PN} = "${systemd_unitdir}/system/appmanager.service"
FILES_${PN} += "/home/root"
FILES_${PN} += "${base_libdir}/rdk/*"

SYSTEMD_SERVICE_${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell', '', 'appmanager.service', d)}"
