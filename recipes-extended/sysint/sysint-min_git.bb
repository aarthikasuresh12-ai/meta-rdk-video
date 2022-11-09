SUMMARY = "Sysint application"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=f36198fb804ffbe39b5b2c336ceef9f8"

PV = "${RDK_RELEASE}"

SYSINT_DEVICE ??= "intel-x86-pc/rdk-ri"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/sysint/generic;module=.;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=sysintgeneric"
SRC_URI += "${RDK_GENERIC_ROOT_GIT}/sysint/devices/${SYSINT_DEVICE};module=.;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};destsuffix=git/device;name=sysintdevice"
SRCREV_sysintgeneric = "${AUTOREV}"
SRCREV_sysintdevice = "${AUTOREV}"
SRCREV_FORMAT = "sysintgeneric_sysintdevice"

S = "${WORKDIR}/git/device"

inherit systemd


do_compile[noexec] = "1"

PLATFORM_BENCH_MARK = "${@bb.utils.contains('DISTRO_FEATURES', 'platform-benchmark', 'true', 'false', d)}"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'platform-benchmark', '', 'crashupload', d)}"

RDEPENDS_${PN} += "bash"
RDEPENDS_${PN} += "busybox"

RF3CE_CTRLM_ENABLED = "${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm', 'true', 'false', d)}"
STG_TYPE = "${@bb.utils.contains('DISTRO_FEATURES', 'storage_sdc','SDCARD', 'OTHERS',d)}"
MMC_TYPE = "${@bb.utils.contains('DISTRO_FEATURES', 'storage_emmc','EMMC', '',d)}"
BIND_ENABLED = "${@bb.utils.contains('DISTRO_FEATURES', 'bind', 'true', 'false', d)}"
FORCE_MTLS = "${@bb.utils.contains('DISTRO_FEATURES', 'mtls_only', 'true', 'false', d)}"
ENABLE_MAINTENANCE="${@bb.utils.contains('DISTRO_FEATURES', 'enable_maintenance_manager', 'true', 'false', d)}"
WIFI_ENABLED="${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'true', 'false', d)}"
ENABLE_SOFTWARE_OPTOUT="${@bb.utils.contains('DISTRO_FEATURES', 'enable_software_optout', 'true', 'false', d)}"
DUNFELL_BUILD = "${@bb.utils.contains('DISTRO_FEATURES', 'dunfell', 'true', 'false', d)}"

do_install() {

	install -d ${D}${base_libdir}/rdk
	install -d ${D}${sysconfdir}

	install -m 0644 ${S}/../etc/*.json ${D}${sysconfdir}
	install -m 0644 ${S}/../etc/*.properties ${D}${sysconfdir}
	install -m 0644 ${S}/../etc/*.conf ${D}${sysconfdir}
	install -m 0755 ${S}/../lib/rdk/zcip.script ${D}${sysconfdir}

	install -d ${D}/var/spool/cron


	if [ -d ${S}/etc ]; then
		if [ -f ${S}/etc/env_setup.sh ]; then
			install -m 0755 ${S}/etc/env_setup.sh ${D}${sysconfdir}
		fi
		if [ -f ${S}/etc/device.properties.wifi ]; then
			install -m 0755 ${S}/etc/device.properties.wifi ${D}${sysconfdir}
		fi
                if [ -f ${S}/etc/warehouseHosts.conf ]; then
                        install -m 0644 ${S}/etc/warehouseHosts.conf ${D}${sysconfdir}
                fi
		install -m 0755 ${S}/etc/*.properties ${D}${sysconfdir}
	fi


        if [ "${BIND_ENABLED}" = "true" ]; then
           echo "BIND_ENABLED=true" >> ${D}${sysconfdir}/device.properties
        fi

        if [ "${MMC_TYPE}" = "EMMC" ]; then
           echo "SD_CARD_TYPE=EMMC" >> ${D}${sysconfdir}/device.properties
        fi

        if [ "${FORCE_MTLS}" = "true" ]; then
           echo "FORCE_MTLS=true" >> ${D}${sysconfdir}/device.properties
        fi

	if [ -d ${S}/lib/rdk/install ]; then
		rm -rf ${S}/lib/rdk/install
	fi

       
        if [ -f ${S}/lib/rdk/tch_difd.sh ]; then 
            install -m 0755 ${S}/lib/rdk/tch_difd.sh ${D}${base_libdir}/rdk
        fi
        install -m 0755 ${S}/lib/rdk/utils.sh ${D}${base_libdir}/rdk

	#
	# The below scripts are installed by xre for emulator so need to
	# delete from sysint generic repo. For now, we will prevent these
	# to be installed by sysint.
	#
	if [ "${MACHINE}" = "qemux86hyb" -o "${MACHINE}" = "qemux86mc" ]; then
		rm ${D}${base_libdir}/rdk/isMocaNetworkUp.sh
		rm ${D}${base_libdir}/rdk/getDeviceDetails.sh
		rm ${D}${base_libdir}/rdk/getNumberOfStartupSteps.sh
		rm ${D}${base_libdir}/rdk/getProgress.sh
        fi


}

do_install_append() {
    if [ "${PLATFORM_BENCH_MARK}" == "true" ]; then
       rm -rf ${D}${base_libdir}/rdk/*
       echo "platform-benchmark" > ${D}${base_libdir}/rdk/benchmark_image.sh
    fi
}


FILES_${PN} += "${base_libdir}/rdk/*"
FILES_${PN} += "${sysconfdir}/*"

