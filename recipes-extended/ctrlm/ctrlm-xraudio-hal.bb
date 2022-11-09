DESCRIPTION = "Control Manager xraudio hal component"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2a944942e1496af1886903d274dedb13"

SECTION = "base"
DEPENDS = "xraudioh ctrlm-xraudio-hal-headers"

SRC_URI  = "${CMF_GIT_ROOT}/rdk/components/generic/xraudio-hal_ctrlm;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=ctrlm-xraudio-hal"
SRCREV_ctrlm-xraudio-hal = "${AUTOREV}"

S = "${WORKDIR}/git"

INHERIT_BREAKPAD_WRAPPER := "${@bb.utils.contains('BBLAYERS', '${RDKROOT}/meta-rdk', 'breakpad-wrapper', '',d)}"
INHERIT_RDKX_LOGGER      := "${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm_voice_sdk', 'rdkx-logger', '', d)}"
INHERIT_VSDK_UTILS       := "${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm_voice_sdk', 'vsdk-utils', '', d)}"

inherit coverity autotools ${INHERIT_VSDK_UTILS} ${INHERIT_BREAKPAD_WRAPPER} ${INHERIT_RDKX_LOGGER}

XLOG_MODULE_NAME="XRAUDIO"

CFLAGS_append = " -fPIC -D_REENTRANT -D_POSIX_C_SOURCE=200809L -Wall -Werror -rdynamic"

XRAUDIO_HAL_CONFIG_MIC     = "${PKG_CONFIG_SYSROOT_DIR}/usr/include/xraudio_hal_config_mic.json"
XRAUDIO_HAL_CONFIG_OEM_ADD = "${S}/../xraudio_hal_config_oem.add.json"
XRAUDIO_HAL_CONFIG_OEM_SUB = "${S}/../xraudio_hal_config_oem.sub.json"

EXTRA_OECONF_append = " CTRLM_XRAUDIO_HAL_CONFIG_MIC=${XRAUDIO_HAL_CONFIG_MIC}"
EXTRA_OECONF_append = " CTRLM_XRAUDIO_HAL_CONFIG_JSON_SUB=${XRAUDIO_HAL_CONFIG_OEM_SUB}"
EXTRA_OECONF_append = " CTRLM_XRAUDIO_HAL_CONFIG_JSON_ADD=${XRAUDIO_HAL_CONFIG_OEM_ADD}"

EXTRA_OECONF_append = " GIT_BRANCH=${CMF_GIT_BRANCH}"

addtask clean_oem_config before do_configure

do_clean_oem_config() { 
    rm -f ${XRAUDIO_HAL_CONFIG_OEM_ADD} ${XRAUDIO_HAL_CONFIG_OEM_SUB}
}
